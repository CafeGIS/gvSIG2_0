/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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

/* CVS MESSAGES:
*
* $Id: AbstractLineSymbol.java 21071 2008-06-02 10:55:35Z vcaballero $
* $Log$
* Revision 1.14  2007-09-21 12:25:32  jaume
* cancellation support extended down to the IGeometry and ISymbol level
*
* Revision 1.13  2007/09/19 16:25:47  caballero
* FFrameLegend
*
* Revision 1.11  2007/09/18 14:50:31  caballero
* Leyendas sobre el Layout
*
* Revision 1.10  2007/09/17 09:33:47  jaume
* some multishapedsymbol bugs fixed
*
* Revision 1.9  2007/08/13 11:36:50  jvidal
* javadoc
*
* Revision 1.8  2007/07/18 06:54:34  jaume
* continuing with cartographic support
*
* Revision 1.7  2007/07/03 10:58:29  jaume
* first refactor on CartographicSupport
*
* Revision 1.6  2007/06/29 13:07:01  jaume
* +PictureLineSymbol
*
* Revision 1.5  2007/05/08 08:47:39  jaume
* *** empty log message ***
*
* Revision 1.4  2007/03/20 16:00:00  jaume
* rename method
*
* Revision 1.3  2007/03/09 11:20:56  jaume
* Advanced symbology (start committing)
*
* Revision 1.2.2.3  2007/02/21 16:09:02  jaume
* *** empty log message ***
*
* Revision 1.2.2.2  2007/02/15 16:23:44  jaume
* *** empty log message ***
*
* Revision 1.2.2.1  2007/02/09 07:47:04  jaume
* Isymbol moved
*
* Revision 1.2  2007/01/24 17:58:22  jaume
* new features and architecture error fixes
*
* Revision 1.1  2007/01/16 11:50:44  jaume
* *** empty log message ***
*
*
*/
package org.gvsig.fmap.mapcontext.rendering.symbols;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.gvsig.compat.print.PrintAttributes;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.rendering.symbols.styles.ILineStyle;
import org.gvsig.fmap.mapcontext.rendering.symbols.styles.SimpleLineStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * AbstractLineSymbol is the class that implements the interface for line symbols.
 * It is considered as the father of all the XXXLineSymbols and will implement all the
 * methods that these classes had not developed (and correspond with one of the methods
 * of AbstractLineSymbol class).
 *
 *
 * @author   jaume dominguez faus - jaume.dominguez@iver.es
 */
public abstract class AbstractLineSymbol extends AbstractSymbol implements ILineSymbol {
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private static final Logger logger = LoggerFactory.getLogger(AbstractLineSymbol.class);
	private Color color = SymbologyFactory.DefaultSymbolColor;
	private ILineStyle lineStyle;


	public Color getColor() {
		return color;
	}

	public void setLineColor(Color color) {
		this.color = color;
	}

	 public int getOnePointRgb() {
		return color.getRGB();
	}

	public final int getSymbolType() {
		return Geometry.TYPES.CURVE;
	}

	public void getPixExtentPlus(Geometry geom, float[] distances, ViewPort viewPort, int dpi) {
		float cs = (float) getCartographicSize(viewPort, dpi, geom);
		// TODO and add the line offset
		distances[0] = cs;
		distances[1] = cs;
	}

	public boolean isSuitableFor(Geometry geom) {
		switch(geom.getType()) {
		// pasted from FSymbol
		case Geometry.TYPES.CURVE:
		case Geometry.TYPES.ARC:
			return true;
		}
		return false;
	}

	public ILineStyle getLineStyle() {
		if (lineStyle == null)
			lineStyle = new SimpleLineStyle();
		return lineStyle;
	}

	public void setLineStyle(ILineStyle lineStyle) {
		this.lineStyle = lineStyle;
	}

	public int getAlpha() {
		return color.getAlpha();
	}

	public void setAlpha(int outlineAlpha) {
		color = new Color(color.getRed(), color.getGreen(), color.getBlue(), outlineAlpha);
	}

	public void drawInsideRectangle(Graphics2D g, AffineTransform scaleInstance, Rectangle r, PrintAttributes properties) throws SymbolDrawingException {
		final int hGap = (int) (r.getWidth() * 0.1); // the left and right margins
		final int vPos = 1; 						 // the top and bottom margins
		final int splitCount = 3; 					 // number of lines
		final int splitHSize = (r.width - hGap - hGap) / splitCount;
		int hPos = hGap;
		boolean swap = false;

		GeneralPathX gpx = new GeneralPathX();
		gpx.moveTo(r.x + hPos, r.y + r.height-vPos);

		for (int i = 0; i < splitCount; i++) {
			swap = !swap;
			gpx.lineTo(r.x + hPos + splitHSize, (swap ? vPos : r.height-vPos) + r.y);
			hPos += splitHSize;
		}

		try {
			if (properties==null)
				draw(g, new AffineTransform(), geomManager.createSurface(gpx, SUBTYPES.GEOM2D), null);
			else
				print(g, new AffineTransform(), geomManager.createSurface(gpx, SUBTYPES.GEOM2D), properties);
		} catch (Exception e) {
			throw new SymbolDrawingException(SymbolDrawingException.UNSUPPORTED_SET_OF_SETTINGS);
		}

	}



	public void setCartographicSize(double cartographicSize, Geometry geom) {
		getLineStyle().setLineWidth((float) cartographicSize);
	}

	public double toCartographicSize(ViewPort viewPort, double dpi, Geometry geom) {
		double oldSize = getLineWidth();
		setCartographicSize(getCartographicSize(
								viewPort,
								dpi,
								geom),
							geom);
		return oldSize;
	}

	public double getCartographicSize(ViewPort viewPort, double dpi, Geometry geom) {
		return CartographicSupportToolkit.
					getCartographicLength(this,
										  getLineWidth(),
										  viewPort,
										  dpi);
	}

	public static Geometry offsetFShape(Geometry shp, double offset) {
		Geometry offsetFShape = null;
		if (shp != null) {
			if (offset == 0)
				return shp;

			ArrayList segments = new ArrayList(); //<Point2D[]>
			GeneralPathX gpx = new GeneralPathX(shp);
			PathIterator it = gpx.getPathIterator(null);
			double[] data = new double[6];
			Point2D segmentIni = null;
			Point2D segmentEnd = null;
			while (!it.isDone()) {
				switch (it.currentSegment(data)) {
				case PathIterator.SEG_MOVETO:
					segmentEnd = new Point2D.Double(
							data[0], data[1]);
					break;

				case PathIterator.SEG_LINETO:
					segmentEnd = segmentIni;
					segmentIni = new Point2D.Double(
							data[0], data[1]);

					segments.add(getParallel(segmentIni, segmentEnd, offset));
					break;

				case PathIterator.SEG_QUADTO:
					break;

				case PathIterator.SEG_CUBICTO:
					break;

				case PathIterator.SEG_CLOSE:
					break;
				}
			}

		}


		return offsetFShape;
	}

	private static Point2D[] getParallel(Point2D p1, Point2D p2, double distance) {
		Point2D[] pParallel=new Point2D[2];
		pParallel[0]=getPerpendicularPoint(p1,p2,p1,distance);
		pParallel[1]=getPerpendicularPoint(p1,p2,p2,distance);
		return pParallel;
	}

	/**
	 * Obtiene el punto que se encuentra a una distancia 'dist' de la recta
	 * p1-p2 y se encuentra en la recta perpendicular que pasa por perpPoint
	 *
	 * @param p1 Punto de la recta p1-p2
	 * @param p2 Punto de la recta p1-p2
	 * @param perpPoint Punto de la recta perpendicular
	 * @param dist Distancia del punto que se quiere obtener a la recta p1-p2
	 *
	 * @return DOCUMENT ME!
	 */
	private static Point2D getPerpendicularPoint(Point2D p1, Point2D p2,
		Point2D perpPoint, double dist) {
		Point2D[] p = getPerpendicular(p1, p2, perpPoint);
		Point2D unit = getUnitVector(p[0], p[1]);

		return new Point2D.Double(perpPoint.getX() + (unit.getX() * dist),
			perpPoint.getY() + (unit.getY() * dist));
	}


	/**
	 * Obtiene un par de puntos que definen la recta perpendicular a p1-p2 que
	 * pasa por el punto perp
	 *
	 * @param p1 punto de la recta p1-p2
	 * @param p2 punto de la recta p1-p2
	 * @param perp Punto por el que pasa la recta perpendicular, debe ser
	 * 		  distinto a p2
	 *
	 * @return Array con dos puntos que definen la recta resultante
	 */
	private static Point2D[] getPerpendicular(Point2D p1, Point2D p2,
		Point2D perp) {
		if ((p2.getY() - p1.getY()) == 0) {
			return new Point2D[] {
				new Point2D.Double(perp.getX(), 0),
				new Point2D.Double(perp.getX(), 1)
			};
		}

		//Pendiente de la recta perpendicular
		double m = (p1.getX() - p2.getX()) / (p2.getY() - p1.getY());

		//b de la funcion de la recta perpendicular
		double b = perp.getY() - (m * perp.getX());

		//Obtenemos un par de puntos
		Point2D[] res = new Point2D[2];

		res[0] = new Point2D.Double(0, (m * 0) + b);
		res[1] = new Point2D.Double(1000, (m * 1000) + b);

		return res;
	}

	/**
	 * Devuelve un vector unitario en forma de punto a partir de dos puntos.
	 *
	 * @param p1 punto origen.
	 * @param p2 punto destino.
	 *
	 * @return vector unitario.
	 */
	private static Point2D getUnitVector(Point2D p1, Point2D p2) {
		Point2D paux = new Point2D.Double(p2.getX() - p1.getX(),
				p2.getY() - p1.getY());
		double v = Math.sqrt(Math.pow(paux.getX(), 2d) +
				Math.pow(paux.getY(), 2d));
		paux = new Point2D.Double(paux.getX() / v, paux.getY() / v);

		return paux;
	}
	public void print(Graphics2D g, AffineTransform at, Geometry geom, PrintAttributes properties) {
		double originalSize = getLineWidth();
		double size=originalSize;
		// scale it to size
		int pq = properties.getPrintQuality();
		if (pq == PrintAttributes.PRINT_QUALITY_NORMAL){
			size *= (double) 300/72;
		}else if (pq == PrintAttributes.PRINT_QUALITY_HIGH){
			size *= (double) 600/72;
		}else if (pq == PrintAttributes.PRINT_QUALITY_DRAFT){
			// size *= 72/72; // (which is the same than doing nothing)
		}
		setLineWidth(size);
		draw(g,at,geom,null);
		setLineWidth(originalSize);
	}
}
