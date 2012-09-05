/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ib��ez, 50
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

package com.iver.cit.gvsig.gui.preferencespage;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.iver.andami.PluginServices;
import com.iver.andami.preferences.AbstractPreferencePage;
import com.iver.andami.preferences.StoreException;
import com.iver.utiles.XMLEntity;
/**
 * CSV Driver preference page where the user can establish default values for
 * <ol>
 *  <li><b>separator</b></li>
 * </ol>
 * @author Vicente Caballero Navarro
 *
 */
//TODO comentado para que compile
public class DriverCSVPage extends AbstractPreferencePage{
	private static final String DEFAULT_SEPARATOR_CSV_DRIVER = "DefaultSeparatorCSVDriver";
	static String id = DriverCSVPage.class.getName();;
	private ImageIcon icon;
	private JTextField txtSeparator;

	/**
	 * Builds preference page where the user can establish default values for
	 * <ol>
	 *  <li><b>separator</b></li>
	 * </ol>
	 */
	public DriverCSVPage() {
		super();
		icon=PluginServices.getIconTheme().get("mapa-icono");
		addComponent(PluginServices.getText(this, "separador"), txtSeparator = new JTextField(5));
		setParentID(DriversPages.class.getName());
	}

	public void storeValues() throws StoreException {
		String separator;
		separator = txtSeparator.getText();
//		DriverManager dm=LayerFactory.getDM();
//		CSVStringDriver cvsDriver=null;
//		try {
//			cvsDriver = (CSVStringDriver)dm.getDriver("csv string");
//		} catch (DriverLoadException e) {
//			throw new StoreException();
//		}
//		cvsDriver.setSeparator(separator);
		PluginServices ps = PluginServices.getPluginServices(this);
		XMLEntity xml = ps.getPersistentXML();
		xml.putProperty(DEFAULT_SEPARATOR_CSV_DRIVER, separator);
	}

	public String getID() {
		return id;
	}

	public String getTitle() {
		return PluginServices.getText(this, "CSVStringDriver");
	}

	public JPanel getPanel() {
		return this;
	}

	public void initializeValues() {
		PluginServices ps = PluginServices.getPluginServices(this);
		XMLEntity xml = ps.getPersistentXML();
		if (xml.contains(DEFAULT_SEPARATOR_CSV_DRIVER)) {
			txtSeparator.setText(xml.getStringProperty(DEFAULT_SEPARATOR_CSV_DRIVER));
		}

	}

	public void initializeDefaults() {
		txtSeparator.setText(",");
	}

	public ImageIcon getIcon() {
		return icon;
	}

	public boolean isValueChanged() {
		return super.hasChanged();
	}

	public void setChangesApplied() {
		setChanged(false);
	}
}
