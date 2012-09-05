package org.gvsig.fmap.dal.resource.exception;

import org.gvsig.fmap.dal.resource.Resource;

public class AccessResourceException extends ResourceException {

	/**
	 *
	 */
	private static final long serialVersionUID = -4409107196914175174L;
	private final static String MESSAGE_FORMAT = "Error while atemp to access resource '%(resource)'.";
	private final static String MESSAGE_KEY = "_AccessResourceException";

	public AccessResourceException(Resource resource, Throwable cause) {
		super(resource, MESSAGE_FORMAT, cause, MESSAGE_KEY, serialVersionUID);
	}
}