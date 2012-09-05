package org.gvsig.gazetteer.utils;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Map;

import org.gvsig.gazetteer.querys.Feature;
import org.gvsig.remoteClient.wfs.schema.type.IXMLType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.gml2.GMLReader;


/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
 *   Av. Blasco Ibáñez, 50
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
 * $Id: GazetteerGeometriesFactory.java 572 2007-07-27 12:40:11 +0000 (Fri, 27 Jul 2007) jpiera $
 * $Log$
 * Revision 1.1.2.2  2007/07/13 12:00:35  jorpiell
 * Add the posibility to add a new panel
 *
 * Revision 1.1.2.1  2007/07/10 11:18:04  jorpiell
 * Added the registers
 *
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class GazetteerGeometriesFactory {
	private static final Logger logger = LoggerFactory.getLogger(GazetteerGeometriesFactory.class);
	private String attName = null;
		
	public GazetteerGeometriesFactory(String attName) {
		super();
		this.attName = attName;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.remoteClient.gml.factories.IGeometriesFactory#createGeometry(java.lang.String)
	 */
	public Object createGeometry(String gmlGeometry) {
		GMLReader reader = new GMLReader();
		try {
			Geometry geom = reader.read(gmlGeometry,new GeometryFactory());
			Point point = geom.getInteriorPoint();
			return new Point2D.Double(point.getX(),point.getY());
		}  catch (Exception e){
			logger.error("Can't parse: " + gmlGeometry,e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.remoteClient.gml.factories.IGeometriesFactory#createMultipoint2D(double[], double[])
	 */
	public Object createMultipoint2D(double[] x, double[] y) {
		double xTotal = 0;
		double yTotal = 0;
		for (int i=0 ; i<x.length ; i++){
			xTotal = xTotal + x[i];
			yTotal = yTotal + y[i];
		}
		if (x.length > 0){
			return new Point2D.Double(xTotal/x.length,
					yTotal/y.length);
		}
		return new Point2D.Double(0,
				0);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.remoteClient.gml.factories.IGeometriesFactory#createPoint2D(java.awt.geom.Point2D)
	 */
	public Object createPoint2D(Point2D point) {
		return point;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.remoteClient.gml.factories.IGeometriesFactory#createPoint2D(double, double)
	 */
	public Object createPoint2D(double x, double y) {
		return new Point2D.Double(x,y);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.remoteClient.gml.factories.IGeometriesFactory#createSimpleFeature(java.lang.String, org.gvsig.remoteClient.gml.types.IXMLType, java.util.Map, java.lang.Object)
	 */
	public Object createSimpleFeature(String element, IXMLType type,Map values,Object geometry) {
		String sGeoname = element;
		if (attName != null){
			if (values.get(attName) != null){
				ArrayList array = (ArrayList)values.get(attName);
				if (array.size() > 0){
					sGeoname = (String)array.get(0);
				}
			}
		}		
		return new Feature(null,
				sGeoname,
				sGeoname,
				(Point2D)geometry);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.remoteClient.gml.factories.IGeometriesFactory#createPolygon2D(double[], double[])
	 */
	public Object createPolygon2D(double[] x, double[] y) {
		double avX = 0;
		double avY = 0;
		for (int i=0 ; i<x.length ; i++){
			avX = avX + x[i];
			avY = avY + y[i];
		}
		if (x.length > 0){
			avX = avX/x.length;
			avY = avY/y.length;
		}
		return new Point2D.Double(avX, avY);		
	}
}
