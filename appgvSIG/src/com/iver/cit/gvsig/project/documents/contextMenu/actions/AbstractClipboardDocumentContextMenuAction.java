package com.iver.cit.gvsig.project.documents.contextMenu.actions;

import java.util.Map;

import com.iver.cit.gvsig.project.documents.contextMenu.AbstractDocumentContextMenuAction;

public abstract class AbstractClipboardDocumentContextMenuAction extends
		AbstractDocumentContextMenuAction {

	public int getGroupOrder() {
		return 0;
	}


	public String getGroup() {
		return "ClipboardActions";
	}


	public Object create() {
		return this;
	}

	public Object create(Object[] args) {
		// TODO Auto-generated method stub
		return this;
	}

	public Object create(Map args) {
		// TODO Auto-generated method stub
		return this;
	}

}
