/*
 * Created on 13-jul-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
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
package org.gvsig.fmap.geom.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.apache.batik.ext.awt.geom.PathLength;
import org.gvsig.fmap.geom.aggregate.MultiPoint;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.util.Converter;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.rendering.symbols.FGraphicUtilities;
import org.gvsig.fmap.mapcontext.rendering.symbols.FSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;

import com.iver.utiles.XMLEntity;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;


/**
 * Se utiliza para etiquetar. Las capas vectoriales tienen un arrayList
 * (m_labels) de FLabel, un FLabel por cada registro.
 *
 * @author FJP
 */
public class FLabel implements Cloneable {
	public final static byte LEFT_TOP = 0;
	public final static byte LEFT_CENTER = 1;
	public final static byte LEFT_BOTTOM = 2;
	public final static byte CENTER_TOP = 6;
	public final static byte CENTER_CENTER = 7;
	public final static byte CENTER_BOTTOM = 8;
	public final static byte RIGHT_TOP = 12;
	public final static byte RIGHT_CENTER = 13;
	public final static byte RIGHT_BOTTOM = 14;
	private String m_Str = null;
	private Point2D m_Orig = null;
	private double m_Height;
	public static final double SQUARE = Math.sqrt(2.0);
	/**
	 * <code>m_rotation</code> en grados, NO en radianes. Se convierte en
	 * radianes al dibujar.
	 */
	private double m_rotation;

	/**
	 * Valores posibles para <code>m_Justification</code>: Left/Top = 0
	 * Center/Top = 6                Right/Top = 12 Left/Center = 1
	 * Center/Center = 7            Right/Center = 13 Left/Bottom = 2
	 * Center/Bottom = 8            Right/Bottom = 14
	 */
	private byte m_Justification = LEFT_BOTTOM; // Por defecto
	private int m_Style;
	private Rectangle2D boundBox;
	private Color color;
	private static Font font=new Font("Dialog",Font.PLAIN,12);
	/**
	 * Crea un nuevo FLabel.
	 */
	public FLabel() {
	}

	/**
	 * Crea un nuevo FLabel.
	 *
	 * @param str DOCUMENT ME!
	 */
	public FLabel(String str) {
		m_Str = str;
	}

	/**
	 * Crea un nuevo FLabel.
	 *
	 * @param str
	 * @param pOrig
	 * @param heightText
	 * @param rotation
	 */
	public FLabel(String str, Point2D pOrig, double heightText, double rotation) {
		m_Str = str;
		m_Orig = pOrig;
		m_Height = heightText;
		m_rotation = rotation;
	}

	/**
	 * Introduce un nuevo FLabel al ya existente.
	 *
	 * @param label
	 */
	public void setFLabel(FLabel label) {
		m_Str = label.m_Str;
		m_Orig = label.m_Orig;
		m_Height = label.m_Height;
		m_rotation = label.m_rotation;
	}

	/**
	 * Clona el FLabel.
	 *
	 * @return Object clonado.
	 */
	public Object clone() {
		FLabel label=new FLabel(m_Str, m_Orig, m_Height, m_rotation);
		label.boundBox=(Rectangle2D)boundBox.clone();
		return label;
	}

	/**
	 * Devuelve la altura del Label.
	 *
	 * @return Returns the m_Height.
	 */
	public double getHeight() {
		return m_Height;
	}

	/**
	 * Devuelve el punto de origen.
	 *
	 * @return Returns the m_Orig.
	 */
	public Point2D getOrig() {
		return m_Orig;
	}

	/**
	 * Devuelve la rotación.
	 *
	 * @return Returns the m_rotation.
	 */
	public double getRotation() {
		return m_rotation;
	}

	/**
	 * Devuelve un String con el texto del Label.
	 *
	 * @return Returns the m_Str.
	 */
	public String getString() {
		return m_Str;
	}

	/**
	 * Introduce la altura del Label.
	 *
	 * @param height The m_Height to set.
	 */
	public void setHeight(double height) {
		m_Height = height;
	}

	/**
	 * Introduce el punto de origen del Label.
	 *
	 * @param orig The m_Orig to set.
	 */
	public void setOrig(Point2D orig) {
		m_Orig = orig;
	}

	/**
	 * Introduce la rotación a aplicar al Label.
	 *
	 * @param m_rotation The m_rotation to set.
	 */
	public void setRotation(double m_rotation) {
		this.m_rotation = Math.toRadians(-m_rotation);
	}

	/**
	 * Introduce el texto del Label.
	 *
	 * @param str The m_Str to set.
	 */
	public void setString(String str) {
		m_Str = str;
	}

	/**
	 * Valores posibles para <code>m_Justification</code>: Left/Top = 0
	 * Center/Top = 6                Right/Top = 12 Left/Center = 1
	 * Center/Center = 7            Right/Center = 13 Left/Bottom = 2
	 * Center/Bottom = 8            Right/Bottom = 14
	 *
	 * @return byte.
	 */
	public byte getJustification() {
		return m_Justification;
	}

	/**
	 * Valores posibles para <code>m_Justification</code>: Left/Top = 0
	 * Center/Top = 6                Right/Top = 12 Left/Center = 1
	 * Center/Center = 7            Right/Center = 13 Left/Bottom = 2
	 * Center/Bottom = 8            Right/Bottom = 14
	 *
	 * @param justification byte
	 */
	public void setJustification(byte justification) {
		m_Justification = justification;
	}

	/**
	 * Devuelve un Objeto XMLEntity con la información los atributos necesarios
	 * para poder después volver a crear el objeto original.
	 *
	 * @return XMLEntity.
	 */
	public XMLEntity getXMLEntity() {
		XMLEntity xml = new XMLEntity();
		xml.putProperty("className",this.getClass().getName());
		xml.setName("flabel");
		xml.putProperty("m_Height", m_Height);
		xml.putProperty("m_Justification", (int) m_Justification);
		xml.putProperty("m_OrigX", m_Orig.getX());
		xml.putProperty("m_OrigY", m_Orig.getY());
		xml.putProperty("m_rotation", m_rotation);
		xml.putProperty("m_Str", m_Str);

		return xml;
	}

	/**
	 * Crea un Objeto de esta clase a partir de la información del XMLEntity.
	 *
	 * @param xml XMLEntity
	 *
	 * @return Objeto de esta clase.
	 */
	public static FLabel createFLabel(XMLEntity xml) {
		FLabel label = new FLabel();
		label.setHeight(xml.getDoubleProperty("m_Height"));
		label.setJustification((byte) xml.getIntProperty("m_Justification"));
		label.setOrig(new Point2D.Double(xml.getDoubleProperty("m_OrigX"),
				xml.getDoubleProperty("m_OrigY")));
		label.setString("m_Str");

		return label;
	}

	/**
	 * Método estático que crea un FLabel a partir de una Geometry.
	 *
	 * @param geom Geometry.
	 *
	 * @return nuevo FLabel creado.
	 * @throws CreateGeometryException 
	 */
	public static FLabel createFLabel(org.gvsig.fmap.geom.Geometry geom) throws CreateGeometryException {
		float angle;
		Point2D pAux = createLabelPoint(geom);

		FLabel label = new FLabel();
		label.setOrig(pAux);
		switch (geom.getType()) {
			case org.gvsig.fmap.geom.Geometry.TYPES.CURVE:
           		PathLength pathLen = new PathLength(geom);
				float midDistance = pathLen.lengthOfPath() / 2;
				angle = pathLen.angleAtLength(midDistance);

				if (angle < 0) {
					angle = angle + (float) (2 * Math.PI);
				}
				if ((angle > (Math.PI / 2)) && (angle < ((3 * Math.PI) / 2))) {
					angle = angle - (float) Math.PI;
				}
				label.setRotation(Math.toDegrees(angle));
				break;
		} // switch

		return label;
	}
	public static Point2D createLabelPoint(org.gvsig.fmap.geom.Geometry geom) throws CreateGeometryException {
		Point2D pAux = null;
		switch (geom.getType()) {
			case org.gvsig.fmap.geom.Geometry.TYPES.POINT:
            	pAux = new Point2D.Double(((org.gvsig.fmap.geom.primitive.Point) geom).getX(),
						((org.gvsig.fmap.geom.primitive.Point) geom).getY());
				return pAux;

			case org.gvsig.fmap.geom.Geometry.TYPES.CURVE:
            	PathLength pathLen = new PathLength(geom);

				// if (pathLen.lengthOfPath() < width / mT.getScaleX()) return;
				float midDistance = pathLen.lengthOfPath() / 2;
				pAux = pathLen.pointAtLength(midDistance);
				return pAux;

			case org.gvsig.fmap.geom.Geometry.TYPES.SURFACE:
            	Geometry geo = Converter.geometryToJts(geom);

				if (geo == null) {
					return null;
				}

				Point pJTS = geo.getCentroid();

				if (pJTS != null) {
					org.gvsig.fmap.geom.Geometry pLabel = Converter.jtsToGeometry(pJTS);
					return new Point2D.Double(((org.gvsig.fmap.geom.primitive.Point) pLabel).getX(),
							((org.gvsig.fmap.geom.primitive.Point) pLabel).getY());
				}
				break;
            case org.gvsig.fmap.geom.Geometry.TYPES.MULTIPOINT:
            	int num=((MultiPoint)geom).getPrimitivesNumber();
    			Rectangle2D r=null;
    			if (num>0){
    				r= ((MultiPoint)geom).getPointAt(0).getBounds2D();
    				for (int i=1;i<num;i++){
    					org.gvsig.fmap.geom.primitive.Point fp=((MultiPoint)geom).getPointAt(i);
    					r.add(new Point2D.Double(fp.getX(),fp.getY()));
    				}
    			}
    			if (r!=null)
    			return new Point2D.Double(r.getCenterX(),r.getCenterY());
    			break;

		} // switch
				return null;
	}
	public void setTypeFont(String t) {
		font=Font.getFont(t,font);
	}
	public int getStyle() {
		return m_Style;
	}

	public void setBoundBox(Rectangle2D boundBox) {
		this.boundBox=boundBox;

	}
	public Rectangle2D getBoundBox(){
		return boundBox;
	}

	public Rectangle2D getBoundingBox(ViewPort vp) {
		return vp.fromMapRectangle(boundBox);
	}

	public void setColor(int i) {
		color=new Color(i);
	}

	public Color getColor() {
		return color;
	}
	/*public Font getFont(AffineTransform at,boolean isInPixels){
		if (!isInPixels) {
			float alturaPixels = (float) ((getHeight() * at.getScaleX())*SQUARE);
			//Font nuevaFuente = font.deriveFont(alturaPixels);//new Font(getTypeFont(),getStyle(),(int)alturaPixels);
			return font.deriveFont(alturaPixels);
		}
		//Font nuevaFuente = font.deriveFont((float)(getHeight()*SQUARE));//new Font(getTypeFont(),getStyle(),(int)(getHeight()*SQUARE));
		return font;//font.deriveFont((float)(getHeight()*SQUARE));
	}*/
	public Font getFont(AffineTransform at,Font font){
		float alturaPixels = (float) ((getHeight() * at.getScaleX())*SQUARE);
		//return font.deriveFont(alturaPixels);
        return font.deriveFont(font.getStyle(),alturaPixels);
	}

	/**
	 * Por ahora, para extender el renderizado de etiquetas, habría que
	 * crear otro FLabel y reimplementar este método para que haga caso
	 * a otros símbolos. Es decir, NO usar FGraphicUtilities
	 * @param g
	 * @param affineTransform
	 * @param theShape
	 * @param theSymbol
	 */
	public void draw(Graphics2D g, AffineTransform affineTransform, Shape theShape, ISymbol theSymbol) {
        FGraphicUtilities.DrawLabel(g, affineTransform,
                (org.gvsig.fmap.geom.Geometry) theShape, (FSymbol) theSymbol, this);


	}

}
