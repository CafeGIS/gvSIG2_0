/*
 * Created on 22-jun-2004
 *
 */
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
package com.iver.cit.gvsig.project.documents.layout.fframes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.BitSet;

import org.gvsig.compat.print.PrintAttributes;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.mapcontext.rendering.symbols.IMarkerSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SimpleFillSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbologyFactory;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.exceptions.SaveException;
import com.iver.cit.gvsig.project.documents.layout.fframes.gui.dialogs.FFrameGraphicsDialog;
import com.iver.cit.gvsig.project.documents.layout.fframes.gui.dialogs.IFFrameDialog;
import com.iver.cit.gvsig.project.documents.layout.geometryadapters.CircleAdapter;
import com.iver.cit.gvsig.project.documents.layout.geometryadapters.GeometryAdapter;
import com.iver.cit.gvsig.project.documents.layout.geometryadapters.PointAdapter;
import com.iver.cit.gvsig.project.documents.layout.geometryadapters.PolyLineAdapter;
import com.iver.cit.gvsig.project.documents.layout.geometryadapters.PolygonAdapter;
import com.iver.cit.gvsig.project.documents.layout.geometryadapters.RectangleAdapter;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;
import com.iver.utiles.XMLEntity;


/**
 * FFrame para contener un gráfico.
 *
 * @author Vicente Caballero Navarro
 */
public class FFrameGraphics extends FFrame implements IFFrameEditableVertex {
    private static double TOL = 0.2;
    public static final int POINT = 21;
    public static final int LINE = 22;
    public static final int POLYLINE = 23;
    public static final int RECTANGLE = 24;
    public static final int CIRCLE = 25;
    public static final int POLYGON = 26;
    public static final int RECTANGLEGROUP = 16;
    public static final int RECTANGLESYMBOL = 17;
    private int m_type = POINT;
    private ISymbol m_symbol = null;
    private Color m_color = null;
    private GeometryAdapter geometry;
    private GeometryAdapter geometryEdit;
    private boolean editing = false;
    private double size = 0.5;
    private BitSet index = new BitSet();

    /**
     * Crea un nuevo FFrameGraphics.
     */
    public FFrameGraphics() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param geom DOCUMENT ME!
     */
    public void setGeometryAdapter(GeometryAdapter geom) {
        geometry = geom;
    }

    /**
     * Rellena el color que se utlizará para dibujar el símbolo.
     *
     * @param color
     */
    public void setColor(Color color) {
        m_color = color;
    }

    /**
     * Actualiza el Fsymbol a partir del tipo de Gráfico que se pase como
     * parámetro.
     *
     * @param type tipo de gráfico.
     * @param at Transformada.
     */
    public void update(int type, AffineTransform at) {
        m_type = type;

        //aT = at;
        if (m_color == null) {
            m_color = Color.black;
        }
        int shapeType = -1;
        /*
         * (jaume) moved to ISymbol
         * switch (m_type) {
            case (POINT):

            	FSymbol symbolPoint= new FSymbol(FConstant.SHAPE_TYPE_POINT, m_color);
               	symbolPoint.setSize((int) FLayoutUtilities.fromSheetDistance(
                        size, at));
               	m_symbol=symbolPoint;
                break;

            case (RECTANGLE):
            	FSymbol symbolRectangle= new FSymbol(FConstant.SYMBOL_TYPE_FILL, m_color);
                symbolRectangle.setColor(null);
                m_symbol=symbolRectangle;
                break;

            case (LINE):
            	FSymbol symbolLine= new FSymbol(FConstant.SYMBOL_TYPE_LINE, m_color);
            	m_symbol=symbolLine;
                break;

            case (POLYLINE):
            	FSymbol symbolPolyline= new FSymbol(FConstant.SYMBOL_TYPE_LINE, m_color);
            	m_symbol=symbolPolyline;
                break;

            case (POLYGON):
            	FSymbol symbolPolygon= new FSymbol(FConstant.SYMBOL_TYPE_FILL, m_color);
                symbolPolygon.setStyle(FConstant.SYMBOL_STYLE_FILL_SOLID);
                symbolPolygon.setOutlined(true);
                symbolPolygon.setOutlineColor(m_color);
                symbolPolygon.setColor(null);
                m_symbol=symbolPolygon;
                break;

            case (CIRCLE):
            	FSymbol symbolCircle= new FSymbol(FConstant.SYMBOL_TYPE_FILL, m_color);
                symbolCircle.setStyle(FConstant.SYMBOL_STYLE_FILL_SOLID);
                symbolCircle.setOutlined(true);
                symbolCircle.setOutlineColor(m_color);
                symbolCircle.setColor(null);
                m_symbol=symbolCircle;
                break;
        }
        */
        switch (m_type) {
        case (POINT):
        	shapeType = Geometry.TYPES.POINT;
            break;

        case (RECTANGLE):
        case (POLYGON):
        case (CIRCLE):
        	shapeType = Geometry.TYPES.SURFACE;
            break;

        case (LINE):
        case (POLYLINE):
        	shapeType = Geometry.TYPES.CURVE;
            break;
        }

        m_symbol = SymbologyFactory.createDefaultSymbolByShapeType(shapeType);
        if (shapeType==Geometry.TYPES.SURFACE){
        	((SimpleFillSymbol)m_symbol).setFillColor(null);
        }
    }

    /**
     * Devuelve el FSymbol que se representa.
     *
     * @return DOCUMENT ME!
     */
    public ISymbol getFSymbol() {
        return m_symbol;
    }

    /**
     * Rellena el FSymbol que se representara al dibujar.
     *
     * @param fs2d
     */
    public void setSymbol(ISymbol symbol) {
        m_symbol = symbol;
    }

    /**
     * Método que dibuja sobre el graphics que se le pasa como parámetro, según
     * la transformada afin que se debe de aplicar y el rectángulo que se debe
     * de dibujar.
     *
     * @param g Graphics
     * @param at Transformada afin.
     * @param rv rectángulo sobre el que hacer un clip.
     * @param imgBase DOCUMENT ME!
     */
    public void draw(Graphics2D g, AffineTransform at, Rectangle2D rv,
        BufferedImage imgBase) {
        Rectangle2D.Double rect = getBoundingBox(at);
        g.rotate(Math.toRadians(getRotation()), rect.x + (rect.width / 2),
            rect.y + (rect.height / 2));
        if (intersects(rv, rect)) {
            g.setColor(Color.black);

            if (m_type == POINT) {
                ((IMarkerSymbol)m_symbol).setSize((int) (rect.getWidth() * 0.7));
            }
//            float stroke=0;
//            if (((FSymbol)m_symbol).getStroke() != null) {
//            	stroke=((BasicStroke)((FSymbol)m_symbol).getStroke()).getLineWidth();
//                BasicStroke basicStroke = new BasicStroke((float)FLayoutUtilities.fromSheetDistance(stroke,at)/100);
//                ((FSymbol)m_symbol).setStroke(basicStroke);
//            }
            geometry.draw(g, at, m_symbol);

//            if (((FSymbol)m_symbol).getStroke() != null) {
//                BasicStroke basicStroke = new BasicStroke(stroke);
//                ((FSymbol)m_symbol).setStroke(basicStroke);
//            }

            if (editing) {
                g.setColor(Color.red);
                geometry.drawVertex(g, at);
            }
        }

        g.rotate(Math.toRadians(-getRotation()), rect.x + (rect.width / 2),
            rect.y + (rect.height / 2));
    }

    /**
     * @see com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame#getXMLEntity()
     */
    public XMLEntity getXMLEntity() throws SaveException {
        XMLEntity xml = super.getXMLEntity();

        try {
//            xml.putProperty("type", Layout.GRAPHICS);
            xml.putProperty("m_type", m_type);
            xml.addChild(m_symbol.getXMLEntity());
            if (geometry!=null) {
                xml.addChild(geometry.getXMLEntity());
            }
           // xml.addChild(geometryEdit.getXMLEntity());
        } catch (Exception e) {
            throw new SaveException(e, this.getClass().getName());
        }

        return xml;
    }

    /**
     * @see com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame#updateNum()
     */
    public void updateNum() {
    }

    /**
     * @see com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame#getNum()
     */
    public int getNum() {
        return 0;
    }

    /**
     * @see com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame#setXMLEntity(com.iver.utiles.XMLEntity,
     *      com.iver.cit.gvsig.project.Project)
     */
    public void setXMLEntity(XMLEntity xml) {
        m_Selected = xml.getIntProperty("m_Selected");

        setRotation(xml.getDoubleProperty("m_rotation"));
        // jaume; m_symbol = FSymbol.createFromXML(xml.getChild(0));
        m_symbol = SymbologyFactory.createSymbolFromXML(xml.getChild(0), null);
        if (xml.contains("m_type")) {
            m_type = xml.getIntProperty("m_type");
           // return;
        }
        if (m_type==RECTANGLESYMBOL) {
			return;
		}
        if (xml.getChildrenCount() ==1) {
            Rectangle2D r = (Rectangle2D) getBoundBox().clone();

            if (m_type == LINE) {
                geometry = new PolyLineAdapter();
            } else if (m_type == RECTANGLE ||
                    m_type == RECTANGLEGROUP) {
                geometry = new RectangleAdapter();
                geometry.addPoint(new Point2D.Double(r.getX(), r.getY()));
                geometry.addPoint(new Point2D.Double(r.getMaxX(), r.getMaxY()));
            } else if (m_type == POLYLINE) {
                geometry = new PolyLineAdapter();
            } else if (m_type == POLYGON) {
                geometry = new PolygonAdapter();
            } else if (m_type == CIRCLE) {
                geometry = new CircleAdapter();
                geometry.addPoint(new Point2D.Double(r.getCenterX(),
                        r.getCenterY()));

                if (r.getWidth() < r.getHeight()) {
                    geometry.addPoint(new Point2D.Double(r.getMaxX(),
                            r.getCenterY()));
                } else {
                    geometry.addPoint(new Point2D.Double(r.getCenterX(),
                            r.getY()));
                }
            } else if (m_type == POINT) {
                geometry = new PointAdapter();
                geometry.addPoint(new Point2D.Double(r.getCenterX(),
                        r.getCenterY()));
            }

            geometry.end();
        } else {
            geometry = GeometryAdapter.createFromXML(xml.getChild(1));
           // geometryEdit = GeometryAdapter.createFromXML(xml.getChild(2));
        }
    }

    /**
     * @see com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame#setXMLEntity(com.iver.utiles.XMLEntity,
     *      com.iver.cit.gvsig.project.Project)
     */
    public void setXMLEntity03(XMLEntity xml, Layout p) {
        m_Selected = xml.getIntProperty("m_Selected");
        m_type = xml.getIntProperty("m_type");
        // jaume; m_symbol = FSymbol.createFromXML(xml.getChild(0));
        m_symbol = SymbologyFactory.createSymbolFromXML(xml.getChild(0), null);

        Rectangle2D r = (Rectangle2D) getBoundBox().clone();

        if (m_type == LINE) {
            geometry = new PolyLineAdapter();
        } else if (m_type == RECTANGLE) {
            geometry = new RectangleAdapter();
            geometry.addPoint(new Point2D.Double(r.getX(), r.getY()));
            geometry.addPoint(new Point2D.Double(r.getMaxX(), r.getMaxY()));
        } else if (m_type == POLYLINE) {
            geometry = new PolyLineAdapter();
        } else if (m_type == POLYGON) {
            geometry = new PolygonAdapter();
        } else if (m_type == CIRCLE) {
            geometry = new CircleAdapter();
            geometry.addPoint(new Point2D.Double(r.getCenterX(), r.getCenterY()));

            if (r.getWidth() < r.getHeight()) {
                geometry.addPoint(new Point2D.Double(r.getMaxX(), r.getCenterY()));
            } else {
                geometry.addPoint(new Point2D.Double(r.getCenterX(), r.getY()));
            }
        } else if (m_type == POINT) {
            geometry = new PointAdapter();
            geometry.addPoint(new Point2D.Double(r.getCenterX(), r.getCenterY()));
        }

        geometry.end();
    }

    /**
     * @see com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame#getNameFFrame()
     */
    public String getNameFFrame() {
        return PluginServices.getText(this, "Graficos")+ num;
    }

    /**
     * Inserta el tamaño del punto.
     *
     * @param size entero que representa el tamaño del punto.
     */
    public void setSize(double size) {
        this.size = size;
        if (m_type==RECTANGLESYMBOL) {
            return;
        }
        Rectangle2D r = geometry.getBounds2D();
        super.setBoundBox(new Rectangle2D.Double(r.getX() - size,
                r.getY() - size, size * 2, size * 2));
    }

    /**
     * Devuelve el tipo de gráfico que contiene el fframe.
     *
     * @return tipo de
     */
    public int getType() {
        return m_type;
    }
    public int getShapeType(){
    	int shapeType=0;
    	switch (m_type) {
        case (POINT):
        	shapeType = Geometry.TYPES.POINT;
            break;

        case (RECTANGLE):
        case (POLYGON):
        case (CIRCLE):
        	shapeType = Geometry.TYPES.SURFACE;
            break;

        case (LINE):
        case (POLYLINE):
        	shapeType = Geometry.TYPES.CURVE;
            break;
        }
    	return shapeType;
    }
    public void setType(int type) {
        m_type = type;
    }
    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     */
    public void setBoundBox(Rectangle2D r) {
        if (m_type==RECTANGLESYMBOL) {
            super.setBoundBox(r);
            return;
        }
        AffineTransform aT = new AffineTransform();

        if (getBoundBox().getWidth() != 0) {
            double w = r.getWidth() / getBoundBox().getWidth();
            double h = r.getHeight() / getBoundBox().getHeight();

            AffineTransform trans2 = AffineTransform.getTranslateInstance(r.getX(),
                    r.getY());
            aT.concatenate(trans2);

            AffineTransform scale1 = AffineTransform.getScaleInstance(w, h);
            aT.concatenate(scale1);

            AffineTransform trans1 = AffineTransform.getTranslateInstance(-getBoundBox()
                                                                               .getX(),
                    -getBoundBox().getY());
            aT.concatenate(trans1);
            geometry.applyTransform(aT);

            size = aT.getScaleX() * size;
        }

        super.setBoundBox(r);
    }

    /**
     * DOCUMENT ME!
     */
    public void startEditing() {
        editing = true;
    }

    /**
     * DOCUMENT ME!
     */
    public void stopEditing() {
        editing = false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isEditing() {
        return editing;
    }

    /**
     * DOCUMENT ME!
     *
     * @param point DOCUMENT ME!
     * @param geom DOCUMENT ME!
     */
    public void pointReleased(Point2D point, GeometryAdapter geom) {
        index.clear();
        geometry = geom;

        Rectangle2D r = geometry.getBounds2D();
        super.setBoundBox(r);
    }

    /**
     * DOCUMENT ME!
     *
     * @param point DOCUMENT ME!
     */
    public void pointPressed(Point2D point) {
        Rectangle2D.Double rect = getBoundBox();
        Point2D[] points = geometry.getPoints();
        geometryEdit = geometry.cloneAdapter();//GeometryAdapter.createFromXML(geometry.getXMLEntity());

        Point2D pAux1 = new Point2D.Double();
        index.clear();
        for (int i = 0; i < points.length; i++) {
            if (getRotation() != 0) {
                AffineTransform af = AffineTransform.getRotateInstance(Math.toRadians(
                            -getRotation()), rect.x + (rect.width / 2),
                        rect.y + (rect.height / 2));
                af.transform(point, pAux1);

                if (points[i].distance(pAux1) <= TOL) {
                    index.set(i);
                }
            } else {
                if (points[i].distance(point) <= TOL) {
                    index.set(i);
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param point DOCUMENT ME!
     */
    public void pointDragged(Point2D point) {
        //Point2D[] points = geometry.getPoints();

        for (int j = index.nextSetBit(0); j >= 0;
                j = index.nextSetBit(j + 1)) {
            if (getRotation() != 0) {
                Rectangle2D.Double rect = getBoundBox();
                AffineTransform af = AffineTransform.getRotateInstance(Math.toRadians(
                            -getRotation()), rect.x + (rect.width / 2),
                        rect.y + (rect.height / 2));
                af.transform(point, point);
            }

            //points[j] = new Point2D.Double(point.getX(), point.getY());
            geometryEdit.changePoint(j,point);
        }
        //geometryEdit.setPoints(points);
        geometryEdit.end();
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param at DOCUMENT ME!
     */
    public void paint(Graphics2D g, AffineTransform at) {
        if (geometryEdit != null) {
            Rectangle2D.Double rect = getBoundingBox(at);
            if (getRotation()!=0){
            g.rotate(Math.toRadians(getRotation()), rect.x + (rect.width / 2),
                rect.y + (rect.height / 2));
            geometryEdit.paint(g,at,false);
          /*  FShape m_shape = null;
            GeneralPathX polyLine = new GeneralPathX(geometryEdit.getShape());
            polyLine.transform(at);
            m_shape = new FPolyline2D(polyLine);
            FGraphicUtilities.DrawShape(g, mT, m_shape, m_symbol);
*/
            g.rotate(Math.toRadians(-getRotation()), rect.x + (rect.width / 2),
                rect.y + (rect.height / 2));
            }else{
                 geometryEdit.paint(g,at,false);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public GeometryAdapter getGeometry() {
        return geometryEdit;
    }

    public void initialize() {
        // TODO Auto-generated method stub

    }
     /**
     * Devuelve un entero que representa donde esta contenido el punto que se
     * pasa como parámetro.
     *
     * @param p punto a comparar.
     *
     * @return entero que representa como esta contenido el punto.
     */
    public int getContains(Point2D p) {
        if (geometry instanceof CircleAdapter) {
            if (ne.contains(p.getX(), p.getY())) {
                return NE;
            } else if (se.contains(p.getX(), p.getY())) {
                return SE;
            } else if (so.contains(p.getX(), p.getY())) {
                return SO;
            } else if (no.contains(p.getX(), p.getY())) {
                return NO;
            } else if (getBoundingBox(null).contains(p.getX(), p.getY())) {
                return RECT;
            }
            return NOSELECT;
        }
            return super.getContains(p);
    }

    /**
     * Dibuja los handlers sobre el boundingBox en el graphics que se pasa como
     * parámetro.
     *
     * @param g
     *            Graphics sobre el que dibujar.
     */
    public void drawHandlers(Graphics2D g) {
        if (geometry instanceof CircleAdapter) {
            int size = 10;
            Rectangle2D r = getBoundingBox(null);
            Point2D p = new Point2D.Double();
            g.rotate(Math.toRadians(getRotation()), r.getX()
                    + (r.getWidth() / 2), r.getY() + (r.getHeight() / 2));

            AffineTransform atRotate = new AffineTransform();
            atRotate.rotate(Math.toRadians(getRotation()), r.getX()
                    + (r.getWidth() / 2), r.getY() + (r.getHeight() / 2));

            g.fillRect((int) r.getX() - size, (int) r.getY() - size,
                            size, size);
            atRotate.transform(new Point2D.Double(r.getX() - size, r.getY()
                    - size), p);
            no.setRect((int) p.getX(), (int) p.getY(), size, size);

            g.fillRect((int) r.getMaxX(), (int) r.getY() - size, size, size);
            atRotate.transform(
                    new Point2D.Double(r.getMaxX(), r.getY() - size), p);
            ne.setRect((int) p.getX(), (int) p.getY(), size, size);

            g.fillRect((int) r.getX() - size, (int) r.getMaxY(), size, size);
            atRotate.transform(
                    new Point2D.Double(r.getX() - size, r.getMaxY()), p);
            so.setRect((int) p.getX(), (int) p.getY(), size, size);

            g.fillRect((int) r.getMaxX(), (int) r.getMaxY(), size, size);
            atRotate.transform(new Point2D.Double(r.getMaxX(), r.getMaxY()), p);
            se.setRect((int) p.getX(), (int) p.getY(), size, size);

            g.rotate(Math.toRadians(-getRotation()), r.getX()
                    + (r.getWidth() / 2), r.getY() + (r.getHeight() / 2));
        } else {
			super.drawHandlers(g);
		}
    }

	public void cloneActions(IFFrame frame) {
		// TODO Auto-generated method stub

	}

	public IFFrameDialog getPropertyDialog() {
		return new FFrameGraphicsDialog(getLayout(),this);
	}

	public void print(Graphics2D g, AffineTransform at, Geometry geom,
			PrintAttributes printingProperties) {
		Rectangle2D.Double rect = getBoundingBox(at);
		g.rotate(Math.toRadians(getRotation()), rect.x + (rect.width / 2),
				rect.y + (rect.height / 2));
		g.setColor(Color.black);

//		if (m_type == POINT) {
//			((IMarkerSymbol)m_symbol).setSize((int) (rect.getWidth() * 0.7));
//		}
		geometry.print(g, at, m_symbol,printingProperties);


		if (editing) {
			g.setColor(Color.red);
			geometry.drawVertex(g, at);
		}

		g.rotate(Math.toRadians(-getRotation()), rect.x + (rect.width / 2),
				rect.y + (rect.height / 2));
	}


}
