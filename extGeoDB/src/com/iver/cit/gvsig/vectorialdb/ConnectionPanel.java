package com.iver.cit.gvsig.vectorialdb;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.ProviderNotRegisteredException;
import org.gvsig.fmap.dal.resource.db.DBParameters;

import com.iver.andami.PluginServices;
import com.iver.utiles.NotExistInXMLEntity;
import com.iver.utiles.XMLEntity;
import com.iver.utiles.swing.JComboBox;
import com.iver.utiles.swing.wizard.Step;
import com.iver.utiles.swing.wizard.WizardControl;


/**
 * DOCUMENT ME!
 *
 * @author Fernando González Cortés
 */
public class ConnectionPanel extends JPanel implements Step {
    private static String passw = null;

    private JPanel jPanelLabels = null;
    private JLabel jLabel = null;
    private JTextField txtHost = null;
    private JPanel jPanelTexts = null;
    private JTextField txtPort = null;
    private JLabel jLabel1 = null;
    private JLabel jLabel2 = null;
    private JTextField txtUser = null;
    private JLabel jLabel3 = null;
    private JPasswordField txtPassword = null;
    private JLabel jLabel4 = null;
    private JTextField txtBD = null;
    private JLabel jLabel5 = null;
    private JComboBox cmbDriver = null;
    private JLabel jLabel6 = null;
    private com.iver.utiles.swing.JComboBox cmbName = null;
    private HashMap cs = new HashMap();

    private JPanel jPanel = null;

	private JLabel jLabelSchema = null;

	private ConnectionPanel XYZ = null;

	private JPanel jPanel1 = null;

	private JPanel jPanelLabels1 = null;

	private JLabel jLabel61 = null;

	private JLabel jLabel7 = null;

	private JLabel jLabel11 = null;

	private JLabel jLabel21 = null;

	private JLabel jLabel31 = null;

	private JLabel jLabel51 = null;

	private JPanel jPanelTexts1 = null;

	private JComboBox cmbName1 = null;

	private JTextField txtHost1 = null;

	private JTextField txtPort1 = null;

	private JTextField txtUser1 = null;

	private JPasswordField txtPassword1 = null;

	private JTextField txtBD1 = null;

	private JComboBox cmbDriver1 = null;

	private JTextField txtSchema = null;



    /**
     * This is the default constructor
     */
    public ConnectionPanel() {
        super();
        initialize();
    }

    /**
     * This method initializes this
     */
    private void initialize() {
        this.setLayout(null);
        this.setSize(335, 240);
        this.add(getJPanel(), null);

        if (passw != null) {
			txtPassword.setText(passw);
		}

    }

    /**
     * This method initializes jPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanelLabels() {
        if (jPanelLabels == null) {
            String string = PluginServices.getText(this, "schema") + ":";
            jLabelSchema = new JLabel();
            jLabelSchema.setName("jLabel4");
            jLabelSchema.setHorizontalAlignment(SwingConstants.RIGHT);
            jLabelSchema.setHorizontalTextPosition(SwingConstants.RIGHT);
            jLabelSchema.setText(string);
            jLabelSchema.setPreferredSize(new Dimension(140, 19));
            FlowLayout flowLayout3 = new FlowLayout();
            flowLayout3.setVgap(15);
            jLabel5 = new JLabel();
            jLabel4 = new JLabel();
            jLabel3 = new JLabel();
            jLabel2 = new JLabel();
            jLabel1 = new JLabel();
            jLabel = new JLabel();
            jPanelLabels = new JPanel();
            jPanelLabels.setLayout(flowLayout3);
            jPanelLabels.setName("jPanelLabels");
            jPanelLabels.setPreferredSize(new java.awt.Dimension(150,400));
            jLabel.setText(PluginServices.getText(this, "host") + ":");
            jLabel.setPreferredSize(new java.awt.Dimension(140,19));
            jLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            jLabel.setName("jLabel");
            jLabel1.setText(PluginServices.getText(this, "puerto")+":");
            jLabel1.setPreferredSize(new java.awt.Dimension(140,19));
            jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
            jLabel1.setName("jLabel1");
            jLabel2.setText(PluginServices.getText(this, "usuario")+":");
            jLabel2.setName("jLabel2");
            jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
            jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            jLabel2.setPreferredSize(new java.awt.Dimension(140,19));
            jLabel3.setText(PluginServices.getText(this, "password")+":");
            jLabel3.setName("jLabel3");
            jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
            jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            jLabel3.setPreferredSize(new java.awt.Dimension(140,19));
            jLabel4.setText(PluginServices.getText(this, "bd")+":");
            jLabel4.setName("jLabel4");
            jLabel4.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
            jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            jLabel4.setPreferredSize(new java.awt.Dimension(140,19));
            jLabel5.setText(PluginServices.getText(this, "driver")+":");
            jLabel5.setName("jLabel5");
            jLabel5.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
            jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            jLabel5.setPreferredSize(new java.awt.Dimension(140,19));
            jPanelLabels.add(getJLabel6(), null);
            jPanelLabels.add(jLabel, null);
            jPanelLabels.add(jLabel1, null);
            jPanelLabels.add(jLabel2, null);
            jPanelLabels.add(jLabel3, null);
            jPanelLabels.add(jLabel4, null);
            jPanelLabels.add(jLabelSchema, null);
            jPanelLabels.add(jLabel5, null);

        }

        return jPanelLabels;
    }

    /**
     * This method initializes txtHost
     *
     * @return javax.swing.JTextField
     */
    private JTextField getTxtHost() {
        if (txtHost == null) {
            txtHost = new JTextField();
            txtHost.setPreferredSize(new java.awt.Dimension(170, 19));
            txtHost.setName("txtHost");
        }

        return txtHost;
    }

    /**
     * This method initializes jPanel1
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanelTexts() {
        if (jPanelTexts == null) {
            FlowLayout flowLayout2 = new FlowLayout(FlowLayout.LEFT);
            flowLayout2.setVgap(15);
            jPanelTexts = new JPanel();
            jPanelTexts.setLayout(flowLayout2);
            jPanelTexts.setPreferredSize(new Dimension(200, 300));

            jPanelTexts.setName("jPanelText");
            jPanelTexts.add(getCmbName(), null);
            jPanelTexts.add(getTxtHost(), null);
            jPanelTexts.add(getTxtPort(), null);
            jPanelTexts.add(getTxtUser(), null);
            jPanelTexts.add(getTxtPassword(), null);
            jPanelTexts.add(getTxtBD(), null);
            jPanelTexts.add(getTxtSchema(), null);
            jPanelTexts.add(getCmbDriver(), null);
        }

        return jPanelTexts;
    }

    /**
     * This method initializes txtPort
     *
     * @return javax.swing.JTextField
     */
    private JTextField getTxtPort() {
        if (txtPort == null) {
            txtPort = new JTextField();
            txtPort.setPreferredSize(new java.awt.Dimension(40, 19));
            txtPort.setName("txtPort");
            txtPort.setText("");
            txtPort.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        }

        return txtPort;
    }

    /**
     * This method initializes txtUser
     *
     * @return javax.swing.JTextField
     */
    private JTextField getTxtUser() {
        if (txtUser == null) {
            txtUser = new JTextField();
            txtUser.setPreferredSize(new java.awt.Dimension(170, 19));
            txtUser.setName("txtUser");
        }

        return txtUser;
    }

    /**
     * This method initializes txtPassword
     *
     * @return javax.swing.JTextField
     */
    private JPasswordField getTxtPassword() {
        if (txtPassword == null) {
            txtPassword = new JPasswordField();
            txtPassword.setPreferredSize(new java.awt.Dimension(170, 19));
            txtPassword.setName("txtPassword");
        }

        return txtPassword;
    }

    /**
     * This method initializes txtBD
     *
     * @return javax.swing.JTextField
     */
    private JTextField getTxtBD() {
        if (txtBD == null) {
            txtBD = new JTextField();
            txtBD.setPreferredSize(new java.awt.Dimension(170, 19));
            txtBD.setName("txtBD");
        }

        return txtBD;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getHost() {
        return getTxtHost().getText();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getPort() {
        return getTxtPort().getText();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getUser() {
        return getTxtUser().getText();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSettingsName() {
        return getCmbName().getSelectedItem().toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getPassword() {
        passw = String.copyValueOf(getTxtPassword().getPassword());
        return passw;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getDBName() {
        return getTxtBD().getText();
    }

    /**
     * DOCUMENT ME!
     *
     * @param drivers DOCUMENT ME!
     */
    public void setDrivers(String[] drivers) {
        DefaultComboBoxModel model = new DefaultComboBoxModel();

        for (int i = 0; i < drivers.length; i++) {
            model.addElement(drivers[i]);
        }

        getCmbDriver().setModel(model);

        //Para que refresque el textbox del puerto
        if (drivers.length > 0) {
			cmbDriver.setSelectedIndex(0);
		}

    }


    /**
     * @return ConnectionSettings based on values in this panel
     */
    public ConnectionSettings getConnectionSettings()
    {
        ConnectionSettings cs = new ConnectionSettings();
        cs.setDb(getDBName());
        cs.setSchema(getSchema());
        cs.setDriver(getDriver());
        cs.setHost(getHost());
        cs.setPort(getPort());
        cs.setUser(getUser());
        cs.setPassw(getPassword());
        cs.setName(getSettingsName());
        return cs;
    }

    public String getSchema() {
		return txtSchema.getText();
	}

    /**
     * Makes persistent the connection settings of this panel.
     */
    public void saveConnectionSettings()
    {
    	ConnectionSettings cs = new ConnectionSettings();
        cs.setDb(getDBName());
        cs.setSchema(getSchema());
        cs.setDriver(getDriver());
        cs.setHost(getHost());
        cs.setPort(getPort());
        cs.setUser(getUser());
        cs.setPassw(getPassword());
        cs.setName(getSettingsName());

        PluginServices ps = PluginServices.getPluginServices(this);
        XMLEntity xml = ps.getPersistentXML();

        try {
            String[] connections = xml.getStringArrayProperty("jdbc-connections");
            String[] newConnections = new String[connections.length + 1];
            System.arraycopy(connections, 0, newConnections, 0, connections.length);
            newConnections[connections.length] = cs.toString();
            xml.putProperty("jdbc-connections", newConnections);
        } catch (NotExistInXMLEntity e) {
            xml.putProperty("jdbc-connections", new String[] { cs.toString() });
        }


    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getDriver() {
        return cmbDriver.getSelectedItem().toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param cs DOCUMENT ME!
     */
    public void setSettings(HashMap cs) {
        this.cs = cs;
        ((DefaultComboBoxModel) getCmbName().getModel()).removeAllElements();

        Iterator i = cs.keySet().iterator();

        while (i.hasNext()) {
            String item = (String) i.next();
            ((DefaultComboBoxModel) getCmbName().getModel()).addElement(item);
        }
    }

    /**
     * @see com.iver.utiles.swing.wizard.Step#init(com.iver.utiles.swing.wizard.WizardControl)
     */
    public void init(WizardControl w) {
    }

    /**
     * This method initializes cmbDriver
     *
     * @return com.iver.utiles.swing.JComboBox
     */
    private JComboBox getCmbDriver() {
        if (cmbDriver == null) {
            cmbDriver = new JComboBox();
            cmbDriver.setPreferredSize(new java.awt.Dimension(170, 19));
            cmbDriver.setName("cmbDriver");
            cmbDriver.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String driverName = cmbDriver.getSelectedItem().toString();
                    DataManager dm = DALLocator.getDataManager();
                    DBParameters dsp=null;
					try {
						dsp = (DBParameters) dm
								.createServerExplorerParameters(driverName);
					} catch (InitializeException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ProviderNotRegisteredException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					// String port = dsp.getPort() + "";
					// getTxtPort().setText(port);
                }
            });
        }

        return cmbDriver;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public boolean done() {
        return (getTxtBD().getText().length() > 0) &&
        (getTxtHost().getText().length() > 0) &&
        (getCmbName().getSelectedItem() != null) &&
        (getCmbName().getSelectedItem().toString().length() > 0);
    }

    /**
     * This method initializes jLabel6
     *
     * @return javax.swing.JLabel
     */
    private JLabel getJLabel6() {
        if (jLabel6 == null) {
            jLabel6 = new JLabel();
            jLabel6.setText(PluginServices.getText(this, "connection_name")+":");
            jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            jLabel6.setPreferredSize(new java.awt.Dimension(140,19));
        }

        return jLabel6;
    }

    /**
     * This method initializes jComboBox
     *
     * @return com.iver.utiles.swing.JComboBox
     */
    private com.iver.utiles.swing.JComboBox getCmbName() {
        if (cmbName == null) {
            cmbName = new com.iver.utiles.swing.JComboBox();
            cmbName.setEditable(true);
            cmbName.setPreferredSize(new java.awt.Dimension(170, 19));
            cmbName.setModel(new DefaultComboBoxModel());
            cmbName.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        Object item = cmbName.getSelectedItem();

                        if (item == null) {
                            return;
                        }

                        ConnectionSettings c = (ConnectionSettings) cs.get(item.toString());

                        if (c != null) {
                            getTxtHost().setText(c.getHost());
                            getTxtPort().setText(c.getPort());
                            getTxtBD().setText(c.getDb());
                            getTxtUser().setText(c.getUser());
                            if (c.getPassw() != null) {
								getTxtPassword().setText(c.getPassw());
							}
                            getCmbDriver().setSelectedItem(c.getDriver());
                        }
                    }
                });
        }

        return cmbName;
    }

    /**
     * This method initializes jPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel() {
    	if (jPanel == null) {
    		jPanel = new JPanel();
    		jPanel.setLayout(new BorderLayout());
    		jPanel.setBounds(0, 0, 363, 240);
    		jPanel.add(getJPanelLabels(), java.awt.BorderLayout.WEST);
    		jPanel.add(getJPanelTexts(), java.awt.BorderLayout.EAST);
    	}
    	return jPanel;
    }

	/**
	 * This method initializes XYZ
	 *
	 * @return com.iver.cit.gvsig.jdbc_spatial.gui.jdbcwizard.ConnectionPanel
	 */
	private ConnectionPanel getXYZ() {
		if (XYZ == null) {
			XYZ = new ConnectionPanel();
			XYZ.setLayout(null);
			XYZ.setSize(new java.awt.Dimension(335,240));
			XYZ.add(getJPanel1(), null);
		}
		return XYZ;
	}

	/**
	 * This method initializes jPanel1
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jPanel1 = new JPanel();
			jPanel1.setLayout(new BorderLayout());
			jPanel1.setBounds(new Rectangle(0, 0, 363, 240));
			jPanel1.add(getJPanelLabels1(), java.awt.BorderLayout.WEST);
			jPanel1.add(getJPanelTexts1(), java.awt.BorderLayout.EAST);
		}
		return jPanel1;
	}

	/**
	 * This method initializes jPanelLabels1
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelLabels1() {
		if (jPanelLabels1 == null) {
			jLabel51 = new JLabel();
			jLabel51.setName("jLabel5");
			jLabel51.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel51.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel51.setText(PluginServices.getText(getXYZ(), "driver") + ":");
			jLabel51.setPreferredSize(new Dimension(140, 19));
			jLabel31 = new JLabel();
			jLabel31.setName("jLabel3");
			jLabel31.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel31.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel31.setText(PluginServices.getText(getXYZ(), "password") + ":");
			jLabel31.setPreferredSize(new Dimension(140, 19));
			jLabel21 = new JLabel();
			jLabel21.setName("jLabel2");
			jLabel21.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel21.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLabel21.setText(PluginServices.getText(getXYZ(), "usuario") + ":");
			jLabel21.setPreferredSize(new Dimension(140, 19));
			jLabel11 = new JLabel();
			jLabel11.setName("jLabel1");
			jLabel11.setHorizontalAlignment(SwingConstants.TRAILING);
			jLabel11.setText(PluginServices.getText(getXYZ(), "puerto") + ":");
			jLabel11.setPreferredSize(new Dimension(140, 19));
			jLabel7 = new JLabel();
			jLabel7.setName("jLabel");
			jLabel7.setHorizontalAlignment(SwingConstants.RIGHT);
			jLabel7.setText(PluginServices.getText(getXYZ(), "host") + ":");
			jLabel7.setPreferredSize(new Dimension(140, 19));
			jLabel61 = new JLabel();
			jLabel61.setPreferredSize(new Dimension(140, 19));
			jLabel61.setText(PluginServices.getText(getXYZ(), "connection_name") + ":");
			jLabel61.setHorizontalAlignment(SwingConstants.RIGHT);
			FlowLayout flowLayout31 = new FlowLayout();
			flowLayout31.setVgap(15);
			jPanelLabels1 = new JPanel();
			jPanelLabels1.setPreferredSize(new Dimension(150, 400));
			jPanelLabels1.setLayout(flowLayout31);
			jPanelLabels1.setName("jPanelLabels");
			jPanelLabels1.add(jLabel61, null);
			jPanelLabels1.add(jLabel7, null);
			jPanelLabels1.add(jLabel11, null);
			jPanelLabels1.add(jLabel21, null);
			jPanelLabels1.add(jLabel31, null);
			jPanelLabels1.add(jLabelSchema, jLabelSchema.getName());
			jPanelLabels1.add(jLabel51, null);
		}
		return jPanelLabels1;
	}

	/**
	 * This method initializes jPanelTexts1
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelTexts1() {
		if (jPanelTexts1 == null) {
			FlowLayout flowLayout21 = new FlowLayout(FlowLayout.LEFT);
			flowLayout21.setVgap(15);
			jPanelTexts1 = new JPanel();
			jPanelTexts1.setPreferredSize(new Dimension(200, 300));
			jPanelTexts1.setLayout(flowLayout21);
			jPanelTexts1.setName("jPanelText");
			jPanelTexts1.add(getCmbName1(), null);
			jPanelTexts1.add(getTxtHost1(), null);
			jPanelTexts1.add(getTxtPort1(), null);
			jPanelTexts1.add(getTxtUser1(), null);
			jPanelTexts1.add(getTxtPassword1(), null);
			jPanelTexts1.add(getTxtBD1(), null);
			jPanelTexts1.add(getCmbDriver1(), null);
		}
		return jPanelTexts1;
	}

	/**
	 * This method initializes cmbName1
	 *
	 * @return com.iver.utiles.swing.JComboBox
	 */
	private JComboBox getCmbName1() {
		if (cmbName1 == null) {
			cmbName1 = new JComboBox();
			cmbName1.setPreferredSize(new Dimension(170, 19));
			cmbName1.setModel(new DefaultComboBoxModel());
			cmbName1.setEditable(true);
		}
		return cmbName1;
	}

	/**
	 * This method initializes txtHost1
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getTxtHost1() {
		if (txtHost1 == null) {
			txtHost1 = new JTextField();
			txtHost1.setName("txtHost");
			txtHost1.setPreferredSize(new Dimension(170, 19));
		}
		return txtHost1;
	}

	/**
	 * This method initializes txtPort1
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getTxtPort1() {
		if (txtPort1 == null) {
			txtPort1 = new JTextField();
			txtPort1.setName("txtPort");
			txtPort1.setText("");
			txtPort1.setHorizontalAlignment(JTextField.LEFT);
			txtPort1.setPreferredSize(new Dimension(40, 19));
		}
		return txtPort1;
	}

	/**
	 * This method initializes txtUser1
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getTxtUser1() {
		if (txtUser1 == null) {
			txtUser1 = new JTextField();
			txtUser1.setName("txtUser");
			txtUser1.setPreferredSize(new Dimension(170, 19));
		}
		return txtUser1;
	}

	/**
	 * This method initializes txtPassword1
	 *
	 * @return javax.swing.JPasswordField
	 */
	private JPasswordField getTxtPassword1() {
		if (txtPassword1 == null) {
			txtPassword1 = new JPasswordField();
			txtPassword1.setName("txtPassword");
			txtPassword1.setPreferredSize(new Dimension(170, 19));
		}
		return txtPassword1;
	}

	/**
	 * This method initializes txtBD1
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getTxtBD1() {
		if (txtBD1 == null) {
			txtBD1 = new JTextField();
			txtBD1.setName("txtBD");
			txtBD1.setPreferredSize(new Dimension(170, 19));
		}
		return txtBD1;
	}

	/**
	 * This method initializes cmbDriver1
	 *
	 * @return com.iver.utiles.swing.JComboBox
	 */
	private JComboBox getCmbDriver1() {
		if (cmbDriver1 == null) {
			cmbDriver1 = new JComboBox();
			cmbDriver1.setName("cmbDriver");
			cmbDriver1.setPreferredSize(new Dimension(170, 19));
		}
		return cmbDriver1;
	}

	/**
	 * This method initializes txtSchema
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getTxtSchema() {
		if (txtSchema == null) {
			txtSchema = new JTextField();
			txtSchema.setName("txtBD");
			txtSchema.setPreferredSize(new Dimension(170, 19));
		}
		return txtSchema;
	}
} //  @jve:decl-index=0:visual-constraint="7,3"
