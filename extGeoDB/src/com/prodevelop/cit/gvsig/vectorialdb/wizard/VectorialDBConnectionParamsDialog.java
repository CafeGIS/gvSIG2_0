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

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataServerExplorerParameters;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ValidateDataParametersException;
import org.gvsig.fmap.dal.serverexplorer.db.DBServerExplorerParameters;
import org.gvsig.fmap.dal.serverexplorer.filesystem.swing.DynObjectEditor;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;


/**
 * Lets the user input the connection parameters.
 *
 * @author jldominguez
 *
 */
public class VectorialDBConnectionParamsDialog extends JPanel implements IWindow,
    ActionListener, KeyListener {
    private static Logger logger = Logger.getLogger(VectorialDBConnectionParamsDialog.class.getName());
    private WindowInfo winfo = new WindowInfo(8); // MODAL only
    private JButton cancelButton = null;
    private JButton okButton = null;
    private JButton advancedButton = null;
    private JPanel paramsPanel = null;
    private JComboBox driverComboBox = null;
    private JTextField portTextField = null;
    private JTextField dbTextField = null;
    private JTextField userTextField = null;
    private JPasswordField passwordField = null;
    private JLabel driverLabel = null;
    private JLabel portLabel = null;
    private JLabel dbLabel = null;
    private JLabel dbLabelWarning = null;
    private JLabel userLabel = null;
    private JLabel pwLabel = null;
    private boolean okPressed = false;
    private JTextField urlTextField = null;
    private JLabel urlLabel = null;
    private JCheckBox connectedCheckBox = null;
    private JLabel connectedLabel = null;
    private JLabel connNameLabel = null;
    private JTextField connNameTextField = null;

    private DBServerExplorerParameters params = null;

    /**
     * This method initializes
     *
     */
    public VectorialDBConnectionParamsDialog() {
        super();
        initialize();
    }

    public void showDialog() {
        PluginServices.getMDIManager().addWindow(this);
    }

    /**
     * This method initializes this
     *
     */
    private void initialize() {
        winfo.setWidth(370);
        winfo.setHeight(317 - 25);
        winfo.setTitle(PluginServices.getText(this, "connection_parameters"));

        this.setSize(new java.awt.Dimension(360, 329));
        this.setLayout(null);
        this.add(getCancelButton(), null);
        this.add(getOkButton(), null);
        this.add(getAdvancedButton(), null);
        this.add(getParamsPanel(), null);
    }

    public WindowInfo getWindowInfo() {
        return winfo;
    }

    /**
     * This method initializes cancelButton
     *
     * @return javax.swing.JButton
     */
    private JButton getCancelButton() {
        if (cancelButton == null) {
            cancelButton = new JButton();
            cancelButton.setText(PluginServices.getText(this, "cancel"));
            cancelButton.addActionListener(this);
            cancelButton.setBounds(new java.awt.Rectangle(124, 292, 90, 26));
        }

        return cancelButton;
    }

    /**
     * This method initializes okButton
     *
     * @return javax.swing.JButton
     */
    private JButton getOkButton() {
        if (okButton == null) {
            okButton = new JButton();
            okButton.setText(PluginServices.getText(this, "ok"));
            okButton.addActionListener(this);
            okButton.setBounds(new java.awt.Rectangle(30, 292, 90, 26));
        }

        return okButton;
    }

	/**
	 * This method initializes okButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getAdvancedButton() {
		if (advancedButton == null) {
			advancedButton = new JButton();
			advancedButton.setText(PluginServices.getText(this, "advanced"));
			advancedButton.addActionListener(this);
			advancedButton.setBounds(new java.awt.Rectangle(218, 292, 90, 26));
		}

		return advancedButton;
	}

	/**
	 * This method initializes paramsPanel
	 *
	 * @return javax.swing.JPanel
	 */
    private JPanel getParamsPanel() {
        if (paramsPanel == null) {
            connNameLabel = new JLabel();
            connNameLabel.setBounds(new java.awt.Rectangle(10, 30, 141, 21));
            connNameLabel.setText(PluginServices.getText(this, "connection_name") +
                ":");
            connectedLabel = new JLabel();
            connectedLabel.setBounds(new java.awt.Rectangle(10, 247, 141, 21));
            connectedLabel.setText(PluginServices.getText(this, "connected") +
                ":");
            urlLabel = new JLabel();
            urlLabel.setBounds(new java.awt.Rectangle(10, 80, 141, 21));
            urlLabel.setText(PluginServices.getText(this, "server_url") + ":");
            pwLabel = new JLabel();
            pwLabel.setBounds(new java.awt.Rectangle(10, 222, 141, 21));
            pwLabel.setText(PluginServices.getText(this, "password") + ":");
            userLabel = new JLabel();
            userLabel.setBounds(new java.awt.Rectangle(10, 197, 141, 21));
            userLabel.setText(PluginServices.getText(this, "user") + ":");
            dbLabel = new JLabel();
            dbLabel.setBounds(new java.awt.Rectangle(10, 130, 141, 21));
            dbLabel.setText(PluginServices.getText(this, "database_name") +
                ":");
            dbLabelWarning = new JLabel();
            dbLabelWarning.setBounds(new java.awt.Rectangle(10, 155, 310, 41));
            dbLabelWarning.setText(PluginServices.getText(this, "warning_you_must_input_the_exact_name_this_difference_between_capital_letters_and_small_letters")
                );

            portLabel = new JLabel();
            portLabel.setBounds(new java.awt.Rectangle(10, 105, 141, 21));
            portLabel.setText(PluginServices.getText(this, "port") + ":");
            driverLabel = new JLabel();
            driverLabel.setBounds(new java.awt.Rectangle(10, 55, 141, 21));
            driverLabel.setText(PluginServices.getText(this, "driver") + ":");
            paramsPanel = new JPanel();
            paramsPanel.setBounds(new java.awt.Rectangle(10, 10, 336, 273));
            paramsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
                    null, PluginServices.getText(this, "connection_params"),
                    javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                    javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
            paramsPanel.setLayout(null);
            paramsPanel.add(getPortTextField(), null);
            paramsPanel.add(getDriverComboBox(), null);
            paramsPanel.add(getDbTextField(), null);
            paramsPanel.add(getUserTextField(), null);
            paramsPanel.add(getPasswordField(), null);
            paramsPanel.add(driverLabel, null);
            paramsPanel.add(portLabel, null);
            paramsPanel.add(dbLabel, null);
            paramsPanel.add(dbLabelWarning, null);
            paramsPanel.add(userLabel, null);
            paramsPanel.add(pwLabel, null);
            paramsPanel.add(getUrlTextArea(), null);
            paramsPanel.add(urlLabel, null);
            paramsPanel.add(getConnectedCheckBox(), null);
            paramsPanel.add(connectedLabel, null);
            paramsPanel.add(connNameLabel, null);
            paramsPanel.add(getConnNameTextField(), null);
        }

        return paramsPanel;
    }

    /**
     * This method initializes driverComboBox
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox getDriverComboBox() {
        if (driverComboBox == null) {
            driverComboBox = new JComboBox();
            driverComboBox.addActionListener(this);
            DataManager dm = DALLocator.getDataManager();
            List<String> explorers = dm.getExplorerProviders();
            Iterator<String> iter = explorers.iterator();
            String expName;

            DataServerExplorerParameters params;
            while (iter.hasNext()) {
            	expName = iter.next();
				try {
					params = dm.createServerExplorerParameters(expName);
				} catch (DataException e) {
					NotificationManager.addWarning(PluginServices.getText(null,
							"DataExplorer_parameters_error")
							+ ": " + expName, e);
					continue;
				}
				if (params instanceof DBServerExplorerParameters) {
					DBServerExplorerParameters dbParams = (DBServerExplorerParameters) params;
					driverComboBox.addItem(new DriverComboBoxItem(
							dbParams));
				}

            }

            driverComboBox.setBounds(new java.awt.Rectangle(155, 55, 166, 21));

        }

        return driverComboBox;
    }

    private class DriverComboBoxItem {
    	private DBServerExplorerParameters params;

    	public DriverComboBoxItem(DBServerExplorerParameters dbParams) {
			this.params = dbParams;
		}

		public String toString() {
			// FIXME
    		return params.getExplorerName();
    	}
    }

    /**
     * This method initializes portTextField
     *
     * @return javax.swing.JTextField
     */
    private JTextField getPortTextField() {
        if (portTextField == null) {
            portTextField = new JTextField();
            portTextField.addKeyListener(this);
            portTextField.setBounds(new java.awt.Rectangle(155, 105, 166, 21));
        }

        return portTextField;
    }

    /**
     * This method initializes dbTextField
     *
     * @return javax.swing.JTextField
     */
    private JTextField getDbTextField() {
        if (dbTextField == null) {
            dbTextField = new JTextField();
            dbTextField.addKeyListener(this);
            dbTextField.setBounds(new java.awt.Rectangle(155, 130, 166, 21));
        }

        return dbTextField;
    }

    /**
     * This method initializes userTextField
     *
     * @return javax.swing.JTextField
     */
    private JTextField getUserTextField() {
        if (userTextField == null) {
            userTextField = new JTextField();
            userTextField.addKeyListener(this);
            userTextField.setBounds(new java.awt.Rectangle(155, 197, 166, 21));
        }

        return userTextField;
    }

    /**
     * This method initializes passwordField
     *
     * @return javax.swing.JPasswordField
     */
    private JPasswordField getPasswordField() {
        if (passwordField == null) {
            passwordField = new JPasswordField();
            passwordField.addKeyListener(this);
            passwordField.setBounds(new java.awt.Rectangle(155, 222, 166, 21));
        }

        return passwordField;
    }

//    private String[] getDriverNames() {
//        Class[] classes = new Class[] { IVectorialDatabaseDriver.class };
//
//        ArrayList ret = new ArrayList();
//        String[] driverNames = LayerFactory.getDM().getDriverNames();
//
//        for (int i = 0; i < driverNames.length; i++) {
//            for (int j = 0; j < classes.length; j++) {
//                if (LayerFactory.getDM().isA(driverNames[i], classes[j])) {
//                    ret.add(driverNames[i]);
//                }
//            }
//        }
//
//        return (String[]) ret.toArray(new String[0]);
//    }

    public void actionPerformed(ActionEvent arg0) {
        Object src = arg0.getSource();

        if (src == connectedCheckBox) {
            if (connectedCheckBox.isSelected()) {
                passwordField.setEnabled(true);
                passwordField.setBackground(Color.WHITE);
            }
            else {
                passwordField.setText("");
                passwordField.setEnabled(false);
                passwordField.setBackground(Color.LIGHT_GRAY);
            }
        }

        if (src == okButton) {
            okPressed = true;
            PluginServices.getMDIManager().closeWindow(this);

            return;
        }

        if (src == cancelButton) {
            okPressed = false;
            PluginServices.getMDIManager().closeWindow(this);

            return;
        }

        if (src == advancedButton) {
        	List<String> toHide = new ArrayList<String>();
        	toHide.add(DBServerExplorerParameters.DYNFIELDNAME_PASSWORD);
        	toHide.add(DBServerExplorerParameters.DYNFIELDNAME_HOST);
			toHide.add(DBServerExplorerParameters.DYNFIELDNAME_PORT);
			toHide.add(DBServerExplorerParameters.DYNFIELDNAME_USER);
			toHide.add(DBServerExplorerParameters.DYNFIELDNAME_DBNAME);
			DBServerExplorerParameters myParams = getParameters();
			try {
				myParams.validate();
			} catch (Exception e) {
				// ignore... only for fill default values
			}
			DynObjectEditor editor = new DynObjectEditor(myParams,
					DynObjectEditor.HIDDE_THIS_PARAMS, toHide);
			editor.editObject(true);

			return;
		}

        if (src == driverComboBox) {
        	DBServerExplorerParameters params = ((DriverComboBoxItem) driverComboBox
					.getSelectedItem()).params;
        		try {
				params.validate();
			} catch (ValidateDataParametersException e) {
			}

//            try {
//            	DataManager dm=DataManager.getManager();
//                featureStore = (FeatureStore) dm.createDataStore()LayerFactory.getDM()
//                                                           .getDriver(driverName);
			if (params.getPort() != null) {
				portTextField.setText(params.getPort() + "");
			} else {
				portTextField.setText("");
			}
//            }
//            catch (DriverLoadException e1) {
//                portTextField.setText("");
//            }

            return;
        }
    }

    public boolean isOkPressed() {
        return okPressed;
    }

    public boolean hasToBeConnected() {
        return connectedCheckBox.isSelected();
    }

    public String getConnectionDriverName() {
        return ((DriverComboBoxItem) driverComboBox.getSelectedItem()).params
				.getExplorerName();
    }

    public String getConnectionServerUrl() {
        return urlTextField.getText();
    }

    public String getConnectionPort() {
        return portTextField.getText();
    }

    public String getConnectionDBName() {
        return dbTextField.getText();
    }

    public String getConnectionUser() {
        return userTextField.getText();
    }

    public String getConnectionPassword() {
        String resp = new String(passwordField.getPassword());

        return resp;
    }

    private JTextField getUrlTextArea() {
        if (urlTextField == null) {
            urlTextField = new JTextField();
            urlTextField.addKeyListener(this);
            urlTextField.setBounds(new java.awt.Rectangle(155, 80, 166, 21));
        }

        return urlTextField;
    }

    /**
     * This method initializes connectedCheckBox
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getConnectedCheckBox() {
        if (connectedCheckBox == null) {
            connectedCheckBox = new JCheckBox();
            connectedCheckBox.setSelected(true);
            connectedCheckBox.addActionListener(this);
            connectedCheckBox.setBounds(new java.awt.Rectangle(155, 247, 26, 21));
        }

        return connectedCheckBox;
    }

    public String getConnectionName() {
        return getConnNameTextField().getText();
    }

    /**
     * This method initializes connNameTextField
     *
     * @return javax.swing.JTextField
     */
    private JTextField getConnNameTextField() {
        if (connNameTextField == null) {
            connNameTextField = new JTextField();
            connNameTextField.addKeyListener(this);
            connNameTextField.setBounds(new java.awt.Rectangle(155, 30, 166, 21));
        }

        return connNameTextField;
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyChar() != '\n') {
            return;
        }

        Object src = e.getSource();

        if (src == passwordField) {
            ActionEvent aevt = new ActionEvent(okButton,
                    ActionEvent.ACTION_PERFORMED, "");
            actionPerformed(aevt);
        }
        else {
            if (src instanceof JTextField) {
                ((JTextField) src).transferFocus();
            }
        }
    }

    public void keyTyped(KeyEvent e) {
	}

    public void loadValues(DBServerExplorerParameters cwp) {
    	if (cwp.getPort() != null){
    		getPortTextField().setText(cwp.getPort().toString());
    	} else{
    		getPortTextField().setText("");
    	}
        selectThisInDriverCombo(cwp.getExplorerName());
        getDbTextField().setText(cwp.getDBName());
        getUserTextField().setText(cwp.getUser());

        if (cwp.getPassword() == null) {
            getPasswordField().setText("");
        }
        else {
            getPasswordField().setText(cwp.getPassword());
        }

        getUrlTextArea().setText(cwp.getHost());

        boolean connected = false;

//        try {
//            connected = (cwp.getConnection() != null) &&
//                (!cwp.getConnection().isClosed());
//        }
//        catch (DBException e) {
//            logger.error("While checking connection: " + e.getMessage());
//            connected = false;
//        }

//        getConnectedCheckBox().setSelected(connected);
		getConnNameTextField().setText(cwp.getExplorerName());
    }

    private void selectThisInDriverCombo(String drvName) {
        int size = getDriverComboBox().getItemCount();
        int curSel = getDriverComboBox().getSelectedIndex();

        for (int i = 0; i < size; i++) {
            DriverComboBoxItem item = (DriverComboBoxItem) getDriverComboBox()
					.getItemAt(i);

            if (item.params.getExplorerName().compareToIgnoreCase(drvName) == 0) {
                getDriverComboBox().setSelectedIndex(i);
                if (curSel != i) {
					this.params = null;
				}

                return;
            }
        }
    }

	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}

	public DBServerExplorerParameters getParameters(){
		if (params == null) {
			DriverComboBoxItem item = (DriverComboBoxItem) getDriverComboBox()
					.getItemAt(getDriverComboBox().getSelectedIndex());
			params = (DBServerExplorerParameters) item.params.getCopy();


		}
		String _host = getConnectionServerUrl();
		String _port = getConnectionPort();
		String _dbname = getConnectionDBName();
		String _user = getConnectionUser();
		String _pw = getConnectionPassword();

		params.setHost(_host);
		if (_port.trim().length() != 0) {
			try {
				params.setPort(Integer.parseInt(_port));
			} catch (NumberFormatException e) {
				NotificationManager.addError("Invalid port", e);
			}
		}
		params.setDBName(_dbname);
		params.setUser(_user);
		params.setPassword(_pw);

		return params;
	}

} //  @jve:decl-index=0:visual-constraint="10,10"
