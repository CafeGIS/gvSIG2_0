package com.iver.cit.gvsig.fmap.exceptions;


/**
* <p>
* Exception thrown when requested projection is not supported by
* Cresques package. We can not reproject LatLon BBox to serve the
* layer in the requested coordenate system
* </p>
* @author Laura
*
*/
public class CannotReprojectExceptionOld extends Exception {
   public CannotReprojectExceptionOld(){
       super();
   }
   
   public CannotReprojectExceptionOld(String message){
       super(message);
   }
   
	/**
	 * Create CanNotReprojectException.
	 *
	 * @param message
	 * @param cause
	 */
	public CannotReprojectExceptionOld(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	  * Crea CanNotReprojectException.
	 *
	 * @param cause
	 */
	public CannotReprojectExceptionOld(Throwable cause) {
		super(cause);
	}   
}
