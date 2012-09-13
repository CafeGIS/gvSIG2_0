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
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.EditableFeature;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.exception.NeedEditingModeException;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.handler.Handler;
import org.gvsig.fmap.geom.operation.Draw;
import org.gvsig.fmap.geom.operation.DrawOperationContext;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.operation.GeometryOperationNotSupportedException;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.project.document.table.gui.FeatureTableDocumentPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.CADExtension;
import com.iver.cit.gvsig.gui.cad.DefaultCADTool;
import com.iver.cit.gvsig.gui.cad.exception.CommandException;
import com.iver.cit.gvsig.gui.cad.tools.smc.SelectionCADToolContext;
import com.iver.cit.gvsig.gui.cad.tools.smc.SelectionCADToolContext.SelectionCADToolState;
import com.iver.cit.gvsig.layers.VectorialLayerEdited;

/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class SelectionCADTool extends DefaultCADTool {
//	public static int tolerance = 4;

	protected SelectionCADToolContext _fsm;

	protected Point2D firstPoint;

	protected String nextState;
// Registros de los que se ha seleccionado algún handler.
	protected ArrayList rowselectedHandlers=new ArrayList();
	protected String type=PluginServices.getText(this,"simple");
//	protected ArrayList pointsPolygon=new ArrayList();

	protected boolean multipleSelection=false;
	/**
	 * Crea un nuevo SelectionCADTool.
	 */
	public SelectionCADTool() {
	}
	/**
	 * Método de incio, para poner el código de todo lo que se requiera de una
	 * carga previa a la utilización de la herramienta.
	 */
	public void init() {
		_fsm = new SelectionCADToolContext(this);
		setNextTool("selection");
		setType(PluginServices.getText(this,"simple"));
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
		try{
		_fsm.addPoint(x, y, event);
		}catch (Exception e) {
			init();
			PluginServices.getMDIManager().restoreCursor();
		}
		System.out.println("ESTADO ACTUAL: " + getStatus());

		// ESTO LO QUITO POR AHORA, PERO PUEDE QUE LO NECESITEMOS VOLVER A PONER.
		// Lo he quitado porque cuando seleccionas algo con CAD, molesta que
		// te hagan un redibujado.
		FLyrVect lv=(FLyrVect)((VectorialLayerEdited)CADExtension.getEditionManager().getActiveLayerEdited()).getLayer();
		//lv.getSource().getRecordset().getSelectionSupport().fireSelectionEvents();
		com.iver.andami.ui.mdiManager.IWindow[] views = PluginServices.getMDIManager().getAllWindows();

		for (int i=0 ; i<views.length ; i++){
			if (views[i] instanceof FeatureTableDocumentPanel){
				FeatureTableDocumentPanel table=(FeatureTableDocumentPanel)views[i];
				if (table.getModel().getAssociatedLayer()!=null && table.getModel().getAssociatedLayer().equals(lv)) {
					table.updateSelection();
				}
			}
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

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.gui.cad.CADTool#transition(com.iver.cit.gvsig.fmap.layers.FBitSet,
	 *      java.lang.String)
	 */
	public void transition(String s) throws CommandException {
		if (!super.changeCommand(s)){
			_fsm.addOption(s);
    	}
	}

	public String getNextState() {
		return nextState;
	}

	protected void pointDoubleClick(MapContext map) throws ReadException {
		FLayer[] actives = map
           .getLayers().getActives();
//        for (int i=0; i < actives.length; i++){
//            if (actives[i] instanceof FLyrAnnotation && actives[i].isEditing()) {
//                FLyrAnnotation lyrAnnotation = (FLyrAnnotation) actives[i];
//               	lyrAnnotation.setSelectedEditing();
//               	lyrAnnotation.setInEdition(lyrAnnotation.getRecordset().getSelection().nextSetBit(0));
//               	FLabel fl=lyrAnnotation.getLabel(lyrAnnotation.getInEdition());
//               	if (fl!=null){
//               		View vista=(View)PluginServices.getMDIManager().getActiveWindow();
//       				TextFieldEdit tfe=new TextFieldEdit(lyrAnnotation);
//        			tfe.show(vista.getMapControl().getViewPort().fromMapPoint(fl.getOrig()),vista.getMapControl());
//       			}
//            }
//        }
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
//		if (event!=null && ((MouseEvent)event).getClickCount()==2){
//			try {
//				pointDoubleClick((MapControl)event.getComponent());
//			} catch (ReadException e) {
//				NotificationManager.addError(e.getMessage(),e);
//			}
//			return;
//		}
		SelectionCADToolState actualState = (SelectionCADToolState) _fsm
		.getPreviousState();
		String status = actualState.getName();
		System.out.println("PREVIOUSSTATE =" + status); // + "ESTADO ACTUAL: " +
		// _fsm.getState());
		VectorialLayerEdited vle = getVLE();
		FeatureStore featureStore=null;
		DisposableIterator iterator = null;
		try {
			featureStore = vle.getFeatureStore();

			ArrayList selectedHandler = vle.getSelectedHandler();
			FeatureSet selection = (FeatureSet) featureStore.getSelection();// vle.getSelectedRow();
			System.out.println("STATUS ACTUAL = " + _fsm.getTransition());
			if (status.equals("Selection.FirstPoint")) {
				firstPoint = new Point2D.Double(x, y);
				// pointsPolygon.add(firstPoint);
			} else if (status.equals("Selection.SecondPoint")) {
			} else if (status.equals("Selection.WithFeatures")) {
			} else if (status.equals("Selection.WithHandlers")) {
				String description = PluginServices.getText(this,
				"move_handlers");
				featureStore.beginEditingGroup(description);
				try {
					iterator = selection.iterator();
					while (iterator.hasNext()) {
						Feature feature = (Feature) iterator.next();
						// }
						// for (int i = 0; i < selectedRow.size(); i++) {
						// IRowEdited row = (IRowEdited) selectedRow.get(i);
						// IFeature feat = (IFeature)
						// row.getLinkedRow().cloneRow();
						Geometry ig = feature.getDefaultGeometry()
								.cloneGeometry();
						// if (vea instanceof AnnotationEditableAdapter) {
						// // Movemos la geometría
						// UtilFunctions.moveGeom(ig, x -
						// firstPoint.getX(), y - firstPoint.getY());
						// }else {
						// Movemos los handlers que hemos seleccionado
						// previamente dentro del método select()
						Handler[] handlers = ig
								.getHandlers(Geometry.SELECTHANDLER);
						for (int k = 0; k < selectedHandler.size(); k++) {
							Handler h = (Handler) selectedHandler.get(k);
							for (int j = 0; j < handlers.length; j++) {
								if (h.getPoint().equals(handlers[j].getPoint())) {
									handlers[j].set(x, y);
								}
							}
						}
						// }
						EditableFeature eFeature = feature.getEditable();
						eFeature.setGeometry(featureStore
								.getDefaultFeatureType()
								.getDefaultGeometryAttributeName(), ig);
						featureStore.update(eFeature);
						// modifyFeature(feature, feature);
						// selectedRowsAux.add(new
						// DefaultRowEdited(feat,IRowEdited.STATUS_MODIFIED,row.getIndex()));
					}
					firstPoint = new Point2D.Double(x, y);
					// vle.setSelectionCache(VectorialLayerEdited.SAVEPREVIOUS,
					// selectedRowsAux);
					// clearSelection();
					// selectedRow.addAll(selectedRowsAux);

				} finally {
					featureStore.endEditingGroup();
				}
			}
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (iterator != null) {
				iterator.dispose();
			}
		}
	}

	/**
	 * Receives second point
	 * @param x
	 * @param y
	 * @return numFeatures selected
	 */
	public long selectWithSecondPoint(double x, double y, InputEvent event) {
		VectorialLayerEdited vle = getVLE();
		PluginServices.getMDIManager().setWaitCursor();
		vle.selectWithSecondPoint(x,y);
		FeatureSet selection=null;
		try {
			selection = (FeatureSet)vle.getFeatureStore().getSelection();
			PluginServices.getMDIManager().restoreCursor();
			long countSel=selection.getSize();
			if (countSel > 0) {
				nextState = "Selection.WithSelectedFeatures";
			} else {
				nextState = "Selection.FirstPoint";
			}
			return countSel;
		} catch (ReadException e) {
			e.printStackTrace();
			return 0;
		} catch (DataException e) {
			e.printStackTrace();
			return 0;
		}
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
		SelectionCADToolState actualState = _fsm.getState();
		String status = actualState.getName();
		VectorialLayerEdited vle = getVLE();
		if (vle == null) {
			return;
		}
		ArrayList selectedHandler = vle.getSelectedHandler();
		ViewPort vp=vle.getLayer().getMapContext().getViewPort();
		if (status.equals("Selection.SecondPoint")) {
			// Dibuja el rectángulo de selección
			GeneralPathX elShape = new GeneralPathX(GeneralPathX.WIND_EVEN_ODD,
					4);
			elShape.moveTo(firstPoint.getX(), firstPoint.getY());
			elShape.lineTo(x, firstPoint.getY());
			elShape.lineTo(x, y);
			elShape.lineTo(firstPoint.getX(), y);
			elShape.lineTo(firstPoint.getX(), firstPoint.getY());
			DrawOperationContext doc=new DrawOperationContext();
			doc.setGraphics((Graphics2D)g);
			doc.setViewPort(vp);
			doc.setSymbol(DefaultCADTool.geometrySelectSymbol);
        	try {
        		createCurve(elShape).cloneGeometry().invokeOperation(Draw.CODE,doc);
			} catch (GeometryOperationNotSupportedException e) {
				e.printStackTrace();
			} catch (GeometryOperationException e) {
				e.printStackTrace();
			}
//			geomFactory.createPolyline2D(elShape).draw((Graphics2D) g,
//					vp,
//					DefaultCADTool.geometrySelectSymbol);
			Image img = vle.getSelectionImage();
	        g.drawImage(img, 0, 0, null);
	        return;
		}else if (status.equals("Selection.WithHandlers")) {
			// Movemos los handlers que hemos seleccionado
			// previamente dentro del método select()
			double xPrev=0;
			double yPrev=0;
			for (int k = 0; k < selectedHandler.size(); k++) {
				Handler h = (Handler)selectedHandler.get(k);
				xPrev=h.getPoint().getX();
				yPrev=h.getPoint().getY();
				h.set(x, y);
			}
			// Y una vez movidos los vértices (handles)
			// redibujamos la nueva geometría.
			for (int i = 0; i < rowselectedHandlers.size(); i++) {
				Feature feature=(Feature)rowselectedHandlers.get(i);
//				IRowEdited rowEd = (IRowEdited) rowselectedHandlers.get(i);
//				IGeometry geom = ((IFeature) rowEd.getLinkedRow())
//						.getGeometry().cloneGeometry();
				Geometry geom=(feature.getDefaultGeometry()).cloneGeometry();
				g.setColor(Color.gray);
				DrawOperationContext doc=new DrawOperationContext();
    			doc.setGraphics((Graphics2D)g);
    			doc.setViewPort(vp);
    			doc.setSymbol(DefaultCADTool.axisReferencesSymbol);
            	try {
					geom.invokeOperation(Draw.CODE,doc);
				} catch (GeometryOperationNotSupportedException e) {
					e.printStackTrace();
				} catch (GeometryOperationException e) {
					e.printStackTrace();
				}
			}
			for (int k = 0; k < selectedHandler.size(); k++) {
				Handler h = (Handler)selectedHandler.get(k);
				h.set(xPrev, yPrev);
			}
			return;
		}else{
			if (!vle.getLayer().isVisible()) {
				return;
			}
			try{
			Image imgSel = vle.getSelectionImage();
	        if (imgSel!=null) {
				g.drawImage(imgSel, 0, 0, null);
			}
	        Image imgHand = vle.getHandlersImage();
	        if (imgHand!=null) {
				g.drawImage(imgHand, 0, 0, null);
			}
			}catch (Exception e) {
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
		SelectionCADToolState actualState = (SelectionCADToolState) _fsm
				.getPreviousState();
		String status = actualState.getName();
		System.out.println("PREVIOUSSTATE =" + status); // + "ESTADO ACTUAL: " +
		// _fsm.getState());
		System.out.println("STATUS ACTUAL = " + _fsm.getTransition());
		if (s.equals(PluginServices.getText(this,"cancel"))){
			init();
			return;
		}
		if (status.equals("Selection.FirstPoint")) {
			setType(s);
			return;
		}
		init();
	}




	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.gui.cad.CADTool#addvalue(double)
	 */
	public void addValue(double d) {
	}

	public String getStatus() {
		try {
			SelectionCADToolState actualState = (SelectionCADToolState) _fsm
					.getPreviousState();
			String status = actualState.getName();

			return status;
		} catch (NullPointerException e) {
			return "Selection.FirstPoint";
		}
	}



	public void end() {
		if (!getNextTool().equals("selection")) {
			CADExtension.setCADTool(getNextTool(),false);
		}
	}

	public String getName() {
		return PluginServices.getText(this,"selection_");
	}

	public boolean selectFeatures(double x, double y, InputEvent event) {
		SelectionCADToolState actualState = _fsm
				.getState();

		String status = actualState.getName();
		VectorialLayerEdited vle = getVLE();
		multipleSelection=event.isControlDown();

		if ((status.equals("Selection.FirstPoint"))
				|| (status.equals("Selection.WithSelectedFeatures"))) {
			PluginServices.getMDIManager().setWaitCursor();
			firstPoint = new Point2D.Double(x, y);
			try {
				vle.getFeatureStore().beginEditingGroup(getName());
				vle.selectWithPoint(x,y,multipleSelection);
				vle.getFeatureStore().endEditingGroup();
			} catch (NeedEditingModeException e) {
				NotificationManager.showMessageError(getName(), e);
			} catch (ReadException e) {
				NotificationManager.showMessageError(getName(), e);
			}
			PluginServices.getMDIManager().restoreCursor();
		}
		FeatureSet selection=null;
		try {
			selection = (FeatureSet)vle.getFeatureStore().getSelection();

//		ArrayList selectedRow = vle.getSelectedRow();
		long countSel=selection.getSize();
		if (countSel > 0) {
			nextState = "Selection.WithSelectedFeatures";
			return true;
		} else {
			{
				nextState = "Selection.SecondPoint";
				return true;
			}
		}
		} catch (ReadException e) {
			e.printStackTrace();
			return false;
		} catch (DataException e) {
			e.printStackTrace();
			return false;
		}
	}

	public int selectHandlers(double x, double y, InputEvent event) {
		Point2D auxPoint = new Point2D.Double(x, y);

		VectorialLayerEdited vle = getVLE();
		ArrayList selectedHandler = vle.getSelectedHandler();
		FeatureSet selection=null;
		DisposableIterator iterator = null;
		try {
			selection = (FeatureSet)vle.getFeatureStore().getSelection();

			// ArrayList selectedRow = vle.getSelectedRow();
			long countSel = selection.getSize();
			System.out.println("DENTRO DE selectHandlers. selectedRow.size= "
					+ countSel);
			selectedHandler.clear();

			// Se comprueba si se pincha en una gemometría
			PluginServices.getMDIManager().setWaitCursor();

			double tam = getCadToolAdapter().getMapControl().getViewPort()
					.toMapDistance(MapControl.tolerance);

			Handler[] handlers = null;
			rowselectedHandlers.clear();
			iterator = selection.iterator();
			while (iterator.hasNext()) {
				Feature feature = (Feature) iterator.next();

				// }
				// for (int i = 0; i < selectedRow.size(); i++) {
				// IRowEdited rowEd = (IRowEdited) selectedRow.get(i);
				//
				// IFeature fea = (IFeature) rowEd.getLinkedRow();
				handlers = (feature.getDefaultGeometry())
						.getHandlers(Geometry.SELECTHANDLER);
				// y miramos los handlers de cada entidad seleccionada
				double min = tam;
				// int hSel = -1;

				for (int j = 0; j < handlers.length; j++) {
					Point2D handlerPoint = handlers[j].getPoint();
					double distance = auxPoint.distance(handlerPoint);
					if (distance <= min) {
						min = distance;
						// hSel = j;
						selectedHandler.add(handlers[j]);
						rowselectedHandlers.add(feature);
					}
				}
			}
			PluginServices.getMDIManager().restoreCursor();
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (iterator != null) {
				iterator.dispose();
			}
		}

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
		if (type.equals("S") || type.equals("s")){
			this.type=PluginServices.getText(this,"simple");
		}else{
			this.type = type;
		}
//		pointsPolygon.clear();
	}

	public String toString() {
		return "_selection";
	}
	public void multipleSelection(boolean b) {
		multipleSelection=b;

	}

}
