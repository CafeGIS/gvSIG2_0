/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Government (CIT)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 * 
 */

/*
 * AUTHORS (In addition to CIT):
 * 2008 Prodevelop S.L  vsanjaime   programador
 */

package org.gvsig.geocoding.dataaccess;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Test
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */
public class TestDataAccessTest extends TestCase {

	private static final Logger log = LoggerFactory
			.getLogger(TestDataAccessTest.class);
	//
	// File dbfFile = new File(
	// "src-test/org/gvsig/geocoding/geocoding/dataaccess/067.dbf");
	//
	// EditableAdapter source = new EditableAdapter();
	// DBFDriver driver = null;
	// FileDataSource fiDatSource = null;
	// SelectableDataSource selDaSource = null;
	//
	// int umbral = 70;
	// int max = 5;
	//
	// public void setUp() {
	//
	// try {
	//
	// driver = new DBFDriver();
	// driver.open(dbfFile);
	//
	// fiDatSource = FileDataSourceFactory.newInstance();
	// fiDatSource.setDriver(driver);
	// FileSourceInfo fsi = new FileSourceInfo();
	// fsi.file = dbfFile.getAbsolutePath();
	// fsi.name = dbfFile.getName();
	// fiDatSource.setSourceInfo(fsi);
	//
	// SelectableDataSource selDaSource = new SelectableDataSource(
	// fiDatSource);
	//
	// source.setOriginalDataSource(selDaSource);
	// source.startEdition(EditionEvent.GRAPHIC);
	//
	// } catch (Exception e) {
	// log.error("Reading the driver", e);
	// }
	//
	// }
	//
	// // public void testDataAccess() {
	// //
	// // String sample = "brahams";
	// //
	// // ArrayList<Integer> registro = new ArrayList<Integer>();
	// // ArrayList<Integer> distancia = new ArrayList<Integer>();
	// //
	// // Value[] values = null;
	// //
	// // try {
	// // int num = (int) source.getRowCount();
	// // int dis;
	// // for (int i = 0; i < num; i++) {
	// //
	// // values = source.getRow(i).getAttributes();
	// // String strTemp = values[1].toString();
	// //
	// // if (strTemp.indexOf(sample) > 0) {
	// // dis = LevenshteinDistance.computeLevenshteinDistance(
	// // strTemp, sample);
	// // registro.add(Integer.valueOf(i));
	// // distancia.add(Integer.valueOf(dis));
	// // }
	// //
	// // }
	// // } catch (Exception e) {
	// // log.error("Reading driver", e);
	// // }
	// //
	// // System.out.println("Number: " + registro.size());
	// // for (int i = 0; i < registro.size(); i++) {
	// // System.out.println("Registro nº: " + registro.get(i)
	// // + " Distancia: " + distancia.get(i));
	// // }
	// // System.out.println("FIN");
	// //
	// // }
	//
	// public void testDataAccess() {
	//
	// ArrayList<Integer> campos = new ArrayList<Integer>();
	// campos.add(1);
	// campos.add(2);
	// campos.add(7);
	// campos.add(8);
	// campos.add(14);
	// campos.add(17);
	//
	// ArrayList<String> cad1 = new ArrayList<String>();
	// cad1.add("Bradford");
	// cad1.add("brad");
	// cad1.add("lk");
	// cad1.add("ca");
	// cad1.add("0");
	// cad1.add("montreal");
	// ArrayList<String> cad2 = new ArrayList<String>();
	// cad2.add("lake");
	// cad2.add("lake");
	// cad2.add("lk");
	// cad2.add("ca");
	// cad2.add("0");
	// cad2.add("white");
	// ArrayList<String> cad3 = new ArrayList<String>();
	// cad3.add("indian");
	// cad3.add("indi");
	// cad3.add("pt");
	// cad3.add("ca");
	// cad3.add("0");
	// cad3.add("johns");
	// ArrayList<String> cad4 = new ArrayList<String>();
	// cad4.add("sanders");
	// cad4.add("sanders");
	// cad4.add("stm");
	// cad4.add("ca");
	// cad4.add("0");
	// cad4.add("winni");
	// ArrayList<String> cad5 = new ArrayList<String>();
	// cad5.add("triple");
	// cad5.add("triple");
	// cad5.add("lk");
	// cad5.add("ca");
	// cad5.add("0");
	// cad5.add("toronto");
	//
	// ArrayList<ArrayList<String>> cadenas = new
	// ArrayList<ArrayList<String>>();
	// cadenas.add(cad1);
	// cadenas.add(cad2);
	// cadenas.add(cad3);
	// cadenas.add(cad4);
	// cadenas.add(cad5);
	//
	// ArrayList<ResultSearch> res = goSearch(source, cad1, campos, umbral,
	// max);
	//
	// // ----------------
	// if (res != null) {
	// System.out.println("Number: " + res.size());
	// for (int i = 0; i < res.size(); i++) {
	// System.out.println("Registro nº: " + res.get(i).getNRow());
	// }
	// }
	//
	// System.out.println("FIN");
	//
	// }
	//
	// public void tearDown() {
	// try {
	// driver.close();
	// } catch (CloseDriverException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// /*------------------------------------------------------- */
	//
	// private ArrayList<ResultSearch> goSearch(IEditableSource source,
	// ArrayList<String> cadenas, ArrayList<Integer> campos, int umbral,
	// int max) {
	//
	// ArrayList<ResultSearch> results = new ArrayList<ResultSearch>();
	//
	// Value[] values = null;
	// ResultSearch reg = null;
	//
	// int i = 0;
	// try {
	// for (i = 0; i < (int) source.getRowCount(); i++) {
	// if (i == 6280) {
	// System.out.println("");
	// }
	// values = source.getRow(i).getAttributes();
	// reg = searchInRow(i, values, cadenas, campos, umbral);
	// if (reg != null) {
	// results.add(reg);
	// }
	// }
	// } catch (Exception e) {
	// log.error("Error in file: " + i);
	// log.error("Reading driver", e);
	// }
	//
	// return results;
	// }
	//
	// private ResultSearch searchInRow(int nRow, Value[] vals,
	// ArrayList<String> strs, ArrayList<Integer> fields, int threshold) {
	//
	// String original;
	// String temp;
	//
	// ResultSearch result = new ResultSearch(nRow, strs.size());
	//
	// for (int i = 0; i < strs.size(); i++) {
	//
	// original = strs.get(i).toUpperCase().trim();
	// temp = vals[(fields.get(i)).intValue()].toString().toUpperCase()
	// .trim();
	//
	// int distance = searchInElement(original, temp);
	// result.setNRow(nRow);
	// result.getPairs().put(fields.get(i), distance);
	// if (distance == -1) {
	// result.lessMath();
	// }
	// }
	// if (result.getMatch() > threshold) {
	// return result;
	// }
	// return null;
	// }
	//
	// private int searchInElement(String oriStr, String str) {
	// int distance = -1;
	//
	// if (oriStr.indexOf(str) > 0 || oriStr.matches(str)
	// || oriStr.startsWith(str) || oriStr.endsWith(str)) {
	// // exists value in field
	// // calculate distance between strings
	// distance = LevenshteinDistance.getLevenshteinDistance(
	// oriStr.trim(), str.trim());
	// }
	// return distance;
	// }

}
