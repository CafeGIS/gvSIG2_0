/* jdwglib. Java Library for reading Dwg files.
 * 
 * Author: Jose Morell Rama (jose.morell@gmail.com).
 * Port from the Pythoncad Dwg library by Art Haas.
 *
 * Copyright (C) 2005 Jose Morell, IVER TI S.A. and Generalitat Valenciana
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
 * Jose Morell (jose.morell@gmail.com)
 * 
 * or
 *
 * IVER TI S.A.
 *  C/Salamanca, 50
 *  46005 Valencia
 *  Spain
 *  +34 963163400
 *  dac@iver.es
 */
package org.gvsig.dwg.lib;

/**
 * Entry in the CLASSES section of a dwg file.
 * @author azabala
 */
public class DwgClass {
	
	int classNum;
    int version;
    String appName;
    String cPlusPlusName;
    String dxfName;
    boolean isZombie;
    int id;
    
    
    public DwgClass(int classNum, int version, String appName,
    		String cPlusPlusName, String dxfName, boolean isZombie, int id){
    	
    	this.classNum = classNum;
        this.version = version;
        this.appName = appName;
        this.cPlusPlusName = cPlusPlusName;
        this.dxfName = dxfName;
        this.isZombie = isZombie;
        this.id = id;
    }
    
    public String toString(){
    	String solution = "";
    	solution += "classNum ="+classNum;
    	solution += ", version = "+version;
    	solution += ", appName = "+appName;
    	solution += ", c++Name = "+cPlusPlusName;
    	solution += ", dxfName = "+dxfName;
    	solution += ", isZombie = " + isZombie;
    	solution += ", id = " + id;
    	return solution;
    }
    
    
    
    
	/**
	 * @return Returns the appName.
	 */
	public String getAppName() {
		return appName;
	}
	/**
	 * @param appName The appName to set.
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}
	/**
	 * @return Returns the classNum.
	 */
	public int getClassNum() {
		return classNum;
	}
	/**
	 * @param classNum The classNum to set.
	 */
	public void setClassNum(int classNum) {
		this.classNum = classNum;
	}
	/**
	 * @return Returns the cPlusPlusName.
	 */
	public String getCPlusPlusName() {
		return cPlusPlusName;
	}
	/**
	 * @param plusPlusName The cPlusPlusName to set.
	 */
	public void setCPlusPlusName(String plusPlusName) {
		cPlusPlusName = plusPlusName;
	}
	/**
	 * @return Returns the dxfName.
	 */
	public String getDxfName() {
		return dxfName;
	}
	/**
	 * @param dxfName The dxfName to set.
	 */
	public void setDxfName(String dxfName) {
		this.dxfName = dxfName;
	}
	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return Returns the isZombie.
	 */
	public boolean isZombie() {
		return isZombie;
	}
	/**
	 * @param isZombie The isZombie to set.
	 */
	public void setZombie(boolean isZombie) {
		this.isZombie = isZombie;
	}
	/**
	 * @return Returns the version.
	 */
	public int getVersion() {
		return version;
	}
	/**
	 * @param version The version to set.
	 */
	public void setVersion(int version) {
		this.version = version;
	}
   
    
    
	
	
	
	
	
}
