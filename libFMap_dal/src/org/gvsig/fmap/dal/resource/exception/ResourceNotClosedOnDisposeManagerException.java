package org.gvsig.fmap.dal.resource.exception;

import org.gvsig.fmap.dal.resource.Resource;

public class ResourceNotClosedOnDisposeManagerException extends ResourceException {

	/**
	 *
	 */
	private static final long serialVersionUID = -4030443484190503144L;
	private final static String MESSAGE_FORMAT = "The '%(resource)' still open when the manager begin to dispose.";
	private final static String MESSAGE_KEY = "_ResourceNotClosedOnDisposeManagerException";

	public ResourceNotClosedOnDisposeManagerException(Resource resource) {
		super(resource, MESSAGE_FORMAT, MESSAGE_KEY, serialVersionUID);
	}
}