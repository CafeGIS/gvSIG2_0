
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
package org.gvsig.gazetteer.querys;

/**
 * This class represents a field from a thesaurus name 
 * (name,description,...)
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class FeatureTypeAttribute {
	private String name;
	private int minOccurs;
	private boolean nillable;
	private String type;

	/**
	 * @param name 
	 * @param minOccurs 
	 * @param nillable 
	 * @param type 
	 */
	public FeatureTypeAttribute(String name, int minOccurs, boolean nillable, String type) {        
		super();
		this.name = name;
		this.minOccurs = minOccurs;
		this.nillable = nillable;
		this.type = type;
	} 

	/**
	 * @return Returns the minOccurs.
	 */
	public int getMinOccurs() {        
		return minOccurs;
	} 

	/**
	 * @param minOccurs The minOccurs to set.
	 */
	public void setMinOccurs(int minOccurs) {        
		this.minOccurs = minOccurs;
	} 

	/**
	 * @return Returns the name.
	 */
	public String getName() {        
		return name;
	} 

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {        
		this.name = name;
	} 

	/**
	 * @return Returns the nillable.
	 */
	public boolean getNillable() {        
		return nillable;
	} 

	/**
	 * @param nillable The nillable to set.
	 */
	public void setNillable(boolean nillable) {        
		this.nillable = nillable;
	} 

	/**
	 * @return Returns the type.
	 */
	public String getType() {        
		return type;
	} 

	/**
	 * @param type The type to set.
	 */
	public void setType(String type) {        
		this.type = type;
	} 
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return name;
	}
}
