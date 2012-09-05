package org.gvsig.gpe.xml.stream.stax;

import java.io.FileWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.gvsig.gpe.xml.stream.EventType;
import org.gvsig.gpe.xml.stream.IXmlStreamWriter;
import org.gvsig.gpe.xml.stream.XmlStreamException;

import com.bea.xml.stream.XMLWriterBase;

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

/**
 * An XML stream writer that uses a StAX writer under the hood.
 */
public class StaxXmlStreamWriter implements IXmlStreamWriter {

    private XMLStreamWriter writer;

    private EventType lastTagEvent;
    private EventType lastEvent;

    private long valueLength;

    private long writtenValueLength;

    /**
     * nsuri for the attribute currently being processed. null if an attribute is not being
     * processed
     */
    private String attributeNamespace;

    /**
     * local name for the attribute currently being processed. null if an attribute is not being
     * processed
     */
    private String attributeLocalName;

    private StringBuffer attributeValue = new StringBuffer();

    private StringBuffer valueBuff = new StringBuffer();

    public StaxXmlStreamWriter(final OutputStream out) throws XmlStreamException {
        final XMLOutputFactory staxFactory = XMLOutputFactory.newInstance();
        //TODO: Change the classloader of gvSIG to use SPI
        //try {
        	this.writer = new XMLWriterBase(new OutputStreamWriter(out));          
        //} catch (XMLStreamException e) {
        //   throw new XmlStreamException(e);
        //}
        lastEvent = EventType.NONE;
    }

    /**
     * @see IXmlStreamWriter#close()
     */
    public void close() throws XmlStreamException {
        if (writer != null) {
            try {
                writer.close();
            } catch (XMLStreamException e) {
                throw new XmlStreamException(e);
            }
            writer = null;
        }
    }

    /**
     * @see IXmlStreamWriter#isOpen()
     */
    public boolean isOpen() {
        return writer != null;
    }

    /**
     * @throws XmlStreamException 
     * @see IXmlStreamWriter#setDefaultNamespace(java.lang.String)
     */
    public void setDefaultNamespace(String defaultNamespaceUri) throws XmlStreamException {
    	  try {
  			writer.setDefaultNamespace(defaultNamespaceUri);
  		} catch (XMLStreamException e) {
  			 throw new XmlStreamException(e);
  		} 
    }

    /**
     * @throws XmlStreamException 
     * @see IXmlStreamWriter#setPrefix(java.lang.String, java.lang.String)
     */
    public void setPrefix(String prefix, String namespaceUri) throws XmlStreamException {
        try {
			writer.setPrefix(prefix, namespaceUri);
		} catch (XMLStreamException e) {
			 throw new XmlStreamException(e);
		}    	
    }

    /**
     * @see IXmlStreamWriter#setSchemaLocation(java.lang.String, java.lang.String)
     */
    public void setSchemaLocation(String namespaceUri, String schemaLocationUri) {
       	throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * @see IXmlStreamWriter#flush()
     */
    public void flush() throws XmlStreamException {
        try {
            writer.flush();
        } catch (XMLStreamException e) {
            throw new XmlStreamException(e);
        }
    }

    /**
     * @see IXmlStreamWriter#getLastEvent()
     */
    public EventType getLastEvent() {
        return lastEvent;
    }

    /**
     * @see IXmlStreamWriter#getLastTagEvent()
     */
    public EventType getLastTagEvent() {
        return lastTagEvent;
    }

    /**
     * @see IXmlStreamWriter#getTagDeep()
     */
    public int getTagDeep() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * @see IXmlStreamWriter#getValueLength()
     */
    public long getValueLength() {
        return valueLength;
    }

    /**
     * @see IXmlStreamWriter#getWrittenValueCount()
     */
    public long getWrittenValueCount() {
        return writtenValueLength;
    }

    /**
     * @see IXmlStreamWriter#startArray(org.gvsig.gpe.xml.stream.EventType, int)
     */
    public void startArray(EventType valueType, int arrayLength) throws XmlStreamException {
        valueLength = arrayLength;
        lastEvent = valueType;
        writtenValueLength = 0;
    }

    /**
     * @see IXmlStreamWriter#endArray()
     */
    public void endArray() throws XmlStreamException {
        // nothing to do
    }

    /**
     * @see IXmlStreamWriter#writeComment(java.lang.String)
     */
    public void writeComment(String commentContent) throws XmlStreamException {
        try {
            writer.writeComment(commentContent);
        } catch (XMLStreamException e) {
            throw new XmlStreamException(e);
        }
        lastEvent = EventType.COMMENT;
    }

    /**
     * @see IXmlStreamWriter#writeEndAttributes()
     */
    public void writeEndAttributes() throws XmlStreamException {
        flushAttribute();
        lastEvent = EventType.ATTRIBUTES_END;
    }

    /**
     * @see IXmlStreamWriter#writeEndDocument()
     */
    public void writeEndDocument() throws XmlStreamException {
        try {
            writer.writeEndDocument();
        } catch (XMLStreamException e) {
            throw new XmlStreamException(e);
        }
        lastEvent = EventType.END_DOCUMENT;
    }

    /**
     * @see IXmlStreamWriter#writeEndElement()
     */
    public void writeEndElement() throws XmlStreamException {
        try {
            writer.writeEndElement();
        } catch (XMLStreamException e) {
            throw new XmlStreamException(e);
        }
        lastEvent = lastTagEvent = EventType.END_ELEMENT;
    }

    /**
     * @see IXmlStreamWriter#writeStartAttribute(java.lang.String, java.lang.String)
     */
    public void writeStartAttribute(String namespaceUri, String localName)
            throws XmlStreamException {
        // do we already have an attribute in course?
        flushAttribute();

        attributeNamespace = namespaceUri;
        attributeLocalName = localName;
        attributeValue.setLength(0);
        lastEvent = EventType.ATTRIBUTE;
    }

    private void flushAttribute() throws XmlStreamException {
        if (attributeLocalName != null) {
            String value = attributeValue.toString();
            try {
                writer.writeAttribute(attributeNamespace, attributeLocalName, value);
            } catch (XMLStreamException e) {
                throw new XmlStreamException(e);
            }
            attributeNamespace = null;
            attributeLocalName = null;
            attributeValue.setLength(0);
        }
    }

    /**
     * @see IXmlStreamWriter#writeStartAttribute(javax.xml.namespace.QName)
     */
    public void writeStartAttribute(QName qname) throws XmlStreamException {
        writeStartAttribute(qname.getNamespaceURI(), qname.getLocalPart());
    }

    /**
     * @see IXmlStreamWriter#writeStartDocument()
     */
    public void writeStartDocument() throws XmlStreamException {
        try {
            writer.writeStartDocument();
        } catch (XMLStreamException e) {
            throw new XmlStreamException(e);
        }
        lastEvent = EventType.START_DOCUMENT;
    }

    /**
     * @see IXmlStreamWriter#writeStartElement(java.lang.String, java.lang.String)
     */
    public void writeStartElement(String namespaceUri, String localName) throws XmlStreamException {
        try {
            writer.writeStartElement(namespaceUri, localName);
        } catch (XMLStreamException e) {
            throw new XmlStreamException(e);
        }
        attributeNamespace = attributeLocalName = null;
        lastEvent = lastTagEvent = EventType.START_ELEMENT;
    }

    /**
     * @see IXmlStreamWriter#writeStartElement(javax.xml.namespace.QName)
     */
    public void writeStartElement(QName qname) throws XmlStreamException {
        writeStartElement(qname.getNamespaceURI(), qname.getLocalPart());
    }

    /**
     * All writeValue methods delegate to this one, which will call a writeCharacters if its an
     * element's value or an attribute if its an attribute value.
     * <p>
     * Whether the value corresponds to an attribute or an element's one is determined by the
     * nullity of the attributeLocalName field.
     * </p>
     * 
     * @param value
     * @throws XmlStreamException
     */
    private void characters(String value) throws XmlStreamException {
        if (attributeLocalName == null) {
            // an element's value
            try {
                writer.writeCharacters(value);
            } catch (XMLStreamException e) {
                throw new XmlStreamException(e);
            }
        } else {
            // got a component of the current attribute value
            attributeValue.append(value);
        }
    }

    /**
     * @see IXmlStreamWriter#writeValue(java.lang.String)
     */
    public void writeValue(String value) throws XmlStreamException {
        characters(value);
        writtenValueLength++;
        lastEvent = EventType.VALUE_STRING;
    }

    /**
     * @see IXmlStreamWriter#writeValue(char[], int, int)
     */
    public void writeValue(char[] chars, int offset, int length) throws XmlStreamException {
        characters(new String(chars, offset, length));
        writtenValueLength++;
        lastEvent = EventType.VALUE_STRING;
    }

    /**
     * @see IXmlStreamWriter#writeValue(int)
     */
    public void writeValue(int value) throws XmlStreamException {
        characters(String.valueOf(value));
        writtenValueLength++;
        lastEvent = EventType.VALUE_INT;
    }

    /**
     * @see IXmlStreamWriter#writeValue(long)
     */
    public void writeValue(long value) throws XmlStreamException {
        characters(String.valueOf(value));
        writtenValueLength++;
        lastEvent = EventType.VALUE_LONG;
    }

    /**
     * @see IXmlStreamWriter#writeValue(float)
     */
    public void writeValue(float value) throws XmlStreamException {
        characters(String.valueOf(value));
        writtenValueLength++;
        lastEvent = EventType.VALUE_FLOAT;
    }

    /**
     * @see IXmlStreamWriter#writeValue(double)
     */
    public void writeValue(double value) throws XmlStreamException {
        characters(String.valueOf(value));
        writtenValueLength++;
        lastEvent = EventType.VALUE_DOUBLE;
    }

    /**
     * @see IXmlStreamWriter#writeValue(boolean)
     */
    public void writeValue(boolean value) throws XmlStreamException {
        characters(String.valueOf(value));
        writtenValueLength++;
        lastEvent = EventType.VALUE_BOOL;
    }

    /**
     * @see IXmlStreamWriter#writeValue(boolean[], int, int)
     */
    public void writeValue(boolean[] value, int offset, int length) throws XmlStreamException {
        valueBuff.setLength(0);
        for (int i = 0; i < length; i++) {
            valueBuff.append(value[offset + i]);
            if (i < length - 1) {
                valueBuff.append(' ');
            }
        }
        characters(valueBuff.toString());
        writtenValueLength += length;
        lastEvent = EventType.VALUE_BOOL;
    }

    /**
     * @see IXmlStreamWriter#writeValue(int[], int, int)
     */
    public void writeValue(int[] value, int offset, int length) throws XmlStreamException {
        valueBuff.setLength(0);
        for (int i = 0; i < length; i++) {
            valueBuff.append(value[offset + i]);
            if (i < length - 1) {
                valueBuff.append(' ');
            }
        }
        characters(valueBuff.toString());
        writtenValueLength += length;
        lastEvent = EventType.VALUE_INT;
    }

    /**
     * @see IXmlStreamWriter#writeValue(long[], int, int)
     */
    public void writeValue(long[] value, int offset, int length) throws XmlStreamException {
        valueBuff.setLength(0);
        for (int i = 0; i < length; i++) {
            valueBuff.append(value[offset + i]);
            if (i < length - 1) {
                valueBuff.append(' ');
            }
        }
        characters(valueBuff.toString());
        writtenValueLength += length;
        lastEvent = EventType.VALUE_LONG;
    }

    /**
     * @see IXmlStreamWriter#writeValue(float[], int, int)
     */
    public void writeValue(float[] value, int offset, int length) throws XmlStreamException {
        valueBuff.setLength(0);
        for (int i = 0; i < length; i++) {
            valueBuff.append(value[offset + i]);
            if (i < length - 1) {
                valueBuff.append(' ');
            }
        }
        characters(valueBuff.toString());
        writtenValueLength += length;
        lastEvent = EventType.VALUE_FLOAT;
    }

    /**
     * @see IXmlStreamWriter#writeValue(double[], int, int)
     */
    public void writeValue(double[] value, int offset, int length) throws XmlStreamException {
        valueBuff.setLength(0);
        for (int i = 0; i < length; i++) {
            valueBuff.append(value[offset + i]);
            if (i < length - 1) {
                valueBuff.append(',');
            }
        }
        valueBuff.append(' ');
        characters(valueBuff.toString());
        writtenValueLength += length;
        lastEvent = EventType.VALUE_DOUBLE;
    }

}
