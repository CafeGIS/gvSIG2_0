package org.gvsig.gpe.parser;
import java.io.InputStream;
import java.net.URI;

import org.gvsig.gpe.GPELocator;
import org.gvsig.gpe.GPEManager;


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
 * $Id: GPEParser.java 173 2007-11-06 12:10:57Z jpiera $
 * $Log$
 * Revision 1.14  2007/06/20 09:35:37  jorpiell
 * Add the javadoc comments
 *
 * Revision 1.13  2007/06/07 14:52:28  jorpiell
 * Add the schema support
 *
 * Revision 1.12  2007/05/09 06:54:07  jorpiell
 * Change the File by URI
 *
 * Revision 1.11  2007/05/07 07:06:26  jorpiell
 * Add a constructor with the name and the description fields
 *
 * Revision 1.10  2007/04/20 12:04:10  csanchez
 * Actualizacion protoripo libGPE, AÃ±adidos test para el parser, parseo con XSOM
 *
 * Revision 1.9  2007/04/19 11:50:20  csanchez
 * Actualizacion protoripo libGPE
 *
 * Revision 1.8  2007/04/19 07:23:20  jorpiell
 * Add the add methods to teh contenhandler and change the register mode
 *
 * Revision 1.7  2007/04/14 16:06:13  jorpiell
 * The writer handler has been updated
 *
 * Revision 1.6  2007/04/13 07:17:54  jorpiell
 * Add the writting tests for the simple geometries
 *
 * Revision 1.5  2007/04/12 17:06:42  jorpiell
 * First GML writing tests
 *
 * Revision 1.4  2007/04/12 10:21:29  jorpiell
 * Add the getWriter() method
 *
 * Revision 1.3  2007/04/11 11:18:15  csanchez
 * Actualizacion protoripo libGPE
 *
 * Revision 1.2  2007/04/11 08:46:21  csanchez
 * Actualizacion protoripo libGPE
 *
 * Revision 1.1  2007/04/11 08:19:32  csanchez
 * actualizacion
 *
 *
 */
/**
 * This class has to be inherited by all the classes that 
 * implements a parser for a geographical format. IT has methods
 * to indicate the formats and the versions that is able to
 * parse.
 * @author Jorge Piera Llodrá (jorge.piera@iver.es)
 * @author Carlos Sánchez Periñán (sanchez_carper@gva.es)
 */


public abstract class GPEParser {
	private IGPEErrorHandler errorHandler;
	private IGPEContentHandler contentHandler;
	private URI mainFile;
	private InputStream is = null;
	private GPEManager gpeManager = null;
		
	/** 
	 * All the GPE parser must implement an empty constructor 
	 **/
	public GPEParser(){
		super();
		gpeManager = GPELocator.getGPEManager();
	}	
	
	/**
	 * Method to parse a file. It cannot to throw 
	 * any exception and it cannot to return any value.
	 * In a future it could be implemented like a independent
	 * thread
	 * @param contents
	 * Application ContentHandler
	 * @param errors
	 * Application ErrorsHandler
	 * @param uri
	 * File to open
	 * @throws Exception
	 */
	public void parse(IGPEContentHandler contents, IGPEErrorHandler errors, URI uri) {
		this.contentHandler = contents;
		this.errorHandler = errors;
		this.mainFile = uri;
		parseURI();
	}	
	
	/**
	 * Parses the file from a URI
	 */
	protected abstract void parseURI();

	/**
	 * Method to parse an InputStream. It cannot to throw 
	 * any exception and it cannot to return any value.
	 * In a future it could be implemented like a independent
	 * thread
	 * @param contents
	 * Application ContentHandler
	 * @param errors
	 * Application ErrorsHandler
	 * @param is
	 * The input stream
	 * @throws Exception
	 */
	public void parse(IGPEContentHandler contents, IGPEErrorHandler errors, InputStream is) {
		this.contentHandler = contents;
		this.errorHandler = errors;
		this.is = is;
		parseStream();
	}
	
	/**
	 * Parses the file from an input stream
	 */
	protected abstract void parseStream();
	
	/**
	 * Return if the driver can open the file
	 * @param uri
	 * File to open
	 * @return
	 * True if the driver is able to open it
	 */
	public abstract boolean accept(URI uri);

	/**
	 * Return the format that the driver
	 * is able to read
	 * @return
	 */
	public abstract String getFormat();
	
	/**
	 * @return the contentHandler
	 */
	public IGPEContentHandler getContentHandler() {
		return contentHandler;
	}


	/**
	 * @return the errorHandler
	 */
	public IGPEErrorHandler getErrorHandler() {
		return errorHandler;
	}

	/**
	 * @return the file
	 */
	public URI getMainFile() {
		return mainFile;
	}

	/**
	 * @return the description
	 */
	public abstract String getDescription();

	/**
	 * @return the name
	 */
	public abstract String getName();

	/**
	 * @return the is
	 */
	protected InputStream getInputStream() {
		return is;
	}

	/**
     * We need to find a better way to reuse or coupling parsers. This method was introduced for the
     * SLD parsing to grab a single geometry from a filter.
     * 
     * @param is the is to set
     * @deprecated to avoid temporal coupling
     */
	protected void setInputStream(InputStream is) {
		this.is = is;
	}

	/**
	 * @param errorHandler the errorHandler to set
	 */
	public void setErrorHandler(GPEErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}

	/**
	 * @param contentHandler the contentHandler to set
	 */
	public void setContentHandler(GPEContentHandler contentHandler) {
		this.contentHandler = contentHandler;
	}
	
	/**
	 * @return the gpeManager
	 */
	public GPEManager getGpeManager() {
		return gpeManager;
	}

}
