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
 * Clase para conseguir la información de la transformación elegida
 * de la EPSG
 * 
 * @author José Luis Gómez Martínez (jolugomar@gmail.com)
 *
 */

public class TransEPSG {
	int parameter_code = 0;
	int coord_op_method_code = 0;
	boolean inverseTransformation = false;
	
	public EpsgConnection connect;
	
	double[] param_value_double = {0,0,0,0,0,0,0};
	String[] param_value = {"0","0","0","0","0","0","0"};
	String[] param_name = {"0","0","0","0","0","0","0"};

	/**
	 * Acepta el código de la transformación a utilizar, y se
	 * recupera la información necesaria para la generación del CRS
	 * @param coord_op_code
	 * @param conn
	 * @param invTr
	 */
	public TransEPSG(int coord_op_code, EpsgConnection conn, boolean invTr){
		inverseTransformation = invTr;
		ResultSet result;
		connect = conn;
		int uom_code = 0;				
		String sentence = "SELECT coord_op_method_code, parameter_code, parameter_value, uom_code " +
				"FROM epsg_coordoperationparamvalue " +
				"WHERE coord_op_code = " + coord_op_code;
		result = Query.select(sentence,connect.getConnection());
		int i = 0;
		try {
			while (result.next()){
				coord_op_method_code = result.getInt("coord_op_method_code");
				parameter_code = result.getInt("parameter_code");
				
				double param_val = result.getDouble("parameter_value");				
								
				sentence = "SELECT parameter_name " +
							"FROM epsg_coordoperationparam " +
							"WHERE parameter_code = " + parameter_code;
				ResultSet result2 = Query.select(sentence,connect.getConnection());
		
				result2.next();
				param_name[i] = result2.getString("parameter_name");
				uom_code = result.getInt("uom_code");
				if (uom_code != 0){
					sentence = "SELECT factor_b, factor_c, unit_of_meas_type " +
					  "FROM epsg_unitofmeasure " +	                              
					  "WHERE uom_code = " + uom_code;
					ResultSet result3 = Query.select(sentence,connect.getConnection());
					
					double factor_b = 0;
					double factor_c = 0;				
					result3.next();
					String type = result3.getString("unit_of_meas_type");
					factor_b = result3.getDouble("factor_b");
					factor_c = result3.getDouble("factor_c");				
					
					if (uom_code == 9202){}
					else {
						if (factor_b != 0 && factor_c != 0 && !type.equals("angle")){						
							param_val = (param_val * factor_b) / factor_c;
							if (type.equals("scale")) 
								param_val = (param_val -1) * 1000000;						
						}
						else if(factor_b != 0 && factor_c != 0 && type.equals("angle")){
							param_val = ((param_val * factor_b) / factor_c) * (180.0 / Math.PI);
							param_val = param_val * 3600;
						}else if (uom_code == 9110){
							param_val = especialDegree(param_val);
							param_val = Math.toDegrees(param_val);
							param_val = param_val * 3600;
						}else {
							System.out.println("Código de medida no válido...");					
						}
					}
					param_value_double[i] = param_val;
					i++;
				}
			}
			
			if (inverseTransformation) {
				for (int j = 0; j < param_value_double.length; j++){
					param_value_double[j] = param_value_double[j] * -1.0;
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for (int k = 0; k < param_value_double.length; k++){
			param_value[k] = ""+ String.valueOf(param_value_double[k]);
		}
	}
	
	/**
	 * Método para pasar a las unidades correctas el valor de los distintos
	 * parámetros de la proyección.
	 * @param val
	 * @return
	 */
	private double especialDegree(double val){
		int signo = 1;
		double grad, min;
		double sec;
		
		if (val < 0){
			signo = -1;
			val = Math.abs(val);
		}		
		
		grad = Math.floor(val);
		val=(val-grad)*100.0;
		min=Math.floor(val);
		sec=(val-min)*100.0;
		
		val = ((grad + (min/60.0) + (sec/3600.0)) * (Math.PI/180.0)) * signo;
		
		return val;
	}
	
	private float round(double f,int i)
	{
		double d=Math.pow(10.0,(double) i);
		double aux=f*d;
		int auxi=(int) aux;
		float df=auxi/((float) d);
		return df;
	}
	
	public String[] getParamName(){
		return param_name;
	}
	
	public String[] getParamValue(){
		return param_value;
	}

}
