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


/**
 * The EventType enum defines the possible parse events a {@link IXmlStreamReader} may return during
 * each call to {@code next()}.
 * <p>
 * These are high level events and may not map one to one to the low level Binary XML format tokens.
 * This is so to better reflect the problem domain for this high level API, letting the low level
 * treatment of specific bxml tokens to the implementation criteria.
 * </p>
 * <p>
 * In general lines, it could be said that the following EventType to TokenType mappings are
 * defined: <table>
 * <tr>
 * <th>EventType</th>
 * <th>TokenType</th>
 * </tr>
 * <tr>
 * <td>NONE</td>
 * <td>Header</td>
 * </tr>
 * <tr>
 * <td>START_DOCUMENT</td>
 * <td>XmlDeclaration, if present, forced otherwise in order to always return a START_DOCUMENT
 * event</td>
 * </tr>
 * <tr>
 * <td>END_DOCUMENT</td>
 * <td>Trailer</td>
 * </tr>
 * <tr>
 * <td>START_ELEMENT</td>
 * <td>EmptyElement, EmptyAttrElement, ContentElement, ContentAttrElement</td>
 * </tr>
 * <tr>
 * <td>END_ELEMENT</td>
 * <td>ElementEnd, or forced when EmptyElement or EmptyAttrElement</td>
 * </tr>
 * <tr>
 * <td>ATTRIBUTE</td>
 * <td>AttributeStart, only used for writing</td>
 * </tr>
 * <tr>
 * <td>ATTRIBUTES_END</td>
 * <td>AttributeListEnd, only used for writing</td>
 * </tr>
 * <tr>
 * <td>COMMENT</td>
 * <td>Comment</td>
 * </tr>
 * <tr>
 * <td>SPACE</td>
 * <td>WhiteSpace</td>
 * </tr>
 * <tr>
 * <td>VALUE_STRING, VALUE_BOOL, VALUE_BYTE, VALUE_INT, VALUE_LONG, VALUE_FLOAT, VALUE_DOUBLE</td>
 * <td>CharContent token, followed by the specific value type</td>
 * </tr>
 * </table>
 * </p>
 * 
 * @author Gabriel Roldan (TOPP)
 * @version $Id: EventType.java 21945 2008-06-30 14:04:53Z jpiera $
 * @since 1.0
 */
public final class EventType {

    private boolean isValueEvent;

    private int code;

    /**
     * Private default constructor inforces the use of this class by its public static members only
     */
    private EventType(final int code) {
        this(code, false);
    }

    private EventType(final int code, boolean isValue) {
        this.code = code;
        this.isValueEvent = isValue;
    }

    public static final int NONE_CODE = 0;

    /**
     * Null object to indicate either a document has not started being parsed yet, or a document has
     * not started being written yet.
     */
    public static final EventType NONE = new EventType(NONE_CODE);

    public static final int START_DOCUMENT_CODE = 1;
    /**
     * Indicates the parser is positioned at the start of the document. The next call to
     * {@link IXmlStreamReader#next()} shall return either {@link #START_ELEMENT}, {@link #COMMENT}
     * or {@link #END_DOCUMENT} if the document is empty.
     */
    public static final EventType START_DOCUMENT = new EventType(START_DOCUMENT_CODE);

    public static final int END_DOCUMENT_CODE = 2;

    /**
     * Indicates the parser has reached the end of the bxml document
     */
    public static final EventType END_DOCUMENT = new EventType(END_DOCUMENT_CODE);

    public static final int START_ELEMENT_CODE = 3;

    /**
     * Signals the opening of an element tag.
     */
    public static final EventType START_ELEMENT = new EventType(START_ELEMENT_CODE);

    public static final int END_ELEMENT_CODE = 4;

    /**
     * Signals the end of the current element. No content elements such as {@code <element/>} and
     * {@code <element att="attval"/>} will be notified by an START_ELEMENT and END_ELEMENT event
     * with no value event in between, to preserve semantic equivalence with the
     * {@code <element></element>} tag sequence.
     */
    public static final EventType END_ELEMENT = new EventType(END_ELEMENT_CODE);

    public static final int VALUE_BOOL_CODE = 5;

    /**
     * Content event that indicates the parser is at a value token whose content is either a Java
     * boolean or an array of booleans.
     */
    public static final EventType VALUE_BOOL = new EventType(VALUE_BOOL_CODE, true);

    public static final int VALUE_BYTE_CODE = 6;

    /**
     * Content event that indicates the parser is at a value token whose content is either a Java
     * byte or an array of bytes.
     */
    public static final EventType VALUE_BYTE = new EventType(VALUE_BYTE_CODE, true);

    public static final int VALUE_INT_CODE = 7;

    /**
     * Content event that indicates the parser is at a value token whose content is either a Java
     * integer or an array of integers. For the sake of simplicity, the low level {@code SmallNum},
     * {@code Short}, {@code UShort}, and {@code Int} value types are mapped to this event type.
     */
    public static final EventType VALUE_INT = new EventType(VALUE_INT_CODE, true);

    public static final int VALUE_LONG_CODE = 8;

    /**
     * Content event that indicates the parser is at a value token whose content is either a Java
     * long or an array of longs.
     */
    public static final EventType VALUE_LONG = new EventType(VALUE_LONG_CODE, true);

    public static final int VALUE_FLOAT_CODE = 9;

    /**
     * Content event that indicates the parser is at a value token whose content is either a Java
     * float or an array of floats.
     */
    public static final EventType VALUE_FLOAT = new EventType(VALUE_FLOAT_CODE, true);

    public static final int VALUE_DOUBLE_CODE = 10;

    /**
     * Content event that indicates the parser is at a value token whose content is either a Java
     * double or an array of double.
     */
    public static final EventType VALUE_DOUBLE = new EventType(VALUE_DOUBLE_CODE, true);

    public static final int VALUE_STRING_CODE = 11;
    /**
     * Content event that indicates the parser is at a value token whose content is a Java String.
     * There are no such a structure as an array of Strings in the BXML spec.
     */
    public static final EventType VALUE_STRING = new EventType(VALUE_STRING_CODE, true);

    public static final int VALUE_CDATA_CODE = 12;

    /**
     * This is event equivalent to the {@code "<![CDATA[content]]>"} structure in textual XML,
     * signaled by the CDataSectionToken. This token is essentially equivalent to the
     * CharContentToken, except that its use may be regarded as a hint to a translator to regenerate
     * a CDATA section in textual XML.
     */
    public static final EventType VALUE_CDATA = new EventType(VALUE_CDATA_CODE, true);

    public static final int VALUE_SPACE_CODE = 13;

    /**
     * Content event that signals the precense of a WhiteSpace token. That is, a potentially
     * insignificant sequence of white space or newline characters
     */
    public static final EventType SPACE = new EventType(VALUE_SPACE_CODE, true);

    public static final int COMMENT_CODE = 14;

    /**
     * Indicates the parser is at a XML comment token
     */
    public static final EventType COMMENT = new EventType(COMMENT_CODE);

    public static final int ATTRIBUTE_CODE = 15;

    /**
     * Used only for writing attributes.
     * 
     * @see IXmlStreamWriter#writeStartAttribute(String, String)
     */
    public static final EventType ATTRIBUTE = new EventType(ATTRIBUTE_CODE);

    public static final int ATTRIBUTES_END_CODE = 16;

    /**
     * Used only for writing attributes.
     * 
     * @see IXmlStreamWriter#writeEndAttributes()
     */
    public static final EventType ATTRIBUTES_END = new EventType(ATTRIBUTES_END_CODE);

    public int getCode() {
        return code;
    }

    /**
     * Convenience method that returns whether this EventType represents a value token
     * 
     * @return {@code true} if {@code eventType} is one of
     *         {@code VALUE_BOOL, VALUE_BYTE, VALUE_INT, VALUE_LONG, VALUE_FLOAT, VALUE_DOUBLE,
     *         VALUE_STRING, VALUE_CDATA, SPACE}
     */
    public boolean isValue() {
        return isValueEvent;
    }

    /**
     * Convenience method that returns whether this EventType represents a tag element
     * 
     * @return {@code true} if {@code eventType} is one of {@code START_ELEMENT, END_ELEMENT}
     */
    public boolean isTag() {
        return START_ELEMENT == this || END_ELEMENT == this;
    }
}
