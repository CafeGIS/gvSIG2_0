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

package org.gvsig.crs.ogr;

import java.sql.ResultSet;
import java.sql.SQLException;


import es.idr.teledeteccion.connection.EpsgConnection;
import es.idr.teledeteccion.connection.Query;

/**
 * Clase que consigue los parámetros necesarios de la base de
 * datos de la EPSG una vez escogido un CRS, y que serán
 * los que se utilicen para la generación de la cadena wkt
 * correcta para generar el CRS.
 * 
 * @author José Luis Gómez Martínez (jolugomar@gmail.com)
 *
 */
public class CrsEPSG {
	
	int epsg_code;
	boolean crs_source;
	int source_code;
	int projection_code;	
	int coord_op_code;	
	
		
	int datum_code;
	int ellipsoid_code, prime_meridian_code;
	
	String PROJCS = null;
	String GEOGCS = null;
	String DATUM = null;
	String[] SPHEROID = null;
	String[] PRIMEM = null;
	String UNIT_A = "degree";
	String UNIT_B = "Meter";
	double unit_BValue = 1.0;
	String PROJECTION = null;
	//ArrayList PARAMETER = null;
	String[] param_name = null;
	String[] param_value = null;
	String[] AUTHORITY = null;
	EpsgConnection connect;
	private int coordSysCode;
	
	public CrsEPSG(){		
	}	
	
	public CrsEPSG(int code, boolean source_yn, int source_cod, int coord_op_cod, EpsgConnection conn){
		epsg_code = code;
		crs_source = source_yn;
		source_code = source_cod;		
		coord_op_code = coord_op_cod;
		connect = conn;
		SPHEROID = new String[3];
		PRIMEM = new String[2];
		AUTHORITY = new String[2];
		initialize();
	}
	
	public void initialize(){
		ResultSet result;
		ResultSet result2;
		/*
		 * Si es base solamente realizamos una consulta, y ya tenemos el Datum
		 * (ver en caso de ser fuente que PROJCS no tiene que estar definido)
		 */
		if (crs_source){
			String sentence = "SELECT coord_ref_sys_name, datum_code, coord_sys_code " +
							  "FROM epsg_coordinatereferencesystem " +	                              
							  "WHERE coord_ref_sys_code = " + epsg_code;
			result = Query.select(sentence,connect.getConnection());
			try {
				while (result.next()){
					GEOGCS = result.getString("coord_ref_sys_name");
					datum_code = Integer.parseInt(result.getString("datum_code"));
					coordSysCode = result.getInt("coord_sys_code");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}			
		}
		else{
			String sentence = "SELECT coord_ref_sys_name, coord_sys_code " +
							  "FROM epsg_coordinatereferencesystem " +	                              
							  "WHERE coord_ref_sys_code = " + epsg_code;
			result = Query.select(sentence,connect.getConnection());
			
			String sentence2 = "SELECT coord_ref_sys_name, datum_code " +
							  "FROM epsg_coordinatereferencesystem " +	                              
							  "WHERE coord_ref_sys_code = " + source_code;
			result2 = Query.select(sentence2,connect.getConnection());
			
			try {
				while (result.next()){
					PROJCS = result.getString("coord_ref_sys_name");
					coordSysCode = result.getInt("coord_sys_code");
				}
				while (result2.next()){
					datum_code = Integer.parseInt(result2.getString("datum_code"));
					GEOGCS = result2.getString("coord_ref_sys_name");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		/*
		 * hasta aqui hemos rellenado projcs, geocs y tenemos el codigo de datum
		 * vamos a obtener los códigos del elipsoide y el prime_meridian
		 */
		String sentence = "SELECT datum_name, ellipsoid_code, prime_meridian_code " +
						  "FROM epsg_datum " +	                              
						  "WHERE datum_code = " + datum_code;
		result = Query.select(sentence,connect.getConnection());
		try {
			while (result.next()){
				DATUM = result.getString("datum_name");
				ellipsoid_code = Integer.parseInt(result.getString("ellipsoid_code"));	
				prime_meridian_code = Integer.parseInt(result.getString("prime_meridian_code"));
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		/*
		 * Ahora vamos a coger varios campos del elipsoide y operaremos con ellos, si la
		 * información de los mismos no esta en las unidades de medida que utilizaremos.
		 */
		sentence = "SELECT ellipsoid_name, semi_major_axis, inv_flattening, uom_code, " +
					"semi_minor_axis, ellipsoid_shape " +
					  "FROM epsg_ellipsoid " +	                              
					  "WHERE ellipsoid_code = " + ellipsoid_code;
		result = Query.select(sentence,connect.getConnection());
		
		SPHEROID = getEllipsoid(result);
		
		sentence = "SELECT prime_meridian_name, greenwich_longitude, uom_code " +
					  "FROM epsg_primemeridian " +	                              
					  "WHERE prime_meridian_code = " + prime_meridian_code;
		result = Query.select(sentence,connect.getConnection());
		
		PRIMEM = getPrimeMeridian(result);
		AUTHORITY = getAuthority(epsg_code);
		/*
		 * si tiene proyección porque es proyectado, la buscamos
		 */
		if (!crs_source){			
			sentence = "SELECT coord_op_method_code " +
						"FROM epsg_coordoperation " +
						"WHERE coord_op_code = " + coord_op_code;
			result = Query.select(sentence,connect.getConnection());
			try {
				while (result.next()){
					projection_code = result.getInt("coord_op_method_code");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			
			sentence = "SELECT coord_op_method_name " +
						  "FROM epsg_coordoperationmethod " +	                              
						  "WHERE coord_op_method_code = " + projection_code;
			result = Query.select(sentence,connect.getConnection());
			try {
				while (result.next()){
					PROJECTION = result.getString("coord_op_method_name");				
				}			
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			parameters(projection_code);
			setUnit_B(coordSysCode);
		}
		/*
		 * Una vez que tengo todo, hago la llamada al constructor de Epsg2wkt para pasarle los
		 * parametros y realizar la cadena
		 * CAMBIARLO Y PONER METODOS getXXX para todos los parametros para hacerlo mas generico
		 	
		if (crs_source){
			cadwkt = new Epsg2wkt(0);
		}
		else{
			cadwkt = new Epsg2wkt();
		}
		*/
	}
	
	/**
	 * Cuando recuperamos el elipsoide del CRS que hemos seleccionado,
	 * buscamos sus parámetros, los pasamos a las unidades correctas
	 * y devolvemos un array de string con el nombre y los valores
	 * del esferoide.
	 * @param result
	 * @return
	 */
	private String[] getEllipsoid(ResultSet result) {
		String[] spheroid = new String[3];		
		double semi_major_axis = 0;
		double semi_minor_axis = 0;
		double inv_flattening = 0;
		int uom_code = 0;
		int ellipsoid_shape = 0;
		
		try {
			while (result.next()){
				spheroid[0] = result.getString("ellipsoid_name");
				semi_major_axis = result.getDouble("semi_major_axis");
				semi_minor_axis = result.getDouble("semi_minor_axis");
				inv_flattening = result.getDouble("inv_flattening");
				uom_code = result.getInt("uom_code");
				ellipsoid_shape = result.getInt("ellipsoid_shape");
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		/*
		 * pasaremos a metros las unidades del semi_major_axis, semi_minor_axis
		 */
		String sentence = "SELECT factor_b, factor_c " +
						  "FROM epsg_unitofmeasure " +	                              
						  "WHERE uom_code = " + uom_code;
		ResultSet result2 = Query.select(sentence,connect.getConnection());
		
		double factor_b = 0;
		double factor_c = 0;
		try{			
			while (result2.next()){
				factor_b = result2.getDouble("factor_b");
				factor_c = result2.getDouble("factor_c");				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (factor_b != 0 && factor_c != 0 ){
			semi_major_axis = (semi_major_axis * factor_b)/ factor_c;
			if (semi_minor_axis != 0){
				semi_minor_axis = (semi_minor_axis * factor_b) / factor_c;
			}
		}
		/*
		 * calculamos inv_flattening en caso de que sea nulo
		 */
		if (inv_flattening == 0){
			if (ellipsoid_shape == 0){
				inv_flattening = 0;
			}
			else{
				inv_flattening = (semi_major_axis) / (semi_major_axis - semi_minor_axis);
			}
		}		
		spheroid[1] = ""+semi_major_axis;
		spheroid[2] = ""+inv_flattening;
		
		return spheroid;
	}

	/**
	 * Este método acepta un result en el que tenemos los parámetros
	 * del PrimeMeridian, se hace la conversión a las unidades correctas
	 * y se devuelve un array de String con el nombre y los parámetros 
	 * pertenecientes a PrimeMeridian.
	 * @param result
	 * @return
	 */
	private String[] getPrimeMeridian(ResultSet result) {
		String[] primem = new String[2];
		double greenwich_longitude = 0;
		
		try {
			while (result.next()){
				primem[0] = result.getString("prime_meridian_name");
				greenwich_longitude = result.getDouble("greenwich_longitude");
				int co= Integer.parseInt((String)result.getString("uom_code"));
				/*
				 * Realizamos este cambio de meridiano origen de la unidad de la epsg
				 * a degrees, para insertarlo en este formato en wkt
				 */
				ResultSet result2 = null;
				if (co != 9110){
					String sentence = "SELECT factor_b, factor_c " +
					  "FROM epsg_unitofmeasure " +	                              
					  "WHERE uom_code = " + co;
					result2 = Query.select(sentence,connect.getConnection());
					
					while(result.next()){						
						greenwich_longitude = ((greenwich_longitude * result2.getDouble("factor_b") )/ result2.getDouble("factor_c")) * (180.0 / Math.PI);
					}					
				} else {
					greenwich_longitude = Dms2Deg(greenwich_longitude);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		primem[1] = ""+greenwich_longitude;
		return primem;
	}
	
	/**
	 * Método para bucar los parámetros de la proyección que
	 * se está utilizando. Se rellenan las variables param_name y
	 * param_value con los datos de la proyección utilizada.
	 * @param proj
	 */
	private void parameters(int proj) {
		/*
		 * para la consecucion de los parametros se tiene que ir buscando dependiendo de la
		 * proyeccion que se utilice, hay que ver que parametros exactos tiene cada uno de ellos
		 * y si hay mas de 17, que son los que se utilizan en WKT, como vamos a tratarlos, mensaje
		 * de error, intentar no mostrarlos, o simplemente pasar de ellos
		 */		
					
		ResultSet result;
		ResultSet result2;
		ResultSet result3;
		
		/*
		 * Conseguimos la lista de parametros y valores, ordenados segun sort_order de manera
		 * ascendente por lo que coincidiram siempre param_name[i] y param_value[i]
		 */
		String sentence = "SELECT COUNT(*) " +
				"FROM epsg_coordoperationparamusage " +
				"WHERE coord_op_method_code = " + proj;
		result = Query.select(sentence,connect.getConnection());
		
		int count = 0;
		try {
			result.next();
			count = result.getInt(1);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}		
		param_name = new String[count];
		param_value = new String[count];
		
		sentence = "SELECT parameter_code " +
		"FROM epsg_coordoperationparamusage " +
		"WHERE coord_op_method_code = " + proj + " " +
				"ORDER BY sort_order ASC";
		result = Query.select(sentence,connect.getConnection());
		
		int i = 0;
		try {
			while (result.next()) {
				
				int cod = result.getInt("parameter_code");
				/*
				 * con el codigo del parametro, obtenemos tanto su nombre como su valor
				 */
				sentence = "SELECT parameter_name " +
						"FROM epsg_coordoperationparam " +
						"WHERE parameter_code = " + cod;
				result2 = Query.select(sentence,connect.getConnection());
				
				result2.next();
				param_name[i] = result2.getString("parameter_name");
				
				sentence = "SELECT parameter_value, uom_code " +
						"FROM epsg_coordoperationparamvalue " +
						"WHERE parameter_code = " + cod + " AND coord_op_code = " + coord_op_code;
				result3 = Query.select(sentence,connect.getConnection());
				
				result3.next();
				double param_val = result3.getDouble("parameter_value");
				int uom_code = result3.getInt("uom_code");
				
				sentence = "SELECT factor_b, factor_c, unit_of_meas_type " +
						  "FROM epsg_unitofmeasure " +	                              
						  "WHERE uom_code = " + uom_code;
				ResultSet result4 = Query.select(sentence,connect.getConnection());
				
				double factor_b = 0;
				double factor_c = 0;				
				result4.next();
				String type = result4.getString("unit_of_meas_type");
				factor_b = result4.getDouble("factor_b");
				factor_c = result4.getDouble("factor_c");				
								
				if (factor_b != 0 && factor_c != 0 && !type.equals("angle")){
					param_val = (param_val * factor_b) / factor_c;
				}
				else if(factor_b != 0 && factor_c != 0 && type.equals("angle")){
					param_val = ((param_val * factor_b) / factor_c) * (180.0 / Math.PI);;
				}else if (uom_code == 9110){
					param_val = especialDegree(param_val);
					param_val = Math.toDegrees(param_val);
				}else {
					System.out.println("Código de medida no válido...");					
				}				
				param_value[i] = ""+param_val;
				
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
			
	}	
	
	/**
	 * Método para pasar a las unidades correctas el valor de los distintos
	 * parámetros de la proyección.
	 * @param val
	 * @return
	 */
	private double especialDegree(double val){
		if(val==0)
			return val;
		int signo = 1;
		if (val < 0){
			signo = -1;
			val = Math.abs(val);
		}		
		String strValue=String.valueOf(val);
		int posPto=strValue.indexOf('.');
		double grad=Double.parseDouble(strValue.substring(0,posPto));
		String strFraction=strValue.substring(posPto+1);
		double min=0;
		double sec=0;
		if(strFraction.length()==1)
		{
			min=Double.parseDouble(strFraction);
			min*=10;
		}
		if(strFraction.length()==2)
		{
			min=Double.parseDouble(strFraction);
		}
		if(strFraction.length()>2)
		{
			min=Double.parseDouble(strFraction.substring(0,2));
			sec=Double.parseDouble(strFraction.substring(2));
			if(strFraction.length()==3)
				sec*=10;
			if(strFraction.length()>4){
				for(int i=0;i<strFraction.length()-4;i++)
					sec=sec/10.0;
			}
			
		}
				
		val = ((grad + (min/60.0) + (sec/3600.0)) * (Math.PI/180.0)) * signo;
		
		return val;
	}	
	
	/**
	 * Método para pasar a las unidades correctas el valor del prime meridian.
	 * @param val
	 * @return
	 */
	private double Dms2Deg(double val){
		if(val==0)
			return val;
		int signo = 1;
		if (val < 0){
			signo = -1;
			val = Math.abs(val);
		}		
		String strValue=String.valueOf(val);
		int posPto=strValue.indexOf('.');
		double grad=Double.parseDouble(strValue.substring(0,posPto));
		String strFraction=strValue.substring(posPto+1);
		double min=0;
		double sec=0;
		if(strFraction.length()==1)
		{
			min=Double.parseDouble(strFraction);
			min*=10;
		}
		if(strFraction.length()==2)
		{
			min=Double.parseDouble(strFraction);
		}
		if(strFraction.length()>2)
		{
			min=Double.parseDouble(strFraction.substring(0,2));
			sec=Double.parseDouble(strFraction.substring(2));
			if(strFraction.length()==3)
				sec*=10;
			if(strFraction.length()>4){
				for(int i=0;i<strFraction.length()-4;i++)
					sec=sec/10.0;
			}
			
		}	
	
		val = ((grad + (min/60.0) + (sec/3600.0)) ) * signo;
		
		return val;
	}	
	
	/**
	 * Genera el authority del CRS
	 * @param epsg_cod
	 * @return
	 */	
	private String[] getAuthority(int epsg_cod) {
		String[] aut = new String[2];
		aut[0] = "EPSG";
		aut[1] = ""+epsg_cod;
		return aut;
	}
	
	public String getPROJCS(){
		return PROJCS;
	}
	
	public String getGEOGCS(){
		return GEOGCS;
	}
	
	public String getDATUM(){
		return DATUM;
	}
	
	public String[] getSPHEROID(){
		return SPHEROID;
	}
	
	public String[] getPRIMEM(){
		return PRIMEM;
	}
	
	public void setUnit_B(int code) {
		int uom = 0;
		String sentence = "SELECT uom_code " +
						"FROM epsg_coordinateaxis " +
						"WHERE coord_sys_code = " + code;
		
		ResultSet result = Query.select(sentence,connect.getConnection());
		/**
		 * Recuperar el nombre de la unidad para poder ponerlo en la cadena
		 * wkt
		 */
		try {
			if (result.next()) {
				uom = result.getInt("uom_code");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sentence = "SELECT unit_of_meas_name " +
		"FROM epsg_unitofmeasure " +
		"WHERE uom_code = " + uom;
		
		result = Query.select(sentence,connect.getConnection());
		
		try {
			if (result.next()) {
				UNIT_B = result.getString("unit_of_meas_name");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setUnit_BValue(uom);
	}
	
	private void setUnit_BValue(int uom) {
		if (uom != 9001) {
			double factor_b =0.0;
			double factor_c = 0.0;
			String sentence = "SELECT factor_b, factor_c " +
			"FROM epsg_unitofmeasure " +
			"WHERE uom_code = " + uom;
			
			ResultSet result = Query.select(sentence,connect.getConnection());
			try {
				if (result.next()) {
					factor_b = result.getDouble("factor_b");
					factor_c = result.getDouble("factor_c");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			unit_BValue = (unit_BValue * factor_b) / factor_c;
			if (uom != 9002) {
				UNIT_B = "[m*"+unit_BValue+"]";				 
			} else
				UNIT_B = "ft";
		}
	}
		
	public String getUNIT_A(){
		return UNIT_A;
	}
	
	public String getPROJECTION(){
		return PROJECTION;
	}
	
	public String[] getParam_name(){
		return param_name;
	}
	
	public String[] getParam_value(){
		return param_value;
	}
	
	public String getUNIT_B(){
		return UNIT_B;
	}	
	
	public String getUnit_BValue() {
		return ""+unit_BValue;
	}
	
	public String[] getAUTHORITY(){
		return AUTHORITY;
	}
}