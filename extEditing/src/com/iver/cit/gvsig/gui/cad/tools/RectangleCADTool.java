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
import java.awt.geom.Point2D;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.operation.Draw;
import org.gvsig.fmap.geom.operation.DrawOperationContext;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.operation.GeometryOperationNotSupportedException;
import org.gvsig.fmap.geom.primitive.GeneralPathX;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.gui.cad.DefaultCADTool;
import com.iver.cit.gvsig.gui.cad.exception.CommandException;
import com.iver.cit.gvsig.gui.cad.tools.smc.RectangleCADToolContext;
import com.iver.cit.gvsig.gui.cad.tools.smc.RectangleCADToolContext.RectangleCADToolState;
import com.iver.cit.gvsig.layers.VectorialLayerEdited;


/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class RectangleCADTool extends DefaultCADTool {
    protected RectangleCADToolContext _fsm;
    protected Point2D firstPoint;
    protected Point2D lastPoint;

    /**
     * Crea un nuevo LineCADTool.
     */
    public RectangleCADTool() {

    }

    /**
     * M�todo de incio, para poner el c�digo de todo lo que se requiera de una
     * carga previa a la utilizaci�n de la herramienta.
     */
    public void init() {
    	_fsm = new RectangleCADToolContext(this);
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
     * Equivale al transition del prototipo pero sin pasarle como par� metro el
     * editableFeatureSource que ya estar� creado.
     *
     * @param sel Bitset con las geometr�as que est�n seleccionadas.
     * @param x par�metro x del punto que se pase en esta transici�n.
     * @param y par�metro y del punto que se pase en esta transici�n.
     */
    public void addPoint(double x, double y,InputEvent event) {
        RectangleCADToolState actualState = (RectangleCADToolState) _fsm.getPreviousState();

        String status = actualState.getName();

        if (status.equals("Rectangle.FirstPoint")) {
            firstPoint = new Point2D.Double(x, y);
        } else if (status == "Rectangle.SecondPointOrSquare") {
            lastPoint = new Point2D.Double(x, y);

            GeneralPathX elShape = new GeneralPathX();
            elShape.moveTo(firstPoint.getX(), firstPoint.getY());
            elShape.lineTo(lastPoint.getX(), firstPoint.getY());
            elShape.lineTo(lastPoint.getX(), lastPoint.getY());
            elShape.lineTo(firstPoint.getX(), lastPoint.getY());
            //elShape.lineTo(firstPoint.getX(), firstPoint.getY());
            elShape.closePath();
            int type=getCadToolAdapter().getActiveLayerType();
            if (type==Geometry.TYPES.SURFACE){
            	insertAndSelectGeometry(createSurface(elShape));
            }else{
            	insertAndSelectGeometry(createCurve(elShape));
            }
            firstPoint = (Point2D) lastPoint.clone();
        } else if (status == "Rectangle.SecondPointSquare") {
            lastPoint = new Point2D.Double(x, y);

            GeneralPathX elShape = new GeneralPathX();
            elShape.moveTo(firstPoint.getX(), firstPoint.getY());
            elShape.lineTo(lastPoint.getX(), firstPoint.getY());

            if (((lastPoint.getY() <= firstPoint.getY()) &&
                    (lastPoint.getX() <= firstPoint.getX())) ||
                    ((lastPoint.getY() > firstPoint.getY()) &&
                    (lastPoint.getX() > firstPoint.getX()))) {
                elShape.lineTo(lastPoint.getX(),
                    firstPoint.getY() + (lastPoint.getX() - firstPoint.getX()));
                elShape.lineTo(firstPoint.getX(),
                    firstPoint.getY() + (lastPoint.getX() - firstPoint.getX()));
            } else {
                elShape.lineTo(lastPoint.getX(),
                    firstPoint.getY() - (lastPoint.getX() - firstPoint.getX()));
                elShape.lineTo(firstPoint.getX(),
                    firstPoint.getY() - (lastPoint.getX() - firstPoint.getX()));
            }

            //elShape.lineTo(firstPoint.getX(), firstPoint.getY());
            elShape.closePath();
            int type=getCadToolAdapter().getActiveLayerType();
            if (type==Geometry.TYPES.SURFACE){
            	insertAndSelectGeometry(createSurface(elShape));
            }else{
            	insertAndSelectGeometry(createCurve(elShape));
            }
            firstPoint = (Point2D) lastPoint.clone();
        }
    }

    /**
     * M�todo para dibujar la lo necesario para el estado en el que nos
     * encontremos.
     *
     * @param g Graphics sobre el que dibujar.
     * @param selectedGeometries BitSet con las geometr�as seleccionadas.
     * @param x par�metro x del punto que se pase para dibujar.
     * @param y par�metro x del punto que se pase para dibujar.
     */
    public void drawOperation(Graphics g, double x,
        double y) {
        RectangleCADToolState actualState = _fsm.getState();
        String status = actualState.getName();

        if (status == "Rectangle.SecondPointOrSquare") {
            GeneralPathX elShape = new GeneralPathX(GeneralPathX.WIND_EVEN_ODD,
                    4);
            elShape.moveTo(firstPoint.getX(), firstPoint.getY());
            elShape.lineTo(x, firstPoint.getY());
            elShape.lineTo(x, y);
            elShape.lineTo(firstPoint.getX(), y);
            elShape.lineTo(firstPoint.getX(), firstPoint.getY());
            DrawOperationContext doc=new DrawOperationContext();
			doc.setGraphics((Graphics2D)g);
			doc.setViewPort(getCadToolAdapter().getMapControl().getViewPort());
			doc.setSymbol(DefaultCADTool.geometrySelectSymbol);
        	try {
        		createCurve(elShape).cloneGeometry().invokeOperation(Draw.CODE,doc);
			} catch (GeometryOperationNotSupportedException e) {
				e.printStackTrace();
			} catch (GeometryOperationException e) {
				e.printStackTrace();
			}
//            geomFactory.createPolyline2D(elShape).draw((Graphics2D) g,
//                getCadToolAdapter().getMapControl().getViewPort(),
//                DefaultCADTool.geometrySelectSymbol);
        } else if (status == "Rectangle.SecondPointSquare") {
            GeneralPathX elShape = new GeneralPathX(GeneralPathX.WIND_EVEN_ODD,
                    4);
            elShape.moveTo(firstPoint.getX(), firstPoint.getY());
            elShape.lineTo(x, firstPoint.getY());

            if (((y <= firstPoint.getY()) && (x <= firstPoint.getX())) ||
                    ((y > firstPoint.getY()) && (x > firstPoint.getX()))) {
                elShape.lineTo(x, firstPoint.getY() + (x - firstPoint.getX()));
                elShape.lineTo(firstPoint.getX(),
                    firstPoint.getY() + (x - firstPoint.getX()));
                elShape.lineTo(firstPoint.getX(), firstPoint.getY());
            } else {
                elShape.lineTo(x, firstPoint.getY() - (x - firstPoint.getX()));
                elShape.lineTo(firstPoint.getX(),
                    firstPoint.getY() - (x - firstPoint.getX()));
                elShape.lineTo(firstPoint.getX(), firstPoint.getY());
            }
            DrawOperationContext doc=new DrawOperationContext();
			doc.setGraphics((Graphics2D)g);
			doc.setViewPort(getCadToolAdapter().getMapControl().getViewPort());
			doc.setSymbol(DefaultCADTool.geometrySelectSymbol);
        	try {
        		createCurve(elShape).cloneGeometry().invokeOperation(Draw.CODE,doc);
			} catch (GeometryOperationNotSupportedException e) {
				e.printStackTrace();
			} catch (GeometryOperationException e) {
				e.printStackTrace();
			}
//            geomFactory.createPolyline2D(elShape).draw((Graphics2D) g,
//                getCadToolAdapter().getMapControl().getViewPort(),
//                DefaultCADTool.geometrySelectSymbol);
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
        RectangleCADToolState actualState = (RectangleCADToolState) _fsm.getPreviousState();
        String status = actualState.getName();

        if (status == "Rectangle.SecondPointOrSquare") {
            if (s.equalsIgnoreCase(PluginServices.getText(this,"RectangleCADTool.square"))) {
                //Opci�n correcta
            }
        }
    }

    /* (non-Javadoc)
     * @see com.iver.cit.gvsig.gui.cad.CADTool#addvalue(double)
     */
    public void addValue(double d) {
    }

	public String getName() {
		return PluginServices.getText(this,"rectangle_");
	}

	public String toString() {
		return "_rectangle";
	}
	public boolean isApplicable(int shapeType) {
		switch (shapeType) {
		case Geometry.TYPES.POINT:
		case Geometry.TYPES.MULTIPOINT:
			return false;
		}
		return true;
	}
}
