package com.iver.cit.gvsig.project.documents.view.toc.actions;

import java.util.ArrayList;

import org.gvsig.fmap.mapcontext.layers.CancelationException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.project.document.table.FeatureTableDocument;
import org.gvsig.project.document.table.FeatureTableDocumentFactory;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.Version;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.ProjectDocument;
import com.iver.cit.gvsig.project.documents.exceptions.OpenException;
import com.iver.utiles.XMLEntity;
import com.iver.utiles.XMLException;

//TODO comentado para que compile
public class CopyPasteLayersUtiles {
	private static CopyPasteLayersUtiles theInstance = null;

	public static CopyPasteLayersUtiles getInstance() {
		if (theInstance == null) {
			theInstance = new CopyPasteLayersUtiles();
		}
		return theInstance;
	}

	public XMLEntity generateXMLCopyLayers(FLayer[] actives) {

		XMLEntity xml = this.newRootNode();
		XMLEntity xmlLayers = this.newLayersNode();
		XMLEntity xmlTables = this.newTablesNode();
		XMLEntity xmlDataSources = this.newDataSourcesNode();

		for (int i=0;i < actives.length; i++) {
			if (!this.addToXMLLayer(actives[i],xmlLayers ,xmlTables,xmlDataSources)) {
				return null;
			}

		}

		if (xmlDataSources.getChildrenCount() > 0) {
			xml.addChild(xmlDataSources);
		}
		if (xmlLayers.getChildrenCount() > 0) {
			xml.addChild(xmlLayers);
		}
		if (xmlTables.getChildrenCount() > 0) {
			xml.addChild(xmlTables);
		}

		return xml;

	}



	/*
	 *
	 *
	 *
	 * Funciones Publicas de carga de un XML (pegar)
	 *
	 *
	 *
	*/

	public boolean loadLayersFromXML(XMLEntity xml, FLayers root) {
		XMLEntity xmlLayers = xml.firstChild("type","layers");
		XMLEntity xmlTables = xml.firstChild("type","tables");
		XMLEntity xmlDataSources = xml.firstChild("type","dataSources");

		if (xmlLayers == null ) {
			return false;
		}

		// Se pegan las tablas igualmente
		/*
		Project project = this.getProject();

		Hashtable tablesConfits = this.getConflicts(xmlTables,project.getTables());
		*/


		if (xmlDataSources != null)  {
			if (!this.registerDataSources(xmlDataSources)) {
				return false;
			}
		}

		if (!this.addLayers(xmlLayers,root)) {
			return false;
		}

		if (xmlTables != null)  {
			if (!this.addTables(xmlTables)) {
				return false;
			}
		}

		return true;

	}

	public boolean loadTablesFromXML(XMLEntity xml) {
		XMLEntity xmlTables = xml.firstChild("type","tables");
		XMLEntity xmlDataSources = xml.firstChild("type","dataSources");


		if (xmlTables == null ) {
			return false;
		}

		/*
		Project project = this.getProject();

		Hashtable tablesConfits = this.getConflicts(xmlTables,project.getTables());
		*/

		if (xmlDataSources != null)  {
			if (!this.registerDataSources(xmlDataSources)) {
				return false;
			}
		}



		return this.addTables(xmlTables);
	}


	public boolean checkXMLRootNode(XMLEntity xml) {
		if (!xml.contains("applicationName")) {
			return false;
		}
		if (!xml.getStringProperty("applicationName").equalsIgnoreCase("gvSIG")) {
			return false;
		}

		if (!xml.contains("version")) {
			return false;
		}
		if (!xml.getStringProperty("version").equalsIgnoreCase(Version.format())) {
			return false;
		}

		return true;
	}

	private void fillXMLRootNode(XMLEntity xml) {
		xml.putProperty("applicationName","gvSIG");
		xml.putProperty("version",Version.format());
	}

	private boolean registerDataSources(XMLEntity xmlDataSources) {
//		try {
//			int numDataSources = xmlDataSources.getChildrenCount();
//
//			if (numDataSources == 0) return true;
//			DataSourceFactory dsFactory = LayerFactory.getDataSourceFactory();
//
//			for (int i = 0; i < numDataSources; i++) {
//				XMLEntity child = xmlDataSources.getChild(i);
//				String name = child.getStringProperty("gdbmsname");
//
//				if (dsFactory.getDriverInfo(name) == null) {
//					if (child.getStringProperty("type").equals("otherDriverFile")) {
//						LayerFactory.getDataSourceFactory().addFileDataSource(
//								child.getStringProperty("driverName"),
//								name,
//								child.getStringProperty("file")
//						);
//
//
//					} else if (child.getStringProperty("type").equals("sameDriverFile")) {
//						/*                                String layerName = child.getStringProperty("layerName");
//						 ProjectView vista = project.getViewByName(child.getStringProperty(
//						 "viewName"));
//						 FLayer layer = vista.getMapContext().getLayers().getLayer(layerName);
//
//						 modelo = ((AlphanumericData) layer).getRecordset();
//						 associatedTable = (AlphanumericData) layer;
//						 */
//					} else if (child.getStringProperty("type").equals("db")) {
//						LayerFactory.getDataSourceFactory().addDBDataSourceByTable(
//								name,
//								child.getStringProperty("host"),
//								child.getIntProperty("port"),
//								child.getStringProperty("user"),
//								child.getStringProperty("password"),
//								child.getStringProperty("dbName"),
//								child.getStringProperty("tableName"),
//								child.getStringProperty("driverInfo")
//						);
//					}
//				}
//			}
//
//			return true;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
		return false;
	}

	private boolean addTables(XMLEntity xmlTables) {
		try {
			int numTables = xmlTables.getChildrenCount();
			if (numTables == 0) {
				return true;
			}

			Project project = this.getProject();

			for (int i = 0; i < numTables; i++) {
				try{
					FeatureTableDocument ptable = (FeatureTableDocument) FeatureTableDocument.createFromXML(xmlTables.getChild(i), project);
					project.addDocument(ptable);
					/*
					if (ptable.getSeedViewInfo()!=null && ptable.getAndamiView()!=null) { // open the view, if it was open, and restore its dimensions
						PluginServices.getMDIManager().addView(ptable.getAndamiView());
						PluginServices.getMDIManager().changeViewInfo(ptable.getAndamiView(), ptable.getSeedViewInfo());
					}
					*/
				}catch(OpenException e){
					e.printStackTrace();
					return false;
				}
			}

			project.setLinkTable();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private boolean addLayers(XMLEntity xmlLayers,FLayers root) {
		try {
			XMLEntity child;
			int numLayers = xmlLayers.getChildrenCount();
			for (int i = 0; i < numLayers; i++) {
				child = xmlLayers.getChild(i);
//				if (!root.addLayerFromXMLEntity(child,null)) return false;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}



	private Project getProject() {
		 return ((ProjectExtension)PluginServices.getExtension(ProjectExtension.class)).getProject();
	}

//	private boolean addToXMLDataSource(SourceInfo source,XMLEntity xmlDataSources, Project project) {
//		if (project== null) {
//			project = this.getProject();
//		}
//  	  	xmlDataSources.addChild(project.getSourceInfoXMLEntity(source));
//
//  	  	return true;
//	}

	private boolean addToXMLTable(FeatureTableDocument pt,XMLEntity xmlTables,XMLEntity xmlDataSources,Project project) {
//		if (project== null) {
//			project = this.getProject();
//		}
//		if (xmlTables.findChildren("name",pt.getName()) != null) return true;
//		XMLEntity xmlTable = null;
//		try {
//			xmlTable = pt.getXMLEntity();
//
//			xmlTables.addChild(xmlTable);
//
//			if (pt.getAssociatedTable() != null) {
//				this.addToXMLDataSource(pt.getAssociatedTable().getRecordset().getSourceInfo(),xmlDataSources,project);
//			}
//
//			if (pt.getLinkTable() != null) {
//				if (xmlTables.findChildren("name",pt.getLinkTable()) == null)  {
//					ProjectTable ptLink = (ProjectTable)project.getProjectDocumentByName(pt.getLinkTable(),ProjectTableFactory.registerName);
//					if (!this.addToXMLTable(ptLink,xmlTables,xmlDataSources,project)) return false;
//				}
//			}
//		} catch (SaveException e) {
//			e.printStackTrace();
//			return false;
//		} catch (ReadException e) {
//			e.printStackTrace();
//			return false;
//		}

  	  	return true;
	}

	private boolean addToXMLLayerDependencies(FLayer lyr,XMLEntity xmlTables,XMLEntity xmlDataSources) {
//		try {
//			Project project = this.getProject();
//
//			if (lyr instanceof FLayers) {
//				FLayers lyrs = (FLayers)lyr;
//				int count = lyrs.getLayersCount();
//				for (int i=0;i < count;i++) {
//					FLayer subLyr = lyrs.getLayer(i);
//					this.addToXMLLayerDependencies(subLyr,xmlTables,xmlDataSources);
//				}
//
//		    } else if (lyr instanceof FLyrVect){
//            	if (!this.addToXMLDataSource(
//        			((FLyrVect)lyr).getRecordset().getSourceInfo(),
//        			xmlDataSources,
//        			project
//
//            	)) return false;
//
//                ProjectTable pt = project.getTable((FLyrVect) lyr);
//                if (pt != null) {
//                	if (!this.addToXMLTable(pt,xmlTables,xmlDataSources,project)) return false;
//                }
//
//            }
//
//		} catch (ReadException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return false;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//
//		}
		return true;

	}

	private boolean addToXMLLayer(FLayer lyr,XMLEntity xmlLayers,XMLEntity xmlTables,XMLEntity xmlDataSources) {
		try {
			xmlLayers.addChild(lyr.getXMLEntity());

			return this.addToXMLLayerDependencies(lyr,xmlTables,xmlDataSources);

		} catch (XMLException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private XMLEntity newRootNode() {
		XMLEntity xml = new XMLEntity();
		fillXMLRootNode(xml);
		return xml;
	}

	private XMLEntity newLayersNode() {
		XMLEntity xmlLayers = new XMLEntity();
		xmlLayers.putProperty("type","layers");
		return xmlLayers;
	}

	private XMLEntity newDataSourcesNode() {
		XMLEntity xmlDataSources = new XMLEntity();
		xmlDataSources.putProperty("type","dataSources");
		return xmlDataSources;
	}

	private XMLEntity newTablesNode() {
		XMLEntity xmlTables = new XMLEntity();
		xmlTables.putProperty("type","tables");
		return xmlTables;
	}

	public boolean removeLayers(FLayer[] actives) {
    	for (int i = actives.length-1; i>=0; i--){
        	try {
				//actives[i].getParentLayer().removeLayer(actives[i]);
				//FLayers lyrs=getMapContext().getLayers();
				//lyrs.addLayer(actives[i]);
				actives[i].getParentLayer().removeLayer(actives[i]);

                if (actives[i] instanceof FLyrVect){
                    Project project = ((ProjectExtension)PluginServices.getExtension(ProjectExtension.class)).getProject();
                    FeatureTableDocument pt = project.getTable((FLyrVect) actives[i]);

                    ArrayList tables = project.getDocumentsByType(FeatureTableDocumentFactory.registerName);
                    for (int j = 0; j < tables.size(); j++) {
                        if (tables.get(j) == pt){
                            project.delDocument((ProjectDocument)tables.get(j));
                            break;
                        }
                    }

                    PluginServices.getMDIManager().closeSingletonWindow(pt);
                }


    		} catch (CancelationException e1) {
    			e1.printStackTrace();
    			return false;
    		}
    	}
		return true;
	}

}
