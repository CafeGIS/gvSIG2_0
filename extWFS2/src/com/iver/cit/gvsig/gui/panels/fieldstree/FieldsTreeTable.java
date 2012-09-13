package com.iver.cit.gvsig.gui.panels.fieldstree;

import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JToolTip;
import javax.swing.JTree;

import org.gvsig.gui.beans.controls.MultiLineToolTip;
import org.gvsig.gui.beans.swing.treeTable.TreeTableModelAdapter;
import org.gvsig.remoteClient.wfs.schema.XMLElement;
import org.gvsig.remoteClient.wfs.schema.type.IXMLType;

import com.iver.cit.gvsig.gui.panels.WFSSelectFieldsPanel;

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
 * Revision 1.4  2007-09-19 16:14:50  jaume
 * removed unnecessary imports
 *
 * Revision 1.3  2007/03/15 13:35:47  ppiqueras
 * Corregido bug: lanzaba una excepción en el método de búsqueda de campo geometría cuando el nodo era null.
 *
 * Revision 1.2  2007/02/19 11:44:42  jorpiell
 * AÃ±adidos los filtros por Ã¡rea
 *
 * Revision 1.1  2006/12/26 09:12:48  ppiqueras
 * Cambiado "atttibutes" en todas las aparaciones en atributos, mÃ©todos, clases, paquetes o comentarios por "fields". (SÃ³lo a aquellas que afectan a clases dentro del proyecto extWFS2). (En este caso se ha cambiado el nombre del paquete aparte de alguno otro nombre que puede haber cambiado).
 *
 * Revision 1.5  2006/11/15 17:37:39  jorpiell
 * Se copia el padre
 *
 * Revision 1.4  2006/11/10 13:14:47  ppiqueras
 * Corregido un bug: caos que haya campos de una feature de una capa WFS, que no tengan tipo (tipo == null).
 *
 * Revision 1.3  2006/11/01 17:29:07  jorpiell
 * Se ha elimiado el nodo virtual de la raiz. Además ya se cargan los valores de un campo complejo en la pestaña del filtro
 *
 * Revision 1.2  2006/10/31 09:39:44  jorpiell
 * Se devuelve el tipo de la entidad completo, y no sus hijos
 *
 * Revision 1.1  2006/10/27 11:33:19  jorpiell
 * Añadida la treetable con los check box para seleccionar los atributos
 *
 * Revision 1.3  2006/10/24 10:13:19  jorpiell
 * Se ha eliminado la pestaña de filter
 *
 * Revision 1.2  2006/10/24 07:58:14  jorpiell
 * Eliminado el parametro booleano que hacía que apareciesen mas de una columna en el tree table
 *
 * Revision 1.1  2006/10/24 07:27:56  jorpiell
 * Algunos cambios en el modelo que usa la tabla
 *
 *
 */

/**
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class FieldsTreeTable extends org.gvsig.gui.beans.swing.treeTable.TreeTable {
	WFSSelectFieldsPanel parent = null;
	
	public FieldsTreeTable(FieldsTreeTableModel treeTableModel) {
		super(treeTableModel);
		JTree tree = (JTree)getTree();
		tree.setCellRenderer(new CheckBoxTreeCellRenderer(this));
		tree.setCellEditor(new CheckBoxTreeCellEditor(this));
		tree.setEditable(true);		
	}

	public FieldsTreeTable(FieldsTreeTableModel treeTableModel,WFSSelectFieldsPanel parent) {
		super(treeTableModel);
		this.parent = parent;
		JTree tree = (JTree)getTree();
		tree.setCellRenderer(new CheckBoxTreeCellRenderer(this));
		tree.setCellEditor(new CheckBoxTreeCellEditor(this));
		tree.setEditable(true);
	}

	/**
	 * Sets the fields
	 * @param fields
	 */
	public void setFields(Vector vFields){
		Object[] fields = new Object[vFields.size()];
		for (int i=0 ; i<vFields.size() ; i++){
			fields[i] = vFields.get(i);
		}
		setModel(new FieldsTreeTableModel(fields));
		deleteIcons();
		setRootVisible(false);
	}

	/**
	 * Gets the selected layer
	 * @return
	 */
	public Object[] getSelectedValues(){
		int[] selectedRows = getSelectedRows();
		TreeTableModelAdapter obj = (TreeTableModelAdapter)getModel();
		Object[] objects = new Object[selectedRows.length];
		for (int i=0 ; i<selectedRows.length ; i++){
			objects[i] = obj.nodeForRow(selectedRows[i]);
		}
		return objects;
	}

	/**
	 * return the selected elements
	 * @return
	 */
	public XMLElement[] getSelectedElements(){
		TreeTableModelAdapter obj = (TreeTableModelAdapter)getModel();
		ArrayList elements = new ArrayList();
		for (int i=0 ; i< obj.getRowCount() ; i++){
			if (obj.nodeForRow(i) instanceof CheckBoxNode){
				CheckBoxNode node = (CheckBoxNode)obj.nodeForRow(i);
				if (node.isSelected()){
					XMLElement element = node.getElement();
					CheckBoxNode parentNode = node.getParentNode();
					if (parentNode != null){
						element.setParentElement(parentNode.getElement());
					}
					elements.add(element);
				}					
			}
		}
	
		XMLElement[] selected = new XMLElement[elements.size()];
		for (int i=0 ; i<elements.size() ; i++){
			selected[i] = (XMLElement)elements.get(i);
		}
		return selected;
	}
	
	
	public void setModel(FieldsTreeTableModel treeTableModel){
		super.setModel(treeTableModel);
		JTree tree = (JTree)getTree();
		tree.setCellRenderer(new CheckBoxTreeCellRenderer(this));
		tree.setCellEditor(new CheckBoxTreeCellEditor(this));
		tree.setEditable(true);		
	}
	
	/**
	 * Gets the geometry field
	 * @return
	 */
	public String getGeometryField(){
		TreeTableModelAdapter obj = (TreeTableModelAdapter)getModel();
		ArrayList elements = new ArrayList();
		for (int i=0 ; i< obj.getRowCount() ; i++){
			if (obj.nodeForRow(i) instanceof CheckBoxNode){
				CheckBoxNode node = (CheckBoxNode)obj.nodeForRow(i);
				XMLElement element = node.getElement();
				if (element.getEntityType() != null) {
					if (element.getEntityType().getType() == IXMLType.GML_GEOMETRY){
						return element.getName();
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Set the selected fields
	 * @param selectedFields
	 */
	public void setSelectedFields(Vector selectedFields) {
		TreeTableModelAdapter obj = (TreeTableModelAdapter)getModel();
		JTree tree = (JTree)getTree();
	
		for (int i = 0; i < tree.getRowCount(); i++) {
			tree.expandRow(i);
		}	
		for (int i=0 ; i<obj.getRowCount() ; i++){
			if (obj.nodeForRow(i) instanceof CheckBoxNode){
				XMLElement element = ((CheckBoxNode)obj.nodeForRow(i)).getElement();
				for (int j=0 ; j<selectedFields.size() ; j++){
					//If the name is equals
					if (((XMLElement)selectedFields.get(j)).getName().equals(element.getName())){
						//If the type is equals					
						if ( (element.getEntityType() != null) && (((XMLElement)selectedFields.get(j)).getEntityType() != null) && (((XMLElement)selectedFields.get(j)).getEntityType().getName().equals(element.getEntityType().getName())) ){
							((CheckBoxNode)obj.nodeForRow(i)).setSelected(true);
						}
					}
				}
			}			
		}
		
	}
	
	/**
	 * <p>Changes the status to applied.</p>
	 * 
	 * @param applicable a boolean value
	 */
	public void setApplicable(boolean applicable) {
		if (parent != null){
			parent.setApplicable(applicable);
		}
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
