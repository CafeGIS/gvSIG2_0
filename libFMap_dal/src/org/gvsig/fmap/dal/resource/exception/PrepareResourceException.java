package org.gvsig.fmap.dal.resource.exception;

import org.gvsig.fmap.dal.resource.Resource;

public class PrepareResourceException extends ResourceException {

	/**
	 *
	 */
	private static final long serialVersionUID = 2198478752691400900L;
	private final static String MESSAGE_FORMAT = "Exception preparing '%(resource)s'.";
	private final static String MESSAGE_KEY = "_PrepareResourceException";

	public PrepareResourceException(Resource resource, Throwable cause) {
		super(resource, MESSAGE_FORMAT, cause, MESSAGE_KEY, serialVersionUID);
	}

}
