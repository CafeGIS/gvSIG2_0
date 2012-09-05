package org.gvsig.fmap.mapcontext.exceptions;


/**
 * @author Vicente Caballero Navarro
 */
public class NameLayerException extends LoadLayerException {

	public NameLayerException(String l,Throwable exception) {
		super(l,exception);
		init();
	}
	/**
	 *
	 */
	private void init() {
		messageKey = "error_name_layer";
		formatString = "Can´t named the layer: %(layer) ";
	}

}
