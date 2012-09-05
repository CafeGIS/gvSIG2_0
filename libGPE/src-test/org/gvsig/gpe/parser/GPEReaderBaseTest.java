package org.gvsig.gpe.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import junit.framework.TestCase;

import org.gvsig.gpe.GPELocator;
import org.gvsig.gpe.GPEManager;
import org.gvsig.gpe.containers.Layer;

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
 * $Id: GPEReaderBaseTest.java 173 2007-11-06 12:10:57Z jpiera $
 * $Log$
 * Revision 1.7  2007/05/09 06:54:07  jorpiell
 * Change the File by URI
 *
 * Revision 1.6  2007/04/20 12:04:10  csanchez
 * Actualizacion protoripo libGPE, AÃ±adidos test para el parser, parseo con XSOM
 *
 * Revision 1.5  2007/04/19 11:50:20  csanchez
 * Actualizacion protoripo libGPE
 *
 * Revision 1.4  2007/04/19 07:23:20  jorpiell
 * Add the add methods to teh contenhandler and change the register mode
 *
 * Revision 1.3  2007/04/14 16:06:35  jorpiell
 * Add the container classes
 *
 * Revision 1.2  2007/04/13 13:14:55  jorpiell
 * Created the base tests and add some methods to the content handler
 *
 * Revision 1.1  2007/04/13 07:17:54  jorpiell
 * Add the writting tests for the simple geometries
 *
 *
 */
/**
 * This class must be implementend by all the classes that
 * implements a GPE reader Parser. 
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public abstract class GPEReaderBaseTest extends TestCase {
	private File file = null;
	private GPEParser parser = null;
	private GPEContentHandler contenHandler = null;
	private GPEErrorHandler errorHandler = null;
	private String parserName="FORMAT VERSION";
	private String parserDescription="default parser description";
	private GPEManager gpeManager = null;

	public void setUp() throws Exception{
		GPELibraryTest lib = new GPELibraryTest();
		lib.initialize();
		lib.postInitialize();
		gpeManager = GPELocator.getGPEManager();
		System.out.println("INFO: Parser registrado");
		file = new File(getFile());
		System.out.println("INFO: Abriendo Fichero: "+file.getName());
		assertEquals(gpeManager.accept(file.toURI()),true);
		System.out.println("INFO: Existe parser registrado para el fomato ");
		parser = gpeManager.createParserByClass(getGPEParserClass().getName());
		System.out.println("INFO: Creado el parser ");
		assertNotNull(parser);
	}	
	
	/**
	 * @return the gpeManager
	 */
	public GPEManager getGpeManager() {
		return gpeManager;
	}
	
	/**
	 * This test parses the file and make the 
	 * asserts.
	 * @throws Exception
	 */
	public void testParse() throws Exception{
		parseFile();
		parseInputStream();		
	}
	
	/**
	 * Parses the file
	 */
	private void parseFile(){
		System.out.println("INFO: PARSING THE FILE...");
		parser.parse(getContenHandler() , getErrorHandler(), file.toURI());
		System.out.println("INFO: ¡¡¡ SUCCESS !!!");
		makeAsserts();
	}
	
	/**
	 * Parses the file like an inputstream
	 * @throws FileNotFoundException
	 */
	private void parseInputStream() throws Exception{
		InputStream is = getInputStream();
		errorHandler = null;
		contenHandler = null;
		System.out.println("INFO: PARSING THE INPUTSTREAM...");
		parser.parse(getContenHandler() , getErrorHandler(), is);
		System.out.println("INFO: ¡¡¡ SUCCESS !!!");
		makeAsserts();
	}	
	
	/**
	 * This method has to be override by the tests that use
	 * other InputStream (KMZ...)
	 * @return
	 * @throws FileNotFoundException
	 */
	public InputStream getInputStream() throws Exception{
		return new FileInputStream(new File(getFile()));
	}
	
	/**
	 * This method must be used by the subclasses
	 * to make the comparations. With getLayers
	 * it can retrieve the parsed layers
	 */
	public abstract void makeAsserts();
	
	/**
	 * Each test must to return its parser name
	 * to register it before to start the parsing
	 * process
	 */
	public String getGPEParserName(){
		return parserName;
	}
	
	/**
	 * Each test must to return its parser description
	 * to register it before to start the parsing
	 * process
	 */
	public String getGPEParserDescription(){
		return parserDescription ;
	}
	/**
	 * Each test must to return its parser name
	 * to register it before to start the parsing
	 * process
	 */
	public void setGPEParserName(String name){
		parserName=name;
	}
	
	/**
	 * Each test must to return its parser description
	 * to register it before to start the parsing
	 * process
	 */
	public void setGPEParserDescription(String description){
		parserDescription=description;
	}
	/**
	 * Each test must to return its parser class
	 * that will be used to create new parsers.
	 */
	public abstract Class getGPEParserClass();
	
	/**
	 * Gets the GML file to open
	 * @return
	 */
	public abstract String getFile();
	
	/**
	 * Gets a list of parsed layers
	 * @return
	 */
	public Layer[] getLayers(){
		ArrayList layers = ((GPEContentHandlerTest)parser.getContentHandler()).getLayers();
		Layer[] aLayers = new Layer[layers.size()];
		for (int i=0 ; i<layers.size() ; i++){
			aLayers[i] = (Layer)layers.get(i);
		}
		return aLayers;
	}
	
	/**
	 * @return the contenHandler
	 */
	public GPEContentHandler getContenHandler() {
		if (contenHandler == null){
			contenHandler = new GPEContentHandlerTest();
		}
		return contenHandler;
	}

	/**
	 * @return the errorHandler
	 */
	public GPEErrorHandler getErrorHandler() {
		if (errorHandler == null){
			errorHandler = new GPEErrorHandlerTest();
		}
		return errorHandler;
	}
	
}
