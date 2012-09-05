package com.iver.cit.gvsig.project.documents.layout.contextmenu.gui;

import java.util.Map;

import org.gvsig.tools.extensionpoint.ExtensionBuilder;

import com.iver.cit.gvsig.project.documents.contextMenu.AbstractContextMenuAction;
import com.iver.cit.gvsig.project.documents.layout.LayoutContext;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;

public abstract class AbstractLayoutContextMenuAction extends
		AbstractContextMenuAction implements ExtensionBuilder {
	private Layout layout;

	public Layout getLayout() {
		return this.layout;
	}

	public void setLayout(Layout layout) {
		this.layout = layout;
	}

	public boolean isEnabled(LayoutContext layoutContext, IFFrame[] selectedFrames) {
		return true;
	}

	public boolean isVisible(LayoutContext layoutContext, IFFrame[] selectedFrames) {
		return true;
	}

	public abstract void execute(LayoutContext layoutContext, IFFrame[] selectedFrames);

	public Object create() {
		return this;
	}

	public Object create(Map args) {
		// TODO Auto-generated method stub
		return this;
	}

	public Object create(Object[] args) {
		// TODO Auto-generated method stub
		return this;
	}

	public boolean isEnabled(Object item, Object[] selectedItems) {
		return isEnabled((LayoutContext)item,(IFFrame[]) selectedItems);
	}

	public boolean isVisible(Object item, Object[] selectedItems) {
		return isVisible((LayoutContext)item,(IFFrame[]) selectedItems);
	}

	public void execute(Object item, Object[] selectedItems) {
		execute((LayoutContext)item,(IFFrame[]) selectedItems);

	}

}
