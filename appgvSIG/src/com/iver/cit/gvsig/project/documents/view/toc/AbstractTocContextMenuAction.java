package com.iver.cit.gvsig.project.documents.view.toc;

import java.util.Map;

import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.tools.extensionpoint.ExtensionBuilder;

import com.iver.cit.gvsig.project.documents.contextMenu.AbstractContextMenuAction;

public abstract class AbstractTocContextMenuAction extends AbstractContextMenuAction implements ExtensionBuilder{
	private MapContext mapContext;

	public MapContext getMapContext() {
		return this.mapContext;
	}

	public void setMapContext(MapContext mapContext) {
		this.mapContext = mapContext;
	}

	/**
	 * @deprecated use public boolean isEnabled(ITocItem item, FLayer[] selectedItems)
	 */
	public boolean isEnabled(Object item, Object[] selectedItems) {
		return this.isEnabled((ITocItem)item, (FLayer[])selectedItems);
	}

	/**
	 * @deprecated use public boolean isVisible(ITocItem item, FLayer[] selectedItems)
	 */
	public boolean isVisible(Object item, Object[] selectedItems) {
		return this.isVisible((ITocItem)item, (FLayer[])selectedItems);
	}

	/**
	 * @deprecated use public void execute(ITocItem item, FLayer[] selectedItems)
	 */
	public void execute(Object item, Object[] selectedItems) {
		this.execute((ITocItem)item, (FLayer[])selectedItems);
	}

	public FLayer getNodeLayer(ITocItem node) {
		if (isTocItemBranch(node))
			return ((TocItemBranch) node).getLayer();
		return null;
	}
	public boolean isTocItemLeaf(ITocItem node) {
		return node instanceof TocItemLeaf;
	}

	public boolean isTocItemBranch(ITocItem node) {
		return node instanceof TocItemBranch;
	}


	public boolean isEnabled(ITocItem item, FLayer[] selectedItems) {
		return true;
	}

	public boolean isVisible(ITocItem item, FLayer[] selectedItems) {
		return true;
	}

	public abstract void execute(ITocItem item, FLayer[] selectedItems);

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

}
