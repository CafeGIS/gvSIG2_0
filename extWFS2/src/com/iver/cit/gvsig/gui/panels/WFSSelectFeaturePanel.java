package com.iver.cit.gvsig.gui.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.Collator;
import java.util.Collections;
import java.util.Locale;
import java.util.Vector;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.gvsig.gui.beans.panelGroup.IPanelGroup;
import org.gvsig.remoteClient.wfs.schema.type.GMLGeometryType;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.gui.panels.WFSSelectFeaturePanel.LayerTable.LayerTableModel;
import com.iver.cit.gvsig.gui.panels.model.WFSSelectedFeature;
import com.iver.cit.gvsig.gui.panels.model.WFSUtils;
import com.iver.utiles.StringComparator;

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
 * $Id: WFSSelectFeaturePanel.java 18799 2008-02-12 16:53:03Z vcaballero $
 * $Log$
 * Revision 1.19  2007-09-19 16:14:50  jaume
 * removed unnecessary imports
 *
 * Revision 1.18  2007/03/15 13:34:16  ppiqueras
 * Eliminado mensaje de aviso no necesario.
 *
 * Revision 1.17  2007/03/05 13:49:42  ppiqueras
 * Si una capa WFS no tiene campos (y por tanto no tiene un campo obligatorio de tipo geometría), que avise al usuario y no permita que se intente cargar dicha capa.
 *
 * Revision 1.16  2007/02/22 12:25:09  ppiqueras
 * Añadidas tool tip text.
 *
 * Revision 1.15  2007/02/09 14:12:39  jorpiell
 * Soporte para WFS 1.1 y WFS-T
 *
 * Revision 1.14  2007/02/07 13:26:23  ppiqueras
 * Corregido bug: al seleccionar una feature, pero sin cerrar el diálogo, se retrocede y se conecta a otro servidor,  se mantenía la posición seleccionada en la tabla de features. Se ha solucionado haciendo que se quede deseleccionada toda la tabla de features.
 *
 * Revision 1.13  2007/01/23 13:12:43  ppiqueras
 * Corregido un pequeño bug: el área de texto del nombre de la feature no debe ser editable, dado que si se edita no procesa el texto escrito.
 *
 * Revision 1.12  2007/01/15 14:16:22  ppiqueras
 * Corregido bug: no cargaba los campos al principio y ademÃ¡s se ejecutaba mÃ¡s de una vez.
 *
 * Revision 1.11  2006/12/15 13:57:34  ppiqueras
 * eliminado un import que sobraba
 *
 * Revision 1.10  2006/12/12 10:24:45  ppiqueras
 * Nueva funcionalidad: Pulsando doble 'click' sobre una capa de un servidor, se carga (igual que antes), pero ademÃ¡s se avanza a la siguiente pestaÃ±a sin tener que pulsar el botÃ³n 'Siguiente'.
 *
 * Revision 1.9  2006/12/04 08:59:47  ppiqueras
 * Algunos bugs corregidos. A cambio hay 2 bugs relacionados que todavÃ­a no han sido corregidos (ver PHPCollab) (los tiene asignados Jorge).
 *
 * Revision 1.8  2006/10/27 06:44:56  jorpiell
 * Se han cambiado algunas etiquetas de texto que salían recortadas
 *
 * Revision 1.7  2006/07/21 11:50:31  jaume
 * improved appearance
 *
 * Revision 1.6  2006/06/21 12:35:45  jorpiell
 * Se ha añadido la ventana de propiedades. Esto implica añadir listeners por todos los paneles. Además no se muestra la geomatría en la lista de atributos y se muestran únicamnete los que se van a descargar
 *
 * Revision 1.5  2006/05/25 10:31:06  jorpiell
 * Como ha cambiado la forma de mostrar las capas (una tabla, en lugar de una lista), los paneles han tenido que ser modificados
 *
 * Revision 1.4  2006/05/23 08:09:39  jorpiell
 * Se ha cambiado la forma en la que se leian los valores seleccionados en los paneles y se ha cambiado el comportamiento de los botones
 *
 * Revision 1.3  2006/05/19 12:57:08  jorpiell
 * Modificados algunos paneles
 *
 * Revision 1.2  2006/04/20 16:38:24  jorpiell
 * Ahora mismo ya se puede hacer un getCapabilities y un getDescribeType de la capa seleccionada para ver los atributos a dibujar. Queda implementar el panel de opciones y hacer el getFeature().
 *
 *
 */


/**
 * <p>Panel where user can select a feature type to load as a WFS layer.</p>
 * 
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es)
 */
public class WFSSelectFeaturePanel extends AbstractWFSPanel {
	private static final long serialVersionUID = -3781080396069038450L;
	private JPanel featureListPanel = null;
	private JScrollPane jScrollPane = null;
	private LayerTable lstFeatures = null;
	private JTextField txtName = null;
	private JCheckBox chkExtendedNames = null;
	private JPanel layerNamePanel = null;
	private WFSSelectedFeature layerNode = null;
	private ListSelectionListener listSelectionListener = null;

	/**
	 * Creates a new WFS select feature panel.
	 */
	public WFSSelectFeaturePanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes coveragesListPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getFeaturesListPanel() {
		if (featureListPanel == null) {
			java.awt.GridBagConstraints gridBagConstraints;
			featureListPanel = new JPanel();
			featureListPanel.setLayout(new java.awt.GridBagLayout());
			featureListPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
					null, PluginServices.getText(this, "select_features"),
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));

			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			featureListPanel.add(getJScrollPane(), gridBagConstraints);
		}

		return featureListPanel;
	}

	/**
	 * This method initializes jScrollPane
	 *
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();		
			jScrollPane.setViewportView(getLstFeatures());
			jScrollPane.setToolTipText(PluginServices.getText(jScrollPane, "feature_Selection_Info"));
		}

		return jScrollPane;
	}

	/**
	 * This method initializes lstFeatures
	 *
	 * @return javax.swing.LayerTable
	 */
	public LayerTable getLstFeatures() {
		if (lstFeatures == null) {
			lstFeatures = new LayerTable();
			lstFeatures.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			lstFeatures.getSelectionModel().addListSelectionListener(getListSelectionListener());
			lstFeatures.setToolTipText(PluginServices.getText(lstFeatures, "feature_Selection_Info"));

			// Double-click -> click the 'Next' button
			lstFeatures.addMouseListener(new MouseAdapter() {
				/*
				 *  (non-Javadoc)
				 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
				 */
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {
						Object obj = getPanelGroup();

						if ((obj != null) && (obj instanceof WFSParamsPanel))
							((WFSParamsPanel)obj).doClickOnNextButton();
					}
				}			
			});
		}

		return lstFeatures;
	}

	/**
	 * Creates the list selection listener for the layers list
	 * 
	 * @return the listener
	 */
	private ListSelectionListener getListSelectionListener(){
		if (listSelectionListener == null){
			listSelectionListener = new ListSelectionListener(){
				public void valueChanged(javax.swing.event.ListSelectionEvent e) {
					DefaultListSelectionModel model = (DefaultListSelectionModel) e.getSource();
					LayerTableModel t_model = ((LayerTableModel)lstFeatures.getModel());
					if (t_model.isUpdatingSelection())
						return;

					// With this condition we force to execute the updating only when a row has been selected by the user
					if ((model.getMinSelectionIndex() > -1) && (e.getValueIsAdjusting() == false)) {
						// Indicate that now we are loading a new layer
						IPanelGroup panelGroup = getPanelGroup();

						if (panelGroup == null)
							return;

						if (((WFSParamsPanel)panelGroup).getFilterPanel() == null)
							return;

						((WFSParamsPanel)panelGroup).getFilterPanel().setWFSFilterPanelIsAsTabForWFSLayersLoad(true);

						refreshData();
					}
				}
			};	
		}

		return listSelectionListener;
	}

	/**
	 * This method initializes jTextField
	 *
	 * @return javax.swing.JTextField
	 */
	public JTextField getTxtName() {
		if (txtName == null) {
			txtName = new JTextField();
			txtName.setBounds(6, 19, 472, 20);
			txtName.setText(PluginServices.getText(this, "WFSLayer"));
			txtName.setEditable(false);
			txtName.setBackground(Color.WHITE);
			txtName.setToolTipText(PluginServices.getText(txtName, "feature_to_load"));

			txtName.getDocument().addDocumentListener(new DocumentListener() {
				/*
				 * (non-Javadoc)
				 * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
				 */
				public void changedUpdate(DocumentEvent e) {
				}

				/*
				 * (non-Javadoc)
				 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
				 */
				public void insertUpdate(DocumentEvent e) {
					IPanelGroup panelGroup = getPanelGroup();

					if (panelGroup == null)
						return;

					((WFSParamsPanel)panelGroup).setApplicable(true);
				}

				/*
				 * (non-Javadoc)
				 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
				 */
				public void removeUpdate(DocumentEvent e) {
				}
			});
		}

		return txtName;
	}

	/**
	 * This method initializes chkExtendedNames
	 *
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getChkExtendedNames() {
		if (chkExtendedNames == null) {
			chkExtendedNames = new JCheckBox();
			chkExtendedNames.setText(PluginServices.getText(this, "show_layer_names"));
			chkExtendedNames.setBounds(10, 372, 382, 20);
			chkExtendedNames.addItemListener(new java.awt.event.ItemListener() {
				/*
				 * (non-Javadoc)
				 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
				 */
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					boolean b = chkExtendedNames.isSelected();
					getLstFeatures().setShowLayerNames(b);
					getLstFeatures().repaint();
				}
			});
		}

		return chkExtendedNames;
	}

	/**
	 * Set the selected layer node
	 * 
	 * @param layerNode the layer to select
	 */
	public void setSelectedFeature(WFSSelectedFeature layerNode){
		int index = -1;

		for (int i=0; i<getLstFeatures().getRowCount(); i++){
			WFSSelectedFeature node = (WFSSelectedFeature)getLstFeatures().getValueAt(i);

			if (node != null && layerNode.getName().equals(node.getName())){
				index = i;
			}
		}

		if (index != -1){
			getLstFeatures().changeSelection(index, 0, false, false);
		}
	}

	/**
	 * Refresh the features list
	 */
	public void refresh(WFSSelectedFeature layer) {
		// Unselects all features
		unselectAllFeatures();

		// Add the new features
		getLstFeatures().addFeatures(getSelectedFeatureManager().getLayerList());

		if (layer != null) {
			layerNode = layer;
			setLayerName(layer.getName());
			setSelectedFeature(layer);			
		}		
	}

	/**
	 * Refreshes the wizard components data each time a feature
	 * is selected. The <i>describeFeatureType</i> operation must be sent.
	 */
	public void refreshData() {
		WFSSelectedFeature lyr = (WFSSelectedFeature)getLstFeatures().getSelectedValue();

		try {
			getTxtName().setText(lyr.getTitle());
		} catch (NullPointerException e){
			getTxtName().setText(PluginServices.getText(this,"default_name"));
			NotificationManager.addError(PluginServices.getText(this,"default_name"), e);
		}

		IPanelGroup panelGroup = getPanelGroup();

		if (panelGroup == null)
			return;

		((WFSParamsPanel)panelGroup).refresh(lyr);

		repaint();
	}

	/**
	 * This method initializes layerNamePanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getLayerNamePanel() {
		if (layerNamePanel == null) {
			java.awt.GridBagConstraints gridBagConstraints;
			layerNamePanel = new JPanel();
			layerNamePanel.setLayout(new java.awt.GridBagLayout());
			layerNamePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
					null, PluginServices.getText(this, "layer_name"),
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));			
			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.weightx = 1.0;
			layerNamePanel.add(getTxtName(), gridBagConstraints);
		}

		return layerNamePanel;
	}

	/**
	 * Gets the name of the layer (feature) selected.
	 *
	 * @return the layer name
	 */
	public String getLayerName() {
		return ((WFSSelectedFeature)getLstFeatures().getSelectedValue()).getName();
	}

	/**
	 * Gets the name space of the layer (feature) selected.
	 *
	 * @return the name space
	 */
	public String getLayerNameSpace(){
		return ((WFSSelectedFeature)getLstFeatures().getSelectedValue()).getNameSpace();
	}

	/**
	 * Gets the selected feature.
	 * 
	 * @return the selected feature
	 */
	public WFSSelectedFeature getSelectedFeature() {
		WFSSelectedFeature selectedFeature = (WFSSelectedFeature)getLstFeatures().getSelectedValue();

		if (selectedFeature != null) {
			layerNode = selectedFeature;
		}

		return layerNode;
	}

	/**
	 * Sets the name of the feature selected.
	 * 
	 * @param name the name of the feature selected
	 */
	public void setLayerName(String name) {
		getTxtName().setText(name);
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.gui.panels.AbstractWFSPanel#initialize()
	 */
	protected void initialize() {
		setLabel(PluginServices.getText(this, "feature"));
		setLabelGroup(PluginServices.getText(this, "wfs"));
		java.awt.GridBagConstraints gridBagConstraints;
		this.setLayout(new java.awt.GridBagLayout());

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		this.add(getLayerNamePanel(), gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		this.add(getFeaturesListPanel(), gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		this.add(getChkExtendedNames(), gridBagConstraints);		

	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.gui.panels.AbstractWFSPanel#setReference(java.lang.Object)
	 */
	public void setReference(Object ref) {
		super.setReference(ref);
		//		if (ref instanceof FLyrWFS){			
		//			getPanelGroup().setEnabledApplyButton(false);
		//		}		
	}	

	/**
	 * Updates the list with no feature selected.
	 */
	public void unselectAllFeatures() {
		// Reset the last row selection of the features table
		int numberOfFeatures = getLstFeatures().getRowCount();

		if (numberOfFeatures > 0) {			
			ListSelectionModel model = getLstFeatures().getSelectionModel();
			model.removeSelectionInterval(0, numberOfFeatures - 1); // whatever row selection
		}
	}

	/**
	 * Table with the information of all layers which could be selected from the server connected.
	 * 
	 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es)
	 * @author Jorge Piera Llodrá (piera_jor@gva.es)
	 */
	public class LayerTable extends JTable {
		private static final long serialVersionUID = 4700375945858283696L;
		private int headerSelected = -1; 

		/**
		 * Creates a new instance of <code>LayerTable</code>
		 */
		public LayerTable() {
			super();

			setModel(new LayerTableModel());

			getTableHeader().setUI(new BasicTableHeaderSelectableUI());

			getTableHeader().addMouseListener(new MouseAdapter() {
				/*
				 * (non-Javadoc)
				 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
				 */
				public void mouseClicked(MouseEvent e) {
					// Sorts according the selected column
					((LayerTableModel)getModel()).sort(getTableHeader().getColumnModel().getColumnIndexAtX(e.getX()));
				}

				/*
				 * (non-Javadoc)
				 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
				 */
				public void mousePressed(MouseEvent e) {
					headerSelected = getTableHeader().getColumnModel().getColumnIndexAtX(e.getX());
					getTableHeader().repaint();
				}

				/*
				 * (non-Javadoc)
				 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
				 */
				public void mouseReleased(MouseEvent e) {
					headerSelected = -1;
				}
			});
		}

		/*
		 * (non-Javadoc)
		 * @see javax.swing.JTable#createDefaultColumnsFromModel()
		 */
		public void createDefaultColumnsFromModel() {
			TableModel m = getModel();
			if (m != null) {
				// Remove any current columns
				TableColumnModel cm = getColumnModel();
				while (cm.getColumnCount() > 0) {
					cm.removeColumn(cm.getColumn(0));
				}

				// Create new columns from the data model info
				for (int i = 0; i < m.getColumnCount(); i++) {
					TableColumn newColumn = new TableColumn(i);

					// Special renderer for supporting selection of a particular column header
					newColumn.setHeaderRenderer(new DefaultTableSelectableCellRenderer());
					addColumn(newColumn);
				}
			}
		}

		/**
		 * Gets the selected <i>WFS</i> layer.
		 * 
		 * @return node of the selected <i>WFS</i> layer
		 */
		public WFSSelectedFeature getSelectedValue() {
			int selectedRow = getSelectedRow();
			LayerTableModel model = (LayerTableModel)getModel();

			return model.getLayerAt(selectedRow);
		}

		/**
		 * Gets the layer at a row position.
		 * 
		 * @return node of the selected <i>WFS</i> layer
		 */
		public WFSSelectedFeature getValueAt(int position) {
			if (position < this.getRowCount()) {
				LayerTableModel model = (LayerTableModel)getModel();

				return model.getLayerAt(position);			
			}

			return null;
		}

		/**
		 * Adds the available features to load as a <i>WFS</i> layer.
		 * 
		 * @param features available features
		 */
		public void addFeatures(WFSSelectedFeature[] features) {
			LayerTableModel model = (LayerTableModel)getModel();
			model.deleteAllRows();

			for (int i=0 ; i < features.length ; i++){
				model.addRow(features[i]);			
			}	
		}	

		/**
		 * Shows the names of the features.
		 * 
		 * @param showFeatureNameType The showLayerNames to set.
		 */
		public void setShowLayerNames(boolean showFeatureNameType) {
			LayerTableModel model = (LayerTableModel)getModel();
			model.setShowedFeatureNameType(showFeatureNameType);
		}

		/**
		 * Model of the <code>LayerTable</code> that stores the features available to load as a WFS layer.
		 * 
		 * @author Jorge Piera Llodrá (piera_jor@gva.es)
		 * @author Pablo Piqueras Bartolome (pablo.piqueras@iver.es)
		 */
		public class LayerTableModel extends AbstractTableModel {  
			private static final long serialVersionUID = 2722138264867593508L;
			private Vector<WFSSelectedFeature> layers = new Vector<WFSSelectedFeature>();
			private boolean showFeatureNameType = false;
			private WFSLayerStringComparator stringComparator;
			private boolean updatingSelection;
			private short previousColumnSorted;


			/**
			 * Constructs an investment table model.
			 */
			public LayerTableModel(){  
				super();

				// Alphabetical sort ordering support
				updatingSelection = false;
				previousColumnSorted = -1;
				Collator collator = Collator.getInstance(new Locale("en_EN"));		
				stringComparator = new WFSLayerStringComparator();
				stringComparator.setLocaleRules(stringComparator.new LocaleRules(true, collator));
				stringComparator.setCaseSensitive(false);
				stringComparator.setAscendingOrdering(true);
			}


			/*
			 *  (non-Javadoc)
			 * @see javax.swing.table.TableModel#getRowCount()
			 */
			public int getRowCount(){  
				return layers.size();		
			}

			/*
			 *  (non-Javadoc)
			 * @see javax.swing.table.TableModel#getColumnCount()
			 */
			public int getColumnCount(){  
				return 2;
			}

			/*
			 *  (non-Javadoc)
			 * @see javax.swing.table.TableModel#getValueAt(int, int)
			 */
			public Object getValueAt(int rowNumber, int columnNumber){  
				if (rowNumber < layers.size()) {
					WFSSelectedFeature layer = (WFSSelectedFeature)layers.get(rowNumber);

					if (columnNumber == 0) {
						return getLayerName(layer);
					} else {
						return PluginServices.getText(this, WFSUtils.getGeometry(layer));
					}
				} else {
					return "";
				}
			}	

			/**
			 * Gets the layer name displaying the type name of the feature collection associated if it's needed.
			 * 
			 * @param layer a <i>WFS</i> layer 
			 * @return the layer name
			 */
			private String getLayerName(WFSSelectedFeature layer){
				if (showFeatureNameType){
					return "[" + layer.getName() + "] " + layer.getTitle(); 
				} else {
					return layer.getTitle();
				}
			}

			/**
			 * Gets the layer at the specified position.
			 * 
			 * @param rowNumber row position
			 * @return <i>WFS</i> layer node
			 */
			public WFSSelectedFeature getLayerAt(int rowNumber){
				try {
					if (rowNumber == -1)
						return null;

					return (WFSSelectedFeature)layers.get(rowNumber);
				} catch (ArrayIndexOutOfBoundsException e) {
					NotificationManager.addError(e);
					return null;
				}
			}	

			/**
			 * Adds a new layer to the table model, each table will be represented as a row.
			 *  
			 * @param layer a new <i>WFS</i> layer
			 */
			public void addRow(WFSSelectedFeature layer){
				layers.add(layer);
				fireTableRowsInserted(getRowCount(), getRowCount());
				fireTableRowsUpdated(0,getRowCount());
			}

			/**
			 * Deletes all the table rows.
			 */
			public void deleteAllRows(){
				layers.clear();
				int rows = getRowCount();

				if (rows >= 1){
					fireTableRowsDeleted(0, rows - 1);
				}		
			}	

			/**
			 * Delete all the table rows
			 */
			public void deleteRow(int rowPosition){
				layers.remove(rowPosition);
				fireTableRowsDeleted(rowPosition, rowPosition);
				fireTableRowsUpdated(0,getRowCount());
			}	

			/*
			 *  (non-Javadoc)
			 * @see javax.swing.table.TableModel#getColumnName(int)
			 */
			public String getColumnName(int columnIndex){
				if (columnIndex == 0){
					return PluginServices.getText(this, "layerName");
				}else{
					return PluginServices.getText(this, "layerType");
				}
			}

			/**
			 * Gets if shows the feature name type.
			 * 
			 * @return <code>true</code> if shows the feature name type; <code>false</code> otherwise
			 */
			public boolean isShowedFeatureNameType() {
				return showFeatureNameType;
			}

			/**
			 * Gets if shows the feature name type.
			 * 
			 * @param showFeatureNameType <code>true</code> if shows the feature name type; <code>false</code> otherwise
			 */
			public void setShowedFeatureNameType(boolean showFeatureNameType) {
				this.showFeatureNameType = showFeatureNameType;

				this.stringComparator.setShowFeatureNameType(showFeatureNameType);
			}

			/**
			 * Sorts the rows of the table alphabetically.
			 * 
			 * @param column index of the column to sort. In this table there are only 2 columns.
			 */
			public void sort(int column) {
				// Orders the layer alphabetically according the Spanish alphabetically rules 
				switch(column) {
				case 0:
					stringComparator.setColumn(WFSLayerStringComparator.LAYER_NAME);
					break;
				case 1:
					stringComparator.setColumn(WFSLayerStringComparator.GEOMETRY_TYPE);
					break;
				}

				if (previousColumnSorted != column)
					stringComparator.setAscendingOrdering(true);

				previousColumnSorted = (short) column;

				WFSSelectedFeature layer = getSelectedValue();

				Collections.sort(layers, stringComparator);

				if (layer != null) {
					updatingSelection = true;
					unselectAllFeatures();
					int row = Collections.binarySearch(layers, layer, stringComparator);

					if (row != -1) {
						ListSelectionModel model = getSelectionModel();
						model.setLeadSelectionIndex(row);
					}

					updatingSelection = false;
				}
				stringComparator.setAscendingOrdering(!stringComparator.isAscendingOrdering());
			}

			/**
			 * Determines if now is updating the selection of the previous selected item.
			 * 
			 * @return <code>true</code> if now is updating the selection of the previous selected item; otherwise <code>false</code>
			 */
			public boolean isUpdatingSelection() {
				return updatingSelection;
			}
		}

		/**
		 * <p>Enhances {@link BasicTableHeaderUI BasicTableHeaderUI} adding support for column head selection.</p>
		 * 
		 * @see BasicTableHeaderUI
		 *
		 * @version 29/01/2008
		 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es) 
		 */
		private class BasicTableHeaderSelectableUI extends BasicTableHeaderUI {
			/**
			 * Creates a new BasicTableHeaderSelectableUI object
			 */
			public BasicTableHeaderSelectableUI() {
				super();
			}

			//
			// Some Paint Methods and support re-implemented
			//

			/*
			 * (non-Javadoc)
			 * @see javax.swing.plaf.basic.BasicTableHeaderUI#paint(java.awt.Graphics, javax.swing.JComponent)
			 */
			public void paint(Graphics g, JComponent c) {
				if (header.getColumnModel().getColumnCount() <= 0) { 
					return; 
				}

				boolean ltr = header.getComponentOrientation().isLeftToRight();

				Rectangle clip = g.getClipBounds(); 
				Point left = clip.getLocation();
				Point right = new Point( clip.x + clip.width - 1, clip.y );
				TableColumnModel cm = header.getColumnModel(); 
				int cMin = header.columnAtPoint( ltr ? left : right );
				int cMax = header.columnAtPoint( ltr ? right : left );
				// This should never happen. 
				if (cMin == -1) {
					cMin =  0;
				}
				// If the table does not have enough columns to fill the view we'll get -1.
				// Replace this with the index of the last column.
				if (cMax == -1) {
					cMax = cm.getColumnCount()-1;  
				}

				TableColumn draggedColumn = header.getDraggedColumn(); 
				int columnWidth;
				Rectangle cellRect = header.getHeaderRect(ltr ? cMin : cMax); 
				TableColumn aColumn;
				if (ltr) {
					for(int column = cMin; column <= cMax ; column++) { 
						aColumn = cm.getColumn(column); 
						columnWidth = aColumn.getWidth();
						cellRect.width = columnWidth;
						if (aColumn != draggedColumn) {
							paintCell(g, cellRect, column);
						} 
						cellRect.x += columnWidth;
					}
				} else {
					for(int column = cMax; column >= cMin; column--) {
						aColumn = cm.getColumn(column);
						columnWidth = aColumn.getWidth();
						cellRect.width = columnWidth;
						if (aColumn != draggedColumn) {
							paintCell(g, cellRect, column);
						}
						cellRect.x += columnWidth;
					}
				} 

				// Paint the dragged column if we are dragging. 
				if (draggedColumn != null) { 
					int draggedColumnIndex = viewIndexForColumn(draggedColumn); 
					Rectangle draggedCellRect = header.getHeaderRect(draggedColumnIndex); 

					// Draw a gray well in place of the moving column. 
					g.setColor(header.getParent().getBackground());
					g.fillRect(draggedCellRect.x, draggedCellRect.y,
							draggedCellRect.width, draggedCellRect.height);

					draggedCellRect.x += header.getDraggedDistance();

					// Fill the background. 
					g.setColor(header.getBackground());
					g.fillRect(draggedCellRect.x, draggedCellRect.y,
							draggedCellRect.width, draggedCellRect.height);

					paintCell(g, draggedCellRect, draggedColumnIndex);
				}

				// Remove all components in the rendererPane. 
				rendererPane.removeAll(); 
			}

			private void paintCell(Graphics g, Rectangle cellRect, int columnIndex) {
				Component component = getHeaderRenderer(columnIndex); 
				rendererPane.paintComponent(g, component, header, cellRect.x, cellRect.y,
						cellRect.width, cellRect.height, true);
			}

			private Component getHeaderRenderer(int columnIndex) { 
				TableColumn aColumn = header.getColumnModel().getColumn(columnIndex); 
				TableCellRenderer renderer = aColumn.getHeaderRenderer();

				if (renderer == null) { 
					renderer = header.getDefaultRenderer(); 
				}

				if (headerSelected == columnIndex) {
					headerSelected = -1;
					return renderer.getTableCellRendererComponent(header.getTable(), aColumn.getHeaderValue(), true, false, -1, columnIndex);
				}
				else {
					return renderer.getTableCellRendererComponent(header.getTable(), aColumn.getHeaderValue(), false, false, -1, columnIndex);
				}
			}

			private int viewIndexForColumn(TableColumn aColumn) {
				TableColumnModel cm = header.getColumnModel();

				for (int column = 0; column < cm.getColumnCount(); column++) {
					if (cm.getColumn(column) == aColumn) {
						return column;
					}
				}

				return -1;
			}
		}

		/**
		 * <p>Simulates the selection / unselection of the head of the column clicked.</p>
		 * 
		 * @version 29/01/2008
		 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es) 
		 */
		private class DefaultTableSelectableCellRenderer extends DefaultTableCellRenderer {
			private static final long serialVersionUID = -3896516869747447668L;

			/*
			 * (non-Javadoc)
			 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
			 */
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

				Color bgColor = getTableHeader().getBackground();

				if (isSelected)
					comp.setBackground(new Color(bgColor.getRed() - 10 , bgColor.getGreen() - 10, bgColor.getBlue() - 10));
				else
					comp.setBackground(bgColor);

				((JLabel)comp).setText((value == null) ? "" : value.toString());
				((JLabel)comp).setBorder(UIManager.getBorder("TableHeader.cellBorder"));

				return comp;
			}
		}

		/**
		 * Compares two chain of characters alphabetically, bearing in mind the information of a WFS layer.
		 * 
		 * @version 24/01/2008
		 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es) 
		 */
		private class WFSLayerStringComparator extends StringComparator {
			public static final short LAYER_NAME = 0;
			public static final short GEOMETRY_TYPE = 1;

			protected short column = LAYER_NAME; // by default the first column
			protected boolean showFeatureNameType = false; // by default doesn't shows the feature name type
			protected boolean ascendingOrdering = true; // by default: ascending ordering

			/*
			 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
			 */
			public int compare(Object o1, Object o2) {
				String s1 = null, s2 = null;

				switch(column) {
				case LAYER_NAME:
					if (ascendingOrdering) {
						s1 = o1.toString();
						s2 = o2.toString();
					}
					else {
						s1 = o2.toString();
						s2 = o1.toString();
					}

					if (showFeatureNameType) {
						if (ascendingOrdering) {
							return super.compare(o1, o2);
						}
						else {
							return super.compare(o2, o1);
						}
					}

					// Only if "showFeatureNameType == true" :
					int index = s1.indexOf(']');

					if (index > 0)
						s1 = s1.substring(index, s1.length()).trim();
					else
						s1 = s1.trim();

					index = s2.indexOf(']');

					if (index > 0)
						s2 = s2.substring(index, s2.length()).trim();
					else
						s2 = s2.trim();

					break;
				case GEOMETRY_TYPE:
					GMLGeometryType gType = ((WFSSelectedFeature)o1).getGeometry();

					if (gType == null)
						s1 = "";
					else
						s1 = gType.getName();

					gType = ((WFSSelectedFeature)o2).getGeometry();

					if (gType == null)
						s2 = "";
					else
						s2 = gType.getName();

					if (ascendingOrdering)
						return super.compare(s1, s2);
					else
						return super.compare(s2, s1);
				default:
					return 0;
				}

				// If localeRules is null -> use the default rules
				if (getLocaleRules() == null) {
					if (isCaseSensitive()) {
						return s1.compareTo(s2);
					}
					else {
						return s1.compareToIgnoreCase(s2);
					}
				}
				else {
					if (getLocaleRules().isUseLocaleRules()) {
						Collator collator = getLocaleRules().getCollator();

						if (isCaseSensitive()) {
							return collator.compare(s1, s2);
						}
						else {
							//return collator.compare(s1.toLowerCase(), s2.toLowerCase());
							return collator.compare(s1.toUpperCase(), s2.toUpperCase());
						}
					}
					else {
						if (isCaseSensitive()) {
							return s1.compareTo(s2);
						}
						else {
							return s1.compareToIgnoreCase(s2);
						}
					}
				}
			}

			/**
			 * Determines if the name of each element includes the type name.
			 * 
			 * @return <code>true</code> if the name of each element includes the type name; otherwise <code>false</code>
			 */
			public boolean isShowFeatureNameType() {
				return showFeatureNameType;
			}

			/**
			 * Sets if the name of each element includes the type name.
			 * 
			 * @param showFeatureNameType <code>true</code> if the name of each element includes the type name; otherwise <code>false</code>
			 */
			public void setShowFeatureNameType(boolean showFeatureNameType) {
				this.showFeatureNameType = showFeatureNameType;
			}

			/**
			 * <p>Sets which column well be alphabetically sort ordered.</p>
			 * 
			 * <p>There are two columns:
			 *  <ul>
			 *   <li><i>LAYER_NAME</i>: name of the layer.</li>
			 *   <li><i>GEOMETRY_TYPE</i>: geometry type that layer is.</li>
			 *  </ul>
			 * </p>
			 * 
			 * @param column
			 */
			public void setColumn(short column) {
				this.column = column;
			}

			/**
			 * <p>Sets if the alphabetical sort will be ascending or descending.</p>
			 * 
			 * @param b <code>true</code> if the alphabetical sort will be ascending, <code>false</code> if descending
			 */
			public void setAscendingOrdering(boolean b) {
				ascendingOrdering = b;
			}

			/**
			 * <p>Determines if the alphabetical sort will be ascending or descending.</p>
			 * 
			 * @param b <code>true</code> if the alphabetical sort will be ascending, <code>false</code> if descending
			 */
			public boolean isAscendingOrdering() {
				return ascendingOrdering;
			}
		}
	}
}