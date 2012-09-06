package org.gvsig.crs.ogr;

import org.gvsig.crs.CrsWkt;

public class Esri2wkt {

	CrsWkt datos = null;
	String cadWKT = "";
	String[] param_name = null;
	String[] param_value = null;
	
	public Esri2wkt(String wkt) {
		datos = new CrsWkt(wkt);		
		param_name = datos.getParam_name();
		param_value = datos.getParam_value();
	}
	
	/**
	 * Formacion de la cadena wkt con los nombres correctos de las proyecciones
	 * de esri para la libreria proj4.
	 * @return
	 */
	public String getWkt(){
		String proj = getNameProjectionWkt(datos.getProjection());
				
		cadWKT = "PROJCS[\""+ datos.getProjcs()+"\", GEOGCS[\"" + datos.getGeogcs() + "\", DATUM[\""+ datos.getDatumName() +
		"\", SPHEROID[\""+ datos.getSpheroid()[0] + "\", "+ datos.getSpheroid()[1] + ", "+ datos.getSpheroid()[2] +"]], " +
		"PRIMEM[\""+ datos.getPrimen()[0] + "\", "+ datos.getPrimen()[1] +"], UNIT[\""+ datos.getUnit()[0] + "\", " + (Math.PI/180) +
		"]], PROJECTION[\""+ proj + "\"], ";
			
		/*
		 * falta la parte de los parámetros... metodo para nombres...
		 */
		for (int i= 0; i< param_name.length;i++){
			cadWKT += "PARAMETER[\""+param_name[i]+"\", " + param_value[i]+ "], ";
		}
		
		cadWKT += "UNIT[\""+ datos.getUnit_p()[0] + "\", 1.0], ";
		cadWKT += "AUTHORITY[\""+ datos.getAuthority()[0] + "\", " + datos.getAuthority()[1] + "]]";
				
		return cadWKT;
	}
	
	/** 
	 * Metodo auxiliar para el cambio del nombre de la proyeccion.
	 */	
	private String getNameProjectionWkt(String projection) {
				
		if (projection.equals("Lambert_Conformal_Conic")){
			projection = "Lambert_Conformal_Conic_1SP";
			for (int i = 0; i < param_name.length; i++) {				
				if (param_name[i].equals("Standard_Parallel_2")) {
					projection = "Lambert_Conformal_Conic_2SP";
				}
			}			
		}				
		return projection;
	}
}
