package org.gvsig.raster.buffer;

/**
 * Excepción lanzada cuando se detecta que un buffer de raster es incorrecto.
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class RasterBufferInvalidException extends Exception{
	private static final long serialVersionUID = -1566113755637911586L;

	public RasterBufferInvalidException(String msg){
		super(msg);
	}
}