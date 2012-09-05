/*
 * Created on 16-may-2006
 *
 * gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ib��ez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
/* CVS MESSAGES:
 *
 * $Id: QuadtreeGt2.java 10627 2007-03-06 17:10:21Z caballero $
 * $Log$
 * Revision 1.3  2007-03-06 17:08:59  caballero
 * Exceptions
 *
 * Revision 1.2  2006/11/29 19:27:59  azabala
 * bug fixed (caused when we query for a bbox which is greater or equal to a layer bbox)
 *
 * Revision 1.1  2006/05/24 21:58:04  azabala
 * *** empty log message ***
 *
 *
 */
package org.gvsig.fmap.dal.index.spatial.gt2;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Stack;

import org.geotools.index.quadtree.Node;
import org.geotools.index.quadtree.QuadTree;
import org.geotools.index.quadtree.StoreException;
import org.geotools.index.quadtree.fs.FileSystemIndexStore;
import org.geotools.index.quadtree.fs.IndexHeader;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.exception.FeatureIndexException;
import org.gvsig.fmap.dal.feature.spi.FeatureReferenceProviderServices;
import org.gvsig.fmap.dal.feature.spi.index.AbstractFeatureIndexProvider;
import org.gvsig.fmap.dal.feature.spi.index.FeatureIndexProvider;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.tools.exception.BaseException;

import com.vividsolutions.jts.geom.Envelope;

/**
 * This Quadtree spatial index implementation is based in a fork of
 * org.geotools.index.quadtree.Quadtree implementation. <br>
 * This implementation offers us:
 * <ol>
 * <li>Persistence of spatial index</li>
 * </ol>
 * We had to fork geotools quadtree for many reasons:
 * <ol>
 * <li>It was strongly dependent of SHP format, so it returned not only a num
 * of rectangle, it also returned byte offset of this rectangle in shp file</li>
 * <li> Query artifact wasnt run well at all </li>
 * </ol>
 *
 * @author azabala
 *
 */
public class GT2Quadtree extends AbstractFeatureIndexProvider implements FeatureIndexProvider {

	public static final String NAME = GT2Quadtree.class.getSimpleName();
	/**
	 * Geotools quadtree implementation
	 */
	QuadTree quadtree;
	/**
	 * Persistent storage
	 */
	String fileName;
	/**
	 * Spatial index file extension
	 */
	final String qExt = ".qix";
	/**
	 * qix format has many versions, and allows different byte orders.
	 */
	String byteOrder;
	/**
	 * Bounds of the layer to index
	 */
	//Envelope bounds;
	/**
	 * Number of records of the layer to index
	 */
	//int numRecs = 0;

	boolean inMemory = false;

	public GT2Quadtree() {

	}

	public void initialize() throws InitializeException {
		try {
			File file = File.createTempFile(getFeatureStore().getName(), ".qix");
			this.fileName = file.getAbsolutePath();
			org.gvsig.fmap.geom.primitive.Envelope env = getFeatureStore()
					.getEnvelope();
			int featureCount = (int) getFeatureStore().getFeatureSet().getSize();
			this.byteOrder = "NM";
			quadtree = new QuadTree(featureCount, toJtsEnvelope(env));
			if (exists()) {
				load();
			}
		} catch (IOException e) {
			throw new InitializeException(e);
		} catch (BaseException e) {
			throw new InitializeException(e);
		}
	}

	/**
	 * If the spatial index file exists and has content
	 */
	public boolean exists() {
		return (new File(fileName).length() != 0);
	}

	public void load() throws FeatureIndexException {
		if (quadtree == null) {
			load(new File(fileName));
		}
	}

	public void load(File f) throws FeatureIndexException {
		try {
			FileSystemIndexStore store = new FileSystemIndexStore(f);
			quadtree = store.load();
			this.fileName = f.getAbsolutePath();
		} catch (StoreException e) {
			throw new FeatureIndexException(e);
		}
	}

	/**
	 * Inserts an object in the index
	 */
	public void insert(org.gvsig.fmap.geom.primitive.Envelope env, int index) {
		if (env == null) {
			throw new IllegalArgumentException("Envelope cannot be null");
		}
		Envelope e = toJtsEnvelope(env);
		if (e == null) {
			throw new IllegalStateException(
					"JTS Envelope conversion returns null");
		}
		System.out.println("recno=" + index);
		if (quadtree == null) {
			throw new IllegalStateException("quadtree is null");
		}
		try {
			quadtree.insert(index, toJtsEnvelope(env));
		} catch (StoreException se) {
			// TODO Auto-generated catch block
			se.printStackTrace();
		}
	}

	public void delete(org.gvsig.fmap.geom.primitive.Envelope env, int index) {
		if (inMemory) {
			quadtree.delete(toJtsEnvelope(env), index);
		}
	}

	public Envelope toJtsEnvelope(org.gvsig.fmap.geom.primitive.Envelope env) {
		if (env == null) {
			return null;
		}
		Point min = env.getLowerCorner();
		Point max = env.getUpperCorner();

		return new Envelope(min.getX(), max.getX(), min.getY(), max.getY());
	}

	/**
	 *
	 * @throws StoreException
	 * @deprecated
	 */
	void openQuadTree() throws StoreException {
		if (quadtree == null) {
			File file = new File(this.fileName);
			// Intento de cargar todo el quadtree en memoria
			FileSystemIndexStore store = new FileSystemIndexStore(file);
			quadtree = store.load();
		}
	}

	void openQuadTreeInMemory() throws StoreException {
		if (quadtree == null) {
			File file = new File(fileName);
			// Intento de cargar todo el quadtree en memoria
			FileSystemIndexStore store = new FileSystemIndexStore(file);
			QuadTree filequadtree = store.load();
			quadtree = new QuadTree(filequadtree.getNumShapes(), filequadtree
					.getMaxDepth(), filequadtree.getRoot().getBounds());
			Stack nodes = new Stack();
			nodes.push(filequadtree.getRoot());
			while (nodes.size() != 0) {
				Node node = (Node) nodes.pop();
				Envelope nodeEnv = node.getBounds();
				int[] shapeIds = node.getShapesId();
				for (int i = 0; i < shapeIds.length; i++) {
					quadtree.insert(shapeIds[i], nodeEnv);
				}
				int numSubnodes = node.getNumSubNodes();
				for (int i = 0; i < numSubnodes; i++) {
					nodes.push(node.getSubNode(i));
				}
			}// while
			filequadtree.close();
		}
	}

	public void flush() throws FeatureIndexException {
		flush(new File(fileName));
	}

	public void flush(File f) throws FeatureIndexException {
		byte order = 0;
		if ((byteOrder == null) || byteOrder.equalsIgnoreCase("NM")) {
			order = IndexHeader.NEW_MSB_ORDER;
		} else if (byteOrder.equalsIgnoreCase("NL")) {
			order = IndexHeader.NEW_LSB_ORDER;
		}
		FileSystemIndexStore store = new FileSystemIndexStore(f, order);
		try {
			store.store(quadtree);
			this.fileName = f.getAbsolutePath();
		} catch (StoreException e) {
			throw new FeatureIndexException(e);
		}
	}

	public File getFile() {
		return new File(this.fileName);
	}

	private FeatureStore getFeatureStore() {
		return getFeatureIndexProviderServices()
				.getFeatureStoreProviderServices().getFeatureStore();
	}

	public void delete(Object value, FeatureReferenceProviderServices fref) {

		if (!isCompatibleOID(fref.getOID())) {
			throw new IllegalArgumentException("OID not compatible. Must be an instance of Number within the Integer range.");
		}

		delete(((org.gvsig.fmap.geom.Geometry) value).getEnvelope(), ((Number) fref.getOID()).intValue());
	}

	public void insert(Object value, FeatureReferenceProviderServices fref) {

		if (!isCompatibleOID(fref.getOID())) {
			throw new IllegalArgumentException("OID not compatible. Must be an instance of Number within the Integer range.");
		}

		insert(((org.gvsig.fmap.geom.Geometry) value).getEnvelope(), ((Number) fref.getOID()).intValue());
	}

	public List match(Object value) throws FeatureIndexException {
		if (quadtree == null) {
			throw new IllegalStateException("This quadtree is null.");
		}
		if (value == null) {
			throw new IllegalArgumentException("Envelope cannot be null.");
		}
		if (!(value instanceof org.gvsig.fmap.geom.primitive.Envelope)) {
			throw new IllegalArgumentException("Not an envelope.");
		}
		org.gvsig.fmap.geom.primitive.Envelope env = null;
		if (value instanceof org.gvsig.fmap.geom.primitive.Envelope) {
			env = (org.gvsig.fmap.geom.primitive.Envelope) value;
		} else if (value instanceof Geometry) {
			env = ((Geometry) value).getEnvelope();
		}
		return new LongList(quadtree.query(toJtsEnvelope(env)));
	}

	public List nearest(int count, Object value) throws FeatureIndexException {
		throw new UnsupportedOperationException();
	}

	public boolean isMatchSupported() {
		return true;
	}

	public boolean isNearestSupported() {
		return false;
	}

	public boolean isNearestToleranceSupported() {
		return false;
	}

	public boolean isRangeSupported() {
		return false;
	}

	public List nearest(int count, Object value, Object tolerance)
			throws FeatureIndexException {
		throw new UnsupportedOperationException();
	}

	public List range(Object value1, Object value2) throws FeatureIndexException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Indicates whether the given OID's type is compatible
	 * with this provider
	 *
	 * @param oid
	 *
	 * @return
	 * 		true if this index provider supports the given oid type
	 */
	private boolean isCompatibleOID(Object oid) {
		if (!(oid instanceof Number)) {
			return false;
		}

		long num = ((Number) oid).longValue();

		if (num > Integer.MAX_VALUE || num < Integer.MIN_VALUE) {
			return false;
		}

		return true;
	}

}
