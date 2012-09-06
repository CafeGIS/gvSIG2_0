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

package org.gvsig.crs;

import org.cresques.cts.IProjection;
import org.gvsig.crs.proj.CrsProj;


/**
 * Interface para un Sistema de Referencuia Coordenado:
 *  CRS (Coordinate Reference Sysytem).
 * @author José Luis Gómez Martínez (jolugomar@gmail.com)
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 *
 */
public interface ICrs extends IProjection {
	
	/**
	 * Código del CRS
	 * @return
	 */
	int getCode();
	/**
	 * Cadena WKT del CRS utilizado
	 * @return
	 */
	String getWKT();
	
	/**
	 * Devuelve la cadena con el parámetro nadgrid para proj4
	 * 
	 * @return
	 */
	//String getTransformationParams();
	
	/**
	 * Establece los parámetros de transformación para el CRS fuente y para 
	 * el destino
	 * 
	 * @param sourceParams Parámetros de transformación (proj4) en el CRS fuente.
	 * <code>null</code> si no se quiere asignar parámetros en la fuente.
	 * @param TargetParams Parámetros de transformación (proj4) en el CRS destino.
	 * <code>null</code> si no se quiere asignar parámetros en el destino.
	 */
	void setTransformationParams(String sourceParams, String TargetParams);

	/**
	 * Le indicamos al crs si el nadgrid está asociado al crs destion al fuente
	 * en una transformación.
	 * @param targetNad true si el nadgrid se asocia al crs destino
	 * @return
	 */
	//void setParamsInTarget(boolean targetNad);
	/**
	 * Devuelve los parametros de la transformacion del crs fuente
	 * @return
	 */
	public String getSourceTransformationParams();	
	
	/**
	 * Devuelve los parametros de la transformacion del crs destino
	 * @return
	 */
	public String getTargetTransformationParams();		
	
	/**
	 * Campos de la cadena wkt
	 * @return
	 */
	CrsWkt getCrsWkt();
	
	CrsProj getCrsProj();
	
	/**
	 * 
	 * @return Cadena Proj4 correspondiente al CRS.
	 * @throws CrsException 
	 */
	String getProj4String() throws CrsException;
}
