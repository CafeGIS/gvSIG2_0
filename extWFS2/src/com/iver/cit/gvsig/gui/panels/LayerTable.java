package com.iver.cit.gvsig.gui.panels;

import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.gui.panels.model.WFSSelectedFeature;
import com.iver.cit.gvsig.gui.panels.model.WFSUtils;

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
 * $Id: LayerTable.java 8847 2006-11-17 11:29:00Z ppiqueras $
 * $Log$
 * Revision 1.4.2.3  2006-11-17 11:28:45  ppiqueras
 * Corregidos bugs y aÃ±adida nueva funcionalidad.
 *
 * Revision 1.6  2006/10/02 09:09:45  jorpiell
 * Cambios del 10 copiados al head
 *
 * Revision 1.4.2.1  2006/09/19 12:28:11  jorpiell
 * Ya no se depende de geotools
 *
 * Revision 1.5  2006/09/18 12:07:31  jorpiell
 * Se ha sustituido geotools por el driver de remoteservices
 *
 * Revision 1.4  2006/07/24 07:30:33  jorpiell
 * Se han eliminado las partes duplicadas y se está usando el parser de GML de FMAP.
 *
 * Revision 1.3  2006/06/21 12:35:45  jorpiell
 * Se ha añadido la ventana de propiedades. Esto implica añadir listeners por todos los paneles. Además no se muestra la geomatría en la lista de atributos y se muestran únicamnete los que se van a descargar
 *
 * Revision 1.2  2006/05/25 16:01:43  jorpiell
 * Se ha añadido la funcionalidad para eliminar el namespace de los tipos de atributos
 *
 * Revision 1.1  2006/05/25 10:29:50  jorpiell
 * Añadida la clase
 *
 *
 */
/**
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class LayerTable extends JTable{
	public LayerTable() {
		super();
		setModel(new LayerTableModel());
	}
	
	/**
	 * Gets the selected layer
	 * @return
	 */
	public WFSSelectedFeature getSelectedValue(){
		int selectedRow = getSelectedRow();
		LayerTableModel model = (LayerTableModel)getModel();
		return model.getLayerAt(selectedRow);
	}
	
	public WFSSelectedFeature getValueAt(int position){
		if (position<this.getRowCount()){
			LayerTableModel model = (LayerTableModel)getModel();
			return model.getLayerAt(position);			
		}
		return null;
	}
	
	/**
	 * Adds a vector of features
	 * @param features
	 */
	public void addFeatures(Object[] features){
		LayerTableModel model = (LayerTableModel)getModel();
		model.deleteAllRows();
		for (int i=0 ; i<features.length ; i++){
			model.addRow((WFSSelectedFeature)features[i]);			
		}			
	}	
	
	/**
	 * @param showLayerNames The showLayerNames to set.
	 */
	public void setShowLayerNames(boolean showLayerNames) {
		LayerTableModel model = (LayerTableModel)getModel();
		model.setShowLayerNames(showLayerNames);
	}
	
	/**
	 * Table model
	 * @author Jorge Piera Llodrá (piera_jor@gva.es)
	 *
	 */
	public class LayerTableModel extends AbstractTableModel{  
		private Vector layers = new Vector();
		private boolean showLayerNames = false;
		
		/**
		 Constructs an investment table model.
		 */
		public LayerTableModel(){  
			super();
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
			if (rowNumber < layers.size()){
				WFSSelectedFeature layer = (WFSSelectedFeature)layers.get(rowNumber);
				if (columnNumber == 0){
					return getLayerName(layer);
				}else{
					return PluginServices.getText(this,WFSUtils.getGeometry(layer));
				}
			}else{
				return "";
			}
		}	
		
		/**
		 * Returns the layer name (depending of the "show layer names"
		 * check cob is clicked)
		 * @param layer
		 * @return
		 */
		private String getLayerName(WFSSelectedFeature layer){
			if (showLayerNames){
				return "[" + layer.getName() + "] " + layer.getTitle(); 
			} else {
				return layer.getTitle();
			}
		}
		
		/**
		 * Return the layer at the specified position
		 * @param rowNumber
		 * Row position
		 * @return
		 */
		public WFSSelectedFeature getLayerAt(int rowNumber){  
			try{
				return (WFSSelectedFeature)layers.get(rowNumber);
			}catch (ArrayIndexOutOfBoundsException e){
				return null;
			}
		}	
		
		/**
		 * It adds a new 
		 * @param values
		 * Array of column values
		 */
		public void addRow(WFSSelectedFeature layer){
			layers.add(layer);		
			fireTableRowsInserted(getRowCount(),getRowCount());
			fireTableRowsUpdated(0,getRowCount());
		}
		
		/**
		 * Delete all the table rows
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
				return PluginServices.getText(this,"layerName");
			}else{
				return PluginServices.getText(this,"layerType");
			}
		}

		/**
		 * @return Returns the showLayerNames.
		 */
		public boolean isShowLayerNames() {
			return showLayerNames;
		}

		/**
		 * @param showLayerNames The showLayerNames to set.
		 */
		public void setShowLayerNames(boolean showLayerNames) {
			this.showLayerNames = showLayerNames;
		}
	}
}
