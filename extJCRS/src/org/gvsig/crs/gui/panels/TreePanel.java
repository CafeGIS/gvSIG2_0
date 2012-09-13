/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 Instituto de Desarrollo Regional and Generalitat Valenciana.
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
 *   Instituto de Desarrollo Regional (Universidad de Castilla La-Mancha)
 *   Campus Universitario s/n
 *   02071 Alabacete
 *   Spain
 *
 *   +34 967 599 200
 */

package org.gvsig.crs.gui.panels;

import java.awt.BorderLayout;
import java.util.Hashtable;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * 
 * @author Diego Guerrero (diego.guerrero@uclm.es)
 *
 */
public class TreePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private int 		wComp = 190;
	private int 		hComp = 360;
	private int 		hTree = (int)Math.floor(hComp * 0.68);
	private int 		hList = 50;
	private JScrollPane pTree = null;
	private JScrollPane pList = null;
	private JTree tree = null;
	//private JList list = null;
	private JTextArea list = null;
	private DefaultMutableTreeNode raiz = null;
	private Hashtable	map;
	String rootName = "";

	public TreePanel(String rootName) {
		super();
		this.rootName = rootName;
		initialize();
	}
	
	private void initialize() {
		map = new Hashtable();
		raiz =  new DefaultMutableTreeNode(rootName);
		this.setLayout(new BorderLayout());
		this.setBorder(new EmptyBorder(0,10,10,10));
		this.add(getPTree(),BorderLayout.CENTER);
		this.add(getPList(),BorderLayout.SOUTH);
	}
	
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JScrollPane getPTree() {
		if (pTree == null) {
			pTree = new JScrollPane();
			//pTree.setPreferredSize(new java.awt.Dimension(wComp, hTree));
			pTree.setViewportBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
			pTree.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			pTree.setViewportView(getTree());
		}
		return pTree;
	}

	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JScrollPane getPList() {
		if (pList == null) {
			pList = new JScrollPane();
			//pList.setPreferredSize(new java.awt.Dimension(wComp, hList));
			pList.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
			pList.setBackground(java.awt.Color.white);
			pList.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			pList.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			pList.setViewportView(getList());
		}
		return pList;
	}

	/**
	 * This method initializes jTree	
	 * 	
	 * @return javax.swing.JTree	
	 */
	public JTree getTree() {
		if (tree == null) {
			tree = new JTree(raiz);
		}
		return tree;
	}

	/**
	 * This method initializes jList	
	 * 	
	 * @return javax.swing.JList	
	 */
	public JTextArea getList() {
		if (list == null) {
			list = new JTextArea();
			list.setLineWrap(true);
			list.setWrapStyleWord(true);
			list.setEditable(false);
		}
		return list;
	}
	
	public void setPanelSize(int w, int h){
		wComp = w;
		hComp = h;
		hTree = (int)Math.floor(hComp * 0.68);
		hList = hComp - hTree;
        this.setPreferredSize(new java.awt.Dimension(wComp, hComp));
		setPreferredSize(new java.awt.Dimension(wComp, hComp));
		pTree.setPreferredSize(new java.awt.Dimension(wComp, hTree));
		pList.setPreferredSize(new java.awt.Dimension(wComp, hList));
	}
	
	/**
	 * Añade una nueva categoria al arbol
	 * @param name	Etiqueta que aparece en el arbol. 
	 * @param pos	Posición en el arbol de la nueva categoria
	 */
	public void addClass(String name, int pos){
		DefaultTreeModel model =(DefaultTreeModel)tree.getModel();
		DefaultMutableTreeNode r = new DefaultMutableTreeNode( name );
		model.insertNodeInto(r, raiz, pos);
	}
	
	/**
	 * Añade una entrada a una categoria
	 * @param name Nombre de la entrada a añadir
	 * @param parentName Categoria a la que añadimos
	 * @param value Valor asociado a la entrada
	 */
	public void addEntry(String name, String parentName, String value){
		DefaultTreeModel model =(DefaultTreeModel)tree.getModel();
		for(int i = 0; i < model.getChildCount(raiz); i++){
			if(model.getChild(raiz, i).toString().equals(parentName)){
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)model.getChild(raiz, i);
				node.add(new DefaultMutableTreeNode(name));
				if (value!=null)
					map.put(name,value);
			}
		}
	}
	
	public void setRoot(String name){
		DefaultTreeModel model =(DefaultTreeModel)tree.getModel();
		//for (int index = 0; index<model.getChildCount(model.getRoot());index++)
		//	model.removeNodeFromParent((DefaultMutableTreeNode)model.getChild(raiz, index));
		rootName=name;
		raiz =  new DefaultMutableTreeNode(rootName);
		model.setRoot(raiz);
		map.clear();
		
	}
}
