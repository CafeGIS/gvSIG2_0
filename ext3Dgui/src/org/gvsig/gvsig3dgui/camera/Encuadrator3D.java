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

import org.gvsig.gvsig3dgui.view.View3D;
import org.gvsig.osgvp.viewer.Camera;

import com.iver.ai2.gvsig3d.camera.ProjectCamera;
import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.view.ListSelectorListener;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;

public class Encuadrator3D implements ListSelectorListener {

	private Project project;

	private BaseView vista;

	private MapContext mapa;

	public Encuadrator3D(Project project, MapContext map, View3D vista) {
		this.project = project;
		this.vista = vista;
		mapa = map;
	}

	/**
	 * @see com.iver.utiles.ListSelectorListener#indexesSelected(int[])
	 */
	public void indexesSelected(int[] indices) {
		// mapa.getViewPort().setExtent(project.getExtents()[indices[0]].getExtent());
		
		View3D vi = (View3D) this.vista;
		Object[] l = project.getCameras();

		Camera ca = (Camera) ((ProjectCamera) l[indices[0]]).getCamera();

		vi.setCamera(ca);	
		vi.getCanvas3d().repaint();
		
		
//		ca = null;
//		ca = vi.getCamera();
//		System.out.println("******************************************");
//		System.out.println("INFORMACION DE LA CAMARA");
//		System.out.println("Centro::X " + ca.getCenter().x() + ";Y "
//				+ ca.getCenter().y() + ";Z " + ca.getCenter().z());
//		System.out.println("Ojo::X " + ca.getEye().x() + ";Y " + ca.getEye().y()
//				+ ";Z " + ca.getEye().z());
//		System.out.println("Up::X " + ca.getUp().x() + ";Y " + ca.getUp().y()
//				+ ";Z " + ca.getUp().z());
//		System.out.println("******************************************");
	}

	/**
	 * @see com.iver.utiles.ListSelectorListener#indexesRemoved(int[])
	 */
	public void indexesRemoved(int[] indices) {
//		System.out
//				.println("!!!!!!!!!!!!!!!!!!!!!ENTANDO EN LA FUNCION BORRAR INDICE!!!!");
		for (int i = 0; i < indices.length; i++) {
			this.project.removeCamera(indices[i]);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.iver.utiles.ListSelectorListener#newElement(java.lang.String)
	 */
	public void newElement(String name) {
//		System.out
//				.println("!!!!!!!!!!!!!!!!!!!!!ENTANDO EN LA FUNCION NUEVO ELEMENTO!!!!");
//		System.out.println("nombre del encuadre ---->" + name);

		View3D vi = (View3D) this.vista;
		Camera ca = vi.getCamera();

		ProjectCamera camera = new ProjectCamera();
		camera.setDescription(name);
		camera.setCamera(ca);
		Object[] l = project.getCameras();
		
		boolean exits = false;
		for (int i = 0; i < l.length; i++) {
			ProjectCamera cam = (ProjectCamera) l[i];
			if (cam.getDescription().equals(camera.getDescription())){
				exits = true;
			}
		}
		
		if (!exits){
			project.addCamera(camera);
		}
		
		
//		System.out.println("******************************************");
//		System.out.println("INFORMACION DE LA CAMARA");
//		System.out.println("Centro.... " + ca.getCenter());
//		System.out.println("X " + ca.getCenter().x() + ";Y "
//				+ ca.getCenter().y() + ";Z " + ca.getCenter().z());
//		System.out.println("Ojo....... " + ca.getEye());
//		System.out.println("X " + ca.getEye().x() + ";Y " + ca.getEye().y()
//				+ ";Z " + ca.getEye().z());
//		System.out.println("Up........ " + ca.getUp());
//		System.out.println("X " + ca.getUp().x() + ";Y " + ca.getUp().y()
//				+ ";Z " + ca.getUp().z());
//		System.out.println("******************************************");

		// ProjectExtent extent = ProjectFactory.createExtent();
		// extent.setDescription(name);
		// // extent.setExtent(mapa.getViewPort().getExtent());
		// project.addExtent(extent);
	}

}
