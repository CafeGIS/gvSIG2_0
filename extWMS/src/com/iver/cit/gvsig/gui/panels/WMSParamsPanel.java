/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
 *   Av. Blasco Ib��ez, 50
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
package com.iver.cit.gvsig.gui.panels;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.TreeSet;
import java.util.Vector;
import java.util.prefs.Preferences;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.tree.TreePath;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.gvsig.fmap.crs.CRSFactory;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.mapcontext.exceptions.ProjectionLayerException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.gui.beans.controls.dnd.JDnDListModel;
import org.gvsig.gui.beans.listeners.BeanListener;
import org.gvsig.gui.beans.swing.JButton;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.fmap.drivers.wms.FMapWMSDriver;
import com.iver.cit.gvsig.fmap.layers.FLyrWMS;
import com.iver.cit.gvsig.fmap.layers.IFMapWMSDimension;
import com.iver.cit.gvsig.fmap.layers.WMSLayerNode;
import com.iver.cit.gvsig.fmap.layers.WMSLayerNode.FMapWMSStyle;
import com.iver.cit.gvsig.gui.WizardPanel;
import com.iver.cit.gvsig.gui.wizards.FormatListModel;
import com.iver.cit.gvsig.gui.wizards.LayerTreeModel;
import com.iver.cit.gvsig.gui.wizards.SRSListModel;
import com.iver.cit.gvsig.gui.wizards.WMSWizardData;
import com.iver.cit.gvsig.gui.wizards.WizardListenerSupport;

/**
 * This class implements the map option panel.
 *
 * It includes a set of Listeners that implement some control rules which
 * refresh the component values that depends on those selected in the other
 * components to avoid to choose an invalid set of options. It also includes a
 * method (isCorrectlyConfigured()) that checks if the current set of values is
 * enough to correctly launch a GetMap request.
 *
 * The information is obtained from a WMSWizardDataSource object.
 *
 *
 * Esta clase implementa el panel de opciones disponibles sobre el mapa
 *
 * Incluye una serie de Listeners que implementan unas reglas de control que
 * refrescan los valores de componentes cuyos valores dependen de aqu�llos
 * seleccionados en otros componentes para evitar que se escoja una combinaci�n
 * de opciones err�nea as� como una funci�n (isCorrectlyConfigured()) que
 * comprueba si la combinaci�n escogida actualmente es suficiente para lanzar
 * una operaci�n GetMap correctamente.
 *
 * La informaci�n obtiene a partir de un objeto WMSWizardDataSource.
 *
 * @author jaume - jaume dominguez faus
 *
 */
public class WMSParamsPanel extends WizardPanel {
	private JScrollPane jScrollPane1 = null;
	private JScrollPane jScrollPane2 = null;
	private JScrollPane jScrollPane5 = null;
	private JButton btnAdd = null;
	private JButton btnDel = null;
	private JScrollPane jScrollPane6 = null;
	private JPanel jPanel2 = null;
	private JPanel tabFormats = null;
	private JPanel jPanel4 = null;
	private JTextField jTxtNomCapa = null;
	private LayerList lstSelectedLayers = null;
	private ArrayList selectedPaths = new ArrayList();
	private JList lstSRSs = null;
	private LayerTree treeLayers = null;
	private JList lstFormats = null;
	private WizardListenerSupport listenerSupport;
	public WMSWizardData dataSource;
	private JTabbedPane jTabbedPane = null;
	private JPanel tabLayers = null;
	private JPanel jPanel1 = null;
	private JPanel jPanel5 = null;
	private StylesPanel tabStyle = null;
	private JCheckBox chkTransparency = null;
	private InfoPanel tabInfo = null;
	private DimensionPanel tabDimensions = null;
	private int dimensionTabIndex;
	private int stylesTabIndex;
	private String oldSRS = null;
	private AbstractCollection oldDimensions = null;
	private Dimension sizeFixed;
	private JCheckBox chkExtendedNames = null;
	private JButton btnUp = null;
	private JButton btnDown = null;

	public static Preferences fPrefs = Preferences.userRoot().node(
			"gvsig.wms-wizard");

	private JCheckBox chkDisagregate = null;

	public WMSParamsPanel() {
		super();
		initialize();
	}

	// TODO: crear nuevo constructor para a�adir los panels que quieras...
	public WMSParamsPanel(WMSWizardData data) {
		super();
		setWizardData(data);
		this.setLayout(null);
		this.setVisible(false);
		this.setBounds(0, 0, 510, 427);
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();
			jTabbedPane.setBounds(4, 4, 502, 415);
			StylesPanel sp = getTabStyle();
			DimensionPanel dp = getTabDimensions();

			jTabbedPane.addTab(PluginServices.getText(this, "capas"), null,
					getTabLayers(), null);
			jTabbedPane.addTab(PluginServices.getText(this, "estilos"), null,
					sp, null);
			stylesTabIndex = jTabbedPane.getTabCount() - 1;
			jTabbedPane.addTab(PluginServices.getText(this, "dimensiones"),
					null, dp, null);
			dimensionTabIndex = jTabbedPane.getTabCount() - 1;
			jTabbedPane.addTab(PluginServices.getText(this, "formatos"), null,
					getTabFormats(), null);
			jTabbedPane.setEnabledAt(dimensionTabIndex, false);
			jTabbedPane.setEnabledAt(stylesTabIndex, false);
		}
		this.add(jTabbedPane, null);

		setListenerSupport(new WizardListenerSupport());
	}

	/**
	 * This method initializes panelPage2
	 *
	 * @return Panel
	 */
	private void initialize() {
		this.setLayout(null);
		this.setVisible(false);
		this.setBounds(0, 0, 510, 427);
		this.add(getJTabbedPane(), null);
	}

	/**
	 * This method initializes jPanel2
	 *
	 * @return Panel
	 */
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			jPanel2 = new JPanel();
			jPanel2.setLayout(null);
			jPanel2.setBounds(5, 59, 485, 328);
			jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(
					null, PluginServices.getText(this, "select_layers"),
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null,
					null));
			jPanel2.add(getChkExtendedNames(), null);

			jPanel2.add(getJScrollPane1(), null);
			jPanel2.add(getBtnDel(), null);
			jPanel2.add(getBtnAdd(), null);
			jPanel2.add(getJScrollPane5(), null);
			jPanel2.add(getBtnUp(), null);
			jPanel2.add(getBtnDown(), null);
			jPanel2.add(getChkDisagregate(), null);
		}

		return jPanel2;
	}

	/**
	 * This method initializes jPanel4
	 *
	 * @return Panel
	 */
	private JPanel getJPanel4() {
		if (jPanel4 == null) {
			jPanel4 = new JPanel();
			jPanel4.setLayout(null);
			jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(
					null, PluginServices.getText(this, "layer_name"),
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null,
					null));
			jPanel4.setBounds(5, 8, 485, 49);
			jPanel4.add(getJTxtNomCapa(), null);
		}

		return jPanel4;
	}

	/**
	 * This method initializes jPanel3
	 *
	 * @return Panel
	 */
	private JPanel getTabFormats() {
		if (tabFormats == null) {
			tabFormats = new JPanel();
			tabFormats.setLayout(null);
			tabFormats.add(getJPanel1(), null);
			tabFormats.add(getJPanel5(), null);
		}

		return tabFormats;
	}

	/**
	 * This method initializes jScrollPane1
	 *
	 * @return ScrollPane
	 */
	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setBounds(11, 22, 460, 123);
			jScrollPane1.setViewportView(getTreeLayers());
			jScrollPane1.setPreferredSize(new java.awt.Dimension(205, 75));
		}

		return jScrollPane1;
	}

	/**
	 * This method initializes btnDel
	 *
	 * @return Button
	 */
	private JButton getBtnDel() {
		if (btnDel == null) {
			btnDel = new JButton();
			// btnDel.setPreferredSize(new java.awt.Dimension(23, 23));
			btnDel.setText(PluginServices.getText(this, "remove"));
			btnDel.setMargin(new java.awt.Insets(2, 0, 2, 0));
			btnDel.setBounds(339, 300, 101, 20);
			btnDel.setEnabled(false);
			btnDel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					delLayer();
				}
			});
		}
		return btnDel;
	}

	/**
	 * This method initializes btnAdd
	 *
	 * @return Button
	 */
	private JButton getBtnAdd() {
		if (btnAdd == null) {
			btnAdd = new JButton();
			// btnAdd.setPreferredSize(new java.awt.Dimension(23, 23));
			btnAdd.setText(PluginServices.getText(this, "add"));
			btnAdd.setMargin(new java.awt.Insets(2, 0, 2, 0));
			btnAdd.setBounds(236, 300, 101, 20);
			btnAdd.setEnabled(false);
			btnAdd.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					addLayer();
				}
			});
		}
		return btnAdd;
	}

	/**
	 * This method initializes jScrollPane5
	 *
	 * @return ScrollPane
	 */
	private JScrollPane getJScrollPane5() {
		if (jScrollPane5 == null) {
			jScrollPane5 = new JScrollPane();
			jScrollPane5.setBounds(11, 155, 422, 120);
			jScrollPane5.setViewportView(getLstSelectedLayers());
			jScrollPane5.setPreferredSize(new java.awt.Dimension(205, 75));
		}

		return jScrollPane5;
	}

	/**
	 * This method initializes jTextField
	 *
	 * @return TextField
	 */
	private JTextField getJTxtNomCapa() {
		if (jTxtNomCapa == null) {
			jTxtNomCapa = new JTextField();
			jTxtNomCapa.setBounds(10, 19, 460, 20);
			jTxtNomCapa.setText(PluginServices.getText(this, "WMSLayer"));
			jTxtNomCapa.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent e) {
					fireWizardComplete(isCorrectlyConfigured());
				}
			});
		}

		return jTxtNomCapa;
	}

	/**
	 * This method initializes jScrollPane6
	 *
	 * @return ScrollPane
	 */
	private JScrollPane getJScrollPane6() {
		if (jScrollPane6 == null) {
			jScrollPane6 = new JScrollPane();
			jScrollPane6.setBounds(5, 23, 470, 140);
			jScrollPane6.setViewportView(getLstFormats());
			jScrollPane6.setPreferredSize(new java.awt.Dimension(250, 200));
		}

		return jScrollPane6;
	}

	/**
	 * This method initializes jScrollPane2
	 *
	 * @return ScrollPane
	 */
	private JScrollPane getJScrollPane2() {
		if (jScrollPane2 == null) {
			jScrollPane2 = new JScrollPane();
			jScrollPane2.setBounds(5, 23, 470, 140);
			jScrollPane2.setViewportView(getLstSRSs());
			jScrollPane2.setPreferredSize(new java.awt.Dimension(100, 200));
		}

		return jScrollPane2;
	}

	/**
	 * Gets the available layers in WMS maintaining the tree structure
	 *
	 * @return
	 */
	public LayerTree getLayerStructure() {
		return treeLayers;
	}

	/**
	 * This method initializes treeLayers
	 *
	 * @return Tree
	 */
	private LayerTree getTreeLayers() {
		if (treeLayers == null) {
			treeLayers = new LayerTree();
			treeLayers.setRootVisible(true);
			treeLayers.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					if (e.getClickCount() == 2) {
						addLayer();
					}
				}
			});
			treeLayers
					.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
						public void valueChanged(
								javax.swing.event.TreeSelectionEvent e) {

							boolean isGetMapable = false; // pffff, jajaja
							TreePath[] selecciones = getTreeLayers()
									.getSelectionPaths();
							if (selecciones != null) // no entenc perqu�
														// peta ac� i li
														// arriba null a
														// voltes...
							{
								for (int i = 0; i < selecciones.length
										&& !isGetMapable; i++) {
									WMSLayerNode layer = ((WMSLayerNode) selecciones[i]
											.getLastPathComponent());
									isGetMapable = layer.getChildren().size() == 0
											|| layer.getName() != null;
								}

							}
							getBtnAdd().setEnabled(isGetMapable);
							fireWizardComplete(isCorrectlyConfigured());
						}
					});
		}

		return treeLayers;
	}

	/**
	 * This method checks for the options selected within the configuration
	 * dialog are correct and enough to perform a GetMap request.
	 *
	 * @return true if you're done, false if not.
	 */
	public boolean isCorrectlyConfigured() {

		boolean b;
		this.repaint();
		if ((getLstSRSs().getSelectedIndex() != -1)
				&& (getLstFormats().getSelectedIndex() != -1)
				&& (getLstSelectedLayers().getModel().getSize() > 0)) {
			b = true;
		} else {
			b = false;
		}

		// Enable or disable del button.
		int[] selectedIndices = getLstSelectedLayers().getSelectedIndices();

		getBtnDel().setEnabled(
				selectedIndices != null && selectedIndices.length > 0);

		return b;
	}

	public WMSLayerNode[] selectedLayersToArray() {
		JDnDListModel modelo = ((JDnDListModel) lstSelectedLayers.getModel());
		ArrayList elements = modelo.getElements();
		WMSLayerNode[] layers = (WMSLayerNode[]) elements
				.toArray(new WMSLayerNode[0]);
		return layers;
	}

	/**
	 * DOCUMENT ME!
	 */
	private void delLayer() {
		int[] indices = lstSelectedLayers.getSelectedIndices();

		try {
			ArrayList removeList = new ArrayList();
			WMSLayerNode[] infos = selectedLayersToArray();
			JDnDListModel modelo = ((JDnDListModel) lstSelectedLayers
					.getModel());
			for (int i = 0; i < indices.length; i++) {
				removeList.add(infos[indices[i]]);
			}
			modelo.delIndices(indices);
			lstSelectedLayers.clearSelection();
			fireWizardComplete(isCorrectlyConfigured());

			// delete from selectedPaths as well
			WMSLayerNode elemento = null;
			WMSLayerNode last = null;
			TreePath path = null;
			for (int i = removeList.size() - 1; i >= 0; i--) {
				elemento = (WMSLayerNode) removeList.get(i);
				for (int j = 0; j < selectedPaths.size(); j++) {
					path = (TreePath) selectedPaths.get(j);
					last = (WMSLayerNode) path.getLastPathComponent();
					if (last.getTitle().compareTo(elemento.getTitle()) == 0) {
						selectedPaths.remove(path);
					}
				}
			}

			refreshData();
			refreshInfo();
		} catch (ArrayIndexOutOfBoundsException ex) {
			// if you reach this, please, tell me how you did it.
			ex.printStackTrace();
		}
	}

	/**
	 * Fires a notification to this wizard listeners telling them if the
	 * configuration is fair enough to send a GetMap request.
	 *
	 * @param b
	 */
	private void fireWizardComplete(boolean b) {
		listenerSupport.callStateChanged(b);
		callStateChanged(b);
	}

	/**
	 * Refreshes the content shown by this.
	 */
	public void refreshData() {
		refreshSRS();
		getLstSelectedLayers().repaint();
		refreshStyleTree();
		refreshDimensions();

	}

	/**
	 * Refreshes the info tab
	 *
	 */
	public void refreshInfo() {
		getTabInfo().refresh(dataSource, selectedLayersVector(),
				getStyleTitles(), getFormat(), getSRS(), getDimensions());
	}

	private void refreshDimensions() {
		int size = getLstSelectedLayers().getModel().getSize();
		boolean enable = false;
		for (int i = 0; i < size; i++) {
			WMSLayerNode node = (WMSLayerNode) getLstSelectedLayers()
					.getModel().getElementAt(i);
			if (node.getDimensions() == null) {
				continue;
			}

			for (int j = 0; j < node.getDimensions().size(); j++) {
				IFMapWMSDimension dim = (IFMapWMSDimension) node
						.getDimensions().get(j);

				try {
					getTabDimensions().addDimension(dim);
					enable = true;
				} catch (IllegalArgumentException e) {
					NotificationManager.addWarning(
							"Unrecognized dimension expression: "
									+ dim.getExpression() + " (for "
									+ dim.getName() + " in layer '"
									+ node.getName() + "')", null);
				}
			}
		}
		jTabbedPane.setEnabledAt(dimensionTabIndex, enable);

	}

	/**
	 *
	 */
	private void refreshStyleTree() {

		int size = getLstSelectedLayers().getModel().getSize();
		WMSLayerNode styleRoot = new WMSLayerNode();
		styleRoot.setTitle(PluginServices.getText(this, "selected_layers"));

		StyleTreeModel model = new StyleTreeModel(styleRoot);
		jTabbedPane.setEnabledAt(stylesTabIndex, false);
		for (int i = 0; i < size; i++) {
			WMSLayerNode node = (WMSLayerNode) getLstSelectedLayers()
					.getModel().getElementAt(i);
			// If any of the layers defines styles, the tab will be enabled.
			if (model.addLayerBranch(node)) {
				jTabbedPane.setEnabledAt(stylesTabIndex, true);
			}
		}

		getStyleTree().setModel(model);
		getStyleTree().expandAll();
		getStyleTree().repaint();
	}

	/**
	 * Takes the array of selected layers and a vector containing the style
	 * names and set them as selected in the styles panel
	 *
	 * @param selectedLayers
	 * @param styles
	 */
	public void setStyleSelections(Vector styles) {

		WMSLayerNode[] layers = selectedLayersToArray();
		int length = layers.length;
		for (int i = 0; i < length; i++) {
			String styleName = (String) styles.get(i);
			layers[length - i - 1].setSelectedStyleByName(styleName);
		}
	}

	/**
	 * Gets the currently shown style tree.
	 *
	 * @return
	 */
	private StyleTree getStyleTree() {
		return getTabStyle().getStyleTree();
	}

	/**
	 * Method called when pressing the Add layer button or when double-clicking
	 * on a layer from the server's layer tree.
	 */
	public void addLayer() {
		boolean alguno = false;

		TreePath[] selecciones = getTreeLayers().getSelectionPaths();
		if (selecciones == null) {
			return;
		}
		for (int i = 0; i < selecciones.length; i++) {
			selectedPaths.add(selecciones[i]);
			WMSLayerNode nodo = (WMSLayerNode) selecciones[i]
					.getLastPathComponent();
			if (nodo.getName() == null || nodo.getName().equals("")) {
				// no es un node que es puga demanar
				continue;
			}
			if (nodo.isSizeFixed()) {
				if (sizeFixed == null) {
					sizeFixed = nodo.getFixedSize();
				} else if ((sizeFixed.getHeight() != nodo.getFixedSize()
						.getHeight())
						|| (sizeFixed.getWidth() != nodo.getFixedSize()
								.getWidth())) {
					JOptionPane.showMessageDialog((Component) PluginServices
							.getMainFrame(), PluginServices.getText(this,
							"server_cant_render_layers"));
					continue;
				}
			}
			// s'afegeix a la llista de capes seleccionades
			JDnDListModel modelo = (JDnDListModel) lstSelectedLayers.getModel();

			if (modelo.addElement(0, nodo.clone())) {
				alguno = true;
			}
		}

		if (alguno) {
			refreshData();
		}
		refreshInfo();
	}

	/**
	 * This method initializes lstSelectedLayers
	 *
	 * @return List
	 */
	public LayerList getLstSelectedLayers() {
		if (lstSelectedLayers == null) {
			lstSelectedLayers = new LayerList();
			lstSelectedLayers.setModel(new JDnDListModel());
			lstSelectedLayers
					.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			lstSelectedLayers
					.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
						public void valueChanged(
								javax.swing.event.ListSelectionEvent e) {
							fireWizardComplete(isCorrectlyConfigured());
						}
					});
			lstSelectedLayers.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					refreshData();
				}
			});
		}

		return lstSelectedLayers;
	}

	/**
	 * This method initializes lstSRSs
	 *
	 * @return JList
	 */
	public JList getLstSRSs() {
		if (lstSRSs == null) {
			lstSRSs = new JList();
			lstSRSs.setModel(new SRSListModel());
			lstSRSs
					.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			lstSRSs
					.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
						public void valueChanged(
								javax.swing.event.ListSelectionEvent e) {
							int i = lstSRSs.getSelectedIndex();
							if (i != -1) {
								oldSRS = (String) lstSRSs.getModel()
										.getElementAt(i);
							}
							fireWizardComplete(isCorrectlyConfigured());
						}
					});
		}

		return lstSRSs;
	}

	/**
	 * This method initializes lstFormats
	 *
	 * @return List
	 */
	public JList getLstFormats() {
		if (lstFormats == null) {
			lstFormats = new JList();
			lstFormats
					.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			lstFormats
					.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
						public void valueChanged(
								javax.swing.event.ListSelectionEvent e) {
							fireWizardComplete(isCorrectlyConfigured());
						}
					});
		}

		return lstFormats;
	}

	/**
	 * Refreshes the SRS list
	 */
	private void refreshSRS() {
		ArrayList elements = ((JDnDListModel) lstSelectedLayers.getModel())
				.getElements();
		WMSLayerNode[] layers = (WMSLayerNode[]) elements
				.toArray(new WMSLayerNode[0]);

		SRSListModel model = new SRSListModel();

		if (layers.length == 0) {
			lstSRSs.setModel(model);// new DefaultListModel());
			sizeFixed = null;
		} else {
			TreeSet srsSet = new TreeSet();
			srsSet.addAll(layers[0].getAllSrs());
			model.setAll(srsSet);
			for (int i = 1; i < layers.length; i++) {
				model.setAll(model.intersect(layers[i].getAllSrs()));
			}
			lstSRSs.setModel(model);
		}
		int srsIndex = getSRSIndex(oldSRS);
		if (srsIndex != -1) {
			getLstSRSs().setSelectedIndex(srsIndex);
		}

	}

	public void setListenerSupport(WizardListenerSupport support) {
		listenerSupport = support;
	}

	public void setWizardData(WMSWizardData data) {
		this.dataSource = data;
		((JDnDListModel) getLstSelectedLayers().getModel()).clear();
		getLstSelectedLayers().repaint();

		getLstFormats().setModel(
				new FormatListModel(this.dataSource.getFormats()));
		getTreeLayers()
				.setModel(new LayerTreeModel(this.dataSource.getLayer()));
		refreshInfo();
	}

	private Vector getStyleTitles() {
		return getTabStyle().getStyleTree().getStyleSelectionTitles();
	}

	/**
	 * @return
	 */
	private Vector selectedLayersVector() {
		JDnDListModel mod = (JDnDListModel) getLstSelectedLayers().getModel();
		Vector lyrs = new Vector();
		for (int i = 0; i < mod.getSize(); i++) {
			lyrs.add(mod.getElementAt(i));
		}
		return lyrs;
	}

	public String getFormat() {
		return (String) getLstFormats().getSelectedValue();
	}

	public String getLayersQuery() {
		ArrayList elements = ((JDnDListModel) getLstSelectedLayers().getModel())
				.getElements();
		WMSLayerNode[] layers = (WMSLayerNode[]) elements
				.toArray(new WMSLayerNode[0]);

		// Nombre separado por comas de las capas
		String layersQuery = layers[layers.length - 1].getName();

		for (int i = layers.length - 2; i >= 0; i--) {
			layersQuery += ("," + layers[i].getName());
		}

		return layersQuery;

	}

	/**
	 * Returns the current selected SRS.
	 *
	 * @return
	 */
	public String getSRS() {
		return (String) getLstSRSs().getSelectedValue();
	}

	/**
	 * Returns the name of the layer that the user has typed-in
	 *
	 * @return
	 */
	public String getLayerName() {
		return getJTxtNomCapa().getText();
	}

	public void setLayerName(String name) {
		getJTxtNomCapa().setText(name);
	}

	public Rectangle2D getLayersRectangle() throws ProjectionLayerException {
		ArrayList elements = ((JDnDListModel) getLstSelectedLayers().getModel())
				.getElements();
		WMSLayerNode[] layers = (WMSLayerNode[]) elements
				.toArray(new WMSLayerNode[0]);
		String[] layerNames = new String[layers.length];
		for (int i = 0; i < layerNames.length; i++) {
			layerNames[i] = layers[i].getName();
		}
		// Boundingbox de todas las capas
		// ArrayList rectangulos = new ArrayList();

		Rectangle2D rect;
		String latLonID;

		rect = dataSource.getBoundingBox(layerNames, getSRS());
		if (rect == null) {
			latLonID = "EPSG:4326";
			rect = dataSource.getBoundingBox(layerNames, latLonID);
			if (rect == null) {
				rect = dataSource.getBoundingBox(layerNames, "CRS:84");
			}

			IProjection reqProj = CRSFactory.getCRS(getSRS());
			IProjection latLonProj = CRSFactory.getCRS(latLonID);
			if ((reqProj != null) && (latLonProj != null)) {
				ICoordTrans ct = latLonProj.getCT(reqProj);
				Rectangle2D reprojectedRect;
				reprojectedRect = ct.convert(rect);
				rect = reprojectedRect;
			} else {
//				throw new ProjectionLayerException(PluginServices.getText(this,
//						"cannotReproject")
//						+ " " + getSRS() + " -> " + latLonID);
				throw new ProjectionLayerException(getName(),null);
			}
		}

		return rect;
	}

	public TreePath[] getSelectedLayers() {
		return getTreeLayers().getSelectionPaths();
	}

	public Hashtable getOnlineResources() {
		return dataSource.getOnlineResources();
	}

	/**
	 * The piece of the GetMap request that has the layer names.
	 *
	 * @return
	 */
	public String getQueryableLayerQuery() {
		ArrayList elements = ((JDnDListModel) getLstSelectedLayers().getModel())
				.getElements();
		WMSLayerNode[] layers = (WMSLayerNode[]) elements
				.toArray(new WMSLayerNode[0]);
		// Nombre separado por comas de las capas
		String layersQuery = "";
		int i;

		for (i = 0; i < layers.length; i++) {
			if (layers[i].isQueryable()) {
				layersQuery = layers[i].getName();

				break;
			}
		}

		for (int j = i + 1; j < layers.length; j++) {
			if (layers[j].isQueryable()) {
				layersQuery += ("," + layers[j].getName());
			}
		}

		return layersQuery;
	}

	/**
	 * Returns the index of the CRS within the CRS list.
	 *
	 * Devuelve la posicion que ocupa el CRS en la lista de CRS
	 *
	 * @param crs
	 * @return The CRS's index if it exists, -1 if it not exists.
	 */
	public int getSRSIndex(String crs) {
		for (int i = 0; i < getLstSRSs().getModel().getSize(); i++) {
			if (crs != null
					&& crs.equals(getLstSRSs().getModel().getElementAt(i))) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Returns the index of the format within the formats list.
	 *
	 * Devuelve la posicion que ocupa el formato en la lista de formatos
	 *
	 * @param format
	 *
	 * @return The format's index if it exists, -1 if it not exists.
	 */
	public int getFormatIndex(String format) {
		for (int i = 0; i < getLstFormats().getModel().getSize(); i++) {
			if (format != null
					&& format
							.equals(getLstFormats().getModel().getElementAt(i))) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * This method initializes jTabbedPane
	 *
	 * @return TabbedPane
	 */
	public JTabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();
			jTabbedPane.setBounds(4, 4, 502, 415);
			InfoPanel ip = getTabInfo();
			ip.addFocusListener(new FocusListener() {
				public void focusGained(FocusEvent e) {
					refreshInfo();
				}

				public void focusLost(FocusEvent e) {
				}
			});

			StylesPanel sp = getTabStyle();
			DimensionPanel dp = getTabDimensions();

			jTabbedPane.addTab(PluginServices.getText(this, "info"), null, ip,
					null);
			jTabbedPane.addTab(PluginServices.getText(this, "capas"), null,
					getTabLayers(), null);
			jTabbedPane.addTab(PluginServices.getText(this, "estilos"), null,
					sp, null);
			stylesTabIndex = jTabbedPane.getTabCount() - 1;
			jTabbedPane.addTab(PluginServices.getText(this, "dimensiones"),
					null, dp, null);
			dimensionTabIndex = jTabbedPane.getTabCount() - 1;
			jTabbedPane.addTab(PluginServices.getText(this, "formatos"), null,
					getTabFormats(), null);
			jTabbedPane.setEnabledAt(dimensionTabIndex, false);
			jTabbedPane.setEnabledAt(stylesTabIndex, false);
		}
		return jTabbedPane;
	}

	/**
	 * This method initializes jPanel
	 *
	 * @return Panel
	 */
	private JPanel getTabLayers() {
		if (tabLayers == null) {
			tabLayers = new JPanel();
			tabLayers.setLayout(null);
			tabLayers.add(getJPanel4(), null);
			tabLayers.add(getJPanel2(), null);
		}
		return tabLayers;
	}

	/**
	 * This method initializes jPanel1
	 *
	 * @return Panel
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jPanel1 = new JPanel();
			jPanel1.setLayout(null);
			jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(
					null, PluginServices.getText(this, "select_formats"),
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null,
					null));
			jPanel1.setBounds(5, 10, 485, 194);
			jPanel1.add(getJScrollPane6(), null);
			jPanel1.add(getChkTransparency(), null);
		}
		return jPanel1;
	}

	/**
	 * This method initializes jPanel5
	 *
	 * @return Panel
	 */
	private JPanel getJPanel5() {
		if (jPanel5 == null) {
			jPanel5 = new JPanel();
			jPanel5.setLayout(null);
			jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(
					null, PluginServices.getText(this, "seleccionar_srs"),
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null,
					null));
			jPanel5.setBounds(5, 205, 485, 173);
			jPanel5.add(getJScrollPane2(), null);
		}
		return jPanel5;
	}

	/**
	 * This method initializes jPanel6
	 *
	 * @return Panel
	 */
	private StylesPanel getTabStyle() {
		if (tabStyle == null) {
			tabStyle = new StylesPanel(this);
			tabStyle.setEnabled(false);
			tabStyle.addListener(new BeanListener() {
				public void beanValueChanged(Object value) {
					fireWizardComplete(isCorrectlyConfigured());
				};
			});
			tabStyle.addFocusListener(new FocusAdapter() {
				public void focusGained(FocusEvent e) {
					refreshStyleTree();
				}
			});
		}
		return tabStyle;
	}

	/**
	 * Sets the focus to the tab next to the current one.
	 *
	 * Enfoca a la solapa siguiente a la actualmente enfocada del TabbedPane
	 *
	 */
	public void avanzaTab() {
		int currentPage = currentPage();
		int nPages = getNumTabs();
		if (nPages - 1 > currentPage) {
			getJTabbedPane().setSelectedIndex(nextEnabledPage());
		}
	}

	/**
	 * Sets the focus to the tab previous to the current one.
	 *
	 * Enfoca a la solapa anterior a la actualmente enfocada del TabbedPane
	 *
	 */
	public void retrocedeTab() {
		this.getJTabbedPane().setSelectedIndex(previousEnabledPage());

	}

	/**
	 * Returns the index of the current tab.
	 *
	 * Devuelve el �ndice de la p�gina actual del wizard.
	 *
	 * @return
	 */
	public int currentPage() {
		return getJTabbedPane().getSelectedIndex();
	}

	/**
	 * Returns the tab amount that the WMSParamsPanel currently have
	 *
	 * Devuelve el n�mero de solapas que tiene actualmente el WMSParamsPanel
	 *
	 * @return int
	 */
	public int getNumTabs() {
		return getJTabbedPane().getTabCount();
	}

	/**
	 * <p>
	 * Returns the index of the previous enabled tab.
	 * </p>
	 * <p>
	 * Devuelve el �ndice de la anterior p�gina habilitada del wizard o -1
	 * si no hay ninguna.
	 * </p>
	 *
	 * @return The index, or -1 if there is no one.
	 */
	public int previousEnabledPage() {
		int currentPage = currentPage();
		int j = 0;
		if (currentPage == 0) {
			j = -1;
		}
		for (int i = currentPage - 1; i > -1; i--) {
			if (getJTabbedPane().isEnabledAt(i)) {
				j = i;
				break;
			}
		}
		return j;
	}

	/**
	 * <p>
	 * Returns the previous of the previous enabled tab.
	 * </p>
	 * <p>
	 * Devuelve el �ndice de la siguiente p�gina habilitada del wizard o -1
	 * si no hay ninguna.
	 * </p>
	 *
	 * @return The index, or -1 if there is no one.
	 */
	public int nextEnabledPage() {
		int currentPage = currentPage();
		int nPages = getNumTabs();
		if (currentPage == nPages) {
			return -1;
		}
		for (int i = currentPage + 1; i < nPages; i++) {
			if (getJTabbedPane().isEnabledAt(i)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Selects/deselects the transparency checkbox.
	 *
	 * @param b
	 */
	public void setTransparent(boolean b) {
		getChkTransparency().setSelected(b);
	}

	/**
	 * This method initializes chkTrasparency
	 *
	 * @return CheckBox
	 */
	private JCheckBox getChkTransparency() {
		if (chkTransparency == null) {
			chkTransparency = new JCheckBox();
			chkTransparency.setText(PluginServices.getText(this,
					"wms_transparency"));
			chkTransparency.setSelected(true);
			chkTransparency.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					fireWizardComplete(isCorrectlyConfigured());
				}
			});
			chkTransparency.setBounds(5, 168, 441, 21);

		}
		return chkTransparency;
	}

	/**
	 * Returns a vector of strings containing the styles selected for the
	 * layers. A blank style is correct and means that default style will be
	 * used.
	 *
	 * @return Vector
	 */
	public Vector getStyles() {
		Vector v = new Vector();
		WMSLayerNode[] layers = selectedLayersToArray();
		for (int i = 0; i < layers.length; i++) {
			FMapWMSStyle sty = layers[i].getSelectedStyle();
			String s = (sty != null) ? sty.name : "";
			v.add(0,s);
		}
		return v;
	}

	public Vector getLayerStyle(String layerName) {
		Vector v = new Vector();
		WMSLayerNode[] layers = selectedLayersToArray();
		for (int i = 0; i < layers.length; i++) {
			if (layers[i].getName().equalsIgnoreCase(layerName)) {
				FMapWMSStyle sty = layers[i].getSelectedStyle();
//				String s = (sty != null) ? sty.name : "";
				v.add(sty);
				return v;
			}
		}
		return v;
	}

	/**
	 * This method initializes tabInfo
	 *
	 * @return Panel
	 */
	private InfoPanel getTabInfo() {
		if (tabInfo == null) {
			tabInfo = new InfoPanel();

		}
		return tabInfo;
	}

	public void setDimensions(Vector dimensions) {
		for (int i = 0; i < dimensions.size(); i++) {
			String st = (String) dimensions.get(i);
			if (st.split("=").length == 2) {
				String[] s = st.split("=");
				getTabDimensions().setDimensionValue(s[0], s[1]);
			}
		}

	}

	/**
	 * This method initializes tabDimension
	 *
	 * @return Panel
	 */
	private DimensionPanel getTabDimensions() {
		if (tabDimensions == null) {
			tabDimensions = new DimensionPanel();
			tabDimensions.setEnabled(false);
			tabDimensions.addListener(new BeanListener() {
				public void beanValueChanged(Object value) {
					Vector myDimensions = (Vector) value;
					if (value != null
							&& (oldDimensions == null || !(oldDimensions.size() == myDimensions
									.size() && oldDimensions
									.containsAll(myDimensions)))) {
						oldDimensions = myDimensions;
					}
					fireWizardComplete(isCorrectlyConfigured());
				}
			});

		}
		return tabDimensions;
	}

	/**
	 * @return
	 */
	public boolean getTransparency() {
		return getChkTransparency().isSelected();
	}

	/**
	 * @return
	 */
	public Vector getDimensions() {
		return getTabDimensions().getDimensions();
	}

	public void initWizard() {
	}

	public void execute() {
	}

	// Gets all the layers available in the WMS Server
	protected FLayer getAllSubTree(WMSLayerNode node) {
		if (node.getChildren().size() > 0) {
			FLayers l = null;
			if (this.getMapCtrl() == null) {
				com.iver.cit.gvsig.project.documents.view.gui.View v = (com.iver.cit.gvsig.project.documents.view.gui.View) PluginServices
						.getMDIManager().getActiveWindow();
				l = new FLayers();
				l.setMapContext(v.getMapControl().getMapContext());
			} else {
				l = new FLayers();
				l.setMapContext(this.getMapCtrl().getMapContext());
			}

			l.setName(node.getTitle());
			l.setVisible(false);
			for (int i = 0; i < node.getChildren().size(); i++) {
				FLayer lyr = getAllSubTree((WMSLayerNode) node.getChildren()
						.get(i));
				if (lyr != null) {
					l.addLayer(lyr);
				}
			}
			return l;
		} else {
			FLyrWMS layer = new FLyrWMS();
			try {
				layer.setHost(new URL(dataSource.getHost()));
				layer.setFullExtent(getLayersRectangle());
			} catch (ProjectionLayerException e) {
				e.printStackTrace();
				return null;
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return null;
			}
			layer.setFormat(getFormat());
			layer.setLayerQuery(node.getName());
			layer.setInfoLayerQuery(getQueryableLayerQuery());
			layer.setSRS(getSRS());
			layer.setName(node.getTitle());
			layer.setWmsTransparency(getTransparency());
			layer.setStyles(getLayerStyle(node.getName()));
			layer.setDimensions(getDimensions());
			layer.setDriver(getDriver());
			layer.setOnlineResources(getOnlineResources());
			layer.setFixedSize(getFixedSize());
			layer.setQueryable(dataSource.isQueryable());
			layer.setVisible(false);
			return layer;
		}
	}

	// Gets all the layers available in the WMS Server
	public FLayer getAllLayers() {
		LayerTree layerTree = getLayerStructure();
		return getAllSubTree((WMSLayerNode) layerTree.getModel().getRoot());
	}

	// changed by Laura:
	// To delegate getLayer to ParamsPanel from WMSWizard and WMSProps
	// public FLayer getLayer() { return null; }
	public FLayer getLayer() {
		if (getDisagregatedLayers()) {
			return getLayerTree();
		} else {
			return getLayerPlain();
		}
	}

	/**
	 * If user does not select the chekbox Disagregate layers, will get the
	 * layer tree in WMS
	 *
	 * @return
	 */
	protected FLayer getLayerTree() {
		LayerTree layerTree = getLayerStructure();
		TreePath[] selection = (TreePath[]) getSelectedPaths().toArray(
				new TreePath[0]);// layerTree.getSelectionPaths();
		if (selection != null) {
			return getSubTree((WMSLayerNode) layerTree.getModel().getRoot(),
					selection);
		}
		return null;
	}

	protected boolean nodeSelected(TreePath[] selection, WMSLayerNode node) {
		for (int i = 0; i < selection.length; i++) {
			Object[] components = selection[i].getPath();
			for (int j = 0; j < components.length; j++) {
				if (components[j] instanceof WMSLayerNode) {
					if (((WMSLayerNode) components[j]).getTitle().compareTo(
							node.getTitle()) == 0) {
						return true;
					}
				}
			}
		}
		return false;
	}

	protected FLayer getSubTree(WMSLayerNode node, TreePath[] selection) {
		if (node.getChildren().size() > 0) {
			FLayers l = null;
			if (this.getMapCtrl() == null) {
				com.iver.cit.gvsig.project.documents.view.gui.View v = (com.iver.cit.gvsig.project.documents.view.gui.View) PluginServices
						.getMDIManager().getActiveWindow();
				l = new FLayers();
				l.setMapContext(v.getMapControl().getMapContext());
			} else {
				l = new FLayers();
				l.setMapContext(this.getMapCtrl().getMapContext());
			}

			l.setName(node.getTitle());
			for (int i = 0; i < node.getChildren().size(); i++) {
				if (nodeSelected(selection, (WMSLayerNode) (node.getChildren()
						.get(i)))) {
					FLayer lyr = getSubTree((WMSLayerNode) node.getChildren()
							.get(i), selection);
					if (lyr != null) {
						l.addLayer(lyr);
					}
				}
			}
			return l;
		} else {
			if (nodeSelected(selection, node)) {
				FLyrWMS layer = new FLyrWMS();
				// layer.setHost(getHost());
				try {
					layer.setHost(new URL(dataSource.getHost()));
					layer.setFullExtent(getLayersRectangle());
				} catch (ProjectionLayerException e) {
					e.printStackTrace();
					return null;
				} catch (MalformedURLException e) {
					e.printStackTrace();
					return null;
				}
				layer.setFormat(getFormat());
				layer.setLayerQuery(node.getName());
				layer.setInfoLayerQuery(getQueryableLayerQuery());
				layer.setSRS(getSRS());
				layer.setName(node.getTitle());
				layer.setWmsTransparency(getTransparency());
				layer.setStyles(getLayerStyle(node.getName()));
				layer.setDimensions(getDimensions());
				layer.setDriver(getDriver());
				layer.setOnlineResources(getDriver().getOnlineResources());
				layer.setFixedSize(getFixedSize());
				layer.setQueryable(node.isQueryable());
				//layer.setQueryable(dataSource.isQueryable());
				return layer;

			} else {
				return null;
			}
		}
	}

	/**
	 * If user selects the chekbox Disagregate layers, will get the selected
	 * layers rendered in one the tree structure in WMS will be lost.
	 *
	 * @return
	 */
	protected FLayer getLayerPlain() {
		FLyrWMS layer = new FLyrWMS();
		// layer.setHost(getHost());
		try {
			layer.setFullExtent(getLayersRectangle());
			layer.setHost(new URL(dataSource.getHost()));
		} catch (ProjectionLayerException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog((Component) PluginServices
					.getMainFrame(), ex.getMessage());
			return null;
		} catch (MalformedURLException e) {
			e.printStackTrace();
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
		layer.setOnlineResources(getDriver().getOnlineResources());
		layer.setFixedSize(getFixedSize());

		//layer.setQueryable(node.isQueryable());
		layer.setQueryable(dataSource.isQueryable());
		return layer;
	}

	// ---------------------

	public FMapWMSDriver getDriver() {
		return dataSource.getDriver();
	}

	public Dimension getFixedSize() {
		return sizeFixed;
	}

	/**
	 * This method initializes chkExtendedNames
	 *
	 * @return CheckBox
	 */
	private JCheckBox getChkExtendedNames() {
		if (chkExtendedNames == null) {
			chkExtendedNames = new JCheckBox();
			chkExtendedNames.setText(PluginServices.getText(this,
					"show_layer_names"));
			chkExtendedNames.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					boolean b = chkExtendedNames.isSelected();
					getTreeLayers().showLayerNames = b;
					getTreeLayers().repaint();
					getLstSelectedLayers().showLayerNames = b;
					getLstSelectedLayers().repaint();
					getStyleTree().showLayerNames = b;
				}
			});
			chkExtendedNames
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							fPrefs.putBoolean("show_layer_names",
									chkExtendedNames.isSelected());
						}
					});
			chkExtendedNames.setBounds(19, 300, 212, 20);
			chkExtendedNames.setSelected(fPrefs.getBoolean("show_layer_names",
					false));

		}
		return chkExtendedNames;
	}

	/**
	 * This method initializes btnUp
	 *
	 * @return Button
	 */
	private JButton getBtnUp() {
		if (btnUp == null) {
			btnUp = new JButton();
			btnUp.setBounds(438, 184, 30, 30);
			btnUp.setToolTipText(PluginServices.getText(this, "move_layer_up"));
			btnUp.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					LayerList list = getLstSelectedLayers();
					int[] selectedIndices = list.getSelectedIndices();
					int index = list.getSelectedIndex();
					if (selectedIndices != null && selectedIndices.length == 1
							&& index > 0) {
						JDnDListModel m = (JDnDListModel) list.getModel();
						m.itemsMoved(index - 1, new int[] { index });
						list.setSelectedIndex(index - 1);
					}
				}
			});
			btnUp.setIcon(PluginServices.getIconTheme().get("aplication-preferences-uparrow"));
		}
		return btnUp;
	}

	/**
	 * This method initializes btnDown
	 *
	 * @return Button
	 */
	private JButton getBtnDown() {
		if (btnDown == null) {
			btnDown = new JButton();
			btnDown.setBounds(438, 215, 30, 30);
			btnDown.setToolTipText(PluginServices.getText(this,
					"move_layer_down"));
			btnDown.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					LayerList list = getLstSelectedLayers();
					int[] selectedIndices = list.getSelectedIndices();
					int index = list.getSelectedIndex();
					if (selectedIndices != null
							&& selectedIndices.length == 1
							&& index < getLstSelectedLayers().getModel()
									.getSize() - 1) {
						JDnDListModel m = (JDnDListModel) list.getModel();
						m.itemsMoved(index + 1, new int[] { index });
						list.setSelectedIndex(index + 1);
					}
				}
			});
			btnDown.setIcon(PluginServices.getIconTheme()
					.get("aplication-preferences-downarrow"));
		}
		return btnDown;
	}

	public void setFixedSize(Dimension sz) {
		sizeFixed = sz;
	}

	public ArrayList getSelectedPaths() {
		return selectedPaths;
	}

	public LayerTree getJustTreeLayer() {
		return treeLayers;
	}

	/**
	 * This method initializes chkDisagregate
	 *
	 * @return CheckBox
	 */
	private JCheckBox getChkDisagregate() {
		if (chkDisagregate == null) {

			chkDisagregate = new JCheckBox();
			chkDisagregate.setBounds(new java.awt.Rectangle(19,276,269,20));
			chkDisagregate.setText(PluginServices.getText(this,
					"disagregate_layers"));
			chkDisagregate.setSelected(false);

		}
		return chkDisagregate;
	}

	public boolean getDisagregatedLayers() {
		return chkDisagregate.isSelected();
	}

	public void disableDisagregatedLayers() {
		chkDisagregate.setEnabled(false);
	}

	@Override
	public DataStoreParameters[] getParameters() {
		// TODO Auto-generated method stub
		return null;
	}
} // @jve:decl-index=0:visual-constraint="10,10"
