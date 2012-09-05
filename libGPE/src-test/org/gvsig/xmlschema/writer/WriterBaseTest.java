package org.gvsig.xmlschema.writer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.gvsig.xmlschema.XMLSchemaBaseTest;
import org.gvsig.xmlschema.exceptions.SchemaCreationException;
import org.gvsig.xmlschema.exceptions.SchemaWrittingException;
import org.gvsig.xmlschema.som.IXSSchema;
import org.gvsig.xmlschema.utils.DOMObjectsFactory;
import org.gvsig.xmlschema.utils.SchemaDocumentBuilder;
import org.xml.sax.SAXException;

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
 * $Id: WriterBaseTest.java 164 2007-07-02 10:00:46Z jorpiell $
 * $Log$
 * Revision 1.3  2007/07/02 09:59:03  jorpiell
 * The generated xsd schemas have to be valid
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
public abstract class WriterBaseTest extends XMLSchemaBaseTest {
	private IXSSchema schema = null;
	private String fileName = "FILETEMP.xsd";
	private SchemaDocumentBuilder documentBuilder = null;
	private DOMObjectsFactory elementsFactory = null;

	/**
	 * @return the documentBuilder
	 */
	public SchemaDocumentBuilder getDocumentBuilder() {
		return documentBuilder;
	}

	/**
	 * @return the elementsFactory
	 */
	public DOMObjectsFactory getElementsFactory() {
		return elementsFactory;
	}

	public void setUp() throws Exception{
		super.setUp();
		documentBuilder = SchemaDocumentBuilder.getInstance();
		schema = documentBuilder.createXSSchema(getNamespaceURI(),getNamespacePrefix());	
		elementsFactory = DOMObjectsFactory.getInstance();
	}
	
	public void tearDown() throws Exception{
		new File(fileName).delete();
	}
	
	public void testCompare() throws SchemaWrittingException, ParserConfigurationException, SAXException, IOException, SchemaCreationException{
		writeSchema();
		schema.write(new FileOutputStream(fileName));
		schema = SchemaDocumentBuilder.getInstance().parse(new FileInputStream(fileName));
		readSchema();
	}	
	
	/**
	 * @return the schema
	 */
	public IXSSchema getSchema() {
		return schema;
	}
	
	public String getNamespaceURI(){
		return "http://www.gvsig.org/cit";
	}
	
	/**
	 * Gets the namespace prefix
	 * @return
	 */
	public String getNamespacePrefix(){
		return "cit";
	}
	
	public abstract void writeSchema();
	
	public abstract void readSchema();


}
