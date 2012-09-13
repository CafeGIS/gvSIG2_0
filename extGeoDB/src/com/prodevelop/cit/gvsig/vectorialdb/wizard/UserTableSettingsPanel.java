/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 Prodevelop and Generalitat Valenciana.
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
 *   Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *   +34 963862235
 *   gvsig@gva.es
 *   www.gvsig.gva.es
 *
 *    or
 *
 *   Prodevelop Integración de Tecnologías SL
 *   Conde Salvatierra de Álava , 34-10
 *   46004 Valencia
 *   Spain
 *
 *   +34 963 510 612
 *   +34 963 510 968
 *   gis@prodevelop.es
 *   http://www.prodevelop.es
 */
package com.prodevelop.cit.gvsig.vectorialdb.wizard;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.gvsig.fmap.dal.serverexplorer.filesystem.swing.DynObjectEditor;
import org.gvsig.fmap.dal.store.db.DBStoreParameters;
import org.gvsig.gui.beans.swing.JButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;


/**
 * Utility class that holds a single table settings controls.
 *
 * @author jldominguez
 *
 */
public class UserTableSettingsPanel extends JPanel implements ActionListener,
    KeyListener {
    private static Logger logger = LoggerFactory
			.getLogger(UserTableSettingsPanel.class.getName());
    private FieldComboItem[] ids;
    private String initLayerName = "";
    private JComboBox idComboBox = null;
    private JTextArea sqlTextArea = null;
    private JLabel idLabel = null;
    private JLabel sqlLabel = null;
    private JCheckBox activateSQLCheckBox = null;
    protected JLabel tableNamejLabel = null;
    private JTextField layerNameTextField = null;
    private JScrollPane sqlTextAreaScroll = null;
    protected WizardDB parent = null;
	private JButton advanzedButton;
	protected DBStoreParameters parameters;

    protected UserTableSettingsPanel() {
		super();
	}

	protected void setInitValues(FieldComboItem[] idComboItems,
			String initialLayerName, boolean empty, WizardDB _p,DBStoreParameters parameters) {
        parent = _p;

        initLayerName = initialLayerName;
		ids = idComboItems;
		this.parameters = parameters;

	}

    public UserTableSettingsPanel(FieldComboItem[] idComboItems,
        String initialLayerName, boolean empty, WizardDB _p,
			DBStoreParameters parameters) {
		setInitValues(idComboItems, initialLayerName, empty, _p, parameters);
        initialize(empty);
    }

    public boolean hasValidValues() {
        if (!combosHaveItems()) {
            return false;
        }

        if ((activateSQLCheckBox.isSelected()) &&
                (getSqlTextArea().getText().trim().length() == 0)) {
            return false;
        }

        if (getLayerNameTextField().getText().trim().length() == 0) {
            return false;
        }

        return true;
    }

    protected void initialize(boolean _empty) {
        tableNamejLabel = new JLabel();
        tableNamejLabel.setText(PluginServices.getText(this, "name"));
        tableNamejLabel.setSize(new java.awt.Dimension(86, 21));
        tableNamejLabel.setLocation(new java.awt.Point(5, 25));
        sqlLabel = new JLabel();
        // sqlLabel.setBounds(new java.awt.Rectangle(30, 90, 116, 21));
		sqlLabel.setBounds(new java.awt.Rectangle(30, 55, 116, 21));
        sqlLabel.setText(PluginServices.getText(this, "sql_restriction"));
        idLabel = new JLabel();
        // idLabel.setBounds(new java.awt.Rectangle(5, 55, 86, 21));
		idLabel.setBounds(new java.awt.Rectangle(260, 25, 86, 21));
        idLabel.setText(PluginServices.getText(this, "id_field"));

        setLayout(null);
        setBounds(new java.awt.Rectangle(3, 225, 501, 246));
        setBorder(javax.swing.BorderFactory.createTitledBorder(null,
                PluginServices.getText(this, "specify_table_settings"),
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
        add(getIdComboBox(), null);
        add(getSqlTextAreaScroll(), null);
        add(idLabel, null);
        add(sqlLabel, null);
        add(getActivateSQLCheckBox(), null);
        add(getLayerNameTextField(), null);
        add(tableNamejLabel, null);
        add(getAdvancedButton(), null);

        loadValues(_empty);
    }

    public void enableAlphaControls(boolean enable) {
        getIdComboBox().setEnabled(enable);
        getLayerNameTextField().setEnabled(enable);
        getSqlTextArea().setEnabled(enable);
        getActivateSQLCheckBox().setEnabled(enable);
        getAdvancedButton().setEnabled(enable);
    }

    public void enableSpatialControls(boolean enable) {

    }

    public void loadValues() {
        loadValues(false);
    }

    protected void loadValues(boolean is_empty) {
        if (is_empty) {
            enableAlphaControls(false);
            enableSpatialControls(false);
            getLayerNameTextField().setText("");
            getActivateSQLCheckBox().setSelected(false);
        }
        else {
            enableAlphaControls(true);
			enableSpatialControls(true);
			getActivateSQLCheckBox().setEnabled(true);
            getIdComboBox().removeAllItems();

            for (int i = 0; i < ids.length; i++) {
                getIdComboBox().addItem(ids[i]);
            }


            getLayerNameTextField().setText(initLayerName);

        }
    }

    private JComboBox getIdComboBox() {
        if (idComboBox == null) {
            idComboBox = new JComboBox();

            // idComboBox.setBounds(new java.awt.Rectangle(105, 55, 126, 21));
			idComboBox.setBounds(new java.awt.Rectangle(351, 25, 134, 21));
        }

        return idComboBox;
    }

    private JButton getAdvancedButton() {
    	if (advanzedButton == null) {
			advanzedButton = new JButton();
			advanzedButton.setLocation(new Point(350, 110));
			advanzedButton.setText(PluginServices.getText(this,
					"advanced_properties"));
			advanzedButton.addActionListener(this);
		}
		return advanzedButton;
	}

    private JCheckBox getActivateSQLCheckBox() {
        if (activateSQLCheckBox == null) {
            activateSQLCheckBox = new JCheckBox();
            activateSQLCheckBox.addActionListener(this);
            //activateSQLCheckBox
					//.setBounds(new java.awt.Rectangle(5, 90, 21, 21));
			activateSQLCheckBox
					.setBounds(new java.awt.Rectangle(5, 55, 21, 21));
        }

        return activateSQLCheckBox;
    }

    private JTextField getLayerNameTextField() {
        if (layerNameTextField == null) {
            layerNameTextField = new JTextField();
            layerNameTextField.setSize(new java.awt.Dimension(118, 21));
			// layerNameTextField.setLocation(new java.awt.Point(105, 25));
			layerNameTextField.setLocation(new java.awt.Point(120, 25));
            layerNameTextField.addKeyListener(this);
        }

        return layerNameTextField;
    }

    private JTextArea getSqlTextArea() {
        if (sqlTextArea == null) {
            sqlTextArea = new JTextArea();
            sqlTextArea.setLineWrap(true);
            sqlTextArea.setWrapStyleWord(true);
            sqlTextArea.addKeyListener(this);

            // sqlTextArea.setBounds(new java.awt.Rectangle(160,90,326,41));
        }

        return sqlTextArea;
    }

    private JScrollPane getSqlTextAreaScroll() {
        if (sqlTextAreaScroll == null) {
            sqlTextAreaScroll = new JScrollPane();
            // sqlTextAreaScroll.setBounds(new java.awt.Rectangle(160, 90, 326,
			// 41));
			sqlTextAreaScroll
					.setBounds(new java.awt.Rectangle(160, 55, 326, 41));
            sqlTextAreaScroll.setViewportView(getSqlTextArea());
            sqlTextAreaScroll.updateUI();
        }

        return sqlTextAreaScroll;
    }

    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == activateSQLCheckBox) {
            enableSQLSettings(activateSQLCheckBox.isSelected());
            parent.checkFinishable();
        } else if (src == advanzedButton) {
        	List<String> toHide = new ArrayList<String>();
			toHide.add(DBStoreParameters.DYNFIELDNAME_PASSWORD);
			toHide.add(DBStoreParameters.DYNFIELDNAME_HOST);
			toHide.add(DBStoreParameters.DYNFIELDNAME_PORT);
			toHide.add(DBStoreParameters.DYNFIELDNAME_USER);
			toHide.add(DBStoreParameters.DYNFIELDNAME_DBNAME);
			toHide.add(DBStoreParameters.DYNFIELDNAME_DEFAULTGEOMETRY);
			toHide.add(DBStoreParameters.DYNFIELDNAME_PKFIELDS);
			toHide.add(DBStoreParameters.DYNFIELDNAME_FIELDS);
			toHide.add(DBStoreParameters.DYNFIELDNAME_INITIALFILTER);
			toHide.add(DBStoreParameters.DYNFIELDNAME_WORKINGAREA);
			toHide.add(DBStoreParameters.DYNFIELDNAME_TABLE);
			toHide.add(DBStoreParameters.DYNFIELDNAME_SRS);
			try {
				parameters.validate();
			} catch (Exception ex) {
				// ignore... only for fill default values
			}
			DynObjectEditor editor = new DynObjectEditor(parameters,
					DynObjectEditor.HIDDE_THIS_PARAMS, toHide);
			editor.editObject(true);

			return;

        }

    }


    private void enableSQLSettings(boolean b) {
        sqlTextArea.setEnabled(b);
    }

    protected String getFormattedDouble(double d) {
        DecimalFormat df = new DecimalFormat("#.###");
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(dfs);
        return df.format(d);
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
        parent.checkFinishable();
    }

    public void keyTyped(KeyEvent e) {
    }


    public String getUserLayerName() {
        return getLayerNameTextField().getText();
    }

    public boolean combosHaveItems() {
        if (getIdComboBox().getItemCount() == 0) {
            return false;
        }

        return true;
    }

    public void repaint() {
        super.repaint();
        getIdComboBox().updateUI();
    }

    public String getIdFieldName() {
        return getIdComboBox().getSelectedItem().toString();
    }

    public String getWhereClause() {
        return getSqlTextArea().getText();
    }

    public boolean isSqlActive() {
        return getActivateSQLCheckBox().isSelected();
    }
}
