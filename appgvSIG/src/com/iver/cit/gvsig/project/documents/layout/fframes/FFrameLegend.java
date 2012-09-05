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
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.gvsig.compat.print.PrintAttributes;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.mapcontext.layers.operations.Classifiable;
import org.gvsig.fmap.mapcontext.layers.operations.IHasImageLegend;
import org.gvsig.fmap.mapcontext.rendering.legend.IClassifiedLegend;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.ITextSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbolDrawingException;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbologyFactory;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.exceptions.SaveException;
import com.iver.cit.gvsig.project.documents.layout.FLayoutUtilities;
import com.iver.cit.gvsig.project.documents.layout.LayoutContext;
import com.iver.cit.gvsig.project.documents.layout.fframes.gui.dialogs.FFrameLegendDialog;
import com.iver.cit.gvsig.project.documents.layout.fframes.gui.dialogs.IFFrameDialog;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;
import com.iver.utiles.XMLEntity;


/**
 * FFrame para introducir una leyenda en el Layout.
 *
 * @author Vicente Caballero Navarro
 */
public class FFrameLegend extends FFrame implements IFFrameViewDependence {
    private static final int PRESENTACION = 0;
    private static final int BORRADOR = 1;
    private int m_quality = 0;
    private int m_viewing = 0;
    private Font m_f = new Font("SansSerif", Font.PLAIN, 9);
    private FFrameView fframeview = null;
    private int m_max;
    private int m_numLayers;
    private FLayers layers = null;
    private int dependenceIndex = -1;
	private ArrayList nameLayers=new ArrayList();
	private ArrayList areVisible=new ArrayList();
	private PrintAttributes properties;

	public FFrameLegend() {

	}
    /**
     * Rellena la calidad que se quiere aplicar.
     *
     * @param q entero que representa la calidad a aplicar.
     */
    public void setQuality(int q) {
        m_quality = q;
    }

    /**
     * Devuelve un entero que representa la calidad que está seleccionada.
     *
     * @return tipo de calidad selccionada.
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
     * Inserta una FFrameView de donde se obtiene la información de las capas
     * para generar la leyenda.
     *
     * @param f FFrameView para obtener los nombres de las capas.
     */
    public void setFFrameDependence(IFFrame f) {
        fframeview = (FFrameView) f;
    }

    public void refreshDependence(IFFrame fant, IFFrame fnew) {
    	if ((fframeview != null) &&
                fframeview.equals(fant)) {
            fframeview=(FFrameView)fnew;
    	}
    }
    /**
     * Devuelve el FFrameView utilizado para obtener la leyenda.
     *
     * @return FFrameView utilizado.
     */
    public IFFrame[] getFFrameDependence() {
        return new IFFrame[]{fframeview};
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
        Rectangle2D.Double re = getBoundingBox(at);
        g.rotate(Math.toRadians(getRotation()), re.x + (re.width / 2),
            re.y + (re.height / 2));

        if ((fframeview != null) && (fframeview.getMapContext() != null)) {
            layers = fframeview.getMapContext().getLayers();
        }

        m_max = 0;
        m_numLayers = 0;

        if (intersects(rv, re)) {
            if (layers == null) { //Si no se ha seleccionado ninguna vista para crear la leyenda.
                drawEmpty(g);
            } else if ((rv == null) || (getQuality() == PRESENTACION)) {
                m_numLayers = getSizeNum(layers);

                double h = re.getHeight() / m_numLayers;
                int[] n = new int[1];
                n[0] = 0;
                drawLegendOrToFFrame(g, re, h, layers, n, null);
            } else if (getQuality() == BORRADOR) { //Si se selecciona la calidad BORRADOR.
                drawDraft(g);
            }
        }

        g.rotate(Math.toRadians(-getRotation()), re.x + (re.width / 2),
            re.y + (re.height / 2));
    }

    /**
     * Dibuja el nombre u símbolo de la capa que se pasa como parámetro.
     *
     * @param g Graphics2D sobre el que se dibuja.
     * @param re Rectangle a rellenar.
     * @param h Altura en pixels.
     * @param layers Capa a representar.
     * @param n índice de la capa a dibujar.
     * @throws ReadDriverException TODO
     */
    private void drawLegendOrToFFrame(Graphics2D g, Rectangle2D re, double h,
        FLayers layers, int[] n, LayoutContext layout) {
        float sizefont = 0;

        if ((re.getHeight() / m_numLayers) < (re.getWidth() / (m_max * 0.7))) {
            sizefont = (float) (re.getHeight() / m_numLayers);
        } else {
            sizefont = (float) (re.getWidth() / (m_max * 0.7));
        }

        int l=0;
        //////Se recorren los layers dibujando el símbolo y su nombre sobre el graphics
        for (int i = layers.getLayersCount() - 1; i >= 0; i--) {
            FLayer layer = layers.getLayer(i);
            boolean b=false;

            if (nameLayers.size()>l && nameLayers.get(l).equals(layer.getName())) {
            	b=((Boolean)areVisible.get(l)).booleanValue();
            }else {
            	b=layer.isVisible();
            }
            l++;
            if (b) {
                if (layer instanceof FLayers) {
                    n[0]++;

                    double dX = 0;
                    double dY = n[0] * h;
                    double xl = (re.getX() + dX);
                    double yl = (re.getY() + dY);
                    if (layout!=null) {
                    	toFFrameText(layout,layer.getName(),re,sizefont,(xl - (re.getWidth() / 5)),yl,h);
                    }else {
                    	drawNameLegend(g, layer.getName(), re, sizefont,
                    			(xl - (re.getWidth() / 5)), yl, h);
                    }
                    	n[0]++;
                    drawLegendOrToFFrame(g, re, h, (FLayers) layer, n, layout);
                    n[0]++;
                } else if (layer instanceof Classifiable && !(layer instanceof IHasImageLegend) ) {
                    Classifiable cO = (Classifiable) layer;

                    if (cO.getLegend() instanceof IClassifiedLegend){// && !(cO instanceof FLyrAnnotation)) {
                        IClassifiedLegend cli = (IClassifiedLegend) cO.getLegend();
                        double dX = 0;
                        double dY = n[0] * h;

                        double xl = (re.getX() + dX);
                        double yl = (re.getY() + dY);
                        if (layout!=null) {
                        	toFFrameText(layout,layer.getName(),re,sizefont,(xl - (re.getWidth() / 5)),yl,h);
                        }else {
                        	drawNameLegend(g, layer.getName(), re, sizefont,
                        			(xl - (re.getWidth() / 5)), yl, h);
                        }
                        n[0]++;
                        String[] descriptions=cli.getDescriptions();
                        ISymbol[] symbols=cli.getSymbols();
                        for (int j = 0; j < descriptions.length; j++) {
                            dY = n[0] * h;

                            xl = (re.getX() + dX);
                            yl = (re.getY() + dY);

                            String s = descriptions[j];
                            if (layout!=null) {
                            	toFFrameText(layout,s,re,sizefont,xl,yl,h);
                            }else {
                            	drawNameLegend(g, s, re, sizefont, xl, yl, h);
                            }
                            ISymbol fs2d = symbols[j];
                            if (layout!=null) {
                            	toFFrameSymbol(layout,re,xl,yl,fs2d,sizefont, h);
                            }else {
                            	drawSymbolLegend(g, re, xl, yl, fs2d, sizefont,
                            			h);
                            }
                            n[0]++;
                        }
                    } else {
                        double dX = 0;
                        double dY = n[0] * h;

                        double xl = (re.getX() + dX);
                        double yl = (re.getY() + dY);
                        if (layout!=null) {
                        	toFFrameText(layout,layer.getName(),re,sizefont,xl,yl,h);
                        }else {
                        	drawNameLegend(g, layer.getName(), re, sizefont, xl,
                        			yl, h);
                        }
                        // TO DO: CAMBIAR TO_DO ESTO PARA QUE ACEPTE ISYMBOL
                        // TODO: comprovar que no es trenca res
                        if (cO.getLegend() != null) {
							ISymbol fs2d = cO.getLegend().getDefaultSymbol();

							if (layout != null) {
								toFFrameSymbol(layout, re, xl, yl, fs2d,
										sizefont, h);
							} else {
								drawSymbolLegend(g, re, xl, yl, fs2d, sizefont,
										h);
							}
						}
                        n[0]++;
                    }
                }else if (layer instanceof IHasImageLegend){
                	Image image=((IHasImageLegend)layer).getImageLegend();
                	String path=((IHasImageLegend)layer).getPathImage();
                	if (image!=null) {
                	FFramePicture picture =(FFramePicture)FrameFactory.createFrameFromName(FFramePictureFactory.registerName);

                	picture.setLayout(getLayout());
                	BufferedImage bi = new BufferedImage(image.getWidth(null),
                            image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
                    Graphics2D biContext = bi.createGraphics();
                    biContext.drawImage(image, 0, 0, null);
                    picture.setImage(bi);
                    double dY = n[0] * h;
                    Rectangle2D rectImage=new Rectangle2D.Double(re.getX(),re.getY()+dY,re.getWidth(),h);
                   if (layout!=null) {
                    	picture.setBoundBox(FLayoutUtilities.toSheetRect(rectImage, layout.getAT()));
                    	picture.setPath(path);
                    	layout.addFFrame(picture, false, true);
                    }else {
                    	picture.setBoundBox(FLayoutUtilities.toSheetRect(rectImage,new AffineTransform()));
                        picture.draw(g,new AffineTransform(),re,bi);
                    }
                	}
                    n[0]++;
                }else {
                	double dX = 0;
                    double dY = n[0] * h;

                    double xl = (re.getX() + dX);
                    double yl = (re.getY() + dY);
                    if (layout!=null) {
                    	toFFrameText(layout,layer.getName(),re,sizefont,xl,yl,h);
                    }else {
                    	drawNameLegend(g, layer.getName(), re, sizefont, xl,
                    			yl, h);
                    }
                    n[0]++;
                }
            }
        }
    }

    /**
     * Dibuja sobre el graphics el nombre de la capa que se pasa como
     * parámetro.
     *
     * @param g Graphics2D sobre el que dibujar.
     * @param name Nombre de la capa.
     * @param re Rectángulo a ocupar por el nombre.
     * @param sizefont tamaño de la fuente.
     * @param x Posición X.
     * @param y Posición Y.
     * @param h Altura.
     */
    private void drawNameLegend(Graphics2D g, String name, Rectangle2D re,
        float sizefont, double x, double y, double h) {
        Font f = getFont(sizefont);
        g.setFont(f);
        g.setColor(Color.black);
        g.drawString(name, (float) (x + (re.getWidth() / 4)),
            (float) (y + (h / 2)));
    }

    private Font getFont(float sizefont) {
		Font f=getFont();
		return new Font(f.getName(),f.getStyle(),(int)sizefont);
  }

	/**
     * Añade al Layout un nuevo FFrameText a partir de los parámetros de
     * entrada.
     *
     * @param layout Layout sobre el que se crea el nuevo FFrame.
	 * @param name Texto.
	 * @param sizefont tamaño de la fuente.
	 * @param x Posición X.
	 * @param y Posición Y.
	 * @param h Altura.
	 * @param wT Rectángulo del FFrame.
	 * @param hT DOCUMENT ME!
     */
    private void toFFrameText(LayoutContext layout, String name, Rectangle2D re,
            float sizefont, double x, double y, double h) {
            //Font f = getFont(sizefont);
            Rectangle2D rAux = new Rectangle2D.Double(x+re.getWidth()/4, ((y + (h / 2)) - h),
                    re.getWidth()-re.getWidth()/4, h);
            FFrameText text =(FFrameText)FrameFactory.createFrameFromName(FFrameTextFactory.registerName);

            text.setLayout(getLayout());
            text.setFixedFontSize(true);
            //text.setFontSize((int) (sizefont*1.4));
            double myScale = layout.getAT().getScaleX() * 0.0234;
            text.setFontSize((int)(sizefont/myScale));
            text.setBoundBox(FLayoutUtilities.toSheetRect(rAux, layout.getAT()));
            text.addText(name);
            layout.addFFrame(text, false, true);
        }

    /**
     * Dibuja sobre el Graphics2D el símbolo.
     *
     * @param g Graphics2D.
     * @param re Rectángulo a cubrir por el símbolo.
     * @param x Posición X.
     * @param y Posición Y.
     * @param symbol2d Símbolo a dibujar.
     * @param sizefont Tamaño de la fuente.
     * @param h Altura.
     */
    private void drawSymbolLegend(Graphics2D g, Rectangle2D re, double x,
    	double y, ISymbol symbol2d, float sizefont, double h) {
    	double pW = 5;
    	double wl = (re.getWidth() / pW);
    	double haux = (sizefont * 0.7);
    	Font font=null;

    	if (symbol2d instanceof ITextSymbol && (font=((ITextSymbol)symbol2d).getFont()) != null) {
    		((ITextSymbol)symbol2d).setFont(new Font(font.getFontName(), font.getStyle(),
    				(int) (wl / 6)));
    	}

		Rectangle rectangle = new Rectangle((int) x, (int) ((y + (h / 2)) - haux), (int) wl,
				(int) haux);
		try {
			symbol2d.drawInsideRectangle(g, new AffineTransform(),
					rectangle, properties);
		} catch (SymbolDrawingException e) {
			if (e.getType() == SymbolDrawingException.UNSUPPORTED_SET_OF_SETTINGS) {
				try {
					SymbologyFactory.getWarningSymbol(
							SymbolDrawingException.STR_UNSUPPORTED_SET_OF_SETTINGS,
							symbol2d.getDescription(),
							SymbolDrawingException.UNSUPPORTED_SET_OF_SETTINGS).drawInsideRectangle(g, null, rectangle,null);
				} catch (SymbolDrawingException e1) {
					// IMPOSSIBLE TO REACH THIS
				}
			} else {
				// should be unreachable code
				throw new Error(PluginServices.getText(this, "symbol_shapetype_mismatch"));
			}
		}
    }

    /**
     * Añade al Layout un nuevo FFrameSymbol.
     *
     * @param layout Layout sobe el que se añade el FFrame.
     * @param x Posición X.
     * @param y Posición Y.
     * @param fs2d Símbolo a añadir.
     * @param h Altura.
     * @param wT Rectángulo del FFrame.
     * @param hT tamaño de la fuente.
     */
    private void toFFrameSymbol(LayoutContext layout, Rectangle2D re, double x,
            double y, ISymbol fs2d, float sizefont, double h) {
            double pW = 5;
            double wl = (re.getWidth() / pW);
            double haux = (sizefont * 0.7);
            Font font=null;
            if (fs2d instanceof ITextSymbol && (font=((ITextSymbol)fs2d).getFont()) != null) {
              ((ITextSymbol)fs2d).setFont(new Font(font.getFontName(), font.getStyle(),
                      (int) (wl / 6)));
            }

            Rectangle2D rAux3 = new Rectangle2D.Double(x, ((y + (h / 2)) - haux),
                    wl, haux);
            FFrameSymbol symbol =(FFrameSymbol)FrameFactory.createFrameFromName(FFrameSymbolFactory.registerName);

            symbol.setLayout(getLayout());
            symbol.setBoundBox(FLayoutUtilities.toSheetRect(rAux3, layout.getAT()));
            symbol.setSymbol(fs2d);
            layout.addFFrame(symbol, false, true);
        }

    /**
     * Devuelve el número total de capas incluyendo las subcapas.
     *
     * @param layers Capa a contar.
     *
     * @return Número de capas y subcapas.
     */
    private int getSizeNum(FLayers layers) {
        int n = 0;

        /////Aquí hay que calcular cuantos layers y sublayers hay, para saber que distancias dejar entre uno y otro.
        ///y además el tamaño de cada uno de ellos para saber que anchura dejar.
        int l=0;
        for (int i = layers.getLayersCount()-1; i>=0; i--) {
            FLayer layer = layers.getLayer(i);
            boolean b=false;
            if (nameLayers.size()>l && nameLayers.get(l).equals(layer.getName())) {
            	b=((Boolean)areVisible.get(l)).booleanValue();
            }else {
            	b=layer.isVisible();
            }
            l++;
            if (b) {
            //if (layer.isVisible()) {
                if (layer.getName().length() > m_max) {
                    m_max = layer.getName().length();
                }

                if (layer instanceof FLayers) {
                    //n++;
                	n = n + 3;
                    n += getSizeNum((FLayers) layer); //m_numLayers += getNumInLyrGroup((FLayers) layer);
                } else {
                	if (layer instanceof Classifiable) {
                    Classifiable cO = (Classifiable) layer;
                    n++;

                    if (cO.getLegend() instanceof IClassifiedLegend){// && !(cO instanceof FLyrAnnotation)) {
                        IClassifiedLegend cli = (IClassifiedLegend) cO.getLegend();

                        for (int j = 0; j < cli.getValues().length; j++) {
                            String s = cli.getDescriptions()[j];

                            if (s.length() > m_max) {
                                m_max = s.length();
                            }

                            n++;
                        }
                    }
                	}else{
                		  String s = layer.getName();

                          if (s.length() > m_max) {
                              m_max = s.length();
                          }

                          n++;
                	}

                }
            }
        }

        return n;
    }

    /**
     * Transforma el FFrameLegend en diferentes FFrameSymbol y FFrameText.
     *
     * @param layout Layout sobre el que añadir los FFrame nuevos y sobre el
     *        que elimnar el FFrameLegend anterior.
     */
    public void toFFrames(LayoutContext layout) {
        Rectangle2D rectangle = getBoundingBox(null);
        //Rectangle2D r = getBoundBox();
        double h = rectangle.getHeight() / m_numLayers;
        FLayers lays = layers;

        //layout.getEFS().startComplexCommand();
        //toFFrames(layout, lays, rectangle, r.getWidth(), r.getHeight(), h, 0);
        int[] n = new int[1];
        n[0] = 0;
        drawLegendOrToFFrame(null,rectangle,h,lays,n,layout);
		layout.delFFrame(this);

        ///layout.getFFrames().remove(this);
        //layout.getEFS().endComplexCommand();
    }

    /**
     * Rellena la fuente a utilizar al dibujar los String sobre el graphics.
     *
     * @param f Font.
     */
    public void setFont(Font f) {
        m_f = f;
    }

    /**
     * Devuelve la fuente que esta utilizando.
     *
     * @return Font.
     */
    public Font getFont() {
        if (m_f != null) {
        	return new Font(m_f.getName(),m_f.getStyle(),9);
        //    return new Font(m_f.getFontName(), m_f.getStyle(), 9);
        }
        return new Font("SansSerif",Font.PLAIN,9);
    }

    /**
     * @see com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame#getXMLEntity()
     */
    public XMLEntity getXMLEntity() throws SaveException {
        XMLEntity xml = super.getXMLEntity();

        try {
//            xml.putProperty("type", Layout.RECTANGLELEGEND);
            xml.putProperty("m_numLayers", m_numLayers);
            xml.putProperty("m_max", m_max);
            xml.putProperty("m_quality", m_quality);
            xml.putProperty("m_viewing", m_viewing);
            xml.putProperty("fontName", m_f.getFontName());
            xml.putProperty("fontStyle", m_f.getStyle());

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

            String[] s=new String[nameLayers.size()];
            boolean[] b=new boolean[nameLayers.size()];
            for (int i=0;i<nameLayers.size();i++) {
            	String k=(String)nameLayers.get(i);
            	s[i]=k;
            	b[i]=((Boolean)areVisible.get(i)).booleanValue();
            }
            xml.putProperty("nameLayers",s);
            xml.putProperty("areVisible",b);
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

        this.m_numLayers = xml.getIntProperty("m_numLayers");
        this.m_max = xml.getIntProperty("m_max");
        this.m_quality = xml.getIntProperty("m_quality");
        this.m_viewing = xml.getIntProperty("m_viewing");
        this.m_f = new Font(xml.getStringProperty("fontName"),
                xml.getIntProperty("fontStyle"), 9);

        if (xml.contains("index")) {
            fframeview = (FFrameView) l.getLayoutContext().getFFrame(xml.getIntProperty("index"));
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

        this.m_numLayers = xml.getIntProperty("m_numLayers");
        this.m_max = xml.getIntProperty("m_max");
        this.m_quality = xml.getIntProperty("m_quality");
        this.m_viewing = xml.getIntProperty("m_viewing");
        this.m_f = new Font(xml.getStringProperty("fontName"),
                xml.getIntProperty("fontStyle"), 9);
        setRotation(xml.getDoubleProperty("m_rotation"));

        if (xml.contains("index")) {
            dependenceIndex = xml.getIntProperty("index");
        }
        if (xml.contains("nameLayers")) {
        	String[] s=xml.getStringArrayProperty("nameLayers");
        	boolean[] b=xml.getBooleanArrayProperty("areVisible");
        	for (int i=0;i<s.length;i++) {
        		nameLayers.add(s[i]);
        		areVisible.add(new Boolean(b[i]));
        	}
        }
    }

    /*
     * @see com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame#getNameFFrame()
     */
    public String getNameFFrame() {
        return PluginServices.getText(this, "leyenda")+ num;
    }

    /*
     * @see com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame#print(java.awt.Graphics2D,
     *      java.awt.geom.AffineTransform)
     */
    public void print(Graphics2D g, AffineTransform at, Geometry geom,
			PrintAttributes properties) {
    	this.properties=properties;
        draw(g, at, null, null);
        this.properties=null;
    }

    /**
     * Actualiza las dependencias que tenga este FFrame con el resto.
     *
     * @param fframes Resto de FFrames.
     */
    public void initDependence(IFFrame[] fframes) {
        if ((dependenceIndex != -1) &&
                fframes[dependenceIndex] instanceof FFrameView) {
            fframeview = (FFrameView) fframes[dependenceIndex];
        }
    }

	public void initialize() {
		// TODO Auto-generated method stub

	}

	public void setNameLayers(ArrayList nameLayers) {
		this.nameLayers=nameLayers;
	}

	public void setAreVisible(ArrayList areVisible) {
		this.areVisible=areVisible;
	}

	public ArrayList getNameLayers() {
		return nameLayers;
	}

	public ArrayList getAreVisible() {
		return areVisible;
	}

	public void cloneActions(IFFrame frame) {
		// TODO Auto-generated method stub

	}

	public IFFrameDialog getPropertyDialog() {
		return new FFrameLegendDialog(getLayout(),this);
	}
}
