package org.gvsig.gpe.xml.stream.stax;

import java.io.InputStream;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.gvsig.gpe.xml.stream.IXmlStreamReader;
import org.gvsig.gpe.xml.stream.XmlStreamException;

/**
 * An {@link IXmlStreamReader} adapter for xml textual parsing using a
 * {@link XMLStreamReader standard StAX} API.
 * 
 * @author Gabriel Roldan (TOPP)
 * @version $Id: StaxXmlStreamReader.java 21945 2008-06-30 14:04:53Z jpiera $
 */
public class StaxXmlStreamReader implements IXmlStreamReader {

    private XMLStreamReader parser;

    /**
     * @param in
     * @throws XmlStreamException
     */
    public StaxXmlStreamReader(InputStream in) throws XmlStreamException {
        setInput(in);
    }

    // public StaxXmlStreamReader(final XMLStreamReader reader) {
    //
    // }

    /**
     * @param inputStream
     * @throws XmlStreamException
     */
    private void setInput(InputStream inputStream) throws XmlStreamException {
        final XMLInputFactory factory = XMLInputFactory.newInstance();
        // tell the factory to return characters content in a single spot
        // factory.setProperty("javax.xml.stream.isCoalescing", "true");
        try {
            parser = factory.createXMLStreamReader(inputStream, null);
        } catch (XMLStreamException e) {
            Throwable nestedException = e.getNestedException();
            String msg = "Error creating StAX parser: " + e.getMessage();
            if (nestedException != null) {
                msg += "(" + nestedException.getMessage() + ")";
            }
            throw new XmlStreamException(msg, e);
        }
    }

    /**
     * @return
     * @see org.gvsig.gpe.xml.stream.IXmlStreamReader#getAttributeCount()
     */
    public int getAttributeCount() throws XmlStreamException {
        return parser.getAttributeCount();
    }

    public QName getAttributeName(int i) throws XmlStreamException {
        return parser.getAttributeName(i);
    }

    public String getAttributeValue(int i) throws XmlStreamException {
        return parser.getAttributeValue(i);
    }

    public int getEventType() throws XmlStreamException {
        int xmlPullEventType;
        xmlPullEventType = parser.getEventType();
        return pullEventToGpeEventType(xmlPullEventType);
    }

    /**
     * Returns the (local) name of the current tag (the current event shall be START_ELEMENT or
     * END_ELEMENT), or null if the current event is not a tag event.
     * 
     * @return
     * @see org.gvsig.gpe.xml.stream.IXmlStreamReader#getName()
     */
    public QName getName() throws XmlStreamException {
        final int eventType = parser.getEventType();
        if (eventType != XMLStreamConstants.START_ELEMENT
                && eventType != XMLStreamConstants.END_ELEMENT) {
            return null;
        }
        // QName name = parser.getName();
        // String qname = null;
        // if(name != null){
        // String prefix = name.getPrefix();
        // String localName = name.getLocalPart();
        // if(XMLConstants.DEFAULT_NS_PREFIX.equals(prefix)){
        // qname = localName;
        // }else{
        // qname = prefix + ":" + localName;
        // }
        // }
        return parser.getName();
    }

    public String getText() throws XmlStreamException {
        return parser.getText();
    }

    public boolean isWhitespace() throws XmlStreamException {
        return parser.isWhiteSpace();
    }

    public int next() throws XmlStreamException {
        int xmlPullEventType;
        try {
            xmlPullEventType = parser.next();
        } catch (XMLStreamException e) {
            String msg = "Failed to call XmlStreamReader.next()";
            if (e.getNestedException() != null) {
                msg += ": " + e.getNestedException().getMessage();
            }
            throw new XmlStreamException(msg, e.getNestedException() == null ? e : e
                    .getNestedException());
        }
        int pullEventToGpeEventType = pullEventToGpeEventType(xmlPullEventType);
        return pullEventToGpeEventType;
    }

    public int nextTag() throws XmlStreamException {
        int xmlPullEventType;
        try {
            xmlPullEventType = parser.nextTag();
        } catch (XMLStreamException e) {
            e.printStackTrace();
            throw new XmlStreamException(e);
        }
        int pullEventToGpeEventType = pullEventToGpeEventType(xmlPullEventType);
        return pullEventToGpeEventType;
    }

    private int pullEventToGpeEventType(int xmlPullEventType) {
        switch (xmlPullEventType) {
        case XMLStreamConstants.START_DOCUMENT:
            return IXmlStreamReader.START_DOCUMENT;
        case XMLStreamConstants.END_DOCUMENT:
            return IXmlStreamReader.END_DOCUMENT;
        case XMLStreamConstants.START_ELEMENT:
            return IXmlStreamReader.START_ELEMENT;
        case XMLStreamConstants.END_ELEMENT:
            return IXmlStreamReader.END_ELEMENT;
        case XMLStreamConstants.CHARACTERS:
            return IXmlStreamReader.CHARACTERS;
        case XMLStreamConstants.CDATA:
            return IXmlStreamReader.CDATA;
        case XMLStreamConstants.ENTITY_REFERENCE:
            return IXmlStreamReader.ENTITY_REFERENCE;
        case XMLStreamConstants.SPACE:
            return IXmlStreamReader.SPACE;
        case XMLStreamConstants.PROCESSING_INSTRUCTION:
            return IXmlStreamReader.PROCESSING_INSTRUCTION;
        case XMLStreamConstants.COMMENT:
            return IXmlStreamReader.COMMENT;
        case XMLStreamConstants.DTD:
            return IXmlStreamReader.DTD;
        default:
            throw new IllegalStateException("Unknown tag type, this should't happen!: "
                    + xmlPullEventType);
        }
    }
}
