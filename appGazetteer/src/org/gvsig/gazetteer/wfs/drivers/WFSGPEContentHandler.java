package org.gvsig.gazetteer.wfs.drivers;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.gvsig.gazetteer.querys.Feature;
import org.gvsig.gpe.parser.GPEContentHandler;


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
 * $Id$
 * $Log$
 *
 */
/**
 * ContentHandler for the WFS
 * @author Jorge Piera Llodrá (jorge.piera@iver.es)
 */
public class WFSGPEContentHandler extends GPEContentHandler{
	private ArrayList features  = null;	
	private String searchField = null;

	public WFSGPEContentHandler(String searchField) {
		super();
		this.searchField = searchField;
		features = new ArrayList();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.GPEContentHandler#addElementToFeature(java.lang.Object, java.lang.Object)
	 */
	public void addElementToFeature(Object element, Object feature) {
		if ((element != null) && (((Element)element).getName().equals(searchField))){
			((Feature)feature).setName(String.valueOf(((Element)element).getValue()));
			((Feature)feature).setDescription(String.valueOf(((Element)element).getValue()));
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.GPEContentHandler#addGeometryToFeature(java.lang.Object, java.lang.Object)
	 */
	public void addGeometryToFeature(Object geometry, Object feature) {
		((Feature)feature).setCoordinates((Point2D)geometry);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.GPEContentHandler#endFeature(java.lang.Object)
	 */
	public void endFeature(Object feature) {
		features.add(feature);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.GPEContentHandler#startElement(java.lang.String, java.lang.Object, java.lang.String, java.lang.Object)
	 */
	public Object startElement(String name, Object value, String xsElementName,
			Object parentElement) {
		return new Element(name, value);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.GPEContentHandler#startFeature(java.lang.String, java.lang.String, java.lang.String, java.lang.Object)
	 */
	public Object startFeature(String id, String name, String xsElementName,
			Object layer) {
		return new Feature(id, name, name, null);		
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.GPEContentHandler#startPoint(java.lang.String, double, double, double, java.lang.String)
	 */
	public Object startPoint(String id, double x, double y, double z, String srs) {
		return new Point2D.Double(x, y);
	}	

	/* (non-Javadoc)
	 * @see org.gvsig.gpe.GPEContentHandler#startLineString(java.lang.String, double[], double[], double[], java.lang.String)
	 */
	public Object startLineString(String id, double[] x, double[] y,
			double[] z, String srs) {
		return getPoint(x, y);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.gpe.GPEContentHandler#startPolygon(java.lang.String, double[], double[], double[], java.lang.String)
	 */
	public Object startPolygon(String id, double[] x, double[] y, double[] z,
			String srs) {
		return getPoint(x, y);
	}
	
	private Point2D getPoint(double[] x, double y[]) {        
		double xs = 0.0;
		double ys = 0.0;
		for (int i=0 ; i<x.length ; i++){
			xs = xs + x[i];
			ys = ys + y[i];
		}
		if (x.length == 0){
			return new Point2D.Double(xs,ys);
		}else{
			return new Point2D.Double(xs/x.length,ys/x.length);
		}
	} 
	
	/**
	 * @author Jorge Piera Llodrá (jorge.piera@iver.es)
	 */
	private class Element{
		private String name = null;
		private Object value = null;
				
		public Element(String name, Object value) {
			super();
			this.name = name;
			this.value = value;
		}
		
		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}
		
		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}
		
		/**
		 * @return the value
		 */
		public Object getValue() {
			return value;
		}
		
		/**
		 * @param value the value to set
		 */
		public void setValue(Object value) {
			this.value = value;
		}		
	}

	/**
	 * @return the features
	 */
	public ArrayList getFeatures() {
		return features;
	}

}
