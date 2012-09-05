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

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImagingOpException;
import java.io.File;

import javax.swing.ImageIcon;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.bridge.ViewBox;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.renderer.StaticRenderer;
import org.gvsig.compat.print.PrintAttributes;
import org.gvsig.fmap.geom.Geometry;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.project.documents.exceptions.SaveException;
import com.iver.cit.gvsig.project.documents.layout.fframes.gui.dialogs.FFramePictureDialog;
import com.iver.cit.gvsig.project.documents.layout.fframes.gui.dialogs.IFFrameDialog;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;
import com.iver.utiles.XMLEntity;
import com.sun.jimi.core.Jimi;


/**
 * FFrame para introducir una imagen en el Layout o para dibujar sobre el
 * graphics un SVG.
 *
 * @author Vicente Caballero Navarro
 */
public class FFramePicture extends FFrame {
    protected static RenderingHints defaultRenderingHints;

    static {
        defaultRenderingHints = new RenderingHints(null);
        defaultRenderingHints.put(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);

        defaultRenderingHints.put(RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    }

    private static final int PRESENTACION = 0;
    private static final int ACTIVO = 1;
    private BufferedImage m_image = null;
    private int m_quality = PRESENTACION;
    private int m_viewing = ACTIVO;
    private String m_path = null;
    private boolean isSVG = false;
    private StaticRenderer renderer = new StaticRenderer();
    private Element elt;
    private GVTBuilder gvtBuilder = new GVTBuilder();
    private GraphicsNode gvtRoot = null;
    private BridgeContext ctx;

    /**
     * Creates a new FFramePicture object.
     */
    public FFramePicture() {
    }

    /**
     * Método que dibuja sobre el graphics que se le pasa como parámetro, según
     * la transformada afin que se debe de aplicar y el rectángulo que se debe
     * de dibujar.
     *
     * @param g Graphics
     * @param at Transformada afin.
     * @param rv rectángulo sobre el que hacer un clip.
     * @param imgBase Imagen para acelerar el dibujado.
     */
    public void draw(Graphics2D g, AffineTransform at, Rectangle2D rv,
        BufferedImage imgBase) {
        Rectangle2D.Double r = getBoundingBox(at);
        g.rotate(Math.toRadians(getRotation()), r.x + (r.width / 2),
            r.y + (r.height / 2));

        double x = r.getMinX();
        double y = r.getMinY();
        double w = r.getWidth();
        double h = r.getHeight();

        if (intersects(rv, r)|| w!=0 || h!=0) {
            if ((m_image == null) && !isSVG) { //Que no hay una imagen.
                drawEmpty(g);
            } else {
                if ((rv == null) || (getQuality() == PRESENTACION)) {
                    if (!isSVG) {
                        double scalex = w / m_image.getWidth(null);
                        double scaley = h / m_image.getHeight(null);
                       try {
                        AffineTransform xform = AffineTransform.getScaleInstance(scalex,
                                scaley);
                        AffineTransform xpos = AffineTransform.getTranslateInstance(x,
                                y);
                        xpos.concatenate(xform);
                        g.drawRenderedImage(m_image, xpos);
                       }catch (ImagingOpException e) {
                    	   NotificationManager.addError("Dibujando FFramePicture", e);
                       }
                    } else if (isSVG) {
                        try {
                            if (r != null) {
                                drawSVG(g, r, rv);
                            }
                        } catch (OutOfMemoryError e) {
                        	 NotificationManager.addError("Dibujando SVG FFramePicture", e);
                        } catch (IllegalArgumentException e) {
                        	 NotificationManager.addError("Dibujando SVG FFramePicture", e);
                        }
                    }
                    System.gc();
                } else {
                    drawDraft(g);
                }
            }
        }

        g.rotate(Math.toRadians(-getRotation()), r.x + (r.width / 2),
            r.y + (r.height / 2));
    }

    /**
     * Dibuja SVG sobre el Graphics que se pasa como parámetro.
     *
     * @param g Graphics
     * @param rect rectángulo que ocupa.
     * @param rv Rectángulo que forma la parte visible del Layout.
     */
    private void drawSVG(Graphics2D g, Rectangle2D rect, Rectangle2D rv) {
        if ((rv == null) || rv.contains(rect)) {
            AffineTransform ataux = new AffineTransform();

            ataux.translate(rect.getX(), rect.getY());

            try {
                ataux.concatenate(ViewBox.getViewTransform(null, elt,
                        (float) rect.getWidth(), (float) rect.getHeight(), ctx));
                gvtRoot.setTransform(ataux);
            } catch (Exception e) {
                // TODO: handle exception
            }
        } else {
            AffineTransform ataux = new AffineTransform();

            ataux.translate(rect.getX(), rect.getY());
            ataux.concatenate(ViewBox.getViewTransform(null, elt,
                    (float) rect.getWidth(), (float) rect.getHeight(), ctx));

            gvtRoot.setTransform(ataux);
        }

        RenderingHints renderingHints = defaultRenderingHints;
        g.setRenderingHints(renderingHints);

        if (gvtRoot != null) {
            gvtRoot.paint(g);
        }
    }

    /**
     * Rellena la calidad según el entero que se pasa como parámetro.
     *
     * @param q entero que representa el tipo de calidad elegido.
     */
    public void setQuality(int q) {
        m_quality = q;
    }

    /**
     * Devuelve la calidad que está seleccionada.
     *
     * @return entero que representa la calidad seleccionada.
     */
    public int getQuality() {
        return m_quality;
    }

    /**
     * Devuelve un entero que representa la forma en que se actualiza la vista.
     *
     * @return forma que se actualiza la vista.
     */
    public int getViewing() {
        return m_viewing;
    }

    /**
     * Rellena la forma de actualizar la vista.
     *
     * @param v entero que representa la forma de actualizar la vista.
     */
    public void setViewing(int v) {
        m_viewing = v;
    }

    /**
     * Rellena el nombre de la imagen.
     *
     * @param path nombre de la imagen.
     */
    public void setPath(String path) {
        m_path = path;
    }

    /**
     * Devuelve la ruta del fichero.
     *
     * @return String
     */
    public String getPath() {
        return m_path;
    }

    /**
     * Rellena la imagen.
     *
     * @param image
     */
    public void setImage(BufferedImage image) {
        m_image = image;
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
//            xml.putProperty("type", Layout.RECTANGLEPICTURE);
            xml.putProperty("m_path", m_path);
            xml.putProperty("m_quality", m_quality);
            xml.putProperty("m_viewing", m_viewing);
        } catch (Exception e) {
            throw new SaveException(e, this.getClass().getName());
        }

        return xml;
    }

    /**
     * Devuelve la dimensión dela imagen.
     *
     * @param file Nombre del fichero donde se encuentra la imagen.
     *
     * @return DOCUMENT ME!
     */
    public Dimension getBound(String file) {
        Image img = load(file);

        if (isSVG) {
            return new Dimension(100, 100);
        }

        if (img == null) {
            return new Dimension((int) getBoundingBox(null).getWidth(),
                (int) getBoundingBox(null).getHeight());
        }

        return new Dimension(img.getWidth(null), img.getHeight(null));
    }

    /**
     * Carga el contnido del fichero.
     *
     * @param file Nombre del fichero a cargar.
     *
     * @return Imagen
     */
    public Image load(String file) {
        if (file==null) {
			return null;
		}
    	ImageIcon tmpIcon = null;
        File f=new File(file);
        if (file == null || !f.exists()) {
            return null;
        }
        setPath(file);
        String iString = file.toLowerCase();

        if (iString.endsWith("jpg") || iString.endsWith("jpeg") ||
                iString.endsWith("gif")) {
            tmpIcon = new ImageIcon(Jimi.getImage(file, Jimi.VIRTUAL_MEMORY)); //((File)main.allImages.elementAt(x)).getAbsolutePath());
        } else if (iString.endsWith("png") || iString.endsWith("tif") ||
                iString.endsWith("ico") || iString.endsWith("xpm") ||
                iString.endsWith("bmp")) {
            tmpIcon = new ImageIcon(Jimi.getImage(file, Jimi.VIRTUAL_MEMORY)); //new ImageIcon(f.getPath());
        } else if (iString.endsWith("svg")) {
            isSVG = true;
            obtainStaticRenderer(new File(file));
        }else {
        	tmpIcon=new ImageIcon(file);
        }

        if (!isSVG && (tmpIcon != null)) {
            Image image = tmpIcon.getImage();

            if (image.getWidth(null)==-1 || image.getHeight(null)==-1){
            	NotificationManager.showMessageError(PluginServices.getText(this,"unsupported_format"), new Exception());
            	return null;
            }

            BufferedImage bi = new BufferedImage(image.getWidth(null),
                    image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics2D biContext = bi.createGraphics();
            biContext.drawImage(image, 0, 0, null);

            setImage(bi);

            return image;
        }

        return null;
    }

    /**
     * Obtiene el renderer para svg a partir del svg.
     *
     * @param file Nombre del fichero.
     */
    private void obtainStaticRenderer(File file) {
        try {
            UserAgentAdapter userAgent = new UserAgentAdapter();
            DocumentLoader loader = new DocumentLoader(userAgent);
            BridgeContext ctx = new BridgeContext(userAgent, loader);
            Document svgDoc = loader.loadDocument(file.toURI().toString());
            gvtRoot = gvtBuilder.build(ctx, svgDoc);
            renderer.setTree(gvtRoot);
            elt = ((SVGDocument) svgDoc).getRootElement();
            ctx =  new BridgeContext(userAgent, loader);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Incorpora los atributos del XMLEntity en el objeto actual.
     *
     * @param xml XMLEntity
     * @param l Referencia al Layout.
     */
    public void setXMLEntity03(XMLEntity xml, Layout l) {
        if (xml.getIntProperty("m_Selected") != 0) {
            this.setSelected(true);
        } else {
            this.setSelected(false);
        }

        this.m_path = xml.getStringProperty("m_path");

        try {
            load(this.m_path);
        } catch (Exception ex) {
            NotificationManager.addError("Excepción :", ex);
        }

        this.m_quality = xml.getIntProperty("m_quality");
        this.m_viewing = xml.getIntProperty("m_viewing");
    }

    /**
     * Incorpora los atributos del XMLEntity en el objeto actual.
     *
     * @param xml XMLEntity
     */
    public void setXMLEntity(XMLEntity xml) {
        if (xml.getIntProperty("m_Selected") != 0) {
            this.setSelected(true);
        } else {
            this.setSelected(false);
        }

        this.m_path = xml.getStringProperty("m_path");

        try {
            load(this.m_path);
        } catch (Exception ex) {
            NotificationManager.addError("Excepción :", ex);
        }

        this.m_quality = xml.getIntProperty("m_quality");
        this.m_viewing = xml.getIntProperty("m_viewing");
        setRotation(xml.getDoubleProperty("m_rotation"));
    }

    /**
     * @see com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame#getNameFFrame()
     */
    public String getNameFFrame() {
        return PluginServices.getText(this, "imagen")+ num;
    }


    public void initialize() {
        // TODO Auto-generated method stub

    }

    public void cloneActions(IFFrame frame) {
       m_image=null;

    }

	public IFFrameDialog getPropertyDialog() {
		return new FFramePictureDialog(getLayout(), this);
	}

	public void print(Graphics2D g, AffineTransform at, Geometry geom,
			PrintAttributes properties) {
		draw(g, at, null, null);
	}
}
