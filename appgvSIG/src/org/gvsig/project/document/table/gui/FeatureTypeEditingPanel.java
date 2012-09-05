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
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.EditableFeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.EditableFeatureType;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.gui.beans.swing.JButton;
import org.gvsig.project.document.table.FeatureTableOperations;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;

/**
 * To modify FeatureTypes from the interface.
 *
 * @author Vicente Caballero Navarro
 *
 */
public class FeatureTypeEditingPanel extends JPanel implements IWindow {
	private static final long serialVersionUID = -4284879326692474318L;

	WindowInfo windowInfo = null;

	private JLabel jLabel = null;

	private JScrollPane jScrollPane = null;

	private JTable jTableFields = null;

	private JButton jBtnNewField = null;

	private JButton jBtnDeleteField = null;

	private JButton jBtnRenameField = null;

	private JButton jBtnOK = null;

	private JButton jBtnCancel = null;

	private CreateNewAttributePanel panelNewField = new CreateNewAttributePanel();

	private FeatureStore featureStore = null;

	private JPanel jPanelButtons = null;

	private EditableFeatureType editableType = null;

	private class MyTableModel extends AbstractTableModel {

		public MyTableModel(FeatureStore fs) {
			try {
				editableType = fs.getDefaultFeatureType()
						.getEditable();
			} catch (DataException e) {
				NotificationManager.addError(PluginServices.getText(this,
						"create_editabletype"), e);
			}

		}

		public int getColumnCount() {
			return 5;
		}

		public int getRowCount() {
			return editableType.size();
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			FeatureAttributeDescriptor myField = null;
			myField = (FeatureAttributeDescriptor) editableType.get(rowIndex);

			switch (columnIndex) {
			case 0:
				return myField.getName();
			case 1:
				String strType = null;
				int type = myField.getDataType();
				if (type == DataTypes.STRING) {
					strType = "String";
				} else if (type == DataTypes.DOUBLE) {
					strType = "Double";
				} else if (type == DataTypes.INT) {
					strType = "Integer";
				} else if (type == DataTypes.BOOLEAN) {
					strType = "Boolean";
				} else if (type == DataTypes.DATE) {
					strType = "Date";
				} else if (type == DataTypes.GEOMETRY) {
					strType = "Geometry";
				}
				return strType;
			case 2:
				return new Integer(myField.getSize());
			case 3:
				return new Integer(myField.getPrecision());
			case 4:
				return myField.getDefaultValue();

			}
			return null;
		}

		public Class getColumnClass(int columnIndex) {
			return super.getColumnClass(columnIndex);
		}

		public String getColumnName(int column) {
			switch (column) {
			case 0:
				return PluginServices.getText(this, "field_name");
			case 1:
				return PluginServices.getText(this, "field_type");
			case 2:
				return PluginServices.getText(this, "field_length");
			case 3:
				return PluginServices.getText(this, "field_decimal_count");
			case 4:
				return PluginServices.getText(this, "field_default_value");

			}
			return super.getColumnName(column);
		}

		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;

		}

		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			if (columnIndex == 0) {
				editableType.remove(rowIndex);
			}
			String name = "";
			int type = DataTypes.STRING;
			int size = 0;
			int precision = 0;
			switch (columnIndex) {
			case 0:
				name = (String) aValue;
				break;
			case 1:
				String strType = (String) aValue;
				if (strType.equals("String")) {
					type = DataTypes.STRING;
				}
				if (strType.equals("Double")) {
					type = DataTypes.DOUBLE;
					precision = 5;
				}
				if (strType.equals("Integer")) {
					type = DataTypes.INT;
				}
				if (strType.equals("Boolean")) {
					type = DataTypes.BOOLEAN;
				}
				if (strType.equals("Date")) {
					type = DataTypes.DATE;
				}
				break;
			case 2:
				size = ((Integer) aValue).intValue();

				// TODO: HACERLO BIEN
				// if (ead.getDataType()==DataTypes.STRING) {
				// ead.setPrecision(5);
				// }
			}
			EditableFeatureAttributeDescriptor ead = editableType.add(name,
					type, size);
			ead.setPrecision(precision);
		}

	}

	/**
	 * This method initializes
	 *
	 */
	public FeatureTypeEditingPanel(FeatureStore fs) {
		super();
		this.featureStore = fs;
		initialize();
		// Add a new row
		TableModel tm;
		tm = new MyTableModel(fs);
		getJTableFields().setModel(tm);
		// Esto lo añado aquí porque si no tiene registros, no hace caso.
		// (Por eso no
		// lo pongo en getJTable()
		TableColumn typeColumn = jTableFields.getColumnModel().getColumn(1);
		JComboBox comboBox = new JComboBox();
		comboBox.addItem("Boolean");
		comboBox.addItem("Date");
		comboBox.addItem("Integer");
		comboBox.addItem("Double");
		comboBox.addItem("String");
		typeColumn.setCellEditor(new DefaultCellEditor(comboBox));

	}

	/**
	 * This method initializes this
	 *
	 */
	private void initialize() {
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setVgap(5);
		flowLayout.setHgap(0);
		BorderLayout borderLayout = new BorderLayout();
		borderLayout.setHgap(15);
		borderLayout.setVgap(15);
		jLabel = new JLabel();
		jLabel.setText(PluginServices.getText(this, "add_delete_edit_fields")
				+ ": ");
		this.setLayout(borderLayout);
		this.setSize(new java.awt.Dimension(663, 404));
		this.setPreferredSize(new java.awt.Dimension(getWidth(), getHeight()));
		JPanel jPanelNorth = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jPanelNorth.add(jLabel);
		this.add(jPanelNorth, BorderLayout.NORTH);
		JPanel jPanelCenter = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		jPanelCenter.add(getJScrollPane());
		this.add(jPanelCenter, BorderLayout.CENTER);

		this.add(getJPanelButtons(), BorderLayout.EAST);
		JPanel jPanelSouth = new JPanel();
		jPanelSouth.setLayout(flowLayout);
		jPanelSouth.add(getJBtnOK(), null);
		jPanelSouth.add(getJBtnCancel(), null);
		this.add(jPanelSouth, BorderLayout.SOUTH);

	}

	public WindowInfo getWindowInfo() {
		if (windowInfo == null) {
			windowInfo = new WindowInfo(WindowInfo.MODALDIALOG
					| WindowInfo.PALETTE | WindowInfo.RESIZABLE);
			windowInfo.setTitle(PluginServices.getText(this, "field_manager"));
			windowInfo.setHeight(this.getHeight());
			windowInfo.setWidth(this.getWidth());
		}
		return windowInfo;
	}

	// /**
	// * Convierte lo que hay en la tabla en una definición de campos adecuada
	// * para crear un LayerDefinition
	// *
	// * @return
	// */
	// public FieldDescription[] getFieldsDescription() {
	// DefaultTableModel tm = (DefaultTableModel) jTableFields.getModel();
	// FieldDescription[] fieldsDesc = new FieldDescription[tm.getRowCount()];
	//
	// for (int i = 0; i < tm.getRowCount(); i++) {
	// fieldsDesc[i] = new FieldDescription();
	// fieldsDesc[i].setFieldName((String) tm.getValueAt(i, 0));
	// String strType = (String) tm.getValueAt(i, 1);
	// if (strType.equals("String"))
	// fieldsDesc[i].setFieldType(Types.VARCHAR);
	// if (strType.equals("Double"))
	// fieldsDesc[i].setFieldType(Types.DOUBLE);
	// if (strType.equals("Integer"))
	// fieldsDesc[i].setFieldType(Types.INTEGER);
	// if (strType.equals("Boolean"))
	// fieldsDesc[i].setFieldType(Types.BOOLEAN);
	// if (strType.equals("Date"))
	// fieldsDesc[i].setFieldType(Types.DATE);
	// int fieldLength = Integer.parseInt((String) tm.getValueAt(i, 2));
	// fieldsDesc[i].setFieldLength(fieldLength);
	//
	// // TODO: HACERLO BIEN
	// if (strType.equals("Double"))
	// fieldsDesc[i].setFieldDecimalCount(5);
	//
	// }
	//
	// return fieldsDesc;
	// }

	/**
	 * This method initializes jScrollPane
	 *
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setPreferredSize(new java.awt.Dimension(482, 350));
			jScrollPane.setViewportView(getJTableFields());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jTableFields
	 *
	 * @return javax.swing.JTable
	 */
	private JTable getJTableFields() {
		if (jTableFields == null) {
			jTableFields = new JTable();
			jTableFields
					.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

			jTableFields.setColumnSelectionAllowed(false);
			// Ask to be notified of selection changes.
			ListSelectionModel rowSM = jTableFields.getSelectionModel();
			rowSM.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					// Ignore extra messages.
					if (e.getValueIsAdjusting()) {
						return;
					}

					ListSelectionModel lsm = (ListSelectionModel) e.getSource();
					if (lsm.isSelectionEmpty()) {
						// no rows are selected
						jBtnDeleteField.setEnabled(false);
					} else {
						jBtnDeleteField.setEnabled(true);
					}
					if (jTableFields.getSelectedRows().length != 1) {
						getJBtnRenameField().setEnabled(false);
					} else {
						getJBtnRenameField().setEnabled(true);
					}

				}
			});

		}
		return jTableFields;
	}

	/**
	 * This method initializes jBtnNewField
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJBtnNewField() {
		if (jBtnNewField == null) {
			jBtnNewField = new JButton();
			jBtnNewField.setText(PluginServices.getText(this, "new_field"));
			jBtnNewField.addActionListener(new java.awt.event.ActionListener() {
				private ArrayList tempFieldNames = new ArrayList();

				{
					try {
						int size = editableType.size();
						for (int i = 0; i < size; i++) {
							FeatureAttributeDescriptor ad = (FeatureAttributeDescriptor) editableType
									.get(i);
							tempFieldNames.add(ad.getName());
						}
					} catch (Exception ex) {
					}
				}

				public void actionPerformed(java.awt.event.ActionEvent e) {
					ActionListener okAction;
					okAction = new java.awt.event.ActionListener() {

						public void actionPerformed(java.awt.event.ActionEvent e) {
							try {
								EditableFeatureAttributeDescriptor ead = panelNewField
										.loadFieldDescription(editableType);
								if (ead == null) {
									return;
								}
								if (ead.getDataType() == DataTypes.STRING
										&& ead.getSize() > FeatureTableOperations.MAX_FIELD_LENGTH) {
									NotificationManager.showMessageInfo( PluginServices.getText(this,
													"max_length_is")
													+ ":" + FeatureTableOperations.MAX_FIELD_LENGTH, null);
									ead.setSize(FeatureTableOperations.MAX_FIELD_LENGTH);
								}
								tempFieldNames.add(ead.getName());
								jTableFields.revalidate();
								PluginServices.getMDIManager().closeWindow(
										panelNewField);
							} catch (ParseException e2) {
								NotificationManager.addError(e2);
							}

						}
					};
					panelNewField.setOkAction(okAction);
					String[] names = (String[]) tempFieldNames
							.toArray(new String[0]);
					panelNewField.setCurrentFieldNames(names);
					panelNewField = (CreateNewAttributePanel) PluginServices
							.getMDIManager().addWindow(panelNewField);
				}
			});
		}
		return jBtnNewField;
	}

	/**
	 * This method initializes jButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJBtnDelete() {
		if (jBtnDeleteField == null) {
			jBtnDeleteField = new JButton();
			jBtnDeleteField.setText(PluginServices
					.getText(this, "delete_field"));
			jBtnDeleteField
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							int[] selecteds = jTableFields.getSelectedRows();
							TableModel tm = jTableFields.getModel();

							for (int i = selecteds.length - 1; i >= 0; i--) {
								String fieldName = (String) tm.getValueAt(
										selecteds[i], 0);
								editableType.remove(fieldName);
							}
							jTableFields.revalidate();
						}
					});
		}
		return jBtnDeleteField;
	}

	/**
	 * This method initializes jBtnRenameField
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJBtnRenameField() {
		if (jBtnRenameField == null) {
			jBtnRenameField = new JButton();
			jBtnRenameField.setText(PluginServices
					.getText(this, "rename_field"));
			jBtnRenameField
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							int[] selecteds = jTableFields.getSelectedRows();
							TableModel tm = jTableFields.getModel();

							for (int i = selecteds.length - 1; i >= 0; i--) {
								String fieldName = (String) tm.getValueAt(
										selecteds[i], 0);
								String newName = JOptionPane
										.showInputDialog(
												(Component) PluginServices
														.getMDIManager()
														.getActiveWindow(),
												PluginServices
														.getText(this,
																"please_insert_new_field_name"),
												fieldName);
								if (newName == null) {
									return;
								}
								if (editableType.getIndex(newName) != -1) {
									NotificationManager.showMessageInfo(
											PluginServices.getText(this,
													"field_already_exists"),
											null);
									return;
								}
								FeatureAttributeDescriptor ad = (FeatureAttributeDescriptor) editableType
										.get(fieldName);
								editableType.remove(ad.getName());
								EditableFeatureAttributeDescriptor ead = editableType
										.add(newName, ad.getDataType(), ad
												.getSize());
								ead.setPrecision(ad.getPrecision());
							}
							jTableFields.repaint();
						}
					});
		}
		return jBtnRenameField;
	}

	/**
	 * This method initializes jBtnOK
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJBtnOK() {
		if (jBtnOK == null) {
			jBtnOK = new JButton();
			jBtnOK.setText(PluginServices.getText(this, "aceptar"));
			jBtnOK.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						featureStore.update(editableType);
					} catch (DataException e1) {
						NotificationManager.showMessageError(PluginServices
								.getText(this, "update_featuretype_error"), e1);
					}
					PluginServices.getMDIManager().closeWindow(
							FeatureTypeEditingPanel.this);
				}
			});
		}
		return jBtnOK;
	}

	/**
	 * This method initializes jButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJBtnCancel() {
		if (jBtnCancel == null) {
			jBtnCancel = new JButton();
			jBtnCancel.setText(PluginServices.getText(this, "cancelar"));
			jBtnCancel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					PluginServices.getMDIManager().closeWindow(
							FeatureTypeEditingPanel.this);
				}
			});
			jBtnCancel.setVisible(false);
		}
		return jBtnCancel;
	}

	/**
	 * This method initializes jPanelButtons
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			jPanelButtons = new JPanel();
			JPanel aux = new JPanel(new GridLayout(3, 1));
			aux.add(getJBtnNewField());
			aux.add(getJBtnRenameField());
			aux.add(getJBtnDelete());
			jPanelButtons.add(aux, BorderLayout.NORTH);
		}
		return jPanelButtons;
	}

	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}

}
