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
 * 2008 PRODEVELOP		Main development
 */

package org.gvsig.geocoding;

import java.util.StringTokenizer;

import junit.framework.TestCase;

/**
 * Test
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */

public class FormattingOperationsTest extends TestCase {

	protected void setUp() {

	}

	protected void tearDown() {
	}

	public void testRemoveSpaces() {
		String txt = "abcde ABCDE   äëïöü ÄËÏÖÜ áéíóú ÁÉÍÓÚ àèìòù ÀÈÌÒÙ";
		StringTokenizer stTexto = new StringTokenizer(txt);
		String texto = "";
		while (stTexto.hasMoreElements())
			texto += stTexto.nextElement();
		assertEquals("abcdeABCDEäëïöüÄËÏÖÜáéíóúÁÉÍÓÚàèìòùÀÈÌÒÙ", texto);

	}

	public void testReplaceMarks() {
		String texto = "abcdeABCDEäëïöüÄËÏÖÜáéíóúÁÉÍÓÚàèìòùÀÈÌÒÙ";
		texto = texto.replace('á', 'a');
		texto = texto.replace('é', 'e');
		texto = texto.replace('í', 'i');
		texto = texto.replace('ó', 'o');
		texto = texto.replace('ú', 'u');
		texto = texto.replace('à', 'a');
		texto = texto.replace('è', 'e');
		texto = texto.replace('ì', 'i');
		texto = texto.replace('ò', 'o');
		texto = texto.replace('ù', 'u');
		texto = texto.replace('ä', 'a');
		texto = texto.replace('ë', 'e');
		texto = texto.replace('ï', 'i');
		texto = texto.replace('ö', 'o');
		texto = texto.replace('ü', 'u');
		texto = texto.replace('Á', 'A');
		texto = texto.replace('É', 'E');
		texto = texto.replace('Í', 'I');
		texto = texto.replace('Ó', 'O');
		texto = texto.replace('Ú', 'U');
		texto = texto.replace('À', 'A');
		texto = texto.replace('È', 'E');
		texto = texto.replace('Ì', 'I');
		texto = texto.replace('Ò', 'O');
		texto = texto.replace('Ù', 'U');
		texto = texto.replace('Ä', 'A');
		texto = texto.replace('Ë', 'E');
		texto = texto.replace('Ï', 'I');
		texto = texto.replace('Ö', 'O');
		texto = texto.replace('Ü', 'U');
		assertEquals("abcdeABCDEaeiouAEIOUaeiouAEIOUaeiouAEIOU", texto);
	}

	public void testToUppercase() {
		String texto = "abcdeABCDEaeiouAEIOUaeiouAEIOUaeiouAEIOU";
		String txt = texto.toUpperCase();
		assertEquals("ABCDEABCDEAEIOUAEIOUAEIOUAEIOUAEIOUAEIOU", txt);
	}

}
