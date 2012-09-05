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
public class CSWISO19115Profile extends CSWAbstractProfile{

	public CSWISO19115Profile() {
		super();		
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalog.csw.drivers.profiles.ICSWProfile#getTitle()
	 */
	public String getTitleProperty() {
		return "title";
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalog.csw.drivers.profiles.ICSWProfile#getAbstract()
	 */
	public String getAbstractProperty() {
		 return "abstract";
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalog.csw.drivers.profiles.ICSWProfile#getCoordinates()
	 */
	public String getCoordinatesProperty() {
		 return "geom";
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalog.csw.drivers.profiles.ICSWProfile#getDateFrom()
	 */
	public String getDateFromProperty() {
		 return "modified";
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalog.csw.drivers.profiles.ICSWProfile#getDateTo()
	 */
	public String getDateToProperty() {
		return "modified";
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalog.csw.drivers.profiles.ICSWProfile#getProvider()
	 */
	public String getProviderProperty() {
		return "publisher";
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalog.csw.drivers.profiles.ICSWProfile#getScale()
	 */
	public String getScaleProperty() {
		  return "scale";
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalog.csw.drivers.profiles.ICSWProfile#getThemeKey()
	 */
	public String getKeywordsProperty() {
		 return "keyword";
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalog.csw.drivers.profiles.ICSWProfile#getTopic()
	 */
	public String getTopicProperty() {
		 return "topic";
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalog.csw.drivers.profiles.ICSWProfile#getTopicValue(java.lang.String)
	 */
	public String getTopicValue(String topic) {
		return topic;
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalog.drivers.profiles.IProfile#getElementNameProperty()
	 */
	public String getElementNameProperty() {
		return null;
	}	
	

}
