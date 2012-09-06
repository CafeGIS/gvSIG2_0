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

package org.gvsig.raster.grid.roi;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import org.gvsig.raster.buffer.RasterBufferInvalidAccessException;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.datastruct.Extent;
import org.gvsig.raster.grid.Grid;
import org.gvsig.raster.grid.GridCell;
import org.gvsig.raster.grid.GridException;
import org.gvsig.raster.grid.GridExtent;
import org.gvsig.raster.grid.IQueryableGrid;

/**
 * Clase que representa las Regiones de Interes. 
 * 
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es).
 *
 */
public abstract class ROI implements IQueryableGrid {
	
	private String 					name		   = "";
	private Color					color		   = null;
	/**
	 * Grid asociado a la ROI y que le proporciona los datos.
	 */
	private Grid 					grid 		   = null;
	private ROIStatistic			statistic 	   = null;
	/**
	 * Mínimo extent que contiene al ROI.
	 */
	private GridExtent 				roiExtent      = null;
	/**
	 * Coord. x del grid donde está laz esquina sup. iz. del roi (coords. pixel).
	 */
	protected int 					xOffset		   = 0;
	/**
	 * Coord. y del grid donde está la esquina sup. iz. del roi (coords. pixel).
	 */
	protected int					yOffset		   = 0;
	/**
	 * Subconjunto de bandas que abarca el ROI.
	 */
	private int 					bandsSubset[]   = null;
	
	/**
	 * Obtiene el extent máximo de todas las ROIs pasadas por parámetro.
	 * @param rois Lista de ROIs para calcular la extensión máxima que ocupan
	 * @return Entent máximo
	 */
	public static Extent getROIsMaximunExtent(ArrayList rois) {
		double minx = 0, miny = 0, maxx = 0, maxy = 0;
		for (int i = 0; i < rois.size(); i++) {
			Extent ext = ((ROI) rois.get(i)).getGridExtent();
			if(i == 0) {
				minx = ext.minX();
				miny = ext.minY();
				maxx = ext.maxX();
				maxy = ext.maxY();
			} else {
				if(ext.minX() < minx)
					minx = ext.minX();
				if(ext.minY() < miny)
					miny = ext.minY();
				if(ext.maxX() > maxx)
					maxx = ext.maxX();
				if(ext.maxY() > maxy)
					maxy = ext.maxY();
			}
		}
		return new Extent(minx, miny, maxx, maxy);
	}
	

	public ROI(Grid grid) {
		this.grid = grid;
		roiExtent = new GridExtent();
		roiExtent.setCellSize(grid.getCellSize());
		statistic = new ROIStatistic(this);
	}

	public ROI add(ROI roi){
		return null;
	}
	
	public ROI intersect(ROI roi){
		return null;
	}
	
	public ROI subtract(ROI roi){
		return null;
	}
	
	public ROI exclusiveOr(ROI roi){
		return null;
	}
	
	public double getAspect(int x, int y)
			throws GridException {
		if (isInGrid(xOffset+x, yOffset+y))
			return grid.getAspect(x, y);
		else return getNoDataValue();
	}

	public double getCellSize() {
		return grid.getCellSize();
	}

	/**
	 * Obtiene el valor de una celda de tipo byte. Si el punto no pertenece al
	 * ROI devuelve un valor NoData. La excepci?n ser? lanzada si intentamos 
	 * acceder a un tipo de dato erroneo.
	 * @param x Posici?n X en coordenadas pixel del ROI
	 * @param y Posici?n Y en coordenadas pixel del ROI
	 * @throws RasterBufferInvalidAccessException
	 * @return Valor byte
	 */
	public byte getCellValueAsByte(int x, int y)
			throws GridException {
		if (isInGrid(xOffset+x, yOffset+y))
			return grid.getCellValueAsByte(xOffset+x, yOffset+y);
		else 
			return (byte) grid.getNoDataValue();
	}

	/**
	 * Obtiene el valor de una celda de tipo double. Si el punto no pertenece al
	 * ROI devuelve un valor NoData. La excepci?n ser? lanzada si intentamos 
	 * acceder a un tipo de dato erroneo.
	 * @param x Posici?n X en coordenadas pixel del ROI
	 * @param y Posici?n Y en coordenadas pixel del ROI
	 * @throws RasterBufferInvalidAccessException
	 * @return Valor double
	 */
	public double getCellValueAsDouble(int x, int y)
			throws GridException {
		if (isInGrid(xOffset+x, yOffset+y))
			return grid.getCellValueAsDouble(xOffset+x, yOffset+y);
		else 
			return grid.getNoDataValue();
	}

	/**
	 * Obtiene el valor de una celda de tipo Float. Si el punto no pertenece al
	 * ROI devuelve un valor NoData. La excepci?n ser? lanzada si intentamos 
	 * acceder a un tipo de dato erroneo.
	 * @param x Posici?n X en coordenadas pixel del ROI
	 * @param y Posici?n Y en coordenadas pixel del ROI
	 * @throws RasterBufferInvalidAccessException
	 * @return Valor Float
	 */
	public float getCellValueAsFloat(int x, int y)
			throws GridException {
		if (isInGrid(xOffset+x, yOffset+y))
			return grid.getCellValueAsFloat(xOffset+x, yOffset+y);
		else 
			return (float) grid.getNoDataValue();
	}

	/**
	 * Obtiene el valor de una celda de tipo int. Si el punto no pertenece al
	 * ROI devuelve un valor NoData. La excepci?n ser? lanzada si intentamos 
	 * acceder a un tipo de dato erroneo.
	 * @param x Posici?n X en coordenadas pixel del ROI
	 * @param y Posici?n Y en coordenadas pixel del ROI
	 * @throws RasterBufferInvalidAccessException
	 * @return Valor int
	 */
	public int getCellValueAsInt(int x, int y)
			throws GridException {
		if (isInGrid(xOffset+x, yOffset+y))
			return grid.getCellValueAsInt(xOffset+x, yOffset+y);
		else 
			return (int) grid.getNoDataValue();
	}

	/**
	 * Obtiene el valor de una celda de tipo short. Si el punto no pertenece al
	 * ROI devuelve un valor NoData. La excepci?n ser? lanzada si intentamos 
	 * acceder a un tipo de dato erroneo.
	 * @param x Posici?n X en coordenadas pixel del ROI
	 * @param y Posici?n Y en coordenadas pixel del ROI
	 * @throws RasterBufferInvalidAccessException
	 * @return Valor short
	 */
	public short getCellValueAsShort(int x, int y)
			throws GridException {
		if (isInGrid(xOffset+x, yOffset+y))
			return grid.getCellValueAsShort(xOffset+x, yOffset+y);
		else 
			return (short) grid.getNoDataValue();
	}

	public int getDirToNextDownslopeCell(int x, int y) throws GridException {
		return getDirToNextDownslopeCell(x, y, true);
	}

	public int getDirToNextDownslopeCell(int x, int y,boolean bForceDirToNoDataCell)
													throws GridException {
		if (isInGrid(xOffset+x, yOffset+y))
			return grid.getDirToNextDownslopeCell(x, y, bForceDirToNoDataCell);
		else
			return -1;
	}

	public double getDistToNeighborInDir(int iDir) {
		return grid.getDistToNeighborInDir(iDir);
	}

	public GridExtent getGridExtent() {
		return roiExtent;
	}

	public int getLayerNX() {
		return grid.getLayerNX();
	}

	public int getLayerNY() {
		return grid.getLayerNY();
	}
	
	/**
	 * 
	 * @return número de celdas del ROI.
	 */
	public int getValues() throws GridException {
		if (!statistic.isStatisticsCalculated())
			statistic.calculateStatistics();
		return statistic.getValues();
	}

	public double getMaxValue() throws GridException {
		if (!statistic.isStatisticsCalculated())
			statistic.calculateStatistics();
		return statistic.getMax();
	}

	public double getMeanValue() throws GridException {
		if (!statistic.isStatisticsCalculated())
			statistic.calculateStatistics();
		return statistic.getMean();
	}

	public double getMinValue() throws GridException {
		if (!statistic.isStatisticsCalculated())
			statistic.calculateStatistics();
		return statistic.getMin();
	}

	/**
	* Devuelve la matriz de varianza-covarianza, si no se encuentra calculada se calcula
	* @retrun Matriz de varianza-covarianza
	*/
	public double[][] getVarCovMatrix() throws GridException {
		if(!statistic.isAdvancedStatisticsCalculated())
			statistic.calculateAdvancedStatistic();
		return statistic.getVarianceCovarianceMatrix();
	}
	
	public int getNX() {
		return roiExtent.getNX();
	}

	public int getNY() {
		return roiExtent.getNY();
	}

	public double getNoDataValue() {
		//		 TODO Ver si usar un nodatavalue especifico del roi.
		return grid.getNoDataValue();
	}

	public double getSlope(int x, int y) throws GridException {
		if (isInGrid(xOffset+x, yOffset+y))
			return grid.getSlope(x, y);
		else 
			return getNoDataValue();
	}

	public GridCell[] getSortedArrayOfCells() throws GridException {
		int i;
		int iX,iY;
		int iNX =  getNX();
		int iCells = getNX() * getNY();
		GridCell [] cells = null;
		GridCell cell = null;

		cells = new GridCell[iCells];

		for (i = 0; i < iCells; i++){
			iX = i % iNX;
			iY = i / iNX;
			switch(grid.getDataType()){
			case IBuffer.TYPE_BYTE: cell = new GridCell(iX, iY, getCellValueAsByte(iX, iY)); break;
			case IBuffer.TYPE_SHORT: cell = new GridCell(iX, iY, getCellValueAsShort(iX, iY)); break;
			case IBuffer.TYPE_INT: cell = new GridCell(iX, iY, getCellValueAsInt(iX, iY)); break;
			case IBuffer.TYPE_FLOAT: cell = new GridCell(iX, iY, getCellValueAsFloat(iX, iY)); break;
			case IBuffer.TYPE_DOUBLE: cell = new GridCell(iX, iY, getCellValueAsDouble(iX, iY)); break;
			}

			cells[i] = cell;
		}

		Arrays.sort(cells);

		return cells;
	}

	public double getVariance() throws GridException {
		if (!statistic.isStatisticsCalculated())
			statistic.calculateStatistics();
		return statistic.getVariance();
	}

	/**
	 * Determina si un punto (en coordenadas pixel) pertenece al ROI o no.
	 * @param x Coordenada X del punto a consultar
	 * @param y Coordenada Y del punto a consultar 
	 * @return true si el punto está dentro del grid y false si está fuera de él
	 */
	public abstract boolean isInGrid(int x, int y);
	
	/**
	 * Comprueba si el rectangulo pasado como parámetro cae dentro del ROI o fuera
	 * @param x Coordenada real X superior izquierda
	 * @param y Coordenada real Y superior izquierda
	 * @param w Ancho del rectangulo 
	 * @param h Alto del rectangulo
	 * @return true si cae dentro del ROI y false si cae fuera
	 */
	public abstract boolean isInside(double x, double y, double w, double h);	

	public boolean isNoDataValue(double dValue) {
		return grid.isNoDataValue(dValue);
	}

	public void setInterpolationMethod(int iMethod) {
		grid.setInterpolationMethod(iMethod);
	}

	public Grid getGrid() {
		return grid;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 *Establece el Subconjunto de bandas que abarca el ROI. 
	 *
	 */
	public void setBandsSubset(int bandsSubset[]){
		this.bandsSubset=bandsSubset;
		statistic.setStatisticsCalculated(false);
	}
	
	/**
	 * Restaura el subconjunto accesible por el ROI a todas las bandas del Grid.
	 */
	public void clearBandsSubset(){
		if (bandsSubset!=null){
			bandsSubset = null;
			statistic.setStatisticsCalculated(false);
		}
	}
	
	/**
	 * Asigna la banda sobre la que se realizan las operaciones. Por defecto es la banda 0
	 * con lo que para el uso de MDTs no habrá que modificar este valor.
	 * @param band Banda sobre la que se realizan las operaciones.
	 */
	public void setBandToOperate(int band){
		if(bandsSubset!=null){
			grid.setBandToOperate(bandsSubset[band]);
			statistic.setBandToOperate(bandsSubset[band]);
		}
		else{
			grid.setBandToOperate(band);
			statistic.setBandToOperate(band);
		}
	}

	/**
	 * 
	 * @return Número de bandas que abarca el ROI.
	 */
	public int getBandCount() {
		if (bandsSubset == null)
			return getGrid().getBandCount();
		return bandsSubset.length;
	}

	/**
	 * 
	 * @return Subconjunto de bandas que abarca el ROI.
	 */
	public int[] getBandsSubset() {
		return bandsSubset;
	}

	protected ROIStatistic getStatistic() {
		return statistic;
	}
}
