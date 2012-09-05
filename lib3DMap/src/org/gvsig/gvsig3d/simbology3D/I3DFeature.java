package org.gvsig.gvsig3d.simbology3D;

import org.gvsig.osgvp.core.osg.Group;



public interface I3DFeature {

	/**
	 * This method build this feature into a group node
	 * 
	 * @param group
	 *            the group to add the feature
	 * @return the group with the feature
	 */
	Group Draw(Group group);

}
