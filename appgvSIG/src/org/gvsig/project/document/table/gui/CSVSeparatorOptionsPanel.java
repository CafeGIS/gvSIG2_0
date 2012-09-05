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
package org.gvsig.project.document.table.gui;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;

/**
 * Class to create csv file at disk with the statistics group generated from a table with an Specifically separator
 *
 *  Basic separator "," -> csv basic file
 *  Excel separator ";" -> csv file to work in gvSIG documents
 *
 *  Optionally separator -> the user can enter a character or a string
 *
 *
 * @author Ángel Fraile Griñán  e-mail: angel.fraile@iver.es
 *
 */


public class CSVSeparatorOptionsPanel extends JPanel implements IWindow,ItemListener,ActionListener{

	private static final long serialVersionUID = 1L;

	public static final String commaChar = ",";
	public static final String semicolonChar = ";";
	private JButton buttonAccept;
	private JButton buttonCancel;
	private JRadioButton commaOption;
	private JRadioButton semicolonOption;
	private JRadioButton otherSymbol;
	private JTextField symbol;
	private String separator = null;


	public CSVSeparatorOptionsPanel() {

		setName("Separation options");
		// Initialize component
		inicialize();

	}
	/**
	 * Creating  a basic option separator panel
	 *
	 */
	private void inicialize() {

		GridBagConstraints gridBagConstraints = new GridBagConstraints();

		setLayout(new java.awt.GridBagLayout());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 5, 5);
        add(getJCheckBoxPointComma(), gridBagConstraints);


        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 5, 10);
        add(getJCheckBoxComma(), gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 5);
        add(getJCheckBoxOtherSymbol(), gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 10);
        add(getTextFieldSymbol(), gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(getAcceptButton(), gridBagConstraints);

	    gridBagConstraints = new java.awt.GridBagConstraints();
	    gridBagConstraints.gridx = 2;
	    gridBagConstraints.gridy = 2;
	    add(getCancelButton(), gridBagConstraints);

	    ButtonGroup group = new ButtonGroup();
	    group.add(commaOption);
	    group.add(semicolonOption);
	    group.add(otherSymbol);

	}


	/**
	 * Selection option comma as the separator
	 *
	 *
	 * @return JRadioButton
	 */

	private JRadioButton getJCheckBoxComma() {
		if (commaOption == null) {
			commaOption = new JRadioButton(PluginServices.getText(this, "comma"),
					false);
			commaOption.setEnabled(true);
			commaOption.setToolTipText(PluginServices.getText(this, "sepatator_info_coma"));
		}
		return commaOption;
	}

	/**
	 * Selection option semicolon as the separator
	 *
	 * @return JRadioButton
	 */
	private JRadioButton getJCheckBoxPointComma() {
		if (semicolonOption == null) {
			semicolonOption = new JRadioButton(PluginServices.getText(this, "semicolon"),
					true);
			semicolonOption.setEnabled(true);
			semicolonOption.setToolTipText(PluginServices.getText(this, "sepatator_info_semicolon"));
		}
		return semicolonOption;
	}

	/**
	 * The user can enter the separator
	 *
	 * @return JRadioButton
	 */
	private JRadioButton getJCheckBoxOtherSymbol() {
		if (otherSymbol == null) {
			otherSymbol = new JRadioButton(PluginServices.getText(this, "otro_simbolo"),
					false);
			otherSymbol.setEnabled(true);
			otherSymbol.addItemListener(this);
			otherSymbol.setToolTipText(PluginServices.getText(this, "sepatator_info_other"));
		}
		return otherSymbol;
	}


	/**
	 * Return textfield where the user can enter a separator (char or string)
	 *
	 * @return TextField.
	 */
	private JTextField getTextFieldSymbol() {
		if (symbol == null) {
			symbol = new JTextField(null, 4);
			symbol.setHorizontalAlignment(JTextField.RIGHT);
			symbol.setEnabled(otherSymbol.isSelected());
			symbol.setToolTipText(PluginServices.getText(this, "sepatator_info"));
		}
		return symbol;
	}

	/**
	 * Method that generates the button to accept selected separator
 	 *
	 * @return JButton
	 */
	private JButton getAcceptButton() {
		if (buttonAccept == null) {
			buttonAccept = new JButton();
			buttonAccept.setText(PluginServices.getText(this, "Aceptar"));
			buttonAccept.addActionListener(this);
			buttonAccept.setToolTipText(PluginServices.getText(this, "Aceptar"));
		}
		return buttonAccept;
	}

	/**
	 * Method that generates the button to cancel csv file creation
	 *
	 * @return JButton
	 */
	private JButton getCancelButton() {
		if (buttonCancel == null) {
			buttonCancel = new JButton();
			buttonCancel.setText(PluginServices.getText(this, "Cancelar"));
			buttonCancel.addActionListener(this);
			buttonCancel.setToolTipText(PluginServices.getText(this, "Cancelar"));
		}
		return buttonCancel;
	}

	/**
	 * Window properties
	 *
	 * @see com.iver.andami.ui.mdiManager.IWindow#getWindowInfo()
	 */
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODALDIALOG | WindowInfo.RESIZABLE);
		m_viewinfo.setTitle(PluginServices.getText(this, "opciones_separacion"));
		m_viewinfo.setHeight(100);
		m_viewinfo.setWidth(300);
		return m_viewinfo;
	}

	/**
	 * Getting the window profile
	 */
//	public Object getWindowProfile() {
//		return WindowInfo.DIALOG_PROFILE;
//	}

	/**
	 * JRadioButton to enter the separator listener
	 */
	public void itemStateChanged(ItemEvent e) {
		symbol.setEnabled(otherSymbol.isSelected());
	}

	/**
	 * JButtons Accept and Cancel listener
	 */
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == this.buttonAccept) {
			if(commaOption.isSelected()) {
				separator = commaChar;

			}else if(semicolonOption.isSelected()) {
				separator = semicolonChar;
			}else if(otherSymbol.isSelected()) {
				separator = symbol.getText();

			}
			PluginServices.getMDIManager().closeWindow(this);

		}
		if (event.getSource() == this.buttonCancel) {
			separator = null;
			PluginServices.getMDIManager().closeWindow(this);
			return;
		}

	}

	/**
	 *
	 * @return separator
	 * 					- Chosen as the separator character or separator string
	 */
	public String getSeparator() {
		return separator;
	}

	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}

}
