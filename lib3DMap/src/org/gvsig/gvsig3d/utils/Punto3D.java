package org.gvsig.gvsig3d.utils;

import org.gvsig.osgvp.core.osg.Vec3;
import org.gvsig.osgvp.core.osg.Vec4;



public class Punto3D {

	private Vec3 position;

	private Vec4 color;

	private int zize;

	public Punto3D(Vec3 position, Vec4 color, int zize) {
		super();
		this.position = position;
		this.color = color;
		this.zize = zize;
	}

	public Vec4 getColor() {
		return color;
	}

	public void setColor(Vec4 color) {
		this.color = color;
	}

	public Vec3 getPosition() {
		return position;
	}

	public void setPosition(Vec3 position) {
		this.position = position;
	}

	public int getZize() {
		return zize;
	}

	public void setZize(int zize) {
		this.zize = zize;
	}

}
