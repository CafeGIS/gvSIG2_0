/*
 * Created on 02-jun-2004
 *
 */
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
package org.gvsig.gvsig3dgui.layout.fframe;

import java.util.ArrayList;

import javax.swing.AbstractListModel;
import javax.swing.JOptionPane;

import org.gvsig.gvsig3dgui.ProjectView3D;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;
import com.iver.cit.gvsig.project.documents.view.ProjectViewBase;
 

/**
 * Modelo de la Lista de vistas a seleccionar.
 *
 * @author Vicente Caballero Navarro
 */
public class ListView3DModel extends AbstractListModel {
	private ArrayList views = new ArrayList();

	/**
	 * Añade las vistas que tiene el proyecto a la lista.
	 */
//	public void addViews() {
//		ProjectExtension projectextension = (ProjectExtension) PluginServices.getExtension(com.iver.cit.gvsig.ProjectExtension.class);
//		views = projectextension.getProject().getDocumentsByType(ProjectViewFactory.registerName);
//	}
	
	
	public void addViews() {
		ProjectExtension projectextension = (ProjectExtension) PluginServices
				.getExtension(com.iver.cit.gvsig.ProjectExtension.class);
		// views =
		// projectextension.getProject().getDocumentsByType(ProjectViewFactory.registerName);
		ArrayList todos = projectextension.getProject().getDocuments();
		views.clear();
		String vistas = "Las siguientes vistas que se muestran a continuacion no pueden "
				+ "introducirse en el mapa si no estan abiertas\n";
		boolean exits = false;
		for (int i = 0; i < todos.size(); i++) {
			if (todos.get(i) instanceof ProjectView3D) {
				ProjectView3D projectView3D = (ProjectView3D) todos.get(i);
				if (projectView3D.getCanvas3d() != null)
					views.add(projectView3D);
				else {
					exits = true;
					vistas += " " + projectView3D.getName() + "\n";
				}
			}
		}
		if (exits) {
			JOptionPane.showMessageDialog(null, vistas);
		}
	}

	/**
	 * Add all fframeviews into a list.
	 * 
	 * @param l
	 *            Layout.
	 */
	public void addViews(Layout l) {
		int num = 0;
		l.getLayoutContext().updateFFrames();
		IFFrame[] fframes=l.getLayoutContext().getFFrames();
		for (int i = 0; i < fframes.length; i++) {
			IFFrame f = fframes[i];

			if (f instanceof FFrameView3D) {
				//((FFrameView)f).getView().setName("FFrameView "+num+((FFrameView)f).getName());
				views.add(f);
				((FFrameView3D) f).setNum(num);
				num++;
			}
		}

		//ProjectExtension projectextension =(ProjectExtension)App.instance.getPc().getExtension(com.iver.cit.gvsig.ProjectExtension.class);
		//views=projectextension.getProject().getViews();
	}

	/**
	 * Devuelve el ArrayList con las FFrameView.
	 *
	 * @return DOCUMENT ME!
	 */
	//public ArrayList getViews() {
	//	return views;
	//}

	/**
	 * @see javax.swing.ListModel#getSize()
	 */
	public int getSize() {
		return views.size();
	}

	/**
	 * @see javax.swing.ListModel#getElementAt(int)
	 */
	public Object getElementAt(int index) {
		return views.get(index);
	}
}
