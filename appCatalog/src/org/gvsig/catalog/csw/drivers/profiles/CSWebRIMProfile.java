package org.gvsig.catalog.csw.drivers.profiles;



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
public class CSWebRIMProfile extends CSWAbstractProfile{

	public CSWebRIMProfile() {
		super();		
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalog.csw.drivers.profiles.ICSWProfile#getTitle()
	 */
	public String getTitleProperty() {
		return "/DataGranule/instrumentShortName";
	}	
	
	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalog.csw.drivers.profiles.ICSWProfile#getAbstract()
	 */
	public String getAbstractProperty() {
		 return "/Dataset/Description/LocalizedString/@value";
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalog.csw.drivers.profiles.ICSWProfile#getCoordinates()
	 */
	public String getCoordinatesProperty() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalog.csw.drivers.profiles.ICSWProfile#getDateFrom()
	 */
	public String getDateFromProperty() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalog.csw.drivers.profiles.ICSWProfile#getDateTo()
	 */
	public String getDateToProperty() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalog.csw.drivers.profiles.ICSWProfile#getProvider()
	 */
	public String getProviderProperty() {
		return "/Organization/Name/LocalizedString/@value";
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalog.csw.drivers.profiles.ICSWProfile#getScale()
	 */
	public String getScaleProperty() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalog.csw.drivers.profiles.ICSWProfile#getThemeKey()
	 */
	public String getKeywordsProperty() {
		 return "/Dataset/Slot:s2/ValueList/Value";
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalog.csw.drivers.profiles.ICSWProfile#getTopic()
	 */
	public String getTopicProperty() {
		return "/Classification/@classificationNode";
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalog.csw.drivers.profiles.ICSWProfile#getTopicValue(java.lang.String)
	 */
	public String getTopicValue(String topic) {
		 if (topic.equals("farming")) {
	            return "urn:x-iso-tc211:19119(2003)/scheme/services/scheme/topics:001";
	        }
	        if (topic.equals("biota")) {
	            return "urn:x-iso-tc211:19119(2003)/scheme/services/scheme/topics:002";
	        }
	        if (topic.equals("boundaries")) {
	            return "urn:x-iso-tc211:19119(2003)/scheme/services/scheme/topics:003";
	        }
	        if (topic.equals("climatologyMeteorologyAtmosphere")) {
	            return "urn:x-iso-tc211:19119(2003)/scheme/services/scheme/topics:004";
	        }
	        if (topic.equals("economy")) {
	            return "urn:x-iso-tc211:19119(2003)/scheme/services/scheme/topics:005";
	        }
	        if (topic.equals("elevation")) {
	            return "urn:x-iso-tc211:19119(2003)/scheme/services/scheme/topics:006";
	        }
	        if (topic.equals("environment")) {
	            return "urn:x-iso-tc211:19119(2003)/scheme/services/scheme/topics:007";
	        }
	        if (topic.equals("geoscientificInformation")) {
	            return "urn:x-iso-tc211:19119(2003)/scheme/services/scheme/topics:008";
	        }
	        if (topic.equals("health")) {
	            return "urn:x-iso-tc211:19119(2003)/scheme/services/scheme/topics:009";
	        }
	        if (topic.equals("imageryBaseMapsEarthCover")) {
	            return "urn:x-iso-tc211:19119(2003)/scheme/services/scheme/topics:010";
	        }
	        if (topic.equals("intelligenceMilitary")) {
	            return "urn:x-iso-tc211:19119(2003)/scheme/services/scheme/topics:011";
	        }
	        if (topic.equals("inlandWaters")) {
	            return "urn:x-iso-tc211:19119(2003)/scheme/services/scheme/topics:012";
	        }
	        if (topic.equals("location")) {
	            return "urn:x-iso-tc211:19119(2003)/scheme/services/scheme/topics:013";
	        }
	        if (topic.equals("oceans")) {
	            return "urn:x-iso-tc211:19119(2003)/scheme/services/scheme/topics:014";
	        }
	        if (topic.equals("planningCadastre")) {
	            return "urn:x-iso-tc211:19119(2003)/scheme/services/scheme/topics:015";
	        }
	        if (topic.equals("society")) {
	            return "urn:x-iso-tc211:19119(2003)/scheme/services/scheme/topics:016";
	        }
	        if (topic.equals("structure")) {
	            return "urn:x-iso-tc211:19119(2003)/scheme/services/scheme/topics:017";
	        }
	        if (topic.equals("transportation")) {
	            return "urn:x-iso-tc211:19119(2003)/scheme/services/scheme/topics:018";
	        }
	        if (topic.equals("utilitiesCommunication")) {
	            return "urn:x-iso-tc211:19119(2003)/scheme/services/scheme/topics:019";
	        }
	        return "";
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalog.drivers.profiles.IProfile#getElementNameProperty()
	 */
	public String getElementNameProperty() {
		return "/DataGranule/";
	} 
}
