package es.idr.test;

import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class TestGeotools21 {
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {	        	
    		String strEpsgCode="EPSG:27572";
    		CoordinateReferenceSystem source;
    		source = CRS.decode(strEpsgCode);
        	System.out.println(source.toWKT());
    		
		} catch (NoSuchAuthorityCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
