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
package com.iver.cit.gvsig.project;

import java.util.EmptyStackException;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import com.iver.cit.gvsig.project.documents.view.legend.gui.VectorialUniqueValue;
import com.iver.utiles.XMLEntity;


/**
 * DOCUMENT ME!
 *
 * @author Fernando González Cortés
 */
public class XMLEntityXMLHandler implements ContentHandler {
    private static final Logger logger = LoggerFactory
    .getLogger(XMLEntityXMLHandler.class);
	private XMLEntity lastEntity;
    private Stack entities = new Stack();

    /**
     * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
     */
    public void setDocumentLocator(Locator arg0) {
    }

    /**
     * @see org.xml.sax.ContentHandler#startDocument()
     */
    public void startDocument() throws SAXException {
    }

    /**
     * @see org.xml.sax.ContentHandler#endDocument()
     */
    public void endDocument() throws SAXException {
    }

    /**
     * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String,
     *      java.lang.String)
     */
    public void startPrefixMapping(String arg0, String arg1)
        throws SAXException {
    }

    /**
     * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
     */
    public void endPrefixMapping(String arg0) throws SAXException {
    }

    /**
     * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
     *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement(String namespaceURI, String localName,
        String qName, Attributes atts) throws SAXException {
        
        XMLEntity aux = new XMLEntity();
        entities.push(aux);

        for (int i = 0; i < atts.getLength(); i++) {
            String name = atts.getLocalName(i);
            String value = atts.getValue(i);

			aux.putProperty(name, value);
        }
    }

    /**
     * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public void endElement(String namespaceURI, String localName, String qName)
        throws SAXException {
        if (localName.equals("entity")){
			XMLEntity aux = (XMLEntity) entities.pop();
			try{
				XMLEntity last = (XMLEntity) entities.peek();
				last.addChild(aux);
			}catch (EmptyStackException e) {
			}
			lastEntity = aux;
        }
    }

    /**
     * @see org.xml.sax.ContentHandler#characters(char[], int, int)
     */
    public void characters(char[] arg0, int arg1, int arg2)
        throws SAXException {
    }

    /**
     * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
     */
    public void ignorableWhitespace(char[] arg0, int arg1, int arg2)
        throws SAXException {
    }

    /**
     * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String,
     *      java.lang.String)
     */
    public void processingInstruction(String arg0, String arg1)
        throws SAXException {
    }

    /**
     * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
     */
    public void skippedEntity(String arg0) throws SAXException {
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public XMLEntity getXMLEntity() {
        return lastEntity;
    }
}
