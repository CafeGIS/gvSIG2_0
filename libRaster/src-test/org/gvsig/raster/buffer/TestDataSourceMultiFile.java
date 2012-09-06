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
package org.gvsig.raster.buffer;

import junit.framework.TestCase;

import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.RasterDriverException;

/**
 * Este test prueba la gestión de multifichero de la clase RasterMultiFile
 * y que incorpora un Grid, así como la gestión de bandas que tiene BandList
 * y que incorpora GeoRasterMultiFile.
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class TestDataSourceMultiFile extends TestCase{
	private String baseDir = "./test-images/";

	private String path1 = baseDir + "band1-30x28byte.tif";
	private String path2 = baseDir + "band2-30x28byte.tif";
	private String path3 = baseDir + "band3-30x28byte.tif";
	
	private RasterDataset f1 = null;
	private RasterDataset f2 = null;
	private RasterDataset f3 = null;
	
	private BufferFactory ds = null;
	
	public void start(){
		this.setUp();
		this.testStack();
	}
	
	static{
		RasterLibrary.wakeUp();
	}
	
	public void setUp() {
		System.err.println("TestDataSourceMultiFile running...");
		try {
			f1 = RasterDataset.open(null, path1);
			f2 = RasterDataset.open(null, path2);
			f3 = RasterDataset.open(null, path3);
			//f4 = RasterDataset.openFile(null, path4);
		} catch (NotSupportedExtensionException e) {
			e.printStackTrace();
		} catch (RasterDriverException e) {
			e.printStackTrace();
		}
	}
	
	public void testStack(){
	
		//EL CONSTRUCTOR AÑADE FICHERO 1
		ds = new BufferFactory(f1);
		//Comprobación de número y nombre de ficheros
		String[] fileList = ds.getDataSource().getNameDatasetStringList(0, 0);
		assertEquals(fileList.length, 1);
		assertEquals(fileList[0], path1);
		//Comprobación de bandas
		String[] bandList = ds.getDataSource().getBands().getBandStringList();
		assertEquals(bandList.length, 1);
		assertEquals(bandList[0], path1);
		
		//AÑADIMOS FICHERO 2
		ds.addFile(f2);
		fileList = ds.getDataSource().getNameDatasetStringList(0, 0);
		//Comprobación de número y nombre de ficheros
		assertEquals(fileList.length, 2);
		assertEquals(fileList[0], path1);
		assertEquals(fileList[1], path2);
		//Comprobación de bandas
		bandList = ds.getDataSource().getBands().getBandStringList();
		assertEquals(bandList.length, 2);
		assertEquals(bandList[0], path1);
		assertEquals(bandList[1], path2);
		
		//AÑADIMOS FICHERO 3
		ds.addFile(f3);
		fileList = ds.getDataSource().getNameDatasetStringList(0, 0);
		//Comprobación de número y nombre de ficheros
		assertEquals(fileList.length, 3);
		assertEquals(fileList[0], path1);
		assertEquals(fileList[1], path2);
		assertEquals(fileList[2], path3);
		//Comprobación de bandas
		bandList = ds.getDataSource().getBands().getBandStringList();
		assertEquals(bandList.length, 3);
		assertEquals(bandList[0], path1);
		assertEquals(bandList[1], path2);
		assertEquals(bandList[2], path3);
		//Posicion dentro del fichero
		int[] bandPos = ds.getDataSource().getBands().getBandPositionList();
		for(int i = 0; i < bandPos.length; i++)
			assertEquals(bandPos[i], 0);
		
		//ELIMINAMOS FICHERO 2
		ds.removeFile(f2);
		fileList = ds.getDataSource().getNameDatasetStringList(0, 0);
		//Comprobación de número y nombre de ficheros
		assertEquals(fileList.length, 2);
		assertEquals(fileList[0], path1);
		assertEquals(fileList[1], path3);
		//Comprobación de bandas
		bandList = ds.getDataSource().getBands().getBandStringList();
		assertEquals(bandList.length, 2);
		assertEquals(bandList[0], path1);
		assertEquals(bandList[1], path3);
		
		//ELIMINAMOS FICHERO 1
		ds.removeFile(f1);
		fileList = ds.getDataSource().getNameDatasetStringList(0, 0);
		//Comprobación de número y nombre de ficheros
		assertEquals(fileList.length, 1);
		assertEquals(fileList[0], path3);
		//Comprobación de bandas
		bandList = ds.getDataSource().getBands().getBandStringList();
		assertEquals(bandList.length, 1);
		assertEquals(bandList[0], path3);
		
		//ELIMINAMOS FICHERO 3
		ds.removeFile(f3);
		fileList = ds.getDataSource().getNameDatasetStringList(0, 0);
		//Comprobación de número y nombre de ficheros
		assertEquals(fileList.length, 0);
		//Comprobación de bandas
		bandList = ds.getDataSource().getBands().getBandStringList();
		assertEquals(bandList.length, 0);

		//AÑADIMOS FICHERO 4
		/*ds.addFile(f4);
		fileList = ds.getDataSource().getNameDatasetStringList();
		//Comprobación de número y nombre de ficheros
		assertEquals(fileList.length, 1);
		assertEquals(fileList[0], path4);
		//Comprobación de bandas
		bandList = ds.getDataSource().getBands().getBandStringList();
		assertEquals(bandList.length, 4);
		assertEquals(bandList[0], path4);
		assertEquals(bandList[1], path4);
		assertEquals(bandList[2], path4);
		assertEquals(bandList[3], path4);
		//Posicion dentro del fichero
		bandPos = ds.getDataSource().getBands().getBandPositionList();
		for(int i = 0; i < bandPos.length; i++)
			assertEquals(bandPos[i], i);*/
	}

}
