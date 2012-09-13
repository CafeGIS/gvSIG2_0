package org.gvsig.fmap.dal.store.gpe.model;

import java.util.LinkedHashMap;

import org.gvsig.fmap.geom.Geometry;

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
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class GPEFeature {
	private static int idFeature = 0;
	private Geometry geometry = null;
	private LinkedHashMap elements = null;
	private Long id = null;
	private String name = null;
	private String typeName = null;
	
	public GPEFeature(Long id, String name, String typeName) {
		this();
		this.id = id;		
		this.name = name;
		this.typeName = typeName;		
	}	

	public GPEFeature(){
		elements = new LinkedHashMap();
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

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
	 * @return the elements
	 */
	public LinkedHashMap getelements() {
		return elements;
	}

	/**
	 * @param elements the elements to set
	 */
	public void setElements(LinkedHashMap elements) {
		this.elements = elements;
	}
	
	/**
	 * It adds a new element
	 * @param element
	 * The element to add
	 */
	public void addElement(GPEElement element){
		elements.put(element.getName(), element);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the typeName
	 */
	public String getTypeName() {
		return typeName;
	}
	
	/**
	 * Initialize the feature id
	 */
	public static void initIdFeature(){
		idFeature = 0;
	}
}
