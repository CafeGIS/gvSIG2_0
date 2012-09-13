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
import org.gvsig.fmap.geom.util.UtilFunctions;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.gui.cad.DefaultCADTool;
import com.iver.cit.gvsig.gui.cad.exception.CommandException;
import com.iver.cit.gvsig.gui.cad.exception.ValueException;
import com.iver.cit.gvsig.gui.cad.tools.smc.PolygonCADToolContext;
import com.iver.cit.gvsig.gui.cad.tools.smc.PolygonCADToolContext.PolygonCADToolState;
import com.iver.cit.gvsig.layers.VectorialLayerEdited;


/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class PolygonCADTool extends DefaultCADTool {
    protected PolygonCADToolContext _fsm;
    protected Point2D center;
    protected int numLines = 5;
    protected boolean isI = true;

    /**
     * Crea un nuevo PolygonCADTool.
     */
    public PolygonCADTool() {

    }

    /**
     * M�todo de incio, para poner el c�digo de todo lo que se requiera de una
     * carga previa a la utilizaci�n de la herramienta.
     */
    public void init() {
    	_fsm = new PolygonCADToolContext(this);
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
     * Equivale al transition del prototipo pero sin pasarle como par�metro el
     * editableFeatureSource que ya estar� creado.
     *
     * @param sel Bitset con las geometr�as que est�n seleccionadas.
     * @param x par�metro x del punto que se pase en esta transici�n.
     * @param y par�metro y del punto que se pase en esta transici�n.
     */
    public void addPoint(double x, double y,InputEvent event) {
        PolygonCADToolState actualState = (PolygonCADToolState) _fsm.getPreviousState();
        String status = actualState.getName();

        if (status.equals("Polygon.NumberOrCenterPoint")) {
            center = new Point2D.Double(x, y);
        } else if (status.equals("Polygon.CenterPoint")) {
            center = new Point2D.Double(x, y);
        } else if (status.equals("Polygon.OptionOrRadiusOrPoint") ||
                status.equals("Polygon.RadiusOrPoint")) {
            Point2D point = new Point2D.Double(x, y);
            //Pol�gono a partir de la circunferencia.
            if (isI) {
                insertAndSelectGeometry(getIPolygon(point, point.distance(center)));
            } else {
                insertAndSelectGeometry(getCPolygon(point, point.distance(center)));
            }
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
        PolygonCADToolState actualState = _fsm.getState();
        String status = actualState.getName();

        if (status.equals("Polygon.OptionOrRadiusOrPoint") ||
                status.equals("Polygon.RadiusOrPoint")) {
            Point2D point = new Point2D.Double(x, y);
            drawLine((Graphics2D) g, center, point,DefaultCADTool.geometrySelectSymbol);
            DrawOperationContext doc=new DrawOperationContext();
    		doc.setGraphics((Graphics2D)g);
    		doc.setViewPort(getCadToolAdapter().getMapControl().getViewPort());
    		doc.setSymbol(DefaultCADTool.geometrySelectSymbol);
            if (isI) {
            	try {
            		getIPolygon(point, point.distance(center)).invokeOperation(Draw.CODE,doc);
        		} catch (GeometryOperationNotSupportedException e) {
        			e.printStackTrace();
        		} catch (GeometryOperationException e) {
        			e.printStackTrace();
        		}
//                getIPolygon(point, point.distance(center)).draw((Graphics2D) g,
//                    getCadToolAdapter().getMapControl().getViewPort(),
//                    DefaultCADTool.geometrySelectSymbol);
            } else {
            	try {
            		getCPolygon(point, point.distance(center)).invokeOperation(Draw.CODE,doc);
        		} catch (GeometryOperationNotSupportedException e) {
        			e.printStackTrace();
        		} catch (GeometryOperationException e) {
        			e.printStackTrace();
        		}
//            	getCPolygon(point, point.distance(center)).draw((Graphics2D) g,
//                    getCadToolAdapter().getMapControl().getViewPort(),
//                    DefaultCADTool.geometrySelectSymbol);
            }
            try {
            	doc.setSymbol(DefaultCADTool.axisReferencesSymbol);
            	createCircle(center, point.distance(center)).invokeOperation(Draw.CODE,doc);
    		} catch (GeometryOperationNotSupportedException e) {
    			e.printStackTrace();
    		} catch (GeometryOperationException e) {
    			e.printStackTrace();
    		}
//            geomFactory.createCircle(center, point.distance(center)).draw((Graphics2D) g,
//                getCadToolAdapter().getMapControl().getViewPort(),
//                DefaultCADTool.axisReferencesSymbol);
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
        PolygonCADToolState actualState = (PolygonCADToolState) _fsm.getPreviousState();
        String status = actualState.getName();

        if (status.equals("Polygon.OptionOrRadiusOrPoint")) {
            if (s.equalsIgnoreCase(PluginServices.getText(this,"PolygonCADTool.circumscribed")) || s.equals(PluginServices.getText(this,"circumscribed"))) {
                isI = false;
            } else if (s.equalsIgnoreCase(PluginServices.getText(this,"PolygonCADTool.into_circle")) || s.equals(PluginServices.getText(this,"into_circle"))) {
                isI = true;
            }
        }
    }

    /* (non-Javadoc)
     * @see com.iver.cit.gvsig.gui.cad.CADTool#addvalue(double)
     */
    public void addValue(double d) {
        PolygonCADToolState actualState = (PolygonCADToolState) _fsm.getPreviousState();
        String status = actualState.getName();

        if (status.equals("Polygon.NumberOrCenterPoint")) {
        	numLines = (int) d;
        } else if (status.equals("Polygon.OptionOrRadiusOrPoint") ||
                status.equals("Polygon.RadiusOrPoint")) {
            double radio = d;

            //Pol�gono a partir de radio.
            Point2D point = UtilFunctions.getPoint(center,
                    new Point2D.Double(center.getX(), center.getY() + 10), radio);

            if (isI) {
                insertAndSelectGeometry(getIPolygon(point, radio));
            } else {
                insertAndSelectGeometry(getCPolygon(point, radio));
            }
        }
    }

    /**
     * Devuelve la geometr�a con el poligono regular circunscrito a la
     * circunferencia formada por el punto central y el radio que se froma
     * entre este y el puntero del rat�n..
     *
     * @param point Puntero del rat�n.
     * @param radio Radio
     *
     * @return GeometryCollection con las geometr�as del pol�gono.
     */
    private Geometry getCPolygon(Point2D point, double radio) {
        Point2D p1 = UtilFunctions.getPoint(center, point, radio);

        double initangle = UtilFunctions.getAngle(center, point);
        Point2D antPoint = p1;
        Point2D antInter = null;
        //Point2D firstPoint= null;
        double an = (Math.PI * 2) / numLines;
        GeneralPathX elShape = new GeneralPathX();
        boolean firstTime=true;
        for (int i = numLines-1; i >= 0 ; i--) {
            Point2D p2 = UtilFunctions.getPoint(center, (an * i) + initangle,
                    radio);
            Point2D[] ps1 = UtilFunctions.getPerpendicular(antPoint, center,
                    antPoint);
            Point2D[] ps2 = UtilFunctions.getPerpendicular(p2, center, p2);
            Point2D inter = UtilFunctions.getIntersection(ps1[0], ps1[1],
                    ps2[0], ps2[1]);

            if (antInter != null) {

                if (firstTime){
                	elShape.moveTo(antInter.getX(), antInter.getY());
                	//firstPoint=new Point2D.Double(antInter.getX(), antInter.getY());
                	firstTime=false;
                }
                elShape.lineTo(inter.getX(), inter.getY());

            }

            antInter = inter;
            antPoint = p2;
        }
        //elShape.lineTo(firstPoint.getX(),firstPoint.getY());
        elShape.closePath();
        int type=getCadToolAdapter().getActiveLayerType();
        Geometry shape=null;
        if (type==Geometry.TYPES.SURFACE){
        	shape = createSurface(elShape);
        }else{
        	shape = createCurve(elShape);
        }
        return shape;
    }

    /**
     * Devuelve la geometr�a con el poligono regular inscrito en la
     * circunferencia.
     *
     * @param point Puntero del rat�n.
     * @param radio Radio
     *
     * @return GeometryCollection con las geometr�as del pol�gono.
     * @throws ValueException
     */
    private Geometry getIPolygon(Point2D point, double radio){
    	Point2D p1 = UtilFunctions.getPoint(center, point, radio);
        double initangle = UtilFunctions.getAngle(center, point);
        Point2D antPoint = p1;
        //Point2D firstPoint= null;
        double an = (Math.PI * 2) / numLines;
        GeneralPathX elShape = new GeneralPathX();
        boolean firstTime=true;

        for (int i = numLines-1; i > 0; i--) {
            Point2D p2 = UtilFunctions.getPoint(center, (an * i) + initangle,
                    radio);

            if (firstTime){
            	 elShape.moveTo(antPoint.getX(), antPoint.getY());
            	 //firstPoint=new Point2D.Double(antPoint.getX(), antPoint.getY());
            	 firstTime=false;
            }

            elShape.lineTo(p2.getX(), p2.getY());

            antPoint = p2;
        }
        //elShape.lineTo(firstPoint.getX(),firstPoint.getY());
        elShape.closePath();
        int type=getCadToolAdapter().getActiveLayerType();
        Geometry shape=null;
        if (type==Geometry.TYPES.SURFACE){
        	shape = createSurface(elShape);
        }else{
        	shape = createCurve(elShape);
        }
        return shape;
    }
    /**
     * Devuelve la geometr�a con el poligono regular circunscrito a la
     * circunferencia formada por el punto central y el radio que se froma
     * entre este y el puntero del rat�n..
     *
     * @param point Puntero del rat�n.
     * @param radio Radio
     *
     * @return GeometryCollection con las geometr�as del pol�gono.
     */
   /* private IGeometry getCPolygonOld(Point2D point, double radio) {
        IGeometry[] geoms = new IGeometry[numLines];
        Point2D p1 = UtilFunctions.getPoint(center, point, radio);

        double initangle = UtilFunctions.getAngle(center, point);
        Point2D antPoint = p1;
        Point2D antInter = null;
        double an = (Math.PI * 2) / numLines;

        for (int i = 1; i < (numLines + 2); i++) {
            Point2D p2 = UtilFunctions.getPoint(center, (an * i) + initangle,
                    radio);
            Point2D[] ps1 = UtilFunctions.getPerpendicular(antPoint, center,
                    antPoint);
            Point2D[] ps2 = UtilFunctions.getPerpendicular(p2, center, p2);
            Point2D inter = UtilFunctions.getIntersection(ps1[0], ps1[1],
                    ps2[0], ps2[1]);

            if (antInter != null) {
                GeneralPathX elShape = new GeneralPathX(GeneralPathX.WIND_EVEN_ODD,
                        2);
                elShape.moveTo(antInter.getX(), antInter.getY());
                elShape.lineTo(inter.getX(), inter.getY());

                geoms[i - 2] = (ShapeFactory.createPolyline2D(elShape));
            }

            antInter = inter;
            antPoint = p2;
        }

        return new FGeometryCollection(geoms);
    }
*/
    /**
     * Devuelve la geometr�a con el poligono regular inscrito en la
     * circunferencia.
     *
     * @param point Puntero del rat�n.
     * @param radio Radio
     *
     * @return GeometryCollection con las geometr�as del pol�gono.
     */
  /*  private IGeometry getIPolygonOld(Point2D point, double radio) {
        IGeometry[] geoms = new IGeometry[numLines];
        Point2D p1 = UtilFunctions.getPoint(center, point, radio);
        double initangle = UtilFunctions.getAngle(center, point);
        Point2D antPoint = p1;
        double an = (Math.PI * 2) / numLines;

        for (int i = 1; i < (numLines + 1); i++) {
            Point2D p2 = UtilFunctions.getPoint(center, (an * i) + initangle,
                    radio);
            GeneralPathX elShape = new GeneralPathX(GeneralPathX.WIND_EVEN_ODD,
                    2);
            elShape.moveTo(antPoint.getX(), antPoint.getY());
            elShape.lineTo(p2.getX(), p2.getY());

            geoms[i - 1] = (ShapeFactory.createPolyline2D(elShape));
            antPoint = p2;
        }

        return new FGeometryCollection(geoms);
    }
*/
	public String getName() {
		return PluginServices.getText(this,"polygon_");
	}

	/**
	 * Devuelve la cadena que corresponde al estado en el que nos encontramos.
	 *
	 * @return Cadena para mostrar por consola.
	 */
	public String getQuestion() {
		PolygonCADToolState actualState = _fsm.getState();
        String status = actualState.getName();

        if (status.equals("Polygon.NumberOrCenterPoint")) {
        	return super.getQuestion()+"<"+numLines+">";
        }
       	return super.getQuestion();

	}
	public String toString() {
		return "_polygon";
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
