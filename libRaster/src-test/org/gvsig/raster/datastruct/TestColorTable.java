/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.raster.datastruct;

import java.awt.Color;
import java.util.ArrayList;

import org.gvsig.raster.BaseTestCase;
/**
 * Test para comprobar el funcionamiento de las tablas de color.
 * @version 12/05/2008
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class TestColorTable extends BaseTestCase {
	private String baseDir = "./test-images/";
	private String path = baseDir + "gifTransparente.gif";

	public void setUp() {
		System.err.println("TestColorTable running...");
	}
	
	public void testStack() {
		dataTest1();
		dataTest2();
	}
	
	/**
	 * Compara un array de bytes con sus respectivos valores de color
	 * @param item
	 * @param red
	 * @param green
	 * @param blue
	 * @param alpha
	 */
	private void compareColor(byte[] item, int red, int green, int blue, int alpha) {
		assertEquals((int)(item[0] & 0xff), red);
		assertEquals((int)(item[1] & 0xff), green);
		assertEquals((int)(item[2] & 0xff), blue);
		assertEquals((int)(item[3] & 0xff), alpha);
	}
	
	/**
	 * Comprueba la tabla de color de una imagen existente
	 */
	private void dataTest1() {
		open(path);
		ColorTable table = dataset.getColorTables()[0];
		compareColor(table.getColorTableByBand()[0],  255, 255, 255,   0);
		compareColor(table.getColorTableByBand()[1],    0,   0,   0, 255);
		compareColor(table.getColorTableByBand()[2],    0, 102, 255, 255);
		compareColor(table.getColorTableByBand()[3],    0, 153, 255, 255);
		compareColor(table.getColorTableByBand()[4],    0,   0, 255, 255);
		compareColor(table.getColorTableByBand()[5],    0,  51, 255, 255);
		compareColor(table.getColorTableByBand()[6],   55,  55, 255, 255);
		compareColor(table.getColorTableByBand()[7],    0, 204, 255, 255);
		compareColor(table.getColorTableByBand()[8],  191, 191, 255, 255);
		compareColor(table.getColorTableByBand()[9],  191, 242, 255, 255);
		compareColor(table.getColorTableByBand()[10], 223, 223, 223, 255);
		compareColor(table.getColorTableByBand()[11], 127, 127, 127, 255);
		compareColor(table.getColorTableByBand()[12],  63,  63,  63, 255);
		compareColor(table.getColorTableByBand()[13], 159, 159, 159, 255);
		compareColor(table.getColorTableByBand()[14],  31,  31,  31, 255);
		compareColor(table.getColorTableByBand()[15],  95,  95,  95, 255);
		compareColor(table.getColorTableByBand()[16], 191, 191, 191, 255);
	}
	
	/**
	 * Comprueba una tabla de color creada a mano
	 */
	private void dataTest2() {
		ColorTable table = new ColorTable("dataTest2");
		ArrayList list = new ArrayList();
		ColorItem item = new ColorItem();
		item.setValue(0.0f);
		item.setColor(Color.black);
		list.add(item);
		item = new ColorItem();
		item.setValue(10.0f);
		item.setColor(Color.white);
		list.add(item);
		item = new ColorItem();
		item.setValue(20.0f);
		item.setColor(Color.red);
		list.add(item);
		
		table.createPaletteFromColorItems(list, false);
		table.setInterpolated(false);

		compareColor(table.getRGBAByBand(0.0f),    0,   0,   0, 255);
		compareColor(table.getRGBAByBand(10.0f), 255, 255, 255, 255);
		compareColor(table.getRGBAByBand(20.0f), 255,   0,   0, 255);
	}
}