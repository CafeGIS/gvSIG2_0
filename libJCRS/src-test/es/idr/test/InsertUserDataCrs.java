package es.idr.test;

import java.sql.SQLException;

import es.idr.teledeteccion.connection.EpsgConnection;


public class InsertUserDataCrs {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EpsgConnection conn = new EpsgConnection();
		conn.setConnectionUsr();
		
		String sentence = "INSERT INTO USR VALUES(2000,'PROJCS[\"Anguilla_1957_British_West_Indies_Grid\",GEOGCS[\"GCS_Anguilla_1957\",DATUM[\"D_Anguilla_1957\",SPHEROID[\"Clarke_1880_RGS\",6378249.145,293.465]],PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]],PROJECTION[\"Transverse_Mercator\"],PARAMETER[\"False_Easting\",400000.0],PARAMETER[\"False_Northing\",0.0],PARAMETER[\"Central_Meridian\",-62.0],PARAMETER[\"Scale_Factor\",0.9995],PARAMETER[\"Latitude_Of_Origin\",0.0],UNIT[\"Meter\",1.0]]','Anguilla_1957_British_West_Indies_Grid','GCS_Anguilla_1957','D_Anguilla_1957')";
		
		try {
			conn.update(sentence);
			conn.shutdown();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
