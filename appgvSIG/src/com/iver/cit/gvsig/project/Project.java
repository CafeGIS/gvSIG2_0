/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
 *   Av. Blasco Ib��ez, 50
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

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.crs.CRSFactory;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.project.document.table.FeatureTableDocument;
import org.gvsig.project.document.table.FeatureTableDocumentFactory;
import org.gvsig.project.document.table.LinkSelectionObserver;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.SingletonWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.Version;
import com.iver.cit.gvsig.project.documents.ProjectDocument;
import com.iver.cit.gvsig.project.documents.ProjectDocumentFactory;
import com.iver.cit.gvsig.project.documents.exceptions.OpenException;
import com.iver.cit.gvsig.project.documents.exceptions.SaveException;
import com.iver.cit.gvsig.project.documents.gui.IDocumentWindow;
import com.iver.cit.gvsig.project.documents.gui.ProjectWindow;
import com.iver.cit.gvsig.project.documents.gui.WindowData;
import com.iver.cit.gvsig.project.documents.layout.ProjectMap;
import com.iver.cit.gvsig.project.documents.layout.ProjectMapFactory;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;
import com.iver.cit.gvsig.project.documents.view.ProjectView;
import com.iver.cit.gvsig.project.documents.view.ProjectViewFactory;
import com.iver.utiles.IPersistence;
import com.iver.utiles.PostProcessSupport;
import com.iver.utiles.StringUtilities;
import com.iver.utiles.XMLEntity;
import com.iver.utiles.XMLException;

/**
 * Clase que representa un proyecto de openSIG
 *
 * @author Fernando Gonz�lez Cort�s
 */
public class Project implements Serializable, PropertyChangeListener {
	public static String VERSION = Version.format();

	static private IProjection defaultProjection = null;

	static private IProjection defaultFactoryProjection = CRSFactory
			.getCRS("EPSG:23030");

	/*
	 * distiguishing between a static field "defaultSelectionColor" and a
	 * selectionColor field will allow to define default color in a multiple
	 * project scenario
	 */
	static private Color defaultSelectionColor = Color.YELLOW;

	private static int defaultMapUnits = -1;

	private static int defaultDistanceUnits = -1;

	private static int defaultDistanceArea = -1;

	private PropertyChangeSupport change;

	private boolean modified = false;

	private String name = PluginServices.getText(this, "untitled");

	// private String path;
	private String creationDate = new Date().toGMTString();

	private String modificationDate = new Date().toGMTString();

	private String owner = "";

	private String comments = "";

	private Color selectionColor = null;

	// private ArrayList views = new ArrayList();
	// private ArrayList tables = new ArrayList();
	// private ArrayList maps = new ArrayList();
	private ArrayList<ProjectDocument> documents = new ArrayList<ProjectDocument>();

	private ArrayList extents = new ArrayList();

	// Lista de objetos del tipo camera. Necesarios para almacenar la posicion
	// del usuario haciendo uso de los marcadores
	private List cameras = new ArrayList();

	/**
	 * this is a runtime-calculated value, do NOT persist it!
	 */
	private long signatureAtStartup;
	private IProjection projection;

	/**
	 * Stores the initial properties of the windows, to be restored just after
	 * the project is loaded. It's an ordered iterator of XMLEntity objects,
	 * each containing a XML version of a WindowInfo object.
	 */
	private Iterator initialWindowProperties = null;

	private TreeMap<ProjectDocument,Integer> sortedDocuments = new TreeMap<ProjectDocument,Integer>(new Comparator() {
		public int compare(Object o1, Object o2) {
			if ((o1 != null) && (o2 != null)) {
				int priority1 = ((ProjectDocument) o1)
						.getProjectDocumentFactory().getPriority();
				int priority2 = ((ProjectDocument) o2)
						.getProjectDocumentFactory().getPriority();
				if (priority1 >= priority2) {
					return 1;
				}
				return -1;
			}
			return 0;
		}
	}); // Para poder ordenar

	/**
	 * Creates a new Project object.
	 */
	public Project() {
		change = new PropertyChangeSupport(this);

		// change.addPropertyChangeListener(this);
		creationDate = DateFormat.getDateInstance().format(new Date());
		modificationDate = creationDate;
		setSelectionColor(getDefaultSelectionColor());
		getDefaultProjection(); //For initialize it
		// signatureAtStartup = computeSignature();

		/*
		 * LayerFactory.setDriversPath(PluginServices.getPluginServices(this)
		 * .getPluginDirectory() .getAbsolutePath() + File.separator +
		 * "drivers");
		 */
	}

	/**
	 * Obtiene la fecha de creaci�n del proyecto
	 *
	 * @return
	 */
	public String getCreationDate() {
		return creationDate;
	}

	/**
	 * Obtiene el nombre del proyecto
	 *
	 * @return
	 */
	public String getName() {
		return name;
	}

	// /**
	// * Obtiene la ruta completa del fichero donde se guardo por �ltima vez el
	// * proyecto
	// *
	// * @return
	// */
	// public String getPath() {
	// return path;
	// }

	/**
	 * Asigna la fecha de creaci�n del proyecto. Este m�todo tiene sentido s�lo
	 * por que al recuperar la fecha del XML hay que asignarla al objeto
	 * proyecto de alguna manera. La fecha se asigna en el constructor y no se
	 * deber�a de modificar nunca
	 *
	 * @param string
	 */
	public void setCreationDate(String string) {
		creationDate = string;
		//modified = true;
		change.firePropertyChange("", null, null);
	}

	/**
	 * Establece el nombre del proyecto
	 *
	 * @param string
	 */
	public void setName(String string) {
		name = string;
		//modified = true;
		change.firePropertyChange("", null, null);
	}

	/**
	 * Devuelve a partir de la capa la tabla asociada.
	 *
	 * @param co
	 *            Capa.
	 *
	 * @return ProjectTable de la tabla asociada.
	 */
	public FeatureTableDocument getTable(FLyrVect co) {
		ArrayList tables = getDocumentsByType(FeatureTableDocumentFactory.registerName);
		/**
		 * Como las tablas se pueden a�adir cuando se pincha en "ver tabla" de
		 * una capa, se puede intentar a�adir dos veces la misma tabla
		 */
		for (int i = 0; i < tables.size(); i++) {
			try {
				if (((FeatureTableDocument) tables.get(i)).getStore().equals(co.getFeatureStore())){
//			if (((FeatureTableDocument) tables.get(i)).getAssociatedTable() == co) {
					return (FeatureTableDocument) tables.get(i);
				}
			} catch (ReadException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	/**
	 * Devuelve a partir del nombre la tabla asociada.
	 *
	 * @param name
	 *            Nombre.
	 * @deprecated utilizar getProjectDocumentByName(...);
	 * @return ProjectTable de la tabla asociada.
	 */
	public FeatureTableDocument getTable(String name) {
		ArrayList tables = getDocumentsByType(FeatureTableDocumentFactory.registerName);
		/**
		 * Como las tablas se pueden a�adir cuando se pincha en "ver tabla" de
		 * una capa, se puede intentar a�adir dos veces la misma tabla
		 */
		for (int i = 0; i < tables.size(); i++) {
			if (((FeatureTableDocument) tables.get(i)).getName().equals(name)) {
				return (FeatureTableDocument) tables.get(i);
			}
		}

		return null;
	}

	private boolean isModifiedDocuments() {
		ProjectDocument[] documents=getDocuments().toArray(new ProjectDocument[0]);
		for (int i=0;i<documents.length;i++) {
			if (documents[i].isModified()) {
				return true;
			}
		}
		return false;
	}
//	/**
//	 * Devuelve true si el proyecto (o alguna tabla, vista o mapa que contiene)
//	 * fue modificado
//	 *
//	 * @return
//	 */
//	public boolean isModified() {
//		if ((this.getDocuments().size() == 0) && !modified && !isModifiedDocuments()) {
//			return false;
//		}
//		return true;
//		// /return modified; TODO El atributo modified solo detecta cuando se
//		// elimina o a�ade una vista,
//		// /mapa o tabla pero no cuando se modifican.
//	}

	/**
	 * Obtiene los comentarios
	 *
	 * @return
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * Obtiene la fecha de la �ltima modificaci�n
	 *
	 * @return
	 */
	public String getModificationDate() {
		return modificationDate;
	}

	/**
	 * Obtiene el propietario del proyecto
	 *
	 * @return
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * Establece una cadena como comentarios al proyecto
	 *
	 * @param string
	 */
	public void setComments(String string) {
		comments = string;
		//modified = true;
		change.firePropertyChange("", null, null);
	}

	/**
	 * Establece la fecha de la �ltima modificaci�n
	 *
	 * @param string
	 */
	public void setModificationDate(String string) {
		modificationDate = string;
		//modified = true;
		change.firePropertyChange("", null, null);
	}

	/**
	 * Establece el propietario del proyecto
	 *
	 * @param string
	 */
	public void setOwner(String string) {
		owner = string;
		//modified = true;
		change.firePropertyChange("", null, null);
	}

	/**
	 * Establece el flag de modificado del proyecto
	 *
	 * @param b
	 */
	public void setModified(boolean b) {
		modified = b;
		if (modified==false) {
			ProjectDocument[] documents=getDocuments().toArray(new ProjectDocument[0]);
			for (int i=0;i<documents.length;i++) {
				documents[i].setModified(false);
			}
		}
	}

	/**
	 * Obtiene el color de selecci�n que se usar� en el proyecto
	 *
	 * @return
	 */
	public Color getSelectionColor() {
		if (selectionColor == null) {
			selectionColor = defaultSelectionColor;
		}
		return selectionColor;
	}

	/**
	 * Establece el color de selecci�n
	 *
	 * @param color
	 */
	public void setSelectionColor(Color color) {
		selectionColor = color;
		MapContext.setSelectionColor(color);
		//modified = true;
		change.firePropertyChange("selectionColor", null, color);
	}

	/**
	 * Obtiene el color como un entero para su serializaci�n a XML
	 *
	 * @return
	 */
	public String getColor() {
		return StringUtilities.color2String(selectionColor);
	}

	/**
	 * M�todo invocado al recuperar de XML para establecer el color de seleccion
	 * del proyecto
	 *
	 * @param color
	 *            Entero que representa un color
	 */
	public void setColor(String color) {
		//modified = true;
		selectionColor = StringUtilities.string2Color(color);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		//this.modified = true;
		change.firePropertyChange(evt);
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param arg1
	 */
	public void addExtent(ProjectExtent arg1) {
		extents.add(arg1);
		//modified = true;
		change.firePropertyChange("addExtent", null, null);
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param arg0
	 *
	 * @return
	 */
	public Object removeExtent(int arg0) {
		//modified = true;
		change.firePropertyChange("delExtent", null, null);

		return extents.remove(arg0);
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public ProjectExtent[] getExtents() {
		return (ProjectExtent[]) extents.toArray(new ProjectExtent[0]);
	}

	/**
	 * Metodo que a�ade una nueva camera a la lista de cameras
	 *
	 * @param arg1
	 *            camera introducida
	 */
	public void addCamera(Object arg1) {
		this.cameras.add(arg1);
		//modified = true;
		change.firePropertyChange("addCamera", null, null);
	}

	/**
	 * Metodo que borra de la lisat un elemento seleccionado
	 *
	 * @param arg0
	 *            indice del elemento que se va a borrar
	 *
	 * @return resultado de la operacion de borrado
	 */
	public Object removeCamera(int arg0) {
		//modified = true;
		change.firePropertyChange("delCamera", null, null);

		return this.cameras.remove(arg0);
	}

	/**
	 * Metodo que devuelve la lista de cameras
	 *
	 * @return lista de objetos de tipo camera
	 */
	public Object[] getCameras() {
		return this.cameras.toArray(new Object[0]);
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param arg0
	 */
	public synchronized void addPropertyChangeListener(
			PropertyChangeListener arg0) {
		change.addPropertyChangeListener(arg0);
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @deprecated utilizar getDocument(String s);
	 * @return
	 */
	public ArrayList getMaps() {
		return getDocumentsByType(ProjectMapFactory.registerName);
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @deprecated utilizar getDocument(String s);
	 * @return
	 */
	public ArrayList getTables() {
		return getDocumentsByType(FeatureTableDocumentFactory.registerName);
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @deprecated utilizar getDocument(String s);
	 * @return
	 */
	public ArrayList getViews() {
		return getDocumentsByType(ProjectViewFactory.registerName);
	}

	/**
	 * A�ade un mapa al proyecto
	 *
	 * @deprecated utilizar addDocument(ProjectDocument pD);
	 * @param m
	 */
	public void addMap(ProjectMap m) {
		addDocument(m);
	}

	/**
	 * Elimina un mapa del proyecto
	 *
	 * @deprecated utilizar delDocument(ProjectDocument pD);
	 * @param i
	 *            indice del mapa
	 */
	public void delMap(int i) {
		ArrayList list = getDocumentsByType(ProjectMapFactory.registerName);
		delDocument((ProjectDocument) list.get(i));
	}

	/**
	 * A�ade una tabla al proyecto
	 *
	 * @deprecated utilizar addDocument(ProjectDocument pD);
	 * @param t
	 */
	public void addTable(FeatureTableDocument t) {
		addDocument(t);
	}

	/**
	 * Elimina una tabla del proyecto
	 *
	 * @deprecated utilizar delDocument(ProjectDocument pD);
	 * @param i
	 *            indice de la tabla
	 */
	public void delTable(int i) {
		ArrayList list = getDocumentsByType(FeatureTableDocumentFactory.registerName);
		delDocument((ProjectDocument) list.get(i));
	}

	/**
	 * A�ade una vista al proyecto
	 *
	 * @deprecated utilizar addDocument(ProjectDocument pD);
	 * @param v
	 */
	public void addView(ProjectView v) {
		addDocument(v);
	}

	/**
	 * Elimina una tabla del proyecto
	 *
	 * @deprecated utilizar delDocument(ProjectDocument pD);
	 * @param i
	 *            indice del proyecto
	 */
	public void delView(int i) {
		ArrayList list = getDocumentsByType(ProjectViewFactory.registerName);
		delDocument((ProjectDocument) list.get(i));
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 * @throws com.iver.utiles.XMLException
	 *
	 * @throws DriverException
	 * @throws XMLException
	 */
	public XMLEntity getXMLEntity() throws XMLException {
		XMLEntity xml = new XMLEntity();
		xml.putProperty("className", this.getClass().getName());
		xml.putProperty("VERSION", VERSION);
		xml.putProperty("comments", getComments());
		xml.putProperty("creationDate", creationDate);

		int numExtents = extents.size();
		xml.putProperty("numExtents", numExtents);

		for (int i = 0; i < numExtents; i++) {
			xml.addChild(((ProjectExtent) extents.get(i)).getXMLEntity());
		}

		// Guardando propiedades de las camaras
		int numCameras = this.cameras.size();
		xml.putProperty("numCameras", numCameras);

		for (int i = 0; i < numCameras; i++) {
			xml.addChild(((IPersistence) this.cameras.get(i)).getXMLEntity());
		}

		// NUEVO: ESTO ESTA EN PRUEBAS. SIRVE PARA
		// BORRAR LAS REFERENCIAS A DATASOURCES QUE HEMOS
		// BORRADO. Hay que probar a borrarlos cuando se
		// borra una tabla y cuando se borra una capa.
//		try {
//			cleanBadReferences();
//		}catch (Exception e) {
//			NotificationManager.addError("clean_bad_references",e);
//		}
//		SourceInfo[] infos = LayerFactory.getDataSourceFactory()
//				.getDriverInfos();
//		xml.putProperty("data-source-count", infos.length);
//
//		for (int i = 0; i < infos.length; i++) {
//			SourceInfo di = infos[i];
//			XMLEntity child = this.getSourceInfoXMLEntity(di);
//			xml.addChild(child);
//		}
		int numDocuments = 0;
		for (int i = 0; i < documents.size(); i++) {
			try {
				XMLEntity xmlchild = (documents.get(i))
						.getXMLEntity();
				xmlchild.putProperty("tagName","document"+i);
				xml.addChild(xmlchild);
				numDocuments++;
			} catch (SaveException e) {
				e.showError();
			}
		}
		xml.putProperty("numDocuments", numDocuments);
		/*
		 * int numViews=0; for (int i = 0; i < views.size(); i++) { try {
		 * XMLEntity xmlchild=((ProjectView) views.get(i)).getXMLEntity();
		 * xml.addChild(xmlchild); numViews++; } catch (SaveException e) {
		 * e.showError(); } } xml.putProperty("numViews", numViews);
		 *
		 * int numMaps=0; for (int i = 0; i < maps.size(); i++) { try {
		 * XMLEntity xmlchild=((ProjectMap) maps.get(i)).getXMLEntity();
		 * xml.addChild(xmlchild); numMaps++; } catch (SaveException e) {
		 * e.showError(); } } xml.putProperty("numMaps", numMaps);
		 */
		xml.putProperty("modificationDate", modificationDate);
		xml.putProperty("name", name, false);
		xml.putProperty("owner", owner);
		xml.putProperty("selectionColor", StringUtilities
				.color2String(selectionColor));
		/*
		 * int numTables=0; for (int i = 0; i < tables.size(); i++) { try {
		 * XMLEntity xmlchild=((ProjectTable) tables.get(i)).getXMLEntity();
		 * xml.addChild(xmlchild); numTables++; } catch (SaveException e) {
		 *
		 * e.showError(); } } xml.putProperty("numTables", numTables);
		 */
		xml.putProperty("projection", defaultProjection.getAbrev());

		saveWindowProperties(xml);
		return xml;
	}

	private void saveWindowProperties(XMLEntity xml) {
		XMLEntity propertyList = new XMLEntity();

		propertyList.setName("AndamiPersistence");
		propertyList.putProperty("className", Project.class.getName(), false);

		boolean projectWindowSaved = false;

		IWindow[] windowList = PluginServices.getMDIManager()
				.getOrderedWindows();
		WindowInfo wi;
		XMLEntity windowProperties;
		for (int winIndex = windowList.length - 1; winIndex >= 0; winIndex--) {
			wi = PluginServices.getMDIManager().getWindowInfo(
					windowList[winIndex]);
			if (wi != null && wi.checkPersistence()) {
				if (windowList[winIndex] instanceof Layout) { // for the
					// moment we
					// can't do this
					// for Maps
					// because they
					// don't have a
					// standard
					// model
					Layout layoutWindow = (Layout) windowList[winIndex];
					windowProperties = wi.getXMLEntity();
					windowProperties.putProperty("documentType",
							ProjectMapFactory.registerName, false);
					windowProperties.putProperty("documentName", layoutWindow
							.getName(), false);
					windowProperties.putProperty("zPosition", winIndex, false);
					propertyList.addChild(windowProperties);
				} else if (windowList[winIndex] instanceof ProjectWindow) {
					projectWindowSaved = true;
					windowProperties = wi.getXMLEntity();
					windowProperties
							.putProperty("className",
									"com.iver.cit.gvsig.project.document.gui.ProjectWindow", false);
					windowProperties.putProperty("zPosition", winIndex, false);
					propertyList.addChild(windowProperties);
				} else if (windowList[winIndex] instanceof SingletonWindow) { // for
					// table,
					// view
					// and
					// maybe
					// other
					// documents
					SingletonWindow viewWindow = (SingletonWindow) windowList[winIndex];
					if (viewWindow.getWindowModel() instanceof ProjectDocument) {
						ProjectDocument doc = (ProjectDocument) viewWindow
								.getWindowModel();
						windowProperties = wi.getXMLEntity();
						windowProperties.putProperty("documentType", doc
								.getProjectDocumentFactory().getRegisterName(), false);
						windowProperties.putProperty("documentName",
								((ProjectDocument) viewWindow.getWindowModel())
										.getName(), false);
						windowProperties.putProperty("zPosition", winIndex, false);

						// TODO this will be generalized to all ProjectDocuments as soon as possible
//						if (viewWindow instanceof BaseView) {
//							BaseView win = (BaseView) viewWindow;
//							windowProperties.addChild(win.getWindowData().getXMLEntity());
//						}
						if (viewWindow instanceof IDocumentWindow) {
							IDocumentWindow win = (IDocumentWindow) viewWindow;
							windowProperties.addChild(win.getWindowData().getXMLEntity());
						}

						propertyList.addChild(windowProperties);
					}
				}
			}
		}

		if (projectWindowSaved == false) {
			// If the Project Manager was closed, it was not in the
			// previous window list. Save it now
			ProjectExtension pe = (ProjectExtension) PluginServices
					.getExtension(com.iver.cit.gvsig.ProjectExtension.class);

			if (pe != null) {
				IWindow projectWindow = pe.getProjectWindow();
				wi = PluginServices.getMDIManager()
						.getWindowInfo(projectWindow);
				if (wi != null && wi.checkPersistence()) {
					windowProperties = wi.getXMLEntity();
					if (windowProperties != null) {
						windowProperties
								.putProperty("className",
										"com.iver.cit.gvsig.project.document.gui.ProjectWindow", false);
						propertyList.addChild(windowProperties);
					}
				}
			}
		}

		xml.addChild(propertyList);
	}

	/**
	 * Store the initial window properties, to later restore the window sizes
	 * and positions
	 */
	private void storeInitialWindowProperties(XMLEntity xml) {
		XMLEntity child;
		int childNumb;

		// order the window properties before restoring them, so that we also
		// restore the zPosition
		TreeMap orderedProperties = new TreeMap();
		int maximum = 1;
		for (childNumb = xml.getChildrenCount() - 1; childNumb >= 0; childNumb--) {
			child = xml.getChild(childNumb);
			if (child.contains("zPosition")) {
				orderedProperties.put(new Integer(-child
						.getIntProperty("zPosition")), child); // reverse the
				// order, so
				// that we add
				// the back
				// windows first
			} else {
				orderedProperties.put(new Integer(maximum++), child); // the
				// windows
				// without
				// zPosition
				// will
				// be on
				// the
				// fore
			}
		}

		this.initialWindowProperties = orderedProperties.values().iterator();
	}

	/**
	 * Store the initial window properties, to later restore the window sizes
	 * and positions
	 */
	private void storeInitialWindowProperties061(XMLEntity xml) {
		XMLEntity child;
		int childNumb = 0;
		ArrayList windowList = new ArrayList();

		child = xml.getChild(xml.getChildrenCount() - 1);
		if (child.contains("className")
				&& child.getStringProperty("className").equals(
						"com.iver.cit.gvsig.project.Project")
				&& child.contains("name")
				&& child.getStringProperty("name").equals("ViewInfoProperties")) {
			child.putProperty("className",
					"com.iver.cit.gvsig.project.document.gui.ProjectWindow");
			windowList.add(child);
		}

		// try to open the views
		if (xml.contains("numExtents")) {
			childNumb += xml.getIntProperty("numExtents");
		}
		if (xml.contains("data-source-count")) {
			childNumb += xml.getIntProperty("data-source-count");
		}
		int limit = 0;
		if (xml.contains("numViews")) {
			limit = xml.getIntProperty("numViews")+childNumb;
		}

		XMLEntity view;
		for (int i = childNumb; i < limit; i++) {
			view = xml.getChild(i);
			child = view.getChild(view.getChildrenCount() - 1);
			if (child.contains("className")
					&& child.getStringProperty("className").equals(
							"com.iver.cit.gvsig.project.ProjectView")
					&& child.contains("name")
					&& child.getStringProperty("name").equals(
							"ViewInfoProperties")) {
				child.putProperty("documentName", view
						.getStringProperty("name"));
				child.putProperty("documentType",
						ProjectViewFactory.registerName);
				windowList.add(child);
			}
		}

		if (xml.contains("numViews")) {
			childNumb += xml.getIntProperty("numViews");
		}

		if (xml.contains("numMaps")) {
			limit = childNumb + xml.getIntProperty("numMaps");
		}

		// try to open the maps
		XMLEntity map;
		for (int i = childNumb; i < limit; i++) {
			map = xml.getChild(i);
			for (int j = 0; j < map.getChildrenCount(); j++) {
				child = map.getChild(j);
				if (child.contains("className")
						&& child.getStringProperty("className").equals(
								"com.iver.cit.gvsig.project.ProjectMap")
						&& child.contains("name")
						&& child.getStringProperty("name").equals(
								"ViewInfoProperties")) {
					child.putProperty("documentName", map
							.getStringProperty("name"));
					child.putProperty("documentType",
							ProjectMapFactory.registerName);
					windowList.add(child);
				}
			}
		}

		this.initialWindowProperties = windowList.iterator();
	}

	/**
	 * Restores the size, position and order of the windows, according to
	 * variable initialWindowProperties. If this variable is null, the method
	 * just opens the project manager window.
	 *
	 */
	public void restoreWindowProperties() {
		boolean projectWindowRestored = false;
		XMLEntity child;

		Iterator propertiesIterator = this.initialWindowProperties;
		if (propertiesIterator != null) {
			this.initialWindowProperties = null;

			while (propertiesIterator.hasNext()) {
				child = (XMLEntity) propertiesIterator.next();
				if (child.contains("name") // restore the position of the
						// document windows
						&& child.getStringProperty("name").equals(
								"ViewInfoProperties")
						&& child.contains("documentType")) {
					boolean isClosed = true;
					if (child.contains("isClosed")) {
						isClosed = child.getBooleanProperty("isClosed");
					}
					if (isClosed == false) {
						WindowInfo windowProps = WindowInfo
								.createFromXMLEntity(child);
						String documentName = child
								.getStringProperty("documentName");
						String documentType = child
								.getStringProperty("documentType");
						ProjectDocument pd = this.getProjectDocumentByName(
								documentName, documentType);
						if (pd==null) {
							continue;
						}
						IWindow win = null;
						if (pd instanceof ProjectDocument
								&& child.getChildrenCount()>0
								&& child.getChild(0).getName().equals("windowData")) {
							// this will be generalized to all ProjectDocuments as soon as possible
							WindowData windowData = new WindowData();
							windowData.setXMLEntity(child.getChild(0));
							pd.storeWindowData(windowData);
							win = (pd).createWindow();
						} else {
							win = pd.createWindow();
						}
						if (win == null){
							continue;
						}
						PluginServices.getMDIManager().addWindow(win);
						PluginServices.getMDIManager().changeWindowInfo(win,
								windowProps);
					}
				} else if (child.contains("className") // restore the position
						// of the project
						// manager window
						&& child
								.getStringProperty("className")
								.equals(
										"com.iver.cit.gvsig.project.document.gui.ProjectWindow")
						&& child.contains("name")
						&& child.getStringProperty("name").equals(
								"ViewInfoProperties")) {
					WindowInfo wi = WindowInfo.createFromXMLEntity(child);
					// don't restore size for ProjectManager window, as it's not resizable
					wi.setHeight(-1);
					wi.setWidth(-1);
					ProjectExtension pe = (ProjectExtension) PluginServices
							.getExtension(com.iver.cit.gvsig.ProjectExtension.class);
					if (pe != null) {
						pe.setProject(this);
						pe.showProjectWindow(wi);
					}
					projectWindowRestored = true;
				}
			}
		}

		if (!projectWindowRestored) { // if the project window was not stored
			// in the project, open it now
			ProjectExtension pe = (ProjectExtension) PluginServices
					.getExtension(com.iver.cit.gvsig.ProjectExtension.class);

			if (pe != null) {
				pe.setProject(this);
				pe.showProjectWindow();
			}
		}
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param xml
	 *            DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 *
	 * @throws XMLException
	 * @throws DriverException
	 * @throws DriverIOException
	 * @throws OpenException
	 * @throws VersionException
	 */
	public static Project createFromXML(XMLEntity xml) throws OpenException {

		int childNumber = 0;
		Project p = new Project();

		try {
			p.comments = xml.getStringProperty("comments");
			p.creationDate = xml.getStringProperty("creationDate");
			int numExtents = xml.getIntProperty("numExtents");

			for (int i = 0; i < numExtents; i++) {
				ProjectExtent pe = ProjectExtent.createFromXML(xml.getChild(i));
				p.extents.add(pe);
			}

			// Leemos el ultiom hijo recogido
			childNumber = numExtents;

			// Recogiendo el numero de cameras
			int numCameras = 0;
			if (xml.contains("numCameras")) {
				numCameras = xml.getIntProperty("numCameras");
			}

			// Recogiendo todo las camaras
			for (int i = childNumber; i < (childNumber + numCameras); i++) {
				XMLEntity xmlProp = xml.getChild(i);
				try {
					String className = xmlProp.getStringProperty("className");
					Class classProp = Class.forName(className);
					Object obj = classProp.newInstance();
					IPersistence objPersist = (IPersistence) obj;
					objPersist.setXMLEntity(xmlProp);
					p.cameras.add(obj);
				} catch (Exception e) {
					continue;
				}
			}

			childNumber += numCameras;

//			int numDataSources = xml.getIntProperty("data-source-count");

//			for (int i = childNumber; i < (childNumber + numDataSources); i++) {
//				XMLEntity child = xml.getChild(i);
//				registerDataSourceFromXML(child);
//			}

//			childNumber += numDataSources;
			int numDocuments = 0;
			if (xml.contains("numDocuments")) {
				numDocuments = xml.getIntProperty("numDocuments");
			} else {
				int numViews = xml.getIntProperty("numViews");
				int numMaps = xml.getIntProperty("numMaps");
				int numTables = xml.getIntProperty("numTables");
				numDocuments = numViews + numMaps + numTables;
			}
			int i=0;
			for (i = childNumber; i < (numDocuments + childNumber); i++) {
				try {
					ProjectDocument pD = ProjectDocument.createFromXML(xml
							.getChild(i), p);
					p.addDocument(pD);
					p.sortedDocuments.put(pD, new Integer(i));
				} catch (OpenException e) {
					XMLEntity childXML=xml.getChild(i);
					e.showMessageError(childXML.getName()+"\n  "+childXML.getStringProperty("className"));
				}
			}
			ProjectDocument[] sortDocKeys = p.sortedDocuments
					.keySet().toArray(new ProjectDocument[0]);
			Integer[] sortDocValues = p.sortedDocuments.values()
					.toArray(new Integer[0]);

			i=0;
			for ( i = 0; i < sortDocValues.length; i++) {
				try {
					sortDocKeys[i].setXMLEntity(xml.getChild(sortDocValues[i]
							.intValue()));
				} catch (OpenException e) {
					XMLEntity childXML=xml.getChild(sortDocValues[i].intValue());
					e.showMessageError(childXML.getName()+"\n  "+childXML.getStringProperty("className"));
				}
			}
			childNumber += numDocuments;

			p.modificationDate = xml.getStringProperty("modificationDate");
			//p.modified = xml.getBooleanProperty("modified");
			p.name = xml.getStringProperty("name");
			p.owner = xml.getStringProperty("owner");
			p.selectionColor = StringUtilities.string2Color(xml
					.getStringProperty("selectionColor"));

			p.setLinkTable();
			String strProj = xml.getStringProperty("projection");

			if (strProj != null) {
				p.setProjection(CRSFactory.getCRS(strProj));
			}

			if (childNumber < xml.getChildrenCount()) { // restore the position
				// of the windows
				XMLEntity child = xml.getChild(childNumber);
				if (child.contains("name")
						&& child.getStringProperty("name").equals(
								"AndamiPersistence")) {
					p.storeInitialWindowProperties(child);
				} else if (child.contains("className")
						&& child.getStringProperty("className").equals(
								"com.iver.cit.gvsig.project.Project")
						&& child.contains("name")
						&& child.getStringProperty("name").equals(
								"ViewInfoProperties")) {
					p.storeInitialWindowProperties061(xml);
				}
			}

			PostProcessSupport.executeCalls();
		} catch (Exception e) {
			throw new OpenException(e, p.getClass().getName());
		}

		return p;

	}

	/**
	 * Reestablece los link que ten�a cada tabla con las dem�s.
	 */
	public void setLinkTable() {
		ArrayList tables = getDocumentsByType(FeatureTableDocumentFactory.registerName);

		for (int i = 0; i < tables.size(); i++) {
			for (int j = 0; j < tables.size(); j++) {
				/*
				 * System.out.println("name = " + ((ProjectTable)
				 * tables.get(j)).getModelo().getName());
				 * System.out.println("linktable = " + ((ProjectTable)
				 * tables.get(i)).getLinkTable());
				 */
//				try {
					if ((((FeatureTableDocument) tables.get(i)).getLinkTable() != null)
							&& ((FeatureTableDocument) tables.get(i)).getLinkTable()
							.equals(
									((FeatureTableDocument) tables.get(j))
									.getStore()
									.getName())) {
						LinkSelectionObserver lso;

						lso = new LinkSelectionObserver(((FeatureTableDocument) tables
								.get(i)).getStore(),
								((FeatureTableDocument) tables.get(j)).getStore(),
								((FeatureTableDocument) tables
										.get(i)).getField1(),
										((FeatureTableDocument) tables.get(i)).getField2());

						(((FeatureTableDocument) tables.get(i)).getStore()).addObserver(lso);
						((FeatureTableDocument) tables.get(i)).addLinkSelectionObserver(lso);

					}
//				} catch (ReadException e) {
//					e.printStackTrace();
//				}
			}
		}
	}

	/**
	 * Obtiene la vista que contiene a la capa que se pasa como par�metro
	 *
	 * @param layer
	 *            Capa cuya vista se quiere obtener
	 *
	 * @return
	 *
	 * @throws RuntimeException
	 *             Si la capa que se pasa como par�metro no se encuentra en
	 *             ninguna vista
	 */
	public String getView(FLayer layer) {
		ArrayList views = getDocumentsByType(ProjectViewFactory.registerName);
		for (int v = 0; v < views.size(); v++) {
			ProjectView pView = (ProjectView) views.get(v);
			FLayers layers = pView.getMapContext().getLayers();
			if (isView(layers, layer)) {
				return pView.getName();
			}
		}

		throw new RuntimeException("The layer '"+layer.getName()+"' is not in a view");
	}

	public boolean isView(FLayers layers, FLayer layer) {
		for (int i = 0; i < layers.getLayersCount(); i++) {
			if (layers.getLayer(i) instanceof FLayers) {
				if (isView((FLayers) layers.getLayer(i), layer)){
					return true;
				}
			}
			if (layers.getLayer(i) == layer) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Devuelve la vista cuyo nombre coincide (sensible a mayusculas) con el que
	 * se pasa como par�metro. Devuelve null si no hay ninguna vista con ese
	 * nombre
	 *
	 * @param viewName
	 *            Nombre de la vista que se quiere obtener
	 *
	 * @return DOCUMENT ME!
	 */
	/*
	 * public ProjectView getViewByName(String viewName) { ArrayList
	 * views=getDocuments(PluginServices.getText(this,"Vista")); Object o =
	 * getProjectDocumentByName(viewName, PluginServices.getText(this,"Vista"));
	 *
	 * if (o == null) { return null; }
	 *
	 * return (ProjectView) o; }
	 */
	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public IProjection getProjection() {
		if (projection == null) {
			projection = Project.defaultProjection;
		}
		return projection;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param defaultProjection
	 *            DOCUMENT ME!
	 */
	public void setProjection(IProjection projection) {
		this.projection = projection;
	}

	/**
	 * Sets the projection used when no projection is defined
	 *
	 * @param defaultProjection
	 *            DOCUMENT ME!
	 */
	public static void setDefaultProjection(IProjection defaultProjection) {
		Project.defaultProjection = defaultProjection;
	}

	public static IProjection getDefaultProjection() {
		if (defaultProjection == null){
			XMLEntity xml = PluginServices.getPluginServices("com.iver.cit.gvsig")
			.getPersistentXML();

			// Default Projection
			String projCode = null;
			if (xml.contains("DefaultProjection")) {
				projCode = xml.getStringProperty("DefaultProjection");
				Project.setDefaultProjection(CRSFactory.getCRS(projCode));
			} else {
				Project.setDefaultProjection(defaultFactoryProjection);
			}

		}
		return Project.defaultProjection;
	}

	/**
	 * Obtiene un documento a partir de su nombre y el nombre de registro en el
	 * pointExtension, este �ltimo se puede obtener del
	 * Project****Factory.registerName.
	 *
	 * @param name
	 *            Nombre del documento
	 * @param type
	 *            nombre de registro en el extensionPoint
	 *
	 * @return Documento
	 */
	public ProjectDocument getProjectDocumentByName(String name, String type) {
		ArrayList docs = getDocumentsByType(type);
		for (Iterator iter = docs.iterator(); iter.hasNext();) {
			ProjectDocument elem = (ProjectDocument) iter.next();

			if (elem.getName().equals(name)) {
				return elem;
			}
		}

		return null;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param name
	 *
	 * @return
	 */
	/*
	 * public ProjectTable getTableByName(String name) { ArrayList
	 * tables=getDocuments(PluginServices.getText(this,"Tabla")); Object o =
	 * getProjectElementByName(name, tables);
	 *
	 * if (o == null) { return null; }
	 *
	 * return (ProjectTable) o; }
	 */
	/**
	 * DOCUMENT ME!
	 *
	 * @param name
	 *
	 * @return
	 */
	/*
	 * public ProjectMap getLayoutByName(String name) { Object o =
	 * getProjectElementByName(name, maps);
	 *
	 * if (o == null) { return null; }
	 *
	 * return (ProjectMap) o; }
	 */
//	public SelectableDataSource getDataSourceByLayer(FLayer layer)
//			throws ReadException {
//		ArrayList tables = getDocumentsByType(ProjectTableFactory.registerName);
//		SelectableDataSource dataSource = null;
//		for (int i = 0; i < tables.size(); i++) {
//			ProjectTable pt = (ProjectTable) tables.get(i);
//			if (pt.getOriginal() == ((FLyrVect) layer)
//					.getRecordset()) {
//				dataSource = pt.getModelo().getRecordset();
//				break;
//			} else if (pt.getModelo() == ((FLyrVect) layer)
//					.getRecordset()) {
//					dataSource = pt.getModelo().getRecordset();
//					break;
//			}
//		}
//
//		if (dataSource == null) {
//			// No se ha creado la tabla asociada al tema
//			dataSource = ((FLyrVect) layer).getRecordset();
//		}
//
//		return dataSource;
//
//	}

	/**
	 * Recorremos las capas y las tablas del proyecto, y creamos una lista con
	 * todos los datasources de GDBMS que estamos usando. Luego recorremos los
	 * que est�n registrados, y borramos aquellos que no est�n siendo usados, es
	 * decir, aquellos que no est�n en nuestra lista (un Hash con clave el
	 * nombre del GDBMS)
	 *
	 */
//	private void cleanBadReferences() {
//		ArrayList tables = getDocumentsByType(ProjectTableFactory.registerName);
//		Hashtable usedDataSources = new Hashtable();
//		// Primero las tablas
//		int i, j;
//		try {
//			for (i = 0; i < tables.size(); i++) {
//				ProjectTable t = (ProjectTable) tables.get(i);
//				SelectableDataSource ds;
//				if (t.getModelo() == null){
//					/*
//					 * if a broken table was found
//					 * we don't clean any source
//					 */
//					return;
//				}
//
//				ds = t.getModelo().getRecordset();
//
//				if (t.getOriginal() != null)
//					usedDataSources.put(t.getOriginal().getRecordset()
//							.getName(), t.getOriginal());
//				usedDataSources.put(ds.getName(), ds);
//			}
//		} catch (ReadException e) {
//			e.printStackTrace();
//		}
//		// Ahora las vistas
//		ProjectView pv;
//		ArrayList views = getDocumentsByType(ProjectViewFactory.registerName);
//		try {
//			for (i = 0; i < views.size(); i++) {
//				pv = (ProjectView) views.get(i);
//				this.findLayersVectDataSorces(pv.getMapContext(), usedDataSources);
//
//				MapContext aux = pv.getMapOverViewContext();
//				if (aux != null) {
//					if (!this.findLayersVectDataSorces(aux, usedDataSources)){
//						/*
//						 * if a broken layer was found
//						 * we don't clean any source
//						 */
//						return;
//					}
//
//				}
//
//			} // for i
//
//		} catch (ReadException e) {
//			e.printStackTrace();
//		}
//
//		// Ahora los mapas
//		ArrayList maps = getDocumentsByType(ProjectMapFactory.registerName);
//		ProjectMap pm;
//		LayoutContext lContext;
//		IFFrame[] fframes;
//		try {
//			for (i = 0; i < maps.size(); i++) {
//				pm = (ProjectMap) maps.get(i);
//				fframes = pm.getModel().getLayoutContext().getFFrames();
//				for (j=0;j < fframes.length; j++){
//					if (!(fframes[j] instanceof FFrameView)){
//						continue;
//					}
//					if (!this.findLayersVectDataSorces(((FFrameView)fframes[i]).getMapContext(), usedDataSources)){
//						/*
//						 * if a broken layer was found
//						 * we don't clean any source
//						 */
//						return;
//
//					}
//				} //for j
//
//			} // for i
//
//		} catch (ReadException e) {
//			e.printStackTrace();
//		}
//
//
//		// Recorremos los dataSources y los borramos si no
//		// los estamos usando.
//		SourceInfo[] infos = LayerFactory.getDataSourceFactory()
//				.getDriverInfos();
//		try {
//			for (i = 0; i < infos.length; i++) {
//				if (!usedDataSources.containsKey(infos[i].name)) {
//					DataSource ds;
//					ds = LayerFactory.getDataSourceFactory()
//							.createRandomDataSource(infos[i].name);
//					ds.remove();
//				}
//			}
//		} catch (ReadException e) {
//			e.printStackTrace();
//		} catch (WriteException e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * Find DataSorces of the layers in the mapContext and store them in dataSourcesMap
	 *
	 * @param mapContext
	 * @param dataSourcesMap
	 * @return false if find a no Available layer
	 * @throws ReadDriverException
	 */
//	private boolean findLayersVectDataSorces(MapContext mapContext,Map dataSourcesMap) throws ReadException{
//		LayersIterator iter = new LayersIterator(mapContext.getLayers()){
//
//			//@Override
//			public boolean evaluate(FLayer layer) {
//				if (!(layer instanceof FLyrVect))
//					return false;
//				return super.evaluate(layer);
//			}
//
//		};
//		FLyrVect layer;
//		while (iter.hasNext()){
//			layer = (FLyrVect)iter.nextLayer();
//			if (!layer.isAvailable()){
//				return false;
//			}
//
//			dataSourcesMap.put(layer.getRecordset().getName(), layer.getRecordset());
//			if (layer.isJoined()){
//				dataSourcesMap.put(layer.getSource().getRecordset().getName(),layer.getSource().getRecordset());
//			}
//
//		}
//		return true;
//	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 * @throws SaveException
	 * @throws XMLException
	 * @throws SaveException
	 */
	public XMLEntity getWindowInfoXMLEntity(IWindow window)
			throws SaveException {
		if (window != null
				&& PluginServices.getMDIManager().getWindowInfo(window) != null) {
			WindowInfo vi = PluginServices.getMDIManager()
					.getWindowInfo(window);
			XMLEntity xml = new XMLEntity();
			// xml.putProperty("nameClass", this.getClass().getName());
			try {
				xml.setName("ViewInfoProperties");
				xml.putProperty("className", this.getClass().getName());
				xml.putProperty("X", vi.getX());
				xml.putProperty("Y", vi.getY());
				xml.putProperty("Width", vi.getWidth());
				xml.putProperty("Height", vi.getHeight());
				xml.putProperty("isVisible", vi.isVisible());
				xml.putProperty("isResizable", vi.isResizable());
				xml.putProperty("isMaximizable", vi.isMaximizable());
				xml.putProperty("isModal", vi.isModal());
				xml.putProperty("isModeless", vi.isModeless());
				xml.putProperty("isClosed", vi.isClosed());
				if (vi.isMaximized() == true) {
					xml.putProperty("isMaximized", vi.isMaximized());
					xml.putProperty("normalX", vi.getNormalX());
					xml.putProperty("normalY", vi.getNormalY());
					xml.putProperty("normalWidth", vi.getNormalWidth());
					xml.putProperty("normalHeight", vi.getNormalHeight());
}
			} catch (Exception e) {
				throw new SaveException(e, this.getClass().getName());
			}
			return xml;
		}
		return null;
	}

	public static WindowInfo createWindowInfoFromXMLEntity(XMLEntity xml) {
		WindowInfo result = new WindowInfo();
		result.setX(xml.getIntProperty("X"));
		result.setY(xml.getIntProperty("Y"));
		result.setHeight(xml.getIntProperty("Height"));
		result.setWidth(xml.getIntProperty("Width"));
		result.setClosed(xml.getBooleanProperty("isClosed"));
		if (xml.contains("isMaximized")) {
			boolean maximized = xml.getBooleanProperty("isMaximized");
			result.setMaximized(maximized);
			if (maximized == true) {
				result.setNormalBounds(xml.getIntProperty("normalX"), xml
						.getIntProperty("normalY"), xml
						.getIntProperty("normalWidth"), xml
						.getIntProperty("normalHeight"));
			} else {
				result.setNormalBounds(result.getBounds());
			}
		}

		return result;
	}

//	public XMLEntity getSourceInfoXMLEntity(SourceInfo di) {
//		XMLEntity child = new XMLEntity();
//
//		if (di instanceof ObjectSourceInfo) {
//			ObjectSourceInfo driver = (ObjectSourceInfo) di;
//			child.putProperty("type", "sameDriverFile");
//			child.putProperty("gdbmsname", driver.name);
//		} else if (di instanceof FileSourceInfo) {
//			FileSourceInfo vfdi = (FileSourceInfo) di;
//			child.putProperty("type", "otherDriverFile");
//			child.putProperty("gdbmsname", vfdi.name);
//			child.putProperty("file", vfdi.file);
//			child.putProperty("driverName", vfdi.driverName);
//		} else if (di instanceof DBSourceInfo) {
//			DBTableSourceInfo dbdi = (DBTableSourceInfo) di;
//			child.putProperty("type", "db");
//			child.putProperty("gdbmsname", dbdi.name);
//			child.putProperty("dbms", dbdi.dbms);
//			child.putProperty("host", dbdi.host);
//			child.putProperty("port", dbdi.port);
//			child.putProperty("user", dbdi.user);
//			child.putProperty("password", dbdi.password);
//			child.putProperty("dbName", dbdi.dbName);
//			child.putProperty("tableName", dbdi.tableName);
//			child.putProperty("driverInfo", dbdi.driverName);
//		}
//
//		return child;
//	}
//
	/**
	 * Devuelve un arrayList con todos los documentos del tipo especificado como
	 * par�metro.
	 *
	 * @param registerName
	 *            nombre de registro en el extensionPoint
	 *
	 * @return Documentos del tipo especificado
	 */
	public ArrayList<ProjectDocument> getDocumentsByType(String registerName) {
		ArrayList<ProjectDocument> docuArray = new ArrayList<ProjectDocument>();
		for (int i = 0; i < documents.size(); i++) {
			ProjectDocument projectDocument = documents
					.get(i);
			ProjectDocumentFactory pdf = projectDocument
					.getProjectDocumentFactory();
			if (pdf == null) {
				continue;
			}
			if (pdf.getRegisterName().equals(registerName)) {
				docuArray.add(projectDocument);
			}
		}
		return docuArray;
	}

	/**
	 * Devuelve un arrayList con todos los documentos.
	 *
	 * @return Documentos
	 */
	public ArrayList<ProjectDocument> getDocuments() {
		ArrayList<ProjectDocument> docuArray = new ArrayList<ProjectDocument>();
		for (int i = 0; i < documents.size(); i++) {
			ProjectDocument projectDocument = documents
					.get(i);
			docuArray.add(projectDocument);
		}
		return docuArray;
	}

	/**
	 * Inserta un documento.
	 *
	 * @param doc
	 *            Documento
	 */
	public void addDocument(ProjectDocument doc) {
		documents.add(doc);
		doc.addPropertyChangeListener(this);
		//modified = true;
		change.firePropertyChange("", "", null);
		doc.setProject(this, 0);
		doc.afterAdd();

	}

	/**
	 * Borra un documento.
	 *
	 * @param doc
	 *            Documento
	 */
	public void delDocument(ProjectDocument doc) {
		documents.remove(doc);
		//modified = true;
		change.firePropertyChange("", null, null);
		doc.afterRemove();
	}

	/**
	 * Sets the default selection color that will be used in subsequent
	 * projects.
	 *
	 * @param color
	 */
	public static void setDefaultSelectionColor(Color color) {
		defaultSelectionColor = color;
	}

	/**
	 * Returns the current default selection color defined which is the color
	 * defined when the user does not define any other one
	 *
	 * @return java.awt.Color
	 */
	public static Color getDefaultSelectionColor() {
		// TODO es millorable?
		XMLEntity xml = PluginServices.getPluginServices("com.iver.cit.gvsig")
				.getPersistentXML();
		if (xml.contains("DefaultSelectionColor")) {
			defaultSelectionColor = StringUtilities.string2Color(xml
					.getStringProperty("DefaultSelectionColor"));
		}
		return defaultSelectionColor;
	}

	/**
	 * Returns the user's default map units. This is the cartography data
	 * distance units.
	 *
	 * @return int (index of the <b>Attributes.NAMES array</b>)
	 */
	public static int getDefaultMapUnits() {
		if (defaultMapUnits == -1) {
			XMLEntity xml = PluginServices.getPluginServices(
					"com.iver.cit.gvsig").getPersistentXML();
			if (xml.contains("DefaultMapUnits")) {
				defaultMapUnits = xml.getIntProperty("DefaultMapUnits");
			} else {
				// first app run case
				String[] unitNames = MapContext.getDistanceNames();
				for (int i = 0; i < unitNames.length; i++) {
					// meter is the factory default's map unit
					if (unitNames[i].equals("Metros")) {
						defaultMapUnits = i;
						break;
					}
				}
			}
			if (defaultMapUnits == -1) {
				defaultMapUnits = MapContext.getDistancePosition("Metros");
			}
		}
		return defaultMapUnits;
	}

	/**
	 * Returns the user's default view units for measuring distances. This is
	 * the units that the user will see in the status bar of the view.
	 *
	 * @return int (index of the <b>Attributes.NAMES array</b>)
	 */
	public static int getDefaultDistanceUnits() {
		if (defaultDistanceUnits == -1) {
			XMLEntity xml = PluginServices.getPluginServices(
					"com.iver.cit.gvsig").getPersistentXML();
			if (xml.contains("DefaultDistanceUnits")) {
				defaultDistanceUnits = xml
						.getIntProperty("DefaultDistanceUnits");
			} else {
				// first app run case
				String[] unitNames = MapContext.getDistanceNames();
				for (int i = 0; i < unitNames.length; i++) {
					// meter is the factory default's distance unit
					if (unitNames[i].equals("Metros")) {
						defaultDistanceUnits = i;
						break;
					}
				}
			}
			if (defaultDistanceUnits == -1) {
				defaultDistanceUnits = MapContext.getDistancePosition("Metros");
			}
		}
		return defaultDistanceUnits;
	}
	/**
	 * Returns the user's default view units for measuring areas. This is
	 * the units that the user will see in the status bar of the view.
	 *
	 * @return int (index of the <b>Attributes.NAMES array</b>)
	 */
	public static int getDefaultDistanceArea() {
		if (defaultDistanceArea == -1) {
			XMLEntity xml = PluginServices.getPluginServices(
					"com.iver.cit.gvsig").getPersistentXML();
			if (xml.contains("DefaultDistanceArea")) {
				defaultDistanceArea = xml
						.getIntProperty("DefaultDistanceArea");
			} else {
				// first app run case
				String[] unitNames = MapContext.getAreaNames();
				for (int i = 0; i < unitNames.length; i++) {
					// meter is the factory default's distance unit
					if (unitNames[i].equals("Metros")) {
						defaultDistanceArea = i;
						break;
					}
				}
			}
			if (defaultDistanceArea == -1){
				defaultDistanceArea=getDefaultDistanceUnits();
			}
		}
		return defaultDistanceArea;
	}
	/**
	 * Sets the default map unit (the units used by the data).
	 *
	 * @param mapUnits
	 */
	public static void setDefaultMapUnits(int mapUnits) {
		defaultMapUnits = mapUnits;
	}

	/**
	 * Sets the default distance units (the units shown in the status bar)
	 *
	 * @param distanceUnits
	 */
	public static void setDefaultDistanceUnits(int distanceUnits) {
		defaultDistanceUnits = distanceUnits;
	}
	/**
	 * Sets the default distance area (the units shown in the status bar)
	 *
	 * @param distanceUnits
	 */
	public static void setDefaultDistanceArea(int distanceArea) {
		defaultDistanceArea = distanceArea;
	}
	public String exportToXML() throws SaveException {
		XMLEntity xml = this.newExportXMLRootNode();

		Iterator iter = this.documents.iterator();
		ProjectDocument document;
		// FIXME: Falta atrapar los errores
		while (iter.hasNext()) {
			document = (ProjectDocument) iter.next();
			document.exportToXML(xml, this);
		}

		return xml.toString();
	}

	public String exportToXML(ProjectDocument[] documents) throws SaveException {
		XMLEntity xml = this.newExportXMLRootNode();

		for (int i = 0; i < documents.length; i++) {
			documents[i].exportToXML(xml, this);
		}

		return xml.toString();
	}

	public void importFromXML(String xml) throws Exception {
		throw new Exception("Not Implemented");
		/*
		 * // FIXME: ?? Exceptions XMLEntity xmlEntity = new XMLEntity();
		 *
		 * try { xmlEntity.parse(xml); } catch (Exception e) { throw new
		 * Exception(e); }
		 *
		 * if (!checkExportXMLRootNode(xmlEntity)) { throw new Exception("Check
		 * Error"); //FIXME: traducir }
		 *
		 * int i;
		 *
		 * XMLEntity xmlDocumentRoot; ProjectDocument document = null;
		 * ProjectDocumentFactory documentFactory = null; for (i=0;i<xmlEntity.getChildrenCount();i++) {
		 * xmlDocumentRoot = xmlEntity.getChild(i); if
		 * (!xmlDocumentRoot.contains("type")) { throw new Exception("Document
		 * root "+i+ "error"); } documentFactory =
		 * Project.getProjectDocumentFactory(xmlDocumentRoot.getStringProperty("type"));
		 * int j; }
		 */

	}

	public void importFromXML(String xml, String type) throws Exception {
		// FIXME: EXCEPTIONS!!!!
		XMLEntity xmlEntity = XMLEntity.parse(xml);

		if (!checkExportXMLRootNode(xmlEntity)) {
			throw new Exception();
		}

		XMLEntity typeRoot = xmlEntity.firstChild("type", type);
		if (typeRoot.getChildrenCount() == 0) {
			return;
		}

		ProjectDocumentFactory documentFactory = getProjectDocumentFactory(type);

		Hashtable conflicts = getConflicts(xmlEntity);

		if (conflicts.size() != 0) {
			if (!documentFactory.resolveImportXMLConflicts(xmlEntity, this,
					conflicts)) {
				return;
			}
		}
//		this.registerDataSources(this.getExportXMLTypeRootNode(xmlEntity,
//				"dataSources"));

		int i;
		ProjectDocument document;
		for (i = 0; i < typeRoot.getChildrenCount(); i++) {
			document = documentFactory.create(this);
			document.importFromXML(xmlEntity, typeRoot, i, this);
		}

	}

	private Hashtable getConflicts(XMLEntity xml) {
		int iType;
		Hashtable conflicts = new Hashtable();
		ArrayList elements;
		XMLEntity typeRoot;
		for (iType = 0; iType < xml.getChildrenCount(); iType++) {
			typeRoot = xml.getChild(iType);
			elements = getDocumentsByType(typeRoot.getStringProperty("type"));
			Hashtable conflictsType = new Hashtable();
			for (int iXML = 0; iXML < typeRoot.getChildrenCount(); iXML++) {
				XMLEntity child = typeRoot.getChild(iXML);
				Iterator iter = elements.iterator();
				while (iter.hasNext()) {
					ProjectDocument element = (ProjectDocument) iter.next();
					if (element.getName().equalsIgnoreCase(
							child.getStringProperty("name"))) {
						conflictsType.put(new Integer(iXML), child);
						break;
					}

				}
			}
			if (conflictsType.size() > 0) {
				conflicts
						.put(typeRoot.getStringProperty("type"), conflictsType);
			}
		}
		return conflicts;
	}

	public boolean isValidXMLForImport(String xml) {
		XMLEntity xmlEntity;
		try {
			xmlEntity = XMLEntity.parse(xml);
		} catch (Exception e) {
			return false;
		}

		return checkExportXMLRootNode(xmlEntity);
	}

	public boolean isValidXMLForImport(String xml, String type) {
		XMLEntity xmlEntity;
		try {
			xmlEntity = XMLEntity.parse(xml);
		} catch (Exception e) {
			return false;
		}

		if (!checkExportXMLRootNode(xmlEntity)) {
			return false;
		}

		XMLEntity typeRoot = xmlEntity.firstChild("type", type);

		if (typeRoot == null) {
			return false;
		}

		return (typeRoot.getChildrenCount() > 0);
	}

	private boolean checkExportXMLRootNode(XMLEntity xml) {
		if (!xml.contains("applicationName")) {
			return false;
		}
		if (!xml.getStringProperty("applicationName").equalsIgnoreCase("gvSIG")) {
			return false;
		}

		if (!xml.contains("version")) {
			return false;
		}
		if (!xml.getStringProperty("version")
				.equalsIgnoreCase(Version.format())) {
			return false;
		}

		return true;
	}

//	public void exportToXMLDataSource(XMLEntity root, String dataSourceName) {
//		XMLEntity dsRoot = this.getExportXMLTypeRootNode(root, "dataSources");
//		SourceInfo sourceInfo = LayerFactory.getDataSourceFactory()
//				.getDriverInfo(dataSourceName);
//		dsRoot.addChild(this.getSourceInfoXMLEntity(sourceInfo));
//	}

	private XMLEntity newExportXMLRootNode() {
		XMLEntity xml = new XMLEntity();
		xml.putProperty("applicationName", "gvSIG");
		xml.putProperty("version", Version.format());
		return xml;
	}

	public XMLEntity getExportXMLTypeRootNode(XMLEntity root, String type) {
		XMLEntity typeRoot = root.firstChild("type", type);
		if (typeRoot == null) {
			typeRoot = this.newExportXMLTypeNode(type);
			root.addChild(typeRoot);
		}
		return typeRoot;
	}

	private XMLEntity newExportXMLTypeNode(String type) {
		XMLEntity xmlDataSources = new XMLEntity();
		xmlDataSources.putProperty("type", type);
		return xmlDataSources;
	}

//	private static boolean registerDataSourceFromXML(XMLEntity xmlDataSource) {
//		String name = xmlDataSource.getStringProperty("gdbmsname");
//
//		if (LayerFactory.getDataSourceFactory().getDriverInfo(name) == null) {
//			if (xmlDataSource.getStringProperty("type").equals(
//					"otherDriverFile")) {
//				LayerFactory.getDataSourceFactory().addFileDataSource(
//						xmlDataSource.getStringProperty("driverName"), name,
//						xmlDataSource.getStringProperty("file"));
//
//			} else if (xmlDataSource.getStringProperty("type").equals(
//					"sameDriverFile")) {
//
//			} else if (xmlDataSource.getStringProperty("type").equals("db")) {
//				LayerFactory.getDataSourceFactory().addDBDataSourceByTable(
//						name, xmlDataSource.getStringProperty("host"),
//						xmlDataSource.getIntProperty("port"),
//						xmlDataSource.getStringProperty("user"),
//						xmlDataSource.getStringProperty("password"),
//						xmlDataSource.getStringProperty("dbName"),
//						xmlDataSource.getStringProperty("tableName"),
//						xmlDataSource.getStringProperty("driverInfo"));
//			} else {
//				return false;
//			}
//
//		}
//		return true;
//	}

//	private boolean registerDataSources(XMLEntity xmlDataSources) {
//		try {
//			int numDataSources = xmlDataSources.getChildrenCount();
//
//			if (numDataSources == 0)
//				return true;
//			DataSourceFactory dsFactory = LayerFactory.getDataSourceFactory();
//
//			for (int i = 0; i < numDataSources; i++) {
//				XMLEntity child = xmlDataSources.getChild(i);
//				if (!this.registerDataSourceFromXML(child)) {
//					return false;
//				}
//			}
//
//			return true;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//	}

	public static ProjectDocumentFactory getProjectDocumentFactory(String type) {
		ProjectDocumentFactory pde = null;
		try {
			ExtensionPointManager epManager = ToolsLocator.getExtensionPointManager();
			try {
				pde = (ProjectDocumentFactory) epManager.get("Documents").create(type);
			} catch (InstantiationException e) {
				NotificationManager.addError(
						"Clase de ProjectDocument no reconocida", e);
			} catch (IllegalAccessException e) {
				NotificationManager.addError(
						"Clase de ProjectDocument no reconocida", e);
			}

		} catch (Exception e1) {
			return null;
		}
		return pde;
	}

	 public boolean hasChanged() {
		 // we return true if the project is not empty (until we have a better method...)
		 if ((this.getDocuments().size() != 0) || modified) {
			 return true;
		 }
		 return false;
//		 return signatureAtStartup != getXMLEntity().hash();
	 }

	public void setSignature(long hash) {
		signatureAtStartup = hash;
	}
}
