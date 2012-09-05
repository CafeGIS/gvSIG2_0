/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: XmlCastor.java 8515 2006-11-06 07:30:00Z jaume $
 */

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
package com.iver.utiles.xmlEntity.generate;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.util.Vector;

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class XmlCastor.
 * 
 * @version $Revision: 8515 $ $Date: 2006-11-06 14:30:00 +0700 (Mon, 06 Nov 2006) $
 */
public class XmlCastor implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _xmlTagList
     */
    private java.util.Vector _xmlTagList;


      //----------------/
     //- Constructors -/
    //----------------/

    public XmlCastor() {
        super();
        _xmlTagList = new Vector();
    } //-- com.iver.utiles.xmlEntity.generate.XmlCastor()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addXmlTag
     * 
     * @param vXmlTag
     */
    public void addXmlTag(com.iver.utiles.xmlEntity.generate.XmlTag vXmlTag)
        throws java.lang.IndexOutOfBoundsException
    {
        _xmlTagList.addElement(vXmlTag);
    } //-- void addXmlTag(com.iver.utiles.xmlEntity.generate.XmlTag) 

    /**
     * Method addXmlTag
     * 
     * @param index
     * @param vXmlTag
     */
    public void addXmlTag(int index, com.iver.utiles.xmlEntity.generate.XmlTag vXmlTag)
        throws java.lang.IndexOutOfBoundsException
    {
        _xmlTagList.insertElementAt(vXmlTag, index);
    } //-- void addXmlTag(int, com.iver.utiles.xmlEntity.generate.XmlTag) 

    /**
     * Method enumerateXmlTag
     */
    public java.util.Enumeration enumerateXmlTag()
    {
        return _xmlTagList.elements();
    } //-- java.util.Enumeration enumerateXmlTag() 

    /**
     * Method getXmlTag
     * 
     * @param index
     */
    public com.iver.utiles.xmlEntity.generate.XmlTag getXmlTag(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _xmlTagList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (com.iver.utiles.xmlEntity.generate.XmlTag) _xmlTagList.elementAt(index);
    } //-- com.iver.utiles.xmlEntity.generate.XmlTag getXmlTag(int) 

    /**
     * Method getXmlTag
     */
    public com.iver.utiles.xmlEntity.generate.XmlTag[] getXmlTag()
    {
        int size = _xmlTagList.size();
        com.iver.utiles.xmlEntity.generate.XmlTag[] mArray = new com.iver.utiles.xmlEntity.generate.XmlTag[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (com.iver.utiles.xmlEntity.generate.XmlTag) _xmlTagList.elementAt(index);
        }
        return mArray;
    } //-- com.iver.utiles.xmlEntity.generate.XmlTag[] getXmlTag() 

    /**
     * Method getXmlTagCount
     */
    public int getXmlTagCount()
    {
        return _xmlTagList.size();
    } //-- int getXmlTagCount() 

    /**
     * Method isValid
     */
    public boolean isValid()
    {
        try {
            validate();
        }
        catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } //-- boolean isValid() 

    /**
     * Method marshal
     * 
     * @param out
     */
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * Method marshal
     * 
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Method removeAllXmlTag
     */
    public void removeAllXmlTag()
    {
        _xmlTagList.removeAllElements();
    } //-- void removeAllXmlTag() 

    /**
     * Method removeXmlTag
     * 
     * @param index
     */
    public com.iver.utiles.xmlEntity.generate.XmlTag removeXmlTag(int index)
    {
        java.lang.Object obj = _xmlTagList.elementAt(index);
        _xmlTagList.removeElementAt(index);
        return (com.iver.utiles.xmlEntity.generate.XmlTag) obj;
    } //-- com.iver.utiles.xmlEntity.generate.XmlTag removeXmlTag(int) 

    /**
     * Method setXmlTag
     * 
     * @param index
     * @param vXmlTag
     */
    public void setXmlTag(int index, com.iver.utiles.xmlEntity.generate.XmlTag vXmlTag)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _xmlTagList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _xmlTagList.setElementAt(vXmlTag, index);
    } //-- void setXmlTag(int, com.iver.utiles.xmlEntity.generate.XmlTag) 

    /**
     * Method setXmlTag
     * 
     * @param xmlTagArray
     */
    public void setXmlTag(com.iver.utiles.xmlEntity.generate.XmlTag[] xmlTagArray)
    {
        //-- copy array
        _xmlTagList.removeAllElements();
        for (int i = 0; i < xmlTagArray.length; i++) {
            _xmlTagList.addElement(xmlTagArray[i]);
        }
    } //-- void setXmlTag(com.iver.utiles.xmlEntity.generate.XmlTag) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.iver.utiles.xmlEntity.generate.XmlCastor) Unmarshaller.unmarshal(com.iver.utiles.xmlEntity.generate.XmlCastor.class, reader);
    } //-- java.lang.Object unmarshal(java.io.Reader) 

    /**
     * Method validate
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
