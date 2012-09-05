package org.gvsig.fmap.dal.resource.exception;

import org.gvsig.fmap.dal.resource.Resource;

public class ResourceNotifyOpenException extends ResourceException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1114089673521505999L;
	private final static String MESSAGE_FORMAT = "Resource '%(resource)s' changed.";
	private final static String MESSAGE_KEY = "_ResourceNotifyOpenException";

	public ResourceNotifyOpenException(Resource resource) {
		super(resource, MESSAGE_FORMAT, MESSAGE_KEY, serialVersionUID);
	}
}
