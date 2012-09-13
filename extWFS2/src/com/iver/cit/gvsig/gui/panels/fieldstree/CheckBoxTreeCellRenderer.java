package com.iver.cit.gvsig.gui.panels.fieldstree;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultTreeCellRenderer;
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
 * Revision 1.3  2006/10/31 12:24:04  jorpiell
 * Comprobado el caso en el que los atributos no tienen tipo
 *
 * Revision 1.2  2006/10/31 09:39:44  jorpiell
 * Se devuelve el tipo de la entidad completo, y no sus hijos
 *
 * Revision 1.1  2006/10/27 11:33:19  jorpiell
 * Añadida la treetable con los check box para seleccionar los atributos
 *
 *
 */
/**
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class CheckBoxTreeCellRenderer extends DefaultTreeCellRenderer{ 
	private JTree tree;
	private FieldsTreeTable treetable;
	
	public CheckBoxTreeCellRenderer(FieldsTreeTable treetable) {
		this.treetable = treetable;
		this.tree = (JTree)treetable.getTree();
	}
	
	/*
	 *  (non-Javadoc)
	 * @see javax.swing.tree.TreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	 */
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
			int[] rows = tree.getSelectionRows();
			TreePath path = tree.getPathForRow(row);
			if (path != null){
				Object node = path.getLastPathComponent();
				if ((node != null) && (node instanceof CheckBoxNode)){
					CheckBoxNode selectedNode = (CheckBoxNode)node;
					if (selectedNode.getParent() == null){
						tree.expandRow(0);
					}
					if (selected){
						selectedNode.setBackground(UIManager.getColor("Tree.selectionBackground"));
						if (hasFocus){
							if ((selectedNode.getElement().getEntityType() == null) || 
								!(selectedNode.getElement().getEntityType().getType() == IXMLType.GML_GEOMETRY)){
								treetable.setApplicable(true);
								selectedNode.setSelected(!selectedNode.isSelected());
								selectedNode.setColor(TetraStateCheckBox.WHITE);
							}
						}
					}else{
						selectedNode.setBackground(UIManager.getColor("Tree.textBackground"));
					}					
					return selectedNode;
				}
			}	
			JLabel label = new JLabel(CheckBoxNode.fillNameWithBlancs("Feature"));
		return label;
	}	

	
}
