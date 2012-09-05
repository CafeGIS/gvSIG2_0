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

/* CVS MESSAGES:
*
* $Id: LayoutPage.java 23757 2008-10-03 12:21:16Z vcaballero $
* $Log$
* Revision 1.7  2006-12-20 14:41:12  caballero
* Remodelado Layout
*
* Revision 1.6  2006/11/08 10:57:55  jaume
* remove unecessary imports
*
* Revision 1.5  2006/10/18 07:57:47  jaume
* *** empty log message ***
*
* Revision 1.4  2006/10/04 09:06:35  jaume
* *** empty log message ***
*
* Revision 1.3  2006/10/04 07:23:53  jaume
* refactored ambiguous methods and field names and added some more features for preference pages
*
* Revision 1.2  2006/10/03 11:12:41  jaume
* initialize values
*
* Revision 1.1  2006/10/03 10:33:34  jaume
* *** empty log message ***
*
*
*/
package com.iver.cit.gvsig.gui.preferencespage;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.iver.andami.PluginServices;
import com.iver.andami.preferences.AbstractPreferencePage;
import com.iver.andami.preferences.StoreException;
import com.iver.cit.gvsig.project.documents.layout.Attributes;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;
import com.iver.utiles.XMLEntity;
/**
 * Layout preference page where the user can establish default values for
 * <ol>
 *  <li><b>grid horizontal gap</b></li>
 *  <li><b>grid vertical gap</b></li>
 *  <li><b>show or hide grid</b></li>
 *  <li><b>adjust elements to grid</b></li>
 *  <li><b>show or hide rules</b></li>
 * </ol>
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 *
 */
public class LayoutPage extends AbstractPreferencePage{

	private static final boolean FACTORY_DEFAULT_LAYOUT_ENABLE_RULES = true;
	private static final boolean FACTORY_DEFAULT_LAYOUT_GRID_SHOW = true;
	private static final double FACTORY_DEFAULT_VERTICAL_GAP = 1;
	private static final double FACTORY_DEFAULT_HORIZONTAL_GAP = 1;
	private static final boolean FACTORY_DEFAULT_LAYOUT_GRID_ENABLE = false;
	private static final String DEFAULT_SHOW_LAYOUT_GRID_KEY_NAME = "DefaultShowLayoutGrid";
	private static final String DEFAULT_ENABLE_LAYOUT_GRID_KEY_NAME = "DefaultEnableLayoutGrid";
	private static final String DEFAULT_SHOW_LAYOUT_RULES_KEY_NAME = "DefaultShowLayoutRules";
	private static final String DEFAULT_LAYOUT_GRID_VERTICAL_GAP_KEY_NAME = "DefaultGridVerticalGap";
	private static final String DEFAULT_LAYOUT_GRID_HORIZONTAL_GAP_KEY_NAME = "DefaultGridHorizontalGap";
	static String id = LayoutPage.class.getName();;
	private ImageIcon icon;
	private JCheckBox chkGridEnabled;
	private JCheckBox chkShowRules;
	private JCheckBox chkShowGrid;
	private JTextField txtVGap;
	private JTextField txtHGap;

	/**
	 * Builds preference page where the user can establish default values for
	 * <ol>
	 *  <li><b>grid horizontal gap</b></li>
	 *  <li><b>grid vertical gap</b></li>
	 *  <li><b>show or hide grid</b></li>
	 *  <li><b>adjust elements to grid</b></li>
	 *  <li><b>show or hide rules</b></li>
	 * </ol>
	 */
	public LayoutPage() {
		super();

		icon = PluginServices.getIconTheme().get("mapa-icono");

		// horizontal gap text field
		addComponent(PluginServices.getText(this, "espaciado_horizontal"), txtHGap = new JTextField(5));

		// vertical gap text field
		addComponent(PluginServices.getText(this, "espaciado_vertical"), txtVGap = new JTextField(5));

		// show/hide show check
		addComponent(chkShowGrid = new JCheckBox(PluginServices.getText(this, "visualizar_cuadricula")));

		// enable/disable grid
		addComponent(chkGridEnabled = new JCheckBox(PluginServices.getText(this, "malla_activada")));

		// show/hide rules
		addComponent(chkShowRules = new JCheckBox(PluginServices.getText(this, "activar_regla")));


	}

	public void storeValues() throws StoreException {
		double hGap, vGap;
		boolean gridEnabled, showRules, showGrid;
		try {
			hGap = Double.parseDouble(txtHGap.getText());
			vGap = Double.parseDouble(txtVGap.getText());
			gridEnabled = chkGridEnabled.isSelected();
			showGrid = chkShowGrid.isSelected();
			showRules = chkShowRules.isSelected();
		} catch (Exception e) {
			throw new StoreException(PluginServices.getText(this, "invalid_value_for_gap"));
		}
		Layout.setDefaultShowGrid(showGrid);
		Layout.setDefaultAdjustToGrid(gridEnabled);
		Layout.setDefaultShowRulers(showRules);
		Attributes.setDefaultGridGap(hGap, vGap);
		PluginServices ps = PluginServices.getPluginServices(this);
		XMLEntity xml = ps.getPersistentXML();
		xml.putProperty(DEFAULT_LAYOUT_GRID_HORIZONTAL_GAP_KEY_NAME, hGap);
		xml.putProperty(DEFAULT_LAYOUT_GRID_VERTICAL_GAP_KEY_NAME, vGap);
		xml.putProperty(DEFAULT_SHOW_LAYOUT_GRID_KEY_NAME, showGrid);
		xml.putProperty(DEFAULT_ENABLE_LAYOUT_GRID_KEY_NAME, gridEnabled);
		xml.putProperty(DEFAULT_SHOW_LAYOUT_RULES_KEY_NAME, showRules);
	}

	public String getID() {
		return id;
	}

	public String getTitle() {
		return PluginServices.getText(this, "Mapa");
	}

	public JPanel getPanel() {
		return this;
	}

	public void initializeValues() {
		PluginServices ps = PluginServices.getPluginServices(this);
		XMLEntity xml = ps.getPersistentXML();
		double hGap=FACTORY_DEFAULT_HORIZONTAL_GAP;
		double vGap=FACTORY_DEFAULT_VERTICAL_GAP;
		boolean showGrid=FACTORY_DEFAULT_LAYOUT_GRID_SHOW;
		boolean gridEnabled=FACTORY_DEFAULT_LAYOUT_GRID_ENABLE;
		boolean showRules=FACTORY_DEFAULT_LAYOUT_ENABLE_RULES;
		// horizontal gap
		if (xml.contains(DEFAULT_LAYOUT_GRID_HORIZONTAL_GAP_KEY_NAME)) {
			hGap=xml.getDoubleProperty(DEFAULT_LAYOUT_GRID_HORIZONTAL_GAP_KEY_NAME);
		}
		txtHGap.setText(String.valueOf(hGap));

		// vertical gap
		if (xml.contains(DEFAULT_LAYOUT_GRID_VERTICAL_GAP_KEY_NAME)) {
			vGap=xml.getDoubleProperty(DEFAULT_LAYOUT_GRID_VERTICAL_GAP_KEY_NAME);
		}
		txtVGap.setText(String.valueOf(vGap));

		// show/hide grid check
		if (xml.contains(DEFAULT_SHOW_LAYOUT_GRID_KEY_NAME)) {
			showGrid=xml.getBooleanProperty(DEFAULT_SHOW_LAYOUT_GRID_KEY_NAME);
		}
		chkShowGrid.setSelected(showGrid);

		// enable/disable grid check
		if (xml.contains(DEFAULT_ENABLE_LAYOUT_GRID_KEY_NAME)) {
			gridEnabled=xml.getBooleanProperty(DEFAULT_ENABLE_LAYOUT_GRID_KEY_NAME);
		}
		chkGridEnabled.setSelected(gridEnabled);

		// enable/disable rules
		if (xml.contains(DEFAULT_SHOW_LAYOUT_RULES_KEY_NAME)) {
			showRules=xml.getBooleanProperty(DEFAULT_SHOW_LAYOUT_RULES_KEY_NAME);
		}
		chkShowRules.setSelected(showRules);

		Layout.setDefaultShowGrid(showGrid);
		Layout.setDefaultAdjustToGrid(gridEnabled);
		Layout.setDefaultShowRulers(showRules);
		Attributes.setDefaultGridGap(hGap, vGap);
	}

	public void initializeDefaults() {
		txtHGap.setText(String.valueOf(FACTORY_DEFAULT_HORIZONTAL_GAP));
		txtVGap.setText(String.valueOf(FACTORY_DEFAULT_VERTICAL_GAP));
		chkShowGrid.setSelected(FACTORY_DEFAULT_LAYOUT_GRID_SHOW);
		chkGridEnabled.setSelected(FACTORY_DEFAULT_LAYOUT_GRID_ENABLE);
		chkShowRules.setSelected(FACTORY_DEFAULT_LAYOUT_ENABLE_RULES);
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
