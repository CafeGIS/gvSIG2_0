package org.gvsig.gvsig3d.labels3D;



import org.gvsig.osgvp.core.osg.Vec3;

import com.iver.cit.gvsig.fmap.core.v02.FLabel;

public class AbstractLabel3D implements ILabel3D {

	Vec3 position;
	FLabel label;
	double heigthField;
	int heigthFieldId;
	String textField;
	int textFieldId;
	private int units;

	public AbstractLabel3D(FLabel label, double heigthField, String textField) {
		super();
		this.label = label;
		position = new Vec3(label.getOrig().getX(), label.getOrig().getY(), 0.0);
		this.heigthField = heigthField;
		this.textField = textField;
	}

	public AbstractLabel3D(FLabel label) {
		super();
		this.label = label;
		position = new Vec3(label.getOrig().getX(), label.getOrig().getY(), 0.0);
	}

	public FLabel getFlabel() {
		return this.label;
	}

	public double getHeightField() {
		return this.heigthField;
	}

	public Vec3 getPosition() {
		return this.position;
	}

	public String getTextField() {
		return this.textField;
	}

	public int getTextFieldId() {
		return this.textFieldId;
	}

	public void setFlabel(FLabel flabel) {
		this.label = flabel;

	}

	public void setHeightField(double heigthField) {
		this.heigthField = heigthField;
	}

	public void setPosition(Vec3 position) {
		this.position = position;
	}

	public void setTextField(String textField) {
		this.textField = textField;
	}

	public void setTextFieldId(int textFieldId) {
		this.textFieldId = textFieldId;
	}

	public int getHeightFieldId() {
		return this.heigthFieldId;
	}

	public void setHeightFieldId(int heigthFieldId) {
		this.heigthFieldId = heigthFieldId;
	}

	public int getUnits() {
		return this.units;
	}

	public void setUnits(int units) {
		this.units = units;
	}
}
