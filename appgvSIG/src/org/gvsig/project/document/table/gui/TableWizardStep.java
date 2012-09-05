/**
 *
 */
package org.gvsig.project.document.table.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JTextField;

import jwizardcomponent.JWizardComponents;
import jwizardcomponent.JWizardPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.utiles.swing.JComboBox;
import com.iver.utiles.swing.objectSelection.ObjectSelectionModel;

public class TableWizardStep extends JWizardPanel {
	private JLabel lbl_header = null;
	private static final long serialVersionUID = 1L;
	private JLabel tableNameLbl = null;
	private JComboBox tableNameCmb = null;
	private JLabel fieldNameLbl = null;
	private JComboBox fieldNameCmb = null;
	private JLabel fieldPrefixLbl = null;
	private JTextField fieldPrefixTxt = null;


	public TableWizardStep(JWizardComponents wizardComponents, String title) {
		super(wizardComponents, title);
		initialize();
	}

	public void setTableModel(ObjectSelectionModel model) {
		getTableNameCmb().removeAllItems();
		Object[] tableNames;
		try {
			tableNames = model.getObjects();
			for (int i=0; i<tableNames.length; i++) {
				getTableNameCmb().addItem(tableNames[i]);
			}
		} catch (Exception e) {
			NotificationManager.addError(PluginServices.getText(this, "Error_getting_table_fields"), e);
		}
	}

	public void setFieldModel(ObjectSelectionModel model) {
		getFieldNameCmb().removeAllItems();
		Object[] fieldNames;
		try {
			fieldNames = model.getObjects();
			for (int i=0; i<fieldNames.length; i++) {
				getFieldNameCmb().addItem(fieldNames[i]);
			}
		} catch (Exception e) {
			NotificationManager.addError(PluginServices.getText(this, "Error_getting_table_fields"), e);
		}

	}

	private void initialize() {
		this.setLayout(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		constraints.insets = new Insets(4,10,8,4);
		this.add(getHeaderLbl(), constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.insets = new Insets(4,10,4,6);
		this.add(getTableNameLbl(), constraints);

		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 0.5;
		constraints.weighty = 0.0;
		this.add(getTableNameCmb(), constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.insets = new Insets(4,10,4,6);
		this.add(getFieldNameLbl(), constraints);

		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 0.5;
		constraints.weighty = 0.0;
		this.add(getFieldNameCmb(), constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.insets = new Insets(4,10,4,6);
		this.add(getFieldPrefixLbl(), constraints);

		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 0.5;
		constraints.weighty = 0.0;
		this.add(getFieldPrefixTxt(), constraints);
	}

	public JLabel getHeaderLbl() {
		if (lbl_header==null) {
			lbl_header = new JLabel();
			Font font = lbl_header.getFont();
			lbl_header.setFont(font.deriveFont(Font.BOLD));
		}
		return lbl_header;
	}

	public JLabel getTableNameLbl() {
		if (tableNameLbl == null) {
			tableNameLbl = new JLabel();
		}
		return tableNameLbl;
	}

	public JComboBox getTableNameCmb() {
		if (tableNameCmb == null) {
			tableNameCmb = new JComboBox();
		}
		return tableNameCmb;
	}

	public JLabel getFieldNameLbl() {
		if (fieldNameLbl==null) {
			fieldNameLbl = new JLabel();
		}
		return fieldNameLbl;
	}

	public JComboBox getFieldNameCmb() {
		if (fieldNameCmb==null) {
			fieldNameCmb = new JComboBox();
		}
		return fieldNameCmb;
	}

	public JLabel getFieldPrefixLbl() {
		if (fieldPrefixLbl==null) {
			fieldPrefixLbl = new JLabel();
		}
		return fieldPrefixLbl;
	}

	public JTextField getFieldPrefixTxt() {
		if (fieldPrefixTxt==null) {
			fieldPrefixTxt = new JTextField();
		}
		return fieldPrefixTxt;
	}
}