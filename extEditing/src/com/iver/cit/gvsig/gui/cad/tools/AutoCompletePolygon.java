package com.iver.cit.gvsig.gui.cad.tools;

import java.awt.Graphics;
import java.awt.Graphics2D;

import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.operation.Draw;
import org.gvsig.fmap.geom.operation.DrawOperationContext;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.operation.GeometryOperationNotSupportedException;
import org.gvsig.fmap.geom.operation.tojts.ToJTS;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.util.Converter;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.gui.cad.DefaultCADTool;

public class AutoCompletePolygon extends PolylineCADTool {

	@Override
	 /**
     * Método para dibujar la lo necesario para el estado en el que nos
     * encontremos.
     *
     * @param g Graphics sobre el que dibujar.
     * @param selectedGeometries BitSet con las geometrías seleccionadas.
     * @param x parámetro x del punto que se pase para dibujar.
     * @param y parámetro x del punto que se pase para dibujar.
     */
    public void drawOperation(Graphics g, double x,
        double y) {
        Geometry geom=getGeometry();
        if (geom.getHandlers(Geometry.SELECTHANDLER).length ==0 && firstPoint!=null) {
        	GeneralPathX gpx = new GeneralPathX();
        	gpx.moveTo(firstPoint.getX(), firstPoint.getY());
        	gpx.lineTo(x, y);
        	DrawOperationContext doc=new DrawOperationContext();
			doc.setGraphics((Graphics2D)g);
			doc.setViewPort(getCadToolAdapter().getMapControl().getViewPort());
			doc.setSymbol(DefaultCADTool.geometrySelectSymbol);
        	try {
				createCurve(gpx).invokeOperation(Draw.CODE, doc);
			} catch (GeometryOperationNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (GeometryOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        else if (geom.getHandlers(Geometry.SELECTHANDLER).length > 1) {
        	GeneralPathX gpxGeom=new GeneralPathX();
        	gpxGeom.moveTo(x,y);
        	gpxGeom.append(geom.getPathIterator(null,Converter.FLATNESS), true);

        	gpxGeom.closePath();
        	DrawOperationContext doc=new DrawOperationContext();
			doc.setGraphics((Graphics2D)g);
			doc.setViewPort(getCadToolAdapter().getMapControl().getViewPort());
			doc.setSymbol(DefaultCADTool.geometrySelectSymbol);
        	try {
				createCurve(gpxGeom).invokeOperation(Draw.CODE, doc);
			} catch (GeometryOperationNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (GeometryOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }


	private Geometry autoComplete(Geometry digitizedGeom) throws CreateGeometryException {
		com.vividsolutions.jts.geom.Geometry jtsGeom = null;
		DisposableIterator iterator = null;
		try {
			jtsGeom = (com.vividsolutions.jts.geom.Geometry)digitizedGeom.invokeOperation(ToJTS.CODE, null);
			FLyrVect lyrVect = (FLyrVect) getVLE().getLayer();
			// Se supone que debe ser rápido, ya que está indexado

			FeatureSet selected = lyrVect.queryByGeometry(digitizedGeom,
					lyrVect.getFeatureStore().getDefaultFeatureType());
			iterator = selected.iterator();
			while (iterator.hasNext()) {
				Feature feature = (Feature) iterator.next();
				Geometry aux = feature.getDefaultGeometry();
				com.vividsolutions.jts.geom.Geometry jtsAux = (com.vividsolutions.jts.geom.Geometry)aux.invokeOperation(ToJTS.CODE, null);
				jtsGeom = jtsGeom.difference(jtsAux);
			}
		} catch (ReadException e) {
			NotificationManager.showMessageError(
					PluginServices.getText(this, "Error_in_Autocomplete_Polygon_Tool_")
					+ " " + e.getLocalizedMessage(),
					e);
		} catch (com.vividsolutions.jts.geom.TopologyException e) {
			NotificationManager.showMessageError(
					PluginServices.getText(this, "Error_in_Autocomplete_Polygon_Tool_")
					+ " " + e.getLocalizedMessage(),
					e);
		} catch (Exception e) {
			NotificationManager.showMessageError(
					PluginServices.getText(this, "Error_in_Autocomplete_Polygon_Tool_")
					+ " " + e.getLocalizedMessage(),
					e);
		} finally {
			if (iterator != null) {
				iterator.dispose();
			}
		}

		return Converter.jtsToGeometry(jtsGeom);
	}

	@Override
	public Feature insertGeometry(Geometry geometry) {
		Geometry newGeom;
		try {
			newGeom = autoComplete(geometry);
			return super.insertGeometry(newGeom);
		} catch (CreateGeometryException e) {
			NotificationManager.addError(e);
		}
		return null;
	}
	
	 public boolean isApplicable(int shapeType) {
	        switch (shapeType) {
	        case Geometry.TYPES.SURFACE:
	            return true;
	        }
	        return false;
	    }
}