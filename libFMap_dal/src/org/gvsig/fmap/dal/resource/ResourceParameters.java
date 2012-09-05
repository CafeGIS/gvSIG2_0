package org.gvsig.fmap.dal.resource;

import org.gvsig.fmap.dal.DataParameters;

/**
 * Interface that contains any resource parameters.
 * Each specific subtype of resource will extend this
 * interface with its own relevant parameters.
 *
 */
public interface ResourceParameters extends DataParameters {

	/**
	 * Returns the type name of the related resource.
	 * 
	 * @return
	 * 		type name of the resource.
	 */
	public String getTypeName();

}

