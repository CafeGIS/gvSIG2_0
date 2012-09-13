package com.iver.cit.gvsig.gui.panels.fieldstree;

import org.gvsig.remoteClient.wfs.schema.XMLElement;

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
 * Revision 1.1  2006-12-26 09:12:48  ppiqueras
 * Cambiado "atttibutes" en todas las aparaciones en atributos, mÃ©todos, clases, paquetes o comentarios por "fields". (SÃ³lo a aquellas que afectan a clases dentro del proyecto extWFS2). (En este caso se ha cambiado el nombre del paquete aparte de alguno otro nombre que puede haber cambiado).
 *
 * Revision 1.2  2006/11/01 17:29:07  jorpiell
 * Se ha elimiado el nodo virtual de la raiz. Además ya se cargan los valores de un campo complejo en la pestaña del filtro
 *
 * Revision 1.1  2006/10/27 11:33:19  jorpiell
 * Añadida la treetable con los check box para seleccionar los atributos
 *
 *
 */
/**
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class TreeTableModelWithCheckBoxes extends FieldsTreeTableModel{

	public TreeTableModelWithCheckBoxes() {
		super();
	}

	public TreeTableModelWithCheckBoxes(Object root) {
		super(generateCheckBoxNodes(root));		
	}
	
	/*
	 *  (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
	 */ 
	public boolean isLeaf(Object node) { 
		CheckBoxNode leaf = (CheckBoxNode)node;	
		return super.isLeaf(leaf.getElement());
	}
	
	/*
	 *  (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
	 */
	public int getChildCount(Object node) { 
		CheckBoxNode leaf = (CheckBoxNode)node;	
		return super.getChildCount(leaf.getElement());
	}
	
	/*
	 *  (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
	 */
	public Object getChild(Object node, int i) { 
		CheckBoxNode leaf = (CheckBoxNode)node;	
		if (leaf.getChildren().size() > 0){
			return leaf.getChildren().get(i);
		}
		return null;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.gui.beans.swing.treeTable.TreeTableModel#getValueAt(java.lang.Object, int)
	 */
	public Object getValueAt(Object node, int column) {
		CheckBoxNode leaf = (CheckBoxNode)node;	
		return super.getValueAt(leaf.getElement(),column);
	}
	
	public static Object generateCheckBoxNodes(Object root){
		Object newRoot = new CheckBoxNode((XMLElement)root,null);
		return newRoot;
	}	
	
}
