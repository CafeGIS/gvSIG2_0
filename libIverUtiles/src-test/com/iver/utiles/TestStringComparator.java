package com.iver.utiles;

import java.text.Collator;
import java.util.Collections;
import java.util.Locale;
import java.util.Vector;
import java.util.List;

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

/**
 * Tests the methods of the class <i>StringComparator</i>
 * 
 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
 */
public class TestStringComparator {
	/**
	 * Test method for the TestMathExtension class
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		StringComparator stringComparator = new StringComparator();
		
		// Initialize the vector of Strings (for test methods with the parameter Comparator)
		Vector v = new Vector();
		v.add("extWMS");
		v.add("libUI");
		v.add("extWMS");
		v.add("libRemoteServices");
		v.add("extWFS2");
		v.add("libNomenclatorIGN");
		v.add("libNomenclatorIGN_GUI");
		v.add("libIverUtiles");
		v.add("extWFS2");
		v.add("EXTWFS2");
		v.add("extWFS2");
		v.add("libGDBMS");
		v.add("libInternationalization");
		v.add("libFMap");
		v.add("6");
		v.add("4");
		v.add("3");
		v.add("5");
		v.add("2");
		v.add("1");
		v.add("extWFS2");
		v.add("libExceptions");
		v.add("libDriverManager");
		v.add("libCq CMS for java");
		v.add("libCorePlugin");
		v.add("extWFS2");
		v.add("extAddIDEELayers");
		v.add("extAlertCClient");
		v.add("extCAD");
		v.add("extWFS2");
		v.add("ÑandÚ");
		v.add("ñandú");
		v.add("extJDBC");
		v.add("extMyPlugin");
		v.add("extRasterTools");
		v.add("extScripting");
		v.add("extWCS");
		v.add("extWFS2");
		v.add("extwfs2");		// If both are null -> true
		v.add("extWMS");
		v.add("extWMS");
		v.add("extWCS");
		v.add("7");
		v.add("9");
		v.add("8");
		v.add("0");
		v.add("EXTWCS");
		v.add("Ñandú");
		v.add("extensión");
		v.add("ÑANDÚ");
		v.add("_fwAndami");
		v.add("abcdefghijklmnñopqrstuvwxyz");
		v.add("ç");
		v.add("appgvSIG");
		v.add("la casa");
		v.add("la montaña");
		v.add("la colina");
		v.add("las abejas");
		v.add("las nutrias");
		v.add("las ballenas");
		v.add("lasaña");
		v.add("Vector");
		v.add("avión");
		v.add("camión");
		v.add("autobús");
		
		// Show the original vector
		System.out.println("Vector original: " + v.toString() + "\n");
		
		// First kind of sort
		stringComparator.setCaseSensitive(true);
		List list = v.subList(0, v.size());
		Collections.sort(list, stringComparator);
		System.out.println("Ordenación normal con case-sensitive:" + list.toString() + "\n");
		
		// Second kind of sort
		stringComparator.setCaseSensitive(false);
		list = v.subList(0, v.size());
		Collections.sort(list, stringComparator);
		System.out.println("Ordenación normal sin case-sensitive:" + list.toString() + "\n");
		
		
		Collator collator = Collator.getInstance(new Locale("es_ES"));
		stringComparator.setLocaleRules(stringComparator.new LocaleRules(true, collator));
		
		// Third kind of sort
		stringComparator.setCaseSensitive(true);
		list = v.subList(0, v.size());
		Collections.sort(list, stringComparator);
		System.out.println("Ordenación con reglas locales y con case-sensitive:" + list.toString() + "\n");

		// Fourth kind of sort
		stringComparator.setCaseSensitive(false);
		list = v.subList(0, v.size());
		Collections.sort(list, stringComparator);
		System.out.println("Ordenación con reglas locales y sin case-sensitive:" + list.toString() + "\n");
		
		
		stringComparator.getLocaleRules().setUseLocaleRules(false);
		
		// Fifth kind of sort
		stringComparator.setCaseSensitive(true);
		list = v.subList(0, v.size());
		Collections.sort(list, stringComparator);
		System.out.println("Ordenación normal con case-sensitive:" + list.toString() + "\n");

		// Sixth kind of sort
		stringComparator.setCaseSensitive(false);
		list = v.subList(0, v.size());
		Collections.sort(list, stringComparator);
		System.out.println("Ordenación normal sin case-sensitive:" + list.toString() + "\n");
	}	
}
