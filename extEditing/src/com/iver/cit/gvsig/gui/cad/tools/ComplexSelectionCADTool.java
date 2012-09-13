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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.EditableFeature;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureSelection;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.handler.Handler;
import org.gvsig.fmap.geom.operation.Draw;
import org.gvsig.fmap.geom.operation.DrawOperationContext;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.operation.GeometryOperationNotSupportedException;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.project.document.table.gui.FeatureTableDocumentPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.CADExtension;
import com.iver.cit.gvsig.gui.cad.DefaultCADTool;
import com.iver.cit.gvsig.gui.cad.exception.CommandException;
import com.iver.cit.gvsig.gui.cad.tools.smc.ComplexSelectionCADToolContext;
import com.iver.cit.gvsig.gui.cad.tools.smc.ComplexSelectionCADToolContext.ComplexSelectionCADToolState;
import com.iver.cit.gvsig.layers.VectorialLayerEdited;

/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class ComplexSelectionCADTool extends SelectionCADTool {
	protected ComplexSelectionCADToolContext _fsm;
	protected List pointsPolygon = new ArrayList();

	/**
	 * Crea un nuevo ComplexSelectionCADTool.
	 */
	public ComplexSelectionCADTool() {
		type = PluginServices.getText(this, "inside_circle");
	}

	/**
	 * Método de incio, para poner el código de todo lo que se requiera de una
	 * carga previa a la utilización de la herramienta.
	 */
	public void init() {
		_fsm = new ComplexSelectionCADToolContext(this);
		setNextTool("complex_selection");

		setType(PluginServices.getText(this, "inside_circle"));
	}

	/**
	 * Equivale al transition del prototipo pero sin pasarle como pará metro el
	 * editableFeatureSource que ya estará creado.
	 *
	 * @param selection
	 *            Bitset con las geometrías que estén seleccionadas.
	 * @param x
	 *            parámetro x del punto que se pase en esta transición.
	 * @param y
	 *            parámetro y del punto que se pase en esta transición.
	 */
	public void addPoint(double x, double y, InputEvent event) {
		if (event != null && ((MouseEvent) event).getClickCount() == 2) {
			try {
				pointDoubleClick(((MapControl) event.getComponent())
						.getMapContext());
			} catch (ReadException e) {
				NotificationManager.addError(e.getMessage(), e);
			}
			return;
		}
		ComplexSelectionCADToolState actualState = (ComplexSelectionCADToolState) _fsm
				.getPreviousState();
		String status = actualState.getName();
		System.out.println("PREVIOUSSTATE =" + status); // + "ESTADO ACTUAL: " +
		// _fsm.getState());
		VectorialLayerEdited vle = getVLE();
		FeatureStore featureStore = null;
		try {
			featureStore = vle.getFeatureStore();
		} catch (ReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList selectedHandler = vle.getSelectedHandler();
		System.out.println("STATUS ACTUAL = " + _fsm.getTransition());
		if (status.equals("Selection.FirstPoint")) {
			firstPoint = new Point2D.Double(x, y);
			pointsPolygon.add(firstPoint);
		} else if (status.equals("Selection.SecondPoint")) {
		} else if (status.equals("Selection.WithFeatures")) {
		} else if (status.equals("Selection.WithHandlers")) {
			String description = PluginServices.getText(this, "move_handlers");
			DisposableIterator iterator = null;
			try {
				featureStore.beginEditingGroup(description);
				ArrayList selectedRowsAux = new ArrayList();

				iterator = ((FeatureSelection) featureStore.getSelection())
						.iterator();

				while (iterator.hasNext()) {
					Feature feature = (Feature) iterator.next();

					// }
					Geometry ig = (feature.getDefaultGeometry())
							.cloneGeometry();
					// Movemos los handlers que hemos seleccionado
					// previamente dentro del método select()
					Handler[] handlers = ig.getHandlers(Geometry.SELECTHANDLER);
					for (int k = 0; k < selectedHandler.size(); k++) {
						Handler h = (Handler) selectedHandler.get(k);
						for (int j = 0; j < handlers.length; j++) {
							if (h.getPoint().equals(handlers[j].getPoint())) {
								handlers[j].set(x, y);
							}
						}
					}
					EditableFeature eFeature = feature.getEditable();
					eFeature.setGeometry(featureStore.getDefaultFeatureType()
							.getDefaultGeometryAttributeName(), ig);
					featureStore.update(eFeature);
					selectedRowsAux.add(eFeature);
				}

				firstPoint = new Point2D.Double(x, y);
				// vle.setSelectionCache(VectorialLayerEdited.SAVEPREVIOUS,
				// selectedRowsAux);

				featureStore.endEditingGroup();
			} catch (DataException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} finally {
				if (iterator != null) {
					iterator.dispose();
				}
			}

		} else if (status.equals("Selection.NextPointPolygon")) {
			pointsPolygon.add(new Point2D.Double(x, y));
		}
	}

	/**
	 * Receives second point
	 *
	 * @param x
	 * @param y
	 * @return numFeatures selected
	 */
	public long selectWithSecondPointOutRectangle(double x, double y,
			InputEvent event) {
		Point2D lastPoint = new Point2D.Double(x, y);
		GeneralPathX gpx = new GeneralPathX();
		gpx.moveTo(firstPoint.getX(), firstPoint.getY());
		gpx.lineTo(lastPoint.getX(), firstPoint.getY());
		gpx.lineTo(lastPoint.getX(), lastPoint.getY());
		gpx.lineTo(firstPoint.getX(), lastPoint.getY());
		gpx.closePath();
		Geometry rectangle = createSurface(gpx);
		return selectWithPolygon(rectangle);
	}

	/**
	 * Receives second point
	 *
	 * @param x
	 * @param y
	 * @return numFeatures selected
	 */
	public long selectWithCircle(double x, double y, InputEvent event) {
		Geometry circle = createCircle(firstPoint,
				new Point2D.Double(x, y));
		return selectWithPolygon(circle);
	}

	public long selectWithPolygon(Geometry polygon) {
		VectorialLayerEdited vle = getVLE();
		PluginServices.getMDIManager().setWaitCursor();

		if (getType().equals(PluginServices.getText(this, "inside_circle"))
				|| getType().equals(
						PluginServices.getText(this, "inside_polygon"))) {
			vle.selectInsidePolygon(polygon);
		} else if (getType().equals(
				PluginServices.getText(this, "cross_circle"))
				|| getType().equals(
						PluginServices.getText(this, "cross_polygon"))) {
			vle.selectCrossPolygon(polygon);
		} else if (getType().equals(PluginServices.getText(this, "out_circle"))
				|| getType()
						.equals(PluginServices.getText(this, "out_polygon"))
				|| getType().equals(
						PluginServices.getText(this, "out_rectangle"))) {
			vle.selectOutPolygon(polygon);
		}
		long countSelection = 0;
		try {
			countSelection = ((FeatureSelection) vle.getFeatureStore()
					.getSelection()).getSize();
		} catch (ReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PluginServices.getMDIManager().restoreCursor();
		if (countSelection > 0) {
			nextState = "Selection.WithSelectedFeatures";
			end();
		} else {
			nextState = "Selection.FirstPoint";
		}
		return countSelection;
	}

	/**
	 * Método para dibujar la lo necesario para el estado en el que nos
	 * encontremos.
	 *
	 * @param g
	 *            Graphics sobre el que dibujar.
	 * @param selectedGeometries
	 *            BitSet con las geometrías seleccionadas.
	 * @param x
	 *            parámetro x del punto que se pase para dibujar.
	 * @param y
	 *            parámetro x del punto que se pase para dibujar.
	 */
	public void drawOperation(Graphics g, double x, double y) {
		ComplexSelectionCADToolState actualState = _fsm.getState();
		String status = actualState.getName();
		VectorialLayerEdited vle = getVLE();
		ArrayList selectedHandler = vle.getSelectedHandler();
		ViewPort vp = vle.getLayer().getMapContext().getViewPort();
		if (status.equals("Selection.SecondPoint")
				|| status.equals("Selection.SecondPointOutRectangle")) {
			// Dibuja el rectángulo de selección
			GeneralPathX elShape = new GeneralPathX(GeneralPathX.WIND_EVEN_ODD,
					4);
			elShape.moveTo(firstPoint.getX(), firstPoint.getY());
			elShape.lineTo(x, firstPoint.getY());
			elShape.lineTo(x, y);
			elShape.lineTo(firstPoint.getX(), y);
			elShape.lineTo(firstPoint.getX(), firstPoint.getY());

			DrawOperationContext doc = new DrawOperationContext();
			doc.setGraphics((Graphics2D) g);
			doc.setViewPort(vp);
			doc.setSymbol(DefaultCADTool.geometrySelectSymbol);
			try {
				createCurve(elShape).invokeOperation(
						Draw.CODE, doc);
			} catch (GeometryOperationNotSupportedException e) {
				e.printStackTrace();
			} catch (GeometryOperationException e) {
				e.printStackTrace();
			}
			Image img = vle.getSelectionImage();
			g.drawImage(img, 0, 0, null);
			return;
		}
		if (status.equals("Selection.SecondPointCircle")) {
			Geometry circle = createCircle(firstPoint,
					new Point2D.Double(x, y));
			GeneralPathX gpx = new GeneralPathX();
			gpx.append(circle.getInternalShape(), true);
			Geometry circleSel = createCurve(gpx);
			// Dibuja el círculo de selección
			DrawOperationContext doc = new DrawOperationContext();
			doc.setGraphics((Graphics2D) g);
			doc.setViewPort(vp);
			doc.setSymbol(DefaultCADTool.geometrySelectSymbol);
			try {
				circleSel.invokeOperation(Draw.CODE, doc);
			} catch (GeometryOperationNotSupportedException e) {
				e.printStackTrace();
			} catch (GeometryOperationException e) {
				e.printStackTrace();
			}
			Image img = vle.getSelectionImage();
			g.drawImage(img, 0, 0, null);
			return;
		} else if (status.equals("Selection.NextPointPolygon")) {
			// Dibuja el polígono de selección
			Geometry polygon = getGeometryPolygon(new Point2D.Double(x, y));
			DrawOperationContext doc = new DrawOperationContext();
			doc.setGraphics((Graphics2D) g);
			doc.setViewPort(vp);
			doc.setSymbol(DefaultCADTool.geometrySelectSymbol);
			try {
				polygon.invokeOperation(Draw.CODE, doc);
			} catch (GeometryOperationNotSupportedException e) {
				e.printStackTrace();
			} catch (GeometryOperationException e) {
				e.printStackTrace();
			}
			Image img = vle.getSelectionImage();
			g.drawImage(img, 0, 0, null);
			return;
		} else if (status.equals("Selection.WithHandlers")) {
			// Movemos los handlers que hemos seleccionado
			// previamente dentro del método select()
			double xPrev = 0;
			double yPrev = 0;
			for (int k = 0; k < selectedHandler.size(); k++) {
				Handler h = (Handler) selectedHandler.get(k);
				xPrev = h.getPoint().getX();
				yPrev = h.getPoint().getY();
				h.set(x, y);
			}
			// Y una vez movidos los vértices (handles)
			// redibujamos la nueva geometría.
			for (int i = 0; i < rowselectedHandlers.size(); i++) {
				Feature rowEd = (Feature) rowselectedHandlers.get(i);
				Geometry geom = (rowEd.getDefaultGeometry())
						.cloneGeometry();
				g.setColor(Color.gray);
				DrawOperationContext doc = new DrawOperationContext();
				doc.setGraphics((Graphics2D) g);
				doc.setViewPort(vp);
				doc.setSymbol(DefaultCADTool.axisReferencesSymbol);
				try {
					geom.invokeOperation(Draw.CODE, doc);
				} catch (GeometryOperationNotSupportedException e) {
					e.printStackTrace();
				} catch (GeometryOperationException e) {
					e.printStackTrace();
				}
			}
			for (int k = 0; k < selectedHandler.size(); k++) {
				Handler h = (Handler) selectedHandler.get(k);
				h.set(xPrev, yPrev);
			}
			return;
		} else {
			if (!vle.getLayer().isVisible()) {
				return;
			}
			try {
				Image imgSel = vle.getSelectionImage();
				if (imgSel != null) {
					g.drawImage(imgSel, 0, 0, null);
				}
				Image imgHand = vle.getHandlersImage();
				if (imgHand != null) {
					g.drawImage(imgHand, 0, 0, null);
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Add a diferent option.
	 *
	 * @param sel
	 *            DOCUMENT ME!
	 * @param s
	 *            Diferent option.
	 */
	public void addOption(String s) {
		ComplexSelectionCADToolState actualState = (ComplexSelectionCADToolState) _fsm
				.getPreviousState();
		String status = actualState.getName();
		System.out.println("PREVIOUSSTATE =" + status); // + "ESTADO ACTUAL: " +
		// _fsm.getState());
		System.out.println("STATUS ACTUAL = " + _fsm.getTransition());
		if (s.equals(PluginServices.getText(this, "cancel"))) {
			init();
			return;
		} else if (s.equals(PluginServices.getText(this, "select_all"))) {
			selectAll();
			init();
			return;
		}
		if (status.equals("Selection.FirstPoint")) {
			setType(s);
			return;
		} else if (status.equals("Selection.NextPointPolygon")) {
			if (s.equals(PluginServices.getText(this, "end_polygon"))
					|| s.equalsIgnoreCase(PluginServices.getText(this,
							"ComplexSelectionCADTool.end"))) {
				Geometry polygon = getGeometryPolygon(null);
				GeneralPathX gpx = new GeneralPathX();
				gpx.append(polygon, true);
				if (gpx.isCCW()) {
					gpx.flip();
					polygon = createSurface(gpx);
				}
				selectWithPolygon(polygon);
				pointsPolygon.clear();
				setType(PluginServices.getText(this, "inside_circle"));
				return;
			}
		}
		init();
	}

	private long selectAll() {
		VectorialLayerEdited vle = getVLE();
		PluginServices.getMDIManager().setWaitCursor();
		vle.selectAll();
		long countSelection = 0;
		try {
			countSelection = ((FeatureSelection) vle.getFeatureStore()
					.getSelection()).getSize();
		} catch (ReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PluginServices.getMDIManager().restoreCursor();
		if (countSelection > 0) {
			nextState = "Selection.WithSelectedFeatures";
		} else {
			nextState = "Selection.FirstPoint";
		}
		end();
		return countSelection;
	}

	private Geometry getGeometryPolygon(Point2D p) {
		Point2D[] points = (Point2D[]) pointsPolygon.toArray(new Point2D[0]);
		GeneralPathX gpx = new GeneralPathX();
		for (int i = 0; i < points.length; i++) {
			if (i == 0) {
				gpx.moveTo(points[i].getX(), points[i].getY());
			} else {
				gpx.lineTo(points[i].getX(), points[i].getY());
			}
		}
		if (p != null) {
			gpx.lineTo(p.getX(), p.getY());
			gpx.closePath();
			Geometry polyline = createCurve(gpx);
			return polyline;
		}
		gpx.closePath();
		Geometry polygon = createSurface(gpx);
		return polygon;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.gui.cad.CADTool#addvalue(double)
	 */
	public void addValue(double d) {
	}

	public void end() {
		if (!getNextTool().equals("complex_selection")) {
			CADExtension.setCADTool(getNextTool(), false);
		}
	}

	public String getName() {
		return PluginServices.getText(this, "complex_selection_");
	}

	public boolean selectFeatures(double x, double y, InputEvent event) {
		ComplexSelectionCADToolState actualState = _fsm.getState();

		String status = actualState.getName();
		VectorialLayerEdited vle = getVLE();

		if ((status.equals("Selection.FirstPoint"))
				|| (status.equals("Selection.WithSelectedFeatures"))) {
			PluginServices.getMDIManager().setWaitCursor();
			firstPoint = new Point2D.Double(x, y);
			vle.selectWithPoint(x, y, multipleSelection);
			PluginServices.getMDIManager().restoreCursor();
		}
		long countSelection = 0;
		try {
			countSelection = ((FeatureSelection) vle.getFeatureStore()
					.getSelection()).getSize();
		} catch (ReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (countSelection > 0) {
			nextState = "Selection.WithSelectedFeatures";
			return true;
		} else {
			{
				nextState = "Selection.SecondPoint";
				return true;
			}
		}
	}

	public int selectHandlers(double x, double y, InputEvent event) {
		Point2D auxPoint = new Point2D.Double(x, y);

		VectorialLayerEdited vle = getVLE();
		ArrayList selectedHandler = vle.getSelectedHandler();
		selectedHandler.clear();

		// Se comprueba si se pincha en una gemometría
		PluginServices.getMDIManager().setWaitCursor();

		double tam = getCadToolAdapter().getMapControl().getViewPort()
				.toMapDistance(MapControl.tolerance);

		Handler[] handlers = null;
		rowselectedHandlers.clear();
		FeatureStore featureStore = null;
		try {
			featureStore = vle.getFeatureStore();
		} catch (ReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DisposableIterator iterator = null;
		try {
			iterator = ((FeatureSelection) featureStore.getSelection())
			.iterator();
			while (iterator.hasNext()) {
				Feature feature = (Feature) iterator.next();

				Geometry geom = (feature.getDefaultGeometry()).cloneGeometry();
				handlers = geom.getHandlers(Geometry.SELECTHANDLER);
				// y miramos los handlers de cada entidad seleccionada
				double min = tam;

				for (int j = 0; j < handlers.length; j++) {
					Point2D handlerPoint = handlers[j].getPoint();
					double distance = auxPoint.distance(handlerPoint);
					if (distance <= min) {
						min = distance;
						selectedHandler.add(handlers[j]);
						EditableFeature eFeature;
						try {
							eFeature = featureStore.createNewFeature(false);
							FeatureType featureType = featureStore
									.getDefaultFeatureType();
							for (int i = 0; i < featureType.size(); i++) {
								eFeature.set(i, feature.get(i));
							}
							eFeature.setGeometry(featureType
									.getDefaultGeometryAttributeName(), geom);
						} catch (InitializeException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (DataException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						rowselectedHandlers.add(feature);
					}
				}
			}
		} catch (DataException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			if (iterator != null) {
				iterator.dispose();
			}
		}

		PluginServices.getMDIManager().restoreCursor();

		int numHandlesSelected = selectedHandler.size();

		/*
		 * if (numHandlesSelected == 0) selectFeatures(x,y);
		 */

		return numHandlesSelected;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		if (type.equalsIgnoreCase(PluginServices.getText(this,
				"ComplexSelectionCADTool.outrectangle"))) {
			this.type = PluginServices.getText(this, "out_rectangle");
		} else if (type.equalsIgnoreCase(PluginServices.getText(this,
				"ComplexSelectionCADTool.intropolygon"))) {
			this.type = PluginServices.getText(this, "inside_polygon");
		} else if (type.equalsIgnoreCase(PluginServices.getText(this,
				"ComplexSelectionCADTool.crosspolygon"))) {
			this.type = PluginServices.getText(this, "cross_polygon");
		} else if (type.equalsIgnoreCase(PluginServices.getText(this,
				"ComplexSelectionCADTool.outpolygon"))) {
			this.type = PluginServices.getText(this, "out_polygon");
		} else if (type.equalsIgnoreCase(PluginServices.getText(this,
				"ComplexSelectionCADTool.introcircle"))) {
			this.type = PluginServices.getText(this, "inside_circle");
		} else if (type.equalsIgnoreCase(PluginServices.getText(this,
				"ComplexSelectionCADTool.crosscircle"))) {
			this.type = PluginServices.getText(this, "cross_circle");
		} else if (type.equalsIgnoreCase(PluginServices.getText(this,
				"ComplexSelectionCADTool.outcircle"))) {
			this.type = PluginServices.getText(this, "out_circle");
		} else if (type.equals(PluginServices.getText(this, "select_all"))) {
			selectAll();
			init();
		} else {
			this.type = type;
		}
		pointsPolygon.clear();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.gui.cad.CADTool#transition(com.iver.cit.gvsig.fmap.layers.FBitSet,
	 *      double, double)
	 */
	public void transition(double x, double y, InputEvent event) {
		System.out.println("TRANSICION DESDE ESTADO " + _fsm.getState()
				+ " x= " + x + " y=" + y);
		try {
			_fsm.addPoint(x, y, event);
		} catch (Exception e) {
			init();
		}
		System.out.println("ESTADO ACTUAL: " + getStatus());

		FLyrVect lv = (FLyrVect) ((VectorialLayerEdited) CADExtension
				.getEditionManager().getActiveLayerEdited()).getLayer();
		com.iver.andami.ui.mdiManager.IWindow[] views = PluginServices
				.getMDIManager().getAllWindows();

		for (int i = 0; i < views.length; i++) {
			if (views[i] instanceof FeatureTableDocumentPanel) {
				FeatureTableDocumentPanel table = (FeatureTableDocumentPanel) views[i];
				if (table.getModel().getAssociatedLayer() != null
						&& table.getModel().getAssociatedLayer().equals(lv)) {
					table.updateSelection();
				}
			}
		}
	}

	public String getStatus() {
		try {
			ComplexSelectionCADToolState actualState = (ComplexSelectionCADToolState) _fsm
					.getPreviousState();
			String status = actualState.getName();

			return status;
		} catch (NullPointerException e) {
			return "Selection.FirstPoint";
		}
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

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.gui.cad.CADTool#transition(com.iver.cit.gvsig.fmap.layers.FBitSet,
	 *      double)
	 */
	public void transition(double d) {
		_fsm.addValue(d);
	}

	public String toString() {
		return "_complex_selection";
	}

	public String getNextState() {
		return nextState;
	}

}
