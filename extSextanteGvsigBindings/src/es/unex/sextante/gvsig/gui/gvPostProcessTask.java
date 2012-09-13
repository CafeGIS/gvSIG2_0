package es.unex.sextante.gvsig.gui;

import java.awt.Component;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.mapcontext.layers.LayersIterator;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.project.document.table.FeatureTableDocument;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.ProjectFactory;
import com.iver.cit.gvsig.project.documents.view.ProjectView;
import com.iver.cit.gvsig.project.documents.view.ProjectViewFactory;

import es.unex.sextante.core.GeoAlgorithm;
import es.unex.sextante.core.ObjectAndDescription;
import es.unex.sextante.core.OutputObjectsSet;
import es.unex.sextante.core.ParametersSet;
import es.unex.sextante.dataObjects.IDataObject;
import es.unex.sextante.dataObjects.ILayer;
import es.unex.sextante.dataObjects.IRasterLayer;
import es.unex.sextante.dataObjects.ITable;
import es.unex.sextante.dataObjects.IVectorLayer;
import es.unex.sextante.gui.additionalResults.AdditionalResults;
import es.unex.sextante.gui.core.SextanteGUI;
import es.unex.sextante.outputs.Output;
import es.unex.sextante.parameters.Parameter;
import es.unex.sextante.parameters.RasterLayerAndBand;

public class gvPostProcessTask implements Runnable{

    private MapContext m_MapContext;
    GeoAlgorithm m_Algorithm;
	private OutputObjectsSet m_Output;
	//private IProjection m_Projection;

    public gvPostProcessTask(GeoAlgorithm algorithm){

        m_Output = algorithm.getOutputObjects();
        m_Algorithm = algorithm;

    }

    public void run(){

    	setOutputView();

    	addResults();

    }

    private void setOutputView() {

        ProjectView view = null;
        ParametersSet parameters = m_Algorithm.getParameters();
        for (int i = 0; i < parameters.getNumberOfParameters(); i++) {
        	Parameter param = parameters.getParameter(i);
        	Object object = param.getParameterValueAsObject();
        	if (object instanceof ILayer){
        		view = getViewFromLayer((ILayer) object);
        		m_MapContext = view.getMapContext();
        		return;
        	}
        	else if (object instanceof ArrayList){
        		ArrayList list = (ArrayList) object;
        		for (int j = 0; j < list.size(); j++) {
					Object obj = list.get(j);
					if (obj instanceof ILayer){
			      		view = getViewFromLayer((ILayer) obj);
		        		m_MapContext = view.getMapContext();
		        		return;
					}
					else if(obj instanceof RasterLayerAndBand){
						RasterLayerAndBand rlab = (RasterLayerAndBand) obj;
			      		view = getViewFromLayer(rlab.getRasterLayer());
		        		m_MapContext = view.getMapContext();
		        		return;
					}
				}
        	}
        }

        final ProjectView newView = ProjectFactory.createView("NUEVA");
        ((ProjectExtension) PluginServices.getExtension(ProjectExtension.class))
        										.getProject().addDocument(newView);
        final IWindow window = newView.createWindow();

        Runnable doWorkRunnable = new Runnable() {
            public void run() {
                PluginServices.getMDIManager().addWindow(window);
        		m_MapContext = newView.getMapContext();
            }
        };
        try {
			SwingUtilities.invokeAndWait(doWorkRunnable);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {

			e.printStackTrace();
		}

    }

    private ProjectView getViewFromLayer(ILayer layer) {

    	FLayer gvSIGBaseLayer = (FLayer) layer.getBaseDataObject();
		Project project = ((ProjectExtension)PluginServices.getExtension(ProjectExtension.class)).getProject();
		ArrayList views = project.getDocumentsByType(ProjectViewFactory.registerName);

		for (int i = 0; i < views.size(); i++) {
			ProjectView view = (ProjectView) views.get(i);
			FLayers layers = view.getMapContext().getLayers();
			LayersIterator iter = new LayersIterator(layers);
			while (iter.hasNext()){
				FLayer gvSIGLayer = iter.nextLayer();
				if (gvSIGLayer.equals(gvSIGBaseLayer)){
					return view;
				}
			}

		}

		return null;

	}

	private void addResults(){

        FLayer layer = null;
        String sDescription;
        boolean bInvalidate = false;
        boolean bShowAdditionalPanel = false;

        if (m_MapContext != null){
            m_MapContext.beginAtomicEvent();
        }

        for (int i = 0; i < m_Output.getOutputObjectsCount(); i++) {

            Output out =  m_Output.getOutput(i);
            sDescription = out.getDescription();
            Object object = out.getOutputObject();
            if (object instanceof IVectorLayer){
            		layer = (FLayer) ((IVectorLayer)object).getBaseDataObject();
                    if (layer != null){
                        layer.setName(sDescription);
                        m_MapContext.getLayers().addLayer(layer);
                        bInvalidate = true;
                        SextanteGUI.getInputFactory().addDataObject((IDataObject) object);
                    }
            }
            else if (object instanceof ITable){
                try {
                	FeatureTableDocument table = (FeatureTableDocument) ((ITable)object).getBaseDataObject();
                    if (table != null){
                    	((ProjectExtension) PluginServices.getExtension(ProjectExtension.class))
    							.getProject().addDocument(table);

                        SextanteGUI.getInputFactory().addDataObject((IDataObject) object);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (object instanceof IRasterLayer){
                IRasterLayer rasterLayer = (IRasterLayer) object;
                layer = (FLayer) rasterLayer.getBaseDataObject();
                if (layer != null){
                    ((FLyrRasterSE)layer).setNoDataValue(rasterLayer.getNoDataValue());
                    layer.setName(sDescription);
                    m_MapContext.getLayers().addLayer(layer);
                    bInvalidate = true;
                    SextanteGUI.getInputFactory().addDataObject((IDataObject) object);
                }
            }
            else if (object instanceof String){
                JTextPane jTextPane;
                JScrollPane jScrollPane;
                jTextPane = new JTextPane();
                jTextPane.setEditable(false);
                jTextPane.setContentType("text/html");
                jTextPane.setText((String) object);
                jScrollPane = new JScrollPane();
                jScrollPane.setViewportView(jTextPane);
                jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                jTextPane.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
                AdditionalResults.addComponent(new ObjectAndDescription(sDescription, jScrollPane));
                bShowAdditionalPanel = true;
            }
            else if (object instanceof Component){
                AdditionalResults.addComponent(new ObjectAndDescription(sDescription, object));
                bShowAdditionalPanel = true;
            }

        }

        if (m_MapContext != null){
            m_MapContext.endAtomicEvent();
        }

        if (bInvalidate){
            m_MapContext.invalidate();
        }

        if (bShowAdditionalPanel){
            AdditionalResults.showPanel();
        }

    }

}
