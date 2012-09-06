package org.gvsig.raster.buffer;

/**
 * Excepción lanzada cuando se detecta un acceso a un tipo de datos incorrecto
 * en un RasterBuffer.
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class RasterBufferInvalidAccessException extends Exception{

	private static final long serialVersionUID = -3065331336250589153L;

	public RasterBufferInvalidAccessException(String msg){
		super(msg);
	}
}