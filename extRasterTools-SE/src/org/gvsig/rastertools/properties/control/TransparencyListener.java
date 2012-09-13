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
package org.gvsig.rastertools.properties.control;

import java.util.ArrayList;
import java.util.EventObject;

import org.gvsig.raster.datastruct.TransparencyRange;
import org.gvsig.raster.grid.GridTransparency;
import org.gvsig.raster.hierarchy.IRasterProperties;
import org.gvsig.rastertools.RasterModule;
import org.gvsig.rastertools.properties.RasterPropertiesTocMenuEntry;
import org.gvsig.rastertools.properties.panels.TransparencyPanel;
/**
 * Clase que hace de interfaz entre los objetos que contienen la información de
 * transparencia y el panel.
 *
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class TransparencyListener implements TranspByPixelEventListener {
	private GridTransparency  transparency      = null;
	private TransparencyPanel tPanel            = null;

	/**
	 * Construye un TransparencyControl
	 * @param tp
	 */
	public TransparencyListener(TransparencyPanel tp) {
		this.tPanel = tp;
		tPanel.getPTranspByPixel().addValueChangedListener(this);
	}

	/**
	 * Carga los valores del panel desde el objeto con la transparencia
	 */
	private void setValuesFromGridTransparencyToPanel() {
		//Asignamos la opacidad al panel
		if (transparency.getOpacity() != 255) {
			tPanel.getOpacityPanel().setControlEnabled(true);
			tPanel.getOpacityPanel().setValue((int) (transparency.getOpacity() * 100) / 255);
		} else {
			tPanel.getOpacityPanel().setControlEnabled(false);
			tPanel.getOpacityPanel().setValue(100);
		}

		// Asignamos los rangos de transparencia al panel
		tPanel.getPTranspByPixel().getListener().setEventsDisabled(true);
		if (transparency.getTransparencyRange().size() > 0) {
			tPanel.getPTranspByPixel().clear();
			tPanel.getPTranspByPixel().setControlEnabled(true);
			for (int i = 0; i < transparency.getTransparencyRange().size(); i++) {
				TransparencyRange range = (TransparencyRange) transparency.getTransparencyRange().get(i);
				tPanel.getPTranspByPixel().addStringElement(range);
			}
		} else {
			tPanel.getPTranspByPixel().clear();
			tPanel.getPTranspByPixel().setControlEnabled(false);
		}
		tPanel.getPTranspByPixel().getListener().setEventsDisabled(false);
	}

	/**
	 * Carga los valores del objeto transparencia desde el panel
	 */
	private void setValuesFromPanelToGridTransparency() {
		if (transparency == null)
			return;
		// Asignamos la opacidad al objeto
		if (tPanel.getOpacityPanel().getCheck().isSelected()) {
			transparency.setOpacity((int) Math.round((tPanel.getOpacityPanel().getValue() * 255) / 100));
		} else
			transparency.setOpacity(255);

		// Asignamos los rangos de transparencia al objeto
		if (tPanel.getPTranspByPixel().getActiveCheck().isSelected()) {
			transparency.clearListOfTransparencyRange();
			ArrayList entries = tPanel.getPTranspByPixel().getEntries();
			for (int i = 0; i < entries.size(); i++)
				transparency.setTransparencyRange((TransparencyRange) entries.get(i));
		} else
			transparency.clearListOfTransparencyRange();

		transparency.activeTransparency();
		
		// Redibujamos
		if (tPanel.getLayer() != null)
			tPanel.getLayer().getMapContext().invalidate();
	}

	/**
	 * Aplica y guarda los cambios del panel
	 */
	public void apply() {
		onlyApply();
		saveStatus();
	}

	/**
	 * Aplicar los cambios sin guardar su estado
	 */
	public void onlyApply() {
		if (RasterPropertiesTocMenuEntry.enableEvents)
			setValuesFromPanelToGridTransparency();
	}

	/**
	 * Guarda el estado actual del panel
	 */
	private void saveStatus() {
		tPanel.getPanelGroup().getProperties().put("opacity", new Integer(transparency.getOpacity()));

		tPanel.getPanelGroup().getProperties().put("transparencyActive", new Boolean(transparency.isTransparencyActive()));

		ArrayList newArray = new ArrayList();
		for (int i = 0; i < transparency.getTransparencyRange().size(); i++) {
			newArray.add(transparency.getTransparencyRange().get(i));
		}
		tPanel.getPanelGroup().getProperties().put("transparencyRange", newArray);
	}

	/**
	 * Deja la capa en el último estado guardado y la refresca
	 */
	public void restoreStatus() {
		transparency.setOpacity(((Integer) tPanel.getPanelGroup().getProperties().get("opacity")).intValue());
		transparency.setTransparencyActive(((Boolean) tPanel.getPanelGroup().getProperties().get("transparencyActive")).booleanValue());

		ArrayList newArray = (ArrayList) tPanel.getPanelGroup().getProperties().get("transparencyRange");
		transparency.clearListOfTransparencyRange();
		for (int i = 0; i < newArray.size(); i++) {
			transparency.setTransparencyRange((TransparencyRange) newArray.get(i));
		}

		if (tPanel.getLayer() != null)
			tPanel.getLayer().getMapContext().invalidate();
	}

	/**
	 * Acciones a ejecutar cuando se acepta
	 */
	public void accept() {
		onlyApply();
	}

	/**
	 * Acciones a ejecutar cuando se cancela
	 */
	public void cancel() {
		restoreStatus();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.properties.panels.TranspByPixelEventListener#actionPixelListChanged(java.util.EventObject)
	 */
	public void actionPixelListChanged(EventObject e) {
		if (!RasterModule.autoRefreshView)
			return;

		onlyApply();
	}

	/**
	 * Aqui se definen los parametros iniciales para la transparencia del panel
	 * @param t
	 */
	public void setLayer(IRasterProperties layer) {
		transparency = layer.getRenderTransparency();
		saveStatus();
		setValuesFromGridTransparencyToPanel();
	}
}