package org.gvsig.gpe.xml.exceptions;

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
 * $Id: GPEXmlToZipFileException.java 149 2007-06-11 06:41:41Z jorpiell $
 * $Log$
 * Revision 1.1  2007/06/11 06:41:29  jorpiell
 * Add a new Exception
 *
 *
 */
/**
 * This exception is throwed when has hasppend an error
 * compressing the XML file to zip
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class GPEXmlToZipFileException extends BaseException{
	private static final long serialVersionUID = -3652496845777784603L;
	private String fileName = null;
	
	public GPEXmlToZipFileException(String fileName, Throwable exception){
		this.fileName = fileName;
		initialize();
		initCause(exception);
	}
	
	/**
	 * Initialize the properties
	 */
	private void initialize() {
		messageKey = "gpe_xmlToZip_error";
		formatString = "Error compressing the XML %(fileName) file";							
		code = serialVersionUID;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.tools.exception.BaseException#values()
	 */
	protected Map values() {
		Hashtable hash = new Hashtable();
		hash.put("fileName", fileName);
		return hash;
	}


}
