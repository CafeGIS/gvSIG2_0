/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
 *   Av. Blasco Ib��ez, 50
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
* $Id: StyleTree.java 4222 2006-02-28 15:26:59Z jaume $
* $Log$
* Revision 1.5  2006-02-28 15:25:14  jaume
* *** empty log message ***
*
* Revision 1.2.2.15  2006/02/20 15:23:08  jaume
* Se muestran algunas de las capas de aes
*
* Revision 1.2.2.14  2006/02/20 08:48:36  jaume
* *** empty log message ***
*
* Revision 1.2.2.13  2006/02/17 13:06:12  jaume
* y las imagenes que faltan
*
* Revision 1.2.2.12  2006/02/17 12:57:34  jaume
* oculta/eXconde los nombres de las capas y adem�s corrige el error de selecci�n de varios styles si hay alguna capa seleccionada repetida
*
* Revision 1.2.2.11  2006/02/14 16:34:40  jaume
* *** empty log message ***
*
* Revision 1.2.2.10  2006/02/14 11:08:24  jaume
* ahora refresca correctamente styles si arrastras y sueltas incluso desde un proyecto guardado
*
* Revision 1.2.2.9  2006/02/14 09:33:38  jaume
* *** empty log message ***
*
* Revision 1.2.2.8  2006/02/07 07:53:22  jaume
* *** empty log message ***
*
* Revision 1.2.2.7  2006/02/06 18:03:47  jaume
* fixed style edition, now supports drag'n'drop on the layer list refreshing
*
* Revision 1.2.2.6  2006/02/06 15:19:50  jaume
* *** empty log message ***
*
* Revision 1.2.2.5  2006/02/02 10:44:18  jaume
* *** empty log message ***
*
* Revision 1.2.2.4  2006/02/01 16:25:23  jaume
* *** empty log message ***
*
* Revision 1.2.2.3  2006/01/31 16:25:24  jaume
* correcciones de bugs
*
* Revision 1.4  2006/01/31 10:40:31  jaume
* *** empty log message ***
*
* Revision 1.3  2006/01/26 16:07:14  jaume
* *** empty log message ***
*
* Revision 1.2.2.1  2006/01/26 12:59:32  jaume
* 0.5
*
* Revision 1.2  2006/01/24 14:36:33  jaume
* This is the new version
*
* Revision 1.1.2.11  2006/01/23 12:54:45  jaume
* *** empty log message ***
*
* Revision 1.1.2.10  2006/01/20 08:50:52  jaume
* handles time dimension for the NASA Jet Propulsion Laboratory WMS
*
* Revision 1.1.2.9  2006/01/17 12:55:40  jaume
* *** empty log message ***
*
* Revision 1.1.2.6  2006/01/11 12:20:30  jaume
* *** empty log message ***
*
* Revision 1.1.2.5  2006/01/10 11:33:31  jaume
* Time dimension working against Jet Propulsion Laboratory's WMS server
*
* Revision 1.1.2.4  2006/01/09 18:10:38  jaume
* casi con el time dimension
*
* Revision 1.1.2.3  2006/01/05 23:15:53  jaume
* *** empty log message ***
*
* Revision 1.1.2.2  2006/01/04 18:09:02  jaume
* Time dimension
*
* Revision 1.1.2.1  2006/01/03 18:08:40  jaume
* *** empty log message ***
*
* Revision 1.1.2.1  2006/01/02 18:08:01  jaume
* Tree de estilos
*
*
*/
/**
 * 
 */
package com.iver.cit.gvsig.gui.panels;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTree;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.iver.cit.gvsig.fmap.layers.WMSLayerNode;
import com.iver.cit.gvsig.fmap.layers.WMSLayerNode.FMapWMSStyle;

/**
 * This class holds a visual tree that handles the selection of the styles in the
 * selected layers. It encapsulates any gui interface handling the mouse selection.
 * <p>
 * It has a method getStylesSelection that returns the current selection in a 
 * bi-dimensional string array containing the layer name and the selected style name.
 * </p>
 * <p>
 * Keep in mind that although it's similar to LayersTree it is not the same.
 * It does not use the same instances of the WMSLayerNode that LayerTreeModel
 * because it allows to repeat layers. So, each layer is a completely new 
 * WMSLayerNode containing same properties but not the same parent (which is always
 * the tree root) and each style is a completely new FMapStyle containing same
 * properties but its parent is one of the layers of the StylesTree and not one
 * of those of the Layers tree.
 * </p>
 * 
 * @author jaume dominguez faus
 *
 */
public class StyleTree extends JTree {
	public boolean showLayerNames = false;
    public StyleTree(){
        super();
        initialize();
    }
    
    private void initialize(){
        setToggleClickCount(1);
        setRowHeight(22);
        
        setCellRenderer(new DefaultTreeCellRenderer() {
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                if (leaf) {
                    JPanel leafComponent = new JPanel();
                    leafComponent.setBackground(Color.WHITE);
                    JRadioButton leafRadioButton = new JRadioButton("", ((StyleTreeModel) getModel()).isSelected((FMapWMSStyle) value));//selected);
                    leafRadioButton.setBackground(Color.WHITE);
                    leafComponent.add(leafRadioButton);
                    leafComponent.add(new JLabel(((FMapWMSStyle) value).title));
                    return leafComponent;
                } else {
                	super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
                	if (value instanceof WMSLayerNode) {
                		WMSLayerNode layer = (WMSLayerNode) value;
                		if (!showLayerNames) {
                			if (layer.getName() != null || layer.getName() == "") {
                				String text = layer.toString();
                				text = text.substring(text.indexOf(']')+2, text.length());
                				setText(text);
                			}
                		}
                	}
            		return this;
                }
            }});
        addMouseListener(new java.awt.event.MouseAdapter() { 
            public void mouseClicked(java.awt.event.MouseEvent e) {
                ((StyleTreeModel)getModel()).setSelectedLeaf(getSelectionPath());
                clearSelection();
                repaint();
            }
        });
    }
    
    /**
     * Expands this tree.
     *
     */
    public void expandAll(){
        int row = 0; 
        while (row < getRowCount()) {
          expandRow(row);
          row++;
          }
    }
    /**
     * Returns a Vector of Strings containing the style titles.
     */
    public Vector getStyleSelectionTitles(){
        if (getModel() instanceof StyleTreeModel)
            return ((StyleTreeModel)getModel()).getStyleSelectionTitles();
        else
            return null;
    }
    
    /**
     * Sets the selected styles in the StylesTree. The argument styleNames is
     * a Vector with exactly the same amount of strings than the amount of
     * themes (layers) contained by the tree. A blank or null string will
     * leave the default style for that layer, but this element <b>must exist</b>
     * in the array in any case.
     * 
     * @param styleNames, Vector containing the style names. 
     * The styles order <b>must match</b> with the layer order. 
     */
    public void initSelections(Vector styleNames){
    	if (styleNames!=null) {
    		StyleTreeModel model = (StyleTreeModel) getModel();
    		model.setStylesSelection(styleNames);
    	}
    }
	
}

class StyleTreeModel implements TreeModel {
	private WMSLayerNode root;
	private ArrayList layers = new ArrayList();

	public StyleTreeModel(WMSLayerNode root){
        this.root = root;
        
    }
	
	/**
	 * Will return true if this style is selected.
	 * @param style
	 * @return
	 */
	public boolean isSelected(FMapWMSStyle style) {
		return style.parent.getSelectedStyle().equals(style);
	}

	/**
	 * Gets the names of the selected styles into a Vector using the same order
	 * as they was represented in the StylesTree.
	 * @return
	 */
	public Vector getStyleSelectionTitles() {
		Vector v = new Vector();
		for (int i = 0; i < layers.size(); i++) {
			WMSLayerNode layer = (WMSLayerNode) layers.get(i);
			FMapWMSStyle sty = layer.getSelectedStyle();
			if (sty==null) v.add("");
			else v.add(sty.title);
		}
		return v;
	}

	/**
	 * Sets the selected styles in the StylesTree. The argument styleNames is
	 * a Vector with exactly the same amount of strings than the amount of
	 * themes (layers) contained by the tree. A blank or null string will
	 * leave the default style for that layer, but this element <b>must exist</b>
	 * in the array in any case.
	 * 
	 * @param styleNames, Vector containing the style names. 
	 * The styles order <b>must match</b> with the layer order. 
	 */
	public void setStylesSelection(Vector styleNames) {
		for (int i = 0; i < layers.size(); i++) {
			WMSLayerNode layer = (WMSLayerNode) layers.get(i);
			for (int j = 0; j < layer.getStyles().size(); j++) {
				FMapWMSStyle sty = (FMapWMSStyle) layer.getStyles().get(j);
				if ( sty.name.equals((String) styleNames.get(i))) {
					layer.setSelectedStyleByIndex(j);
	}}}}
	
	/**
	 * Adds a new branch to this tree. It must be a layer node.
	 * @param node
	 * @return <b>True</b>, if the added branch actually defines any style.
	 *         <b>False</b>, if the layer has no styles.
	 */
	public boolean addLayerBranch(WMSLayerNode node) {
		layers.add(node);
		if (node.getStyles()==null || node.getStyles().size()==0){
			return false;
		}
		for (int i = 0; i < node.getStyles().size(); i++) {
			((FMapWMSStyle) node.getStyles().get(i)).parent = node;
		}
		((WMSLayerNode)getRoot()).getChildren().add(node);
		return true;
	}
	
	/**
     * Sets a leaf (an style) selected.
     * @param selectionPath to this leaf.
     */
    protected void setSelectedLeaf(TreePath selectionPath) {
        if (selectionPath!=null){
            Object[] objects = selectionPath.getPath();
            Object item = objects[objects.length-1];
            if (isLeaf(item)){
            	FMapWMSStyle style = (FMapWMSStyle) item;
            	WMSLayerNode layer = style.parent ;
            	for (int i = 0; i < layer.getStyles().size(); i++) {
            		FMapWMSStyle sty = (FMapWMSStyle) layer.getStyles().get(i);
					if (sty.name.equals(style.name)) {
						layer.setSelectedStyleByIndex(i);
						return;
	}}}}}


	/*
	 *  (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getRoot()
	 */
	public Object getRoot() {
		if (root == null) {
            root = new WMSLayerNode();
            root.setParent(null);
        }
        return root;
	}

	/*
	 *  (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
	 */
	public int getChildCount(Object parent) {
        int count=0;
        
        if (parent == root)
            count = ((WMSLayerNode) parent).getChildren().size();
        else
            count = ((WMSLayerNode) parent).getStyles().size();
        return count;
    }

	/*
	 *  (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
	 */
	public boolean isLeaf(Object node) {
		return (node instanceof FMapWMSStyle);
	}

	/*
	 *  (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#addTreeModelListener(javax.swing.event.TreeModelListener)
	 */
	public void addTreeModelListener(TreeModelListener l) {
	}

	/*
	 *  (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#removeTreeModelListener(javax.swing.event.TreeModelListener)
	 */
	public void removeTreeModelListener(TreeModelListener l) {
	}

	/*
	 *  (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
	 */
	public Object getChild(Object parent, int index) {
		if (parent instanceof FMapWMSStyle)
            return null;
        if (parent == root)
            return ((WMSLayerNode) parent).getChildren().get(index);
        
        return ((WMSLayerNode) parent).getStyles().get(index);
	}

	/*
	 *  (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object, java.lang.Object)
	 */
	public int getIndexOfChild(Object parent, Object child) {
		if (parent instanceof FMapWMSStyle){ 
            return -1;   
        }
         
        WMSLayerNode l = (WMSLayerNode)parent;
        if (l.getParent()==null){
            for (int i = 0; i < l.getChildren().size(); i++) {
                if (l.getChildren().get(i).equals(child)){
                   return i;
                }
            }
        } else {
            for (int i = 0; i < l.getStyles().size(); i++) {
                if (l.getChildren().get(i).equals(child)){
                    return i;
                }
            }
        }
        return -1;
	}

	/*
	 *  (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#valueForPathChanged(javax.swing.tree.TreePath, java.lang.Object)
	 */
	public void valueForPathChanged(TreePath path, Object newValue) {
	}
}