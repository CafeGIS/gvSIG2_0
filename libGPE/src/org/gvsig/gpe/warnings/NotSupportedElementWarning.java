package org.gvsig.gpe.warnings;

import java.util.Hashtable;
import java.util.Map;

import org.gvsig.tools.exception.BaseException;

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
 * $Id: NotSupportedElementWarning.java 162 2007-06-29 12:19:48Z jorpiell $
 * $Log$
 * Revision 1.2  2007/06/29 12:19:14  jorpiell
 * The schema validation is made independently of the concrete writer
 *
 * Revision 1.1  2007/06/28 13:04:33  jorpiell
 * The Qname has been updated to the 1.5 JVM machine. The schema validation is made in the GPEWriterHandlerImplementor class
 *
 * Revision 1.1  2007/06/22 12:19:07  jorpiell
 * Add the exception
 *
 *
 */
/**
 * It is thrown when there is an element that can not
 * be validated using the XML Schema
 * @author Jorge Piera Llodrá (jorge.piera@iver.es)
 * @author Carlos Sánchez Periñán (sanchez_carper@gva.es)
 */
public class NotSupportedElementWarning extends BaseException{
	private static final long serialVersionUID = -8478703373223856720L;
	private String elementName = null;
	private String xsElementName = null;
	private String parentName = null;
	private String xsParentElementName = null;

	public NotSupportedElementWarning(String elementName, String xsElementName, String parentName, String xsParentElementName) {
		this.elementName = elementName;
		this.xsElementName = xsElementName;
		this.parentName = parentName;
		this.xsParentElementName = xsParentElementName;
		initialize();
	}

	/**
	 * Initialize the properties
	 */
	private void initialize() {
		messageKey = "gpe_not_supported_element_warning";
		formatString = "The element '%(elementName)' with a XML schema " + 
			"type '%(xsElementName)' is not contained in the parent element " +
			"'%(parentName)' with a XML Schema type '%(xsParentElementName)'";
		code = serialVersionUID;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.tools.exception.BaseException#values()
	 */
	protected Map values() {
		Hashtable hash = new Hashtable();
		hash.put("elementName", elementName);
		hash.put("xsElementName", xsElementName);
		hash.put("parentName", parentName);
		hash.put("xsParentElementName", xsParentElementName);
		return hash;
	}
	
}
