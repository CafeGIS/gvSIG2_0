
/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
package org.gvsig.catalog.querys;

/**
 * This class represents a symple double pair of coordinates
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class Coordinates {
	public String ulx;
	public String uly;
	public String brx;
	public String bry;

	/**
	 * @param ulx 
	 * @param uly 
	 * @param brx 
	 * @param bry 
	 */
	public  Coordinates(String ulx, String uly, String brx, String bry) {        
		this.ulx = ulx;
		this.uly = uly;
		this.brx = brx;
		this.bry = bry;
	} 

	/**
	 * 
	 * 
	 * 
	 * @param ulx 
	 * @param uly 
	 * @param brx 
	 * @param bry 
	 */
	public  Coordinates(double ulx, double uly, double brx, double bry) {        
		this.ulx = String.valueOf(ulx);
		this.uly = String.valueOf(uly);
		this.brx = String.valueOf(brx);
		this.bry = String.valueOf(bry);
	} 

	/**
	 * 
	 * 
	 * 
	 * @return Returns the brx.
	 */
	public String getBrx() {        
		return brx;
	} 

	/**
	 * 
	 * 
	 * 
	 * @return 
	 */
	public double getDoubleBrx() {        
		return Double.valueOf(brx).doubleValue();
	} 

	/**
	 * 
	 * 
	 * 
	 * @param brx The brx to set.
	 */
	public void setBrx(String brx) {        
		this.brx = brx;
	} 

	/**
	 * 
	 * 
	 * 
	 * @return Returns the bry.
	 */
	public String getBry() {        
		return bry;
	} 

	/**
	 * 
	 * 
	 * 
	 * @return 
	 */
	public double getDoubleBry() {        
		return Double.valueOf(bry).doubleValue();
	} 

	/**
	 * 
	 * 
	 * 
	 * @param bry The bry to set.
	 */
	public void setBry(String bry) {        
		this.bry = bry;
	} 

	/**
	 * 
	 * 
	 * 
	 * @return Returns the ulx.
	 */
	public String getUlx() {        
		return ulx;
	} 

	/**
	 * 
	 * 
	 * 
	 * @return 
	 */
	public double getDoubleUlx() {        
		return Double.valueOf(ulx).doubleValue();
	} 

	/**
	 * 
	 * 
	 * 
	 * @param ulx The ulx to set.
	 */
	public void setUlx(String ulx) {        
		this.ulx = ulx;
	} 

	/**
	 * 
	 * 
	 * 
	 * @return Returns the uly.
	 */
	public String getUly() {        
		return uly;
	} 

	/**
	 * 
	 * 
	 * 
	 * @return 
	 */
	public double getDoubleUly() {        
		return Double.valueOf(uly).doubleValue();
	} 

	/**
	 * 
	 * 
	 * 
	 * @param uly The uly to set.
	 */
	public void setUly(String uly) {        
		this.uly = uly;
	} 
}
