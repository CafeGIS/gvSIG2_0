package org.gvsig.gpe.writer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import junit.framework.TestCase;

import org.gvsig.gpe.GPELocator;
import org.gvsig.gpe.GPEManager;
import org.gvsig.gpe.containers.Layer;
import org.gvsig.gpe.parser.GPEContentHandler;
import org.gvsig.gpe.parser.GPEContentHandlerTest;
import org.gvsig.gpe.parser.GPEErrorHandler;
import org.gvsig.gpe.parser.GPEErrorHandlerTest;
import org.gvsig.gpe.parser.GPELibraryTest;
import org.gvsig.gpe.parser.GPEParser;

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
 * $Id: GPEWriterBaseTest.java 262 2007-12-14 08:11:39Z jpiera $
 * $Log$
 * Revision 1.14  2007/06/22 12:38:59  jorpiell
 * The typeNotFoundException has been deleted. It never was thrown
 *
 * Revision 1.12  2007/06/07 14:52:28  jorpiell
 * Add the schema support
 *
 * Revision 1.11  2007/05/16 09:28:19  jorpiell
 * The polygons has to be closed
 *
 * Revision 1.10  2007/05/15 12:10:44  jorpiell
 * Outpout file deleted
 *
 * Revision 1.9  2007/05/15 09:52:00  jorpiell
 * The namespace is deleted from the element name
 *
 * Revision 1.8  2007/05/09 06:54:07  jorpiell
 * Change the File by URI
 *
 * Revision 1.7  2007/05/02 11:46:07  jorpiell
 * Writing tests updated
 *
 * Revision 1.6  2007/04/26 14:39:12  jorpiell
 * Add some tests
 *
 * Revision 1.5  2007/04/19 11:50:20  csanchez
 * Actualizacion protoripo libGPE
 *
 * Revision 1.4  2007/04/19 07:23:20  jorpiell
 * Add the add methods to teh contenhandler and change the register mode
 *
 * Revision 1.3  2007/04/17 06:27:20  jorpiell
 * Changed the default filename
 *
 * Revision 1.2  2007/04/14 16:06:35  jorpiell
 * Add the container classes
 *
 * Revision 1.1  2007/04/13 07:17:54  jorpiell
 * Add the writting tests for the simple geometries
 *
 *
 */
/**
 * This class must be implementend by all the classes that
 * implements a GPE writer Parser. It creates a writer, write some
 * features and then uses a reader to compare the writting 
 * process
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public abstract class GPEWriterBaseTest extends TestCase{
	private GPEWriterHandler writerHandler = null;
	private GPEContentHandler contenHandler = null;
	private GPEErrorHandler errorHandler = null;
	private GPEParser parser = null;
	private File outputFile = null;
	private GPEManager gpeManager = null;

	/**
	 * Register the driver and gets the handler
	 * @throws Exception 
	 */
	public void setUp() throws Exception{
		GPELibraryTest lib = new GPELibraryTest();
		lib.initialize();
		lib.postInitialize();	
		gpeManager = GPELocator.getGPEManager();
		outputFile = new File(this.getClass().getName() + Math.random());
	}	
	
	/**
	 * @return the gpeManager
	 */
	public GPEManager getGpeManager() {
		return gpeManager;
	}
	
	/**
	 * Delete the file
	 */
	public void tearDown() throws Exception{
		System.out.println("******** WARNINGS ********");
		for (int i=0 ; i<getErrorHandler().getWarningsSize(); i++){
			System.out.println(getErrorHandler().getWarningAt(i));
		}	
	}
	
	/**
	 * This test writes some objects into the file and then
	 * try to read the created file. It compare that the written
	 * objects are the same that the read them
	 * @throws Exception 
	 */
	public void testWriter() throws Exception{
		OutputStream os = createOutputStream(outputFile);
		getWriterHandler().setOutputStream(os);
		writeObjects();		
		
		parser = gpeManager.createParserByClass(getGPEParserClass().getName());
		InputStream is = createInputStream(outputFile);
		parser.parse(getContenHandler(),getErrorHandler() ,is);
		readObjects();
		
		outputFile.delete();		
	}
	
	protected OutputStream createOutputStream(File file) throws FileNotFoundException{
		return new FileOutputStream(file);
	}
	
	protected InputStream createInputStream(File file) throws FileNotFoundException{
		return new FileInputStream(file);
	}	
	
	/**
	 * This method write somo objects into the writer handler
	 */
	public abstract void writeObjects();
	
	/**
	 * It read the objects and make all the comparations
	 *
	 */
	public abstract void readObjects();
			
	/**
	 * Each test must to return its parser name
	 * to register it before to start the parsing
	 * process
	 */
	public String getGPEWriterHandlerName(){
		return "FORMAT VERSION";
	}
	
	/**
	 * Each test must to return its parser description
	 * to register it before to start the parsing
	 * process
	 */
	public String getGPEWriterHandlerDescription(){
		return "default writer handler description";
	}
	
	/**
	 * Each test must to return its parser class
	 * that will be used to create new parsers.
	 */
	public abstract Class getGPEWriterHandlerClass();
	
	
	/**
	 * Each test must to return its parser name
	 * to register it before to start the parsing
	 * process
	 */
	public String getGPEParserName(){
		return "FORMAT VERSION";
	}
	
	/**
	 * Each test must to return its parser description
	 * to register it before to start the parsing
	 * process
	 */
	public String getGPEParserDescription(){
		return "default parser description";
	}
	
	/**
	 * Each test must to return its parser class
	 * that will be used to create new parsers.
	 */
	public abstract Class getGPEParserClass();
	
	/**
	 * Gets the file format. The deafult writer
	 * format will be used by default
	 * @return
	 */
	public String getFormat(){
		return null;
	}
	
	/**
	 * It creates a random point
	 * @param length
	 * @return
	 */
	protected double generateRandomPoint(){
		return Math.random();		
	}
	
	/**
	 * It creates a Random bbox coordinates. It return 
	 * 10 coordinates 
	 * @return
	 */
	protected double[] generateRandomCoordinates(){
		return generateRandomCoordinates(10);
	}
	
	/**
	 * It creates a Random bbox coordinates
	 * @param length
	 * The number of coordinates
	 * @return
	 */
	protected double[] generateRandomCoordinates(int length){
		double[] coord = new double[length];
		for (int i=0 ; i<coord.length ; i++){
			coord[i] = generateRandomPoint();
		}
		return coord;
	}
	
	/**
	 * It creates a Random linear ring. It return 
	 * 10 coordinates 
	 * @return
	 */
	protected double[] generateRandomLinearRing(){
		return generateRandomLinearRing(10);
	}
	
	/**
	 * It creates a random linear ring
	 * @param length
	 * @return
	 */
	protected double[] generateRandomLinearRing(int length){
		double[] coord = new double[length];
		for (int i=0 ; i<coord.length-1 ; i++){
			coord[i] = generateRandomPoint();
		}
		coord[length-1] = coord[0];
		return coord;
	}
	
	/**
	 * It creates a Random bbox coordinates
	 * @return
	 */
	protected double[] generateRandomBBox(){
		double[] coord = new double[2];
		for (int i=0 ; i<coord.length ; i++){
			coord[i] = generateRandomPoint();
		}
		return coord;
	}
	
	/**
	 * Closes a polygon
	 * @param x
	 * Polygon coordinates
	 * @return
	 * A closed polygon
	 */
	protected double[] closePolygon(double[] x){
		double[] xClosed = new double[x.length + 1];
		System.arraycopy(x, 0, xClosed, 0, x.length);
		xClosed[xClosed.length -1] = x[0];
		return xClosed;
	}

	/**
	 * @return the handler
	 */
	public GPEWriterHandler getWriterHandler() {
		if (writerHandler == null){
			try {
				writerHandler = gpeManager.createWriterByClass(getGPEWriterHandlerClass().getName());
				writerHandler.setErrorHandler(getErrorHandler());
			} catch (Exception e) {
				//never throwed
				e.printStackTrace();
			}			
		}
		return writerHandler;
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
}
