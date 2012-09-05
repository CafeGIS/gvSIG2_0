package org.gvsig.xmlschema.writer;

import org.gvsig.xmlschema.som.IXSComplexContent;
import org.gvsig.xmlschema.som.IXSComplexTypeDefinition;
import org.gvsig.xmlschema.som.IXSContentType;
import org.gvsig.xmlschema.som.IXSElementDeclaration;

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
 * $Id: ElementWithComplexTypesWrittingTest.java 164 2007-07-02 10:00:46Z jorpiell $
 * $Log$
 * Revision 1.4  2007/07/02 09:59:03  jorpiell
 * The generated xsd schemas have to be valid
 *
 * Revision 1.3  2007/06/28 13:04:33  jorpiell
 * The Qname has been updated to the 1.5 JVM machine. The schema validation is made in the GPEWriterHandlerImplementor class
 *
 * Revision 1.2  2007/06/22 12:21:18  jorpiell
 * The typeNotFoundException has been deleted. It never was thrown
 *
 * Revision 1.1  2007/06/14 16:15:03  jorpiell
 * builds to create the jars generated and add the schema code to the libGPEProject
 *
 * Revision 1.1  2007/06/14 13:50:07  jorpiell
 * The schema jar name has been changed
 *
 * Revision 1.3  2007/06/08 11:35:16  jorpiell
 * IXSSchema interface updated
 *
 * Revision 1.2  2007/06/07 14:54:13  jorpiell
 * Add the schema support
 *
 * Revision 1.1  2007/05/30 12:25:48  jorpiell
 * Add the element collection
 *
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class ElementWithComplexTypesWrittingTest extends WriterBaseTest {
	private String elementName1 = "people";
	private String elementType1 = "people_type";
    //People
	//private String typeName1 = "people_type";
	private String typeType1 = IXSComplexTypeDefinition.SEQUENCE;
	private String contentType1 = IXSContentType.COMPLEX_CONTENT;
	private String contentRestriction1 = IXSContentType.EXTENSION;
	private String type11ElementName = "name";
	private String type11ElementType =  null;
	//private String type12ElementName = "age";
	//private String type12ElementType = "xs:integer";
	private String type13ElementName = "address";
	private String type13ElementType = null;
	//Name
	private String typeType11 = IXSComplexTypeDefinition.SEQUENCE;
	private String contentType11 = IXSContentType.WITOUT_CONTENT;
	private String contentRestriction11 = IXSContentType.WITOUT_RESTRICTION;		
	private String type111ElementName = "first name";
	private String type111ElementType = "xs:string";
	private String type112ElementName = "second name";
	private String type112ElementType = "xs:string";
	//Address
	private String typeType13 = IXSComplexTypeDefinition.SEQUENCE;
	private String contentType13 = IXSContentType.WITOUT_CONTENT;
	private String contentRestriction13 = IXSContentType.WITOUT_RESTRICTION;	
	private String type131ElementName = "street";
	private String type131ElementType = "xs:string";
	private String type132ElementName = "number";
	private String type132ElementType = "xs:integer";
	private String type133ElementName = "city";
	private String type133ElementType = "xs:string";
	private String type134ElementName = "postal code";
	private String type134ElementType = "xs:integer";
	
	public void readSchema()  {
		IXSElementDeclaration element = getSchema().getElementDeclarationByName(getNamespaceURI(), elementName1);
		assertNotNull(element);
		assertEquals(element.getQName().getLocalPart(), elementName1);
		assertTrue(element.getTypeDefinition() instanceof IXSComplexTypeDefinition);
		IXSComplexTypeDefinition type = (IXSComplexTypeDefinition)element.getTypeDefinition();		
		assertTrue(type.getContentType() instanceof IXSComplexContent);
		IXSComplexContent content = (IXSComplexContent)type.getContentType();
		assertTrue(content.getExtension() != null);
	}

	public void writeSchema() {
		//Add the element
		IXSElementDeclaration element1 = getSchema().addElement( 
				elementName1,
				elementType1);				
		
		IXSComplexTypeDefinition complexType = element1.addComplexType(
				typeType1, contentType1, contentRestriction1);
		IXSElementDeclaration element11 = complexType.addElement(
				type11ElementName,
				type11ElementType);		
		//IXSElementDeclaration element12 = complexType.addElement(
		//		type12ElementName,
		//		type12ElementType);	
		IXSElementDeclaration element13 = complexType.addElement(
				type13ElementName,
				type13ElementType);
		
		IXSComplexTypeDefinition eComplex11 = element11.addComplexType(typeType11,
				contentType11,
				contentRestriction11);
		eComplex11.addElement(type111ElementName,
				type111ElementType);
		eComplex11.addElement(type112ElementName,
				type112ElementType);
		
		IXSComplexTypeDefinition eComplex13 = element13.addComplexType(typeType13,
				contentType13,
				contentRestriction13);
		eComplex13.addElement(type131ElementName,
				type131ElementType);
		eComplex13.addElement(type132ElementName,
				type132ElementType);
		eComplex13.addElement(type133ElementName,
				type133ElementType);
		eComplex13.addElement(type134ElementName,
				type134ElementType);
	}

}
