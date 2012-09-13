/*
 * Created on 10-abr-2006
 *
 * gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
/* CVS MESSAGES:
*
* $Id:
* $Log:
*/
package com.iver.cit.gvsig.gui.cad.tools;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.gvsig.fmap.dal.feature.EditableFeature;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.operation.Draw;
import org.gvsig.fmap.geom.operation.DrawOperationContext;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.operation.GeometryOperationNotSupportedException;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.util.Converter;
import org.gvsig.fmap.mapcontext.layers.SpatialCache;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.tools.locator.LocatorException;

import statemap.State;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.gui.cad.DefaultCADTool;
import com.iver.cit.gvsig.gui.cad.exception.CommandException;
import com.iver.cit.gvsig.gui.cad.tools.smc.SplitGeometryCADToolContext;
import com.iver.cit.gvsig.gui.cad.tools.split.SplitStrategy;
import com.iver.cit.gvsig.layers.VectorialLayerEdited;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.PrecisionModel;


/**
 * CAD Tool which splits the selected geometries of a vectorial editing
 * layer with a digitized polyline.
 *
 *
 * @author Alvaro Zabala
 *
 */
public class SplitGeometryCADTool extends DefaultCADTool {

	private static Logger logger = Logger.getLogger(SplitGeometryCADTool.class.getName());

	/**
	 * String representation of this tool (used for example to active the tool
	 * in mapcontrol)
	 */
	public static final String SPLIT_GEOMETRY_TOOL_NAME = "_split_geometry";



	/**
	 * finite state machine for this CAD tool
	 */
	protected SplitGeometryCADToolContext _fsm;



	/**
	 * Flag to mark if the digitized line has been finished.
	 */
	protected boolean digitizingFinished = false;

	/**
	 * Collection of digitized geometries
	 */
	protected List<Point2D> clickedPoints;


	/**
	 * Default Constructor
	 */
	public SplitGeometryCADTool(){

	}


	/**
	 * Initialization method.
	 */
	public void init() {
		digitizingFinished = false;
		_fsm = new SplitGeometryCADToolContext(this);
		setNextTool(SplitGeometryCADTool.SPLIT_GEOMETRY_TOOL_NAME);
	}



	public boolean isDigitizingFinished() {
		return digitizingFinished;
	}

	public String toString() {
		return SplitGeometryCADTool.SPLIT_GEOMETRY_TOOL_NAME;
	}



	public void finishDigitizedLine(){
	}
//	public ArrayList getSelectedRows(){
//		VectorialLayerEdited vle = getVLE();
//		ArrayList selection = vle.getSelectedRow();
//		if( selection.size() == 0){
//			VectorialEditableAdapter vea = vle.getVEA();
//			try {
//				FBitSet bitset = vea.getSelection();
//				for (int j = bitset.nextSetBit(0);j >= 0;j = bitset.nextSetBit(j + 1)){
//					IRowEdited rowEd = vea.getRow(j);
//					selection.add(rowEd);
//				}
//			} catch (ExpansionFileReadException e) {
//				e.printStackTrace();
//			} catch (ReadDriverException e) {
//				e.printStackTrace();
//			}
//		}//selection size
//		return selection;
//	}
//	public ArrayList getSelectedRows(){
//		return (ArrayList) CADUtil.getSelectedFeatures(getVLE());
//	}
	public Coordinate[] getPoint2DAsCoordinates(Point2D[] point2d) {
		Coordinate[] solution = new Coordinate[point2d.length];
		for (int i = 0; i < point2d.length; i++) {
			solution[i] = new Coordinate(point2d[i].getX(), point2d[i].getY());
		}
		return solution;
	}
	public void splitSelectedGeometryWithDigitizedLine(){
		Point2D[] clickedPts = new Point2D[this.clickedPoints.size()];
		clickedPoints.toArray(clickedPts);
		Coordinate[] digitizedCoords = getPoint2DAsCoordinates(clickedPts);
		LineString splittingLs = new GeometryFactory(
				new PrecisionModel(10000)).createLineString(digitizedCoords);
		try {
			VectorialLayerEdited vle = getVLE();
			FeatureStore store=vle.getFeatureStore();
			Iterator<Feature> selected = store.getFeatureSelection().iterator();

			getCadToolAdapter().getMapControl().getMapContext().beginAtomicEvent();
			store.beginEditingGroup(getName());
			while (selected.hasNext()) {
				Feature feature = (Feature) selected.next();
				org.gvsig.fmap.geom.Geometry ig = feature.getDefaultGeometry();
				Geometry jtsGeo = Converter.geometryToJts(ig);
				if (jtsGeo==null)
					return;
				Geometry splitGeo = SplitStrategy.splitOp(jtsGeo, splittingLs);
				if(splitGeo instanceof GeometryCollection
						&& ((GeometryCollection)splitGeo).getNumGeometries()>1){

					GeometryCollection gc = (GeometryCollection)splitGeo;
					for(int j = 0; j < gc.getNumGeometries(); j++){
						Geometry g = gc.getGeometryN(j);
						org.gvsig.fmap.geom.Geometry fmapGeo = Converter.jtsToGeometry(g);

						if (j==0){
							EditableFeature eFeature=feature.getEditable();
							eFeature.setGeometry(store.getDefaultFeatureType().getDefaultGeometryAttributeName(), fmapGeo);
							store.update(eFeature);
						}else{

							EditableFeature newFeature=store.createNewFeature(store.getDefaultFeatureType(), feature);
							newFeature.setGeometry(store.getDefaultFeatureType().getDefaultGeometryAttributeName(), fmapGeo);
							store.insert(newFeature);
							SpatialCache spatialCache=((FLyrVect)vle.getLayer()).getSpatialCache();
							Envelope envelope = fmapGeo.getEnvelope();
							spatialCache.insert(envelope,fmapGeo);
						}
					}//for j
				}//if splitGeo

			}

			store.endEditingGroup();

			getCadToolAdapter().getMapControl().getMapContext().endAtomicEvent();
		} catch (Exception ex) {
			PluginServices.getLogger().error("Error splitting geom", ex);
		}
	}

	public void end(){
		getCadToolAdapter().refreshEditedLayer();
		init();
	}


	public void addOption(String s) {
		State actualState = _fsm.getPreviousState();
		String status = actualState.getName();
		if (s.equals(PluginServices.getText(this, "cancel"))) {
			init();
			return;
		}
		if (status.equals("TopologicalEdition.FirstPoint")) {
			return;
		}
		init();

	}

	public void addPoint(double x, double y, InputEvent event) {

		State actualState = _fsm.getPreviousState();
		String status = actualState.getName();
		if (status.equals("SplitGeometry.FirstPoint")) {
			clickedPoints = new ArrayList<Point2D>();
			clickedPoints.add(new Point2D.Double(x,y));
		} else if (status.equals("SplitGeometry.DigitizingLine")) {
			clickedPoints.add(new Point2D.Double(x,y));
			if (event != null && ((MouseEvent) event).getClickCount() == 2) {
				digitizingFinished = true;
				finishDigitizedLine();
				splitSelectedGeometryWithDigitizedLine();
				end();
			}
		}
	}

	public void addValue(double d) {
	}


	/**
	 * Draws a polyline with the clicked digitized points in the specified graphics.
	 *
	 * @param g2 graphics on to draw the polyline
	 * @param x last x mouse pointer position
	 * @param y last y mouse pointer position
	 */
	protected void drawPolyLine(Graphics2D g, double x, double y) {
		GeneralPathX gpx =
			new GeneralPathX(GeneralPathX.WIND_EVEN_ODD,
									clickedPoints.size());
		Point2D firstPoint = clickedPoints.get(0);
		gpx.moveTo(firstPoint.getX(), firstPoint.getY());
		for (int i = 1; i < clickedPoints.size(); i++) {
			Point2D clickedPoint = clickedPoints.get(i);
			gpx.lineTo(clickedPoint.getX(), clickedPoint.getY());

		}
		gpx.lineTo(x, y);
		org.gvsig.fmap.geom.Geometry geom;
		try {
			geom = GeometryLocator.getGeometryManager().createCurve(gpx, SUBTYPES.GEOM2D);
			DrawOperationContext doc=new DrawOperationContext();
			doc.setGraphics((Graphics2D)g);
			doc.setViewPort(getCadToolAdapter().getMapControl().getViewPort());
			doc.setSymbol(DefaultCADTool.geometrySelectSymbol);
			geom.invokeOperation(Draw.CODE,doc);
		} catch (GeometryOperationNotSupportedException e) {
			e.printStackTrace();
		} catch (GeometryOperationException e) {
			e.printStackTrace();
		} catch (LocatorException e) {
			e.printStackTrace();
		} catch (CreateGeometryException e) {
			e.printStackTrace();
		}
	}




	public void drawOperation(Graphics g, double x, double y) {
		State actualState = _fsm.getState();
        String status = actualState.getName();

        // draw splitting line
        if ((status.equals("SplitGeometry.DigitizingLine"))) {
        	drawPolyLine((Graphics2D) g, x, y);
         }

        // draw selection
        try {
        	Image imgSel = getVLE().getSelectionImage();
        	if (imgSel != null)
        		g.drawImage(imgSel, 0, 0, null);
        } catch (Exception e) {
        	PluginServices.getLogger().error("Error drawing Editing Selection", e);
        }
	}

	public String getName() {
		return PluginServices.getText(this, "split_geometry_shell");
	}


	public void transition(double x, double y, InputEvent event) {
		try {
			_fsm.addPoint(x, y, event);
		} catch (Exception e) {
			init();
		}

	}


	public void transition(double d) {
		_fsm.addValue(d);
	}

	public void transition(String s) throws CommandException {
		if (!super.changeCommand(s)) {
			_fsm.addOption(s);
		}
	}

}
