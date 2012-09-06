/*
 * Created on 9-ago-2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class TestCompositeTilesSimplification extends TestCase {
	
	static {
		RasterLibrary.wakeUp();
	}
	
	public void start() {
		this.setUp();
		this.testStack();
	}
	
	public void setUp() {
		System.err.println("TestCompositeTilesSimplification running...");
		Integer[][] result = null;

		Integer[][] values = new Integer[10][10];
		for (int row = 3; row < 5; row++)
			for (int col = 3; col < 5; col++)
				values[row][col] = new Integer(new String(row + "" + col));
			
		result = compress(values);
		show(result);
		
		values = new Integer[10][10];
		for (int row = 0; row < 5; row++)
			for (int col = 0; col < 5; col++)
				values[row][col] = new Integer(new String(row + "" + col));
		
		result = compress(values);
		show(result);
		
		values = new Integer[10][10];
		for (int row = 5; row < 10; row++)
			for (int col = 5; col < 10; col++)
				values[row][col] = new Integer(new String(row + "" + col));
		
		result = compress(values);
		show(result);
	}
	
	public void testStack(){
		
	}
	
	
	private Integer[][] compress(Integer[][] values) {
		int n = values.length;
		int m = values[0].length;
		int posInitX = 0;
		int posInitY = 0;
		
		int nRows = n, nCols = m;
		//Contador de filas
		boolean first = true;
		for (int row = 0; row < n; row++) {
			boolean isNull = true;
			for (int col = 0; col < m; col++) {
				if(values[row][col] != null) {
					isNull = false;
					if(first) {
						posInitX = col;
						first = false;
					}
				}
			}			
			if(isNull)
				nRows --;
		}
		
		//Contador de columnas
		first = true;
		for (int col = 0; col < m; col++) {
			boolean isNull = true;
			for (int row = 0; row < n; row++) {
				if(values[row][col] != null) {
					isNull = false;
					if(first) {
						posInitY = row;
						first = false;
					}
				}
			}			
			if(isNull)
				nCols --;
		}
		System.out.println("Rows:" + nRows + " Cols:" + nCols );
		//Copia de datos
		Integer[][] result = new Integer[nRows][nCols];
		
		System.out.println("posInitX:" + posInitX + " posInitY:" + posInitY );
		
		for (int row = 0; row < result.length; row++) 
			for (int col = 0; col < result[row].length; col++) 
				result[row][col] = values[row + posInitY][col + posInitX];
		return result;
	}
	
	private void show(Integer[][] result) {
		for (int row = 0; row < result.length; row++) { 
			for (int col = 0; col < result[row].length; col++) { 
				System.out.print(result[row][col].intValue() + " ");
			}
			System.out.println();
		}
	}
}
