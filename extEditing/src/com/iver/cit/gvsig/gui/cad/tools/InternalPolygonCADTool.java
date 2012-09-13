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
package com.iver.cit.gvsig.gui.cad.tools;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.EditableFeature;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureSelection;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.aggregate.MultiPrimitive;
import org.gvsig.fmap.geom.aggregate.impl.BaseMultiPrimitive;
import org.gvsig.fmap.geom.operation.DrawInts;
import org.gvsig.fmap.geom.operation.DrawOperationContext;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.operation.GeometryOperationNotSupportedException;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.util.Converter;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.CADExtension;
import com.iver.cit.gvsig.gui.cad.DefaultCADTool;
import com.iver.cit.gvsig.gui.cad.exception.CommandException;
import com.iver.cit.gvsig.gui.cad.tools.smc.InternalPolygonCADToolContext;
import com.iver.cit.gvsig.layers.VectorialLayerEdited;
import com.vividsolutions.jts.geom.GeometryCollection;


/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class InternalPolygonCADTool extends DefaultCADTool {
    protected InternalPolygonCADToolContext _fsm;
    protected ArrayList<Point2D> points=new ArrayList<Point2D>();
    protected Geometry geometry=null;

    /**
     * Crea un nuevo PolylineCADTool.
     */
    public InternalPolygonCADTool() {
    }

    /**
     * Método de incio, para poner el código de todo lo que se requiera de una
     * carga previa a la utilización de la herramienta.
     */
    public void init() {
        _fsm = new InternalPolygonCADToolContext(this);
        points.clear();
    }

    /* (non-Javadoc)
     * @see com.iver.cit.gvsig.gui.cad.CADTool#transition(com.iver.cit.gvsig.fmap.layers.FBitSet, double, double)
     */
    public void transition(double x, double y, InputEvent event) {
        _fsm.addPoint(x, y, event);
    }

    /* (non-Javadoc)
     * @see com.iver.cit.gvsig.gui.cad.CADTool#transition(com.iver.cit.gvsig.fmap.layers.FBitSet, double)
     */
    public void transition(double d) {
        _fsm.addValue(d);
    }

    /* (non-Javadoc)
     * @see com.iver.cit.gvsig.gui.cad.CADTool#transition(com.iver.cit.gvsig.fmap.layers.FBitSet, java.lang.String)
     */
    public void transition(String s) throws CommandException {
    	if (!super.changeCommand(s)){
    		_fsm.addOption(s);
    	}
    }

    /**
     * DOCUMENT ME!
     */
    public void selection() {
    	FeatureSet selection=null;
    	try {
    		selection = (FeatureSet)getVLE().getFeatureStore().getSelection();

    		if (selection.getSize() == 0 && !CADExtension.getCADTool().getClass().getName().equals("com.iver.cit.gvsig.gui.cad.tools.SelectionCADTool")) {
    			CADExtension.setCADTool("_selection",false);
    			((SelectionCADTool) CADExtension.getCADTool()).setNextTool(
    			"_internalpolygon");
    		}
    	} catch (ReadException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /**
     * Equivale al transition del prototipo pero sin pasarle como parámetro el
     * editableFeatureSource que ya estará creado.
     *
     * @param x parámetro x del punto que se pase en esta transición.
     * @param y parámetro y del punto que se pase en esta transición.
     */
    public void addPoint(double x, double y,InputEvent event) {
    	if (((MouseEvent)event).getClickCount()==2){
    		addOption(PluginServices.getText(this,"InternalPolygonCADTool.end"));
    		return;
    	}
    	VectorialLayerEdited vle=getVLE();
    	FeatureSet featureCollection=null;
    	try {
    		featureCollection = (FeatureSet)vle.getFeatureStore().getSelection();
    		if (featureCollection.getSize()==1){
    			Feature feature=(Feature) featureCollection.iterator().next();
    			geometry=(feature.getDefaultGeometry()).cloneGeometry();
    			if (geometry.contains(x,y)){
    				points.add(new Point2D.Double(x,y));
    			}else{
    				JOptionPane.showMessageDialog(((Component)PluginServices.getMainFrame()),PluginServices.getText(this,"debe_insertar_el_punto_dentro_de_los_limites_de_la_geometria"));
    			}
    		}
    	} catch (ReadException e) {
    		NotificationManager.addError(e.getMessage(),e);
    	} catch (DataException e) {
    		NotificationManager.addError(e.getMessage(),e);
		}
	}

	/**
     * Método para dibujar la lo necesario para el estado en el que nos
     * encontremos.
     *
     * @param g Graphics sobre el que dibujar.
     * @param x parámetro x del punto que se pase para dibujar.
     * @param y parámetro x del punto que se pase para dibujar.
     */
    public void drawOperation(Graphics g, double x, double y) {
    	Point2D[] ps=points.toArray(new Point2D[0]);
    	GeneralPathX gpx=new GeneralPathX();
    	GeneralPathX gpx1=new GeneralPathX();

    	if (ps.length>0){
    	for (int i=0;i<ps.length;i++){
    		if (i==0){
    			gpx.moveTo(ps[i].getX(),ps[i].getY());
    			gpx1.moveTo(ps[i].getX(),ps[i].getY());
    		}else{
    			gpx.lineTo(ps[i].getX(),ps[i].getY());
    			gpx1.lineTo(ps[i].getX(),ps[i].getY());
    		}

    	}
    	DrawOperationContext doc=new DrawOperationContext();
		doc.setGraphics((Graphics2D)g);
		doc.setViewPort(getCadToolAdapter().getMapControl().getViewPort());
		doc.setSymbol(DefaultCADTool.selectionSymbol);
		gpx.lineTo(x,y);
    	gpx.closePath();
    	gpx1.closePath();
//    	if (ps.length==1){
//    		Geometry geom=geomFactory.createPolyline2D(gpx);
//
//        	try {
//				geom.invokeOperation(DrawInts.CODE,doc);
//			} catch (GeometryOperationNotSupportedException e) {
//				e.printStackTrace();
//			} catch (GeometryOperationException e) {
//				e.printStackTrace();
//			}
////        	geom.drawInts((Graphics2D)g,CADExtension.getEditionManager().getMapControl().getViewPort(),DefaultCADTool.geometrySelectSymbol);
//    	}
    	Geometry geom = createSurface(gpx);
    	Geometry geom1 = createSurface(gpx1);
    	try {

			geom1.invokeOperation(DrawInts.CODE,doc);
		} catch (GeometryOperationNotSupportedException e) {
			e.printStackTrace();
		} catch (GeometryOperationException e) {
			e.printStackTrace();
		}

    	try {
    		doc.setSymbol(DefaultCADTool.geometrySelectSymbol);

			geom.invokeOperation(DrawInts.CODE,doc);
		} catch (GeometryOperationNotSupportedException e) {
			e.printStackTrace();
		} catch (GeometryOperationException e) {
			e.printStackTrace();
		}
    	}
    }

    /**
     * Add a diferent option.
     *
     * @param s Diferent option.
     */
    public void addOption(String s) {
    	VectorialLayerEdited vle=getVLE();
    	FeatureStore featureStore=null;
    	try {
			featureStore = vle.getFeatureStore();
		} catch (ReadException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	DisposableIterator iterator = null;
    	try {
			iterator = ((FeatureSelection) featureStore.getSelection())
					.iterator();
			if (s.equals(PluginServices.getText(this, "end"))
					|| s.equalsIgnoreCase(PluginServices.getText(this,
							"InternalPolygonCADTool.end"))) {
				if (points.size() > 0) {
					Feature feature = (Feature) iterator.next();
					geometry = (feature.getDefaultGeometry()).cloneGeometry();
					if (geometry instanceof GeometryCollection) {
						BaseMultiPrimitive gc = (BaseMultiPrimitive) geometry;
						geometry = createNewPolygonGC(gc, points
								.toArray(new Point2D[0]));
					} else {
						geometry = createNewPolygon(geometry, points
								.toArray(new Point2D[0]));
					}
					try {
						EditableFeature eFeature = feature.getEditable();
						eFeature.setGeometry(featureStore
								.getDefaultFeatureType()
								.getDefaultGeometryAttributeName(), geometry);
						featureStore.update(eFeature);
					} catch (ReadException e) {
						NotificationManager.addError(e.getMessage(), e);
					} catch (DataException e) {
						NotificationManager.addError(e.getMessage(), e);
					}
					ArrayList rows = new ArrayList();
					rows.add(feature);
					// vle.setSelectionCache(VectorialLayerEdited.NOTSAVEPREVIOUS,
					// rows);
    			}
    			points.clear();
				refresh();

    		} else if (s.equals(PluginServices.getText(this, "cancel"))) {
				points.clear();
			}
		} catch (DataException e1) {
			if (iterator != null) {
				iterator.dispose();
			}
			e1.printStackTrace();
    	}
    }
    /* (non-Javadoc)
     * @see com.iver.cit.gvsig.gui.cad.CADTool#addvalue(double)
     */
    public void addValue(double d) {
    }
    private Geometry createNewPolygon(Geometry gp,Point2D[] ps) {
        GeneralPathX newGp = new GeneralPathX();
        double[] theData = new double[6];

        PathIterator theIterator;
        int theType;
        int numParts = 0;


        theIterator = gp.getPathIterator(null, Converter.FLATNESS);
        while (!theIterator.isDone()) {
            theType = theIterator.currentSegment(theData);
            switch (theType) {

                case PathIterator.SEG_MOVETO:
                    numParts++;
                    newGp.moveTo(theData[0], theData[1]);
                    break;

                case PathIterator.SEG_LINETO:
                    newGp.lineTo(theData[0], theData[1]);
                    break;

                case PathIterator.SEG_QUADTO:
                    newGp.quadTo(theData[0], theData[1], theData[2], theData[3]);
                    break;

                case PathIterator.SEG_CUBICTO:
                    newGp.curveTo(theData[0], theData[1], theData[2], theData[3], theData[4], theData[5]);
                    break;

                case PathIterator.SEG_CLOSE:
                	newGp.closePath();
                    break;
            } //end switch

            theIterator.next();
        } //end while loop
     GeneralPathX gpxInternal=new GeneralPathX();
     gpxInternal.moveTo(ps[ps.length-1].getX(),ps[ps.length-1].getY());
     for (int i=ps.length-1;i>=0;i--){
    	 gpxInternal.lineTo(ps[i].getX(),ps[i].getY());
     }
     gpxInternal.lineTo(ps[ps.length-1].getX(),ps[ps.length-1].getY());
     if (!gpxInternal.isCCW()) {
    	 gpxInternal.flip();
     }
     newGp.append(gpxInternal,false);

     return createSurface(newGp);
    }
    private Geometry createNewPolygonGC(BaseMultiPrimitive gp,Point2D[] ps) {
    	ArrayList geoms=new ArrayList();
    	Geometry[] geometries=gp.getGeometries();
    	for (int i = 0;i<geometries.length;i++) {
    		geoms.add(geometries[i]);
    	}
    	GeneralPathX gpx=new GeneralPathX();
		gpx.moveTo(ps[ps.length-1].getX(),ps[ps.length-1].getY());
    	for (int i=ps.length-2;i>=0;i--){
    		gpx.lineTo(ps[i].getX(),ps[i].getY());
    		geoms.add(createCurve(gpx));
    		gpx=new GeneralPathX();
    		gpx.moveTo(ps[i].getX(),ps[i].getY());
    	}
    	gpx.lineTo(ps[ps.length-1].getX(),ps[ps.length-1].getY());
    	geoms.add(createCurve(gpx));
    	MultiPrimitive gc = createMultiPrimitive(((Geometry[])geoms.toArray(new Geometry[0])));
    	return gc;
    }
    public String getName() {
		return PluginServices.getText(this,"internal_polygon_");
	}

	public String toString() {
		return "_internalpolygon";
	}

	public boolean isApplicable(int shapeType) {
		switch (shapeType) {
		case Geometry.TYPES.POINT:
		case Geometry.TYPES.CURVE:
		case Geometry.TYPES.MULTIPOINT:
			return false;
		}
		return true;
	}
}
