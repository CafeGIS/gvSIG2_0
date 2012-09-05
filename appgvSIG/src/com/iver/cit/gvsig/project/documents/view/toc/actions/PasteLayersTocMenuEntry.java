package com.iver.cit.gvsig.project.documents.view.toc.actions;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLayers;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;
import com.iver.utiles.XMLEntity;

public class PasteLayersTocMenuEntry extends AbstractTocContextMenuAction {
	private XMLEntity xml=null;
	private CopyPasteLayersUtiles utiles = CopyPasteLayersUtiles.getInstance();


	public String getGroup() {
		return "copyPasteLayer";
	}

	public int getGroupOrder() {
		return 60;
	}

	public int getOrder() {
		return 2;
	}

	public String getText() {
		return PluginServices.getText(this, "pegar");
	}

	public boolean isEnabled(ITocItem item, FLayer[] selectedItems) {
		if (isTocItemBranch(item)) {
			FLayer lyr = getNodeLayer(item);
			if (lyr instanceof FLayers) {
				this.xml = this.getCheckedXMLFromClipboard();
				return true;
			}

		} else if (!isTocItemLeaf(item)) {
			if (getNodeLayer(item) == null) {
				this.xml = this.getCheckedXMLFromClipboard();
				return this.xml != null;
			}
		}
		return false;
	}

	private XMLEntity getCheckedXMLFromClipboard() {
		String sourceString = PluginServices.getFromClipboard();
		if (sourceString == null) return null;

		XMLEntity xml;
		try {
			xml = XMLEntity.parse(sourceString);
		} catch (MarshalException e) {
			return null;
		} catch (ValidationException e) {
			return null;
		}


		if (!this.utiles.checkXMLRootNode(xml)) return null;

		if (xml.findChildren("type","layers") == null) return null;

		return  xml;
	}

	public void execute(ITocItem item, FLayer[] selectedItems) {
		FLayers root;

		if (this.xml == null) return;

		if (isTocItemBranch(item)) {
			root = (FLayers)getNodeLayer(item);
		} else if (getNodeLayer(item) == null){
			root = getMapContext().getLayers();
		} else {
			return;
		}
		getMapContext().beginAtomicEvent();

		boolean isOK = this.utiles.loadLayersFromXML(this.xml,root);

		getMapContext().endAtomicEvent();

		if (isOK) {
			getMapContext().invalidate();
			Project project=((ProjectExtension)PluginServices.getExtension(ProjectExtension.class)).getProject();
			project.setModified(true);
		}
	}


}
