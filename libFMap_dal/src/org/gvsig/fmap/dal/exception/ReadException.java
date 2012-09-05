package org.gvsig.fmap.dal.exception;


/**
 * FIXME
 *
 *
 */

public class ReadException extends DataException {

	/**
	 *
	 */
	private static final long serialVersionUID = -8512708060535066804L;
	private final static String MESSAGE_FORMAT = "Exception reading '%(store)'.";
	private final static String MESSAGE_KEY = "_ReadException";

	public ReadException(String store, Throwable cause) {
		super(MESSAGE_FORMAT, cause, MESSAGE_KEY, serialVersionUID);
		setValue("store", store);
	}

}
