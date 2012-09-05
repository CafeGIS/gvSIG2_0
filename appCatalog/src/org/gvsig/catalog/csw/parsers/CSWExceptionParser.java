package org.gvsig.catalog.csw.parsers;

import org.gvsig.catalog.csw.drivers.CSWException;
import org.gvsig.catalog.metadataxml.XMLNode;

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
public class CSWExceptionParser {

	public static CSWException parse(XMLNode node){
		CSWException exception = new CSWException();
		XMLNode exNode = node.searchNode(CSWConstants.OWSEXCEPTION);
		if (exNode != null){
			exception = new CSWException();
			exception.setCode(exNode.searchAtribute(CSWConstants.OWSEXCEPTIONCODE));
			exception.setSubcode(exNode.searchAtribute(CSWConstants.OWSEXCEPTIONSUBCODE));
			exception.setReason(exNode.searchAtribute(CSWConstants.OWSEXCEPTIONREASON));
			exception.setAbstract(exNode.getText());
		}
		return exception;
	}
}
