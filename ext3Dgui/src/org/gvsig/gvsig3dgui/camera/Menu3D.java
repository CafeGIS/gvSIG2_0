/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.gvsig3dgui.camera;

import org.gvsig.gvsig3dgui.view.InsertPosition;
import org.gvsig.gvsig3dgui.view.View3D;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.view.IProjectView;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.cit.gvsig.project.documents.view.gui.FPanelExtentSelector;

public class Menu3D extends Extension {


	private boolean activa = true;

	public void execute(String actionCommand) {
//		System.out.println("EXECUTE");
		
		com.iver.andami.ui.mdiManager.IWindow view = PluginServices.getMDIManager().getActiveWindow();
		if (!(view instanceof View3D))
			return;
//		System.out.println("RECUPERADA LA VISTA 3D");
		View3D vista3D =(View3D)view;

		if (actionCommand.equals("ENCUADRE")) {
//			System.out.println("DEBUG: Mostrando herramienta de encuadre");
			// create and show the modal fopen dialog

			FPanelExtentSelector l = new FPanelExtentSelector();

			ProjectExtension p = (ProjectExtension) PluginServices.getExtension(com.iver.cit.gvsig.ProjectExtension.class);
			Project project = p.getProject();
			ExtentListSelectorModel3D modelo = new ExtentListSelectorModel3D(project);
			project.addPropertyChangeListener(modelo);
			l.setModel(modelo);
			l.addSelectionListener(new Encuadrator3D(project, null, vista3D));
//			l.addSelectionListener(new Encuadrator(project, mapa, vista));
			PluginServices.getMDIManager().addWindow(l);
			
//			GUI g = new GUI();
//			//g.setVisible(true);
//			PluginServices.getMDIManager().addWindow((IWindow) g);
		} else if (actionCommand.equals("POINT3D")) {
			InsertPosition insertPosition = new InsertPosition(vista3D.getModel());
			PluginServices.getMDIManager().addWindow(insertPosition);
		}
	}

	public void initialize() {
		// Register new icons
		// extent manegement 
		PluginServices.getIconTheme().registerDefault(
				"encuadrator-icon",
				this.getClass().getClassLoader().getResource(
				"images/encuadre.png"));
		
		
		
		this.setActiva(true);

	}

	public boolean isEnabled() {
		return true;
	}

	public boolean isVisible() {
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices
				.getMDIManager().getActiveWindow();

		if (f == null) {
			return false;
		}

		if (f instanceof View3D) {
			BaseView vista = (BaseView) f;
			IProjectView model = vista.getModel();
			MapContext mapa = model.getMapContext();

			return mapa.getLayers().getLayersCount() > 0;
		}
		return false;
	}

	public void terminate() {
		super.terminate();
	}

	public boolean isActiva() {
		return activa;
	}

	public void setActiva(boolean activa) {
		this.activa = activa;
	}
}
