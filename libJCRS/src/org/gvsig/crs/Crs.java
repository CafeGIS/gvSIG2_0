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
import org.gvsig.crs.proj.CrsProj;
import org.gvsig.crs.proj.JNIBaseCrs;
import org.gvsig.crs.proj.OperationCrsException;

/**
 * Clase que construye el CRS a partir de la cadena WKT
 * @author José Luis Gómez Martínez (jolugomar@gmail.com)
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 * @author Miguel García Jiménez (garciajimenez.miguel@gmail.com)
 *
 */
public class Crs implements ICrs {
	private static final Color basicGridColor = new Color(64, 64, 64, 128);
	private Proj4 proj4;
	private String proj4String;
	private String trans;
	//private String transOrigin = "";
	private String abrev;
	private String name = "";
	private CrsProj crsProj;
	private CrsProj crsBase = null;
	private CrsWkt crsWkt;
	private int epsg_code = 23030;
	String sourceTrParams = null;
	String targetTrParams = null;
	String wkt = null;
	Color gridColor = basicGridColor;
	CRSDatum datum = null;	

	public Crs(int epsgCode, int aut) throws CrsException
	{
		String strEpsgCode = "";
		if (aut == 1) {
			strEpsgCode="EPSG:"+epsgCode;			
		} else if (aut == 2){
			strEpsgCode="ESRI:"+epsgCode;
		} else if (aut == 3){
			strEpsgCode="IAU2000:"+epsgCode;
		} else if (aut == 4) {
			strEpsgCode="USR:"+epsgCode;
		} else System.out.println("Error, autorithy erróneo");
		crsWkt=new CrsWkt(strEpsgCode);
		setWKT(crsWkt.getWkt());
	}
	
	
	/**
	 * Construye un CRS a partir del código EPSG o de la cadena WKT.
	 * 
	 * @param code
	 * @throws CrsException
	 */
	public Crs(String code) throws CrsException {
		String fullCode;
		setWKT(code);
		if(code.charAt(0) == 'E' || code.charAt(0) == 'G' || code.charAt(0) == 'P')
			fullCode = code;
		else fullCode = "EPSG:"+code;
		String cod = "";
		if(code.length() < 15 ) {
			code = code.substring(code.indexOf(":")+1);
			try {
				//Creamos el objeto que tendra los diferentes parametros de la cadena Wkt
				crsWkt = new CrsWkt(fullCode);				
				setName(fullCode);
				setAbrev(fullCode);
				
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			String aux;
			String code2 = "";
			for(int i = 0; i < code.length(); i++) {
				aux = ""+code.charAt(i);
				if(!aux.equals(" ")) {
					code2+=aux;
				}else {
					code2 += "";
				}
			}
			crsWkt = new CrsWkt(code2);
	    	    		
    		setName(fullCode);
    		//setAbrev(crsWkt.getName());
    		setAbrev(crsWkt.getAuthority()[0]+":"+crsWkt.getAuthority()[1]);	    	
	    	
    	}
		//	Asignar el datum y el crs base (en el caso de ser projectado):
		if (!(crsWkt.getSpheroid()[1].equals("")||crsWkt.getSpheroid()[2].equals(""))){
			double eSemiMajorAxis = Double.valueOf(crsWkt.getSpheroid()[1]).doubleValue();
			double eIFlattening = Double.valueOf(crsWkt.getSpheroid()[2]).doubleValue();
			datum = new CRSDatum(eSemiMajorAxis,eIFlattening);
			
			//crs base (en el caso de ser projectado):
			/*if(this.isProjected()){
				String proj4Base = "+proj=latlong +a=" + crsWkt.getSpheroid()[1] + " +rf=" + crsWkt.getSpheroid()[2] ;
				crsBase = new CrsProj(proj4Base);
			}*/
		}
	}	
	
	/**
	 *Construye un CRS a partir del código EPSG o de la cadena WKT.
	 * 
	 * @param epsg_cod
	 * @param code
	 * @throws CrsException
	 */
	public Crs(int epsg_cod, String code) throws CrsException {
		String fullCode;
		setWKT(code);
		setCode(epsg_cod);
		if(code != null || code.charAt(0) == 'E' || code.charAt(0) == 'G' || code.charAt(0) == 'P')
			fullCode = code;
		else fullCode = "EPSG:"+code;
		String cod = "";
		if(code.length() < 15 ) {
			code = code.substring(code.indexOf(":")+1);
			try {
				//Creamos el objeto que tendra los diferentes parametros de la cadena Wkt
				crsWkt = new CrsWkt(fullCode);				
				setName(fullCode);
				setAbrev(fullCode);
				
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			String aux;
			String code2 = "";
			for(int i = 0; i < code.length(); i++) {
				aux = ""+code.charAt(i);
				if(!aux.equals(" ")) {
					code2+=aux;
				}else {
					code2 += "";
				}
			}
			crsWkt = new CrsWkt(code2);
			/*
			 * Arreglo temporal para ver si funcionan de la iau2000 las proyecciones
			 * cilindrica equidistante y oblicua cilindrica equidistante
			 * que no estan en gdal, por lo que asignaremos directamente su cadena
			 * en proj4 para trabajar con ellas
			 */
			if (!crsWkt.getProjection().equals("Equidistant_Cylindrical") && !crsWkt.getProjection().equals("Oblique_Cylindrical_Equal_Area")){		    		    		
	    		setName(fullCode);
	    		//setAbrev(crsWkt.getName());
	    		setAbrev(crsWkt.getAuthority()[0]+":"+crsWkt.getAuthority()[1]);		    	 
			}
			else if (crsWkt.getProjection().equals("Equidistant_Cylindrical")){
				String spheroid1 = crsWkt.getSpheroid()[1];
				String spheroid2 = crsWkt.getSpheroid()[2];
				String centralMeridian = "";
				String falseNorthing = "";
				String falseEasting = "";
				String standardParallel1 = "0.0";
				for (int i=0; i<crsWkt.getParam_name().length;i++){
					if (crsWkt.getParam_name()[i].equals("Central_Meridian"))
						centralMeridian = crsWkt.getParam_value()[i];	
					if (crsWkt.getParam_name()[i].equals("False_Easting"))
						falseEasting = crsWkt.getParam_value()[i];					
					if(crsWkt.getParam_name()[i].equals("False_Northing"))
						falseNorthing = crsWkt.getParam_value()[i];					
					if (crsWkt.getParam_name()[i].equals("Standard_Parallel_1"))
						standardParallel1 = crsWkt.getParam_value()[i];					
				}
				if (spheroid2.equals("0.0")){
					proj4String = "+proj=eqc +a=" + spheroid1 +
					" +lon_0="+ centralMeridian + " +x_0="+falseEasting+" +y_0="+falseNorthing +
					" +lat_ts="+standardParallel1;
				} else {
					proj4String = "+proj=eqc +a="+ spheroid1 +" +rf=" + spheroid2 +
					" +lon_0="+ centralMeridian + " +x_0="+falseEasting+" +y_0="+falseNorthing +
					" +lat_ts="+standardParallel1;
				}				
				setName(fullCode);
	    		//setAbrev(crsWkt.getName());
	    		setAbrev(crsWkt.getAuthority()[0]+":"+crsWkt.getAuthority()[1]);
				
			}
			else if (crsWkt.getProjection().equals("Oblique_Cylindrical_Equal_Area")){
				String spheroid1 = crsWkt.getSpheroid()[1];
				String spheroid2 = crsWkt.getSpheroid()[2];
				String centralMeridian = "";
				String falseNorthing = "";
				String falseEasting = "";
				String standardParallel1 = "";
				String standardParallel2 = "0.0";
				for (int i=0; i<crsWkt.getParam_name().length;i++){					
					if (crsWkt.getParam_name()[i].equals("Central_Meridian"))
						centralMeridian = crsWkt.getParam_value()[i];	
					if (crsWkt.getParam_name()[i].equals("False_Easting"))
						falseEasting = crsWkt.getParam_value()[i];					
					if(crsWkt.getParam_name()[i].equals("False_Northing"))
						falseNorthing = crsWkt.getParam_value()[i];					
					if (crsWkt.getParam_name()[i].equals("Standard_Parallel_1"))
						standardParallel1 = crsWkt.getParam_value()[i];
					if (crsWkt.getParam_name()[i].equals("Standard_Parallel_2"))
						standardParallel2 = crsWkt.getParam_value()[i];		
				}
				if (spheroid2.equals("0.0")){
					proj4String = "+proj=ocea +a="+ spheroid1  +
					" +lon_0="+ centralMeridian + " +x_0="+falseEasting+" +y_0="+falseNorthing +
					" +lat_1="+standardParallel1+" +lat_2="+standardParallel2+" +lon_1=long_1" +
					" +lon_2=long_2 +no_defs";
				} else {
					proj4String = "+proj=ocea +a="+ spheroid1 + " +rf=" + spheroid2 +
					" +lon_0="+ centralMeridian + " +x_0="+falseEasting+" +y_0="+falseNorthing +
					" +lat_1="+standardParallel1+" +lat_2="+standardParallel2+" +lon_1=long_1" +
					" +lon_2=long_2 +no_defs";
				}				
				setName(fullCode);
	    		//setAbrev(crsWkt.getName());
	    		setAbrev(crsWkt.getAuthority()[0]+":"+crsWkt.getAuthority()[1]);
			}
		}
		//	Asignar el datum:
		if (!(crsWkt.getSpheroid()[1].equals("")||crsWkt.getSpheroid()[2].equals(""))){
			double eSemiMajorAxis = Double.valueOf(crsWkt.getSpheroid()[1]).doubleValue();
			double eIFlattening = Double.valueOf(crsWkt.getSpheroid()[2]).doubleValue();
			datum = new CRSDatum(eSemiMajorAxis,eIFlattening);
			
			// Crs base (en el caso de ser projectado):
			/*if(this.isProjected()){
				String proj4Base = "+proj=latlong +a=" + crsWkt.getSpheroid()[1] + " +rf=" + crsWkt.getSpheroid()[2] ;
				crsBase = new CrsProj(proj4Base);
			}*/
		}
	}
	
	/**
	 * Construye un CRS a partir del código EPSG o de la cadena WKT.
	 * En el caso de WKT le añade a la cadena proj4 el string params.
	 * 
	 * @param epsg_cod
	 * @param code
	 * @param params
	 * @throws CrsException
	 */
	public Crs(int epsg_cod, String code,String params) throws CrsException {
		String fullCode;
		setCode(epsg_cod);
		setWKT(code);
		if(code.charAt(0) == 'E' || code.charAt(0) == 'G' || code.charAt(0) == 'P')
			fullCode = code;
		else fullCode = "EPSG:"+code;
		String cod = "";
		if(code.length() < 15 ) {
			code = code.substring(code.indexOf(":")+1);
			try {
				//Creamos el objeto que tendra los diferentes parametros de la cadena Wkt
				crsWkt = new CrsWkt(fullCode);
				
				setName(fullCode);
				setAbrev(fullCode);
				
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			String aux;
			String code2 = "";
			for(int i = 0; i < code.length(); i++) {
				aux = ""+code.charAt(i);
				if(!aux.equals(" ")) {
					code2+=aux;
				}else {
					code2 += "";
				}
			}
			crsWkt = new CrsWkt(code2);
	    	
    		setName(fullCode);
    		//setAbrev(crsWkt.getName());
    		setAbrev(crsWkt.getAuthority()[0]+":"+crsWkt.getAuthority()[1]);
	    	
		}
		//	Asignar el datum:
		if (!(crsWkt.getSpheroid()[1].equals("")||crsWkt.getSpheroid()[2].equals(""))){
			double eSemiMajorAxis = Double.valueOf(crsWkt.getSpheroid()[1]).doubleValue();
			double eIFlattening = Double.valueOf(crsWkt.getSpheroid()[2]).doubleValue();
			datum = new CRSDatum(eSemiMajorAxis,eIFlattening);
			
			// Crs base (en el caso de ser projectado):
			/*if(this.isProjected()){
				String proj4Base = "+proj=latlong +a=" + crsWkt.getSpheroid()[1] + " +rf=" + crsWkt.getSpheroid()[2] ;
				crsBase = new CrsProj(proj4Base);
			}*/
		}
	}
		
/*	public Crs(CrsEpsg source) throws CrsException {
		crsProj4 = source;
	}*/
	
	/**
	 * @param code 
	 */
	public void setTrans(String code) {
		trans = code;
		changeTrans(trans);
	}
	
	/**
	 * 
	 * @param code
	 */
	public void changeTrans(String code) {
		getCrsProj().changeStrCrs(code);
	}
	
	/**
	 * 
	 */
	public String getAbrev() {
		return abrev;
	}
	
	/**
	 * 
	 * @param code
	 */
	protected void setAbrev(String code) {
		abrev = code;
	}
	
	/**
	 * 
	 * @param nom
	 */
	public void setName(String nom) {
		name = nom;
	}
	
	/**
	 * @return
	 */
	public IDatum getDatum() {
		
		return datum;
	}
	
	/**
	 * @return
	 */
	public CrsWkt getCrsWkt() {
		return crsWkt;
	}
	
	/**
	 * 
	 * @param wkt
	 */
	public void setWKT(String wkt){
		this.wkt = wkt;
	}
	
	/**
	 * @return
	 */
	public String getWKT(){
		return this.wkt;
	}
	
	/**
	 * 
	 * @param params
	 */
	public void setTransformationParams(String sourceParams, String targetParams){
		this.sourceTrParams = sourceParams;
		this.targetTrParams = targetParams;
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
	
	/**
	 * @return
	 */
	public String getTransformationParams(){
		return this.sourceTrParams;
	}
		
	/**
	 * 
	 * @return
	 */
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

	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public Point2D createPoint(double x, double y) {
		return new Point2D.Double(x,y);
	}

	/**
	 * @param g
	 * @param vp
	 */
	public void drawGrid(Graphics2D g, ViewPortData vp) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param c
	 */
	public void setGridColor(Color c) {
		gridColor = c;
	}

	/**
	 * @return
	 */
	public Color getGridColor() {
		return gridColor;
	}

	/**
	 * @param dest
	 * @return
	 */
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
		
		if (getTransformationParams()!=null){
			if (isTargetParams())
				operation.setParamsCrsProj(new CrsProj(crsDest.getProj4String()+getTransformationParams()), true);
			else
				operation.setParamsCrsProj(new CrsProj(getProj4String()+getTransformationParams()), false);
			
			return operation;
		}
		
		return operation;	*/	
	}
	
	/**
	 * @param pt
	 * @return
	 */
	public Point2D toGeo(Point2D pt) {
		if (isProjected()){
			double x[] = {pt.getX()};
			double y[] = {pt.getY()};
			double z[] = {0D};
			try {
				JNIBaseCrs.operate( x , y, z,
						getCrsProj(),crsBase);
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
	 * @param gPt
	 * @param mPt
	 * @return
	 */
	public Point2D fromGeo(Point2D gPt, Point2D mPt) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return
	 */
	public boolean isProjected() {
		return !getCrsProj().isLatlong();		
	}

	/**
	 * @param minX
	 * @param maxX
	 * @param width
	 * @param dpi
	 * @return
	 */
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
	
	/**
	 * 
	 * @param epsg_cod
	 */
	public void setCode(int epsg_cod){
		epsg_code = epsg_cod;
	}

	/**
	 * @return
	 */
	public int getCode() {
		// TODO Auto-generated method stub
		return epsg_code;
	}

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
	 * 
	 * @return Cadena proj4 Correspondiente al CRS.
	 * @throws CrsException 
	 */
	public String getProj4String() throws CrsException {
		if (proj4String == null)
			proj4String = getProj4().exportToProj4(this);			
		return proj4String;
	}
}
