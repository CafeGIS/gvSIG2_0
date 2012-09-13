/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 IVER T.I. and Generalitat Valenciana.
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
 */

package org.gvsig.fmap.raster.grid.roi;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.raster.datastruct.Extent;
import org.gvsig.raster.grid.Grid;
import org.gvsig.raster.grid.roi.ROI;

/**
 * Clase que representa una región de interes conformada por
 * elementos vectoriales.
 *
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 *
 */
public class VectorialROI extends ROI {

	/**
	 * Array de geometrías (IGeometry) que definen el ROI.
	 */
	private ArrayList				geometries   = null;
	/**
	 * Extent máximo de todas las geometrias
	 */
	private Extent                  extent       = null;

	public VectorialROI(Grid grid) {
		super(grid);
		geometries = new ArrayList();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.roi.ROI#isInside(double, double, double, double)
	 */
	public boolean isInside(double x, double y, double w, double h) {
		if((x + w) < extent.minX() || x > extent.maxX() || (y - h) > extent.maxY() || y < extent.minY())
			return false;
		for (int i = 0; i < geometries.size(); i++) {
			Rectangle2D celda = new Rectangle2D.Double(x, y, w, h);
			if(((Geometry)geometries.get(i)).intersects(celda))
				return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.roi.ROI#isInGrid(int, int)
	 */
	public boolean isInGrid(int x, int y) {
		double newX = x - xOffset;
		double newY = y - yOffset;
		//newY = getNY() - (newY + 1);
		if (newX>=0 && newX<=getNX() && newY >=0 && newY<=getNY()){
			//Cooordendas pixel a coordenadas "reales".
			double cellSize = getCellSize();
			double mapX = this.getGridExtent().getMin().getX()+newX*cellSize;
			double mapY = this.getGridExtent().getMax().getY()-newY*cellSize;
			for (int i = 0; i < geometries.size(); i++){
				Rectangle2D celda = new Rectangle2D.Double(mapX, mapY-cellSize, cellSize, cellSize);
				if(((Geometry)geometries.get(i)).intersects(celda))
					return true;
			}
		}
		return false;
	}

	/**
	 * Añade una geometría a la ROI
	 *
	 * @param geometry
	 */
	public void addGeometry(Geometry geometry){

		geometries.add(geometry);
		double cellSize = getGridExtent().getCellSize();

		Rectangle2D geometryBounds = geometry.getBounds();
		if(extent == null)
			extent = new Extent(geometryBounds.getMinX(), geometryBounds.getMaxY(), geometryBounds.getMaxX(), geometryBounds.getMinY());
		else
			extent = new Extent(Math.min(geometryBounds.getMinX(), extent.minX()),
								Math.max(geometryBounds.getMaxY(), extent.maxY()),
								Math.max(geometryBounds.getMaxX(), extent.maxX()),
								Math.min(geometryBounds.getMinY(), extent.minY()));

		double minX = geometryBounds.getMinX() - Math.abs(geometryBounds.getMinX() - getGrid().getGridExtent().getMin().getX()) % cellSize;
		double minY = (geometryBounds.getMinY()) - (Math.abs(geometryBounds.getMinY() - getGrid().getGridExtent().getMin().getY())) % cellSize;
		double maxX = (geometryBounds.getMaxX()  + cellSize) - Math.abs(geometryBounds.getMaxX() - getGrid().getGridExtent().getMin().getX()) % cellSize;
		double maxY = (geometryBounds.getMaxY() + cellSize) - (Math.abs(geometryBounds.getMaxY() - getGrid().getGridExtent().getMin().getY())) % cellSize;

		if (geometries.size() > 1){
			minX = Math.min(minX,getGridExtent().getMin().getX());
			minY = Math.min(minY,getGridExtent().getMin().getY());
			maxX = Math.max(maxX,getGridExtent().getMax().getX());
			maxY = Math.max(maxY,getGridExtent().getMax().getY());
		}

		getGridExtent().setXRange(minX, maxX);
		getGridExtent().setYRange(minY, maxY);

		xOffset = (int)((minX - getGrid().getGridExtent().getMin().getX())/cellSize);
		yOffset = (int)((getGrid().getGridExtent().getMax().getY()- maxY)/cellSize);
		getStatistic().setStatisticsCalculated(false);
		getStatistic().setAdvancedStatisticCalculated(false);
	}

	/**
	 * Eliminar la geometría con índice <code>index</code> de la ROI
	 *
	 * @param index indice de la geometría a eliminar
	 */
	public void deleteGeometry(int index){
		geometries.remove(index);
		getStatistic().setStatisticsCalculated(false);
		getStatistic().setAdvancedStatisticCalculated(false);

		//TODO: Reajustar el Extent.
	}

	/**
	 * Elimina todas las geometrías que definen la ROI.
	 *
	 */
	public void clear(){
		geometries.clear();
		getStatistic().setStatisticsCalculated(false);
		getStatistic().setAdvancedStatisticCalculated(false);

		//TODO: Reajustar el Extent.
	}

	/**
	 *
	 * @return ArryList con las geometrías que definen la ROI
	 */
	public ArrayList getGeometries() {
		return geometries;
	}
}
