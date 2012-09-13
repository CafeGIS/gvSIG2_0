package com.iver.cit.gvsig.wfs.filters;

import junit.framework.TestCase;
/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
/* CVS MESSAGES:
 *
 * $Id: FilterEncodingTest.java 12328 2007-06-26 07:50:20Z jorpiell $
 * $Log$
 * Revision 1.1.2.3  2007-06-26 07:50:20  jorpiell
 * Test updated
 *
 * Revision 1.1.2.2  2006/11/17 11:28:45  ppiqueras
 * Corregidos bugs y aÃ±adida nueva funcionalidad.
 *
 * Revision 1.1  2006/10/05 10:26:26  jorpiell
 * Añadidas las clases para obtener los filtros
 *
 *
 */
/**
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class FilterEncodingTest extends TestCase {
	String query1 = "nombre='pepe' and apellidos='sanchez' or edad < 3"; 
	String filter1 = "<Filter><Or><And><PropertyIsEqualTo><PropertyName>nombre</PropertyName><Literal>pepe</Literal></PropertyIsEqualTo><PropertyIsEqualTo><PropertyName>apellidos</PropertyName><Literal>sanchez</Literal></PropertyIsEqualTo></And><PropertyIsLessThan><PropertyName>edad</PropertyName><Literal>3</Literal></PropertyIsLessThan></Or></Filter>";
	String query2 = "nombre='pepe' or apellidos='sanchez' and edad < 3"; 
	String filter2 = "<Filter><Or><PropertyIsEqualTo><PropertyName>nombre</PropertyName><Literal>pepe</Literal></PropertyIsEqualTo><And><PropertyIsEqualTo><PropertyName>apellidos</PropertyName><Literal>sanchez</Literal></PropertyIsEqualTo><PropertyIsLessThan><PropertyName>edad</PropertyName><Literal>3</Literal></PropertyIsLessThan></And></Or></Filter>";
	String query3 = "nombre='pepe' or apellidos='sanchez' or edad < 3"; 
	String filter3 = "<Filter><Or><PropertyIsEqualTo><PropertyName>nombre</PropertyName><Literal>pepe</Literal></PropertyIsEqualTo><Or><PropertyIsEqualTo><PropertyName>apellidos</PropertyName><Literal>sanchez</Literal></PropertyIsEqualTo><PropertyIsLessThan><PropertyName>edad</PropertyName><Literal>3</Literal></PropertyIsLessThan></Or></Or></Filter>";
	
//	public void test1(){
//		parseQuery(query1,filter1);
//	}
//	
//	public void test2(){
//		parseQuery(query2,filter2);
//	}
	
	public void test3(){
		parseQuery(query3,filter3);
	}
	
	private void parseQuery(String query, String filter){
//		FilterEncoding fe = new FilterEncoding(new SQLExpressionFormat());
//		fe.setQuery(query);
//		System.out.println(fe.toString());
//		assertEquals(fe.toString(), filter);
	}
}
