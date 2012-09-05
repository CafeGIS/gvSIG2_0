package org.gvsig.cacheservice;
/**
 * 
 * @author Rafa Gait�n <rgaitan@dsic.upv.es>
 *
 */
public class CacheServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CacheServiceException(Exception e) {
		super(e);
	}
}
