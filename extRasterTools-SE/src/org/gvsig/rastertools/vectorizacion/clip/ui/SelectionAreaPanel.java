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
package org.gvsig.rastertools.vectorizacion.clip.ui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.gvsig.gui.beans.buttonbar.ButtonBarContainer;
import org.gvsig.gui.beans.table.TableContainer;
import org.gvsig.raster.util.BasePanel;
import org.gvsig.raster.util.RasterToolsUtil;

/**
 * Panel con los controles de selección de área.
 * 
 * 09/06/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class SelectionAreaPanel extends BasePanel implements ActionListener, TableModelListener {
	private static final long         serialVersionUID       = 1L;
	private JRadioButton              areaSelect             = null;
	private JRadioButton              roiSelect              = null;
	private JRadioButton              vectorizeBBoxSelect    = null;
	private JRadioButton              vectorizeOnlyInside    = null;
	private ButtonGroup               areaSelectGroup        = null;
	private ButtonGroup               typeVectGroup          = null;
	
	private JButton                   selectROI              = null;
	private boolean                   enabled                = true;
	
	private TableContainer            tableContainer         = null;
	private ButtonBarContainer        buttonBarContainer     = null;
		
	/**
	 *Inicializa componentes gráficos y traduce
	 */
	public SelectionAreaPanel() {
		init();
		translate();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.util.BasePanel#translate()
	 */
	protected void translate() {
		
	}
	
	/**
	 * Inicialización de los componentes gráficos
	 */
	protected void init() {
		areaSelectGroup = new ButtonGroup();
		areaSelectGroup.add(getAreaSelector());
		areaSelectGroup.add(getROISelector());
				
		typeVectGroup = new ButtonGroup();
		typeVectGroup.add(getVectorizeAllBBox());
		typeVectGroup.add(getVectorizeOnlyInside());
		
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createTitledBorder(null, RasterToolsUtil.getText(this, "selection"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.PLAIN, 10), null));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		
		add(getAreaSelector(), gbc);
		
		gbc.gridy = 1;
		gbc.insets = new Insets(0, 20, 0, 0);
		add(getButtonBarContainer(), gbc);
		
		gbc.gridy = 2;
		gbc.insets = new Insets(0, 0, 0, 0);
		add(getROISelector(), gbc);

		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 1;
		gbc.gridy = 3;
		gbc.insets = new Insets(0, 20, 0, 0);
		add(getTableContainer(), gbc);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weighty = 0;
		gbc.gridy = 5;
		gbc.insets = new Insets(0, 27, 0, 8);
		add(getROI(), gbc);
		gbc.insets = new Insets(0, 20, 0, 0);
		
		gbc.gridy = 6;
		add(getVectorizeAllBBox(), gbc);
		
		gbc.gridy = 7;
		add(getVectorizeOnlyInside(), gbc);
		
		getAreaSelector().setSelected(true);
		getVectorizeAllBBox().setSelected(true);
		
		setAreaSelectorEnabled(true);
		setROISelectorEnabled(false);
	}
	
	/**
	 * Obtiene el botón de selección por área. 
	 * @return JRadioButton
	 */
	public JRadioButton getAreaSelector() {
		if(areaSelect == null) {
			areaSelect = new JRadioButton(RasterToolsUtil.getText(this, "porarea"));
			areaSelect.addActionListener(this);
		}
		return areaSelect;
	}
	
	/**
	 * Obtiene el botón de selección por ROI. 
	 * @return JRadioButton
	 */
	public JRadioButton getROISelector() {
		if(roiSelect == null) {
			roiSelect = new JRadioButton(RasterToolsUtil.getText(this, "porroi"));
			roiSelect.addActionListener(this);
		}
		return roiSelect;
	}
	
	/**
	 * Obtiene el botón de vectorización de todo lo que hay en el interior de la bounding box. 
	 * @return JRadioButton
	 */
	public JRadioButton getVectorizeAllBBox() {
		if(vectorizeBBoxSelect == null) {
			vectorizeBBoxSelect = new JRadioButton(RasterToolsUtil.getText(this, "vectbbox"));
		}
		return vectorizeBBoxSelect;
	}
		
	/**
	 * Obtiene el botón de no vectorizar lo que queda en el exterior de la ROI 
	 * @return JRadioButton
	 */
	public JRadioButton getVectorizeOnlyInside() {
		if(vectorizeOnlyInside == null) {
			vectorizeOnlyInside = new JRadioButton(RasterToolsUtil.getText(this, "vectinside"));
		}
		return vectorizeOnlyInside;
	}
		
	/*
	 * (non-Javadoc)
	 * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
	 */
	public void tableChanged(TableModelEvent e) {
	}
	
	/**
	 * Obtiene el contenedor con la tabla.
	 * @return
	 */
	public TableContainer getTableContainer() {
		if (tableContainer == null) {
			String[] columnNames = {" ", "Nombre", ""};
			int[] columnWidths = {22, 334, 0};
			tableContainer = new TableContainer(columnNames, columnWidths);
			tableContainer.setModel("CheckBoxModel");
			tableContainer.initialize();
			tableContainer.setControlVisible(false);
			tableContainer.setMoveRowsButtonsVisible(false);
			tableContainer.getTable().getJTable().getColumnModel().getColumn(0).setMinWidth(22);
			tableContainer.getTable().getJTable().getColumnModel().getColumn(0).setMaxWidth(22);
			tableContainer.getTable().getJTable().getColumnModel().getColumn(2).setMinWidth(0);
			tableContainer.getTable().getJTable().getColumnModel().getColumn(2).setMaxWidth(0);
			tableContainer.getModel().addTableModelListener(this);
		}
		return tableContainer;
	}
	
	/**
	 * Inicializa la barra con los botones de selección de área
	 * @return
	 */
	public ButtonBarContainer getButtonBarContainer() {
		if (buttonBarContainer == null) {
			buttonBarContainer = new ButtonBarContainer();
			buttonBarContainer.addButton("fullExtent.png", RasterToolsUtil.getText(this, "fullExtent"), 0);
			buttonBarContainer.addButton("selectTool.png", RasterToolsUtil.getText(this, "select_tool"), 1);
			buttonBarContainer.setButtonAlignment("left");
			buttonBarContainer.setComponentBorder(false);
		}
		return buttonBarContainer;
	}

	/**
	 * Gestión del evento del check de activación y desactivación
	 */
	public void actionPerformed(ActionEvent e) {
		setComponentEnabled(enabled);
		if(e.getSource() == getAreaSelector()) {
			setAreaSelectorEnabled(true);
			setROISelectorEnabled(false);
		}
		if(e.getSource() == getROISelector()) {
			setAreaSelectorEnabled(false);
			setROISelectorEnabled(true);
		}
	}

	/**
	 * Activa o desactiva el componente. El estado de activación y desactivación de un
	 * componente depende de los controles que contiene. En este caso activa o desactiva
	 * la barra de incremento.
	 * @param enabled
	 */
	public void setComponentEnabled(boolean enabled) {
		setAreaSelectorEnabled(enabled);
		setROISelectorEnabled(enabled);
		this.enabled = !enabled;
	}
	
	/**
	 * Activa o desactiva el componente de selector de área
	 * @param enabled
	 */
	public void setAreaSelectorEnabled(boolean enabled) {
		getButtonBarContainer().getButton(0).setEnabled(enabled);
		getButtonBarContainer().getButton(1).setEnabled(enabled);
	}
	
	/**
	 * Activa o desactiva el componente de selector de ROI
	 * @param enabled
	 */
	public void setROISelectorEnabled(boolean enabled) {
		getTableContainer().setEnabled(enabled);
		getROI().setEnabled(enabled);
		getVectorizeAllBBox().setEnabled(enabled);
		getVectorizeOnlyInside().setEnabled(enabled);
	}
	
	/**
	 * Obtener región de interés desde la vista.
	 * @return JButton
	 */
	public JButton getROI() {
		if(selectROI == null)
			selectROI = new JButton(RasterToolsUtil.getText(this, "selectROI"));
		return selectROI;
	}

}
