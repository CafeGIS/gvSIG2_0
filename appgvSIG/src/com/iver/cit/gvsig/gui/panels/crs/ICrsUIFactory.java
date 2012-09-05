package com.iver.cit.gvsig.gui.panels.crs;

import org.cresques.cts.IProjection;

public interface ICrsUIFactory {
	public ISelectCrsPanel getSelectCrsPanel(IProjection projection, boolean isTransPanelActive);
}
