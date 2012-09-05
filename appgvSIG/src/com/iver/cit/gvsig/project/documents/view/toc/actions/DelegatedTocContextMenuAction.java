package com.iver.cit.gvsig.project.documents.view.toc.actions;

import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.layers.FLayer;

import com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;

/**
 * Clase abstract para facilitar sobreescribir una accion sobre el TOC.
 *
 * @author Joaquin del Cerro
 */
public abstract class DelegatedTocContextMenuAction extends AbstractTocContextMenuAction {

	private AbstractTocContextMenuAction delegated = null; 
	
	public DelegatedTocContextMenuAction(AbstractTocContextMenuAction delegated) {
		this.delegated = delegated;
	}
	
	public void execute(ITocItem item, FLayer[] selectedItems) {
		delegated.execute(item,selectedItems);
	}

	public String getText() {
		return delegated.getText();
	}
	public String getGroup() {
		return delegated.getGroup();
	}

	public int getGroupOrder() {
		return delegated.getGroupOrder();
	}

	public int getOrder() {
		return delegated.getOrder();
	}

	public boolean isEnabled(ITocItem item, FLayer[] selectedItems) {
		return delegated.isEnabled(item, selectedItems);
	}

	public boolean isVisible(ITocItem item, FLayer[] selectedItems) {
		return delegated.isVisible(item, selectedItems);
		
	}
	
	public MapContext getMapContext() {
		return delegated.getMapContext();
	}
	
	public void setMapContext(MapContext mapContext) {
		delegated.setMapContext(mapContext);
	}

}
