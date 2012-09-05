package org.gvsig.gvsig3d.labels3D;

import com.iver.cit.gvsig.fmap.core.v02.FLabel;
import com.iver.cit.gvsig.fmap.rendering.styling.labeling.AttrInTableLabelingStrategy;

public class SimpleLabel3D extends AbstractLabel3D {
	
	private AttrInTableLabelingStrategy strategy;

	public SimpleLabel3D(FLabel label, double heigthField, String textField) {
		super(label, heigthField, textField);
		// TODO Auto-generated constructor stub
	}

	public SimpleLabel3D(FLabel label) {
		super(label);
		// TODO Auto-generated constructor stub
	}

	public AttrInTableLabelingStrategy getStrategy() {
		return strategy;
	}

	public void setStrategy(AttrInTableLabelingStrategy strategy) {
		this.strategy = strategy;
	}
	
	


}
