package org.gvsig.fmap.dal.resource.spi;



public interface ResourceConsumer {

	public boolean closeResourceRequested(ResourceProvider resource);

	public void resourceChanged(ResourceProvider resource);

}
