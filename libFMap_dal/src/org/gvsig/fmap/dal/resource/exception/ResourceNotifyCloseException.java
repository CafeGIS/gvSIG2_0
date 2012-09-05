package org.gvsig.fmap.dal.resource.exception;

import org.gvsig.fmap.dal.resource.Resource;

public class ResourceNotifyCloseException extends ResourceException {


	/**
	 *
	 */
	private static final long serialVersionUID = -805056352573291250L;
	private final static String MESSAGE_FORMAT = "Resource '%(resource)s' changed.";
	private final static String MESSAGE_KEY = "_ResourceNotifyCloseExceptionn";

	public ResourceNotifyCloseException(Resource resource) {
		super(resource, MESSAGE_FORMAT, MESSAGE_KEY, serialVersionUID);
	}
}

