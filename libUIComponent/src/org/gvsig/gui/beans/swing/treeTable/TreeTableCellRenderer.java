package org.gvsig.gui.beans.swing.treeTable;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.TableCellRenderer;
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
 * $Id: TreeTableCellRenderer.java 13655 2007-09-12 16:28:55Z bsanchez $
 * $Log$
 * Revision 1.3  2007-09-12 16:28:23  bsanchez
 * *** empty log message ***
 *
 * Revision 1.2  2007/08/21 09:47:17  bsanchez
 * - Quitados warnings en imports innecesarios
 *
 * Revision 1.1  2007/08/20 08:34:46  evercher
 * He fusionado LibUI con LibUIComponents
 *
 * Revision 1.2  2006/10/25 14:45:52  jorpiell
 * Añadidos los renderers al arbol
 *
 * Revision 1.1  2006/10/23 08:18:39  jorpiell
 * Añadida la clase treetable
 *
 *
 */
/**
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class TreeTableCellRenderer extends JTree implements TableCellRenderer {
  private static final long serialVersionUID = 1017457033135612066L;
	private int visibleRow;
	private TreeTable treeTable;
	
	public TreeTableCellRenderer(TreeModel model,TreeTable treetable) { 
		super(model); 		
		this.treeTable = treetable;
	}
	
	public TreeTableCellRenderer(TreeModel model,TreeTable treetable,TreeCellRenderer cellRenderer,TreeCellEditor cellEditor){ 
		super(model); 
		this.setCellRenderer(cellRenderer);
		this.setCellEditor(cellEditor);
		this.treeTable = treetable;		    
	}
	
	public void setBounds(int x, int y, int w, int h) {
		super.setBounds(x, 0, w, treeTable.getHeight());
	}
	
	public void paint(Graphics g) {
		g.translate(0, -visibleRow * getRowHeight());
		super.paint(g);
	}
	
	public Component getTableCellRendererComponent(JTable table,
			Object value,
			boolean isSelected,
			boolean hasFocus,
			int row, int column) {
		if(isSelected)
			setBackground(table.getSelectionBackground());
		else
			setBackground(table.getBackground());
		
		visibleRow = row;
		return this;
	}
}