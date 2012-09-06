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

package org.gvsig.crs.ogr;

import java.util.ArrayList;

/**
 * Clase que genera de un CRS escogido del repositorio de EPSG su cadena
 * en wkt correctamente para la creación del CRS conforme a los parámetros
 * aceptados en la librería proj4 (en cuanto a formato)
 * 
 * @author José Luis Gómez Martínez (jolugomar@gmail.com)
 */
public class Epsg2wkt {
	
	String cadWKT= "";	
	
	/*
	 * definimos 2 arrays para el cambio de proyecciones y de parametros de la epsg a 
	 * proyecciones y parámetros entendibles por gdal, de manera que el elemento i 
	 * de cada uno de ellos coincide con el cambio correcto de epsg a gdal.
	 */
	String[] paramsEPSG = {"Angle from Rectified to Skew Grid", "Azimuth of initial line", 
			"Easting at false origin", "Easting at projection centre", "False easting", 
			"False northing", "Latitude of false origin", "Latitude of 1st standard parallel",
			"Latitude of natural origin", "Latitude of origin", "Latitude of projection centre",
			"Latitude of 2nd standard parallel", "Longitude of false origin",
			"Longitude of natural origin", "Longitude of origin", "Longitude of projection centre",
			"Northing at false origin", "Northing at projection centre",
			"Scale factor at natural origin", "Scale factor on initial line",};
	
	String[] paramsWkt = {"rectified_grid_angle", "azimuth", "false_easting", "false_easting",
			"false_easting", "false_northing", "latitude_of_origin", "standard_parallel_1",
			"latitude_of_origin", "latitude_of_origin", "latitude_of_center", "standard_parallel_2",
			"central_meridian", "central_meridian", "central_meridian", "longitude_of_center",
			"false_northing", "false_northing", "scale_factor", "scale_factor",};

	String[] projectionsEPSG = {"Lambert Conic Conformal (1SP)","Lambert Conic Conformal (2SP)",
			"Lambert Conic Conformal (2SP) Belgium", "American Polyconic", "Krovak Oblique Conic Conformal", 
			"Albers Equal Area"};
	
	String[] projectionsWkt = {"Lambert Conformal Conic 1SP","Lambert Conformal Conic 2SP",
			"Lambert Conformal Conic 2SP Belgium", "Polyconic", "Krovak", "Albers Conic Equal Area"};
			
	
	/**
	 * Constructor para un CRS horizontal y proyectado 
	 */
	public Epsg2wkt(CrsEPSG epsg, String kind) {
		
		if (kind.equals("proj")){
			String[] spheroid = epsg.getSPHEROID();
			String[] primem = epsg.getPRIMEM();
			String[] param_name = epsg.getParam_name();
			String[] param_value = epsg.getParam_value();
			String[] authority = epsg.getAUTHORITY();
			
			cadWKT = "PROJCS[\""+ epsg.getPROJCS()+"\", GEOGCS[\"" + epsg.getGEOGCS() + "\", DATUM[\""+ epsg.getDATUM() +
					"\", SPHEROID[\""+ spheroid[0] + "\", "+ spheroid[1] + ", "+ spheroid[2] +"]], " +
					"PRIMEM[\""+ primem[0] + "\", "+ primem[1] +"], UNIT[\""+ epsg.getUNIT_A() + "\", " + (Math.PI/180) +
					"]], PROJECTION[\""+ getNameProjectionWkt(epsg.getPROJECTION()) + "\"], ";
						
			/*
			 * falta la parte de los parámetros... metodo para nombres...
			 */
			for (int i= 0; i< param_name.length;i++){
				param_name[i] = getParametersWkt(param_name[i]);
				cadWKT += "PARAMETER[\""+param_name[i]+"\", " + param_value[i]+ "], ";
			}
			
			cadWKT += "UNIT[\""+ epsg.getUNIT_B() + "\", "+epsg.getUnit_BValue()+"], ";
			cadWKT += "AUTHORITY[\""+ authority[0] + "\", " + authority[1] + "]]";
		}
		else if (kind.equals("geog")) {
			String[] spheroid = epsg.getSPHEROID();
			String[] primem = epsg.getPRIMEM();
			String[] authority = epsg.getAUTHORITY();
			cadWKT = "GEOGCS[\"" + epsg.getGEOGCS() + "\", DATUM[\""+ epsg.getDATUM() +
					"\", SPHEROID[\""+ spheroid[0] + "\", "+ spheroid[1] + ", "+ spheroid[2] +"]], " +
					"PRIMEM[\""+ primem[0] + "\", "+ primem[1] +"], UNIT[\""+ epsg.getUNIT_A() + "\", " + (Math.PI/180) +
			"], ";
			cadWKT += "AUTHORITY[\""+ authority[0] + "\", " + authority[1] + "]]";
		}
		
		else if (kind.equals("geoc")){
			String[] spheroid = epsg.getSPHEROID();
			String[] primem = epsg.getPRIMEM();
			cadWKT = "GEOCCS[\"" + epsg.getGEOGCS() + "\", DATUM[\""+ epsg.getDATUM() +
					"\", SPHEROID[\""+ spheroid[0] + "\", "+ spheroid[1] + ", "+ spheroid[2] +"]], " +
					"PRIMEM[\""+ primem[0] + "\", "+ primem[1] +"], UNIT[\""+ epsg.getUNIT_A() + "\", " + (Math.PI/180) +
			"]]";
			/*
			 * parte necesaria cuando tratemos CRS geocentricos
			 */
		}
		
		else if (kind.equals("comp")){
			/*
			 * parte necesaria cuando tratemos CRS compuestos (Hablar con David)
			 */
		}	
	}
	
	/*
	 * Contructor para un CRS local (por ahora no se contempla)
	 */
	public Epsg2wkt(String localcs, String local_datum,  String unit, ArrayList axis) {
		cadWKT = "LOCAL_CS[" + localcs + ", LOCAL_DATUM["+ local_datum +
		"], UNIT["+ unit +"]";	
		
		for (int i =0; i< axis.size(); i++){
			cadWKT += ", AXIS[" + axis.get(i) + "]";
		}
		
		cadWKT += "]";		
	}
	
	public String getWKT(){
		return cadWKT;
	}
	
	/**
	 * Consigue la proyección en el formato correcto para que sea legible
	 * por la librería proj4. Acepta la proyección del CRS seleccionado y
	 * devuelve la misma proyección escrita de manera correcta.
	 * @param projection
	 * @return
	 */	
	private String getNameProjectionWkt(String projection) {
		String proj = null;
		/*
		 * hacer el cambio de nombre de proyeccion correcta
		 */
		for (int i = 0; i< projectionsEPSG.length; i++){
			if (projectionsEPSG[i].equals(projection)){
				projection = projectionsWkt[i];
			}
		}		
		proj = projection.replaceAll(" ", "_");		
		return proj;
	}
	
	/**
	 * Acepta el parámetro actual, realiza el cambio necesario para que
	 * sea legible por la proj4, y lo devuelve para insertarlo en la
	 * cadena wkt
	 * 
	 * @param param
	 * @return
	 */
	private String getParametersWkt(String param){
		for (int j=0; j< paramsEPSG.length; j++){
			if (param.equals(paramsEPSG[j])){
				param = paramsWkt[j];
			}
		}		
		return param.replaceAll(" ", "_");		
	}	
}
