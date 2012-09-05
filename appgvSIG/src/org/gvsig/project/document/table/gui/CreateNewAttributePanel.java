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
package org.gvsig.project.document.table.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.ParseException;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.feature.EditableFeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.EditableFeatureType;
import org.gvsig.gui.beans.AcceptCancelPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;

/**
 * To create new FeatureAttributeDescriptor from the interface.
 *
 * @author Vicente Caballero Navarro
 *
 */
public class CreateNewAttributePanel extends JPanel implements IWindow {

	private static final String DEFAULT_FIELD_LENGTH = "50";
	private JLabel jLblFieldName = null;
	private JTextField jTxtFieldName = null;
	private JLabel jLblFieldType = null;
	private JComboBox jCboFieldType = null;
	private JLabel jLblFieldLength = null;
	private JTextField jTxtFieldLength = null;
	private JLabel jLblFieldPrecision = null;
	private JTextField jTxtFieldPrecision = null;
	private JLabel jLblDefaultValue = null;
	private JTextField jTxtDefaultValue = null;
	private WindowInfo viewInfo;
	private JPanel jPanel = null;
	private AcceptCancelPanel jPanelOkCancel = null;
	private JPanel jPnlFields = null;
	private KeyListener checkInt = new KeyListener() {
		public void keyPressed(KeyEvent e) {
		}

		public void keyReleased(KeyEvent e) {
			JTextField component = (JTextField) e.getComponent();

			try {
				component.setText(String.valueOf(Integer.parseInt(component
						.getText())));

			} catch (Exception ex) {
				String text = component.getText();
				text = (text.length() <= 1) ? "0" : text.substring(0, text
						.length() - 1);
				component.setText(text);
			}
		}

		public void keyTyped(KeyEvent e) {
		}
	};
	private String[] currentFieldNames;

	public CreateNewAttributePanel() {
		super();
		initialize();
	}

	public CreateNewAttributePanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		initialize();
	}

	public CreateNewAttributePanel(LayoutManager layout) {
		super(layout);
		initialize();
	}

	public CreateNewAttributePanel(LayoutManager layout,
			boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		initialize();
	}

	public WindowInfo getWindowInfo() {
		if (viewInfo == null) {
			viewInfo = new WindowInfo(WindowInfo.MODALDIALOG);
			viewInfo.setWidth(this.getWidth() + 8);
			viewInfo.setHeight(this.getHeight());
			viewInfo.setTitle(PluginServices.getText(this,
					"new_field_properties"));
		}
		return viewInfo;
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setSize(300, 210);
		this.setPreferredSize(new java.awt.Dimension(300, 210));
		this.add(getJPanel(), java.awt.BorderLayout.CENTER);
		this.add(getJPanelOkCancel(), java.awt.BorderLayout.SOUTH);
	}

	/**
	 * This method initializes jTxtFieldName
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTxtFieldName() {
		if (jTxtFieldName == null) {
			jTxtFieldName = new JTextField();
			jTxtFieldName.setBounds(new java.awt.Rectangle(147, 15, 138, 22));
		}
		return jTxtFieldName;
	}

	/**
	 * This method initializes jCboFieldType
	 *
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getJCboFieldType() {
		if (jCboFieldType == null) {
			jCboFieldType = new JComboBox();
			jCboFieldType.setBounds(new java.awt.Rectangle(147, 52, 138, 22));
			jCboFieldType.addItem(DataTypes.TYPE_NAMES[DataTypes.BOOLEAN]);
			jCboFieldType.addItem(DataTypes.TYPE_NAMES[DataTypes.DATE]);
			jCboFieldType.addItem(DataTypes.TYPE_NAMES[DataTypes.INT]);
			jCboFieldType.addItem(DataTypes.TYPE_NAMES[DataTypes.DOUBLE]);
			jCboFieldType.addItem(DataTypes.TYPE_NAMES[DataTypes.STRING]);

			jCboFieldType.setSelectedIndex(4);
			jCboFieldType
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							String strType = (String) getJCboFieldType()
									.getModel().getSelectedItem();
							if (strType == DataTypes.TYPE_NAMES[DataTypes.STRING]) {
								getJTxtFieldPrecision().setEnabled(true);
								if (getJTxtFieldPrecision().getText()
										.equals("")) {
									getJTxtFieldPrecision().setText("3");
								} else {
									try {
										Integer
												.parseInt(getJTxtFieldPrecision()
														.getText());
									} catch (NumberFormatException e1) {
										getJTxtFieldPrecision().setText("3");
									}
								}
							} else {
								getJTxtFieldPrecision().setEnabled(false);
							}
							if (strType == DataTypes.TYPE_NAMES[DataTypes.BOOLEAN]) {
								getJTxtFieldLength().setText("0");
								getJTxtFieldLength().setEnabled(false);
							} else {
								getJTxtFieldLength().setEnabled(true);
							}

						}
					});

		}
		return jCboFieldType;
	}

	/**
	 * This method initializes jTxtFieldLength
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTxtFieldLength() {
		if (jTxtFieldLength == null) {
			jTxtFieldLength = new JTextField();
			jTxtFieldLength.setBounds(new java.awt.Rectangle(147, 89, 138, 22));
			jTxtFieldLength.setText(DEFAULT_FIELD_LENGTH);
			jTxtFieldLength.addKeyListener(checkInt);
		}
		return jTxtFieldLength;
	}

	/**
	 * This method initializes jTxtFieldPrecision
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTxtFieldPrecision() {
		if (jTxtFieldPrecision == null) {
			jTxtFieldPrecision = new JTextField();
			jTxtFieldPrecision.setBounds(new java.awt.Rectangle(147, 126, 138,
					22));
			jTxtFieldPrecision.setEnabled(false);
			jTxtFieldPrecision.addKeyListener(checkInt);
		}
		return jTxtFieldPrecision;
	}

	/**
	 * This method initializes jTxtDefaultValue
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTxtDefaultValue() {
		if (jTxtDefaultValue == null) {
			jTxtDefaultValue = new JTextField();
			jTxtDefaultValue
					.setBounds(new java.awt.Rectangle(147, 163, 138, 22));
		}
		return jTxtDefaultValue;
	}

	public EditableFeatureAttributeDescriptor loadFieldDescription(
			EditableFeatureType featureType) throws ParseException {
		String nameAttr = "";
		int typeAttr = DataTypes.STRING;
		int sizeAttr = 0;
		int precisionAttr = 0;
		Object defaultValueAttr = "";

		nameAttr = getJTxtFieldName().getText();
		String strType = (String) getJCboFieldType().getModel()
				.getSelectedItem();
		for (int type = 0; type < DataTypes.TYPE_NAMES.length; type++) {
			if (DataTypes.TYPE_NAMES[type] == strType) {
				typeAttr = type;
				break;
			}
		}
		try {
			int fieldLength = Integer.parseInt(getJTxtFieldLength().getText());
			sizeAttr = fieldLength;
		} catch (Exception e) {
			throw new ParseException(e.getMessage(), 0);
		}

		if (strType == DataTypes.TYPE_NAMES[DataTypes.DOUBLE]) {
			try {
				precisionAttr = Integer.parseInt(getJTxtFieldPrecision()
						.getText());
			} catch (NumberFormatException e) {
				precisionAttr = 3;
			}
		}
		defaultValueAttr = getJTxtDefaultValue().getText();
		if (defaultValueAttr.equals("")){
			defaultValueAttr=null;
		}
		if (featureType.getIndex(nameAttr) != -1) {
			NotificationManager.showMessageInfo(PluginServices.getText(this,
					"field_already_exists"), null);
			return null;
		}
		EditableFeatureAttributeDescriptor ead = featureType.add(nameAttr,
				typeAttr, sizeAttr);
		ead.setPrecision(precisionAttr);
		ead.setDefaultValue(defaultValueAttr);
		return ead;
	}

	public void setOkAction(ActionListener okAction) {
		getJPanelOkCancel().setOkButtonActionListener(okAction);

	}

	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(null);

			jPanel.add(getJPnlFields(), null);
		}
		return jPanel;
	}

	/**
	 * This method initializes jPanelOkCancel
	 *
	 * @return javax.swing.JPanel
	 */
	private AcceptCancelPanel getJPanelOkCancel() {
		if (jPanelOkCancel == null) {
			jPanelOkCancel = new AcceptCancelPanel();
			jPanelOkCancel.setCancelButtonActionListener(new ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					PluginServices.getMDIManager().closeWindow(
							CreateNewAttributePanel.this);
				};
			});
			jPanelOkCancel.setPreferredSize(new java.awt.Dimension(10, 50));
		}
		return jPanelOkCancel;
	}

	/**
	 * This method initializes jPnlFields
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPnlFields() {
		if (jPnlFields == null) {
			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(6);
			gridLayout.setVgap(3);
			gridLayout.setHgap(5);
			gridLayout.setColumns(2);
			jPnlFields = new JPanel();
			jPnlFields.setLayout(gridLayout);
			jPnlFields.setBounds(new java.awt.Rectangle(5, 12, 290, 142));
			jLblDefaultValue = new JLabel();
			jLblDefaultValue
					.setBounds(new java.awt.Rectangle(14, 163, 125, 22));
			jLblDefaultValue.setText(PluginServices.getText(this,
					"default_value"));
			jLblFieldPrecision = new JLabel();
			jLblFieldPrecision.setBounds(new java.awt.Rectangle(14, 126, 112,
					22));
			jLblFieldPrecision.setText(PluginServices
					.getText(this, "precision"));
			jLblFieldLength = new JLabel();
			jLblFieldLength.setBounds(new java.awt.Rectangle(14, 89, 99, 22));
			jLblFieldLength.setText(PluginServices
					.getText(this, "field_length"));
			jLblFieldType = new JLabel();
			jLblFieldType.setBounds(new java.awt.Rectangle(14, 52, 94, 22));
			jLblFieldType.setText(PluginServices.getText(this, "field_type"));
			jLblFieldName = new JLabel();
			jLblFieldName.setText(PluginServices.getText(this, "field_name"));
			jLblFieldName.setBounds(new java.awt.Rectangle(14, 15, 99, 22));
			jPnlFields.add(jLblFieldName, null);
			jPnlFields.add(getJTxtFieldName(), null);
			jPnlFields.add(jLblFieldType, null);
			jPnlFields.add(getJCboFieldType(), null);
			jPnlFields.add(jLblFieldLength, null);
			jPnlFields.add(getJTxtFieldLength(), null);
			jPnlFields.add(jLblFieldPrecision, null);
			jPnlFields.add(getJTxtFieldPrecision(), null);
			jPnlFields.add(jLblDefaultValue, null);
			jPnlFields.add(getJTxtDefaultValue(), null);
		}
		return jPnlFields;
	}

	public void setCurrentFieldNames(String[] fieldNames) {
		currentFieldNames = fieldNames;
		String newField = PluginServices.getText(this, "field").replaceAll(
				" +", "_");
		int index = 0;
		for (int i = 0; i < currentFieldNames.length; i++) {
			if (currentFieldNames[i].startsWith(newField)) {
				try {
					index = Integer.parseInt(currentFieldNames[i].replaceAll(
							newField, ""));
				} catch (Exception e) { /* we don't care */
				}
			}
		}
		jTxtFieldName.setText(newField + (++index));
	}

	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}

}
