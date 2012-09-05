package org.gvsig.fmap.mapcontext.exceptions;

import java.util.Hashtable;
import java.util.Map;

import org.gvsig.tools.exception.BaseException;
/**
 * @author Vicente Caballero Navarro
 */
public class StartEditionLayerException extends BaseException {
	private String layer;
	public StartEditionLayerException(String l,Throwable exception) {
		this.layer=l;
		init();
		initCause(exception);
	}
	/**
	 *
	 */
	private void init() {
		messageKey = "error_start_editing_layer";
		formatString = "Can´t start editing the layer: %(tag) ";
	}


	protected Map values() {
		Hashtable params = new Hashtable();
		params.put("layer",layer);
		return params;
	}

}
