package org.gvsig.fmap.dal.resource.exception;

import org.gvsig.fmap.dal.resource.Resource;

public class ResourceBeginException extends ResourceException {


	/**
	 *
	 */
	private static final long serialVersionUID = 8753529361743873531L;
	private final static String MESSAGE_FORMAT = "Error while atemp to begin to use resource '%(resource)s'.";
	private final static String MESSAGE_KEY = "_ResourceBeginException";

	public ResourceBeginException(Resource resource, Throwable cause) {
		super(resource, MESSAGE_FORMAT, cause, MESSAGE_KEY, serialVersionUID);
	}
}