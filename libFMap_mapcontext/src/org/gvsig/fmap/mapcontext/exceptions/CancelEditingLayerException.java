package org.gvsig.fmap.mapcontext.exceptions;

import java.util.Hashtable;
import java.util.Map;

import org.gvsig.tools.exception.BaseException;
/**
 * @author Vicente Caballero Navarro
 */
public class CancelEditingLayerException extends BaseException {

	private String layer = null;

	public CancelEditingLayerException(String layer,Throwable exception) {
		this.layer = layer;
		init();
		initCause(exception);
	}

	private void init() {
		messageKey = "error_cancel_editing_layer";
		formatString = "Can´t cancel editing the layer: %(layer) ";
	}

	protected Map values() {
		Hashtable params = new Hashtable();
		params.put("layer",layer);
		return params;
	}

}
