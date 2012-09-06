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
package org.gvsig.gui.beans.treelist.listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import org.gvsig.gui.beans.treelist.event.TreeListEvent;

/**
 * Esta clase contiene la lógica que añade elementos desde el arbol a la lista, arrastrando o
 * haciendo doble click sobre el elemento en el arbol, elimina elementos de la lista haciendo 
 * doble click sobre ellos o intercambia elementos arrastrando en la lista de una posición 
 * a otra.
 * Nacho Brodin (brodin_ign@gva.es)
 */

public class TreeListListener implements MouseListener, MouseMotionListener, TreeListComponentListener {
	private JTree tree = null;
	private JList 					list = null;
	private ArrayList				listListeners = null;
	private TreeListEvent 			ev = new TreeListEvent();

	/**
	 * This method initializes 
	 * 
	 */
	public TreeListListener() {
	}

	/**
	 * Asigna la lista
	 * @param list
	 */
	public void setList(JList list) {
		this.list = list;
	}

	/**
	 * Asigna el arbol
	 * @param tree
	 */
	public void setTree(JTree tree) {
		this.tree = tree;
	}

	//-------------------------------------------
	//MÉTODOS DEL LISTENER
	
	//AÑADIR A LA LISTA
	//Pinchar con doble click el elemento del arbol
	
	//Pichar lista -> obtiene draggLabel
	//Arrastrar -> draggActive
	//Released -> !draggActive. Si enterList y se ha arrastrado y se ha pichado un elemento de la lista -> añade elemento
	
	//ELIMINAR DE LA LISTA
	//Pinchar con doble click el elemento de la lista.
	
	//CAMBIAR EL ORDEN DE LOS ELEMENTOS DE LA LISTA
	//Pinchar lista -> Obtiene index del elem pinchado
	//Arrastrar -> draggElement a true
	//Soltar -> si hay elemento seleccionado estamos dentro de la lista y draggElement es true -> se intercambiar
	
	/**
	 * Variable que contiene la etiqueta seleccionada y arrastrado hasta la lista
	 */
	private String draggLabel = null;
	/**
	 * Variable que está a true si estamos arrastrando una etiqueta
	 */
	private boolean draggActive = false;
	/**
	 * Variable que está a true si el ratón ha entrado en la lista
	 */
	private boolean enterInList = false;
	/**
	 * Elemento seleccionado de la lista. -1 si no hay ninguno;
	 */
	private int selectElement = -1;
	/**
	 * Variable que está a true si estamos arrastrando un elemento de la lista
	 */
	private boolean draggElement = false;
	
	public void deleteElement() {
		if (list.getSelectedIndex() != -1) {
			DefaultListModel model = (DefaultListModel) list.getModel();

			// Lanzamos el evento de eliminar un elemento
			ev.resetValues();
			ev.setElementRemoved((String) list.getSelectedValue());

			model.remove(list.getSelectedIndex());

			elementRemoved(ev);
		}
	}
	
	public void insertElement() {
		if (draggLabel != null) {
			DefaultListModel model = (DefaultListModel) list.getModel();

			// Si ya esta en la lista no lo añadimos
			if (model.indexOf(draggLabel) != -1) return;

			// Lanzamos el evento de añadir un elemento
			ev.resetValues();
			ev.setElementAdded(draggLabel);

			model.addElement(draggLabel);
//			draggLabel = null;

			elementAdded(ev);
		}
	}
	
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == list)
			// Si es doble click y hay algún elemento seleccionado en la lista lo eliminamos
			if (e.getClickCount() == 2)
				deleteElement();

		if (e.getSource() == tree)
			// Si es doble click y hay algún elemento seleccionado en el arbol lo añadimos
			if (e.getClickCount() == 2)
				insertElement();
	}
	
	public void mouseEntered(MouseEvent e) {
		if(e.getSource() == list)
			enterInList = true;
	}

	public void mouseExited(MouseEvent e) {
		if(e.getSource() == list)
			enterInList = false;
	}

	public void mousePressed(MouseEvent e) {
		selectElement = -1;
		
		//Al pulsar sobre el Arbol se captura el elemento seleccionado
		if(e.getSource() == tree){
			TreePath[] tp = tree.getSelectionPaths();
			if(tp != null){
				String val = tp[0].toString();
				//Comprobamos que se haya pinchado sobre una rama
				String[] pathTree = val.split(", ");
				if(pathTree.length <= 2)
					draggLabel = null;
				else
					draggLabel = val.substring(val.lastIndexOf(", ") + 2, val.lastIndexOf("]"));
			}
		}
		
		//Al pulsar sobre la lista capturamos el elemento seleccionado
		if(e.getSource() == list){
			selectElement = list.getSelectedIndex();
			if(selectElement < 0)
				return;
			
			//Capturamos datos para el evento elementMoved
			ev.resetValues();
			ev.setPositionSourceElement(selectElement);
			ev.setSourceElement((String)((DefaultListModel)list.getModel()).getElementAt(selectElement));
		}
	}

	public void mouseReleased(MouseEvent e) {
		//Si se ha pinchado sobre un elemento del arbol y se ha arrastrado lo añadimos
		if (enterInList){
			if (draggLabel != null && draggActive == true) {
				insertElement();
/*				DefaultListModel model = (DefaultListModel)list.getModel();
				
				//Lanzamos el evento de añadir un elemento
				ev.setElementAdded(draggLabel);
				elementAdded(ev);
				
				model.addElement(draggLabel);
*/
				draggLabel = null;
			}
		}
		
		//Si se está arrastrando un elemento de la lista se intercambia por el lugar donde se suelta
		if(draggElement && e.getSource() == list && selectElement != -1){
			if(list.getSelectedIndex() != selectElement){
				DefaultListModel model = (DefaultListModel)list.getModel();
				int posDst = list.getSelectedIndex();
				String elemOrig = (String)model.getElementAt(selectElement);
				
				ev.setPositionDestElement(posDst);
				ev.setDestElement((String)((DefaultListModel)list.getModel()).getElementAt(posDst));
				
				model.remove(selectElement);
				model.insertElementAt(elemOrig, posDst);

				elementMoved(ev);
			}
		}
		
		//Al levantar el click del ratón desactivamos los arrastres
		draggActive = false;
		draggElement = false;
		selectElement = -1;
	}
		
	public void mouseDragged(MouseEvent e) {
		//Al arrastrar desde el arbor se pone draggActive a true se se ha pinchado antes en un elemento
		if(e.getSource() == tree){
			if(draggLabel != null)
				draggActive = true;
		}
		
		if(e.getSource() == list){
			if(selectElement != -1)
				draggElement = true;
		}
	}

	public void mouseMoved(MouseEvent e) {
	}	
	
	/**
	 * Añade la lista de listeners
	 * @param l
	 */
	public void setListeners(ArrayList l){
		this.listListeners = l;
	}

	/**
	 * Ejecutamos el método elementAdded de todos los listeners registrados
	 * @param e TreeListEvent
	 */
	public void elementAdded(TreeListEvent e) {
		if(listListeners == null || listListeners.size() == 0)
			return;
		for(int i = 0; i < listListeners.size(); i++)
			((TreeListComponentListener)listListeners.get(i)).elementAdded(e);
	}
	
	/**
	 * Ejecutamos el método elementRemoved de todos los listeners registrados
	 * @param e TreeListEvent
	 */
	public void elementRemoved(TreeListEvent e) {
		if(listListeners == null || listListeners.size() == 0)
			return;
		for(int i = 0; i < listListeners.size(); i++)
			((TreeListComponentListener)listListeners.get(i)).elementRemoved(e);
	}

	/**
	 * Ejecutamos el método elementMoved de todos los listeners registrados
	 * @param e TreeListEvent
	 */
	public void elementMoved(TreeListEvent e) {
		if(listListeners == null || listListeners.size() == 0)
			return;
		for(int i = 0; i < listListeners.size(); i++)
			((TreeListComponentListener)listListeners.get(i)).elementMoved(e);
	}
}