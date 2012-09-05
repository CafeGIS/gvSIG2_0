package org.gvsig.fmap.mapcontext.exceptions;



/**
 * @author Vicente Caballero Navarro
 */
public class FileLayerException extends LoadLayerException {

	public FileLayerException(String l,Throwable exception) {
		super(l,exception);
		init();
	}
	/**
	 *
	 */
	private void init() {
		messageKey = "error_file_layer";
		formatString = "Can´t found the file of the layer: %(layer) ";
	}
}
