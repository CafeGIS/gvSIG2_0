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
package org.gvsig.raster.beans.previewbase;

import java.awt.Dimension;
import java.awt.Graphics2D;

import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.impl.DefaultFeatureType.SubtypeFeatureTypeNameException;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateEnvelopeException;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.imagenavigator.IClientImageNavigator;
import org.gvsig.gui.beans.imagenavigator.ImageUnavailableException;
import org.gvsig.raster.grid.filter.FilterTypeException;
import org.gvsig.raster.hierarchy.IRasterRendering;
import org.gvsig.raster.util.RasterToolsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;

/**
 * Gestor de visualización de preview. Se encarga del repintado de la imagen
 * de la previsualización
 *
 * 19/02/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class PreviewRequestManager implements IClientImageNavigator {
	private static GeometryManager  geomManager       = GeometryLocator.getGeometryManager();
	private static final Logger     logger            = LoggerFactory.getLogger(PreviewRequestManager.class);
	private PreviewBasePanel        previewBasePanel  = null;
	private FLyrRasterSE            previewLayer      = null;
	private IPreviewRenderProcess   renderProcess     = null;

	/**
	 * Construye un ColorTableListener
	 * @param
	 */
	public PreviewRequestManager(	PreviewBasePanel panel,
									IPreviewRenderProcess renderProcess,
									FLyrRasterSE layer) {
		this.previewBasePanel = panel;
		this.renderProcess = renderProcess;
		setLayer(layer);
	}

	/**
	 * Asigna la capa raster de la vista
	 * @param fLayer
	 */
	private void setLayer(FLayer fLayer) {
		if (fLayer instanceof FLyrRasterSE)
			try {
				previewLayer = (FLyrRasterSE) fLayer.cloneLayer();
			} catch (Exception e) {
				RasterToolsUtil.messageBoxError("preview_not_available", previewBasePanel, e);
			}
	}

	/**
	 * Cierra la capa abierta para previsualización
	 */
	public void closePreviewLayer() {
		if (previewLayer != null) {
			previewLayer.setRemoveRasterFlag(true);
			previewLayer.removeLayerListener(null);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.imagenavigator.IClientImageNavigator#drawImage(java.awt.Graphics2D,
	 *      double, double, double, double, double, int, int)
	 */
	public void drawImage(Graphics2D g, double x1, double y1, double x2, double y2, double zoom, int width, int height)
		throws ImageUnavailableException {
		if (previewLayer == null || !(previewLayer instanceof IRasterRendering))
			throw new ImageUnavailableException(PluginServices.getText(this, "error_dont_exists_layer"));

		IRasterRendering rendering = (previewLayer);

		// Inicializo el ViewPort
		ViewPort vp = new ViewPort(null);
		Envelope env;
		try {
			env = geomManager.createEnvelope(x1, y2, x2, y1, SUBTYPES.GEOM2D);
			vp.setEnvelope(env);
		} catch (CreateEnvelopeException e3) {
			logger.error("Error drawing the image", e3);
		}	
		vp.setImageSize(new Dimension(width, height));

		rendering.getRenderFilterList().pushStatus();
		try {
			renderProcess.process(rendering);
		} catch (FilterTypeException e1) {
			RasterToolsUtil.debug("error_adding_filters", this, e1);
			throw new ImageUnavailableException(PluginServices.getText(this, "error_adding_filters"));
		} catch (ImageUnavailableException e2) {
			throw new ImageUnavailableException(PluginServices.getText(this, "error_adding_filters"));
		} catch (Exception e2) {
			RasterToolsUtil.debug("error_adding_filters", this, e2);
			throw new ImageUnavailableException(PluginServices.getText(this, "error_adding_filters"));
		}

		try {
			previewLayer.draw(null, g, vp, null, 1.0);
		} catch (ReadException e) {
			RasterToolsUtil.debug("error_preview_render", this, e);
			throw new ImageUnavailableException(PluginServices.getText(this, "error_preview_render"));
		} catch (Exception e2) {
			RasterToolsUtil.debug("error_preview_render", this, e2);
			throw new ImageUnavailableException(PluginServices.getText(this, "error_preview_render"));
		}
		rendering.getRenderFilterList().popStatus();
	}
}