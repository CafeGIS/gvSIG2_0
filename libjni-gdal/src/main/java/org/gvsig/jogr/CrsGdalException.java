package org.gvsig.jogr;

public class CrsGdalException extends Exception{
	
	public CrsGdalException(){
		getStrError();
	}
	
	public String getStrError(){
		return ("I can't create object");
	}
}
