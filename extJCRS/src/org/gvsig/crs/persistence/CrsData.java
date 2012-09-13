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

package org.gvsig.crs.persistence;

import java.util.Date;
import java.util.Properties;

import com.iver.utiles.DateTime;

/**
 * Esta clase contiene los datos de un CRS que son utilizados 
 * por el mecanismo de persistencia para almacenar los CRSs
 * recientemente utilizados. 
 * 
 * @author Diego Guerrero Sevilla diego.guerrero@uclm.es
 */
public class CrsData implements Comparable{
	
	private String authority = null;
	private int code = 0;
	private String name = null;
	private Date date = null;
	private Properties properies = new Properties();
	
	
	
	public CrsData(String authority, int code, String name) {
		super();
		// TODO Auto-generated constructor stub
		this.authority = authority;
		this.code = code;
		this.name = name;
		this.date = DateTime.getCurrentDate();
	}
	
	public CrsData(String authority, int code, String name, Date date) {
		super();
		// TODO Auto-generated constructor stub
		this.authority = authority;
		this.code = code;
		this.name = name;
		this.date = date;
	}

	public CrsData() {
		// TODO Auto-generated constructor stub
	}
	
	public String getAuthority() {
		return authority;
	}
	
	public int getCode() {
		return code;
	}
	
	public String getName() {
		return name;
	}
	
	/**
	 * @return Returns the properies.
	 */
	public Properties getProperies() {
		return properies;
	}

	/**
	 * @param properies The properies to set.
	 */
	public void setProperies(Properties properies) {
		this.properies = properies;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	} 
	
	public void updateLastAccess() {        
		date = DateTime.getCurrentDate();
	}

	public int compareTo(Object o) {
		CrsData crsData = (CrsData)o;
		return this.date.compareTo(crsData.getDate());
	} 
}
