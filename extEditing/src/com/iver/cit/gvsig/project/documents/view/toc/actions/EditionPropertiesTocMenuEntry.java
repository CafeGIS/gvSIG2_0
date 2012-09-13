package com.iver.cit.gvsig.project.documents.view.toc.actions;

import java.util.Iterator;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;
import org.gvsig.tools.extensionpoint.ExtensionPoint.Extension;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.preferences.AbstractPreferencePage;
import com.iver.andami.preferences.GenericDlgPreferences;
import com.iver.cit.gvsig.gui.preferences.EditionPreferencePage;
import com.iver.cit.gvsig.gui.preferences.FlatnessPage;
import com.iver.cit.gvsig.gui.preferences.SnapConfigPage;
import com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;
import com.iver.utiles.extensionPointsOld.ExtensionPoints;
import com.iver.utiles.extensionPointsOld.ExtensionPointsSingleton;

/**
 * Abre el diálogo de propiedades de edición.
 *
 * @author Vicente Caballero Navarro
 */
public class EditionPropertiesTocMenuEntry extends AbstractTocContextMenuAction {
	private ExtensionPointManager extensionPoints =ToolsLocator.getExtensionPointManager();

	public Object create() {
		ExtensionPoint ep = this.extensionPoints.add("cad_editing_properties_pages", "");
		ep.append("flatness", "", FlatnessPage.class);
		ep.append("snapping", "", SnapConfigPage.class);
    	return super.create();
	}
	public String getGroup() {
		return "edition";
	}

	public int getGroupOrder() {
		return 60;
	}

	public int getOrder() {
		return 60;
	}

	public String getText() {
		return PluginServices.getText(this, "Edition_Properties");
	}

	public boolean isEnabled(ITocItem item, FLayer[] selectedItems) {
		return true;
	}

	public boolean isVisible(ITocItem item, FLayer[] selectedItems) {
		return (isTocItemBranch(item)) && (selectedItems.length == 1 && selectedItems[0].isAvailable() && selectedItems[0] instanceof FLyrVect) && ((FLyrVect)selectedItems[0]).isEditing();
	}

	public void execute(ITocItem item, FLayer[] selectedItems) {
		EditionPreferencePage pref = new EditionPreferencePage();

		pref.setMapContext(getMapContext());
//		GridPage gridPage=new GridPage();
//		gridPage.setParentID(pref.getID());
//		FlatnessPage flatnessPage=new FlatnessPage();
//		flatnessPage.setParentID(pref.getID());


		GenericDlgPreferences dlg = new GenericDlgPreferences();
		dlg.addPreferencePage(pref);
		ExtensionPoint ep = this.extensionPoints.get("cad_editing_properties_pages");

//		 ExtensionPoints extensionPoints = ExtensionPointsSingleton.getInstance();
//	        ExtensionPoint extensionPoint =(ExtensionPoint)extensionPoints.get("cad_editing_properties_pages");
	        Iterator iterator = ep.iterator();
	        while (iterator.hasNext()) {
	            try {
	            	Extension obj= (Extension)iterator.next();
	            	AbstractPreferencePage  app = (AbstractPreferencePage )ep.create(obj.getName());
	            	app.setParentID(pref.getID());
	            	dlg.addPreferencePage(app);
	            } catch (InstantiationException e) {
	            	NotificationManager.addError(e.getMessage(),e);
	            } catch (IllegalAccessException e) {
	            	NotificationManager.addError(e.getMessage(),e);
	            } catch (ClassCastException e) {
	            	NotificationManager.addError(e.getMessage(),e);
	            }
	        }

//		dlg.addPreferencePage(gridPage);
//		dlg.addPreferencePage(flatnessPage);
//		dlg.addPreferencePage(fieldExpresionPage);
		dlg.getWindowInfo().setTitle(PluginServices.getText(this, "Edition_Properties"));
		dlg.setActivePage(pref);
		PluginServices.getMDIManager().addWindow(dlg);
   }
}
