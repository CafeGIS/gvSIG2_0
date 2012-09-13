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
package org.gvsig.georeferencing.ui.zoom;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.georeferencing.ui.zoom.layers.GCPsGraphicLayer;
import org.gvsig.georeferencing.view.BaseZoomView;
import org.gvsig.raster.dataset.InvalidSetViewException;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.datastruct.Extent;
import org.gvsig.raster.datastruct.ViewPortData;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.raster.util.RasterUtilities;

/**
 * Gestor de peticiones de zoom sobre el panel con el raster a georreferenciar.
 * Implementa el interfaz IExtensionRequest para que este comunique la peticiones
 * de zoom y ViewRasterRequestManager pueda hacer las peticiones de datos a la capa.
 *
 * 30/12/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class ViewRasterRequestManager implements IExtensionRequest {
	private FLyrRasterSE       				lyr 				= null;
	private BaseZoomView       				view 				= null;
	private GCPsGraphicLayer   				graphicLayer 		= null;
	private Color              				backGroundColor   	= null;

	/**
	 * Asigna la capa a georreferenciar de donde se obtienen los datos.
	 * @param lyr
	 */
	public ViewRasterRequestManager(BaseZoomView view, FLyrRasterSE lyr) {
		this.lyr = lyr;
		this.view = view;
	}

	/**
	 * Asigna la capa de puntos de control
	 * @param gl
	 */
	public void setGCPsGraphicLayer(GCPsGraphicLayer gl) {
		this.graphicLayer = gl;
	}
	
	/**
	 * Calcula la extensión que contendrá la vista a partir del extent
	 * máximo de la capa/s que contienen . Tienen en cuenta las distintan proporciones
	 * entre vista y petición
	 * @param Rectangle2D
	 */
	public Rectangle2D initRequest(Envelope env) throws InvalidRequestException {
		Rectangle2D r = new Rectangle2D.Double(env.getMinimum(0), env.getMinimum(1), env.getLength(0), env.getLength(1));
		return initRequest(r);
	}

	/**
	 * Calcula la extensión que contendrá la vista a partir del extent
	 * máximo de la capa/s que contienen . Tienen en cuenta las distintan proporciones
	 * entre vista y petición
	 * @param Rectangle2D
	 */
	public Rectangle2D initRequest(Rectangle2D extent) throws InvalidRequestException {
		double x = extent.getX();
		double y = extent.getY();
		double w = extent.getWidth();
		double h = extent.getHeight();
		//Calculamos la extensión de la vista para el extent máximo que va a contener
		//teniendo en cuenta las proporciones de ambos.
		if(extent.getWidth() < extent.getHeight()) {
			if(((double)view.getCanvasWidth() / (double)view.getCanvasHeight()) <= (extent.getWidth() / extent.getHeight())) {
				h = (view.getCanvasHeight() * w) / view.getCanvasWidth();
				y = extent.getCenterY() - (h / 2);
			} else { //p1 < p2
				w = (view.getCanvasWidth() * h) / view.getCanvasHeight();
				x = extent.getCenterX() - (w / 2);
			}
		} else {
			if(((double)view.getCanvasWidth() / (double)view.getCanvasHeight()) >= (extent.getWidth() / extent.getHeight())) {
				w = (view.getCanvasWidth() * h) / view.getCanvasHeight();
				x = extent.getCenterX() - (w / 2);
			} else { //p1 < p2
				h = (view.getCanvasHeight() * w) / view.getCanvasWidth();
				y = extent.getCenterY() - (h / 2);
			}
		}
		Rectangle2D r = new Rectangle2D.Double(x, y, w, h);
		setDrawParams(null, r);
		request(r);
		return r;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.georeferencing.ui.zoom.IExtensionRequest#request(java.awt.geom.Rectangle2D)
	 */
	public Rectangle2D request(Rectangle2D extent) throws InvalidRequestException {
		if(extent == null) {
			Envelope env = lyr.getFullEnvelope();
			Rectangle2D r = new Rectangle2D.Double(env.getMinimum(0), env.getMaximum(1), env.getLength(0), env.getLength(1));
			return r;
		}

		if(lyr.getFullRasterExtent() == null) {
			RasterToolsUtil.messageBoxError("error_set_view",null);
			return null;
		}

		//Ajustamos el extent al del raster
		Extent ext = null;
		Extent extSelection = null;
		if(view.getCanvas().getMinxMaxyUL()) {
			ext = new Extent(extent);
			extSelection = calculateAdjustedView(ext, lyr.getFullRasterExtent());
		} else {
			ext = new Extent(extent.getMinX(), extent.getMinY(), extent.getMaxX(), extent.getMaxY());
		    extSelection = RasterUtilities.calculateAdjustedView(ext, lyr.getFullRasterExtent());
		}
		
		//Obtenemos el viewport y calculamos la matriz de transformación
		ViewPortData vp = new ViewPortData(	null, ext, new Dimension(view.getCanvasWidth(), view.getCanvasHeight()));
		vp.calculateAffineTransform();
		
		//Calculamos el punto del canvas de la vista donde se empieza a dibujar el buffer de la imagen
		Point2D pt = new Point2D.Double(extSelection.getULX(), extSelection.getULY());
		AffineTransform at = vp.mat;
		if(!view.getCanvas().getMinxMaxyUL()) {
			at = new AffineTransform(1, 0, 0, -1, 0, 0);
			at.concatenate(vp.mat);
		}
		at.transform(pt, pt);
		
		try {
			//Dibujamos a través del render de la capa en un graphics como el de la vista
			BufferedImage initImg = new BufferedImage(view.getCanvasWidth(), view.getCanvasHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics2D img = ((Graphics2D)initImg.getGraphics());
			if(backGroundColor != null && backGroundColor != Color.BLACK) {
				img.setColor(backGroundColor);
				img.fillRect(0, 0, view.getCanvasWidth(), view.getCanvasHeight());
			}
			BufferedImage drawedImg = (BufferedImage)lyr.getRender().draw(img, vp);
			img.drawImage(drawedImg, (int) Math.round(pt.getX()), (int) Math.round(pt.getY()), null);
		
			setDrawParams(initImg, extent);
			
			if(graphicLayer != null)
				graphicLayer.recalcPixelDrawCoordinates();
			
		} catch (RasterDriverException e) {
			throw new InvalidRequestException("Error en al acceso al fichero");
		} catch (InvalidSetViewException e) {
			throw new InvalidRequestException("Error asignando el área de la petición");
		} catch (InterruptedException e) {
		}
		return extent;
	}

	/**
	 * Ajusta la extensión del primer parámetro a los límites de la segunda
	 * @param extToAdj
	 * @param imgExt
	 * @return
	 */
	private Extent calculateAdjustedView(Extent extToAdj, Extent imgExt) {
        double vx = extToAdj.minX();
        double vy = extToAdj.minY();
        double vx2 = extToAdj.maxX();
        double vy2 = extToAdj.maxY();

        if (extToAdj.minX() < imgExt.minX()) {
			vx = imgExt.minX();
		}
        if (extToAdj.minY() < imgExt.minY()) {
			vy = imgExt.minY();
		}
        if (extToAdj.maxX() > imgExt.maxX()) {
			vx2 = imgExt.maxX();
		}
        if (extToAdj.maxY() > imgExt.maxY()) {
			vy2 = imgExt.maxY();
		}

        return new Extent(vx, vy2, vx2, vy);
    }

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.georeferencing.ui.zoom.IExtensionRequest#fullExtent(java.awt.Dimension)
	 */
	public void fullExtent() throws InvalidRequestException  {
		Rectangle2D r = lyr.getFullRasterExtent().toRectangle2D();
		this.initRequest(r);
	}

	/**
	 * Asigna los parámetros para el control de zoom del mapa
	 * @param img BufferedImage
	 * @param vp ViewPort
	 */
	public void setDrawParams(BufferedImage img, Rectangle2D extBuf) {
		if(view != null && lyr != null) {
			if(img != null)
				view.setDrawParams(img, extBuf, extBuf.getWidth() / img.getWidth(), new Point2D.Double(extBuf.getCenterX(), extBuf.getCenterY()));
			else
				view.setDrawParams(img, extBuf, extBuf.getWidth() / view.getCanvasWidth(), new Point2D.Double(extBuf.getCenterX(), extBuf.getCenterY()));
		}
	}

	/**
	 * Obtiene el color de fondo
	 * @return
	 */
	public Color getBackGroundColor() {
		return backGroundColor;
	}

	/**
	 * Asigna el color de fondo
	 * @param backGroundColor
	 */
	public void setBackGroundColor(Color backGroundColor) {
		this.backGroundColor = backGroundColor;
	}

}
