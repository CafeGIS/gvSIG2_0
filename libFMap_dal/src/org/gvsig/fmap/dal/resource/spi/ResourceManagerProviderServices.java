package org.gvsig.fmap.dal.resource.spi;

import java.util.List;

import org.gvsig.fmap.dal.DataParameters;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.resource.Resource;
import org.gvsig.fmap.dal.resource.ResourceManager;
import org.gvsig.fmap.dal.resource.ResourceParameters;

public interface ResourceManagerProviderServices extends ResourceManager {

	public boolean register(String type, String description, Class handler,
			Class params);

	public DataParameters createParameters(String type)
			throws InitializeException;

	public ResourceProvider createResource(ResourceParameters params)
			throws InitializeException;

	public ResourceProvider createResource(String type, Object[] params)
			throws InitializeException;

	public void remove(Resource resource) throws DataException;

	public void remove(String name) throws DataException;
	
	public List getResourceProviders();

}
