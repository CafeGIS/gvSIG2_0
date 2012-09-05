/*
 * Created on 20-feb-2004
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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;

import org.gvsig.compat.print.PrintAttributes;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.mapcontext.MapContext;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.exceptions.SaveException;
import com.iver.cit.gvsig.project.documents.layout.Attributes;
import com.iver.cit.gvsig.project.documents.layout.FLayoutUtilities;
import com.iver.cit.gvsig.project.documents.layout.fframes.gui.dialogs.FFrameScaleBarDialog;
import com.iver.cit.gvsig.project.documents.layout.fframes.gui.dialogs.IFFrameDialog;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;
import com.iver.utiles.StringUtilities;
import com.iver.utiles.XMLEntity;



/**
 * FFrame para introducir una barra de escala en el Layout.
 *
 * @author Vicente Caballero Navarro
 */
public class FFrameScaleBar extends FFrame implements IFFrameViewDependence {
    private static final int NUMERO = 0;
    private static final int BARRA1 = 1;
    private static final int BARRA2 = 2;
    private static final int BARRA3 = 3;
    private static final int BARRA4 = 4;
    private double DIFDOWN = 1.5;
    private double DIFL = 30;
    private double DIFR = 30;
    private double DIFUP = 10;
    private boolean m_bIntervalSet = false;
    private int m_style = NUMERO;
    private int m_units = 1; //Metros
    private int m_mapUnits = 1; //unidad de medida de la vista(Metros)
    private double m_interval = 1;
    private int m_numinterval = 3;
    private int m_numleft = 2;
    private double m_height = 0;
    private FFrameView fframeview = null;
    private double m_typeUnit = Attributes.CHANGE[1]; //METROS;
    private String m_nameUnit = null;
    private double m_numUnit = 0;
    private double m_dif = 1;
    private int m_hasleft = 0;
    private Font m_f = new Font("SansSerif", Font.PLAIN, 9);
    private Color barcolor = Color.black;
    private Color textcolor = Color.black;
    private boolean showNameUnits = true;
    private boolean showDescription = false;
    private boolean aboveName = false;
    private boolean aboveIntervals = true;
    private boolean aboveDescription = false;
    private int dependenceIndex = -1;
    private int numDec = 0;
    public static NumberFormat numberFormat = NumberFormat.getInstance();

    /**
     * Creates a new FFrameScaleBar object.
     */
    public FFrameScaleBar() {
    }
    public void refreshDependence(IFFrame fant, IFFrame fnew) {
    	if ((fframeview != null) &&
                fframeview.equals(fant)) {
            fframeview=(FFrameView)fnew;
            if (fframeview==null) {
				return;
			}
            setMapUnits(fframeview.getMapUnits());
            setHeight(fnew.getBoundBox().height);
    	}
    }
    /**
     * Inserta el FFrameView sobre el que obtener la escala gráfica.
     *
     * @param f FFrameView sobre  el que obtener la escala.
     */
    public void setFFrameDependence(IFFrame f) {
        fframeview = (FFrameView) f;
        if (fframeview==null) {
			return;
		}
        setMapUnits(fframeview.getMapUnits());
        setHeight(f.getBoundBox().height);
    }

    /**
     * Devuelve el FFrameView que se representa con la escala.
     *
     * @return FFrameView a representar.
     */
    public IFFrame[] getFFrameDependence() {
        return new IFFrame[]{fframeview};
    }

    /**
     * Devuelve el FMap de la vista que se representa en la escala.
     *
     * @return FMap.
     */
    public MapContext getFMap() {
        return fframeview.getMapContext();
    }

    /**
     * Rellenar el estilo de barra de escala que se elige.
     *
     * @param s entero que representa el tipo de barra seleccionada.
     */
    public void setStyle(int s) {
        m_style = s;
    }

    /**
     * Devuelve el tipo de escala a dibujar.
     *
     * @return entero.
     */
    public int getStyle() {
        return m_style;
    }

    /**
     * Rellenar el tamaño de un intervalo.
     *
     * @param s tamaño de un intervalo.
     */
    public void setInterval(double s) {
        m_interval = s;

        if (m_numleft != 0) {
            m_hasleft = 1;
        }

        Rectangle2D.Double rect = getBoundBox();
        double difL = (rect.width / DIFL);
        double difR = (rect.width / DIFR);
        double n = (rect.width - difL - difR);
        m_numUnit = (m_interval * m_typeUnit) / getScaleView();
        m_numinterval = (int) (n / m_numUnit) - m_hasleft;
        m_dif = 1;
    }

    /**
     * Devuelve el valor del intervalo.
     *
     * @return long.
     */
    public String obtainInterval() {
        if (fframeview != null) {
            Rectangle2D.Double rect = getBoundBox();

            if (m_numleft != 0) {
                m_hasleft = 1;
            }

            double difL = (rect.width / DIFL);
            double difR = (rect.width / DIFR);
            double n = (rect.width - difL - difR);
            m_numUnit = n / (m_numinterval + m_hasleft);

            double scaleXunit = (m_numUnit * getScaleView()) / m_typeUnit;
            m_dif = getExact(scaleXunit);
            m_numUnit = m_numUnit * m_dif;
            m_interval = scaleXunit;

            ///m_interval = (scaleXunit * m_dif);
            return format(m_interval);
        }

        return "0";
    }

    /**
     * Rellenar el número de intervalos.
     *
     * @param s número de intervalos.
     */
    public void setNumInterval(int s) {
        m_numinterval = s;

        if (m_numleft != 0) {
            m_hasleft = 1;
        }

        Rectangle2D.Double rect = getBoundBox();
        double difL = (rect.width / DIFL);
        double difR = (rect.width / DIFR);
        double n = (rect.width - difL - difR);
        m_numUnit = n / (m_numinterval + m_hasleft);

        double scaleXunit = (m_numUnit * getScaleView()) / m_typeUnit;
        m_dif = getExact(scaleXunit);
        m_numUnit = m_numUnit * m_dif;
        m_interval = (scaleXunit * m_dif);
    }

    /**
     * Devuelve el número de intervalos por encima del cero.
     *
     * @return entero.
     */
    public int getNumInterval() {
        return m_numinterval;
    }

    /**
     * Rellenar el número de intervalos a la izquierda del cero.
     *
     * @param s número de intervalos a la izquierda.
     */
    public void setNumLeft(int s) {
        m_numleft = s;
        if (m_numleft != 0) {
            m_hasleft = 1;
        }else{
        	m_hasleft=0;
        }
    }

    /**
     * Devuelve el número de intervalos  en los que se quiere particionar el
     * intervalo  que se queda por debajo del cero.
     *
     * @return entero.
     */
    public int getNumLeft() {
        return m_numleft;
    }

    /**
     * Seleccionar ,a true, si se quiere o, a false, si no mantener los
     * intervalos.
     *
     * @param b boolean a true si se quiere mantener los intervalos.
     */
    public void setIntervalSet(boolean b) {
        m_bIntervalSet = b;
    }

    /**
     * Seleccionar la unidad de medida a representar en la barra de escala.
     *
     * @param s entero que representa la unidad de medida que se selecciona.
     */
    public void setUnits(int s) {
        m_units = s;
        m_typeUnit =  MapContext.getDistanceTrans2Meter()[s]*100;//Attributes.CHANGE[s];
        m_nameUnit = PluginServices.getText(this,MapContext.getDistanceNames()[s]);
    }

    /**
     * Devuelve un entero que representa el tipo de unidades que representamos.
     *
     * @return entero.
     */
    public int getUnits() {
        return m_units;
    }

    /**
     * Devuelve el long que representa el intervalo.
     *
     * @return Intervalo.
     */
    public String getInterval() {
        return format(m_interval);
    }

    /**
     * Rellenar el rectángulo de la vista sobre la que se obtiene la escala.
     *
     * @param d Rectángulo.
     */
    public void setHeight(double d) {
        m_height = d;
    }

    /**
     * Rellena la unidad de medida en la que está la vista.
     *
     * @param i entero que representa la unidad de medida de la vista.
     */
    public void setMapUnits(int i) {
        m_mapUnits = i;
    }

    /**
     * Devuelve la escala de la vista seleccionada.
     *
     * @return la escala de la vista.
     */
    private long getScaleView() {
        if (fframeview == null) {
            return 1;
        }

        return fframeview.getScale();
    }

    /**
     * Método que dibuja sobre el graphics que se le pasa como parámetro, según
     * la transformada afin que se debe de aplicar y el rectángulo que se debe
     * de dibujar.
     *
     * @param g Graphics
     * @param at Transformada afin.
     * @param rv rectángulo sobre el que hacer un clip.
     * @param imgBase Image para acelerar el dibujado.
     */
    public void draw(Graphics2D g, AffineTransform at, Rectangle2D rv,
        BufferedImage imgBase) {
        Rectangle2D.Double r = getBoundingBox(at);
        g.rotate(Math.toRadians(getRotation()), r.x + (r.width / 2),
            r.y + (r.height / 2));

        if (intersects(rv, r)) {
            if ((fframeview == null) || (fframeview.getMapContext() == null)) {
                drawEmpty(g);
            } else {
                switch (m_style) {
                    case (NUMERO):

                        double scalex = r.getWidth() / (8);

                        if (scalex > (r.getHeight() / (8))) {
                            scalex = r.getHeight() / (2);
                        }

                        g.setColor(textcolor);

                        if (m_f != null) {
                            m_f = new Font(m_f.getFontName(), m_f.getStyle(),
                                    (int) (scalex));
                            g.setFont(m_f);
                        }

                        FontMetrics fm = g.getFontMetrics();
                        double d = r.getWidth();
                        long scl=getScaleView();
                        if (scl<1){
                            String unknowScale=PluginServices.getText(this,"escala_desconocida");
                            if (fm.stringWidth(unknowScale) > (d * 0.8)) {
                                double dif = fm.stringWidth(unknowScale) / (d * 0.8);
                                m_f = new Font(m_f.getName(), m_f.getStyle(),
                                        (int) (m_f.getSize() / dif));
                                g.setFont(m_f);
                            }
                               g.drawString(unknowScale, (int) r.x,
                                    (int) (r.y + (r.height / 2)));
                               return;
                        }
                        String scale = " 1:" + scl;

                        if (fm.stringWidth(String.valueOf(m_interval)) > (d * 0.8)) {
                            double dif = fm.stringWidth(String.valueOf(
                                        m_interval)) / (d * 0.8);
                            m_f = new Font(m_f.getName(), m_f.getStyle(),
                                    (int) (m_f.getSize() / dif));
                            g.setFont(m_f);
                        }

                        g.drawString(scale, (int) r.x,
                            (int) (r.y + (r.height / 2)));

                        break;

                    case (BARRA1):
                    case (BARRA2):
                    case (BARRA3):
                    case (BARRA4):
                        drawBar(m_style, g, at);

                        break;
                }
            }
        }

        g.rotate(Math.toRadians(-getRotation()), r.x + (r.width / 2),
            r.y + (r.height / 2));
    }

    /**
     * Rellena con el rectángulo que se pasa como parámetro el boundBox(en
     * centímetros) del fframe del cual con una transformación se podrá
     * calcular el BoundingBox (en pixels).
     *
     * @param r Rectángulo en centímetros.
     */
    public void setBoundBox(Rectangle2D.Double r) {
        if ((m_numUnit < 1) || (fframeview == null) ||
                (fframeview.getMapContext() == null)) {
            super.setBoundBox(r);

            return;
        }

        double difL = (r.width / DIFL);
        double difR = (r.width / DIFR);

        if (m_bIntervalSet) {
            m_numinterval = (int) (((r.width - (difL + difR)) * m_dif) / m_numUnit) -
                m_hasleft;
        }

        if (m_numinterval < 1) {
            m_numinterval = 1;
            r.width = ((m_numinterval + m_hasleft) * m_numUnit) + difL + difR;
        }

        getBoundBox().setRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    /**
     * Dibuja sobre el Graphics la escala gráfica.
     *
     * @param type Tipo de barra.
     * @param g Graphics sobre el que dibujar.
     * @param at Matriz de transformación.
     */
    private void drawBar(int type, Graphics2D g, AffineTransform at) {
        Rectangle2D.Double rect = getBoundBox();
        Rectangle2D.Double r = getBoundingBox(at);
        double numleft = m_numleft;
        initDistances();
        //drawOrder(g,r);
        double difDown = (rect.height / DIFDOWN);
        double difUp = (rect.height / DIFUP);
        double difL = (rect.width / DIFL);
        double difR = (rect.width / DIFR);
        double n = (rect.width - difL - difR);

        //setDescripcion("escala 1:" + String.valueOf(fframeview.getScale()));
        g.setStroke(new BasicStroke(0));

        if (!m_bIntervalSet) {
            m_numUnit = n / (m_numinterval + m_hasleft);

            double scaleXunit = (m_numUnit * getScaleView()) / m_typeUnit;
            m_dif = getExact(scaleXunit);
            m_numUnit = m_numUnit * m_dif;
            m_interval = (scaleXunit * m_dif);
        }

        if (m_bIntervalSet) {
            m_numUnit = (m_interval * m_typeUnit) / (m_dif * getScaleView());
            m_numinterval = (int) (((rect.width - (difL + difR)) * m_dif) / m_numUnit) -
                m_hasleft;
        }

        if (m_numinterval < 1) {
            m_numinterval = 1;
            rect.width = ((m_numinterval + m_hasleft) * m_numUnit) + difL +
                difR;
        }

        double h = 0;

        if (type == BARRA1) {
            h = (rect.height - (difUp + difDown));
        } else if ((type == BARRA2) || (type == BARRA3)) {
            h = (rect.height - (difUp + difDown)) / 2;
        }

        //Dibujar el rectángulo que bordea todo.
        Rectangle2D.Double rectotal = (FLayoutUtilities.fromSheetRect(new Rectangle2D.Double((rect.x +
                    difL), (rect.y + difUp),
                    m_numUnit * (m_hasleft + m_numinterval), h), at));
        g.setColor(barcolor);
        g.fillRect((int) rectotal.x, (int) rectotal.y, (int) (rectotal.width),
            (int) rectotal.height);
        g.setColor(Color.white);
        g.fillRect((int) rectotal.x+2, (int) rectotal.y+2, (int) (rectotal.width-3),
                (int) rectotal.height-4);
        g.setColor(barcolor);

        if (m_f != null) {
            m_f = new Font(m_f.getFontName(), m_f.getStyle(),
                    (int) (r.getHeight() / 4));
        } else {
            m_f = new Font("SansSerif", Font.PLAIN, (int) (r.getHeight() / 4));
        }

        g.setFont(m_f);
        Double interval=new Double(m_interval);
        if (interval.isNaN()) {
            String unknowScale=PluginServices.getText(this,"escala_desconocida");
            FontMetrics fm = g.getFontMetrics();
             double d = r.getWidth();
            if (fm.stringWidth(unknowScale) > (d * 0.8)) {
                double dif = fm.stringWidth(unknowScale) / (d * 0.8);
                m_f = new Font(m_f.getName(), m_f.getStyle(),
                        (int) (m_f.getSize() / dif));
                g.setFont(m_f);
            }
               g.drawString(unknowScale, (int) r.x,
                    (int) (r.y + (r.height / 2)));
               return;
        }

        FontMetrics fm = g.getFontMetrics();
        String formatInterval = format(m_interval);
        double d = (rectotal.getWidth() / m_numinterval) + m_hasleft;
        double difpos = ((r.getHeight() / 4) * formatInterval.length()) / 4;

        if (fm.stringWidth(formatInterval) > (d * 0.7)) {
            double dif = fm.stringWidth(formatInterval) / (d * 0.7);
            difpos = (d * 0.7) / 2;
            m_f = new Font(m_f.getName(), m_f.getStyle(),
                    (int) (m_f.getSize() / dif));
            g.setFont(m_f);
        }

        //Derecha del cero
        for (int i = 0; i < m_numinterval; i++) {
            Rectangle2D.Double recder = FLayoutUtilities.fromSheetRect(new Rectangle2D.Double((rect.x +
                        (difL) + ((m_numUnit * i) + (m_hasleft * m_numUnit))),
                        (rect.y + (difUp)), (m_numUnit),
                        (rect.height - (difUp + difDown)) / 2), at);
            Rectangle2D.Double recderB = FLayoutUtilities.fromSheetRect(new Rectangle2D.Double((rect.x +
                        (difL) + ((m_numUnit * i) + (m_hasleft * m_numUnit))),
                        (rect.y + difUp) +
                        ((rect.height - (difUp + difDown)) / 2), (m_numUnit),
                        ((rect.height - (difUp + difDown)) / 2)), at);

            //			Corrección cuando la altura en pixels del rectángulo es impar.
            rectotal.y = recder.y;
            recderB.height = rectotal.height - recder.height + 0.5;

            /**
             * if (i == (m_numinterval - 1)) { if (recder.getMaxX() !=
             * rectotal.getMaxX()) { recder.width = (recder.width +
             * recder.getMaxX()) - rectotal.getMaxX(); } if (recderB.getMaxX()
             * != rectotal.getMaxX()) { recderB.width = (recderB.width +
             * recderB.getMaxX()) - rectotal.getMaxX(); }} if
             * (recderB.getMaxY() != rectotal.getMaxY()) { recderB.height =
             * (recderB.height + recderB.getMaxY()) - rectotal.getMaxY(); }
             */
            if ((m_numleft % 2) == 0) {
                if ((i % 2) == 0) {
                    if ((type == BARRA1) || (type == BARRA2)) {
                        fillRect(g, recder);
                    } else if (type == BARRA3) {
                        g.drawRect((int) recder.x, (int) recder.y,
                            (int) recder.width, (int) recder.height);
                    }
                } else if (type == BARRA1) {
                    fillRect(g, recderB);
                }
            } else {
                if ((i % 2) != 0) {
                    if ((type == BARRA1) || (type == BARRA2)) {
                        fillRect(g, recder);
                    } else if (type == BARRA3) {
                        g.drawRect((int) recderB.x, (int) recderB.y,
                            (int) recderB.width, (int) recderB.height);
                    }
                } else if (type == BARRA1) {
                    fillRect(g, recderB);
                }
            }

            String interString = format(m_interval * i);
            Point2D.Double p = null;

            if (isAboveIntervals()) {
                p = new Point2D.Double(recder.x - difpos,
                        r.getMinY() + (r.getHeight() / DIFUP));
            } else {
                p = new Point2D.Double(recder.x - difpos,
                        ((r.getHeight() / 4) + r.getMaxY()) -
                        (r.getHeight() / DIFDOWN));
            }

            //Para dibujar el 0 centrado en su sitio.
            if (i == 0) {
                double dif0 = recder.x - (fm.stringWidth(interString) / 2);
                p = new Point2D.Double(dif0, p.getY());
            }

            drawInterval(g, interString, p);
        }

        //Último número a dibujar.
        String interString = format(m_interval * m_numinterval);

        Point2D.Double p = null;

        if (isAboveIntervals()) {
            p = new Point2D.Double(rectotal.getMaxX() - difpos,
                    r.getMinY() + (r.getHeight() / DIFUP));
        } else {
            p = new Point2D.Double(rectotal.getMaxX() - difpos,
                    ((r.getHeight() / 4) + r.getMaxY()) -
                    (r.getHeight() / DIFDOWN));
        }

        drawInterval(g, interString, p);

        //Izquierda del cero
        for (int i = 0; i < m_numleft; i++) {
            Rectangle2D.Double reciz = FLayoutUtilities.fromSheetRect(new Rectangle2D.Double((rect.x +
                        difL + ((m_numUnit / m_numleft) * i)),
                        (rect.y + difUp), (m_numUnit / numleft),
                        (rect.height - (difUp + difDown)) / 2), at);
            Rectangle2D.Double recizB = FLayoutUtilities.fromSheetRect(new Rectangle2D.Double((rect.x +
                        difL + ((m_numUnit / m_numleft) * i)),
                        (rect.y + difUp) +
                        ((rect.height - (difUp + difDown)) / 2),
                        (m_numUnit / numleft),
                        ((rect.height - (difUp + difDown)) / 2)), at);

            //Corrección cuando la altura en pixels del rectangulo es impar.
            reciz.y = rectotal.y;
            recizB.height = rectotal.height - reciz.height + 0.5;

            if ((i % 2) == 0) {
                if ((type == BARRA1) || (type == BARRA2)) {
                    fillRect(g, reciz);
                } else if (type == BARRA3) {
                    g.drawRect((int) reciz.x, (int) reciz.y, (int) reciz.width,
                        (int) reciz.height);
                }
            } else if (type == BARRA1) {
                fillRect(g, recizB);
            }
        }

        if (m_numleft > 0) {
            interString = format(m_interval);

            if (isAboveIntervals()) {
                p = new Point2D.Double(rectotal.x - difpos,
                        r.getMinY() + (r.getHeight() / DIFUP));
            } else {
                p = new Point2D.Double(rectotal.x - difpos,
                        ((r.getHeight() / 4) + r.getMaxY()) -
                        (r.getHeight() / DIFDOWN));
            }

            drawInterval(g, interString, p);
        }

        //En el caso de que se pida como númro de intervalos a la izquierda del 0, se reajusta el tamaño del rectángulo exterior de la escala gráfica.
        if (m_numleft == 0) {
            Rectangle2D.Double recAux = FLayoutUtilities.fromSheetRect(new Rectangle2D.Double((rect.x +
                        difL + ((m_numUnit / 1) * 0)), (rect.y + difUp),
                        (m_numUnit / 1), (rect.height - (difUp + difDown)) / 2),
                    at);
            rectotal.x = rectotal.x + recAux.width;
            rectotal.width = rectotal.width - recAux.width;
        }

        //Se dibuja el rectángulo que bordea toda la escala gráfica.
        g.drawRect((int) rectotal.x, (int) rectotal.y, (int) (rectotal.width),
            (int) rectotal.height);

        drawNameUnitsAndDescriptions(g, r);
        //drawDescription(g, r);
    }

    /**
     * Inicializa las distancias de la barra de escala.
     */
    private void initDistances() {
        int numUp = 0;
        int numDown = 0;

        DIFDOWN = 30;
        DIFL = 40;
        DIFR = 40;
        DIFUP = 30;

        if (isAboveName()) {
            numUp++;
        } else {
            numDown++;
        }

        if (isAboveIntervals()) {
            numUp++;
        } else {
            numDown++;
        }

        if (isAboveDescription()) {
            numUp++;
        } else {
            numDown++;
        }

        if (numDown == 1) {
            DIFDOWN = 3;
        } else if (numDown == 2) {
            DIFDOWN = 2;
        } else if (numDown == 3) {
            DIFDOWN = 1.2;
        }

        if (numUp == 1) {
            DIFUP = 3;
        } else if (numUp == 2) {
            DIFUP = 2;
        } else if (numUp == 3) {
            DIFUP = 1.2;
        }
    }

    /**
     * Dibuja sobre el Graphics el nombre de la unidad de medida de la escala y la descripción,
     * siendo por defecto la escala 1:.
     *
     * @param g Graphics sobre el que se dibuja.
     * @param rec Rectángulo que ocupa la escala gráfica.
     */
    private void drawNameUnitsAndDescriptions(Graphics g, Rectangle2D.Double rec) {
        FontMetrics fm = g.getFontMetrics();
        Point2D.Double pD = null;
        Point2D.Double pU = null;
        g.setColor(textcolor);
               switch (getFormat()) {
               case DUIB:
                   pD = new Point2D.Double(rec.getCenterX() -
                        (fm.stringWidth(getDescription()) / 2),
                        (rec.getMinY() + (rec.getHeight() / 9) +
                        (fm.getHeight() / 2)));
                   pU = new Point2D.Double(rec.getCenterX() -
                           (fm.stringWidth(m_nameUnit) / 2),
                           (rec.getMinY() + (rec.getHeight() / 3) +
                           (fm.getHeight() / 2)));

                   break;
               case DUBI:
                   pD = new Point2D.Double(rec.getCenterX() -
                        (fm.stringWidth(getDescription()) / 2),
                        (rec.getMinY() + (rec.getHeight() / 9) +
                        (fm.getHeight() / 2)));
                   pU = new Point2D.Double(rec.getCenterX() -
                           (fm.stringWidth(m_nameUnit) / 2),
                           (rec.getMinY() + (rec.getHeight() / 3) +
                           (fm.getHeight() / 2)));
                   break;
               case DBIU:
                   pD = new Point2D.Double(rec.getCenterX() -
                        (fm.stringWidth(getDescription()) / 2),
                        (rec.getMinY() + (rec.getHeight() / 9) +
                        (fm.getHeight() / 2)));
                   pU = new Point2D.Double(rec.getCenterX() -
                            (fm.stringWidth(m_nameUnit) / 2),
                            (rec.getMaxY() - (rec.getHeight() / 9) +
                            (fm.getHeight() / 3)));
                   break;
               case DIBU:
                   pD = new Point2D.Double(rec.getCenterX() -
                        (fm.stringWidth(getDescription()) / 2),
                        (rec.getMinY() + (rec.getHeight() / 9) +
                        (fm.getHeight() / 2)));
                   pU = new Point2D.Double(rec.getCenterX() -
                            (fm.stringWidth(m_nameUnit) / 2),
                            (rec.getMaxY() - (rec.getHeight() / 9) +
                            (fm.getHeight() / 3)));
                   break;
               case UIBD:
                   pD = new Point2D.Double(rec.getCenterX() -
                            (fm.stringWidth(getDescription()) / 2),
                            (rec.getMaxY() - (rec.getHeight() / 9) +
                            (fm.getHeight() / 3)));
                   pU = new Point2D.Double(rec.getCenterX() -
                           (fm.stringWidth(m_nameUnit) / 2),
                           (rec.getMinY() + (rec.getHeight() / 9) +
                           (fm.getHeight() / 2)));
                   break;
               case UBID:
                   pD = new Point2D.Double(rec.getCenterX() -
                        (fm.stringWidth(getDescription()) / 2),
                        (rec.getMaxY() - (rec.getHeight() / 9) +
                        (fm.getHeight() / 3)));
                   pU = new Point2D.Double(rec.getCenterX() -
                           (fm.stringWidth(m_nameUnit) / 2),
                           (rec.getMinY() + (rec.getHeight() / 9) +
                           (fm.getHeight() / 2)));
                   break;
               case IBUD:
                   pD = new Point2D.Double(rec.getCenterX() -
                        (fm.stringWidth(getDescription()) / 2),
                        (rec.getMaxY() - (rec.getHeight() / 3) +
                        (fm.getHeight() / 3)));
                   pU = new Point2D.Double(rec.getCenterX() -
                            (fm.stringWidth(m_nameUnit) / 2),
                            (rec.getMaxY() - (rec.getHeight() / 9) +
                            (fm.getHeight() / 3)));

                   break;
               case BIUD:
                   pD = new Point2D.Double(rec.getCenterX() -
                        (fm.stringWidth(getDescription()) / 2),
                        (rec.getMaxY() - (rec.getHeight() / 3) +
                        (fm.getHeight() / 3)));
                   pU = new Point2D.Double(rec.getCenterX() -
                            (fm.stringWidth(m_nameUnit) / 2),
                            (rec.getMaxY() - (rec.getHeight() / 9) +
                            (fm.getHeight() / 3)));
                   break;
               }
               if (isShowNameUnits()) {
                   g.drawString(m_nameUnit, (int) pU.x, (int) pU.y);
               }
            if (isShowDescription()) {
                g.drawString(getDescription(), (int) pD.x, (int) pD.y);
            }
    }
    /**
     * Rellena la fuente utilizada para dibujar los intervalos y la unidad de
     * medida utilizada.
     *
     * @param f fuente a utilizar.
     */
    public void setFont(Font f) {
        m_f = f;
    }

    /**
     * Devuelve la fuente con la que se está dibujando sobre el graphics.
     *
     * @return fuente utilizada.
     */
    public Font getFont() {
        return new Font(m_f.getFontName(), m_f.getStyle(), 9);
    }

    /**
     * Devuelve si el intervalo es variable o si por el contrario es fijo.
     *
     * @return true si el intervalo es fijo.
     */
    public boolean isbIntervalSet() {
        return m_bIntervalSet;
    }

    /**
     * Especificar si el intervalo se debe mantener o es variable.
     *
     * @param b si se quiere mantener el intervalo especificado.
     */
    public void setbIntervalSet(boolean b) {
        m_bIntervalSet = b;
    }

    /**
     * Devuelve el porcentaje por el cual hay que multiplicar  el intervalo
     * para conseguir un intervalo redondeado,  de momento con una cifra
     * significativas(NUM=1).
     *
     * @param total intervalo.
     *
     * @return Porcentaje
     */
    private double getExact(double total) {
    	int NUM = 1;
    	double t = 0;
        double dif = 1;
        Double d = new Double(total);
        Long l = new Long(d.longValue());
        int num = l.toString().length();
        t = ((long) (total / Math.pow(10, num - NUM)) * Math.pow(10, num - NUM));
        dif = t / total;

        if (dif == 0) {
            return 1;
        }

        return dif;
    }

    /**
     * Rellena un rectángulo.
     *
     * @param g Graphics sobre el que dibujar.
     * @param r Rectángulo a rellenar.
     */
    private void fillRect(Graphics2D g, Rectangle2D.Double r) {
        g.fillRect((int) r.x, (int) r.y, (int) r.width, (int) r.height);
    }

    /**
     * Escribe sobre el Graphics y en la posición indicada el tamaño del
     * intervalo.
     *
     * @param g Graphics sobre el que dibujar.
     * @param inter Valor del intervalo.
     * @param p Punto donde dibujarlo.
     */
    private void drawInterval(Graphics2D g, String inter, Point2D.Double p) {
        //Double l = new Double(inter);
        g.setColor(textcolor);
        g.drawString(inter, (int) p.x, (int) p.y);
        g.setColor(barcolor);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SaveException
     *
     * @see com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame#getXMLEntity()
     */
    public XMLEntity getXMLEntity() throws SaveException {
        XMLEntity xml = super.getXMLEntity();

        try {
//            xml.putProperty("type", Layout.RECTANGLESCALEBAR);
            xml.putProperty("m_bIntervalSet", m_bIntervalSet);
            xml.putProperty("m_dif", m_dif);
            xml.putProperty("m_hasleft", m_hasleft);
            xml.putProperty("m_nameUnit", m_nameUnit);
            xml.putProperty("m_numUnit", m_numUnit);

            xml.putProperty("m_height", m_height);

            xml.putProperty("m_style", m_style);
            xml.putProperty("m_units", m_units);
            xml.putProperty("m_interval", m_interval);
            xml.putProperty("m_numinterval", m_numinterval);
            xml.putProperty("m_numleft", m_numleft);
            xml.putProperty("m_mapUnits", m_mapUnits);
            xml.putProperty("fontName", m_f.getFontName());
            xml.putProperty("fontStyle", m_f.getStyle());
            xml.putProperty("numDec", numDec);
            xml.putProperty("m_units", m_units);

            if (fframeview != null) {
                Layout layout = fframeview.getLayout();
                IFFrame[] fframes = layout.getLayoutContext().getAllFFrames();

                for (int i = 0; i < fframes.length; i++) {
               	 if (fframeview.equals(fframes[i])) {
                       xml.putProperty("index", i);
                       break;
                   }
               }
            }

            xml.putProperty("barcolor", StringUtilities.color2String(barcolor));
            xml.putProperty("textcolor", StringUtilities.color2String(textcolor));
            xml.putProperty("showNameUnits", showNameUnits);
            xml.putProperty("showDescription", showDescription);
            xml.putProperty("aboveName", aboveName);
            xml.putProperty("aboveIntervals", aboveIntervals);
            xml.putProperty("aboveDescription", aboveDescription);
        } catch (Exception e) {
            throw new SaveException(e, this.getClass().getName());
        }

        return xml;
    }

    /**
     * @see com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame#setXMLEntity(com.iver.utiles.XMLEntity,
     *      com.iver.cit.gvsig.project.Project)
     */
    public void setXMLEntity03(XMLEntity xml, Layout l) {
        if (xml.getIntProperty("m_Selected") != 0) {
            this.setSelected(true);
        } else {
            this.setSelected(false);
        }

        this.m_bIntervalSet = xml.getBooleanProperty("m_bIntervalSet");
        this.m_dif = xml.getDoubleProperty("m_dif");
        this.m_hasleft = xml.getIntProperty("m_hasleft");
        this.m_nameUnit = xml.getStringProperty("m_nameUnit");
        this.m_numUnit = xml.getDoubleProperty("m_numUnit");

        this.m_height = xml.getDoubleProperty("m_height");

        this.m_style = xml.getIntProperty("m_style");
        this.m_interval = xml.getLongProperty("m_interval");
        this.m_numinterval = xml.getIntProperty("m_numinterval");
        this.m_numleft = xml.getIntProperty("m_numleft");
        this.m_mapUnits = xml.getIntProperty("m_mapUnits");
        this.m_f = new Font(xml.getStringProperty("fontName"),
                xml.getIntProperty("fontStyle"), 9);

        fframeview = (FFrameView) l.getLayoutContext().getFFrame(xml.getIntProperty("index"));

        if (xml.contains("description")) { //Comprobar que es de la versión que cambia el diálogo.
            this.barcolor = StringUtilities.string2Color(xml.getStringProperty(
                        "barcolor"));
            this.textcolor = StringUtilities.string2Color(xml.getStringProperty(
                        "textcolor"));
            this.showNameUnits = xml.getBooleanProperty("showNameUnits");
            this.showDescription = xml.getBooleanProperty("showDescription");
            this.aboveName = xml.getBooleanProperty("aboveName");
            this.aboveIntervals = xml.getBooleanProperty("aboveIntervals");
            this.aboveDescription = xml.getBooleanProperty("aboveDescription");
        }
    }

    /**
     * @see com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame#setXMLEntity(com.iver.utiles.XMLEntity,
     *      com.iver.cit.gvsig.project.Project)
     */
    public void setXMLEntity(XMLEntity xml) {
        if (xml.getIntProperty("m_Selected") != 0) {
            this.setSelected(true);
        } else {
            this.setSelected(false);
        }

        this.m_bIntervalSet = xml.getBooleanProperty("m_bIntervalSet");
        this.m_dif = xml.getDoubleProperty("m_dif");
        this.m_hasleft = xml.getIntProperty("m_hasleft");
        this.m_nameUnit = xml.getStringProperty("m_nameUnit");
        this.m_numUnit = xml.getDoubleProperty("m_numUnit");

        this.m_height = xml.getDoubleProperty("m_height");

        this.m_style = xml.getIntProperty("m_style");
        this.m_interval = xml.getDoubleProperty("m_interval");
        this.m_numinterval = xml.getIntProperty("m_numinterval");
        this.m_numleft = xml.getIntProperty("m_numleft");
        this.m_mapUnits = xml.getIntProperty("m_mapUnits");
        this.m_f = new Font(xml.getStringProperty("fontName"),
                xml.getIntProperty("fontStyle"), 9);
        setRotation(xml.getDoubleProperty("m_rotation"));

        if (xml.contains("m_units")) { //Comprobación por versión 0.5
            setUnits(xml.getIntProperty("m_units"));
        }

        if (xml.contains("index")) {
            dependenceIndex = xml.getIntProperty("index");
        }


        if (xml.contains("showDescription")) { //Comprobar que es de la versión que cambia el diálogo.
            this.barcolor = StringUtilities.string2Color(xml.getStringProperty(
            "barcolor"));

            this.textcolor = StringUtilities.string2Color(xml.getStringProperty(
                        "textcolor"));
            this.showNameUnits = xml.getBooleanProperty("showNameUnits");
            this.showDescription = xml.getBooleanProperty("showDescription");
            this.aboveName = xml.getBooleanProperty("aboveName");
            this.aboveIntervals = xml.getBooleanProperty("aboveIntervals");
            this.aboveDescription = xml.getBooleanProperty("aboveDescription");
        }

        if (xml.contains("numDec")) {
            setNumDec(xml.getIntProperty("numDec"));
        }
    }

    /**
     * @see com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame#getNameFFrame()
     */
    public String getNameFFrame() {
        return PluginServices.getText(this, "escala")+ num;
    }

    /**
     * Inserta el color de la escala gráfica.
     *
     * @param color Color de la escala gráfica.
     */
    public void setBarColor(Color color) {
        barcolor = color;
    }

    /**
     * Inserta el color del texto.
     *
     * @param color Color del texto.
     */
    public void setTextColor(Color color) {
        textcolor = color;
    }

    /**
     * Devuelve el color de la escala gráfica.
     *
     * @return Color de la escala gráfica.
     */
    public Color getBarColor() {
        return barcolor;
    }

    /**
     * Devuelve el color del texto.
     *
     * @return Color del texto.
     */
    public Color getTextColor() {
        return textcolor;
    }

    /**
     * Devuelve true si se debe mostrar el nombre de las unidades de medida de
     * la escala gráfica.
     *
     * @return True si se muestra las unidades de medida.
     */
    public boolean isShowNameUnits() {
        return showNameUnits;
    }

    /**
     * Inserta si se debe de mostrar el nombre de las unidades de medida o no.
     *
     * @param showNameUnits True si se muestra el nombre de las unidades de
     *        medida.
     */
    public void setShowNameUnits(boolean showNameUnits) {
        this.showNameUnits = showNameUnits;
    }

    /**
     * Devuelve true si se muestra la descripción a la parte de arriba de la
     * escala gráfica.
     *
     * @return True si se muestra arriba de la escala gráfica.
     */
    public boolean isAboveDescription() {
        return aboveDescription;
    }

    /**
     * Introduce true si se muestra arriba de la escala gráfica la descripción.
     *
     * @param aboveDescription True si se muestra arriba la descripción.
     */
    public void setAboveDescription(boolean aboveDescription) {
        this.aboveDescription = aboveDescription;
    }

    /**
     * Devuelve true si se muestran a la parte de arriba los valores de los
     * intervalos.
     *
     * @return True si se muestran arriba de la escala gráfica.
     */
    public boolean isAboveIntervals() {
        return aboveIntervals;
    }

    /**
     * Inserta si se muestran los valores de los intervalos a la parte de
     * arriba de la escala o debajo.
     *
     * @param aboveIntervals True si se muestran los valores de los intervalos
     *        a la parte de arriba de la escala gráfica.
     */
    public void setAboveIntervals(boolean aboveIntervals) {
        this.aboveIntervals = aboveIntervals;
    }

    /**
     * Devuelve si se muestra a la parte de arriba de la escala gráfica el
     * nombre de las unidades de medida o debajo.
     *
     * @return True si se muestra a la parte de arriba de la escala gráfica.
     */
    public boolean isAboveName() {
        return aboveName;
    }

    /**
     * Inserta si el nombre se muestra a la parte de arriba de la escala
     * gráfica o a la parte de abajo.
     *
     * @param aboveName True si se muestra a la parte de arriba de la escala
     *        gráfica.
     */
    public void setAboveName(boolean aboveName) {
        this.aboveName = aboveName;
    }

    /**
     * Devuelve si se debe mostrar la descripción o no.
     *
     * @return True si se muestra la descripción.
     */
    public boolean isShowDescription() {
        return showDescription;
    }

    /**
     * Inserta si se muestra o no la descripción.
     *
     * @param showDescription True si se muestra la descripción.
     */
    public void setShowDescription(boolean showDescription) {
        this.showDescription = showDescription;
    }

    /**
     * Devuelve la descripción de la escala.
     *
     * @return Descripción de la escala.
     */
    public String getDescription() {
        if (fframeview!=null){
        	if (fframeview.getScale()==0){
        		return PluginServices.getText(this,"escala_desconocida");
        	}
            return "1:" + String.valueOf(fframeview.getScale());
        }
            return "1:";
    }

    /**
     * @see com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame#print(java.awt.Graphics2D,
     *      java.awt.geom.AffineTransform)
     */
    public void print(Graphics2D g, AffineTransform at, Geometry geom,
			PrintAttributes properties) {
        draw(g, at, null, null);
    }

    /**
     * Update the dependences that have this FFrame with the other FFrame.
     *
     * @param fframes Other FFrames.
     */
    public void initDependence(IFFrame[] fframes) {
        if ((dependenceIndex != -1) &&
                fframes[dependenceIndex] instanceof FFrameView) {
            fframeview = (FFrameView) fframes[dependenceIndex];
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String format(double d) {
        if ((d % (long) d) != 0) {
            numberFormat.setMaximumFractionDigits(getNumDec());
        } else {
            numberFormat.setMaximumFractionDigits(0);
        }

        //String s = String.valueOf(nf.format(d));
        //s=s.replace('.','*');
        //s = s.replace(',', '.');
        //s=s.replace('*',',');
        return numberFormat.format(d); //(Double.valueOf(s).doubleValue());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNumDec() {
        return numDec;
    }

    /**
     * DOCUMENT ME!
     *
     * @param numDec DOCUMENT ME!
     */
    public void setNumDec(int numDec) {
        this.numDec = numDec;
    }

    public void initialize() {
        // TODO Auto-generated method stub

    }
    private final static int DUIB=0;
    private final static int DUBI=1;
    private final static int DBIU=2;
    private final static int DIBU=3;
    private final static int UIBD=4;
    private final static int UBID=5;
    private final static int IBUD=6;
    private final static int BIUD=7;

    private int getFormat(){
        if (isAboveDescription()){
            if (isAboveName()){
                if (isAboveIntervals()){
                    return DUIB;
                }
                return DUBI;
            }
            if (isAboveIntervals()){
                    return DIBU;
                }
                return DBIU;
        }
        if (isAboveName()){
            if (isAboveIntervals()){
                return UIBD;
            }
            return UBID;
        }
        if (isAboveIntervals()){
            return IBUD;
        }
        return BIUD;
    }

	public void cloneActions(IFFrame frame) {
		// TODO Auto-generated method stub

	}

	public IFFrameDialog getPropertyDialog() {
		return new FFrameScaleBarDialog(getLayout(),this);
	}
}
