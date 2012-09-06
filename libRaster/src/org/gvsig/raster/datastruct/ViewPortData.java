/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.raster.datastruct;

import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.cresques.geo.Projected;


/**
 * Datos de vista sobre las capas.
 *
 * Mantiene un conjunto de datos necesarios, que describen el modo de
 * ver las capas actual.
 *
 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>
 */
public class ViewPortData implements Projected {
    /**
     * Tipo de proyección de la vista.
     */
    IProjection proj = null;

    /**
     * Sistema de coordenadas de la vista.
     */
    IProjection cs = null;

    /**
     * Amplitud de la vista, en coordenadas proyectadas.
     */
    Extent extent = null;

    /**
     * Tamaño de la vista, en coordenadas de dispositivo.
     */
    Dimension2D viewSize = null;

    /**
     * Transformación afín usada en la vista actual.
     */
    public AffineTransform mat = null;
    
    /**
     * Información de tamaño en pixeles. Esta no es calculada. Solo se almacena
     * si el usuario la necesita.
     */
    public Point2D pxSize = null;

    /**
     * Resolución (Puntos por pulgada) de la vista actual.
     * Se necesita para los cálculos de escala geográfica.
     */
    int dpi = java.awt.Toolkit.getDefaultToolkit().getScreenResolution();

    public ViewPortData() {
    	mat = new AffineTransform();
    	mat.scale(1.0, -1.0);
    }

    public ViewPortData(IProjection proj, Extent extent, Dimension2D size) {
        this.proj = proj;
        this.extent = extent;
        this.viewSize = size;
        mat = new AffineTransform();
        mat.scale(1.0, -1.0);
    }

    public IProjection getProjection() {
        return proj;
    }

    public void setProjection(IProjection proj) {
        this.proj = proj;
    }

    public void reProject(ICoordTrans rp) {
        // TODO metodo reProject pendiente de implementar
    }

    public void setCoordSys(IProjection cs) {
        this.cs = cs;
    }

    //public void setCoordTrans(ICoordTrans ct) { this.ct = ct; }
    public AffineTransform getMat() {
        return mat;
    }

    public void setMat(AffineTransform mat) {
        this.mat = mat;
    }

    public Object clone() {
        ViewPortData vp = new ViewPortData();

        if (mat != null) {
            vp.mat = new AffineTransform(mat);
        }

        if (extent != null) {
            vp.extent = new Extent(extent);
        }

        vp.proj = proj;
        vp.viewSize = viewSize;
        vp.dpi = dpi;

        return vp;
    }

    /**
     * Obtiene el ancho de la vista
     * @return
     */
    public double getWidth() {
        return viewSize.getWidth();
    }

    /**
     * Obtiene el alto de la vista
     * @return
     */
    public double getHeight() {
        return viewSize.getHeight();
    }

    /**
     * Obtiene el tamaño de la vista
     */
    public Dimension2D getSize() {
        return viewSize;
    }

    /**
     * Asigna el ancho y alto de la vista
     * @param w Ancho
     * @param h Alto
     */
    public void setSize(double w, double h) {
        setSize(new Dimension((int) w, (int) h));
    }
    
    public void setSize(Dimension2D sz) {
    	viewSize = sz;
        reExtent();
    }

    public Extent getExtent() {
        return extent;
    }

    public void setExtent(Dimension2D sz) {
        Point2D.Double pt0 = new Point2D.Double(0, 0);
        Point2D.Double ptSz = new Point2D.Double(sz.getWidth(), sz.getHeight());

        try {
            mat.inverseTransform(pt0, pt0);
            mat.inverseTransform(ptSz, ptSz);
        } catch (Exception e) {
            e.printStackTrace();
        }

        extent = new Extent(pt0, ptSz);
    }
    
    /**
     * Asigna la extensión del raster
     * @param ext Extent
     */
    public void setExtent(Extent ext) {
    	extent = ext;
    }

    public void reExtent() {
        setExtent(viewSize);
    }

    public void setDPI(int dpi) {
        this.dpi = dpi;
    }

    public int getDPI() {
        return this.dpi;
    }

    /**
     * zoom a un marco.
     *
     * @param extent
     */
    public void zoom(Extent extent) {
        double[] scale = extent.getScale(getWidth(), getHeight());
        double escala = Math.min(scale[0], scale[1]);

        mat.setToIdentity();
        mat.scale(escala, -escala);
        mat.translate(-extent.minX(), -extent.maxY());
        this.extent = extent;
        reExtent();
    }

    /**
     * zoom centrado en un punto.
     *
     * @param zoom
     * @param pt
     */
    public void zoom(double zoom, Point2D pt) {
        zoom(zoom, zoom, pt);
    }

    public void zoom(double zx, double zy, Point2D pt) {
        centerAt(pt);
        mat.scale(zx, zy);
        centerAt(pt);
        reExtent();
    }

    /**
     * Zoom a una escala (geográfica);
     *
     * @param scale
     */
    public void zoomToCenter(double f) {
        Point2D.Double ptCenter = new Point2D.Double(getWidth() / 2.0,
                                                     getHeight() / 2.0);

        try {
            mat.inverseTransform(ptCenter, ptCenter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        zoom(f, ptCenter);
    }

    /**
     * Centrar en un punto.
     *
     * @param pt
     */
    public void centerAt(Point2D pt) {
        Point2D.Double ptCenter = new Point2D.Double(getWidth() / 2.0,
                                                     getHeight() / 2.0);

        try {
            mat.inverseTransform(ptCenter, ptCenter);
            mat.translate(ptCenter.x - pt.getX(), ptCenter.y - pt.getY());
        } catch (Exception e) {
            e.printStackTrace();
        }

        reExtent();
    }

    /**
     * Desplaza la vista actual.
     *
     * @param pt
     */
    public void pan(Point2D ptIni, Point2D ptFin) {
        mat.translate(ptFin.getX() - ptIni.getX(), ptFin.getY() - ptIni.getY());
        reExtent();
    }

    public Point2D getCenter() {
        Point2D.Double ptCenter = new Point2D.Double(getWidth() / 2.0,
                                                     getHeight() / 2.0);

        try {
            mat.inverseTransform(ptCenter, ptCenter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ptCenter;
    }
    
    /**
     * Calcula la transformación afín.
     */
	public void calculateAffineTransform() {
		if ((viewSize == null) || (extent == null) ||
			(viewSize.getWidth() <= 0) || (viewSize.getHeight() <= 0)) 
			return;

		AffineTransform escalado = new AffineTransform();
		AffineTransform translacion = new AffineTransform();

		double escalaX;
		double escalaY;

		escalaX = viewSize.getWidth() / extent.width();
		escalaY = viewSize.getHeight() / extent.height();

		double xCenter = extent.getCenterX();
		double yCenter = extent.getCenterY();
		double newHeight;
		double newWidth;

		Rectangle2D adjustedExtent = new Rectangle2D.Double();
		double scale = 0;
		
		if (escalaX < escalaY) {
			scale = escalaX;
			newHeight = viewSize.getHeight() / scale;
			adjustedExtent.setRect(xCenter - (extent.width() / 2.0),
					yCenter - (newHeight / 2.0), extent.width(), newHeight);
		} else {
			scale = escalaY;
			newWidth = viewSize.getWidth() / scale;
			adjustedExtent.setRect(xCenter - (newWidth / 2.0),
					yCenter - (extent.height() / 2.0), newWidth,
					extent.height());
		}
		escalado.setToScale(scale, -scale);
		translacion.setToTranslation(-extent.getULX(), -extent.getULY());

		mat.setToIdentity();
		mat.concatenate(escalado);
		mat.concatenate(translacion);
	}
}
