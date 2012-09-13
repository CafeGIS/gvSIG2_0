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
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.dataset.serializer.RmfSerializerException;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.rastertools.geolocation.behavior.ITransformIO;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.IWindowListener;
import com.iver.andami.ui.mdiManager.WindowInfo;

/**
 * Dialogo de geolocalización de raster. Este es el que se usa para georreferenciar cuando 
 * se abre una imagen que no tiene georreferenciación asociada, si el usuario así lo solicita.
 * 
 * @version 12/12/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class GeoLocationOpeningRasterDialog extends JPanel implements IWindow, IWindowListener, ITransformIO {
	private static final long              serialVersionUID = 7362459094802955247L;
	private GeoLocationOpeningRasterPanel  geolocationPanel = null;

	private int                            widthWindow      = 260;
	private int                            heightWindow     = 165;

	/**
	 * Constructor
	 */
	public GeoLocationOpeningRasterDialog() {
		BorderLayout bl = new BorderLayout(5, 5);
		this.setLayout(bl);
		
		this.add(getGeoLocationPanel());
	}
	
	/**
	 * Constructor. Asigna la capa raster.
	 *
	 */
	public GeoLocationOpeningRasterDialog(FLyrRasterSE lyr) {
		BorderLayout bl = new BorderLayout(5, 5);
		this.setLayout(bl);

		this.add(getGeoLocationPanel());
		getGeoLocationPanel().setParams(lyr);
	}
		
	/**
	 * Acciones de inicialización
	 *
	 */
	public void init(MapControl mapCtrl) {
		geolocationPanel.setMapCtrl(mapCtrl);
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
	public GeoLocationOpeningRasterPanel getGeoLocationPanel(){
		if (geolocationPanel == null) 
			geolocationPanel = new GeoLocationOpeningRasterPanel(this);
		
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
		WindowInfo m_viewinfo=new WindowInfo(WindowInfo.MODALDIALOG);
		m_viewinfo.setHeight(heightWindow);
		m_viewinfo.setWidth(widthWindow);
		/*m_viewinfo.setX(posWindowX);
		m_viewinfo.setY(posWindowY);*/
		return m_viewinfo;
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
	}

	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}
}
