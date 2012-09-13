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
import java.awt.Image;
import java.awt.event.InputEvent;
import java.awt.geom.Point2D;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureSelection;
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
import com.iver.cit.gvsig.gui.cad.tools.smc.CopyCADToolContext;
import com.iver.cit.gvsig.gui.cad.tools.smc.CopyCADToolContext.CopyCADToolState;
import com.iver.cit.gvsig.layers.VectorialLayerEdited;


/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class CopyCADTool extends DefaultCADTool {
    private CopyCADToolContext _fsm;
    private Point2D firstPoint;
    private Point2D lastPoint;

    /**
     * Crea un nuevo PolylineCADTool.
     */
    public CopyCADTool() {
    }

    /**
     * Método de incio, para poner el código de todo lo que se requiera de una
     * carga previa a la utilización de la herramienta.
     */
    public void init() {
        _fsm = new CopyCADToolContext(this);
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
    			"_copy");
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
    	CopyCADToolState actualState = (CopyCADToolState) _fsm.getPreviousState();
    	String status = actualState.getName();
    	VectorialLayerEdited vle=getVLE();
    	FeatureStore featureStore=null;
    	try {
    		featureStore = vle.getFeatureStore();
    		//    		FeatureSelection selection = featureStore.createFeatureSelection();
    		//			selection.select((FeatureSet) featureStore.getSelection());
    		FeatureSelection selection = (FeatureSelection) featureStore
    		.getSelection();
    		if (status.equals("Copy.FirstPointToMove")) {
    			firstPoint = new Point2D.Double(x, y);
    		} else if (status.equals("Copy.SecondPointToMove")) {
    			PluginServices.getMDIManager().setWaitCursor();
    			lastPoint = new Point2D.Double(x, y);
    			featureStore.beginEditingGroup(getName());

    			FeatureSelection newSelection = featureStore
    			.createFeatureSelection();

    			DisposableIterator iterator = null;

    			try{
    				iterator = selection.iterator();

    				featureStore.beginComplexNotification();

    				while (iterator.hasNext()) {
    					Feature feature = (Feature) iterator.next();
    					// Movemos la geometría
    					Geometry geometry=(feature.getDefaultGeometry()).cloneGeometry();
    					//    				EditableFeature eFeature=featureStore.createNewFeature(true);
    					UtilFunctions.moveGeom(geometry, lastPoint.getX() -
    							firstPoint.getX(), lastPoint.getY() - firstPoint.getY());
    					//    				eFeature.setGeometry(featureStore.getDefaultFeatureType().getDefaultGeometryAttributeName(),geometry);
    					//    				featureStore.insert(eFeature);
    					newSelection.select(insertGeometry(geometry, feature));
    				}
    				//    			clearSelection();
    				featureStore.endComplexNotification();
    				featureStore.setSelection(newSelection);
    				featureStore.endEditingGroup();
    				PluginServices.getMDIManager().restoreCursor();
    			} catch (DataException e) {
    				featureStore.endComplexNotification();
    				throw e;
    			} finally{
    				if (iterator != null){
    					iterator.dispose();
    				}
    			}

    		} else {
    			}
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
        CopyCADToolState actualState = _fsm.getState();
        String status = actualState.getName();
        VectorialLayerEdited vle=getVLE();
        if (status.equals("Copy.SecondPointToMove")) {
        	ViewPort vp=vle.getLayer().getMapContext().getViewPort();
            int dx = vp.fromMapDistance(x - firstPoint.getX());
            int dy = -vp.fromMapDistance(y - firstPoint.getY());
            Image img = vle.getSelectionImage();
            g.drawImage(img, dx, dy, null);
          /*  	 for (int i = 0; i < selectedRow.size(); i++) {
            		IRowEdited edRow = (IRowEdited) selectedRow.get(i);
         			IFeature feat = (IFeature) edRow.getLinkedRow();
         			IGeometry geometry = feat.getGeometry().cloneGeometry();
            		 // Movemos la geometría
                    UtilFunctions.moveGeom(geometry, x - firstPoint.getX(), y - firstPoint.getY());
                    geometry.draw((Graphics2D) g,
                        getCadToolAdapter().getMapControl().getViewPort(),
                        CADTool.drawingSymbol);
                }
          */
        }else{
        	if (!vle.getLayer().isVisible()) {
				return;
			}
        	 Image imgSel = vle.getSelectionImage();
             g.drawImage(imgSel, 0, 0, null);
             Image imgHand = vle.getHandlersImage();
             g.drawImage(imgHand, 0, 0, null);
        }
    }

    /**
     * Add a diferent option.
     *
     * @param s Diferent option.
     */
    public void addOption(String s) {
    }

    /* (non-Javadoc)
     * @see com.iver.cit.gvsig.gui.cad.CADTool#addvalue(double)
     */
    public void addValue(double d) {
    }

	public String getName() {
		return PluginServices.getText(this,"copy_");
	}

	public String toString() {
		return "_copy";
	}


}
