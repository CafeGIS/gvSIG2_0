/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
import java.awt.image.BufferedImage;
import java.io.File;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.crs.CRSFactory;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.ProviderNotRegisteredException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.store.shp.SHPStoreParameters;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateEnvelopeException;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.LayerFactory;
import org.gvsig.gui.beans.imagenavigator.IClientImageNavigator;
import org.gvsig.gui.beans.imagenavigator.ImageNavigator;
import org.gvsig.rastertools.TestUI;
import org.gvsig.rastertools.rasterresolution.ZoomPixelCursorListener;
import org.gvsig.tools.task.Cancellable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemoVectorial implements IClientImageNavigator {
	private static final GeometryManager 	geomManager 		= GeometryLocator.getGeometryManager();
	private static final Logger 			logger 				= LoggerFactory.getLogger(DemoVectorial.class);
	private FLayer         					layer              	= null;
	static final String    					fwAndamiDriverPath	= "../_fwAndami/gvSIG/extensiones/com.iver.cit.gvsig/drivers";

	public DemoVectorial() {
		try {
			IProjection PROJ = CRSFactory.getCRS("EPSG:23030");

			File file=new File("C:\\Documents and Settings\\borja\\Escritorio\\images_gvsig\\vectorial\\t_areas.shp");
			DataManager datamanager=DALLocator.getDataManager();
			SHPStoreParameters params=null;
			try {
				params = (SHPStoreParameters)datamanager.createStoreParameters(SHPStoreParameters.DYNCLASS_NAME);
				params.setSRS(PROJ);
			} catch (InitializeException e) {
				throw new LoadLayerException(file.getName(),e);
			} catch (ProviderNotRegisteredException e) {
				throw new LoadLayerException(file.getName(),e);
			}
			params.setFile(file);
			layer = LayerFactory.getInstance().createLayer("line", params);
			TestUI jFrame = new TestUI("DemoVectorial");
			jFrame.setSize(new Dimension(598, 167));
			ImageNavigator imNav = new ImageNavigator(this);
			jFrame.setContentPane(imNav);
			Envelope b = layer.getFullEnvelope();
			imNav.setViewDimensions(b.getMinimum(0), b.getMaximum(1), b.getMaximum(0), b.getMinimum(1));
			imNav.updateBuffer();
			imNav.setEnabled(true);
			jFrame.setVisible(true);
		} catch (Exception e) {
		}
	}

	public static void main(String[] args) {
		new DemoVectorial();
	}

	public void drawImage(Graphics2D g, double x1, double y1, double x2, double y2, double zoom, int width, int height) {
		ViewPort vp = new ViewPort(null);
		try {
			vp.setEnvelope(geomManager.createEnvelope(x1, y2, x2, y1, SUBTYPES.GEOM2D));
		} catch (CreateEnvelopeException e1) {
			logger.error("Error creating the envelope", e1);
		}
		vp.setImageSize(new Dimension(width, height));
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		try {
			layer.draw(bi, g, vp, new Cancellable(){

				public boolean isCanceled() {
					// TODO Auto-generated method stub
					return false;
				}

				public void setCanceled(boolean canceled) {
					// TODO Auto-generated method stub

				}

			}, 0);
		} catch (ReadException e) {
		}
	}
}