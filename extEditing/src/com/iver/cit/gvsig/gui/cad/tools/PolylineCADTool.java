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
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.aggregate.MultiPrimitive;
import org.gvsig.fmap.geom.aggregate.impl.BaseMultiPrimitive;
import org.gvsig.fmap.geom.handler.Handler;
import org.gvsig.fmap.geom.operation.Draw;
import org.gvsig.fmap.geom.operation.DrawOperationContext;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.operation.GeometryOperationNotSupportedException;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.util.Converter;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.gui.cad.DefaultCADTool;
import com.iver.cit.gvsig.gui.cad.exception.CommandException;
import com.iver.cit.gvsig.gui.cad.tools.smc.PolylineCADToolContext;
import com.iver.cit.gvsig.gui.cad.tools.smc.PolylineCADToolContext.PolylineCADToolState;
import com.iver.cit.gvsig.layers.VectorialLayerEdited;


/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class PolylineCADTool extends DefaultCADTool {
	protected PolylineCADToolContext _fsm;
	protected Point2D firstPoint;
	protected Point2D antPoint;
	protected Point2D antantPoint;
	protected Point2D antCenter;
	protected Point2D antInter;
	protected ArrayList list = new ArrayList();
	protected boolean close=false;
	protected GeneralPathX gpx = null;

    /**
     * Crea un nuevo PolylineCADTool.
     */
    public PolylineCADTool() {

    }

    /**
     * M�todo de incio, para poner el c�digo de todo lo que se requiera de una
     * carga previa a la utilizaci�n de la herramienta.
     */
    public void init() {
        _fsm = new PolylineCADToolContext(this);
    }
    public Geometry getGeometry() {
		Geometry[] geoms = (Geometry[]) list.toArray(new Geometry[0]);
		MultiPrimitive fgc = createMultiPrimitive(geoms);
		// No queremos guardar FGeometryCollections:
		GeneralPathX gp = new GeneralPathX();
		gp.append(fgc.getPathIterator(null, Converter.FLATNESS), true);
		Geometry newGeom = createCurve(gp);
		return newGeom;
	}
    public int getLinesCount() {
    	return list.size();
    }
    public boolean isPolygonLayer(){
    	try {
			return (((FLyrVect)getVLE().getLayer()).getShapeType()==Geometry.TYPES.SURFACE);
		} catch (ReadException e) {
			return false;
		}
    }
    public void endGeometry() {
    	if (gpx==null) {
    		gpx=new GeneralPathX();
    		Geometry[] geoms = (Geometry[]) list.toArray(new Geometry[0]);
    		MultiPrimitive fgc = createMultiPrimitive(geoms);
    		// No queremos guardar FGeometryCollections:
    		gpx.append(fgc.getPathIterator(null,Converter.FLATNESS), true);
    	}
        try {
 			if (((FLyrVect)getVLE().getLayer()).getShapeType()==Geometry.TYPES.SURFACE && !close){
 				closeGeometry();
 			}
 		} catch (ReadException e) {
 			NotificationManager.addError(e.getMessage(),e);
		}

        Geometry newGeom = null;
        int type=getCadToolAdapter().getActiveLayerType();
        if (type==Geometry.TYPES.SURFACE){
        	newGeom = createSurface(gpx);
        }else{
        	newGeom = createCurve(gpx);
        }
        insertAndSelectGeometry(newGeom);
        _fsm = new PolylineCADToolContext(this);
        list.clear();
        clearTemporalCache();
        close=false;
        gpx=null;
        antantPoint=antCenter=antInter=antPoint=firstPoint=null;
    }
    public void closeGeometry(){
    	if (gpx==null) {
    		gpx=new GeneralPathX();
    		Geometry[] geoms = (Geometry[]) list.toArray(new Geometry[0]);
    		MultiPrimitive fgc = createMultiPrimitive(geoms);
    		// No queremos guardar FGeometryCollections:
    		gpx.append(fgc.getPathIterator(null,Converter.FLATNESS), true);
    	}
    	close=true;
    	gpx.closePath();
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
        PolylineCADToolState actualState = (PolylineCADToolState) _fsm.getPreviousState();
        String status = actualState.getName();
        if (status.equals("Polyline.NextPointOrArcOrClose") || status.equals("Polyline.FirstPoint")) {

           if (firstPoint == null) {
               firstPoint = new Point2D.Double(x, y);
           }
        	Point2D point = new Point2D.Double(x, y);

        	if (antPoint != null && (antPoint.getX()!=point.getX() || antPoint.getY()!=point.getY())) {
                GeneralPathX elShape = new GeneralPathX(GeneralPathX.WIND_EVEN_ODD,
                        2);
                elShape.moveTo(antPoint.getX(), antPoint.getY());
                elShape.lineTo(point.getX(), point.getY());
                Geometry geom= createCurve(elShape);
                list.add(geom);
                addTemporalCache(geom);
            }
            if (antPoint==null)
         	   antPoint = (Point2D)firstPoint.clone();

            if (antPoint != null) {
                antantPoint = antPoint;
            }

            antPoint = point;
        } else if (status.equals("Polyline.NextPointOrLineOrClose")) {
            Point2D point = new Point2D.Double(x, y);
            Point2D lastp = antPoint; //(Point2D)points.get(i-1);

            if (antantPoint == null) {
                antantPoint = new Point2D.Double(lastp.getX() +
                        (point.getX() / 2), lastp.getY() + (point.getY() / 2));
            }

            if (((point.getY() == lastp.getY()) &&
                    (point.getX() < lastp.getX())) ||
                    ((point.getX() == lastp.getX()) &&
                    (point.getY() < lastp.getY()))) {
            } else {
                if (point.getX() == lastp.getX()) {
                    point = new Point2D.Double(point.getX() + 0.00000001,
                            point.getY());
                } else if (point.getY() == lastp.getY()) {
                    point = new Point2D.Double(point.getX(),
                            point.getY() + 0.00000001);
                }

                if (point.getX() == antantPoint.getX()) {
                    point = new Point2D.Double(point.getX() + 0.00000001,
                            point.getY());
                } else if (point.getY() == antantPoint.getY()) {
                    point = new Point2D.Double(point.getX(),
                            point.getY() + 0.00000001);
                }

                if (!(list.size() > 0) ||
                        (((Geometry) list.get(list.size() - 1)).getType() == Geometry.TYPES.CURVE)) {
                    Point2D[] ps1 = UtilFunctions.getPerpendicular(antantPoint,
                            lastp, lastp);
                    Point2D mediop = new Point2D.Double((point.getX() +
                            lastp.getX()) / 2, (point.getY() + lastp.getY()) / 2);
                    Point2D[] ps2 = UtilFunctions.getPerpendicular(lastp,
                            point, mediop);
                    Point2D interp = UtilFunctions.getIntersection(ps1[0],
                            ps1[1], ps2[0], ps2[1]);
                    antInter = interp;

                    double radio = interp.distance(lastp);

                    if (UtilFunctions.isLowAngle(antantPoint, lastp, interp,
                                point)) {
                        radio = -radio;
                    }

                    Point2D centerp = UtilFunctions.getPoint(interp, mediop,
                            radio);
                    antCenter = centerp;

                    Geometry ig = createArc(lastp, centerp, point);

                    if (ig != null) {
                        list.add(ig);
                        addTemporalCache(ig);
                    }
                } else {
                    Point2D[] ps1 = UtilFunctions.getPerpendicular(lastp,
                            antInter, lastp);
                    double a1 = UtilFunctions.getAngle(ps1[0], ps1[1]);
                    double a2 = UtilFunctions.getAngle(ps1[1], ps1[0]);
                    double angle = UtilFunctions.getAngle(antCenter, lastp);
                    Point2D ini1 = null;
                    Point2D ini2 = null;

                    if (UtilFunctions.absoluteAngleDistance(angle, a1) > UtilFunctions.absoluteAngleDistance(
                                angle, a2)) {
                        ini1 = ps1[0];
                        ini2 = ps1[1];
                    } else {
                        ini1 = ps1[1];
                        ini2 = ps1[0];
                    }

                    Point2D unit = UtilFunctions.getUnitVector(ini1, ini2);
                    Point2D correct = new Point2D.Double(lastp.getX() +
                            unit.getX(), lastp.getY() + unit.getY());
                    Point2D[] ps = UtilFunctions.getPerpendicular(lastp,
                            correct, lastp);
                    Point2D mediop = new Point2D.Double((point.getX() +
                            lastp.getX()) / 2, (point.getY() + lastp.getY()) / 2);
                    Point2D[] ps2 = UtilFunctions.getPerpendicular(lastp,
                            point, mediop);
                    Point2D interp = UtilFunctions.getIntersection(ps[0],
                            ps[1], ps2[0], ps2[1]);
                    antInter = interp;

                    double radio = interp.distance(lastp);

                    if (UtilFunctions.isLowAngle(correct, lastp, interp, point)) {
                        radio = -radio;
                    }

                    Point2D centerp = UtilFunctions.getPoint(interp, mediop,
                            radio);
                    antCenter = centerp;
                    Geometry geom=createArc(lastp, centerp, point);
                    list.add(geom);
                    addTemporalCache(geom);
                }

                antantPoint = antPoint;
                antPoint = point;
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
        PolylineCADToolState actualState = _fsm.getState();
        String status = actualState.getName();

        if (status.equals("Polyline.NextPointOrArcOrClose") || status.equals("Polyline.FirstPoint")) {
            for (int i = 0; i < list.size(); i++) {
            	DrawOperationContext doc=new DrawOperationContext();
    			doc.setGraphics((Graphics2D)g);
    			doc.setViewPort(getCadToolAdapter().getMapControl().getViewPort());
    			doc.setSymbol(DefaultCADTool.geometrySelectSymbol);
            	try {
            		((Geometry) list.get(i)).cloneGeometry().invokeOperation(Draw.CODE,doc);
				} catch (GeometryOperationNotSupportedException e) {
					e.printStackTrace();
				} catch (GeometryOperationException e) {
					e.printStackTrace();
				}
//                ((Geometry) list.get(i)).cloneGeometry().draw((Graphics2D) g,
//                    getCadToolAdapter().getMapControl().getViewPort(),
//                     DefaultCADTool.geometrySelectSymbol);
            }
            if (antPoint!=null)
            	drawLine((Graphics2D) g, antPoint, new Point2D.Double(x, y),DefaultCADTool.geometrySelectSymbol);

        } else if ((status.equals("Polyline.NextPointOrLineOrClose"))) {
            for (int i = 0; i < list.size(); i++) {
            	DrawOperationContext doc=new DrawOperationContext();
    			doc.setGraphics((Graphics2D)g);
    			doc.setViewPort(getCadToolAdapter().getMapControl().getViewPort());
    			doc.setSymbol(DefaultCADTool.geometrySelectSymbol);
            	try {
            		((Geometry) list.get(i)).cloneGeometry().invokeOperation(Draw.CODE,doc);
				} catch (GeometryOperationNotSupportedException e) {
					e.printStackTrace();
				} catch (GeometryOperationException e) {
					e.printStackTrace();
				}
//                ((Geometry) list.get(i)).cloneGeometry().draw((Graphics2D) g,
//                    getCadToolAdapter().getMapControl().getViewPort(),
//                     DefaultCADTool.geometrySelectSymbol);
            }

            Point2D point = new Point2D.Double(x, y);
            Point2D lastp = antPoint;

            if (!(list.size() > 0) ||
                    (((Geometry) list.get(list.size() - 1)).getType() == Geometry.TYPES.CURVE)) {
                if (antantPoint == null) {
                    drawArc(point, lastp,
                        new Point2D.Double(lastp.getX() + (point.getX() / 2),
                            lastp.getY() + (point.getY() / 2)), g);
                } else {
                    drawArc(point, lastp, antantPoint, g);
                }
            } else {
                if (antInter != null) {
                    Point2D[] ps1 = UtilFunctions.getPerpendicular(lastp,
                            antInter, lastp);
                    double a1 = UtilFunctions.getAngle(ps1[0], ps1[1]);
                    double a2 = UtilFunctions.getAngle(ps1[1], ps1[0]);
                    double angle = UtilFunctions.getAngle(antCenter, lastp);
                    Point2D ini1 = null;
                    Point2D ini2 = null;

                    if (UtilFunctions.absoluteAngleDistance(angle, a1) > UtilFunctions.absoluteAngleDistance(
                                angle, a2)) {
                        ini1 = ps1[0];
                        ini2 = ps1[1];
                    } else {
                        ini1 = ps1[1];
                        ini2 = ps1[0];
                    }

                    Point2D unit = UtilFunctions.getUnitVector(ini1, ini2);
                    Point2D correct = new Point2D.Double(lastp.getX() +
                            unit.getX(), lastp.getY() + unit.getY());
                    drawArc(point, lastp, correct, g);
                }
            }
        }
        try {
			if (((FLyrVect)getVLE().getLayer()).getShapeType()==Geometry.TYPES.SURFACE && !list.isEmpty()){
				Handler handler1=((Geometry) list.get(0)).getHandlers(Geometry.SELECTHANDLER)[0];
				GeneralPathX gpx=new GeneralPathX();
				gpx.moveTo(x,y);
				Point2D p1=handler1.getPoint();
				gpx.lineTo(p1.getX(),p1.getY());
				DrawOperationContext doc=new DrawOperationContext();
    			doc.setGraphics((Graphics2D)g);
    			doc.setViewPort(getCadToolAdapter().getMapControl().getViewPort());
    			doc.setSymbol(DefaultCADTool.geometrySelectSymbol);
            	try {
            		createCurve(gpx).invokeOperation(Draw.CODE,doc);
				} catch (GeometryOperationNotSupportedException e) {
					e.printStackTrace();
				} catch (GeometryOperationException e) {
					e.printStackTrace();
				}
//				geomFactory.createPolyline2D(gpx).draw((Graphics2D) g,
//			            getCadToolAdapter().getMapControl().getViewPort(),
//			             DefaultCADTool.geometrySelectSymbol);
			}
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
		} catch (ReadException e) {
			e.printStackTrace();
		}
    }

    /**
     * Dibuja el arco sobre el graphics.
     *
     * @param point Puntero del rat�n.
     * @param lastp �ltimo punto de la polilinea.
     * @param antp Punto antepenultimo.
     * @param g Graphics sobre el que se dibuja.
     */
    private void drawArc(Point2D point, Point2D lastp, Point2D antp, Graphics g) {
        if (((point.getY() == lastp.getY()) && (point.getX() < lastp.getX())) ||
                ((point.getX() == lastp.getX()) &&
                (point.getY() < lastp.getY()))) {
        } else {
            if (point.getX() == lastp.getX()) {
                point = new Point2D.Double(point.getX() + 0.00000001,
                        point.getY());
            } else if (point.getY() == lastp.getY()) {
                point = new Point2D.Double(point.getX(),
                        point.getY() + 0.00000001);
            }

            if (point.getX() == antp.getX()) {
                point = new Point2D.Double(point.getX() + 0.00000001,
                        point.getY());
            } else if (point.getY() == antp.getY()) {
                point = new Point2D.Double(point.getX(),
                        point.getY() + 0.00000001);
            }

            Point2D[] ps1 = UtilFunctions.getPerpendicular(lastp, antp, lastp);
            Point2D mediop = new Point2D.Double((point.getX() + lastp.getX()) / 2,
                    (point.getY() + lastp.getY()) / 2);
            Point2D[] ps2 = UtilFunctions.getPerpendicular(lastp, point, mediop);
            Point2D interp = UtilFunctions.getIntersection(ps1[0], ps1[1],
                    ps2[0], ps2[1]);

            double radio = interp.distance(lastp);

            if (UtilFunctions.isLowAngle(antp, lastp, interp, point)) {
                radio = -radio;
            }

            Point2D centerp = UtilFunctions.getPoint(interp, mediop, radio);

            drawLine((Graphics2D) g, lastp, point,DefaultCADTool.geometrySelectSymbol);

            Geometry ig = createArc(lastp, centerp, point);

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
//                ig.draw((Graphics2D) g,
//                    getCadToolAdapter().getMapControl().getViewPort(),
//                      DefaultCADTool.axisReferencesSymbol);
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
    	/*       PolylineCADToolState actualState = (PolylineCADToolState) _fsm.getPreviousState();
        String status = actualState.getName();

        if (status.equals("Polyline.NextPointOrArcOrClose")) {
            if (s.equals("A") || s.equals("a") || s.equals(PluginServices.getText(this,"inter_arc"))) {
                //Arco
            } else if (s.equals("C") || s.equals("c")) {
                GeneralPathX elShape = new GeneralPathX(GeneralPathX.WIND_EVEN_ODD, 2);
                elShape.moveTo(antPoint.getX(), antPoint.getY());
                elShape.lineTo(firstPoint.getX(), firstPoint.getY());
                list.add(ShapeFactory.createPolyline2D(elShape));
                //closeGeometry();
            }
        } else if (status.equals("Polyline.NextPointOrLineOrClose")) {
            if (s.equals("N") || s.equals("n") || s.equals(PluginServices.getText(this,"inter_line"))) {
                //L�nea
            } else if (s.equals("C") || s.equals("c")) {
                GeneralPathX elShape = new GeneralPathX(GeneralPathX.WIND_EVEN_ODD, 2);
                elShape.moveTo(antPoint.getX(), antPoint.getY());
                elShape.lineTo(firstPoint.getX(), firstPoint.getY());
                list.add(ShapeFactory.createPolyline2D(elShape));
                //closeGeometry();
            }
        }
  */
    }

    /* (non-Javadoc)
     * @see com.iver.cit.gvsig.gui.cad.CADTool#addvalue(double)
     */
    public void addValue(double d) {
    }

    public void cancel(){
        //endGeometry();
        list.clear();
        clearTemporalCache();
        antantPoint=antCenter=antInter=antPoint=firstPoint=null;
    }

    public void end() {
        /* CADExtension.setCADTool("polyline");
        PluginServices.getMainFrame().setSelectedTool("POLYLINE"); */
    }

    public String getName() {
        return PluginServices.getText(this,"polyline_");
    }

    public String toString() {
        return "_polyline";
    }
    public boolean isApplicable(int shapeType) {
        switch (shapeType) {
        case Geometry.TYPES.POINT:
        case Geometry.TYPES.MULTIPOINT:
            return false;
        }
        return true;
    }

	public void endTransition(double x, double y, MouseEvent event) {
		_fsm.endPoint(x, y, event);
	}
}
