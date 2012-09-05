package org.gvsig.fmap.mapcontext.exceptions;


/**
 * @author Vicente Caballero Navarro
 */
public class ProjectionLayerException extends LoadLayerException {

	public ProjectionLayerException(String l,Throwable exception) {
		super(l,exception);
		init();
	}
	/**
	 *
	 */
	private void init() {
		messageKey = "error_projection_layer";
		formatString = "Can´t project the layer: %(layer) ";
	}

}
