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
package org.gvsig.raster.grid;

import org.gvsig.raster.buffer.BufferFactory;

/**
 * Interfaz que deben implementar los grid en los que se puede escribir datos.
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public interface IWritableGrid{
	
	public GridExtent getGridExtent();
	
	public void assign(byte value)throws GridException;
	
	public void assign(short value)throws GridException;
	
	public void assign(int value)throws GridException;
	
	public void assign(float value)throws GridException;
	
	public void assign(double value)throws GridException;
	
	public void assign(BufferFactory dataSource)throws GridException;
	
	public void assign(Grid driver)throws GridException;
	
	public void addToCellValue(int x, int y, byte value) throws GridException;
	
	public void addToCellValue(int x, int y, short value) throws GridException;
	
	public void addToCellValue(int x, int y, int value) throws GridException;
	
	public void addToCellValue(int x, int y, float value) throws GridException;
	
	public void addToCellValue(int x, int y, double value) throws GridException;
	
	public void assignNoData();
	
	public void setCellValue(int x, int y, byte value)throws OutOfGridException;
	
	public void setCellValue(int x, int y, short value)throws OutOfGridException;
	
	public void setCellValue(int x, int y, int value)throws OutOfGridException;
	
	public void setCellValue(int x, int y, float value)throws OutOfGridException;
	
	public void setCellValue(int x, int y, double value)throws OutOfGridException;
	
	public void add(Grid driver) throws GridException;
	
	public void multiply(double dValue) throws GridException;
	
	/**
	 * Asigna el valor que indica que una celda no tiene datos para el buffer
	 * @param dNoDataValue Valor NoData 
	 */
	public void setNoDataValue(double dNoDataValue);
	
	public void setNoData(int x, int y);
}
