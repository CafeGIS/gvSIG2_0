package org.gvsig.catalog.drivers.profiles;

import com.iver.utiles.swing.jcomboServer.ServerData;

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
public abstract class AbstractProfile implements IProfile{
	private ServerData serverData = null;

	/**
	 * @param serverData
	 */
	public AbstractProfile(ServerData serverData) {
		super();
		this.serverData = serverData;
	}	

	/**
	 * @param serverData
	 */
	public AbstractProfile() {
		super();		
	}

	/**
	 * @return the abstract property name
	 */	
	public String getAbstract() {
		String property = getProperty(IProfile.ABSTRACT);
		if (property != null){
			return property;
		}	
		return getAbstractProperty();
	}

	/**
	 * @return the coordinates property name
	 */
	public String getCoordinates() {
		String property = getProperty(IProfile.COORDINATES);
		if (property != null){
			return property;
		}	
		return getCoordinatesProperty();
	}

	/**
	 * @return the date from property
	 */
	public String getDateFrom() {
		String property = getProperty(IProfile.DATEFROM);
		if (property != null){
			return property;
		}	
		return getDateFromProperty();
	}

	/**
	 * @return the date to property
	 */
	public String getDateTo() {
		String property = getProperty(IProfile.DATETO);
		if (property != null){
			return property;
		}	
		return getDateToProperty();
	}

	/**
	 * @return the keyword property
	 */
	public String getKeywords() {
		String property = getProperty(IProfile.KEYWORDS);
		if (property != null){
			return property;
		}	
		return getKeywordsProperty();
	}

	/**
	 * @return the provider property
	 */
	public String getProvider() {
		String property = getProperty(IProfile.PROVIDER);
		if (property != null){
			return property;
		}	
		return getProviderProperty();
	}

	/**
	 * @return the scale property
	 */
	public String getScale() {
		String property = getProperty(IProfile.SCALE);
		if (property != null){
			return property;
		}	
		return getScaleProperty();
	}

	/**
	 * @return the title property name
	 */
	public String getTitle() {
		String property = getProperty(IProfile.TITLE);
		if (property != null){
			return property;
		}	
		return getTitleProperty();
	}
	
	/**
	 * @return the element name property
	 */
	public String getElementName(){
		String property = getProperty(IProfile.ELEMENT_NAME);
		if (property != null){
			return property;
		}
		return getElementNameProperty();
	}
	
	/**
	 * Return a property with a concrete name
	 * @param propertyName
	 * The property name
	 * @return
	 */
	private String getProperty(String propertyName){
		if ((serverData != null) && (serverData.getProperies() != null)){
			Object obj = serverData.getProperies().getProperty(propertyName);
			if (!((obj == null) || (((String)obj).compareTo("") == 0))){
				return (String)obj;
			}
		}
		return null;
	}

	/**
	 * @return the topic property
	 */
	public String getTopic() {
		String property = getProperty(IProfile.CATHEGORY);
		if (property != null){
			return property;
		}	
		return getTopicProperty();
	}

	/**
	 * @return the serverData
	 */
	public ServerData getServerData() {
		return serverData;
	}

	/**
	 * @param serverData the serverData to set
	 */
	public void setServerData(ServerData serverData) {
		this.serverData = serverData;
	}
	
}
