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

import java.util.Iterator;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.ai2.animationgui.gui.document.Animation.StateUpdate;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.animation.AnimationContainer;
import com.iver.cit.gvsig.fmap.layers.XMLException;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.ProjectDocument;
import com.iver.cit.gvsig.project.documents.exceptions.OpenException;
import com.iver.cit.gvsig.project.documents.exceptions.SaveException;
import com.iver.utiles.IPersistence;
import com.iver.utiles.XMLEntity;

public class ProjectAnimationDocument extends ProjectDocument {

	
	private static final long serialVersionUID = 1L;
	AnimationContainer animationContainer;
	private Animation animation = null;

	public ProjectAnimationDocument() {
		super();
		this.animationContainer = new AnimationContainer();
	}

	public void afterAdd() {
		// TODO Auto-generated method stub

	}

	public void afterRemove() {
		// TODO Auto-generated method stub

	}

	public IWindow createWindow() {
		// Introduce here the code to make the gui.
		if (animation == null)
			animation = new Animation(this);
		else
			animation.startThread();
		return animation;
	}

	public void exportToXML(XMLEntity root, Project project) {
		// TODO Auto-generated method stub
	}

	public IWindow getProperties() {
		// TODO Auto-generated method stub
		// Introduce here the code to make the gui for the properties.
		return new AnimationProperties(this, false);
	}

	public void importFromXML(XMLEntity root, XMLEntity typeRoot,
			int elementIndex, Project project, boolean removeDocumentsFromRoot) {
		// TODO Auto-generated method stub

	}

	public AnimationContainer getAnimationContainer() {
		return animationContainer;
	}

	public void setAnimationContainer(AnimationContainer animationContainer) {
		this.animationContainer = animationContainer;
	}

	public XMLEntity getXMLEntity() {
		XMLEntity xml = null;
		try {
			xml = super.getXMLEntity();
			xml.addChild(this.animationContainer.getXMLEntity());
		} catch (SaveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return xml;
	}

	public void setXMLEntity(XMLEntity xml) {
		// Busqueda de un hijo a partir de su nombre.
		// Node AnimationContainer in the animation tree. Calling setXmlEntity()
		// methods.
		// Reconstruyendo el framework. Primer paso: nuevo container para
		// almacenar.
		try {
			super.setXMLEntity(xml);

			int animations = 0;
			Iterator<XMLEntity> iterator = xml.findChildren(
					"animationContainer", "true");
			if (iterator.hasNext()) {
				XMLEntity xmlAC = iterator.next();
				animations++;
					String classAC = xmlAC.getStringProperty("className");
					Class<?> clase = Class.forName(classAC);
					animationContainer = (AnimationContainer) clase
							.newInstance();
			}

			// Crear objeto a partir de su clase.
			Iterator<XMLEntity> iterXML = xml.findChildren("className",
					"com.iver.cit.gvsig.animation.AnimationContainer");
			if (iterXML.hasNext()) {
				((IPersistence) animationContainer)
						.setXMLEntity((XMLEntity) iterXML.next());
			}

		} catch (XMLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (OpenException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ReadDriverException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


}
