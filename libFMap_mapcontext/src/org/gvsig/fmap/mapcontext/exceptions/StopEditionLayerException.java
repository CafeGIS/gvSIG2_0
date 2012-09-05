package org.gvsig.fmap.mapcontext.exceptions;

import java.util.Hashtable;
import java.util.Map;

import org.gvsig.tools.exception.BaseException;
/**
 * @author Vicente Caballero Navarro
 */
public class StopEditionLayerException extends BaseException{

	private String layer;
	public StopEditionLayerException(String l,Throwable exception) {
		this.layer=l;
		init();
		initCause(exception);
	}
	/**
	 *
	 */
	private void init() {
		messageKey = "error_stop_editing_layer";
		formatString = "Can´t stop editing the layer: %(tag) ";
	}


	protected Map values() {
		Hashtable params = new Hashtable();
		params.put("layer",layer);
		return params;
	}

}
