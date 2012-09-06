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

import org.gvsig.raster.buffer.RasterBufferInvalidAccessException;
import org.gvsig.raster.buffer.RasterBufferInvalidException;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.grid.Grid;
import org.gvsig.raster.grid.GridException;
import org.gvsig.raster.grid.OutOfGridException;

/**
 * Clase que representa una región de interes conformada por
 * un raster. 
 * 
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 *
 */
public class RasterROI extends ROI {
	
	/**
	 * Grid que define el ROI a modo de máscara
	 * (NO_DATA -> no isInGrid)
	 */
	private Grid maskGrid = null;

	public RasterROI(Grid grid) {
		super(grid);
		int bands[] = {1};
		try {
			maskGrid = new Grid(grid.getGridExtent(), grid.getGridExtent(), IBuffer.TYPE_INT, bands);
			getGridExtent().setXRange(maskGrid.getGridExtent().getMin().getX(), maskGrid.getGridExtent().getMax().getX());
			getGridExtent().setYRange(maskGrid.getGridExtent().getMin().getY(), maskGrid.getGridExtent().getMax().getY());
			for (int x = 0; x < maskGrid.getNX();x++)
				for (int y = 0; y<maskGrid.getNY();y++)
					maskGrid.setNoData(x, y);
		} catch (RasterBufferInvalidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isInGrid(int x, int y) {
		try {
			return (maskGrid.isInGrid(x, y) && !maskGrid.isNoDataValue(getValue(x, y)));
		} catch (GridException e) {
			return false;
		}
		
	}

	/**
	 * 
	 * @return Grid que define la ROI
	 */
	public Grid getMaskGrid() {
		return maskGrid;
	}
	
	/**
	 * Obtiene un valor de una celda del grid independientemente del tipo de dato.
	 * @param x Posición en X
	 * @param y Posición en Y
	 * @return Valor de la celda en formato double
	 * @throws RasterBufferInvalidAccessException 
	 * @throws RasterBufferInvalidAccessException
	 */
	private double getValue(int x, int y) throws GridException {
		switch(getMaskGrid().getDataType()) {
		case IBuffer.TYPE_BYTE:return (double)(getMaskGrid().getCellValueAsByte(x, y));
		case IBuffer.TYPE_SHORT:return (double)(getMaskGrid().getCellValueAsShort(x, y));
		case IBuffer.TYPE_INT:return (double)(getMaskGrid().getCellValueAsInt(x, y));
		case IBuffer.TYPE_FLOAT:return (double)getMaskGrid().getCellValueAsFloat(x, y);
		case IBuffer.TYPE_DOUBLE:return (double)getMaskGrid().getCellValueAsDouble(x, y);
		}
		return 0;
	}
	
	/**
	 * Añade el pixel con coordenadas <code>x</code>,<code>y</code> (coordenadas pixel) al ROI
	 * 
	 * @param x
	 * @param y
	 */
	public void addPoint(int x, int y) {
		try {
			maskGrid.setCellValue(x, y, 1);
			getStatistic().setStatisticsCalculated(false);
			getStatistic().setAdvancedStatisticCalculated(false);
		} catch (OutOfGridException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Excluye al pixel con coordenadas <code>x</code>,<code>y</code> de su participación en la ROI.
	 * 
	 * @param x
	 * @param y
	 */
	public void removePoint(int x, int y){
			maskGrid.setNoData(x, y);
			getStatistic().setStatisticsCalculated(false);
			getStatistic().setAdvancedStatisticCalculated(false);
	}
	
	/**
	 * Excluye a todos los píxeles de la ROI
	 *
	 */
	public void clear(){
		for(int x = 0; x < 	getGridExtent().getNX(); x++)
			for(int y = 0; y < getGridExtent().getNY(); y++)
				maskGrid.setNoData(x, y);
		getStatistic().setStatisticsCalculated(false);
		getStatistic().setAdvancedStatisticCalculated(false);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.roi.ROI#isInside(double, double, double, double)
	 */
	public boolean isInside(double x, double y, double w, double h) {
		return false;
	}

}
