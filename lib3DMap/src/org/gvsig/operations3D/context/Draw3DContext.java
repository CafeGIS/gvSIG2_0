package org.gvsig.operations3D.context;

import org.gvsig.fmap.geom.operation.GeometryOperationContext;
import org.gvsig.osgvp.core.osg.Group;
import org.gvsig.osgvp.planets.Planet;

public class Draw3DContext extends GeometryOperationContext {

	private Planet planet = null;
	private Object symbol = null;
	private Group group = null;

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Planet getPlanet() {
		return planet;
	}

	public void setPlanet(Planet planet) {
		this.planet = planet;
	}

	public Object getSymbol() {
		return symbol;
	}

	public void setSymbol(Object symbol) {
		this.symbol = symbol;
	}
	
	public Draw3DContext clone(){
		Draw3DContext ctx3D = new Draw3DContext();
		ctx3D.setGroup(this.getGroup());
		ctx3D.setPlanet(this.getPlanet());
		ctx3D.setSymbol(this.getSymbol());
		return ctx3D;
		
	}
}
