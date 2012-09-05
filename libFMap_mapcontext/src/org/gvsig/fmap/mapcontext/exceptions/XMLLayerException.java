package org.gvsig.fmap.mapcontext.exceptions;

import java.util.Map;

import com.iver.utiles.XMLException;


/**
 * @author Vicente Caballero Navarro
 */
public class XMLLayerException extends XMLException {

	public XMLLayerException(String l,Throwable exception) {
		init();
	}
	/**
	 *
	 */
	private void init() {
		messageKey = "error_xml_layer";
		formatString = "Can´t save and open the layer: %(layer) ";
	}
	protected Map values() {
		// TODO Auto-generated method stub
		return null;
	}

}
