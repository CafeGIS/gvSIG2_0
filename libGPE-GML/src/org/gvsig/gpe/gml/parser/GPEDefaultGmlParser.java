package org.gvsig.gpe.gml.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.gvsig.gpe.gml.parser.profiles.IBindingProfile;
import org.gvsig.gpe.xml.exceptions.GPEXmlEmptyFileException;
import org.gvsig.gpe.xml.parser.GPEXmlParser;
import org.gvsig.gpe.xml.stream.IXmlStreamReader;
import org.gvsig.gpe.xml.stream.XmlStreamException;

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
 * $Id: GPEGmlParser.java 162 2007-06-29 12:19:48Z jorpiell $
 * $Log$
 * Revision 1.15  2007/06/29 12:19:34  jorpiell
 * The schema validation is made independently of the concrete writer
 *
 * Revision 1.14  2007/06/07 14:53:30  jorpiell
 * Add the schema support
 *
 * Revision 1.13  2007/05/24 07:29:47  csanchez
 * AÃ±adidos Alias GML2
 *
 * Revision 1.12  2007/05/17 11:20:51  jorpiell
 * Add the layer methods
 *
 * Revision 1.11  2007/05/16 12:34:55  csanchez
 * GPEParser Prototipo final de lectura
 *
 * Revision 1.10  2007/05/14 11:23:12  jorpiell
 * ProjectionFactory updated
 *
 * Revision 1.9  2007/05/14 09:52:26  jorpiell
 * Tupes separator tag updated
 *
 * Revision 1.8  2007/05/09 06:54:25  jorpiell
 * Change the File by URI
 *
 * Revision 1.7  2007/05/07 07:06:46  jorpiell
 * Add a constructor with the name and the description fields
 *
 * Revision 1.6  2007/04/25 11:08:38  csanchez
 * Parseo correcto con XSOM de esquemas, EntityResolver para los imports
 *
 * Revision 1.5  2007/04/20 12:04:10  csanchez
 * Actualizacion protoripo libGPE, AÃ±adidos test para el parser, parseo con XSOM
 *
 * Revision 1.4  2007/04/19 11:51:43  csanchez
 * Actualizacion protoripo libGPE
 *
 * Revision 1.1  2007/04/18 12:54:45  csanchez
 * Actualizacion protoripo libGPE
 *
 *
 */
/**
 * @author Carlos Sánchez Periñán (sanchez_carper@gva.es)
 */

public abstract class GPEDefaultGmlParser extends GPEXmlParser {
	private IXmlStreamReader fileParser = null;
	private IBindingProfile profile = null;

	/***********************************************************************************************
	 * <GPEGmlParser> Constructor Method
	 **********************************************************************************************/
	public GPEDefaultGmlParser() {
		super();
	}

	/***********************************************************************************************
	 * <getFormats> Returns the file extensions that are accepted to GML Parser
	 * 
	 * @return String[] Accepted Extensions
	 **********************************************************************************************/
	public String[] getFormats() {
		String[] formats = new String[3];
		formats[0] = "GML";
		formats[1] = "XML";
		formats[2] = "BGML";
		return formats;
	}

	/***********************************************************************************************
	 * <accept> Returns true if the file is a gml file and the parser has to parse it
	 * 
	 * @param URI uri
	 * @return boolean
	 **********************************************************************************************/
	public boolean accept(URI uri) {
		if ((uri.getPath().toUpperCase().endsWith("GML"))
				|| (uri.getPath().toUpperCase().endsWith("XML"))
				|| (uri.getPath().toUpperCase().endsWith("BGML"))) {
			return true;
		}
		return false;
	}

	/***********************************************************************************************
	 * <createInputStream> Creates an InputStream from a file.
	 * 
	 * @param File file
	 * @return FileInputStream
	 **********************************************************************************************/
	protected InputStream createInputStream(File file) throws FileNotFoundException {
		return new FileInputStream(file);
	}

	/***********************************************************************************************
	 * <initParse> Starts to parse the file.
	 * 
	 * @throws XmlStreamException
	 * @throws GPEXmlEmptyFileException
	 **********************************************************************************************/
	protected void initParse() throws GPEXmlEmptyFileException, XmlStreamException {
		// First, it gets the file parser.
		fileParser = getParser();
		// If the file is empty
		if (getParser().getEventType() == IXmlStreamReader.END_DOCUMENT) {
			throw new GPEXmlEmptyFileException();
		}
		try{
			getProfile().getFeatureCollectionBinding().
			parse(getParser(), this);		
		} catch (IOException e) {
			getErrorHandler().addError(e);
		}	
	}

	public IXmlStreamReader getFileParser() {
		return fileParser;
	}

	/***********************************************************************************************
	 * <getNext> Gets the next tag or text token from the file. without white spaces.
	 **********************************************************************************************/
	public void getNext() {
		// Get next tag --> next():Method from KXML library to get next tag
		try {
			fileParser.next();
			if ((fileParser.getEventType() == IXmlStreamReader.CHARACTERS)
					&& (fileParser.isWhitespace())) {
				getNext();
			}
		} catch (XmlStreamException e) {
			// TODO Bloque catch generado automáticamente
			System.out.println("Error en XmlPullParser al intentar obtener la siguiente etiqueta: "
					+ e.getMessage());
		} catch (IOException e) {
			// TODO Bloque catch generado automáticamente
			System.out.println("Error al leer la siguiente etiqueta en XmlPullParser: "
					+ e.getMessage());
		}
	}

	/**
	 * @param profile the profile to set
	 */
	protected void setProfile(IBindingProfile profile) {
		this.profile = profile;
	}

	/**
	 * @return the profile
	 */
	public IBindingProfile getProfile() {
		return profile;
	}
}
