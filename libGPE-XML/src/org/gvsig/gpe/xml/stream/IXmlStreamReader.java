package org.gvsig.gpe.xml.stream;

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
 * $Id: IXmlStreamReader.java 19593 2008-03-12 17:23:30Z groldan $
 * $Log$
 */

import javax.xml.namespace.QName;

import org.gvsig.gpe.parser.IGPEContentHandler;

/**
 * Represents an abstraction layer over a concrete XML parsing technology.
 * <p>
 * This interface provides a pull like approach to parsing xml formatted documents for the libGPE
 * library, yet allowing to interchange implementations which can be based, for example, in XML SAX,
 * XML Pull, Binary XML, etc.
 * </p>
 * <p>
 * Note at this time this interface contract is made just of the methods from XmlPullParser that
 * where already used so far in the libGPE-GML and libGPE-KML modules. The intention is to first
 * introduce this abstraction layer and then evolve it as needed to support an intermediate level of
 * abstraction between actual xml parsing and {@link IGPEContentHandler}, while keeping the unit
 * tests passing all the time. So this is work in progress and expected to change dramatically,
 * though ensuring no functional breackages in any time.
 * </p>
 * 
 * @author Gabriel Roldan (TOPP)
 * @version $Id: IXmlStreamReader.java 19593 2008-03-12 17:23:30Z groldan $
 */
public interface IXmlStreamReader {

    /**
     * Indicates an event is a start element
     * 
     * @see javax.xml.stream.events.StartElement
     */
    public static final int START_ELEMENT = 1;
    /**
     * Indicates an event is an end element
     * 
     * @see javax.xml.stream.events.EndElement
     */
    public static final int END_ELEMENT = 2;
    /**
     * Indicates an event is a processing instruction
     * 
     * @see javax.xml.stream.events.ProcessingInstruction
     */
    public static final int PROCESSING_INSTRUCTION = 3;

    /**
     * Indicates an event is characters
     * 
     * @see javax.xml.stream.events.Characters
     */
    public static final int CHARACTERS = 4;

    /**
     * Indicates an event is a comment
     * 
     * @see javax.xml.stream.events.Comment
     */
    public static final int COMMENT = 5;

    /**
     * The characters are white space (see [XML], 2.10 "White Space Handling"). Events are only
     * reported as SPACE if they are ignorable white space. Otherwise they are reported as
     * CHARACTERS.
     * 
     * @see javax.xml.stream.events.Characters
     */
    public static final int SPACE = 6;

    /**
     * Indicates an event is a start document
     * 
     * @see javax.xml.stream.events.StartDocument
     */
    public static final int START_DOCUMENT = 7;

    /**
     * Indicates an event is an end document
     * 
     * @see javax.xml.stream.events.EndDocument
     */
    public static final int END_DOCUMENT = 8;

    /**
     * Indicates an event is an entity reference
     * 
     * @see javax.xml.stream.events.EntityReference
     */
    public static final int ENTITY_REFERENCE = 9;

    /**
     * Indicates an event is an attribute
     * 
     * @see javax.xml.stream.events.Attribute
     */
    //public static final int ATTRIBUTE = 10;

    /**
     * Indicates an event is a DTD
     * 
     * @see javax.xml.stream.events.DTD
     */
    public static final int DTD = 11;

    /**
     * Indicates an event is a CDATA section
     * 
     * @see javax.xml.stream.events.Characters
     */
    public static final int CDATA = 12;

    /**
     * Indicates the event is a namespace declaration
     * 
     * @see javax.xml.stream.events.Namespace
     */
    public static final int NAMESPACE = 13;

    /**
     * Indicates a Notation
     * 
     * @see javax.xml.stream.events.NotationDeclaration
     */
    public static final int NOTATION_DECLARATION = 14;

    /**
     * Indicates a Entity Declaration
     * 
     * @see javax.xml.stream.events.NotationDeclaration
     */
    public static final int ENTITY_DECLARATION = 15;

    /**
     * @return
     * @throws IllegalStateException
     */
    int getAttributeCount() throws XmlStreamException;

    QName getAttributeName(int i) throws XmlStreamException;

    /**
     * Returns the complete (coalesced) value for the attribute at index {@code i}
     * @param i
     * @return
     * @throws XmlStreamException
     */
    String getAttributeValue(int i) throws XmlStreamException;

    /**
     * Returns the qualified name (with prefix if applicable) for the current element.
     * 
     * @return the qualified name for the current START_ELEMENT or END_ELEMENT event, or
     *         <code>null</code> if the current event is not a START_ELEMENT or END_ELEMENT.
     * @throws XmlStreamException
     */
    QName getName() throws XmlStreamException;

    int getEventType() throws XmlStreamException;

    String getText() throws XmlStreamException;

    int next() throws XmlStreamException;

    int nextTag() throws XmlStreamException;

    boolean isWhitespace() throws XmlStreamException;
}
