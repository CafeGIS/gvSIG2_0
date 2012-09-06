/*
 * Created on 19-jul-2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
/* gvSIG. Sistema de Informaciï¿½n Geogrï¿½fica de la Generalitat Valenciana
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
 */
package org.gvsig.raster.dataset;

import junit.framework.TestCase;

import org.gvsig.raster.RasterLibrary;

/**
 * Este test que obtiene la información de un único pixel. Repetira esta
 * operación sobre varios pixels sobre una misma imagen comprobado que los resultados
 * sean correctos, es decir para la posición seleccionada existe ese pixel obtenido.
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class TestDataByPixelEcw extends TestCase {

	private String baseDir = "./test-images/";
	private String path1 = baseDir + "miniraster30x30.jp2";
	
	private RasterDataset f1 = null;
	
	public void start() {
		this.setUp();
		this.testStack();
	}
	
	static {
		RasterLibrary.wakeUp();
	}
	
	public void setUp() {
		System.err.println("TestDataByPixelEcw running...");
		try {
			f1 = RasterDataset.open(null, path1);
		} catch (NotSupportedExtensionException e1) {
			e1.printStackTrace();
		} catch (RasterDriverException e1) {
			e1.printStackTrace();
		}
		
	}
	
	public void testStack(){
		try {
			testPixelsValues();
		} catch (InvalidSetViewException e) {
			e.printStackTrace();
		} catch (FileNotOpenException e) {
			e.printStackTrace();
		} catch (RasterDriverException e1) {
			e1.printStackTrace();
		}
	}
	
	public void testPixelsValues()throws InvalidSetViewException, FileNotOpenException, RasterDriverException {
		assertEquals(((Integer)f1.getData(1, 1, 0)).intValue(), 73);
		assertEquals(((Integer)f1.getData(1, 1, 1)).intValue(), 87);
		assertEquals(((Integer)f1.getData(1, 1, 2)).intValue(), 72);
		
		assertEquals(((Integer)f1.getData(18, 16, 0)).intValue(), 61);
		assertEquals(((Integer)f1.getData(18, 16, 1)).intValue(), 74);
		assertEquals(((Integer)f1.getData(18, 16, 2)).intValue(), 57);
		
		assertEquals(((Integer)f1.getData(17, 23, 0)).intValue(), 172);
		assertEquals(((Integer)f1.getData(17, 23, 1)).intValue(), 154);
		assertEquals(((Integer)f1.getData(17, 23, 2)).intValue(), 140);
		
		assertEquals(((Integer)f1.getData(23, 3, 0)).intValue(), 188);
		assertEquals(((Integer)f1.getData(23, 3, 1)).intValue(), 186);
		assertEquals(((Integer)f1.getData(23, 3, 2)).intValue(), 189);
		
		assertEquals(((Integer)f1.getData(29, 29, 0)).intValue(), 126);
		assertEquals(((Integer)f1.getData(29, 29, 1)).intValue(), 134);
		assertEquals(((Integer)f1.getData(29, 29, 2)).intValue(), 111);
		
		assertEquals(((Integer)f1.getData(0, 29, 0)).intValue(), 175);
		assertEquals(((Integer)f1.getData(0, 29, 1)).intValue(), 175);
		assertEquals(((Integer)f1.getData(0, 29, 2)).intValue(), 147);
		
		assertEquals(((Integer)f1.getData(0, 0, 0)).intValue(), 43);
		assertEquals(((Integer)f1.getData(0, 0, 1)).intValue(), 53);
		assertEquals(((Integer)f1.getData(0, 0, 2)).intValue(), 26);
		
		assertEquals(((Integer)f1.getData(29, 0, 0)).intValue(), 255);
		assertEquals(((Integer)f1.getData(29, 0, 1)).intValue(), 252);
		assertEquals(((Integer)f1.getData(29, 0, 2)).intValue(), 246);
	}

}
