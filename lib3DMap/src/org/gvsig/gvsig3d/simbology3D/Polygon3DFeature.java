package org.gvsig.gvsig3d.simbology3D;

import org.apache.log4j.Logger;
import org.gvsig.gvsig3d.simbology3D.geometry3D.Abstract3DGeometry;
import org.gvsig.gvsig3d.simbology3D.geometry3D.Polygon3DGeometry;
import org.gvsig.gvsig3d.simbology3D.symbol3D.Abstract3DSymbol;
import org.gvsig.gvsig3d.simbology3D.symbol3D.polygon.ExtrusionPolygon3DSymbol;
import org.gvsig.gvsig3d.simbology3D.symbol3D.polygon.SimplePolygon3DSymbol;
import org.gvsig.osgvp.core.osg.Group;
import org.gvsig.osgvp.exceptions.node.NodeException;
import org.gvsig.osgvp.planets.Planet;

import com.iver.ai2.gvsig3d.legend.symbols.BaseExtrusionSymbol;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.core.symbols.ISymbol;

public class Polygon3DFeature extends AbstractFeature3D {

	Polygon3DGeometry p3DGeometry;
	Abstract3DSymbol pp3DSymbol;
	private static Logger logger = Logger.getLogger(Abstract3DGeometry.class
			.getName());

	public Polygon3DFeature(ISymbol symbol, IGeometry geometry) {
		super(symbol, geometry);
		// TODO Auto-generated constructor stub
		// p3DGeometry = (Polygon3DGeometry) this.getGeometry();
		// pp3DSymbol = (SimplePolygon3DSymbol) this.getSymbol();
		p3DGeometry = new Polygon3DGeometry(geometry);
		if (this.getSymbol().getClass().equals(BaseExtrusionSymbol.class)) {
			pp3DSymbol = new ExtrusionPolygon3DSymbol(symbol);
		} else {
			pp3DSymbol = new SimplePolygon3DSymbol(symbol);
		}
	}

	public Group Draw(Group group) {
		// TODO Auto-generated method stub
		try {
			group.addChild(p3DGeometry.generateGeometry(pp3DSymbol));
		} catch (NodeException e) {
			logger.error("Command: " + "Error adding new child.", e);
		}
		return group;
	}

	public void setPlanet(Planet planet) {
		if (this.p3DGeometry != null) {
			this.p3DGeometry.setPlanet(planet);
		}
	}

	public void setHeigth(float heigth) {
		if (this.p3DGeometry != null) {
			this.p3DGeometry.setHeigth(heigth);
		}
	}

}
