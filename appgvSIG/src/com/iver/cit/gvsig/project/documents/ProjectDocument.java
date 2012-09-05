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
package com.iver.cit.gvsig.project.documents;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint.Extension;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.exceptions.OpenException;
import com.iver.cit.gvsig.project.documents.exceptions.SaveException;
import com.iver.cit.gvsig.project.documents.gui.WindowData;
import com.iver.utiles.XMLEntity;
import com.iver.utiles.XMLException;


/**
 * Clase base de los elementos del proyecto (mapas, tablas y vistas)
 *
 * @author Fernando González Cortés
 */
public abstract class ProjectDocument implements Serializable {
	public static HashMap<String,Integer> NUMS = new HashMap<String,Integer>();
	protected PropertyChangeSupport change;
	protected Project project;
	protected int index;
	protected String name;
	protected String creationDate;
	protected String owner;
	protected String comment;
	private boolean locked = false;
	protected WindowData windowData = null;
	private boolean isModified=false;
	private ProjectDocumentFactory projectDocumentFactory;
	private ArrayList<ProjectDocumentListener> projectDocListener = new ArrayList<ProjectDocumentListener>();

	/**
	 * Creates a new ProjectElement object.
	 */
	public ProjectDocument() {
		creationDate = DateFormat.getDateInstance().format(new Date());
		change = new PropertyChangeSupport(this);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return name;
	}

	/**
	 * Obtiene el nombre del elemento
	 *
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Establece el nombre del elemento
	 *
	 * @param string
	 */
	public void setName(String string) {
		String oldValue = name;
		name = string;
		change.firePropertyChange("name", oldValue, name);
	}

	/**
	 * Obtiene la fecha de creación del elemento
	 *
	 * @return
	 */
	public String getCreationDate() {
		return creationDate;
	}

	/**
	 * Obtiene el propietario del elemento
	 *
	 * @return
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * Establece la fecha de creación del elemento.
	 *
	 * @param string
	 */
	public void setCreationDate(String string) {
		creationDate = string;
		change.firePropertyChange("creationDate", creationDate, creationDate);
	}

	/**
	 * Establece el propietario del elemento
	 *
	 * @param string
	 */
	public void setOwner(String string) {
		owner = string;
		change.firePropertyChange("owner", owner, owner);
	}

	/**
	 * Obtiene los comentarios del proyecto
	 *
	 * @return
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Establece los comentarios del proyecto
	 *
	 * @param string
	 */
	public void setComment(String string) {
		comment = string;
		change.firePropertyChange("comment", comment, comment);
	}

	/**
	 * Añade un listener para los cambios en las bounded properties
	 *
	 * @param listener
	 */
	public synchronized void addPropertyChangeListener(
		PropertyChangeListener listener) {
		change.addPropertyChangeListener(listener);
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 * @throws XMLException
	 * @throws SaveException
	 */
	public XMLEntity getXMLEntity() throws SaveException {
		XMLEntity xml = new XMLEntity();
		try{
		//xml.putProperty("nameRegister",this.getRegisterName());
		xml.putProperty("className", projectDocumentFactory.getRegisterName());
		xml.putProperty("comment", comment);
		xml.putProperty("creationDate", creationDate);
		xml.putProperty("name", name);
		xml.putProperty("owner", owner);
		}catch (Exception e) {
			throw new SaveException(e,this.getClass().getName());
		}
		return xml;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param xml DOCUMENT ME!
	 * @param p DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 *
	 * @throws XMLException
	 * @throws DriverException
	 * @throws DriverIOException
	 * @throws OpenException
	 */
	public static ProjectDocument createFromXML(XMLEntity xml, Project p)
		throws XMLException {
		ProjectDocumentFactory pde = null;
			try {
				pde = (ProjectDocumentFactory) ToolsLocator
					.getExtensionPointManager().get("Documents").create(
							xml.getStringProperty("className"));
			} catch (InstantiationException e) {
				NotificationManager.showMessageError(PluginServices.getText(p,"documento_no_reconocido")+": "+xml.getStringProperty("className"),
						e);
			} catch (IllegalAccessException e) {
				NotificationManager.showMessageError(PluginServices.getText(p,"documento_no_reconocido")+": "+xml.getStringProperty("className"),
						e);
			}catch (Exception e) {
				throw new OpenException(e,xml.getStringProperty("className"));
			}
			if (pde==null){
				Exception e=new Exception(PluginServices.getText(p,"documento_no_reconocido")+": "+xml.getStringProperty("className"));
				NotificationManager.showMessageWarning(PluginServices.getText(p,"documento_no_reconocido")+": "+xml.getStringProperty("className"),e);
				throw new OpenException(e,xml.getStringProperty("className"));
			}
			ProjectDocument pe=pde.create(p);
			pe.setProjectDocumentFactory(pde);
			return pe;

	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param xml DOCUMENT ME!
	 * @param p DOCUMENT ME!
	 *
	 * @throws XMLException
	 * @throws OpenException
	 * @throws ReadException
	 * @throws com.iver.utiles.XMLException
	 * @throws ReadDriverException
	 */
	public void setXMLEntity(XMLEntity xml)
		throws XMLException{

		this.setComment(xml.getStringProperty("comment"));
		this.setCreationDate(xml.getStringProperty("creationDate"));
		this.setName(xml.getStringProperty("name"));
		this.setOwner(xml.getStringProperty("owner"));

	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public Project getProject() {
		return project;
	}
	/**
	 * DOCUMENT ME!
	 *
	 * @param project DOCUMENT ME!
	 */
	public void setProject(Project project, int index) {
		this.project = project;
		this.index = index;
	}
	public int getIndex() {
		return index;
	}

	/**
	 * Locks this project element protecting it from deleting from the project.
	 */
	public void lock() {
		locked = true;
	}

	/**
	 * Unlocks this element. So, from now on, it can be removed from the project.
	 */
	public void unlock() {
		locked = false;
	}

	/**
	 * Tells whether if this project's element is locked/protected or not. A protected
	 * element cannot be removed from the current project.
	 *
	 * @see <b>lock()</b> and <b>unlock()</b> methods.
	 *
	 * @return true if it is locked, false otherwise
	 */
	public boolean isLocked() {
		return locked;
	}

	public abstract IWindow createWindow();
	public abstract IWindow getProperties();
	public abstract void afterRemove();
	public abstract void afterAdd();


	public void setProjectDocumentFactory(
			ProjectDocumentFactory projectDocumentFactory) {
		this.projectDocumentFactory = projectDocumentFactory;
	}

	public ProjectDocumentFactory getProjectDocumentFactory() {
		return projectDocumentFactory;
	}

	public abstract void exportToXML(XMLEntity root, Project project)  throws SaveException ;

	public abstract void importFromXML(XMLEntity root, XMLEntity typeRoot,int elementIndex ,Project project, boolean removeDocumentsFromRoot) throws XMLException, OpenException, ReadException;


	public void importFromXML(XMLEntity root, XMLEntity typeRoot,int elementIndex ,Project project) throws XMLException, OpenException, ReadException{
		importFromXML(root,typeRoot, elementIndex,project,false);
	}

	/**
	 * Get the layout properties (size, position, state of the components)
	 * of the window associated with this ProjectDocument.
	 * This is used to re-open the window with the same properties it had
	 * when it was closed.
	 *
	 * @return A WindowData object storing the properties of the window.
	 */
	public WindowData getWindowData() {
		return windowData;
	}

	/**
	 * Store the layout properties (size, position, state of the components)
	 * of the window associated with this ProjectDocument.
	 * This is used to re-open the window with the same properties it had
	 * when it was closed.
	 */
	public void storeWindowData(WindowData data) {
		windowData = data;
	}

	public boolean isModified() {
		return isModified;
	}

	public void setModified(boolean modified) {
		isModified=modified;
	}

	public static void initializeNUMS() {
		NUMS.clear();
		Iterator<Extension> iterator = ToolsLocator.getExtensionPointManager()
				.get(
				"Documents").iterator();
		while (iterator.hasNext()) {
			try {
				ProjectDocumentFactory documentFactory = (ProjectDocumentFactory) iterator
						.next().create();
				NUMS.put(documentFactory.getRegisterName(),new Integer(0));
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassCastException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * Register a ProjectViewListener.
	 * @param  listener
	 *         ProjectViewListener
	 */
	public void addProjectViewListener(ProjectDocumentListener listener) {
		if(this.projectDocListener.indexOf(listener) == -1)
			this.projectDocListener.add(listener);
	}
	/**
	 * Throw this event when a new window is created
	 * @param  window
	 *         IWindow created
	 */
	protected void callCreateWindow(IWindow window) {
		for (int i = 0; i < projectDocListener.size(); i++)
			projectDocListener.get(i).windowCreated(window);
	}
}
