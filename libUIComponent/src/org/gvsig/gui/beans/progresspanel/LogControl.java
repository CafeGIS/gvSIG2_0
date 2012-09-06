/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
package org.gvsig.gui.beans.progresspanel;
/**
 * <code>LogControl</code>. Objeto para un control básico de un log. Para añadir
 * y reemplazar la última línea añadida.
 *
 * @version 27/03/2007
 * @author BorSanZa - Borja Sanchez Zamorano (borja.sanchez@iver.es)
 */
public class LogControl {
	String text = "";

	/**
	 * Añade una línea al log.
	 * @param line
	 */
	public void addLine(String line) {
		if (text.length() > 0)
			text += "\n";
		text += line;
	}

	/**
	 * Reemplaza la última línea añadida al log.
	 * @param line
	 */
	public void replaceLastLine(String line) {
		int index = text.lastIndexOf("\n");
		if (index < 0)
			index = 0;
		text = text.substring(0, index);
		addLine(line);
	}

	/**
	 * Establece el texto completo del log.
	 * @param value
	 */
	public void setText(String value) {
		text = value;
	}

	/**
	 * Devuelve el contenido del log.
	 * @return String
	 */
	public String getText() {
		return text;
	}
}