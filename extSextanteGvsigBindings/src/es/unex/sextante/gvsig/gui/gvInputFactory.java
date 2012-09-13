package es.unex.sextante.gvsig.gui;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.mapcontext.layers.LayersIterator;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.project.document.table.FeatureTableDocument;
import org.gvsig.project.document.table.FeatureTableDocumentFactory;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.view.ProjectView;
import com.iver.cit.gvsig.project.documents.view.ProjectViewFactory;

import es.unex.sextante.dataObjects.IDataObject;
import es.unex.sextante.gui.core.AbstractInputFactory;
import es.unex.sextante.gui.core.NamedExtent;
import es.unex.sextante.gvsig.core.FileTools;
import es.unex.sextante.gvsig.core.gvRasterLayer;
import es.unex.sextante.gvsig.core.gvTable;
import es.unex.sextante.gvsig.core.gvVectorLayer;

public class gvInputFactory extends AbstractInputFactory {

	public void createDataObjects() {

		ArrayList list = new ArrayList();

		Project project = ((ProjectExtension)PluginServices.getExtension(ProjectExtension.class)).getProject();
		ArrayList views = project.getDocumentsByType(ProjectViewFactory.registerName);
		for (int i = 0; i < views.size(); i++) {
			ProjectView view = (ProjectView) views.get(i);
			FLayers layers = view.getMapContext().getLayers();
			LayersIterator iter = new LayersIterator(layers);
			while (iter.hasNext()){
				FLayer layer = iter.nextLayer();
				if (layer instanceof FLyrRasterSE){
					gvRasterLayer rasterLayer = new gvRasterLayer();
					rasterLayer.create((FLyrRasterSE)layer);
					list.add(rasterLayer);
				}
				else if (layer instanceof FLyrVect){
					gvVectorLayer vectorLayer =new gvVectorLayer();
					vectorLayer.create((FLyrVect)layer);
					list.add(vectorLayer);
				}
			}
		}

		ArrayList tables = project.getDocumentsByType(FeatureTableDocumentFactory.registerName);
		for (int i = 0; i < tables.size(); i++) {
			gvTable table = new gvTable();
			table.create((FeatureTableDocument) tables.get(i));
			list.add(table);
		}

		m_Objects =  new IDataObject[list.size()];
		for (int i = 0; i < list.size(); i++) {
			m_Objects[i] = (IDataObject)list.get(i);
		}

	}

	public NamedExtent[] getPredefinedExtents() {

		Project project = ((ProjectExtension)PluginServices.getExtension(ProjectExtension.class)).getProject();
		ArrayList views = project.getDocumentsByType(ProjectViewFactory.registerName);
		NamedExtent ne[] = new NamedExtent[views.size()];
		for (int i = 0; i < views.size(); i++) {
			ProjectView view = (ProjectView) views.get(i);
			Rectangle2D extent = view.getMapContext().getViewPort().getExtent();
			String sName = view.getName();
			ne[i] = new NamedExtent(sName, extent);
		}

		return ne;
	}


	public String[] getRasterLayerInputExtensions() {

		return FileTools.RASTER_EXT_IN;

	}

	public String[] getVectorLayerInputExtensions() {

		return FileTools.VECTOR_EXT_IN;

	}

	public String[] getTableInputExtensions() {

		return FileTools.TABLE_EXT;

	}

	public IDataObject openDataObjectFromFile(String sFilename) {

		Object object = FileTools.open(sFilename);

		if (object == null){
			return null;
		}
		else if (object instanceof FLyrRasterSE){
			gvRasterLayer layer = new gvRasterLayer();
			layer.create(object);
			return layer;
		}
		else if (object instanceof FLyrVect){
			gvVectorLayer layer = new gvVectorLayer();
			layer.create(object);
			return layer;
		}
		else{
			return null;
		}

	}

}
