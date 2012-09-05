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
package com.iver.cit.gvsig.project;

import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.project.document.table.FeatureTableDocument;
import org.gvsig.project.document.table.FeatureTableDocumentFactory;
import org.gvsig.tools.ToolsLocator;

import com.iver.cit.gvsig.project.documents.ProjectDocumentFactory;
import com.iver.cit.gvsig.project.documents.layout.ProjectMap;
import com.iver.cit.gvsig.project.documents.layout.ProjectMapFactory;
import com.iver.cit.gvsig.project.documents.view.ProjectView;
import com.iver.cit.gvsig.project.documents.view.ProjectViewFactory;



public class ProjectFactory {
	public static ProjectMap createMap(String baseName){
		ProjectDocumentFactory pdf=null;
		try {
			pdf = (ProjectDocumentFactory) ToolsLocator
					.getExtensionPointManager().get("Documents").create(
							ProjectMapFactory.registerName);
		} catch (Exception e) {
			// FIXME Exception
			e.printStackTrace();
		}
		ProjectMap pm=(ProjectMap)pdf.create((Project)null);
		pm.setProjectDocumentFactory(pdf);
		pm.setName(baseName);
		return pm;
	}

	public static FeatureTableDocument createTable(String name, FeatureStore fs){
		ProjectDocumentFactory pdf=null;
		try {
			pdf = (ProjectDocumentFactory) ToolsLocator
					.getExtensionPointManager().get("Documents").create(
							FeatureTableDocumentFactory.registerName);
		} catch (Exception e) {
			// FIXME Exception
			e.printStackTrace();
		}

		FeatureTableDocument pt = FeatureTableDocumentFactory.createTable(name, fs);
		pt.setProjectDocumentFactory(pdf);
		return pt;
	}

	//TODO implementar bien
/*	public static ProjectTable createTable(String viewName, FTable ftable){
		return Table.createTable(viewName, ftable);
	}
*/
	public static ProjectView createView(String viewName){
		ProjectDocumentFactory pdf=null;
		try {
			pdf = (ProjectDocumentFactory) ToolsLocator
					.getExtensionPointManager().get("Documents").create(
							ProjectViewFactory.registerName);
		} catch (Exception e) {
			// FIXME Exception
			e.printStackTrace();
		}
		ProjectView pv=(ProjectView)pdf.create((Project)null);
		pv.setProjectDocumentFactory(pdf);
		pv.setName(viewName);
		return pv;
	}

	public static Project createProject(){
		return new Project();
	}

	public static ProjectExtent createExtent(){
		return new ProjectExtent();
	}



}
