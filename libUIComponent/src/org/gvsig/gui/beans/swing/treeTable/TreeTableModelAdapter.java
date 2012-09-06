package org.gvsig.gui.beans.swing.treeTable;

import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.TreePath;

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
 * $Id: TreeTableModelAdapter.java 13655 2007-09-12 16:28:55Z bsanchez $
 * $Log$
 * Revision 1.2  2007-09-12 16:28:23  bsanchez
 * *** empty log message ***
 *
 * Revision 1.1  2007/08/20 08:34:46  evercher
 * He fusionado LibUI con LibUIComponents
 *
 * Revision 1.3  2006/10/25 14:45:52  jorpiell
 * Añadidos los renderers al arbol
 *
 * Revision 1.2  2006/10/24 08:04:13  jorpiell
 * nodeFroRow es ahora public
 *
 * Revision 1.1  2006/10/23 08:18:39  jorpiell
 * Añadida la clase treetable
 *
 *
 */
/**
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class TreeTableModelAdapter extends AbstractTableModel{
  private static final long serialVersionUID = -5509267189480739255L;
	JTree tree;
	TreeTableModel treeTableModel;
	
	public TreeTableModelAdapter(TreeTableModel treeTableModel, JTree tree) {
		this.tree = tree;
		this.treeTableModel = treeTableModel;
		
		tree.addTreeExpansionListener(new TreeExpansionListener() {
			// Don't use fireTableRowsInserted() here; 
			// the selection model would get  updated twice. 
			public void treeExpanded(TreeExpansionEvent event) {  
				fireTableDataChanged(); 
			}
			public void treeCollapsed(TreeExpansionEvent event) {  
				fireTableDataChanged(); 
			}
		});
	}
	
	// Wrappers, implementing TableModel interface. 
	
	public int getColumnCount() {
		return treeTableModel.getColumnCount();
	}
	
	public String getColumnName(int column) {
		return treeTableModel.getColumnName(column);
	}
	
	public Class getColumnClass(int column) {
		return treeTableModel.getColumnClass(column);
	}
	
	public int getRowCount() {
		return tree.getRowCount();
	}
	
	public Object nodeForRow(int row) {
		TreePath treePath = tree.getPathForRow(row);
		return treePath.getLastPathComponent();         
	}
	
	public Object getValueAt(int row, int column) {
		return treeTableModel.getValueAt(nodeForRow(row), column);
	}
	
	public boolean isCellEditable(int row, int column) {
		return treeTableModel.isCellEditable(nodeForRow(row), column);
	}
	
	public void setValueAt(Object value, int row, int column) {
		treeTableModel.setValueAt(value, nodeForRow(row), column);
	}
}


