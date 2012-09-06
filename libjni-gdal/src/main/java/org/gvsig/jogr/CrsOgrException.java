package org.gvsig.jogr;


public class CrsOgrException extends Exception {
	
	public CrsOgrException(){
	}
	
	public String getStrError(){
		return ("Error to export Proj4 string");
	}
}
