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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import org.geotools.referencing.factory.epsg.HSQLDataSource;
import org.geotools.referencing.factory.iau2000.FactoryUsingHSQL;
import org.hsqldb.jdbc.jdbcDataSource;

/**
 * Clase para la conexión con la base de datos de hsqldb.
 * Establece el driver necesario, así como la cadena de
 * conexión a la base de datos de la EPSG y la IAU2000
 * 
 * @author José Luis Gómez Martínez (jolugomar@gmail.com)
 *
 */

public class EpsgConnection extends jdbcDataSource {
	
	Connection connect;

	public EpsgConnection() {	
	/*	try {
			Class.forName("org.hsqldb.jdbcDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}*/
	}
	
	public void setConnectionEPSG() {
		HSQLDataSource ds = new HSQLDataSource();
				
		try {
			connect = ds.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Establece la conexión con la base de datos de la IAU2000
	 *
	 */
	public void setConnectionIAU2000() {
		setDatabase("jdbc:hsqldb:file:gvSIG/extensiones/org.gvsig.crs/iau2000");
		setUser("sa");
		try {
			connect = super.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Establece la conexión con la base de datos de ESRI
	 *
	 */
	public void setConnectionEsri() {
		setDatabase("jdbc:hsqldb:file:gvSIG/extensiones/org.gvsig.crs/esri");
		setUser("sa");
		try {
			connect = super.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Establece la conexión con la base de datos de USR
	 *
	 */
	public void setConnectionUsr() {
		setDatabase("jdbc:hsqldb:file:gvSIG/extensiones/org.gvsig.crs/usr");
		setUser("sa");
		try {
			connect = super.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Connection getConnection(){
		return connect;
	}
	
	public void shutdown() throws SQLException {

        Statement st = connect.createStatement();

        // db writes out to files and performs clean shuts down
        // otherwise there will be an unclean shutdown
        // when program ends
        st.execute("SHUTDOWN");
        connect.close();    // if there are no other open connection
    }
	
	public synchronized void update(String expression) throws SQLException {

        Statement st = null;

        st = connect.createStatement();    // statements

        int i = st.executeUpdate(expression);    // run the query

        if (i == -1) {
            System.out.println("db error : " + expression);
        }

        st.close();
    }
	/*
	*//**
	 * Establece la conexión con la base de datos de la EPSG
	 *
	 *//*
	public void setConnectionEPSG() {
		try {			
			connect =  DriverManager.getConnection("jdbc:hsqldb:file:gvSIG/extensiones/org.gvsig.crs/db_epsg", "sa", "");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	*//**
	 * Establece la conexión con la base de datos de la IAU2000
	 *
	 *//*
	public void setConnectionIAU2000() {
		try {			
			connect =  DriverManager.getConnection("jdbc:hsqldb:file:gvSIG/extensiones/org.gvsig.crs/db_iau2000", "sa", "");			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	*//**
	 * Establece la conexión con la base de datos de ESRI
	 *
	 *//*
	public void setConnectionEsri() {
		try {			
			connect =  DriverManager.getConnection("jdbc:hsqldb:file:/home/jlgomez/gvSIGF2/_fwAndami/gvSIG/extensiones/org.gvsig.crs/db_esri", "sa", "");			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	*//**
	 * Establece la conexión con la base de datos de USR
	 *
	 *//*
	public void setConnectionUsr() {
		try {			
			connect =  DriverManager.getConnection("jdbc:hsqldb:file:gvSIG/extensiones/org.gvsig.crs/db_usr", "sa", "");			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	public Connection getConnection(){
		return connect;
	}
	
	public void shutdown() throws SQLException {

        Statement st = connect.createStatement();

        // db writes out to files and performs clean shuts down
        // otherwise there will be an unclean shutdown
        // when program ends
        st.execute("SHUTDOWN");
        connect.close();    // if there are no other open connection
    }*/
	
	 /**
     * Returns {@code true} if the database contains data. This method returns {@code false}
     * if an empty EPSG database has been automatically created by HSQL and not yet populated.
     */
    private static boolean dataExists(final Connection connection) throws SQLException {
        final ResultSet tables = connection.getMetaData().getTables(
                null, null, "IAU2000_%", new String[] {"TABLE"});
        final boolean exists = tables.next();
        tables.close();
        return exists;
    }
}
