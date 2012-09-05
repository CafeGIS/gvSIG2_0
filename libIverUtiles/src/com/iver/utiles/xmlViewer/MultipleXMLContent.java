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
package com.iver.utiles.xmlViewer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;


/**
 * XMLContent al que se le pueden añadir varios ficheros XML en forma de String
 *
 * @author Fernando González Cortés
 */
public class MultipleXMLContent implements XMLContent {
    private ContentHandler handler;
    private ArrayList xmls = new ArrayList();

    /**
     * Añade un fichero XML al modelo
     *
     * @param xmlContent String con el contenido xml
     */
    public void addXML(String xmlContent) {
        xmls.add(xmlContent);
    }

    /**
     * @see com.iver.utiles.xmlViewer.XMLContent#setContentHandler(org.xml.sax.ContentHandler)
     */
    public void setContentHandler(ContentHandler handler) {
        this.handler = handler;
    }

    /**
     * Genera un evento de startDocument, luego genera todos los eventos de los
     * XML que han sido añadidos a la clase filtrando los startDocument y
     * endDocument de cada uno de  ellos y luego genera un endDocument
     *
     * @see com.iver.utiles.xmlViewer.XMLContent#parse()
     */
    public void parse() throws SAXException {
   	XMLReader reader;

        //Empieza el documento
        handler.startDocument();

        for (int i = 0; i < xmls.size(); i++) {
            String text = (String) xmls.get(i);

            //Handler que filtra los eventos de startDocument y end Document
            FilterHandler filter = new FilterHandler();
            filter.setHandler(handler);

            //Generamos los eventos del XML i-ésimo
            reader = XMLReaderFactory.createXMLReader();
            reader.setFeature("http://xml.org/sax/features/namespaces", false);
            reader.setContentHandler(filter);

            if (text == null) {
                continue;
            }

            try {
                reader.parse(new InputSource(
                        new ByteArrayInputStream(text.getBytes())));
            } catch (IOException e) {
                //Una IO exception en un array de bytes???
            }
        }

        //Finalizamos el documento
        handler.endDocument();
    }

    /**
     * Manejador que delega en otro manejador todos los eventos excepto el
     * startDocument y el endDocument. "Filtra" los eventos de startDocument y
     * endDocument
     *
     * @author Fernando González Cortés
     */
    public class FilterHandler implements ContentHandler {
        private ContentHandler handler;

        /**
         * Establece el handler al que se le van a filtrar los eventos de
         * comienzo y fin de documento
         *
         * @param handler The handler to set.
         */
        public void setHandler(ContentHandler handler) {
            this.handler = handler;
        }

        /**
         * DOCUMENT ME!
         *
         * @param arg0
         * @param arg1
         * @param arg2
         *
         * @throws SAXException
         */
        public void characters(char[] arg0, int arg1, int arg2)
            throws SAXException {
            handler.characters(arg0, arg1, arg2);
        }

        /**
         * DOCUMENT ME!
         *
         * @throws SAXException
         */
        public void endDocument() throws SAXException {
        }

        /**
         * DOCUMENT ME!
         *
         * @param arg0
         * @param arg1
         * @param arg2
         *
         * @throws SAXException
         */
        public void endElement(String arg0, String arg1, String arg2)
            throws SAXException {
            handler.endElement(arg0, arg1, arg2);
        }

        /**
         * DOCUMENT ME!
         *
         * @param arg0
         *
         * @throws SAXException
         */
        public void endPrefixMapping(String arg0) throws SAXException {
            handler.endPrefixMapping(arg0);
        }

        /**
         * DOCUMENT ME!
         *
         * @param arg0
         * @param arg1
         * @param arg2
         *
         * @throws SAXException
         */
        public void ignorableWhitespace(char[] arg0, int arg1, int arg2)
            throws SAXException {
            handler.ignorableWhitespace(arg0, arg1, arg2);
        }

        /**
         * DOCUMENT ME!
         *
         * @param arg0
         * @param arg1
         *
         * @throws SAXException
         */
        public void processingInstruction(String arg0, String arg1)
            throws SAXException {
            handler.processingInstruction(arg0, arg1);
        }

        /**
         * DOCUMENT ME!
         *
         * @param arg0
         */
        public void setDocumentLocator(Locator arg0) {
            handler.setDocumentLocator(arg0);
        }

        /**
         * DOCUMENT ME!
         *
         * @param arg0
         *
         * @throws SAXException
         */
        public void skippedEntity(String arg0) throws SAXException {
            handler.skippedEntity(arg0);
        }

        /**
         * DOCUMENT ME!
         *
         * @throws SAXException
         */
        public void startDocument() throws SAXException {
        }

        /**
         * DOCUMENT ME!
         *
         * @param arg0
         * @param arg1
         * @param arg2
         * @param arg3
         *
         * @throws SAXException
         */
        public void startElement(String arg0, String arg1, String arg2,
            Attributes arg3) throws SAXException {
            handler.startElement(arg0, arg1, arg2, arg3);
        }

        /**
         * DOCUMENT ME!
         *
         * @param arg0
         * @param arg1
         *
         * @throws SAXException
         */
        public void startPrefixMapping(String arg0, String arg1)
            throws SAXException {
            handler.startPrefixMapping(arg0, arg1);
        }
    }
}
