/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 IVER T.I. and Generalitat Valenciana.
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
 */
package org.gvsig.gui.beans.treelist;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.gvsig.gui.beans.Messages;
import org.gvsig.gui.beans.treelist.event.TreeListChangeEvent;
import org.gvsig.gui.beans.treelist.listeners.TreeListChangeListener;
import org.gvsig.gui.beans.treelist.listeners.TreeListComponentListener;
import org.gvsig.gui.beans.treelist.listeners.TreeListListener;
/**
 * Componente consistente en un menú de arbol al que se le pueden añadir
 * entradas y una lista de elementos debajo de este. Haciendo doble click o
 * arrastrando un elemento del menú a la lista este queda añadido en esta.
 * Haciendo doble click en un elemento de la lista se elimina de esta y
 * arrastrando elementos dentro de la lista se varia su posición en ella.
 *
 * @version 31/05/2007
 * @author Nacho Brodin (brodin_ign@gva.es)
 */
public class TreeListContainer extends JPanel implements ActionListener, TreeSelectionListener, ListSelectionListener {
	private static final long serialVersionUID = 6665259638830401366L;
	private ArrayList<TreeListChangeListener> actionChangeListeners = new ArrayList<TreeListChangeListener>();
	private ArrayList<TreeListComponentListener> listListeners = new ArrayList<TreeListComponentListener>();
	private Hashtable<String, String> map = null;

	// Componentes visuales
	private JScrollPane            pTree         = null;
	private JScrollPane            pList         = null;
	private JTree                  tree          = null;
	private JList                  list          = null;
	private JButton                bAdd          = null;
	private JButton                bDel          = null;
	private JSplitPane             jSplitPane1   = null;
	private JPanel                 jPanelButtons = null;

	private DefaultMutableTreeNode raiz          = null;
	private TreeListListener       listener      = null;
	private String                 pathToImages  = "images/";

	/**
	 * This method initializes
	 */
	public TreeListContainer() {
		super();
		listener = new TreeListListener();
		initialize();
		listener.setList(getList());
		listener.setTree(getTree());
		this.setPreferredSize(new Dimension(160, 0));
		getTree().addTreeSelectionListener(this);
		getList().addListSelectionListener(this);
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		map = new Hashtable<String, String>();
		raiz =  new DefaultMutableTreeNode(Messages.getText("filtros"));
		setLayout(new BorderLayout());

		getPList().setMinimumSize(new Dimension(0, 60));
		jSplitPane1 = new JSplitPane();
		jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
		jSplitPane1.setTopComponent(getPTree());
		jSplitPane1.setBottomComponent(getPList());
		jSplitPane1.setResizeWeight(1.0);
		jSplitPane1.setContinuousLayout(true);
		this.add(jSplitPane1, BorderLayout.CENTER);
		this.add(getJPanelButtons(), BorderLayout.SOUTH);
	}

	/**
	 * Establece el ToolTip del boton de añadir
	 * @param text
	 */
	public void setAddToolTipText(String text) {
		getAddButton().setToolTipText(text);
	}

	/**
	 * Establece el ToolTip del boton de borrar
	 * @param text
	 */
	public void setDelToolTipText(String text) {
		getDelButton().setToolTipText(text);
	}

	/**
	 * Devuelve el boton de Añadir
	 * @return
	 */
	private JButton getAddButton() {
		if (bAdd == null) {
			bAdd = new JButton();
			bAdd.setIcon(new ImageIcon(getClass().getResource(pathToImages + "addlayer.png")));
			bAdd.setPreferredSize(new Dimension(22, 19));
			bAdd.addActionListener(this);
		}
		return bAdd;
	}

	/**
	 * Devuelve el boton de Borrar
	 * @return
	 */
	private JButton getDelButton() {
		if (bDel == null) {
			bDel = new JButton();
			bDel.setIcon(new ImageIcon(getClass().getResource(pathToImages + "delall.png")));
			bDel.setPreferredSize(new Dimension(22, 19));
			bDel.addActionListener(this);
		}
		return bDel;
	}
	
	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			jPanelButtons = new JPanel();
			jPanelButtons.setPreferredSize(new Dimension(0, 21));




			FlowLayout flowLayout5 = new FlowLayout();
			flowLayout5.setHgap(5);
			flowLayout5.setVgap(0);
			flowLayout5.setAlignment(java.awt.FlowLayout.CENTER);
			jPanelButtons.setLayout(flowLayout5);

			jPanelButtons.add(getAddButton(), null);
			jPanelButtons.add(getDelButton(), null);
		}
		return jPanelButtons;
	}

	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JScrollPane getPTree() {
		if (pTree == null) {
			pTree = new JScrollPane();
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
			pList.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
			pList.setBackground(java.awt.Color.white);
			pList.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
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
			tree.addMouseListener(listener);
			tree.addMouseMotionListener(listener);
		}
		return tree;
	}

	/**
	 * This method initializes jList
	 *
	 * @return javax.swing.JList
	 */
	public JList getList() {
		if (list == null) {
			DefaultListModel m = new DefaultListModel();
			list = new JList(m);
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			list.addMouseListener(listener);
			list.addMouseMotionListener(listener);
		}
		return list;
	}

	public ListModel getListModel() {
		return list.getModel();
	}

	//-------------------------------------------
	//MÉTODOS DE ARBOL

	/**
	 * Añade una nueva categoria al arbol
	 * @param name	Etiqueta que aparece en el arbol.
	 * @param pos	Posición en el arbol de la nueva categoria
	 */
	public void addClass(String name, int pos) {
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		DefaultMutableTreeNode r = new DefaultMutableTreeNode(name);
		model.insertNodeInto(r, raiz, pos);
	}

	/**
	 * Añade una entrada a una categoria
	 * @param name Nombre de la entrada a añadir
	 * @param parentName Categoria a la que añadimos
	 * @param value Valor asociado a la entrada
	 */
	public void addEntry(String name, String parentName, String value){
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		for (int i = 0; i < model.getChildCount(raiz); i++) {
			if (model.getChild(raiz, i).toString().equals(parentName)) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) model.getChild(raiz, i);
				node.add(new DefaultMutableTreeNode(name));
				if (value != null)
					map.put(name, value);
			}
		}
	}

	/**
	 * Método que comprueba si una entrada existe en la lista de seleccion.
	 * @param value Valor que se quiere comprobar si está en la lista
	 * @return true si el valor está en la lista y false si no lo está
	 */
	public boolean isInList(String value) {
		DefaultListModel model = (DefaultListModel) getList().getModel();
		for (int i = 0; i < model.getSize(); i++)
			if (((String) model.get(i)).equals(value))
				return true;
		return false;
	}

	/**
	 * Añade un listener TreeListComponent
	 * @param e
	 */
	public void addTreeListListener(TreeListComponentListener e){
		listListeners.add(e);
		listener.setListeners(listListeners);
	}

	/**
	 * Añade un elemento a la lista
	 * @param element Elemento a añadir
	 */
	public void addElementInList(String element){
		DefaultListModel model = (DefaultListModel)getList().getModel();
		model.addElement(element);
	}

	/**
	 * Elimina un elemento a la lista
	 * @param element Elemento a eliminar
	 */
	public void removeElementInList(String element){
		DefaultListModel model = (DefaultListModel)getList().getModel();
		model.removeElement(element);
	}

	/**
	 * Elimina un elemento a la lista por indice
	 * @param element Indice del elemento a eliminar
	 */
	public void removeElementInList(int element){
		DefaultListModel model = (DefaultListModel) getList().getModel();
		model.remove(element);
	}

	@SuppressWarnings("unchecked")
	public Hashtable getMap() {
		return map;
	}

	@SuppressWarnings("unchecked")
	public void setMap(Hashtable map) {
		this.map = map;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getAddButton()) {
			listener.insertElement();
		}
		if (e.getSource() == getDelButton()) {
			listener.deleteElement();
		}
	}

	/**
	 * Añadir el disparador de cuando se pulsa un botón.
	 * @param listener
	 */
	public void addChangeSelectionListener(TreeListChangeListener listener) {
		if (!actionChangeListeners.contains(listener))
			actionChangeListeners.add(listener);
	}

	/**
	 * Borrar el disparador de eventos de los botones.
	 * @param listener
	 */
	public void removeChangeSelectionListener(TreeListChangeListener listener) {
		actionChangeListeners.remove(listener);
	}

	private void callActionChangeListeners(String item) {
		Iterator<TreeListChangeListener> acIterator = actionChangeListeners.iterator();
		while (acIterator.hasNext()) {
			TreeListChangeListener listener = acIterator.next();
			String name = map.get(item);
			if (name != null)
				listener.actionChangeSelection(new TreeListChangeEvent(this, name));
			else
				listener.actionChangeSelection(new TreeListChangeEvent(this, item));
		}
	}

	public void valueChanged(TreeSelectionEvent e) {
		String draggLabel = null;
		if (e.getSource() == tree) {
			TreePath[] tp = tree.getSelectionPaths();
			if (tp != null) {
				String val = tp[0].toString();
				// Comprobamos que se haya pinchado sobre una rama
				String[] pathTree = val.split(", ");
				if (pathTree.length > 2) {
					draggLabel = val.substring(val.lastIndexOf(", ") + 2, val.lastIndexOf("]"));
					callActionChangeListeners(draggLabel);
				}
			}
		}
	}

	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
			if (getList().getSelectedValue() != null)
				callActionChangeListeners(getList().getSelectedValue().toString());
		}
	}
}