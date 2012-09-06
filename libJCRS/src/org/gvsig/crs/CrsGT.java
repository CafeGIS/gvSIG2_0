/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 Instituto de Desarrollo Regional and Generalitat Valenciana.
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
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   Instituto de Desarrollo Regional (Universidad de Castilla La-Mancha)
 *   Campus Universitario s/n
 *   02071 Alabacete
 *   Spain
 *
 *   +34 967 599 200
 */

package org.gvsig.crs;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IDatum;
import org.cresques.cts.IProjection;
import org.cresques.geo.ViewPortData;
import org.geotools.referencing.crs.AbstractDerivedCRS;
import org.geotools.referencing.crs.AbstractSingleCRS;
import org.geotools.referencing.datum.DefaultGeodeticDatum;
import org.gvsig.crs.proj.CrsProj;
import org.gvsig.crs.proj.CrsProjException;
import org.gvsig.crs.proj.JNIBaseCrs;
import org.gvsig.crs.proj.OperationCrsException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Clase que representa un CRS basado en GeoTools/GeoApi.
 * 
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 *
 */
public class CrsGT implements ICrs {
	private static final Color basicGridColor = new Color(64, 64, 64, 128);
	
	/**
	 * CRS GeoApi. Nucleo de la clare CrsGT.
	 */
	private CoordinateReferenceSystem 	crsGT			= null;
	
	/**
	 *Cadena proj4
	 */
	private String 						proj4String 	= null;
	
	private Color 						gridColor 		= basicGridColor;
	
	/**
	 * Parámetros de transformación para el CRS fuente en formato proj4.
	 */
	private String 						sourceTrParams			= null;
	
	/**
	 * Parámetros de transformación para el CRS destino en formato proj4.
	 */
	private String 						targetTrParams			= null;
	
	/**
	 * CRS operable con proj4.
	 */
	private CrsProj 					crsProj			= null;
	
	/**
	 * CRS Base operable con proj4.
	 */
	private CrsProj 					crsProjBase		= null;
	
	/**
	 * Provisional, hasta que se elimine getCrsWkt de ICrs.
	 */
	private CrsWkt 						crsWkt			= null;
	
	/**
	 * Conversor de CRSs a cadenas proj4.
	 */
	private Proj4 						proj4			= null;
	
	
	
	/**
	 * Constructor a partir de un CoordinateReferenceSystem
	 * 
	 * @param crsGT
	 * @throws CrsProjException 
	 */
	public CrsGT(CoordinateReferenceSystem crsGT){
		this.crsGT = crsGT;
	}

	public int getCode() {
		return Integer.valueOf(getAbrev().split(":")[1]).intValue();
	}

	public CrsWkt getCrsWkt() {
		if (crsWkt==null)
			crsWkt = new CrsWkt(crsGT);
		return crsWkt;
	}

	public String getWKT() {
		return crsGT.toWKT();
	}

	public void setTransformationParams(String SourceParams, String TargetParams) {
		this.sourceTrParams = SourceParams;
		this.targetTrParams = TargetParams;
	}
	
	/**
	 * Devuelve los parametros de la transformacion del crs fuente
	 * @return
	 */
	public String getSourceTransformationParams() {
		return this.sourceTrParams;
	}
	
	/**
	 * Devuelve los parametros de la transformacion del crs destino
	 * @return
	 */
	public String getTargetTransformationParams() {
		return this.targetTrParams;
	}

	public Point2D createPoint(double x, double y) {
		return new Point2D.Double(x,y);
	}

	public void drawGrid(Graphics2D g, ViewPortData vp) {
		// TODO Auto-generated method stub

	}

	public Point2D fromGeo(Point2D gPt, Point2D mPt) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAbrev() {		
		return ((AbstractSingleCRS)crsGT).getIdentifiers().iterator().next().toString();
	}

	public ICoordTrans getCT(IProjection dest) {
		
		try {
			if (dest == this)
				return null;
			COperation operation = null;
			if(((ICrs)dest).getSourceTransformationParams() != null || ((ICrs)dest).getTargetTransformationParams() != null) 
				operation = new COperation(this, (ICrs)dest,((ICrs)dest).getTargetTransformationParams(),((ICrs)dest).getSourceTransformationParams());
			else
				operation = new COperation(this, (ICrs)dest,sourceTrParams,targetTrParams);
			return operation;
		}catch (CrsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		/*
		try {
			operation = new COperation(this, (ICrs)dest);
		} catch (CrsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (!getTransformationParams().equals("")){
			if (isParamsInTarget())
				operation.setParamsCrsProj(new CrsProj(crsDest.getProj4String()+getTransformationParams()), true);
			else
				operation.setParamsCrsProj(new CrsProj(getProj4String()+getTransformationParams()), false);
			return operation;
		}
		
		return operation;	*/
	}

	public IDatum getDatum() {
		DefaultGeodeticDatum datumGT = (DefaultGeodeticDatum)((AbstractSingleCRS)crsGT).getDatum();
		CRSDatum datum = new CRSDatum(datumGT.getEllipsoid().getSemiMajorAxis(),datumGT.getEllipsoid().getInverseFlattening());
		return datum;
	}

	public Color getGridColor() {
		return gridColor;
	}

	public double getScale(double minX, double maxX, double width, double dpi) {
		double scale = 0D;
        if (!isProjected()) { // Es geográfico; calcula la escala.
            scale = ((maxX - minX) * // grados

            // 1852.0 metros x minuto de meridiano
            (dpi / 2.54 * 100.0 * 1852.0 * 60.0)) / // px / metro
                    width; // pixels
        }
        else{
        	 scale = ((maxX - minX) * // metros
                    (dpi / 2.54 * 100.0)) / // px / metro
                    width; // pixels
        }
        return scale;
	}
	
	public double getScale(double minX, double maxX, double minY, double maxY, double width, double dpi) {
		
		double scale = 0D;
		double incX = (maxX-minX);
		
		if (!isProjected()) {
			double a = getDatum().getESemiMajorAxis();
			double invF = getDatum().getEIFlattening();
			double meanY = (minY+maxY)/2.0;
			double radius = 0.0;
			
			
			if (invF == Double.POSITIVE_INFINITY){
				radius = a;
			}
			else{
				double e2 = 2.0/invF-Math.pow(1.0/invF,2.0);
				radius = a/Math.sqrt(1.0-e2*Math.pow(Math.sin(meanY*Math.PI/180.0),2.0))*Math.cos(meanY*Math.PI/180.0);
			}
			incX *= Math.PI/180.0*radius;
		}
			
		scale = (incX * // metros
				(dpi / 2.54 * 100.0)) / // px / metro
					width; // pixels
		
        return scale;
	}

	public boolean isProjected() {
		if (crsGT instanceof AbstractDerivedCRS){
			return true;
		}else
			return false;
	}

	public void setGridColor(Color c) {
		gridColor = c;
	}

	public Point2D toGeo(Point2D pt) {
		if (isProjected()){
			double x[] = {pt.getX()};
			double y[] = {pt.getY()};
			double z[] = {0D};
			try {
				JNIBaseCrs.operate( x , y, z,
						getCrsProj(),getCrsProjBase());
			} catch (OperationCrsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new Point2D.Double(x[0],y[0]);
		}
		else
			return pt;
	}
	
	/**
	 * @param targetParam
	 */
	/*public void setParamsInTarget(boolean targetParam) {
		this.paramsInTarget = targetParam;
	}*/
	
	/**
	 * 
	 * @return Cadena proj4 Correspondiente al CRS.
	 * @throws CrsException 
	 */
	public String getProj4String() throws CrsException {
		if (proj4String == null)
		proj4String = getProj4().exportToProj4(crsGT);
			
		return proj4String;
	}

	public CoordinateReferenceSystem getCrsGT() {
		return crsGT;
	}
	
	public CrsProj getCrsProj() {
		if (crsProj == null)
			try {
				crsProj = new CrsProj(getProj4String());
			} catch (CrsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		return crsProj;
	}

	private CrsProj getCrsProjBase() {
		if (crsProjBase == null){
			AbstractDerivedCRS derivedCRS = (AbstractDerivedCRS)crsGT;
			try {
				crsProjBase = new CrsProj(getProj4().exportToProj4(derivedCRS.getBaseCRS()));
			} catch (CrsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return crsProjBase;
	}

	private Proj4 getProj4() {
		if (proj4 == null)
			try {
				proj4 = new Proj4();
			} catch (CrsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return proj4;
	}
	
	/**
	 * @return Authority:code:proj@Sourceparam@TargerParam@
	 */
	public String getFullCode() {
		if (this.sourceTrParams == null && this.targetTrParams == null)
			return getAbrev();
		String sourceParams = "";
		String targetParams = "";
		if (sourceTrParams != null)
			sourceParams = sourceTrParams;
		if (targetTrParams != null)
			targetParams = targetTrParams;
		
		return getAbrev()+":proj@"+sourceParams+"@"+targetParams;
	}

    public Rectangle2D getExtent(Rectangle2D extent,double scale,double wImage,double hImage,double mapUnits,double distanceUnits,double dpi) {
    	double w =0;
		double h =0;
		double wExtent =0;
		double hExtent =0;
    	if (isProjected()) {
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

}
