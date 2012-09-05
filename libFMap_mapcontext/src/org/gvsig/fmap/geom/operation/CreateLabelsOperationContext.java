package org.gvsig.fmap.geom.operation;

import org.gvsig.fmap.geom.operation.GeometryOperationContext;

public class CreateLabelsOperationContext extends GeometryOperationContext{

	private int position;
	private boolean dublicates;
	public boolean isDublicates() {
		return dublicates;
	}
	public void setDublicates(boolean dublicates) {
		this.dublicates = dublicates;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}


}
