package org.gvsig.raster.grid;

/**
 * Excepción lanzada cuando se detecta que un buffer de raster es incorrecto.
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class OutOfGridException extends Exception{
	private static final long serialVersionUID = -8164541183139611665L;

	public OutOfGridException(String msg){
		super(msg);
	}
}