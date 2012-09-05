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

import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;


/**
 * Clase que sirve de XMLContent al control XMLViewer a partir de un string con
 * un fichero XML
 *
 * @author Fernando González Cortés
 */
public class TextXMLContent implements XMLContent {
    private String text;
    private ContentHandler handler;

    /**
     * Crea un nuevo TextXMLContent.
     *
     * @param text Texto con el XML
     */
    public TextXMLContent(String text) {
        this.text = text;
    }

    /**
     * @see com.iver.utiles.xmlViewer.XMLContent#setContentHandler(org.xml.sax.ContentHandler)
     */
    public void setContentHandler(ContentHandler handler) {
        this.handler = handler;
    }

    /**
     * @see com.iver.utiles.xmlViewer.XMLContent#parse()
     */
    public void parse() throws SAXException {
    	XMLReader reader = XMLReaderFactory.createXMLReader();
    	reader.setFeature("http://xml.org/sax/features/namespaces", false);
        reader.setContentHandler(handler);

        if (text == null) {
            text = "<?xml version='1.0'?>";
        }

        try {
            reader.parse(new InputSource(
                    new ByteArrayInputStream(text.getBytes())));
        } catch (IOException e) {
            //Una IO exception en un array de bytes???
        }
    }
    
    public String toString(){
    	return text;
    }
}
