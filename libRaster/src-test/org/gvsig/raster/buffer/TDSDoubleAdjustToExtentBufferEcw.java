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
package org.gvsig.raster.buffer;

import org.gvsig.raster.BaseTestCase;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.InvalidSetViewException;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.RasterDriverException;
/**
 * Este test prueba el acceso a datos a traves de un DataSource sin resampleo
 * para un ECW con coordenadas reales. 
 * 
 * Lee el raster completo y comprueba que los datos leidos sean correctos 
 * comparando los valores de las cuatro esquinas y algunos valores dentro de la imagen.
 * Se lee un área más grande que el extent completo del raster para comprobar que el ajuste
 * al extent lo hace correctamente.
 * 
 * Después hace selecciona un área dentro de la imagen de 2x2 y compara que los valores
 * leidos sean correctos.
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class TDSDoubleAdjustToExtentBufferEcw extends BaseTestCase {
	private String        baseDir = "./test-images/";
	private String        path    = baseDir + "miniraster30x30.jp2";

	public void start() {
		this.setUp();
		this.testStack();
	}

	public void setUp() {
		System.err.println("TDSDoubleAdjustToExtentBufferEcw running...");
	}

	static {
		RasterLibrary.wakeUp();
	}

	public void testStack() {
		RasterDataset f;
		int[] drawableBands = { 0, 1, 2 };
		try {
			deleteRMF(path);
			f = RasterDataset.open(null, path);
		} catch (RasterDriverException e) {
			return;
		} catch (NotSupportedExtensionException e) {
			return;
		}
		BufferFactory ds = new BufferFactory(f);
		ds.setDrawableBands(drawableBands);
		//Se selecciona un área mayor que el extent para probar que lo ajusta a este 
		try {
			ds.setAreaOfInterest(645860.0, 4923870.0, 645986.0, 4923744.0, 10, 10);
		} catch (RasterDriverException e) {
			e.printStackTrace();
		} catch (InvalidSetViewException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		print(ds.getRasterBuf());
		dataTest1(ds.getRasterBuf());
				
		try {
			ds.setAreaOfInterest(645860.0, 4923870.0, 645986.0, 4923744.0, 2, 2);
		} catch (RasterDriverException e) {
			e.printStackTrace();
		} catch (InvalidSetViewException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		print(ds.getRasterBuf());
		dataTest2(ds.getRasterBuf());
	}

	private void dataTest1(IBuffer raster) {
		int band0[] = { 43, 14, 87, 166, 174, 109, 90, 93, 190, 143, 85, 93, 103, 112, 138, 94, 138, 192, 180, 196, 99, 209, 41, 98, 168, 170, 89, 141, 158, 199, 108, 71, 67, 92, 131, 176, 243, 98, 163, 141, 157, 170, 94, 159, 233, 114, 144, 62, 147, 173, 159, 173, 184, 94, 56, 18, 69, 78, 83, 115, 95, 37, 80, 57, 68, 68, 55, 59, 58, 101, 69, 44, 24, 58, 115, 40, 58, 56, 68, 111, 84, 117, 55, 88, 170, 135, 169, 99, 74, 42, 114, 223, 171, 67, 216, 104, 167, 83, 66, 45 };
		int band1[] = { 53, 15, 109, 154, 164, 111, 109, 93, 189, 151, 89, 102, 107, 111, 142, 100, 143, 195, 178, 190, 100, 192, 61, 108, 154, 172, 99, 149, 160, 206, 122, 74, 81, 94, 136, 195, 248, 105, 173, 141, 158, 162, 98, 158, 226, 114, 132, 66, 150, 176, 168, 172, 175, 105, 82, 39, 85, 89, 73, 104, 92, 51, 80, 71, 88, 84, 71, 74, 68, 106, 82, 62, 37, 74, 129, 49, 78, 67, 89, 126, 100, 132, 71, 107, 164, 145, 154, 108, 85, 48, 130, 210, 164, 66, 209, 115, 160, 85, 90, 61 };
		int band2[] = { 26, 11, 82, 145, 165, 97, 86, 71, 191, 135, 74, 75, 85, 110, 117, 82, 115, 186, 179, 183, 91, 181, 43, 85, 139, 178, 78, 133, 152, 206, 98, 61, 71, 109, 118, 181, 244, 82, 144, 117, 144, 159, 83, 155, 231, 98, 108, 60, 136, 163, 146, 170, 162, 111, 102, 41, 62, 60, 64, 68, 97, 26, 74, 50, 64, 70, 47, 48, 56, 70, 54, 35, 19, 50, 91, 35, 48, 52, 62, 101, 73, 91, 35, 80, 140, 119, 140, 91, 58, 36, 112, 202, 146, 66, 192, 81, 131, 71, 63, 37 };
		compareRaster(raster, band0, band1, band2);
	}

	private void dataTest2(IBuffer raster) {
		int band0[] = { 43, 109, 160, 18 };
		int band1[] = { 53, 111, 168, 39 };
		int band2[] = { 26, 97, 146, 41 };
		compareRaster(raster, band0, band1, band2);
	}

	public void compareRaster(IBuffer raster, int band0[], int band1[], int band2[]) {
		int cont = 0;
		for (int line = 0; line < raster.getHeight(); line++) {
			for (int col = 0; col < raster.getWidth(); col++) {
				assertEquals((int) (raster.getElemByte(line, col, 0) & 0xff), band0[cont]);
				assertEquals((int) (raster.getElemByte(line, col, 1) & 0xff), band1[cont]);
				assertEquals((int) (raster.getElemByte(line, col, 2) & 0xff), band2[cont]);
				cont++;
			}
		}
	}

	/**
	 * Imprime todos los pixels de la fuente de datos en RGB
	 */
	public void print(IBuffer raster) {
		System.out.println("");
		for (int band = 0; band < 3; band++) {
			System.out.print("int band" + band + "[] = { ");
			int cont = 0;
			for (int line = 0; line < raster.getHeight(); line++) {
				for (int col = 0; col < raster.getWidth(); col++) {
					if (cont != 0)
						System.out.print(", ");
					System.out.print(((int) (raster.getElemByte(line, col, band) & 0xff)));
					cont++;
				}
			}
			System.out.println("};");
		}
	}
}