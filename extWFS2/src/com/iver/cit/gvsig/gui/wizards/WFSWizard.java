package com.iver.cit.gvsig.gui.wizards;

import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.prefs.Preferences;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.crs.CRSFactory;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.serverexplorer.wfs.WFSServerExplorer;
import org.gvsig.fmap.dal.serverexplorer.wfs.WFSServerExplorerParameters;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.gui.beans.Messages;
import org.gvsig.gui.beans.panelGroup.PanelGroupManager;
import org.gvsig.gui.beans.swing.JButton;
import org.gvsig.tools.exception.BaseException;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.persistence.serverData.ServerDataPersistence;
import com.iver.cit.gvsig.gui.WizardPanel;
import com.iver.cit.gvsig.gui.panels.WFSParamsPanel;
import com.iver.cit.gvsig.panelGroup.loaders.PanelGroupLoaderFromExtensionPoint;
import com.iver.utiles.NotExistInXMLEntity;
import com.iver.utiles.XMLEntity;
import com.iver.utiles.swing.jcomboServer.JComboServer;
import com.iver.utiles.swing.jcomboServer.ServerData;

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
/* CVS MESSAGES:
 *
 * $Id: WFSWizard.java 24753 2008-11-04 15:21:32Z jpiera $
 * $Log$
 * Revision 1.24  2007-02-09 14:12:38  jorpiell
 * Soporte para WFS 1.1 y WFS-T
 *
 * Revision 1.23  2007/01/24 13:34:17  ppiqueras
 * Reencuadrado JScrollPane de descripción de conexión.
 *
 * Revision 1.22  2006/12/12 10:24:45  ppiqueras
 * Nueva funcionalidad: Pulsando doble 'click' sobre una capa de un servidor, se carga (igual que antes), pero ademÃ¡s se avanza a la siguiente pestaÃ±a sin tener que pulsar el botÃ³n 'Siguiente'.
 *
 * Revision 1.21  2006/12/11 11:00:52  ppiqueras
 * - Quitado el "Enter" asociado a los botones "Siguiente" y "Anterior"
 *
 * Revision 1.20  2006/12/05 09:22:59  ppiqueras
 * Que con la tecla "Enter" permita hacer lo mismo que pulsando con el ratÃ³n sobre los botones "Conectar", "Siguiente" y "Anterior" en WFS.
 *
 * Revision 1.19  2006/10/31 09:38:15  jorpiell
 * Se ha creado una factoria para crear la capa. De ese modo no se repite código desde le panel de propiedades y desde el panel de la capa
 *
 * Revision 1.18  2006/10/27 06:44:56  jorpiell
 * Se han cambiado algunas etiquetas de texto que salían recortadas
 *
 * Revision 1.17  2006/10/10 12:55:06  jorpiell
 * Se ha añadido el soporte de features complejas
 *
 * Revision 1.16  2006/10/02 09:09:45  jorpiell
 * Cambios del 10 copiados al head
 *
 * Revision 1.14.2.3  2006/09/29 14:12:53  luisw2
 * CRSFactory.getCRS substitutes ProjectionPool.get
 *
 * Revision 1.14.2.2  2006/09/28 08:54:01  jorpiell
 * Ya se puede reproyectar
 *
 * Revision 1.14.2.1  2006/09/27 11:12:14  jorpiell
 * Hay que comprobar que se han devuelto un número de features menor que el número máximo permitido
 *
 * Revision 1.14  2006/09/03 13:25:04  jorpiell
 * Los servidores se guardan ahora en el plugin-persistence como WFS
 *
 * Revision 1.13  2006/08/30 07:42:29  jorpiell
 * Se le asigna a la capa creada una proyección concreta. Si esto no se hace, al exportar a postigis se produce un error.
 *
 * Revision 1.12  2006/07/21 11:50:31  jaume
 * improved appearance
 *
 * Revision 1.11  2006/07/11 11:55:41  jorpiell
 * Se ha añadido el fallo de tipo de dato en el log
 *
 * Revision 1.10  2006/06/15 11:17:06  jorpiell
 * Se ha encontrado la forma de comprobar cuando se prodicia un error de parseo al hacer un hasnext (en la feature). Se atrapa y se lanza la excepción hacia arriba
 *
 * Revision 1.9  2006/06/14 08:46:24  jorpiell
 * Se tiene en cuanta la opcion para refrescar las capabilities
 *
 * Revision 1.8  2006/05/25 16:22:59  jorpiell
 * Se limpia el panel cada vez que se conecta con un servidor distinto
 *
 * Revision 1.7  2006/05/25 16:01:51  jorpiell
 * Se ha añadido la funcionalidad para eliminar el namespace de los tipos de atributos
 *
 * Revision 1.6  2006/05/25 10:29:07  jorpiell
 * Añadido el checkbox para que se  haga uso de la cache
 *
 * Revision 1.5  2006/05/24 12:08:53  jorpiell
 * Cambiado el nombre de la pestaña a WFS
 *
 * Revision 1.4  2006/05/23 13:21:59  jorpiell
 * Si hay algún problema en la carga se muestra un mensaje de error
 *
 * Revision 1.3  2006/05/23 08:09:53  jorpiell
 * Se ha cambiado la forma en la que se leian los valores seleccionados en los paneles y se ha cambiado el comportamiento de los botones
 *
 * Revision 1.2  2006/05/19 12:58:03  jorpiell
 * Modificados algunos paneles
 *
 * Revision 1.1  2006/04/19 12:50:16  jorpiell
 * Primer commit de la aplicación. Se puede hacer un getCapabilities y ver el mensaje de vienvenida del servidor
 *
 *
 */

/**
 * <p>Wizard that allows add a new WFS layer.</p>
 * 
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es)
 */
public class WFSWizard extends WizardPanel {
	private int page;
	private boolean connected = false;
	private JComboServer cmbHost = null;
	private JButton btnConnect = null;
	private javax.swing.JPanel jPanel = null;
	private javax.swing.JLabel jLabel1 = null;
	private javax.swing.JLabel lblTitle = null;
	private javax.swing.JScrollPane jScrollPane = null;
	private javax.swing.JTextArea txtAbstract = null;
	private javax.swing.JPanel panelPage1 = null;
	private javax.swing.JPanel pnlName = null;
	private JButton btnSiguiente = null;
	private JButton btnAnterior = null;
	private javax.swing.JPanel jPanel1 = null;
	private WFSParamsPanel wfsParamsPanel = null;
	private WFSServerExplorer serverExplorer = null;
	private JLabel lblServerType = null;
	private JLabel lblServerTypeValue = null;
	private JCheckBox chkCaching = null;
	private static Preferences fPrefs = Preferences.userRoot().node( "gvsig.wfs-wizard" );
	private boolean refreshing = fPrefs.getBoolean("refresh_capabilities", false);
	private final String wfs_properties_extensionpoint_name = "WFSPropertiesDialog";

	/**
	 * This is the default constructor
	 */
	public WFSWizard() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		setTabName(PluginServices.getText(this, "WFS"));

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

		this.setLayout(null);
//		this.setPreferredSize(new java.awt.Dimension(750, 320));
//		this.setPreferredSize(new Dimension(750, 320));
		this.setVisible(true);
//		this.setSize(518, 468);

		this.add(getPanelPage1(), null);
		page = 0;
		this.add(getPanelPage2(), null);
	
		this.add(getBtnAnterior(), null);
		this.add(getBtnSiguiente(), null);

		this.add(lblServerType, null);
		this.add(lblServerTypeValue, null);

		uptateNavigatingButtons();
	}

	/**
	 * Adds the host addres to a persistent data storage.
	 *
	 * @param host the host of the server
	 */
	private void addHost(String host) {
		host = host.trim();
		ServerDataPersistence persistence = new ServerDataPersistence(this,ServerData.SERVER_TYPE_WFS);
		persistence.addServerData(new ServerData(host, ServerData.SERVER_TYPE_WFS));
	}

	/**
	 * Fills up the initial WCSWizard controls.
	 *
	 * jaume
	 */
	private void rellenarControles() throws Exception{
		try {
			String url = cmbHost.getModel().getSelectedItem().toString();
						
			DataManager dataManager = DALLocator.getDataManager();
			//Create the server explorer parameters
			WFSServerExplorerParameters parameters = 
				(WFSServerExplorerParameters) dataManager
					.createServerExplorerParameters(WFSServerExplorer.NAME);			
			parameters.setUrl(url);
			addHost(url);
			//Create the server explorer
			serverExplorer = (WFSServerExplorer)dataManager.createServerExplorer(parameters);
			
			//Update the forms
			getLblTitle().setText(serverExplorer.getTitle());
			getTxtAbstract().setText(serverExplorer.getAbstract());
			lblServerTypeValue.setText(serverExplorer.getServerType());
			wfsParamsPanel.setServerExplorer(serverExplorer);
			connected = true;
		} catch(Exception e) {				
			throw e;
		}
	}

	/**
	 * Enables or disables the Next and Previous buttons according with the current
	 * page and the enabled pages.
	 */
	private void uptateNavigatingButtons() {
		if (page == 0) {
			getBtnAnterior().setEnabled(false);
			getBtnSiguiente().setVisible(true);
			getBtnSiguiente().setEnabled(false);
		}
		else {
			short tabIndex = (short) wfsParamsPanel.getSelectedIndex();
			
			getBtnAnterior().setEnabled(true);
			if (wfsParamsPanel.nextPageEnabled() == -1) {
			    getBtnSiguiente().setEnabled(false);
			    
			    if (tabIndex == (wfsParamsPanel.getPanelInGUICount() -1))
					getBtnSiguiente().setVisible(false);
			}
			else {
				getBtnSiguiente().setVisible(true);
				getBtnSiguiente().setEnabled(true);
			}
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

			ServerDataPersistence persistence = new ServerDataPersistence(this,ServerData.SERVER_TYPE_WFS);
			ServerData[] servers = persistence.getArrayOfServerData();

			if (servers.length == 0){
				PluginServices ps = PluginServices.getPluginServices(this);
				XMLEntity xml = ps.getPersistentXML();
				try {
					String[] oldServers = xml.getStringArrayProperty("wfs-servers");
					servers = new ServerData[oldServers.length];
					for (int i=0; i<oldServers.length; i++) {
						servers[i] = new ServerData(oldServers[i],ServerData.SERVER_TYPE_WMS);
					}
					xml.remove("wfs-servers");
					ps.setPersistentXML(xml);
				} catch (NotExistInXMLEntity e) {
					// Old servers doesn't exist
					servers = new ServerData[2];
					servers[0] = new ServerData("http://www2.dmsolutions.ca/cgi-bin/mswfs_gmap",ServerData.SERVER_TYPE_WFS);
					servers[1] = new ServerData("http://www.idee.es/IDEE-WFS/ogcwebservice",ServerData.SERVER_TYPE_WFS);
				}

				for (int i = 0; i < servers.length; i++){
					persistence.addServerData(servers[i]);
				}
				persistence.setPersistent();
			}

			cmbHost.setServerList(servers);

			// Allows that user can start the connection to the written server pressing the 'Enter' key
			cmbHost.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
				/*
				 * (non-Javadoc)
				 * @see java.awt.event.KeyAdapter#keyReleased(java.awt.event.KeyEvent)
				 */
				public void keyReleased(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						getBtnConnect().doClick();
					}
				}
			});
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
					btnConnect.setEnabled(false); // This avoids multiple contiguous actions

					try {
						rellenarControles();
						wfsParamsPanel.refreshCapabilitiesInfo();
						wfsParamsPanel.refreshWFSSelectFeaturePanel();
						wfsParamsPanel.enableDefaultTabs(false);
						btnConnect.setEnabled(true); // This avoids multiple contiguous actions
						
						getBtnSiguiente().setEnabled(true);
						getBtnSiguiente().requestFocus(); // Sets focus to the 'next' button
					}
					catch(Exception e1) {
						JOptionPane.showMessageDialog(null, 
								Messages.getText("invalid_url"), 
								Messages.getText("warning"),
								JOptionPane.WARNING_MESSAGE);
						btnConnect.setEnabled(true); 
					}
				}
			});

			// If user press the 'Enter' key -> advance
			btnConnect.addKeyListener(new KeyAdapter() {
				/*
				 *  (non-Javadoc)
				 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
				 */
				public void keyReleased(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						btnConnect.doClick();
					}
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
			jPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
					PluginServices.getText(this, "descripcion"),
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null,
					null));
			jPanel.setBounds(2, 96, 477, 324);
			jPanel.add(getJScrollPane(), null);
			jPanel.add(getPnlName(), null);
		}

		return jPanel;
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

	/**
	 * This method initializes jLabel1
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel1() {
		if (jLabel1 == null) {
			jLabel1 = new javax.swing.JLabel();
			jLabel1.setText(PluginServices.getText(this, "nombre") + ":");
			jLabel1.setBounds(15, 26, 65, 15);
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
			lblTitle.setPreferredSize(new java.awt.Dimension(350, 16));
			lblTitle.setBounds(82, 26, 350, 16);
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
	 * This method initializes panelPage2
	 *
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getPanelPage2() {
		if (wfsParamsPanel == null) {

			try {
				PanelGroupManager manager = PanelGroupManager.getManager();
				manager.registerPanelGroup(WFSParamsPanel.class);
				manager.setDefaultType(WFSParamsPanel.class);

				// Initially there is no layer
				wfsParamsPanel = (WFSParamsPanel) manager.getPanelGroup(null);
				wfsParamsPanel.setBounds(4, 9, 502, 423);
				wfsParamsPanel.loadPanels(new PanelGroupLoaderFromExtensionPoint(wfs_properties_extensionpoint_name));
				wfsParamsPanel.addChangeListener(new ChangeListener() {
			        // This method is called whenever the selected tab changes
			        public void stateChanged(ChangeEvent evt) {
			            JTabbedPane pane = (JTabbedPane)evt.getSource();

			            // Update the navigation buttons
			            uptateNavigatingButtons();
			        }
			    });
			} catch (BaseException bE) {
				NotificationManager.showMessageError(bE.getLocalizedMessageStack(), bE);
			} catch (Exception e) {
				NotificationManager.showMessageError(e.getLocalizedMessage(), e);
			}
		}

		return wfsParamsPanel;
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
					if (page == 0) {
						page = 1;
						getPanelPage1().setVisible(false);
						getPanelPage2().setVisible(true);
					}
					else {
						wfsParamsPanel.goToNextTab();
					}

					uptateNavigatingButtons();
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
			btnAnterior.setPreferredSize(new java.awt.Dimension(100, 30));
			btnAnterior.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					int tabIndex = wfsParamsPanel.getSelectedIndex();
					
					if (page == 1) {
						if (tabIndex == 0) {
							page = 0;
							connected = false;

							wfsParamsPanel.enableDefaultTabs(false);
							getLblTitle().setText("-");
							getTxtAbstract().setText("");


							getPanelPage2().setVisible(false);
							getPanelPage1().setVisible(true);
						}
						else {
							wfsParamsPanel.goToPreviousTab();	
						}
					}
					else {
						
					}
					
					uptateNavigatingButtons();
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
	 * Returns the wizard's data source
	 *
	 * @return the wizard's data source
	 */
	public WFSServerExplorer getServerExplorer() {
		return serverExplorer;
	}


	/**
	 * Returns the rectangle that contains all the others
	 *
	 * @param rects[] rectangles with its positions and dimensions
	 *
	 * @return Rectangle2D the rectangle that contains all the others
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
	 * @return URL del host
	 */
	public URL getHost() {
		try {
			return new URL(cmbHost.getModel().getSelectedItem().toString());
		} catch (MalformedURLException e) {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.gui.WizardPanel#initWizard()
	 */
	public void initWizard() {
		//setServerExplorer(new WFSWizardData());
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.gui.WizardPanel#execute()
	 */
	public void execute() {
		FLayer layer = wfsParamsPanel.getLayer();
		IProjection projection = wfsParamsPanel.getSelectedFeatureProjection();
		if (projection == null){
			projection = getMapCtrl().getProjection();
		}
		layer.setProjection(projection);
		MapContext mapContext = this.getMapCtrl().getMapContext();
		mapContext.getLayers().addLayer(layer);
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
					chkCaching.setEnabled(false); // This avoids multiple contiguous actions
					fPrefs.putBoolean("refresh_capabilities", chkCaching.isSelected());
					chkCaching.setEnabled(true); // This avoids multiple contiguous actions
				}
			});

		}
		return chkCaching;
	}

	/**
	 * This method makes the 'btnSiguiente' to be clicked
	 */
	public void doClickOnNextButton() {
		getBtnSiguiente().doClick();
	}

	/* (non-Javadoc)
	 * @see com.iver.cit.gvsig.gui.WizardPanel#getParameters()
	 */
	public DataStoreParameters[] getParameters() {
		// TODO Auto-generated method stub
		return null;
	}
}
