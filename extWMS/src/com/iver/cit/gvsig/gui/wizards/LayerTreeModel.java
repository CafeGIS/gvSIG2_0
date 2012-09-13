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
package com.iver.cit.gvsig.gui.wizards;

import java.util.ArrayList;
import java.util.Vector;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.iver.cit.gvsig.fmap.layers.WMSLayerNode;


public class LayerTreeModel implements TreeModel {

	WMSLayerNode root;

	public LayerTreeModel(WMSLayerNode root){
		this.root = root;
	}

	public Object getRoot() {
		return root;
	}

	public int getChildCount(Object parent) {
		return ((WMSLayerNode)parent).getChildren().size();
	}

    public boolean isLeaf(Object node) {
		return ((WMSLayerNode)node).getChildren().size() == 0;
	}

	public void addTreeModelListener(TreeModelListener l) {
	}

	public void removeTreeModelListener(TreeModelListener l) {
	}

	public Object getChild(Object parent, int index) {
		return (WMSLayerNode)((WMSLayerNode)parent).getChildren().get(index);
	}

	public int getIndexOfChild(Object parent, Object child) {
        WMSLayerNode pare = (WMSLayerNode) parent;
		for (int i = 0; i < pare.getChildren().size(); i++)
			if (child == pare.getChildren().get(i)) return i;
		return -1;
	}

	public void valueForPathChanged(TreePath path, Object newValue) {
	}

    /**
     * @param node, the tree's desired node.
     * @return Returns the name of the node.
     */
    public String getName(Object node) {
        return ((WMSLayerNode) node).getName();
    }
    
    /**
     * @param node, the tree's desired node.
     * @return Returns the namedStyles.
     */
    public ArrayList getStyles(Object node) {
        return ((WMSLayerNode) node).getStyles();
    }
    /**
     * @param node, the tree's desired node.
     * @return Returns the queryable.
     */
    public boolean isQueryable(Object node) {
        return ((WMSLayerNode) node).isQueryable();
    }
    
    /**
     * @param node, the tree's desired node.
     * @return Returns the srs.
     */
    public Vector getSrs(Object node) {
        return ((WMSLayerNode) node).getAllSrs();
    }
    /**
     * @param node, the tree's desired node.
     * @return Returns the title.
     */
    public String getTitle(Object node) {
        return ((WMSLayerNode) node).getTitle();
    }
    /**
     * @param node, the tree's desired node.
     * @return Returns the transparency.
     */
    public boolean hasTransparency(Object node) {
        return ((WMSLayerNode) node).isTransparent();
    }
    
    public String toString(){
        return getTitle(this);
    }
    
    /**
     * Searches in the tree for the first (and must be the unique) WMSLayerNode with
     * the name specified by name.
     * @param name
     * @return
     */
    public Object getNodeByName(String name) {
    	return getNodeByName(root, name);
    }
    
    /**
     * Service method.
     * @param node
     * @param name
     * @return
     */
    private Object getNodeByName(WMSLayerNode node, String name) {
    	WMSLayerNode n = node;
    	if (n.getName()!= null && n.getName().equals(name)) {
    		return n;
    	} else {
    		for (int i = 0; i < getChildCount(n); i++) {
    			WMSLayerNode n1 = (WMSLayerNode) n.getChildren().get(i);
    			Object obj = getNodeByName(n1, name);
    			if (obj!= null)
    				return obj;
			}
    	}
    	return null;
    }
}
