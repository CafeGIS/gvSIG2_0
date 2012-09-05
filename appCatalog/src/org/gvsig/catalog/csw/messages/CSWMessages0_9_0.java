package org.gvsig.catalog.csw.messages;

import org.gvsig.catalog.csw.drivers.profiles.CSWAbstractProfile;
import org.gvsig.catalog.csw.parsers.CSWConstants;
import org.gvsig.catalog.languages.FilterEncoding;

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
public class CSWMessages0_9_0 extends CSWAbstractMessages{
	private static final String TYPENAMES = "Dataset";
	private static final String ESCAPECHAR = "escape";
	
	public CSWMessages0_9_0(CSWAbstractProfile profile) {
		super(profile);		
	}

	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalog.csw.messages.CSWAbstractMessages#getContraintVersion()
	 */
	protected String getContraintVersion() {
		return CSWConstants.CONSTRAINT_VERSION_0_9_0;
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalog.csw.messages.CSWAbstractMessages#createGetRecordsQuery()
	 */
	protected String createGetRecordsQuery(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("<" + CSWConstants.CSW_NAMESPACE + ":" + CSWConstants.QUERY + " ");
		buffer.append(CSWConstants.TYPENAMES + "=\"" + TYPENAMES + "\"");
		buffer.append(">");
		buffer.append("<" + CSWConstants.CSW_NAMESPACE + ":" + CSWConstants.ELEMENTSETNAME + " ");
		buffer.append(CSWConstants.TYPENAMES + "=\"" + TYPENAMES + "\"");
		buffer.append(">");
		buffer.append(CSWConstants.FULL);
		buffer.append("</" + CSWConstants.CSW_NAMESPACE + ":" + CSWConstants.ELEMENTSETNAME + ">");
		buffer.append(createGetRecordsConstraint());
		buffer.append("</" + CSWConstants.CSW_NAMESPACE + ":" + CSWConstants.QUERY + ">");
		return buffer.toString();
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.catalog.csw.messages.CSWAbstractMessages#getFilterEncoding()
	 */
	protected FilterEncoding getFilterEncoding(){
		FilterEncoding filter = super.getFilterEncoding();
		filter.setEscapeCharLabel(ESCAPECHAR);
		return filter;
	}

}
