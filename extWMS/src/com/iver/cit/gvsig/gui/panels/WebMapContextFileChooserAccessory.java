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
* $Id: WebMapContextFileChooserAccessory.java 13881 2007-09-19 16:22:04Z jaume $
* $Log$
* Revision 1.7  2007-09-19 16:15:41  jaume
* removed unnecessary imports
*
* Revision 1.6  2007/06/27 06:29:27  jaume
* Views jcombobox bug fixed
*
* Revision 1.5  2006/09/20 07:45:21  caballero
* constante registerName
*
* Revision 1.4  2006/09/18 08:28:44  caballero
* cambio de nombre
*
* Revision 1.3  2006/09/15 10:44:24  caballero
* extensibilidad de documentos
*
* Revision 1.2  2006/04/21 11:34:30  jaume
* *** empty log message ***
*
* Revision 1.1  2006/04/19 07:57:29  jaume
* *** empty log message ***
*
* Revision 1.1  2006/04/12 17:10:53  jaume
* *** empty log message ***
*
*
*/
package com.iver.cit.gvsig.gui.panels;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.project.documents.view.ProjectView;
import com.iver.cit.gvsig.project.documents.view.ProjectViewFactory;
import com.iver.cit.gvsig.project.documents.view.gui.View;

public class WebMapContextFileChooserAccessory extends JPanel {
	public static int NEW_VIEW = -1;
	public static int CURRENT_VIEW = -1;
	public static int USERS_VIEW_SELECTION = -1;

	private JComboBox cmbMode = null;
	private JComboBox cmbViews = null;
	private JLabel lblExplain = null;
	private String currentView = null;

	public WebMapContextFileChooserAccessory(String currentViewName) {
		super();
		currentView = currentViewName;
		initialize();
	}

	private void initialize() {
		lblExplain = new JLabel();
		lblExplain.setBounds(25, 52, 268, 55);
		lblExplain.setText(PluginServices.getText(this, "html_in_a_new_view"));
		setLayout(null);
		setSize(315, 240);
		setPreferredSize(new Dimension(315, 72));
		this.add(getCmbViews(), null);
		this.add(lblExplain, null);
		setBorder(javax.swing.BorderFactory.createTitledBorder(
				  null, PluginServices.getText(this, "open_layers_as"),
				  javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				  javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
		this.add(getCmbMode(), null);
	}

	/**
	 * This method initializes cmbBoxMode
	 *
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getCmbMode() {
		if (cmbMode == null) {
			cmbMode = new JComboBox();
			cmbMode.setBounds(25, 26, 268, 20);
			cmbMode.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					cmbViews.setVisible(false);

					if (cmbMode.getSelectedIndex() == NEW_VIEW) {
						lblExplain.setText(PluginServices.getText(this, "html_in_a_new_view"));
					} else if (cmbMode.getSelectedIndex() == CURRENT_VIEW) {
						lblExplain.setText(PluginServices.getText(this, "html_in_the_current_view"));
					} else if (cmbMode.getSelectedIndex() == USERS_VIEW_SELECTION) {
						lblExplain.setText(PluginServices.getText(this, "html_in_other_view"));
						cmbViews.setVisible(true);
					}
				}
			});
			cmbMode.removeAllItems();
			cmbMode.addItem(PluginServices.getText(this, "a_new_view"));
			if (currentView!=null) {
				cmbMode.addItem(PluginServices.getText(this, "layers_in_the_current_view")+": "+currentView);
				CURRENT_VIEW = cmbMode.getItemCount()-1;
				cmbMode.setSelectedIndex(CURRENT_VIEW);
			}
			cmbViews.removeAllItems();
			ProjectExtension pe = (ProjectExtension) PluginServices.getExtension(ProjectExtension.class);
			if (pe.getProject().getDocumentsByType(ProjectViewFactory.registerName).size()>0) {
				cmbMode.addItem(PluginServices.getText(this, "layers_in_other_view"));
				USERS_VIEW_SELECTION = cmbMode.getItemCount() -1;
			}
		}

		return cmbMode;
	}

	public void setCurrentView(View v) {
		currentView = v.getName();
	}

	public ProjectView getSelectedView() {
		String viewName = null;
		if (getCmbMode().getSelectedIndex() == NEW_VIEW)
			return null;
		else if (getCmbMode().getSelectedIndex() == CURRENT_VIEW)
			viewName = currentView;
		else if (getCmbMode().getSelectedIndex() == USERS_VIEW_SELECTION)
			viewName = (String) getCmbViews().getSelectedItem();
		ProjectExtension pe = (ProjectExtension) PluginServices.getExtension(ProjectExtension.class);
		return (ProjectView) pe.getProject().getProjectDocumentByName(viewName, ProjectViewFactory.registerName);
	}

	public int getOption() {
		return cmbMode.getSelectedIndex();
	}

	/**
	 * This method initializes cmbViews
	 *
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getCmbViews() {
		if (cmbViews == null) {
			cmbViews = new JComboBox();
			cmbViews.setBounds(25, 114, 266, 20);
			ProjectExtension pe = (ProjectExtension) PluginServices.getExtension(ProjectExtension.class);
			ArrayList views = pe.getProject().getDocumentsByType(ProjectViewFactory.registerName);
			for (int i = 0; i < views.size(); i++) {
				ProjectView v = (ProjectView)views.get(i);
				cmbViews.addItem(v.getName());
			}
		}
		return cmbViews;
	}
}  //  @jve:decl-index=0:visual-constraint="10,0"
