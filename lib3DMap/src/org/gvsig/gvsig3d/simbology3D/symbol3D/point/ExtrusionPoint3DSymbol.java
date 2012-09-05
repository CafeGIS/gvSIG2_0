/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
*
* Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
*/
package org.gvsig.gvsig3d.simbology3D.symbol3D.point;

import java.awt.Color;
import java.util.List;

import org.gvsig.gvsig3d.gui.FeatureFactory;
import org.gvsig.gvsig3d.simbology3D.symbol3D.Abstract3DSymbol;
import org.gvsig.osgvp.core.osg.Node;
import org.gvsig.osgvp.core.osg.Vec3;
import org.gvsig.osgvp.core.osg.Vec4;

import com.iver.ai2.gvsig3d.legend.symbols.BaseExtrusionSymbol;
import com.iver.cit.gvsig.fmap.core.symbols.ISymbol;

public class ExtrusionPoint3DSymbol extends Abstract3DSymbol{
	

	protected Vec4 rgba ;
	private double extrusion;

	public ExtrusionPoint3DSymbol(ISymbol symbol) {
		super(symbol);
		BaseExtrusionSymbol baseExtrusionSymbol = (BaseExtrusionSymbol) symbol;

	//	ILineSymbol fillSymbol = (ILineSymbol) symbol;
		
		// Getting symbol properties

		// Symbol color and alpha value
		Color color = baseExtrusionSymbol.getFillColor();
//		float alpha = baseExtrusionSymbol.getFillAlpha() / 255f;
		rgba = new Vec4(color.getRed() / 255f, color.getGreen() / 255f, color
				.getBlue() / 255f, 0.7);
		extrusion = baseExtrusionSymbol.getExtrusion(); 
//		System.err.println("extrusion " + extrusion);
	}

	public Node generateSymbol(List<Vec3> position) {
		// TODO Auto-generated method stub
//		return FeatureFactory.insertPolygon(position, rgba, null);
		//return FeatureFactory.addPointToNode(position,rgba , null,extrusion);
		//return	FeatureFactory.addPointToNode(node, newPosition, rgba, (int) size);
		return FeatureFactory.insertPointExtruded(position,rgba , null,extrusion);
	}

}
