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
		String txt = "abcde ABCDE   ����� ����� ����� ����� ����� �����";
		StringTokenizer stTexto = new StringTokenizer(txt);
		String texto = "";
		while (stTexto.hasMoreElements())
			texto += stTexto.nextElement();
		assertEquals("abcdeABCDE������������������������������", texto);

	}

	public void testReplaceMarks() {
		String texto = "abcdeABCDE������������������������������";
		texto = texto.replace('�', 'a');
		texto = texto.replace('�', 'e');
		texto = texto.replace('�', 'i');
		texto = texto.replace('�', 'o');
		texto = texto.replace('�', 'u');
		texto = texto.replace('�', 'a');
		texto = texto.replace('�', 'e');
		texto = texto.replace('�', 'i');
		texto = texto.replace('�', 'o');
		texto = texto.replace('�', 'u');
		texto = texto.replace('�', 'a');
		texto = texto.replace('�', 'e');
		texto = texto.replace('�', 'i');
		texto = texto.replace('�', 'o');
		texto = texto.replace('�', 'u');
		texto = texto.replace('�', 'A');
		texto = texto.replace('�', 'E');
		texto = texto.replace('�', 'I');
		texto = texto.replace('�', 'O');
		texto = texto.replace('�', 'U');
		texto = texto.replace('�', 'A');
		texto = texto.replace('�', 'E');
		texto = texto.replace('�', 'I');
		texto = texto.replace('�', 'O');
		texto = texto.replace('�', 'U');
		texto = texto.replace('�', 'A');
		texto = texto.replace('�', 'E');
		texto = texto.replace('�', 'I');
		texto = texto.replace('�', 'O');
		texto = texto.replace('�', 'U');
		assertEquals("abcdeABCDEaeiouAEIOUaeiouAEIOUaeiouAEIOU", texto);
	}

	public void testToUppercase() {
		String texto = "abcdeABCDEaeiouAEIOUaeiouAEIOUaeiouAEIOU";
		String txt = texto.toUpperCase();
		assertEquals("ABCDEABCDEAEIOUAEIOUAEIOUAEIOUAEIOUAEIOU", txt);
	}

}
