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
package org.gvsig.gazetteer.loaders;


import java.awt.Color;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.gvsig.fmap.crs.CRSFactory;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateEnvelopeException;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.utils.FConstant;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.layers.GraphicLayer;
import org.gvsig.fmap.mapcontext.rendering.legend.FGraphicLabel;
import org.gvsig.fmap.mapcontext.rendering.symbols.FSymbol;
import org.gvsig.gazetteer.DeleteSearchesExtension;
import org.gvsig.gazetteer.querys.Feature;
import org.gvsig.gazetteer.querys.GazetteerQuery;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;



/**
 * This class is used to load a new feature like a layer in gvSIG
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class FeatureLoader {     
	private static GeometryManager geometryManager = GeometryLocator.getGeometryManager();
	
	/**
	 * Coordinates Transformer
	 */
	private ICoordTrans coordTrans;	
	
	/**
	 * @param projection
	 * Server projection
	 */
	public FeatureLoader(String sProjection){
		BaseView activeView = 
			(BaseView) PluginServices.getMDIManager().getActiveWindow();
		
		
		IProjection projection = CRSFactory.getCRS(sProjection);
		if (projection == null){
			projection = activeView.getMapControl().getViewPort().getProjection();
		}
		
		coordTrans=  projection.getCT(activeView.getMapControl().getViewPort().getProjection());
	}
	/**
	 * It makes a zoom in gvSIG
	 * @param
	 * feature
	 * @param
	 * query
	 * Query that contains advanced options to search and
	 * to show the results
	 * @return
	 * true or false if fail
	 */
	public boolean load(Feature feature,GazetteerQuery query) {
		addAndDrawLabel(feature,
				query.getOptions().getAspect().isKeepOld(),
				query.getOptions().getAspect().isPaintCurrent());

		if (query.getOptions().getAspect().isGoTo()){
			focusCenter(feature);
		}        

		return true;
	}    

	/**
	 * This method focus the toponim in the center of the view 
	 * @param feature
	 * Feature that contains the coordinates
	 */
	private void focusCenter(Feature feature){
		BaseView activeView = 
			(BaseView) PluginServices.getMDIManager().getActiveWindow();

		IProjection projection = activeView.getProjection();
		ViewPort viewPort = activeView.getMapControl().getViewPort();
		Point2D point = getReprojectedPoint(feature.getCoordinates());
		
		Rectangle2D zoomExtent = null;
		if (viewPort.getAdjustedExtent() == null){
			
		}else{
			Toolkit kit = Toolkit.getDefaultToolkit();
			double dpi = kit.getScreenResolution();
			Envelope envelope = viewPort.getAdjustedExtent();
			Rectangle2D rectangle = new Rectangle2D.Double(envelope.getLowerCorner().getX(),
					envelope.getLowerCorner().getY(),
					envelope.getUpperCorner().getX() - envelope.getLowerCorner().getX(),
					envelope.getUpperCorner().getY() - envelope.getLowerCorner().getY());
			Rectangle2D extent = projection.getExtent(rectangle,
					new Double(25000).doubleValue(),
					new Double(viewPort.getImageWidth()).doubleValue(),
					new Double(viewPort.getImageHeight()).doubleValue(),
					MapContext.CHANGE[viewPort.getMapUnits()],
					MapContext.CHANGE[viewPort.getDistanceUnits()],
					dpi);				
			if (extent != null){
				try {
					envelope = geometryManager.createEnvelope(SUBTYPES.GEOM2D);
					Point loweCorner = geometryManager.createPoint(point.getX() - extent.getWidth()/2,
							point.getY() - extent.getHeight()/2,
							SUBTYPES.GEOM2D);
					Point upperCorner = geometryManager.createPoint(point.getX() + extent.getWidth()/2,
							point.getY() + extent.getHeight()/2,
							SUBTYPES.GEOM2D);
					envelope.setLowerCorner(loweCorner);
					envelope.setUpperCorner(upperCorner);				
					activeView.getMapControl().getMapContext().zoomToEnvelope(envelope);
				} catch (CreateEnvelopeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (CreateGeometryException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
			}
		}		
	} 
	
	private Envelope getEnvelope(Rectangle2D rectangle) throws CreateGeometryException, CreateEnvelopeException{	
		Envelope envelope = geometryManager.createEnvelope(SUBTYPES.GEOM2D);
		Point loweCorner = geometryManager.createPoint(rectangle.getMinX(),
				rectangle.getMinY(),
				SUBTYPES.GEOM2D);
		Point upperCorner = geometryManager.createPoint(rectangle.getMaxX(),
				rectangle.getMaxY(),
				SUBTYPES.GEOM2D);
		envelope.setLowerCorner(loweCorner);
		envelope.setUpperCorner(upperCorner);	
		return envelope;
	}

	/**
	 * It adds a new Label to the current view
	 * @param feature
	 * To obtain the coordinates and the toponim name
	 * @param isRemoveOldClicked
	 * To remove or keep the old searches
	 */
	private void addAndDrawLabel(Feature feature,boolean isRemoveOldClicked,boolean isMarkedPlaceClicked){
		BaseView activeView = 
			(BaseView) PluginServices.getMDIManager().getActiveWindow();

		GraphicLayer lyr = activeView.getMapControl().getMapContext().getGraphicsLayer();
		
		if (isRemoveOldClicked){
			lyr.clearAllGraphics();
		}	
		
		if (isMarkedPlaceClicked){
			int idSymbol = lyr.addSymbol(getSymbol());			
				
			Point2D point2d = getReprojectedPoint(feature.getCoordinates());
			Point point;
			try {
				point = geometryManager.createPoint(point2d.getX(), point2d.getY(), SUBTYPES.GEOM2D);
				FGraphicLabel theLabel = new FGraphicLabel(point, idSymbol, feature.getName());
				lyr.addGraphic(theLabel);

				DeleteSearchesExtension.setVisible();
				PluginServices.getMainFrame().enableControls();
			} catch (CreateGeometryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}			
		
		activeView.getMapControl().drawGraphics();

		//This line could look stupid, but is necessary because grawGraphics does't
		//remove the old Graphics searched
		try {
			activeView.getMapControl().getViewPort().setEnvelope(getEnvelope(activeView.getMapControl().getViewPort().getExtent()));
		} catch (CreateGeometryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CreateEnvelopeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a FSymbol
	 * @return
	 * FSymbol
	 */
	private FSymbol getSymbol(){
		FSymbol theSymbol = new FSymbol(FConstant.SYMBOL_TYPE_TEXT); 
		theSymbol.setColor(Color.RED);
		theSymbol.setStyle(1);
		theSymbol.setFontColor(Color.BLACK);
		theSymbol.setSizeInPixels(true);
		theSymbol.setSize(5);
		return theSymbol;
	}
	
//	private SimpleTextSymbol getSymbol(String text){
//		SimpleTextSymbol theSymbol = new SimpleTextSymbol(); 
//		theSymbol.setTextColor(Color.RED);
//		theSymbol.setText(text);
//		theSymbol.setFontSize(10);		
//		return theSymbol;
//	}
	
	/**
	 * Reprojects the new point
	 * @param ptOrig
	 * Origin point
	 * @return
	 * FPoint2D
	 */
	private Point2D getReprojectedPoint(Point2D ptOrigin){
		Point2D ptDest = null;
		return getCoordTrans().convert(ptOrigin, ptDest);
	}
	/**
	 * @return the coordTrans
	 */
	public ICoordTrans getCoordTrans() {
		return coordTrans;
	}
	
	

}
