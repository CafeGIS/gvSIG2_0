package org.gvsig.catalog.drivers.profiles;
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
public interface IProfile {
	public static final String TITLE = "titlePropertyName";
	public static final String ABSTRACT = "abstractPropertyName";
	public static final String KEYWORDS = "keywordsPropertyName";
	public static final String CATHEGORY = "cathegoryPropertyName";
	public static final String SCALE = "scalePropertyName";
	public static final String PROVIDER = "providerPropertyName";
	public static final String COORDINATES = "coordinatesPropertyName";
	public static final String DATEFROM = "datefromPropertyName";
	public static final String DATETO = "datetoPropertyName";
	public static final String ELEMENT_NAME = "elementNamePropertyName";
	
	/**
	 * @return the element name property
	 */
	public String getElementNameProperty();
	
	/**
	 * @return the title attribute name
	 */
	public String getTitleProperty();

	/**
	 * @return the abstract attribute name
	 */
	public String getAbstractProperty();

	/**
	 * @return the keyword attribute name 
	 */
	public String getKeywordsProperty();

	/**
	 * @return the topic attribute name  
	 */
	public String getTopicProperty();

	/**
	 * @return the scale attribute name  
	 */
	public String getScaleProperty();

	/**
	 * @return the provider attribute name 
	 */
	public String getProviderProperty();

	/**
	 * @return the date from attribute name  
	 */
	public String getDateFromProperty();

	/**
	 * @return the date to attribute name  
	 */
	public String getDateToProperty();

	/**
	 * @return the coordinates attribute name  
	 */
	public String getCoordinatesProperty();

	/**
	 * Return the topic name for a concrete profile
	 * @param topic
	 * @return
	 * The topic name
	 */
	public String getTopicValue(String topic);     

}
