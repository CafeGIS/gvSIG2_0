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

import java.awt.geom.AffineTransform;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.datainput.DataInputContainer;
import org.gvsig.raster.util.Historical;
import org.gvsig.rastertools.geolocation.listener.GeoLocationPanelListener;
/**
 * Clase base para los paneles que tienen la geolocalización. Tiene los métodos necesarios
 * para que el listener que gestiona sus acciones sea común.
 * 
 * 13/12/2007
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public abstract class GeolocationBaseClassPanel extends JPanel {
	private static final long serialVersionUID = 1055900686557565837L;
	private FLyrRasterSE               lyr          = null;
	private boolean                    geolocModify = false;
	private MapControl                 mapCtrl      = null;
	private ViewPort                   vp           = null;
	protected GeoLocationPanelListener listener     = null;

	/**
	 * Número de decimales a mostrar
	 */
	protected int                      tailValue    = 2;
	
	/**
	 * Obtiene la capa raster asociada
	 * @return
	 */
	public FLyrRasterSE getLayer() {
		return this.lyr;
	}
	
	/**
	 * Asigna la capa raster asociada
	 * @return
	 */
	public void setLayer(FLyrRasterSE lyr) {
		this.lyr = lyr;
	}
	
	/**
	 * Obtiene el viewport de la vista en el momento de lanzar el interfaz
	 * @return
	 */
	public ViewPort getViewPort() {
		return this.vp;
	}
	
	/**
	 * Asigna el viewport de la vista en el momento de lanzar el interfaz
	 * @return
	 */
	public void setViewPort(ViewPort vp) {
		this.vp = vp;
	}
	
	/**
	 * Obtiene el MapControl
	 * @return MapControl
	 */
	public MapControl getMapCtrl() {
		return mapCtrl;
	}
	
	/**
	 * Asigna el MapControl
	 * @param mapCtrl
	 */
	public void setMapCtrl(MapControl mapCtrl) {
		this.mapCtrl = mapCtrl;
	}
	
	/**
	 * Carga los parámetros en el dialogo a partir de la capa
	 * @param lyr Capa raster
	 */
	public abstract void loadTransform(AffineTransform at);
	
	/**
	 * Activa o desactiva los botones de transformación anterior y siguiente dependiendo
	 * del estado de la lista de transformaciones.
	 * @return
	 */
	public void activeButtons() {}
	
	/**
	 * Asigna el flag que dice si se ha modificado la georreferenciación
	 * y aún no se ha salvado
	 * @return true si se ha modificado y false si no se ha hecho
	 */
	public void setModify(boolean modif) {
		geolocModify = modif;
	}
	
	/**
	 * Obtiene el flag que dice si se ha modificado la georreferenciación
	 * y aún no se ha salvado
	 * @return true si se ha modificado y false si no se ha hecho
	 */
	public boolean getModify() {
		return geolocModify;
	}
	
	/**
	 * Obtiene el botón de aplicar
	 * @return JButton
	 */
	public JButton getApplyButton() {
		return null;
	}
	
	/**
	 * Obtiene el botón de cancelar
	 * @return JButton
	 */
	public JButton getCancelButton() {
		return null;
	}

	/**
	 * Obtiene el botón de atrás
	 * @return JButton
	 */
	public JButton getBackButton() {
		return null;
	}

	/**
	 * Obtiene el botón de ir a la primera transformación
	 * @return JButton
	 */
	public JButton getFirstButton() {
		return null;
	}

	/**
		 * Obtiene el botón de salvar
	 * @return JButton
	 */
	public JButton getSaveButton() {
		return null;
	}
	
	/**
		 * Obtiene el botón de reset
	 * @return JButton
	 */
	public JButton getResetButton() {
		return null;
	}
	
	/**
	 * Obtiene el botón de siguiente transformación
	 * @return JButton
	 */
	public JButton getNextButton() {
		return null;
	}
	
	/**
	 * Obtiene el botón de carga de georreferenciación desde tfw
	 * @return JButton
	 */
	public JButton getTfwLoad() {
		return null;
	}

	/**
	 * Obtiene el botón de centrado de raster en la vista
	 * @return JButton
	 */
	public JButton getCenterToView() {
		return null;
	}
	
	/**
	 * Obtiene el botón de recuperación del foco de la tool
	 * @return JButton
	 */
	public JButton getFocus() {
		return null;
	}
	
	/**
	 * Obtiene el tamaño de pixel en X
	 * @return
	 */
	public abstract DataInputContainer getPsx();

	/**
	 * Obtiene el tamaño de pixel en Y
	 * @return
	 */
	public abstract DataInputContainer getPsy();

	/**
	 * Obtiene la rotación en X
	 * @return
	 */
	public abstract DataInputContainer getRotx();

	/**
	 * Obtiene la rotación en Y
	 * @return
	 */
	public abstract DataInputContainer getRoty();

	/**
	 * Obtiene la X de la coordenada superior izquierda
	 * @return
	 */
	public abstract DataInputContainer getUlx();

	/**
	 * Obtiene la Y de la coordenada superior izquierda
	 * @return
	 */
	public abstract DataInputContainer getUly();
		
	/**
	 * Obtiene el historico de transformaciones
	 * @return Historical
	 */
	public Historical getHistorical() {
		return lyr.getAffineTransformHistorical();
	}
}
