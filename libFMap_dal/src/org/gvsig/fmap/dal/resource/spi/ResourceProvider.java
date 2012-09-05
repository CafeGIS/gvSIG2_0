package org.gvsig.fmap.dal.resource.spi;

import org.gvsig.fmap.dal.resource.Resource;
import org.gvsig.fmap.dal.resource.ResourceParameters;
import org.gvsig.fmap.dal.resource.exception.PrepareResourceException;
import org.gvsig.fmap.dal.resource.exception.ResourceException;
import org.gvsig.fmap.dal.resource.exception.ResourceNotifyChangesException;
import org.gvsig.fmap.dal.resource.exception.ResourceNotifyCloseException;
import org.gvsig.fmap.dal.resource.exception.ResourceNotifyDisposeException;
import org.gvsig.fmap.dal.resource.exception.ResourceNotifyOpenException;


public interface ResourceProvider extends Resource {

	public void notifyOpen() throws ResourceNotifyOpenException;

	public void notifyClose() throws ResourceNotifyCloseException;

	public void notifyDispose() throws ResourceNotifyDisposeException;

	public void notifyChanges() throws ResourceNotifyChangesException;

	public void prepare() throws PrepareResourceException;

	public boolean isThis(ResourceParameters parameters)
			throws ResourceException;
}
