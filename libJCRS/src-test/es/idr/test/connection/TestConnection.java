package es.idr.test.connection;


import java.sql.ResultSet;
import java.sql.SQLException;

import es.idr.teledeteccion.connection.EpsgConnection;
import es.idr.teledeteccion.connection.Query;



public class TestConnection {

	public TestConnection () {
		testEpsgDataBase();
		testEsriDataBase();
		testIau2000DataBase();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestConnection tc = new TestConnection();
        
		
	}
	
	private void testEpsgDataBase() {
		EpsgConnection connect = new EpsgConnection();
		connect.setConnectionEPSG();
		String sentence = "SELECT coord_ref_sys_name " +
							"FROM epsg_coordinatereferencesystem " +	                              
							"WHERE coord_ref_sys_code = 23030";
		
		ResultSet result;
        try {			
			result = Query.select(sentence,connect.getConnection());				
			result.next();
			System.out.println(result.getString("coord_ref_sys_name"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void testEsriDataBase (){
		EpsgConnection connect = new EpsgConnection();
		connect.setConnectionEsri();
		String sentence = "SELECT esri_proj " +							 
						  "FROM ESRI " +	                              
						  "WHERE esri_code = 23030";
		
		ResultSet result = null;
    	try {
    		result = Query.select(sentence,connect.getConnection());				
			result.next();
			System.out.println(result.getString("esri_proj"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		        	
	}
	
	private void testIau2000DataBase() {
		EpsgConnection connect = new EpsgConnection();
		connect.setConnectionIAU2000();
		String sentence = "SELECT iau_geog " +							 
						  "FROM IAU2000 " +	                              
						  "WHERE iau_code = 19900";
		
		ResultSet result = null;    	
		try {
			result = Query.select(sentence,connect.getConnection());				
			result.next();
			System.out.println(result.getString("iau_geog"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
