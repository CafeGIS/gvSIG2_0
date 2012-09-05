/*
 * Created on 15-may-2006
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
* $Id: RTreeJsi.java 13884 2007-09-19 16:26:04Z jaume $
* $Log$
* Revision 1.5  2007-09-19 16:25:39  jaume
* ReadExpansionFileException removed from this context and removed unnecessary imports
*
* Revision 1.4  2007/06/27 20:17:30  azabala
* new spatial index (rix)
*
* Revision 1.3  2007/03/06 17:08:59  caballero
* Exceptions
*
* Revision 1.2  2006/06/05 16:59:08  azabala
* implementada busqueda de vecino mas proximo a partir de rectangulos
*
* Revision 1.1  2006/05/24 21:58:04  azabala
* *** empty log message ***
*
*
*/
package org.gvsig.fmap.dal.index.spatial.jsi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.feature.exception.FeatureIndexException;
import org.gvsig.fmap.dal.feature.spi.FeatureReferenceProviderServices;
import org.gvsig.fmap.dal.feature.spi.index.AbstractFeatureIndexProvider;
import org.gvsig.fmap.dal.feature.spi.index.FeatureIndexProvider;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.primitive.Point;

import com.infomatiq.jsi.IntProcedure;
import com.infomatiq.jsi.Rectangle;
import com.infomatiq.jsi.rtree.RTree;

/**
 * RTree spatial index implementation based in library
 * JSI (java spatial index).
 *
 * http://jsi.sourceforge.net/
 *
 * This RTree has better performance that Spatial Index Library
 * RTree, and that JTS'RTree, because
 * it uses the GNU's Trove Collections API.
 *
 * We are doing some probes with it, because it offers
 * a Nearest Neighbour algorithm implementation
 * (useful for Spatial Join geoprocess, for example).
 *
 * It isnt persistent, and We've found some problems
 * with delete operations.
 *
 *
 *
 *
 * @author azabala
 * @author jyarza
 *
 */
public class JSIRTree extends AbstractFeatureIndexProvider implements FeatureIndexProvider {

	public static final String NAME = JSIRTree.class.getSimpleName();

	protected RTree rtree;

	public JSIRTree() {
		rtree = new RTree();
	}


	public void initialize() throws InitializeException {
		Properties props = new Properties();
//		props.setProperty("MaxNodeEntries", "500");
//		props.setProperty("MinNodeEntries", "200");
		rtree.init(props);
	}

	class ListIntProcedure implements IntProcedure{
		ArrayList solution = new ArrayList();

		public boolean execute(int arg0) {
			solution.add(new Integer(arg0));
			return true;
		}

		public List getSolution(){
			return solution;
		}
	}

	protected List findNNearest(int numberOfNearest, Point point){
		com.infomatiq.jsi.Point jsiPoint =
			new com.infomatiq.jsi.Point((float)point.getX(), (float)point.getY());
		return (List) rtree.nearest(jsiPoint, numberOfNearest);
	}

	public Iterator iterator(){
		return rtree.iterator();
	}

	public int size(){
		return rtree.size();
	}

	protected Rectangle toJsiRect(Envelope env){
		Point min = env.getLowerCorner();
		Point max = env.getUpperCorner();

		Rectangle jsiRect = new Rectangle((float)min.getX(),
				(float)min.getY(),
				(float)max.getX(),
				(float)max.getY());
		return jsiRect;
	}

	public void insert(Object value, FeatureReferenceProviderServices fref) {
		Envelope env = getEnvelope(value);

		if (env == null) {
			throw new IllegalArgumentException("value is neither Geometry or Envelope");
		}
		
		Object oid = fref.getOID();
		if (!isCompatibleOID(oid)) {
			throw new IllegalArgumentException("OID type not compatible: " + oid.getClass().getName());
		}		

		rtree.add(toJsiRect(env), ((Number) oid).intValue());
	}

	public void delete(Object value, FeatureReferenceProviderServices fref) {
		Envelope env = getEnvelope(value);

		if (env == null) {
			throw new IllegalArgumentException("value is neither Geometry or Envelope");
		}
		
		Object oid = fref.getOID();
		if (!isCompatibleOID(oid)) {
			throw new IllegalArgumentException("OID type not compatible: " + oid.getClass().getName());
		}
		
		rtree.delete(toJsiRect(env), ((Number) oid).intValue());
	}

	public List match(Object value) throws FeatureIndexException {
		Envelope env = getEnvelope(value);

		if (env == null) {
			throw new IllegalArgumentException("value is neither Geometry or Envelope");
		}
		ListIntProcedure solution = new ListIntProcedure();
		rtree.intersects(toJsiRect(env), solution);
		return new LongList(solution.getSolution());
	}

	public List nearest(int count, Object value) {
		if (value == null) {
			throw new IllegalArgumentException("value is null");
		}
		
		if (value instanceof Point) {
			Point p = (Point) value;
			com.infomatiq.jsi.Point jsiPoint =
				new com.infomatiq.jsi.Point((float) p.getDirectPosition().getOrdinate(0), (float) p.getDirectPosition().getOrdinate(1));
			return (List) rtree.nearest(jsiPoint, count);
		} else {
			Envelope env = getEnvelope(value);

			if (env == null) {
				throw new IllegalArgumentException("value is neither Geometry or Envelope");
			}
			return (List) rtree.nearest(toJsiRect(env), count);
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
	
	protected Envelope getEnvelope(Object value) {
		Envelope env = null;
		
		if (value instanceof Envelope) {
			env = (Envelope) value;
		} else if (value instanceof Geometry) {
			env = ((Geometry) value).getEnvelope();
		}
		return env;
	}	

}

