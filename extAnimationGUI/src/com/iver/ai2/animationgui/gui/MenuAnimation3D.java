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
*/

package com.iver.ai2.animationgui.gui;

import javax.swing.JOptionPane;

import com.iver.ai2.animationgui.gui.document.ProjectAnimationDocument;
import com.iver.ai2.animationgui.gui.toc.TocAnimationDate;
import com.iver.ai2.animationgui.gui.util.AnimationUtils;
import com.iver.ai2.gvsig3d.camera.ProjectCamera;
import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.plugins.IExtension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.animation.AnimationContainer;
import com.iver.cit.gvsig.animation.AnimationPlayer;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.utiles.extensionPoints.ExtensionPoints;
import com.iver.utiles.extensionPoints.ExtensionPointsSingleton;

/**
 * @author
 * @since 1.1
 * 
 * Menu of animation toolbar's options.
 */

public class MenuAnimation3D extends Extension {

	// private variable that contains the project
	private Project project;
	// It is the project Document create to create a fast animation.
	private ProjectAnimationDocument pad;

	public void execute(String actionCommand) {

		AnimationContainer ac;
		String prefix = "Default-animation-document";
		// if the button pressed is animation control the animation panel is
		// created.
		if (actionCommand.equals("CONTROLES")) {
			// Getting the animation container.
			if (pad != null) {
				ac = pad.getAnimationContainer();
				AnimationPlayer ap = ac.getAnimationPlayer();
				ap.setAnimationContainer(ac);
				AnimationContol3D fp = new AnimationContol3D(ap);
				PluginServices.getMDIManager().addWindow((IWindow) fp);
			} else {
				if (AnimationUtils.exitsProject(prefix, project)){
					ac = AnimationUtils.getProjectDocument(prefix, project).getAnimationContainer();
					AnimationPlayer ap = ac.getAnimationPlayer();
					ap.setAnimationContainer(ac);
					AnimationContol3D fp = new AnimationContol3D(ap);
					PluginServices.getMDIManager().addWindow((IWindow) fp);
				}else{
					// Show a message that indicates why it is impossible to open
					// the player
					JOptionPane
					.showMessageDialog(null,
					"There is not any animation document. Please create one");
				}
			}

		} else if (actionCommand.equals("ENCUADRECAP")) {// button capture
			// if there is not create the default animation. we will create it.
			pad = AnimationUtils.createAnimationDocument(prefix, project);
			if (!AnimationUtils.exitsProject(prefix, project)){
				project.addDocument(pad);
			}

			// Getting the active windows
			IWindow w = PluginServices.getMDIManager().getActiveWindow();

			// Getting the animation container.
			ac = pad.getAnimationContainer();

			// Capture the new encuadrator
			AnimationUtils.captureEncuadrator((BaseView) w, ac, prefix);

		} else if (actionCommand.equals("CAPACAP")) {// button capture layer
			// pressed.
			System.out.println("pulsado boton de capturar capa!!!");
		} else if (actionCommand.equals("CLEAR_TRACK")) {
			//removeCameras(AnimationPrefix);
			// // AKFinterval = null;
			// cont = 0;
			// ac = this.getAnimationContainer();
			// ac.removeAllTrack();
		}

	}

	/**
	 * This method removes all the captures that there are in the encuadrator
	 * with the substring "substring"
	 * 
	 * @param substring
	 *            the method removes all the captures that contain this
	 *            substring.
	 * 
	 */
	void removeCameras(String substring) {
		// Remove all animation keyframe from the encuadrator.
		Object[] pcList = project.getCameras();
		int i = 0;
		while (i < pcList.length) {
			ProjectCamera projectCamera = (ProjectCamera) pcList[i];
			if (projectCamera.getDescription().contains(substring)) {
				project.removeCamera(i);
				pcList = project.getCameras();
			} else {
				i++;
			}
		}
	}

	public void postInitialize() {

		IExtension extension = PluginServices
				.getExtension(com.iver.cit.gvsig.ProjectExtension.class);
		ProjectExtension pe = (ProjectExtension) extension;
		project = pe.getProject();

		PluginServices.getIconTheme().registerDefault(
				"camera-link-icon",
				this.getClass().getClassLoader().getResource(
						"images/camera_link.png"));
		PluginServices.getIconTheme().registerDefault(
				"camera-add-icon",
				this.getClass().getClassLoader().getResource(
						"images/camera_add.png"));
		PluginServices.getIconTheme().registerDefault(
				"camera-edit-icon",
				this.getClass().getClassLoader().getResource(
						"images/camera_edit.png"));

	}

	public void initialize() {

		// Registering TOC actions
		ExtensionPoints extensionPoints = ExtensionPointsSingleton
				.getInstance();
		extensionPoints.add("View_TocActions", "Generar animacion temporal",
				new TocAnimationDate());
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

		// Only isVisible = true, where the view3D have layers
		if (f instanceof BaseView) {
			return true;
		}
		return false;
	}

}
