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
 * Testea las operaciones de Grid. Las operaciones testeadas son las siguiente:
 * <UL>
 * <LI>Multiplica el grid por un valor</LI>
 * <LI>Suma de grids</LI>
 * <LI>Sobreescritura de un grid con los datos de otro</LI>
 * <LI>Ordenar celdas</LI>
 * <LI>Media, mínimo y máximo de un grid</LI>
 * </UL>
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class TGOperations extends TestCase{
	
	static{
		RasterLibrary.wakeUp();	
	}
	
	public void setUp() {
		System.err.println("TGOperations running...");
	}
	
	public void start(){
		this.setUp();
		this.testStack();
	}
	
	public void testStack(){
		try {
			GridExtent layerExtent = new GridExtent(1000, 1000, 1500, 1500, 100);
			Grid g1 = new Grid(layerExtent, layerExtent, IBuffer.TYPE_INT, new int[]{0, 1, 2});
			init(g1);
			
			//Multiplicar por un entero
			g1.multiply(2);
			int[][] m1 = new int[][]{{0, 2, 4, 6, 8}, {2, 4, 6, 8, 10}, {4, 6, 8, 10, 12}, {6, 8, 10, 12, 14}, {8, 10, 12, 14, 16}};
			compare(m1, g1);
					
			//Sumar dos grids
			Grid g2 = new Grid(layerExtent, layerExtent, IBuffer.TYPE_INT, new int[]{0, 1, 2});
			init(g1);
			init(g2);
			g1.add(g2);
			compare(m1, g1);
			
			//Sobreescritura
			g2.assign(g1);
			compare(m1, g1);
			//print(g1);
			
			//Celdas ordenadas
			GridCell[] cells = g1.getSortedArrayOfCells();
			int[] values = new int[]{0, 2, 2, 4, 4, 4, 6, 6, 6, 6, 8, 8, 8, 8, 8, 10, 10, 10, 10, 12, 12, 12, 14, 14, 16};
			compareArray(values, cells);
			
			//Media de un grid (getMeanValue)
			init(g1);
			g1.setBandToOperate(0);
			assertEquals((int)g1.getMeanValue(), 4);
			assertEquals((int)g1.getMinValue(), 0);
			assertEquals((int)g1.getMaxValue(), 8);
			
			
		} catch (RasterBufferInvalidException e1) {
			e1.printStackTrace();
		}  catch (OutOfGridException e3) {
			e3.printStackTrace();
		} catch (RasterBufferInvalidAccessException e) {
			e.printStackTrace();
		} catch (GridException e) {
			e.printStackTrace();
		}
	}
	
	private void init(Grid g) throws OutOfGridException{
		for (int i = 0; i < g.getNX(); i++) {
			for (int j = 0; j < g.getNY(); j++)
				g.setCellValue(j, i, (int)(j + i));
		}
	}
	
	private void compare(int[][] m, Grid g) throws RasterBufferInvalidAccessException, GridException {
		for(int line = 0; line < g.getNY(); line++){
			for(int col = 0; col < g.getNX(); col++)
				assertEquals(g.getCellValueAsInt(col, line), m[line][col]);
		}
	}
	
	private void compareArray(int[] a, GridCell[] cells) throws RasterBufferInvalidAccessException {
		for (int i = 0; i < cells.length; i++) 
			assertEquals((int)cells[i].getValue(), (int)cells[i].getValue());
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
