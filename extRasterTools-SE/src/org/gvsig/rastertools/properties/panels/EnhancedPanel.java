/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
package org.gvsig.rastertools.properties.panels;

import java.awt.Dimension;
import java.awt.GridLayout;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.fmap.raster.layers.IRasterLayerActions;
import org.gvsig.gui.beans.panelGroup.panels.AbstractPanel;
import org.gvsig.gui.util.StatusComponent;
import org.gvsig.raster.grid.filter.RasterFilterList;
import org.gvsig.raster.hierarchy.IRasterDataset;
import org.gvsig.raster.hierarchy.IRasterProperties;
import org.gvsig.rastertools.properties.control.EnhancedControl;

import com.iver.andami.PluginServices;
/**
 * Panel para los controles de brillo y contrase .
 *
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class EnhancedPanel extends AbstractPanel {
	final private static long serialVersionUID = 0;

	private EnhancedBrightnessContrastPanel contrastPanel   = null;
	private EnhancedWithTrimPanel           trimPanel       = null;
	private EnhancedControl                 enhancedControl = null;
	private FLayer                          fLayer          = null;

	/**
	 * Contructor
	 */
	public EnhancedPanel() {
		setLabel(PluginServices.getText(this, "realce"));
		initialize();
	}


	protected void initialize() {
		setLayout(new GridLayout(1, 2));
		add(getBrightnessContrastPanel());
		add(getEnhancedWithTrimPanel());
		this.setPreferredSize(new Dimension(100, 80));
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.gui.properties.dialog.IRegistrablePanel#initializeUI()
	 */
	public void initializeUI() {
	}

	/**
	 * Obtiene el panel de brillo y contraste
	 * @return EnhancedBrightnessContrastPanel
	 */
	public EnhancedBrightnessContrastPanel getBrightnessContrastPanel() {
		if (contrastPanel == null) {
			contrastPanel = new EnhancedBrightnessContrastPanel();
		}
		return contrastPanel;
	}

	/**
	 * Obtiene el panel de realce con recorte de colas
	 * @return EnhancedWithTrimPanel
	 */
	public EnhancedWithTrimPanel getEnhancedWithTrimPanel() {
		if (trimPanel == null) {
			trimPanel = new EnhancedWithTrimPanel();
		}
		return trimPanel;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.gui.properties.dialog.IRegistrablePanel#accept()
	 */
	public void accept() {
		enhancedControl.accept();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.gui.properties.dialog.IRegistrablePanel#apply()
	 */
	public void apply() {
		enhancedControl.apply();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.gui.properties.dialog.IRegistrablePanel#cancel()
	 */
	public void cancel() {
		enhancedControl.cancel();
	}

	public void setReference(Object ref) {
		super.setReference(ref);

		if (!(ref instanceof FLayer))
			return;

		FLayer lyr = (FLayer) ref;

		fLayer = lyr;
		actionEnabled();
		IRasterDataset dataset = null;
		RasterFilterList rfl = null;

		if (lyr instanceof IRasterDataset)
			dataset = (IRasterDataset) lyr;

		if (lyr instanceof IRasterProperties)
			rfl = (((IRasterProperties) lyr).getRenderFilterList());

		enhancedControl = new EnhancedControl(getPanelGroup(), this, dataset, fLayer, rfl);
	}


	private void actionEnabled() {
		FLyrRasterSE fLyrRasterSE = ((FLyrRasterSE) fLayer);

		if (!fLyrRasterSE.isActionEnabled(IRasterLayerActions.BRIGHTNESSCONTRAST))
			StatusComponent.setDisabled(getBrightnessContrastPanel());

		if (!fLyrRasterSE.isActionEnabled(IRasterLayerActions.ENHANCED))
			StatusComponent.setDisabled(getEnhancedWithTrimPanel());

		if (!fLyrRasterSE.isActionEnabled(IRasterLayerActions.BRIGHTNESSCONTRAST) &&
				!fLyrRasterSE.isActionEnabled(IRasterLayerActions.ENHANCED))
			setVisible(false);
		else
			setVisible(true);
	}

	public void selected() {
	}
}