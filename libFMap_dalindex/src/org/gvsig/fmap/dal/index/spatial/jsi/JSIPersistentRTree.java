/*
 * Created on 13-jun-2007
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
* $Id: PersistentRTreeJsi.java 12380 2007-06-27 20:17:30Z azabala $
* $Log$
* Revision 1.1  2007-06-27 20:17:30  azabala
* new spatial index (rix)
*
*
*/
package org.gvsig.fmap.dal.index.spatial.jsi;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;

import javax.imageio.stream.FileImageOutputStream;

import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.feature.exception.FeatureIndexException;
import org.gvsig.fmap.dal.feature.spi.FeatureReferenceProviderServices;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.primitive.Envelope;

import com.infomatiq.jsi.Rectangle;
import com.infomatiq.jsi.rtree.RTree;

/**
 * Persistent spatial index which can resolve nearest neighbour queries.
 * <br>
 *
 * To use:
 *
 * PersistentRTreeJsi sptidx = new PersistentRtreeJsi("/home/kk");
 * if(sptidx.exists())
 *  sptidx.load();
 *
 *
 *  sptidx.add(rect, int);
 *  ...
 *  sptidx.add(rect2,int2);
 *  sptidx.flush();
 *
 * @author azabala
 *
 */
public class JSIPersistentRTree extends JSIRTree {

	public static final String NAME = JSIPersistentRTree.class.getSimpleName();

	/**
	 * Spatial index file
	 */
	private File file;

	private boolean hasChanged = false;
	/**
	 * Spatial index file extension
	 */
	final String rExt = ".rix";

	private LinkedHashMap  rectangles;

	public JSIPersistentRTree() {
		super();
	}

	public void initialize() throws InitializeException {
		super.initialize();
		try {
			file = File.createTempFile("RTreeJsi" + getFeatureIndexProviderServices().getTemporaryFileName(), rExt);
			rectangles = new LinkedHashMap();
			load(file);
		} catch (IOException e) {
			throw new InitializeException(e);
		} catch (FeatureIndexException e) {
			throw new InitializeException(e);
		}
	}

	public void flush(File f) throws FeatureIndexException {
		try {
			if(! hasChanged) {
				return;
			}
			RandomAccessFile file = new RandomAccessFile(f,
															"rw");
			FileImageOutputStream output = new FileImageOutputStream(file);
			output.setByteOrder(ByteOrder.LITTLE_ENDIAN);
			int numShapes = rtree.size();
			output.writeInt(numShapes);

			Iterator iterator = rtree.iterator();
			int count = 0;
			while(iterator.hasNext()){
				Integer  idx = (Integer) iterator.next();
				Rectangle nr = (Rectangle) rectangles.get(idx);
				float xmin = nr.min[0];
				float ymin = nr.min[1];

				float xmax = nr.max[0];
				float ymax = nr.max[1];

				output.writeFloat(xmin);
				output.writeFloat(ymin);
				output.writeFloat(xmax);
				output.writeFloat(ymax);

				output.writeInt(idx.intValue());
				count++;
			}
			output.flush();
			output.close();
			file.close();
			hasChanged = false;
		} catch (FileNotFoundException e) {
			throw new FeatureIndexException(e);
		} catch (IOException e) {
			throw new FeatureIndexException(e);
		}

	}

	private void load(File f) throws FeatureIndexException {
		if (f == null) {
			throw new IllegalArgumentException("File f cannot be null");
		}

		try {
			if(! f.exists()){
				return;
			}
			RandomAccessFile file = new RandomAccessFile(f, "r");
			FileChannel channel = file.getChannel();
			MappedByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
			buf.order(ByteOrder.LITTLE_ENDIAN);
			int numShapes = buf.getInt();
			for(int i = 0; i < numShapes; i++){
				float xmin, ymin, xmax, ymax;
				int shapeIndex;
				xmin = buf.getFloat();
				ymin = buf.getFloat();
				xmax = buf.getFloat();
				ymax = buf.getFloat();
				shapeIndex = buf.getInt();

				Rectangle jsiRect = new Rectangle(xmin, ymin, xmax, ymax);
				rtree.add(jsiRect, shapeIndex);
			}
		}catch(Exception e){
			throw new FeatureIndexException(e);
		}
	}

	public void insert(Object value, FeatureReferenceProviderServices fref) {	
		super.insert(value, fref);
		Envelope env = getEnvelope(value);
		rectangles.put(fref.getOID(), toJsiRect(env));
		hasChanged = true;
	}


	public void delete(Object value, FeatureReferenceProviderServices fref) {
		super.delete(value, fref);
		rectangles.remove(fref.getOID());
		hasChanged = true;
	}

}

