/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
 */
package org.gvsig.rastertools.filter.ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.gvsig.gui.beans.treelist.TreeListContainer;
import org.gvsig.raster.beans.previewbase.IUserPanelInterface;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.rastertools.filter.FilterListener;
/**
 * Es el panel central del panel de filtros. Se compone de dos partes.
 * El lateral izquierdo contiene la lista de filtros posibles y filtros seleccionados
 * La parte derecha contiene las opciones de los filtros
 * En la parte de abajo tenemos el fichero de salida.
 * 
 * @version 22/02/2008
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class FilterMainPanel extends JPanel implements IUserPanelInterface {
	private static final long serialVersionUID = -406089078173595028L;
	private TreeListContainer treeListContainer = null;
	private JPanel            centralPanel      = null;
	private FilterListener    filterListener    = null;

	public FilterMainPanel(FilterListener filterListener) {
		this.filterListener = filterListener;
		initialize();
	}
	
	private void initialize() {
		setLayout(new BorderLayout(5, 5));
		add(getTreeListContainer(), BorderLayout.WEST);
		add(getCentralPanel(), BorderLayout.CENTER);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.beans.previewbase.IUserPanelInterface#getPanel()
	 */
	public JPanel getPanel() {
		return this;
	}

	/**
	 * This method initializes jPanel
	 * @return javax.swing.JPanel
	 */
	public TreeListContainer getTreeListContainer() {
		if (treeListContainer == null) {
			treeListContainer = new TreeListContainer();
			treeListContainer.getTree().expandRow(0);
			treeListContainer.addTreeListListener(filterListener);
			treeListContainer.addChangeSelectionListener(filterListener);
			treeListContainer.setAddToolTipText(RasterToolsUtil.getText(this, "anadir_filtro"));
			treeListContainer.setDelToolTipText(RasterToolsUtil.getText(this, "eliminar_filtro"));
		}
		return treeListContainer;
	}

	/**
	 * Obtener y generar el JPanel central que contendrá las propiedades
	 * @return
	 */
	public JPanel getCentralPanel() {
		if (centralPanel == null) {
			centralPanel = new JPanel();
			centralPanel.setLayout(new BorderLayout());
		}
		return centralPanel;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.beans.previewbase.IUserPanelInterface#getTitle()
	 */
	public String getTitle() {
		return "Filtros";
	}
}