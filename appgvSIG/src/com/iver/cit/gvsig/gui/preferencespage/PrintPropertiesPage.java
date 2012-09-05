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

/* CVS MESSAGES:
*
* $Id: PrintPropertiesPage.java 15647 2007-10-30 12:03:52Z jmvivo $
* $Log$
* Revision 1.1  2006-10-18 07:57:47  jaume
* *** empty log message ***
*
*
*/
package com.iver.cit.gvsig.gui.preferencespage;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.iver.andami.PluginServices;
import com.iver.andami.preferences.AbstractPreferencePage;
import com.iver.andami.preferences.StoreException;

public class PrintPropertiesPage extends AbstractPreferencePage{

	private static final boolean FACTORY_DEFAULT_LANDSCAPED_PAGE = true;
	protected static String id = PrintPropertiesPage.class.getName();
	private ImageIcon icon;
	private JRadioButton rdBtnPortraitPage;
	private JRadioButton rdBtnLandscapePage;
	private JCheckBox chkCustomMargins;
	private JTextField txtTopMargin;
	private JTextField txtLeftMargin;
	private JTextField txtBottomMargin;
	private JTextField txtRightMargin;

	public PrintPropertiesPage() {
		super();
		setParentID(LayoutPage.class.getName());
		icon = PluginServices.getIconTheme().get("prepare-page-icon");
		rdBtnPortraitPage = new JRadioButton(PluginServices.getText(this, "options.layout.paper_properties.portrait"));
		ImageIcon portrait = PluginServices.getIconTheme().get("portrait-page-setup");
		rdBtnLandscapePage = new JRadioButton(PluginServices.getText(this, "options.layout.paper_properties.landscaped"));
		ImageIcon landscape = PluginServices.getIconTheme().get("landscape-page-setup");

		ButtonGroup group = new ButtonGroup();
		group.add(rdBtnLandscapePage);
		group.add(rdBtnPortraitPage);

		JPanel aux = new JPanel(new GridLayout(2,2,10,0));
		aux.setPreferredSize(new Dimension(200, 150));
		aux.setSize(200, 150);
		aux.add(new JLabel(landscape));
		aux.add(new JLabel(portrait));
		aux.add(rdBtnLandscapePage);
		aux.add(rdBtnPortraitPage);

		addComponent(new JLabel(PluginServices.
			getText(this, "options.layout.paper_properties.paper_direction")));
		addComponent("", aux);
		addComponent(chkCustomMargins = new JCheckBox(PluginServices.getText(this, "personalizar_margenes")));

		JPanel aux2 = new JPanel(new GridLayout(2, 4, 10, 3));
		aux2.add(new JLabel(PluginServices.getText(this, "Superior")));
		aux2.add(txtTopMargin = new JTextField(10));
		aux2.add(new JLabel(PluginServices.getText(this, "Izquierdo")));
		aux2.add(txtLeftMargin = new JTextField(10));
		aux2.add(new JLabel(PluginServices.getText(this, "Inferior")));
		aux2.add(txtBottomMargin = new JTextField(10));
		aux2.add(new JLabel(PluginServices.getText(this, "Derecho")));
		aux2.add(txtRightMargin = new JTextField(10));
		addComponent("", aux2);
	}

	public void storeValues() throws StoreException {
		// TODO Auto-generated method stub

	}

	public void setChangesApplied() {
		// TODO Auto-generated method stub

	}

	public String getID() {
		return id;
	}

	public String getTitle() {
		return PluginServices.getText(this, "options.layout.paper_properties.title");
	}

	public JPanel getPanel() {
		return this;
	}

	public void initializeValues() {
		// TODO Auto-generated method stub

	}

	public void initializeDefaults() {
		rdBtnLandscapePage.setSelected(FACTORY_DEFAULT_LANDSCAPED_PAGE);
		rdBtnPortraitPage.setSelected(!FACTORY_DEFAULT_LANDSCAPED_PAGE);
	}

	public ImageIcon getIcon() {
		return icon;
	}

	public boolean isValueChanged() {
		// TODO Auto-generated method stub
		return false;
	}

}
