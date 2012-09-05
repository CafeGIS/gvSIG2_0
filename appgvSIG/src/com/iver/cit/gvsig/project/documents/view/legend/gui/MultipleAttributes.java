package com.iver.cit.gvsig.project.documents.view.legend.gui;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;

import com.iver.andami.PluginServices;

public class MultipleAttributes extends AbstractParentPanel {

	public String getDescription() {
		return null;
	}

	public String getTitle() {
		return PluginServices.getText(this, "multiple_atributes");
	}

	public boolean isSuitableFor(FLayer layer) {
		return layer instanceof FLyrVect;
	}
}
