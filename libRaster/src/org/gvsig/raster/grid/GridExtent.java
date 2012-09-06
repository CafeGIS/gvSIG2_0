/*******************************************************************************
		GridExtent.java
		Copyright (C) Victor Olaya
		
		This program is free software; you can redistribute it and/or modify
		it under the terms of the GNU General Public License as published by
		the Free Software Foundation; either version 2 of the License, or
		(at your option) any later version.

		This program is distributed in the hope that it will be useful,
		but WITHOUT ANY WARRANTY; without even the implied warranty of
		MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
		GNU General Public License for more details.

		You should have received a copy of the GNU General Public License
		along with this program; if not, write to the Free Software
		Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*******************************************************************************/ 
package org.gvsig.raster.grid;

import java.awt.geom.Point2D;

import org.gvsig.raster.datastruct.Extent;
/**
 * This class defines a grid system (coordinates and cellsize)
 * @author Victor Olaya (volaya@ya.com)
 */
public class GridExtent extends Extent {
	double cellSizeX = 1;
	double cellSizeY = -1;
	int    m_iNX;
	int    m_iNY;
	
	public GridExtent(){}
	
	/**
	 * Assign the extension value and cell size.
	 * @param minX minimun value in X coordinate
	 * @param minY minimun value in Y coordinate
	 * @param maxX maximun value in X coordinate
	 * @param maxY maximun value in Y coordinate
	 * @param dCellSize cell size
	 */
	public GridExtent(double minX, double minY, double maxX, double maxY, double dCellSize) {
		super(minX, minY, maxX, maxY);
		cellSizeX = dCellSize;
		cellSizeY = -dCellSize;
		recalculateNXAndNY();
	}
	
	/**
	 * Assign the extension value and cell size.
	 * @param minX minimun value in X coordinate
	 * @param minY minimun value in Y coordinate
	 * @param maxX maximun value in X coordinate
	 * @param maxY maximun value in Y coordinate
	 * @param dCellSize cell size
	 */
	public GridExtent(Extent extent, double dCellSize) {
		super(extent);
		cellSizeX = dCellSize;
		cellSizeY = -dCellSize;
		recalculateNXAndNY();
	}
	
	/**
	 * Assign the extension value and cell size.
	 * @param minX minimun value in X coordinate
	 * @param minY minimun value in Y coordinate
	 * @param maxX maximun value in X coordinate
	 * @param maxY maximun value in Y coordinate
	 * @param dCellSizeX cell size in X coordinate
	 * @param dCellSizeX cell size in X coordinate
	 */
	public GridExtent(double minX, double minY, double maxX, double maxY, double dCellSizeX, double dCellSizeY) {
		super(minX, minY, maxX, maxY);
		cellSizeX = dCellSizeX;
		cellSizeY = dCellSizeY;
		recalculateNXAndNY();
	}
	
	/**
	 * Assign the extension value and cell size.
	 * @param minX minimun value in X coordinate
	 * @param minY minimun value in Y coordinate
	 * @param maxX maximun value in X coordinate
	 * @param maxY maximun value in Y coordinate
	 * @param dCellSizeX cell size in X coordinate
	 * @param dCellSizeX cell size in X coordinate
	 */
	public GridExtent(Extent extent, double dCellSizeX, double dCellSizeY) {
		super(extent);
		cellSizeX = dCellSizeX;
		cellSizeY = dCellSizeY;
		recalculateNXAndNY();
	}
		
	public void setXRange(double dXMin, double dXMax){
		getMin().setLocation(Math.min(dXMin, dXMax), minY());
		getMax().setLocation(Math.max(dXMin, dXMax), maxY());
		recalculateNXAndNY();
	}
	
	public void setYRange(double dYMin, double dYMax){
		getMin().setLocation(minX(), Math.min(dYMin, dYMax));
		getMax().setLocation(maxX(), Math.max(dYMin, dYMax));
		recalculateNXAndNY();
		
	}

	/**
	 * Get cell size
	 * @return cell size in double value
	 */
	public double getCellSize() {
		return cellSizeX;
	}

	/**
	 * Set cell size and recalculate pixel distance in both axis
	 * @param cellSize cell size in double value
	 */
	public void setCellSize(double cellSize) {
		this.cellSizeX = cellSize;
		this.cellSizeY = -cellSize;
		recalculateNXAndNY();
	}

	/**
	 * Get pixel width 
	 * @return A integer with the pixel width value
	 */
	public int getNX() {		
		return m_iNX;
	}

	/**
	 * Get pixel height 
	 * @return A integer with the pixel height value
	 */
	public int getNY() {
		return m_iNY;
	}
	
	/**
	 * Calculates pixel width and pixel height
	 */
	private void recalculateNXAndNY(){
		m_iNY = (int) Math.abs(Math.floor((minY() - maxY()) / cellSizeY));
		m_iNX = (int) Math.abs(Math.floor((maxX() - minX()) / cellSizeX));
	}
	
	public boolean contains(double x, double y){
		return (x >= minX() && x <= maxX() && y >= minY() && y <= maxY());
	}
	
	public boolean fitsIn(GridExtent extent){
		
		boolean bFitsX, bFitsY;
		double dOffset;
		double dOffsetCols;
		double dOffsetRows;
		
		if ((extent.getCellSizeX() != this.getCellSizeX())||(extent.getCellSizeY() != this.getCellSizeY())){
			return false;
		}
		dOffset = Math.abs(extent.minX() - minX());
		dOffsetCols = dOffset / getCellSizeX();
		bFitsX = (dOffsetCols == Math.floor(dOffsetCols));

		dOffset = Math.abs(extent.maxY() - maxY());
		dOffsetRows = dOffset / getCellSizeY();
		bFitsY = (dOffsetRows == Math.floor(dOffsetRows));
		
		return bFitsX && bFitsY;
		
	}
	
	/**
	 * Compare a extent with the current GridExtent
	 * @param extent extent to compare
	 * @return true if two extents are equals and false if not
	 */
	public boolean equals(GridExtent extent){
		return 	(minX() == extent.minX() &&
			 	maxX() == extent.maxX() &&
			 	minY() == extent.minY() &&
			 	maxY() == extent.maxY() &&
			 	cellSizeX == extent.getCellSizeX() && 
			 	cellSizeY == extent.getCellSizeY());
	}
	
	/**
	 * Add the layer extent as current extent
	 * @param layer Layer to set the extent
	 */
	/*public void addRasterLayerToExtent(FLyrRaster layer){
		getMin().setLocation(Math.min(layer.getMinX(), minX()), Math.min(layer.getMinY(), minY()));
		getMax().setLocation(Math.max(layer.getMaxX(), maxX()), Math.max(layer.getMaxY(), maxY()));
				
		cellSize = Math.min(layer.getGrid().getXCellSize(), cellSize);
		recalculateNXAndNY();
	}*/
	
	public GridCell getGridCoordsFromWorldCoords(Point2D pt){
		int x = (int)Math.floor((pt.getX() - minX()) / cellSizeX);
		int y = (int)Math.ceil((maxY() - pt.getY()) / cellSizeY);
		GridCell cell = new GridCell(x, y, 0.0);
		
		return cell;
	}
	
	public Point2D getWorldCoordsFromGridCoords(GridCell cell){
		double x = minX() + (cell.getX() + 0.5) * cellSizeX;
		double y = maxY() - (cell.getY() + 0.5) * cellSizeY;
		
		Point2D pt = new Point2D.Double(x, y);
		
		return pt;
	}

	public double getCellSizeX() {
		return cellSizeX;
	}

	public void setCellSizeX(double cellSizeX) {
		this.cellSizeX = cellSizeX;
		recalculateNXAndNY();
	}

	public double getCellSizeY() {
		return cellSizeY;
	}

	public void setCellSizeY(double cellSizeY) {
		this.cellSizeY = cellSizeY;
		recalculateNXAndNY();
	}
}