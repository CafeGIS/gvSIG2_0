package org.gvsig.gvsig3d.labels3D;



import org.gvsig.osgvp.core.osg.Vec3;

import com.iver.cit.gvsig.fmap.core.v02.FLabel;

public interface ILabel3D {
	

	public void setTextField(String textField);

	public String getTextField();

	public void setTextFieldId(int textFieldId);

	public int getTextFieldId();

	public void setHeightField(double heigthField);

	public double getHeightField();
	
	public void setHeightFieldId(int heigthFieldId);

	public int getHeightFieldId();

	public void setPosition(Vec3 position);

	public Vec3 getPosition();

	public void setFlabel(FLabel flabel);

	public FLabel getFlabel();
	
	public void setUnits(int units);
	public int getUnits();

}
