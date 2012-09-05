/*
 * Created on 17-may-2004
 *
 */
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
package com.iver.cit.gvsig.project.documents.layout.fframes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.exceptions.OpenException;
import com.iver.cit.gvsig.project.documents.exceptions.SaveException;
import com.iver.cit.gvsig.project.documents.layout.FLayoutUtilities;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;
import com.iver.cit.gvsig.project.documents.layout.gui.dialogs.Tag;
import com.iver.utiles.XMLEntity;



/**
 * Clase que implementa la interface IFFrame con los m�todos por defecto de
 * todos los FFrames  que extenderan de este, dejando uno como m�todo
 * abstracto para implementar por todos los  FFrames para ser dibujados.
 *
 * @author Vicente Caballero Navarro
 */
public abstract class FFrame implements IFFrame {
    protected String m_name = "FFrame";
    protected Rectangle2D.Double m_BoundBox = new Rectangle2D.Double();
    private Rectangle2D.Double m_BoundingBox = new Rectangle2D.Double();
    protected int m_Selected = 0;
    protected Rectangle n = new Rectangle();
    protected Rectangle ne = new Rectangle();
    protected Rectangle e = new Rectangle();
    protected Rectangle se = new Rectangle();
    protected Rectangle s = new Rectangle();
    protected Rectangle so = new Rectangle();
    protected Rectangle o = new Rectangle();
    protected Rectangle no = new Rectangle();
    private String tag = null;
    protected int num = 0;
    private double m_rotation = 0;
    private int level = -1;
	private Rectangle2D lastMoveRect;
	private Layout layout;
	protected FrameFactory factory;
//	protected PrintRequestAttributeSet printingProperties;
	private static final Image iNEResize = PluginServices.getIconTheme()
		.get("neresize-icon").getImage();
	private static final Image iEResize = PluginServices.getIconTheme()
		.get("eresize-icon").getImage();
	private static final Image iNResize = PluginServices.getIconTheme()
		.get("nresize-icon").getImage();
	private static final Image iMove = PluginServices.getIconTheme()
		.get("move-icon").getImage();
	private static final Image iSEResize = PluginServices.getIconTheme()
		.get("sereresize-icon").getImage();

    /**
     * Dibuja los handlers sobre el boundingBox en el graphics que se pasa como
     * par�metro.
     *
     * @param g Graphics sobre el que dibujar.
     */
    public void drawHandlers(Graphics2D g) {
        int size = 10;
        Rectangle2D r = getBoundingBox(null);
        Point2D p = new Point2D.Double();
        g.rotate(Math.toRadians(getRotation()), r.getX() + (r.getWidth() / 2),
            r.getY() + (r.getHeight() / 2));

        AffineTransform atRotate = new AffineTransform();
        atRotate.rotate(Math.toRadians(getRotation()),
            r.getX() + (r.getWidth() / 2), r.getY() + (r.getHeight() / 2));

        g.fillRect((int) r.getX() - size, (int) r.getY() - size, size, size);
        atRotate.transform(new Point2D.Double(r.getX() - size, r.getY() - size),
            p);
        no.setRect((int) p.getX(), (int) p.getY(), size, size);

        g.fillRect((int) r.getMaxX(), (int) r.getY() - size, size, size);
        atRotate.transform(new Point2D.Double(r.getMaxX(), r.getY() - size), p);
        ne.setRect((int) p.getX(), (int) p.getY(), size, size);

        g.fillRect((int) r.getX() - size, (int) r.getMaxY(), size, size);
        atRotate.transform(new Point2D.Double(r.getX() - size, r.getMaxY()), p);
        so.setRect((int) p.getX(), (int) p.getY(), size, size);

        g.fillRect((int) r.getMaxX(), (int) r.getMaxY(), size, size);
        atRotate.transform(new Point2D.Double(r.getMaxX(), r.getMaxY()), p);
        se.setRect((int) p.getX(), (int) p.getY(), size, size);

        g.fillRect((int) r.getCenterX() - (size / 2), (int) r.getY() - size,
            size, size);
        atRotate.transform(new Point2D.Double(r.getCenterX() - (size / 2),
                r.getY() - size), p);
        n.setRect((int) p.getX(), (int) p.getY(), size, size);

        g.fillRect((int) r.getCenterX() - (size / 2), (int) r.getMaxY(), size,
            size);
        atRotate.transform(new Point2D.Double(r.getCenterX() - (size / 2),
                r.getMaxY()), p);
        s.setRect((int) p.getX(), (int) p.getY(), size, size);

        g.fillRect((int) r.getX() - size, (int) r.getCenterY() - (size / 2),
            size, size);
        atRotate.transform(new Point2D.Double(r.getX() - size,
                r.getCenterY() - (size / 2)), p);
        o.setRect((int) p.getX(), (int) p.getY(), size, size);

        g.fillRect((int) r.getMaxX(), (int) r.getCenterY() - (size / 2), size,
            size);
        atRotate.transform(new Point2D.Double(r.getMaxX(),
                r.getCenterY() - (size / 2)), p);
        e.setRect((int) p.getX(), (int) p.getY(), size, size);
        g.rotate(Math.toRadians(-getRotation()), r.getX() + (r.getWidth() / 2),
            r.getY() + (r.getHeight() / 2));
    }

    /**
     * Establece que tipo de selecci�n se realiza sobre el fframe.
     *
     * @param p punto sobre el que se debe de establecer si se selecciona o no
     *        el fframe.
     */
    public void setSelected(Point2D p,MouseEvent e) {
        m_Selected = getContains(p);
    }

    /**
     * Actualiza el BoundBox del FFrame a partir de su rect�ngulo en pixels y
     * la matriz de transformaci�n.
     *
     * @param r Rect�ngulo.
     * @param at Matriz de transformaci�n.
     */
    public void updateRect(Rectangle2D r, AffineTransform at) {
        Rectangle2D.Double rec = FLayoutUtilities.toSheetRect(r, at);
        rec.setRect((int) rec.getMinX(), (int) rec.getMinY(),
            (int) rec.getWidth(), (int) rec.getHeight());
        setBoundBox(rec);
    }

    /**
     * Devuelve el rect�ngulo a partir del desplazamiento en el eje x y el
     * desplazamiento en el eje y.
     *
     * @param difx desplazamiento sobre el eje x.
     * @param dify desplazamiento sobre el eje y.
     *
     * @return rect�ngulo modificado en funci�n del desplazamiento realizado.
     */
    public Rectangle2D getMovieRect(int difx, int dify) {
        double x = 0;
        double y = 0;
        double w = 0;
        double h = 0;

        lastMoveRect = new Rectangle2D.Double(this.getBoundingBox(
                    null).x, this.getBoundingBox(null).y,
                this.getBoundingBox(null).width,
                this.getBoundingBox(null).height);
        Rectangle2D.Double rec = this.getBoundingBox(null);
        int difn = 0;
        difn = difx;
        x = lastMoveRect.getX();
        y = lastMoveRect.getY();
        w = lastMoveRect.getWidth();
        h = lastMoveRect.getHeight();

        switch (this.getSelected()) {
            case (RECT):
                lastMoveRect.setRect((x + difx), (y + dify), w, h);

                break;

            case (N):

                if ((y + dify) > rec.getMaxY()) {
                    y = rec.getMaxY();
                } else {
                    y = y + dify;
                }

                lastMoveRect.setRect(x, y, w, Math.abs(h - dify));

                break;

            case (O):

                if ((x + difx) > rec.getMaxX()) {
                    x = rec.getMaxX();
                } else {
                    x = x + difx;
                }

                lastMoveRect.setRect(x, y, Math.abs(w - difx), h);

                break;

            case (S):

                if (y > (rec.getMaxY() + dify)) {
                    y = rec.getMaxY() + dify;
                }

                lastMoveRect.setRect(x, y, w, Math.abs(h + dify));

                break;

            case (E):

                if (x > (rec.getMaxX() + difx)) {
                    x = rec.getMaxX() + difx;
                }

                lastMoveRect.setRect(x, y, Math.abs(w + difx), h);

                break;

            case (NE):

                if ((y - difn) > rec.getMaxY()) {
                    y = rec.getMaxY();
                    x = rec.getMaxX() + difn;
                } else {
                    y = y - difn;
                }

                lastMoveRect.setRect(x, y, Math.abs(w + difn), Math.abs(h + difn));

                break;

            case (NO):

                if ((y + difn) > rec.getMaxY()) {
                    y = rec.getMaxY();
                    x = rec.getMaxX();
                } else {
                    x = x + difn;
                    y = y + difn;
                }

                lastMoveRect.setRect(x, y, Math.abs(w - difn), Math.abs(h - difn));

                break;

            case (SE):

                if (y > (rec.getMaxY() + difn)) {
                    y = rec.getMaxY() + difn;
                    x = rec.getMaxX() + difn;
                }

                lastMoveRect.setRect(x, y, Math.abs(w + difn), Math.abs(h + difn));

                break;

            case (SO):

                if ((x + difn) > rec.getMaxX()) {
                    x = rec.getMaxX();
                    y = rec.getMaxY() - difn;
                } else {
                    x = x + difn;
                }

                lastMoveRect.setRect(x, y, Math.abs(w - difn), Math.abs(h - difn));

                break;

            default:
                lastMoveRect.setRect((x), (y), w, h);
        }

        return lastMoveRect;
    }
    /**
     * Devuelve el rect�ngulo que representa el �ltimo generado al desplazar o modificar el tama�o del fframe.
     *
     * @return Rectangle2D
     *
     */
    public Rectangle2D getLastMoveRect(){
    	return lastMoveRect;
    }
    /**
     * Devuelve un entero que representa el tipo de selecci�n que se ha
     * realizado sobre el fframe.
     *
     * @return tipo de selecci�n que se ha realizado.
     */
    public int getSelected() {
        return m_Selected;
    }

    /**
     * Devuelve true, si el punto que se pasa como par�metro esta contenido
     * dentro del boundingbox del fframe.
     *
     * @param p punto a comprobar.
     *
     * @return true si el punto esta dentro del boundingbox.
     */
    public boolean contains(Point2D p) {
        return getBoundingBox(null).contains(p.getX(), p.getY());
    }

    /**
     * Devuelve un entero que representa donde esta contenido el punto que se
     * pasa como par�metro.
     *
     * @param p punto a comparar.
     *
     * @return entero que representa como esta contenido el punto.
     */
    public int getContains(Point2D p) {
        if (n.contains(p.getX(), p.getY())) {
            return N;
        } else if (ne.contains(p.getX(), p.getY())) {
            return NE;
        } else if (e.contains(p.getX(), p.getY())) {
            return E;
        } else if (se.contains(p.getX(), p.getY())) {
            return SE;
        } else if (s.contains(p.getX(), p.getY())) {
            return S;
        } else if (so.contains(p.getX(), p.getY())) {
            return SO;
        } else if (o.contains(p.getX(), p.getY())) {
            return O;
        } else if (no.contains(p.getX(), p.getY())) {
            return NO;
        } else if (getBoundingBox(null).contains(p.getX(), p.getY())) {
            return RECT;
        }

        return NOSELECT;
    }

    /**
     * Devuelve el Cursor adecuado seg�n como est� contenido el punto, si es
     * para desplazamiento, o cambio de tama�o.
     *
     * @param p punto a comprobar.
     *
     * @return Cursor adecuado a la posici�n.
     */
    public Image getMapCursor(Point2D p) {
        int select = getContains(p);

        switch (select) {
            case (N):
                return iNResize;

            case (NE):
                return iNEResize;

            case (E):
                return iEResize;

            case (SE):
                return iSEResize;

            case (S):
                return iNResize;

            case (SO):
                return iNEResize;

            case (O):
                return iEResize;

            case (NO):
                return iSEResize;

            case (RECT):
                return iMove;
        }

        return null;
    }

    /**
     * Este m�todo se implementa en cada una de las fframe, ya que cada una se
     * dibuja de una forma diferente sobre el graphics. M�todo que dibuja
     * sobre el graphics que se le pasa como par�metro, seg�n la transformada
     * afin que se debe de aplicar y el rect�ngulo que se debe de dibujar.
     * M�todo que dibuja sobre el graphics que se le pasa como par�metro,
     * seg�n la transformada afin que se debe de aplicar y el rect�ngulo que
     * se debe de dibujar.
     *
     * @param g Graphics
     * @param at Transformada afin.
     * @param r rect�ngulo sobre el que hacer un clip.
     * @param imgBase DOCUMENT ME!
     */
    public abstract void draw(Graphics2D g, AffineTransform at, Rectangle2D r,
        BufferedImage imgBase);

    /**
     * Devuelve el nombre que representa al fframe.
     *
     * @return String
     */
    public String getName() {
        return m_name;
    }

    /**
     * Rellena el String que representa al nombre del fframe.
     *
     * @param n nombre del fframe.
     */
    public void setName(String n) {
        m_name = n;
    }

    /**
     * Devuelve el boundingBox del fframe en funci�n de la transformada af�n
     * que se pasa como par�metro. Si se pasa como par�metro null, devuelve el
     * �ltimo boundingbox que se calcul�.
     *
     * @param at Transformada af�n
     *
     * @return Rect�ngulo que representa el BoundingBox del fframe.
     */
    public Rectangle2D.Double getBoundingBox(AffineTransform at) {
        if (at == null) {
            return m_BoundingBox;
        }

        m_BoundingBox = FLayoutUtilities.fromSheetRect(m_BoundBox, at);

        return m_BoundingBox;
    }

    /**
     * Rellena con el rect�ngulo que se pasa como par�metro el boundBox(en
     * cent�metros) del fframe del cual con una transformaci�n se podr�
     * calcular el BoundingBox (en pixels).
     *
     * @param r Rect�ngulo en cent�metros.
     */
    public void setBoundBox(Rectangle2D r) {
        m_BoundBox.setRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    /**
     * Devuelve el rect�ngulo que representa el fframe en cent�metros.
     *
     * @return Rect�ngulo en centimetros.
     */
    public Rectangle2D.Double getBoundBox() {
        return m_BoundBox;
    }

    /**
     * Pasando como par�metro true,  se toma como que esta seleccionado el
     * fframe  y si es false como que esta sin seleccionar,  de esta forma se
     * selecciona un fframe directamente  sin comprobar si un punto esta
     * contenido en �l.
     *
     * @param b true si se quiere seleccionar y false si se quiere
     *        deseleccionar.
     */
    public void setSelected(boolean b) {
        if (b) {
            m_Selected = RECT;
        } else {
            m_Selected = IFFrame.NOSELECT;
        }
    }

    /**
     * Crea un Objeto FFrame seg�n el tipo que sea, a partir de la informaci�n
     * del XMLEntity.
     *
     * @param xml XMLEntity
     * @param l Layout.
     * @param p Proyecto.
     *
     * @return Objeto de esta clase.
     */
//    public static IFFrame createFFrame03(XMLEntity xml, Layout l, Project p) {
//        IFFrame fframe = null;
//
//        try {
//        	String className;
//        	if (classnames.containsKey(xml.getStringProperty("className"))) {
//        		className = (String)classnames.get(xml.getStringProperty("className"));
//        	}else {
//        		className = xml.getStringProperty("className");
//        	}
//            Class clase = Class.forName(className);
//            fframe = (IFFrame) clase.newInstance();
//        } catch (Exception e) {
//            NotificationManager.addError("Clase de Frame sobre el Layout no reconocida",
//                e);
//        }
//
//        if (fframe instanceof IFFrameUseProject) {
//            ((IFFrameUseProject) fframe).setProject(p);
//        }
//
//        fframe.setBoundBox(new Rectangle2D.Double(xml.getDoubleProperty("x"),
//                xml.getDoubleProperty("y"), xml.getDoubleProperty("w"),
//                xml.getDoubleProperty("h")));
//        fframe.setXMLEntity03(xml, l);
//        fframe.setName(xml.getStringProperty("m_name"));
//
//        fframe.setTag(xml.getStringProperty("tag"));
//
//        return fframe;
//    }

    /**
     * Crea un Objeto FFrame seg�n el tipo que sea, a partir de la informaci�n
     * del XMLEntity.
     *
     * @param xml XMLEntity
     * @param p Proyecto.
     *
     * @return Objeto de esta clase.
     *
     * @throws OpenException DOCUMENT ME!
     */
//    public static IFFrame createFFrame(XMLEntity xml, Project p,Layout layout)
//        throws OpenException {
//        IFFrame fframe = null;
//        String className = "IFFrame";
//        Class clase = null;
//
//        try {
//        	if (classnames.containsKey(xml.getStringProperty("className"))) {
//        		className = (String)classnames.get(xml.getStringProperty("className"));
//        	}else {
//        		className = xml.getStringProperty("className");
//        	}
//            clase = Class.forName(className);
//        } catch (ClassNotFoundException e) {
//            NotificationManager.addError("Clase de Frame sobre el Layout no reconocida",
//                e);
//        }
//
//        try {
//            fframe = (IFFrame) clase.newInstance();
//        } catch (InstantiationException e) {
//            NotificationManager.addError("Fallo creando el Frame: " +
//                clase.getName(), e);
//        } catch (IllegalAccessException e) {
//            NotificationManager.addError("Fallo creando el Frame: " +
//                clase.getName(), e);
//        }
//
//        try {
//            if (fframe instanceof IFFrameUseProject) {
//                ((IFFrameUseProject) fframe).setProject(p);
//            }
//                fframe.setLayout(layout);
//
//
//            if (xml.contains("level")) {
//                fframe.setLevel(xml.getIntProperty("level"));
//            }
//            if (xml.contains("num")){
//            	fframe.setNum(xml.getIntProperty("num"));
//            }
//            fframe.setName(xml.getStringProperty("m_name"));
//            fframe.setBoundBox(new Rectangle2D.Double(xml.getDoubleProperty("x"),
//                    xml.getDoubleProperty("y"), xml.getDoubleProperty("w"),
//                    xml.getDoubleProperty("h")));
//            fframe.setXMLEntity(xml);
//            fframe.setTag(xml.getStringProperty("tag"));
//        } catch (Exception e) {
//            throw new OpenException(e, className);
//        }
//
//        return fframe;
//    }

    /**
     * Dibuja sobre el graphics el rect�ngulo del fframe en modo borrador.
     *
     * @param g Graphics so bre el que dibujar.
     */
    public void drawDraft(Graphics2D g) {
        Rectangle2D r = getBoundingBox(null);

        g.setColor(Color.lightGray);
        g.fillRect((int) r.getX(), (int) r.getY(), (int) r.getWidth(),
            (int) r.getHeight());
        g.setColor(Color.black);
        g.drawRect((int) r.getX(), (int) r.getY(), (int) r.getWidth()-1,
                (int) r.getHeight()-1);
        int scale = (int) (r.getWidth() / 12);
        Font f = new Font("SansSerif", Font.PLAIN, scale);
        g.setFont(f);
        g.drawString(getName(),
            (int) (r.getCenterX() - ((getName().length() * scale) / 4)),
            (int) (r.getCenterY()));
    }

    /**
     * Rellena con el n�mero de FFrame.
     *
     * @param i n�mero
     */
    public void setNum(int i) {
        num = i;
    }

    /**
     * Dibuja sobre el graphics el rect�ngulo del fframe pero vacio, mostrando
     * el nombre del fframe y vacio.
     *
     * @param g Graphics sobre el que dibujar.
     */
    public void drawEmpty(Graphics2D g) {
        Rectangle2D r = getBoundingBox(null);
        g.setColor(Color.lightGray);
        g.fillRect((int) r.getX(), (int) r.getY(), (int) r.getWidth(),
            (int) r.getHeight());
        g.setColor(Color.darkGray);
        g.setStroke(new BasicStroke(2));
        g.drawRect((int) r.getX(), (int) r.getY(), (int) r.getWidth(),
                (int) r.getHeight());
        g.setColor(Color.black);

        int scale = (int) (r.getWidth() / 12);
        Font f = new Font("SansSerif", Font.PLAIN, scale);
        g.setFont(f);

        String s = this.getNameFFrame() + " " +
            PluginServices.getText(this, "vacia");

        g.drawString(s, (int) (r.getCenterX() - ((s.length() * scale) / 4)),
            (int) (r.getCenterY()));
    }

    /**
     * Devuelve true si el rect�ngulo primero es null o si es distinto de null
     * e intersecta.
     *
     * @param rv Rect�ngulo
     * @param r Rect�ngulo
     *
     * @return True si intersecta o es null.
     */
    public boolean intersects(Rectangle2D rv, Rectangle2D r) {
        return (((rv != null) && rv.intersects(r)) || (rv == null));
    }

    /**
     * Abre el di�logo para cambiar o a�adir el tag.
     */
    public void openTag() {
        Tag tag = new Tag(this);
        PluginServices.getMDIManager().addWindow(tag);
    }

    /**
     * Rellena el tag del FFrame.
     *
     * @param s String que representa el valor a guardar en el tag.
     */
    public void setTag(String s) {
        tag = s;
    }

    /**
     * Devuelve el tag.
     *
     * @return tag.
     */
    public String getTag() {
        return tag;
    }

    /**
     * Dibuja sobre el graphics que se pasa como par�metro el icono que
     * representa que contiene un tag.
     *
     * @param g Graphics sobre el que dibujar el icono.
     */
    public void drawSymbolTag(Graphics2D g) {
        Rectangle2D rec = getBoundingBox(null);
        g.rotate(Math.toRadians(getRotation()),
            rec.getX() + (rec.getWidth() / 2),
            rec.getY() + (rec.getHeight() / 2));

        try {
            Image image = PluginServices.getIconTheme().get("symboltag-icon").getImage();
            g.drawImage(image, (int) rec.getX(), (int) rec.getCenterY(), 30, 30, null);
        } catch (NullPointerException npe) {
        }

        g.rotate(Math.toRadians(-getRotation()),
            rec.getX() + (rec.getWidth() / 2),
            rec.getY() + (rec.getHeight() / 2));
    }

    /**
     * Rellenar la rotaci�n para aplicar al FFrame.
     *
     * @param rotation rotaci�n que se quiere aplicar.
     */
    public void setRotation(double rotation) {
        m_rotation = rotation;
    }

    /**
     * Devuelve la rotaci�n del FFrame.
     *
     * @return Rotaci�n del FFrame.
     */
    public double getRotation() {
        return m_rotation;
    }

    /**
     * Devuelve el nivel en el que se encuentra el FFrame.
     *
     * @return nivel
     */
    public int getLevel() {
        return level;
    }

    /**
     * Inserta el nivel al que se encuentra el FFrame.
     *
     * @param l entero que refleja el nivel del FFrame.
     */
    public void setLevel(int l) {
        level = l;
    }

    /**
     * DOCUMENT ME!
     *
     * @param layout DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public IFFrame cloneFFrame(Layout layout) {
        Project p = ((ProjectExtension) PluginServices.getExtension(ProjectExtension.class)).getProject();
        IFFrame frame = null;

        try {
            frame = createFromXML(this.getXMLEntity(), p,layout);
        } catch (OpenException e) {
            e.showError();
        } catch (SaveException e) {
            e.showError();
        }
        ((FFrame)frame).m_BoundingBox=this.m_BoundingBox;
        frame.setLayout(layout);

        if (frame instanceof IFFrameViewDependence) {
            ((IFFrameViewDependence) frame).initDependence(layout.getLayoutContext().getAllFFrames());
        }
        frame.setFrameLayoutFactory(factory);
        cloneActions(frame);
        return frame;
    }
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SaveException DOCUMENT ME!
     */
    public XMLEntity getXMLEntity() throws SaveException {
        XMLEntity xml = new XMLEntity();
        xml.putProperty("className", getFrameLayoutFactory().getRegisterName());
        xml.putProperty("m_name", m_name);
        xml.putProperty("x", getBoundBox().x);
        xml.putProperty("y", getBoundBox().y);
        xml.putProperty("w", getBoundBox().width);
        xml.putProperty("h", getBoundBox().height);
        xml.putProperty("m_Selected", m_Selected);
        xml.putProperty("tag", getTag());
        xml.putProperty("m_rotation", getRotation());
        xml.putProperty("level",getLevel());
        xml.putProperty("num",num);
        return xml;
    }

	public void setLayout(Layout layout) {
		this.layout=layout;
	}

	public Layout getLayout() {
		return layout;
	}

	public static IFFrame createFromXML(XMLEntity xml, Project p, Layout layout)
		throws OpenException {
		FrameFactory flf = null;
		try{
			ExtensionPoint extPoint = ToolsLocator.getExtensionPointManager()
					.get("FFrames");
			try {
				flf = (FrameFactory) extPoint.create(xml.getStringProperty("className"));
			} catch (InstantiationException e) {
				NotificationManager.addError("Clase de FFrame no reconocida",
						e);
			} catch (IllegalAccessException e) {
				NotificationManager.addError("Clase de FFrame no reconocida",
					e);
			}
			IFFrame frame=flf.createFrame();
			frame.setFrameLayoutFactory(flf);

			if (frame instanceof IFFrameUseProject) {
		          ((IFFrameUseProject) frame).setProject(p);
		    }
		    frame.setLayout(layout);
            if (xml.contains("level")) {
            	frame.setLevel(xml.getIntProperty("level"));
		    }
		    if (xml.contains("num")){
		       	frame.setNum(xml.getIntProperty("num"));
		    }
		    frame.setName(xml.getStringProperty("m_name"));
		    frame.setBoundBox(new Rectangle2D.Double(xml.getDoubleProperty("x"),
		              xml.getDoubleProperty("y"), xml.getDoubleProperty("w"),
		              xml.getDoubleProperty("h")));
		    frame.setXMLEntity(xml);
		    frame.setTag(xml.getStringProperty("tag"));
			return frame;
		}catch (Exception e) {
			throw new OpenException(e,flf.getNameType());
		}
	}
	public void setFrameLayoutFactory(FrameFactory flf) {
		factory=flf;
	}

	public FrameFactory getFrameLayoutFactory() {
		return factory;
	}

//	public void setPrintingProperties(PrintRequestAttributeSet properties) {
//		printingProperties=properties;
//	}
}
