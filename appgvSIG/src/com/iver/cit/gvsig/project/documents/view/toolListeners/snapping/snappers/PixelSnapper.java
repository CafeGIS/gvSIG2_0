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
package com.iver.cit.gvsig.project.documents.view.toolListeners.snapping.snappers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.mapcontrol.tools.snapping.snappers.AbstractSnapper;
import org.gvsig.fmap.mapcontrol.tools.snapping.snappers.ISnapperRaster;

import com.vividsolutions.jts.geom.Coordinate;

public class PixelSnapper extends AbstractSnapper implements ISnapperRaster {
	private Color refColor = Color.BLACK;
	private int tolColorR = 100;
	private int tolColorG = 100;
	private int tolColorB = 100;
	/* (non-Javadoc)
	 * @see com.iver.cit.gvsig.gui.cad.snapping.ISnapperRaster#getSnapPoint(java.awt.image.BufferedImage, java.awt.geom.Point2D, double, java.awt.geom.Point2D)
	 */
	public Point2D getSnapPoint(MapControl mapControl, Point2D point,
			double mapTolerance, Point2D lastPoint) {
		// Miramos dentro del rect�ngulo que define la tolerancia
		// y nos quedamos con el pixel que m�s se acerca al que buscamos
		BufferedImage img = mapControl.getImage();
		ViewPort vp = mapControl.getViewPort();
		Point2D pPixel = vp.fromMapPoint(point);
		int xPixel = (int) pPixel.getX();
		int yPixel = (int) pPixel.getY();
//		int centerRGB = img.getRGB(xPixel, yPixel);
		int centerRGB = refColor.getRGB();
		double x1 = ColorModel.getRGBdefault().getRed(centerRGB);
		double y1 = ColorModel.getRGBdefault().getGreen(centerRGB);
		double z1 = ColorModel.getRGBdefault().getBlue(centerRGB);
		Coordinate c = new Coordinate(x1, y1, z1);

		int half = vp.fromMapDistance(mapTolerance) / 2;
		double minDistColor = Double.MAX_VALUE;
		int xSnapped = -1;
		int ySnapped = -1;
		int fromX =xPixel -half;
		if (fromX <0) fromX = 0;
		int fromY =yPixel -half;
		if (fromY <0) fromY = 0;

		int toX =xPixel + half;
		if (toX > vp.getImageWidth()) toX = vp.getImageWidth();
		int toY =yPixel + half;
		if (toY > vp.getImageHeight()) toY = vp.getImageHeight();

		for (int testX= fromX; testX< toX; testX++)
		{
			for (int testY= fromY; testY< toY; testY++)
			{
				// System.out.println("Testing: " + testX + ", " + testY);
				int testRGB = img.getRGB(testX, testY);
				// TODO: Aqu� deber�amos trabajar con un ColorSpace y usar toCIEXYZ. Por ahora lo calculo con RGB.
				int r = ColorModel.getRGBdefault().getRed(testRGB);
				int g = ColorModel.getRGBdefault().getGreen(testRGB);
				int b = ColorModel.getRGBdefault().getBlue(testRGB);
				Coordinate cAux = new Coordinate(r, g, b);

				if (Math.abs(r-x1) < tolColorR)
					if (Math.abs(g-y1) < tolColorG)
						if (Math.abs(b-z1) < tolColorB)
						{
							double dist = c.distance(cAux);
							if (dist < minDistColor)
							{
								minDistColor = dist;
								xSnapped = testX;
								ySnapped = testY;
							}
						}
			}
		}
		Point2D pResul = null;
		if (xSnapped != -1)
		{
			pResul = vp.toMapPoint(xSnapped, ySnapped);
		}
		return pResul;
	}

	public void draw(Graphics g, Point2D pPixels) {
		g.setColor(getColor());
		int half = getSizePixels() / 2;
		g.drawOval((int) (pPixels.getX() - half),
				(int) (pPixels.getY() - half),
				getSizePixels(), getSizePixels());
	}

	public String getToolTipText() {
		return "pixel_point";
	}

}


