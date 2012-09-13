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
package com.iver.cit.gvsig.gui.cad;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.EditableFeature;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureSelection;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.exception.CreateGeometryException;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.Geometry.TYPES;
import org.gvsig.fmap.geom.aggregate.MultiPrimitive;
import org.gvsig.fmap.geom.exception.CreateEnvelopeException;
import org.gvsig.fmap.geom.handler.Handler;
import org.gvsig.fmap.geom.operation.Draw;
import org.gvsig.fmap.geom.operation.DrawOperationContext;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.operation.GeometryOperationNotSupportedException;
import org.gvsig.fmap.geom.primitive.Arc;
import org.gvsig.fmap.geom.primitive.Circle;
import org.gvsig.fmap.geom.primitive.Curve;
import org.gvsig.fmap.geom.primitive.Ellipse;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.primitive.Primitive;
import org.gvsig.fmap.geom.primitive.Spline;
import org.gvsig.fmap.geom.primitive.Surface;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.layers.SpatialCache;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.fmap.mapcontext.rendering.symbols.FGraphicUtilities;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbologyFactory;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.CADExtension;
import com.iver.cit.gvsig.gui.cad.exception.CommandException;
import com.iver.cit.gvsig.layers.VectorialLayerEdited;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.utiles.console.JConsole;

/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public abstract class DefaultCADTool implements CADTool {
	public static ISymbol selectionSymbol = SymbologyFactory
			.createDefaultSymbolByShapeType(Geometry.TYPES.GEOMETRY, new Color(
					255, 0, 0, 100)); // Le ponemos una transparencia
	public static ISymbol axisReferencesSymbol = SymbologyFactory
			.createDefaultSymbolByShapeType(Geometry.TYPES.GEOMETRY, new Color(
					100, 100, 100, 100));
	public static ISymbol geometrySelectSymbol = SymbologyFactory
			.createDefaultSymbolByShapeType(Geometry.TYPES.GEOMETRY, Color.RED);
	public static ISymbol handlerSymbol = SymbologyFactory
			.createDefaultSymbolByShapeType(Geometry.TYPES.GEOMETRY,
					Color.ORANGE);
	protected static Logger logger = Logger.getLogger(DefaultCADTool.class
			.getName());
	private CADToolAdapter cadToolAdapter;

	private String question;

	private String[] currentdescriptions;

	private String tool = "selection";

	private DefaultCADTool previousTool;

	private ArrayList temporalCache = new ArrayList();
	protected GeometryManager geomManager = GeometryLocator.getGeometryManager();

	public void addTemporalCache(Geometry geom) {
		temporalCache.add(geom);
		try {
			insertSpatialCache(geom);
		} catch (CreateEnvelopeException e) {
			logger.error("Error adding the spatial cache", e);
		}
	}

	public void clearTemporalCache() {
		Geometry[] geoms = (Geometry[]) temporalCache.toArray(new Geometry[0]);
		for (int i = 0; i < geoms.length; i++) {
			try {
				removeSpatialCache(geoms[i]);
			} catch (CreateEnvelopeException e) {
				logger.error("Error removing the temporal cache", e);
			}
		}
		temporalCache.clear();
	}

	private void insertSpatialCache(Geometry geom) throws CreateEnvelopeException {
		VectorialLayerEdited vle = getVLE();
		SpatialCache spatialCache = ((FLyrVect) vle.getLayer())
				.getSpatialCache();
		Envelope r = geom.getEnvelope();
		if (geom.getType() == Geometry.TYPES.POINT) {
			r = geomManager.createEnvelope(r.getMinimum(0), r.getMinimum(1), r
					.getMinimum(0) + 1, r.getMinimum(1) + 1, SUBTYPES.GEOM2D);// Rectangle2D.Double(r.getX(),r.getY(),1,1);
		}
		spatialCache.insert(r, geom);

	}

	private void removeSpatialCache(Geometry geom) throws CreateEnvelopeException {
		VectorialLayerEdited vle = getVLE();
		SpatialCache spatialCache = ((FLyrVect) vle.getLayer())
				.getSpatialCache();
		Envelope r = geom.getEnvelope();
		if (geom.getType() == Geometry.TYPES.POINT) {
			r = geomManager.createEnvelope(r.getMinimum(0), r.getMinimum(1), r
					.getMinimum(0) + 1, r.getMinimum(1) + 1, SUBTYPES.GEOM2D);// Rectangle2D.Double(r.getX(),r.getY(),1,1);
		}
		spatialCache.remove(r, geom);

	}

	/**
	 * DOCUMENT ME!
	 */
	public void draw(Geometry geometry) {
		if (geometry != null) {
			BufferedImage img = getCadToolAdapter().getMapControl().getImage();
			Graphics2D gImag = (Graphics2D) img.getGraphics();
			ViewPort vp = getCadToolAdapter().getMapControl().getViewPort();
			DrawOperationContext doc = new DrawOperationContext();
			doc.setGraphics(gImag);
			doc.setViewPort(vp);
			doc.setSymbol(DefaultCADTool.geometrySelectSymbol);
			try {
				geometry.cloneGeometry().invokeOperation(Draw.CODE, doc);
			} catch (GeometryOperationNotSupportedException e) {
				e.printStackTrace();
			} catch (GeometryOperationException e) {
				e.printStackTrace();
			}
			// geometry.draw(gImag, vp, DefaultCADTool.selectionSymbol);
		}
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param cta
	 *            DOCUMENT ME!
	 */
	public void setCadToolAdapter(CADToolAdapter cta) {
		cadToolAdapter = cta;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public CADToolAdapter getCadToolAdapter() {
		return cadToolAdapter;
	}

	public VectorialLayerEdited getVLE() {
		return (VectorialLayerEdited) CADExtension.getEditionManager()
				.getActiveLayerEdited();
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param g
	 *            DOCUMENT ME!
	 * @param firstPoint
	 *            DOCUMENT ME!
	 * @param endPoint
	 *            DOCUMENT ME!
	 */
	public void drawLine(Graphics2D g, Point2D firstPoint, Point2D endPoint,
			ISymbol symbol) {
		GeneralPathX elShape = new GeneralPathX(GeneralPathX.WIND_EVEN_ODD, 2);
		elShape.moveTo(firstPoint.getX(), firstPoint.getY());
		elShape.lineTo(endPoint.getX(), endPoint.getY());
		DrawOperationContext doc = new DrawOperationContext();
		doc.setGraphics(g);
		doc.setViewPort(getCadToolAdapter().getMapControl().getViewPort());
		doc.setSymbol(symbol);
		try {
			Curve curve = (Curve)geomManager.create(Geometry.TYPES.CURVE, Geometry.SUBTYPES.GEOM2D);
			curve.setGeneralPath(elShape);
			curve.invokeOperation(Draw.CODE, doc);
		} catch (GeometryOperationNotSupportedException e) {
			e.printStackTrace();
		} catch (GeometryOperationException e) {
			e.printStackTrace();
		} catch (org.gvsig.fmap.geom.exception.CreateGeometryException e) {
			e.printStackTrace();
		}
		// draw(g,
		// getCadToolAdapter().getMapControl().getViewPort(),
		// symbol);

	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param geometry
	 *            DOCUMENT ME!
	 */
	public Feature insertGeometry(Geometry geometry, Feature feature) {
		VectorialLayerEdited vle = getVLE();

		try {
			FeatureStore featureStore = ((FLyrVect) vle.getLayer())
					.getFeatureStore();
			EditableFeature eFeature = featureStore.createNewFeature(
					featureStore.getDefaultFeatureType(), feature);
			eFeature.setGeometry(featureStore.getDefaultFeatureType()
					.getDefaultGeometryAttributeName(), geometry);
			featureStore.insert(eFeature);
//			drawToImage(featureStore, vle, eFeature);
			insertSpatialCache(geometry);
			draw(geometry.cloneGeometry());
			return eFeature;
		} catch (ReadException e) {
			NotificationManager.addError(e.getMessage(), e);
			return null;
		} catch (DataException e) {
			NotificationManager.addError(e.getMessage(), e);
			return null;
		} catch (CreateEnvelopeException e) {
			NotificationManager.addError(e.getMessage(), e);
			return null;
		}
	}
	public Feature insertAndSelectGeometry(Geometry geometry){
		Feature feature=null;
		try{
			FeatureStore featureStore = getVLE().getFeatureStore();
			featureStore.beginComplexNotification();
			featureStore.beginEditingGroup(getName());
			FeatureSelection newSelection = featureStore
			.createFeatureSelection();
			feature=insertGeometry(geometry);
			newSelection.select(feature);
//			clearSelection();
			featureStore.setSelection(newSelection);
			featureStore.endEditingGroup();
			featureStore.endComplexNotification();
		}catch (DataException e) {
			NotificationManager.showMessageError("insertAndSelectGeoemtry", e);
		}
		return feature;
	}
	public Feature insertGeometry(Geometry geometry) {
		VectorialLayerEdited vle = getVLE();

		try {
			FeatureStore featureStore = ((FLyrVect) vle.getLayer())
					.getFeatureStore();
			EditableFeature eFeature = featureStore.createNewFeature(true);
			eFeature.setGeometry(featureStore.getDefaultFeatureType()
					.getDefaultGeometryAttributeName(), geometry);
			featureStore.insert(eFeature);

//			drawToImage(featureStore, vle, eFeature);
			insertSpatialCache(geometry);
			draw(geometry.cloneGeometry());
			return eFeature;
		} catch (ReadException e) {
			NotificationManager.addError(e.getMessage(), e);
			return null;
		} catch (DataException e) {
			NotificationManager.addError(e.getMessage(), e);
			return null;
		} catch (CreateEnvelopeException e) {
			NotificationManager.addError(e.getMessage(), e);
			return null;
		}

	}
	private void drawToImage(FeatureStore featureStore,VectorialLayerEdited vle, Feature feature) throws DataException, GeometryOperationNotSupportedException, GeometryOperationException {
		// clearSelection();
		// ViewPort vp = vle.getLayer().getMapContext().getViewPort();
//		BufferedImage selectionImage = new BufferedImage(
//				vp.getImageWidth(), vp.getImageHeight(),
//				BufferedImage.TYPE_INT_ARGB);
//		Graphics2D gs = selectionImage.createGraphics();
//		BufferedImage handlersImage = new BufferedImage(
//				vp.getImageWidth(), vp.getImageHeight(),
//				BufferedImage.TYPE_INT_ARGB);
//		Graphics2D gh = handlersImage.createGraphics();

		// int inversedIndex=vea.getInversedIndex(index);
		// FeatureSelection selection = (FeatureSelection)
		// featureStore.getSelection();
		// selection.select(feature);
		// vle.addSelectionCache(new DefaultRowEdited(df,
		// IRowEdited.STATUS_ADDED, inversedIndex ));
		// vea.getSelection().set(inversedIndex);
		// Geometry geom = (Geometry)feature.getDefaultGeometry();
//		DrawOperationContext doc = new DrawOperationContext();
//		doc.setGraphics(gs);
//		doc.setViewPort(vp);
//		doc.setSymbol(DefaultCADTool.selectionSymbol);
		Geometry geometry=feature.getDefaultGeometry();
//		geometry.cloneGeometry().invokeOperation(Draw.CODE, doc);
//		// draw(gs, vp, DefaultCADTool.selectionSymbol);
//		vle.drawHandlers(geometry.cloneGeometry(), gh, vp);
//		vle.setHandlersImage(handlersImage);
//		vle.setSelectionImage(selectionImage);
		try {
			insertSpatialCache(geometry);
		} catch (CreateEnvelopeException e) {
			logger.error("Error creating the envelope", e);
		}

	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param geometry
	 *            DOCUMENT ME!
	 */
	// public void modifyFeature(Feature feature, Feature oldFeature) {
	// try {
	// CommandsRecord
	// cr=((FLyrVect)getVLE().getLayer()).getFeatureStore().getCommandsRecord();
	// cr.update(feature, oldFeature);
	// } catch (ReadException e) {
	// NotificationManager.addError(e.getMessage(),e);
	// }
	// draw(((Geometry)feature.getDefaultGeometry()).cloneGeometry());
	// }
	/**
	 * DOCUMENT ME!
	 *
	 * @param geometry
	 *            DOCUMENT ME!
	 * @param values
	 *            DOCUMENT ME!
	 */
//	public Feature addGeometry(Geometry geometry, Object[] values) {
//		// int index = 0;
//		try {
//			FeatureStore featureStore = ((FLyrVect) getVLE().getLayer())
//					.getFeatureStore();
//			EditableFeature eFeature = featureStore.createNewFeature(true);
//			eFeature.setGeometry(featureStore.getDefaultFeatureType()
//					.getDefaultGeometryAttributeName(), geometry);
//			for (int i = 0; i < values.length; i++) {
//				eFeature.set(i, values[i]);
//			}
//			featureStore.insert(eFeature);
//			return eFeature;
//		} catch (DataException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		// try {
//		// String newFID = vea.getNewFID();
//		// DefaultFeature df = new DefaultFeature(geometry, values, newFID);
//		// index = vea.addRow(df, getName(), EditionEvent.GRAPHIC);
//		// insertSpatialCache(geometry);
//		// } catch (ValidateRowException e) {
//		// NotificationManager.addError(e);
//		// } catch (ReadException e) {
//		// NotificationManager.addError(e);
//		// }
//		// return vea.getInversedIndex(index);
//		return null;
//	}

	/**
	 * Devuelve la cadena que corresponde al estado en el que nos encontramos.
	 *
	 * @return Cadena para mostrar por consola.
	 */
	public String getQuestion() {
		return question;
	}

	/**
	 * Actualiza la cadena que corresponde al estado actual.
	 *
	 * @param s
	 *            Cadena que aparecerá en consola.
	 */
	public void setQuestion(String s) {
		question = s;
		// ConsoleToken.addQuestion(s);
	}

	/**
	 * Provoca un repintado "soft" de la capa activa en edición. Las capas por
	 * debajo de ella no se dibujan de verdad, solo se dibuja la que está en
	 * edición y las que están por encima de ella en el TOC.
	 */
	public void refresh() {
		// getCadToolAdapter().getMapControl().drawMap(false);

		getCadToolAdapter().getMapControl().rePaintDirtyLayers();
	}

	/*
	 * public void drawHandlers(Graphics g, FBitSet sel, AffineTransform at)
	 * throws DriverIOException { for (int i = sel.nextSetBit(0); i >= 0; i =
	 * sel.nextSetBit(i + 1)) { IGeometry ig =
	 * getCadToolAdapter().getVectorialAdapter() .getShape(i).cloneGeometry();
	 * if (ig == null) continue; Handler[] handlers =
	 * ig.getHandlers(IGeometry.SELECTHANDLER);
	 * FGraphicUtilities.DrawHandlers((Graphics2D) g, at, handlers); } }
	 */
	public void drawHandlers(Graphics g, ArrayList selectedRows,
			AffineTransform at) {
		FeatureSet selection = null;
		DisposableIterator iterator = null;
		try {
			selection = (FeatureSet) ((FLyrVect) getVLE().getLayer())
					.getFeatureStore().getSelection();

			iterator = selection.iterator();
			while (iterator.hasNext()) {
				Feature feature = (Feature) iterator.next();

				// }
				// for (int i = 0; i < selectedRows.size(); i++) {
				// IRowEdited edRow = (IRowEdited) selectedRows.get(i);
				// IFeature feat = (IFeature) edRow.getLinkedRow();
				// IFeature feat = (IFeature) selectedRows.get(i);
				Geometry ig = (feature.getDefaultGeometry())
						.cloneGeometry();
				if (ig == null) {
					continue;
				}
				Handler[] handlers = ig.getHandlers(Geometry.SELECTHANDLER);
				FGraphicUtilities.DrawHandlers((Graphics2D) g, at, handlers,
						DefaultCADTool.handlerSymbol);
			}

		} catch (ReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (iterator != null) {
				iterator.dispose();
			}
		}

	}

	public void setDescription(String[] currentdescriptions) {
		this.currentdescriptions = currentdescriptions;
	}

	public String[] getDescriptions() {
		return currentdescriptions;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.gui.cad.CADTool#end()
	 */
	public void end() {
		CADExtension.setCADTool("_selection", true);
		PluginServices.getMainFrame().setSelectedTool("_selection");
		CADTool cadtool = CADExtension.getCADTool();
		cadtool.setPreviosTool(this);
	}

	public void init() {
		// jaume, should not be necessary
		// CADTool.drawingSymbol.setOutlined(true);
		// CADTool.drawingSymbol.setOutlineColor(Color.GREEN);

	}

	// protected ArrayList getSelectedRows() {
	// VectorialLayerEdited vle = getVLE();
	// ArrayList selectedRow = vle.getSelectedRow();
	// return selectedRow;
	// }

	protected ArrayList getSelectedHandlers() {
		VectorialLayerEdited vle = getVLE();
		ArrayList selectedHandlers = vle.getSelectedHandler();
		return selectedHandlers;
	}

	public void clearSelection() throws DataException {
		VectorialLayerEdited vle = getVLE();
		FeatureSelection selection = null;
		selection = (FeatureSelection) ((FLyrVect) vle.getLayer())
					.getFeatureStore().getSelection();
		// ArrayList selectedRow = vle.getSelectedRow();
		ArrayList selectedHandlers = vle.getSelectedHandler();
		selection.deselectAll();
		selectedHandlers.clear();
		// VectorialEditableAdapter vea = vle.getVEA();
		// FBitSet selection = vea.getSelection();
		// selection.clear();
		vle.setSelectionImage(null);
		vle.setHandlersImage(null);

	}

	public String getNextTool() {
		return tool;
	}

	public void setNextTool(String tool) {
		this.tool = tool;
	}

	public boolean changeCommand(String name) throws CommandException {
		CADTool[] cadtools = CADExtension.getCADTools();
		for (int i = 0; i < cadtools.length; i++) {
			CADTool ct = cadtools[i];
			if (name.equalsIgnoreCase(ct.getName())
					|| name.equalsIgnoreCase(ct.toString())) {
				int type = Geometry.TYPES.POINT;
				try {
					type = ((FLyrVect) getVLE().getLayer()).getShapeType();
				} catch (ReadException e) {
					throw new CommandException(e);
				}
				if (ct.isApplicable(type)) {
					getCadToolAdapter().setCadTool(ct);
					ct.init();
					View vista = (View) PluginServices.getMDIManager()
							.getActiveWindow();
					vista.getConsolePanel().addText("\n" + ct.getName(),
							JConsole.COMMAND);
					String question = ct.getQuestion();
					vista.getConsolePanel().addText(
							"\n" + "#" + question + " > ", JConsole.MESSAGE);
					return true;
				}
				throw new CommandException(name);
			}
		}
		return false;
	}

	public boolean isApplicable(int shapeType) {
		return true;
	}

	public abstract String toString();

	public void throwValueException(String s, double d) {
		View vista = (View) PluginServices.getMDIManager().getActiveWindow();
		vista.getConsolePanel().addText(s + " : " + d, JConsole.ERROR);
	}

	public void throwOptionException(String s, String o) {
		View vista = (View) PluginServices.getMDIManager().getActiveWindow();
		vista.getConsolePanel().addText(s + " : " + o, JConsole.ERROR);
	}

	public void throwPointException(String s, double x, double y) {
		View vista = (View) PluginServices.getMDIManager().getActiveWindow();
		vista.getConsolePanel().addText(s + " : " + " X = " + x + ", Y = " + y,
				JConsole.ERROR);
	}

	public void setPreviosTool(DefaultCADTool tool) {
		previousTool = tool;
	}

	public void restorePreviousTool() {
		CADExtension.setCADTool(previousTool.toString(), true);
		PluginServices.getMainFrame().setSelectedTool(previousTool.toString());
	}

	public void endTransition(double x, double y, MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * Create a curve from a GeneralPath. If there is an
	 * error return <code>null</code> and add the error
	 * to the log
	 * @param gpx
	 * The GeneralPath
	 * @return
	 * The Curve
	 */
	protected Curve createCurve(GeneralPathX gpx){
		Curve curve = null;
		try {
			curve = (Curve)geomManager.create(TYPES.CURVE, SUBTYPES.GEOM2D);
			curve.setGeneralPath(gpx);
		} catch (org.gvsig.fmap.geom.exception.CreateGeometryException e) {
			logger.error(new CreateGeometryException(TYPES.CURVE, SUBTYPES.GEOM2D, e));
		}
		return curve;
	}

	/**
	 * Create a surface from a GeneralPath. If there is an
	 * error return <code>null</code> and add the error
	 * to the log
	 * @param gpx
	 * The general path
	 * @return
	 * The Surface
	 */
	protected Surface createSurface(GeneralPathX gpx){
		Surface surface = null;
		try {
			surface = (Surface)geomManager.create(TYPES.SURFACE, SUBTYPES.GEOM2D);
			surface.setGeneralPath(gpx);
		} catch (org.gvsig.fmap.geom.exception.CreateGeometryException e) {
			logger.error(new CreateGeometryException(TYPES.SURFACE, SUBTYPES.GEOM2D, e));
		}
		return surface;
	}

	/**
	 * Create a curve point. If there is an
	 * error return <code>null</code> and add the error
	 * to the log
	 * @param p1
	 * The AWT point
	 * @return
	 * The gvSIG point
	 */
	protected Point createPoint(Point2D p1){
		return createPoint(p1.getX(), p1.getY());
	}

	/**
	 * Create point. If there is an
	 * error return <code>null</code> and add the error
	 * to the log
	 * @param x
	 * The X coordinate
	 * @param y
	 * The y coordinate
	 * @return
	 * The Point
	 */
	protected Point createPoint(double x, double y){
		Point point = null;
		try {
			point = (Point)geomManager.create(TYPES.POINT, SUBTYPES.GEOM2D);
			point.setX(x);
			point.setY(y);
		} catch (org.gvsig.fmap.geom.exception.CreateGeometryException e) {
			logger.error(new CreateGeometryException(TYPES.POINT, SUBTYPES.GEOM2D, e));
		}
		return point;
	}

	/**
	 * Create an Arc. If there is an
	 * error return <code>null</code> and add the error
	 * to the log
	 * @param p1
	 * @param p2
	 * @param p3
	 * @return
	 * The arc
	 */
	protected Arc createArc(Point2D p1, Point2D p2, Point2D p3){
		return createArc(createPoint(p1), createPoint(p2), createPoint(p3));
	}

	/**
	 * Create an arc. If there is an
	 * error return <code>null</code> and add the error
	 * to the log
	 * @param p1
	 * @param p2
	 * @param p3
	 * @return
	 * The arc
	 */
	protected Arc createArc(Point p1, Point p2, Point p3){
		Arc arc = null;
		try {
			arc = (Arc)geomManager.create(TYPES.ARC, SUBTYPES.GEOM2D);
			arc.setPoints(p1, p2, p3);
	    } catch (org.gvsig.fmap.geom.exception.CreateGeometryException e) {
			logger.error(new CreateGeometryException(TYPES.ARC, SUBTYPES.GEOM2D, e));
		}
	    return arc;
	}

	/**
	 * Create a circle. If there is an
	 * error return <code>null</code> and add the error
	 * to the log
	 * @param p1
	 * @param p2
	 * @param p3
	 * @return
	 * The Circle
	 */
	protected Circle createCircle(Point p1, Point p2, Point p3){
		Circle circle = null;
		try {
			circle = (Circle)geomManager.create(TYPES.CIRCLE, SUBTYPES.GEOM2D);
			circle.setPoints(p1, p2, p3);
		} catch (org.gvsig.fmap.geom.exception.CreateGeometryException e) {
			logger.error(new CreateGeometryException(TYPES.CIRCLE, SUBTYPES.GEOM2D, e));
		}
		return circle;
	}

	/**
	 * Create a circle from a GeneralPath. If there is an
	 * error return <code>null</code> and add the error
	 * to the log
	 * @param p1
	 * @param p2
	 * @return
	 * The circle
	 */
	protected Circle createCircle(Point2D p1, Point2D p2){
		return createCircle(createPoint(p1), createPoint(p2));
	}

	/**
	 * Create a circle. If there is an
	 * error return <code>null</code> and add the error
	 * to the log
	 * @param p1
	 * @param p2
	 * @return
	 * The circle
	 */
	protected Circle createCircle(Point p1, Point p2){
		Circle circle = null;
		try {
			circle = (Circle)geomManager.create(TYPES.CIRCLE, SUBTYPES.GEOM2D);
			circle.setPoints(p1, p2);
		} catch (org.gvsig.fmap.geom.exception.CreateGeometryException e) {
			logger.error(new CreateGeometryException(TYPES.CIRCLE, SUBTYPES.GEOM2D, e));
		}
		return circle;
	}

	/**
	 * Create a circle. If there is an
	 * error return <code>null</code> and add the error
	 * to the log
	 * @param p1
	 * @param radious
	 * @return
	 * The Circle
	 */
	protected Circle createCircle(Point2D p1, double radious){
		return createCircle(createPoint(p1), radious);
	}

	/**
	 * Create a circle. If there is an
	 * error return <code>null</code> and add the error
	 * to the log
	 * @param p1
	 * @param radious
	 * @return
	 * The Circle
	 */
	protected Circle createCircle(Point p1, double radious){
		Circle circle = null;
		try {
			circle = (Circle)geomManager.create(TYPES.CIRCLE, SUBTYPES.GEOM2D);
			circle.setPoints(p1, radious);
		} catch (org.gvsig.fmap.geom.exception.CreateGeometryException e) {
			logger.error(new CreateGeometryException(TYPES.CIRCLE, SUBTYPES.GEOM2D, e));
		}
		return circle;
	}

	/**
	 * Create an Ellipse. If there is an
	 * error return <code>null</code> and add the error
	 * to the log
	 * @param p1
	 * @param p2
	 * @param d
	 * @return
	 * The Ellipse
	 */
	protected Ellipse createEllipse(Point2D p1, Point2D p2, double d){
		return createEllipse(createPoint(p1), createPoint(p2), d);
	}

	/**
	 * Create an Ellipse. If there is an
	 * error return <code>null</code> and add the error
	 * to the log
	 * @param p1
	 * @param p2
	 * @param d
	 * @return
	 * The Ellipse
	 */
	protected Ellipse createEllipse(Point p1, Point p2, double d){
		Ellipse ellipse = null;
		try {
			ellipse = (Ellipse)geomManager.create(TYPES.ELLIPSE, SUBTYPES.GEOM2D);
			ellipse.setPoints(p1, p2, d);
		} catch (org.gvsig.fmap.geom.exception.CreateGeometryException e) {
			logger.error(new CreateGeometryException(TYPES.ELLIPSE, SUBTYPES.GEOM2D, e));
		}
		return ellipse;
	}

	/**
	 * Create a Spline from a GeneralPath. If there is an
	 * error return <code>null</code> and add the error
	 * to the log
	 * @param points
	 * @return
	 * The Spline
	 */
	protected Spline createSpline(Point2D[] points){
		Spline spline = null;
		try {
			spline = (Spline)geomManager.create(TYPES.SPLINE, SUBTYPES.GEOM2D);
			for (int i=0 ; i<points.length ; i++){
				spline.addVertex(createPoint(points[i]));
			}
		} catch (org.gvsig.fmap.geom.exception.CreateGeometryException e) {
			logger.error(new CreateGeometryException(TYPES.SPLINE, SUBTYPES.GEOM2D, e));
		}
		return spline;
	}

	/**
	 * Create a MultiPrimitive. If there is an
	 * error return <code>null</code> and add the error
	 * to the log
	 * @param geometries
	 * @return
	 */
	protected MultiPrimitive createMultiPrimitive(Geometry[] geometries){
		MultiPrimitive multiPrimitive = null;
		try {
			multiPrimitive = (MultiPrimitive)geomManager.create(TYPES.AGGREGATE, SUBTYPES.GEOM2D);
			for (int i=0 ; i<geometries.length ; i++){
				multiPrimitive.addPrimitive((Primitive)geometries[i]);
			}
		} catch (org.gvsig.fmap.geom.exception.CreateGeometryException e) {
			logger.error(new CreateGeometryException(TYPES.SPLINE, SUBTYPES.GEOM2D, e));
		}
		return multiPrimitive;
	}
}
