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
package com.iver.cit.gvsig.project.documents.layout.fframes;

import java.util.ArrayList;

import javax.swing.AbstractListModel;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;
import com.iver.cit.gvsig.project.documents.view.ProjectViewFactory;


/**
 * Modelo de la Lista de vistas a seleccionar.
 *
 * @author Vicente Caballero Navarro
 */
public class ListViewModel extends AbstractListModel {
	private ArrayList views = new ArrayList();

	/**
	 * Añade las vistas que tiene el proyecto a la lista.
	 */
	public void addViews() {
		ProjectExtension projectextension = (ProjectExtension) PluginServices.getExtension(com.iver.cit.gvsig.ProjectExtension.class);
		views = projectextension.getProject().getDocumentsByType(ProjectViewFactory.registerName);
	}

	/**
	 * Add all fframeviews into a list.
	 *
	 * @param l Layout.
	 */
	public void addViews(Layout l) {
		int num = 0;
		l.getLayoutContext().updateFFrames();
		IFFrame[] fframes=l.getLayoutContext().getFFrames();
		for (int i = 0; i < fframes.length; i++) {
			IFFrame f = fframes[i];

			if (f instanceof FFrameView) {
				//((FFrameView)f).getView().setName("FFrameView "+num+((FFrameView)f).getName());
				views.add(f);
				((FFrameView) f).setNum(num);
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
