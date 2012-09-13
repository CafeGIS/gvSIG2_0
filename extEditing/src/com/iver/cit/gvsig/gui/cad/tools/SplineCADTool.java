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

import org.apache.log4j.Logger;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.operation.Draw;
import org.gvsig.fmap.geom.operation.DrawOperationContext;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.operation.GeometryOperationNotSupportedException;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.gui.cad.DefaultCADTool;
import com.iver.cit.gvsig.gui.cad.exception.CommandException;
import com.iver.cit.gvsig.gui.cad.tools.smc.SplineCADToolContext;
import com.iver.cit.gvsig.gui.cad.tools.smc.SplineCADToolContext.SplineCADToolState;
import com.iver.cit.gvsig.layers.VectorialLayerEdited;


/**
 * CADTool Spline
 *
 * @author Vicente Caballero Navarro
 */
public class SplineCADTool extends DefaultCADTool {
	protected static Logger logger = Logger.getLogger(SplineCADTool.class.getName());
	protected SplineCADToolContext _fsm;
	protected ArrayList list = new ArrayList();
    protected boolean close=false;

    /**
     * Crea un nuevo SplineCADTool.
     */
    public SplineCADTool() {

    }

    /**
     * M�todo de incio, para poner el c�digo de todo lo que se requiera de una
     * carga previa a la utilizaci�n de la herramienta.
     */
    public void init() {
        _fsm = new SplineCADToolContext(this);
    }

    public void endGeometry() {
        try {
			if (((FLyrVect)getVLE().getLayer()).getShapeType()==Geometry.TYPES.SURFACE && !close){
				closeGeometry();
			}
		} catch (ReadException e) {
			NotificationManager.addError(e.getMessage(),e);
		}

        // No queremos guardar FGeometryCollections:
        Geometry newGeom = createSpline((Point2D[])list.toArray(new Point2D[0]));
        insertAndSelectGeometry(newGeom);
        _fsm = new SplineCADToolContext(this);
        list.clear();
        clearTemporalCache();
        close=false;
    }
    public void closeGeometry(){
    	close=true;
        Point2D endPoint=new Point2D.Double(((Point2D)list.get(0)).getX(),((Point2D)list.get(0)).getY());
        list.add(endPoint);
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
        SplineCADToolState actualState = (SplineCADToolState) _fsm.getPreviousState();
        String status = actualState.getName();
        if (status.equals("Spline.NextPoint") || status.equals("Spline.FirstPoint")) {
           list.add(new Point2D.Double(x,y));
           Geometry spline=createSpline((Point2D[])list.toArray(new Point2D[0]));
           addTemporalCache(spline);
        }
    }

    /**
     * M�todo para dibujar lo necesario para el estado en el que nos
     * encontremos.
     *
     * @param g Graphics sobre el que dibujar.
     * @param selectedGeometries BitSet con las geometr�as seleccionadas.
     * @param x par�metro x del punto que se pase para dibujar.
     * @param y par�metro x del punto que se pase para dibujar.
     */
    public void drawOperation(Graphics g, double x,
        double y) {
        SplineCADToolState actualState = _fsm.getState();
        String status = actualState.getName();
        if (status.equals("Spline.NextPoint") || status.equals("Spline.FirstPoint")) {
//        	ArrayList points=new ArrayList();
        	Point2D[] points =new Point2D[list.size()+1];
        	Point2D[] auxPoints=(Point2D[])list.toArray(new Point2D[0]);
        	for (int i = 0; i < auxPoints.length; i++) {
				points[i]=(Point2D)auxPoints[i].clone();
			}

//        	points.addAll(list);
        	points[points.length-1]=new Point2D.Double(x,y);
        	Geometry spline=createSpline(points);
        	ViewPort vp=getCadToolAdapter().getMapControl().getViewPort();
        	DrawOperationContext doc=new DrawOperationContext();
			doc.setGraphics((Graphics2D)g);
			doc.setViewPort(vp);
			doc.setSymbol(DefaultCADTool.selectionSymbol);
        	try {
        		spline.invokeOperation(Draw.CODE,doc);
			} catch (GeometryOperationNotSupportedException e) {
				e.printStackTrace();
			} catch (GeometryOperationException e) {
				e.printStackTrace();
			}

//        	spline.draw((Graphics2D)g,vp,DefaultCADTool.selectionSymbol);
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

    }

    /**
     * Dibuja el arco sobre el graphics.
     *
     * @param point Puntero del rat�n.
     * @param lastp �ltimo punto de la polilinea.
     * @param antp Punto antepenultimo.
     * @param g Graphics sobre el que se dibuja.
     */
//    private void drawSpline(ArrayList ps, double x, double y, Graphics g) {
//    	int num=ps.size()+1;
//    	ArrayList points=new ArrayList();
//    	points.addAll(list);
//    	points.add(new Point2D.Double(x,y));
//    	double[] px=new double[num];
//    	double[] py=new double[num];
//    	for (int i=0;i<num;i++) {
//    		Point2D p=(Point2D)points.get(i);
//    		px[i]=p.getX();
//    		py[i]=p.getY();
//
//    	}
//    	Spline splineX = new Spline(px);
//        Spline splineY = new Spline(py);
//        double x0 = 0;
//        double y0 = 0;
//        ViewPort vp=getCadToolAdapter().getMapControl().getViewPort();
//        for (int i = 0; i < px.length - 1; i++) {
//            x0 = splineX.fn(i, 0);
//            y0 = splineY.fn(i, 0);
//            Point2D p0=vp.fromMapPoint(x0,y0);
//            g.fillRect((int)p0.getX() - 4, (int)p0.getY() - 4, 8, 8);
//            for (int t = 1; t < 31; t++) {
//                double x1 = splineX.fn(i, ((double) t) / 30.0);
//                double y1 = splineY.fn(i, ((double) t) / 30.0);
//                Point2D p1=vp.fromMapPoint(x1,y1);
//                g.drawLine((int)p0.getX(), (int)p0.getY(), (int)p1.getX(), (int)p1.getY());
//                p0=p1;
//                //x0 = x1;
//                //y0 = y1;
//            }
//        }
//    }

    /**
     * Add a diferent option.
     *
     * @param sel DOCUMENT ME!
     * @param s Diferent option.
     */
    public void addOption(String s) {
    }

    /* (non-Javadoc)
     * @see com.iver.cit.gvsig.gui.cad.CADTool#addvalue(double)
     */
    public void addValue(double d) {
    }

    public void cancel(){
        list.clear();
        clearTemporalCache();
        close=false;
    }

    public void end() {
        /* CADExtension.setCADTool("Spline");
        PluginServices.getMainFrame().setSelectedTool("Spline"); */
    }

    public String getName() {
        return PluginServices.getText(this,"Spline_");
    }

    public String toString() {
        return "_Spline";
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
//    private class Spline {
//    	private double y[];
//    	private double y2[];
//
//    	/**
//    	 * The constructor calculates the second derivatives of the interpolating function
//    	 * at the tabulated points xi, with xi = (i, y[i]).
//    	 * Based on numerical recipes in C, http://www.library.cornell.edu/nr/bookcpdf/c3-3.pdf .
//    	 * @param y Array of y coordinates for cubic-spline interpolation.
//    	 */
//    	public Spline(double y[]) {
//    		this.y = y;
//    		int n = y.length;
//    		y2 = new double[n];
//    		double u[] = new double[n];
//    		for (int i = 1; i < n - 1; i++) {
//    			y2[i] = -1.0 / (4.0 + y2[i - 1]);
//    			u[i] = (6.0 * (y[i + 1] - 2.0 * y[i] + y[i - 1]) - u[i - 1]) / (4.0 + y2[i - 1]);
//    		}
//    		for (int i = n - 2; i >= 0; i--) {
//    			y2[i] = y2[i] * y2[i + 1] + u[i];
//    		}
//    	}
//
//    	/**
//    	 * Returns a cubic-spline interpolated value y for the point between
//    	 * point (n, y[n]) and (n+1, y[n+1), with t ranging from 0 for (n, y[n])
//    	 * to 1 for (n+1, y[n+1]).
//    	 * @param n The start point.
//    	 * @param t The distance to the next point (0..1).
//    	 * @return A cubic-spline interpolated value.
//    	 */
//    	public double fn(int n, double t) {
//    		return t * y[n + 1] - ((t - 1.0) * t * ((t - 2.0) * y2[n] - (t + 1.0) * y2[n + 1])) / 6.0 + y[n] - t * y[n];
//    	}
//
//    }


}
