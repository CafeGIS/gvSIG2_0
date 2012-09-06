/*
 * Cresques Mapping Suite. Graphic Library for constructing mapping applications.
 *
 * Copyright (C) 2004-6.
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
 *
 * For more information, contact:
 *
 * cresques@gmail.com
 */
package org.cresques.impl.cts.gt2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IDatum;
import org.cresques.cts.IProjection;
import org.cresques.geo.ViewPortData;
import org.geotools.cs.CoordinateSystem;
import org.geotools.cs.CoordinateSystemFactory;
import org.geotools.cs.GeographicCoordinateSystem;
import org.geotools.cs.ProjectedCoordinateSystem;
import org.opengis.referencing.FactoryException;


/**
 * Sistema de Coordenadas (Proyección).
 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>
 */
public class CoordSys implements IProjection {
    private static final Color basicGridColor = new Color(64, 64, 64, 128);
    protected CoordinateSystemFactory csFactory = CoordinateSystemFactory.getDefault();
    protected CSDatum datum = null;
    protected GeographicCoordinateSystem geogCS = null;
    protected ProjectedCoordinateSystem projCS = null;
    protected String abrev = "";
    Color gridColor = basicGridColor;

    public CoordSys(CSDatum datum) {
        this.datum = datum;
        this.geogCS = new GeographicCoordinateSystem(datum.getName(null), datum.getDatum());
	}

    public CoordSys(String wkt) {
    	try {
        	//((GeographicCoordinateSystem)
    		CoordinateSystem cs = CoordinateSystemFactory.getDefault().createFromWKT(wkt);
//			 ).getHorizontalDatum();
    		if (cs instanceof GeographicCoordinateSystem)
    			geogCS = (GeographicCoordinateSystem) cs;
    		if (cs instanceof ProjectedCoordinateSystem) {
    			projCS = (ProjectedCoordinateSystem) cs;
    			geogCS = projCS.getGeographicCoordinateSystem();
    		}
    		datum = new CSDatum(geogCS.getHorizontalDatum());
    	} catch (FactoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /**
     * Crea un nuevo CoordSys geográfico a partir de uno proyectado.
     * Si el actual es geográfico retorna el mismo.
     * @return
     */
    public CoordSys toGeo() {
        CoordSys coordSys = new CoordSys((CSDatum) getDatum());

        if (geogCS != null) {
            coordSys.geogCS = this.geogCS;
        } else {
            coordSys.geogCS = projCS.getGeographicCoordinateSystem();
        }

        return coordSys;
    }

    public IDatum getDatum() {
        return datum;
    }

    public CoordinateSystem getCS() {
        if (projCS != null) {
            return projCS;
        }

        return geogCS;
    }

    public CoordinateSystem getGeogCS() {
        if (geogCS != null) {
            return geogCS;
        }

        return projCS.getGeographicCoordinateSystem();
    }

    /* (no Javadoc)
     * @see org.cresques.cts.IProjection#createPoint(double, double)
     */
    public Point2D createPoint(double x, double y) {
        return new Point2D.Double(x, y);
    }

    public String toString() {
        if (projCS != null) {
            return projCS.toString();
        }

        return geogCS.toString();
    }

    public void setAbrev(String abrev) {
        this.abrev = abrev;
    }

    public String getAbrev() {
        return abrev;
    }

    /* (no Javadoc)
     * @see org.cresques.cts.IProjection#drawGrid(java.awt.Graphics2D, org.cresques.geo.ViewPortData)
     */
    public void drawGrid(Graphics2D g, ViewPortData vp) {
        // TODO Apéndice de método generado automáticamente
    }

    public void setGridColor(Color c) {
        gridColor = c;
    }

    public Color getGridColor() {
        return gridColor;
    }

    /* (no Javadoc)
     * @see org.cresques.cts.IProjection#toGeo(java.awt.geom.Point2D)
     */
    public Point2D toGeo(Point2D pt) {
    //TODO VCN Esto si no lo comento no me trasforma el punto en coordenadas geográficas.
//        if (getGeogCS() == geogCS) {
//            return pt;
//        } else {
            CoordTrans ct = new CoordTrans(this, toGeo());

            return ct.convert(pt, null);
//        }
    }

    /* (no Javadoc)
     * @see org.cresques.cts.IProjection#fromGeo(java.awt.geom.Point2D, java.awt.geom.Point2D)
     */
    public Point2D fromGeo(Point2D gPt, Point2D mPt) {
        // TODO Apéndice de método generado automáticamente
        return null;
    }

    public double getScale(double minX, double maxX, double w, double dpi) {
        double scale = 0D;

        if (projCS == null) { // Es geográfico; calcula la escala.
            scale = ((maxX - minX) * // grados

            // 1852.0 metros x minuto de meridiano
            (dpi / 2.54 * 100.0 * 1852.0 * 60.0)) / // px / metro
                    w; // pixels
        }

        return scale;
    }
    public Rectangle2D getExtent(Rectangle2D extent,double scale,double wImage,double hImage,double mapUnits,double distanceUnits,double dpi) {
    	double w =0;
		double h =0;
		double wExtent =0;
		double hExtent =0;
    	if (projCS!=null) {
			w = ((wImage / dpi) * 2.54);
			h = ((hImage / dpi) * 2.54);
			wExtent =w * scale*distanceUnits/ mapUnits;
			hExtent =h * scale*distanceUnits/ mapUnits;

		}else {
			w = ((wImage / dpi) * 2.54);
			h = ((hImage / dpi) * 2.54);
			wExtent =(w*scale*distanceUnits)/ (mapUnits*1852.0*60.0);
			hExtent =(h*scale*distanceUnits)/ (mapUnits*1852.0*60.0);
		}
    	double xExtent = extent.getCenterX() - wExtent/2;
		double yExtent = extent.getCenterY() - hExtent/2;
		Rectangle2D rec=new Rectangle2D.Double(xExtent,yExtent,wExtent,hExtent);
    	return  rec;
    }
    public boolean isProjected() {
    	return projCS != null;
    }

	public ICoordTrans getCT(IProjection dest) {
		return new CoordTrans(this, (CoordSys) dest);
	}

	public String getFullCode() {
		return getAbrev();
	}
}
