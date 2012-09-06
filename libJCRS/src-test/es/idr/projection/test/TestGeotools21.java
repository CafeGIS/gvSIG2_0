package es.idr.projection.test;

import org.gvsig.crs.CrsException;
import org.gvsig.crs.CrsFactory;
import org.gvsig.crs.ICrs;
import org.gvsig.crs.Proj4;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

public class TestGeotools21 {
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {	        	
    		String strEpsgCode;
			// Notas:
			// - transverse mercator south orientated no esta soportada por Proj4
    		// - Buscar ejemplo de la CylindricalEqualArea
    		// - Buscar ejemplo de la AzimuthalEquidistant
    		// - Buscar ejemplo de la Gnomonic
    		// - Buscar ejemplo de la Bonne
    		// - Buscar ejemplo de la Equirectangular
    		// - Buscar ejemplo de la EquidistantConic
    		// - Buscar ejemplo de la MillerCylindrical
    		// - Buscar ejemplo de la Mollweide
    		// - Buscar ejemplo de la EckertIV
    		// - Buscar ejemplo de la EckertVI
    		// - Buscar ejemplo de la Robinson
    		// - Buscar ejemplo de la VanDerGrinten
    		// - Buscar ejemplo de la Sinusoidal
    		// - Buscar ejemplo de la GallStereographic
    		// - Buscar ejemplo de la Goode
			
			//strEpsgCode="EPSG:2065"; // Krovak
    		//strEpsgCode="EPSG:2052";  // tm south orientated No en mi Proj4
    		//strEpsgCode="EPSG:2084";  // tm south orientated
    		//strEpsgCode="EPSG:32662";  // Plate Carree  -> Funciona, problema elipsoide
    											// Tomara como radio el semieje mayor del elipsoide
    		//strEpsgCode="EPSG:27200";  // NewZealandMapGrid -> Mirar lo de +datum
    		// He tenido que incluir un mecanismo para valores concretos de parametros
    		strEpsgCode="EPSG:32761";  // CassiniSoldner -> Falla +to_meter=..
    		//strEpsgCode="EPSG:2099";
    		//strEpsgCode="EPSG:2200";  // en lugar de stere pone sterea,igual 2171
    									// 2036 - falla, 32661
    		//strEpsgCode="EPSG:2163";    // Laea esferica, funciona
    		//strEpsgCode="EPSG:3035";    // ETRS-LAEA, funciona
    		//strEpsgCode="EPSG:29100";	// Polyconic, falla en geotools
    		//strEpsgCode="EPSG:2964";	// Albers-conic-equal-area
    		                            // debería fallar por cambio de unidad
    		//strEpsgCode="EPSG:24571";    // Oblicua de mercator, rf mas decimales
    		                               // quitar segundo alpha
    		//strEpsgCode="EPSG:29700";    // Oblicua de mercator, rf mas decimales
    		//strEpsgCode="EPSG:21780";		//somer, sin alpha
    		ICrs crs=null;
    		//CoordinateReferenceSystem source;
    		//source = CRS.decode(strEpsgCode);
        	crs = new CrsFactory().getCRS(strEpsgCode); 
    		
        	System.out.println(crs.getWKT());        	
			System.out.println(crs.getProj4String()); //proj4.exportToProj4(crs));
				
					
		} catch (CrsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
