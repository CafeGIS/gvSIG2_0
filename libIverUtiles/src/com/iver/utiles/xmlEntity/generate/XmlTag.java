/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: XmlTag.java 8515 2006-11-06 07:30:00Z jaume $
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
 * Class XmlTag.
 * 
 * @version $Revision: 8515 $ $Date: 2006-11-06 14:30:00 +0700 (Mon, 06 Nov 2006) $
 */
public class XmlTag implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _propertyList
     */
    private java.util.Vector _propertyList;

    /**
     * Field _xmlTagList
     */
    private java.util.Vector _xmlTagList;


      //----------------/
     //- Constructors -/
    //----------------/

    public XmlTag() {
        super();
        _propertyList = new Vector();
        _xmlTagList = new Vector();
    } //-- com.iver.utiles.xmlEntity.generate.XmlTag()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addProperty
     * 
     * @param vProperty
     */
    public void addProperty(com.iver.utiles.xmlEntity.generate.Property vProperty)
        throws java.lang.IndexOutOfBoundsException
    {
        _propertyList.addElement(vProperty);
    } //-- void addProperty(com.iver.utiles.xmlEntity.generate.Property) 

    /**
     * Method addProperty
     * 
     * @param index
     * @param vProperty
     */
    public void addProperty(int index, com.iver.utiles.xmlEntity.generate.Property vProperty)
        throws java.lang.IndexOutOfBoundsException
    {
        _propertyList.insertElementAt(vProperty, index);
    } //-- void addProperty(int, com.iver.utiles.xmlEntity.generate.Property) 

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
     * Method enumerateProperty
     */
    public java.util.Enumeration enumerateProperty()
    {
        return _propertyList.elements();
    } //-- java.util.Enumeration enumerateProperty() 

    /**
     * Method enumerateXmlTag
     */
    public java.util.Enumeration enumerateXmlTag()
    {
        return _xmlTagList.elements();
    } //-- java.util.Enumeration enumerateXmlTag() 

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Method getProperty
     * 
     * @param index
     */
    public com.iver.utiles.xmlEntity.generate.Property getProperty(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _propertyList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (com.iver.utiles.xmlEntity.generate.Property) _propertyList.elementAt(index);
    } //-- com.iver.utiles.xmlEntity.generate.Property getProperty(int) 

    /**
     * Method getProperty
     */
    public com.iver.utiles.xmlEntity.generate.Property[] getProperty()
    {
        int size = _propertyList.size();
        com.iver.utiles.xmlEntity.generate.Property[] mArray = new com.iver.utiles.xmlEntity.generate.Property[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (com.iver.utiles.xmlEntity.generate.Property) _propertyList.elementAt(index);
        }
        return mArray;
    } //-- com.iver.utiles.xmlEntity.generate.Property[] getProperty() 

    /**
     * Method getPropertyCount
     */
    public int getPropertyCount()
    {
        return _propertyList.size();
    } //-- int getPropertyCount() 

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
     * Method removeAllProperty
     */
    public void removeAllProperty()
    {
        _propertyList.removeAllElements();
    } //-- void removeAllProperty() 

    /**
     * Method removeAllXmlTag
     */
    public void removeAllXmlTag()
    {
        _xmlTagList.removeAllElements();
    } //-- void removeAllXmlTag() 

    /**
     * Method removeProperty
     * 
     * @param index
     */
    public com.iver.utiles.xmlEntity.generate.Property removeProperty(int index)
    {
        java.lang.Object obj = _propertyList.elementAt(index);
        _propertyList.removeElementAt(index);
        return (com.iver.utiles.xmlEntity.generate.Property) obj;
    } //-- com.iver.utiles.xmlEntity.generate.Property removeProperty(int) 

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
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Method setProperty
     * 
     * @param index
     * @param vProperty
     */
    public void setProperty(int index, com.iver.utiles.xmlEntity.generate.Property vProperty)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _propertyList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _propertyList.setElementAt(vProperty, index);
    } //-- void setProperty(int, com.iver.utiles.xmlEntity.generate.Property) 

    /**
     * Method setProperty
     * 
     * @param propertyArray
     */
    public void setProperty(com.iver.utiles.xmlEntity.generate.Property[] propertyArray)
    {
        //-- copy array
        _propertyList.removeAllElements();
        for (int i = 0; i < propertyArray.length; i++) {
            _propertyList.addElement(propertyArray[i]);
        }
    } //-- void setProperty(com.iver.utiles.xmlEntity.generate.Property) 

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
        return (com.iver.utiles.xmlEntity.generate.XmlTag) Unmarshaller.unmarshal(com.iver.utiles.xmlEntity.generate.XmlTag.class, reader);
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
