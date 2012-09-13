/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 Instituto de Desarrollo Regional and Generalitat Valenciana.
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
 *   Av. Blasco Ibañez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   Instituto de Desarrollo Regional (Universidad de Castilla La-Mancha)
 *   Campus Universitario s/n
 *   02071 Alabacete
 *   Spain
 *
 *   +34 967 599 200
 */

package org.gvsig.remotesensing.processtest;

import junit.framework.TestCase;

import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.buffer.RasterBufferInvalidException;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.grid.Grid;
import org.gvsig.raster.grid.GridExtent;
import org.gvsig.remotesensing.gridmath.GridMathProcess;
import org.gvsig.remotesensing.gridmath.gui.GridMathPanel;

/**
* Este test prueba el proceso de construcción de la imagen resultante al operar las 
* dos bandas, elemento a elemento, de la imagen pc_CreateImageTest.tif.
* 
* ** @author Alejandro Muñoz Sanchez (alejandro.munoz@uclm.es)
* */


public class TGridMathProcessTest extends TestCase {
	
	private String baseDir = "./test-images/";
	private String path1 = baseDir + "pc_CreateImageTest.tif";
	private RasterDataset f1 = null;
	private BufferFactory ds1 = null;
	
	static{
		RasterLibrary.wakeUp();	
	}
	
	public void start() {
		this.setUp();
		this.testStack();
	}
	
	public void setUp() {
		System.err.println("TGridMathProcessTest running...");
		try {
			f1 = RasterDataset.open(null, path1);
		} catch (NotSupportedExtensionException e) {
			System.out.print("Open file error..");
		} catch (RasterDriverException e) {
			System.out.print("Invalid access..");
		} 
		ds1 = new BufferFactory(f1);
		
	}
	public void testStack() {
		try {
			
	
			Grid g = new Grid(ds1,new int[]{0});
			Grid g2 = new Grid(ds1,new int[]{1});
			GridMathPanel cp= new GridMathPanel(null,null);
			cp.getCalculatorPanel().getQWindowsHash().put("b1", g);
			cp.getCalculatorPanel().getQWindowsHash().put("b2", g2);
			
			GridMathProcess proceso= new GridMathProcess();
			proceso.addParam("expresion", new String("b1+b2"));
			proceso.addParam("extent",(GridExtent)g.getGridExtent());
			
			// Se añaden las variables al hashmap
			cp.getCalculatorPanel().getQWindowsHash().put("b1", new Object[]{g.getRasterBuf(), new Integer(2)});		
			cp.getCalculatorPanel().getQWindowsHash().put("b2", new Object[]{g2.getRasterBuf(), new Integer(2)});
			proceso.addParam("params",cp.getCalculatorPanel().getQWindowsHash());		
			proceso.run();
			IBuffer result= (IBuffer)proceso.getResult();
			
			// Imagen monobanda
			assertEquals(result.getBandCount(),1);
				
			//Comparación de valores
			assertEquals(result.getElemDouble(0, 0,0),1390,0.1);
			assertEquals(result.getElemDouble(0, 1,0),1293,0.1);
			assertEquals(result.getElemDouble(0, 2,0),1295,0.1);
			assertEquals(result.getElemDouble(0, 3,0),1137,0.1);	
			
			assertEquals(result.getElemDouble(1, 0,0),1452,0.1);
			assertEquals(result.getElemDouble(1, 1,0),1490,0.1);
			assertEquals(result.getElemDouble(1, 2,0),1431,0.1);
			assertEquals(result.getElemDouble(1, 3,0),1172,0.1);
			
			assertEquals(result.getElemDouble(2, 0,0),1615,0.1);
			assertEquals(result.getElemDouble(2, 1,0),1650,0.1);
			assertEquals(result.getElemDouble(2, 2,0),1493,0.1);
			assertEquals(result.getElemDouble(2, 3,0),1333,0.1);	
			
			assertEquals(result.getElemDouble(3, 0,0),1420,0.1);
			assertEquals(result.getElemDouble(3, 1,0),1456,0.1);
			assertEquals(result.getElemDouble(3, 2,0),1495,0.1);
			assertEquals(result.getElemDouble(3, 3,0),1468,0.1);	
			
			
			
		} catch (RasterBufferInvalidException e1) {
			e1.printStackTrace();
		}
	}
	
	
	
}
