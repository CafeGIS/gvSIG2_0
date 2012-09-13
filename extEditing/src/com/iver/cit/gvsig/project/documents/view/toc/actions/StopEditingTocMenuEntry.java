package com.iver.cit.gvsig.project.documents.view.toc.actions;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.StopEditing;
import com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;

/**
 * Termina la edici�n de la capa seleccionada.
 *
 * @author Vicente Caballero Navarro
 */
public class StopEditingTocMenuEntry extends AbstractTocContextMenuAction {
	public String getGroup() {
		return "edition";
	}

	public int getGroupOrder() {
		return 1;
	}

	public int getOrder() {
		return 2;
	}

	public String getText() {
		return PluginServices.getText(this, "stop_edition");
	}

	public boolean isEnabled(ITocItem item, FLayer[] selectedItems) {
		return true;
	}

	public boolean isVisible(ITocItem item, FLayer[] selectedItems) {
		return (isTocItemBranch(item)) && (selectedItems.length == 1 && selectedItems[0].isAvailable() && selectedItems[0] instanceof FLyrVect) && ((FLyrVect)selectedItems[0]).isEditing();
	}

	public void execute(ITocItem item, FLayer[] selectedItems) {
		StopEditing stopEditind=(StopEditing)PluginServices.getExtension(StopEditing.class);
		stopEditind.execute("STOPEDITING");
		PluginServices.getMainFrame().enableControls();
   }
}
