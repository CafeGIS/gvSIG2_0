package org.gvsig.fmap.dal.resource.exception;

import org.gvsig.fmap.dal.exception.InitializeException;

public class ResourceNotRegisteredException extends InitializeException {

	/**
	 *
	 */
	private static final long serialVersionUID = -3783563399272946137L;
	private final static String MESSAGE_FORMAT = "The '%(resourceType)' resource type is not registered.";
	private final static String MESSAGE_KEY = "_ResourceNotRegisteredException";

	public ResourceNotRegisteredException(String resourceType) {
		super(MESSAGE_FORMAT, MESSAGE_KEY, serialVersionUID);
		this.values.put("resourceType", resourceType);
	}
}