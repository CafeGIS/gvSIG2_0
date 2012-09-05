/*
 * Created on 19-feb-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
/* gvSIG. Sistema de Informaciï¿½n Geogrï¿½fica de la Generalitat Valenciana
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
 *   Av. Blasco Ibï¿½ï¿½ez, 50
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
package org.gvsig.fmap.mapcontext.rendering.symbols;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;

import org.gvsig.compat.CompatLocator;
import org.gvsig.compat.print.PrintAttributes;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.tools.task.Cancellable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.utiles.StringUtilities;
import com.iver.utiles.XMLEntity;


/**
 * Sï¿½mbolo utilizado para guardar las caracterï¿½sticas que se deben de aplicar a
 * los Shapes a dibujar.
 *
 * @author Vicente Caballero Navarro
 * @deprecated (jaume)
 */
public class FSymbol implements ISymbol{
	private static final Logger logger = LoggerFactory.getLogger(FSymbol.class);
//	 Para símbolos de tipo polígono.
	public final static int SYMBOL_STYLE_FILL_SOLID = 1;
	public final static int SYMBOL_STYLE_FILL_TRANSPARENT = 2;
	public final static int SYMBOL_STYLE_FILL_HORIZONTAL = 3;
	public final static int SYMBOL_STYLE_FILL_VERTICAL = 4;
	public final static int SYMBOL_STYLE_FILL_CROSS = 5;
	public final static int SYMBOL_STYLE_FILL_UPWARD_DIAGONAL = 6;
	public final static int SYMBOL_STYLE_FILL_DOWNWARD_DIAGONAL = 7;
	public final static int SYMBOL_STYLE_FILL_CROSS_DIAGONAL = 8;
	public final static int SYMBOL_STYLE_FILL_GRAYFILL = 9;
	public final static int SYMBOL_STYLE_FILL_LIGHTGRAYFILL = 10;
	public final static int SYMBOL_STYLE_FILL_DARKGRAYFILL = 11;

	//	Para símbolos de tipo Punto
	public final static int SYMBOL_STYLE_MARKER_CIRCLE = 30;
	public final static int SYMBOL_STYLE_MARKER_SQUARE = 31;
	public final static int SYMBOL_STYLE_MARKER_TRIANGLE = 32;
	public final static int SYMBOL_STYLE_MARKER_CROSS = 33;
	public final static int SYMBOL_STYLE_MARKER_TRUETYPE = 34;
	public final static int SYMBOL_STYLE_MARKER_IMAGEN = 35;
	public final static int SYMBOL_STYLE_DGNSPECIAL = 40;

	// Para símbolos de líneas
	public final static int SYMBOL_STYLE_LINE_SOLID = 60;
	public final static int SYMBOL_STYLE_LINE_DASH = 61;
	public final static int SYMBOL_STYLE_LINE_DOT = 62;
	public final static int SYMBOL_STYLE_LINE_DASHDOT = 63;
	public final static int SYMBOL_STYLE_LINE_DASHDOTDOT = 64;
	public final static int SYMBOL_STYLE_LINE_RAIL = 65;
	public final static int SYMBOL_STYLE_LINE_ARROW = 66;

	// Para símbolos de tipo texto
	public final static int SYMBOL_STYLE_TEXT_NORMAL = 90;
	public final static int SYMBOL_STYLE_TEXT_CURSIVE = 91;
	public final static int SYMBOL_STYLE_TEXT_BOLD = 92;
	public final static int SYMBOL_STYLE_TEXT_BOLDCURSIVE = 93;



	private static BufferedImage img =
		CompatLocator.getGraphicsUtils().createBufferedImage(1, 1,
			BufferedImage.TYPE_INT_ARGB);
	
	private static Rectangle rect = new Rectangle(0, 0, 1, 1);
	private int m_symbolType;
	private int m_Style;
	private boolean m_useOutline;
	private Color m_Color;
	private Color m_outlineColor;
	private Font m_Font;
	private Color m_FontColor;
	private float m_FontSize;
	private int rgb;
	private ImageObserver imgObserver;

	/**
	 * Si <code>m_bUseFontSize</code> viene a false entonces m_FontSize viene
	 * en unidades de mapa (metros)
	 */
	private boolean m_bUseFontSizeInPixels;

	/**
	 * <code>m_bDrawShape</code> indica si queremos dibujar el shape de fondo.
	 * Es ï¿½til cuando estï¿½s etiquetando y no quieres que se dibuje el sï¿½mbolo
	 * que te sirve de base para etiquetar.
	 */
	private boolean m_bDrawShape = true;
	private int m_Size;
	private Image m_Icon;
	private URI m_IconURI;
	private int m_Rotation;
	private Paint m_Fill;
	public String m_LinePattern = "0"; // Solo para poder mostrarlo cuando vamos a seleccionar un sï¿½mbolo

	// En realidad lo podemos ver de BasicStroke, pero....
	// ya veremos si luego lo quitamos.
	private Stroke m_Stroke;

	//private float m_stroke=0;
	// public int m_Transparency; // Ya la lleva dentro del Color
	private boolean m_bUseSize; // Si estï¿½ a true, m_Size viene en coordenadas de mundo real.
	private int m_AlingVert;
	private int m_AlingHoriz;
	private String m_Descrip;
	public Color m_BackColor;
	public Paint m_BackFill;
	private int resolutionPrinting=300;

    /**
     * Converts the comma-delimited string into a List of trimmed strings.
     *
     * @param linePattern a String with comma-delimited values
     * @param lineWidth DOCUMENT ME!
     *
     * @return a List of the Strings that were delimited by commas
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public static float[] toArray(String linePattern, float lineWidth) {
        StringTokenizer st = new StringTokenizer(linePattern, ",");
        int numTokens = st.countTokens();

        float[] array = new float[numTokens];

        for (int i = 0; i < numTokens; i++) {
            String string = st.nextToken();
            array[i] = Float.parseFloat(string) * lineWidth;

            if (array[i] <= 0) {
                return null;
            }
        }

        return array;
    }

	/**
	 * Crea un nuevo FSymbol.
	 * @deprecated use SymbologyFactory.createDefaultSymbol() instead
	 */
//    FSymbol() {
	private FSymbol() {
	}

	/**
	 * Creates a new FSymbol object.
	 *
	 * @param tipoSymbol Tipo de sï¿½mbolo.
	 * @param c Color.
	 * @deprecated use SymbologyFactory.createDefaultSymbol(shapeType, color) instead
	 */
//	public FSymbol(int tipoSymbol, Color c) {
	private FSymbol(int tipoSymbol, Color c) {
		createSymbol(tipoSymbol, c);
	}

	/**
	 * Crea un nuevo FSymbol.
	 *
	 * @param tipoSymbol Tipo de Sï¿½mbolo.
	 * 			case FConstant.SYMBOL_TYPE_POINT:
			case FConstant.SYMBOL_TYPE_POINTZ:
			case FConstant.SYMBOL_TYPE_MULTIPOINT:
				m_bUseSize = true; // Esto es lo primero que hay que hacer siempre

				// para evitar un StackOverflow
				m_useOutline = false;
				setStyle(FConstant.SYMBOL_STYLE_MARKER_SQUARE);
				setSize(5); //pixels

				break;

			case FConstant.SYMBOL_TYPE_LINE:
			case FConstant.SYMBOL_TYPE_POLYLINEZ:
			case FConstant.SYMBOL_TYPE_POLYGONZ:
				setStroke(new BasicStroke());
				setStyle(FConstant.SYMBOL_STYLE_LINE_SOLID);

				break;

			case FConstant.SYMBOL_TYPE_FILL:
			    setStroke(new BasicStroke());
				setStyle(FConstant.SYMBOL_STYLE_FILL_SOLID);

				break;
			case FShape.MULTI:
				m_bUseSize = true;
			    setStroke(new BasicStroke());
				setStyle(FConstant.SYMBOL_STYLE_FILL_SOLID);

				// setStyle(FConstant.SYMBOL_STYLE_MARKER_SQUARE);
				setSize(5); //pixels
				break;

			case FConstant.SYMBOL_TYPE_TEXT:
				setStroke(new BasicStroke());
				setStyle(FConstant.SYMBOL_STYLE_TEXT_NORMAL);
				setFont(new Font("Dialog",Font.PLAIN,12));
				break;

	 */
	public FSymbol(int tipoSymbol) {
//	private FSymbol(int tipoSymbol) {
		int numreg = (int) (Math.random() * 100);
		Color colorAleatorio = new Color(((numreg * numreg) + 100) % 255,
				(numreg + ((3 * numreg) + 100)) % 255, numreg % 255);

		createSymbol(tipoSymbol, colorAleatorio);
	}

	/**
	 * A partir de un sï¿½mbolo devuelve otro similar pero con el color de
	 * selecciï¿½n.
	 *
	 * @param sym Sï¿½mbolo a modificar.
	 *
	 * @return Sï¿½mbolo modificado.
	 */
	public static FSymbol getSymbolForSelection(FSymbol sym) {
		FSymbol selecSymbol = sym.fastCloneSymbol();
		selecSymbol.setColor(MapContext.getSelectionColor());

		selecSymbol.setFill(null);
		// 050215, jmorell: Si en los drivers cambiamos el estilo, aquï¿½ tenemos que
		// actualizar los cambios. SYMBOL_STYLE_MARKER_SQUARE --> SYMBOL_STYLE_DGNSPECIAL.
		if ((selecSymbol.getStyle() == SYMBOL_STYLE_FILL_TRANSPARENT)
		        || (selecSymbol.getStyle() == SYMBOL_STYLE_DGNSPECIAL)) {
			selecSymbol.setStyle(SYMBOL_STYLE_FILL_SOLID);
		} else if (selecSymbol.getStyle() == SYMBOL_STYLE_TEXT_BOLD ||
				selecSymbol.getStyle() == SYMBOL_STYLE_TEXT_BOLDCURSIVE ||
				selecSymbol.getStyle() == SYMBOL_STYLE_TEXT_CURSIVE ||
				selecSymbol.getStyle() == SYMBOL_STYLE_TEXT_NORMAL){
			selecSymbol.setFontColor(MapContext.getSelectionColor());
		}
		selecSymbol.rgb = MapContext.getSelectionColor().getRGB();

		return selecSymbol;
	}

	/**
	 * Clona el sï¿½mbolo actual.
	 *
	 * @return Nuevo sï¿½mbolo clonado.
	 */
	public FSymbol cloneSymbol() {
		return createFromXML(getXMLEntity());
	}

	/**
	 * Se usa para el sï¿½mbolo de selecciï¿½n. Es una forma
	 * rï¿½pida de clonar un sï¿½mbolo, sin hacerlo via XML.
	 * Vicente, no lo borres!!!
	 * @return
	 */
	public FSymbol fastCloneSymbol()
	{
		FSymbol nS = new FSymbol();


		nS.m_symbolType = m_symbolType;
		nS.m_Style = m_Style;
		nS.m_useOutline = m_useOutline;
		nS.m_Color = m_Color;
		nS.m_outlineColor = m_outlineColor;
		nS.m_Font = m_Font;
		nS.m_FontColor = m_FontColor;
		nS.m_FontSize = m_FontSize;
		nS.m_bUseFontSizeInPixels = m_bUseFontSizeInPixels;
		nS.m_bDrawShape = m_bDrawShape;
		nS.m_Size = m_Size;
		nS.m_Icon = m_Icon;
		nS.m_IconURI = m_IconURI;
		nS.m_Rotation = m_Rotation;
		nS.m_Fill = m_Fill;
		nS.m_Stroke = m_Stroke;
		// nS.m_Transparency =m_Transparency ;
		nS.m_bUseSize = m_bUseSize;
		nS.m_AlingVert = m_AlingVert;
		nS.m_AlingHoriz = m_AlingHoriz;
		nS.m_Descrip = m_Descrip;
		nS.m_BackColor = m_BackColor;
		nS.m_BackFill = m_BackFill;

		nS.m_LinePattern = m_LinePattern;

		return nS;
	}


	/**
	 * Crea un sï¿½mbolo a partir del tipo y el color.
	 *
	 * @param tipoSymbol Tipo de sï¿½mbolo.
	 * @param c Color del sï¿½mbolo a crear.
	 */
	private void createSymbol(int tipoSymbol, Color c) {
		// OJO: HE HECHO COINCIDIR LOS TIPOS DE SIMBOLO
		//FConstant.SYMBOL_TYPE_POINT, LINE Y FILL CON
		// FShape.POINT, LINE, POLYGON. EL .MULTI SE REFIERE
		// A MULTIPLES TIPO DENTRO DEL SHAPE, ASï¿½ QUE SERï¿½ UN
		// MULTISIMBOLO
		// Tipo de simbolo
		m_symbolType = tipoSymbol; // Para no recalcular el pixel, no usamos los set

		// Ponemos un estilo por defecto
		m_useOutline = true;
		m_Color = c;
		m_Stroke = null;
		m_Fill = null;

		m_FontColor = Color.BLACK;
		m_FontSize = 10;
		m_bUseFontSizeInPixels = true;

		m_Size = 2;

		switch (getSymbolType()) {
			case Geometry.TYPES.POINT:
			case Geometry.TYPES.MULTIPOINT:
				m_bUseSize = true; // Esto es lo primero que hay que hacer siempre

				// para evitar un StackOverflow
				m_useOutline = false;
				setStyle(SYMBOL_STYLE_MARKER_SQUARE);
				setSize(5); //pixels

				break;

			case Geometry.TYPES.CURVE:			
				setStroke(new BasicStroke());
				setStyle(SYMBOL_STYLE_LINE_SOLID);

				break;

			case Geometry.TYPES.SURFACE:
			    setStroke(new BasicStroke());
				setStyle(SYMBOL_STYLE_FILL_SOLID);

				break;
			case Geometry.TYPES.GEOMETRY:
				m_bUseSize = true;
			    setStroke(new BasicStroke());
				setStyle(SYMBOL_STYLE_FILL_SOLID);

				// setStyle(FConstant.SYMBOL_STYLE_MARKER_SQUARE);
				setSize(5); //pixels
				break;

			case Geometry.TYPES.TEXT:
				setStroke(new BasicStroke());
				setStyle(SYMBOL_STYLE_TEXT_NORMAL);
				setFont(new Font("Dialog",Font.PLAIN,12));
				break;
		}

		m_outlineColor = c.darker();

		calculateRgb();
	}

	/**
	 * Calcula el RGB del sï¿½mbolo.
	 */
	public void calculateRgb() {
		// Recalculamos el RGB
		Graphics2D g2 = img.createGraphics();

		try {
			FGraphicUtilities.DrawSymbol(g2, g2.getTransform(), rect, this);
		} catch (CreateGeometryException e) {
			logger.error("Creating a geometry", e);
			e.printStackTrace();
		}
		rgb = img.getRGB(0, 0);
	}

	/**
	 * Devuelve el rgb del sï¿½mbolo.
	 *
	 * @return rgb del sï¿½mbolo.
	 */
	public int getOnePointRgb() {
		return rgb;
	}

	/**
	 * @see com.iver.cit.gvsig.gui.layout.fframes.IFFrame#getXMLEntity()
	 */
	public XMLEntity getXMLEntity() {
		XMLEntity xml = new XMLEntity();
		xml.putProperty("className",this.getClass().getName());
		xml.putProperty("m_symbolType", getSymbolType());
		xml.putProperty("m_Style", getStyle());
		xml.putProperty("m_useOutline", isOutlined());

		if (getColor() != null) {
			xml.putProperty("m_Color", StringUtilities.color2String(getColor()));
		}

		if (getOutlineColor() != null) {
			xml.putProperty("m_outlineColor",
				StringUtilities.color2String(getOutlineColor()));
		}

		if (getFont() != null) {
			xml.putProperty("fontname", getFont().getName());
			xml.putProperty("fontstyle", getFont().getStyle());

			xml.putProperty("m_FontSize", getFontSize());
			xml.putProperty("m_FontColor",
				StringUtilities.color2String(getFontColor()));

		}
		xml.putProperty("m_bUseFontSize", isFontSizeInPixels());
		xml.putProperty("m_bDrawShape", isShapeVisible());
		xml.putProperty("m_Size", getSize());

		//xml.putProperty("m_Icon",m_Icon.);
		xml.putProperty("m_Rotation", getRotation());

		if (getFill() instanceof Color) {
			xml.putProperty("m_Fill",
				StringUtilities.color2String((Color) getFill()));
		}
		else
			if (getFill() != null)
			{
			    xml.putProperty("m_Fill", "WithFill");
			}


		xml.putProperty("m_LinePattern", m_LinePattern);

		//Ancho del stroke en float
		if (getStroke() != null) {
			xml.putProperty("m_stroke",
				((BasicStroke) getStroke()).getLineWidth());
		} else {
			xml.putProperty("m_stroke", 0f);
		}

		xml.putProperty("m_bUseSize", isSizeInPixels());
		xml.putProperty("m_AlingVert", getAlingVert());
		xml.putProperty("m_AlingHoriz", getAlingHoriz());
		xml.putProperty("m_Descrip", getDescription());

		if (m_BackColor != null) {
			xml.putProperty("m_BackColor",
				StringUtilities.color2String(m_BackColor));
		}

		if (m_BackFill instanceof Color) {
			xml.putProperty("m_BackFill",
				StringUtilities.color2String((Color) m_BackFill));
		}

		xml.putProperty("rgb", rgb);

		if (m_Icon != null)
		{
		    xml.putProperty("m_IconURI", m_IconURI);
		}

		return xml;
	}
	/**
	 * Crea el sï¿½mbolo a partir del xml.
	 *
	 * @param xml xml que contiene la informaciï¿½n para crear el sï¿½mbolo.
	 *
	 * @return Sï¿½mbolo creado a partir del XML.
	 */
	public static FSymbol createFromXML(XMLEntity xml) {
		FSymbol symbol = new FSymbol();
		symbol.setSymbolType(xml.getIntProperty("m_symbolType"));
		symbol.setStyle(xml.getIntProperty("m_Style"));
		// System.out.println("createFromXML: m_Style=" + xml.getIntProperty("m_Style"));

		symbol.setOutlined(xml.getBooleanProperty("m_useOutline"));

		if (xml.contains("m_Color")) {
			symbol.setColor(StringUtilities.string2Color(xml.getStringProperty(
						"m_Color")));
		}

		if (xml.contains("m_outlineColor")) {
			symbol.setOutlineColor(StringUtilities.string2Color(
					xml.getStringProperty("m_outlineColor")));
		}

		if (xml.contains("fontname")) {
			symbol.setFont(new Font(xml.getStringProperty("fontname"),
					xml.getIntProperty("fontstyle"),
					(int) xml.getFloatProperty("m_FontSize")));

			symbol.setFontColor(StringUtilities.string2Color(
					xml.getStringProperty("m_FontColor")));
			symbol.setFontSize(xml.getFloatProperty("m_FontSize"));

		}
		symbol.setFontSizeInPixels(xml.getBooleanProperty("m_bUseFontSize"));
		symbol.setShapeVisible(xml.getBooleanProperty("m_bDrawShape"));
		symbol.setSize(xml.getIntProperty("m_Size"));

		//xml.putProperty("m_Icon",m_Icon.);
		symbol.setRotation(xml.getIntProperty("m_Rotation"));

		if (xml.contains("m_Fill")) {
		    // TODO: Si es un Fill de tipo imagen, deberï¿½amos recuperar la imagen.
		    String strFill = xml.getStringProperty("m_Fill");
		    if (strFill.compareTo("WithFill") == 0) {
				symbol.setFill(FSymbolFactory.createPatternFill(symbol.getStyle(), symbol.getColor()));
			} else {
				symbol.setFill(StringUtilities.string2Color(strFill));
			}
		}


		symbol.m_LinePattern = xml.getStringProperty("m_LinePattern");

		//Ancho del stroke en float
        float lineWidth = xml.getFloatProperty("m_stroke");
        if (symbol.m_LinePattern.compareTo("0") == 0)
        {
            symbol.setStroke(new BasicStroke(lineWidth));
        }
        else
        {
            symbol.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_BEVEL, 1.0f,
                        toArray(symbol.m_LinePattern, lineWidth), 0));
        }

		symbol.setSizeInPixels(xml.getBooleanProperty("m_bUseSize"));
		symbol.setAlingVert(xml.getIntProperty("m_AlingVert"));
		symbol.setAlingHoriz(xml.getIntProperty("m_AlingHoriz"));
		symbol.setDescription(xml.getStringProperty("m_Descrip"));

		if (xml.contains("m_BackColor")) {
			symbol.m_BackColor = StringUtilities.string2Color(xml.getStringProperty(
						"m_BackColor"));
		}

		if (xml.contains("m_BackFill")) {
			symbol.m_BackFill = StringUtilities.string2Color(xml.getStringProperty(
						"m_BackFill"));
		}

		symbol.rgb = xml.getIntProperty("rgb");

		if (xml.contains("m_IconURI")) {
		    try {
                symbol.setIconURI(new URI(xml.getStringProperty("m_IconURI")));
            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
		}

		return symbol;
	}

	/**
	 * Introduce el estilo del sï¿½mbolo.
	 *
	 * @param m_Style The m_Style to set.
	 */
	public void setStyle(int m_Style) {
		this.m_Style = m_Style;

		//		 calculateRgb();
	}

	/**
	 * Devuelve el estilo del sï¿½mbolo.
	 *
	 * @return Returns the m_Style.
	 */
	public int getStyle() {
		return m_Style;
	}

	/**
	 * Introduce el tipo de sï¿½mbolo.
	 *
	 * @param m_symbolType The m_symbolType to set.
	 */
	public void setSymbolType(int m_symbolType) {
		this.m_symbolType = m_symbolType;
	}

	/**
	 * Devuelve el tipo de sï¿½mbolo.
	 *
	 * @return Returns the m_symbolType.
	 */
	public int getSymbolType() {
		return m_symbolType;
	}

	/**
	 * Introduce si el sï¿½mbolo contiene linea de brode o no.
	 *
	 * @param m_useOutline The m_useOutline to set.
	 */
	public void setOutlined(boolean m_useOutline) {
		this.m_useOutline = m_useOutline;

		//		 calculateRgb();
	}

	/**
	 * Devuelve si el sï¿½mbolo contiene o no linea de borde.
	 *
	 * @return Returns the m_useOutline.
	 */
	public boolean isOutlined() {
		return m_useOutline;
	}

	/**
	 * Introduce el color del sï¿½mbolo.
	 *
	 * @param m_Color The m_Color to set.
	 */
	public void setColor(Color m_Color) {
		this.m_Color = m_Color;
		calculateRgb();
	}

	/**
	 * Devuelve el color del sï¿½mbolo.
	 *
	 * @return Returns the m_Color.
	 */
	public Color getColor() {
		return m_Color;
	}

	/**
	 * Introduce el color de la lï¿½nea de borde.
	 *
	 * @param m_outlineColor The m_outlineColor to set.
	 */
	public void setOutlineColor(Color m_outlineColor) {
		this.m_outlineColor = m_outlineColor;

		//		 calculateRgb();
	}

	/**
	 * Devuelve el color de la lï¿½nea de borde.
	 *
	 * @return Returns the m_outlineColor.
	 */
	public Color getOutlineColor() {
		return m_outlineColor;
	}

	/**
	 * Introduce el Font del sï¿½mbolo.
	 *
	 * @param m_Font The m_Font to set.
	 */
	public void setFont(Font m_Font) {
		this.m_Font = m_Font;

		//		 calculateRgb();
	}

	/**
	 * Devuelve el Font del sï¿½mbolo.
	 *
	 * @return Returns the m_Font.
	 */
	public Font getFont() {
		return m_Font;
	}

	/**
	 * Introduce el color de la fuente.
	 *
	 * @param m_FontColor The m_FontColor to set.
	 */
	public void setFontColor(Color m_FontColor) {
		this.m_FontColor = m_FontColor;

		//		 calculateRgb();
	}

	/**
	 * Devuelve el color de la fuente.
	 *
	 * @return Returns the m_FontColor.
	 */
	public Color getFontColor() {
		return m_FontColor;
	}

	/**
	 * Introduce si se usa el tamaï¿½o de la fuente en pixels.
	 *
	 * @param m_bUseFontSize The m_bUseFontSize to set.
	 */
	public void setFontSizeInPixels(boolean m_bUseFontSize) {
		this.m_bUseFontSizeInPixels = m_bUseFontSize;

		// calculateRgb();
	}

	/**
	 * Devuelve true si el tamaï¿½o de la fuente esta seleccionado en pixels.
	 *
	 * @return Returns the m_bUseFontSize.
	 */
	public boolean isFontSizeInPixels() {
		return m_bUseFontSizeInPixels;
	}

	/**
	 * Introduce si el shape e visible o no lo es.
	 *
	 * @param m_bDrawShape The m_bDrawShape to set.
	 */
	public void setShapeVisible(boolean m_bDrawShape) {
		this.m_bDrawShape = m_bDrawShape;

		//		 calculateRgb();
	}

	/**
	 * Devuelve true si el shape es visible.
	 *
	 * @return Returns the m_bDrawShape.
	 */
	public boolean isShapeVisible() {
		return m_bDrawShape;
	}

	/**
	 * Introduce el tamaï¿½o del sï¿½mbolo.
	 *
	 * @param m_Size The m_Size to set.
	 */
	public void setSize(int m_Size) {
		this.m_Size = m_Size;

		//		 calculateRgb();
	}

	/**
	 * Devuelve el tamaï¿½o del sï¿½mbolo.
	 *
	 * @return Returns the m_Size.
	 */
	public int getSize() {
		return m_Size;
	}

	/**
	 * Introduce la imagen que hace de icono.
	 *
	 * @param m_Icon The m_Icon to set.
	 */
	public void setIcon(Image m_Icon) {
		this.m_Icon = m_Icon;

		//		 calculateRgb();
	}

	/**
	 * Devuelve el icono.
	 *
	 * @return Returns the m_Icon.
	 */
	public Image getIcon() {
		return m_Icon;
	}

	/**
	 * Introduce la rotaciï¿½n.
	 *
	 * @param m_Rotation The m_Rotation to set.
	 */
	public void setRotation(int m_Rotation) {
		this.m_Rotation = m_Rotation;

		//		 calculateRgb();
	}

	/**
	 * Devuelve la rotaciï¿½n.
	 *
	 * @return Returns the m_Rotation.
	 */
	public int getRotation() {
		return m_Rotation;
	}

	/**
	 * Introduce el relleno.
	 *
	 * @param m_Fill The m_Fill to set.
	 */
	public void setFill(Paint m_Fill) {
		this.m_Fill = m_Fill;

		//		 calculateRgb();
	}

	/**
	 * Devuelve el relleno.
	 *
	 * @return Returns the m_Fill.
	 */
	public Paint getFill() {
		return m_Fill;
	}

	/**
	 * Introduce el Stroke.
	 *
	 * @param m_Stroke The m_Stroke to set.
	 */
	public void setStroke(Stroke m_Stroke) {
		this.m_Stroke = m_Stroke;

		//		 calculateRgb();
	}

	/**
	 * Devuelve el Stroke.
	 *
	 * @return Returns the m_Stroke.
	 */
	public Stroke getStroke() {
		return m_Stroke;
	}

	/**
	 * Introduce si el tamaï¿½o del simbolo estï¿½ en pixels.
	 *
	 * @param m_bUseSize The m_bUseSize to set.
	 */
	public void setSizeInPixels(boolean m_bUseSize) {
		this.m_bUseSize = m_bUseSize;

		//		 calculateRgb();
	}

	/**
	 * Devuelve si el tamaï¿½o del sï¿½mbolo estï¿½ en pixels.
	 *
	 * @return Returns the m_bUseSize.
	 */
	public boolean isSizeInPixels() {
		return m_bUseSize;
	}

	/**
	 * Introduce la descripciï¿½n del sï¿½mbolo.
	 *
	 * @param m_Descrip The m_Descrip to set.
	 */
	public void setDescription(String m_Descrip) {
		this.m_Descrip = m_Descrip;
	}

	/**
	 * Devuelve la descripciï¿½n del sï¿½mbolo.
	 *
	 * @return Returns the m_Descrip.
	 */
	public String getDescription() {
		return m_Descrip != null ? m_Descrip : "Default";
	}

	/**
	 * Introduce la alineaciï¿½n en vertical.
	 *
	 * @param m_AlingVert The m_AlingVert to set.
	 */
	public void setAlingVert(int m_AlingVert) {
		this.m_AlingVert = m_AlingVert;

		//		 calculateRgb();
	}

	/**
	 * Devuelve la alineaciï¿½n en vertical.
	 *
	 * @return Returns the m_AlingVert.
	 */
	public int getAlingVert() {
		return m_AlingVert;
	}

	/**
	 * Introduce la alineaciï¿½n en horizontal.
	 *
	 * @param m_AlingHoriz The m_AlingHoriz to set.
	 */
	public void setAlingHoriz(int m_AlingHoriz) {
		this.m_AlingHoriz = m_AlingHoriz;

		// calculateRgb();
	}

	/**
	 * Devuelve la alineaciï¿½n en horizontal.
	 *
	 * @return Returns the m_AlingHoriz.
	 */
	public int getAlingHoriz() {
		return m_AlingHoriz;
	}


	/**
	 * Introduce el tamaï¿½o de la fuente.
	 *
	 * @param m_FontSize The m_FontSize to set.
	 */
	public void setFontSize(float m_FontSize) {
		this.m_FontSize = m_FontSize;
	}

	/**
	 * Devuelve el tamaï¿½o de la fuente.
	 *
	 * @return Returns the m_FontSize.
	 */
	public float getFontSize() {
		return m_FontSize;
	}
    public URI getIconURI() {
        return m_IconURI;
    }
    public void setIconURI(URI iconURI) {
        m_IconURI = iconURI;
        ImageIcon prov;
        try {
            prov = new ImageIcon(iconURI.toURL());
            m_Icon = prov.getImage();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
   	/**
	 * @return Returns the imgObserver.
	 */
	public ImageObserver getImgObserver() {
		return imgObserver;
	}

	/**
	 * @param imgObserver The imgObserver to set.
	 */
	public void setImgObserver(ImageObserver imgObserver) {
		this.imgObserver = imgObserver;
	}

	public ISymbol getSymbolForSelection() {
		return getSymbolForSelection(this);
	}

	public void draw(Graphics2D g, AffineTransform affineTransform, Geometry geom, Cancellable cancel) {
		FGraphicUtilities.DrawShape(g, affineTransform, geom, this);
	}

	public void getPixExtentPlus(Geometry geom, float[] distances, ViewPort viewPort, int dpi) {

	}

	public boolean isSuitableFor(Geometry geom) {
		return true;
	}

	public void drawInsideRectangle(Graphics2D g2, AffineTransform scaleInstance, Rectangle r, PrintAttributes properties) throws SymbolDrawingException {
		try {
			FGraphicUtilities.DrawSymbol(g2, scaleInstance, r, this);
		} catch (CreateGeometryException e) {
			
			throw new SymbolDrawingException(getSymbolType());
		}
	}

	public String getClassName() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setXMLEntity(XMLEntity xml) {
		// TODO Auto-generated method stub

	}

	public void print(Graphics2D g, AffineTransform at, Geometry shape, PrintAttributes properties){
		int strokeValue=0;
		BasicStroke stroke=(BasicStroke)this.getStroke();
		if (stroke != null && stroke.getLineWidth()!=0) {
			strokeValue=(int)stroke.getLineWidth();
			double d = strokeValue;
			
			int pq = properties.getPrintQuality();
			if (pq == PrintAttributes.PRINT_QUALITY_NORMAL){
				d *= (double) 300/72;
			}else if (pq == PrintAttributes.PRINT_QUALITY_HIGH){
				d *= (double) 600/72;
			}else if (pq == PrintAttributes.PRINT_QUALITY_DRAFT){
				// d *= 72/72; // (which is the same than doing nothing)
			}
			this.setStroke(new BasicStroke((int)d,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
		}
		draw(g, at, shape, null);
		this.setStroke(new BasicStroke(strokeValue));
	}

}
