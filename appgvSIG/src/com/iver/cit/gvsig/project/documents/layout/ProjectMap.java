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
package com.iver.cit.gvsig.project.documents.layout;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.ProjectDocument;
import com.iver.cit.gvsig.project.documents.ProjectDocumentFactory;
import com.iver.cit.gvsig.project.documents.exceptions.OpenException;
import com.iver.cit.gvsig.project.documents.exceptions.SaveException;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameView;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;
import com.iver.cit.gvsig.project.documents.layout.gui.MapProperties;
import com.iver.cit.gvsig.project.documents.view.ProjectView;
import com.iver.cit.gvsig.project.documents.view.ProjectViewFactory;
import com.iver.utiles.XMLEntity;
import com.iver.utiles.XMLException;


/**
 * Modelo del Layout.
 *
 * @author Fernando González Cortés
 */
public class ProjectMap extends ProjectDocument {
	//public static int numMaps = 0;
	private Layout model;

	/**
	 * @see com.iver.cit.gvsig.project.documents.layout.ProjectMap#getModel()
	 */
	public Layout getModel() {
		return model;
	}

	/**
	 * @see com.iver.cit.gvsig.project.documents.layout.ProjectMap#setMapContext(com.iver.cit.gvsig.project.castor.XMLEntity)
	 */
	public void setModel(Layout f) {
		model = f;
		f.setName(getName());
	}

	/**
	 * @see com.iver.cit.gvsig.project.documents.ProjectDocument#setName(java.lang.String)
	 */
	public void setName(String string) {
		super.setName(string);

		Layout m = getModel();

		if (m != null) {
			m.setName(string);
		}
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 * @throws SaveException
	 * @throws XMLException
	 *
	 * @throws ReadDriverException
	 */
	public XMLEntity getXMLEntity() throws SaveException   {
		XMLEntity xml = super.getXMLEntity();
		try{
		//xml.putProperty("nameClass", this.getClass().getName());
		int numMaps=(ProjectDocument.NUMS.get(ProjectMapFactory.registerName)).intValue();
		xml.putProperty("numMaps", numMaps);
		xml.addChild(model.getXMLEntity());
		}catch (Exception e) {
			throw new SaveException(e,this.getClass().getName());
		}
		return xml;
	}

	/**
	 * @throws OpenException
	 * @see com.iver.cit.gvsig.project.documents.ProjectDocument#setXMLEntity(com.iver.utiles.XMLEntity)
	 */
	public void setXMLEntity(XMLEntity xml) throws OpenException {
		try {
			super.setXMLEntity(xml);
			int numMaps=xml.getIntProperty("numMaps");
			ProjectDocument.NUMS.put(ProjectMapFactory.registerName,new Integer(numMaps));
			for (int i=0; i<xml.getChildrenCount(); i++)
			{
				XMLEntity child = xml.getChild(i);
				if (child.contains("className")
						&& (child.getStringProperty("className").equals("com.iver.cit.gvsig.gui.layout.Layout") || child.getStringProperty("className").equals(Layout.class.getName()))
						&& child.contains("name")
						&& child.getStringProperty("name").equals("layout")) {
					setModel(Layout.createLayout(child,getProject()));
				}
			}
			this.model.setProjectMap(this);
		} catch (Exception e) {
			throw new OpenException(e,this.getClass().getName());
		}
	}

	public IWindow createWindow() {
		Layout l = getModel();
        setName(l.getName());
        l.setProjectMap(this);
		l.getLayoutControl().fullRect();
        l.getWindowInfo().setTitle(PluginServices.getText(this,
        "Mapa") + " : " +l.getName());
        callCreateWindow(l);
		return l;
	}
	public IWindow getProperties() {
		return new MapProperties(this);
	}

	public void afterRemove() {
		// TODO Auto-generated method stub

	}

	public void afterAdd() {
		// TODO Auto-generated method stub

	}

	public void exportToXML(XMLEntity root, Project project) throws SaveException {
		XMLEntity mapsRoot = project.getExportXMLTypeRootNode(root,ProjectMapFactory.registerName);
		mapsRoot.addChild(this.getXMLEntity());
		this.exportToXMLDependencies(root,project);
	}

	private void exportToXMLDependencies( XMLEntity root,Project project)
		throws SaveException {
		XMLEntity viewsRoot = project.getExportXMLTypeRootNode(root,ProjectViewFactory.registerName);
		IFFrame[] components = this.getModel().getLayoutContext().getFFrames();
		for (int i=0; i < components.length; i++) {
			if (components[i] instanceof FFrameView) {
				ProjectView view = ((FFrameView)components[i]).getView();
				XMLEntity viewXML = viewsRoot.firstChild("name",view.getName());
				if (viewXML==null) {
					view.exportToXML(root,project);
				}
			}
		}

	}

	public void importFromXML(XMLEntity root, XMLEntity typeRoot, int elementIndex, Project project, boolean removeDocumentsFromRoot) throws XMLException {
		XMLEntity element = typeRoot.getChild(elementIndex);

		XMLEntity layout = element.getChild(0);
		//Cargamos las vistas vinculadas:

		//Recuperamos todos los nombres
		int childIndex;
		XMLEntity child;
		// Lo hacemos en un map por si una vista se usa varias veces
		HashMap viewsName = new HashMap();
		for (childIndex=0;childIndex<layout.getChildrenCount();childIndex++) {
			child = layout.getChild(childIndex);
			if (child.contains("viewName")) {
				viewsName.put(child.getStringProperty("viewName"),child.getStringProperty("viewName"));
			}

		}


		XMLEntity viewsRoot = project.getExportXMLTypeRootNode(root,ProjectViewFactory.registerName);
		XMLEntity viewXML;

		// Construimos un diccionario ordenado inversamente por el indice
		// del elemento (por si se van eliminando elementos al importar) y
		// como valor el nombre de la vista
		TreeMap viewsToImport = new TreeMap( new Comparator() {

			public int compare(Object o1, Object o2) {

				if (((Integer)o1).intValue() > ((Integer)o2).intValue()) {
					return -1; //o1 first
				} else if (((Integer)o1).intValue() < ((Integer)o2).intValue()){
					return 1; //o1 second
				}
				return 0;
			}

		});
		Iterator iterViewsName = viewsName.keySet().iterator();
		int viewIndex;
		String viewName;
		while (iterViewsName.hasNext()) {
			viewName = (String)iterViewsName.next();
			viewIndex = viewsRoot.firstIndexOfChild("name",viewName);
			viewsToImport.put(new Integer(viewIndex),viewName);
		}


		ProjectView view;
		ProjectDocumentFactory viewFactory = project.getProjectDocumentFactory(ProjectViewFactory.registerName);

		Iterator iterViewToImport = viewsToImport.entrySet().iterator();
		Entry entry;
		// Nos recorremos las vistas a importar
		while (iterViewToImport.hasNext()) {
			entry = (Entry)iterViewToImport.next();
			viewName = (String)entry.getValue();
			viewIndex = ((Integer)entry.getKey()).intValue();
			// Si ya existe la vista no la importamos
			view = (ProjectView)project.getProjectDocumentByName(viewName,ProjectViewFactory.registerName);
			if (view == null) {
				view = (ProjectView)viewFactory.create(project);
//				try {
					view.importFromXML(root,viewsRoot,viewIndex,project,removeDocumentsFromRoot);
//				} catch (ReadException e) {
//					throw new OpenException(e,getName());
//				}
			}

		}


		this.setXMLEntity(element);
		project.addDocument(this);
		if (removeDocumentsFromRoot) {
			typeRoot.removeChild(elementIndex);
		}


	}

//	public int computeSignature() {
//		int result = 17;
//
//		Class clazz = getClass();
//		Field[] fields = clazz.getDeclaredFields();
//		for (int i = 0; i < fields.length; i++) {
//			try {
//				String type = fields[i].getType().getName();
//				if (type.equals("boolean")) {
//					result += 37 + ((fields[i].getBoolean(this)) ? 1 : 0);
//				} else if (type.equals("java.lang.String")) {
//					Object v = fields[i].get(this);
//					if (v == null) {
//						result += 37;
//						continue;
//					}
//					char[] chars = ((String) v).toCharArray();
//					for (int j = 0; j < chars.length; j++) {
//						result += 37 + (int) chars[i];
//					}
//				} else if (type.equals("byte")) {
//					result += 37 + (int) fields[i].getByte(this);
//				} else if (type.equals("char")) {
//					result += 37 + (int) fields[i].getChar(this);
//				} else if (type.equals("short")) {
//					result += 37 + (int) fields[i].getShort(this);
//				} else if (type.equals("int")) {
//					result += 37 + fields[i].getInt(this);
//				} else if (type.equals("long")) {
//					long f = fields[i].getLong(this) ;
//					result += 37 + (f ^ (f >>> 32));
//				} else if (type.equals("float")) {
//					result += 37 + Float.floatToIntBits(fields[i].getFloat(this));
//				} else if (type.equals("double")) {
//					long f = Double.doubleToLongBits(fields[i].getDouble(this));
//					result += 37 + (f ^ (f >>> 32));
//				} else {
//					Object obj = fields[i].get(this);
//					result += 37 + ((obj != null)? obj.hashCode() : 0);
//				}
//			} catch (Exception e) { e.printStackTrace(); }
//
//		}
//		return result;
//	}
}
