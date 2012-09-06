/*******************************************************************************
		GridWrapperInterpolated
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
import org.gvsig.raster.buffer.BufferInterpolation;
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.buffer.RasterBufferInvalidAccessException;
import org.gvsig.raster.buffer.RasterBufferInvalidException;
import org.gvsig.raster.dataset.InvalidSetViewException;
import org.gvsig.raster.dataset.RasterDriverException;

/**
 * A grid wrapper that performs interpolation to calculate
 * cell values. This should be used when the window extent
 * does not 'fit' into the structure (coordinates and cellsize)
 * of the grid.
 * 
 * @author Victor Olaya
 */

public class GridInterpolated extends GridReader {

	public static final int INTERPOLATION_NearestNeighbour = BufferInterpolation.INTERPOLATION_NearestNeighbour;
	public static final int INTERPOLATION_Bilinear = BufferInterpolation.INTERPOLATION_Bilinear;
	public static final int INTERPOLATION_InverseDistance = BufferInterpolation.INTERPOLATION_InverseDistance;
	public static final int INTERPOLATION_BicubicSpline = BufferInterpolation.INTERPOLATION_BicubicSpline;
	public static final int INTERPOLATION_BSpline = BufferInterpolation.INTERPOLATION_BSpline;
	
	double m_dXMin , m_dYMax; //coordinates of the layer, not the window
	double m_dCellSizeX; //cellsize of the layer, not the window
	double m_dCellSizeY; //cellsize of the layer, not the window
	int m_iInterpolationMethod = INTERPOLATION_BSpline;

	/**
	 * Crea un objeto lector a partir de un buffer de datos y el extent de la extensión
	 * completa y de la ventana accedida.
	 * @param rb Buffer de datos
	 * @param layerExtent extent de la capa completa
	 * @param windowExtent Extent
	 * @param bands Número de bandas del origen
	 */
	public GridInterpolated(	RasterBuffer rb, 
								GridExtent layerExtent,
								GridExtent windowExtent,
								int[] bands){
		super(rb, layerExtent, windowExtent, bands);
		m_dXMin = layerExtent.minX();
		m_dYMax = layerExtent.maxY();
		
		m_dCellSizeX = layerExtent.getCellSizeX();
		m_dCellSizeY = layerExtent.getCellSizeY();
	}
	
	/**
	 * Crea un objeto lector a partir de una fuente de datos y el extent de la extensión
	 * completa y de la ventana accedida.
	 * @param ds Fuente de datos
	 * @param layerExtent extent de la capa completa
	 * @param windowExtent Extent
	 * @param bands Número de bandas del origen
	 */
	public GridInterpolated(	BufferFactory ds, 
								GridExtent layerExtent,
								GridExtent windowExtent,
								int[] bands){
		super(ds, layerExtent, windowExtent, bands);
		init();
	}
	
	/**
	 * Inicialización de la fuente de datos y carga del buffer.
	 */
	private void init(){
		m_dXMin = bufferFactory.getDataSource().getExtent().minX();
		m_dYMax = bufferFactory.getDataSource().getExtent().maxY();
		
		m_dCellSizeX = bufferFactory.getDataSource().getAffineTransform(0).getScaleX();
		m_dCellSizeY = bufferFactory.getDataSource().getAffineTransform(0).getScaleY();
		
		bufferFactory.setDrawableBands(bands);
	}
		
	/**
	 * Asigna el método de interpolación 
	 * @param iMethod
	 */
	public void setInterpolationMethod(int iMethod){
		m_iInterpolationMethod = iMethod;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.fmap.grid.GridReader#getCellValueAsByte(int, int)
	 */
	public byte getCellValueAsByte(int x, int y) throws RasterBufferInvalidAccessException, RasterBufferInvalidException {
		return  (byte)_getValueAt(x, y);
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.fmap.grid.GridReader#getCellValueAsShort(int, int)
	 */
	public short getCellValueAsShort(int x, int y) throws RasterBufferInvalidAccessException, RasterBufferInvalidException {
		return (short) _getValueAt(x, y);
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.fmap.grid.GridReader#getCellValueAsInt(int, int)
	 */
	public int getCellValueAsInt(int x, int y) throws RasterBufferInvalidAccessException, RasterBufferInvalidException {		
		return (int) _getValueAt(x, y);
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.fmap.grid.GridReader#getCellValueAsFloat(int, int)
	 */
	public float getCellValueAsFloat(int x, int y)  throws RasterBufferInvalidAccessException, RasterBufferInvalidException {
		return (float) _getValueAt(x, y);
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.fmap.grid.GridReader#getCellValueAsDouble(int, int)
	 */
	public double getCellValueAsDouble(int x, int y) throws RasterBufferInvalidAccessException, RasterBufferInvalidException {
		return _getValueAt(x, y);
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.fmap.grid.GridReader#getCellValueAsDouble(int, int)
	 */
	public double getCellValue(int x, int y) throws RasterBufferInvalidAccessException, RasterBufferInvalidException {
		return _getValueAt(x, y);
	}
	
	private double _getValueAt(int x, int y) throws RasterBufferInvalidAccessException, RasterBufferInvalidException {
		double dX = windowExtent.minX() + windowExtent.getCellSize() * (x + 0.5);
		double dY = windowExtent.maxY() - windowExtent.getCellSize() * (y + 0.5);
		return getValueAt(dX, dY);
	}

	
	/** Devuelve el valor interpolado para unas coordenadas de la imagen no enteras
	 *  la distancia se toma al vertice superior izquierdo del pixel
	 * */
	public double _getValueAt(double x, double y) throws RasterBufferInvalidAccessException, RasterBufferInvalidException {
		int x_int, y_int;
		x_int= (int)Math.floor(x);
		y_int= (int)Math.floor(y);
		double dX = windowExtent.minX() + windowExtent.getCellSize() * (x_int + (x-x_int));
		double dY = windowExtent.maxY() - windowExtent.getCellSize() * (y_int + (y-y_int));
		return getValueAt(dX, dY);
	}
	
	
	private double getValueAt(double xPosition, double yPosition) throws RasterBufferInvalidAccessException, RasterBufferInvalidException {
		int	x, y;
		double	dx, dy;
		double dValue;

		x	= (int) Math.floor(xPosition = (xPosition - m_dXMin) / m_dCellSizeX);
		y	= (int) Math.floor(yPosition = (yPosition - m_dYMax) / m_dCellSizeY);

		dValue = _getCellValueInLayerCoords(x, y);
		
		if(	!isNoDataValue(dValue) ) {

			dx	= xPosition - x;
			dy	= yPosition - y;
			
			switch( m_iInterpolationMethod ) {
			case INTERPOLATION_NearestNeighbour:
				dValue = _getValueNearestNeighbour (x, y, dx, dy);
				break;

			case INTERPOLATION_Bilinear:
				dValue	= _getValueBiLinear (x, y, dx, dy);
				break;

			case INTERPOLATION_InverseDistance:
				dValue	= _getValueInverseDistance(x, y, dx, dy);
				break;

			case INTERPOLATION_BicubicSpline:
				dValue	= _getValueBiCubicSpline (x, y, dx, dy);
				break;

			case INTERPOLATION_BSpline:
				dValue	= _getValueBSpline (x, y, dx, dy);
				break;
			}
		} else {
			dValue = getNoDataValue();
		}

		return dValue;
	}

	private double _getValueNearestNeighbour(int x, int y, double dx, double dy) throws RasterBufferInvalidAccessException, RasterBufferInvalidException {
		x += (int)(0.5 + dx);
		y += (int)(0.5 + dy);

		return _getCellValueInLayerCoords(x, y);
	}

	private  double _getValueBiLinear(int x, int y, double dx, double dy) throws RasterBufferInvalidAccessException, RasterBufferInvalidException {
		double	z = 0.0, n = 0.0, d;
		double dValue;
		
		dValue = _getCellValueInLayerCoords(x, y);
		if (!isNoDataValue(dValue)){
			 d = (1.0 - dx) * (1.0 - dy);
			 z += d * dValue; 
			 n += d;
		}
		
		dValue = _getCellValueInLayerCoords(x + 1, y);
		if (!isNoDataValue(dValue)){
			 d = (dx) * (1.0 - dy);
			 z += d * dValue; 
			 n += d;
		}
		
		dValue = _getCellValueInLayerCoords(x, y + 1);
		if (!isNoDataValue(dValue)){
			 d = (1.0 - dx) * (dy);
			 z += d * dValue; 
			 n += d;
		}
		
		dValue = _getCellValueInLayerCoords(x + 1, y + 1);
		if (!isNoDataValue(dValue)){
			 d = (dx) * (dy);
			 z += d * dValue; 
			 n += d;
		}

		if( n > 0.0 ){
			return( z / n );
		}

		return( getNoDataValue() );
	}

	private double _getValueInverseDistance(int x, int y, double dx, double dy) throws RasterBufferInvalidAccessException, RasterBufferInvalidException {
		double	z = 0.0, n = 0.0, d;
		double dValue;
		
		if( dx > 0.0 || dy > 0.0 ){

			dValue = _getCellValueInLayerCoords(x, y);
			if (!isNoDataValue(dValue)){
				d = 1.0 / Math.sqrt(dx*dx + dy*dy); 
				z += d * dValue; 
				n += d;
			}
			
			dValue = _getCellValueInLayerCoords(x + 1, y);
			if (!isNoDataValue(dValue)){
				d = 1.0 / Math.sqrt((1.0-dx)*(1.0-dx) + dy*dy); 
				z += d * dValue; 
				n += d;
			}
			
			dValue = _getCellValueInLayerCoords(x, y + 1);
			if (!isNoDataValue(dValue)){
				d = 1.0 / Math.sqrt(dx*dx + (1.0-dy)*(1.0-dy)); 
				z += d * dValue; 
				n += d;
			}
			
			dValue = _getCellValueInLayerCoords(x + 1, y + 1);
			if (!isNoDataValue(dValue)){
				d = 1.0 / Math.sqrt((1.0-dx)*(1.0-dx) + (1.0-dy)*(1.0-dy)); 
				z += d * dValue; 
				n += d;
			}

			if( n > 0.0 ){
				return( z / n );
			}
		}else
			return( _getCellValueInLayerCoords(x, y) );
		
		return( getNoDataValue());
	}

	private double _getValueBiCubicSpline(int x, int y, double dx, double dy) throws RasterBufferInvalidAccessException, RasterBufferInvalidException {
		int		i;
		double	a0, a2, a3, b1, b2, b3, c[], z_xy[][];

		c = new double[4];
		z_xy = new double[4][4];
		
		if( _get4x4Submatrix(x, y, z_xy) ){
			
			for(i=0; i<4; i++){
				a0		= z_xy[0][i] - z_xy[1][i];
				a2		= z_xy[2][i] - z_xy[1][i];
				a3		= z_xy[3][i] - z_xy[1][i];

				b1		= -a0 / 3.0 + a2       - a3 / 6.0;
				b2		=  a0 / 2.0 + a2 / 2.0;
				b3		= -a0 / 6.0 - a2 / 2.0 + a3 / 6.0;

				c[i]	= z_xy[1][i] + b1 * dx + b2 * dx*dx + b3 * dx*dx*dx;
			}

			a0		= c[0] - c[1];
			a2		= c[2] - c[1];
			a3		= c[3] - c[1];

			b1		= -a0 / 3.0 + a2       - a3 / 6.0;
			b2		=  a0 / 2.0 + a2 / 2.0;
			b3		= -a0 / 6.0 - a2 / 2.0 + a3 / 6.0;

			return( c[1] + b1 * dy + b2 * dy*dy + b3 * dy*dy*dy );
		}

		return( _getValueBiLinear(x, y, dx, dy) );
	}

	private double _getValueBSpline(int x, int y, double dx, double dy) throws RasterBufferInvalidAccessException, RasterBufferInvalidException {
		int		i, ix, iy;
		double	z, px, py, Rx[], Ry[], z_xy[][];
		
		Rx = new double[4];
		Ry = new double[4];
		z_xy = new double [4][4];

		if( _get4x4Submatrix(x, y, z_xy) ) {
			for(i=0, px=-1.0-dx, py=-1.0-dy; i<4; i++, px++, py++) {
				Rx[i]	= 0.0;
				Ry[i]	= 0.0;

				if( (z = px + 2.0) > 0.0 )
					Rx[i]	+=        z*z*z;
				if( (z = px + 1.0) > 0.0 )
					Rx[i]	+= -4.0 * z*z*z;
				if( (z = px + 0.0) > 0.0 )
					Rx[i]	+=  6.0 * z*z*z;
				if( (z = px - 1.0) > 0.0 )
					Rx[i]	+= -4.0 * z*z*z;
				if( (z = py + 2.0) > 0.0 )
					Ry[i]	+=        z*z*z;
				if( (z = py + 1.0) > 0.0 )
					Ry[i]	+= -4.0 * z*z*z;
				if( (z = py + 0.0) > 0.0 )
					Ry[i]	+=  6.0 * z*z*z;
				if( (z = py - 1.0) > 0.0 )
					Ry[i]	+= -4.0 * z*z*z;

				Rx[i]	/= 6.0;
				Ry[i]	/= 6.0;
			}

			for(iy=0, z=0.0; iy<4; iy++) {
				for(ix=0; ix<4; ix++){
					z	+= z_xy[ix][iy] * Rx[ix] * Ry[iy];
				}
			}
			return( z );
		}
		return( _getValueBiLinear(x, y, dx, dy) );
	}

	private boolean _get4x4Submatrix(int x, int y, double z_xy[][]) throws RasterBufferInvalidAccessException, RasterBufferInvalidException {
		int	ix, iy, px, py;
		double dValue;

		for(iy=0, py=y-1; iy<4; iy++, py++) {
			for(ix=0, px=x-1; ix<4; ix++, px++) {
				dValue = _getCellValueInLayerCoords(px, py);
				if (isNoDataValue(dValue)) {
					return false;
				} else {
					z_xy[ix][iy] = dValue;
				}
			}
		}
		return( true );
	}

	private double _getCellValueInLayerCoords(int x, int y) throws RasterBufferInvalidAccessException, RasterBufferInvalidException {
		int w, h;
		int pX = 0, pY = 0;
		if (bufferFactory != null) {
			w = bufferFactory.getSourceWidth();
			h = bufferFactory.getSourceHeight();
			try {
				if (y >= h || x >= w || x < 0 || y < 0)
					return getNoDataValue();
				bufferFactory.setAreaOfInterest(x, y, 1, 1);
			} catch (InvalidSetViewException e) {
				throw new RasterBufferInvalidAccessException("");
			} catch (RasterDriverException e) {
				throw new RasterBufferInvalidException("");
			} catch (InterruptedException e) {
				// La cancelación de la lectura de un pixel no es significativa
			}
			rasterBuf = (RasterBuffer) bufferFactory.getRasterBuf();
		} else {
			w = rasterBuf.getWidth();
			h = rasterBuf.getHeight();
			pX = x;
			pY = y;
		}

		if (x >= 0 && y >= 0 && x < w && y < h) {
			switch (dataType) {
				case RasterBuffer.TYPE_DOUBLE:
					return rasterBuf.getElemDouble(pY, pX, bandToOperate);
				case RasterBuffer.TYPE_INT:
					return (double) rasterBuf.getElemInt(pY, pX, bandToOperate);
				case RasterBuffer.TYPE_FLOAT:
					return (double) rasterBuf.getElemFloat(pY, pX, bandToOperate);
				case RasterBuffer.TYPE_BYTE:
					return ((byte) (rasterBuf.getElemByte(pY, pX, bandToOperate)) & 0xff);
				case RasterBuffer.TYPE_SHORT:
				case RasterBuffer.TYPE_USHORT:
					return (double) rasterBuf.getElemShort(pY, pX, bandToOperate);
			}
		}
		return getNoDataValue();
	}

	public byte[] getBandsValuesAsByte(int x, int y) {
		//TODO: FUNCIONALIDAD: getBandsValuesAsByte:Obtener los valores interpolados en todas las bandas
		return null;
	}

	public short[] getBandsValuesAsShort(int x, int y) {
		//TODO: FUNCIONALIDAD: getBandsValuesAsShort:Obtener los valores interpolados en todas las bandas
		return null;
	}

	public int[] getBandsValuesAsInt(int x, int y) {
		//TODO: FUNCIONALIDAD: getBandsValuesAsInt:Obtener los valores interpolados en todas las bandas
		return null;
	}

	public float[] getBandsValuesAsFloat(int x, int y) {
		//TODO: FUNCIONALIDAD: getBandsValuesAsFloat:Obtener los valores interpolados en todas las bandas
		return null;
	}

	public double[] getBandsValuesAsDouble(int x, int y) {
		//TODO: FUNCIONALIDAD: getBandsValuesAsDouble:Obtener los valores interpolados en todas las bandas
		return null;
	}
}
