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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.geom.AffineTransform;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.datainput.DataInputContainer;
import org.gvsig.raster.util.MathUtils;
import org.gvsig.rastertools.geolocation.listener.GeoLocationPanelListener;

import com.iver.andami.PluginServices;

/**
 * Panel de geolocalización. Este muestra las esquinas superior derecha e 
 * inferior izquierda.
 * 
 * @version 12/12/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class GeoLocationOpeningRasterCornersPanel extends GeolocationBaseClassPanel {
	private static final long          serialVersionUID = -7797379892312214949L;
	private DataInputContainer	       ulx = null;
	private DataInputContainer	       uly = null;
	private DataInputContainer	       lrx = null;
	private DataInputContainer	       lry = null;
		
	private JPanel			           coordsULPanel = null;
	private JPanel			           coordsLRPanel = null;
	
	private String                     pathToImages = "images/";

	
	/**
	 * Constructor
	 */
	public GeoLocationOpeningRasterCornersPanel(GeoLocationPanelListener list) {
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
		
		lrx = new DataInputContainer();
		lrx.setLabelText(PluginServices.getText(this,"ux"));
		lrx.addValueChangedListener(listener);
		lrx.addKeyListener(listener);
				
		lry = new DataInputContainer();
		lry.setLabelText(PluginServices.getText(this,"uy"));
		lry.addValueChangedListener(listener);
		lry.addKeyListener(listener);
		
		GridLayout l = new GridLayout(2, 1);
		l.setVgap(2);
		
		coordsULPanel = new JPanel(l);
		coordsLRPanel = new JPanel(l);
		
		init();
	}
	
	/**
	 * Inicialización de los componentes gráficos del panel.
	 */
	private void init() {		
		JPanel p1 = new JPanel(new BorderLayout());
		p1.add(getIcoUL(), BorderLayout.WEST);
		p1.add(ulx, BorderLayout.CENTER);
		
		JPanel p2 = new JPanel(new BorderLayout());
		p2.add(getIcoUL(), BorderLayout.WEST);
		p2.add(uly, BorderLayout.CENTER);
		
		coordsULPanel.add(p1);
		coordsULPanel.add(p2);
		
		JPanel p3 = new JPanel(new BorderLayout());
		p3.add(getIcoLR(), BorderLayout.WEST);
		p3.add(lrx, BorderLayout.CENTER);
		
		JPanel p4 = new JPanel(new BorderLayout());
		p4.add(getIcoLR(), BorderLayout.WEST);
		p4.add(lry, BorderLayout.CENTER);
		
		coordsLRPanel.add(p3);
		coordsLRPanel.add(p4);
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new java.awt.Insets(1, 1, 1, 1);
		this.add(coordsULPanel, gbc);
		
		gbc.gridy = 1;
		this.add(coordsLRPanel, gbc);		
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
	 * Obtiene el JLabel con el icono de la esquina superior
	 * @return JLabel
	 */
	private JLabel getIcoUL(){
		return new JLabel(new ImageIcon(getClass().getResource(pathToImages + "upleft.png")));
	}

	/**
	 * Obtiene el JLabel con el icono de la esquina inferior
	 * @return JLabel
	 */
	private JLabel getIcoLR(){
		return new JLabel(new ImageIcon(getClass().getResource(pathToImages + "downright.png")));
	}
	
	/**
	 * Carga los parámetros en el dialogo a partir de la capa
	 * @param lyr Capa raster
	 */
	public void loadTransform(AffineTransform at) {
		listener.setEnableValueChangeEvent(false);
		setUlx(String.valueOf(MathUtils.format(at.getTranslateX(), tailValue)));
		setUly(String.valueOf(MathUtils.format(at.getTranslateY(), tailValue)));
		setLrx(String.valueOf(MathUtils.format(at.getTranslateX() + (at.getScaleX() * getLayer().getPxWidth()), tailValue)));
		setLry(String.valueOf(MathUtils.format(at.getTranslateY() + (at.getScaleY()  * getLayer().getPxHeight()), tailValue)));		
		listener.setEnableValueChangeEvent(true);
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
	 * Asigna la coordenada inferior derecha
	 * @param lrx
	 */
	public void setLrx(String lrx) {
		this.lrx.setValue(lrx);
	}

	/**
	 * Asigna la coordenada superior derecha
	 * @param lrx 
	 */
	public void setLry(String lry) {
		this.lry.setValue(lry);
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

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.geolocation.ui.GeolocationBaseClassPanel#getPsx()
	 */
	public DataInputContainer getPsx() {
		DataInputContainer d = new DataInputContainer();
		try {
			double dUlx = Double.valueOf(ulx.getValue()).doubleValue();
			double dLrx = Double.valueOf(lrx.getValue()).doubleValue();
			d.setValue(String.valueOf((dLrx - dUlx) / getLayer().getPxWidth()));
		} catch (NumberFormatException e) {
			d.setValue("1");
		}
		return d;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.geolocation.ui.GeolocationBaseClassPanel#getPsy()
	 */
	public DataInputContainer getPsy() {
		DataInputContainer d = new DataInputContainer();
		try {
			double dUly = Double.valueOf(uly.getValue()).doubleValue();
			double dLry = Double.valueOf(lry.getValue()).doubleValue();
			d.setValue(String.valueOf((dLry - dUly) / getLayer().getPxHeight()));
		} catch (NumberFormatException e) {
			d.setValue("1");
		}
		return d;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.geolocation.ui.GeolocationBaseClassPanel#getRotx()
	 */
	public DataInputContainer getRotx() {
		DataInputContainer d = new DataInputContainer();
		d.setValue("0");
		return d;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.geolocation.ui.GeolocationBaseClassPanel#getRoty()
	 */
	public DataInputContainer getRoty() {
		DataInputContainer d = new DataInputContainer();
		d.setValue("0");
		return d;
	}

}
