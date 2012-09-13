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
package org.gvsig.rastertools.geolocation.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.dataset.serializer.RmfSerializerException;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.rastertools.geolocation.behavior.ITransformIO;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.IWindowListener;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;

/**
 * Dialogo de geolocalización de raster.
 * 
 * @version 30/07/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class GeoLocationDialog extends JPanel implements IWindow, IWindowListener, ITransformIO {
	private static final long serialVersionUID = 7362459094802955247L;
	private GeoLocationPanel  geolocationPanel = null;

	/**
	 * Posición de la ventana en X y en Y
	 */
	private int               posWindowX       = 0;
	private int               posWindowY       = 0;

	private int               widthWindow      = 260;
	private int               heightWindow     = 155;

	private String            lastTool         = null;
	private BaseView          view             = null;

	/**
	 * Constructor
	 */
	public GeoLocationDialog() {
		BorderLayout bl = new BorderLayout(5, 5);
		this.setLayout(bl);
		
		this.add(getGeoLocationPanel());
	}
	
	/**
	 * Constructor. Asigna la capa raster.
	 *
	 */
	public GeoLocationDialog(FLyrRasterSE lyr, ViewPort vp, BaseView view) {
		this.view = view;
		BorderLayout bl = new BorderLayout(5, 5);
		this.setLayout(bl);

		this.add(getGeoLocationPanel());
		getGeoLocationPanel().setParams(lyr, vp);
	}
	
	/**
	 * Obtiene la vista asociada a este dialogo
	 * @return BaseView
	 */
	public BaseView getAssociateView() {
		return view;
	}
	
	/**
	 * Asigna la posición de la ventana
	 * @param x Posición en X
	 * @param y Posición en Y
	 */
	public void setPosition(int x, int y) {
		this.posWindowX = x;
		this.posWindowY = y;
	}
	
	/**
	 * Obtiene el tamaño de la ventana
	 * @return
	 */
	public Dimension getSizeWindow() {
		return new Dimension(widthWindow, heightWindow);
	}
	
	/**
	 * Referencia la capa que esté seleccionada
	 *
	 */
	private void loadLayer(ViewPort vp) {
		if(getGeoLocationPanel().getLayer() != null)
			return;
		//Este código es para poder lanzar la funcionalidad desde la barra de herramientas y no desde el TOC
		FLayers flyrs = getGeoLocationPanel().getMapCtrl().getMapContext().getLayers();
		FLayer[] actives = flyrs.getActives();
		for (int i = 0; i < actives.length; i++) {
			if(actives[i] instanceof FLyrRasterSE) {
				getGeoLocationPanel().setParams(((FLyrRasterSE)actives[i]), vp);
				break;
			}
		}
	}
	
	/**
	 * Acciones de inicialización
	 *
	 */
	public void init(MapControl mapCtrl) {
		geolocationPanel.setMapCtrl(mapCtrl);
		lastTool = mapCtrl.getCurrentTool();
		loadLayer(mapCtrl.getViewPort());
		FLyrRasterSE lyr = getGeoLocationPanel().getLayer();
		if(lyr != null) {
			lyr.getAffineTransformHistorical().clear();
			lyr.getAffineTransformHistorical().add(lyr.getAffineTransform());
			loadTransform(lyr.getAffineTransform());
		}
		activeButtons();
	}
	
	/**
	 * Carga los parámetros en el dialogo a partir de la capa
	 * @param lyr Capa raster
	 */
	public void loadTransform(AffineTransform at) {
		geolocationPanel.loadTransform(at);
	}
	
	/**
	 * Activa o desactiva los botones de transformación anterior y siguiente dependiendo
	 * del estado de la lista de transformaciones.
	 * @return
	 */
	public void applyTransformation() {
		geolocationPanel.setModify(true);
		geolocationPanel.activeButtons();	
	}
	/**
	 * Obtiene el panel con el histograma
	 * @return HistogramPanel
	 */
	public GeoLocationPanel getGeoLocationPanel(){
		if (geolocationPanel == null) 
			geolocationPanel = new GeoLocationPanel(this);
		
		return geolocationPanel;
	}

	/**
	 * Activa o desactiva los botones de transformación anterior y siguiente dependiendo
	 * del estado de la lista de transformaciones.
	 * @return
	 */
	public void activeButtons() {
		getGeoLocationPanel().activeButtons();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.ui.mdiManager.IWindow#getWindowInfo()
	 */
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo=new WindowInfo(WindowInfo.MODELESSDIALOG | WindowInfo.RESIZABLE);
		m_viewinfo.setHeight(heightWindow);
		m_viewinfo.setWidth(widthWindow);
		m_viewinfo.setX(posWindowX);
		m_viewinfo.setY(posWindowY);
		return m_viewinfo;
	}

	/**
	 * Asigna el último tool seleccionado antes de abrir el diálogo para 
	 * restaurarlo cuando se cierre este.
	 * @param tool
	 */
	public void setLastTool(String tool) {
		this.lastTool = tool;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.ui.mdiManager.IWindowListener#windowActivated()
	 */
	public void windowActivated() {
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.ui.mdiManager.IWindowListener#windowClosed()
	 */
	public void windowClosed() {
		//Se consulta si se desean salvar los cambios
		if(geolocationPanel.getModify()) {
			if(RasterToolsUtil.messageBoxYesOrNot(PluginServices.getText(this,"aviso_salir_salvando"), geolocationPanel)) {
				try {
					geolocationPanel.getLayer().saveGeoToRmf();
				} catch (RmfSerializerException e1) {
					RasterToolsUtil.messageBoxError(PluginServices.getText(this,"error_salvando_rmf"), geolocationPanel, e1);
				}
			}
			geolocationPanel.setModify(false);
		}
		
		//Restauramos la tool que había antes de de activar la actual
		if(lastTool != null)
			getGeoLocationPanel().getMapCtrl().setTool(lastTool);
		
		//TODO: Si queremos usar la funcionalidad de geolocalización desde la barra de herramientas deberemos 
		//, al cerrar la ventana, asignar la capa raster de GeoRasterBehevior a null ya que sino se aplicarán las
		//transformaciones sobre la última capa que se abrió desde el menú del TOC
	}

	public Object getWindowProfile() {
		return WindowInfo.PROPERTIES_PROFILE;
	}
}
