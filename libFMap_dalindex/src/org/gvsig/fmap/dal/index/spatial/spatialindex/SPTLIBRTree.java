package org.gvsig.fmap.dal.index.spatial.spatialindex;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.feature.exception.FeatureIndexException;
import org.gvsig.fmap.dal.feature.spi.FeatureReferenceProviderServices;
import org.gvsig.fmap.dal.feature.spi.index.AbstractFeatureIndexProvider;
import org.gvsig.fmap.dal.feature.spi.index.FeatureIndexProvider;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.primitive.Point;

import spatialindex.rtree.RTree;
import spatialindex.spatialindex.IData;
import spatialindex.spatialindex.INode;
import spatialindex.spatialindex.IVisitor;
import spatialindex.spatialindex.Region;
import spatialindex.storagemanager.DiskStorageManager;
import spatialindex.storagemanager.IBuffer;
import spatialindex.storagemanager.IStorageManager;
import spatialindex.storagemanager.PropertySet;
import spatialindex.storagemanager.RandomEvictionsBuffer;

/**
 * <p>
 * RTree spatial index based in spatial index library: <br>
 * http://u-foria.org/marioh/spatialindex/index.html <br>
 * marioh@cs.ucr.edu
 * </p>
 * It has the problem that spatial index file creation is a bit slowly (in
 * comparation with other indexes).
 */
public class SPTLIBRTree extends AbstractFeatureIndexProvider implements
		FeatureIndexProvider {

	public static final String NAME = SPTLIBRTree.class.getSimpleName();
	/**
	 * Page size of associated file
	 */
	private static final int defaultPageSize = 32 * 1024;
	private static final double defaultFillFactor = 0.85d;

	/**
	 * Size of memory buffer of the index
	 */
	private static final int BUFFER_SIZE = 25000;
	RTree rtree;
	String fileName;
	IStorageManager diskfile;

	public SPTLIBRTree() {

	}

	public void initialize() throws InitializeException {
		try {
			PropertySet ps = new PropertySet();
			ps.setProperty("Overwrite", new Boolean(false));
			// .idx and .dat extensions will be added.
			fileName = getFeatureIndexProviderServices().getNewFileName(null, null);
			ps.setProperty("FileName", fileName);
			ps.setProperty("PageSize", new Integer(defaultPageSize));
			diskfile = new DiskStorageManager(ps);
			load();
		} catch (SecurityException e) {
			throw new InitializeException(e);
		} catch (FileNotFoundException e) {
			throw new InitializeException(e);
		} catch (IOException e) {
			throw new InitializeException(e);
		}
	}

	/**
	 * If the spatial index file exists and has content
	 */
	public boolean exists() {
		return (new File(fileName + ".dat").length() != 0);
	}

	class RTreeVisitor implements IVisitor {
		ArrayList solution = new ArrayList();

		public void visitNode(INode n) {
		}

		public void visitData(IData d) {
			solution.add(new Integer(d.getIdentifier()));
		}

		public List getSolution() {
			return solution;
		}
	}

	public List containtmentQuery(Envelope env) {
		List solution = null;
		Region region = createRegion(env);
		RTreeVisitor visitor = new RTreeVisitor();
		rtree.containmentQuery(region, visitor);
		solution = visitor.getSolution();
		return solution;
	}

	/**
	 * Warn! This RTree implemention doesnt care if 'index' entry has been
	 * indexed yet
	 */
	public void insert(Envelope env, int index) {
		rtree.insertData(null, createRegion(env), index);
	}

	private Region createRegion(Envelope env) {
		double[] x = new double[2];
		double[] y = new double[2];
		x[0] = env.getLowerCorner().getX();
		y[0] = env.getLowerCorner().getY();
		x[1] = env.getUpperCorner().getX();
		y[1] = env.getUpperCorner().getY();
		return new Region(x, y);
	}

	/**
	 * Looks for N indexes nearest to the specified rect.
	 *
	 * @param numberOfNearest
	 * @param rect
	 * @return
	 */
	public List findNNearest(int numberOfNearest, Envelope env) {
		List solution = null;
		Region region = createRegion(env);
		RTreeVisitor visitor = new RTreeVisitor();
		rtree.nearestNeighborQuery(numberOfNearest, region, visitor);
		solution = visitor.getSolution();
		return solution;
	}

	/**
	 * Looks for the N indexes nearest to the specified point
	 *
	 * @param numberOfNearest
	 * @param point
	 * @return
	 */
	public List findNNearest(int numberOfNearest, Point point) {
		List solution = null;
		spatialindex.spatialindex.Point sptPoint = new spatialindex.spatialindex.Point(
				new double[] { point.getX(), point.getY() });
		RTreeVisitor visitor = new RTreeVisitor();
		rtree.nearestNeighborQuery(numberOfNearest, sptPoint, visitor);
		solution = visitor.getSolution();
		return solution;
	}

	public void flush() {
		rtree.flush();
	}

	public void load() {
		// applies a main memory random buffer on top of the persistent
		// storage manager
		IBuffer buffer = new RandomEvictionsBuffer(diskfile, BUFFER_SIZE, false);

		// Create a new, empty, RTree with dimensionality 2, minimum load
		// 70%, using "file" as
		// the StorageManager and the RSTAR splitting policy.
		PropertySet ps2 = new PropertySet();

		Double f = new Double(defaultFillFactor);
		ps2.setProperty("FillFactor", f);

		Integer i = new Integer(2);
		ps2.setProperty("Dimension", i);

		File file = new File(fileName + ".dat");
		if (file.length() != 0) {
			ps2.setProperty("IndexIdentifier", new Integer(1));
		}
		rtree = new RTree(ps2, buffer);
	}

	public void load(File f) throws FeatureIndexException {
		load();
	}

	public void flush(File f) throws FeatureIndexException {
		flush();
	}

	public void close() {
	}

	public File getFile() {
		return new File(fileName + ".dat");
	}

	public void delete(Object value, FeatureReferenceProviderServices fref) {
		rtree.deleteData(createRegion(((Geometry) value).getEnvelope()), ((Integer) fref
				.getOID()).intValue());
	}

	public void insert(Object value, FeatureReferenceProviderServices fref) {
		Envelope env = null;
		if (value instanceof Envelope) {
			env = (Envelope) value;
		} else if (value instanceof Geometry) {
			env = ((Geometry) value).getEnvelope();
		}
		rtree.insertData(null, createRegion(env), ((Integer) fref
				.getOID()).intValue());
	}

	public List match(Object value) throws FeatureIndexException {
		List solution = null;
		Envelope env = null;
		if (value instanceof Envelope) {
			env = (Envelope) value;
		} else if (value instanceof Geometry) {
			env = ((Geometry) value).getEnvelope();
		}
		Region region = createRegion(env);
		RTreeVisitor visitor = new RTreeVisitor();
		rtree.intersectionQuery(region, visitor);
		solution = visitor.getSolution();
		return new LongList(solution);
	}

	public List match(Object min, Object max) {
		throw new UnsupportedOperationException();
	}

	public List nearest(int count, Object value) throws FeatureIndexException {
		if (value instanceof Envelope) {
			return this.findNNearest(count, (Envelope) value);
		} else if (value instanceof Point) {
			return this.findNNearest(count, (Point) value);
		} else if (value instanceof Geometry) {
			return this.findNNearest(count, ((Geometry) value).getEnvelope());
		} else {
			throw new IllegalArgumentException ("value must be either an Envelope or either a Point2D");
		}
	}

	public boolean isMatchSupported() {
		return true;
	}

	public boolean isNearestSupported() {
		return true;
	}

	public boolean isNearestToleranceSupported() {
		return false;
	}

	public boolean isRangeSupported() {
		// TODO Auto-generated method stub
		return false;
	}

	public List nearest(int count, Object value, Object tolerance)
			throws FeatureIndexException {
		throw new UnsupportedOperationException();
	}

	public List range(Object value1, Object value2)
			throws FeatureIndexException {
		throw new UnsupportedOperationException();
	}
}
