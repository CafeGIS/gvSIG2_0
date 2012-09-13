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

package com.iver.ai2.animationgui.gui.document;

import java.text.DateFormat;
import java.util.Date;
import java.util.Hashtable;

import javax.swing.ImageIcon;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.ProjectDocument;
import com.iver.cit.gvsig.project.documents.ProjectDocumentFactory;
import com.iver.utiles.XMLEntity;

public class ProjectAnimationDocumentFactory extends ProjectDocumentFactory {

	public static String registerName = "ProjectAnimationDocument";
	private int numViews = 0;
	private boolean createFromGUI;
	
	public ProjectDocument createFromGUI(Project project) {
		createFromGUI = true;
		ProjectDocument doc = create(project);
		createFromGUI = false;
		return doc;
	}

	@Override
	public ProjectDocument create(Project project) {
		String viewName = "";
        String aux = PluginServices.getText(this, "untitled");
               viewName = aux + " - " + numViews ++;

        if (project != null) {
            while (existName(project, viewName)) {
                viewName = aux + " - " + numViews++;
            }
        }
//       ProjectDocument.NUMS.put(registerName,new Integer(numViews));
       ProjectAnimationDocument Animation = createAnimation(viewName);
		
		if (Animation != null)
			Animation.setProject(project, 0);

		return Animation;
	}
	

	/**
	 * This method creates a new instance of animation
	 * @param animationName
	 * @return
	 */
	private ProjectAnimationDocument createAnimation(String animationName) {
		 ProjectAnimationDocument animation = new ProjectAnimationDocument();
		 animation.setName(animationName);
		 animation.setCreationDate(DateFormat.getInstance().format(new Date()));
		return animation;
	}

	public ImageIcon getButtonIcon() {
		return new ImageIcon(this.getClass().getClassLoader().getResource(
		"images/animation.png"));
	}

	public String getRegisterName() {
		// TODO Auto-generated method stub
		return ProjectAnimationDocumentFactory.registerName;
	}

	public ImageIcon getSelectedButtonIcon() {
		return new ImageIcon(this.getClass().getClassLoader().getResource(
		"images/animation_sel.png"));
	}

	public boolean resolveImportXMLConflicts(XMLEntity root, Project project,
			Hashtable conflicts) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * Registers in the points of extension the Factory with alias.
	 * 
	 */
	public static void register() {
		register(registerName , new ProjectAnimationDocumentFactory());
	}
	
	/**
	 * Returns the priority of de ProjectDocument.
	 * 
	 * @return Priority.
	 */
	public int getPriority() {
		return 40;
	}

	/**
	 * Returns the priority of the Document in the ProjectWindow list.
	 * 
	 * @return Priority.
	 */
	public int getListPriority() {
		return 40; // so the 3D View appears before other document types
	}
	

	/**
	 * Returns the name of ProjectDocument.
	 * 
	 * @return Name of ProjectDocument.
	 */
	public String getNameType() {
		return  PluginServices.getText(this, "Project_animation_document");
	}

}
