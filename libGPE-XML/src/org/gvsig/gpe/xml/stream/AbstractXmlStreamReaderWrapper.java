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

import javax.xml.namespace.QName;

/**
 * Plain wrapper for an {@link IXmlStreamReader} aimed at easying the creation of IXmlStreamReader
 * decorators.
 * <p>
 * This class is a pure abstract wrapper, so all methods delegate to the wrapped object.
 * </p>
 * 
 * @author Gabriel Roldan (TOPP)
 * @version $Id: AbstractXmlStreamReaderWrapper.java 20425 2008-05-05 14:24:58Z groldan $
 */
public abstract class AbstractXmlStreamReaderWrapper implements IXmlStreamReader {

    private IXmlStreamReader wrapped;

    protected AbstractXmlStreamReaderWrapper() {
        // do nothing
    }

    protected AbstractXmlStreamReaderWrapper(IXmlStreamReader wrapped) {
        setWrapped(wrapped);
    }

    public void setWrapped(IXmlStreamReader wrapped) {
        this.wrapped = wrapped;
    }
    
    /**
     * @see org.gvsig.gpe.xml.stream.IXmlStreamReader#getAttributeCount()
     */
    public int getAttributeCount() throws XmlStreamException {
        return wrapped.getAttributeCount();
    }

    /**
     * @see org.gvsig.gpe.xml.stream.IXmlStreamReader#getAttributeName(int)
     */
    public QName getAttributeName(int i) throws XmlStreamException {
        return wrapped.getAttributeName(i);
    }

    /**
     * @param i
     * @see org.gvsig.gpe.xml.stream.IXmlStreamReader#getAttributeValue(int)
     */
    public String getAttributeValue(int i) throws XmlStreamException {
        return wrapped.getAttributeValue(i);
    }

    /**
     * @see org.gvsig.gpe.xml.stream.IXmlStreamReader#getEventType()
     */
    public int getEventType() throws XmlStreamException {
        return wrapped.getEventType();
    }

    /**
     * @see org.gvsig.gpe.xml.stream.IXmlStreamReader#getName()
     */
    public QName getName() throws XmlStreamException {
        return wrapped.getName();
    }

    /**
     * @see org.gvsig.gpe.xml.stream.IXmlStreamReader#getText()
     */
    public String getText() throws XmlStreamException {
        return wrapped.getText();
    }

    /**
     * @see org.gvsig.gpe.xml.stream.IXmlStreamReader#isWhitespace()
     */
    public boolean isWhitespace() throws XmlStreamException {
        return wrapped.isWhitespace();
    }

    /**
     * @see org.gvsig.gpe.xml.stream.IXmlStreamReader#next()
     */
    public int next() throws XmlStreamException {
        return wrapped.next();
    }

    /**
     * @see org.gvsig.gpe.xml.stream.IXmlStreamReader#nextTag()
     */
    public int nextTag() throws XmlStreamException {
        return wrapped.nextTag();
    }

}
