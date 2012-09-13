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
package org.gvsig.rastertools.colortable.combobox;

import java.awt.Component;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.gvsig.raster.datastruct.ColorTable;
import org.gvsig.raster.datastruct.persistence.ColorTableLibraryPersistence;
import org.gvsig.rastertools.colortable.ui.library.ColorTableIconPainter;
/**
 *
 * @version 17/04/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class TestComboBoxColorTable {
	private JFrame      frame        = new JFrame();
	private JComboBox   jComboBox    = null;

	private String palettesPath = System.getProperty("user.home") +
	File.separator +
	"gvSIG" + // PluginServices.getArguments()[0] +
	File.separator + "colortable";

	public TestComboBoxColorTable() {
		super();
		initialize();
	}

	public static void main(String[] args){
		new TestComboBoxColorTable();
	}

	private void initialize() {
		jComboBox = new JComboBox();

		ArrayList fileList = ColorTableLibraryPersistence.getPaletteFileList(palettesPath);

		for (int i = 0; i < fileList.size(); i++) {
			ArrayList paletteItems = new ArrayList();
			String paletteName = ColorTableLibraryPersistence.loadPalette(palettesPath, (String) fileList.get(i), paletteItems);

			if (paletteItems.size() <= 0)
				continue;

			ColorTable colorTable = new ColorTable();
			colorTable.setName(paletteName);
			colorTable.createPaletteFromColorItems(paletteItems, true);
			colorTable.setInterpolated(true);

			ArrayList array = new ArrayList();
			array.add(paletteName);
			array.add(new ColorTableIconPainter(colorTable));

			jComboBox.addItem(array);
		}

		frame.setContentPane(jComboBox);
		frame.setSize(new java.awt.Dimension(400, 60));
		frame.setResizable(true);
		frame.setTitle("Tablas de color");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		jComboBox.setRenderer(new ListCellRenderer() {
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				ArrayList array = (ArrayList) value;


				PaintItem paintItem = new PaintItem((String) array.get(0), (ColorTableIconPainter) array.get(1), isSelected);
				return paintItem;
			}
		});
	}
}