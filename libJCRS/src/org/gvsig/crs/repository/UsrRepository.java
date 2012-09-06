package org.gvsig.crs.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.gvsig.crs.Crs;
import org.gvsig.crs.CrsException;
import org.gvsig.crs.ICrs;

import es.idr.teledeteccion.connection.EpsgConnection;
import es.idr.teledeteccion.connection.Query;

public class UsrRepository implements ICrsRepository {

	EpsgConnection connection;
	
	public UsrRepository() {
		super();
		connection = new EpsgConnection();		
	}

	public ICrs getCrs(String code) {
		// TODO Auto-generated method stub
		String cadWKT = "";
		Crs crs = null;
		
		String sentence = "SELECT usr_code, usr_wkt, usr_proj, usr_geog, usr_datum " +							 
						  "FROM USR " +	                              
                          "WHERE usr_code = " + code;
		
		connection.setConnectionUsr();
		ResultSet result = Query.select(sentence,connection.getConnection());	
		try {
			connection.shutdown();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if (!result.next())
				return null;		
			cadWKT = result.getString("usr_wkt");			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}		
		cadWKT = cadWKT.substring(0, cadWKT.length()-1) + ", AUTHORITY[\"USR\","+ Integer.parseInt(code)+"]]";
				
		try {
			crs = new Crs(Integer.parseInt(code),cadWKT);
		} catch (CrsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return crs;		
	}

}
