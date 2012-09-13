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
package org.gvsig.raster.gui.preferences.panels;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.ListCellRenderer;

import org.gvsig.raster.Configuration;
import org.gvsig.raster.datastruct.ColorTable;
import org.gvsig.raster.datastruct.persistence.ColorTableLibraryPersistence;
import org.gvsig.raster.gui.preferences.combocolortable.PaintItem;
import org.gvsig.raster.gui.preferences.combocolortable.PreferenceColorTableIconPainter;
import org.gvsig.raster.util.BasePanel;
/**
 * PreferenceLoadLayer es una clase para la configuracion de las preferencias
 * de una capa raster.
 * 
 * @version 14/12/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class PreferenceLoadLayer extends BasePanel implements ActionListener {
	private static final long    serialVersionUID      = 1L;
	private JComboBox            comboBoxColorTable    = null;
	private JRadioButton         radioButtonRealce     = null;
	private JRadioButton         radioButtonColorTable = null;
	private JLabel               labelLoadLayer        = null;
	private ButtonGroup          buttonGroup           = null;

	/**
	 *Inicializa componentes gráficos y traduce
	 */
	public PreferenceLoadLayer() {
		init();
		translate();
	}
	
	/**
	 * Traduce los textos de esta clase
	 */
	protected void translate() {
		setBorder(BorderFactory.createTitledBorder(getText(this, "carga_capas")));
		getLabelLoadLayer().setText(getText(this, "loadlayer_aplicar") + ":");
		getRadioButtonRealce().setText(getText(this, "realce"));
		getRadioButtonColorTable().setText(getText(this, "tabla_color"));
	}

	protected void init() {
		GridBagConstraints gridBagConstraints;

		setLayout(new GridBagLayout());

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(5, 5, 2, 5);
		add(getLabelLoadLayer(), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(2, 5, 2, 5);
		add(getRadioButtonRealce(), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(2, 5, 5, 2);
		add(getRadioButtonColorTable(), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(2, 2, 5, 5);
		add(getComboBoxColorTable(), gridBagConstraints);
	}
	
	/**
	 * Obtiene el path del fichero de paletas
	 * @return
	 */
	public String getPalettesPath() {
		return System.getProperty("user.home") +
				File.separator +
				"gvSIG" + // PluginServices.getArguments()[0] +
				File.separator + "colortable";
	}
	
	/**
	 * Obtiene el grupo de botones 
	 * @return ButtonGroup
	 */
	public ButtonGroup getButtonGroup() {
		if(buttonGroup == null)
			buttonGroup = new ButtonGroup();
		return buttonGroup;
	}

	public JRadioButton getRadioButtonRealce() {
		if (radioButtonRealce == null) {
			radioButtonRealce = new JRadioButton();
			getButtonGroup().add(radioButtonRealce);
			radioButtonRealce.addActionListener(this);
			radioButtonRealce.setSelected(true);
			radioButtonRealce.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			radioButtonRealce.setMargin(new Insets(0, 0, 0, 0));
		}
		return radioButtonRealce;
	}

	public JRadioButton getRadioButtonColorTable() {
		if (radioButtonColorTable == null) {
			radioButtonColorTable = new JRadioButton();
			getButtonGroup().add(radioButtonColorTable);
			radioButtonColorTable.addActionListener(this);
			radioButtonColorTable.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			radioButtonColorTable.setMargin(new Insets(0, 0, 0, 0));
		}
		return radioButtonColorTable;
	}

	public JLabel getLabelLoadLayer() {
		if (labelLoadLayer == null) {
			labelLoadLayer = new JLabel();
		}
		return labelLoadLayer;
	}

	public JComboBox getComboBoxColorTable() {
		if (comboBoxColorTable == null) {
			comboBoxColorTable = new JComboBox();

			ArrayList fileList = ColorTableLibraryPersistence.getPaletteFileList(getPalettesPath());

			for (int i = 0; i < fileList.size(); i++) {
				ArrayList paletteItems = new ArrayList();
				String paletteName = ColorTableLibraryPersistence.loadPalette(getPalettesPath(), (String) fileList.get(i), paletteItems);

				if (paletteItems.size() <= 0)
					continue;

				ColorTable colorTable = new ColorTable();
				colorTable.setName(paletteName);
				colorTable.createPaletteFromColorItems(paletteItems, true);
				colorTable.setInterpolated(true);

				ArrayList array = new ArrayList();
				array.add(paletteName);
				array.add(new PreferenceColorTableIconPainter(colorTable));

				comboBoxColorTable.addItem(array);
			}

			comboBoxColorTable.setRenderer(new ListCellRenderer() {
				public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
					ArrayList array = (ArrayList) value;

					PaintItem paintItem = new PaintItem((String) array.get(0), (PreferenceColorTableIconPainter) array.get(1), isSelected);
					return paintItem;
				}
			});
		}
		return comboBoxColorTable;
	}

	private void setColorTableEnabled(boolean enabled) {
		getComboBoxColorTable().setEnabled(enabled);
	}

	/**
	 * Establece los valores por defecto de la clase
	 */
	public void initializeDefaults() {
		getRadioButtonRealce().setSelected(true);
		setColorTableEnabled(false);
		selectComboName("Default");
	}

	private boolean selectComboName(String name) {
		if (name != null) {
			for (int i=0; i<getComboBoxColorTable().getItemCount(); i++) {
				if (((String) ((ArrayList) getComboBoxColorTable().getItemAt(i)).get(0)).equals(name)) {
					getComboBoxColorTable().setSelectedIndex(i);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Establece los valores que ha definido el usuario en los componentes
	 */
	public void initializeValues() {
		String colorTable = Configuration.getValue("loadlayer_usecolortable", (String) null);

		boolean finded = selectComboName(colorTable);

		if (finded == false) {
			getRadioButtonRealce().setSelected(true);
			setColorTableEnabled(false);
			Configuration.setValue("loadlayer_usecolortable", (String) null);
			selectComboName("Default");
		} else {
			getRadioButtonColorTable().setSelected(true);
			setColorTableEnabled(true);
		}
	}

	/**
	 * Guarda los valores que hay en los componentes como valores de configuracion
	 */
	public void storeValues() {
		if (getRadioButtonColorTable().isSelected())
			Configuration.setValue("loadlayer_usecolortable", (String) ((ArrayList) getComboBoxColorTable().getSelectedItem()).get(0));
		else
			Configuration.setValue("loadlayer_usecolortable", null);
	}

	public void actionPerformed(ActionEvent e) {
		if (getRadioButtonColorTable().isSelected()) {
			setColorTableEnabled(true);
		} else {
			setColorTableEnabled(false);
		}
	}
}