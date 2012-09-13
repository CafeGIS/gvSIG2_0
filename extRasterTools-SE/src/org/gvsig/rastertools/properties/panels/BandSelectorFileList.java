/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
*
* Copyright (C) 2007 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.rastertools.properties.panels;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import com.iver.utiles.DefaultListModel;
/**
 * Panel que contiene la lista de ficheros cargados desde donde se leen las
 * bandas visualizadas. Contiene controles para añadir y eliminar los ficheros,
 * así como un control para seleccionar el número de bandas a visualizar.
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class BandSelectorFileList extends JPanel {
	private static final long serialVersionUID = 3329020254004687818L;
	private JScrollPane       jScrollPane      = null;
	private JButton           addButton        = null;
	private JButton           delButton        = null;
	private JList             jList            = null;
	private DefaultListModel  listModel        = null;
	private JPanel            jPanel1          = null;

	/**
	* This is the default constructor
	*/
	public BandSelectorFileList() {
		super();
		listModel = new DefaultListModel();
		initialize();
	}

	/**
	 * Inicialización de componentes gráficos
	 *
	 */
	private void initialize() {
		setLayout(new BorderLayout());
		add(getJScrollPane(), BorderLayout.CENTER);
		add(getButtonsPanel(), BorderLayout.EAST);
	}
	
	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJList());
		}
		return jScrollPane;
	}

	/**
	 * Obtiene el botón de añadir fichero
	 * @return JButton
	 */
	public JButton getJButtonAdd() {
		if (addButton == null) {
			addButton = new JButton("Añadir");
			addButton.setPreferredSize(new java.awt.Dimension(80, 25));
		}
		return addButton;
	}

	/**
	 * Obtiene el botón de eliminar fichero
	 * @return JButton
	 */
	public JButton getJButtonRemove() {
		if (delButton == null) {
			delButton = new JButton("Eliminar");
			delButton.setPreferredSize(new java.awt.Dimension(80, 25));
		}
		return delButton;
	}

	/**
	 * This method initializes jList
	 * 
	 * @return javax.swing.JList
	 */
	public JList getJList() {
		if (jList == null) {
			jList = new JList(listModel);
			jList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			jList.setLayoutOrientation(JList.VERTICAL);
		}
		return jList;
	}

	/**
	 * This method initializes jPanel1
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getButtonsPanel() {
		if (jPanel1 == null) {
			jPanel1 = new JPanel();
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(0, 0, 3, 0);
			gbc.weightx = 1.0;
			jPanel1.setLayout(new GridBagLayout());
			jPanel1.add(getJButtonAdd(), gbc);
			gbc.gridy = 1;
			jPanel1.add(getJButtonRemove(), gbc);
		}
		return jPanel1;
	}

	/**
	 * Añade el nombre de un fichero a la lista
	 * @param fName
	 */
	public void addFName(String fName) {
		listModel.addElement(fName);
	}

	/**
	 * Elimina un fichero de la lista
	 * @param fName
	 */
	public void removeFName(String fName) {
		for (int i = 0; i < listModel.size(); i++) {
			if(((String)listModel.get(i)).endsWith(fName))
				listModel.remove(i);
		}
	}

	/**
	 * Elimina todos los ficheros de la lista
	 */
	public void clear(){
		listModel.clear();
	}

	/**
	 * Obtiene el número de ficheros en la lista
	 * @return
	 */
	public int getNFiles() {
		return listModel.size();
	}
	
	/**
	 * Obtiene el modelo de la lista
	 * @return DefaultListModel
	 */
	public DefaultListModel getModel() {
		return listModel;
	}
	
	/**
	 * Activa y desactiva el control
	 * @param enabled true para activar y false para desactivar
	 */
	public void setEnabled(boolean enabled) {
		getJButtonAdd().setEnabled(enabled);
		getJButtonRemove().setEnabled(enabled);
		getJList().setEnabled(enabled);
		getJScrollPane().setEnabled(enabled);
	}
}