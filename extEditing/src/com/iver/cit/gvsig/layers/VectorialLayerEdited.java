package com.iver.cit.gvsig.layers;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.EmptyStackException;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureSelection;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.Geometry.TYPES;
import org.gvsig.fmap.geom.exception.CreateEnvelopeException;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.handler.Handler;
import org.gvsig.fmap.geom.operation.Draw;
import org.gvsig.fmap.geom.operation.DrawOperationContext;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.operation.GeometryOperationNotSupportedException;
import org.gvsig.fmap.geom.primitive.Circle;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.layers.CancelationException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.LayerDrawEvent;
import org.gvsig.fmap.mapcontext.layers.LayerDrawingListener;
import org.gvsig.fmap.mapcontext.layers.LayerEvent;
import org.gvsig.fmap.mapcontext.layers.vectorial.ContainsEnvelopeEvaluator;
import org.gvsig.fmap.mapcontext.layers.vectorial.ContainsGeometryEvaluator;
import org.gvsig.fmap.mapcontext.layers.vectorial.CrossEnvelopeEvaluator;
import org.gvsig.fmap.mapcontext.layers.vectorial.CrossesGeometryEvaluator;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.fmap.mapcontext.layers.vectorial.IntersectsGeometryEvaluator;
import org.gvsig.fmap.mapcontext.layers.vectorial.OutGeometryEvaluator;
import org.gvsig.fmap.mapcontext.rendering.legend.ILegend;
import org.gvsig.fmap.mapcontext.rendering.symbols.FGraphicUtilities;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.tools.evaluator.Evaluator;
import org.gvsig.tools.observer.Observable;
import org.gvsig.tools.observer.Observer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.CADExtension;
import com.iver.cit.gvsig.EditionChangeManager;
import com.iver.cit.gvsig.StartEditing;
import com.iver.cit.gvsig.ViewCommandStackExtension;
import com.iver.cit.gvsig.gui.cad.CADTool;
import com.iver.cit.gvsig.gui.cad.DefaultCADTool;
import com.iver.cit.gvsig.gui.cad.tools.SelectionCADTool;
import com.iver.cit.gvsig.gui.cad.tools.select.SelectRowPanel;
import com.iver.cit.gvsig.project.documents.view.gui.View;

public class VectorialLayerEdited extends DefaultLayerEdited implements
		LayerDrawingListener, Observer {
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private static final Logger logger = LoggerFactory.getLogger(VectorialLayerEdited.class);
	private ArrayList selectedHandler = new ArrayList();
	private Point2D lastPoint;
	private Point2D firstPoint;
	private CADTool cadtool = null;

	private ILegend legend;
	private Image imageSelection;
	private Image imageHandlers;
	private EditionChangeManager echm=null;

	public VectorialLayerEdited(FLayer lyr) {
		super(lyr);
		lyr.getMapContext().addLayerDrawingListener(this);
		// Por defecto, siempre hacemos snapping sobre la capa en edición.
		lyr.getMapContext().getLayersToSnap().add(lyr);
		try {
			((FLyrVect) lyr).getFeatureStore().addObserver(this);
		} catch (ReadException e) {
			NotificationManager.addError(e.getMessage(), e);
		}
	}

	public ArrayList getSelectedHandler() {
		return selectedHandler;
	}

	public void clearSelection() throws DataException {
		if (getFeatureStore() == null) {
			return;
		}
		FeatureSelection selection = getFeatureStore().getFeatureSelection();
		selectedHandler.clear();
		selection.deselectAll();
	}

	public void selectWithPoint(double x, double y, boolean multipleSelection) {
		try {
			firstPoint = new Point2D.Double(x, y);
			FeatureStore featureStore = getFeatureStore();
			// Se comprueba si se pincha en una gemometría
			ViewPort vp = getLayer().getMapContext().getViewPort();
			double tol = vp.toMapDistance(MapControl.tolerance);

			GeometryManager manager = GeometryLocator.getGeometryManager();
			Point center = (org.gvsig.fmap.geom.primitive.Point)manager.create(TYPES.POINT, SUBTYPES.GEOM2D);
			center.setX(x);
			center.setY(y);
			Circle circle = (Circle)geomManager.create(TYPES.CIRCLE, SUBTYPES.GEOM2D);
			circle.setPoints(center, tol);

			FeatureQuery fq = featureStore.createFeatureQuery();
			fq.setAttributeNames(new String[]{featureStore.getDefaultFeatureType().getDefaultGeometryAttributeName()});
			Evaluator evaluator=new IntersectsGeometryEvaluator(circle, vp.getProjection(), featureStore.getDefaultFeatureType(), featureStore.getDefaultFeatureType().getDefaultGeometryAttributeName());
			fq.setFilter(evaluator);
			FeatureSet set = featureStore.getFeatureSet(fq);
			if (!multipleSelection && set.getSize()>1){
				SelectRowPanel selectionPanel=new SelectRowPanel(set, this);
				PluginServices.getMDIManager().addCentredWindow(selectionPanel);
			}
			selectAndDrawGeometries(featureStore, set, vp, multipleSelection);
		} catch (ReadException e) {
			NotificationManager.addError(e.getMessage(), e);
		} catch (GeometryOperationNotSupportedException e) {
			NotificationManager.addError(e.getMessage(), e);
		} catch (GeometryOperationException e) {
			NotificationManager.addError(e.getMessage(), e);
		} catch (DataException e) {
			NotificationManager.addError(e.getMessage(), e);
		} catch (CreateGeometryException e) {
			NotificationManager.addError(e.getMessage(), e);
		}
	}

	public void selectWithSecondPoint(double x, double y) {
		try {
			FeatureStore featureStore = getFeatureStore();
			lastPoint = new Point2D.Double(x, y);
			ViewPort vp = getLayer().getMapContext().getViewPort();
			double x1;
			double y1;
			double w1;
			double h1;

			if (firstPoint.getX() < lastPoint.getX()) {
				x1 = firstPoint.getX();
				w1 = lastPoint.getX() - firstPoint.getX();
			} else {
				x1 = lastPoint.getX();
				w1 = firstPoint.getX() - lastPoint.getX();
			}

			if (firstPoint.getY() < lastPoint.getY()) {
				y1 = firstPoint.getY();
				h1 = lastPoint.getY() - firstPoint.getY();
			} else {
				y1 = lastPoint.getY();
				h1 = firstPoint.getY() - lastPoint.getY();
			}

			Envelope envelope = geomManager.createEnvelope(x1, y1, x1 + w1, y1 + h1, SUBTYPES.GEOM2D);

			FeatureQuery fq = featureStore.createFeatureQuery();
			fq.setAttributeNames(new String[]{featureStore.getDefaultFeatureType().getDefaultGeometryAttributeName()});
			Evaluator evaluator=null;
			if (firstPoint.getX() < lastPoint.getX()) {
				evaluator=new ContainsEnvelopeEvaluator(envelope, vp.getProjection(), featureStore.getDefaultFeatureType(), featureStore.getDefaultFeatureType().getDefaultGeometryAttributeName());
			}else{
				evaluator=new CrossEnvelopeEvaluator(envelope, vp.getProjection(), featureStore.getDefaultFeatureType(), featureStore.getDefaultFeatureType().getDefaultGeometryAttributeName());
			}
			fq.setFilter(evaluator);
			FeatureSet set = featureStore.getFeatureSet(fq);
			selectAndDrawGeometries(featureStore, set, vp, false);
		} catch (ReadException e) {
			NotificationManager.addError(e.getMessage(), e);
		} catch (GeometryOperationNotSupportedException e) {
			NotificationManager.addError(e.getMessage(), e);
		} catch (GeometryOperationException e) {
			NotificationManager.addError(e.getMessage(), e);
		} catch (DataException e) {
			NotificationManager.addError(e.getMessage(), e);
		} catch (CreateEnvelopeException e) {
			NotificationManager.addError(e.getMessage(), e);
		}
	}

	public void selectInsidePolygon(org.gvsig.fmap.geom.Geometry polygon) {
		try {
			FeatureStore featureStore = getFeatureStore();
			ViewPort vp = getLayer().getMapContext().getViewPort();
			FeatureQuery fq = featureStore.createFeatureQuery();
			fq.setAttributeNames(new String[]{featureStore.getDefaultFeatureType().getDefaultGeometryAttributeName()});
			Evaluator evaluator=new ContainsGeometryEvaluator(polygon, vp.getProjection(), featureStore.getDefaultFeatureType(), featureStore.getDefaultFeatureType().getDefaultGeometryAttributeName());
			fq.setFilter(evaluator);
			FeatureSet set = featureStore.getFeatureSet(fq);
			selectAndDrawGeometries(featureStore, set, vp, false);
		} catch (ReadException e) {
			NotificationManager.addError(e.getMessage(), e);
		} catch (DataException e) {
			NotificationManager.addError(e.getMessage(), e);
		} catch (GeometryOperationNotSupportedException e) {
			NotificationManager.addError(e.getMessage(), e);
		} catch (GeometryOperationException e) {
			NotificationManager.addError(e.getMessage(), e);
		}
	}
	private void selectAndDrawGeometries(FeatureStore featureStore, FeatureSet set, ViewPort vp, boolean multipleSelection) throws DataException, GeometryOperationNotSupportedException, GeometryOperationException{
		BufferedImage selectionImage = null;
		BufferedImage handlersImage = null;
//		if (!multipleSelection) {
//			clearSelection();
//		}
		if (multipleSelection && getSelectionImage()!=null && getHandlersImage()!=null) {
			selectionImage=(BufferedImage)getSelectionImage();
			handlersImage = (BufferedImage)getHandlersImage();
		}else{
			selectionImage = new BufferedImage(vp.getImageWidth(), vp.getImageHeight(), BufferedImage.TYPE_INT_ARGB);
			handlersImage = new BufferedImage(vp.getImageWidth(), vp.getImageHeight(), BufferedImage.TYPE_INT_ARGB);
		}
		Graphics2D gs = selectionImage.createGraphics();
		Graphics2D gh = handlersImage.createGraphics();
		FeatureSelection featureSelection = null;
		if (multipleSelection){
			featureSelection=(FeatureSelection)featureStore.getSelection();
			featureSelection.select(set);
		}else{
			featureSelection=featureStore.createFeatureSelection();
			featureSelection.select(set);
			featureStore.setSelection(featureSelection);
		}

		DisposableIterator iterator = null;

		try {
		iterator = set.iterator();
		DrawOperationContext doc = new DrawOperationContext();
		doc.setGraphics(gs);
		doc.setViewPort(vp);
		doc.setSymbol(DefaultCADTool.selectionSymbol);
		while (iterator.hasNext()) {
			Feature feature = (Feature) iterator.next();
			org.gvsig.fmap.geom.Geometry geom = feature
					.getDefaultGeometry();
			if (geom == null) {
				continue;
			}
			geom.cloneGeometry().invokeOperation(Draw.CODE, doc);
			drawHandlers(geom.cloneGeometry(), gh, vp);
		}
		} finally {
			if (iterator != null) {
				iterator.dispose();
			}
		}

		set.dispose();
		setSelectionImage(selectionImage);
		setHandlersImage(handlersImage);
	}
	public void selectCrossPolygon(org.gvsig.fmap.geom.Geometry polygon) {
		try {
			FeatureStore featureStore = getFeatureStore();
			ViewPort vp = getLayer().getMapContext().getViewPort();
			FeatureQuery fq = featureStore.createFeatureQuery();
			fq.setAttributeNames(new String[]{featureStore.getDefaultFeatureType().getDefaultGeometryAttributeName()});
			Evaluator evaluator=new CrossesGeometryEvaluator(polygon, vp.getProjection(), featureStore.getDefaultFeatureType(), featureStore.getDefaultFeatureType().getDefaultGeometryAttributeName());
			fq.setFilter(evaluator);
			FeatureSet set = featureStore.getFeatureSet(fq);
			selectAndDrawGeometries(featureStore,set,vp,false);
		} catch (ReadException e) {
			NotificationManager.addError(e.getMessage(), e);
		} catch (DataException e) {
			NotificationManager.addError(e.getMessage(), e);
		} catch (GeometryOperationNotSupportedException e) {
			NotificationManager.addError(e.getMessage(), e);
		} catch (GeometryOperationException e) {
			NotificationManager.addError(e.getMessage(), e);
		}
	}

	public void selectOutPolygon(org.gvsig.fmap.geom.Geometry polygon) {
		try {
			FeatureStore featureStore = getFeatureStore();
			ViewPort vp = getLayer().getMapContext().getViewPort();
			FeatureQuery fq = featureStore.createFeatureQuery();
			fq.setAttributeNames(new String[]{featureStore.getDefaultFeatureType().getDefaultGeometryAttributeName()});
			Evaluator evaluator=new OutGeometryEvaluator(polygon, vp.getProjection(), featureStore.getDefaultFeatureType(), featureStore.getDefaultFeatureType().getDefaultGeometryAttributeName());
			fq.setFilter(evaluator);
			FeatureSet set = featureStore.getFeatureSet(fq);
			selectAndDrawGeometries(featureStore, set, vp, false);

		} catch (ReadException e) {
			NotificationManager.addError(e.getMessage(), e);
		} catch (DataException e) {
			NotificationManager.addError(e.getMessage(), e);
		} catch (GeometryOperationNotSupportedException e) {
			NotificationManager.addError(e.getMessage(), e);
		} catch (GeometryOperationException e) {
			NotificationManager.addError(e.getMessage(), e);
		}
	}

	public void selectAll() {
		try {
			FeatureStore featureStore = getFeatureStore();
			ViewPort vp = getLayer().getMapContext().getViewPort();
			FeatureSet set = featureStore.getFeatureSet();
			selectAndDrawGeometries(featureStore, set, vp, false);
		} catch (ReadException e) {
			NotificationManager.addError(e.getMessage(), e);
		} catch (DataException e) {
			NotificationManager.addError(e.getMessage(), e);
		} catch (GeometryOperationNotSupportedException e) {
			NotificationManager.addError(e.getMessage(), e);
		} catch (GeometryOperationException e) {
			NotificationManager.addError(e.getMessage(), e);
		}
	}

	public void drawHandlers(org.gvsig.fmap.geom.Geometry geom, Graphics2D gs,
			ViewPort vp) {
		// if (!(getLayer() instanceof FLyrAnnotation)){
		Handler[] handlers = geom
				.getHandlers(org.gvsig.fmap.geom.Geometry.SELECTHANDLER);
		FGraphicUtilities.DrawHandlers(gs, vp.getAffineTransform(), handlers,
				DefaultCADTool.handlerSymbol);
		// }
	}

	public Image getSelectionImage() {
		return imageSelection;
	}

	public void setSelectionImage(Image image) {
		imageSelection = image;
	}

	public Image getHandlersImage() {
		return imageHandlers;
	}

	public void setHandlersImage(Image image) {
		imageHandlers = image;
	}

	public void beforeLayerDraw(LayerDrawEvent e) throws CancelationException {
		FeatureStore featureStore = null;
		try {
			featureStore = getFeatureStore();
		} catch (ReadException e2) {
			e2.printStackTrace();
		}
		if (featureStore.isEditing()) {
			ViewPort vp = getLayer().getMapContext().getViewPort();
			BufferedImage selectionImage = new BufferedImage(
					vp.getImageWidth(), vp.getImageHeight(),
					BufferedImage.TYPE_INT_ARGB);
			BufferedImage handlersImage = new BufferedImage(vp.getImageWidth(),
					vp.getImageHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D gs = selectionImage.createGraphics();
			Graphics2D gh = handlersImage.createGraphics();
			DisposableIterator iterator = null;
			try {
				iterator = featureStore.getFeatureSelection()
				.iterator();
				try {
					while (iterator.hasNext()) {
						Feature feature = (Feature) iterator.next();
						org.gvsig.fmap.geom.Geometry geom = feature
						.getDefaultGeometry();

						DrawOperationContext doc = new DrawOperationContext();
						doc.setGraphics(gs);
						doc.setViewPort(vp);
						doc.setSymbol(DefaultCADTool.selectionSymbol);
						try {
							geom.cloneGeometry()
									.invokeOperation(Draw.CODE, doc);
						} catch (GeometryOperationNotSupportedException e1) {
							e1.printStackTrace();
						} catch (GeometryOperationException e1) {
							e1.printStackTrace();
						}
						drawHandlers(geom.cloneGeometry(), gh, vp);
					}
				} catch (ConcurrentModificationException e1) {
					// throw new CancelationException(e1);
					// A mitad de pintado se cambia la selección y por tanto no
					// se puede seguir recorriendo la anterior.
					return;
				}
			} catch (DataException e2) {
				e2.printStackTrace();
			} finally {
				if (iterator != null) {
					iterator.dispose();
				}
			}

			setSelectionImage(selectionImage);
			setHandlersImage(handlersImage);
		}
	}

	public void afterLayerDraw(LayerDrawEvent e) throws CancelationException {
	}

	public void beforeGraphicLayerDraw(LayerDrawEvent e)
			throws CancelationException {
	}

	public void afterLayerGraphicDraw(LayerDrawEvent e)
			throws CancelationException {
	}

	public void activationGained(LayerEvent e) {
		if (ViewCommandStackExtension.csd != null) {
			try {
				ViewCommandStackExtension.csd.setModel(((FLyrVect) getLayer())
						.getFeatureStore());
			} catch (ReadException e1) {
				e1.printStackTrace();
			}
		}
		IWindow window = PluginServices.getMDIManager().getActiveWindow();
		if (window instanceof View) {
			View view = (View) window;
			if (e.getSource().isEditing()) {
				view.showConsole();
			}
		}
		if (cadtool != null) {
			CADExtension.getCADToolAdapter().setCadTool(cadtool);
			PluginServices.getMainFrame().setSelectedTool(cadtool.toString());
			StartEditing.startCommandsApplicable(null, (FLyrVect) getLayer());
			CADExtension.initFocus();
		}

	}

	public void activationLost(LayerEvent e) {
		try {
			cadtool = CADExtension.getCADTool();
			IWindow window = PluginServices.getMDIManager().getActiveWindow();
			if (window instanceof View) {
				View view = (View) window;
				view.hideConsole();
			}
		} catch (EmptyStackException e1) {
			cadtool = new SelectionCADTool();
			cadtool.init();
		}

	}

	public void setLegend(ILegend legend) {
		this.legend = legend;
	}

	public ILegend getLegend() {
		return legend;
	}

	public FeatureStore getFeatureStore() throws ReadException {
		return ((FLyrVect) getLayer()).getFeatureStore();
	}

	public void update(Observable observable, Object notification) {
		echm.update(observable, notification);
	}

	public EditionChangeManager getEditionChangeManager() {
		return echm;
	}

	public void setEditionChangeManager(EditionChangeManager echm) {
		this.echm = echm;
	}
}
