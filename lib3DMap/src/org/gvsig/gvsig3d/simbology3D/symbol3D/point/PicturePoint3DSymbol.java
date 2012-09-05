package org.gvsig.gvsig3d.simbology3D.symbol3D.point;

import java.util.List;

import org.gvsig.gvsig3d.gui.FeatureFactory;
import org.gvsig.gvsig3d.simbology3D.symbol3D.Abstract3DSymbol;
import org.gvsig.osgvp.core.osg.Node;
import org.gvsig.osgvp.core.osg.Vec3;
import org.gvsig.osgvp.core.osg.Vec4;
import org.gvsig.symbology.fmap.symbols.PictureMarkerSymbol;

import com.iver.cit.gvsig.fmap.core.symbols.ISymbol;

public class PicturePoint3DSymbol extends Abstract3DSymbol{

	private PictureMarkerSymbol pictureMarketSymbol;

	public PicturePoint3DSymbol(ISymbol symbol) {
		super(symbol);
		pictureMarketSymbol = (PictureMarkerSymbol) symbol;
	}

	@Override
	public Node generateSymbol(List<Vec3> position) {
		return FeatureFactory.generateQuadPoligon(position.get(0),new Vec4(1.0,1.0,1.0,1.0),pictureMarketSymbol.getImagePath(),(float)pictureMarketSymbol.getSize()*10);
	}

}
