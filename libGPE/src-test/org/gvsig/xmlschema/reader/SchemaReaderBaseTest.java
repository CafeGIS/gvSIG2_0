package org.gvsig.xmlschema.reader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;

import junit.framework.TestCase;

import org.gvsig.xmlschema.exceptions.SchemaCreationException;
import org.gvsig.xmlschema.som.IXSAll;
import org.gvsig.xmlschema.som.IXSChoice;
import org.gvsig.xmlschema.som.IXSComplexContent;
import org.gvsig.xmlschema.som.IXSComplexTypeDefinition;
import org.gvsig.xmlschema.som.IXSContentType;
import org.gvsig.xmlschema.som.IXSElementDeclaration;
import org.gvsig.xmlschema.som.IXSExtension;
import org.gvsig.xmlschema.som.IXSGroup;
import org.gvsig.xmlschema.som.IXSRestriction;
import org.gvsig.xmlschema.som.IXSSchema;
import org.gvsig.xmlschema.som.IXSSequence;
import org.gvsig.xmlschema.som.IXSSimpleContent;
import org.gvsig.xmlschema.som.IXSSimpleTypeDefinition;
import org.gvsig.xmlschema.som.IXSTypeDefinition;
import org.gvsig.xmlschema.utils.SchemaDocumentBuilder;

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
 * $Id: SchemaReaderBaseTest.java 161 2007-06-28 13:05:27Z jorpiell $
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
 * Revision 1.2  2007/06/08 07:31:20  jorpiell
 * Add the euroRoadS test
 *
 * Revision 1.1  2007/06/08 06:55:05  jorpiell
 * Fixed some bugs
 *
 * Revision 1.4  2007/06/07 14:54:13  jorpiell
 * Add the schema support
 *
 * Revision 1.3  2007/05/30 12:25:48  jorpiell
 * Add the element collection
 *
 * Revision 1.2  2007/05/28 12:38:03  jorpiell
 * Some bugs fixed
 *
 * Revision 1.1  2007/05/25 11:55:00  jorpiell
 * First update
 *
 *
 */
/**
 * This class must be implemented by all the xml Schema 
 * reading tests.
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public abstract class SchemaReaderBaseTest extends TestCase {
	private SchemaDocumentBuilder schemaBuilder = null;
	private IXSSchema schema = null;
	
	public void setUp(){
		schemaBuilder = SchemaDocumentBuilder.getInstance();
	}
	
	public void testParse() throws SchemaCreationException, FileNotFoundException{
		schema = getSchemaBuilder().parse(new FileInputStream(getFile()));
		makeAsserts();
		printSchema();
	}
	
	/**
	 * Gets the XML schema file to open
	 * @return
	 */
	public abstract String getFile();
	
	/**
	 * This method must be used by the subclasses
	 * to make the comparations. 
	 */
	public abstract void makeAsserts() ;
	
	/**
	 * @return the schema
	 */
	public SchemaDocumentBuilder getSchemaBuilder() {
		return schemaBuilder;
	}

	/**
	 * @return the schema
	 */
	public IXSSchema getSchema() {
		return schema;
	}
	
	/**
	 * Print the schema elements by console
	 * @
	 */
	private void printSchema() {
		Iterator it = getSchema().getElementDeclarations().iterator();
		while (it.hasNext()){
			IXSElementDeclaration element = (IXSElementDeclaration)it.next();
			printElement(element, "");			
		}
	}		

	/**
	 * Print one element
	 * @param element
	 * xml schema element to print
	 * @param tab
	 * Tabs to write on the left part of each line
	 * @
	 */
	private void printElement(IXSElementDeclaration element, String tab) {
		IXSTypeDefinition type = element.getTypeDefinition();
		System.out.print(tab + "ELEMENT: " + element.getQName().getLocalPart());
		System.out.print(", TYPE: ");
		if (type == null){
			System.out.print("NULL");
			System.out.print(", TYPE NAME: " + element.getTypeName());
			System.out.print("\n");
		}else{
			System.out.print(type.getQName().getLocalPart());
			System.out.print("\n");
			printType(type,tab + "\t");
		}		
	}
	
	private void printType(IXSTypeDefinition type, String tab) {
		if (type instanceof IXSSimpleTypeDefinition){
			System.out.print(tab + "SIMPLE TYPE");
			System.out.print("\n");
		}else if (type instanceof IXSComplexTypeDefinition){ 
			System.out.print(tab + "COMPLEX TYPE");
			System.out.print("\n");
			printComplexType((IXSComplexTypeDefinition)type,tab + "\t");			
		}
	}
	
	private void printComplexType(IXSComplexTypeDefinition complexType, String tab) {
		IXSContentType contentType = complexType.getContentType();
		if (contentType != null){
			printContentType(contentType,tab);
		}
		IXSGroup group = complexType.getGroup();
		if (group != null){
			printGroup(group,tab);
		}
	}

	private void printContentType(IXSContentType contentType, String tab)  {
		if (contentType instanceof IXSSimpleContent){
			System.out.println(tab + "SIMPLE CONTENT");
			System.out.print("\n");
			printSimpleContent((IXSSimpleContent)contentType, tab + "\t");
		}else if (contentType instanceof IXSComplexContent){
			System.out.print(tab + "COMPLEX CONTENT");
			System.out.print("\n");
			printComplexContent((IXSComplexContent)contentType, tab + "\t");
		}else if (contentType instanceof IXSGroup){
			printGroup((IXSGroup)contentType, tab + "\t");
		}		
	}
	
	private void printSimpleContent(IXSSimpleContent simpleContent, String tab) {
		IXSRestriction restriction = simpleContent.getRestriction();
		if (restriction != null){
			printRestriction(restriction, tab  + "\t");
		}
		IXSExtension extension = simpleContent.getExtension();
		if (extension != null){
			printExtension(extension, tab  + "\t");
		}
	}
	
	private void printComplexContent(IXSComplexContent complexContent, String tab) {
		IXSRestriction restriction = complexContent.getRestriction();
		if (restriction != null){
			printRestriction(restriction, tab);
		}
		IXSExtension extension = complexContent.getExtension();
		if (extension != null){
			printExtension(extension, tab);
		}
	}

	private void printExtension(IXSExtension extension, String tab)  {
		System.out.print(tab + "EXTENSION");
		System.out.print("\n");
		Iterator it = extension.getItems().iterator();
		while (it.hasNext()){
			Object item = it.next();
			if (item instanceof IXSElementDeclaration){
				printElement((IXSElementDeclaration)item, tab + "\t");
			}else {
				printGroup((IXSGroup)item, tab + "\t");
			}				
		}		
	}
	
	private void printRestriction(IXSRestriction restriction, String tab){
		System.out.print(tab + "RESTRICTION");
		System.out.print("\n");
		Iterator it = restriction.getItems().iterator();
		while (it.hasNext()){
			Object item = it.next();
			if (item instanceof IXSElementDeclaration){
				printElement((IXSElementDeclaration)item, tab + "\t");
			}else {
				printGroup((IXSGroup)item, tab + "\t");
			}
		}		
	}
	
	private void printGroup(IXSGroup group, String tab)  {
		if (group instanceof IXSChoice){			
			System.out.print(tab + "CHOICE");	
		}else if (group instanceof IXSSequence){
			System.out.print(tab + "SEQUENCE");	
		}else if (group instanceof IXSAll){
			System.out.print(tab + "ALL");	
		}else {
			System.out.print(tab + "GROUP");	
		}
		System.out.print("\n");
		Iterator it = group.getItems().iterator();
		while (it.hasNext()){
			Object item = it.next();
			if (item instanceof IXSElementDeclaration){
				printElement((IXSElementDeclaration)item, tab + "\t");				
			}else {
				printGroup((IXSGroup)item, tab);
			}				
		}
	}

//	private void printChoice(IXSChoice choice, String tab){
//		System.out.print(tab + "CHOICE");	
//		System.out.print("\n");
//		Iterator it = choice.getItems().iterator();
//		while (it.hasNext()){
//			IXSElementDeclaration element = (IXSElementDeclaration)it.next();
//			printElement(element, "");
//			System.out.print("\n");
//		}
//	}	
}
