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
package org.gvsig.raster.filter.mask;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.datainput.DataInputContainer;
import org.gvsig.gui.beans.datainput.DataInputContainerListener;
import org.gvsig.gui.beans.table.TableContainer;
import org.gvsig.gui.beans.table.exceptions.NotInitializeException;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.grid.filter.RegistrableFilterListener;
import org.gvsig.raster.grid.roi.ROI;
import org.gvsig.raster.util.RasterToolsUtil;
/**
 * Interfaz gráfico para los filtros de mascara.
 * 
 * 14/03/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class MaskUI extends RegistrableFilterListener implements TableModelListener, ChangeListener, DataInputContainerListener {
	private static final long   serialVersionUID  = 4525736825113598035L;
	private TableContainer      tableContainer    = null;
	private FLayer              layer             = null;
	private ArrayList           rois              = null;
	private DataInputContainer  valueNoData       = null;
	private JCheckBox           negative          = null;
	private JCheckBox           transp            = null;
	private JLabel              warning           = null;
	private boolean             lastTransp        = false;
	private boolean             lastInv           = false;

	/**
	 * Constructor. Inicializa los elementos gráficos.
	 */
	public MaskUI() {
		initialize();
	}
	
	/**
	 * Inicializa los elementos gráficos.
	 */
	private void initialize() {
		setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints;
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(0, 0, 2, 0);
		add(getWarning(), gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(0, 0, 2, 0);
		add(getInverse(), gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new Insets(0, 0, 2, 0);
		add(getTableContainer(), gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(0, 0, 2, 0);

		add(getValueNoData(), gridBagConstraints);
		
	}
	
	/**
	 * Obtiene el checkbox que informa de si asigna valor noData a los
	 * pixeles del interior de las ROIs o a los del exterior.
	 * @return JCheckBox
	 */
	private JCheckBox getInverse() {
		if(negative == null) {
			negative = new JCheckBox();
			negative.setText(RasterToolsUtil.getText(this, "inversa"));
			negative.addChangeListener(this);
		}
		return negative;
	}
	
	/**
	 * Obtiene el checkbox que informa de si se activa la generación de capa
	 * de transparencia o no.
	 * @return JCheckBox
	 */
	private JCheckBox getTransparencyActive() {
		if(transp == null) {
			transp = new JCheckBox();
			transp.setText(RasterToolsUtil.getText(this, "transparencia"));
			transp.addChangeListener(this);
		}
		return transp;
	}
	
	/**
	 * Obtiene el valor noData
	 * @return DataInputContainer
	 */
	private DataInputContainer getValueNoData() {
		if(valueNoData == null) {
			valueNoData = new DataInputContainer();
			valueNoData.setLabelText(RasterToolsUtil.getText(this, "value"));
			valueNoData.setValue(RasterLibrary.defaultNoDataValue + "");
			valueNoData.getDataInputField().addValueChangedListener(this);
		}
		return valueNoData;
	}
	
	/**
	 * Obtiene el mensaje de aviso de que no hay rois en la lista. Esta etiqueta solo
	 * es mostrada en caso en que la capa no tenga ROIs asociadas.
	 * @return JLabel Etiqueta con el mensaje de aviso.
	 */
	public JLabel getWarning() {
		if(warning == null) {
			warning = new JLabel(RasterToolsUtil.getText(this, "rois_needed"));
			warning.setVisible(false);
		}
		return warning;
	}
	
	/**
	 * Obtiene el contenedor con la tabla.
	 * @return
	 */
	private TableContainer getTableContainer() {
		if (tableContainer == null) {
			String[] columnNames = {" ", "Nombre", ""};
			int[] columnWidths = {22, 334, 0};
			tableContainer = new TableContainer(columnNames, columnWidths);
			tableContainer.setPreferredSize(new Dimension(0, 130));
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
	 * Asigna la lista de regiones de interés.
	 * @param rois Lista de ROIs
	 */
	public void setRois(ArrayList rois) {
		this.rois = rois;
	}
	
	/**
	 * Asigna la capa.
	 * @param layer
	 */
	public void setLayer(FLayer layer) {
		this.layer = layer;
		if (layer == null)
			return;
		
		if(((FLyrRasterSE) layer).getRois() == null || ((FLyrRasterSE) layer).getRois().size() == 0)
			getWarning().setVisible(true);
		
		ArrayList roisArray = ((FLyrRasterSE) layer).getRois();
		if (roisArray != null) {
			for (int i = 0; i < roisArray.size(); i++) {
				ROI roi = (ROI) roisArray.get(i);
	
				Object row[] = {"", "", ""};
				
				boolean active = false;
				
				if (rois != null) {
					for (int r = 0; r < rois.size(); r++) {
						if (((ROI) rois.get(r)) == roi) {
							active = true;
							break;
						}
					}
				}
				
				row[0] = new Boolean(active);
				row[1] = roi.getName(); 
				row[2] = new Integer(i);
				try {
					getTableContainer().addRow(row);
				} catch (NotInitializeException e) {
				}
			}
		}
	}
	
	/**
	 * Obtiene la lista de ROIs seleccionadas
	 * @return ArrayList con la lista de ROIs
	 */
	private ArrayList getSelectedROIs() {
		if (layer == null)
			return null;

		ArrayList roisArray = ((FLyrRasterSE) layer).getRois();
		ArrayList selected = new ArrayList();
		if (roisArray != null) {
			for (int i = 0; i < roisArray.size(); i++) {
				try {
					if (((Boolean) tableContainer.getModel().getValueAt(i, 0)).booleanValue()) {
						selected.add(roisArray.get(i));
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					//Entra aquí si se han añadido ROIs con el cuadro abierto. Pasamos de hacer nada
				}
			}
		}
		return selected;
	}
	
	/**
	 * Sobrecargamos el método getParams para que siempre devuelva
	 * algo.
	 */
	public Params getParams() {
		params = new Params();
		params.setParam("rois",
				getSelectedROIs(),
				-1,
				null);
		params.setParam("inverse",
				new Boolean(getInverse().isSelected()),
				-1,
				null);
		try {
			params.setParam("nodata",
					Double.valueOf(getValueNoData().getValue()),
					-1,
					null);
		} catch (NumberFormatException e) {
			params.setParam("nodata", Double.valueOf(-99999), -1, null);
		}
		return params;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
	 */
	public void tableChanged(TableModelEvent e) {
		callStateChanged();
	}

	/**
	 * Cambio de estado para el check de inversa
	 * @param e
	 */
	public void stateChanged(ChangeEvent e) {
		if(e.getSource().equals(getTransparencyActive())) {
			if(((JCheckBox)e.getSource()).isSelected() != lastTransp) {
				callStateChanged();
				lastTransp = ((JCheckBox)e.getSource()).isSelected();
			}
		}
		if(e.getSource().equals(getInverse())) {
			if(((JCheckBox)e.getSource()).isSelected() != lastInv) {
				callStateChanged();
				lastInv = ((JCheckBox)e.getSource()).isSelected();
			}
		}
	}

	/**
	 * Cambio de valor para la entrada de texto para el valor de fondo
	 * @param e
	 */
	public void actionValueChanged(EventObject e) {
		callStateChanged();
	}
}