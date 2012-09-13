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
package org.gvsig.rastertools.saveraster;

import org.gvsig.rastertools.TestUI;
import org.gvsig.rastertools.saveraster.ui.info.EndInfoDialog;
/**
 * @version 05/11/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class EndInfoDialogTest {
	public EndInfoDialogTest(){
		TestUI.defaultInit();
		for (int i = 0; i <= 24; i++) {
			EndInfoDialog.show("C:\\Documents and Settings\\borja\\Escritorio\\a\\sagsadfhsdghssagsadfhsdghssagsadfhsdghssagsadfhsdghssagsadfhsdghssagsadfhsdghssagsadfhsdghssagsadfhsdghs\\" + i + ".png", 500);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new EndInfoDialogTest();
	}
}