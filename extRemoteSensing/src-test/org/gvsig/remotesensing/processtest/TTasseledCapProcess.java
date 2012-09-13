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

import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.buffer.RasterBufferInvalidAccessException;
import org.gvsig.raster.buffer.RasterBufferInvalidException;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.grid.Grid;
import org.gvsig.remotesensing.tasseledcap.TasseledCapProcess;


/**
 * Este test prueba el proceso de calculo de la trasformacion Tasseled Cap.
 * A partir del fichero "bhtmref.img", se construye un grid con las bandas correspondientes
 * (las 6 bandas de la imagen para el caso de LandSat TM y ETM y las 4 primeras bandas para el caso de MSS).
 * Construido el grid se lanza el calculo y se comparan los resultados con los ficheros  que se
 * detallan a continuación calculados previamente con Envi.
 * Para el caso de la Imagen LandSat TM, se compara el resultado con Imagen: LandSatTM_Envi
 * Para el caso de la Imagen LandSat ETM, se compara el resultado con Imagen LandSatETM_Envi
 * Para el caso de la Imagen LandSat MSS, se compara el resultado con Imagen LandSatMSS_Envi.tif
 *
 * @author Alejandro Muñoz Sanchez (alejandro.munoz@uclm.es)
 * */

public class TTasseledCapProcess extends TestCase {

	private String baseDir = "./test-images/";
	private String path1 = baseDir + "bhtmref.img";
	private String path2 = baseDir + "LandSatTM_Envi";
	private String path3 = baseDir + "LandSatETM_Envi";
	private String path4 = baseDir + "LandSatMSS_Envi";
	private RasterDataset f2,f3,f4 = null;
	private BufferFactory ds2,ds3,ds4 = null;
	private FLyrRasterSE lyr1 = null;

	static{
		RasterLibrary.wakeUp();
	}

	public void start() {
		this.setUp();
	    this.testStack();
	}

	public void setUp() {
		System.err.println("TTasseledCapProcess running...");

		try {
			lyr1 = FLyrRasterSE.createLayer(
					path1,
					path1,
					null
					);
		} catch (LoadLayerException e) {
			System.out.print("Error en la construcción de la capa");
		}

		try {
			f2=	RasterDataset.open(null,path2);
			f3 = RasterDataset.open(null,path3);
			f4= RasterDataset.open(null, path4);
		} catch (NotSupportedExtensionException e) {
			e.printStackTrace();
		} catch (RasterDriverException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ds2=  new BufferFactory (f2);
		ds3=  new BufferFactory (f3);
		ds4=  new BufferFactory (f4);

	}

	public void testStack() {

		Grid enviResultTM=null;
		Grid enviResultETM=null;
		Grid enviResultMSS=null;

		try {
			 enviResultTM= new Grid(ds2);
			 enviResultETM= new Grid(ds3);
			 enviResultMSS= new Grid (ds4);

		} catch (RasterBufferInvalidException e) {
			e.printStackTrace();
		}


//		**Caso LandSat MSS
		TasseledCapProcess mssProcess= new TasseledCapProcess();
		mssProcess.addParam("bands", new int[]{0,1,2,3});
		mssProcess.addParam("layer",lyr1);
		mssProcess.addParam("type",new Integer(0));
		mssProcess.run();
		//try {
		//	compare(mssProcess.getBufferResult(),enviResultMSS.getRasterBuf());
		//} catch (RasterBufferInvalidAccessException e) {
		//	e.printStackTrace();
		//}



		//**Caso LandSat TM
		TasseledCapProcess tmProcess= new TasseledCapProcess();
		tmProcess.addParam("bands", new int[]{0,1,2,3,4,5});
		tmProcess.addParam("layer",lyr1);
		tmProcess.addParam("type",new Integer(1));
		tmProcess.run();
		try {
			compare(tmProcess.getBufferResult(),enviResultTM.getRasterBuf());
		} catch (RasterBufferInvalidAccessException e) {
			e.printStackTrace();
		}


		//**Caso LandSat ETM
		TasseledCapProcess etmProcess= new TasseledCapProcess();
		etmProcess.addParam("bands", new int[]{0,1,2,3,4,5});
		etmProcess.addParam("layer",lyr1);
		etmProcess.addParam("type",new Integer(2));
		etmProcess.run();
		try {
			compare(etmProcess.getBufferResult(),enviResultETM.getRasterBuf());
		} catch (RasterBufferInvalidAccessException e) {
			e.printStackTrace();
		}


	}

	//**Metodo que compara celda a celda cada uno de los valores de dos grids.
	 // Para imagenes LandSat ETM, la sexta banda no se compara porque envi realiza una transformacion
	 // para evitar los numeros negativos *//*
	private void compare(IBuffer buffer, IBuffer buffer2) throws RasterBufferInvalidAccessException {
		assertEquals(buffer.getBandCount(),buffer2.getBandCount());
		for(int band=0; band<buffer.getBandCount();band++){
			if(band<5){
				for(int line = 0; line < buffer2.getHeight(); line++){
					for(int col = 0; col < buffer2.getWidth(); col++){
						assertEquals(buffer2.getElemFloat(col,line,band), buffer.getElemFloat(col, line,band),0.1);
					}
				}
			}

		}
	}
}
