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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.datainput.DataInputContainer;
import org.gvsig.raster.util.MathUtils;
import org.gvsig.rastertools.geolocation.listener.GeoLocationPanelListener;

import com.iver.andami.PluginServices;

/**
 * Panel de geolocalización. Este muestra los parámetros de la matriz de transformación
 * que está aplicandose en esos momentos al raster. 
 * 
 * @version 12/12/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class GeoLocationOpeningRasterTransfPanel extends GeolocationBaseClassPanel {
	private static final long         serialVersionUID = -7797379892312214949L;
	private DataInputContainer	       ulx = null;
	private DataInputContainer	       uly = null;
	private DataInputContainer	       psx = null;
	private DataInputContainer	       psy = null;
	private DataInputContainer	       rotx = null;
	private DataInputContainer	       roty = null;
	
		
	private JPanel			           coordsPanel = null;
	private JPanel			           paramsPanel = null;
	
	/**
     * Número de decimales a mostrar
     */
    private int                        tailValue = 2;
	
	/**
	 * Constructor
	 */
	public GeoLocationOpeningRasterTransfPanel(GeoLocationPanelListener list) {
		this.listener = list;
		GridBagLayout gl = new GridBagLayout();
		this.setLayout(gl);
		setBorder(javax.swing.BorderFactory.createTitledBorder(null, PluginServices.getText(this, "geolocation"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
		
		ulx = new DataInputContainer();
		ulx.setLabelText(PluginServices.getText(this,"ux"));
		ulx.addValueChangedListener(listener);
		ulx.addKeyListener(listener);
		
		uly = new DataInputContainer();
		uly.setLabelText(PluginServices.getText(this,"uy"));
		uly.addValueChangedListener(listener);
		uly.addKeyListener(listener);
		
		psx = new DataInputContainer();
		psx.setLabelText(PluginServices.getText(this,"px"));
		psx.addValueChangedListener(listener);
		psx.addKeyListener(listener);
		
		psy = new DataInputContainer();
		psy.setLabelText(PluginServices.getText(this,"py"));
		psy.addValueChangedListener(listener);
		psy.addKeyListener(listener);
		
		rotx = new DataInputContainer();
		rotx.setLabelText(PluginServices.getText(this,"rx"));
		rotx.addValueChangedListener(listener);
		rotx.addKeyListener(listener);
		
		roty = new DataInputContainer();
		roty.setLabelText(PluginServices.getText(this,"ry"));
		roty.addValueChangedListener(listener);
		roty.addKeyListener(listener);
				
		coordsPanel = new JPanel();
		GridLayout l = new GridLayout(2, 1);
		l.setVgap(2);
		coordsPanel.setLayout(l);
		
		paramsPanel = new JPanel();
		GridLayout l1 = new GridLayout(2, 2);
		l1.setVgap(2);
		paramsPanel.setLayout(l1);

		init();
	}
	
	private void init() {
		coordsPanel.add(ulx);
		coordsPanel.add(uly);
		paramsPanel.add(psx);
		paramsPanel.add(psy);
		paramsPanel.add(rotx);
		paramsPanel.add(roty);
				
		GridBagConstraints gbc = new GridBagConstraints();
				
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new java.awt.Insets(1, 1, 1, 1);
		this.add(coordsPanel, gbc);
		
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		this.add(paramsPanel, gbc);
		
	}
	
	/**
	 * Activa o desactiva los botones de transformación anterior y siguiente dependiendo
	 * del estado de la lista de transformaciones.
	 * @return
	 */
	public void activeButtons() {

	}
	
	/**
	 * Asigna la capa raster del raster seleccionado en el TOC en base 
	 * al cual se asigna la georreferenciación al dialogo.
	 * @param lyr
	 */
	public void setParams(FLyrRasterSE lyr, ViewPort vp) {
		setLayer(lyr);
		setViewPort(vp);
		loadTransform(lyr.getAffineTransform());
	}

	/**
	 * Carga los parámetros en el dialogo a partir de la capa
	 * @param lyr Capa raster
	 */
	public void loadTransform(AffineTransform at) {
		listener.setEnableValueChangeEvent(false);
		setUlx(String.valueOf(MathUtils.format(at.getTranslateX(), tailValue)));
		setUly(String.valueOf(MathUtils.format(at.getTranslateY(), tailValue)));
		setPsx(String.valueOf(MathUtils.format(at.getScaleX(), tailValue)));
		setPsy(String.valueOf(MathUtils.format(at.getScaleY(), tailValue)));
		setRotx(String.valueOf(MathUtils.format(at.getShearX(), tailValue)));
		setRoty(String.valueOf(MathUtils.format(at.getShearY(), tailValue)));
		listener.setEnableValueChangeEvent(true);
	}
	
	/**
	 * Asigna el tamaño de pixel en X
	 * @param psx
	 */
	public void setPsx(String psx) {
		this.psx.setValue(psx);
	}

	/**
	 * Asigna el tamaño de pixel en Y
	 * @param psy
	 */
	public void setPsy(String psy) {
		this.psy.setValue(psy);
	}

	/**
	 * Asigna la rotación en X
	 * @param rotx
	 */
	public void setRotx(String rotx) {
		this.rotx.setValue(rotx);
	}

	/**
	 * Asigna la rotación en Y
	 * @param roty
	 */
	public void setRoty(String roty) {
		this.roty.setValue(roty);
	}

	/**
	 * Asigna la coordenada superior izquierda
	 * @param ulx 
	 */
	public void setUlx(String ulx) {
		this.ulx.setValue(ulx);
	}

	/**
	 * Asigna la coordenada superior derecha
	 * @param ulx 
	 */
	public void setUly(String uly) {
		this.uly.setValue(uly);
	}
	
	/**
	 * Obtiene el tamaño de pixel en X
	 * @return
	 */
	public DataInputContainer getPsx() {
		return psx;
	}

	/**
	 * Obtiene el tamaño de pixel en Y
	 * @return
	 */
	public DataInputContainer getPsy() {
		return psy;
	}

	/**
	 * Obtiene la rotación en X
	 * @return
	 */
	public DataInputContainer getRotx() {
		return rotx;
	}

	/**
	 * Obtiene la rotación en Y
	 * @return
	 */
	public DataInputContainer getRoty() {
		return roty;
	}

	/**
	 * Obtiene la X de la coordenada superior izquierda
	 * @return
	 */
	public DataInputContainer getUlx() {
		return ulx;
	}

	/**
	 * Obtiene la Y de la coordenada superior izquierda
	 * @return
	 */
	public DataInputContainer getUly() {
		return uly;
	}
	
}
