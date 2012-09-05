
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
import org.gvsig.catalog.querys.Coordinates;

/**
 * This class represents a thesaurus name (city, cementer, ...)
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class FeatureType {
	private String name;
	private String title;
	private String abstract_;
	private FeatureTypeAttribute[] attributes = null;
	private String keywords;
	private String srs;
	private Coordinates coordinates;
	private FeatureType[] featureTypes;

	public  FeatureType() {        
		
	} 

	/**
	 * @param name 
	 */
	public  FeatureType(String name) {        
		this.name = name;		
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
	 * @return Returns the abstract_.
	 */
	public String getAbstract() {        
		return abstract_;
	} 

	/**
	 * @param abstract_ The abstract_ to set.
	 */
	public void setAbstract(String abstract_) {        
		this.abstract_ = abstract_;
	} 

	/**
	 * @return Returns the coordinates.
	 */
	public Coordinates getCoordinates() {        
		return coordinates;
	} 

	/**
	 * @param coordinates The coordinates to set.
	 */
	public void setCoordinates(Coordinates coordinates) {        
		this.coordinates = coordinates;
	} 

	/**
	 * @return Returns the keywords.
	 */
	public String getKeywords() {        
		return keywords;
	} 

	/**
	 * @param keywords The keywords to set.
	 */
	public void setKeywords(String keywords) {        
		this.keywords = keywords;
	} 

	/**
	 * @return Returns the srs.
	 */
	public String getSrs() {        
		return srs;
	} 

	/**
	 * @param srs The srs to set.
	 */
	public void setSrs(String srs) {        
		this.srs = srs;
	} 

	/**
	 * @return Returns the title.
	 */
	public String getTitle() {        
		return title;
	} 

	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {        
		this.title = title;
	} 

	/**
	 * To load into a List
	 * @return 
	 */
	public String toString() {        
		return title;
	} 

	/**
	 * @return Returns the features.
	 */
	public FeatureType[] getFeatures() {        
		return featureTypes;
	} 

	/**
	 * @param features The features to set.
	 */
	public void setFeatures(FeatureType[] featureTypes) {        
		this.featureTypes = featureTypes;
	} 

	/**
	 * @return Returns the fields.
	 */
	public FeatureTypeAttribute[] getFields() {        
		return attributes; 
	} 

	/**
	 * @param fields The fields to set.
	 */
	public void setFields(FeatureTypeAttribute[] attributes) {        
		this.attributes = attributes;
	} 
}
