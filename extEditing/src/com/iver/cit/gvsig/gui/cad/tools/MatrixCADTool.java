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
import java.awt.event.InputEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.EditableFeature;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureSelection;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.handler.Handler;
import org.gvsig.fmap.geom.operation.Draw;
import org.gvsig.fmap.geom.operation.DrawOperationContext;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.operation.GeometryOperationNotSupportedException;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.mapcontext.ViewPort;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.CADExtension;
import com.iver.cit.gvsig.gui.cad.DefaultCADTool;
import com.iver.cit.gvsig.gui.cad.exception.CommandException;
import com.iver.cit.gvsig.gui.cad.panels.matrix.MatrixOperations;
import com.iver.cit.gvsig.gui.cad.panels.matrix.MatrixProperty;
import com.iver.cit.gvsig.gui.cad.tools.smc.MatrixCADToolContext;
import com.iver.cit.gvsig.gui.cad.tools.smc.MatrixCADToolContext.MatrixCADToolState;
import com.iver.cit.gvsig.layers.VectorialLayerEdited;

/**
 * Herramienta para crear una matriz de geometrías.
 *
 * @author Vicente Caballero Navarro
 */
public class MatrixCADTool extends DefaultCADTool {
	protected MatrixCADToolContext _fsm;
	protected Point2D firstPoint;
	protected Point2D secondPoint;
	protected MatrixProperty matrixProperty = null;
	protected MatrixOperations operations = null;
	protected String option;

	/**
	 * Crea un nuevo MatrixCADTool.
	 */
	public MatrixCADTool() {
		matrixProperty = new MatrixProperty();
		operations = new MatrixOperations();
	}

	/**
	 * Método de inicio, para poner el código de todo lo que se requiera de una
	 * carga previa a la utilización de la herramienta.
	 */
	public void init() {
		_fsm = new MatrixCADToolContext(this);
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
						.setNextTool("_matrix");
			} else {
				// init();
				matrixPropeties();
			}
		} catch (ReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void matrixPropeties() {
		matrixProperty.setMatrixCADTool(this);
		PluginServices.getMDIManager().addWindow(matrixProperty);
		endMatrix();

	}

	public void endMatrix() {
		if (operations.isAccepted()) {
			PluginServices.getMDIManager().setWaitCursor();

			// ArrayList selectedRow = getSelectedRows();
			ArrayList selectedRowAux = new ArrayList();
			VectorialLayerEdited vle = getVLE();
			FeatureStore featureStore = null;
			DisposableIterator iterator = null;
			try {
				featureStore = vle.getFeatureStore();

				featureStore.beginEditingGroup(getName());
				iterator = ((FeatureSelection) featureStore
						.getSelection()).iterator();
				while (iterator.hasNext()) {
					Feature feature = (Feature) iterator.next();
					// Object[] attributes=new Object[feature.size()];
					// for (int j = 0; j < feature.size(); j++) {
					// attributes[j]=feature.get(j);
					// }
					if (operations.isRectangular()) {// Si es rectangular la
														// matriz

						for (int columns = 0; columns < operations
								.getNumColumns(); columns++) {

							for (int rows = 0; rows < operations.getNumRows(); rows++) {
								if (columns == 0 && rows == 0) {
									continue;
								}

								// DefaultFeature feaCloned = (DefaultFeature)
								// fea
								// .cloneRow();
								Geometry geom = (feature
										.getDefaultGeometry()).cloneGeometry();
								Rectangle2D originalRec = geom.getBounds2D();
								Geometry g = createPoint(
										originalRec.getX()
												+ operations.getDistColumns()
												* columns, originalRec.getY()
												+ operations.getDistRows()
												* rows);
								AffineTransform at = new AffineTransform();
								at.rotate(Math.toRadians(operations
										.getRotation()), originalRec.getMinX(),
										originalRec.getMinY());
								g.transform(at);
								Point2D pDest = new Point2D.Double(g
										.getBounds2D().getX(), g.getBounds2D()
										.getY());

								double difX = pDest.getX() - originalRec.getX();
								double difY = pDest.getY() - originalRec.getY();
								Handler[] handlers = geom
										.getHandlers(Geometry.SELECTHANDLER);
								for (int j = 0; j < handlers.length; j++) {
									Handler h = handlers[j];
									Point2D p = h.getPoint();
									h.set(p.getX() + (difX), p.getY() + (difY));
								}
								EditableFeature eFeature = featureStore
										.createNewFeature(feature.getType(),
												feature);
								eFeature.setGeometry(featureStore
										.getDefaultFeatureType()
										.getDefaultGeometryAttributeName(),
										geom);
								selectedRowAux.add(eFeature);
							}

						}

					} else { // Polar

						double rotation = 360 / operations.getNum();

						for (int numElem = 0; numElem < operations.getNum(); numElem++) {
							System.out.println("numElem = " + numElem);
							if (numElem == 0) {
								continue;
							}

							// DefaultFeature feaCloned = (DefaultFeature) fea
							// .cloneRow();
							Geometry geom = (feature
									.getDefaultGeometry()).cloneGeometry();

							if (!operations.isRotateElements()) {
								Rectangle2D originalRec = geom.getBounds2D();
								Geometry g = createPoint(
										originalRec.getX(), originalRec.getY());
								AffineTransform at = new AffineTransform();
								at.rotate(Math.toRadians(rotation * numElem),
										operations.getPositionX(), operations
												.getPositionY());
								g.transform(at);
								Point2D pDest = new Point2D.Double(g
										.getBounds2D().getX(), g.getBounds2D()
										.getY());

								double difX = pDest.getX() - originalRec.getX();
								double difY = pDest.getY() - originalRec.getY();
								Handler[] handlers = geom
										.getHandlers(Geometry.SELECTHANDLER);
								for (int j = 0; j < handlers.length; j++) {
									Handler h = handlers[j];
									Point2D p = h.getPoint();
									h.set(p.getX() + (difX), p.getY() + (difY));
								}
							} else {// Cuando los elemtos rotan al mismo tiempo
									// que se van añadiendo.

								Rectangle2D originalRec = geom.getBounds2D();
								AffineTransform at = new AffineTransform();
								at.rotate(Math.toRadians(rotation * numElem),
										operations.getPositionX(), operations
												.getPositionY());
								geom.transform(at);
								Point2D pDest = new Point2D.Double(geom
										.getBounds2D().getX(), geom
										.getBounds2D().getY());

								double difX = pDest.getX() - originalRec.getX();
								double difY = pDest.getY() - originalRec.getY();
								Handler[] handlers = geom
										.getHandlers(Geometry.SELECTHANDLER);
								for (int j = 0; j < handlers.length; j++) {
									Handler h = handlers[j];
									Point2D p = h.getPoint();
									h.set(p.getX() + (difX), p.getY() + (difY));
								}
							}
							EditableFeature eFeature = featureStore
									.createNewFeature(feature.getType(),
											feature);
							eFeature.setGeometry(featureStore
									.getDefaultFeatureType()
									.getDefaultGeometryAttributeName(), geom);

							selectedRowAux.add(eFeature);
						}
					}
				}
				featureStore.endEditingGroup();
				// vle.setSelectionCache(VectorialLayerEdited.SAVEPREVIOUS,
				// selectedRowAux);
				PluginServices.getMDIManager().restoreCursor();
				end();
			} catch (DataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (iterator != null) {
					iterator.dispose();
				}
			}
		} else {// Cancelado

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
		// MatrixCADToolState actualState = _fsm.getState();
		MatrixCADToolState previousState = (MatrixCADToolState) _fsm
				.getPreviousState();
		String status = previousState.getName();
		if (status.equals("Matrix.Start") || status.equals("Matrix.FirstPoint")) {
			firstPoint = new Point2D.Double(x, y);
		} else if (status.equals("Matrix.SecondPoint")) {
			secondPoint = new Point2D.Double(x, y);
			if (option.equals("lagX") || option.equals("lagXY")) {
				operations.setDistColumns(secondPoint.getX()
						- firstPoint.getX());
				matrixProperty.refreshLagX();
			}
			if (option.equals("lagY") || option.equals("lagXY")) {
				operations.setDistRows(secondPoint.getY() - firstPoint.getY());
				matrixProperty.refreshLagY();
			}
			if (option.equals("rotation")) {

				double w;
				double h;
				w = secondPoint.getX() - firstPoint.getX();
				h = secondPoint.getY() - firstPoint.getY();
				operations.setRotation((-Math.atan2(w, h) + (Math.PI / 2))
						* 180 / Math.PI);
				matrixProperty.refreshRotation();
			}
			firstPoint = null;
			PluginServices.getMDIManager().addWindow(matrixProperty);
		}
	}

	/**
	 * Método para dibujar lo necesario para el estado en el que nos
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
		if (_fsm == null || firstPoint == null) {
			return;
		}
		GeneralPathX gpx = new GeneralPathX();
		gpx.moveTo(firstPoint.getX(), firstPoint.getY());
		gpx.lineTo(x, y);
		VectorialLayerEdited vle = getVLE();
		ViewPort vp = vle.getLayer().getMapContext().getViewPort();
		DrawOperationContext doc = new DrawOperationContext();
		doc.setGraphics((Graphics2D) g);
		doc.setViewPort(vp);
		doc.setSymbol(DefaultCADTool.axisReferencesSymbol);
		try {
			createCurve(gpx).invokeOperation(Draw.CODE, doc);
		} catch (GeometryOperationNotSupportedException e) {
			e.printStackTrace();
		} catch (GeometryOperationException e) {
			e.printStackTrace();
		}

		// geomFactory.createPolyline2D(gpx).draw((Graphics2D)g,vp,DefaultCADTool.axisReferencesSymbol);

	}

	/**
	 * Add a diferent option.
	 *
	 * @param s
	 *            Diferent option.
	 */
	public void addOption(String s) {
		option = s;
		PluginServices.getMDIManager().closeWindow(matrixProperty);
		/*
		 * MatrixCADToolState actualState = _fsm .getState(); String status =
		 * actualState.getName();
		 *
		 * if (status.equals("Matrix.LagXY")) {
		 *
		 * }else if (status.equals("Matrix.LagX")) {
		 *
		 * }else if (status.equals("Matrix.LagY")) {
		 *  }
		 */
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.gui.cad.CADTool#addvalue(double)
	 */
	public void addValue(double d) {

	}

	public String getName() {
		return PluginServices.getText(this, "matrix_");
	}

	public String toString() {
		return "_matrix";
	}

	public MatrixOperations getOperations() {
		return operations;
	}

}
