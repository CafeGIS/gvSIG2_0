package org.gvsig.fmap.mapcontext.exceptions;


/**
 * @author Vicente Caballero Navarro
 */
public class ReloadLayerException extends LoadLayerException {

	public ReloadLayerException(String l,Throwable exception) {
		super(l,exception);
		init();
	}
	/**
	 *
	 */
	private void init() {
		messageKey = "error_reload_layer";
		formatString = "Can´t reload the layer: %(layer) ";
	}

}
