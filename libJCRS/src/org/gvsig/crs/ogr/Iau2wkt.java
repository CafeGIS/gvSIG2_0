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

import org.gvsig.crs.CrsWkt;

/**
 * Aunque iau2000 posee la cadena wkt, tendremos que volver a generarla con los
 * parámetros válidos para que no falle con gdal. Lo que haremos será recuperar
 * todos los campos, buscar la proyección correcta y volver a generar una
 * cadena wkt entendible por gdal
 * @author José Luis Gómez Martínez
 *
 */

public class Iau2wkt {

	String[] projectionsIAU = {"Albers"};
	String[] projectionsGDAL = {"Albers_Conic_Equal_Area"};
	CrsWkt datos = null;
	String cadWKT = "";
	String[] param_name = null;
	String[] param_value = null;
	
	public Iau2wkt(String wkt) {	
		datos = new CrsWkt(wkt);		
		param_name = datos.getParam_name();
		param_value = datos.getParam_value();
	}
	
	/**
	 * Formacion de la cadena wkt con los nombres correctos de las proyecciones
	 * de iau2000 para la libreria proj4.
	 * @return
	 */
	public String getWkt(){
		String proj = getNameProjectionWkt(datos.getProjection());
				
		cadWKT = "PROJCS[\""+ datos.getProjcs()+"\", GEOGCS[\"" + datos.getGeogcs() + "\", DATUM[\""+ datos.getDatumName() +
		"\", SPHEROID[\""+ datos.getSpheroid()[0] + "\", "+ datos.getSpheroid()[1] + ", "+ datos.getSpheroid()[2] +"]], " +
		"PRIMEM[\""+ datos.getPrimen()[0] + "\", "+ datos.getPrimen()[1] +"], UNIT[\""+ datos.getUnit()[0] + "\", " + (Math.PI/180) +
		"]], PROJECTION[\""+ proj + "\"], ";
			
		/*
		 * falta la parte de los parámetros... metodo para nombres...
		 */
		for (int i= 0; i< param_name.length;i++){
			if (proj.equals("Mercator_1SP")){
				if (param_name[i].equals("Standard_Parallel_1"))
					if (param_value[i].startsWith("-"))
						cadWKT += "PARAMETER[\""+param_name[i]+"\", " + param_value[i].substring(1,param_value[i].length())+ "], ";
					else
						cadWKT += "PARAMETER[\""+param_name[i]+"\", " + param_value[i]+ "], ";
				else if (param_name[i].equals("Standard_Parallel_2")){}					
				else 
					cadWKT += "PARAMETER[\""+param_name[i]+"\", " + param_value[i]+ "], ";
				
			}else
				cadWKT += "PARAMETER[\""+param_name[i]+"\", " + param_value[i]+ "], ";
		}
		
		cadWKT += "UNIT[\""+ datos.getUnit_p()[0] + "\", 1.0], ";
		cadWKT += "AUTHORITY[\""+ datos.getAuthority()[0] + "\", " + datos.getAuthority()[1] + "]]";
				
		return cadWKT;
	}
	
	/** 
	 * Metodo auxiliar para el cambio del nombre de la proyeccion.
	 */	
	private String getNameProjectionWkt(String projection) {
		String proj = null;
		
		for (int i = 0; i<projectionsIAU.length; i++){
			if (projection.equals(projectionsIAU[i])) {
				projection = projectionsGDAL[i];
			}
		}
		
		if (projection.equals("Lambert_Conformal_Conic")){
			projection = "Lambert_Conformal_Conic_1SP";
			String standardParallel1 = "";
			String standardParallel2 = "";
			for (int i = 0; i < param_name.length; i++) {
				if (param_name[i].equals("Standard_Parallel_1")) {
					standardParallel1 = param_value[i];
				}
				if (param_name[i].equals("Standard_Parallel_2")) {
					standardParallel2 = param_value[i];
					projection = "Lambert_Conformal_Conic_2SP";
				}
			}
			if (standardParallel1.startsWith("-") && standardParallel2.equals(standardParallel1.substring(1, standardParallel1.length())))
				projection = "Mercator_1SP";
		}
		
		if (projection.equals("Mercator")){
			projection = "Mercator_1SP";
			for (int i = 0; i < param_name.length; i++) {
				if (param_name[i].equals("Standard_Parallel_2")) {
					projection = "Mercator_2SP";
				}
			}
		}		
		proj = projection.replaceAll(" ", "_");		
		return proj;
	}
	
	

}
