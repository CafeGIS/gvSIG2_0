package org.gvsig.raster.buffer.cache;

/**
 * Excepción lanzada cuando el número de página al que se intenta acceder no es valido.
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class InvalidPageNumberException extends Exception{
	private static final long serialVersionUID = -4312548276663417111L;

	public InvalidPageNumberException(String msg){
		super(msg);
	}
}