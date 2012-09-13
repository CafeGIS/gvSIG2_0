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

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.georeferencing.ui.launcher.GeorefLauncherDialog;
import org.gvsig.georeferencing.ui.table.GCPTablePanel;
import org.gvsig.georeferencing.ui.zoom.InvalidRequestException;
import org.gvsig.georeferencing.ui.zoom.ViewEvent;
import org.gvsig.georeferencing.ui.zoom.ViewListener;
import org.gvsig.georeferencing.ui.zoom.ViewMapRequestManager;
import org.gvsig.georeferencing.ui.zoom.ViewRasterRequestManager;
import org.gvsig.georeferencing.ui.zoom.layers.GCPsGraphicLayer;
import org.gvsig.georeferencing.ui.zoom.layers.GPGraphic;
import org.gvsig.georeferencing.ui.zoom.layers.ZoomCursorGraphicLayer;
import org.gvsig.georeferencing.ui.zoom.tools.PanTool;
import org.gvsig.georeferencing.ui.zoom.tools.SelectPointTool;
import org.gvsig.georeferencing.ui.zoom.tools.ToolEvent;
import org.gvsig.georeferencing.ui.zoom.tools.ToolListener;
import org.gvsig.georeferencing.ui.zoom.tools.ZoomRectangleTool;
import org.gvsig.georeferencing.view.ViewDialog;
import org.gvsig.georeferencing.view.ZoomDialog;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener;
import org.gvsig.gui.beans.table.exceptions.NotInitializeException;
import org.gvsig.raster.datastruct.GeoPoint;
import org.gvsig.raster.util.RasterToolsUtil;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiFrame.MDIFrame;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.view.ProjectView;
import com.iver.cit.gvsig.project.documents.view.ProjectViewFactory;
import com.iver.cit.gvsig.project.documents.view.gui.IView;

/**
 * Clase principal para la georreferenciación. Se encarga de la inicialización
 * de la funcionalidad. Las acciones a realizar son:
 * <UL>
 * <LI>Lanzamiento del dialogo de inicialización.</LI>
 * <LI>Carga de la capa raster a georreferenciar.</LI>
 * <LI>Creación de las ventanas si no han sido creadas previamente.</LI>
 * <LI>Asignación de tamaños y posiciones a las ventanas.</LI>
 * </UL>
 * 26/12/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class Georeferencing implements ButtonsPanelListener, ToolListener, ViewListener {
	public static final int               UNDEFINED                      = -1;
	//Algoritmo
	public static final int               POLYNOMIAL                     = 0;
	public static final int               AFFINE                         = 1;

	//Tipo de georreferenciacion
	public static final int               WITH_MAP                       = 0;
	public static final int               WITHOUT_MAP                    = 1;

	public static int                     DEFAULT_DEGREE                 = 5;
	private static int                    ZOOM_RATIO                     = 5;

	private FLyrRasterSE                  lyrToGeoref                    = null;
	private String                        selectedView                   = null;

	private static final int              smallWindowsHeight             = 100;
	private static final double           smallWindowsWidthPercent       = 0.5;

	//Dialogos
	private GeorefLauncherDialog          initDialog                     = null;
	private ViewDialog                    viewRaster                     = null;
	private ViewDialog                    viewMap                        = null;
	private ZoomDialog                    zoomMap                        = null;
	private ZoomDialog                    zoomRaster                     = null;

	//Herramientas de selección
	private SelectPointTool               viewRasterSelectPointTool      = null;
	private SelectPointTool               viewMapSelectPointTool         = null;
	private SelectPointTool               zoomRasterSelectPointTool      = null;
	private SelectPointTool               zoomMapSelectPointTool         = null;

	private GCPTablePanel                 table                          = null;
	private ApplicationControlsListener   buttonsListener                = null;
	private GeorefOptions                 options                        = null;

	private LayersPointManager            layersPointManager             = null;
	private ViewsManager                  viewsManager                   = null;

	public void initialize() {
		getLayerPointManager();
		getViewsManager();
		launcher();
	}

	/**
	 * Obtiene el gestor para capas de puntos de la aplicación,
	 * @return LayersPointManager
	 */
	public LayersPointManager getLayerPointManager() {
		if(layersPointManager == null)
			layersPointManager = new LayersPointManager(this);
		return layersPointManager;
	}

	/**
	 * Obtiene el gestor para las vistas de la aplicación,
	 * @return ViewsManager
	 */
	public ViewsManager getViewsManager() {
		if(viewsManager == null)
			viewsManager = new ViewsManager();
		return viewsManager;
	}

	/**
	 * Obtiene la clase con las opciones de georreferenciación
	 * @return
	 */
	public GeorefOptions getOptions() {
		if(options == null)
			options = new GeorefOptions();
		return options;
	}

	/**
	 * Obtiene la lista de capas de la vista de gvSIG
	 * @return
	 */
	public FLayers getLayers() {
		MapControl mapControl = null;
		IWindow[] windowList = PluginServices.getMDIManager().getAllWindows();
		for (int i = 0; i < windowList.length; i++) {
			if(windowList[i] instanceof IView)
				mapControl = ((IView)windowList[i]).getMapControl();
		}
		if(mapControl != null)
			return mapControl.getMapContext().getLayers();
		return null;
	}

	/**
	 * Colocación inicial de las ventanas del espacio de trabajo. Carga la configuración
	 * de ventanas con cartografía de referencia.
	 */
	private void initWindowsWithoutMap() {
		MDIFrame p = (MDIFrame)PluginServices.getMainFrame();
		int totalWidth = p.getWidth();
		int wViews = totalWidth;
		int hViews = p.getHeight() - 178 - smallWindowsHeight;
		int posYViews = p.getHeight() - 138 - smallWindowsHeight;
		int smallWindowsWidth = (int)(totalWidth * 0.25);
		int tableWidth = (int)(totalWidth * 0.75);

		getOptions().setType(WITHOUT_MAP);

		//Si hay ventanas de georreferenciación abiertas las cerramos
		closeAllWindows();

		//Inicialización de la tabla
		table = new GCPTablePanel(0, posYViews, tableWidth, smallWindowsHeight, this);
		buttonsListener = new ApplicationControlsListener(table, this);
		table.initialize(buttonsListener);

		//Inicialización de la ventana con la vista raster
		viewRaster = new ViewDialog(0, 0, wViews, hViews, this);
		viewRaster.setViewListener(this);
		viewRaster.setMinxMaxyUL(false);
		viewRaster.setShowInfo(true);
		ViewRasterRequestManager viewRasterRequestManager = new ViewRasterRequestManager(viewRaster, lyrToGeoref);
		viewRaster.setExtensionRequest(viewRasterRequestManager);
		viewRasterSelectPointTool = new SelectPointTool(viewRaster.getCanvas(), this);

		//Inicialización de la ventana del zoom de la vista raster
		zoomRaster = new ZoomDialog(tableWidth, posYViews, smallWindowsWidth, smallWindowsHeight);
		zoomRaster.setMinxMaxyUL(false);
		ViewRasterRequestManager zoomRasterRequestManager = new ViewRasterRequestManager(zoomRaster, lyrToGeoref);
		zoomRaster.setExtensionRequest(zoomRasterRequestManager);
		zoomRasterSelectPointTool = new SelectPointTool(zoomRaster.getCanvas(), this);

		//Añadimos las ventanas a gvSIG
		PluginServices.getMDIManager().addWindow(viewRaster);
		PluginServices.getMDIManager().addWindow(table);
		PluginServices.getMDIManager().addWindow(zoomRaster);

		layersPointManager.setViews(viewRaster, null, zoomRaster, null);
		layersPointManager.setTools(viewRasterSelectPointTool, null, zoomRasterSelectPointTool, null);
		layersPointManager.registerPointToolListener();

		viewsManager.setViews(viewRaster, null, zoomRaster, null);
		viewsManager.setRequestsManager(viewRasterRequestManager, null, zoomRasterRequestManager, null);
		viewsManager.setTablePanel(table);

		try {
			viewRaster.setCursorSize(zoomRaster.getCanvasWidth() / ZOOM_RATIO, zoomRaster.getCanvasHeight() / ZOOM_RATIO);
			viewRaster.setCursorPosition(viewRaster.getCanvasWidth() / 2, viewRaster.getCanvasHeight() / 2);

			//Inicializamos la vista de raster
			viewRasterRequestManager.initRequest(lyrToGeoref.getFullEnvelope());

			//Inicializamos el zoom de raster
			Rectangle2D ext = viewRaster.getCursorAdjustedWorldCoordinates(zoomRaster.getCanvasWidth(), zoomRaster.getCanvasHeight());
			zoomRasterRequestManager.initRequest(lyrToGeoref.getFullEnvelope());
			zoomRasterRequestManager.request(ext);
			
			layersPointManager.createGCPPIxelLayer(viewRaster, viewRasterRequestManager, zoomRasterRequestManager);

		} catch (InvalidRequestException e1) {
			RasterToolsUtil.messageBoxError("error_setview_preview", this, e1);
			closeAllWindows();
		}catch (Exception e1) {
			RasterToolsUtil.messageBoxError("error_setview_preview", this, e1);
			closeAllWindows();
		}
	}

	/**
	 * Colocación inicial de las ventanas del espacio de trabajo. Carga la configuración
	 * de ventanas con cartografía de referencia.
	 */
	private void initWindowsWithMap() {
		MDIFrame p = (MDIFrame)PluginServices.getMainFrame();
		int wViews = p.getWidth() >> 1;
		int hViews = p.getHeight() - 178 - smallWindowsHeight;
		int posYViews = p.getHeight() - 138 - smallWindowsHeight;
		int smallWindowsWidth = (int)(p.getWidth() * (smallWindowsWidthPercent * 0.5));
		int tableWidth = (int)(p.getWidth() * (1 - smallWindowsWidthPercent));

		getOptions().setType(WITH_MAP);

		//Si hay ventanas de georreferenciación abiertas las cerramos
		closeAllWindows();

		MapControl mapControl = null;
		IWindow[] windowList = PluginServices.getMDIManager().getAllWindows();
		for (int i = 0; i < windowList.length; i++) {
			if(windowList[i] instanceof IView && windowList[i].getWindowInfo().getTitle().endsWith(": " + selectedView))
				mapControl = ((IView)windowList[i]).getMapControl();
		}

		if(mapControl == null) {
			RasterToolsUtil.messageBoxError("error_lookingfor_view", this);
			return;
		}

		//Inicializamos el control de tabla
		table = new GCPTablePanel(smallWindowsWidth, posYViews, tableWidth, smallWindowsHeight, this);
		buttonsListener = new ApplicationControlsListener(table, this);
		table.initialize(buttonsListener);

		//Inicialización de la ventana con la vista mapa
		viewMap = new ViewDialog(0, 0, wViews, hViews, this);
		viewMap.setViewListener(this);
		viewMap.setShowInfo(true);
		ViewMapRequestManager viewMapRequestManager = new ViewMapRequestManager(viewMap, mapControl);
		viewMap.setExtensionRequest(viewMapRequestManager);
		viewMapSelectPointTool = new SelectPointTool(viewMap.getCanvas(), this);

		//Inicialización de la ventana con la vista raster
		viewRaster = new ViewDialog(wViews, 0, wViews, hViews, this);
		viewRaster.setViewListener(this);
		viewRaster.setMinxMaxyUL(false);
		viewRaster.setShowInfo(true);
		ViewRasterRequestManager viewRasterRequestManager = new ViewRasterRequestManager(viewRaster, lyrToGeoref);
		viewRaster.setExtensionRequest(viewRasterRequestManager);
		viewRasterSelectPointTool = new SelectPointTool(viewRaster.getCanvas(), this);

		//Inicialización de la ventana del zoom de la vista raster
		zoomRaster = new ZoomDialog(smallWindowsWidth + tableWidth, posYViews, smallWindowsWidth, smallWindowsHeight);
		zoomRaster.setMinxMaxyUL(false);
		zoomRaster.setShowInfo(true);
		ViewRasterRequestManager zoomRasterRequestManager = new ViewRasterRequestManager(zoomRaster, lyrToGeoref);
		zoomRaster.setExtensionRequest(zoomRasterRequestManager);
		zoomRasterSelectPointTool = new SelectPointTool(zoomRaster.getCanvas(), this);

		//Inicialización de la ventana del zoom de la vista de referencia
		zoomMap = new ZoomDialog(0, posYViews, smallWindowsWidth, smallWindowsHeight);
		zoomMap.setShowInfo(true);
		ViewMapRequestManager zoomMapRequestManager = new ViewMapRequestManager(zoomMap, mapControl);
		zoomMap.setExtensionRequest(zoomMapRequestManager);
		zoomMapSelectPointTool = new SelectPointTool(zoomMap.getCanvas(), this);

		//Añadimos las ventanas a gvSIG
		PluginServices.getMDIManager().addWindow(viewRaster);
		PluginServices.getMDIManager().addWindow(viewMap);
		PluginServices.getMDIManager().addWindow(table);
		PluginServices.getMDIManager().addWindow(zoomMap);
		PluginServices.getMDIManager().addWindow(zoomRaster);

		layersPointManager.setViews(viewRaster, viewMap, zoomRaster, zoomMap);
		layersPointManager.setTools(viewRasterSelectPointTool, viewMapSelectPointTool, zoomRasterSelectPointTool, zoomMapSelectPointTool);
		layersPointManager.registerPointToolListener();

		viewsManager.setViews(viewRaster, viewMap, zoomRaster, zoomMap);
		viewsManager.setRequestsManager(viewRasterRequestManager, viewMapRequestManager, zoomRasterRequestManager, zoomMapRequestManager);
		viewsManager.setTablePanel(table);

		try {
			viewMap.setCursorSize(zoomMap.getCanvasWidth() / ZOOM_RATIO, zoomMap.getCanvasHeight() / ZOOM_RATIO);
			viewRaster.setCursorSize(zoomRaster.getCanvasWidth() / ZOOM_RATIO, zoomRaster.getCanvasHeight() / ZOOM_RATIO);
			viewMap.setCursorPosition(viewMap.getCanvasWidth() / 2, viewMap.getCanvasHeight() / 2);
			viewRaster.setCursorPosition(viewRaster.getCanvasWidth() / 2, viewRaster.getCanvasHeight() / 2);

			if(mapControl.getMapContext().getLayers().getFullEnvelope() == null)
				RasterToolsUtil.messageBoxError("error_setview_preview", this);

			//Inicializamos la vista de mapa
			viewMapRequestManager.initRequest(mapControl.getMapContext().getLayers().getFullEnvelope());

			//Inicializamos la vista de raster
			viewRasterRequestManager.initRequest(lyrToGeoref.getFullEnvelope());

			//Inicializamos el zoom de mapa
			Rectangle2D ext = viewMap.getCursorAdjustedWorldCoordinates(zoomMap.getCanvasWidth(), zoomMap.getCanvasHeight());
			zoomMapRequestManager.initRequest(ext);
			
			//Inicializamos el zoom de raster
			ext = viewRaster.getCursorAdjustedWorldCoordinates(zoomRaster.getCanvasWidth(), zoomRaster.getCanvasHeight());
			zoomRasterRequestManager.initRequest(lyrToGeoref.getFullEnvelope());
			zoomRasterRequestManager.request(ext);

			layersPointManager.createGCPPIxelLayer(viewRaster, viewRasterRequestManager, zoomRasterRequestManager);
			layersPointManager.createGCPMapLayer(viewMap, viewMapRequestManager, zoomMapRequestManager);

		} catch (InvalidRequestException e1) {
			RasterToolsUtil.messageBoxError("error_setview_preview", this, e1);
			closeAllWindows();
		}catch (Exception e1) {
			RasterToolsUtil.messageBoxError("error_setview_preview", this, e1);
			closeAllWindows();
		}
	}

	/**
	 * Cierra todas las ventanas de georreferenciación
	 */
	public void closeAllWindows() {
		if(zoomMap != null)
			PluginServices.getMDIManager().closeWindow(zoomMap);
		if(zoomRaster != null)
			PluginServices.getMDIManager().closeWindow(zoomRaster);
		if(viewRaster != null)
			PluginServices.getMDIManager().closeWindow(viewRaster);
		if(viewMap != null)
			PluginServices.getMDIManager().closeWindow(viewMap);
		if(table != null)
			PluginServices.getMDIManager().closeWindow(table);
	}

	/**
	 * Añade una capa en la vista del mapa para previsualizar el resultado
	 * @param lyr
	 */
	public void addTestRasterLayer(FLyrRasterSE lyr) {
		viewsManager.addTestRasterLayer(lyr,
				getOptions().getAlgorithm(),
				getOptions().getInterpolationMethod(),
				getOptions().getDegree(),
				getOptions().getOutFile());
	}

	/**
	 * Elimina la capa de test de la vista de mapa
	 * @throws InvalidRequestException
	 */
	public void removeTestRasterLayer() {
		viewsManager.removeTestRasterLayer();
	}

	/**
	 * Obtiene la última capa procesada con los puntos de  control
	 * @return FLyrRaterSE
	 */
	public FLyrRasterSE getLastTestLayer() {
		return viewsManager.getLastTestLayer();
	}

	/**
	 * Lanzador del cuadro de inicialización de propiedades de georreferenciación.
	 */
	private void launcher() {
		Project p = ((ProjectExtension) PluginServices.getExtension(ProjectExtension.class)).getProject();
		ArrayList docs = p.getDocumentsByType(ProjectViewFactory.registerName);
		String[] viewList = new String[docs.size()];
		for (int i = 0; i < docs.size(); i++)
			viewList[i] = (((ProjectView)docs.get(i)).getName());
		initDialog = new GeorefLauncherDialog(viewList, DEFAULT_DEGREE, this);
		PluginServices.getMDIManager().addWindow(initDialog);
	}

	/**
	 * Captura de eventos de los botones Aceptar y Cancelar del cuadro
	 * inicial con las propiedades de georreferenciación.
	 */
	public void actionButtonPressed(ButtonsPanelEvent e) {

		//-----------------
		// Botón de Aceptar
		if (e.getButton() == ButtonsPanel.BUTTON_ACCEPT) {
			if(initDialog != null) {
				selectedView = initDialog.getSelectedView();

				if(initDialog.getType() == Georeferencing.WITH_MAP) {
					boolean findView = false;
					IWindow[] windowList = PluginServices.getMDIManager().getAllWindows();
					for (int i = 0; i < windowList.length; i++)
						if(windowList[i] instanceof IView && windowList[i].getWindowInfo().getTitle().endsWith(": " + selectedView))
							findView = true;

					if(!findView) {
						RasterToolsUtil.messageBoxError("no_view_found", this);
						return;
					}
				}

				lyrToGeoref = initDialog.getLayer();
				getOptions().setAlgorithm(initDialog.getAlgorithm());
				getOptions().setDegree(initDialog.getDegree());
				getOptions().setOutFile(initDialog.getOutFile());
				getOptions().setInterpolationMethod(initDialog.getInterpolationMethod());
				getOptions().setXCellSize(initDialog.getXCellSizeValue());
				getOptions().setYCellSize(initDialog.getYCellSizeValue());
				//Inicializamos la matriz de transformación en la imagen que vamos a georreferenciar para
				//que nos de coordenadas pixel
				if(lyrToGeoref == null)
					RasterToolsUtil.messageBoxError("layer_not_loaded", this);
				else {
					lyrToGeoref.setAffineTransform(new AffineTransform(1, 0, 0, 1, 0, 0));
					PluginServices.getMDIManager().closeWindow(initDialog);
					if(initDialog.getType() == Georeferencing.WITH_MAP)
						initWindowsWithMap();
					if(initDialog.getType() == Georeferencing.WITHOUT_MAP)
						initWindowsWithoutMap();
				}
			}
		}

		//----------------
		// Botón de Cerrar
		if (e.getButton() == ButtonsPanel.BUTTON_CANCEL) {
			if(initDialog != null)
				PluginServices.getMDIManager().closeWindow(initDialog);
		}
	}

	/**
	 * Controla los eventos de finalización de la capa con el
	 * cursor gráfico que controla el área de la miniimagen asociada y
	 * la terminación de la operación de mover punto
	 */
public void endAction(ToolEvent ev) {
		
		//-----------------
		//Operación de zoom
		if(ev.getSource() instanceof ZoomCursorGraphicLayer) {
			try {	
				Rectangle2D ext = null;
				if(viewRaster != null) {
					ext = viewRaster.getCursorAdjustedWorldCoordinates(zoomRaster.getCanvasWidth(), zoomRaster.getCanvasHeight());
					viewsManager.getZoomRasterRequestManager().request(ext);
				}
				if(viewMap != null) {
					ext = viewMap.getCursorAdjustedWorldCoordinates(zoomMap.getCanvasWidth(), zoomMap.getCanvasHeight());
					viewsManager.getZoomMapRequestManager().request(ext);
				}
			} catch (InvalidRequestException e1) {
				RasterToolsUtil.messageBoxError("error_setview_preview", this);
			}
		}
		
		//-----------
		//Mover punto
		if(ev.getSource() instanceof GCPsGraphicLayer) {
			buttonsListener.setEnableTableEvent(false);
			//Se redibujan, se obtienen las coordenadas del punto y se actualiza la tabla
			if(layersPointManager != null)
				layersPointManager.redrawPoints();
			GPGraphic gPoint = ((GCPsGraphicLayer)ev.getSource()).getLastPoint();
			Point2D pRaster = gPoint.getGeoPoint().pixelPoint;
			Point2D pMap = gPoint.getGeoPoint().mapPoint;
			try {
				table.updatePoint(	new Double(pMap.getX()), 
									new Double(pMap.getY()), 
									new Double(pRaster.getX()), 
									new Double(pRaster.getY()), 
									gPoint.getGeoPoint().number);
				table.getTable().getTable().getJTable().setRowSelectionInterval(gPoint.getGeoPoint().number, gPoint.getGeoPoint().number);
			} catch (NotInitializeException e) {
				RasterToolsUtil.messageBoxError("no_rows_selected", table, e);
			} catch (ArrayIndexOutOfBoundsException e) {
				RasterToolsUtil.messageBoxError("no_rows_selected", table, e);
			}
			if(getOptions().isCenterView())
				viewsManager.centerToPoint(gPoint.getGeoPoint().number);
			buttonsListener.setEnableTableEvent(true);
		}
		
		//------------------
		//Selección de punto
		if(ev.getSource() instanceof SelectPointTool) {
			try {
				//Se obtiene el punto seleccionado, se actualiza la tabla y se redibujan
				int nPoint = table.getTable().getSelectedRow();
				GeoPoint gp = layersPointManager.getPointByNumber(nPoint);
				
				if(ev.getSource() == viewRasterSelectPointTool || ev.getSource() == zoomRasterSelectPointTool) {
					Point2D[] p = (Point2D[])((SelectPointTool)ev.getSource()).getResult();
					table.updateRasterPoint(new Double(p[1].getX()), new Double(p[1].getY()), nPoint);
					if(gp != null) {
						gp.pixelPoint = p[1];
						layersPointManager.redrawPoints();
					}
				}
				if(ev.getSource() == viewMapSelectPointTool || ev.getSource() == zoomMapSelectPointTool) {
					Point2D[] p = (Point2D[])((SelectPointTool)ev.getSource()).getResult();
					table.updateMapPoint(new Double(p[1].getX()), new Double(p[1].getY()), nPoint);
					if(gp != null) {
						gp.mapPoint = p[1];
						layersPointManager.redrawPoints();
					}
				}
				if(getOptions().isCenterView())
					viewsManager.centerToPoint(nPoint);
			} catch (NotInitializeException e) {
				RasterToolsUtil.messageBoxError("no_rows_selected", table, e);
			} catch (ArrayIndexOutOfBoundsException e) {
				RasterToolsUtil.messageBoxError("no_rows_selected", table, e);
			}
		}
	}

	/**
	 *  <P>
	 *  Controla las prioridades de eventos sobre la vista en las herramientas y capas de selección.
	 *  Esto es necesario para que no se mezclen eventos entre capas.
	 *  <P></P>
	 *  Prioridades:
	 *  </P>
	 *  <UL>
	 *  <LI>Capa de ZoomCursorGraphicLayer</LI>
	 *  <LI>Capa de GCPsGraphicLayer</LI>
	 *  <LI>Tool de selección de zoom rectangulo</LI>
	 *  <LI>Tool de selección de puntos</LI>
	 *  </UL>
	 */
	public void onTool(ToolEvent ev) {

		//---------------------------
		//Operación de cursor de zoom
		if(ev.getSource() instanceof ZoomCursorGraphicLayer) {
			layersPointManager.setActiveLayerPoints(false);
			viewsManager.sleepActiveTools();
			return;
		}

		//-----------
		//Mover punto
		if(ev.getSource() instanceof GCPsGraphicLayer) {
			layersPointManager.setActiveLayerZoomCursor(false);
			viewsManager.sleepActiveTools();
			return;
		}

		//------------------
		//Selección de punto
		if(ev.getSource() instanceof SelectPointTool) {
			if(viewMap != null) {
				viewMap.getControl().selectTool(ZoomRectangleTool.class, false);
				viewMap.getControl().selectTool(PanTool.class, false);
				viewMap.getControl().getBSelectZoomArea().setSelected(false);
				viewMap.getControl().getBMove().setSelected(false);
			}
			if(viewRaster != null) {
				viewRaster.getControl().selectTool(ZoomRectangleTool.class, false);
				viewRaster.getControl().selectTool(PanTool.class, false);
				viewRaster.getControl().getBSelectZoomArea().setSelected(false);
				viewRaster.getControl().getBMove().setSelected(false);
			}
		}

		//-------------------------
		//Selección de zoom ventana
		if(ev.getSource() instanceof ZoomRectangleTool) {
			if(viewMap != null) {
				viewMap.getControl().selectTool(SelectPointTool.class, false);
				viewMap.getControl().selectTool(PanTool.class, false);
				viewMap.getControl().getBMove().setSelected(false);
			}
			if(viewRaster != null) {
				viewRaster.getControl().selectTool(SelectPointTool.class, false);
				viewRaster.getControl().selectTool(PanTool.class, false);
				viewRaster.getControl().getBMove().setSelected(false);
			}
			table.getToolSelectPointButton().setSelected(false);
		}

		//---------------------------
		//Selección de desplazamiento
		if(ev.getSource() instanceof PanTool) {
			if(viewMap != null) {
				viewMap.getControl().selectTool(SelectPointTool.class, false);
				viewMap.getControl().selectTool(ZoomRectangleTool.class, false);
				viewMap.getControl().getBSelectZoomArea().setSelected(false);
			}
			if(viewRaster != null) {
				viewRaster.getControl().selectTool(SelectPointTool.class, false);
				viewRaster.getControl().selectTool(ZoomRectangleTool.class, false);
				viewRaster.getControl().getBSelectZoomArea().setSelected(false);
			}
			table.getToolSelectPointButton().setSelected(false);
		}
	}

	/**
	 *
	 */
	public void offTool(ToolEvent ev) {

		//-----------------
		//Operación de zoom
		if(ev.getSource() instanceof ZoomCursorGraphicLayer) {
			layersPointManager.setActiveLayerPoints(true);
			viewsManager.awakeActiveTools();
			return;
		}

		//-----------
		//Mover punto
		if(ev.getSource() instanceof GCPsGraphicLayer) {
			layersPointManager.setActiveLayerZoomCursor(true);
			viewsManager.awakeActiveTools();
			return;
		}

		//Selección de punto
		/*if(ev.getSource() instanceof SelectPointTool) {
		}
		//Selección de zoom ventana
		if(ev.getSource() instanceof ZoomRectangleTool) {
		}*/
	}

	//*******************************************************
	//Eventos sobre las vistas


	public void addingTool(ViewEvent ev) {
	}

	public void endDraw(ViewEvent ev) {
	}

	public void startDraw(ViewEvent ev) {
	}

	/**
	 * Cuando cambia el zoom de alguna vista hay que recalcular la posición
	 * del cursor de zoom
	 */
	public void zoomViewChanged(ViewEvent ev) {
		Rectangle2D ext = null;
		try {
			if(viewRaster != null && zoomRaster != null && viewsManager.getZoomRasterRequestManager() != null) {
				ext = viewRaster.getCursorAdjustedWorldCoordinates(zoomRaster.getCanvasWidth(), zoomRaster.getCanvasHeight());
				viewsManager.getZoomRasterRequestManager().request(ext);
			}
			if(viewMap != null && zoomMap != null && viewsManager.getZoomMapRequestManager() != null) {
				ext = viewMap.getCursorAdjustedWorldCoordinates(zoomMap.getCanvasWidth(), zoomMap.getCanvasHeight());
				viewsManager.getZoomMapRequestManager().request(ext);
			}
		}catch(InvalidRequestException ex) {
			RasterToolsUtil.messageBoxError("error_set_view", table, ex);
		}
	}

	/**
	 * Obtiene la capa a georreferenciar
	 * @return FLyrRasterSE
	 */
	public FLyrRasterSE getLayer() {
		return lyrToGeoref;
	}

}
