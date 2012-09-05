/*
 * Created on 16-jul-2004
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
package com.iver.cit.gvsig.project.documents.layout;

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.mapcontext.ViewPort;



/**
 * Clase que recoge métodos estáticos sobre el Layout.
 *
 * @author Vicente Caballero Navarro
 */
public class FLayoutUtilities {
	/**
	 * Devuelve true si las dos ArrayList que se le pasan como parametro son
	 * iguales.
	 *
	 * @param n lista anterior
	 * @param l lista actual
	 *
	 * @return true si los ArrayList son iguales.
	 */
	public static boolean isEqualList(ArrayList n, ArrayList l) {
		if (n.size() != l.size()) {
			return false;
		}

		for (int i = 0; i < n.size(); i++) {
			if (l.get(i) != n.get(i)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Pasa una distancia en pixels a unidades del folio.
	 *
	 * @param d distancia en pixels.
	 * @param at Matriz de transformación.
	 *
	 * @return distancia en unidades de folio.
	 */
	public static double toSheetDistance(double d, AffineTransform at) {
		double dist = d / at.getScaleX(); // pProv.x;

		return dist;
	}

	/**
	 * Pasa una distancia de coordenadas del folio a pixels.
	 *
	 * @param d distancia en coordenadas de folio.
	 * @param at Matriz de transformación.
	 *
	 * @return double en pixels.
	 */
	public static double fromSheetDistance(double d, AffineTransform at) {
		Point2D.Double pSheet1 = new Point2D.Double(0, 0);
		Point2D.Double pSheet2 = new Point2D.Double(1, 0);
		Point2D.Double pScreen1 = new Point2D.Double();
		Point2D.Double pScreen2 = new Point2D.Double();

		try {
			at.transform(pSheet1, pScreen1);
			at.transform(pSheet2, pScreen2);
		} catch (Exception e) {
			System.err.print(e.getMessage());
		}

		return pScreen1.distance(pScreen2) * d;
	}

	/**
	 * Pasa un punto en pixels a coordenadas del folio.
	 *
	 * @param pScreen pixels.
	 * @param at Matriz de transformación.
	 *
	 * @return Point2D en coordenadas de folio.
	 */
	public static Point2D.Double toSheetPoint(Point2D pScreen,
		AffineTransform at) {
		Point2D.Double pWorld = new Point2D.Double();
		AffineTransform at1;

		try {
			at1 = at.createInverse();
			at1.transform(pScreen, pWorld);
		} catch (NoninvertibleTransformException e) {
		}

		return pWorld;
	}

	/**
	 * Pasa un retángulo de pixels a coordenadas del folio.
	 *
	 * @param r rectángulo en coordenadas de pixels a coordenadas de folio.
	 * @param at Matriz de transformación.
	 *
	 * @return Rectangle2D en coordenadas de folio.
	 */
	public static Rectangle2D.Double toSheetRect(Rectangle2D r,
		AffineTransform at) {
		Point2D.Double pSheet = toSheetPoint(new Point2D.Double(r.getX(),
					r.getY()), at);
		Point2D.Double pSheetX = toSheetPoint(new Point2D.Double(r.getMaxX(),
					r.getMinY()), at);
		Point2D.Double pSheetY = toSheetPoint(new Point2D.Double(r.getMinX(),
					r.getMaxY()), at);
		Rectangle2D.Double res = new Rectangle2D.Double();
		res.setRect(pSheet.getX(), pSheet.getY(), pSheet.distance(pSheetX),
			pSheet.distance(pSheetY));

		return res;
	}

	/**
	 * Pasa de un punto en coordenadas del folio a pixels.
	 *
	 * @param pSheet punto en coordenadas de folio.
	 * @param at Matriz de transformación.
	 *
	 * @return Point2D en pixels.
	 */
	public static Point2D.Double fromSheetPoint(Point2D pSheet,
		AffineTransform at) {
		Point2D.Double pScreen = new Point2D.Double();

		try {
			at.transform(pSheet, pScreen);
		} catch (Exception e) {
			System.err.print(e.getMessage());
		}

		return pScreen;
	}

	/**
	 * Pasa un rectángulo en coordenadas del folio a pixels.
	 *
	 * @param r rectángulo en coordenadas de folio.
	 * @param at Matriz de transformación.
	 *
	 * @return Rectangle2D en pixels.
	 */
	public static Rectangle2D.Double fromSheetRect(Rectangle2D r,
		AffineTransform at) {
		Point2D.Double pSheet = new Point2D.Double(r.getX(), r.getY());
		Point2D.Double pSX = new Point2D.Double(r.getMaxX(), r.getMinY());
		Point2D.Double pSY = new Point2D.Double(r.getMinX(), r.getMaxY());
		Point2D.Double pScreen = new Point2D.Double();
		Point2D.Double pScreenX = new Point2D.Double();
		Point2D.Double pScreenY = new Point2D.Double();

		try {
			at.transform(pSheet, pScreen);
			at.transform(pSX, pScreenX);
			at.transform(pSY, pScreenY);
		} catch (Exception e) {
			System.err.print(e.getMessage());
		}

		Rectangle2D.Double res = new Rectangle2D.Double();
		res.setRect(pScreen.getX(), pScreen.getY(), pScreen.distance(pScreenX),
			pScreen.distance(pScreenY));

		return res;
	}

	/**
	 * Obtiene el punto ajustado al grid del layout.
	 *
	 * @param p Punto a ajustar.
	 * @param distX Distancia mínima en pixels de X.
	 * @param distY Distancia mínima en pixels de Y.
	 * @param at Matriz de transformación.
	 */
	public static Point getPointGrid(Point p, double distX, double distY,
			AffineTransform at) {

		Point2D.Double auxp = FLayoutUtilities.fromSheetPoint(new Point2D.Double(
						0, 0), at);
		int x = (int)(((p.getX() - distX) % distX) - ((auxp.x) % distX));
		int y = (int)(((p.getY() - distY) % distY) - ((auxp.y) % distY));
		return new Point((int)(p.getX()-x),(int)(p.getY()-y));
	}

	/**
	 * Cuando se dibuja sobre el graphics todo se tiene que situar en enteros y
	 * aquí lo que se comprueba es que si los valores que contiene el
	 * Rectangle2D, que toma como parámetro, supera los valores soportados por
	 * un entero.
	 *
	 * @param r Rectangle2D a comprobar si los valores que contiene no superan
	 * 		  a los que puede tener un entero.
	 *
	 * @return true si no se han superado los límites.
	 */
	public static boolean isPosible(Rectangle2D.Double r) {
		if ((r.getMaxX() > Integer.MAX_VALUE) ||
				(r.getMaxY() > Integer.MAX_VALUE) ||
				(r.getMinX() < Integer.MIN_VALUE) ||
				(r.getMinY() < Integer.MIN_VALUE) ||
				(r.getWidth() > Integer.MAX_VALUE) ||
				(r.getHeight() > Integer.MAX_VALUE)) {
			return false;
		}

		return true;
	}

	/**
	 * Devuelve un long representando a la escala en función  de que unidad de
	 * medida se pase como parámetro.
	 *
	 * @param map FMap
	 * @param h Rectángulo.
	 *
	 * @return escala.
	 */
/*	public static long getScaleView(FMap map, double h) {
		if (map == null) {
			return 0;
		}

		long scaleView = 1;
		double hextent = map.getViewPort().getExtent().getHeight();
		double hview = h;
		scaleView = (long) (Attributes.CHANGE[map.getViewPort().getMapUnits()] * (hextent / hview));

		return scaleView;
	}
	*/
	public static long getScaleView(ViewPort viewPort,double wcm,double wpixels) {
		double dpi = wpixels/wcm*2.54;
		IProjection proj = viewPort.getProjection();

		//if (viewPort.getImageSize() == null)
		//    return -1;
		if (viewPort.getAdjustedExtent() == null) {
			return 0;
		}

		if (proj == null || viewPort.getImageSize() == null) {
			return (long) (viewPort.getAdjustedExtent().getLength(1) / wcm * Attributes.CHANGE[viewPort
																					.getMapUnits()]);
		}

		return (long) proj.getScale(viewPort.getAdjustedExtent().getMinimum(0),
			viewPort.getAdjustedExtent().getMaximum(0),
			wpixels, dpi);
	}

}
