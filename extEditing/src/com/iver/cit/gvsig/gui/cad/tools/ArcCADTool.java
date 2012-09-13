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
import java.awt.geom.Point2D;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.FeatureSelection;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.operation.Draw;
import org.gvsig.fmap.geom.operation.DrawOperationContext;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.operation.GeometryOperationNotSupportedException;
import org.gvsig.fmap.geom.primitive.Point;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.gui.cad.DefaultCADTool;
import com.iver.cit.gvsig.gui.cad.exception.CommandException;
import com.iver.cit.gvsig.gui.cad.tools.smc.ArcCADToolContext;
import com.iver.cit.gvsig.gui.cad.tools.smc.ArcCADToolContext.ArcCADToolState;
import com.iver.cit.gvsig.layers.VectorialLayerEdited;


/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class ArcCADTool extends DefaultCADTool {
    private ArcCADToolContext _fsm;
    private Point p1;
    private Point p2;
    private Point p3;

    /**
     * Crea un nuevo LineCADTool.
     */
    public ArcCADTool() {
    }

    /**
     * Método de incio, para poner el código de todo lo que se requiera de una
     * carga previa a la utilización de la herramienta.
     */
    public void init() {
    	_fsm = new ArcCADToolContext(this);
    }

    /* (non-Javadoc)
     * @see com.iver.cit.gvsig.gui.cad.CADTool#transition(com.iver.cit.gvsig.fmap.layers.FBitSet, double, double)
     */
    public void transition(double x, double y, InputEvent event) {
        _fsm.addPoint(x, y,event);
    }

    /* (non-Javadoc)
     * @see com.iver.cit.gvsig.gui.cad.CADTool#transition(com.iver.cit.gvsig.fmap.layers.FBitSet, double)
     */
    public void transition(double d){
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
     * Equivale al transition del prototipo pero sin pasarle como pará metro el
     * editableFeatureSource que ya estará creado.
     *
     * @param sel Bitset con las geometrías que estén seleccionadas.
     * @param x parámetro x del punto que se pase en esta transición.
     * @param y parámetro y del punto que se pase en esta transición.
     */
    public void addPoint(double x, double y,InputEvent event) {
        ArcCADToolState actualState = (ArcCADToolState) _fsm.getPreviousState();
        String status = actualState.getName();

        if (status.equals("Arc.FirstPoint")) {
            p1 = createPoint(x, y);
        } else if (status.equals("Arc.SecondPoint")) {
            p2 = createPoint(x, y);
        } else if (status.equals("Arc.ThirdPoint")) {
            p3 = createPoint(x, y);
            Geometry ig = createArc(p1, p2, p3);
           	if (ig != null) {
           		insertAndSelectGeometry(ig);
           	}
        }
    }

    /**
     * Método para dibujar lo necesario para el estado en el que nos
     * encontremos.
     *
     * @param g Graphics sobre el que dibujar.
     * @param selectedGeometries BitSet con las geometrías seleccionadas.
     * @param x parámetro x del punto que se pase para dibujar.
     * @param y parámetro x del punto que se pase para dibujar.
     */
    public void drawOperation(Graphics g, double x,
        double y) {
        ArcCADToolState actualState = _fsm.getState();
        String status = actualState.getName();

        if (status.equals("Arc.SecondPoint")) {
            drawLine((Graphics2D) g, new Point2D.Double(p1.getX(), p1.getY()), new Point2D.Double(x, y),geometrySelectSymbol);
        } else if (status.equals("Arc.ThirdPoint")) {
            Point current = createPoint(x, y);
            try{
            Geometry ig = createArc(p1, p2, current);

            if (ig != null) {
            	DrawOperationContext doc=new DrawOperationContext();
    			doc.setGraphics((Graphics2D)g);
    			doc.setViewPort(getCadToolAdapter().getMapControl().getViewPort());
    			doc.setSymbol(DefaultCADTool.geometrySelectSymbol);
            	try {
					ig.invokeOperation(Draw.CODE,doc);
				} catch (GeometryOperationNotSupportedException e) {
					e.printStackTrace();
				} catch (GeometryOperationException e) {
					e.printStackTrace();
				}
            }
            }catch (IllegalArgumentException e) {
				// TODO: handle exception
			}
            Point2D p = getCadToolAdapter().getMapControl().getViewPort()
                            .fromMapPoint(p1.getX(), p1.getY());
            g.drawRect((int) p.getX(), (int) p.getY(), 1, 1);
            p = getCadToolAdapter().getMapControl().getViewPort().fromMapPoint(p2.getX(),
                    p2.getY());
            g.drawRect((int) p.getX(), (int) p.getY(), 1, 1);
        }else{
        	VectorialLayerEdited vle=getVLE();
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

    }

    /**
     * Add a diferent option.
     *
     * @param sel DOCUMENT ME!
     * @param s Diferent option.
     */
    public void addOption(String s) {
        // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
     * @see com.iver.cit.gvsig.gui.cad.CADTool#addvalue(double)
     */
    public void addValue(double d) {
    }

	public String getName() {
		return PluginServices.getText(this,"arc_");
	}

	public String toString() {
		return "_arc";
	}

	public boolean isApplicable(int shapeType) {
		switch (shapeType) {
		case Geometry.TYPES.POINT:
		case Geometry.TYPES.SURFACE:
		case Geometry.TYPES.MULTIPOINT:
			return false;
		}
		return true;
	}

}
