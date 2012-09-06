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
import org.gvsig.crs.Crs;
import org.gvsig.crs.CrsException;
import org.geotools.referencing.factory.epsg.HSQLDataSource;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;



public class ValidateEpsgCRSs {

	public ValidateEpsgCRSs() {
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HSQLDataSource ds = new HSQLDataSource();
		Connection conn;
		try {
			conn = ds.getConnection();
		
			final PreparedStatement stmt;
	        stmt = conn.prepareStatement("SELECT COORD_REF_SYS_CODE"
	                                      + " FROM epsg_coordinatereferencesystem");
	        ResultSet result;
			
			result = stmt.executeQuery();
			
	        ArrayList res = new ArrayList();
	        ArrayList units = new ArrayList();
	        ArrayList validos = new ArrayList();
	        while (result.next()) {
	        	int cod = result.getInt("COORD_REF_SYS_CODE");
	        	
        		String strEpsgCode="EPSG:"+cod;		
        		CoordinateReferenceSystem source;
        		
        			try {
						source = CRS.decode(strEpsgCode);
						validos.add(String.valueOf(cod));
						if (source instanceof DefaultProjectedCRS) {
							String str;
							boolean esta = false;
							DefaultProjectedCRS sour = (DefaultProjectedCRS) source;
						
							Unit u = ((DefaultGeodeticDatum) sour.getDatum()).getPrimeMeridian().getAngularUnit();
							units.add(u.toString());
							
							/*for (int j = 0; j< units.size(); j++) {
					        	if (units.get(j).equals(u.toString())){
					        		esta = true;
					        		break;
					        	}						        	
					        }
							if (!esta){
								units.add(u.toString());
								esta = false;
							}*/
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
					}
	        }
	        for (int i = 0; i< res.size(); i++) {
	        	System.out.println(res.get(i));	        	
	        }	       
	        for (int i = 0; i< units.size(); i++) {
	        	System.out.println(units.get(i));
	        }
	        System.out.println("Numero CRSs validos: " + validos.size());
	        System.out.println("Numero CRSs fallidos: " + res.size());
	        System.out.println("Numero medidas: " + units.size());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
	}
		
}
			
			
			/*CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:4326");
			CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:23032");

			MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);
			System.out.println("Dimensiones: "+transform.getSourceDimensions());
			double points[] = {2.5,3.0};
			System.out.println("Puntos fuente: "+ points[0]+"  "+points[1]);
			transform.transform(points, 0, points, 0, 1);
			System.out.println("Puntos destino: "+ points[0]+"  "+points[1]);

			int epsgCount = 0;
			System.out.println("CRSs Disponibles en EPSG: \n");
			Set codes = CRS.getSupportedCodes("EPSG");
			for (Iterator iter = codes.iterator(); iter.hasNext();) {
			String code = (String) iter.next();
			System.out.println("EPSG:"+code);
			epsgCount++;
			}
			System.out.println("N�mero de CRSs en EPSG: "+epsgCount);
			try {
			System.out.println("Descripci�n de EPSG:23030:  \n"+CRS.getAuthorityFactory(false).getDescriptionText("EPSG:23030"));
			} catch (FactoryRegistryException e) {
//			 TODO Auto-generated catch block
			e.printStackTrace();
			} catch (FactoryException e) {
//			 TODO Auto-generated catch block
			e.printStackTrace();
			}
			
		} catch (NoSuchAuthorityCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/  

