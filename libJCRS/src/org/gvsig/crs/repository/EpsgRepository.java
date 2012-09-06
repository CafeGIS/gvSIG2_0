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
import org.gvsig.crs.ogr.Epsg2wkt;
import org.gvsig.crs.ogr.CrsEPSG;

import es.idr.teledeteccion.connection.EpsgConnection;
import es.idr.teledeteccion.connection.Query;

/**
 * Repositorio de CRSs de EPSG
 * 
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 *
 */
public class EpsgRepository implements ICrsRepository {
	
	public EpsgConnection connection = null;
	
	public EpsgRepository() {
		connection = new EpsgConnection();
	}

	/**
	 * Obtiene un CRS a partir de su código EPSG.
	 * 
	 * @param code Codigo EPSG del CRS. 
	 * @return ICrs 
	 */
	public ICrs getCrs(String code) {
		int epsg_code  = Integer.parseInt(code);
		boolean source_yn = false;
		int source_cod = 0;
		int datum_code = 0;
		int projection_conv_code = 0;
		String crs_kind = null;
		
		Epsg2wkt wkt = null;
		
		Crs crs = null;
		
		ResultSet result = null;
		String sentence = "SELECT source_geogcrs_code, projection_conv_code, "
			+ "coord_ref_sys_kind, datum_code "
			+ "FROM epsg_coordinatereferencesystem "
			+ "WHERE coord_ref_sys_code = " + code;
						
		connection.setConnectionEPSG();
		result = Query.select(sentence,connection.getConnection());	
		
		/*try {
			connection.shutdown();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		try {
			if (!result.next())
				return null;
			source_cod = result.getInt("source_geogcrs_code");
			projection_conv_code = result.getInt("projection_conv_code");
			crs_kind = result.getString("coord_ref_sys_kind");
			datum_code = result.getInt("datum_code");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		if (datum_code != 0){
			source_yn = true;
		}
		else if (source_cod != 0){
			source_yn = false;
		}
		else source_yn = true;
		
		CrsEPSG ep = new CrsEPSG(epsg_code, source_yn, source_cod, projection_conv_code, connection);
		
		if (crs_kind.equals("geographic 2D") || crs_kind.equals("geographic 3D")){
			wkt = new Epsg2wkt(ep , "geog");			
		}
		else if (crs_kind.equals("projected")){
			wkt = new Epsg2wkt(ep, "proj");
		}
		else if (crs_kind.equals("compound")){
			wkt = new Epsg2wkt(ep,"comp");
		}
		else if (crs_kind.equals("geocentric")){
			wkt = new Epsg2wkt(ep,"geoc");
		}
		
		try {
			crs = new Crs(Integer.parseInt(code),wkt.getWKT());
		} catch (CrsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return crs;
	}
}
