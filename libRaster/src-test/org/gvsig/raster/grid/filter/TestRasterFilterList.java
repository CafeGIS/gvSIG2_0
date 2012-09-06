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
package org.gvsig.raster.grid.filter;

import junit.framework.TestCase;

import org.gvsig.raster.RasterLibrary;
/**
 * Este test prueba la lista de filtros.
 * Mete n filtros en la lista y asigna prioridades para comprobar luego que la lista
 * esté en el orden correcto. Se comprueban las operaciones de eliminar filtro por
 * prioridad, por nombre y por objeto y sustituir filtro por posición y por nombre.
 *
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class TestRasterFilterList extends TestCase {
	private boolean               show       = false;
	private int                   nFilters   = 7;
	private RasterFilterForTest[] filterList = null;
	private RasterFilterList      list       = new RasterFilterList();

	static {
		RasterLibrary.wakeUp();
	}

	public void setUp() {
		System.err.println("TestRasterFilterList running...");
		try {
			initList();
		} catch (FilterTypeException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		this.setUp();
		this.testStack();
	}

	public void testStack() {
		if (show)
			System.out.println("Lista completa");
		for (int i = 0; i < list.lenght(); i++) {
			if (show)
				System.out.println(" Name: " + ((RasterFilter) list.get(i)).getName());
			assertEquals(((RasterFilter) list.get(i)).getName(), "filtro" + (i + 1));
		}
		try {
			initList();

			list.remove("filtro5");

			list.remove("filtro2");
			if (show)
				System.out.println("Completa menos filtros 5 y 2");
			for (int i = 0; i < list.lenght(); i++) {
				if (show)
					System.out.println(" Name: " + ((RasterFilter) list.get(i)).getName());
				switch (i) {
					case 0:
						assertEquals(((RasterFilter) list.get(i)).getName(), "filtro1");
						break;
					case 1:
						assertEquals(((RasterFilter) list.get(i)).getName(), "filtro3");
						break;
					case 2:
						assertEquals(((RasterFilter) list.get(i)).getName(), "filtro4");
						break;
					case 3:
						assertEquals(((RasterFilter) list.get(i)).getName(), "filtro6");
						break;
					case 4:
						assertEquals(((RasterFilter) list.get(i)).getName(), "filtro7");
						break;
				}
			}

			initList();
			RasterFilter rf = new RasterFilterForTest();
			rf.setName("filtroNew");
			list.replace(rf, "filtro3");
			if (show)
				System.out.println("Completa reemplazando el filtro3 por filtroNew");
			for (int i = 0; i < list.lenght(); i++) {
				if (show)
					System.out.println("Name: " + ((RasterFilter) list.get(i)).getName());
				switch (i) {
					case 0:
						assertEquals(((RasterFilter) list.get(i)).getName(), "filtro1");
						break;
					case 1:
						assertEquals(((RasterFilter) list.get(i)).getName(), "filtro2");
						break;
					case 2:
						assertEquals(((RasterFilter) list.get(i)).getName(), "filtroNew");
						break;
					case 3:
						assertEquals(((RasterFilter) list.get(i)).getName(), "filtro4");
						break;
					case 4:
						assertEquals(((RasterFilter) list.get(i)).getName(), "filtro5");
						break;
					case 5:
						assertEquals(((RasterFilter) list.get(i)).getName(), "filtro6");
						break;
					case 6:
						assertEquals(((RasterFilter) list.get(i)).getName(), "filtro7");
						break;
				}
			}

			initList();
			list.remove(RasterFilterForTest.class);
			if (show)
				System.out.println("Eliminar por clase");
			for (int i = 0; i < list.lenght(); i++)
				if (show)
					System.out.println("Name: " + ((RasterFilter) list.get(i)).getName());
			assertEquals(list.lenght(), 0);
		} catch (FilterTypeException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Crea la lista
	 * @throws FilterTypeException
	 */
	private void initList() throws FilterTypeException {
		list.clear();
		filterList = new RasterFilterForTest[nFilters];
		for (int i = 0; i < nFilters; i++) {
			filterList[i] = new RasterFilterForTest();
			filterList[i].setName("filtro" + (i + 1));
		}

		for (int i = 0; i < nFilters; i++)
			list.add(filterList[i]);
	}
}