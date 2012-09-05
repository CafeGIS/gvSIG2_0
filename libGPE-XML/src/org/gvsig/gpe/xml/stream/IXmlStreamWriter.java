/* gvSIG. Sistem a de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2007 Generalitat Valenciana.
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
 *      +34 9638 62 495
 *      gvsig@gva.es
 *      www.gvsig.gva.es
 */
package org.gvsig.gpe.xml.stream;

import java.io.IOException;

import javax.xml.namespace.QName;

/**
 * IXmlStreamWriter defines an "XML push" like streaming API for writing XML documents encoded as
 * per the OGC Binary XML Best Practices document.
 * <p>
 * This interface does not defines setter methods to control encoding behavior such as byte order to
 * use or character encoding. A IXmlStreamWriter instance is meant to be already configured with the
 * desired encoding options when returned from the {@link BxmlOutputFactory}.
 * </p>
 * <p>
 * Sample usage:
 * 
 * <pre>
 * <code>
 * BxmlOutputFactory factory = BxmlFactoryFinder.newOutputFactory();
 * IXmlStreamWriter writer = factory.createSerializer(inputStream);
 * 
 * writer.setSchemaLocation("http://www.example.com", "http://www.example.com/schema.xsd");
 * 
 * writer.writeStartDocument();
 * 
 * writer.setPrefix("ex", "http://www.example.com")
 * writer.setDefaultNamespace("http://www.anotherexample.com");
 * 
 * writer.writeStartElement("", "root");
 * 
 * writer.writeStartElement("http://www.example.com", "child");
 * 
 * //child attributes list...
 * writer.writeStartAttribute("http://www.example.com", "att");
 * writer.writeValue("attValue");
 * writer.writeStartAttribute("http://www.example.com", "emptyAtt");
 * writer.writeEndAttributes();
 * 
 * //child value as a purely streamed int array
 * writer.startArray(EventType.VALUE_INT, 10);
 * for(int i = 0; i < 10; i++){
 *   writer.writeValue(i);
 * }
 * writer.endArray();
 * 
 * writer.writeComment("interleaved value type follow");
 * 
 * writer.writeValue("10 11 12 13");
 * 
 * writer.writeEndElement(); // child
 * writer.writeEndElement(); // root
 * writer.writeEndDocument();
 * </code>
 * </pre>
 * 
 * </p>
 * 
 * @author Gabriel Roldan (TOPP)
 * @version $Id: IXmlStreamWriter.java 21945 2008-06-30 14:04:53Z jpiera $
 * @since 1.0
 */
public interface IXmlStreamWriter {

    /**
     * Returns a safe copy of the encoding options this writer is settled up with.
     * <p>
     * Modifications to the returned object state does not affect this writer behaviour.
     * </p>
     * 
     * @return an object representing the set of encoding options this writer operates with
     */
    //public EncodingOptions getEncodingOptions();

    /**
     * Returns the event corresponding to the last write call.
     * <p>
     * For example, if the last write call was
     * {@link #writeStartElement(String, String) writeStartElement} it shall return
     * {@link EventType#START_ELEMENT START_ELEMENT}, if it was
     * {@link #writeStartAttribute(String, String) writeStartAttribute},
     * {@link EventType#ATTRIBUTE ATTRIBUTE}, etc.
     * </p>
     * 
     * @post {$return != null}
     * @return {@link EventType#NONE} if writing has not yet started (ie, writeStartDocument has not
     *         been called), the event for the last write operation otherwise.
     */
    public EventType getLastEvent();

    /**
     * @return {@code null} if no tag event has been written yet, START_ELEMENT or END_ELEMENT
     *         otherwise, depending on which of them was lately called
     */
    public EventType getLastTagEvent();

    /**
     * @post {$return >= 0}
     * @return how deep is the current element tree
     */
    public int getTagDeep();

    /**
     * Closes this stream writer and releases any system resources associated with it, including the
     * underlying output stream or any other output source it may be operating upon.
     * <p>
     * A call to this method implies a flush of any possible buffered wtrite before closing the
     * stream.
     * </p>
     * <p>
     * The first call to this method closes and releases resources. Subsequent calls shall take no
     * effect and return quitely. After the first call to this method any non query method (defined
     * as metadata retrieval methods in the class' javadoc) shall fail with an io exception.
     * </p>
     * 
     * @throws IOException if an I/O error occurs.
     */
    public void close() throws IOException;

    /**
     * Returns whether this stream writer and its underlying output source are open and able to
     * receive more write calls.
     * <p>
     * A {@code IXmlStreamWriter} should be open since its creation until {@link #close()} is
     * explicitly called by client code.
     * </p>
     * 
     * @return {@code true} if this stream is still open, {@code false} otherwise
     */
    public boolean isOpen();

    /**
     * Write any cached data to the underlying output mechanism.
     * 
     * @throws IOException
     */
    public void flush() throws IOException;

    /**
     * @pre {getLastEvent() == NONE}
     * @pre {namespaceUri != null AND schemaLocationUri != null}
     * @pre {namespaceUri != ""}
     * @param namespaceUri
     * @param schemaLocationUri
     */
    public void setSchemaLocation(String namespaceUri, String schemaLocationUri);

    /**
     * Initiates the encoding of an XML formatted document in the BXML encoding.
     * <p>
     * This should be the first non query method to call after a stream writer is created. The
     * implementation will use the call to this method to write down the document header and xml
     * declaration.
     * </p>
     * 
     * @pre {getLastEvent() == NONE}
     * @post {getLastEvent() == START_DOCUMENT}
     * @throws IOException
     */
    public void writeStartDocument() throws IOException;

    /**
     * Finishes up encoding a BXML document.
     * <p>
     * Implementations shall use this method to write down the document's trailer token.
     * </p>
     * 
     * @pre {getTagDeep() == 0}
     * @post {getLastEvent() == END_DOCUMENT}
     * @throws IOException
     */
    public void writeEndDocument() throws IOException;

    /**
     * @pre {namespaceUri != null}
     * @pre {localName != null}
     * @post {getLastEvent() == START_ELEMENT}
     * @param namespaceUri the namespace uri for the element
     * @param localName the elements non qualified (local) name
     * @throws IOException
     */
    public void writeStartElement(final String namespaceUri, final String localName)
            throws IOException;

    /**
     * @pre {qname != null}
     * @post {getLastEvent() == START_ELEMENT}
     * @param qname qualified element name. Only namespace and localName are relevant. Prefix, if
     *            present, is ignored. The currently mapped prefix for the namespace will be used in
     *            any case.
     * @throws IOException
     * @see #setPrefix(String, String)
     */
    public void writeStartElement(final QName qname) throws IOException;

    /**
     * @pre {getTagDeep() > 0}
     * @pre { ( getLastEvent().isValue() AND getWrittenValueCount() == getValueLength() ) ||
     *      getLastEvent() IN ( START_ELEMENT, END_ELEMENT, ATTRIBUTES_END, COMMENT}
     * @post {getLastEvent() == END_ELEMENT}
     * @throws IOException
     */
    public void writeEndElement() throws IOException;

    /**
     * Starts writting an xml attribute.
     * <p>
     * Writting an attribute and its value is split in two parts. This method only stand for the
     * attribute declaration. Writting an attribute value is made after calling this method and is
     * optional. The attribute value can be done with any of the {@code writeValue} methods, even
     * using various {@code writeValue} calls for the same attribute.
     * </p>
     * <p>
     * After the last element attribute has been written, {@link #writeEndAttributes()} shall be
     * called to indicate the end of the attribute list.
     * </p>
     * Sample usage:
     * 
     * <pre>
     * <code>
     * <b>writer.startAttribute("", "att1");</b>
     * writer.writeValue("value1");
     * writer.writeValue(1);
     * writer.writeValue(2);
     * writer.writeValue(3);
     * <b>writer.startAttribute("", "att2");</b>
     * writer.writeValue(1.0L);
     * ...
     * writer.writeEndAttributes();
     * </code>
     * </pre>
     * 
     * @pre {getLastEvent() IN (START_ELEMENT, ATTRIBUTE) }
     * @pre {namespaceUri != null}
     * @pre {localName != null}
     * @post {getLastEvent() == ATTRIBUTE}
     * @param namespaceUri not null
     * @param localName not null
     * @throws IOException
     * @see #writeEndAttributes()
     */
    public void writeStartAttribute(final String namespaceUri, final String localName)
            throws IOException;

    /**
     * Starts writing an xml attribute for the given qualified name.
     * 
     * @pre {qname != null}
     * @param qname a QName where to get the namespace uri and local name for the attribute
     * @throws IOException
     * @see #writeStartAttribute(String, String)
     */
    public void writeStartAttribute(final QName qname) throws IOException;

    /**
     * Indicates there are no more element attributes to encode for the current element.
     * <p>
     * This method is useful for the encoder to recognize (and write down the appropriate tokens)
     * the following potential call to some writeValue method corresponds to the element's contents
     * and not to the current attribute value.
     * </p>
     * Sample usage:
     * 
     * <pre>
     * <code>
     * writer.startElement("", "element");
     * writer.startAttribute("", "att1");
     * writer.writeValue("value1");
     * writer.startAttribute("", "att2");
     * writer.writeValue(1.0L);
     * ...
     * <b>writer.writeEndAttributes();</b>
     * writer.value("element value");
     * ...
     * </code>
     * </pre>
     * 
     * @pre {getLastTagEvent() == START_ELEMENT}
     * @post {getLastEvent() == ATTRIBUTES_END}
     * @throws IOException
     */
    public void writeEndAttributes() throws IOException;

    /**
     * @pre {getLastEvent().isValue() == true || getLastEvent() IN (START_ELEMENT, ATTRIBUTE,
     *      COMMENT )}
     * @post {getLastEvent() == VALUE_STRING}
     * @post {getValueLength() == 1}
     * @post {getWrittenValueCount() == 1}
     * @param value
     * @throws IOException
     */
    public void writeValue(final String value) throws IOException;

    /**
     * @pre {getLastEvent().isValue() == true || getLastEvent() IN (START_ELEMENT, ATTRIBUTE,
     *      COMMENT )}
     * @post {getLastEvent() == VALUE_STRING}
     * @param chars
     * @param offset
     * @param length
     * @throws IOException
     */
    public void writeValue(final char[] chars, final int offset, final int length)
            throws IOException;

    /**
     * @pre {getLastEvent().isValue() == true || getLastEvent() IN (START_ELEMENT, ATTRIBUTE,
     *      COMMENT )}
     * @post {getLastEvent() == VALUE_INT}
     * @param value
     * @throws IOException
     */
    public void writeValue(final int value) throws IOException;

    /**
     * @pre {getLastEvent().isValue() == true || getLastEvent() IN (START_ELEMENT, ATTRIBUTE,
     *      COMMENT )}
     * @post {getLastEvent() == VALUE_LONG}
     * @param value
     * @throws IOException
     */
    public void writeValue(final long value) throws IOException;

    /**
     * @pre {getLastEvent().isValue() == true || getLastEvent() IN (START_ELEMENT, ATTRIBUTE,
     *      COMMENT )}
     * @post {getLastEvent() == VALUE_FLOAT}
     * @param value
     * @throws IOException
     */
    public void writeValue(final float value) throws IOException;

    /**
     * @pre {getLastEvent().isValue() == true || getLastEvent() IN (START_ELEMENT, ATTRIBUTE,
     *      COMMENT )}
     * @post {getLastEvent() == VALUE_DOUBLE}
     * @param value
     * @throws IOException
     */
    public void writeValue(final double value) throws IOException;

    /**
     * @pre {getLastEvent().isValue() == true || getLastEvent() IN (START_ELEMENT, ATTRIBUTE,
     *      COMMENT )}
     * @post {getLastEvent() == VALUE_BOOL}
     * @param value
     * @throws IOException
     */
    public void writeValue(final boolean value) throws IOException;

    /**
     * @pre {getLastEvent().isValue() == true || getLastEvent() IN (START_ELEMENT, ATTRIBUTE,
     *      COMMENT )}
     * @post {getLastEvent() == VALUE_BOOL}
     * @param value
     * @param offset
     * @param length
     * @throws IOException
     */
    public void writeValue(final boolean[] value, final int offset, final int length)
            throws IOException;

    /**
     * @pre {getLastEvent().isValue() == true || getLastEvent() IN (START_ELEMENT, ATTRIBUTE,
     *      COMMENT )}
     * @post {getLastEvent() == VALUE_INT}
     * @param value
     * @param offset
     * @param length
     * @throws IOException
     */
    public void writeValue(final int[] value, final int offset, final int length)
            throws IOException;

    /**
     * @pre {getLastEvent().isValue() == true || getLastEvent() IN (START_ELEMENT, ATTRIBUTE,
     *      COMMENT )}
     * @post {getLastEvent() == VALUE_LONG}
     * @param value
     * @param offset
     * @param length
     * @throws IOException
     */
    public void writeValue(final long[] value, final int offset, final int length)
            throws IOException;

    /**
     * @pre {getLastEvent().isValue() == true || getLastEvent() IN (START_ELEMENT, ATTRIBUTE,
     *      COMMENT )}
     * @post {getLastEvent() == VALUE_FLOAT}
     * @param value
     * @param offset
     * @param length
     * @throws IOException
     */
    public void writeValue(final float[] value, final int offset, final int length)
            throws IOException;

    /**
     * Writes a value section defined by the
     * 
     * @pre {getLastEvent() IN ( START_ELEMENT START_ELEMENT, ATTRIBUTE ATTRIBUTE )}
     * @post {getLastEvent() == VALUE_DOUBLE}
     * @param value
     * @param offset
     * @param length
     * @throws IOException
     */
    public void writeValue(final double[] value, int offset, int length) throws IOException;

    /**
     * Writes a comments block
     * <p>
     * The provided {@code commentContent} is the coalesced value of the equivalent XML comment
     * block (enclosed between {@code <!--} and {@code -->} tags.
     * </p>
     * 
     * @post {getLastEvent() == COMMENT}
     * @param commentContent the coallesced value of the comment section
     * @throws IOException
     */
    public void writeComment(String commentContent) throws IOException;

    /**
     * @pre {valueType != null}
     * @pre {valueType.isValue() == true}
     * @pre {valueType != VALUE_STRING}
     * @pre {arrayLength >= 0}
     * @post {getLastEvent() == valueType}
     * @post {getWrittenValueCount() == 0}
     * @post {getValueLength() == arrayLength}
     * @param valueType
     * @param arrayLength
     * @throws IOException
     */
    public void startArray(EventType valueType, int arrayLength) throws IOException;

    /**
     * Needs to be called when done with startArray
     * 
     * @pre {getWrittenValueCount() == getValueLength()}
     * @throws IOException
     */
    public void endArray() throws IOException;

    /**
     * Binds a URI to the default namespace for the current context.
     * <p>
     * This URI is bound in the scope of the current START_ELEMENT / END_ELEMENT pair. If this
     * method is called before a START_ELEMENT has been written the uri is bound in the root scope.
     * </p>
     * 
     * @pre {getLastEvent() == NONE || getLastEvent() == START_ELEMENT}
     * @param defaultNamespaceUri the uri to bind to the default namespace, may be {@code null}
     * @throws XmlStreamException 
     */
    public void setDefaultNamespace(String defaultNamespaceUri) throws XmlStreamException;

    /**
     * Sets the prefix the uri is bound to, for the current context.
     * <p>
     * This prefix is bound in the scope of the current START_ELEMENT / END_ELEMENT pair (current
     * context). If this method is called before a START_ELEMENT has been written the prefix is
     * bound in the root scope.
     * </p>
     * 
     * @pre {getLastEvent() == NONE || getLastEvent() == START_ELEMENT}
     * @param prefix
     * @param namespaceUri
     * @throws XmlStreamException 
     */
    public void setPrefix(final String prefix, final String namespaceUri) throws XmlStreamException;

    /**
     * @pre {getLastEvent().isValue() == true}
     * @post {$return >= 0}
     * @return the length of the current value being written.
     */
    public long getValueLength();

    /**
     * @pre {getLastEvent().isValue() == true}
     * @post {$return >= 0}
     * @post {$return <= getValueLength()}
     * @return how many elements has already being written for the current value
     */
    public long getWrittenValueCount();

}
