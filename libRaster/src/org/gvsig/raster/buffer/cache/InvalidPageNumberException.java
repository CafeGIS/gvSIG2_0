package org.gvsig.raster.buffer.cache;

/**
 * Excepci�n lanzada cuando el n�mero de p�gina al que se intenta acceder no es valido.
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class InvalidPageNumberException extends Exception{
	private static final long serialVersionUID = -4312548276663417111L;

	public InvalidPageNumberException(String msg){
		super(msg);
	}
}