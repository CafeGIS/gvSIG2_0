package org.gvsig.fmap.mapcontext.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.gvsig.tools.exception.BaseException;
/**
 * @author Vicente Caballero Navarro
 */
public class LoadLayerException extends BaseException {
	private String layer = null;

	public LoadLayerException(String layer,Throwable exception) {
		this.layer = layer;
		init();
		initCause(exception);
	}

	private void init() {
		messageKey = "error_load_layer";
		formatString = "Can´t load the layer: %(layer) ";
	}

	protected Map values() {
		Map params = new HashMap();
		params.put("layer",layer);
		return params;
	}

}
