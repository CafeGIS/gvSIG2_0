package org.gvsig.gui.beans.swing.treeTable;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

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
 * $Id: TreeTable.java 13655 2007-09-12 16:28:55Z bsanchez $
 * $Log$
 * Revision 1.2  2007-09-12 16:28:23  bsanchez
 * *** empty log message ***
 *
 * Revision 1.1  2007/08/20 08:34:46  evercher
 * He fusionado LibUI con LibUIComponents
 *
 * Revision 1.4  2007/01/17 17:18:29  jorpiell
 * Añadido un método para expandir todo el árbol
 *
 * Revision 1.3  2006/10/25 14:45:52  jorpiell
 * Añadidos los renderers al arbol
 *
 * Revision 1.2  2006/10/23 11:13:55  jorpiell
 * Añadida la posibilidad de recargar el árbol tantas veces como queramos
 *
 * Revision 1.1  2006/10/23 08:18:39  jorpiell
 * Añadida la clase treetable
 *
 *
 */
/**
 * To create a new tree table it is necessary to create a new
 * treetablemodel. See the ExampleModel.java.
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */

public class TreeTable extends JTable {
  private static final long serialVersionUID = 6630322246955414756L;
	protected TreeTableCellRenderer tree;
		
	public TreeTable(TreeTableModel treeTableModel) {
		super();
	    setModel(treeTableModel);  
	}
	
	public TreeTable(TreeTableModel treeTableModel,TreeCellRenderer cellRenderer,TreeCellEditor cellEditor) {
		super();
	    setModel(treeTableModel,cellRenderer,cellEditor);  
	}
	
	public void setModel(TreeTableModel treeTableModel,TreeCellRenderer cellRenderer,TreeCellEditor cellEditor){
		tree = new TreeTableCellRenderer(treeTableModel,this,cellRenderer,cellEditor);
		super.setModel(new TreeTableModelAdapter(treeTableModel, tree));
		setModel();
	}
	
	public void setModel(TreeTableModel treeTableModel){
		tree = new TreeTableCellRenderer(treeTableModel,this); 
		super.setModel(new TreeTableModelAdapter(treeTableModel, tree));
		setModel();
	}
	
	private void setModel(){
		// Force the JTable and JTree to share their row selection models. 
		tree.setSelectionModel(new DefaultTreeSelectionModel() { 
      	private static final long serialVersionUID = 2719965083562067462L;
			// Extend the implementation of the constructor, as if: 
			/* public this() */ {
				setSelectionModel(listSelectionModel); 
			} 
		}); 
		// Make the tree and table row heights the same. 
		tree.setRowHeight(getRowHeight());
		
		// Install the tree editor renderer and editor. 
		setDefaultRenderer(TreeTableModel.class, tree); 
		setDefaultEditor(TreeTableModel.class, new TreeTableCellEditor());  
		
		setShowGrid(false);
		setIntercellSpacing(new Dimension(0, 0)); 
	}
	
	/* Workaround for BasicTableUI anomaly. Make sure the UI never tries to 
	 * paint the editor. The UI currently uses different techniques to 
	 * paint the renderers and editors and overriding setBounds() below 
	 * is not the right thing to do for an editor. Returning -1 for the 
	 * editing row in this case, ensures the editor is never painted. 
	 */
	public int getEditingRow() {
		return (getColumnClass(editingColumn) == TreeTableModel.class) ? -1 : editingRow;  
	}

	
	public class TreeTableCellEditor extends AbstractCellEditor implements TableCellEditor {
		public Component getTableCellEditorComponent(JTable table, Object value,
				boolean isSelected, int r, int c) {
			return tree;
		}
	}
	
	/**
	 * Deletes all the tree icons
	 *
	 */
	public void deleteIcons(){
		setLeafIcon(null);
	    setClosedIcon(null);
	    setOpenIcon(null);
	}
	
	/**
	 * Sets the root visible
	 */
	public void setRootVisible(boolean rootVisible){
		tree.setRootVisible(rootVisible);
	}
	
	/**
	 * Sets the leaftIcon
	 * @param newIcon
	 */
	public void setLeafIcon(Icon newIcon){
		TreeTableCellRenderer renderer = (TreeTableCellRenderer)getTree();
		DefaultTreeCellRenderer treeRenderer = (DefaultTreeCellRenderer)renderer.getCellRenderer();
		treeRenderer.setLeafIcon(newIcon);
	}
	
	/**
	 * Sets the open icon
	 * @param newIcon
	 */
	public void setOpenIcon(Icon newIcon){
		TreeTableCellRenderer renderer = (TreeTableCellRenderer)getTree();
		DefaultTreeCellRenderer treeRenderer = (DefaultTreeCellRenderer)renderer.getCellRenderer();
		treeRenderer.setOpenIcon(newIcon);
	}
	
	/**
	 * Sets the close icon
	 * @param newIcon
	 */
	public void setClosedIcon(Icon newIcon){
		TreeTableCellRenderer renderer = (TreeTableCellRenderer)getTree();
		DefaultTreeCellRenderer treeRenderer = (DefaultTreeCellRenderer)renderer.getCellRenderer();
		treeRenderer.setClosedIcon(newIcon);
	}

	public Component getTree() {
		return tree;
	}
	
	public int expandJTreeNode (Object node, int row){
		TreeModel model = tree.getModel();
		if (node != null  &&  !model.isLeaf(node)){
			tree.expandRow(row);
			for (int index = 0;	row + 1 < tree.getRowCount()  &&  index < model.getChildCount(node); index++){
				row++;
				Object child = model.getChild(node, index);
				if (child == null)
					break;
				javax.swing.tree.TreePath path;
				while ((path = tree.getPathForRow(row)) != null  &&	path.getLastPathComponent() != child){
					tree.expandRow(row);
					row++;					
				}
				if (path == null)
					break;
				row = expandJTreeNode(child, row);				
			}
		}
		return row;
	}
	
}

