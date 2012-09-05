package org.gvsig.xmlschema.performance;

import java.io.File;

import junit.framework.TestCase;

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
 * $Id: MemoryUsageTest.java 151 2007-06-14 16:15:05Z jorpiell $
 * $Log$
 * Revision 1.1  2007/06/14 16:15:03  jorpiell
 * builds to create the jars generated and add the schema code to the libGPEProject
 *
 * Revision 1.1  2007/06/14 13:50:07  jorpiell
 * The schema jar name has been changed
 *
 * Revision 1.1  2007/05/30 12:25:48  jorpiell
 * Add the element collection
 *
 * Revision 1.1  2007/05/25 11:55:00  jorpiell
 * First update
 *
 *
 */
/**
 * This test parses some XSD files and calculates the
 * used memory for each of them. The parsing process
 * must to be implemented by the classes tha inherit
 * from this.
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public abstract class MemoryUsageTest extends TestCase {
	private static final int TIMES_TO_LOOP = 5;
	private static final float KB = 1024.0f;
	private static final float MB = 1048576.0f;
	private static final String SKB="KB";
	private static final String SMB="MB";
			
	private String file1 = "testdata/localidad.xsd";	
	private String file2 = "testdata/municipios.xsd";
	private String file3 = "testdata/WithSchemaLocationLink.xsd";
	private String file4 = "testdata/WFSDescribeFeatureType.xsd";
	private String file5 = "testdata/CityGML.xsd";
	

	public void test1() throws Exception{
		parseSchemaXTimes(file1);
	}
	
	public void test2() throws Exception{
		parseSchemaXTimes(file2);
	}
	
	public void test3() throws Exception{
		parseSchemaXTimes(file3);
	}
	
	public void test4() throws Exception{
		parseSchemaXTimes(file4);
	}
	
	public void test5() throws Exception{
		parseSchemaXTimes(file5);
	}

	
	/**
	 * Parser the same XSD file the number of times 
	 * specified by TIMES_TO_LOOP
	 * @param file
	 * XSD file to parse
	 * @throws Exception 
	 */
	private void parseSchemaXTimes(String file) throws Exception{
		float usedMemory = 0;
		float usedTime = 0;
		for (int i=0 ; i<TIMES_TO_LOOP ; i++){
			usedMemory = usedMemory + parseSchemaMemory(file);
			usedTime = usedTime + parseSchemaTime(file);
		}
		usedMemory = usedMemory / TIMES_TO_LOOP;
		usedTime = usedTime / TIMES_TO_LOOP;
		float divisor = (usedMemory<MB) ? KB : MB;
		String sufix = (usedMemory<MB) ? SKB : SMB;	
		usedMemory = usedMemory/divisor;		
		System.out.println(usedMemory + " " + sufix + " used to parse " + file + " wich size is " 
				+ new File(file).length()/KB + " KB in " + usedTime + " milis");
	}
	
	/**
	 * Parses one schema and returns the used memory
	 * @param file
	 * XSD file
	 * @return
	 * The memory
	 * @throws Exception 
	 */
	public float parseSchemaMemory(String file) throws Exception{
		//Call the garbage collector
		Runtime.getRuntime().gc();
		float totalBefore = Runtime.getRuntime().totalMemory();
	    float freeBefore = Runtime.getRuntime().freeMemory();
		//Parse the file
	    parse(file);
		float freeAfter = Runtime.getRuntime().freeMemory();
		float totalAfter = Runtime.getRuntime().totalMemory();
		float usedMemory = (totalAfter - freeAfter) - (totalBefore - freeBefore);		 	
		//Call the garbage collector
		Runtime.getRuntime().gc();
		return usedMemory;		
	}
	
	/**
	 * Parses one schema and returns the used time
	 * @param file
	 * XSD file
	 * @return
	 * The memory
	 * @throws Exception 
	 */
	public long parseSchemaTime(String file) throws Exception{
		long t1 = System.currentTimeMillis();
		//Parse the file
	    parse(file);
	    long t2 = System.currentTimeMillis();
		return t2 - t1;		
	}
	
	/**
	 * Parse the schema
	 * @param file
	 * XSD to parse
	 * @throws Exception 
	 */
	protected abstract void parse(String file) throws Exception;
	
}
