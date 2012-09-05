package org.gvsig.gvsig3d.simbology3D.symbol3D.polygon;

import java.awt.Color;
import java.util.List;

import org.gvsig.gvsig3d.gui.FeatureFactory;
import org.gvsig.gvsig3d.simbology3D.symbol3D.Abstract3DSymbol;
import org.gvsig.osgvp.core.osg.Node;
import org.gvsig.osgvp.core.osg.Vec3;
import org.gvsig.osgvp.core.osg.Vec4;

import com.iver.ai2.gvsig3d.legend.symbols.BaseExtrusionSymbol;
import com.iver.cit.gvsig.fmap.core.symbols.IFillSymbol;
import com.iver.cit.gvsig.fmap.core.symbols.ISymbol;

public class ExtrusionPolygon3DSymbol extends Abstract3DSymbol {

	protected Vec4 rgba ;
	private double extrusion;

	public ExtrusionPolygon3DSymbol(ISymbol symbol) {
		super(symbol);
		BaseExtrusionSymbol baseExtrusionSymbol = (BaseExtrusionSymbol) symbol;

		IFillSymbol fillSymbol = (IFillSymbol) symbol;
		
		// Getting symbol properties

		// Symbol color and alpha value
		Color color = baseExtrusionSymbol.getFillColor();
//		float alpha = baseExtrusionSymbol.getFillAlpha() / 255f;
		rgba = new Vec4(color.getRed() / 255f, color.getGreen() / 255f, color
				.getBlue() / 255f, 1.0);
		extrusion = baseExtrusionSymbol.getExtrusion(); 
//		System.err.println("extrusion " + extrusion);
	}

	@Override
	public Node generateSymbol(List<Vec3> position) {
		// TODO Auto-generated method stub
//		return FeatureFactory.insertPolygon(position, rgba, null);
		return FeatureFactory.insertPolygonExtruded(position,rgba , null,extrusion);
	}

}
