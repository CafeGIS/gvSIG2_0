package org.gvsig.gpe.xml.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

import org.gvsig.gpe.parser.GPEParser;
import org.gvsig.gpe.xml.XmlProperties;
import org.gvsig.gpe.xml.exceptions.GPEXmlEmptyFileException;
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
 * $Id: GPEXmlParser.java 193 2007-11-26 08:58:56Z jpiera $
 * $Log$
 * Revision 1.11  2007/06/22 12:22:40  jorpiell
 * The typeNotFoundException has been deleted. It never was thrown
 *
 * Revision 1.10  2007/06/14 13:50:05  jorpiell
 * The schema jar name has been changed
 *
 * Revision 1.9  2007/06/07 14:53:30  jorpiell
 * Add the schema support
 *
 * Revision 1.8  2007/05/09 06:54:25  jorpiell
 * Change the File by URI
 *
 * Revision 1.7  2007/05/07 07:07:04  jorpiell
 * Add a constructor with the name and the description fields
 *
 * Revision 1.6  2007/04/19 12:01:55  jorpiell
 * Updated the getEncoding method
 *
 * Revision 1.5  2007/04/19 11:56:03  csanchez
 * Actualizacion protoripo libGPE
 *
 * Revision 1.4  2007/04/19 07:30:40  jorpiell
 * Add the add methods to teh contenhandler and change the register mode
 *
 * Revision 1.3  2007/04/18 12:54:45  csanchez
 * Actualizacion protoripo libGPE
 *
 * Revision 1.2  2007/04/12 11:47:15  jorpiell
 * Add a getParser method
 *
 * Revision 1.1  2007/04/12 10:23:41  jorpiell
 * Add some writers and the GPEXml parser
 *
 *
 */
/**
 * This class can be implemented by all the classes that implements a GPE driver based on the XML
 * format.
 * 
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public abstract class GPEXmlParser extends GPEParser {
    private IXmlStreamReader parser = null;

    public GPEXmlParser() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.gvsig.gpe.GPEParser#parseStream()
     */
    protected void parseStream() {
        try {
            parser = getXmlReader();            
            //Check the XMLSchema
             if (getGpeManager().getBooleanProperty(XmlProperties.XML_SCHEMA_VALIDATED) == true){
            	 
             }
            initParse();

        } // EL PARSER LANZARÁ UN EVENTO AL ERRORHANDLER SEGÚN EL ERROR RECOGIDO
        catch (IOException e) {
            getErrorHandler().addError(e);
        } catch (GPEXmlEmptyFileException e) {
            getErrorHandler().addError(e);
        }
    }

    private IXmlStreamReader getXmlReader() throws XmlStreamException {
        PushbackInputStream pushBackStream = new PushbackInputStream(getInputStream());

        IXmlStreamReader reader;
        try {
            int firstByte = pushBackStream.read();
            pushBackStream.unread(firstByte);
            
            if (0x01 == firstByte) {
                // binary
                reader = GPEXmlParserFactory.getParser("text/x-bxml", pushBackStream);
            } else {
                // text
                reader = GPEXmlParserFactory.getParser("text/xml", pushBackStream);
            }
        } catch (IOException e) {
            throw new XmlStreamException(e);
        }
        return reader;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.gvsig.gpe.GPEParser#parseURI()
     */
    protected void parseURI() {
        try {
            // Creates an input stream to read the file
            File file = new File(getMainFile());
            InputStream inputStream = createInputStream(file);
            setInputStream(inputStream);
            parseStream();
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: Fichero " + getMainFile().getPath() + " no encontrado");
            getErrorHandler().addError(e);
        } catch (IOException e) {
            System.out.println("ERROR: No se puede leer/escribir la codificación del fichero: "
                    + e.getMessage());
            getErrorHandler().addError(e);
        }
    }

    /**
     * Creates an input stream from a file.
     * 
     * @param file
     * @return InputStream
     * @throws FileNotFoundException
     */
    protected abstract InputStream createInputStream(File file) throws FileNotFoundException;

    /**
     * This method start the parse process. It is called after the XML parser is initialized
     */
    protected abstract void initParse() throws GPEXmlEmptyFileException, XmlStreamException;

    /**
     * @return the parser
     */
    protected IXmlStreamReader getParser() {
        return parser;
    }

}
