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
public class StreetsDBFTest extends TestCase {
	private static final Logger log =
	LoggerFactory.getLogger(StreetsDBFTest.class);
	//
	// private static final float SENSIBILITY = 0.8f;
	//
	// private static final String SEARCH_TERM = "DON JUAN";
	// private static final String TEST_FILE = "test/Horta.dbf";
	//
	// String INDEX_DIRPATH = new File("test/dbf").getAbsolutePath();
	//
	// DBFDriver driverdbf = null;
	// int nFields;
	// int nRows;
	//
	// int indID = 1;
	// int indNAME = 8;
	//
	// String strID, strNAME, strTIMEZONE;
	//
	// private FieldDescription[] descs;
	//
	// public void setUp() {
	// File f = new File(TEST_FILE);
	//
	// driverdbf = new DBFDriver();
	//
	// try {
	// driverdbf.open(f);
	// nFields = driverdbf.getFieldCount();
	// nRows = (int) driverdbf.getRowCount();
	//
	// strID = driverdbf.getFieldName(indID);
	// strNAME = driverdbf.getFieldName(indNAME);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// log.info("Fichero DBF abierto (" + nRows + " filas)");
	// }
	//
	// // public void testFields() throws ReadDriverException{
	// //
	// // for (int i=0;i<nFields;i++){
	// // log.info(i + " " + driverdbf.getFieldName(i) + " :" +
	// // driverdbf.getFieldValue(0, i));
	// // }
	// // }
	//
	// /* Este test crea los indices */
	// public void testGenerateIndex() {
	// log.info("Creando índice...");
	//
	// long t1 = System.currentTimeMillis();
	//
	// IndexWriter indexWriter = null;
	// try {
	// indexWriter = new IndexWriter(INDEX_DIRPATH,
	// new StandardAnalyzer(), true);
	// createIndex(indexWriter);
	// indexWriter.optimize();
	// } catch (Exception e) {
	//
	// }
	// log.info("Índice creado en " + (System.currentTimeMillis() - t1)
	// + " msecs");
	//
	// }
	//
	// private void createIndex(IndexWriter indexWriter)
	// throws CorruptIndexException, ReadDriverException, IOException {
	// for (int i = 0; i < nRows; i++) {
	//
	// if (i % (nRows / 4 + 1) == 0)
	// log.info(i * 1.0 / nRows * 100 + "%");
	//
	// indexWriter.addDocument(createDoc(i));
	// }
	// }
	//
	// /* Busqueda de cadenas */
	// public void testLuceneRAMDirectorySearch() throws CorruptIndexException,
	// LockObtainFailedException, IOException, ReadDriverException {
	// Analyzer an = null;
	//
	// ArrayList<String> cadenas = new ArrayList<String>();
	//
	// cadenas.add(SEARCH_TERM);
	// log
	// .info("--------------TEST DE BÚSQUEDA LUCENE CON RAMDirectory--------------");
	// IndexWriter indexWriter = null;
	// Directory dir = null;
	// log.info("----Crear índice----");
	// long id1 = System.currentTimeMillis();
	// dir = new RAMDirectory();
	// indexWriter = new IndexWriter(dir, new StandardAnalyzer(), true);
	// createIndex(indexWriter);
	// indexWriter.optimize();
	// log.info("Índice creado en " + (System.currentTimeMillis() - id1)
	// + " msecs");
	//
	// log.info("----Buscando...----");
	// id1 = System.currentTimeMillis();
	// an = new StandardAnalyzer();
	// buscar(new IndexSearcher(dir), an, cadenas);
	// long id2 = System.currentTimeMillis();
	//
	// log.info("Búsqueda realizada en " + (id2 - id1) + "msecs");
	// log
	// .info("--------------TEST DE BÚSQUEDA LUCENE CON RAMDirectory--------------");
	// }
	//
	// /* Busqueda de cadenas */
	// public void testLuceneSearch() throws CorruptIndexException, IOException
	// {
	// Analyzer an = null;
	//
	// ArrayList<String> cadenas = new ArrayList<String>();
	//
	// cadenas.add(SEARCH_TERM);
	// log.info("--------------TEST DE BÚSQUEDA LUCENE--------------");
	// long id1 = System.currentTimeMillis();
	// an = new StandardAnalyzer();
	// buscar(new IndexSearcher(INDEX_DIRPATH), an, cadenas);
	// long id2 = System.currentTimeMillis();
	//
	// log.info("Búsqueda realizada en " + (id2 - id1) + "msecs");
	//
	// log.info("--------------TEST DE BÚSQUEDA LUCENE--------------");
	// }
	//
	// public void testDBFSearch() throws ReadDriverException {
	// log.info("--------------TEST DE BÚSQUEDA ITERANDO--------------");
	// long id1 = System.currentTimeMillis();
	// int results = 0;
	// String fieldValue;
	// for (int i = 0; i < nRows; i++) {
	// fieldValue = driverdbf.getFieldValue(i, indNAME).toString();
	// if (fieldValue.toLowerCase().indexOf(SEARCH_TERM.toLowerCase()) > -1) {
	// log.info(driverdbf.getFieldValue(i, indID) + "\t" + fieldValue);
	// results++;
	// }
	// }
	//
	// long id2 = System.currentTimeMillis();
	//
	// log.info(results + " resultados");
	// log.info("Búsqueda realizada en " + (id2 - id1) + "msecs");
	//
	// log.info("--------------TEST DE BÚSQUEDA ITERANDO--------------");
	// }
	//
	// private void testDataAccessFilters() {
	// log.info("--------------TEST DE BÚSQUEDA FILTROS--------------");
	// // TODO
	// log.info("--------------TEST DE BÚSQUEDA FILTROS--------------");
	// }
	//
	// private void buscar(IndexSearcher indexSearcher, Analyzer an,
	// List<String> cadenas) {
	// int results = 0;
	// try {
	// for (String cadena : cadenas) {
	// log.info("Buscando ``" + cadena + "`` con el analizador "
	// + an.getClass().getName());
	// // Se crear la consulta indicando el nombre del campo donde
	// // estan
	// // las cadenas del indices
	// // junto con el analizador
	// Query consulta = null;
	//
	// // consulta = new QueryParser(strNAME, an).parse(cadena);
	// // consulta = new FuzzyQuery(new Term(strNAME, "don"),
	// // SENSIBILITY);
	//
	// consulta = new BooleanQuery();
	// BooleanQuery boolConsulta = (BooleanQuery) consulta;
	// boolConsulta.add(
	// new FuzzyQuery(new Term(strNAME, "don"), 0.5f),
	// BooleanClause.Occur.MUST);
	// boolConsulta.add(
	// new FuzzyQuery(new Term(strNAME, "joan"), 0.5f),
	// BooleanClause.Occur.MUST);
	//
	// // se realiza la busqueda de una cadena y se obtinen los
	// // resultados
	// Hits hits = indexSearcher.search(consulta);
	//
	// results = hits.length();
	// for (int i = 0; i < hits.length(); i++) {
	// Document docu = hits.doc(i);
	//
	// log.info(docu.get(strID) + "\t " + docu.get(strNAME) + "("
	// + hits.score(i) + ")");
	// }
	// }
	//
	// log.info(results + " resultados");
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// indexSearcher.close();
	// } catch (IOException e) {
	// log.error("Error cerrando índice", e);
	// }
	//
	// }
	// }
	//
	// private Document createDoc(int i) throws ReadDriverException {
	// Document doc = new Document();
	//
	// // Add ID
	// Field fi;
	// String fieldValue = driverdbf.getFieldValue(i, indID).toString();
	// fi = new Field(strID, fieldValue, Field.Store.YES,
	// Field.Index.UN_TOKENIZED);
	// doc.add(fi);
	//
	// // Add NANME
	// fieldValue = driverdbf.getFieldValue(i, indNAME).toString();
	// fi = new Field(strNAME, fieldValue, Field.Store.YES,
	// Field.Index.TOKENIZED);
	// doc.add(fi);
	//
	// return doc;
	// }
	//
	// public void tearDown() {
	// try {
	// driverdbf.close();
	// } catch (CloseDriverException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

}
