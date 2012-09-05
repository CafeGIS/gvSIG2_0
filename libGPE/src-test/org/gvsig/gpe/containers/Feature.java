package org.gvsig.gpe.containers;

import java.util.ArrayList;

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
 * $Id: Feature.java 120 2007-05-15 12:10:01Z jorpiell $
 * $Log$
 * Revision 1.4  2007/05/15 12:09:41  jorpiell
 * The bbox is linked to the feature
 *
 * Revision 1.3  2007/05/02 11:46:07  jorpiell
 * Writing tests updated
 *
 * Revision 1.2  2007/04/19 07:23:20  jorpiell
 * Add the add methods to teh contenhandler and change the register mode
 *
 * Revision 1.1  2007/04/14 16:06:35  jorpiell
 * Add the container classes
 *
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class Feature {
	private String name = null;
	private String id = null;
	private Geometry geometry = null;
	private ArrayList elements = new ArrayList();
	private Bbox bbox = null;
	private ArrayList metaDataList = new ArrayList();
	/**
	 * @return the geometry
	 */
	public Geometry getGeometry() {
		return geometry;
	}
	/**
	 * @param geometry the geometry to set
	 */
	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}
	
	/**
	 * @param geometry the geometry to set
	 */
	public void setGeometry(Object geometry) {
		if (geometry instanceof Geometry){
			this.geometry = (Geometry)geometry;
		}
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
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
	 * @return the elements
	 */
	public ArrayList getElements() {
		return elements;
	}
		
	/**
	 * @return the element at position i
	 * @param i
	 * Element position
	 */
	public Element getElementAt(int i) {
		return (Element)elements.get(i);
	}
	
	/**
	 * Adds a new element
	 * @param layer
	 */
	public void addElement(Element element){
		elements.add(element);
	}
	
	/**
	 * Adds a new element
	 * @param layer
	 */
	public void addElement(Object element){
		if (element instanceof Element){
			elements.add(element);
		}
	}
	
	/**
	 * @return the bbox
	 */
	public Bbox getBbox() {
		return bbox;
	}

	/**
	 * @param bbox the bbox to set
	 */
	public void setBbox(Bbox bbox) {
		this.bbox = bbox;
	}
	
	/**
	 * @param bbox the bbox to set
	 */
	public void setBbox(Object bbox) {
		if (bbox instanceof Bbox){
			this.bbox = (Bbox)bbox;
		}
	}
	
	public void addMetadata(Object metadata) {
		if (metadata instanceof MetaData){
			metaDataList.add(metadata);
		}
	}
	
	public void addTime(Object time) {
		if (time instanceof Time){
		}
	}
}
