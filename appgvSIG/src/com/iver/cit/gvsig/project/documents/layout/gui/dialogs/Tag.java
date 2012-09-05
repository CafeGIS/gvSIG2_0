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
package com.iver.cit.gvsig.project.documents.layout.gui.dialogs;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;


/**
 * Diálogo para insertar o modificar el Tag asociado a un FFrame.
 *
 * @author Vicente Caballero Navarro
 */
public class Tag extends JPanel implements IWindow {
	private JTextField jTextField = null;
	private JLabel jLabel = null;
	private JButton jButton = null;
	private JButton jButton1 = null;
	private IFFrame fframe = null;
    WindowInfo m_viewinfo = new WindowInfo(WindowInfo.PALETTE);

	/**
	 * This is the default constructor
	 *
	 * @param f FFrame al que asociar el Tag.
	 */
	public Tag(IFFrame f) {
		super();
		fframe = f;
		initialize();
	}

	/**
	 * This method initializes jTextField
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextField() {
		if (jTextField == null) {
			jTextField = new JTextField();

			if (fframe.getTag() != null) {
				jTextField.setText(fframe.getTag());
			}

			jTextField.setPreferredSize(new java.awt.Dimension(200, 20));
		}

		return jTextField;
	}

	/**
	 * This method initializes jButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setText("Aceptar");
			jButton.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						if (getJTextField().getText().compareTo("") == 0) {
							fframe.setTag(null);
						} else {
							fframe.setTag(getJTextField().getText());
						}

						PluginServices.getMDIManager().closeWindow(Tag.this);
					}
				});
		}

		return jButton;
	}

	/**
	 * This method initializes jButton1
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJButton1() {
		if (jButton1 == null) {
			jButton1 = new JButton();
			jButton1.setText("Cancelar");
			jButton1.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						PluginServices.getMDIManager().closeWindow(Tag.this);
					}
				});
		}

		return jButton1;
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		jLabel = new JLabel();
		this.setSize(284, 100);
		jLabel.setText("tag");
		this.add(jLabel, null);
		this.add(getJTextField(), null);
		this.add(getJButton(), null);
		this.add(getJButton1(), null);
	}

	/**
	 * @see com.iver.mdiApp.ui.MDIManager.IWindow#getWindowInfo()
	 */
	public WindowInfo getWindowInfo() {
		m_viewinfo.setTitle(PluginServices.getText(this, "tag"));

		return m_viewinfo;
	}

	/**
	 * @see com.iver.mdiApp.ui.MDIManager.IWindow#windowActivated()
	 */
	public void viewActivated() {
	}

	public Object getWindowProfile() {
		return WindowInfo.TOOL_PROFILE;
	}

}
