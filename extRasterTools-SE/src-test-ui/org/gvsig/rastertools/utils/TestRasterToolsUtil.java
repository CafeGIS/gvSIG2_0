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
package org.gvsig.rastertools.utils;

import java.awt.Toolkit;

import javax.swing.UIManager;

import org.gvsig.raster.util.RasterToolsUtil;

public class TestRasterToolsUtil {

	public static void main(String[] args) {
		Toolkit.getDefaultToolkit().setDynamicLayout(true);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
		boolean result = RasterToolsUtil.messageBoxYesOrNot("¿Eres la Persona mas negativa del mundo?\n\nPulsa en algun boton de estos.", null);
		RasterToolsUtil.messageBoxError("Has pulsado un valor: \n\n    " + result, null);
		RasterToolsUtil.messageBoxInfo("Has pulsado un valor: \n\n    " + result, null);
	}
}
