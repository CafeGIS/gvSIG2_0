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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.gvsig.compat.print.PrintAttributes;
import org.gvsig.fmap.geom.Geometry;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.exceptions.SaveException;
import com.iver.cit.gvsig.project.documents.layout.FLayoutUtilities;
import com.iver.cit.gvsig.project.documents.layout.fframes.gui.dialogs.FFrameTextDialog;
import com.iver.cit.gvsig.project.documents.layout.fframes.gui.dialogs.IFFrameDialog;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;
import com.iver.utiles.StringUtilities;
import com.iver.utiles.XMLEntity;


/**
 * FFrame para introducir un texto en el Layout.
 *
 * @author Vicente Caballero Navarro
 */
public class FFrameText extends FFrame {
    /** Localización del texto. */
    public static final int LEFT = 0;
    public static final int CENTER = 1;
    public static final int RIGTH = 2;
    private ArrayList m_text = new ArrayList();
    private boolean m_isFixed = false;
    private int m_pos = LEFT;
    private Color textColor = Color.BLACK;

    //jaume
    private boolean surrounded = false; // The text field is surrounded by a rectangle
    private double cellPadding = 0; // The gap between the the text field and the surrounding rectangle
    private boolean fixedFontSize = false; // The text field font size is constant fixed to the folio's size
    private int fontSize; // Text field's font size
    private boolean hasTitle; // The text field has title
    private String title; // The text for the title
    private int titleSize; // The title's font size
    private double frameBorderSize = 0; // The surrounding rectangle's border size
    private Color frameColor = Color.BLACK; // The surrounding rectangle's color
    private Color titleColor = Color.BLACK; // The title's color
    private Font m_f = null;
    private boolean transicionPixelsMilimetros = true;

    /**
     * Crea un nuevo FFrameText.
     */
    public FFrameText() {
        m_f = new Font("SansSerif", Font.PLAIN, 9);
    }

    /**
     * Inserta la fuente del texto.
     *
     * @param f Fuente del texto.
     */
    public void setFont(Font f) {
        m_f = f;
    }

    /**
     * Devuelve el color del texto del FFrameText.
     *
     * @return Color del texto.
     */
    public Color getTextColor() {
        return textColor;
    }

    /**
     * Obtiene el fixedFontSize
     *
     * @return boolean
     */
    public boolean isFontSizeFixed() {
        return fixedFontSize;
    }

    /**
     * Establece fixedFontSize
     *
     * @param fixedFontSize
     */
    public void setFixedFontSize(boolean fixedFontSize) {
        this.fixedFontSize = fixedFontSize;
    }

    /**
     * Obtiene el fontSize
     *
     * @return int
     */
    public int getFontSize() {
        return fontSize;
    }

    /**
     * Establece fontSize
     *
     * @param fontSize
     */
    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    /**
     * Obtiene el cellPadding
     *
     * @return int
     */
    public double getCellPadding() {
        return cellPadding;
    }

    /**
     * Inserta el color del texto a escribir.
     *
     * @param color Color del texto.
     */
    public void setTextColor(Color color) {
        textColor = color;
    }

    /**
     * Devuelve la fuente del texto.
     *
     * @return Fuente del texto.
     */
    public Font getFont() {
        return new Font(m_f.getFontName(), m_f.getStyle(), 9);
    }

    /**
     * Devuelve la posición izquierda, centro o derecha del texto.
     *
     * @return Posición del texto.
     */
    public int getPos() {
        return m_pos;
    }

    /**
     * Pone la posición izquierda, centro o derecha del texto.
     *
     * @param p 0=LEFT,1=CENTER,2=RIGTH.
     */
    public void setPos(int p) {
        m_pos = p;
    }

    /**
     * Método que dibuja sobre el graphics que se le pasa como parámetro, según
     * la transformada afin que se debe de aplicar y el rectángulo que se debe
     * de dibujar.
     *
     * @param g Graphics
     * @param at Transformada afín.
     * @param rv rectángulo sobre el que hacer un clip.
     * @param imgBase Imagen para acelerar el dibujado.
     */
    public void draw(Graphics2D g, AffineTransform at, Rectangle2D rv,
        BufferedImage imgBase) {
        g.setColor(Color.BLACK);

        Rectangle2D.Double rec = getBoundingBox(at);
        Rectangle2D.Double raux = (Rectangle2D.Double) rec.clone();
        g.rotate(Math.toRadians(getRotation()), raux.x + (raux.width / 2),
            raux.y + (raux.height / 2));

        int longmax = 1;

        if (intersects(rv, raux)) { // comprueba que no cae fuera de la pantalla

            if (m_text.isEmpty()) {
                drawEmpty(g);
            } else {
                for (int i = 0; i < m_text.size(); i++) {
                    if (m_text.get(i) == null) {
                        m_text.set(i, "<NULL>");
                    }
                    String text=(String)m_text.get(i);
                    if (text.length() > longmax) {
                        longmax = text.length();
                    }
                }

                FontRenderContext frc = g.getFontRenderContext();
                int scaledFontSize;

                // TODO myScale es la escala sobre la que se extraen todos los escalados. Esto
                // funciona bien tal cual está si la ratio de aspecto (alto/ancho) del folio es constante
                // porque se toma como medidas el ancho del folio real y el ancho del folio en pantalla.
                // No se que pasará si la ratio cambia, por ejemplo si se usan USLetter en lugar de DIN A4
                double myScale = at.getScaleX() * 0.0234; //FLayoutUtilities.fromSheetDistance(folio.getAncho(),at)/rv.getWidth();

                // Distinguish when the font has fixed size or not
                if (isFontSizeFixed()) {
                    scaledFontSize = (int) (myScale * fontSize);
                } else {
                    scaledFontSize = ((int) (raux.width)) / longmax;

                    if (scaledFontSize > (int) ((raux.height) / m_text.size())) {
                        scaledFontSize = (int) ((raux.height) / m_text.size());
                    }
                }

                if (m_f != null) {
                    // Aquí se ajusta a partir de las características de la fuente, una nueva fuente con el tamaño ajustado.
                    m_f = new Font(m_f.getFontName(), m_f.getStyle(),
                            scaledFontSize);
                } else {
                    // Aquí pasa cuando se abre el diálogo para seleccionar un tipo de fuente y se cierra sin haber seleccionado ninguna.
                    m_f = new Font("SansSerif", Font.PLAIN, scaledFontSize);
                }

                // Draw the text title if it exists
                if (hasTitle()) {
                    int scaledTitleFontSize = (int) (myScale * titleSize);
                    int gap = 3;

                    if (isSurrounded()) {
                        // Para evitar que el marco se pinte sobre el título
                        gap += (int) (FLayoutUtilities.fromSheetDistance(frameBorderSize,
                            at) * myScale);
                    }

                    g.setColor(titleColor);

                    Font titleFont = new Font(m_f.getFontName(),
                            m_f.getStyle(), scaledTitleFontSize);

                    if (!getTitle().equals("")) {
                        TextLayout titleTextLayout = new TextLayout(getTitle(),
                                titleFont, frc);
                        titleTextLayout.draw(g, (float) raux.getX(),
                            (float) (raux.getY() - (gap * myScale)));
                    }
                }

                // Draw the frame involving the text if it exists
                if (isSurrounded()) {
                    g.setColor(frameColor);
//                    g.drawRect((int) raux.x, (int) raux.y, (int) raux.width,
//                        (int) raux.height);

                    double scaledCellPadding = FLayoutUtilities.fromSheetDistance(cellPadding,
                            at);
                    double sizeBorder = FLayoutUtilities.fromSheetDistance(frameBorderSize,
                            at);

//                  if (sizeBorder > 1) {
                    int bordermm = (int)(sizeBorder*10);
                    int scaledCellPaddingmm = (int)(scaledCellPadding*10);
//                    System.out.println("borderSize = " + bordermm);
                        g.setStroke(new BasicStroke(bordermm));
//                        int scaledBorderSize = (int) (sizeBorder * myScale);

//                        for (int i = scaledBorderSize - 1; i > 0; i--)
//                            g.drawRect((int) raux.x - i, (int) raux.y - i,
//                                (int) raux.width + (2 * i),
//                                (int) raux.height + (2 * i));
                        g.drawRect((int) (raux.x+bordermm/2-1), (int) (raux.y+bordermm/2-1),
                                (int) (raux.width-bordermm+2),
                                (int) (raux.height-bordermm+2));
                        g.setStroke(new BasicStroke(0));
                    //}

                    // Recalibro el rectangulo para establecer el área donde se dibujan las fuentes
                    // al área marcada con el ratón menos la distancia al marco especificada
                    raux.setRect(raux.getX() + scaledCellPaddingmm + bordermm,
                        raux.getY() + scaledCellPaddingmm + bordermm,
                        raux.getWidth() - (scaledCellPaddingmm * 2) - bordermm * 2,
                        raux.getHeight() - (scaledCellPaddingmm * 2) - bordermm * 2);

                    if (raux.getWidth()<=0 || raux.getHeight()<=0){
						//JOptionPane.showMessageDialog((Component)PluginServices.getMainFrame(),PluginServices.getText(this,"too_large_border")+": "+bordermm+ " + "+scaledCellPaddingmm);
						g.setColor(Color.red);
						drawError(getBoundingBox(at), 15, g,PluginServices.getText(this,"too_large_border")+": "+frameBorderSize+ "cm + "+cellPadding+"cm");
//						g.rotate(Math.toRadians(-getRotation()),
//				                    raux.x + (raux.width / 2), raux.y + (raux.height / 2));
						return;
					}
                }

                g.setColor(textColor);

                drawText(raux, scaledFontSize, g);
                g.rotate(Math.toRadians(-getRotation()),
                    raux.x + (raux.width / 2), raux.y + (raux.height / 2));
            }
        }

        raux = null;
    }
    private void drawError(Rectangle2D r, int sfs, Graphics2D g,String error) {
    	FontRenderContext frc = g.getFontRenderContext();
        TextLayout textLayout = null;
       	m_f = new Font(m_f.getFontName(), m_f.getStyle(), sfs);
        textLayout = new TextLayout(error, m_f, frc);
        textLayout.draw(g, (float) r.getCenterX(),
                            (float) (r.getCenterY()));

	}
    /**
     * Dibuja el texto sobre el graphics con el tamaño adecuado.
     *
     * @param r Rectángulo sobre el que dibujar.
     * @param sfs Tamaño aproximado del texto.
     * @param g Graphics sobre el que dibujar el texto.
     */
    private void drawText(Rectangle2D r, int sfs, Graphics2D g) {
        int minSFS = Integer.MAX_VALUE;
        FontRenderContext frc = g.getFontRenderContext();
        int ht = (int) (r.getHeight() / m_text.size());

        if (isFontSizeFixed()) {
            minSFS = sfs;
        } else {
            for (int i = 0; i < m_text.size(); i++) {
                if (!((String) m_text.get(i)).equals("") && !((String) m_text.get(i)).equals("\n")) {
                    TextLayout textaux = new TextLayout((String) m_text.get(i),
                            m_f, frc);
                    Rectangle2D txtBound = textaux.getBounds();
                    double difW = txtBound.getWidth() / r.getWidth();
                    double difH = (txtBound.getHeight() * m_text.size()) / (r.getHeight());

                    if (difW > difH) {
                        if (minSFS > sfs) {
                            minSFS = (int) (sfs / difW);
                        }
                    } else {
                        if (minSFS > sfs) {
                            minSFS = (int) (sfs / difH);
                        }
                    }
                }
            }
        }

        TextLayout textLayout = null;

        for (int i = 0; i < m_text.size(); i++) {
            if (!((String) m_text.get(i)).equals("")) {
            	m_f = new Font(m_f.getFontName(), m_f.getStyle(), minSFS);

                textLayout = new TextLayout((String) m_text.get(i), m_f, frc);

                Rectangle2D txtBound = textLayout.getBounds();
                float difW = (float) (r.getWidth() - txtBound.getWidth());

                switch (m_pos) {
                    case (LEFT):
                        textLayout.draw(g, (float) r.getX(),
                            (float) (r.getY() + (ht * (i + 0.8))));

                        break;

                    case (CENTER):
                        textLayout.draw(g, (float) r.getX() + (difW / 2),
                            (float) (r.getY() + (ht * (i + 0.8))));

                        break;

                    case (RIGTH):
                        textLayout.draw(g, (float) r.getX() + difW,
                            (float) (r.getY() + (ht * (i + 0.8)))); //- (ht / 2))));

                        break;
                }
            }
        }
    }

    /*
     * @see com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame#print(java.awt.Graphics2D,
     *      java.awt.geom.AffineTransform)
     */
    public void print(Graphics2D g, AffineTransform at, Geometry geom,
			PrintAttributes properties) {
        draw(g, at, null, null);
    }

    /**
     * Rellenar el texto que se quiere añadir al Layout.
     *
     * @param s String a añadir.
     */
    public void addText(String s) {
        m_text.add(s);
    }

    /**
     * Devuelve el ArrayList que contiene las líneas de texto.
     *
     * @return ArrayList.
     */
    public ArrayList getText() {
        return m_text;
    }

    /**
     * Seleccionar si se quiere un tamaño fijo o adecuado a la escala.
     *
     * @param b true si se quiere tamaño fijo.
     */
    public void setSizeFixed(boolean b) {
        m_isFixed = b;
    }

    /**
     * Devuelve si está fijado el tamaño.
     *
     * @return True si está fijado el tamaño.
     */
    public boolean isSizeFixed() {
        return m_isFixed;
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
//            xml.putProperty("type", Layout.RECTANGLETEXT);

            String[] s = (String[]) m_text.toArray(new String[0]);
            xml.putProperty("s", s);
            xml.putProperty("m_isFixed", m_isFixed);

            if (m_isFixed) {
                xml.putProperty("fontSize", m_f.getSize());
            }

            xml.putProperty("m_pos", m_pos);

            xml.putProperty("fontName", m_f.getName());
            xml.putProperty("fontStyle", m_f.getStyle());
            xml.putProperty("textColor", StringUtilities.color2String(textColor));

            xml.putProperty("transicionPixelsMilimetros",
                transicionPixelsMilimetros);

            // jaume
            xml.putProperty("cellPadding", cellPadding);
            xml.putProperty("fontSize", fontSize);
            xml.putProperty("fixedFontSize", fixedFontSize);
            xml.putProperty("surrounded", surrounded);
            xml.putProperty("hasTitle", hasTitle);
            xml.putProperty("title", title);
            xml.putProperty("titleSize", titleSize);
            xml.putProperty("frameBorderSize", frameBorderSize);
            xml.putProperty("frameColor",
                StringUtilities.color2String(frameColor));
            xml.putProperty("titleColor",
                StringUtilities.color2String(titleColor));
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

        String[] s = xml.getStringArrayProperty("s");

        for (int i = 0; i < s.length; i++) {
            this.m_text.add(s[i]);
        }

        this.m_isFixed = xml.getBooleanProperty("m_isFixed");
        this.m_pos = xml.getIntProperty("m_pos");
        setRotation(xml.getDoubleProperty("m_rotation"));

        this.m_f = new Font(xml.getStringProperty("fontName"),
                xml.getIntProperty("fontStyle"), 9);

        if (xml.contains("textColor")) {
            this.textColor = StringUtilities.string2Color(xml.getStringProperty(
                        "textColor"));
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

        String[] s = xml.getStringArrayProperty("s");

        for (int i = 0; i < s.length; i++) {
            this.m_text.add(s[i]);
        }

        this.m_isFixed = xml.getBooleanProperty("m_isFixed");

        int size = 9;

        if (m_isFixed && xml.contains("fontSize")) {
            size = xml.getIntProperty("fontSize");
        }

        this.m_pos = xml.getIntProperty("m_pos");
        setRotation(xml.getDoubleProperty("m_rotation"));
        this.m_f = new Font(xml.getStringProperty("fontName"),
                xml.getIntProperty("fontStyle"), size);

        if (xml.contains("textColor")) {
            this.textColor = StringUtilities.string2Color(xml.getStringProperty(
                        "textColor"));
        }

        // jaume
        if (xml.contains("cellPadding")) {
            this.cellPadding = xml.getDoubleProperty("cellPadding");
        }

        if (xml.contains("fontSize")) {
            this.fontSize = xml.getIntProperty("fontSize");
        }

        if (xml.contains("fixedFontSize")) {
            this.fixedFontSize = xml.getBooleanProperty("fixedFontSize");
        }

        if (xml.contains("surrounded")) {
            this.surrounded = xml.getBooleanProperty("surrounded");
        }

        if (xml.contains("hasTitle")) {
            this.hasTitle = xml.getBooleanProperty("hasTitle");
        }

        if (xml.contains("title")) {
            this.title = xml.getStringProperty("title");
        }

        if (xml.contains("titleSize")) {
            this.titleSize = xml.getIntProperty("titleSize");
        }

        if (xml.contains("frameBorderSize")) {
            this.frameBorderSize = xml.getDoubleProperty("frameBorderSize");
        }

        if (xml.contains("frameColor")) {
            this.frameColor = StringUtilities.string2Color(xml.getStringProperty(
                        "frameColor"));
        }

        if (xml.contains("titleColor")) {
            this.titleColor = StringUtilities.string2Color(xml.getStringProperty(
                        "titleColor"));
        }

        if (!xml.contains("transicionPixelsMilimetros")) {
            cellPadding = 0.05;
            frameBorderSize = 0.01;
        }
    }

    /**
     * @see com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame#getNameFFrame()
     */
    public String getNameFFrame() {
        return PluginServices.getText(this, "texto")+ num;
    }

    /**
     * Sets FFrameText to draw an involving rectangle
     *
     * @param b
     */
    public void setSurrounded(boolean b) {
        surrounded = b;
    }

    /**
     * True if the FFrameText is set to draw an involving rectangle, or false
     * if not.
     *
     * @return boolean
     */
    public boolean isSurrounded() {
        return surrounded;
    }

    /**
     * Sets the gap between the involving rectangle and the text
     *
     * @param i
     */
    public void setCellPadding(double i) {
        cellPadding = i;
    }

    /**
     * Devuelve true si tiene un título.
     *
     * @return
     */
    public boolean hasTitle() {
        return hasTitle;
    }

    /**
     * Devuelve un string con el título.
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * Inserta true si tiene título
     *
     * @param b
     */
    public void setHasTitle(boolean b) {
        hasTitle = b;
    }

    /**
     * Inserta un string con el título.
     *
     * @param text
     */
    public void setTitle(String text) {
        title = text;
    }

    /**
     * Devuelve el tamaño del título.
     *
     * @return
     */
    public int getTitleSize() {
        return titleSize;
    }

    /**
     * Inserta el tamaño del título.
     *
     * @param size DOCUMENT ME!
     */
    public void setTitleSize(int size) {
        titleSize = size;
    }

    /**
     * Inserta el tamaño del borde.
     *
     * @param size
     */
    public void setFrameBorderSize(double size) {
        frameBorderSize = size;
    }

    /**
     * Devuelve el tamaño del borde.
     *
     * @return
     */
    public double getFrameBorderSize() {
        return frameBorderSize;
    }

    /**
     * DOCUMENT ME!
     *
     * @param frameColor
     */
    public void setFrameColor(Color frameColor) {
        this.frameColor = frameColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param titleColor DOCUMENT ME!
     */
    public void setTitleColor(Color titleColor) {
        this.titleColor = titleColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Color getFrameColor() {
        return frameColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Color getTitleColor() {
        return titleColor;
    }

    /**
     * Use this method if you want the text in the FFrameText to be removed.
     */
    public void clearText() {
        m_text.clear();
    }

	public void initialize() {
		// TODO Auto-generated method stub

	}

	public void cloneActions(IFFrame frame) {
		// TODO Auto-generated method stub

	}

	public IFFrameDialog getPropertyDialog() {
		return new FFrameTextDialog(getLayout(),this);
	}
}
