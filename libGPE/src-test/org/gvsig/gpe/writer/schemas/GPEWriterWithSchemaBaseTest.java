package org.gvsig.gpe.writer.schemas;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.gvsig.gpe.writer.GPEWriterBaseTest;
import org.gvsig.xmlschema.som.IXSSchema;
import org.gvsig.xmlschema.utils.DOMObjectsFactory;
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
 * $Id: GPEWriterWithSchemaBaseTest.java 173 2007-11-06 12:10:57Z jpiera $
 * $Log$
 * Revision 1.8  2007/07/02 10:00:46  jorpiell
 * The generated xsd schemas have to be valid
 *
 * Revision 1.7  2007/06/29 12:20:41  jorpiell
 * The schema validation is made independently of the concrete writer
 *
 * Revision 1.6  2007/06/29 12:19:14  jorpiell
 * The schema validation is made independently of the concrete writer
 *
 * Revision 1.5  2007/06/22 12:38:59  jorpiell
 * The typeNotFoundException has been deleted. It never was thrown
 *
 * Revision 1.4  2007/06/22 12:21:18  jorpiell
 * The typeNotFoundException has been deleted. It never was thrown
 *
 * Revision 1.3  2007/06/14 13:50:06  jorpiell
 * The schema jar name has been changed
 *
 * Revision 1.2  2007/06/07 14:55:21  jorpiell
 * Add the schema support
 *
 * Revision 1.1  2007/06/07 14:52:28  jorpiell
 * Add the schema support
 *
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public abstract class GPEWriterWithSchemaBaseTest extends GPEWriterBaseTest{
	private IXSSchema schema = null;
	private String schemafileName = "SCHEMAFILETEMP";
	private SchemaDocumentBuilder documentBuilder = null;
	private DOMObjectsFactory elementsFactory = null;
	private File schemaFile = null;
	
	/**
	 * Initialize the schemas factory
	 */
	public void setUp() throws Exception{
		documentBuilder = SchemaDocumentBuilder.getInstance();
		schema = documentBuilder.createXSSchema(getNamespaceURI(),getNamespacePrefix());	
		elementsFactory = DOMObjectsFactory.getInstance();
		super.setUp();
	}	
	
	/**
	 * Delete the schema file
	 */
	public void tearDown() throws Exception{
		getSchemaFile().delete();
		super.tearDown();
	}
	
	/**
	 * Writes the schema a parses the file
	 */
	public void testWriter() throws Exception{
		writeSchema();
		getSchema().write(new FileOutputStream(getSchemaFile()));
		getGpeManager().setProperty("schemaName", getSchemaFile().getAbsolutePath());
		//getWriterHandler().getSchemaDocument().addSchema(
		//		new URI(getSchemaFile().getPath()), 
		//		getSchema());
		//getWriterHandler().getSchemaDocument().setTargetNamespace(getNamespaceURI());
		super.testWriter();
	}
	
	/**
	 * Writes the schema
	 * @throws TypeNotFoundException
	 */
	public abstract void writeSchema();
	
	/**
	 * Gets the namespace URI
	 * @return
	 */
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

	/**
	 * @return the schemaFile
	 * @throws IOException 
	 */
	public File getSchemaFile() throws IOException {
		if (schemaFile == null){
			schemaFile = new File(schemafileName);					
		}
		return schemaFile;
	}
	
	/**
	 * @return the elementsFactory
	 */
	public DOMObjectsFactory getElementsFactory() {
		return elementsFactory;
	}

	/**
	 * @return the schema
	 */
	public IXSSchema getSchema() {
		return schema;
	}
	
	
}
