package com.iver.cit.gvsig.gui.panels.fieldstree;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;

import org.gvsig.remoteClient.wfs.schema.type.IXMLType;

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
 * Revision 1.3  2007-09-19 16:14:50  jaume
 * removed unnecessary imports
 *
 * Revision 1.2  2006/12/26 10:25:37  ppiqueras
 * Corregidas las dependencias con las nuevas ubicaciones de clases: IXMLType, XMLElement, IXMLComplexType, etc. (en libRemoteServices)
 *
 * Revision 1.1  2006/12/26 09:12:48  ppiqueras
 * Cambiado "atttibutes" en todas las aparaciones en atributos, mÃ©todos, clases, paquetes o comentarios por "fields". (SÃ³lo a aquellas que afectan a clases dentro del proyecto extWFS2). (En este caso se ha cambiado el nombre del paquete aparte de alguno otro nombre que puede haber cambiado).
 *
 * Revision 1.2  2006/10/31 12:24:04  jorpiell
 * Comprobado el caso en el que los atributos no tienen tipo
 *
 * Revision 1.1  2006/10/27 11:33:19  jorpiell
 * Añadida la treetable con los check box para seleccionar los atributos
 *
 *
 */
/**
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class CheckBoxTreeCellEditor extends AbstractCellEditor implements TreeCellEditor {
	CheckBoxTreeCellRenderer renderer = null;
	ChangeEvent changeEvent = null;
	JTree tree;
	FieldsTreeTable treetable;
	
	public CheckBoxTreeCellEditor(FieldsTreeTable treetable) {
		this.treetable = treetable;
		this.tree = (JTree)treetable.getTree();
		this.renderer = new CheckBoxTreeCellRenderer(treetable);
	}	
	
	/*
	 *  (non-Javadoc)
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	public Object getCellEditorValue() {
		return null;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see javax.swing.CellEditor#isCellEditable(java.util.EventObject)
	 */
	public boolean isCellEditable(EventObject event) {
		if (event instanceof MouseEvent) {
			MouseEvent mouseEvent = (MouseEvent) event;
			TreePath path = tree.getPathForLocation(mouseEvent.getX(),
					mouseEvent.getY());
			if (path != null) {
				Object node = path.getLastPathComponent();
				if ((node != null) && (node instanceof CheckBoxNode)) {
					CheckBoxNode selectedNode = (CheckBoxNode)node;
					if (mouseEvent.getClickCount() == 2){
						
					}else if(mouseEvent.getClickCount() == 1){
						changeAllChildren(selectedNode,!selectedNode.isSelected());
						changeParentState(selectedNode,!selectedNode.isSelected());
						tree.repaint();
					}
				}
			}
		}
		return false;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see javax.swing.tree.TreeCellEditor#getTreeCellEditorComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int)
	 */
	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row) {
		Component editor = renderer.getTreeCellRendererComponent(tree, value,
				true, expanded, leaf, row, true);
		
		if (editor instanceof JCheckBox) {
			//((JCheckBox) editor).addItemListener(new CheckBoxListener());
		}		
		return editor;
	}	
	
	/**
	 * It enable or disable the parent node state depending of its children values
	 * @param selectedNode
	 * Selected node
	 * @param isSelected
	 * Current node status
	 */
	private void changeParentState(CheckBoxNode selectedNode,boolean isSelected){
		CheckBoxNode parent = selectedNode.getParentNode();
		while (parent != null){			
			if (isSelected != parent.isSelected()){
				parent.setColor(TetraStateCheckBox.GREY);
			}else{	
				boolean isEnabled = true;
				for (int i=0 ; i<parent.getChildren().size() ; i++){
					CheckBoxNode child = (CheckBoxNode)parent.getChildren().get(i);
					if (child != selectedNode){
						if (parent.isSelected() != child.isSelected()){
							isEnabled = false;
						}
					}
				}
				if (!isEnabled){
					parent.setColor(TetraStateCheckBox.GREY);
				}else{
					parent.setColor(TetraStateCheckBox.WHITE);
				}					
				
			}			
			selectedNode = parent;
			parent = selectedNode.getParentNode();			
		}
	}
	
	
	/**
	 * It changes all the children status
	 * @param selectedNode
	 * Root node
	 * @param selected
	 * New state
	 */
	private void changeAllChildren(CheckBoxNode selectedNode,boolean selected){
		for (int i=0 ; i<selectedNode.getChildren().size() ; i++){
			CheckBoxNode child = ((CheckBoxNode)selectedNode.getChildren().get(i));
			if ((child.getElement().getEntityType() == null) || 
					(child.getElement().getEntityType().getType() != IXMLType.GML_GEOMETRY)){
				child.setSelected(selected);	
			}
			if (child.getChildren().size() > 0){
				changeAllChildren(child,selected);
			}			
		}
	}
}
