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
public class LuceneDataAccessTXTTest extends TestCase {
	private static final Logger log =
	 LoggerFactory.getLogger(LuceneDataAccessTXTTest.class);
	// String[] stops = {"de","con","para","en"};
	// String INDEX_DIRPATH = new File(
	// "test")
	// .getAbsolutePath();
	//
	// ArrayList<String> cadenas = null;
	//
	// public void setUp() {
	// }
	//
	// /* Este test crea los indices */
	// public void testGenerateIndex() {
	// log.info("--------------GENERACIÓN DEL ÍNDICE--------------");
	//
	// File f = new File(
	// "src-test/org/gvsig/geocoding/geocoding/dataaccess/testLucene.txt");
	//
	// cadenas = readTextFile(f);
	// log.info("Archivo leido");
	//
	// IndexWriter indexWriterStandard = null;
	// IndexWriter indexWriterSnowBall = null;
	//		
	// try {
	// // Escritor de indices que recibe la carpeta donde almacenar los
	// // indices,
	// // el analizador: (StandardAnalyzer[búsquedas exactas],
	// // SnowballAnalyzer[Búsquedas semejantes])
	// // y booleano que indica si se deben sobrescribir los indices
	//
	// indexWriterStandard = new IndexWriter(INDEX_DIRPATH + "/standard", new
	// StandardAnalyzer(stops), true);
	// indexWriterSnowBall = new IndexWriter(INDEX_DIRPATH + "/snowball", new
	// SnowballAnalyzer("Spanish"), true);
	//
	// for (int i = 0; i < cadenas.size(); i++) {
	//
	// if (i % 10 == 0) {
	// log.info("Leyendo cadenas (" + i + "/" + cadenas.size()
	// + ")");
	// }
	//
	// // //desordena las cadenas
	// // Collections.shuffle(cadenas);
	//
	// // generar un identificador
	// StringBuffer caw = new StringBuffer();
	//
	// // caw.reset();
	// caw.append("quote-");
	// caw.append(Long.toString(
	// Double.doubleToLongBits(Math.random()), 34));
	// String sQuoteId = caw.toString();
	//
	// // Documento donde se introduce el indice y se puede introducir
	// // las cadenas
	// Document doc = new Document();
	//
	// // Cada document recibe el nombre del campo,
	// // el segundo es el valor del campo (id, string)
	// // el tercero indica que la cadena de texto se almacena junto al
	// // indice YES o NO
	// // el cuarto indica si se analiza la cadena de texto con el
	// // analizador
	// // en el caso de TOKENIZED se analiza y en el caso de
	// // UN_TOKENIZED no se analiza
	// doc.add(new Field("id", sQuoteId, Field.Store.YES,
	// Field.Index.UN_TOKENIZED));
	// doc.add(new Field("str", cadenas.get(i), Field.Store.YES,
	// Field.Index.TOKENIZED));
	// // añadir el documento al escritor de indices
	// indexWriterSnowBall.addDocument(doc);
	// indexWriterStandard.addDocument(doc);
	// }
	// indexWriterSnowBall.optimize();
	// indexWriterStandard.optimize();
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// if (indexWriterSnowBall != null) indexWriterSnowBall.close();
	// } catch (Exception e) {}
	//			
	// try {
	// if (indexWriterStandard != null) indexWriterStandard.close();
	// } catch (Exception e) {}
	//			
	// indexWriterSnowBall = null;
	// indexWriterStandard = null;
	// log.info("Indices generados");
	// }
	// log.info("--------------GENERACIÓN DEL ÍNDICE--------------");
	// }
	//
	// /* Prueba de un analizador del tipo SnowballAnalyzer */
	// public void testAnalizer() {
	// log.info("--------------TEST DE ANALIZADOR--------------");
	// String sTxt = "Villarrasa Vil·larrasa Villarrasá";
	// StringReader sr = null;
	// TokenStream tokenStream = null;
	// org.apache.lucene.analysis.Token token = null;
	// // StandardAnalyzer analizer = new StandardAnalyzer();
	// SnowballAnalyzer analizer = new SnowballAnalyzer("Spanish");
	// try {
	// sr = new StringReader(sTxt);
	// tokenStream = analizer.tokenStream("con", sr);
	// while ((token = tokenStream.next()) != null) {
	// log.info("[" + token.termText() + "] ");
	// }
	// System.err.println("");
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	//
	// }
	// log.info("--------------TEST DE ANALIZADOR--------------");
	// }
	//
	// /* Busqueda de cadenas */
	// public void testExactSearch() {
	// Analyzer an = null;
	//		
	// ArrayList<String> cadenas = new ArrayList<String>();
	//		
	// cadenas.add("Villarrasa");
	// cadenas.add("Villarrasá");
	// cadenas.add("Villarrasé");
	// cadenas.add("villarrase");
	// cadenas.add("villarrasa");
	// cadenas.add("vilarrasa");
	// cadenas.add("Don Juan de Vil·larrasa");
	//				
	//		
	// log.info("--------------TEST DE BÚSQUEDA EXACTA--------------");
	//		
	// an = new StandardAnalyzer(stops);
	// buscar(INDEX_DIRPATH + "/standard", an, cadenas);
	// log.info("--------------TEST DE BÚSQUEDA EXACTA--------------");
	//		
	// log.info("--------------TEST DE BÚSQUEDA SNOWBALL--------------");
	// an = new SnowballAnalyzer("Spanish");
	// buscar(INDEX_DIRPATH + "/snowball",an, cadenas);
	// log.info("--------------TEST DE BÚSQUEDA SNOWBALL--------------");
	// }
	//	
	//	
	// private void buscar(String indexPath, Analyzer an, List<String> cadenas){
	// IndexSearcher indexSearcher = null;
	//		
	//
	//		
	// try {
	// // Instaciar la clase que va a realizar la busqueda donde se indica
	// // el directorio donde estan los indices
	// indexSearcher = new IndexSearcher(indexPath);
	//			
	// for (String cadena:cadenas){
	// log.info("Buscando ``"+ cadena + "`` con el analizador " +
	// an.getClass().getName());
	// // Se crear la consulta indicando el nombre del campo donde estan
	// // las cadenas del indices
	// // junto con el analizador
	// Query consulta = null;
	//				
	// consulta = new QueryParser("str", an).parse(cadena);
	// // consulta = new FuzzyQuery(new Term("str",cadena));
	//				
	//				
	// // se realiza la busqueda de una cadena y se obtinen los resultados
	// Hits hits = indexSearcher.search(consulta);
	//				
	//				
	// log.info("Resultados: " + hits.length());
	// for (int i = 0; i < hits.length(); i++) {
	// Document docu = hits.doc(i);
	//					
	// log.info("str: " + docu.get("str") + "\t id: " + docu.get("id"));
	// }
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// indexSearcher.close();
	// } catch (IOException e) {
	// log.error("Error cerrando índice",e);
	// }
	//
	// }
	// }
	//	
	//	
	// public void tearDown() {
	//
	// }
	//
	// private ArrayList<String> readTextFile(File file) {
	//
	// ArrayList<String> arr = new ArrayList<String>();
	// if (file != null) {
	// FileReader fr = null;
	// String str = "";
	// try {
	// fr = new FileReader(file);
	// BufferedReader br = new BufferedReader(fr);
	// while ((str = br.readLine()) != null) {
	// arr.add(str);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// log.debug("ERROR reading a file");
	// }
	// }
	// return arr;
	// }

}
