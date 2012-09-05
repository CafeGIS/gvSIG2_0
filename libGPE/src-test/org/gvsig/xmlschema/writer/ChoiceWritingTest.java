package org.gvsig.xmlschema.writer;

import java.util.Iterator;

import org.gvsig.xmlschema.som.IXSChoice;
import org.gvsig.xmlschema.som.IXSComplexTypeDefinition;
import org.gvsig.xmlschema.som.IXSContentType;
import org.gvsig.xmlschema.som.IXSTypeDefinition;

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
 * $Id: ChoiceWritingTest.java 161 2007-06-28 13:05:27Z jorpiell $
 * $Log$
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
public class ChoiceWritingTest extends WriterBaseTest {
	private String typeName1 = "river_type";
	private String typeType1 = IXSComplexTypeDefinition.CHOICE;
	private String contentType1 = IXSContentType.WITOUT_CONTENT;
	private String contentRestriction1 = IXSContentType.WITOUT_RESTRICTION;
	private String type1ElementName1 = "length";
	private String type1ElementType1 = "xs:double";	
	private String type1ElementName2 = "name";
	private String type1ElementType2 = "xs:string";
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.writer.WriterBaseTest#readSchema()
	 */
	public void readSchema(){
		IXSTypeDefinition type = getSchema().getTypeByName(getNamespaceURI(), typeName1);
		assertNotNull(type);
		assertEquals(type.getQName().getLocalPart(), typeName1);
		IXSComplexTypeDefinition cType = (IXSComplexTypeDefinition)type;
		Iterator it = cType.getItems().iterator();
		IXSChoice choice = null;
		while (it.hasNext()){
			choice = (IXSChoice) it.next();
		}
		assertNotNull(choice);				
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.writer.WriterBaseTest#writeSchema()
	 */
	public void writeSchema() {
		IXSComplexTypeDefinition complexType = getSchema().addComplexType(
				typeName1, typeType1, contentType1, contentRestriction1);
		complexType.addElement(
				type1ElementName1,
				type1ElementType1);		
		complexType.addElement(
				type1ElementName2,
				type1ElementType2);			
	}
}
