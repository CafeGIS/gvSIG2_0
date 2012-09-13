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

package com.iver.ai2.animationgui.gui.toc;

import java.sql.Date;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.IExtension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.animation.AnimationContainer;
import com.iver.cit.gvsig.animation.IAnimationType;
import com.iver.cit.gvsig.animation.interval.AnimationDatedInterval;
import com.iver.cit.gvsig.animation.traks.AnimationDatedTrack;
import com.iver.cit.gvsig.animation.traks.IAnimationTrack;
import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.view.IProjectView;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;
import com.iver.utiles.extensionPoints.ExtensionPoint;
import com.iver.utiles.extensionPoints.ExtensionPoints;
import com.iver.utiles.extensionPoints.ExtensionPointsSingleton;

public class TocAnimationDate extends AbstractTocContextMenuAction {

	private AnimationDatedTrack adt;
	
	FLayer layer;

	public String getGroup() {
		return "group6"; // FIXME
	}

	public int getGroupOrder() {
		return 60;
	}

	public int getOrder() {
		return 10;
	}

	public boolean isEnabled(ITocItem item, FLayer[] selectedItems) {
		// return selectedItems.length == 1;

		// return true;

		boolean enable = false;

		if (selectedItems.length > 0)
			enable = true;

		return enable;

	}

	public boolean isVisible(ITocItem item, FLayer[] selectedItems) {
		// return true;
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices
				.getMDIManager().getActiveWindow();

		if (f == null) {
			return false;
		}

		if (!isTocItemBranch(item))
			return false;

		BaseView vista = (BaseView) f;
		
		IProjectView model = vista.getModel();
		MapContext mapa = model.getMapContext();
	
		return mapa.getLayers().getLayersCount() > 0;
	}

	public void execute(ITocItem item, FLayer[] selectedItems) {
		System.out
				.println("MENU CONTEXTUAL DE GENERACION DE ANIMACIONES DE FECHAS");

//		// Getting view3
//		com.iver.andami.ui.mdiManager.IWindow view = PluginServices
//				.getMDIManager().getActiveWindow();
//		BaseView vista3D =  (BaseView) view;
//		 MapControl model = (MapControl)vista3D.getModel();
//		model.setEnabled(true);
		
		layer = selectedItems[0];
		
		// Generate animation using filters
		generateAniamtionDate();
		
		// introducir layer
		
//		this.adt.setAnimatedObject(layer);
		// Show the animation control player
//		AnimationDateModePanel = new AnimationDateModePanel();
//		AnimationDateModePanel.setModal(true);
//		AnimationDateModePanel.pack();
//		AnimationDateModePanel.setVisible(true);
		
		
		AnimationDateModePanel fp = new AnimationDateModePanel(adt.getAnimationType());
		PluginServices.getMDIManager().addWindow((IWindow) fp);
		
		
	}

	public String getText() {
		// Name that appears in toc menu
		return "Animacion de fecha";
	}

	private Project getProject() {
		IExtension extension = PluginServices
				.getExtension(com.iver.cit.gvsig.ProjectExtension.class);
		ProjectExtension pe = (ProjectExtension) extension;
		return pe.getProject();
	}

	private void generateAniamtionDate() {
		// TODO Auto-generated method stub
		// traerse el contenedor
		//Project project = this.getProject();
		AnimationContainer ac = null;//(AnimationContainer) project.getAnimationContainer();
		// desactivar los track existentes

		// crear el track
		adt = getAnimationDateTrack("Date-track", ac);
		// generar un intervalo por cada fecha

		// DEBUGGGGG!!!!
		System.out.println(ac);
	}

	private AnimationDatedTrack getAnimationDateTrack(String name,
			AnimationContainer ac) {
		AnimationDatedTrack at = null;
		IAnimationTrack aa = ac.findTrack(name);
		if (aa == null) {

			ExtensionPoints extensionPoints = ExtensionPointsSingleton
					.getInstance();
			ExtensionPoint extPoint = ((ExtensionPoint) extensionPoints
					.get("Animation"));

			// traerse el tipo de animacion
			IAnimationType animationDate3D = null;
			try {
				animationDate3D = (IAnimationType) extPoint
						.create("AnimationDate3D");
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			animationDate3D.setAnimatedObject(layer);
			at = (AnimationDatedTrack) ac.CreateDatedTrack(animationDate3D);
			at.setName(name);
			at.setEnable(true);
			
			//crear el intervalo
			AnimationDatedInterval adi = (AnimationDatedInterval) at.createInterval();
			adi.setBeginDateInterval(Date.valueOf("2000-01-01"));
			adi.setEndDateInterval(Date.valueOf("2000-01-04"));
			
			
//			at.setAnimatedObject(layer);
			// at.setAnimationType(animationDate3D);
		}

		return at;

	}

}
