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

import java.util.ArrayList;

import javax.units.ConversionException;
import javax.units.Unit;

import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.AbstractSingleCRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.crs.DefaultProjectedCRS;
import org.geotools.referencing.datum.DefaultEllipsoid;
import org.geotools.referencing.datum.DefaultGeodeticDatum;
import org.geotools.referencing.datum.DefaultPrimeMeridian;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PrimeMeridian;

/**
 * Clase que parsea una cadena wkt y consigue todos sus parámetros
 * de manera independiente para poder ser utilizados
 * en distintas funciones
 * 
 * @author José Luis Gómez Martínez (JoseLuis.Gomez@uclm.es)
 *
 */

public class CrsWkt {
	private String wkt;
	private String projcs = "";
	private String geogcs = "";
	private String datum = "";
	private String[] spheroid = {"" ,"",""};
	private String[] primem = {"",""};
	private String[] unit = {"",""};
	private String[] unit_p = {"",""}; //unidades de la projeccion (final de la cadena wkt)
	private String projection = "";
	private String[] param_name;
	private String[] param_value;
	private int contador = 0;
	private String[] authority = {"",""};
	
	int divider=10000;
	
	/**
     * Small tolerance factor for rounding errors.
     */
    private static final double EPS = 1E-8;
	
	public CrsWkt(String wkt_spaces) {
		String aux;
		String wkt = "";
		for(int i = 0; i < wkt_spaces.length(); i++) {
			aux = ""+wkt_spaces.charAt(i);
			if(!aux.equals(" ")) {
				wkt+=aux;
			}else {
				wkt += "";
			}
		}
		if (wkt.length()>15) {
			fromWKT(wkt, false);
		} else {
			fromCode(wkt);
		}
	}
	
	public CrsWkt(CoordinateReferenceSystem crsGT) {
		AbstractSingleCRS crs = (AbstractSingleCRS)crsGT;
		String authority = crs.getName().toString().split(":")[0];
		setAuthority(((AbstractSingleCRS)crsGT).getIdentifiers().iterator().next().toString());
		setWkt(crsGT.toWKT());
		
		if (crsGT instanceof DefaultProjectedCRS) {
			DefaultProjectedCRS sour = (DefaultProjectedCRS) crsGT;
			setProjcs(sour.getName().toString().split(":")[1]);
			String[] val = sour.getBaseCRS().getName().toString().split(":");
			if (val.length<2)
				setGeogcs(sour.getBaseCRS().getName().toString().split(":")[0]);
			else
				setGeogcs(sour.getBaseCRS().getName().toString().split(":")[1]);
			DefaultGeodeticDatum d = (DefaultGeodeticDatum) sour.getDatum();
			val = d.getName().toString().split(":");
			if (val.length<2)
				setDatumName(d.getName().toString().split(":")[0]);
			else
				setDatumName(d.getName().toString().split(":")[1]);
			setSpheroid((DefaultEllipsoid)d.getEllipsoid());				
			setPrimen(d.getPrimeMeridian());
			val = sour.getConversionFromBase().getMethod().getName().toString().split(":");
			if (val.length<2)
				setProjection(sour.getConversionFromBase().getMethod().getName().toString().split(":")[0]);
			else
				setProjection(sour.getConversionFromBase().getMethod().getName().toString().split(":")[1]);
			String str;
			param_name = new String[sour.getConversionFromBase().getParameterValues().values().size()];
			param_value= new String[sour.getConversionFromBase().getParameterValues().values().size()];
			for (int i=0; i< sour.getConversionFromBase().getParameterValues().values().size();i++) {
				str = sour.getConversionFromBase().getParameterValues().values().get(i).toString();
				Unit u = sour.getConversionFromBase().getParameterValues().parameter(str.split("=")[0]).getUnit();
				double value = sour.getConversionFromBase().getParameterValues().parameter(str.split("=")[0]).doubleValue();
				value = convert(value, u.toString());
				param_name[i] = str.split("=")[0];
				param_value [i] = String.valueOf(value);
			}
			
		}
		
		if (crsGT instanceof DefaultGeographicCRS) {
			DefaultGeographicCRS sour = (DefaultGeographicCRS) crsGT;
			String[] val = sour.getName().toString().split(":");
			if (val.length<2)
				setGeogcs(sour.getName().toString().split(":")[0]);
			else
				setGeogcs(sour.getName().toString().split(":")[1]);
			
			DefaultGeodeticDatum d = (DefaultGeodeticDatum) sour.getDatum();
			val = d.getName().toString().split(":");
			if (val.length<2)
				setDatumName(d.getName().toString().split(":")[0]);
			else
				setDatumName(d.getName().toString().split(":")[1]);
			setSpheroid((DefaultEllipsoid)d.getEllipsoid());				
			setPrimen(d.getPrimeMeridian());
			
		} 
	}
	
	/**
	 * Acepta la cadena wkt y si es un CRS proyectado para conseguir el parser
	 * de los dos modelos de cadena wkt que se utilizan, una cuando es base
	 * y otra cuando es proyectado
	 * @param wkt
	 * @param isProj
	 */
	private void fromWKT(String wkt, boolean isProj) {
		
		String res = new String();
		if(!wkt.startsWith("EPSG:")) {
			res = "";
			for(; wkt.charAt(contador) != '"'; contador++) {
				res += wkt.charAt(contador);
			}
			if(res.equals("GEOGCS[")) {
				
				contador++;
				for(; wkt.charAt(contador) != '"'; contador++) {
					geogcs += wkt.charAt(contador);
				}
				
				res = "";
				contador++;
				for(; wkt.charAt(contador) != '"'; contador++) {
					res += wkt.charAt(contador);
				}
				if(res.equals(",DATUM[")) {
					contador++;
					for(; wkt.charAt(contador) != '"'; contador++) {
						datum += wkt.charAt(contador);
					}
				}
				res = "";
				contador++;
				for(; wkt.charAt(contador) != '"'; contador++) {
					res += wkt.charAt(contador);
				}
				if(res.equals(",SPHEROID[")) {
					contador++;
					for(; wkt.charAt(contador) != ']'; contador++) {
						while(wkt.charAt(contador) != '"' ) {
							spheroid[0] += wkt.charAt(contador);
							contador++;
						}
						contador +=2;
						while(wkt.charAt(contador) != ',') {
							spheroid[1] += wkt.charAt(contador);
							contador++;
						}
						contador++;
						while(wkt.charAt(contador) != ']') {
							spheroid[2] += wkt.charAt(contador);
							contador++;
						}
					}
				}
				contador++;
				res = "";
				for(; wkt.charAt(contador) != '"'; contador++) {
					res += wkt.charAt(contador);
				}
				if(res.equals(",PRIMEM[")) {
					contador++;
					for(; wkt.charAt(contador) != ','; contador++) {
						while(wkt.charAt(contador) != '"' ){
							primem[0] += wkt.charAt(contador);
							contador ++;
						}
						contador +=2;
						while(wkt.charAt(contador) != ']') {
							primem[1] += wkt.charAt(contador);
							contador ++;
						}
					}
				}
				contador++;
				res = "";
				for(; wkt.charAt(contador) != '"'; contador++) {
					res += wkt.charAt(contador);
				}
				if(res.equals("UNIT[")) {
					contador++;
					for(; wkt.charAt(contador) != ','; contador++) {
						while(wkt.charAt(contador) != '"' ){
							unit[0] += wkt.charAt(contador);
							contador ++;
						}
						contador +=2;
						while(wkt.charAt(contador) != ']') {
							unit[1] += wkt.charAt(contador);
							contador ++;
						}
					}
				}
				contador++;
				res = "";
				for(; wkt.charAt(contador) != '"'; contador++) {
					res += wkt.charAt(contador);
				}
				if(res.equals("AUTHORITY[")) {
					contador++;
					for(; wkt.charAt(contador) != ']'; contador++) {
						while(wkt.charAt(contador) != '"' ){
							authority[0] += wkt.charAt(contador);
							contador ++;
						}
						contador +=2;
						while(wkt.charAt(contador) != ']') {
							authority[1] += wkt.charAt(contador);
							contador ++;
						}
					}
				}
			}else if (res.equals("PROJCS[")) {
				contador++;
				for(; wkt.charAt(contador) != '"'; contador++) {
					projcs += wkt.charAt(contador);
				}
				contador++;
				res = "";
				for(; wkt.charAt(contador) != '"'; contador++) {
					res += wkt.charAt(contador);
				}
				if(res.equals(",GEOGCS[")) {
					contador++;
					for(; wkt.charAt(contador) != '"'; contador++) {
						geogcs += wkt.charAt(contador);
					}
				}
								
				res = "";
				contador++;
				for(; wkt.charAt(contador) != '"'; contador++) {
					res += wkt.charAt(contador);
				}
				if(res.equals(",DATUM[")) {
					contador++;
					for(; wkt.charAt(contador) != '"'; contador++) {
						datum += wkt.charAt(contador);
					}
				}
				res = "";
				contador++;
				for(; wkt.charAt(contador) != '"'; contador++) {
					res += wkt.charAt(contador);
				}
				if(res.equals(",SPHEROID[")) {
					contador++;
					for(; wkt.charAt(contador) != ']'; contador++) {
						while(wkt.charAt(contador) != '"' ) {
							spheroid[0] += wkt.charAt(contador);
							contador++;
						}
						contador +=2;
						while(wkt.charAt(contador) != ',') {
							spheroid[1] += wkt.charAt(contador);
							contador++;
						}
						contador++;
						while(wkt.charAt(contador) != ']') {
							spheroid[2] += wkt.charAt(contador);
							contador++;
						}
					}
				}
				contador++;
				res = "";
				for(; wkt.charAt(contador) != '"'; contador++) {
					res += wkt.charAt(contador);
				}
				if(res.equals(",PRIMEM[")) {
					contador++;
					for(; wkt.charAt(contador) != ','; contador++) {
						while(wkt.charAt(contador) != '"' ){
							primem[0] += wkt.charAt(contador);
							contador ++;
						}
						contador +=2;
						while(wkt.charAt(contador) != ']') {
							primem[1] += wkt.charAt(contador);
							contador ++;
						}
					}
				}
				contador++;
				res = "";
				for(; wkt.charAt(contador) != '"'; contador++) {
					res += wkt.charAt(contador);
				}
				if(res.equals("UNIT[")) {
					contador++;
					for(; wkt.charAt(contador) != ']'; contador++) {
						while(wkt.charAt(contador) != '"' ){
							unit[0] += wkt.charAt(contador);
							contador ++;
						}
						contador +=2;
						while(wkt.charAt(contador) != ']') {
							unit[1] += wkt.charAt(contador);
							contador ++;
						}
					}
				}
				contador++;
				res = "";
				for(; wkt.charAt(contador) != '"'; contador++) {
					res += wkt.charAt(contador);
				}
				if(res.equals(",PROJECTION[")) {
					contador++;
					for(; wkt.charAt(contador) != '"'; contador++) {
						projection += wkt.charAt(contador);
					}
				}
				contador = contador+2;
				res = "";
				for(; wkt.charAt(contador) != '"'; contador++) {
					res += wkt.charAt(contador);
				}
				//Hallamos el numero de parametros que tiene la cadena wkt
				int i = 0;
				
				int copiacontador = contador;
				if(res.equals(",PARAMETER[")) {
					do{
						for(; wkt.charAt(copiacontador) != ']'; copiacontador++) {
							while(wkt.charAt(copiacontador) != '"' )
								copiacontador++;
							copiacontador += 2;
							while(wkt.charAt(copiacontador) != ']' )
								copiacontador++;
							copiacontador--;
						}
						i++;
						copiacontador++;
						res = "";
						for(; wkt.charAt(copiacontador) != '"'; copiacontador++) {
							res += wkt.charAt(copiacontador);
						}
					} while (res.equals(",PARAMETER["));
					res = ",PARAMETER[";
				}
				// Inicializamos los parametros
				param_name = new String[i];
				param_value = new String[i];
				for(int j = 0 ;j < i; j++ ){
					param_name[j] = "";
					param_value[j] = "";
				}
				i = 0;
				if(res.equals(",PARAMETER[")) {
					do{
						contador++;
						for(; wkt.charAt(contador) != ']'; contador++) {
							while(wkt.charAt(contador) != '"' ){
								param_name[i] += wkt.charAt(contador);
								contador++;
							}
							contador += 2;
							while(wkt.charAt(contador) != ']' ){
								param_value[i] += wkt.charAt(contador);
								contador++;
							}
							contador--;
						}
						i++;
						contador++;
						res = "";
						for(; wkt.charAt(contador) != '"'; contador++) {
							res += wkt.charAt(contador);
						}
					} while (res.equals(",PARAMETER["));					
				}
				
				if (res.equals(",UNIT[")){
					contador++;
					for(; wkt.charAt(contador) != ','; contador++) {
						while(wkt.charAt(contador) != '"' ){
							unit_p[0] += wkt.charAt(contador);
							contador ++;
						}
						contador +=2;
						while(wkt.charAt(contador) != ']') {
							unit_p[1] += wkt.charAt(contador);
							contador ++;
						}
					}
				}
				contador++;
				res = "";
				for(; wkt.charAt(contador) != '"'; contador++) {
					res += wkt.charAt(contador);
				}
				if(res.equals("AUTHORITY[")) {
					contador++;
					for(; wkt.charAt(contador) != ']'; contador++) {
						while(wkt.charAt(contador) != '"' ){
							authority[0] += wkt.charAt(contador);
							contador ++;
						}
						contador +=2;
						while(wkt.charAt(contador) != ']') {
							authority[1] += wkt.charAt(contador);
							contador ++;
						}
					}
				}
			}
			else if (res.equals("GEOCCS[")){
				/*
				 * parte necesaria para capturar la cadena geocentrica...
				 */
				
			}
		}else
			geogcs = wkt;
	}
	
	public String getProjection() {
		return projection;
	}
	
	public String getProjcs() {
		return projcs;
	}
	
	public String getGeogcs() {
		return geogcs;
	}
	
	public String getDatumName() {
		return datum;
	}
	
	public String[] getSpheroid() {
		return spheroid;
	}
	
	public String[] getPrimen() {
		return primem;
	}
	
	public String getName() {
		if(projcs == "") 
			return geogcs;
		return projcs;
	}
	
	public String[] getUnit() {
		return unit;
	}
	
	public String[] getUnit_p() {
		return unit_p;
	}
	
	public String[] getParam_name() {
		return param_name;
	}
	
	public String[] getParam_value() {
		return param_value;
	}
	
	public String[] getAuthority(){
		return authority;
	}
	
	/*
	 * Parser a medio hacer, orientado a objeto y recursivo.
	 * by LWS.
	 */
	
	public static class WKT {
		public class Param {
			String key;
			ArrayList values = new ArrayList();
			ArrayList params = new ArrayList();
			public int pos = 0;
			public Param(String key) {
				this.key = key;
			}
			public void addValue(String name) {
				values.add(name);
			}
			public void addParam(Param p) {
				params.add(p);
			}
		}
		String data;
		public WKT(String data) {
			this.data = data;
			WKT.Param param = parseParam(0);
		}
		private WKT.Param parseParam(int pos) {
			WKT.Param param = null;
			String key, name;
			
			int l = data.length();
			for (int i = pos; i<l;) {
				int nextParam = data.indexOf(",", i);
				if (nextParam == i) {
					nextParam = data.indexOf(",", ++i);
				}
				int cierra = data.indexOf("]", i);
				int abre = data.indexOf("[", i);
				if (cierra < abre) { // Esta mal
					pinta(" =>");
					param.pos = cierra;
					return param;
				}
				if (param == null) {
					if (abre > 0) { //hay claves
						key = data.substring(i, data.indexOf("[", i));
						pinta(key+ " <= ");
						i = abre+1;
						param = new WKT.Param(key);
					}
				} else {
					if (data.substring(i).startsWith("\"")) {
						name = data.substring(i+1, data.indexOf("\"", i+1));
						i+=name.length()+2;
						pinta("|"+name+"|,");
						param.addValue(name);
					} else if (nextParam<abre) {
						name = data.substring(i, data.indexOf(",", i));
						i+=name.length();
						pinta(name+",");
						param.addValue(name);
					} else {
						Param p = parseParam(i);
						i = p.pos+1;
					}
				}
			}
			return param;
		}
		static int cnt=0;
		public static void pinta(String str) {
			cnt++;
			if (cnt>60)
				System.exit(1);
			System.out.println(str);
		}
	}
	
	private void fromCode(String code) {
		CoordinateReferenceSystem source;
		setAuthority(code);
		try {
			source = CRS.decode(code);
			setWkt(source.toWKT());
			
			if (source instanceof DefaultProjectedCRS) {
				DefaultProjectedCRS sour = (DefaultProjectedCRS) source;
				setProjcs(sour.getName().toString().split(":")[1]);
				String[] val = sour.getBaseCRS().getName().toString().split(":");
				if (val.length<2)
					setGeogcs(sour.getBaseCRS().getName().toString().split(":")[0]);
				else
					setGeogcs(sour.getBaseCRS().getName().toString().split(":")[1]);
				DefaultGeodeticDatum d = (DefaultGeodeticDatum) sour.getDatum();
				val = d.getName().toString().split(":");
				if (val.length<2)
					setDatumName(d.getName().toString().split(":")[0]);
				else
					setDatumName(d.getName().toString().split(":")[1]);
				setSpheroid((DefaultEllipsoid)d.getEllipsoid());				
				setPrimen(d.getPrimeMeridian());
				val = sour.getConversionFromBase().getMethod().getName().toString().split(":");
				if (val.length<2)
					setProjection(sour.getConversionFromBase().getMethod().getName().toString().split(":")[0]);
				else
					setProjection(sour.getConversionFromBase().getMethod().getName().toString().split(":")[1]);
				String str;
				param_name = new String[sour.getConversionFromBase().getParameterValues().values().size()];
				param_value= new String[sour.getConversionFromBase().getParameterValues().values().size()];
				for (int i=0; i< sour.getConversionFromBase().getParameterValues().values().size();i++) {
					str = sour.getConversionFromBase().getParameterValues().values().get(i).toString();
					Unit u = sour.getConversionFromBase().getParameterValues().parameter(str.split("=")[0]).getUnit();
					double value = sour.getConversionFromBase().getParameterValues().parameter(str.split("=")[0]).doubleValue();
					value = convert(value, u.toString());
					param_name[i] = str.split("=")[0];
					param_value [i] = String.valueOf(value);
				}
				
			}
			
			if (source instanceof DefaultGeographicCRS) {
				DefaultGeographicCRS sour = (DefaultGeographicCRS) source;
				String[] val = sour.getName().toString().split(":");
				if (val.length<2)
					setGeogcs(sour.getName().toString().split(":")[0]);
				else
					setGeogcs(sour.getName().toString().split(":")[1]);
				
				DefaultGeodeticDatum d = (DefaultGeodeticDatum) sour.getDatum();
				val = d.getName().toString().split(":");
				if (val.length<2)
					setDatumName(d.getName().toString().split(":")[0]);
				else
					setDatumName(d.getName().toString().split(":")[1]);
				setSpheroid((DefaultEllipsoid)d.getEllipsoid());				
				setPrimen(d.getPrimeMeridian());
				
			}
		} catch (NoSuchAuthorityCodeException e) { //Pertenece a CRS
			//System.out.println("Hola: 1");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryException e) { //Pertenece a CRS
			//System.out.println("Hola: 2 ");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	private void parseWKT(String data) {
		WKT wkt = new WKT(data);
	}
	
	public void setProjcs (String proj) {
		this.projcs = proj;
	}
	
	public void setProjection (String projection) {
		this.projection = projection;
	}
	public void setGeogcs (String geo) {
		this.geogcs = geo;
	}
	
	public void setDatumName (String dat) {
		this.datum = dat;
	}
	
	public void setSpheroid (DefaultEllipsoid ellips) {
		Unit u = ellips.getAxisUnit();
		double semi_major = convert( ellips.getSemiMajorAxis(), u.toString());
		double inv_f = convert( ellips.getInverseFlattening(), u.toString());
		String[] val =	ellips.getName().toString().split(":");
		if (val.length<2)
			this.spheroid[0] = ellips.getName().toString().split(":")[0];
		else
			this.spheroid[0] = ellips.getName().toString().split(":")[1];
		this.spheroid[1] = String.valueOf(semi_major);
		this.spheroid[2] = String.valueOf(inv_f);
	}
	
	public void setPrimen (PrimeMeridian prim) {
		DefaultPrimeMeridian pm = (DefaultPrimeMeridian)(prim);
		Unit u = pm.getAngularUnit();
		double value = convert( pm.getGreenwichLongitude(), u.toString());
		String[] val = pm.getName().toString().split(":");
		if (val.length<2)
			this.primem[0] = pm.getName().toString().split(":")[0];
		else
			this.primem[0] = pm.getName().toString().split(":")[1];
		this.primem[1] = String.valueOf(value);
	}
		
	public void setAuthority (String aut) {
		this.authority = aut.split(":");		
	}
	
	public void setWkt(String wkt){
		this.wkt = wkt;
	}
	
	public String getWkt() {
		return wkt;
	}
	
	public double convert(double value, String measure) throws ConversionException {
		if (measure.equals("D.MS")) {		
			value *= this.divider;
	        int deg,min;
	        deg = (int) (value/10000); value -= 10000*deg;
	        min = (int) (value/  100); value -=   100*min;
	        if (min<=-60 || min>=60) {  // Accepts NaN
	            if (Math.abs(Math.abs(min) - 100) <= EPS) {
	                if (min >= 0) deg++; else deg--;
	                min = 0;
	            } else {
	                throw new ConversionException("Invalid minutes: "+min);
	            }
	        }
	        if (value<=-60 || value>=60) { // Accepts NaN
	            if (Math.abs(Math.abs(value) - 100) <= EPS) {
	                if (value >= 0) min++; else min--;
	                value = 0;
	            } else {
	                throw new ConversionException("Invalid secondes: "+value);
	            }
	        }
	        value = ((value/60) + min)/60 + deg;
	        return value;
		}
		if (measure.equals("grad") || measure.equals("grade")) 
			return ((value * 180.0) / 200.0);			
		if (measure.equals(""+(char)176)) 
			return value;		
		if (measure.equals("DMS") ) 
			return value;		
		if (measure.equals("m")) 
			return value;
		//TODO revisar este if, y ver si seria correcto...
		if (measure.startsWith("[m*")) {
			return value*Double.parseDouble(measure.substring(3,measure.length()-1));
		}
		if (measure.equals("")) 
			return value;
		if (measure.equalsIgnoreCase("ft")||measure.equalsIgnoreCase("foot")||measure.equalsIgnoreCase("feet")) 
			return (value*0.3048/1.0);
		
		throw new ConversionException("Conversion no contemplada: "+measure);
    }
}