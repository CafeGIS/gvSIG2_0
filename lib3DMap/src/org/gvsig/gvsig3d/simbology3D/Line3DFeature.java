package org.gvsig.gvsig3d.simbology3D;

import org.apache.log4j.Logger;
import org.gvsig.gvsig3d.simbology3D.geometry3D.Line3DGeometry;
import org.gvsig.gvsig3d.simbology3D.symbol3D.Abstract3DSymbol;
import org.gvsig.gvsig3d.simbology3D.symbol3D.polyline.ExtrusionLine3DSymbol;
import org.gvsig.gvsig3d.simbology3D.symbol3D.polyline.SimpleLine3DSymbol;
import org.gvsig.osgvp.core.osg.Group;
import org.gvsig.osgvp.exceptions.node.NodeException;
import org.gvsig.osgvp.planets.Planet;

import com.iver.ai2.gvsig3d.legend.symbols.BaseExtrusionSymbol;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.core.symbols.ISymbol;

public class Line3DFeature extends AbstractFeature3D {

	private Line3DGeometry l3DGeometry;
	private Abstract3DSymbol l3DSymbol;

	private static Logger logger = Logger.getLogger(Line3DFeature.class
			.getName());

	public Line3DFeature(ISymbol symbol, IGeometry geometry) {
		super(symbol, geometry);
		l3DGeometry = new Line3DGeometry(geometry);
		// l3DGeometry = (Line3DGeometry) this.getGeometry();
		// l3DSymbol = (SimpleLine3DSymbol) this.getSymbol();
		
		if (this.getSymbol().getClass().equals(BaseExtrusionSymbol.class)) {
			l3DSymbol = new ExtrusionLine3DSymbol(symbol);
		} else {
			l3DSymbol = new SimpleLine3DSymbol(symbol);
		}
		

	}

	public Group Draw(Group group) {
		try {
			group.addChild(l3DGeometry.generateGeometry(l3DSymbol));
		} catch (NodeException e) {
			logger.error("Command:" + "Error adding new child.", e);
		}
		return group;
	}

	public void setPlanet(Planet planet) {
		if (this.l3DGeometry != null) {
			this.l3DGeometry.setPlanet(planet);
		}
	}

	public void setHeigth(float heigth) {
		if (this.l3DGeometry != null) {
			this.l3DGeometry.setHeigth(heigth);
		}
	}
}
