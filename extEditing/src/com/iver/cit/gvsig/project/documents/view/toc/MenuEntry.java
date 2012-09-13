package com.iver.cit.gvsig.project.documents.view.toc;

import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;

import com.iver.cit.gvsig.project.documents.view.toc.actions.EditionPropertiesTocMenuEntry;
import com.iver.cit.gvsig.project.documents.view.toc.actions.StartEditingTocMenuEntry;
import com.iver.cit.gvsig.project.documents.view.toc.actions.StopEditingTocMenuEntry;

/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class MenuEntry {
    /**
     * DOCUMENT ME!
     */
    public static void register() {
    	ExtensionPoint exPoint = ToolsLocator.getExtensionPointManager().add(
				"View_TocActions", "");
		exPoint	.append("StartEditing", "",
				new StartEditingTocMenuEntry());
		exPoint.append("StopEditing", "",
				new StopEditingTocMenuEntry());
		exPoint.append("EditionProperties", "",
				new EditionPropertiesTocMenuEntry());
    }
}
