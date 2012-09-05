package org.gvsig.gvsig3d.simbology3D.symbol3D;

import java.util.List;

import org.gvsig.osgvp.core.osg.Node;
import org.gvsig.osgvp.core.osg.Vec3;




public interface I3DSymbol {
	public Node generateSymbol(List<Vec3> position);

	public Node generateSymbol(Vec3[] position);
}
