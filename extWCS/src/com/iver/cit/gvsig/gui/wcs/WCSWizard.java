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
package com.iver.cit.gvsig.gui.wcs;

import java.awt.FlowLayout;
import java.awt.geom.Rectangle2D;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.prefs.Preferences;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.gui.beans.swing.JButton;

import com.iver.andami.PluginServices;
import com.iver.andami.persistence.serverData.ServerDataPersistence;
import com.iver.cit.gvsig.fmap.layers.FLyrWCS;
import com.iver.cit.gvsig.gui.WizardPanel;
import com.iver.cit.gvsig.gui.panels.WCSParamsPanel;
import com.iver.cit.gvsig.gui.wizards.WizardListener;
import com.iver.cit.gvsig.gui.wizards.WizardListenerSupport;
import com.iver.utiles.NotExistInXMLEntity;
import com.iver.utiles.XMLEntity;
import com.iver.utiles.swing.jcomboServer.JComboServer;
import com.iver.utiles.swing.jcomboServer.ServerData;

/**
 * Class implementing the WCSWizard.
 *
 * Clase que implementa el Wizard para coberturas WCS
 * @author Jaume Domínguez Faus
 */
public class WCSWizard extends WizardPanel {
	private int page = 0;
	private boolean connected = false;
	private JComboServer cmbHost = null;
	private JButton btnConnect = null;
	private JPanel jPanel = null;
	private JLabel jLabel1 = null;
	private JLabel lblTitle = null;
	private JScrollPane jScrollPane = null;
	private JTextArea txtAbstract = null;
	private JPanel panelPage1 = null;
	private JButton btnSiguiente = null;
	private JButton btnAnterior = null;
	private JPanel jPanel1 = null;
	private WizardListenerSupport listenerSupport = new WizardListenerSupport();
	private WCSParamsPanel wcsParamsPanel = null;
	private WCSWizardData dataSource = null;
	private JPanel pnlName;
	private static Preferences fPrefs = Preferences.userRoot().node( "gvsig.wcs-wizard" );
	private boolean refreshing = fPrefs.getBoolean("refresh_capabilities", false);
	private JCheckBox chkCaching;


	/**
	 * This is the default constructor
	 */
	public WCSWizard() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		setTabName("WCS");
		this.setLayout(null);
		this.setPreferredSize(new java.awt.Dimension(750, 320));
		this.setVisible(true);
		this.setSize(510, 468);
		this.add(getPanelPage1(), null);
		this.add(getPanelPage2(), null);
		this.add(getBtnAnterior(), null);
		this.add(getBtnSiguiente(), null);
		activarVisualizarBotones();
	}

	/**
	 * Adds the host addres to a persistent data storage.
	 *
	 * con addHost guardamos la información del combo de servidores en un
	 * almacén persistente
	 *
	 * @param host
	 */
	private void addHost(String host) {
		host = host.trim();

		ServerDataPersistence persistence = new ServerDataPersistence(this,ServerData.SERVER_TYPE_WCS);
		persistence.addServerData(new ServerData(host, ServerData.SERVER_TYPE_WCS));
		persistence.setPersistent();
	}
	/**
	 * Fills up the initial WCSWizard controls.
	 *
	 * Rellena los primeros controles del WCS Wizard
	 *
	 * jaume
	 */
	private void fillupComponents() {
		try {
			String host = cmbHost.getModel().getSelectedItem().toString();

			dataSource.setHost(new URL(host), refreshing);

			addHost(host);
			getLblTitle().setText(dataSource.getTitle());
			getTxtAbstract().setText(dataSource.getDescription());
//			wcsParamsPanel.getLstCoverages().setListData(
//					dataSource.getCoverageList());
			wcsParamsPanel.setWizardData(dataSource);

			connected = true;
			activarVisualizarBotones();
		} catch (Exception e) {
			listenerSupport.callError(e);
			e.printStackTrace();
		}
	}

	/**
	 * Enables or disables the Next and Previous buttons according with the current
	 * page and the enabled pages.
	 */
	private void activarVisualizarBotones() {
		if (page == 0) {
			getBtnAnterior().setEnabled(false);
			getBtnSiguiente().setVisible(true);
			if (connected) {
				getBtnSiguiente().setEnabled(true);
			} else {
				getBtnSiguiente().setEnabled(false);
			}
		} else if (page < wcsParamsPanel.getNumTabs()) {
			getBtnSiguiente().setEnabled(true);
			getBtnSiguiente().setVisible(true);
			getBtnAnterior().setEnabled(true);
			getBtnAnterior().setVisible(true);
			if (wcsParamsPanel.nextPageEnabled()==-1){
			    getBtnSiguiente().setEnabled(false);
			}
			listenerSupport.callStateChanged(wcsParamsPanel.isCorretlyConfigured());
		} else if (page == wcsParamsPanel.getNumTabs()) {
			getBtnSiguiente().setVisible(false);
			getBtnSiguiente().setEnabled(false);
			listenerSupport.callStateChanged(wcsParamsPanel
					.isCorretlyConfigured());
		}

	}



	/**
	 * This method initializes txtHost from the persistent data storage
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

			ServerDataPersistence persistence = new ServerDataPersistence(this,ServerData.SERVER_TYPE_WCS);
			ServerData[] servers = persistence.getArrayOfServerData();

			if (servers.length == 0){
				PluginServices ps = PluginServices.getPluginServices(this);
				XMLEntity xml = ps.getPersistentXML();
				try {
					String[] oldServers = xml.getStringArrayProperty("wcs-servers");
					servers = new ServerData[oldServers.length];
					for (int i=0; i<oldServers.length; i++) {
						servers[i] = new ServerData(oldServers[i],ServerData.SERVER_TYPE_WCS);
					}
					xml.remove("wcs-servers");
					ps.setPersistentXML(xml);
				} catch (NotExistInXMLEntity e) {
					// Old servers doesn't exist
					servers = new ServerData[2];
					servers[0] = new ServerData("http://maps.gdal.org/cgi-bin/mapserv_dem",ServerData.SERVER_TYPE_WCS);
					servers[1] = new ServerData("http://inspire.cop.gva.es/mapserver/wcs",ServerData.SERVER_TYPE_WCS);
				}

				for (int i=0 ; i<servers.length ; i++){
					persistence.addServerData(servers[i]);
				}
				persistence.setPersistent();
			}

			cmbHost.setServerList(servers);
		}
		return cmbHost;
}

	/**
	 * This method initializes btnDetalles
	 *
	 * @return JButton
	 */
	private JButton getBtnConnect() {
		if (btnConnect == null) {
			btnConnect = new JButton();
			btnConnect.setPreferredSize(new java.awt.Dimension(100, 20));
			btnConnect.setBounds(366, 50, 100, 20);
			btnConnect.setText(PluginServices.getText(this, "connect"));
			btnConnect.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					wcsParamsPanel.cleanupWizard();
					connected = false;
					fillupComponents();
				}
			});
		}

		return btnConnect;
	}

	/**
	 * This method initializes jPanel
	 *
	 * @return JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new javax.swing.JPanel();
			jPanel.setLayout(null);
			jPanel.setPreferredSize(new java.awt.Dimension(470, 130));
			jPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
					PluginServices.getText(this, "descripcion"),
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null,
					null));
			jPanel.setBounds(2, 96, 477, 324);
			jPanel.add(getPnlName(), null);
			jPanel.add(getJScrollPane(), null);
		}

		return jPanel;
	}

	/**
	 * This method initializes jLabel1
	 *
	 * @return JLabel
	 */
	private JLabel getJLabel1() {
		if (jLabel1 == null) {
			jLabel1 = new javax.swing.JLabel();
			jLabel1.setText(PluginServices.getText(this, "nombre") + ":");
			jLabel1.setBounds(37, 26, 40, 15);
		}

		return jLabel1;
	}

	/**
	 * This method initializes lblTitle
	 *
	 * @return JLabel
	 */
	private JLabel getLblTitle() {
		if (lblTitle == null) {
			lblTitle = new javax.swing.JLabel();
			lblTitle.setText("-");
			lblTitle.setPreferredSize(new java.awt.Dimension(350, 16));
			lblTitle.setBounds(82, 26, 350, 16);
		}

		return lblTitle;
	}

	/**
	 * This method initializes jScrollPane
	 *
	 * @return JScrollPane
	 */
	private JScrollPane getJScrollPane() {
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
	 * @return JTextArea
	 */
	private JTextArea getTxtAbstract() {
		if (txtAbstract == null) {
			txtAbstract = new javax.swing.JTextArea();
			txtAbstract.setWrapStyleWord(true);
			txtAbstract.setColumns(30);
			txtAbstract.setLineWrap(true);
		}

		return txtAbstract;
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
	 * This method initializes panelPage1
	 *
	 * @return JPanel
	 */
	private JPanel getPanelPage1() {
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
	 * This method initializes panelPage2
	 *
	 * @return JPanel
	 */
	private JPanel getPanelPage2() {
		if (wcsParamsPanel == null) {
			wcsParamsPanel = new WCSParamsPanel();
			wcsParamsPanel.setListenerSupport(this.listenerSupport);
			wcsParamsPanel.setBounds(0, 5, 510, 428);
		}

		return wcsParamsPanel;
	}

	/**
	 * This method initializes pnlName
	 *
	 * @return JPanel
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



	/**
	 * This method initializes btnSiguiente
	 *
	 * @return JButton
	 */
	private JButton getBtnSiguiente() {
		if (btnSiguiente == null) {
			btnSiguiente = new JButton();
			btnSiguiente.setPreferredSize(new java.awt.Dimension(100, 30));
			btnSiguiente.setBounds(395, 444, 100, 20);
			btnSiguiente.setText(PluginServices.getText(this, "siguiente"));
			btnSiguiente.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (page > 0) {
						// si page es mayor que cero, vamos enfocando las
						// sucesivas SOLAPAS del WCSParamsPanel
						wcsParamsPanel.avanzaTab();
						page = wcsParamsPanel.getIndiceSolapaActual();
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
	 * @return JButton
	 */
	private JButton getBtnAnterior() {
		if (btnAnterior == null) {
			btnAnterior = new JButton();
			btnAnterior.setBounds(292, 444, 100, 20);
			btnAnterior.setText(PluginServices.getText(this, "anterior"));
			btnAnterior.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					page --;
					if (page > 0) {
						// Tenemos que retroceder en el wcsParamsPanel
						wcsParamsPanel.retrocedeTab();
						page = wcsParamsPanel.getIndiceSolapaActual() + 1;
						activarVisualizarBotones();
					} else if (page==0){
						activarVisualizarBotones();
						page = 1;
						wcsParamsPanel.cleanupWizard();
						getLblTitle().setText("-");
						getTxtAbstract().setText("");
						wcsParamsPanel.retrocedeTab();
						getPanelPage1().setVisible(true);
						getPanelPage2().setVisible(false);
						getBtnSiguiente().setEnabled(false);
						connected = false;
					}
					listenerSupport.callStateChanged(wcsParamsPanel.isCorretlyConfigured());
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
	private JPanel getJPanel1() {
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
	 * Adds the gvSIG's wizard listener
	 *
	 * @param listener
	 */
	public void addWizardListener(WizardListener listener) {
		listenerSupport.addWizardListener(listener);
	}

	/**
	 * Removes the gvSIG's wizard listener
	 *
	 * @param listener
	 */
	public void removeWizardListener(WizardListener listener) {
		listenerSupport.removeWizardListener(listener);
	}

	/**
	 * Returns the Wizard's data source
	 *
	 * @return
	 */
	public WCSWizardData getDataSource() {
		return dataSource;
	}

	/**
	 * sets the wizard's data source
	 *
	 * @param source
	 */
	public void setDataSource(WCSWizardData source) {
		dataSource = source;
	}

	/**
	 * Returns the rectangle that contains all the others
	 *
	 * Obtiene el rectángulo que contiene a todos los otros
	 *
	 * @param rects[]  rectangulos
	 *
	 * @return Rectangle2D
	 */
	public static Rectangle2D getRectangle(Rectangle2D[] rects) {
		Rectangle2D ret = rects[0];

		for (int i = 1; i < rects.length; i++) {
			ret.add(rects[i]);
		}

		return ret;
	}

	/**
	 * Returns the host typed in the host text field or null if nothing is typed
	 * or it is not a valid URL.
	 *
	 * Devuelve el host que está escrito en el cuadro de texto del host o null
	 * en caso de que no haya nada escrito o esté mal escrito
	 *
	 * @return URL del host
	 */
	public URL getHost() {
		try {
			return new URL(cmbHost.getModel().getSelectedItem().toString());
		} catch (MalformedURLException e) {
			return null;
		}
	}

	/**
	 * Returns the name of the coverage that appears in the TOC
	 *
	 * @return String
	 */
	public String getLayerName() {
		return wcsParamsPanel.getLayerName();
	}

	public void initWizard() {
		setDataSource(new WCSWizardData());
	}

	public void execute() {
		this.getMapCtrl().getMapContext().getLayers().addLayer(this.getLayer());
	}

	/**
	 * Creates a new layer to be passed to gvSIG. This method is automatically called
	 * by gvSIG and it must ensure that a layer is fully and correctly created or then
	 * return null.
	 */
	public FLayer getLayer() {
		FLyrWCS layer = new FLyrWCS();
		layer.setHost(this.getHost().toString());
		layer.setCoverageName(wcsParamsPanel.getCurrentCoverageName());
		layer.setSRS(wcsParamsPanel.getSRS());
		layer.setFormat(wcsParamsPanel.getFormat());
		layer.setFullExtent(wcsParamsPanel.getExtent());
		layer.setDriver(wcsParamsPanel.getDriver());
		layer.setTime(wcsParamsPanel.getTime());
		layer.setParameter(wcsParamsPanel.getParameterString());
		layer.setName(wcsParamsPanel.getCoverageName());
		layer.setMaxResolution(wcsParamsPanel.getMaxRes());

		return layer;
	}

	@Override
	public DataStoreParameters[] getParameters() {
		// TODO Auto-generated method stub
		return null;
	}

}

