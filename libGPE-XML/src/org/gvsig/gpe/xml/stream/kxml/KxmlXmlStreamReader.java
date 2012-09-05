package org.gvsig.gpe.xml.stream.kxml;

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
 * $Id: XmlPullStreamReader.java 19593 2008-03-12 17:23:30Z groldan $
 * $Log$
 */

import java.io.IOException;
import java.io.InputStream;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import org.gvsig.gpe.xml.stream.IXmlStreamReader;
import org.gvsig.gpe.xml.stream.XmlStreamException;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * An {@link IXmlStreamReader} adapter for xml textual parsing using a
 * {@link XmlPullParser pull parser}.
 * 
 * @author Gabriel Roldan (TOPP)
 * @version $Id: XmlPullStreamReader.java 19593 2008-03-12 17:23:30Z groldan $
 */
public class KxmlXmlStreamReader implements IXmlStreamReader {

    private XmlPullParser parser;
	/**
     * Default constructor for this parser
     * @param in 
     * @throws XmlStreamException 
     */
    public KxmlXmlStreamReader(InputStream in) throws XmlStreamException {
			setInput(in);
    }

    /**
     * Sets the internal xmlpull parser input stream
     * 
     * @param inputStream
     * @throws XmlStreamException
     * @see org.gvsig.gpe.xml.stream.IXmlStreamReader#setInput(java.io.InputStream)
     */
    public void setInput(final InputStream inputStream) throws XmlStreamException {
        try {
            parser = new KXmlParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            // let the parser inferr the encoding
            parser.setInput(inputStream, null);
        } catch (XmlPullParserException e) {
            throw new XmlStreamException(e);
        }
    }

    public int getAttributeCount() throws XmlStreamException {
        return parser.getAttributeCount();
    }

    public QName getAttributeName(int i) throws XmlStreamException {
        return new QName(parser.getNamespace(), parser.getAttributeName(i));
    }

    public String getAttributeValue(int i) throws XmlStreamException {
        return parser.getAttributeValue(i);
    }

    public int getEventType() throws XmlStreamException {
        // TODO: improve this mapping
        int xmlPullEventType;
        try {
            xmlPullEventType = parser.getEventType();
        } catch (XmlPullParserException e) {
            throw new XmlStreamException(e);
        }
        return pullEventToGpeEventType(xmlPullEventType);
    }

    /**
     * @return
     * @see org.gvsig.gpe.xml.stream.IXmlStreamReader#getName()
     */
    public QName getName() throws XmlStreamException {
    	int eventType;
		try {
			eventType = parser.getEventType();
			if (eventType != XMLStreamReader.START_ELEMENT
	                 && eventType  != XMLStreamReader.END_ELEMENT
	                 && eventType  != XMLStreamReader.PROCESSING_INSTRUCTION
	                 && eventType  != XMLStreamReader.CHARACTERS) {
	             return null;
	        }
			String name = parser.getName();
			if (name != null){
    			name = name.substring(name.indexOf(":") + 1, name.length());
    			parser.getNamespace();
    			return new QName(parser.getNamespace(), name);
    		}
		} catch (XmlPullParserException e) {
			return null;
		}
		return null;     	
    }

    public String getText() throws XmlStreamException {
        return parser.getText();
    }

    public boolean isWhitespace() throws XmlStreamException {
        try {
            return parser.isWhitespace();
        } catch (XmlPullParserException e) {
            throw new XmlStreamException(e);
        }
    }

    public int next() throws XmlStreamException {
        int xmlPullEventType;
        if (getEventType()==XMLStreamReader.END_DOCUMENT)
        	throw new XmlStreamException("End Document Exception! \n There aren't more items to get.");
        try {
            xmlPullEventType = parser.next();
        } catch (XmlPullParserException e) {
            throw new XmlStreamException(e);
        } catch (IOException e) {
            throw new XmlStreamException(e);
        }
        return pullEventToGpeEventType(xmlPullEventType);
    }

    public int nextTag() throws XmlStreamException {
        int xmlPullEventType;
        if (getEventType()==XMLStreamReader.END_DOCUMENT)
        	throw new XmlStreamException("End Document Exception! \n There aren't more tags to get.");
        try {
            xmlPullEventType = parser.nextTag();
        } catch (XmlPullParserException e) {
            throw new XmlStreamException(e);
        } catch (IOException e) {
            throw new XmlStreamException(e);
        }
        return pullEventToGpeEventType(xmlPullEventType);
    }

    private int pullEventToGpeEventType(int xmlPullEventType) {
        switch (xmlPullEventType) {
        case XmlPullParser.START_DOCUMENT:
            return IXmlStreamReader.START_DOCUMENT;
        case XmlPullParser.END_DOCUMENT:
            return IXmlStreamReader.END_DOCUMENT;
        case XmlPullParser.START_TAG:
            return IXmlStreamReader.START_ELEMENT;
        case XmlPullParser.END_TAG:
            return IXmlStreamReader.END_ELEMENT;
        case XmlPullParser.TEXT:
            return IXmlStreamReader.CHARACTERS;
        case XmlPullParser.CDSECT:
            return IXmlStreamReader.CDATA;
        case XmlPullParser.ENTITY_REF:
            return IXmlStreamReader.ENTITY_REFERENCE;
        case XmlPullParser.IGNORABLE_WHITESPACE:
            return IXmlStreamReader.SPACE;
        case XmlPullParser.PROCESSING_INSTRUCTION:
            return IXmlStreamReader.PROCESSING_INSTRUCTION;
        case XmlPullParser.COMMENT:
            return IXmlStreamReader.COMMENT;
        case XmlPullParser.DOCDECL:
            return IXmlStreamReader.DTD;
        default:
            throw new IllegalStateException("Unknown tag type, this should't happen!: "
                    + xmlPullEventType);
        }
    }

}
