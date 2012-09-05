package com.iver.cit.gvsig.gui.panels.crs;

import javax.swing.JButton;

import org.cresques.cts.IProjection;

public interface ISelectCRSButton {
	 public boolean isTransPanelActive();
	 public void setTransPanelActive(boolean transPanelActive);
	 public IProjection getCurProj();
	 public boolean isOkPressed();
	 public void setCurProj(IProjection curProj);
}
