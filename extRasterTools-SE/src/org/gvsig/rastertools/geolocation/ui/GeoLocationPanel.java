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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.geom.AffineTransform;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.datainput.DataInputContainer;
import org.gvsig.raster.util.Historical;
import org.gvsig.raster.util.MathUtils;
import org.gvsig.rastertools.geolocation.listener.GeoLocationPanelListener;

import com.iver.andami.PluginServices;

/**
 * Panel de geolocalización. Este muestra los parámetros de la matriz de transformación
 * que está aplicandose en esos momentos al raster. Los parámetros son variados en 
 * tiempo real a medida que modificamos la georreferenciación con las herramientas.
 * 
 * @version 30/07/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class GeoLocationPanel extends GeolocationBaseClassPanel {
	private static final long         serialVersionUID = -7797379892312214949L;
	private DataInputContainer	       ulx             = null;
	private DataInputContainer	       uly             = null;
	private DataInputContainer	       psx             = null;
	private DataInputContainer	       psy             = null;
	private DataInputContainer	       rotx            = null;
	private DataInputContainer	       roty            = null;
	private JButton                    first           = null;
	private JButton                    save            = null;
	private JButton                    back            = null;
	private JButton                    next            = null;
	private JButton                    apply           = null;
	private JButton                    reset           = null;
	private JButton                    tfwload         = null;
	private JButton                    center          = null;
	private JButton                    focus           = null;
	
	private JPanel			           coordsPanel     = null;
	private JPanel			           paramsPanel     = null;
	private JPanel			           buttonsPanel    = null;
		
	/**
	 * Constructor
	 */
	public GeoLocationPanel(GeoLocationDialog dialog) {
		ImageIcon backIcon = null;
		ImageIcon nextIcon = null;
		ImageIcon saveIcon = null;
		ImageIcon firstIcon = null;
		ImageIcon resetIcon = null;
		ImageIcon tfwLoadIcon = null;
		ImageIcon centerRasterIcon = null;
		ImageIcon focusIcon = null;
		try {
			backIcon = PluginServices.getIconTheme().get("back-icon");
			nextIcon = PluginServices.getIconTheme().get("next-icon");
			saveIcon = PluginServices.getIconTheme().get("save-icon"); 
			firstIcon = PluginServices.getIconTheme().get("undo-icon");
			resetIcon = PluginServices.getIconTheme().get("reset-icon");
			centerRasterIcon = PluginServices.getIconTheme().get("centerraster-icon");
			tfwLoadIcon = PluginServices.getIconTheme().get("tfwload-icon");
			focusIcon = PluginServices.getIconTheme().get("focus-icon");
		} catch(NullPointerException e) {
			
		}
		
		listener = new GeoLocationPanelListener(this, dialog);
		
		GridBagLayout gl = new GridBagLayout();
		this.setLayout(gl);
		setBorder(javax.swing.BorderFactory.createTitledBorder(null, PluginServices.getText(this, "geolocation"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
		ulx = new DataInputContainer();
		ulx.setLabelText(PluginServices.getText(this,"ux"));
		ulx.addValueChangedListener(listener);
		
		uly = new DataInputContainer();
		uly.setLabelText(PluginServices.getText(this,"uy"));
		uly.addValueChangedListener(listener);
		
		psx = new DataInputContainer();
		psx.setLabelText(PluginServices.getText(this,"px"));
		psx.addValueChangedListener(listener);
		
		psy = new DataInputContainer();
		psy.setLabelText(PluginServices.getText(this,"py"));
		psy.addValueChangedListener(listener);
		
		rotx = new DataInputContainer();
		rotx.setLabelText(PluginServices.getText(this,"rx"));
		rotx.addValueChangedListener(listener);
		
		roty = new DataInputContainer();
		roty.setLabelText(PluginServices.getText(this,"ry"));
		roty.addValueChangedListener(listener);
		
		first = new JButton(firstIcon);
		save = new JButton(saveIcon);
		back = new JButton(backIcon);
		next = new JButton(nextIcon);
		apply = new JButton(PluginServices.getText(this,"apply"));
		reset = new JButton(resetIcon);
		tfwload = new JButton(tfwLoadIcon);
		center = new JButton(centerRasterIcon);
		focus = new JButton(focusIcon);
		
		save.setToolTipText(PluginServices.getText(this,"salvar_transf"));
		back.setToolTipText(PluginServices.getText(this,"back_transf"));
		next.setToolTipText(PluginServices.getText(this,"next_transf"));
		apply.setToolTipText(PluginServices.getText(this,"aplicar_transf"));
		first.setToolTipText(PluginServices.getText(this,"first_transf"));
		reset.setToolTipText(PluginServices.getText(this,"reset_transf"));
		center.setToolTipText(PluginServices.getText(this,"center_raster"));
		tfwload.setToolTipText(PluginServices.getText(this,"tfw_load"));
		focus.setToolTipText(PluginServices.getText(this,"get_tool_focus"));
		
		save.setPreferredSize(new Dimension(28, 24));
		first.setPreferredSize(new Dimension(28, 24));
		back.setPreferredSize(new Dimension(28, 24));
		next.setPreferredSize(new Dimension(28, 24));
		apply.setPreferredSize(new Dimension(64,24));
		reset.setPreferredSize(new Dimension(28,24));
		center.setPreferredSize(new Dimension(28,24));
		tfwload.setPreferredSize(new Dimension(28,24));
		focus.setPreferredSize(new Dimension(28,24));
		
		first.addActionListener(listener);
		save.addActionListener(listener);
		back.addActionListener(listener);
		next.addActionListener(listener);
		apply.addActionListener(listener);
		reset.addActionListener(listener);
		tfwload.addActionListener(listener);
		center.addActionListener(listener);
		focus.addActionListener(listener);
		
		save.setEnabled(false);
		back.setEnabled(false);
		next.setEnabled(false);
		first.setEnabled(false);
		reset.setEnabled(true);
		center.setEnabled(true);
		tfwload.setEnabled(true);
		focus.setEnabled(true);
		
		coordsPanel = new JPanel();
		GridLayout l = new GridLayout(2, 1);
		l.setVgap(2);
		coordsPanel.setLayout(l);
		
		paramsPanel = new JPanel();
		GridLayout l1 = new GridLayout(2, 2);
		l1.setVgap(2);
		paramsPanel.setLayout(l1);

		getButtonsPanel();
		
		init();
	}
	
	private void init() {
		coordsPanel.add(ulx);
		coordsPanel.add(uly);
		paramsPanel.add(psx);
		paramsPanel.add(psy);
		paramsPanel.add(rotx);
		paramsPanel.add(roty);
		
		JPanel smallButtons = new JPanel();
		smallButtons.setLayout(new GridBagLayout());
		JPanel applyButton = new JPanel();
		applyButton.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
				
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new java.awt.Insets(1, 0, 0, 2);
		smallButtons.add(focus, gbc);
		gbc.gridx = 1;
		smallButtons.add(center, gbc);
		gbc.gridx = 2;
		smallButtons.add(reset, gbc);
		gbc.gridx = 3;
		smallButtons.add(first, gbc);
		gbc.gridx = 4;
		smallButtons.add(back, gbc);
		gbc.gridx = 5;
		smallButtons.add(next, gbc);
		gbc.gridx = 6;
		smallButtons.add(tfwload, gbc);
		gbc.gridx = 7;
		smallButtons.add(save, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.EAST;
		applyButton.add(apply, gbc);
		
		buttonsPanel.add(smallButtons, gbc);
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		buttonsPanel.add(applyButton, gbc);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new java.awt.Insets(1, 1, 1, 1);
		this.add(coordsPanel, gbc);
		
		gbc.gridy = 1;
		this.add(paramsPanel, gbc);
		gbc.weightx = 1.0;
		gbc.gridy = 2;
		this.add(buttonsPanel, gbc);
	}
	
	/**
	 * Obtiene el panel con los botones
	 * @return JPanel
	 */
	public JPanel getButtonsPanel() {
		if(buttonsPanel == null) {
			buttonsPanel = new JPanel();
			buttonsPanel.setLayout(new GridBagLayout());
			//buttonsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
		}
		return buttonsPanel;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.geolocation.ui.GeolocationBaseClassPanel#activeButtons()
	 */
	public void activeButtons() {
		Historical affineTransformHist = getLayer().getAffineTransformHistorical();
		if(!affineTransformHist.existNext())
			next.setEnabled(false);
		else
			next.setEnabled(true);
		if(!affineTransformHist.existBack())
			back.setEnabled(false);
		else
			back.setEnabled(true);
		if(affineTransformHist.getElementsCount() <= 1)
			first.setEnabled(false);
		else
			first.setEnabled(true);
		if(affineTransformHist.getElementsCount() == 0)
			save.setEnabled(false);
		else
			save.setEnabled(true);
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
	 * Obtiene el botón de aplicar
	 * @return JButton
	 */
	public JButton getApplyButton() {
		return apply;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.geolocation.ui.GeolocationBaseClassPanel#getBackButton()
	 */
	public JButton getBackButton() {
		return back;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.geolocation.ui.GeolocationBaseClassPanel#getFirstButton()
	 */
	public JButton getFirstButton() {
		return first;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.geolocation.ui.GeolocationBaseClassPanel#getSaveButton()
	 */
	public JButton getSaveButton() {
		return save;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.geolocation.ui.GeolocationBaseClassPanel#getResetButton()
	 */
	public JButton getResetButton() {
		return reset;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.geolocation.ui.GeolocationBaseClassPanel#getNextButton()
	 */
	public JButton getNextButton() {
		return next;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.geolocation.ui.GeolocationBaseClassPanel#getTfwLoad()
	 */
	public JButton getTfwLoad() {
		return tfwload;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.geolocation.ui.GeolocationBaseClassPanel#getCenterToView()
	 */
	public JButton getCenterToView() {
		return center;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.geolocation.ui.GeolocationBaseClassPanel#getFocus()
	 */
	public JButton getFocus() {
		return focus;
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
