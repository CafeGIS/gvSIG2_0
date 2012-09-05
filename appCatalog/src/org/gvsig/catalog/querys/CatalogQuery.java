
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
package org.gvsig.catalog.querys;

import org.gvsig.catalog.utils.CatalogConstants;

/**
 * This class implements a general query. It contains an attribute
 * for each parameter that can be showed in the search form.
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class CatalogQuery extends DiscoveryServiceQuery{
	private Search service = null;
	private String title;
	private String titleFilter;
	private String abstract_;
	private String themeKey;
	private String topic;
	private String scale;
	private String provider;
	private String DateFrom;
	private String DateTo;
	private Coordinates coordinates;
	private String coordinatesFilter;
	private boolean isMinimized;
	private boolean isCoordinatesClicked;

	public  CatalogQuery() {        
		super();
	} 

	/**
	 * @param title 
	 * @param titleFilter 
	 * @param abstract_ 
	 * @param themeKey 
	 * @param topic 
	 * @param scale 
	 * @param provider 
	 * @param dateFrom 
	 * @param dateTo 
	 * @param coordinates 
	 * @param coordinatesFilter 
	 * @param translator 
	 */
	public  CatalogQuery(String title, String titleFilter, String abstract_, String themeKey, String topic, String scale, String provider, String dateFrom, String dateTo, Coordinates coordinates, String coordinatesFilter) {        
		super();
		this.title = title;
		this.titleFilter = titleFilter;
		this.abstract_ = abstract_;
		this.themeKey = themeKey;
		this.topic = topic;
		this.scale = scale;
		this.provider = provider;
		//Date
		this.DateFrom = dateFrom;
		this.DateTo = dateTo;
		//Coordinates
		this.coordinates = coordinates;
		if (this.coordinates != null) {
			if (this.coordinates.getUlx().equals("") ||
					this.coordinates.getUlx().equals("") ||
					this.coordinates.getBrx().equals("") ||
					this.coordinates.getBry().equals("")) {
				this.coordinates = null;
			} else {
				try {
					Double.parseDouble(this.coordinates.getUlx());
					Double.parseDouble(this.coordinates.getUlx());
					Double.parseDouble(this.coordinates.getBrx());
					Double.parseDouble(this.coordinates.getBry());
					this.coordinates = coordinates;
				} catch (Exception e) {
					this.coordinates = null;

				}
			}
		}
		this.coordinatesFilter = coordinatesFilter;

		setMinimized(false);
	} 

	/**
	 * Return logic operators
	 * 
	 * @param titleKeys E,A o Y --> Exact, All, anY
	 * 
	 * @return String
	 * @param concordancia 
	 */
	public String getOperator(String concordancia) {        
		if (concordancia.equals(CatalogConstants.ANY_WORD)) {
			return CatalogConstants.OR;
		} else {
			return CatalogConstants.OR;
		}
	} 

	/**
	 * 
	 * 
	 * 
	 * @return Returns the abstract_.
	 */
	public String getAbstract() {        
		return abstract_;
	} 

	/**
	 * 
	 * 
	 * 
	 * @param abstract_ The abstract_ to set.
	 */
	public void setAbstract(String abstract_) {        
		this.abstract_ = abstract_;
	} 

	/**
	 * 
	 * 
	 * 
	 * @return Returns the coordinates.
	 */
	public Coordinates getCoordenates() {        
		return coordinates;
	} 

	/**
	 * 
	 * 
	 * 
	 * @param coordenates The coordinates to set.
	 */
	public void setCoordenates(Coordinates coordenates) {        
		this.coordinates = coordenates;
	} 

	/**
	 * 
	 * 
	 * 
	 * @return Returns the coordinatesFilter.
	 */
	public String getCoordenatesFilter() {        
		return coordinatesFilter;
	} 

	/**
	 * 
	 * 
	 * 
	 * @param coordenatesFilter The coordenatesFilter to set.
	 */
	public void setCoordenatesFilter(String coordenatesFilter) {        
		this.coordinatesFilter = coordenatesFilter;
	} 

	/**
	 * 
	 * 
	 * 
	 * @return Returns the dateFrom.
	 */
	public String getDateFrom() {        
		return DateFrom;
	} 

	/**
	 * 
	 * 
	 * 
	 * @param dateFrom The dateFrom to set.
	 */
	public void setDateFrom(String dateFrom) {        
		DateFrom = dateFrom;
	} 

	/**
	 * 
	 * 
	 * 
	 * @return Returns the dateTo.
	 */
	public String getDateTo() {        
		return DateTo;
	} 

	/**
	 * 
	 * 
	 * 
	 * @param dateTo The dateTo to set.
	 */
	public void setDateTo(String dateTo) {        
		DateTo = dateTo;
	} 

	/**
	 * 
	 * 
	 * 
	 * @return Returns the provider.
	 */
	public String getProvider() {        
		return provider;
	} 

	/**
	 * 
	 * 
	 * 
	 * @param provider The provider to set.
	 */
	public void setProvider(String provider) {        
		this.provider = provider;
	} 

	/**
	 * 
	 * 
	 * 
	 * @return Returns the scale.
	 */
	public String getScale() {        
		return scale;
	} 

	/**
	 * 
	 * 
	 * 
	 * @param scale The scale to set.
	 */
	public void setScale(String scale) {        
		this.scale = scale;
	} 

	/**
	 * 
	 * 
	 * 
	 * @return Returns the themeKey.
	 */
	public String getThemeKey() {        
		return themeKey;
	} 

	/**
	 * 
	 * 
	 * 
	 * @param themeKey The themeKey to set.
	 */
	public void setThemeKey(String themeKey) {        
		this.themeKey = themeKey;
	} 

	/**
	 * 
	 * 
	 * 
	 * @return Returns the title.
	 */
	public String getTitle() {        
		return title;
	} 

	/**
	 * 
	 * 
	 * 
	 * @param title The title to set.
	 */
	public void setTitle(String title) {        
		this.title = title;
	} 

	/**
	 * 
	 * 
	 * 
	 * @return Returns the titleFilter.
	 */
	public String getTitleFilter() {        
		return titleFilter;
	} 

	/**
	 * 
	 * 
	 * 
	 * @param titleFilter The titleFilter to set.
	 */
	public void setTitleFilter(String titleFilter) {        
		this.titleFilter = titleFilter;
	} 

	/**
	 * 
	 * 
	 * 
	 * @return Returns the topic.
	 */
	public String getTopic() {        
		return topic;
	} 

	/**
	 * 
	 * 
	 * 
	 * @param topic The topic to set.
	 */
	public void setTopic(String topic) {        
		this.topic = topic;
	} 

	/**
	 * 
	 * 
	 * 
	 * @return Returns the isMinimized.
	 */
	public boolean isMinimized() {        
		return isMinimized;
	} 

	/**
	 * 
	 * 
	 * 
	 * @param isMinimized The isMinimized to set.
	 */
	public void setMinimized(boolean isMinimized) {        
		this.isMinimized = isMinimized;
		if (isMinimized){
			this.setAbstract(getTitle());
		}
	} 

	/**
	 * 
	 * 
	 * 
	 * @return Returns the isCoordinatesClicked.
	 */
	public boolean isCoordinatesClicked() {        
		return isCoordinatesClicked;
	} 

	/**
	 * 
	 * 
	 * 
	 * @param isCoordinatesClicked The isCoordinatesClicked to set.
	 */
	public void setCoordinatesClicked(boolean isCoordinatesClicked) {        
		this.isCoordinatesClicked = isCoordinatesClicked;
	} 


	/**
	 * It returns the lower scale
	 * 
	 * 
	 * @return String or Null if ther is not a low value (0)
	 */
	public String getMinScale() {        
		if (scale.equals(">1.000.000")){
			return "1000000";
		}

		if (scale.equals("1.000.000 - 250.000")){
			return "250000";
		}

		if (scale.equals("250.000 - 50.000")){
			return "50000";
		}

		if (scale.equals("50.000 - 10.000")){
			return "10000";
		}

		if (scale.equals("10.000 - 5000")){
			return "5000";
		}

		return null;        
	} 

	/**
	 * It returns the upper scale
	 * 
	 * 
	 * @return String or Null if ther is not a value (infinity)
	 */
	public String getMaxScale() {        
		if (scale.equals("1.000.000 - 250.000")){
			return "1000000";
		}

		if (scale.equals("250.000 - 50.000")){
			return "250000";
		}	

		if (scale.equals("50.000 - 10.000")){
			return "50000";
		}

		if (scale.equals("10.000 - 5000")){
			return "10000";
		}

		if (scale.equals("<5.000")){
			return "5000";
		}   
		return null;        
	}

	/**
	 * @return the service
	 */
	public Search getService() {
		return service;
	}

	/**
	 * @param service the service to set
	 */
	public void setService(Search service) {
		this.service = service;
	} 
	
}
