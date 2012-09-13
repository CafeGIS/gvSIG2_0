package com.iver.cit.gvsig.gui.panels;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolTip;

import org.gvsig.gui.beans.controls.MultiLineToolTip;
import org.gvsig.gui.beans.panelGroup.IPanelGroup;
import org.gvsig.remoteClient.wfs.schema.XMLElement;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.gui.panels.fieldstree.FieldsTreeTable;
import com.iver.cit.gvsig.gui.panels.fieldstree.FieldsTreeTableModel;
import com.iver.cit.gvsig.gui.panels.fieldstree.TreeTableModelWithCheckBoxes;
import com.iver.cit.gvsig.gui.panels.model.WFSSelectedFeature;

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
 * $Id$
 * $Log$
 * Revision 1.8  2007-04-11 12:10:08  ppiqueras
 * Corregido bug: si la capa no tiene campos, no permitir cargarla.
 *
 * Revision 1.7  2007/02/22 12:24:51  ppiqueras
 * Añadidas tool tip text.
 *
 * Revision 1.6  2007/02/19 11:44:42  jorpiell
 * AÃ±adidos los filtros por Ã¡rea
 *
 * Revision 1.5  2006/12/26 09:19:52  ppiqueras
 * Cambiado "atttibutes" en todas las aparaciones en atributos, mÃ©todos, clases, paquetes o comentarios por "fields". (SÃ³lo a aquellas que afectan a clases dentro del proyecto extWFS2).
 *
 * Revision 1.17  2006/12/15 13:58:25  ppiqueras
 * Eliminado un import que sobraba, y reorganizado cÃ³digo
 *
 * Revision 1.16  2006/12/04 08:59:47  ppiqueras
 * Algunos bugs corregidos. A cambio hay 2 bugs relacionados que todavÃ­a no han sido corregidos (ver PHPCollab) (los tiene asignados Jorge).
 *
 * Revision 1.15  2006/11/01 17:29:08  jorpiell
 * Se ha elimiado el nodo virtual de la raiz. Además ya se cargan los valores de un campo complejo en la pestaña del filtro
 *
 * Revision 1.14  2006/10/31 09:39:18  jorpiell
 * Cambiado el tamaño del panel
 *
 * Revision 1.13  2006/10/27 11:33:19  jorpiell
 * Añadida la treetable con los check box para seleccionar los atributos
 *
 * Revision 1.12  2006/10/24 07:58:14  jorpiell
 * Eliminado el parametro booleano que hacía que apareciesen mas de una columna en el tree table
 *
 * Revision 1.11  2006/10/24 07:27:56  jorpiell
 * Algunos cambios en el modelo que usa la tabla
 *
 * Revision 1.10  2006/10/10 12:55:06  jorpiell
 * Se ha añadido el soporte de features complejas
 *
 * Revision 1.9  2006/10/02 09:09:45  jorpiell
 * Cambios del 10 copiados al head
 *
 * Revision 1.7.2.1  2006/09/19 12:28:11  jorpiell
 * Ya no se depende de geotools
 *
 * Revision 1.8  2006/09/18 12:07:31  jorpiell
 * Se ha sustituido geotools por el driver de remoteservices
 *
 * Revision 1.7  2006/09/05 13:01:31  jorpiell
 * Reducido el tamaño de los botones
 *
 * Revision 1.6  2006/07/24 07:30:33  jorpiell
 * Se han eliminado las partes duplicadas y se está usando el parser de GML de FMAP.
 *
 * Revision 1.5  2006/07/21 11:50:31  jaume
 * improved appearance
 *
 * Revision 1.4  2006/06/21 12:35:45  jorpiell
 * Se ha añadido la ventana de propiedades. Esto implica añadir listeners por todos los paneles. Además no se muestra la geomatría en la lista de atributos y se muestran únicamnete los que se van a descargar
 *
 * Revision 1.3  2006/06/15 07:50:58  jorpiell
 * Añadida la funcionalidad de reproyectar y hechos algunos cambios en la interfaz
 *
 * Revision 1.2  2006/05/26 06:29:42  jorpiell
 * Se ha cambiado el tamaño de los botones. Además, al seleccionar todos los atributos se seleccionan en orden.
 *
 * Revision 1.1  2006/05/25 10:30:13  jorpiell
 * Esta clase se ha renombrado. WFSFields era algo confusa
 *
 * Revision 1.3  2006/05/23 08:09:39  jorpiell
 * Se ha cambiado la forma en la que se leian los valores seleccionados en los paneles y se ha cambiado el comportamiento de los botones
 *
 * Revision 1.2  2006/05/19 12:57:08  jorpiell
 * Modificados algunos paneles
 *
 * Revision 1.1  2006/04/20 16:38:24  jorpiell
 * Ahora mismo ya se puede hacer un getCapabilities y un getDescribeType de la capa seleccionada para ver los atributos a dibujar. Queda implementar el panel de opciones y hacer el getFeature().
 *
 *
 */

/**
 * <p>Panel with the fields of the feature selected of the current layer.</p>
 * 
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
 */
public class WFSSelectFieldsPanel extends AbstractWFSPanel {
	private static final long serialVersionUID = -1596729857889892588L;

	private JScrollPaneML fieldsScroll = null;
	private FieldsTreeTable fieldsTreeTable = null;
	private FieldsTreeTableModel model = null;
	private JPanel treeTablePanel = null;
	private boolean fieldsSelectedOfSameLayerHasChanged;
	private String namespace = null;

	/**
	 * Gets the name space of the layer which contains the fields stored
	 * 
	 * @return the namespace name space of the layer which contains the fields stored
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * Creates a new WFS select fields panel.
	 */
	public WFSSelectFieldsPanel(){
		super();
		initialize();
	}

	/**
	 * This method initializes jScrollPane
	 *
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getFieldsScroll() {
		if (fieldsScroll == null) {
			fieldsScroll = new JScrollPaneML();			
			fieldsScroll.setViewportView(getFieldsTreeTable());
			MultiLineToolTip tip = new MultiLineToolTip();
			tip.setComponent(fieldsScroll);
			fieldsScroll.setToolTipText(PluginServices.getText(fieldsScroll, "fields_Selection_Info"));
		}
		return fieldsScroll;
	}

	/**
	 * This method initializes lstTemps
	 *
	 * @return javax.swing.JList
	 */
	public FieldsTreeTable getFieldsTreeTable() {
		if (fieldsTreeTable == null) {
			model = new TreeTableModelWithCheckBoxes();
			fieldsTreeTable = new FieldsTreeTable(model,this);
			fieldsTreeTable.setToolTipText(PluginServices.getText(fieldsTreeTable, "fields_Selection_Info"));

			// If user has changed the fields selection in the same layer
			fieldsTreeTable.addPropertyChangeListener(new PropertyChangeListener() {
				/*
				 *  (non-Javadoc)
				 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
				 */
				public void propertyChange(PropertyChangeEvent evt) {
					// If the selection of the fields of the layer has changed
					if (evt.getPropertyName().compareTo("tableCellEditor") == 0) {
						fieldsSelectedOfSameLayerHasChanged = true;						
					}					
					else if (evt.getPropertyName().compareTo("selectionModel") == 0) { // If the layer has changed
						fieldsSelectedOfSameLayerHasChanged = false;
					}
				}
			});
		}
		return fieldsTreeTable;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.gui.panels.AbstractWFSPanel#refresh(com.iver.cit.gvsig.fmap.layers.WFSLayerNode)
	 */
	public void refresh(WFSSelectedFeature feature){
		setFields(feature);	
		setSelectedFields(feature);
	}

	/**
	 * <p>Updates the inner tree table with the fields of the layer.</p>
	 *
	 * @param feature node with the fields
	 */
	private void setFields(WFSSelectedFeature feature){
		Vector<Object> fields = feature.getFields();

		// If there are no fields
		if (fields.size() == 0) {
			return;
		}

		model = new TreeTableModelWithCheckBoxes(fields.get(0));
		getFieldsTreeTable().setModel(model);
		namespace = feature.getNameSpace();
	}

	/**
	 * <p>Changes to enabled the selection value of the selected fields.</p>
	 * 
	 * @param feature node with the fields 
	 */
	public void setSelectedFields(WFSSelectedFeature feature){
		Vector<Object> selectedFields = feature.getSelectedFields();
		getFieldsTreeTable().setSelectedFields(selectedFields);
	}

	/**
	 * Gets the name of the field which is a geometry
	 * 
	 * @return name of the field which is a geometry
	 */
	public String getGeometryFieldName(){
		return getFieldsTreeTable().getGeometryField();		
	}

	/**
	 * Gets only the selected fields.
	 *
	 * @return the selected fields
	 */
	public XMLElement[] getSelectedFields(){
		return fieldsTreeTable.getSelectedElements();
	}

	public String getSelectedFieldsAsString(){
		StringBuffer output = new StringBuffer();
		XMLElement[] selectedFields= getSelectedFields();
		for (int i=0 ; i<selectedFields.length-1 ; i++){
			output.append(selectedFields[i].getName() + ",");
		}
		if (selectedFields.length > 0){
			output.append(selectedFields[selectedFields.length-1].getName());
		}
		return output.toString();
	}

	/**
	 * This method initializes treeTablePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getTreeTablePanel() {
		if (treeTablePanel == null) {
			java.awt.GridBagConstraints gridBagConstraints;
			treeTablePanel = new JPanel();
			treeTablePanel.setLayout(new java.awt.GridBagLayout());
			treeTablePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
					null, PluginServices.getText(this, "select_fields"),
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));

			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			treeTablePanel.add(getFieldsScroll(), gridBagConstraints);
		}
		return treeTablePanel;
	}

	/**
	 * Returns true if user has changed the selection of some field of the same layer; else returns false
	 * 
	 * @return A boolean value
	 */
	public boolean getFieldsSelectedOfSameLayerHasChanged() {
		return this.fieldsSelectedOfSameLayerHasChanged;
	}

	/**
	 * <p>Changes the status to applied.</p>
	 * 
	 * @param applicable a boolean value
	 */
	public void setApplicable(boolean applicable) {
		IPanelGroup panelGroup = getPanelGroup();

		if (panelGroup == null)
			return;

		((WFSParamsPanel)panelGroup).setApplicable(applicable);
	}

	/**
	 * Resets the value of the field 'fieldsSelectedOfSameLayerHasChanged'
	 * 
	 * @return A boolean value
	 */
	public void resetFieldsSelectedOfSameLayerHasChanged() {
		this.fieldsSelectedOfSameLayerHasChanged = false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.gui.panels.AbstractWFSPanel#initialize()
	 */
	protected void initialize(){
		setLabel(PluginServices.getText(this, "fields_uppercase_first"));
		setLabelGroup(PluginServices.getText(this, "wfs"));
		this.setLayout(new java.awt.GridBagLayout());
		
		java.awt.GridBagConstraints gridBagConstraints;
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		this.add(getTreeTablePanel(), gridBagConstraints);
		fieldsSelectedOfSameLayerHasChanged = false;
	}

	/**
	 * JCrollPane with multi line tool tip text.
	 * 
	 * @see JScrollPane
	 * 
	 * @author Pablo Piqueras Bartolome (pablo.piqueras@iver.es)
	 */
	private class JScrollPaneML extends JScrollPane {
		private static final long serialVersionUID = -5589382813201358787L;

		/**
		 * @see JScrollPane#JScrollPane()
		 */
		public JScrollPaneML() {
			super();
		}

		/**
		 * @see JScrollPane#JScrollPane(Component, int, int)
		 */
		public JScrollPaneML(Component view, int vsbPolicy, int hsbPolicy) {
			super(view, vsbPolicy, hsbPolicy);
		}

		/**
		 * @see JScrollPane#JScrollPane(Component)
		 */
		public JScrollPaneML(Component view) {
			super(view);
		}

		/**
		 * @see JScrollPane#JScrollPane(int, int)
		 */
		public JScrollPaneML(int vsbPolicy, int hsbPolicy) {
			super(vsbPolicy, hsbPolicy);
		}

		/*
		 * (non-Javadoc)
		 * @see javax.swing.JComponent#createToolTip()
		 */
		public JToolTip createToolTip() {
			// Multiline support
			MultiLineToolTip tip = new MultiLineToolTip();
			tip.setComponent(this);
			return tip;
		}
	}
}
