package org.gvsig.gvsig3d.simbology3D.symbol3D;

import java.util.ArrayList;
import java.util.List;

import org.gvsig.osgvp.core.osg.Node;
import org.gvsig.osgvp.core.osg.Vec3;

import com.iver.cit.gvsig.fmap.core.symbols.ISymbol;

public abstract class Abstract3DSymbol implements I3DSymbol {

	ISymbol symbol = null;

	public Abstract3DSymbol(ISymbol symbol) {
		super();
		this.symbol = symbol;
	}

	public abstract Node generateSymbol(List<Vec3> position);

	public Node generateSymbol(Vec3[] position) {
		// TODO Auto-generated method stub
		List<Vec3> auxList = new ArrayList<Vec3>();
		for (int i = 0; i < position.length; i++) {
			Vec3 vec3 = position[i];
			auxList.add(vec3);
		}
		return generateSymbol(auxList);
	}

}
