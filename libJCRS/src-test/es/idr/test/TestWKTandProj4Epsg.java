package es.idr.test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.units.ConversionException;

import org.geotools.referencing.factory.epsg.HSQLDataSource;
import org.geotools.referencing.wkt.UnformattableObjectException;
import org.gvsig.crs.Crs;
import org.gvsig.crs.CrsException;
import org.gvsig.crs.CrsFactory;
import org.gvsig.crs.CrsGT;
import org.gvsig.crs.ICrs;
import org.gvsig.crs.Proj4;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class TestWKTandProj4Epsg {

	public static void main(String[] args) {
		HSQLDataSource ds = new HSQLDataSource();
		Connection conn;
		try {
			conn = ds.getConnection();
			int indice = 0;
			int fallo = 0;
			final PreparedStatement stmt;
	        stmt = conn.prepareStatement("SELECT COORD_REF_SYS_CODE, COORD_REF_SYS_NAME"
	        		          				+" FROM epsg_coordinatereferencesystem ");
	        		          				//+" WHERE COORD_REF_SYS_CODE>29372");
	        ResultSet result;
			
			result = stmt.executeQuery();
				      
	        String filename = "salida.txt";
	        
	        BufferedWriter bw =  new BufferedWriter(new FileWriter(filename));
	       
	        while (result.next()) {
	        	ICrs crs = null;
	        	int cod = result.getInt("COORD_REF_SYS_CODE");
	        	try {
        			
					crs = new CrsFactory().getCRS("EPSG:"+cod);
					if (crs instanceof CrsGT) {
//						//continue;
						if (crs.getProj4String() == null){
							bw.write("Código: "+cod+"\tNombre: "+result.getString("COORD_REF_SYS_NAME")+"\n");
				        	bw.write("Cadena WKT: "+crs.getWKT()+"\n");
				        	bw.write("Cadena Proj4: No cadena proj4 \n");
				        	bw.write("Fallo: Fallo generando en cadena proj4 \n");		        	
				        	bw.write("\n\n");
						}
					}						
					else {
						if (crs != null) {
							bw.write("Código: "+cod+"\tNombre: "+result.getString("COORD_REF_SYS_NAME")+"\n");
				        	bw.write("Cadena WKT: "+crs.getWKT()+"\n");
				        	bw.write("Cadena Proj4: "+crs.getProj4String()+"\n");
				        	bw.write("Fallo: Creado con crs antiguo \n");		        	
				        	bw.write("\n\n");
						}
						else {
							bw.write("Código: "+cod+"\tNombre: "+result.getString("COORD_REF_SYS_NAME")+"\n");
							bw.write("Fallo: Falla tanto geotools como crs antiguo \n");		        	
				        	bw.write("\n\n");
						}
			        	
					}
						
					
				} catch (CrsException e) {
					bw.write("Código: "+cod+"\tNombre: "+result.getString("COORD_REF_SYS_NAME")+"\n");
		        	bw.write("Cadena WKT: No cadena wkt \n");
		        	bw.write("Cadena Proj4: No cadena proj4\n");
		        	bw.write("Fallo: "+e+" \n");		        	
		        	bw.write("\n\n");
				} catch (NumberFormatException e) {
					bw.write("Código: "+cod+"\tNombre: "+result.getString("COORD_REF_SYS_NAME")+"\n");
		        	bw.write("Cadena WKT: No cadena wkt \n");
		        	bw.write("Cadena Proj4: No cadena proj4\n");
		        	bw.write("Fallo: "+e+" \n");		        	
		        	bw.write("\n\n");
				} catch (ConversionException e) {
					bw.write("Código: "+cod+"\tNombre: "+result.getString("COORD_REF_SYS_NAME")+"\n");
		        	bw.write("Cadena WKT: No cadena wkt \n");
		        	bw.write("Cadena Proj4: No cadena proj4\n");
		        	bw.write("Fallo: "+e+" \n");		        	
		        	bw.write("\n\n");
				}catch (UnformattableObjectException e) {					
				}				
	        }
	        	
	        bw.flush();
	        bw.close();
	       
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
