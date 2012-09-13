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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.InputEvent;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.EditableFeature;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.operation.Draw;
import org.gvsig.fmap.geom.operation.DrawOperationContext;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.operation.GeometryOperationNotSupportedException;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.util.Converter;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.CADExtension;
import com.iver.cit.gvsig.gui.cad.DefaultCADTool;
import com.iver.cit.gvsig.gui.cad.exception.CommandException;
import com.iver.cit.gvsig.gui.cad.tools.smc.BreakCADToolContext;
import com.iver.cit.gvsig.gui.cad.tools.smc.BreakCADToolContext.BreakCADToolState;
import com.iver.cit.gvsig.layers.VectorialLayerEdited;

/**
 * Recorta una polilínea en dos partes.
 *
 * @author Vicente Caballero Navarro
 */
public class BreakCADTool extends DefaultCADTool {
	protected BreakCADToolContext _fsm;
	protected Point2D firstPoint;
	protected Point2D secondPoint;
	protected Feature rowEdited;

	/**
	 * Crea un nuevo PolylineCADTool.
	 */
	public BreakCADTool() {
	}

	/**
	 * Método de incio, para poner el código de todo lo que se requiera de una
	 * carga previa a la utilización de la herramienta.
	 */
	public void init() {
		_fsm = new BreakCADToolContext(this);
		firstPoint = null;
		secondPoint = null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.gui.cad.CADTool#transition(com.iver.cit.gvsig.fmap.layers.FBitSet,
	 *      double, double)
	 */
	public void transition(double x, double y, InputEvent event) {
		_fsm.addPoint(x, y, event);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.gui.cad.CADTool#transition(com.iver.cit.gvsig.fmap.layers.FBitSet,
	 *      double)
	 */
	public void transition(double d) {
		_fsm.addValue(d);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.gui.cad.CADTool#transition(com.iver.cit.gvsig.fmap.layers.FBitSet,
	 *      java.lang.String)
	 */
	public void transition(String s) throws CommandException {
		if (!super.changeCommand(s)) {
			_fsm.addOption(s);
		}
	}

	/**
	 * DOCUMENT ME!
	 */
	public void selection() {
		FeatureSet selection = null;
		try {
			selection = (FeatureSet) getVLE().getFeatureStore().getSelection();

			if (selection.getSize() == 0
					&& !CADExtension
							.getCADTool()
							.getClass()
							.getName()
							.equals(
									"com.iver.cit.gvsig.gui.cad.tools.SelectionCADTool")) {
				CADExtension.setCADTool("_selection", false);
				((SelectionCADTool) CADExtension.getCADTool())
						.setNextTool("_break");
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
	 * @param x
	 *            parámetro x del punto que se pase en esta transición.
	 * @param y
	 *            parámetro y del punto que se pase en esta transición.
	 */
	public void addPoint(double x, double y, InputEvent event) {
		BreakCADToolState actualState = (BreakCADToolState) _fsm
				.getPreviousState();
		String status = actualState.getName();

		if (status.equals("Break.FirstPoint")) {
			// if (rowEdited!=null &&
			// intersects(((DefaultFeature)rowEdited.getLinkedRow()).getGeometry(),new
			// Point2D.Double(x,y)))
			firstPoint = new Point2D.Double(x, y);

		} else if (status.equals("Break.SecondPoint")) {
			// if (rowEdited !=null &&
			// intersects(((DefaultFeature)rowEdited.getLinkedRow()).getGeometry(),new
			// Point2D.Double(x,y))){
			secondPoint = new Point2D.Double(x, y);
			try {
				// IGeometry
				// geom=((DefaultFeature)rowEdited.getLinkedRow()).getGeometry();
				// if (geom instanceof FGeometryCollection) {
				// breakGeometryGC(rowEdited);
				// }else {
				breakGeometry(rowEdited);
				// }
			} catch (ReadException e) {
				NotificationManager.addError(e.getMessage(), e);
			}
		}
		// }
	}

	/*
	 * private void breakGeometryGC(DefaultRowEdited dre) throws IOException,
	 * DriverIOException { GeneralPathX newGp1 = new GeneralPathX();
	 * GeneralPathX newGp2 = new GeneralPathX(); FGeometryCollection
	 * gc=(FGeometryCollection)((DefaultFeature)rowEdited.getLinkedRow()).getGeometry();
	 * IGeometry[] geoms=gc.getGeometries(); for (int i = 0;i<geoms.length;i++) {
	 * PathIterator theIterator=geoms[i].getPathIterator(null); double[] theData =
	 * new double[6]; boolean isFirstPart=true; boolean isCut=false; int
	 * theType; int numParts = 0;
	 *
	 * Point2D previous=null;
	 *
	 * while (!theIterator.isDone()) { theType =
	 * theIterator.currentSegment(theData); switch (theType) {
	 *
	 * case PathIterator.SEG_MOVETO: numParts++; previous=new
	 * Point2D.Double(theData[0], theData[1]); if (isFirstPart)
	 * newGp1.moveTo(theData[0], theData[1]); else newGp2.moveTo(theData[0],
	 * theData[1]); break;
	 *
	 * case PathIterator.SEG_LINETO: if (previous!=null){ GeneralPathX gpx=new
	 * GeneralPathX(); gpx.moveTo(previous.getX(),previous.getY());
	 * gpx.lineTo(theData[0], theData[1]); IGeometry
	 * geom=ShapeFactory.createPolyline2D(gpx); Point2D
	 * p1=getNearPoint(previous); Point2D p2=getDistantPoint(previous); if
	 * (intersects(geom,p1)){ isFirstPart=false;
	 * newGp1.lineTo(p1.getX(),p1.getY()); newGp2.moveTo(p2.getX(),p2.getY());
	 * isCut=true; } } previous=new Point2D.Double(theData[0], theData[1]); if
	 * (isFirstPart) newGp1.lineTo(theData[0], theData[1]); else
	 * newGp2.lineTo(theData[0], theData[1]); break;
	 *
	 * case PathIterator.SEG_QUADTO: if (previous!=null){ GeneralPathX gpx=new
	 * GeneralPathX(); gpx.moveTo(previous.getX(),previous.getY());
	 * gpx.quadTo(theData[0], theData[1],theData[2], theData[3]); IGeometry
	 * geom=ShapeFactory.createPolyline2D(gpx); Point2D
	 * p1=getNearPoint(previous); Point2D p2=getDistantPoint(previous); if
	 * (intersects(geom,p1)){ isFirstPart=false;
	 * newGp1.lineTo(p1.getX(),p1.getY()); newGp2.moveTo(p2.getX(),p2.getY());
	 * isCut=true; } } previous=new Point2D.Double(theData[0], theData[1]); if
	 * (isFirstPart) newGp1.quadTo(theData[0], theData[1],theData[2],
	 * theData[3]); else newGp2.quadTo(theData[0], theData[1],theData[2],
	 * theData[3]);
	 *
	 * break;
	 *
	 * case PathIterator.SEG_CUBICTO: if (previous!=null){ GeneralPathX gpx=new
	 * GeneralPathX(); gpx.moveTo(previous.getX(),previous.getY());
	 * gpx.curveTo(theData[0], theData[1],theData[2], theData[3],theData[4],
	 * theData[5]); IGeometry geom=ShapeFactory.createPolyline2D(gpx); Point2D
	 * p1=getNearPoint(previous); Point2D p2=getDistantPoint(previous); if
	 * (intersects(geom,p1)){ isFirstPart=false;
	 * newGp1.lineTo(p1.getX(),p1.getY()); newGp2.moveTo(p2.getX(),p2.getY());
	 * isCut=true; } } previous=new Point2D.Double(theData[0], theData[1]); if
	 * (isFirstPart) newGp1.curveTo(theData[0], theData[1],theData[2],
	 * theData[3],theData[4], theData[5]); else newGp2.curveTo(theData[0],
	 * theData[1],theData[2], theData[3],theData[4], theData[5]);
	 *
	 * break;
	 *
	 * case PathIterator.SEG_CLOSE: //if (isFirstPart) // newGp1.closePath();
	 * //else // newGp2.closePath(); break; } //end switch
	 *
	 * theIterator.next(); } //end while loop
	 *
	 * if (isCut) { IGeometry geom1 = ShapeFactory.createPolyline2D(newGp1);
	 * IGeometry geom2 = ShapeFactory.createPolyline2D(newGp2);
	 * VectorialLayerEdited vle = getVLE(); VectorialEditableAdapter vea =
	 * vle.getVEA(); ArrayList selectedRow = vle.getSelectedRow();
	 * vea.startComplexRow(); vea.removeRow(dre.getIndex(), getName(),
	 * EditionEvent.GRAPHIC); int num = vea.getRowCount(); if (gc.isClosed()) {
	 * ArrayList geomsAux1 = new ArrayList(); geomsAux1.add(geom2); for (int k =
	 * i + 1; k < geoms.length; k++) { geomsAux1.add(geoms[k]); } for (int k =
	 * 0; k < i; k++) { geomsAux1.add(geoms[k]); } geomsAux1.add(geom1);
	 *
	 * DefaultFeature df1 = new DefaultFeature( new
	 * FGeometryCollection((IGeometry[]) geomsAux1 .toArray(new IGeometry[0])),
	 * dre .getAttributes(), String.valueOf(num)); int index1 = vea.addRow(df1,
	 * PluginServices.getText(this, "parte1"), EditionEvent.GRAPHIC);
	 *
	 * clearSelection(); selectedRow.add(new DefaultRowEdited(df1,
	 * IRowEdited.STATUS_ADDED, index1)); vea.endComplexRow(); return; }else {
	 *
	 * ArrayList geomsAux1 = new ArrayList(); for (int k = 0; k < i; k++) {
	 * geomsAux1.add(geoms[k]); } geomsAux1.add(geom1);
	 *
	 * ArrayList geomsAux2 = new ArrayList(); geomsAux2.add(geom2); for (int k =
	 * i + 1; k < geoms.length; k++) { geomsAux2.add(geoms[k]); }
	 *
	 * DefaultFeature df1 = new DefaultFeature( new
	 * FGeometryCollection((IGeometry[]) geomsAux1 .toArray(new IGeometry[0])),
	 * dre .getAttributes(), String.valueOf(num)); int index1 = vea.addRow(df1,
	 * PluginServices.getText(this, "parte1"), EditionEvent.GRAPHIC);
	 * DefaultFeature df2 = new DefaultFeature( new
	 * FGeometryCollection((IGeometry[]) geomsAux2 .toArray(new IGeometry[0])),
	 * dre .getAttributes(), String.valueOf(num + 1)); int index2 =
	 * vea.addRow(df2, PluginServices.getText(this, "parte2"),
	 * EditionEvent.GRAPHIC); clearSelection(); selectedRow.add(new
	 * DefaultRowEdited(df2, IRowEdited.STATUS_ADDED, index2));
	 * selectedRow.add(new DefaultRowEdited(df1, IRowEdited.STATUS_ADDED,
	 * index1));
	 *
	 * vea.endComplexRow(); return; }
	 *  } } }
	 */

	private void breakGeometry(Feature dre) throws ReadException {
		breakGeom(dre);
	}

	private void breakGeom(Feature dre) throws ReadException {
		GeneralPathX newGp1 = new GeneralPathX();
		GeneralPathX newGp2 = new GeneralPathX();
		Geometry geomAux = (Geometry) rowEdited.getDefaultGeometry();
		PathIterator theIterator = geomAux.getPathIterator(null,
				Converter.FLATNESS);
		Point2D[] pointsOrdered = getOrderPoints(geomAux);
		double[] theData = new double[6];
		boolean isFirstPart = true;
		boolean intersectsP2 = false;
		int theType;
		int numParts = 0;
		boolean isBreaked = false;
		Point2D previous = null;

		while (!theIterator.isDone()) {
			theType = theIterator.currentSegment(theData);
			switch (theType) {

			case PathIterator.SEG_MOVETO:
				numParts++;

				previous = new Point2D.Double(theData[0], theData[1]);

				if (isFirstPart)
					newGp1.moveTo(theData[0], theData[1]);
				else
					newGp2.moveTo(theData[0], theData[1]);
				break;

			case PathIterator.SEG_LINETO:

				if (previous != null) {
					GeneralPathX gpx = new GeneralPathX();
					gpx.moveTo(previous.getX(), previous.getY());
					gpx.lineTo(theData[0], theData[1]);
					Geometry geom = createCurve(gpx);
					Point2D p1 = pointsOrdered[0];
					Point2D p2 = pointsOrdered[1];

					if (intersects(geom, p1) && !isBreaked) {
						isFirstPart = false;
						newGp1.lineTo(p1.getX(), p1.getY());
					}
					if (intersects(geom, p2) && !isBreaked) {
						isBreaked = true;
						intersectsP2 = true;
						newGp2.moveTo(p2.getX(), p2.getY());
					}
				}
				previous = new Point2D.Double(theData[0], theData[1]);
				if (isFirstPart)
					newGp1.lineTo(theData[0], theData[1]);
				else if (intersectsP2) {
					newGp2.lineTo(theData[0], theData[1]);
				}

				break;

			case PathIterator.SEG_QUADTO:
				if (previous != null) {
					GeneralPathX gpx = new GeneralPathX();
					gpx.moveTo(previous.getX(), previous.getY());
					gpx.quadTo(theData[0], theData[1], theData[2], theData[3]);
					Geometry geom = createCurve(gpx);
					Point2D p1 = pointsOrdered[0];
					Point2D p2 = pointsOrdered[1];
					if (intersects(geom, p1) && !isBreaked) {
						isFirstPart = false;
						newGp1.lineTo(p1.getX(), p1.getY());
					}
					if (intersects(geom, p2) && !isBreaked) {
						isBreaked = true;
						intersectsP2 = true;
						newGp2.moveTo(p2.getX(), p2.getY());

					}
				}
				previous = new Point2D.Double(theData[0], theData[1]);
				if (isFirstPart)
					newGp1.quadTo(theData[0], theData[1], theData[2],
							theData[3]);
				else
					newGp2.quadTo(theData[0], theData[1], theData[2],
							theData[3]);

				break;

			case PathIterator.SEG_CUBICTO:
				if (previous != null) {
					GeneralPathX gpx = new GeneralPathX();
					gpx.moveTo(previous.getX(), previous.getY());
					gpx.curveTo(theData[0], theData[1], theData[2], theData[3],
							theData[4], theData[5]);
					Geometry geom = createCurve(gpx);
					Point2D p1 = pointsOrdered[0];
					Point2D p2 = pointsOrdered[1];
					if (intersects(geom, p1) && !isBreaked) {
						isFirstPart = false;
						newGp1.lineTo(p1.getX(), p1.getY());
					}
					if (intersects(geom, p2) && !isBreaked) {
						isBreaked = true;
						intersectsP2 = true;
						newGp2.moveTo(p2.getX(), p2.getY());

					}
				}
				previous = new Point2D.Double(theData[0], theData[1]);
				if (isFirstPart)
					newGp1.curveTo(theData[0], theData[1], theData[2],
							theData[3], theData[4], theData[5]);
				else
					newGp2.curveTo(theData[0], theData[1], theData[2],
							theData[3], theData[4], theData[5]);

				break;

			case PathIterator.SEG_CLOSE:
				// if (isFirstPart)
				// newGp1.closePath();
				// else
				// newGp2.closePath();
				break;
			} // end switch

			theIterator.next();
		} // end while loop
		GeneralPathX gpx = new GeneralPathX();
		gpx.append(geomAux.getInternalShape(), true);
		VectorialLayerEdited vle = getVLE();
		FeatureStore featureStore = ((FLyrVect) vle.getLayer())
				.getFeatureStore();
		// VectorialEditableAdapter vea = vle.getVEA();
		ArrayList selectedRowAux = new ArrayList();
		try {
			featureStore.beginEditingGroup(getName());

			featureStore.delete(dre);
			// vea.startComplexRow();
			if (gpx.isClosed()) {

				newGp2.append(newGp1.getPathIterator(null, Converter.FLATNESS),
						true);
				Geometry geom1 = createCurve(newGp2);

				// Feature dfLine1 = dre.cloneRow();
				EditableFeature eFeature = featureStore.createNewFeature(dre
						.getType(), dre);
				eFeature.setGeometry(featureStore.getDefaultFeatureType()
						.getDefaultGeometryAttributeName(), geom1);
				selectedRowAux.add(eFeature);

			} else {
				Geometry geom1 = createCurve(newGp1);
				Geometry geom2 = createCurve(newGp2);

				// DefaultFeature dfLine1 = (DefaultFeature)
				// dre.getLinkedRow().cloneRow();
				EditableFeature eFeature = featureStore.createNewFeature(dre
						.getType(), dre);
				eFeature.setGeometry(featureStore.getDefaultFeatureType()
						.getDefaultGeometryAttributeName(), geom2);

				selectedRowAux.add(eFeature);

				// dre.editing();
				// dre.setGeometry(geom2);
				// DefaultFeature dfLine2 = (DefaultFeature)
				// dre.getLinkedRow().cloneRow();
				// dfLine2.setGeometry(geom2);
				// int indexLine2 = addGeometry(dre);

				// selectedRowAux.add(new DefaultRowEdited(dfLine2,
				// IRowEdited.STATUS_ADDED, indexLine2));

			}

			// vea.removeRow(dre.getIndex(), getName(), EditionEvent.GRAPHIC);
			// vea.endComplexRow(getName());
			featureStore.endEditingGroup();
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// vle.setSelectionCache(VectorialLayerEdited.NOTSAVEPREVIOUS,
		// selectedRowAux);

	}

	private Point2D[] getOrderPoints(Geometry geomAux) {
		PathIterator theIterator = geomAux.getPathIterator(null,
				Converter.FLATNESS);
		double[] theData = new double[6];
		Point2D previous = null;
		ArrayList points = new ArrayList();
		boolean isFirstPointBreak = false;
		boolean isSecondPointBreak = false;
		while (!theIterator.isDone()) {
			int theType = theIterator.currentSegment(theData);
			switch (theType) {

			case PathIterator.SEG_MOVETO:
				previous = new Point2D.Double(theData[0], theData[1]);
				break;

			case PathIterator.SEG_LINETO:

				if (previous != null) {
					GeneralPathX gpx = new GeneralPathX();
					gpx.moveTo(previous.getX(), previous.getY());
					gpx.lineTo(theData[0], theData[1]);
					Geometry geom = createCurve(gpx);
					boolean intersectFirst = intersects(geom, firstPoint);
					boolean intersectSecond = intersects(geom, secondPoint);
					if (intersectFirst && intersectSecond && !isFirstPointBreak) {
						isFirstPointBreak = true;
						isSecondPointBreak = true;
						points.add(getNearPoint(previous));
						points.add(getDistantPoint(previous));
						return (Point2D[]) points.toArray(new Point2D[0]);
					} else if (intersectFirst && !isFirstPointBreak) {
						isFirstPointBreak = true;
						points.add(firstPoint);
					} else if (intersectSecond && !isSecondPointBreak) {
						isSecondPointBreak = true;
						points.add(secondPoint);
					}
				}
				previous = new Point2D.Double(theData[0], theData[1]);
				break;

			case PathIterator.SEG_QUADTO:
				if (previous != null) {
					GeneralPathX gpx = new GeneralPathX();
					gpx.moveTo(previous.getX(), previous.getY());
					gpx.quadTo(theData[0], theData[1], theData[2], theData[3]);
					Geometry geom = createCurve(gpx);
					boolean intersectFirst = intersects(geom, firstPoint);
					boolean intersectSecond = intersects(geom, secondPoint);
					if (intersectFirst && intersectSecond && !isFirstPointBreak) {
						isFirstPointBreak = true;
						isSecondPointBreak = true;
						points.add(getNearPoint(previous));
						points.add(getDistantPoint(previous));
						return (Point2D[]) points.toArray(new Point2D[0]);
					} else if (intersectFirst && !isFirstPointBreak) {
						isFirstPointBreak = true;
						points.add(firstPoint);
					} else if (intersectSecond && !isSecondPointBreak) {
						isSecondPointBreak = true;
						points.add(secondPoint);
					}
				}
				previous = new Point2D.Double(theData[0], theData[1]);

				break;

			case PathIterator.SEG_CUBICTO:
				if (previous != null) {
					GeneralPathX gpx = new GeneralPathX();
					gpx.moveTo(previous.getX(), previous.getY());
					gpx.curveTo(theData[0], theData[1], theData[2], theData[3],
							theData[4], theData[5]);
					Geometry geom = createCurve(gpx);
					boolean intersectFirst = intersects(geom, firstPoint);
					boolean intersectSecond = intersects(geom, secondPoint);
					if (intersectFirst && intersectSecond && !isFirstPointBreak) {
						isFirstPointBreak = true;
						isSecondPointBreak = true;
						points.add(getNearPoint(previous));
						points.add(getDistantPoint(previous));
						return (Point2D[]) points.toArray(new Point2D[0]);
					} else if (intersectFirst && !isFirstPointBreak) {
						isFirstPointBreak = true;
						points.add(firstPoint);
					} else if (intersectSecond && !isSecondPointBreak) {
						isSecondPointBreak = true;
						points.add(secondPoint);
					}
				}
				previous = new Point2D.Double(theData[0], theData[1]);

				break;

			case PathIterator.SEG_CLOSE:
				// if (isFirstPart)
				// newGp1.closePath();
				// else
				// newGp2.closePath();
				break;
			} // end switch

			theIterator.next();
		} // end while loop

		return (Point2D[]) points.toArray(new Point2D[0]);
	}

	private Point2D getDistantPoint(Point2D previous) {
		if (firstPoint.distance(previous) > secondPoint.distance(previous)) {
			return firstPoint;
		}
		return secondPoint;
	}

	private Point2D getNearPoint(Point2D previous) {
		if (firstPoint.distance(previous) <= secondPoint.distance(previous)) {
			return firstPoint;
		}
		return secondPoint;
	}

	/**
	 * Método para dibujar la lo necesario para el estado en el que nos
	 * encontremos.
	 *
	 * @param g
	 *            Graphics sobre el que dibujar.
	 * @param x
	 *            parámetro x del punto que se pase para dibujar.
	 * @param y
	 *            parámetro x del punto que se pase para dibujar.
	 */
	public void drawOperation(Graphics g, double x, double y) {
		VectorialLayerEdited vle = getVLE();
		FeatureSet selection = null;
		try {
			selection = (FeatureSet) vle.getFeatureStore().getSelection();

			ViewPort vp = CADExtension.getEditionManager().getMapControl()
					.getViewPort();
			if (selection.getSize() == 1) {
				if (firstPoint != null) {
					Geometry g1 = createCircle(createPoint(firstPoint), vp
							.toMapDistance(3));
					Geometry g2 = createCircle(createPoint(firstPoint), vp
							.toMapDistance(5));

					DrawOperationContext doc = new DrawOperationContext();
					doc.setGraphics((Graphics2D) g);
					doc.setViewPort(vp);
					doc.setSymbol(DefaultCADTool.axisReferencesSymbol);
					try {
						g1.invokeOperation(Draw.CODE, doc);
						g2.invokeOperation(Draw.CODE, doc);
					} catch (GeometryOperationNotSupportedException e) {
						e.printStackTrace();
					} catch (GeometryOperationException e) {
						e.printStackTrace();
					}

					// g1.draw((Graphics2D)g,vp,DefaultCADTool.axisReferencesSymbol);
					// g2.draw((Graphics2D)g,vp,DefaultCADTool.axisReferencesSymbol);
				}
				rowEdited = (Feature) selection.iterator().next();
				// .getLinkedRow()
				// .cloneRow();
				Geometry geom = ((Geometry) rowEdited.getDefaultGeometry())
						.cloneGeometry();
				if (intersects(geom, new Point2D.Double(x, y))) {
					DrawOperationContext doc = new DrawOperationContext();
					doc.setGraphics((Graphics2D) g);
					doc.setViewPort(vp);
					doc.setSymbol(DefaultCADTool.geometrySelectSymbol);
					try {
						geom.invokeOperation(Draw.CODE, doc);
					} catch (GeometryOperationNotSupportedException e) {
						e.printStackTrace();
					} catch (GeometryOperationException e) {
						e.printStackTrace();
					}

					// geom.draw((Graphics2D)g,vp,DefaultCADTool.geometrySelectSymbol);
				}
			}else{
	        	if (!vle.getLayer().isVisible())
	        		return;
	        	try{
	        		Image imgSel = vle.getSelectionImage();
	        		if (imgSel!=null)
	        			g.drawImage(imgSel, 0, 0, null);
	        		Image imgHand = vle.getHandlersImage();
	        		if (imgHand!=null)
	        			g.drawImage(imgHand, 0, 0, null);
	        	}catch (Exception e) {
	        	}
	        }
		} catch (ReadException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean intersects(double x, double y) {
		Point2D p = new Point2D.Double(x, y);
		VectorialLayerEdited vle = getVLE();
		FeatureSet selection = null;
		try {
			selection = (FeatureSet) vle.getFeatureStore().getSelection();

			if (selection.getSize() == 1) {
				rowEdited = (Feature) selection.iterator().next();
				Geometry g = ((Geometry) rowEdited.getDefaultGeometry())
						.cloneGeometry();
				return intersects(g, p);
			}
		} catch (ReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	private boolean intersects(Geometry geom, Point2D p) {
		double tol = 1;
		tol = CADExtension.getEditionManager().getMapControl().getViewPort()
				.toMapDistance((int) tol);
		Rectangle2D r = new Rectangle2D.Double(p.getX() - tol / 2, p.getY()
				- tol / 2, tol, tol);
		return (geom.intersects(r) && !geom.contains(r));
	}

	/**
	 * Add a diferent option.
	 *
	 * @param s
	 *            Diferent option.
	 */
	public void addOption(String s) {
		if (s.equals(PluginServices.getText(this, "cancel")) || s.equals("c")
				|| s.equals("C")) {
			init();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.gui.cad.CADTool#addvalue(double)
	 */
	public void addValue(double d) {
	}

	public String getName() {
		return PluginServices.getText(this, "break_");
	}

	public String toString() {
		return "_break";
	}

	public boolean isApplicable(int shapeType) {
		switch (shapeType) {
		case Geometry.TYPES.GEOMETRY:
		case Geometry.TYPES.CURVE:
			return true;
		}
		return false;
	}

}
