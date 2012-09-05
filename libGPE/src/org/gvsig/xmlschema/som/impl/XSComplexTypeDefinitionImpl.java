package org.gvsig.xmlschema.som.impl;

import org.gvsig.xmlschema.som.IXSComplexTypeDefinition;
import org.gvsig.xmlschema.som.IXSContentType;
import org.gvsig.xmlschema.som.IXSElementDeclaration;
import org.gvsig.xmlschema.som.IXSGroup;
import org.gvsig.xmlschema.som.IXSNode;
import org.gvsig.xmlschema.som.IXSSchema;
import org.gvsig.xmlschema.utils.SchemaCollection;
import org.gvsig.xmlschema.utils.SchemaDefaults;
import org.gvsig.xmlschema.utils.SchemaObjectsMapping;
import org.gvsig.xmlschema.utils.SchemaTags;
import org.w3c.dom.Element;

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
 * $Id: XSComplexTypeDefinitionImpl.java 157 2007-06-22 12:22:53Z jorpiell $
 * $Log$
 * Revision 1.2  2007/06/22 12:20:48  jorpiell
 * The typeNotFoundException has been deleted. It never was thrown
 *
 * Revision 1.1  2007/06/14 16:15:03  jorpiell
 * builds to create the jars generated and add the schema code to the libGPEProject
 *
 * Revision 1.1  2007/06/14 13:50:07  jorpiell
 * The schema jar name has been changed
 *
 * Revision 1.4  2007/06/08 11:35:16  jorpiell
 * IXSSchema interface updated
 *
 * Revision 1.3  2007/06/07 14:54:13  jorpiell
 * Add the schema support
 *
 * Revision 1.2  2007/05/30 12:25:48  jorpiell
 * Add the element collection
 *
 * Revision 1.1  2007/05/25 11:55:00  jorpiell
 * First update
 *
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class XSComplexTypeDefinitionImpl extends XSTypeDefinitionImpl implements IXSComplexTypeDefinition {
			
	public XSComplexTypeDefinitionImpl(IXSSchema schema) {
		super(schema);
	}
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.som.IXSComplexTypeDefinition#getContentType()
	 */
	public IXSContentType getContentType(){
		IXSNode node = new SchemaCollection(getSchema(),getElement(),getContentMapping()).getFirstNode();
		if (node != null){
			return (IXSContentType)node;
		}
		return null;
	}

	/**
	 * @return The extension mapping
	 * @throws TypeNotFoundException
	 */
	private SchemaObjectsMapping getContentMapping(){
		SchemaObjectsMapping elementTm = new SchemaObjectsMapping(getSchema());
		elementTm.addType(SchemaTags.COMPLEX_CONTENT, XSComplexContentImpl.class);
		elementTm.addType(SchemaTags.SIMPLE_CONTENT, XSComplexContentImpl.class);
		return elementTm;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.som.IXSComplexTypeDefinition#getGroup()
	 */
	public IXSGroup getGroup(){
		IXSNode node = new SchemaCollection(getSchema(),getElement(),getGroupMapping()).getFirstNode();
		if (node != null){
			return (IXSGroup)node;
		}
		return null;
	}
	
	/**
	 * @return The extension mapping
	 * @throws TypeNotFoundException
	 */
	private SchemaObjectsMapping getGroupMapping(){
		SchemaObjectsMapping elementTm = new SchemaObjectsMapping(getSchema());
		elementTm.addType(SchemaTags.SEQUENCE, XSSequenceImpl.class);
		elementTm.addType(SchemaTags.ALL, XSAllImpl.class);
		elementTm.addType(SchemaTags.GROUP, XSGroupImpl.class);
		elementTm.addType(SchemaTags.CHOICE, XSChoiceImpl.class);
		return elementTm;
	}
	
	/**
	 * Adds a new element to the complex type
	 */
	public IXSElementDeclaration addElement(String name, String typeName, boolean nillable, int minOccurs, int maxOccurs){
		Element eElement = getElementsFactory().createElement(getSchema(),
				name,
				typeName,
				nillable,
				minOccurs,
				maxOccurs);
		getElementsFactory().addElementToComplexType(
				getSchema(),
				eElement, 
				getElement());	
		XSElementDeclarationImpl elementDeclaration = new XSElementDeclarationImpl(getSchema());
		elementDeclaration.setElement(eElement);
		return elementDeclaration;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.som.IXSComplexTypeDefinition#addElement(java.lang.String, java.lang.String)
	 */
	public IXSElementDeclaration addElement(String name, String typeName) {
		return addElement(name, typeName, SchemaDefaults.ELEMENT_NILLABLE,
				SchemaDefaults.ELEMENT_MIN_OCCURS, SchemaDefaults.ELEMENT_MAX_OCCURS);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.som.IXSTypeDefinition#getTypeName()
	 */
	public String getTypeName() {
		return getElement().getAttribute(SchemaTags.NAME);		
	}

}
