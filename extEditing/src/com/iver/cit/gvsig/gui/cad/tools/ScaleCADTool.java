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
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.ImagingOpException;
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
import org.gvsig.fmap.geom.operation.Draw;
import org.gvsig.fmap.geom.operation.DrawOperationContext;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.operation.GeometryOperationNotSupportedException;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.gvsig.fmap.mapcontext.ViewPort;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.CADExtension;
import com.iver.cit.gvsig.gui.cad.CADTool;
import com.iver.cit.gvsig.gui.cad.DefaultCADTool;
import com.iver.cit.gvsig.gui.cad.exception.CommandException;
import com.iver.cit.gvsig.gui.cad.tools.smc.ScaleCADToolContext;
import com.iver.cit.gvsig.gui.cad.tools.smc.ScaleCADToolContext.ScaleCADToolState;
import com.iver.cit.gvsig.layers.VectorialLayerEdited;

/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class ScaleCADTool extends DefaultCADTool {
	private ScaleCADToolContext _fsm;
	private Point2D firstPoint;
	private Point2D lastPoint;
	private Point2D scalePoint;
	private Double orr;
	private Double frr;
	private Double ore;
	private Double fre;

	/**
	 * Crea un nuevo PolylineCADTool.
	 */
	public ScaleCADTool() {
	}

	/**
	 * Método de incio, para poner el código de todo lo que se requiera de una
	 * carga previa a la utilización de la herramienta.
	 */
	public void init() {
		_fsm = new ScaleCADToolContext(this);
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
						.setNextTool("_scale");
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
		ScaleCADToolState actualState = (ScaleCADToolState) _fsm
				.getPreviousState();
		String status = actualState.getName();

		if (status.equals("Scale.PointMain")) {
			firstPoint = new Point2D.Double(x, y);
			scalePoint = firstPoint;
		} else if (status.equals("Scale.ScaleFactorOrReference")) {
			PluginServices.getMDIManager().setWaitCursor();
			lastPoint = new Point2D.Double(x, y);

			// double w;
			// double h;
			// w = lastPoint.getX() - firstPoint.getX();
			// h = lastPoint.getY() - firstPoint.getY();

			try {
				double size = getCadToolAdapter().getMapControl().getViewPort()
						.toMapDistance(
								getCadToolAdapter().getMapControl().getWidth());
				scale(firstPoint.distance(lastPoint) / (size / 40));
			} catch (ReadException e) {
				NotificationManager.addError(e.getMessage(), e);
			}

			PluginServices.getMDIManager().restoreCursor();
		} else if (status.equals("Scale.PointOriginOrScaleFactor")) {
			orr = new Point2D.Double(x, y);
		} else if (status.equals("Scale.EndPointReference")) {
			frr = new Point2D.Double(x, y);
		} else if (status.equals("Scale.OriginPointScale")) {
			ore = new Point2D.Double(x, y);
			firstPoint = ore;
		} else if (status.equals("Scale.EndPointScale")) {
			fre = new Point2D.Double(x, y);

			double distrr = orr.distance(frr);
			double distre = ore.distance(fre);
			double escalado = distre / distrr;

			try {
				scale(escalado);
			} catch (ReadException e) {
				NotificationManager.addError(e.getMessage(), e);
			}
		}

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
		ScaleCADToolState actualState = _fsm.getState();
		String status = actualState.getName();
		FeatureSet selection = null;
		DisposableIterator iterator = null;
		try {
			selection = (FeatureSet) getVLE().getFeatureStore().getSelection();

			// ArrayList selectedRow = getSelectedRows();
			Point2D currentPoint = new Point2D.Double(x, y);
			long selectionCount = selection.getSize();
			if (status.equals("Scale.ScaleFactorOrReference")) {
				VectorialLayerEdited vle = getVLE();
				ViewPort vp = vle.getLayer().getMapContext().getViewPort();
				Point2D point = vp.fromMapPoint(firstPoint.getX(), firstPoint
						.getY());
				double size = getCadToolAdapter().getMapControl().getViewPort()
						.toMapDistance(
								getCadToolAdapter().getMapControl().getWidth());
				double scale = firstPoint.distance(currentPoint) / (size / 40);
				drawLine((Graphics2D) g, firstPoint, currentPoint,
						DefaultCADTool.axisReferencesSymbol);

				if (selectionCount < CADTool.TOPGEOMETRY) {
					iterator = selection.iterator();
					while (iterator.hasNext()) {
						Feature feature = (Feature) iterator.next();

						// }
						// for (int i = 0; i < selectionCount; i++) {
						// DefaultFeature fea = (DefaultFeature)
						// ((DefaultRowEdited) selectedRow
						// .get(i)).getLinkedRow();
						Geometry geometry = (feature
								.getDefaultGeometry()).cloneGeometry();

						UtilFunctions.scaleGeom(geometry, firstPoint, scale,
								scale);

						DrawOperationContext doc = new DrawOperationContext();
						doc.setGraphics((Graphics2D) g);
						doc.setViewPort(getCadToolAdapter().getMapControl()
								.getViewPort());
						doc.setSymbol(DefaultCADTool.axisReferencesSymbol);
						try {
							geometry.invokeOperation(Draw.CODE, doc);
						} catch (GeometryOperationNotSupportedException e) {
							e.printStackTrace();
						} catch (GeometryOperationException e) {
							e.printStackTrace();
						}

						// geometry.draw((Graphics2D) g, getCadToolAdapter()
						// .getMapControl().getViewPort(),
						// DefaultCADTool.axisReferencesSymbol);

					}

				} else {
					AffineTransform at = new AffineTransform();
					at.setToTranslation(point.getX(), point.getY());
					at.scale(scale, scale);
					at.translate(-point.getX(), -point.getY());
					Image imgSel = vle.getSelectionImage();
					try {
						((Graphics2D) g).drawImage(imgSel, at, null);
					} catch (ImagingOpException e) {
					}
				}
				PluginServices.getMainFrame().getStatusBar().setMessage("5",
						"Factor = " + scale);
			} else if (status.equals("Scale.EndPointScale")) {
				VectorialLayerEdited vle = getVLE();
				ViewPort vp = vle.getLayer().getMapContext().getViewPort();
				Point2D point = vp.fromMapPoint(scalePoint.getX(), scalePoint
						.getY());

				double distrr = orr.distance(frr);
				double distre = ore.distance(currentPoint);
				double escalado = distre / distrr;
				if (selectionCount < CADTool.TOPGEOMETRY) {
					iterator = selection.iterator();
					while (iterator.hasNext()) {
						Feature feature = (Feature) iterator.next();
						// for (int i = 0; i < selectedRow.size(); i++) {
						// DefaultFeature fea = (DefaultFeature)
						// ((DefaultRowEdited) selectedRow
						// .get(i)).getLinkedRow();
						Geometry geometry = (feature
								.getDefaultGeometry()).cloneGeometry();
						UtilFunctions.scaleGeom(geometry, scalePoint, escalado,
								escalado);

						DrawOperationContext doc = new DrawOperationContext();
						doc.setGraphics((Graphics2D) g);
						doc.setViewPort(getCadToolAdapter().getMapControl()
								.getViewPort());
						doc.setSymbol(DefaultCADTool.axisReferencesSymbol);
						try {
							geometry.invokeOperation(Draw.CODE, doc);
						} catch (GeometryOperationNotSupportedException e) {
							e.printStackTrace();
						} catch (GeometryOperationException e) {
							e.printStackTrace();
						}

						// geometry.draw((Graphics2D) g, getCadToolAdapter()
						// .getMapControl().getViewPort(),
						// DefaultCADTool.axisReferencesSymbol);

					}
				} else {
					AffineTransform at = new AffineTransform();
					at.setToTranslation(point.getX(), point.getY());
					at.scale(escalado, escalado);
					at.translate(-point.getX(), -point.getY());
					Image imgSel = vle.getSelectionImage();
					try {
						((Graphics2D) g).drawImage(imgSel, at, null);
					} catch (ImagingOpException e) {
					}
				}
				drawLine((Graphics2D) g, firstPoint, new Point2D.Double(x, y),
						DefaultCADTool.axisReferencesSymbol);
			}
		} catch (DataException e) {
			e.printStackTrace();
		} finally {
			if (iterator != null) {
				iterator.dispose();
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
		ScaleCADToolState actualState = (ScaleCADToolState) _fsm
				.getPreviousState();
		String status = actualState.getName();
		if (status.equals("Scale.ScaleFactorOrReference")) {
			/*
			 * try { scale(2); } catch (DriverIOException e) { // TODO
			 * Auto-generated catch block e.printStackTrace(); } catch
			 * (IOException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); }
			 */
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.gui.cad.CADTool#addvalue(double)
	 */
	public void addValue(double d) {
		ScaleCADToolState actualState = (ScaleCADToolState) _fsm
				.getPreviousState();
		String status = actualState.getName();
		if (status.equals("Scale.ScaleFactorOrReference")) {
			try {
				scale(d);
			} catch (ReadException e) {
				NotificationManager.addError(e.getMessage(), e);
			}
		}
	}

	private void scale(double scaleFactor) throws ReadException {
		VectorialLayerEdited vle = getVLE();
		DisposableIterator iterator = null;
		try {
			FeatureStore featureStore = vle.getFeatureStore();

			featureStore.beginEditingGroup(getName());
			iterator = ((FeatureSelection) featureStore.getSelection())
					.iterator();
			ArrayList selectedRowAux = new ArrayList();
			while (iterator.hasNext()) {
				Feature feature = (Feature) iterator.next();

				// }
				// for (int i = 0; i < selectedRow.size(); i++) {
				// IRowEdited edRow = (IRowEdited) selectedRow.get(i);
				// DefaultFeature fea = (DefaultFeature)
				// edRow.getLinkedRow().cloneRow();
				Geometry geometry = (feature.getDefaultGeometry())
						.cloneGeometry();
				UtilFunctions.scaleGeom(geometry, scalePoint, scaleFactor,
						scaleFactor);

				EditableFeature eFeature = feature.getEditable();
				eFeature.setGeometry(featureStore.getDefaultFeatureType()
						.getDefaultGeometryAttributeName(), geometry);
				featureStore.update(eFeature);

				// vea.modifyRow(edRow.getIndex(),
				// fea,getName(),EditionEvent.GRAPHIC);
				selectedRowAux.add(feature);
			}
			featureStore.endEditingGroup();
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (iterator != null) {
				iterator.dispose();
			}
		}
		// vle.setSelectionCache(VectorialLayerEdited.NOTSAVEPREVIOUS,
		// selectedRowAux);
		// clearSelection();
		// selectedRow.addAll(selectedRowAux);
	}

	public String getName() {
		return PluginServices.getText(this, "scale_");
	}

	public String toString() {
		return "_scale";
	}

}
