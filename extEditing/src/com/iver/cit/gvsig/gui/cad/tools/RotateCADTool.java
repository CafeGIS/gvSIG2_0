/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
 *   Av. Blasco Ib��ez, 50
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
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.EditableFeature;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.gvsig.fmap.mapcontext.ViewPort;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.CADExtension;
import com.iver.cit.gvsig.gui.cad.DefaultCADTool;
import com.iver.cit.gvsig.gui.cad.exception.CommandException;
import com.iver.cit.gvsig.gui.cad.tools.smc.RotateCADToolContext;
import com.iver.cit.gvsig.gui.cad.tools.smc.RotateCADToolContext.RotateCADToolState;
import com.iver.cit.gvsig.layers.VectorialLayerEdited;

/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class RotateCADTool extends DefaultCADTool {
	private RotateCADToolContext _fsm;
	private Point2D firstPoint;
	private Point2D lastPoint;

	/**
	 * Crea un nuevo PolylineCADTool.
	 */
	public RotateCADTool() {
	}

	/**
	 * M�todo de incio, para poner el c�digo de todo lo que se requiera de una
	 * carga previa a la utilizaci�n de la herramienta.
	 */
	public void init() {
		_fsm = new RotateCADToolContext(this);
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
						.setNextTool("_rotate");
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
	 * Equivale al transition del prototipo pero sin pasarle como par�metro el
	 * editableFeatureSource que ya estar� creado.
	 *
	 * @param x
	 *            par�metro x del punto que se pase en esta transici�n.
	 * @param y
	 *            par�metro y del punto que se pase en esta transici�n.
	 */
	public void addPoint(double x, double y, InputEvent event) {
		RotateCADToolState actualState = (RotateCADToolState) _fsm
				.getPreviousState();
		String status = actualState.getName();
		VectorialLayerEdited vle = getVLE();
		FeatureStore featureStore = null;
		try {
			featureStore = vle.getFeatureStore();
		} catch (ReadException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		DisposableIterator iterator = null;
		try {
			FeatureSet selection = (FeatureSet) featureStore.getSelection();// getSelectedRows();
			ArrayList selectedRowAux = new ArrayList();

			if (status.equals("Rotate.PointMain")) {
				firstPoint = new Point2D.Double(x, y);
			} else if (status.equals("Rotate.AngleOrPoint")) {
				PluginServices.getMDIManager().setWaitCursor();
				lastPoint = new Point2D.Double(x, y);

				double w;
				double h;
				w = lastPoint.getX() - firstPoint.getX();
				h = lastPoint.getY() - firstPoint.getY();

				featureStore.beginEditingGroup(getName());
				try {
					iterator = selection.iterator();
					while (iterator.hasNext()) {
						Feature feature = (Feature) iterator.next();

						// }
						// for (int i = 0; i < selectedRow.size(); i++) {
						// DefaultRowEdited row=(DefaultRowEdited)
						// selectedRow.get(i);
						// DefaultFeature fea = (DefaultFeature)
						// row.getLinkedRow().cloneRow();
						// Rotamos la geometry
						Geometry geometry = (feature.getDefaultGeometry())
								.cloneGeometry();
						EditableFeature eFeature = feature.getEditable();
						UtilFunctions.rotateGeom(geometry, -Math.atan2(w, h)
								+ (Math.PI / 2), firstPoint.getX(), firstPoint
								.getY());
						eFeature.setGeometry(featureStore
								.getDefaultFeatureType()
								.getDefaultGeometryAttributeName(), geometry);
						featureStore.update(eFeature);
						// vea.modifyRow(row.getIndex(), fea,
						// getName(),EditionEvent.GRAPHIC);
						// selectedRowAux.add(new
						// DefaultRowEdited(fea,IRowEdited.STATUS_MODIFIED,row.getIndex()));
					}
				} finally {

					featureStore.endEditingGroup();
				}
				// vle.setSelectionCache(VectorialLayerEdited.NOTSAVEPREVIOUS,
				// selectedRowAux);
				// clearSelection();
				// selectedRow.addAll(selectedRowAux);

				PluginServices.getMDIManager().restoreCursor();
			}
		} catch (DataException e) {
			NotificationManager.addError(e.getMessage(), e);
		} finally {
			if (iterator != null) {
				iterator.dispose();
			}
		}
	}

	/**
	 * M�todo para dibujar la lo necesario para el estado en el que nos
	 * encontremos.
	 *
	 * @param g
	 *            Graphics sobre el que dibujar.
	 * @param x
	 *            par�metro x del punto que se pase para dibujar.
	 * @param y
	 *            par�metro x del punto que se pase para dibujar.
	 */
	public void drawOperation(Graphics g, double x, double y) {
		RotateCADToolState actualState = _fsm.getState();
		String status = actualState.getName();
		VectorialLayerEdited vle = getVLE();
		// ArrayList selectedRow=getSelectedRows();

		// drawHandlers(g, selectedRow,
		// getCadToolAdapter().getMapControl().getViewPort()
		// .getAffineTransform());
		if (status.equals("Rotate.AngleOrPoint")) {
			double w;
			double h;
			w = x - firstPoint.getX();
			h = y - firstPoint.getY();
			ViewPort vp = vle.getLayer().getMapContext().getViewPort();
			Point2D point = vp.fromMapPoint(firstPoint.getX(), firstPoint
					.getY());
			AffineTransform at = AffineTransform.getRotateInstance(Math.atan2(
					w, h)
					- (Math.PI / 2), (int) point.getX(), (int) point.getY());

			Image imgSel = vle.getSelectionImage();
			((Graphics2D) g).drawImage(imgSel, at, null);
			drawLine((Graphics2D) g, firstPoint, new Point2D.Double(x, y),
					DefaultCADTool.axisReferencesSymbol);
			// /AffineTransform at =
			// AffineTransform.getRotateInstance(+Math.atan2(
			// / w, h) - (Math.PI / 2), (int) point.getX(),
			// / (int) point.getY());
			// /Image img =
			// getCadToolAdapter().getVectorialAdapter().getImage();

			// /((Graphics2D) g).drawImage(img, at, null);

			// /drawLine((Graphics2D) g, firstPoint, new Point2D.Double(x, y));

			/*
			 * for (int i = 0; i < selectedRow.size(); i++) { // IGeometry
			 * geometry = //
			 * getCadToolAdapter().getVectorialAdapter().getShape(i); IRowEdited
			 * edRow = (IRowEdited) selectedRow.get(i); IFeature feat =
			 * (IFeature) edRow.getLinkedRow(); IGeometry geometry =
			 * feat.getGeometry().cloneGeometry(); // Rotamos la geometry
			 * UtilFunctions.rotateGeom(geometry, -Math.atan2(w, h) + Math.PI /
			 * 2, firstPoint.getX(), firstPoint.getY());
			 *
			 * geometry.draw((Graphics2D) g, getCadToolAdapter()
			 * .getMapControl().getViewPort(), CADTool.drawingSymbol);
			 * GeneralPathX elShape = new GeneralPathX(
			 * GeneralPathX.WIND_EVEN_ODD, 2); elShape.moveTo(firstPoint.getX(),
			 * firstPoint.getY()); elShape.lineTo(x, y);
			 * ShapeFactory.createPolyline2D(elShape).draw((Graphics2D) g,
			 * getCadToolAdapter().getMapControl().getViewPort(),
			 * CADTool.drawingSymbol); }
			 */
		} else {
			if (!vle.getLayer().isVisible()) {
				return;
			}
			Image imgSel = vle.getSelectionImage();
			if (imgSel != null) {
				g.drawImage(imgSel, 0, 0, null);
			}
			Image imgHand = vle.getHandlersImage();
			if (imgHand != null) {
				g.drawImage(imgHand, 0, 0, null);
			}
		}
	}

	/**
	 * Add a diferent option.
	 *
	 * @param s
	 *            Diferent option.
	 */
	public void addOption(String s) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.gui.cad.CADTool#addvalue(double)
	 */
	public void addValue(double d) {
		RotateCADToolState actualState = (RotateCADToolState) _fsm
				.getPreviousState();
		String status = actualState.getName();
		VectorialLayerEdited vle = getVLE();
		FeatureStore featureStore = null;
		try {
			featureStore = vle.getFeatureStore();
		} catch (ReadException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		DisposableIterator iterator = null;
		try {
			FeatureSet selection = (FeatureSet) featureStore.getSelection();// getSelectedRows();

			if (status.equals("Rotate.AngleOrPoint")) {

				featureStore.beginEditingGroup(getName());
				try {
					// /ArrayList selectedRowAux=new ArrayList();
					iterator = selection.iterator();
					while (iterator.hasNext()) {
						Feature feature = (Feature) iterator.next();

						// }
						// for (int i = 0; i < selectedRow.size(); i++) {
						// DefaultRowEdited row=(DefaultRowEdited)
						// selectedRow.get(i);
						// DefaultFeature fea = (DefaultFeature)
						// row.getLinkedRow().cloneRow();
						// Rotamos la geometry
						AffineTransform at = new AffineTransform();
						at.rotate(Math.toRadians(d), firstPoint.getX(),
								firstPoint.getY());
						Geometry geometry = (feature.getDefaultGeometry())
								.cloneGeometry();
						EditableFeature eFeature = feature.getEditable();
						geometry.transform(at);
						eFeature.setGeometry(featureStore
								.getDefaultFeatureType()
								.getDefaultGeometryAttributeName(), geometry);
						featureStore.update(eFeature);
						// vea.modifyRow(row.getIndex(),
						// fea,getName(),EditionEvent.GRAPHIC);
						// /selectedRowAux.add(new
						// DefaultRowEdited(fea,IRowEdited.STATUS_MODIFIED,index));
					}
				} finally {
					featureStore.endEditingGroup();
				}
				clearSelection();
				// /selectedRow=selectedRowAux;

			}
		} catch (DataException e) {
			NotificationManager.addError(e.getMessage(), e);
		} finally {
			if (iterator != null) {
				iterator.dispose();
			}
		}
	}

	public String getName() {
		return PluginServices.getText(this, "rotate_");
	}

	public String toString() {
		return "_rotate";
	}

}
