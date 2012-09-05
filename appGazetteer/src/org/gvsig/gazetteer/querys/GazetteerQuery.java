
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
import org.gvsig.catalog.querys.DiscoveryServiceQuery;
import org.gvsig.catalog.utils.CatalogConstants;

/**
 * This class contains all the queryable fields that the user interface
 * supports.
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class GazetteerQuery extends DiscoveryServiceQuery{
	private String name;
	private String nameFilter;
	private String fieldAttribute;
	private int recsByPage;
	private Coordinates coordinates;
	private String coordinatesFilter;
	private boolean isCoordinatesClicked;
	private GazetteerOptions options;
	public java.util.Collection thesaurusName = new java.util.ArrayList();
	

	/**
	 * @param name 
	 * @param nameFilter 
	 * @param feature 
	 * @param fieldAttribute 
	 * @param recsByPage 
	 * @param coordinates 
	 * @param coordinatesFilter 
	 */
	public GazetteerQuery(String name, String nameFilter, FeatureType[] feature, String fieldAttribute, int recsByPage, Coordinates coordinates, String coordinatesFilter) {        
		super();
		this.name = name;
		this.nameFilter = nameFilter;
		setFeatures(feature);
		this.fieldAttribute = fieldAttribute;
		this.recsByPage = recsByPage;
		this.coordinates = coordinates;
		this.coordinatesFilter = coordinatesFilter;
		options = new GazetteerOptions();
	} 

	/**
	 * Constructor
	 */
	public  GazetteerQuery() {        
		super();
		options = new GazetteerOptions();		
	} 

	/**
	 * To search a place by name
	 * @param name Place name
	 * @param description Thesaurus description
	 */
	public  GazetteerQuery(String name, String description) {        
		super();
		FeatureType[] thesaurus = new FeatureType[1];
		thesaurus[0] = new FeatureType();
		thesaurus[0].setName(description);
		this.name = name;
		this.nameFilter = CatalogConstants.ANY_WORD;
		setFeatures(thesaurus);
		this.recsByPage = 100;
		this.coordinates = null;
		this.coordinatesFilter = null;		
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
	 * @return Returns the coordinatesFilter.
	 */
	public String getCoordinatesFilter() {        
		return coordinatesFilter;
	} 

	/**
	 * @param coordinatesFilter The coordinatesFilter to set.
	 */
	public void setCoordinatesFilter(String coordinatesFilter) {        
		this.coordinatesFilter = coordinatesFilter;
	} 

	/**
	 * @return Returns the feature.
	 */
	public FeatureType[] getFeatures() {        
		FeatureType[] aux = new FeatureType[thesaurusName.size()];
		for (int i=0 ; i<aux.length ; i++){
			aux[i] = (FeatureType)thesaurusName.toArray()[i];
		}
		return aux;  
	} 

	/**
	 * @param feature The feature to set.
	 * @param features The feature to set.
	 */
	public void setFeatures(FeatureType[] features) {        
		this.thesaurusName = new java.util.ArrayList();
		if (features == null){
			return;
		}
		for (int i=0 ; i<features.length ; i++){
			this.thesaurusName.add(features[i]);
		}     
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
	 * @return Returns the nameFilter.
	 */
	public String getNameFilter() {        
		return nameFilter;
	} 

	/**
	 * @param nameFilter The nameFilter to set.
	 */
	public void setNameFilter(String nameFilter) {        
		this.nameFilter = nameFilter;
	} 

	/**
	 * @return Returns the recsByPage.
	 */
	public int getRecsByPage() {        
		return recsByPage;
	} 

	/**
	 * @param recsByPage The recsByPage to set.
	 */
	public void setRecsByPage(int recsByPage) {        
		this.recsByPage = recsByPage;
	} 

	/**
	 * @return Returns the fieldAttribute.
	 */
	public String getFieldAttribute() {        
		return fieldAttribute;
	} 

	/**
	 * @param fieldAttribute The fieldAttribute to set.
	 */
	public void setFieldAttribute(String fieldAttribute) {        
		this.fieldAttribute = fieldAttribute;
	} 

	/**
	 * @return Returns the isCoordinatesClicked.
	 */
	public boolean isCoordinatesClicked() {        
		return isCoordinatesClicked;
	} 

	/**
	 * @param isCoordinatesClicked The isCoordinatesClicked to set.
	 */
	public void setCoordinatesClicked(boolean isCoordinatesClicked) {        
		this.isCoordinatesClicked = isCoordinatesClicked;
	}

	/**
	 * @return the gaazetter options
	 */	
	public GazetteerOptions getOptions() {
		return options;
	}

	/**
	 * Set the options
	 * @param options
	 */
	public void setOptions(GazetteerOptions options) {
		this.options = options;
	}

}
