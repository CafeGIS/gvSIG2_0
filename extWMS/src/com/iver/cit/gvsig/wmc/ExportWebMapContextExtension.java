/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */

/* CVS MESSAGES:
 *
 * $Id: ExportWebMapContextExtension.java 26945 2009-02-26 09:16:44Z jmvivo $
 * $Log$
 * Revision 1.14  2007-01-08 07:57:34  jaume
 * *** empty log message ***
 *
 * Revision 1.13  2006/09/27 13:28:38  jaume
 * *** empty log message ***
 *
 * Revision 1.12  2006/09/20 10:38:43  jaume
 * *** empty log message ***
 *
 * Revision 1.11  2006/09/20 07:45:21  caballero
 * constante registerName
 *
 * Revision 1.10  2006/09/18 08:28:44  caballero
 * cambio de nombre
 *
 * Revision 1.9  2006/09/15 10:44:24  caballero
 * extensibilidad de documentos
 *
 * Revision 1.8  2006/08/29 07:56:15  cesar
 * Rename the *View* family of classes to *Window* (ie: SingletonView to SingletonWindow, ViewInfo to WindowInfo, etc)
 *
 * Revision 1.7  2006/08/29 07:13:43  cesar
 * Rename class com.iver.andami.ui.mdiManager.View to com.iver.andami.ui.mdiManager.IWindow
 *
 * Revision 1.6  2006/07/21 10:31:05  jaume
 * *** empty log message ***
 *
 * Revision 1.5  2006/06/19 11:20:26  jorpiell
 * Cambiadas las comprobaciones de una clase con == por instanceof.
 *
 * Revision 1.4  2006/05/30 08:56:57  jaume
 * fixed bug http://www.gvsig.org/phpcollab/tasks/viewtask.php?id=279&msg=addAssignment&PHPSESSID=3fea791a62107ef5325283641039b311#etDAnchor
 *
 * Revision 1.3  2006/05/12 07:47:39  jaume
 * removed unnecessary imports
 *
 * Revision 1.2  2006/05/03 11:10:54  jaume
 * *** empty log message ***
 *
 * Revision 1.1  2006/05/03 07:51:21  jaume
 * *** empty log message ***
 *
 * Revision 1.7  2006/05/02 16:12:12  jorpiell
 * Se ha cambiado la interfaz Extension por dos clases: una interfaz (IExtension) y una clase abstract(Extension). A partir de ahora todas las extensiones deben heredar de Extension
 *
 * Revision 1.6  2006/05/02 15:57:44  jaume
 * Few better javadoc
 *
 * Revision 1.5  2006/04/25 11:40:56  jaume
 * *** empty log message ***
 *
 * Revision 1.4  2006/04/21 10:27:32  jaume
 * exporting now supported
 *
 * Revision 1.3  2006/04/20 17:11:54  jaume
 * Attempting to export
 *
 * Revision 1.2  2006/04/19 16:34:29  jaume
 * *** empty log message ***
 *
 * Revision 1.1  2006/04/19 07:57:29  jaume
 * *** empty log message ***
 *
 * Revision 1.3  2006/04/12 17:10:53  jaume
 * *** empty log message ***
 *
 * Revision 1.2  2006/04/07 12:10:37  jaume
 * *** empty log message ***
 *
 * Revision 1.1  2006/04/04 14:22:22  jaume
 * Now exports MapContext (not yet tested)
 *
 *
 */
package com.iver.cit.gvsig.wmc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLayers;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.plugins.Extension;
import com.iver.andami.plugins.IExtension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.fmap.layers.FLyrWMS;
import com.iver.cit.gvsig.gui.panels.WebMapContextSettingsPanel;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.view.ProjectView;
import com.iver.cit.gvsig.project.documents.view.ProjectViewFactory;
import com.iver.cit.gvsig.project.documents.view.gui.View;

/**
 * Extension to export a view with WMS layers to a OGC's Web Map Context XML
 * file
 *
 * @author jaume domínguez faus - jaume.dominguez@iver.es
 * @author laura díaz sánchez - laura.diaz@iver.es
 */
public class ExportWebMapContextExtension extends Extension {
	private static ArrayList supportedVersions;
	private View viewToExport;
	private WebMapContextSettingsPanel mc;
	private static IExtension thisExtension;

	{
		supportedVersions = new ArrayList();
		supportedVersions.add("1.1.0");
	}

	public void initialize() {
		thisExtension = PluginServices
				.getExtension(ExportWebMapContextExtension.class);
	}

	public void execute(String actionCommand) {
		if (actionCommand.equals("EXPORT")) {
			// Here we collect the info
			ProjectView[] views = getExportableViews();
			if (views.length <= 0) {
				return;
			}
			mc = new WebMapContextSettingsPanel(views);
			PluginServices.getMDIManager().addWindow(mc);

		} else if (actionCommand.equals("DO_EXPORT")) {
			// Here the target file is produced (called from the WebMapContextSettingsPanel)
			String xml = mc.getXML();
			File f = mc.getTargetFile();
			if (xml != null) {
				createFile(f, xml);
			}

		}
	}

	/**
	 * Takes a File object and its XML contents and stores it as a regular
	 * file in the file system.
	 * @param f
	 * @param xml
	 */
	public static void createFile(File f, String xml) {
		if (xml != null) {
			try {
				if (!f.exists()) {
					f.createNewFile();
				}
				BufferedWriter bw = new BufferedWriter(new FileWriter(f));
				bw.write(xml);
				bw.close();
				bw = null;
			} catch (IOException e) {
				NotificationManager.addError(PluginServices.getText(
						thisExtension, "error_writting_file"), e);
			}
		}
	}

	public boolean isEnabled() {
		return true;
	}

	public boolean isVisible() {
		// Will be visible if the current project has, at least, one FLyrWMS.
		Project project = ((ProjectExtension) PluginServices
				.getExtension(ProjectExtension.class)).getProject();
		if (project == null) {
			return false;
		}
		IWindow f = PluginServices.getMDIManager().getActiveWindow();
		if (f instanceof View) {
			View v = (View) f;
			if (v != null && v  instanceof View) {
				// Check if the active contains WMS layers. If so, this view
				// will be the one to be exported.
				FLayers lyrs = v.getMapControl().getMapContext().getLayers();
				for (int i = 0; i < lyrs.getLayersCount(); i++) {
					FLayer lyr = lyrs.getLayer(i);
					if (WebMapContext.containsExportableLayers(lyr)) {
						viewToExport = v;
						return true;
					}
				}
			}
		}

		// Since the active view does not contain WMS layers then will
		// see what about the others. In this case, no view is set to be
		// the exported one.
		viewToExport = null;
		ArrayList views = project.getDocumentsByType(ProjectViewFactory.registerName);
		for (int i = 0; i < views.size(); i++) {
			ProjectView v = ((ProjectView) views.get(i));
			if (v != null) {
				FLayers lyrs = v.getMapContext().getLayers();
				for (int j = 0; j < lyrs.getLayersCount(); j++) {
					FLayer lyr = lyrs.getLayer(j);
					if (WebMapContext.containsExportableLayers(lyr)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * <p>
	 * Searches the views in the current project that can be exported to a
	 * WebMapContext file (with ".cml" extension) and return them in a
	 * ProjectView array.<br>
	 * </p>
	 * <p>
	 * A view is exportable to WebMapContext if it contains at least one FLyrWMS
	 * and in a near future, any other OGC layer such as WCS, WFS, and so on. Only
	 * these layers will be exported. Other kind of layers are ignored since they
	 * are out of the OGC premises.
	 * </p>
	 * @return
	 */
	private ProjectView[] getExportableViews() {
		Project project = ((ProjectExtension) PluginServices
				.getExtension(ProjectExtension.class)).getProject();

		ArrayList views = project.getDocumentsByType(ProjectViewFactory.registerName);
		ArrayList exportableViews = new ArrayList();
		if (viewToExport!=null) {
			exportableViews.add(viewToExport.getModel());
		}

		for (int i = 0; i < views.size(); i++) {
			ProjectView v = ((ProjectView) views.get(i));
			if (v != null) {
				FLayers lyrs = v.getMapContext().getLayers();
				for (int j = 0; j < lyrs.getLayersCount(); j++) {
					FLayer lyr = lyrs.getLayer(j);
					if (lyr instanceof FLyrWMS && !exportableViews.contains(v)) {
						exportableViews.add(v);
						break;
					}
				}
			}
		}
		return (ProjectView[]) exportableViews.toArray(new ProjectView[0]);
	}

}
