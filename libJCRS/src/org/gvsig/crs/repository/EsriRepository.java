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
 *   Instituto de Desarrollo Regional (Universidad de Castilla La-Mancha
 *   Campus Universitario s/n
 *   02071 Alabacete
 *   Spain
 *
 *   +34 967 599 200
 */
package org.gvsig.crs.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.gvsig.crs.Crs;
import org.gvsig.crs.CrsException;
import org.gvsig.crs.ICrs;
import org.gvsig.crs.ogr.Esri2wkt;

import es.idr.teledeteccion.connection.EpsgConnection;
import es.idr.teledeteccion.connection.Query;

/**
 * Repositorio de CRSs de EPSG
 * 
 * @author José Luis Gómez Martínez (jolugomar@gmail.com)
 *
 */

public class EsriRepository implements ICrsRepository {

	EpsgConnection connection = null;
	
	public EsriRepository() {
		connection = new EpsgConnection();
		connection.setConnectionEsri();		
	}

	public ICrs getCrs(String code) {
		String cadWKT = "";
		Crs crs = null;
		
		String sentence = "SELECT esri_code, esri_wkt, esri_proj, esri_geog, esri_datum " +							 
						  "FROM ESRI " +	                              
                          "WHERE esri_code = " + code;
		
		ResultSet result = Query.select(sentence,connection.getConnection());	
		/*try {
			connection.shutdown();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		try {
			if (!result.next())
				return null;	
			cadWKT = result.getString("esri_wkt");			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}		
		cadWKT = cadWKT.substring(0, cadWKT.length()-1) + ", AUTHORITY[\"ESRI\","+ Integer.parseInt(code)+"]]";
		if (cadWKT.charAt(0) == 'P'){
			Esri2wkt wk = new Esri2wkt(cadWKT);
			cadWKT = wk.getWkt();
		}
		
		try {
			crs = new Crs(Integer.parseInt(code),cadWKT);
		} catch (CrsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return crs;

	}

}
