package org.gvsig.fmap.mapcontext.exceptions;

/**
 * @author Vicente Caballero Navarro
 */
public class ReprojectLayerException extends LoadLayerException {

	public ReprojectLayerException(String l,Throwable exception) {
		super(l,exception);
		init();
	}
	/**
	 *
	 */
	private void init() {
		messageKey = "error_projection_layer";
		formatString = "Can´t reproject the layer: %(layer) ";
	}

}