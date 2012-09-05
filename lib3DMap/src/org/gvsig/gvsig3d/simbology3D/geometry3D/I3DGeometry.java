package org.gvsig.gvsig3d.simbology3D.geometry3D;

import org.gvsig.gvsig3d.simbology3D.symbol3D.I3DSymbol;
import org.gvsig.osgvp.core.osg.Group;
import org.gvsig.osgvp.core.osg.Vec3;

import com.iver.cit.gvsig.fmap.core.IGeometry;

public interface I3DGeometry {

	Group generateGeometry(I3DSymbol symbol3D);

	Vec3 getGeometryPosition(IGeometry geometry, double[] dataLine,
			float heigth, int contH);
}
