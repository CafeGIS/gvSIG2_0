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
package org.gvsig.raster.buffer.cache;

import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.IDataWriter;

/**
 * Sirve los datos desde un IBuffer para el driver de escritura de bloques de 
 * caché. La altura servida será igual a la del bloque de cache.
 * 
 * @deprecated Esta clase fue creada para que CacheDataServer salvara sus datos a tif. 
 * Se ha cambiado por la escritura de los bytes en disco porque era más óptimo. 
 * 
 * NO DEBE BORRARSE porque se usa para test pero no debería utilizarse para el trabajo
 * normal de escritura.
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class WriterBufferCompleteServer implements IDataWriter{
	private IBuffer 		buffer = null;

	public WriterBufferCompleteServer(IBuffer buffer){
		this.buffer = buffer;
	}

	public int[] readARGBData(int sizeX, int sizeY, int nBand) {
		return null;
	}

	public byte[][] readByteData(int sizeX, int sizeY) {
		int len = buffer.getWidth() * buffer.getHeight();
		byte[][] b = new byte[buffer.getBandCount()][len];
		for(int iBand = 0; iBand < buffer.getBandCount(); iBand ++)
			for(int j = 0; j < buffer.getHeight(); j ++)
				for(int i = 0; i < buffer.getWidth(); i ++)
					b[iBand][j * buffer.getWidth() + i] = buffer.getElemByte(j, i, iBand);
		return b;
	}

	public short[][] readShortData(int sizeX, int sizeY) {
		int len = buffer.getWidth() * buffer.getHeight();
		short[][] b = new short[buffer.getBandCount()][len];
		for(int iBand = 0; iBand < buffer.getBandCount(); iBand ++)
			for(int j = 0; j < buffer.getHeight(); j ++)
				for(int i = 0; i < buffer.getWidth(); i ++)
					b[iBand][j * buffer.getWidth() + i] = buffer.getElemShort(j, i, iBand);
		return b;
	}

	public int[][] readIntData(int sizeX, int sizeY) {
		int len = buffer.getWidth() * buffer.getHeight();
		int[][] b = new int[buffer.getBandCount()][len];
		for(int iBand = 0; iBand < buffer.getBandCount(); iBand ++)
			for(int j = 0; j < buffer.getHeight(); j ++)
				for(int i = 0; i < buffer.getWidth(); i ++)
					b[iBand][j * buffer.getWidth() + i] = buffer.getElemInt(j, i, iBand);
		return b;
	}

	public float[][] readFloatData(int sizeX, int sizeY) {
		int len = buffer.getWidth() * buffer.getHeight();
		float[][] b = new float[buffer.getBandCount()][len];
		for(int iBand = 0; iBand < buffer.getBandCount(); iBand ++)
			for(int j = 0; j < buffer.getHeight(); j ++)
				for(int i = 0; i < buffer.getWidth(); i ++)
					b[iBand][j * buffer.getWidth() + i] = buffer.getElemFloat(j, i, iBand);
		return b;
	}

	public double[][] readDoubleData(int sizeX, int sizeY) {
		int len = buffer.getWidth() * buffer.getHeight();
		double[][] b = new double[buffer.getBandCount()][len];
		for(int iBand = 0; iBand < buffer.getBandCount(); iBand ++)
			for(int j = 0; j < buffer.getHeight(); j ++)
				for(int i = 0; i < buffer.getWidth(); i ++)
					b[iBand][j * buffer.getWidth() + i] = buffer.getElemDouble(j, i, iBand);
		return b;
	}	
}