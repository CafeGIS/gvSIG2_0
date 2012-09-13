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
package org.gvsig.rastertools.properties.panels;

import java.io.File;

import junit.framework.TestCase;

import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.io.RasterDriverException;

import com.iver.cit.gvsig.project.Project;
/**
 * Este test prueba el acceso a datos a traves de un FLyrRasterSE.
 *
 * Lee el raster completo y comprueba que los datos leidos sean correctos
 * comparando los valores de las cuatro esquinas y algunos valores dentro de la imagen.
 * Después selecciona un área dentro de la imagen de 2x2 y compara que los valores
 * leidos sean correctos.
 *
 * @version 20/03/2007
 * @author BorSanZa - Borja Sanchez Zamorano (borsanza@gmail.com)
 */
public class TestFLyrRasterSE extends TestCase {

	private String baseDir = "./test-images/";
	private String path1 = baseDir + "band1-30x28byte.tif";
	private String path2 = baseDir + "band2-30x28byte.tif";
	private String path3 = baseDir + "band3-30x28byte.tif";
	private FLyrRasterSE f = null;

	public void setUp() {
		System.out.println("TestFlyrRasterSE running...");
	}

	static {
		RasterLibrary.wakeUp();
	}

	public void testStack() {

		//TODO: Quizás lo mejor sería que te montaras un directorio con los drivers. Es posible que
		//podamos cambiar test-images por test-data y meterlo dentro junto con imagenes de prueba
//		String driversPath = "../_fwAndami/gvSIG/extensiones/com.iver.cit.gvsig/drivers";
//
//		LayerFactory.setDriversPath(driversPath);

		try {
			f = FLyrRasterSE.createLayer("Prueba", new File(path1), Project.getDefaultProjection());
		} catch (LoadLayerException e) {
			e.printStackTrace();
		}
		try {
			f.addFile(path2);
			f.addFile(path3);
		} catch (NotSupportedExtensionException e) {
			e.printStackTrace();
		} catch (RasterDriverException e) {
			e.printStackTrace();
		}

		print();
		dataTest1();

	}

	private void dataTest1() {
		assertNotNull("band1-30x28byte.tif", f.getFileName()[0]);
		assertNotNull("band2-30x28byte.tif", f.getFileName()[1]);
		assertNotNull("band3-30x28byte.tif", f.getFileName()[2]);

		assertTrue("f.getFileSize()[0]== (long) 65940", f.getFileSize()[0] == (long) 65940);
		assertTrue("f.getFileSize()[1]== (long) 65940", f.getFileSize()[1] == (long) 65940);
		assertTrue("f.getFileSize()[2]== (long) 65940", f.getFileSize()[2] == (long) 65940);

		assertTrue("f.getPxWidth()==30,0", f.getPxWidth() == 30.0);
		assertTrue("f.getPxHeight()==28,0", f.getPxHeight() == 28.0);

		assertNotNull("tif", f.getFileFormat());
		assertEquals("tif", f.getFileFormat(), "tif");

		assertTrue("f.isGeoreferenced()", f.isGeoreferenced());

		assertTrue("f.getBandCount()==3.0", f.getBandCount() == 3.0);

		assertTrue("f.getMinX()==207166.5", f.getMinX() == 207166.5);
		assertTrue("f.getMinY()==4368565.5", f.getMinY() == 4368565.5);
		assertTrue("f.getMaxX()==216514.5", f.getMaxX() == 216514.5);
		assertTrue("f.getMaxY()==4377400.5", f.getMaxY() == 4377400.5);

		//Upper Left
/*
		assertEquals((int)(raster.getElemByte(0, 0, 0) & 0xff), 14);
		assertEquals((int)(raster.getElemByte(0, 0, 1) & 0xff), 14);
		assertEquals((int)(raster.getElemByte(0, 0, 2) & 0xff), 0);
*/
	}

	/**
	 * Imprime todos los datos de FLyrRasterSE
	 */
	private void print(){
		System.out.println("Nombre Archivo 1: " + f.getFileName()[0]);
		System.out.println("Nombre Archivo 2: " + f.getFileName()[1]);
		System.out.println("Nombre Archivo 3: " + f.getFileName()[2]);

		System.out.println("Tamaño 1: " + f.getFileSize()[0]);
		System.out.println("Tamaño 2: " + f.getFileSize()[1]);
		System.out.println("Tamaño 3: " + f.getFileSize()[2]);

		System.out.println("Ancho: " + f.getPxWidth());
		System.out.println("Alto: " + f.getPxHeight());

		System.out.println("Formato: " + f.getFileFormat());

		System.out.println("GeoReferenciado: " + f.isGeoreferenced());

		System.out.println("Bandas: " + f.getBandCount());

		System.out.println("xmin: " + f.getMinX());
		System.out.println("ymin: " + f.getMinY());
		System.out.println("xmax: " + f.getMaxX());
		System.out.println("ymax: " + f.getMaxY());


/*		System.out.println(f.getMaxScale());
		System.out.println(f.getMinScale());
		System.out.println(f.getHeight());
		System.out.println(f.getWidth());
		System.out.println(f.getWCHeight());
		System.out.println(f.getWCWidth());
		System.out.println(f.getFileCount());
		System.out.println(f.getTransparency());*/

	}
}