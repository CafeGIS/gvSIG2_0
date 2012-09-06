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

package es.idr.teledeteccion.connection;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Clase para realizar las consultas a las bases de datos utilizadas
 * 
 * @author José Luis Gómez Martínez (jolugomar@gmail.com)
 *
 */
public class Query implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Acepta la sentencia y la conexión a la base de datos actual,
	 * realiza una selección y devuelve los datos en un ResultSet
	 * @param sentence
	 * @param conn
	 * @return
	 */
	public static synchronized ResultSet select(String sentence, Connection conn){
		Statement st = null;
		ResultSet rs = null;
				
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sentence);
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return rs;
	}	
}

