/*******************************************************************************
    GridWrapperNotInterpolated
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

import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.buffer.RasterBufferInvalidAccessException;
import org.gvsig.raster.buffer.RasterBufferInvalidException;

/**
 * A grid wrapper that does not perform interpolation to 
 * calculate cell values. This should be used when the window 
 * extent 'fits' into the structure (coordinates and cellsize)
 * of the grid, so it is faster than using a grid wrapper with
 * interpolation
 * 
 * Upon construction, cellsizes are not checked, so they are assumed
 * to be equal. Use a QueryableGridWindow to safely create a GridWrapper
 * better than instantiating this class directly.
 * 
 * @author Victor Olaya (volaya@ya.com)
 */

public class GridNotInterpolated extends GridReader {
	
	//this offsets are in cells, not in map units.
	int m_iOffsetX;
	int m_iOffsetY;
	
	//width of the valid area (the RasterBuffer)
	int m_iWidth;
	int m_iHeight;
	
	/**
	 * Crea un objeto lector a partir de un buffer de datos y el extent de la extensión
	 * completa y de la ventana accedida.
	 * @param rb Buffer de datos
	 * @param layerExtent extent de la capa completa
	 * @param windowExtent Extent
	 * @param bands Número de bandas del origen
	 */
	public GridNotInterpolated(	RasterBuffer rb, 
								GridExtent layerExtent,
								GridExtent windowExtent,
								int[] bands) {
		super(rb, layerExtent, windowExtent, bands);
		m_iWidth = rb.getWidth();
		m_iHeight = rb.getHeight();
	}
	
	/**
	 * Crea un objeto lector a partir de una fuente de datos y el extent de la extensión
	 * completa y de la ventana accedida. La fuente de datos no tiene bandas asignadas ni área
	 * de interés. Estas son calculadas a partir de los extent pasados por parámetro.
	 * @param ds Fuente de datos
	 * @param layerExtent extent de la capa completa
	 * @param windowExtent Extent
	 * @param bands Número de bandas del origen
	 */
	public GridNotInterpolated(	BufferFactory ds, 
								GridExtent layerExtent,
								GridExtent windowExtent,
								int[] bands) {
		super(ds, layerExtent, windowExtent, bands);
		init();
	}
	
	/**
	 * Inicialización de la fuente de datos y carga del buffer.
	 */
	private void init() {
		double dMinX, dMaxX, dMinY, dMaxY;
		int iWindowMinX, iWindowMinY;	
		int iBufMinX, iBufMaxX, iBufMinY, iBufMaxY;	
		
		iWindowMinX = (int) ((windowExtent.minX() - layerExtent.minX() ) 
				/ windowExtent.getCellSizeX());
		iWindowMinY = (int) ((layerExtent.maxY() - windowExtent.maxY() ) 
				 / windowExtent.getCellSizeY());
		
		dMinX = Math.min(Math.max(windowExtent.minX(), layerExtent.minX()), layerExtent.maxX());
		dMinY = Math.min(Math.max(windowExtent.minY(), layerExtent.minY()), layerExtent.maxY());
		dMaxX = Math.max(Math.min(windowExtent.maxX(), layerExtent.maxX()), layerExtent.minX());
		dMaxY = Math.max(Math.min(windowExtent.maxY(), layerExtent.maxY()), layerExtent.minY());
		
		iBufMinX = (int) Math.floor((dMinX - windowExtent.minX()) / windowExtent.getCellSizeX())
					+ iWindowMinX;
		iBufMinY = (int) Math.floor((dMaxY - windowExtent.maxY()) / windowExtent.getCellSizeY())
					+ iWindowMinY;;
		iBufMaxX = (int) Math.floor((dMaxX - windowExtent.minX()) / windowExtent.getCellSizeX())
					+ iWindowMinX;
		iBufMaxY = (int) Math.floor((dMinY - windowExtent.maxY()) / windowExtent.getCellSizeY())
					+ iWindowMinY;
		
		m_iOffsetX = iBufMinX - iWindowMinX;
		m_iOffsetY = iBufMinY - iWindowMinY;
		
		m_iWidth = Math.abs(iBufMaxX - iBufMinX) ;
		m_iHeight = Math.abs(iBufMaxY - iBufMinY) ;
			
		try {
			bufferFactory.clearDrawableBand();
			bufferFactory.setDrawableBands(bands);
			bufferFactory.setAreaOfInterest(iBufMinX, iBufMinY, m_iWidth, m_iHeight);
			rasterBuf = (RasterBuffer)bufferFactory.getRasterBuf();
		} catch (Exception e){
			rasterBuf = null;
		}
	}	
	
	/**
	 * Comprueba si una coordenada pixel está dentro del grid
	 * @param x Coordenada X a comprobar
	 * @param y Coordenada Y a comprobar
	 * @return true si la coordenada X e Y pasada por parámetro cae dentro del grid
	 * y false si cae fuera.
	 */
	private boolean isInRasterBuf(int x, int y){
		if (rasterBuf != null)
			return x >= 0 && y >= 0 && x <  m_iWidth && y < m_iHeight;
		else
			return false;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.fmap.grid.GridReader#getCellValueAsByte(int, int)
	 */
	public byte getCellValueAsByte(int x, int y) throws RasterBufferInvalidAccessException, RasterBufferInvalidException {
		try{
			if (isInRasterBuf(x - m_iOffsetX, y -  m_iOffsetY))
				return rasterBuf.getElemByte(y -  m_iOffsetY, x - m_iOffsetX, bandToOperate);
			else
				return (byte) rasterBuf.getNoDataValue(); 
		}catch(ArrayIndexOutOfBoundsException e){
			throw new RasterBufferInvalidAccessException("Access to data type " + dataType + "invalid");
		}catch(NullPointerException e){
			throw new RasterBufferInvalidException("Null Buffer");
		}		
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.fmap.grid.GridReader#getCellValueAsShort(int, int)
	 */
	public short getCellValueAsShort(int x, int y) throws RasterBufferInvalidAccessException, RasterBufferInvalidException {
		try{
			if (isInRasterBuf(x - m_iOffsetX, y -  m_iOffsetY))
				return rasterBuf.getElemShort(y -  m_iOffsetY, x - m_iOffsetX, bandToOperate);
			else
				return (short)rasterBuf.getNoDataValue();
		}catch(ArrayIndexOutOfBoundsException e){
			throw new RasterBufferInvalidAccessException("Access to data type " + dataType + "invalid");
		}catch(NullPointerException e){
			throw new RasterBufferInvalidException("Null Buffer");
		}
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.fmap.grid.GridReader#getCellValueAsInt(int, int)
	 */
	public int getCellValueAsInt(int x, int y) throws RasterBufferInvalidAccessException, RasterBufferInvalidException {
		try{
			if (isInRasterBuf(x - m_iOffsetX, y -  m_iOffsetY))
				return rasterBuf.getElemInt(y -  m_iOffsetY, x - m_iOffsetX, bandToOperate);
			else
				return (int) rasterBuf.getNoDataValue();
		}catch(ArrayIndexOutOfBoundsException e){
			throw new RasterBufferInvalidAccessException("Access to data type " + dataType + "invalid");
		}catch(NullPointerException e){
			throw new RasterBufferInvalidException("Null Buffer");
		}		
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.fmap.grid.GridReader#getCellValueAsFloat(int, int)
	 */
	public float getCellValueAsFloat(int x, int y) throws RasterBufferInvalidAccessException, RasterBufferInvalidException {
		try{
			if (isInRasterBuf(x - m_iOffsetX, y -  m_iOffsetY))
				return rasterBuf.getElemFloat(y -  m_iOffsetY, x - m_iOffsetX, bandToOperate);
			else
				return (float) rasterBuf.getNoDataValue();
		}catch(ArrayIndexOutOfBoundsException e){
			throw new RasterBufferInvalidAccessException("Access to data type " + dataType + "invalid");
		}catch(NullPointerException e){
			throw new RasterBufferInvalidException("Null Buffer");
		}
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.fmap.grid.GridReader#getCellValueAsDouble(int, int)
	 */
	public double getCellValueAsDouble(int x, int y) throws RasterBufferInvalidAccessException, RasterBufferInvalidException {
		try{
			if (isInRasterBuf(x - m_iOffsetX, y -  m_iOffsetY))
				return rasterBuf.getElemDouble(y -  m_iOffsetY, x - m_iOffsetX, bandToOperate);
			else
				return rasterBuf.getNoDataValue();
		}catch(ArrayIndexOutOfBoundsException e){
			throw new RasterBufferInvalidAccessException("Access to data type " + dataType + "invalid");
		}catch(NullPointerException e){
			throw new RasterBufferInvalidException("Null Buffer");
		}
	}
	
	/**
	 * Obtiene el valor de una celda en double.
	 * @param x Posición X del valor que queremos recuperar
	 * @param y Posición Y del valor que queremos recuperar
	 * @return Valor de tipo double contenido en la posición especificada
	 */
	public double getCellValue(int x, int y)throws RasterBufferInvalidAccessException{
		try{
			if (dataType == RasterBuffer.TYPE_DOUBLE) {
	        	return rasterBuf.getElemDouble(x, y, 0);
	        } else if (dataType == RasterBuffer.TYPE_INT) {
	        	return (double) rasterBuf.getElemInt(x, y, 0);
	        } else if (dataType == RasterBuffer.TYPE_FLOAT) {
	        	return (double) rasterBuf.getElemFloat(x, y, 0);
	        } else if (dataType == RasterBuffer.TYPE_BYTE) {
	        	return (double) rasterBuf.getElemByte(x, y, 0);
	        } else if ((dataType == RasterBuffer.TYPE_SHORT) | (dataType == RasterBuffer.TYPE_USHORT)) {
	        	return (double) rasterBuf.getElemShort(x, y, 0);
	        }
		}catch(ArrayIndexOutOfBoundsException e){
			throw new RasterBufferInvalidAccessException("Access to data type " + dataType + "invalid");
		}catch(NullPointerException e){
			throw new RasterBufferInvalidAccessException("Access to data type " + dataType + "invalid");
		}
		return rasterBuf.getNoDataValue();
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.fmap.grid.GridReader#getBandsValuesAsByte(int, int)
	 */
	public byte[] getBandsValuesAsByte(int x, int y) throws RasterBufferInvalidAccessException, RasterBufferInvalidException {
		byte[] b = new byte[rasterBuf.getBandCount()];
		try{
			if (isInRasterBuf(x - m_iOffsetX, y -  m_iOffsetY)){
				try{
					rasterBuf.getElemByte(y -  m_iOffsetY, x - m_iOffsetX, b);
				}catch(ArrayIndexOutOfBoundsException e){
					throw new RasterBufferInvalidAccessException("Access to data type " + dataType + "invalid");
				}catch(NullPointerException e){
					throw new RasterBufferInvalidException("Null Buffer");
				}
				return b;
			}else{
				for(int i = 0; i < rasterBuf.getBandCount(); i++)
	        		b[i] = (byte)rasterBuf.getNoDataValue();
				return b;
			}
		}catch(Exception e){
			for(int i = 0; i < rasterBuf.getBandCount(); i++)
        		b[i] = (byte)rasterBuf.getNoDataValue();
			return b;
		}	
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.fmap.grid.GridReader#getBandsValuesAsShort(int, int)
	 */
	public short[] getBandsValuesAsShort(int x, int y) throws RasterBufferInvalidAccessException, RasterBufferInvalidException {
		short[] b = new short[rasterBuf.getBandCount()];
		try{
			if (isInRasterBuf(x - m_iOffsetX, y -  m_iOffsetY)){
				try{
					rasterBuf.getElemShort(y -  m_iOffsetY, x - m_iOffsetX, b);
				}catch(ArrayIndexOutOfBoundsException e){
					throw new RasterBufferInvalidAccessException("Access to data type " + dataType + "invalid");
				}catch(NullPointerException e){
					throw new RasterBufferInvalidException("Null Buffer");
				}
				return b;
			}else{
				for(int i = 0; i < rasterBuf.getBandCount(); i++)
	        		b[i] = (short)rasterBuf.getNoDataValue();
				return b;
			}
		}catch(Exception e){
			for(int i = 0; i < rasterBuf.getBandCount(); i++)
        		b[i] = (short)rasterBuf.getNoDataValue();
			return b;
		}
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.fmap.grid.GridReader#getBandsValuesAsInt(int, int)
	 */
	public int[] getBandsValuesAsInt(int x, int y) throws RasterBufferInvalidAccessException, RasterBufferInvalidException {
		int[] b = new int[rasterBuf.getBandCount()];
		try{
			if (isInRasterBuf(x - m_iOffsetX, y -  m_iOffsetY)){
				try{
					rasterBuf.getElemInt(y -  m_iOffsetY, x - m_iOffsetX, b);
				}catch(ArrayIndexOutOfBoundsException e){
					throw new RasterBufferInvalidAccessException("Access to data type " + dataType + "invalid");
				}catch(NullPointerException e){
					throw new RasterBufferInvalidException("Null Buffer");
				}
				return b;
			}else{
				for(int i = 0; i < rasterBuf.getBandCount(); i++)
	        		b[i] = (int)rasterBuf.getNoDataValue();
				return b;
			}
		}catch(Exception e){
			for(int i = 0; i < rasterBuf.getBandCount(); i++)
        		b[i] = (int)rasterBuf.getNoDataValue();
			return b;
		}
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.fmap.grid.GridReader#getBandsValuesAsFloat(int, int)
	 */
	public float[] getBandsValuesAsFloat(int x, int y)  throws RasterBufferInvalidAccessException, RasterBufferInvalidException {
		float[] b = new float[rasterBuf.getBandCount()];
		try{
			if (isInRasterBuf(x - m_iOffsetX, y -  m_iOffsetY)){
				try{
					rasterBuf.getElemFloat(y -  m_iOffsetY, x - m_iOffsetX, b);
				}catch(ArrayIndexOutOfBoundsException e){
					throw new RasterBufferInvalidAccessException("Access to data type " + dataType + "invalid");
				}catch(NullPointerException e){
					throw new RasterBufferInvalidException("Null Buffer");
				}
				return b;
			}else{
				for(int i = 0; i < rasterBuf.getBandCount(); i++)
	        		b[i] = (float)rasterBuf.getNoDataValue();
				return b;
			}
		}catch(Exception e){
			for(int i = 0; i < rasterBuf.getBandCount(); i++)
        		b[i] = (float)rasterBuf.getNoDataValue();
			return b;
		}
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.fmap.grid.GridReader#getBandsValuesAsDouble(int, int)
	 */
	public double[] getBandsValuesAsDouble(int x, int y) throws RasterBufferInvalidAccessException, RasterBufferInvalidException {
		double[] b = new double[rasterBuf.getBandCount()];
		try{
			if (isInRasterBuf(x - m_iOffsetX, y -  m_iOffsetY)){
				try{
					rasterBuf.getElemDouble(y -  m_iOffsetY, x - m_iOffsetX, b);
				}catch(ArrayIndexOutOfBoundsException e){
					throw new RasterBufferInvalidAccessException("Access to data type " + dataType + "invalid");
				}catch(NullPointerException e){
					throw new RasterBufferInvalidException("Null Buffer");
				}
				return b;
			}else{
				for(int i = 0; i < rasterBuf.getBandCount(); i++)
	        		b[i] = rasterBuf.getNoDataValue();
				return b;
			}
		}catch(Exception e){
			for(int i = 0; i < rasterBuf.getBandCount(); i++)
        		b[i] = rasterBuf.getNoDataValue();
			return b;
		}
	}
}
