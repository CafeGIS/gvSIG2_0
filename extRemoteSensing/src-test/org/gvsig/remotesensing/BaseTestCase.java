/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2007 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.remotesensing;

import java.awt.geom.Point2D;
import java.io.File;

import junit.framework.TestCase;

import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.dataset.FileNotFoundInListException;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.IRasterDataSource;
import org.gvsig.raster.dataset.InvalidSetViewException;
import org.gvsig.raster.dataset.MultiRasterDataset;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.dataset.properties.DatasetColorInterpretation;
/**
 * Clase base para todos los tests. Contiene métodos de uso común.
 * Los errores no se capturan. Se lanzan en una traza por consola.
 * 
 * 07/05/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class BaseTestCase extends TestCase {
	/**
	 * Directorio donde están las imagenes de pruebas
	 */
	protected String               baseDir       = "./test-images/";
	/**
	 * Directorio para la generación de imagenes temporales en los tests
	 */
	protected String               tempDir       = "/tmp"; 
	
	protected MultiRasterDataset   dataset       = null;
	protected String               out           = null;
	protected long                 t1, t2;
	
	static {
		RasterLibrary.wakeUp();
	}
	
	/**
	 * Resetea el contador de tiempo
	 */
	protected void resetTime() {
		t1 = System.currentTimeMillis();
	}
	
	/**
	 * Obtiene el tiempo transcurrido desde que se reseteo el tiempo
	 * @return
	 */
	protected double getTime() {
		t2 = System.currentTimeMillis();
		return ((t2 - t1) / 1000D);
	}
	
	/**
	 * Abre el dataset
	 * @param s
	 */
	protected MultiRasterDataset open(String s) {
		try {
			dataset = MultiRasterDataset.open(null, s);
			return dataset;
		} catch (NotSupportedExtensionException e1) {
			e1.printStackTrace();
		} catch (RasterDriverException e1) {
			e1.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Abre el dataset
	 * @param s
	 */
	protected MultiRasterDataset open(String[] s) {
		try {
			if(s == null)
				return null;
			for (int i = 0; i < s.length; i++) {
				if(i == 0)
					dataset = MultiRasterDataset.open(null, s[0]);
				else
					dataset.addDataset(new String[]{s[i]});
				
			}
			return dataset;
		} catch (NotSupportedExtensionException e1) {
			e1.printStackTrace();
		} catch (RasterDriverException e1) {
			e1.printStackTrace();
		} catch (FileNotFoundInListException e2) {
			e2.printStackTrace();
		}
		return null;
	}

	/**
	 * Borra el RMF de un fichero raster
	 * @param file
	 */
	protected void deleteRMF(String file) {
		int last = file.lastIndexOf(".");
		if (last == -1)
			last = file.length();

		File fichero = new File(file.substring(0, last) + ".rmf");
		fichero.delete();
	}
	
	/**
	 * Obtiene un nombre aleatorio para fichero temporal
	 * @return
	 */
	public String getFileTemp() {
		out = tempDir + "/test-" + System.currentTimeMillis();
		return out;
	}
	
	/**
	 * Crea un objeto de interpretación de color
	 * @param nBands
	 * @return
	 */
	protected DatasetColorInterpretation getColorInterpretation(int nBands) {
		String[] ci = new String[nBands];
		if(nBands == 1)
			ci[0] = DatasetColorInterpretation.GRAY_BAND;
		else {
			for (int j = 0; j < ci.length; j++) {
				switch (j) {
				case 0: ci[j] = DatasetColorInterpretation.RED_BAND; break;
				case 1: ci[j] = DatasetColorInterpretation.GREEN_BAND; break;
				case 2: ci[j] = DatasetColorInterpretation.BLUE_BAND; break;
				default: ci[j] = DatasetColorInterpretation.UNDEF_BAND; break;
				}
			}
		}
		return new DatasetColorInterpretation(ci);
	}
	
	/**
	 * Compara un dataset completo (ds2) con una parte de otro dataset (ds1)
	 * @param ds1
	 * @param ds2
	 * @param coordsDs1 Coordenada x, y del dataset 1 a partir de la cual empieza la comparación
	 * @param step Proporción de tamaño del dataset 2 con respecto al 1. Si el step es 2 quiere decir que el 
	 * dataset 2 es el doble que el 1
	 * @param drawableBands
	 * @param dataType
	 */
	protected void compareDatasets(IRasterDataSource ds1, IRasterDataSource ds2, Point2D coordsDs1, int step, int[] drawableBands, int dataType) {
		BufferFactory bufferFactory1 = new BufferFactory(ds1);
		bufferFactory1.setDrawableBands(drawableBands);
		BufferFactory bufferFactory2 = new BufferFactory(ds2);
		bufferFactory2.setDrawableBands(drawableBands);
		try {
			bufferFactory1.setAreaOfInterest((int)coordsDs1.getX(), (int)coordsDs1.getY(), (int)(ds2.getWidth() / step), (int)(ds2.getHeight() / step));
			IBuffer buf1 = bufferFactory1.getRasterBuf();
			bufferFactory2.setAreaOfInterest();
			IBuffer buf2 = bufferFactory2.getRasterBuf();
			for (int band = 0; band < buf1.getBandCount(); band++) {
				for (int row = 0; row < (buf2.getHeight() / step); row++) {
					for (int col = 0; col < (buf2.getWidth() / step); col++) {
						switch (dataType) {
						case IBuffer.TYPE_BYTE: assertEquals(buf1.getElemByte(row, col, band), buf2.getElemByte(row * step, col * step, band)); break;
						case IBuffer.TYPE_SHORT: assertEquals(buf1.getElemShort(row, col, band), buf2.getElemShort(row * step, col * step, band)); break;
						case IBuffer.TYPE_INT: assertEquals(buf1.getElemInt(row, col, band), buf2.getElemInt(row * step, col * step, band)); break;
						case IBuffer.TYPE_FLOAT: assertEquals((int)buf1.getElemFloat(row, col, band), (int)buf2.getElemFloat(row * step, col * step, band)); break;
						case IBuffer.TYPE_DOUBLE: assertEquals((int)buf1.getElemDouble(row, col, band), (int)buf2.getElemDouble(row * step, col * step, band)); break;
						}
					}
				}
			}
		} catch (RasterDriverException e) {
			e.printStackTrace();
		} catch (InvalidSetViewException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Compara la banda de un dataset completo (ds2) con una parte de otro dataset (ds1)
	 * @param banda del dataset 1 que corresponde con el dataset 2
	 * @param ds1
	 * @param ds2
	 * @param coordsDs1 Coordenada x, y del dataset 1 a partir de la cual empieza la comparación
	 * @param step Proporción de tamaño del dataset 2 con respecto al 1. Si el step es 2 quiere decir que el 
	 * dataset 2 es el 
	 * @param drawableBands
	 * @param dataType
	 */
	protected void compareDatasets(int band, IRasterDataSource ds1, IRasterDataSource ds2, Point2D coordsDs1, int step, int[] drawableBands, int dataType) {
		BufferFactory bufferFactory1 = new BufferFactory(ds1);
		bufferFactory1.setDrawableBands(drawableBands);
		BufferFactory bufferFactory2 = new BufferFactory(ds2);
		bufferFactory2.setDrawableBands(drawableBands);
		try {
			bufferFactory1.setAreaOfInterest((int)coordsDs1.getX(), (int)coordsDs1.getY(), (int)(ds2.getWidth() / step), (int)(ds2.getHeight() / step));
			IBuffer buf1 = bufferFactory1.getRasterBuf();
			bufferFactory2.setAreaOfInterest();
			IBuffer buf2 = bufferFactory2.getRasterBuf();
			for (int row = 0; row < buf2.getHeight(); row++) {
				for (int col = 0; col < buf2.getWidth(); col++) {
					switch (dataType) {
					case IBuffer.TYPE_BYTE: assertEquals(buf1.getElemByte(row, col, band), buf2.getElemByte(row * step, col * step, 0)); break;
					case IBuffer.TYPE_SHORT: assertEquals(buf1.getElemShort(row, col, band), buf2.getElemShort(row * step, col * step, 0)); break;
					case IBuffer.TYPE_INT: assertEquals(buf1.getElemInt(row, col, band), buf2.getElemInt(row * step, col * step, 0)); break;
					case IBuffer.TYPE_FLOAT: assertEquals((int)buf1.getElemFloat(row, col, band), (int)buf2.getElemFloat(row * step, col * step, 0)); break;
					case IBuffer.TYPE_DOUBLE: assertEquals((int)buf1.getElemDouble(row, col, band), (int)buf2.getElemDouble(row * step, col * step, 0)); break;
					}
				}
			}
		} catch (RasterDriverException e) {
			e.printStackTrace();
		} catch (InvalidSetViewException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Detiene la ejecución del thread actual durante n milisegundos
	 * @param n Numero de milisegundos detenido
	 */
	protected void pause(int n) {
		try {
			Thread.sleep(n);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
