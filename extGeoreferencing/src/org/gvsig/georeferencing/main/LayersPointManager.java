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

import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.gvsig.georeferencing.ui.table.GCPTablePanel;
import org.gvsig.georeferencing.ui.zoom.ViewMapRequestManager;
import org.gvsig.georeferencing.ui.zoom.ViewRasterRequestManager;
import org.gvsig.georeferencing.ui.zoom.layers.GCPsGraphicLayer;
import org.gvsig.georeferencing.ui.zoom.layers.GPGraphic;
import org.gvsig.georeferencing.ui.zoom.tools.PanTool;
import org.gvsig.georeferencing.ui.zoom.tools.SelectPointTool;
import org.gvsig.georeferencing.ui.zoom.tools.ZoomRectangleTool;
import org.gvsig.georeferencing.view.BaseZoomView;
import org.gvsig.georeferencing.view.ViewDialog;
import org.gvsig.georeferencing.view.ZoomDialog;
import org.gvsig.gui.beans.table.exceptions.NotInitializeException;
import org.gvsig.raster.datastruct.GeoPoint;
import org.gvsig.raster.datastruct.GeoPointList;

/**
 * Clase para realizar operaciones y mantener la sincronización entre las 
 * capas de puntos de las dos vistas y las dos ventanas de zoom.
 * 
 * 04/02/2008
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class LayersPointManager {
	private Georeferencing                appMain                        = null;
	//Vistas
	private ViewDialog                    viewRaster                     = null;
	private ViewDialog                    viewMap                        = null;
	private ZoomDialog                    zoomMap                        = null;
	private ZoomDialog                    zoomRaster                     = null;
	
	//Capas de puntos
	private GCPsGraphicLayer              viewRasterGCPs                 = null;
	private GCPsGraphicLayer              viewMapGCPs                    = null;
	private GCPsGraphicLayer              viewRasterGCPsZoom             = null;
	private GCPsGraphicLayer              viewMapGCPsZoom                = null;
	
	//Herramientas de selección
	private SelectPointTool               viewRasterSelectPointTool      = null;
	private SelectPointTool               viewMapSelectPointTool         = null;
	private SelectPointTool               zoomRasterSelectPointTool      = null;
	private SelectPointTool               zoomMapSelectPointTool         = null;
	
	/**
	 * Constructor. Asigna la clase de georreferenciación
	 * @param appMain
	 */
	public LayersPointManager(Georeferencing appMain) {
		this.appMain = appMain;
	}
	
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
	 * Asigna las capas de puntos
	 * @param vRaster
	 * @param vMap
	 * @param zRaster
	 * @param zMap
	 */
	public void setLayerPoints(GCPsGraphicLayer vRaster, GCPsGraphicLayer vMap, GCPsGraphicLayer zRaster, GCPsGraphicLayer zMap) {
		viewRasterGCPs = vRaster;
		viewMapGCPs = vMap;
		viewRasterGCPsZoom = zRaster;
		viewMapGCPsZoom = zMap;
	}
	
	/**
	 * Asigna las herramientas de selección de puntos
	 * @param vRaster
	 * @param vMap
	 * @param zRaster
	 * @param zMap
	 */
	public void setTools(SelectPointTool vRaster, SelectPointTool vMap, SelectPointTool zRaster, SelectPointTool zMap) {
		viewRasterSelectPointTool = vRaster;
		viewMapSelectPointTool = vMap;
		zoomRasterSelectPointTool = zRaster;
		zoomMapSelectPointTool = zMap;
	}
	
	/**
	 * Activa o desactiva la capa de puntos
	 * @param active
	 */
	public void setActiveLayerZoomCursor(boolean active) {
		if(viewRaster != null && viewRaster.getZoomCursorGraphicLayer() != null) 
			viewRaster.getZoomCursorGraphicLayer().setActive(active);
		if(viewMap != null && viewMap.getZoomCursorGraphicLayer() != null)
			viewMap.getZoomCursorGraphicLayer().setActive(active);
	}
	
	/**
	 * Recalcula las coordenadas de dibujado de los puntos de control
	 * y redibuja estos en todas las vistas
	 */
	public void redrawPoints() {
		if(viewMapGCPs != null)
			viewMapGCPs.recalcMapDrawCoordinates();
		if(viewRasterGCPs != null)
			viewRasterGCPs.recalcPixelDrawCoordinates();
		if(viewMapGCPsZoom != null)
			viewMapGCPsZoom.recalcMapDrawCoordinates();
		if(viewRasterGCPsZoom != null)
			viewRasterGCPsZoom.recalcPixelDrawCoordinates();
		repaint();	
	}
	
	/**
	 * Redibuja el canvas de todas las vistas
	 */
	private void repaint() {
		if(viewRaster != null)
			viewRaster.getCanvas().repaint();
		if(viewMap != null)
			viewMap.getCanvas().repaint();
		if(zoomMap != null)
			zoomMap.getCanvas().repaint();
		if(zoomRaster != null)
			zoomRaster.getCanvas().repaint();
	}
	
	/**
	 * Registra el listener para la herramienta de selección de puntos de control
	 */
	public void registerPointToolListener() {
		if(zoomMap != null) {
			zoomMapSelectPointTool.setActive(false);
			zoomMap.getControl().addTool(zoomMapSelectPointTool);
			zoomMap.getControl().registerToolListener(ZoomRectangleTool.class, appMain);			
			zoomMap.getControl().registerToolListener(PanTool.class, appMain);
		}
		if(zoomRaster != null) {
			zoomRasterSelectPointTool.setActive(false);
			zoomRaster.getControl().addTool(zoomRasterSelectPointTool);
			zoomRaster.getControl().registerToolListener(ZoomRectangleTool.class, appMain);
			zoomRaster.getControl().registerToolListener(PanTool.class, appMain);
		}
		if(viewRaster != null) {
			viewRasterSelectPointTool.setActive(false);
			viewRaster.getControl().addTool(viewRasterSelectPointTool);
			viewRaster.getControl().registerToolListener(ZoomRectangleTool.class, appMain);
			viewRaster.getControl().registerToolListener(PanTool.class, appMain);
		}
		if(viewMap != null) {
			viewMapSelectPointTool.setActive(false);
			viewMap.getControl().addTool(viewMapSelectPointTool);
			viewMap.getControl().registerToolListener(ZoomRectangleTool.class, appMain);
			viewMap.getControl().registerToolListener(PanTool.class, appMain);
		}
	}
	
	/**
	 * Añade un punto a las capas gráficas
	 * @param map Coordenadas del mapa del punto
	 * @param raster Coordenadas pixel del punto
	 * @return identificador del punto añadido
	 */
	public long addPoint(Point2D map, Point2D raster) {
		GeoPoint gp = new GeoPoint(raster, map);
		long id = System.currentTimeMillis();	
		//Esperamos al menos 5 ms para que la carga de los puntos no sea
		//tan rápida que coincida el id de dos puntos
		try {
			Thread.sleep(5);
		} catch (InterruptedException e) {
		}
		if(viewRasterGCPs != null) 
			viewRasterGCPs.addPixelGeoPoint(gp, id);
		if(viewMapGCPs != null)
			viewMapGCPs.addMapGeoPoint(gp, id);
		if(viewRasterGCPsZoom != null)
			viewRasterGCPsZoom.addPixelGeoPoint(gp, id);
		if(viewMapGCPsZoom != null)
			viewMapGCPsZoom.addMapGeoPoint(gp, id);
		return id;
	}
	
	/**
	 * Elimina todos los puntos gráficos de las capas
	 *
	 */
	public void removeAllPoints() {
		if(viewRasterGCPs != null)
			viewRasterGCPs.removeAllPoints();
		if(viewMapGCPs != null)
			viewMapGCPs.removeAllPoints();
		if(viewRasterGCPsZoom != null)
			viewRasterGCPsZoom.removeAllPoints();
		if(viewMapGCPsZoom != null)
			viewMapGCPsZoom.removeAllPoints();
	}
	
	/**
	 * Asigna las coordenadas del mundo real y raster al punto indicado en el parámetro. 
	 * @param point Punto a asignar las nuevas coordenadas
	 * @param xMap Coordenada real en X
	 * @param yMap Coordenada real en Y
	 * @param xRaster Coordenada pixel en X
	 * @param yRaster Coordenada pixel en Y
	 */
	public void setCoordinates(int point, double xMap, double yMap, double xRaster, double yRaster) {
		setXMapCoordinate(point, xMap);
		setYMapCoordinate(point, yMap);
		setXRasterCoordinate(point, xRaster);
		setYRasterCoordinate(point, yRaster);
		redrawPoints();
	}
	
	/**
	 * Asigna la coordenada del mundo real X
	 * @param point
	 * @param xMap
	 */
	public void setXMapCoordinate(int point, double xMap) {
		double oldMapY = viewRasterGCPs.getPoint(point).getGeoPoint().mapPoint.getY();
		viewRasterGCPs.getPoint(point).getGeoPoint().mapPoint.setLocation(xMap, oldMapY); 
	}
	
	/**
	 * Asigna la coordenada del mundo real Y
	 * @param point
	 * @param yMap
	 */
	public void setYMapCoordinate(int point, double yMap) {
		double oldMapX = viewRasterGCPs.getPoint(point).getGeoPoint().mapPoint.getX();
		viewRasterGCPs.getPoint(point).getGeoPoint().mapPoint.setLocation(oldMapX, yMap); 
	}
	
	/**
	 * Asigna la coordenada raster X
	 * @param point
	 * @param xRaster
	 */
	public void setXRasterCoordinate(int point, double xRaster) {
		double oldRasterY = viewRasterGCPs.getPoint(point).getGeoPoint().pixelPoint.getY();
		viewRasterGCPs.getPoint(point).getGeoPoint().pixelPoint.setLocation(xRaster, oldRasterY); 
	}
	
	/**
	 * Asigna la coordenada raster Y
	 * @param point
	 * @param xRaster
	 */
	public void setYRasterCoordinate(int point, double yRaster) {
		double oldRasterX = viewRasterGCPs.getPoint(point).getGeoPoint().pixelPoint.getX();
		viewRasterGCPs.getPoint(point).getGeoPoint().pixelPoint.setLocation(oldRasterX, yRaster);
	}
	
	/**
	 * Elimina un punto de la lista a partir de su posición
	 * @param position
	 */
	public void removePoint(int position) {
		if(viewRasterGCPs != null)
			viewRasterGCPs.removePixelGeoPoint(position);
		if(viewMapGCPs != null)
			viewMapGCPs.removePixelGeoPoint(position);
		if(viewRasterGCPsZoom != null)
			viewRasterGCPsZoom.removePixelGeoPoint(position);
		if(viewMapGCPsZoom != null)
			viewMapGCPsZoom.removePixelGeoPoint(position);
	}
	
	/**
	 * Informa de si la tool de selección de puntos está activa o no
	 * @return true si está activa y false si no lo está
	 */
	public boolean isSelectPointToolSelected() {
		if(viewRaster != null)
			if(viewRaster.getControl().getToolSelected() instanceof SelectPointTool)
				return true;
		return false;
	}
	
	/**
	 * Obtiene la lista de puntos
	 * @return ArrayList
	 */
	public ArrayList getPointList() {
		if(viewRasterGCPs != null)
			return viewRasterGCPs.getPointList();
		return null;
	}
	
	/**
	 * Obtiene un array con la lista de puntos 
	 * @return
	 */
	public GeoPointList getGeoPoints() {
		ArrayList pointList = getPointList();
		GeoPointList geoPointList = new GeoPointList();
		for (int i = 0; i < pointList.size(); i++)
			geoPointList.add(((GPGraphic) pointList.get(i)).getGeoPoint());
		return geoPointList;
	}
	
	/**
	 * Activa o desactiva la visualización de un punto.
	 * @param active true para activarlo y false para desactivarlo
	 * @param position Posición del punto
	 */
	public void setVisiblePoint(boolean visible, int position) {
		if(viewRasterGCPs != null)
			((GPGraphic)viewRasterGCPs.getPointList().get(position)).setDraw(visible);
		if(viewMapGCPs != null)
			((GPGraphic)viewMapGCPs.getPointList().get(position)).setDraw(visible);
		if(viewRasterGCPsZoom != null)
			((GPGraphic)viewRasterGCPsZoom.getPointList().get(position)).setDraw(visible);
		if(viewMapGCPsZoom != null)
			((GPGraphic)viewMapGCPsZoom.getPointList().get(position)).setDraw(visible);
	}
	
	/**
	 * Asigna la numeración a los puntos de control. Tanto a la tabla como
	 * a los GeoPuntos
	 * @throws NotInitializeException
	 */
	public void calcPointsNumeration(GCPTablePanel table) throws NotInitializeException {
		ArrayList pointList = getPointList();
		
		for (int i = 0; i < table.getRowCount(); i++) { 
			table.setValueAt(new Integer(i), i, 1);
			long id = (long)((Long)table.getValueAt(i, table.getColumnCount() - 1)).longValue();
			
			if(i < pointList.size()) {
				for (int j = 0; j < pointList.size(); j++) {
					GPGraphic gpg = ((GPGraphic)pointList.get(j));
					if(id == gpg.getId()) {
						((GPGraphic)pointList.get(j)).getGeoPoint().number = i;
						break;
					}
				}
			}
		}
		redrawPoints();
	}
	
	/**
	 * Crea una capa de puntos de control y la añade a la vista
	 * que se le indica por parámetro
	 * @param view
	 */
	public GCPsGraphicLayer createGCPPIxelLayer(BaseZoomView view, 
									ViewRasterRequestManager viewRasterRequestManager, 
									ViewRasterRequestManager zoomRasterRequestManager) {
		viewRasterGCPs = new GCPsGraphicLayer(GPGraphic.PIXEL, appMain);
		viewRasterGCPs.setCanvas(view.getCanvas());
		view.addGraphicLayer(viewRasterGCPs);
		viewRasterRequestManager.setGCPsGraphicLayer(viewRasterGCPs);
		
		viewRasterGCPsZoom = new GCPsGraphicLayer(GPGraphic.PIXEL, appMain);
		viewRasterGCPsZoom.setCanvas(zoomRaster.getCanvas());
		zoomRaster.addGraphicLayer(viewRasterGCPsZoom);
		zoomRasterRequestManager.setGCPsGraphicLayer(viewRasterGCPsZoom);
		
		return viewRasterGCPs;
	}
	
	/**
	 * Crea una capa de puntos de control y la añade a la vista
	 * que se le indica por parámetro
	 * @param view
	 */
	public GCPsGraphicLayer createGCPMapLayer(BaseZoomView view, 
									ViewMapRequestManager viewMapRequestManager, 
									ViewMapRequestManager zoomMapRequestManager) {
		viewMapGCPs = new GCPsGraphicLayer(GPGraphic.MAP, appMain);
		viewMapGCPs.setCanvas(view.getCanvas());
		view.addGraphicLayer(viewMapGCPs);
		viewMapRequestManager.setGCPsGraphicLayer(viewMapGCPs);
		
		viewMapGCPsZoom = new GCPsGraphicLayer(GPGraphic.MAP, appMain);
		viewMapGCPsZoom.setCanvas(zoomMap.getCanvas());
		zoomMap.addGraphicLayer(viewMapGCPsZoom);
		zoomMapRequestManager.setGCPsGraphicLayer(viewMapGCPsZoom);
		
		return viewMapGCPs;
	}
	
	/**
	 * Activa y desactiva los números de punto de las capas gráficas
	 * @param active
	 */
	public void setActiveNumberPoint(boolean active) {
		if(viewMapGCPs != null)
			viewMapGCPs.setShowNumber(active);
		if(viewRasterGCPs != null)
			viewRasterGCPs.setShowNumber(active);
		if(viewMapGCPsZoom != null)
			viewMapGCPsZoom.setShowNumber(active);
		if(viewRasterGCPsZoom != null)
			viewRasterGCPsZoom.setShowNumber(active);
	}
	
	/**
	 * Activa o desactiva la capa de puntos
	 * @param active
	 */
	public void setActiveLayerPoints(boolean active) {
		if(viewRasterGCPs != null) 
			viewRasterGCPs.setActive(active);
		if(viewMapGCPs != null)
			viewMapGCPs.setActive(active);
		if(viewRasterGCPsZoom != null)
			viewRasterGCPsZoom.setActive(active);
		if(viewMapGCPsZoom != null)
			viewMapGCPsZoom.setActive(active);
	}
	
	/**
	 * Obtiene un punto (GeoPoint) a partir de su número. Hay que tener en cuenta que el número de
	 * punto puede no coincidir con su posición en el array.
	 * @return GeoPoint
	 */
	public GeoPoint getPointByNumber(int number) {
		ArrayList pointList = getPointList();
		for (int i = 0; i < pointList.size(); i++) {
			GeoPoint p = ((GPGraphic)pointList.get(i)).getGeoPoint();
			if(p.number == number)
				return p;
		}
		return null;
	}
	
	/**
	 * Obtiene la posición de un punto a partir de su número. Hay que tener en cuenta que el número de
	 * punto puede no coincidir con su posición en el array.
	 * @return entero con la posición o -1 si no existe
	 */
	public int getPointPositionByNumber(int number) {
		ArrayList pointList = getPointList();
		for (int i = 0; i < pointList.size(); i++) {
			GeoPoint p = ((GPGraphic)pointList.get(i)).getGeoPoint();
			if(p.number == number)
				return i;
		}
		return -1;
	}
}
