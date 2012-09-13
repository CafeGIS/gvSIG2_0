/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.rastertools.clipping.ui.listener;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.mapcontrol.tools.Events.EnvelopeEvent;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.fmap.raster.tools.SaveRasterListenerImpl;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.raster.util.RasterUtilities;
import org.gvsig.rastertools.clipping.ClippingData;
import org.gvsig.rastertools.clipping.ui.ClippingPanel;
/**
 * <code>ClippingMouseViewListener</code> es un listener del recorte de raster.
 * Al seleccionar un área sobre la vista debe cargar el cuadro con los datos de
 * coordenadas pixel, coordenadas reales, ancho y alto del raster resultante,
 * tamaño de celda.
 *
 * @version 19/04/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class ClippingMouseViewListener extends SaveRasterListenerImpl {
	private FLyrRasterSE    layer          = null;
	private ClippingPanel   clippingPanel  = null;
	private ClippingData    data           = null;
	private MapControl      mapControl     = null;
	private String          currentTool    = null;

	/**
	 * Crea un nuevo <code>ClippingMouseViewListener</code>
	 *
	 * @param mapCtrl
	 * @param cutRasterDialog
	 */
	public ClippingMouseViewListener(MapControl mapControl, ClippingPanel clippingPanel, ClippingData data, FLyrRasterSE lyr) {
		super(mapControl);
		this.clippingPanel = clippingPanel;
		this.data = data;
		this.layer = lyr;
		this.mapControl = mapControl;
		this.currentTool = mapControl.getCurrentTool();
	}

	/**
	 * Asigna la capa raster.
	 * @param flyr Capa raster
	 */
	public void setRasterLayer(FLyrRasterSE flyr) {
		layer = flyr;
	}

	/**
	 * Realiza las acciones de selección del área de recorte por medio de un rectangulo
	 * sobre la vista.
	 */
	public void rectangle(EnvelopeEvent event) {
		super.rectangle(event);
		clippingPanel.getButtonsPanel().getButton(ButtonsPanel.BUTTON_APPLY).setEnabled(false);
		clippingPanel.getButtonsPanel().getButton(ButtonsPanel.BUTTON_ACCEPT).setEnabled(false);

		if(((FLyrRasterSE) layer).getAffineTransform() == null)
			return;
		
		AffineTransform at = ((FLyrRasterSE) layer).getAffineTransform();
		//Temporalmente cargamos las coordenadas reales para luego transformarlas a pixel
		Point2D ulWc = new Point2D.Double(rect.getMinimum(0), rect.getMaximum(1));
		Point2D lrWc = new Point2D.Double(rect.getMaximum(0), rect.getMinimum(1));
		Point2D urWc = new Point2D.Double(rect.getMaximum(0), rect.getMaximum(1));
		Point2D llWc = new Point2D.Double(rect.getMinimum(0), rect.getMinimum(1));
		Point2D ulPx = new Point2D.Double();
		Point2D lrPx = new Point2D.Double();
		Point2D urPx = new Point2D.Double();
		Point2D llPx = new Point2D.Double();
		try {
			at.inverseTransform(ulWc, ulPx);
			at.inverseTransform(lrWc, lrPx);
			at.inverseTransform(urWc, urPx);
			at.inverseTransform(llWc, llPx);
		} catch (NoninvertibleTransformException e) {
			RasterToolsUtil.messageBoxError(RasterToolsUtil.getText(this, "coordenadas_erroneas"), null);
			return;
		}
		
		Point2D[] pointList = new Point2D[]{ulPx, lrPx, urPx, llPx};
		Point2D dim = new Point2D.Double(layer.getPxWidth(), layer.getPxHeight());

		//Comprobamos si la selección está completamente fuera del área
		if(isOutside(ulPx, lrPx)) {
			data.setOutOfArea();
			return;
		}

		//Ajustamos los puntos al área en pixeles del raster
		RasterUtilities.adjustToPixelSize(pointList, dim);

		//Convertimos nuevamente a coordenadas reales después de ajustar
		at.transform(ulPx, ulWc);
		at.transform(lrPx, lrWc);
		at.transform(llPx, llWc);
		at.transform(urPx, urWc);

		data.setCoorPixel(ulPx, lrPx, llPx, urPx);
		data.setCoorReal(ulWc, lrWc, llWc, urWc);
		data.setAffineTransform(at);
		data.initSize();
				
		clippingPanel.saveStatus(data);

		clippingPanel.getButtonsPanel().getButton(ButtonsPanel.BUTTON_APPLY).setEnabled(true);
		clippingPanel.getButtonsPanel().getButton(ButtonsPanel.BUTTON_ACCEPT).setEnabled(true);
		
		if (currentTool != null)
			mapControl.setTool(currentTool);
	}

	/**
	 * Comprueba si la selección del punto está fuera del área del raster.
	 * @param ulPx Coordenada superior izquierda en pixeles
	 * @param lrPx Corrdenada inferior derecha en pixeles
	 * @return true si la selección del punto está fuera del raster y false si no lo está
	 */
	private boolean isOutside(Point2D ulPx, Point2D lrPx) {
		double minX = Math.min(ulPx.getX(), lrPx.getX());
		double minY = Math.min(ulPx.getY(), lrPx.getY());
		double maxX = Math.max(ulPx.getX(), lrPx.getX());
		double maxY = Math.max(ulPx.getY(), lrPx.getY());
		if(minX >= layer.getPxWidth() || minY >= layer.getPxHeight() || maxX < 0 || maxY < 0)
			return true;
		return false;
	}
}