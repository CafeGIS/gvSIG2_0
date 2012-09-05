package org.gvsig.fmap.mapcontext.exceptions;


/**
 * @author Vicente Caballero Navarro
 */
public class DriverLayerException extends LoadLayerException {

	public DriverLayerException(String l,Throwable exception) {
		super(l,exception);
		init();
	}
	/**
	 *
	 */
	private void init() {
		messageKey = "error_driver_layer";
		formatString = "Can´t load driver of the layer: %(layer) ";
	}
}
