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

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.File;

import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.georeferencing.process.GeoreferencingProcess;
import org.gvsig.georeferencing.process.geotransform.GeoTransformDataResult;
import org.gvsig.georeferencing.ui.table.GCPTablePanel;
import org.gvsig.georeferencing.ui.zoom.InvalidRequestException;
import org.gvsig.georeferencing.ui.zoom.ViewMapRequestManager;
import org.gvsig.georeferencing.ui.zoom.ViewRasterRequestManager;
import org.gvsig.georeferencing.ui.zoom.tools.SelectPointTool;
import org.gvsig.georeferencing.view.ViewDialog;
import org.gvsig.georeferencing.view.ZoomDialog;
import org.gvsig.gui.beans.table.exceptions.NotInitializeException;
import org.gvsig.raster.IProcessActions;
import org.gvsig.raster.datastruct.GeoPointList;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.raster.util.RasterUtilities;


/**
 * Gestor para operaciones con las vistas y zooms
 *
 * 04/02/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class ViewsManager implements IProcessActions {
	//Dialogos
	private ViewDialog                    viewRaster                     = null;
	private ViewDialog                    viewMap                        = null;
	private ZoomDialog                    zoomMap                        = null;
	private ZoomDialog                    zoomRaster                     = null;

	//Gestores de peticiones
	private ViewRasterRequestManager      viewRasterRequestManager       = null;
	private ViewMapRequestManager         viewMapRequestManager          = null;
	private ViewRasterRequestManager      zoomRasterRequestManager       = null;
	private ViewMapRequestManager         zoomMapRequestManager          = null;

	private GCPTablePanel                 table                          = null;
	private String                        fileName                       = null;

	//Última capa procesada con los puntos de control. Al cerrar la aplicación
	//esta será la que se use como resultado
	private FLyrRasterSE                  lastTestLayer                  = null;

	/**
	 * Asigna las vistas
	 * @param vRaster
	 * @param vMap
	 * @param zRaster
	 * @param zMap
	 */
	public void setViews(ViewDialog vRaster, ViewDialog vMap, ZoomDialog zRaster, ZoomDialog zMap) {
		viewRaster = vRaster;
		viewMap = vMap;
		zoomMap = zMap;
		zoomRaster = zRaster;
	}

	/**
	 * Asigna los gestores de peticiones.
	 * @param vRaster
	 * @param vMap
	 * @param zRaster
	 * @param zMap
	 */
	public void setRequestsManager(ViewRasterRequestManager vRaster, ViewMapRequestManager vMap, ViewRasterRequestManager zRaster, ViewMapRequestManager zMap) {
		viewRasterRequestManager = vRaster;
		viewMapRequestManager= vMap;
		zoomRasterRequestManager = zRaster;
		zoomMapRequestManager = zMap;
	}

	/**
	 * Asigna el panel con la tabla
	 * @param tablePanel
	 */
	public void setTablePanel(GCPTablePanel tablePanel) {
		this.table = tablePanel;
	}

	/**
	 * Desactiva la tool activa de las vistas
	 * @param active
	 */
	public void sleepActiveTools() {
		if(zoomMap != null)
			zoomMap.getControl().sleepTools();
		if(zoomRaster != null)
			zoomRaster.getControl().sleepTools();
		if(viewRaster != null)
			viewRaster.getControl().sleepTools();
		if(viewMap != null)
			viewMap.getControl().sleepTools();
	}

	/**
	 * Activa la tool activa de las vistas
	 * @param active
	 */
	public void awakeActiveTools() {
		if(zoomMap != null)
			zoomMap.getControl().awakeTools();
		if(zoomRaster != null)
			zoomRaster.getControl().awakeTools();
		if(viewRaster != null)
			viewRaster.getControl().awakeTools();
		if(viewMap != null)
			viewMap.getControl().awakeTools();
	}

	/**
	 * Selecciona o deselecciona la tool de selección de punto para cada vista
	 * @param true para activar y false para desactivar
	 */
	public void selectPointTool(boolean select) {
		if(zoomMap != null)
			zoomMap.getControl().selectTool(SelectPointTool.class, select);
		if(zoomRaster != null)
			zoomRaster.getControl().selectTool(SelectPointTool.class, select);
		if(viewRaster != null)
			viewRaster.getControl().selectTool(SelectPointTool.class, select);
		if(viewMap != null)
			viewMap.getControl().selectTool(SelectPointTool.class, select);
	}

	/**
	 * Asigna el color de los gráficos.
	 * @param c
	 */
	public void setGraphicsColor(Color c) {
		if(viewRaster != null) {
			viewRaster.getCanvas().setTextColor(c);
			viewRaster.getZoomCursorGraphicLayer().setColor(c);
		}
		if(viewMap != null) {
			viewMap.getCanvas().setTextColor(c);
			viewMap.getZoomCursorGraphicLayer().setColor(c);
		}
		if(zoomRaster != null)
			zoomRaster.getCanvas().setTextColor(c);
		if(zoomMap != null)
			zoomMap.getCanvas().setTextColor(c);
	}

	/**
	 * Asigna el color de los gráficos.
	 * @param c
	 */
	public void setBackgroundColor(Color c) {
		if(viewMapRequestManager != null)
			viewMapRequestManager.setBackGroundColor(c);
		if(viewRasterRequestManager != null)
			viewRasterRequestManager.setBackGroundColor(c);
		if(zoomMapRequestManager != null)
			zoomMapRequestManager.setBackGroundColor(c);
		if(zoomRasterRequestManager != null)
			zoomRasterRequestManager.setBackGroundColor(c);
		try {
			if(viewRaster != null) {
				viewRaster.getCanvas().setBackgroundColor(c);
				viewRaster.getCanvas().setForceRequest(true);
				viewRaster.getControl().getExtensionRequest().request(viewRaster.getCanvas().getEnvelope());
			}
			if(viewMap != null) {
				viewMap.getCanvas().setBackgroundColor(c);
				viewMap.getCanvas().setForceRequest(true);
				viewMap.getControl().getExtensionRequest().request(viewMap.getCanvas().getEnvelope());
			}
			if(zoomRaster != null) {
				zoomRaster.getCanvas().setBackgroundColor(c);
				zoomRaster.getCanvas().setForceRequest(true);
				zoomRaster.getControl().getExtensionRequest().request(zoomRaster.getCanvas().getEnvelope());
			}
			if(zoomMap != null) {
				zoomMap.getCanvas().setBackgroundColor(c);
				zoomMap.getCanvas().setForceRequest(true);
				zoomMap.getControl().getExtensionRequest().request(zoomMap.getCanvas().getEnvelope());
			}
		} catch (InvalidRequestException e) {
		}
	}

	/**
	 * Centra las vistas sobre el punto pasado por parámetro
	 * @param n Número de punto sobre el que se centrará la visualización
	 */
	public void centerToPoint(int n) {
		try {
			if(table.getTable().getRowCount() <= 0) {
				RasterToolsUtil.messageBoxError("no_selected_point", table);
				return;
			}

			double[] values = table.getCoordinates(n);
			double xMap = values[0];
			double yMap = values[1];
			double xRaster = values[2];
			double yRaster = values[3];

			if(viewRaster != null)
				viewRaster.setCenter(new Point2D.Double(xRaster, yRaster));
			if(zoomRaster != null)
				zoomRaster.getControl().setCenter(new Point2D.Double(xRaster, yRaster));
			if(viewMap != null)
				viewMap.setCenter(new Point2D.Double(xMap, yMap));
			if(zoomMap != null)
				zoomMap.getControl().setCenter(new Point2D.Double(xMap, yMap));
		} catch (NotInitializeException e1) {
			RasterToolsUtil.messageBoxYesOrNot("table_not_initialize", table, e1);
		}
	}

	/**
	 * Añade una capa en la vista del mapa para previsualizar el resultado
	 * @param lyr
	 * @param algorithm Algoritmo con el que se realiza la transformación
	 */
	public void addTestRasterLayer(	FLyrRasterSE lyr,
									int algorithm,
									int method,
									int order,
									String file) {
		if(!testNumberOfPoints(order))
			return;

		fileName = file;
		FLyrRasterSE lyrClon = null;
		try {
			lyrClon = (FLyrRasterSE)lyr.cloneLayer();
		} catch (Exception e1) {
			RasterToolsUtil.messageBoxError("error_clone_layer", this, e1);
			return;
		}

		if(algorithm == Georeferencing.AFFINE) {
			GeoTransformDataResult result = table.getGeoTransformDataResult();

			if(result == null) {
				RasterToolsUtil.messageBoxInfo("error_georef", this);
				return;
			}

			AffineTransform at = new AffineTransform(	result.getPixelToMapCoefX()[1],
					result.getPixelToMapCoefY()[1],
					result.getPixelToMapCoefX()[2],
					result.getPixelToMapCoefY()[2],
					result.getPixelToMapCoefX()[0],
					result.getPixelToMapCoefY()[0]);
			lyrClon.setAffineTransform(at);
			lastTestLayer = lyrClon;
		}

		if(algorithm == Georeferencing.POLYNOMIAL) {
			GeoreferencingProcess process = new GeoreferencingProcess();
			process.addParam("fLayer", lyr);
			process.addParam("filename", file);
			process.addParam("method", new Integer(method));
			process.addParam("gpcs", table.getAppMain().getLayerPointManager().getGeoPoints());
			process.addParam("orden", new Integer(order));
			process.addParam("xCellSize", new Double(table.getAppMain().getOptions().getXCellSize()));
			process.addParam("yCellSize", new Double(table.getAppMain().getOptions().getYCellSize()));
			process.setActions(this);
			process.start();
		}

		//Con vista de de referencia cargamos la preview en esta
		if(viewMapRequestManager != null) {
			if(algorithm == Georeferencing.AFFINE) {
				try {
					viewMapRequestManager.addTestRasterLayer(lyrClon);
				} catch (InvalidRequestException e) {
					RasterToolsUtil.messageBoxError("error_setview_preview", this, e);
				}
			}
		}
	}

	/**
	 * Consulta si hay suficientes puntos de control en la lista para los calculos
	 * @param order Orden del polinomio a utilizar
	 * @return true si hay suficientes puntos de control y false si no los hay
	 */
	private boolean testNumberOfPoints(int order) {
		GeoPointList gp = table.getAppMain().getLayerPointManager().getGeoPoints();
		if (gp != null) {
			// Obtenemos el número de puntos activos
			int nPointsActive = 0;
			for (int i = 0; i < gp.size(); i++) {
				if (gp.get(i).active)
					nPointsActive++;
			}
			int nPoints = (order + 1) * (order + 2) / 2;
			if (nPointsActive < nPoints) {
				RasterToolsUtil.messageBoxError(RasterToolsUtil.getText(this, "more_points") + ((int) Math.ceil(nPoints)), null);
				return false;
			}
		}
		return true;
	}

	/**
	 * Elimina la capa de test de la vista de mapa
	 * @throws InvalidRequestException
	 */
	public void removeTestRasterLayer() {
		if(viewMapRequestManager != null) {
			try {
				viewMapRequestManager.removeTestRasterLayer();
			} catch (InvalidRequestException e) {
				RasterToolsUtil.messageBoxError("error_setview_preview", this);
			}
		}
	}

	/**
	 * Obtiene la vista con el mapa de referencia
	 * @return ViewDialog
	 */
	public ViewDialog getViewMap() {
		return viewMap;
	}

	/**
	 * Obtiene la vista con el raster a georreferenciar
	 * @return ViewDialog
	 */
	public ViewDialog getViewRaster() {
		return viewRaster;
	}

	/**
	 * Obtiene la vista con el zoom del mapa de referencia
	 * @return ZoomMapDialog
	 */
	public ZoomDialog getZoomMap() {
		return zoomMap;
	}

	/**
	 * Obtiene la vista con el zoom del raster a georreferenciar
	 * @return ZoomRasterDialog
	 */
	public ZoomDialog getZoomRaster() {
		return zoomRaster;
	}

	/**
	 * Obtiene el gestor de la vista con la cartografía de referencia
	 * @return ViewMapRequestManager
	 */
	public ViewMapRequestManager getViewMapRequestManager() {
		return viewMapRequestManager;
	}

	/**
	 * Obtiene el gestor de la vista con el raster a georreferenciar
	 * @return ViewRasterRequestManager
	 */
	public ViewRasterRequestManager getViewRasterRequestManager() {
		return viewRasterRequestManager;
	}

	/**
	 * Obtiene el gestor de la vista zoom con la cartografía de referencia
	 * @return ViewMapRequestManager
	 */
	public ViewMapRequestManager getZoomMapRequestManager() {
		return zoomMapRequestManager;
	}

	/**
	 * Obtiene el gestor de la vista zoom con el raster
	 * @return ViewRasterRequestManager
	 */
	public ViewRasterRequestManager getZoomRasterRequestManager() {
		return zoomRasterRequestManager;
	}

	/**
	 * Cuando termina el proceso de georreferenciación carga la capa en la
	 * vista con cartografía de referencia si esta existe.
	 */
	public void end(Object param) {
		if(viewMapRequestManager != null) {
			try {
				FLyrRasterSE lyr = FLyrRasterSE.createLayer(RasterUtilities.getLastPart(fileName, File.separator), fileName, null);
				viewMapRequestManager.addTestRasterLayer(lyr);
				lastTestLayer = lyr;
			} catch (InvalidRequestException e) {
				RasterToolsUtil.messageBoxError("error_setview_preview", this, e);
			} catch (LoadLayerException e) {
				RasterToolsUtil.messageBoxError("error_setview_preview", this, e);
			}
		}
	}

	public void interrupted() {
	}

	/**
	 * Obtiene la última capa procesada con los puntos de  control
	 * @return FLyrRaterSE
	 */
	public FLyrRasterSE getLastTestLayer() {
		return lastTestLayer;
	}

}
