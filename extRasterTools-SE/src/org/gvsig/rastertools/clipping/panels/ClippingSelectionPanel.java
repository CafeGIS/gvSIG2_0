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
package org.gvsig.rastertools.clipping.panels;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JPanel;

import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.table.TableContainer;
import org.gvsig.gui.beans.table.exceptions.NotInitializeException;
import org.gvsig.raster.dataset.IRasterDataSource;
import org.gvsig.raster.util.RasterToolsUtil;

import com.iver.andami.PluginServices;
/**
 * Panel de seleccion de que bandas. Permite seleccionar que bandas se van a
 * guardar y especificar su orden.
 * 
 * @version 25/09/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class ClippingSelectionPanel extends JPanel {
  private static final long serialVersionUID = 813234785743904477L;
	private TableContainer tableContainer = null;

	/**
	 * Construye un Panel de seleccion para el recorte
	 */
	public ClippingSelectionPanel() {
		initialize();
	}
	

	private void initialize() {
		setLayout(new BorderLayout());
		add(getTableContainer(), BorderLayout.CENTER);
	}
	
	/**
	 * Obtiene la tabla de selección de bandas
	 * @return Tabla de selección de bandas
	 */
	public TableContainer getTableContainer() {
		if (tableContainer == null) {
			String[] columnNames = {PluginServices.getText(this, "bandas"), PluginServices.getText(this, "nombre"), ""};
			int[] columnWidths = {55, 305, 0};
			tableContainer = new TableContainer(columnNames, columnWidths);
			tableContainer.setModel("CheckBoxModel");
			tableContainer.initialize();
			tableContainer.setControlVisible(false);
			tableContainer.setMoveRowsButtonsVisible(true);
			
			tableContainer.getTable().getJTable().getColumnModel().getColumn(2).setMinWidth(0);
			tableContainer.getTable().getJTable().getColumnModel().getColumn(2).setMaxWidth(0);
			tableContainer.getTable().getJTable().getColumnModel().getColumn(0).setMinWidth(55);
			tableContainer.getTable().getJTable().getColumnModel().getColumn(0).setMaxWidth(55);
			
		}

		return tableContainer;
	}
	
	/**
	 * Establecer la capa para usarla en el recorte
	 * @param fLayer
	 */
	public void setLayer(FLyrRasterSE fLayer) {
		// Rellenar el arbol de bandas
		IRasterDataSource mDataset = fLayer.getDataSource();
		int cont = 0;
		for (int i = 0; i < mDataset.getDatasetCount(); i++) {
			String fName = mDataset.getDataset(i)[0].getFName();
			String bandName = new File(fName).getName();

			if (mDataset.getDataset(i)[0].getBandCount() > 1) {
				for (int b = 0; b < mDataset.getDataset(i)[0].getBandCount(); b++) {
					Object row[] = { new Boolean(true), new String("B" + (b + 1) + " - " + bandName), new Integer(cont++)};
					try {
						getTableContainer().addRow(row);
					} catch (NotInitializeException e) {
						RasterToolsUtil.messageBoxError("error_rowtable", this, e);
					}
				}
			} else {
				Object row[] = { new Boolean(true), new String("B - " + bandName), new Integer(cont++)};
				try {
					getTableContainer().addRow(row);
				} catch (NotInitializeException e) {
					RasterToolsUtil.messageBoxError("error_rowtable", this, e);
				}
			}
		}
	}
}