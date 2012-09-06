/* gvSIG. Sistema de Informaciï¿½n Geogrï¿½fica de la Generalitat Valenciana
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
package org.gvsig.raster.grid;

import junit.framework.TestCase;

import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.buffer.RasterBufferInvalidAccessException;
import org.gvsig.raster.buffer.RasterBufferInvalidException;
import org.gvsig.raster.dataset.IBuffer;
/**
 * Este test prueba el acceso a datos a traves de un grid.
 * 
 * 1-Creará un buffer vacio para escritura con la extensión pasado por
 *   parámetro. El extent completo será igual que el extent de la vista asignada
 *   (SIN INTERPOLACIÓN).
 * 2-Comprueba que se calcule bien el número de pixels
 * 3-Asigna datos al grid
 * 4-Los recupera comprobando que están bien asignados
 * 
 * 1-Creará un buffer vacio para escritura con la extensión pasado por
 *   parámetro. El extent completo será distinto que el extent de la vista
 *   asignada (CON INTERPOLACIÓN).
 * 2-Comprueba que se calcule bien el número de pixels
 * 3-Asigna datos al grid
 * 4-Los recupera con distintos métodos de interpolación comprobando que están
 *   bien asignados
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class TGEmptyBufferForWrite extends TestCase {

	static {
		RasterLibrary.wakeUp();
	}

	public void setUp() {
		System.err.println("TGEmptyBufferForWrite running...");
	}

	public void start() {
		this.setUp();
		this.testStack();
	}

	public void testStack() {
		try {
			// SIN INTERPOLACIÓN
			GridExtent layerExtent = new GridExtent(1000, 1000, 1500, 1500, 50);
			Grid g = new Grid(layerExtent, layerExtent, IBuffer.TYPE_INT, new int[] { 0, 1, 2 });

			// Tamaño en pixels
			assertEquals(layerExtent.getNX(), 10);
			assertEquals(layerExtent.getNY(), 10);
			assertEquals(g.getNX(), 10);
			assertEquals(g.getNY(), 10);

			for (int i = 0; i < g.getNX(); i++) {
				for (int j = 0; j < g.getNY(); j++)
					g.setCellValue(j, i, (int) (j * i));
			}
			// print(g);
			for (int i = 0; i < g.getNX(); i++) {
				for (int j = 0; j < g.getNY(); j++)
					assertEquals(g.getCellValueAsInt(j, i), (int) (j * i));
			}

			// CON INTERPOLACIÓN
			GridExtent windowExtent = new GridExtent(1125, 1125, 1375, 1375, 50);
			layerExtent = new GridExtent(1000, 1000, 1500, 1500, 50);
			g = new Grid(layerExtent, windowExtent, IBuffer.TYPE_INT, new int[] { 0, 1, 2 });

			// Tamaño en pixels
			assertEquals(layerExtent.getNX(), 10);
			assertEquals(layerExtent.getNY(), 10);
			assertEquals(g.getNX(), 5);
			assertEquals(g.getNY(), 5);

			for (int i = 0; i < g.getLayerNX(); i++) {
				for (int j = 0; j < g.getLayerNY(); j++)
					g.setCellValue(j, i, (int) (j * i));
			}
			g.setInterpolationMethod(GridInterpolated.INTERPOLATION_BicubicSpline);
			int[][] m1 = new int[][] { { 9, 12, 15, 18, 21 }, { 12, 16, 20, 24, 28 }, { 15, 20, 25, 30, 35 }, { 18, 24, 30, 36, 42 }, { 21, 28, 35, 42, 49 } };
			// print(g);
			compare(m1, g);

			g.setInterpolationMethod(GridInterpolated.INTERPOLATION_NearestNeighbour);
			// print(g);
			int[][] m2 = new int[][] { { 9, 12, 15, 18, 21 }, { 12, 16, 20, 24, 28 }, { 15, 20, 25, 30, 35 }, { 18, 24, 30, 36, 42 }, { 21, 28, 35, 42, 49 } };
			compare(m2, g);

			g.setInterpolationMethod(GridInterpolated.INTERPOLATION_Bilinear);
			// print(g);
			int[][] m3 = new int[][] { { 9, 12, 15, 18, 21 }, { 12, 16, 20, 24, 28 }, { 15, 20, 25, 30, 35 }, { 18, 24, 30, 36, 42 }, { 21, 28, 35, 42, 49 } };
			compare(m3, g);

			g.setInterpolationMethod(GridInterpolated.INTERPOLATION_BSpline);
			// print(g);
			int[][] m4 = new int[][] { { 9, 11, 14, 18, 21 }, { 12, 15, 19, 23, 27 }, { 14, 20, 24, 29, 35 }, { 18, 23, 30, 36, 41 }, { 21, 27, 35, 42, 49 } };
			compare(m4, g);

			g.setInterpolationMethod(GridInterpolated.INTERPOLATION_InverseDistance);
			// print(g);
			int[][] m5 = new int[][] { { 9, 12, 15, 18, 21 }, { 12, 16, 20, 24, 28 }, { 15, 20, 25, 30, 35 }, { 18, 24, 30, 36, 42 }, { 21, 28, 35, 42, 49 } };
			compare(m5, g);

		} catch (RasterBufferInvalidException e1) {
			e1.printStackTrace();
		} catch (OutOfGridException e3) {
			e3.printStackTrace();
		} catch (RasterBufferInvalidAccessException e) {
			e.printStackTrace();
		} catch (GridException e) {
			e.printStackTrace();
		}
	}
	
	private void compare(int[][] m, Grid g) throws RasterBufferInvalidAccessException, GridException {
		for (int line = 0; line < g.getNY(); line++) {
			for (int col = 0; col < g.getNX(); col++)
				assertEquals(g.getCellValueAsInt(col, line), m[line][col]);
		}
	}
	
	/**
	 * Imprime todos los pixels de la fuente de datos en RGB
	 * @throws RasterBufferInvalidAccessException 
	 */
	/*private void print(Grid g) throws RasterBufferInvalidAccessException {
		for(int line = 0; line < g.getNY(); line++){
			for(int col = 0; col < g.getNX(); col++)
				System.out.print(g.getCellValueAsInt(col, line) + " ");
			System.out.println();
		}
		System.out.println();
	}*/
}
