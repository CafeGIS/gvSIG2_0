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
package com.iver.cit.gvsig.gui.wizards;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.geom.Rectangle2D;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Vector;
import java.util.prefs.Preferences;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.mapcontext.exceptions.ProjectionLayerException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLayers;

import com.iver.andami.PluginServices;
import com.iver.andami.persistence.serverData.ServerDataPersistence;
import com.iver.cit.gvsig.fmap.drivers.wms.FMapWMSDriver;
import com.iver.cit.gvsig.fmap.layers.FLyrWMS;
import com.iver.cit.gvsig.fmap.layers.WMSLayerNode;
import com.iver.cit.gvsig.gui.WizardPanel;
import com.iver.cit.gvsig.gui.panels.LayerTree;
import com.iver.cit.gvsig.gui.panels.WMSParamsPanel;
import com.iver.utiles.NotExistInXMLEntity;
import com.iver.utiles.XMLEntity;
import com.iver.utiles.swing.jcomboServer.JComboServer;
import com.iver.utiles.swing.jcomboServer.ServerData;


/**
 * The WMS properties container panel.
 *
 * @author Jaume Dominguez Faus
 */
public class WMSWizard extends WizardPanel {
	private static Logger logger = Logger.getLogger(WMSWizard.class.getName());
	protected int page;
	protected boolean connected = false;
	private JComboServer cmbHost = null;
	private org.gvsig.gui.beans.swing.JButton btnConnect = null;
	private javax.swing.JPanel jPanel = null;
	private javax.swing.JLabel jLabel1 = null;
	private javax.swing.JLabel lblTitle = null;
	private javax.swing.JScrollPane jScrollPane = null;
	private javax.swing.JTextArea txtAbstract = null;
	private javax.swing.JPanel panelPage1 = null;
	private org.gvsig.gui.beans.swing.JButton btnSiguiente = null;
	private org.gvsig.gui.beans.swing.JButton btnAnterior = null;
	private javax.swing.JPanel jPanel1 = null;
	protected WizardListenerSupport listenerSupport = new WizardListenerSupport();
	protected WMSWizardData dataSource;
	protected WMSParamsPanel wmsParamsPanel = null;
	protected JLabel lblServerType = null;
	protected JLabel lblServerTypeValue = null;
	private JCheckBox chkCaching = null;
	private static Preferences fPrefs = Preferences.userRoot().node( "gvsig.wms-wizard" );
	private boolean refreshing = fPrefs.getBoolean("refresh_capabilities", false);
	protected int firstPage;
	private JPanel pnlName = null;

	public WMSWizard (WMSParamsPanel params){

		super();
		firstPage = 1;
		page = firstPage;
		this.wmsParamsPanel = params;
		this.dataSource = wmsParamsPanel.dataSource;

		setTabName("WMS");
		lblServerType = new JLabel();
		lblServerType.setBounds(20, 444, 100, 20);
		lblServerType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		lblServerType.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
		lblServerType.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 11));
		lblServerType.setText(PluginServices.getText(this, "server_type")+":");
		lblServerTypeValue = new JLabel();
		lblServerTypeValue.setBounds(128, 444, 148, 20);
		lblServerTypeValue.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 11));
		lblServerTypeValue.setText("-");
		this.setSize(510, 468);
		this.setLayout(null);
		this.setPreferredSize(new java.awt.Dimension(750, 420));
		this.setVisible(true);

		wmsParamsPanel.setListenerSupport(this.listenerSupport);
		wmsParamsPanel.setBounds(0, 5, 510, 428);
		wmsParamsPanel.getJTabbedPane().addMouseListener(
				new java.awt.event.MouseAdapter() {
					public void mouseClicked(java.awt.event.MouseEvent e) {
						page = wmsParamsPanel.currentPage() + 1;
						activarVisualizarBotones();
					}
				});
		this.add(wmsParamsPanel, null);
		this.add(lblServerType, null);
		this.add(lblServerTypeValue, null);
		getPanelPage2().setVisible(true);

		connected = true;
	}

	/**
	 * This is the default constructor
	 */
	public WMSWizard() {
		super();
		firstPage = 0;
		page = firstPage;
		initialize();
	}
	/**
	 * This method initializes this
	 */
	private void initialize() {
		setTabName("WMS");

		lblServerType = new JLabel();
		lblServerType.setBounds(20, 444, 100, 20);
		lblServerType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		lblServerType.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
		lblServerType.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 11));
		lblServerType.setText(PluginServices.getText(this, "server_type")+":");
		lblServerTypeValue = new JLabel();
		lblServerTypeValue.setBounds(128, 444, 148, 20);
		lblServerTypeValue.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 11));
		lblServerTypeValue.setText("-");
		this.setSize(510, 468);
		this.setLayout(null);
		this.setPreferredSize(new java.awt.Dimension(750, 420));
		this.setVisible(true);
		this.add(getPanelPage1(), null);
		this.add(getPanelPage2(), null);
		this.add(getBtnAnterior(), null);
		this.add(getBtnSiguiente(), null);
		this.add(lblServerType, null);
		this.add(lblServerTypeValue, null);
		activarVisualizarBotones();
	}


	protected JPanel getPanelPage2(){
		if (wmsParamsPanel == null){
			wmsParamsPanel = new WMSParamsPanel();
			wmsParamsPanel.setListenerSupport(this.listenerSupport);
			wmsParamsPanel.setBounds(0, 5, 510, 428);
			wmsParamsPanel.getJTabbedPane().addMouseListener(
					new java.awt.event.MouseAdapter() {
						public void mouseClicked(java.awt.event.MouseEvent e) {
							page = wmsParamsPanel.currentPage() + 1;
							activarVisualizarBotones();
						}
					});
		}
		return wmsParamsPanel;
	}
	/**
	 * DOCUMENT ME!
	 *
	 * @param host DOCUMENT ME!
	 */
	private void addHost(String host) {
		host = host.trim();

		ServerDataPersistence persistence = new ServerDataPersistence(this,ServerData.SERVER_TYPE_WMS);
		persistence.addServerData(new ServerData(host, ServerData.SERVER_TYPE_WMS));
		persistence.setPersistent();
	}

	/**
	 * DOCUMENT ME!
	 */
	private void rellenarControles() {
		try {
			String host = cmbHost.getModel().getSelectedItem().toString();

			dataSource.setHost(new URL(host), refreshing);
			lblTitle.setText(dataSource.getTitle());
			lblServerTypeValue.setText(dataSource.getServerType());
			txtAbstract.setText(dataSource.getAbstract());

			addHost(host);
			wmsParamsPanel.setWizardData(dataSource);
			connected = true;
			activarVisualizarBotones();
		} catch (Exception e) {
			listenerSupport.callError(e);
			e.printStackTrace();
		}
	}

	/**
	 * DOCUMENT ME!
	 */
	protected void activarVisualizarBotones() {
		if (page == firstPage) {
			getBtnAnterior().setEnabled(false);
			getBtnSiguiente().setVisible(true);

			if (connected) {
				getBtnSiguiente().setEnabled(true);
			} else {
				getBtnSiguiente().setEnabled(false);
			}
		} else if (page < wmsParamsPanel.getNumTabs()) {
			getBtnSiguiente().setEnabled(true);
			getBtnSiguiente().setVisible(true);
			getBtnAnterior().setEnabled(true);
			getBtnAnterior().setVisible(true);
			if (wmsParamsPanel.nextEnabledPage()==-1){
				getBtnSiguiente().setEnabled(false);
			}
			listenerSupport.callStateChanged(wmsParamsPanel.isCorrectlyConfigured());
		} else if (page == wmsParamsPanel.getNumTabs()) {
			getBtnSiguiente().setVisible(false);
			getBtnSiguiente().setEnabled(false);
			listenerSupport.callStateChanged(wmsParamsPanel
					.isCorrectlyConfigured());
		}
	}

	/**
	 * This method initializes txtHost
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JComboBox getTxtHost() {
		if (cmbHost == null) {
			cmbHost = new JComboServer();
			cmbHost.setModel(new DefaultComboBoxModel());
			cmbHost.setPreferredSize(new java.awt.Dimension(350, 20));
			cmbHost.setBounds(11, 26, 454, 20);
			cmbHost.setEditable(true);

			ServerDataPersistence persistence = new ServerDataPersistence(this,ServerData.SERVER_TYPE_WMS);
			ServerData[] servers = persistence.getArrayOfServerData();

			if (servers.length == 0){
				PluginServices ps = PluginServices.getPluginServices(this);
				XMLEntity xml = ps.getPersistentXML();
				try {
					String[] oldServers = xml.getStringArrayProperty("wms-servers");
					servers = new ServerData[oldServers.length];
					for (int i=0; i<oldServers.length; i++) {
						servers[i] = new ServerData(oldServers[i],ServerData.SERVER_TYPE_WMS);
					}
					xml.remove("wms-servers");
					ps.setPersistentXML(xml);
				} catch (NotExistInXMLEntity e) {
					// Old servers doesn't exist
					servers = new ServerData[12];
					servers[0] = new ServerData("http://www.idee.es/wms/IDEE-Base/IDEE-Base",ServerData.SERVER_TYPE_WMS);
					servers[1] = new ServerData("http://wms.jpl.nasa.gov/wms.cgi",ServerData.SERVER_TYPE_WMS);
					servers[2] = new ServerData("http://www2.dmsolutions.ca/cgi-bin/mswms_gmap?",ServerData.SERVER_TYPE_WMS);
					servers[3] = new ServerData("http://orto.cth.gva.es/wmsconnector/com.esri.wms.Esrimap/wms_patricova",ServerData.SERVER_TYPE_WMS);
					servers[4] = new ServerData("http://orto.cth.gva.es/wmsconnector/com.esri.wms.Esrimap/wms_parque_fondo_prug",ServerData.SERVER_TYPE_WMS);
					servers[5] = new ServerData("http://orto.cth.gva.es/wmsconnector/com.esri.wms.Esrimap/wms_pgof",ServerData.SERVER_TYPE_WMS);
					servers[6] = new ServerData("http://orto.cth.gva.es/wmsconnector/com.esri.wms.Esrimap/wms_urbanismo_tematicos",ServerData.SERVER_TYPE_WMS);
					servers[7] = new ServerData("http://onearth.jpl.nasa.gov/wms.cgi",ServerData.SERVER_TYPE_WMS);
					servers[8] = new ServerData("http://www.demis.nl/wms/wms.asp?WMS=WorldMap",ServerData.SERVER_TYPE_WMS);
					servers[9] = new ServerData("http://aes.gsfc.nasa.gov/cgi-bin/wms",ServerData.SERVER_TYPE_WMS);
					servers[10] = new ServerData("http://mapas.euitto.upm.es/cgi-bin/cnauticas",ServerData.SERVER_TYPE_WMS);
					servers[11] = new ServerData("http://ovc.catastro.meh.es/Cartografia/WMS/ServidorWMS.aspx", ServerData.SERVER_TYPE_WMS);
				}

				for (int i=0 ; i<servers.length ; i++){
					persistence.addServerData(servers[i]);
				}
				persistence.setPersistent();
			}

			cmbHost.setServerList(servers);


//			XMLEntity xml = PluginServices.getPluginServices(this)
//			.getPersistentXML();
//			if (xml == null)
//			xml = new XMLEntity();

//			if (!xml.contains("wms-servers")) {
//			String[] servers = new String[11];
//			// Begining of rubbish code
//			//
//			// The following block should not exist and it only will live in the
//			// 0.6 version. It fixes an error that has been happening since the 0.4 version
//			// and recovers the user's server data from the wrong location that has been used to use.
//			// This code will be removed from the 0.7 version
//			boolean bool = false;
//			{
//			Class gvSIGClass = AddLayer.class; // for example... (we only need to use a class that has been loaded by gvSIG's class loader
//			XMLEntity xml2 = PluginServices.getPluginServices(PluginServices.getExtension(gvSIGClass)).getPersistentXML();
//			if (xml2.contains("wms-servers")) {
//			String oldServers[] = xml2.getStringArrayProperty("wms-servers");
//			servers = new String[servers.length+oldServers.length];
//			for (int i = 0; i < oldServers.length; i++) {
//			servers[oldServers.length+i] = oldServers[i];
//			}
//			xml2.remove("wms-servers");
//			bool = true;
//			}
//			}
//			if (!bool) {
//			// end of the rubbish code (don't forget to remove the closing bracket)

//			servers[0] = "http://www.idee.es/wms/IDEE-Base/IDEE-Base";
//			servers[1] = "http://wms.jpl.nasa.gov/wms.cgi";
//			servers[2] = "http://www2.dmsolutions.ca/cgi-bin/mswms_gmap?";
//			servers[3] = "http://demo.deegree.org:8080/deegree/wms";
//			servers[4] = "http://orto.cth.gva.es/wmsconnector/com.esri.wms.Esrimap/wms_patricova";
//			servers[5] = "http://orto.cth.gva.es/wmsconnector/com.esri.wms.Esrimap/wms_parque_fondo_prug";
//			servers[6] = "http://orto.cth.gva.es/wmsconnector/com.esri.wms.Esrimap/wms_pgof";
//			servers[7] = "http://orto.cth.gva.es/wmsconnector/com.esri.wms.Esrimap/wms_urbanismo_tematicos";
//			servers[8] = "http://onearth.jpl.nasa.gov/wms.cgi";
//			servers[9] = "http://www.demis.nl/wms/wms.asp?WMS=WorldMap";
//			servers[9] = "http://aes.gsfc.nasa.gov/cgi-bin/wms";
//			servers[10]= "http://mapas.euitto.upm.es/cgi-bin/cnauticas";

//			} // this bracket will be removed too in version <= 0.7
//			xml.putProperty("wms-servers", servers);
//			}
//			try {
//			String[] servers = xml.getStringArrayProperty("wms-servers");
//			for (int i = 0; i < servers.length; i++) {
//			((DefaultComboBoxModel) cmbHost.getModel()).addElement(servers[i]);
//			}
//			} catch (NotExistInXMLEntity e) {
//			}
		}

		return cmbHost;
	}


	/**
	 * This method initializes btnDetalles
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBtnConnect() {
		if (btnConnect == null) {
			btnConnect = new org.gvsig.gui.beans.swing.JButton();
			btnConnect.setPreferredSize(new java.awt.Dimension(100, 20));
			btnConnect.setBounds(366, 50, 100, 20);
			btnConnect.setText(PluginServices.getText(this, "conectar"));
			btnConnect.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					rellenarControles();
				}
			});
		}

		return btnConnect;
	}

	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new javax.swing.JPanel();
			jPanel.setLayout(null);
			jPanel.setPreferredSize(new java.awt.Dimension(470, 130));
			jPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
					null, PluginServices.getText(this, "description"),
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
			jPanel.setBounds(2, 96, 477, 324);

			jPanel.add(getJScrollPane(), null);
			jPanel.add(getPnlName(), null);
		}

		return jPanel;
	}

	/**
	 * This method initializes jLabel1
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel1() {
		if (jLabel1 == null) {
			jLabel1 = new javax.swing.JLabel();
			jLabel1.setText(PluginServices.getText(this, "name") + ":");
		}

		return jLabel1;
	}

	/**
	 * This method initializes lblTitle
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLblTitle() {
		if (lblTitle == null) {
			lblTitle = new javax.swing.JLabel();
			lblTitle.setText("-");
		}

		return lblTitle;
	}

	/**
	 * This method initializes jScrollPane
	 *
	 * @return javax.swing.JScrollPane
	 */
	private javax.swing.JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new javax.swing.JScrollPane();
			jScrollPane.setViewportView(getTxtAbstract());
			jScrollPane.setPreferredSize(new java.awt.Dimension(450, 60));
			jScrollPane.setBounds(10, 47, 457, 267);
		}

		return jScrollPane;
	}

	/**
	 * This method initializes txtAbstract
	 *
	 * @return javax.swing.JTextArea
	 */
	private javax.swing.JTextArea getTxtAbstract() {
		if (txtAbstract == null) {
			txtAbstract = new javax.swing.JTextArea();
			txtAbstract.setWrapStyleWord(true);
			txtAbstract.setColumns(30);
			txtAbstract.setLineWrap(true);
		}

		return txtAbstract;
	}

	/**
	 * This method initializes panelPage1
	 *
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getPanelPage1() {
		if (panelPage1 == null) {
			panelPage1 = new javax.swing.JPanel();
			panelPage1.setLayout(null);
			panelPage1.setPreferredSize(new java.awt.Dimension(480, 220));
			panelPage1.setVisible(true);
			panelPage1.setBounds(15, 5, 480, 427);
			panelPage1.add(getJPanel1(), null);
			panelPage1.add(getJPanel(), null);
		}

		return panelPage1;
	}

	/**
	 * This method initializes btnSiguiente
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBtnSiguiente() {
		if (btnSiguiente == null) {
			btnSiguiente = new org.gvsig.gui.beans.swing.JButton();
			btnSiguiente.setPreferredSize(new java.awt.Dimension(100, 30));
			btnSiguiente.setBounds(395, 444, 100, 20);
			btnSiguiente.setText(PluginServices.getText(this, "siguiente"));
			btnSiguiente.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (page > firstPage) {
						// si page es mayor que cero, vamos enfocando las
						// sucesivas SOLAPAS del WCSParamsPanel
						wmsParamsPanel.avanzaTab();
						page = wmsParamsPanel.currentPage();
					}
					page++;
					getPanelPage1().setVisible(false);
					getPanelPage2().setVisible(true);
					activarVisualizarBotones();
				}
			});
		}
		return btnSiguiente;
	}

	/**
	 * This method initializes btnAnterior
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBtnAnterior() {
		if (btnAnterior == null) {
			btnAnterior = new org.gvsig.gui.beans.swing.JButton();
			btnAnterior.setBounds(292, 444, 100, 20);
			btnAnterior.setPreferredSize(new java.awt.Dimension(100, 30));
			btnAnterior.setText(PluginServices.getText(this, "anterior"));
			btnAnterior.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					page --;
					if (page > firstPage) {
						// Tenemos que retroceder en el wcsParamsPanel
						wmsParamsPanel.retrocedeTab();
						page = wmsParamsPanel.currentPage() + 1;
						activarVisualizarBotones();
					} else if (page==firstPage){
						activarVisualizarBotones();
						page = firstPage +1;
						//wmsParamsPanel.limpiaWizard();
						getLblTitle().setText("-");
						getTxtAbstract().setText("");
						wmsParamsPanel.retrocedeTab();
						getPanelPage1().setVisible(true);
						getPanelPage2().setVisible(false);
						getBtnSiguiente().setEnabled(false);
						connected = false;
					}
					listenerSupport.callStateChanged(wmsParamsPanel.isCorrectlyConfigured());
				}
			});
		}

		return btnAnterior;
	}

	/**
	 * This method initializes jPanel1
	 *
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJPanel1() {
		if (jPanel1 == null) {
			jPanel1 = new javax.swing.JPanel();
			jPanel1.setLayout(null);
			jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(
					null, PluginServices.getText(this, "server"),
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
			jPanel1.setBounds(2, 5, 477, 85);
			jPanel1.add(getTxtHost(), null);
			jPanel1.add(getBtnConnect(), null);
			jPanel1.add(getChkCaching(), null);
		}

		return jPanel1;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param listener
	 */
	public void addWizardListener(WizardListener listener) {
		listenerSupport.addWizardListener(listener);
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param listener
	 */
	public void removeWizardListener(WizardListener listener) {
		listenerSupport.removeWizardListener(listener);
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return
	 */
//	public WMSWizardDataSource getDataSource() {
//	return dataSource;
//	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param source
	 */
//	public void setDataSource(WMSWizardDataSource source) {
//	dataSource = source;
//	}


	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getSRS() {
		return wmsParamsPanel.getSRS();
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getFormat() {
		return wmsParamsPanel.getFormat();
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public TreePath[] getSelectedLayers() {
		return wmsParamsPanel.getSelectedLayers();
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 * @throws ProjectionLayerException
	 */
	public Rectangle2D getLayersRectangle() throws ProjectionLayerException{
		return wmsParamsPanel.getLayersRectangle();
	}



	/**
	 * Devuelve el host que estï¿½ escrito en el cuadro de texto del host o null
	 * en caso de que no haya nada escrito o estï¿½ mal escrito
	 *
	 * @return URL del host
	 */
	public URL getHost() {
		try {
			if ((cmbHost == null)|| (cmbHost.getModel().getSelectedItem() == null)) {
				return new URL(dataSource.getHost());
			} else {
				return new URL(cmbHost.getModel().getSelectedItem().toString());
			}
		} catch (MalformedURLException e) {
			return null;
		}
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return Nombre de la capa que aparece en el TOC.
	 */
	public String getLayerName() {
		return wmsParamsPanel.getLayerName();
	}


	public void initWizard() {
		dataSource = new WMSWizardData();
	}

	public void execute() {
		this.getMapCtrl().getMapContext().getLayers().addLayer(this.getLayer());

	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	protected String getLayersQuery() {
		return wmsParamsPanel.getLayersQuery();
	}

	//gets all the layers available in the WMS
	public FLayer getAllLayers(){
		return wmsParamsPanel.getAllLayers();
	}

	public FLayer getLayer(){

		return wmsParamsPanel.getLayer();
//		if(wmsParamsPanel.getDisagregatedLayers()){
//			return getLayerTree();
//		}else{
//			return getLayerPlain();
//		}
	}

//	private Object[] getFullPath(String name){
//		Vector path = new Vector();
//		WMSLayerNode layerNode;
//		WMSLayerNode parent;
//		for (int i = 0; i <wmsParamsPanel.getLstSelectedLayers().getModel().getSize(); i++) {
//			layerNode = (WMSLayerNode)wmsParamsPanel.getLstSelectedLayers().getModel().getElementAt(i);
//			if(layerNode.getName().compareTo( name ) == 0 ){
//				path.add(layerNode);
//				parent = (WMSLayerNode)layerNode.getParent().clone();
//				while ( parent != null){
//					path.add(parent);
//					parent = (WMSLayerNode)parent.getParent().clone();
//				}
//			}
//		}
//		return path.toArray();
//	}

	/**
	 * If user does not select the chekbox Disagregate layers, will get the layer tree in WMS
	 * @return
	 */
    protected FLayer getLayerTree() {
        LayerTree layerTree = wmsParamsPanel.getLayerStructure();
		TreePath[] selection = (TreePath[])wmsParamsPanel.getSelectedPaths().toArray(new TreePath[0]);//layerTree.getSelectionPaths();
		if (selection!=null){
			return getSubTree((WMSLayerNode)layerTree.getModel().getRoot(), selection);
		}
		return null;
    }

    protected boolean nodeSelected(TreePath[] selection, WMSLayerNode node){
		for (int i = 0; i < selection.length; i++) {
			Object[] components = selection[i].getPath();
			for (int j=0; j < components.length ; j++)
			{
				if (components[j] instanceof WMSLayerNode){
					if (((WMSLayerNode)components[j]).getTitle().compareTo(node.getTitle()) == 0){
						return true;
					}
				}
			}
		}
		return false;
    }

    /**
     * @deprecated
     * @param node
     * @param selection
     * @return
     */
	protected FLayer getSubTree(WMSLayerNode node, TreePath[] selection)
	{
		if (node.getChildren().size() > 0)
		{
	        //com.iver.cit.gvsig.gui.View v = (com.iver.cit.gvsig.gui.View) PluginServices.getMDIManager().getActiveView();
	        FLayers l = new FLayers();
	        l.setMapContext(this.getMapCtrl().getMapContext());
	        l.setName(node.getTitle());
			for(int i = 0; i < node.getChildren().size(); i++ ){
				if (nodeSelected(selection, (WMSLayerNode)(node.getChildren().get(i))))
				{
					FLayer lyr = getSubTree((WMSLayerNode)node.getChildren().get(i), selection);
					if(lyr != null){
						l.addLayer(lyr);
					}
				}
			}
			return l;
		}
		else{
			if (nodeSelected(selection, node))
			{
				FLyrWMS layer = new FLyrWMS();
				layer.setHost(getHost());
				try {
					layer.setFullExtent(getLayersRectangle());
				} catch (ProjectionLayerException e) {
					e.printStackTrace();
					return null;
				}
				layer.setFormat(getFormat());
				layer.setLayerQuery(node.getName());
		        layer.setInfoLayerQuery(getQueryableLayerQuery());
		        layer.setSRS(getSRS());
		        layer.setName(node.getTitle());
		        layer.setWmsTransparency(getTransparency());
		        layer.setStyles(getStyles());
		        layer.setDimensions(getDimensions());
		        layer.setDriver(getDriver());
		        layer.setOnlineResources(getOnlineResources());
		        layer.setFixedSize(getFixedSize());
		        layer.setQueryable(node.isQueryable());

		        return layer;

			}else{
				return null;
			}
		}
	}

	/**
	 * @deprecated
	 * If user selects the chekbox Disagregate layers, will get the selected layers rendered in one
	 * the tree structure in WMS will be lost.
	 * @return
	 */
    protected FLayer getLayerPlain(){
        FLyrWMS layer = new FLyrWMS();
        layer.setHost(getHost());
        try{
        	layer.setFullExtent(getLayersRectangle());
        }catch(ProjectionLayerException ex)
        {
        	ex.printStackTrace();
        	JOptionPane.showMessageDialog((Component)PluginServices.getMainFrame(), ex.getMessage());
        	return null;
        }
        layer.setFormat(getFormat());
        layer.setLayerQuery(getLayersQuery());
        layer.setInfoLayerQuery(getQueryableLayerQuery());
        layer.setSRS(getSRS());
        layer.setName(getLayerName());
        layer.setWmsTransparency(getTransparency());
        layer.setStyles(getStyles());
        layer.setDimensions(getDimensions());
        layer.setDriver(getDriver());
        layer.setOnlineResources(getOnlineResources());
        layer.setFixedSize(getFixedSize());
        layer.setQueryable(isQueryable());
        return layer;
    }

	protected boolean isQueryable() {
		return dataSource.isQueryable();
	}

	protected Dimension getFixedSize() {
		return wmsParamsPanel.getFixedSize();
	}

	private Hashtable getOnlineResources() {
		return wmsParamsPanel.getOnlineResources();
	}

	protected FMapWMSDriver getDriver() {
		return wmsParamsPanel.getDriver();
	}

	/**
	 * @return
	 */
	protected Vector getStyles() {
		return wmsParamsPanel.getStyles();
	}

	protected Vector getDimensions() {
		return wmsParamsPanel.getDimensions();
	}

	/**
	 * @return
	 */
	protected boolean getTransparency() {
		return wmsParamsPanel.getTransparency();
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getQueryableLayerQuery() {
		return wmsParamsPanel.getQueryableLayerQuery();
	}

	/**
	 * This method initializes chkCaching
	 *
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getChkCaching() {
		if (chkCaching == null) {
			chkCaching = new JCheckBox();
			chkCaching.setBounds(7, 51, 349, 20);
			chkCaching.setText(PluginServices.getText(this, "refresh_capabilities"));
			chkCaching.setToolTipText(PluginServices.getText(this, "refresh_capabilities_tooltip"));
			chkCaching.setSelected(refreshing);
			chkCaching.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					refreshing = chkCaching.isSelected();
				}
			});
			chkCaching.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					fPrefs.putBoolean("refresh_capabilities", chkCaching.isSelected());
				}
			});

		}
		return chkCaching;
	}

	/**
	 * This method initializes pnlName
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getPnlName() {
		if (pnlName == null) {
			pnlName = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
			pnlName.setBounds(new java.awt.Rectangle(9,19,452,24));
			pnlName.add(getJLabel1(), null);
			pnlName.add(getLblTitle(), null);
		}
		return pnlName;
	}

	@Override
	public DataStoreParameters[] getParameters() {
		// TODO Auto-generated method stub
		return null;
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
