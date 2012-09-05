package org.gvsig.fmap.dal.resource.exception;

import java.util.HashMap;
import java.util.Map;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.resource.Resource;

public abstract class ResourceException extends DataException {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	protected Map values = new HashMap();

	private Resource resource;

	public ResourceException(String messageFormat, Throwable cause,
			String messageKey, long code) {
		super(messageFormat, cause, messageKey, code);
	}

	public ResourceException(String messageFormat, String messageKey, long code) {
		super(messageFormat, messageKey, code);
	}

	public ResourceException(Resource resource, String messageFormat,
			Throwable cause, String messageKey, long code) {
		super(messageFormat, cause, messageKey, code);
		this.resource = resource;
		try {
			setValue("resource", resource.getName());
		} catch (AccessResourceException e) {
			setValue("resource", "unknow");
		}
	}

	public ResourceException(Resource resource, String messageFormat,
			String messageKey, long code) {
		super(messageFormat, messageKey, code);
		this.resource = resource;
		try {
			setValue("resource", resource.getName());
		} catch (AccessResourceException e) {
			setValue("resource", "unknow");
		}
	}

	public Resource getResource() {
		return resource;
	}

	protected void setValue(String name, String value) {
		this.values.put(name, value);
	}

	protected Map values() {
		return this.values;
	}
}
