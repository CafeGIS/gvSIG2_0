package es.idr.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.units.Unit;

import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.crs.DefaultProjectedCRS;
import org.geotools.referencing.datum.DefaultGeodeticDatum;
import org.geotools.referencing.factory.epsg.HSQLDataSource;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import es.idr.teledeteccion.connection.EpsgConnection;
import es.idr.teledeteccion.connection.Query;

public class ValidateEsriCRSs {

	public ValidateEsriCRSs() {
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//HSQLDataSource ds = new HSQLDataSource();
		EpsgConnection conn = new EpsgConnection();
		//Connection conn;
		conn.setConnectionEsri();
		try {
			//conn = ds.getConnection();
		
			//final PreparedStatement stmt;
	        /*stmt = conn.prepareStatement("SELECT esri_code"
	                                      + " FROM esri");
	        */
	        String sentence = "SELECT esri_code,esri_wkt " +							 
								  "FROM ESRI";
	        ResultSet result;
	        result = Query.select(sentence,conn.getConnection());
	        
	        
			
			//result = stmt.executeQuery();
			
	        ArrayList res = new ArrayList();
	        ArrayList units = new ArrayList();
	        ArrayList validos = new ArrayList();
	        while (result.next()) {
	        	int cod = result.getInt("esri_code");
	        	String wkt = result.getString("esri_wkt");
        		//String strEpsgCode="ESRI:"+cod;		
        		CoordinateReferenceSystem source;
        		
        			/*try {
						source = CRS.parseWKT(wkt);
						validos.add(String.valueOf(cod));
						if (source instanceof DefaultProjectedCRS) {
							String str;
							boolean esta = false;
							DefaultProjectedCRS sour = (DefaultProjectedCRS) source;
						
							Unit u = ((DefaultGeodeticDatum) sour.getDatum()).getPrimeMeridian().getAngularUnit();
							units.add(u.toString());
							
							for (int j = 0; j< units.size(); j++) {
					        	if (units.get(j).equals(u.toString())){
					        		esta = true;
					        		break;
					        	}						        	
					        }
							if (!esta){
								units.add(u.toString());
								esta = false;
							}
							for (int i=0; i< sour.getConversionFromBase().getParameterValues().values().size();i++) {
								str = sour.getConversionFromBase().getParameterValues().values().get(i).toString();
								u = sour.getConversionFromBase().getParameterValues().parameter(str.split("=")[0]).getUnit();
								units.add(u.toString());
																																								
							}
						}
						if (source instanceof DefaultGeographicCRS) {
							DefaultGeographicCRS sour = (DefaultGeographicCRS) source;
							boolean esta = false;
							Unit u = ((DefaultGeodeticDatum) sour.getDatum()).getPrimeMeridian().getAngularUnit();
							units.add(u.toString());							
													
						}
					} catch (NoSuchAuthorityCodeException e) {
						res.add(String.valueOf(cod)+" --> "+e);
						// TODO Auto-generated catch block
						//e.printStackTrace();
					} catch (FactoryException e) {
						res.add(String.valueOf(cod)+" --> "+e);
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}*/
	        }
	        /*for (int i = 0; i< res.size(); i++) {
	        	System.out.println(res.get(i));
	        }*/	
	        for (int i = 0; i< units.size(); i++) {
	        	System.out.println(units.get(i));
	        }
	        System.out.println("Numero CRSs validos: " + validos.size());
	        System.out.println("Numero medidas: " + units.size());
	        System.out.println("Numero CRSs fallidos: " + res.size());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
	}
}
