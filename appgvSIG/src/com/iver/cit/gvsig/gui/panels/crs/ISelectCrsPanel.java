package com.iver.cit.gvsig.gui.panels.crs;

import org.cresques.cts.IProjection;

import com.iver.andami.ui.mdiManager.IWindow;

public interface ISelectCrsPanel extends IWindow {
	public IProjection getProjection();
    public void setProjection(IProjection proj);
    public boolean isOkPressed();
}
