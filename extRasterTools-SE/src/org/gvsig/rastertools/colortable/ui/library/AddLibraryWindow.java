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
package org.gvsig.rastertools.colortable.ui.library;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.gvsig.raster.beans.stretchselector.StretchSelectorData;
import org.gvsig.raster.datastruct.ColorItem;
import org.gvsig.raster.datastruct.ColorTable;

import com.iver.andami.PluginServices;

/**
 * Ventana para añadir una nueva libreria. En ella se especificara si queremos
 * una tabla de color por intervalos o el numero de cortes. La generará con el
 * nombre seleccionado y todos los colores seran negros inicialmente.
 * 
 * @version 01/10/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class AddLibraryWindow extends JOptionPane {
	private static final long   serialVersionUID  = 1L;
	private AddLibraryPanel     libraryPanel      = null;
	/**
	 * Si supera el numero de particiones estimado, se le preguntará al usuario si
	 * quiere continuar
	 */
	private int                 limit_question    = 500;
	
	/**
	 * Devuelve el componente interno del JOptionPane
	 * @return
	 */
	private Object getMessageComponent() {
		return getLibraryPanel();
	}
	
	/**
	 * Obtiene el panel AddLibraryPanel
	 * @return
	 */
	public AddLibraryPanel getLibraryPanel() {
		if(libraryPanel == null)
			libraryPanel = new AddLibraryPanel();
		return libraryPanel;
	}

	/**
	 * Muestra el diálogo de la creación de la tabla de color. Devolverá
	 * JOptionPane.OK_OPTION si el usuario le ha dado a aceptar y otro valor en
	 * caso contrario.
	 * @param parentComponent
	 * @return
	 */
	public int showConfirm(Component parentComponent) {
		return showConfirmDialog(
				parentComponent,
				getMessageComponent(),
				PluginServices.getText(this, "nueva_libreria_title"),
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);
	}

	/**
	 * Devuelve la tabla de color creada segun los parametros especificados en el
	 * panel.
	 * @return
	 */
	public ColorTable getColorTable() {
		ColorTable colorTable = new ColorTable();
		colorTable.setName(getLibraryPanel().getLibraryName().getText());
		ArrayList items = new ArrayList();

		StretchSelectorData stretchListData = getLibraryPanel().getStretchListPanel().getData(); 
		double start = stretchListData.getMinimum();
		double end = stretchListData.getMaximum();

		if (start > end) {
			double aux = start;
			start = end;
			end = aux;
		}

		double num = getLibraryPanel().getStretchListPanel().getInterval();
		boolean quest = false;
		if (getLibraryPanel().getStretchListPanel().getIntervalNumber().isSelected())
			quest = (num > limit_question);
		else
			quest = (((end - start) / num) > limit_question);
		if (quest && JOptionPane.showConfirmDialog(this, PluginServices.getText(this, "addlibrary_supera_limite"), PluginServices.getText(this, "confirmar"), JOptionPane.YES_NO_OPTION) == NO_OPTION)
			return null;
		if (getLibraryPanel().getStretchListPanel().getIntervalNumber().isSelected()) {
			for (double i = 0; i <= num; i++) {
				ColorItem colorItem = new ColorItem();
				colorItem.setValue(start + (((end - start) / num) * i));
				items.add(colorItem);
			}
		} else {
			for (double i = start; i <= end; i += num) {
				ColorItem colorItem = new ColorItem();
				colorItem.setValue(i);
				items.add(colorItem);
			}
		}

		colorTable.createPaletteFromColorItems(items, false);
		return colorTable;
	}
}
