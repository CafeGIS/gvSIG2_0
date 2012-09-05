/*
 * Created on 28-abr-2006
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
* $Id: QuadtreeJts.java 11288 2007-04-19 17:32:50Z azabala $
* $Log$
* Revision 1.2  2007-04-19 17:32:50  azabala
* new constructor (fmap spatial index from an existing jts spatial index)
*
* Revision 1.1  2006/05/01 18:38:41  azabala
* primera version en cvs del api de indices espaciales
*
*
*/
package org.gvsig.fmap.dal.index.spatial.jts;

import java.util.List;

import org.gvsig.fmap.dal.feature.exception.FeatureIndexException;
import org.gvsig.fmap.dal.feature.spi.FeatureReferenceProviderServices;
import org.gvsig.fmap.dal.feature.spi.index.AbstractFeatureIndexProvider;
import org.gvsig.fmap.dal.feature.spi.index.FeatureIndexProvider;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.primitive.NullGeometry;
import org.gvsig.fmap.geom.primitive.Point;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.quadtree.Quadtree;
/**
 * Adapter for ISPatialIndex gvSIG's interface to
 * JTS Quadtree.
 *
 *
 * @author azabala
 *
 */
public class JTSQuadtree extends AbstractFeatureIndexProvider implements FeatureIndexProvider {

	public static final String NAME = JTSQuadtree.class.getSimpleName();

	private Quadtree quadtree;

	public JTSQuadtree() {
	}

	public void initialize() {
		quadtree = new Quadtree();
	}

	private Envelope fromEnvelope(org.gvsig.fmap.geom.primitive.Envelope env){
		Point min = env.getLowerCorner();
		Point max = env.getUpperCorner();
		Envelope env2 = new Envelope(min.getX(), max.getX(), min.getY(), max.getY());
		return env2;
	}

	public void delete(Object o, FeatureReferenceProviderServices fref) {
		Integer integer=new Integer(((Long)(fref).getOID()).intValue());
		quadtree.remove(
				fromEnvelope(((Geometry) o).getEnvelope()), integer);
	}

	public void insert(Object o, FeatureReferenceProviderServices fref) {
		if (o == null || o instanceof NullGeometry) {
			return;
		}
		Integer integer=new Integer(((Long)(fref).getOID()).intValue());
		quadtree.insert(
				fromEnvelope(((Geometry) o).getEnvelope()), integer);

	}

	public List match(Object value) throws FeatureIndexException {
		org.gvsig.fmap.geom.primitive.Envelope env = null;
		if (value instanceof org.gvsig.fmap.geom.primitive.Envelope) {
			env = (org.gvsig.fmap.geom.primitive.Envelope) value;
		} else if (value instanceof Geometry) {
			env = ((Geometry) value).getEnvelope();
		}
		return new LongList(quadtree.query(fromEnvelope(env)));
	}

	public List match(Object min, Object max) {
		throw new UnsupportedOperationException("Can't perform this kind of search.");
	}

	public List nearest(int count, Object value) throws FeatureIndexException {
		throw new UnsupportedOperationException("Can't perform this kind of search.");
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

	public List range(Object value1, Object value2)
			throws FeatureIndexException {
		throw new UnsupportedOperationException();
	}
}

