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
package org.gvsig.georeferencing.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.io.IOException;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.georeferencing.ui.options.GeorefOptionsDialog;
import org.gvsig.georeferencing.ui.table.GCPTablePanel;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener;
import org.gvsig.gui.beans.table.exceptions.NotInitializeException;
import org.gvsig.raster.dataset.IRasterDataSource;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.serializer.RmfSerializerException;
import org.gvsig.raster.datastruct.GeoPoint;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.raster.util.RasterUtilities;

import com.iver.andami.PluginServices;

/**
 * Listener para los botones de control de la aplicación de georreferenciación.
 * Físicamente están en el panel de la tabla de puntos de control.
 *
 * 25/01/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class ApplicationControlsListener implements ActionListener, TableModelListener, ButtonsPanelListener {

	private GCPTablePanel             tablePanel          = null;
	private Georeferencing            appMain             = null;
	private boolean                   enableTableEvent    = true;
	private GeorefOptionsDialog       optionsDialog       = null;
	private GeoPointsPersistence      persistence         = null;

	/**
	 * Flag que estará a true después de crear un nuevo punto de control. Volverá a false
	 * cuando se ejecute el evento de inserción de fila en una tabla. Esto es necesario para
	 * que no se centre el punto sobre la vista después de insertar un nuevo punto. Ya que las
	 * vistas se van a la posición (0,0)
	 */
	private boolean                   newPointCreated     = false;

	/**
	 * Constructor. Asigna el panel con los controles
	 * @param tablePanel
	 */
	public ApplicationControlsListener(GCPTablePanel tablePanel, Georeferencing appMain) {
		this.tablePanel = tablePanel;
		this.appMain = appMain;
	}

	/**
	 * Eventos de los botones del cuadro de tabla y selección de fila de tabla
	 */
	public void actionPerformed(ActionEvent e) {
		//-------------------------------------
		//Finalización de la georreferenciación
		if(e.getSource() == tablePanel.getEndGeorefButton())
			endGeoref();

		//-----------------------------------------
		//Centrar la vista en el punto seleccionado
		if(e.getSource() == tablePanel.getCenterButton()) {
			try {
				getViewsManager().centerToPoint(tablePanel.getTable().getSelectedRow());
			} catch (NotInitializeException e1) {
				RasterToolsUtil.messageBoxYesOrNot("table_not_initialize", tablePanel, e1);
			}
		}

		//---------------------------------
		//Selección de una fila de la tabla
		if(e.getActionCommand().equals("SELECT_ROW")) {
			if(getOptions().isCenterView() && !newPointCreated)
				getViewsManager().centerToPoint(e.getID());
			newPointCreated = false;
		}

		//----------------------------------------------------------
		//Seleccion de opciones. Se asignan las propiedades al abrir
		if(e.getSource() == tablePanel.getOptionsButton()) {
			if(optionsDialog == null)
				optionsDialog = new GeorefOptionsDialog(Georeferencing.DEFAULT_DEGREE, this);
			optionsDialog.setDegree(getOptions().getDegree());
			optionsDialog.setAlgorithm(getOptions().getAlgorithm());
			optionsDialog.setBackGroundColor(getOptions().getBackgroundColor());
			optionsDialog.setTextColor(getOptions().getTextColor());
			optionsDialog.getOptionsPanel(this).getCheckOptionsPanel().getAddErrorsCSVCheck().setSelected(getOptions().isAddErrorsCSV());
			optionsDialog.getOptionsPanel(this).getCheckOptionsPanel().getCenterViewCheck().setSelected(getOptions().isCenterView());
			optionsDialog.getOptionsPanel(this).getCheckOptionsPanel().getShowNumberCheck().setSelected(getOptions().isShowNumber());
			optionsDialog.setThresholdError(getOptions().getThreshold());
			optionsDialog.setInterpolationMethod(getOptions().getInterpolationMethod());
			optionsDialog.setOutFile(getOptions().getOutFile());
			optionsDialog.setXCellSize(getOptions().getXCellSize());
			optionsDialog.setYCellSize(getOptions().getYCellSize());
			PluginServices.getMDIManager().addWindow(optionsDialog);
		}

		//------------------------------
		//Selección de puntos de control
		if( e.getSource() == tablePanel.getToolSelectPointButton()) {
			if( tablePanel.getToolSelectPointButton().isSelected()) {
				try {
					if(!getPointManager().isSelectPointToolSelected()) {
						//No hay filas en la tabla
						if(tablePanel.getTable().getRowCount() == 0) {
							RasterToolsUtil.messageBoxError("no_rows", tablePanel);
							tablePanel.getToolSelectPointButton().setSelected(false);
							return;
						}

						//No hay filas seleccionadas en la tabla
						if(tablePanel.getTable().getSelectedRow() == -1) {
							RasterToolsUtil.messageBoxError("no_rows_selected", tablePanel);
							tablePanel.getToolSelectPointButton().setSelected(false);
							return;
						}
						getViewsManager().selectPointTool(true);
					}
				} catch (NotInitializeException e1) {
					RasterToolsUtil.messageBoxYesOrNot("table_not_initialize", tablePanel, e1);
				}
			} else {
				getViewsManager().selectPointTool(false);
			}
		}

		//-------
		//Testear
		if( e.getSource() == tablePanel.getTestButton()) {
			appMain.addTestRasterLayer(appMain.getLayer());
		}

		//------------
		//Fin del Test
		if( e.getSource() == tablePanel.getEndTestButton()) {
			appMain.getLastTestLayer().setRemoveRasterFlag(false);
			appMain.removeTestRasterLayer();
		}

		//------------
		//Salvar a CSV
		if( e.getSource() == tablePanel.getExporToCSVButton()) {
			getPersistence().exportToCSV(getPointManager().getPointList(), getOptions().isAddErrorsCSV());
		}

		//----------------
		//Cargar desde CSV
		if( e.getSource() == tablePanel.getLoadFromCSVButton()) {
			setEnableTableEvent(false);
			getPersistence().importFromCSV(getPointManager(), tablePanel);
			setEnableTableEvent(true);
		}

		//------------
		//Salvar a RMF
		if( e.getSource() == tablePanel.getSaveToXMLButton()) {
			getPersistence().saveToRMF(getPointManager().getPointList(), appMain.getLayer().getDataSource());
		}

		//----------------
		//Cargar desde RMF
		if( e.getSource() == tablePanel.getLoadFromXMLButton()) {
			setEnableTableEvent(false);
			getPersistence().loadFromRMF(appMain.getLayer().getDataSource(), getPointManager(), tablePanel);
			setEnableTableEvent(true);
		}
	}

	/**
	 * Acciones de finalización de georreferenciación. La secuencia de acciones es:
	 * <UL>
	 * <LI>Preguntar si está seguro de finalizar.</LI>
	 * <LI>Preguntar si se desea salvar la transformación (Solo en caso de ser afín).</LI>
	 * <LI>Preguntar si se desea cargar el resultado en la vista (Solo en caso de ser con mapa de referencia).</LI>
	 * </UL>
	 */
	private void endGeoref() {
		if(RasterToolsUtil.messageBoxYesOrNot("ask_end_georef", null)) {
			boolean saveTransform = false;
			if(appMain.getOptions().getAlgorithm() == Georeferencing.AFFINE) {
				if(RasterToolsUtil.messageBoxYesOrNot("ask_save_transformation", null))
					saveTransform = true;
				else {
					appMain.removeTestRasterLayer();
					appMain.closeAllWindows();
					return;
				}
			}

			if(appMain.getOptions().getType() == Georeferencing.WITH_MAP) {
				if(appMain.getLastTestLayer() != null)
					appMain.getLastTestLayer().setRemoveRasterFlag(false);

				if(RasterToolsUtil.messageBoxYesOrNot("ask_load_view", null)) {
					FLayers lyrs = appMain.getLayers();
					if(lyrs != null) {
						try {
							if(appMain.getLastTestLayer() != null){
								lyrs.removeLayer(appMain.getLastTestLayer());
								PluginServices.getMainFrame().enableControls();
							}
						} catch(NullPointerException ex){
							//No hay raster para eliminar
						}
					}

					//Cargar en la vista
					if(appMain.getOptions().getAlgorithm() == Georeferencing.AFFINE) {
						appMain.addTestRasterLayer(appMain.getLayer());
					}
					if(appMain.getOptions().getAlgorithm() == Georeferencing.POLYNOMIAL) {
						if(appMain.getLastTestLayer() != null && lyrs != null) {
							appMain.getLayers().addLayer(appMain.getLastTestLayer());
						} else {
							RasterToolsUtil.messageBoxInfo("raster_not_created", null);
							return;
						}
					}
				} else {
					appMain.removeTestRasterLayer();
				}
			}

			if (saveTransform) {
				// Salvar transformación
				if (appMain.getLastTestLayer() == null) {
					RasterToolsUtil.messageBoxInfo("raster_not_created", null);
					return;
				}

				// Guardamos la GeoReferenciacion de cada dataset
				IRasterDataSource dataSource = appMain.getLastTestLayer().getDataSource();
				try {
					for (int i = 0; i < dataSource.getDatasetCount(); i++) {
						dataSource.saveObjectToRmf(i, RasterDataset.class, dataSource.getDataset(i)[0]);
						RasterUtilities.createWorldFile(dataSource.getNameDatasetStringList(0, 0)[i], dataSource.getAffineTransform(0), (int)dataSource.getWidth(), (int)dataSource.getHeight());
					}
				} catch (RmfSerializerException e) {
					RasterToolsUtil.messageBoxError("error_salvando_rmf", null, e);
				} catch (IOException e) {
					RasterToolsUtil.messageBoxError("error_salvando_rmf", null, e);
				}
			}

			appMain.closeAllWindows();
		}
	}

	/**
	 * Evento de modificación de la tabla de puntos de control. El añadir, borrar
	 * o mover puntos dentro de la tabla hace que estos se creen, borren o muevan en
	 * la lista de GeoPoints.
	 */
	public void tableChanged(TableModelEvent e) {
		if(!enableTableEvent)
			return;
		setEnableTableEvent(false);
		try{

			//-----------------------------------
			//Inserción de nuevo fila en la tabla
			if(e.getType() == TableModelEvent.INSERT) {
				long id = getPointManager().addPoint(new Point2D.Double(0, 0), new Point2D.Double(0, 0));
				tablePanel.initializeRow(e.getFirstRow(), id);
				getPointManager().calcPointsNumeration(tablePanel);

				//Activamos la herramienta de pinchar punto en la vista al añadir punto
				tablePanel.getToolSelectPointButton().setSelected(true);
				getViewsManager().selectPointTool(true);

				newPointCreated = true;
			}

			//-----------------------------
			//Eliminado de fila de la tabla
			if(e.getType() == TableModelEvent.DELETE) {
				for (int i = e.getLastRow(); i >= e.getFirstRow(); i--)
					getPointManager().removePoint(i);
				getPointManager().calcPointsNumeration(tablePanel);
				tablePanel.updateErrors();
			}

			//-------------------------------------
			//Actualización de una fila de la tabla
			if(e.getType() == TableModelEvent.UPDATE) {
				getPointManager().calcPointsNumeration(tablePanel);
				//Revisamos el flag de activo de cada punto poniendo el valor que pone en la entrada de la tabla
				for (int i = 0; i < tablePanel.getRowCount(); i++) {
					boolean active = ((Boolean) tablePanel.getValueAt(i, 0)).booleanValue();
					int position = getPointManager().getPointPositionByNumber(i);
					if (position < 0)
						return;
					GeoPoint pt = getPointManager().getGeoPoints().get(position);
					pt.active = active;
					getPointManager().setVisiblePoint(active, position);

					//Para la funcionalidad de cambio de coordenadas desde la tabla: reasignamos el valor
					//de las coordenadas por si han cambiado estas
					double[] values = tablePanel.getCoordinates(i);

					getPointManager().setCoordinates(position, values[0], values[1], values[2], values[3]);
				}
				tablePanel.updateErrors();
			}

		}catch (NotInitializeException ex) {
			RasterToolsUtil.messageBoxError("table_not_initialize", tablePanel, ex);
		}
		setEnableTableEvent(true);
	}

	/**
	 * Eventos de los botones del cuadro de opciones
	 */
	public void actionButtonPressed(ButtonsPanelEvent e) {

		//---------------------------------------------------
		//Botón de Aplicar y Aceptar en el cuadro de opciones
		if (e.getButton() == ButtonsPanel.BUTTON_APPLY || e.getButton() == ButtonsPanel.BUTTON_ACCEPT) {
			if(optionsDialog != null) {

				//Asignamos las propiedades seleccionadas
				getOptions().setAlgorithm(optionsDialog.getAlgorithm());
				getOptions().setBackgroundColor(optionsDialog.getBackGroundColor());
				getOptions().setTextColor(optionsDialog.getTextColor());
				getOptions().setAddErrorsCSV(optionsDialog.getOptionsPanel(this).getCheckOptionsPanel().getAddErrorsCSVCheck().isSelected());
				getOptions().setCenterView(optionsDialog.getOptionsPanel(this).getCheckOptionsPanel().getCenterViewCheck().isSelected());
				getOptions().setShowNumber(optionsDialog.getOptionsPanel(this).getCheckOptionsPanel().getShowNumberCheck().isSelected());
				getOptions().setInterpolationMethod(optionsDialog.getInterpolationMethod());
				getOptions().setOutFile(optionsDialog.getOptionsPanel(this).getOutFileSelectionPanel().getOutFile());

				//Recalculamos los errores si ha cambiado el grado
				if(optionsDialog.getDegree() != getOptions().getDegree()) {
					getOptions().setDegree(optionsDialog.getDegree());
					tablePanel.updateErrors();
				}

				try {
					getOptions().setThreshold(Double.valueOf(optionsDialog.getThresholdError().getValue()).doubleValue());
					getOptions().setXCellSize(optionsDialog.getOptionsPanel(this).getCellSizePanel().getXCellSizeValue());
					getOptions().setYCellSize(optionsDialog.getOptionsPanel(this).getCellSizePanel().getYCellSizeValue());
				} catch(NumberFormatException exc) {
					RasterToolsUtil.messageBoxError("error_numeric_format", null, exc);
				}

				getViewsManager().setGraphicsColor(getOptions().getTextColor());
				getViewsManager().setBackgroundColor(getOptions().getBackgroundColor());
				getPointManager().setActiveNumberPoint(getOptions().isShowNumber());

			}
		}

		//--------------------------------------------------
		// Botón de Cerrar y Aceptar en el cuadro de opciones
		if (e.getButton() == ButtonsPanel.BUTTON_CANCEL || e.getButton() == ButtonsPanel.BUTTON_ACCEPT) {
			if(optionsDialog != null)
				PluginServices.getMDIManager().closeWindow(optionsDialog);
		}
	}

	/**
	 * Consulta si están activos los eventos de tabla o no
	 * @return true si están activos y false si no lo están
	 */
	public boolean isEnableTableEvent() {
		return enableTableEvent;
	}

	/**
	 * Activa o desactiva los eventos de tabla
	 * @param enableTableEvent true para activarlos o false para desactivarlos
	 */
	public void setEnableTableEvent(boolean enableTableEvent) {
		this.enableTableEvent = enableTableEvent;
	}

	/**
	 * Obtiene el gestor para capas de puntos de la aplicación,
	 * @return LayersPointManager
	 */
	public LayersPointManager getPointManager() {
		return appMain.getLayerPointManager();
	}

	/**
	 * Obtiene el gestor para las vistas de la aplicación,
	 * @return ViewsManager
	 */
	public ViewsManager getViewsManager() {
		return appMain.getViewsManager();
	}

	/**
	 * Obtiene el objeto con las opciones almacenadas
	 * @return GeorefOptions
	 */
	public GeorefOptions getOptions() {
		return appMain.getOptions();
	}

	/**
	 * Obtiene una instancia del objeto que gestiona la
	 * persistencia en disco de los puntos de control
	 * @return GeoPointsPersistence
	 */
	public GeoPointsPersistence getPersistence() {
		if(persistence == null)
			persistence = new GeoPointsPersistence();
		return persistence;
	}
}
