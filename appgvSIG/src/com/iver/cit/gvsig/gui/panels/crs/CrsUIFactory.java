package com.iver.cit.gvsig.gui.panels.crs;

import org.cresques.cts.IProjection;

import com.iver.cit.gvsig.project.documents.view.info.gui.CSSelectionDialog;

public class CrsUIFactory implements ICrsUIFactory {


	public ISelectCrsPanel getSelectCrsPanel(IProjection projection,
			boolean isTransPanelActive) {
		CSSelectionDialog panel = new CSSelectionDialog();
		panel.setProjection(projection);
		return panel;
	}

}
