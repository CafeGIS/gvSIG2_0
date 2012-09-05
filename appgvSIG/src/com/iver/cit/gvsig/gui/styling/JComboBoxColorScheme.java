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
package com.iver.cit.gvsig.gui.styling;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.gvsig.raster.datastruct.ColorItem;
import org.gvsig.raster.datastruct.ColorTable;
import org.gvsig.raster.datastruct.persistence.ColorTableLibraryPersistence;

import com.iver.andami.PluginServices;

/**
 * Creates a JComboBox where the user has the option to select different
 * colors.
 *
 * @autor jaume dominguez faus - jaume.dominguez@iver.es
 */
public class JComboBoxColorScheme extends JComboBox {
	private String palettesPath = System.getProperty("user.home") +
	File.separator +
	"gvSIG" +
	File.separator +
	"ColorSchemes";
	private boolean interpolated = false;
	private ArrayList fileList = new ArrayList();

	private ActionListener innerActionUpdateTooltip = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			Object o = getSelectedItem();
			if (o != null) {
				ArrayList aux = (ArrayList) o;
				setToolTipText((String) aux.get(0));
			} else {
				setToolTipText(PluginServices.getText(this, "select_a_color_scheme"));
			}
		}
	};
	/**
	 * Constructor method
	 *
	 * @param interpolateValues
	 */
	public JComboBoxColorScheme(boolean interpolateValues) {
		super();
		interpolated = interpolateValues;
		setPreferredSize(new Dimension(150, 20));
		ArrayList fileList = ColorTableLibraryPersistence.getPaletteFileList(palettesPath);


		for (int i = 0; i < fileList.size(); i++) {
			ArrayList paletteItems = new ArrayList();
			String paletteName = ColorTableLibraryPersistence.loadPalette(
						palettesPath,
						(String) fileList.get(i),
						paletteItems);

			if (paletteItems.size() <= 0) {
				continue;
			}

			ColorTable colorTable = new ColorTable();
			colorTable.setName(paletteName);
			colorTable.createPaletteFromColorItems(paletteItems, true);
			colorTable.setInterpolated(interpolateValues);

			ArrayList array = new ArrayList();
			array.add(paletteName);
			array.add(new ColorTablePaint(colorTable));

			addItem(array);
		}
		addActionListener(innerActionUpdateTooltip);
		setRenderer(new ListCellRenderer() {
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				ArrayList array = (ArrayList) value;

				ColorSchemeItemPainter paintItem = new ColorSchemeItemPainter((String) array.get(0), (ColorTablePaint) array.get(1), isSelected);
				paintItem.setPreferredSize(getPreferredSize());
				return paintItem;
			}
		});

	}
	/**
	 * Returns an array composed with the selected colors
	 * @return
	 */
	public ColorItem[] getSelectedColors() {
		Object o = getSelectedItem();

		if (o == null) {
			return null;
		}

		ColorTable ct = ((ColorTablePaint) ((ArrayList) o).get(1)).colorTable;
		return (ColorItem[]) ct.getColorItems().toArray(new ColorItem[0]) ;
	}

	public void setSelectedColors(ColorItem[] colors) {

		fileList.clear();

		if (colors == null) {
			setSelectedIndex(0);
			return;
		}
		else {

			for (int i = 0; i < getItemCount(); i++) {
				fileList.add(getItemAt(i));
			}

			for (int i = 0; i < fileList.size(); i++) {
				Object o = fileList.get(i);
				ColorTable ct = ((ColorTablePaint) ((ArrayList) o).get(1)).colorTable;
				ColorItem[] myColors = (ColorItem[]) ct.getColorItems().toArray(new ColorItem[0]) ;

				boolean isEqual = true;
				if(myColors.length == colors.length) {
					for (int j = 0; isEqual && j < myColors.length; j++) {
						Color c1 = myColors[j].getColor();
						Color c2 = colors[j].getColor();
						isEqual = c1.getRGB() == c2.getRGB() && c1.getAlpha() == c2.getAlpha();
					}
					if(isEqual) {
						setSelectedIndex(i);
						repaint();
						return;
					}
				}
			}
			if(getItemCount()> 0) {
				setSelectedItem(0);
			}
		}

	}

	/**
	 *
	 * Class to paint a color palette in a box. It can be indicated if the palette
	 * is selected and if it is painted with interpolations
	 *
	 * @version 30/07/2007
	 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
	 */
	private class ColorTablePaint {
		private ColorTable colorTable;

		/**
		 * Build a ColorTablePaint with a color table (parameter or the method)
		 *
		 * @param colorTable
		 */
		public ColorTablePaint(ColorTable colorTable) {
			this.colorTable = colorTable;
		}

		/**
		 * Defines if the values are interpolated between them or not.
		 *
		 * @param value
		 */
		public void setInterpolated(boolean value) {
			colorTable.setInterpolated(value);
		}

		/**
		 * Obtains the color array of the color palette
		 *
		 * @return
		 */
		public ArrayList getColorItems() {
			return colorTable.getColorItems();
		}

		/**
		 * Obtains the ColorTable
		 * @return
		 */
		public ColorTable getColorTable() {
			return colorTable;
		}

		/**
		 * Specifies the colors of the color table, defining it the values are
		 * interpolated and if the palette will be compressed or not.
		 *
		 * @param value
		 * @param interpolated
		 * @param compress
		 */
		public void setColorItems(ArrayList value, boolean interpolated, boolean compress) {
			colorTable.createPaletteFromColorItems(value, compress);
			setInterpolated(interpolated);
		}

		/**
		 * Method to paint the color table
		 *
		 * @param g
		 * @param isSelected
		 */
		public void paint(Graphics2D g, boolean isSelected, Rectangle bounds) {
			Rectangle area = bounds;
			area.y=0;
			area.x=0;
			int x1 = area.x;
			int x2 = area.x + area.width - 1;

			if (colorTable.getColorItems().size()>=1) {
				double min = ((ColorItem) colorTable.getColorItems().get(0)).getValue();
				double max = ((ColorItem) colorTable.getColorItems().get(colorTable.getColorItems().size()-1)).getValue();
				for (int i = area.x; i < (area.x + area.width); i++) {
					double pos = min + (((max - min) * (i - area.x)) / (area.width - 2));

					byte[] col3 = colorTable.getRGBAByBand(pos);
					g.setColor(new Color(col3[0] & 0xff, col3[1] & 0xff, col3[2] & 0xff));
					g.drawLine(i, area.y, i, area.y + area.height);
				}
			} else {
				g.setColor(new Color(224, 224, 224));
				g.fillRect(x1, area.y, x2 - x1, area.height - 1);
			}
			if (isSelected) {
				g.setColor(Color.black);
			} else {
				g.setColor(new Color(96, 96, 96));
			}
			g.drawRect(x1, area.y, x2 - x1, area.height - 1);
		}
	}

	private class ColorSchemeItemPainter extends JComponent {
		private static final long serialVersionUID = -6448740563809113949L;
		private boolean isSelected = false;
		private ColorTablePaint colorTablePaint = null;
		/**
		 * Constructor method
		 *
		 * @param name
		 * @param colorTablePaint
		 * @param isSelected
		 */
		public ColorSchemeItemPainter(String name, ColorTablePaint colorTablePaint, boolean isSelected) {
			super();
			this.colorTablePaint = colorTablePaint;
			this.isSelected = isSelected;
			setToolTipText(name);
		}

		public void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			if (isSelected) {
				Color color1 = new Color(89, 153, 229);
				Color color2 = new Color(31, 92, 207);
				g2.setPaint(new GradientPaint(0, 1, color1, 0, getHeight() - 1, color2, false));
				g2.fillRect(0, 1, getWidth(), getHeight() - 1);
				g2.setColor(new Color(61, 123, 218));
				g2.drawLine(0, 0, getWidth(), 0);
				g2.setColor(Color.white);
			} else {
				g2.setColor(Color.white);
				g2.fillRect(0, 0, getWidth(), getHeight());
				g2.setColor(Color.black);
			}

			colorTablePaint.paint(g2, isSelected, getBounds());
		}
	}
}
