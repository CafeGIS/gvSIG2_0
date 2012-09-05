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
package org.gvsig.fmap.mapcontrol.tools.grid;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.mapcontext.ViewPort;


/**
 * Clase encargada de gestionar las diferentes operaciones que se realizan
 * sobre el grid.
 *
 * @author Vicente Caballero Navarro
 */
public class Grid {
	public static boolean DefaultShowGrid = false;
	public static double DefaultGridSizeX = 1000;
	public static double DefaultGridSizeY = 1000;
	public static boolean DefaultAdjustGrid=false;

	public static boolean SHOWGRID = false;
	public static double GRIDSIZEX = 1000;
	public static double GRIDSIZEY = 1000;
	public static boolean ADJUSTGRID=false;

	private boolean grid = SHOWGRID;
	private double gridSizeX = GRIDSIZEX;
	private double gridSizeY = GRIDSIZEY;
	private ViewPort viewport=null;
	private boolean adjustGrid=ADJUSTGRID;

	/**
	 * Inserta el viewPort.
	 *
	 * @param vp
	 */
	public void setViewPort(ViewPort vp) {
		viewport = vp;

//		if (gridSize == 0) {
//			gridSize = viewport.toMapDistance(25);
//		}
	}

	/**
	 * Ajusta un punto de la imagen que se pasa como  parámetro al handler más
	 * cercano si se encuentra lo suficientemente  cerca y devuelve la
	 * distancia del punto original al punto ajustado
	 *
	 * @param point
	 *
	 * @return Distancia del punto que se pasa como parámetro al punto ajustado
	 */
	public double adjustToGrid(Point2D point) {
		if (adjustGrid) {
			Point2D auxp = new Point2D.Double(0, 0);
			double x = ((point.getX() + gridSizeX) % gridSizeX) -
				((auxp.getX()) % gridSizeX);
			double y = ((point.getY() + gridSizeY) % gridSizeY) -
				((auxp.getY()) % gridSizeY);
			Point2D p = (Point2D) point.clone();
			if (x>gridSizeX/2){
				x=x-gridSizeX;
			}
			if (y>gridSizeY/2){
				y=y-gridSizeY;
			}
			point.setLocation((point.getX() - x), (point.getY() - y));
			return p.distance(point);
		}
		return Double.MAX_VALUE;
	}

	/**
	 * Dibuja el grid sobre el graphics que se pasa como parámetro
	 *
	 * @param g Graphics sobre el que dibujar el grid.
	 */
	public void drawGrid(Graphics g) {
		if (!grid) {
			return;
		}
		if (viewport.fromMapDistance(gridSizeX) > 3
				&& viewport.fromMapDistance(gridSizeY) > 3) {
			g.setColor(Color.lightGray);

			Envelope extent = viewport.getAdjustedExtent();
			Point2D auxp = new Point2D.Double(0, 0);
			if (extent==null)
				return;
			for (double i = extent.getMinimum(0); i < (extent.getMaximum(0) + gridSizeX); i += gridSizeX) {
				for (double j = extent.getMinimum(0); j < (extent.getMaximum(1) + gridSizeY); j += gridSizeY) {
					Point2D po = new Point2D.Double(i, j);
					Point2D point = viewport.fromMapPoint(po);
					double x = ((po.getX() + gridSizeX) % gridSizeX)
							- ((auxp.getX()) % gridSizeX);
					double y = ((po.getY() + gridSizeY) % gridSizeY)
							- ((auxp.getY()) % gridSizeY);
					x = (point.getX() - viewport.fromMapDistance(x));
					y = (point.getY() + viewport.fromMapDistance(y));

					g.drawRect((int) x, (int) y, 1, 1);

				}
			}
		}
	}

	/**
	 * Inserta un boolean que indica si se utiliza o no el grid y de esta forma
	 * dibujarse.
	 *
	 * @param b boolean
	 */
	public void setShowGrid(boolean b) {
		grid = b;
	}

	/**
	 * Devuelve true si se usa el grid.
	 *
	 * @return True si se usa el grid.
	 */
	public boolean isShowGrid() {
		return grid;
	}

	/**
	 * Inserta un boolean que indica si se ajusta o no al grid y de esta forma
	 * dibujarse.
	 *
	 * @param b boolean
	 */
	public void setAdjustGrid(boolean b) {
		adjustGrid = b;
	}

	/**
	 * Devuelve true si se ha de ajustar al grid.
	 *
	 * @return True si se está ajustando al grid.
	 */
	public boolean isAdjustGrid() {
		return adjustGrid;
	}

	public double getGridSizeX() {
		return gridSizeX;
	}
	public double getGridSizeY() {
		return gridSizeY;
	}
	public void setGridSizeX(double gridSize) {
		this.gridSizeX = gridSize;
	}
	public void setGridSizeY(double gridSize) {
		this.gridSizeY = gridSize;
	}
}
