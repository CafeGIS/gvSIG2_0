package org.gvsig.fmap.dal.resource.exception;

import org.gvsig.fmap.dal.resource.Resource;

public class ResourceNotifyDisposeException extends ResourceException {
	/**
	 *
	 */
	private static final long serialVersionUID = -7470242143471869600L;
	private final static String MESSAGE_FORMAT = "Resource '%(resource)s' changed.";
	private final static String MESSAGE_KEY = "_ResourceNotifyDisposeException";

	public ResourceNotifyDisposeException(Resource resource) {
		super(resource, MESSAGE_FORMAT, MESSAGE_KEY, serialVersionUID);
	}
}
