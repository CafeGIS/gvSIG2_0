package org.gvsig.gvsig3d.simbology3D.symbol3D.polyline;

import java.awt.Color;
import java.util.List;

import org.gvsig.gvsig3d.gui.FeatureFactory;
import org.gvsig.gvsig3d.simbology3D.symbol3D.Abstract3DSymbol;
import org.gvsig.osgvp.core.osg.Node;
import org.gvsig.osgvp.core.osg.Vec3;
import org.gvsig.osgvp.core.osg.Vec4;

import com.iver.cit.gvsig.fmap.core.symbols.ILineSymbol;
import com.iver.cit.gvsig.fmap.core.symbols.ISymbol;

public class SimpleLine3DSymbol extends Abstract3DSymbol {

	protected Vec4 rgba;
	protected float lineWidth;

	public SimpleLine3DSymbol(ISymbol symbol) {
		super(symbol);
		
		ILineSymbol lineSymbol = (ILineSymbol) symbol;
			// Getting symbol properties

		// Symbol color and alpha value
		Color color = lineSymbol.getColor();
		float alpha = lineSymbol.getAlpha() / 255f;
		rgba = new Vec4(color.getRed() / 255f, color.getGreen() / 255f, color
				.getBlue() / 255f, alpha);

		// LineWidth and stroke
		lineWidth = 2f;
		lineWidth = (float) lineSymbol.getLineWidth();
	}

	@Override
	public Node generateSymbol(List<Vec3> position) {
		return FeatureFactory.insertLine(position, rgba, lineWidth);
	}

}
