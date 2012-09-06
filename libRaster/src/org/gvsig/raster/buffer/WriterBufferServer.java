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
package org.gvsig.raster.buffer;

import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.IDataWriter;
/**
 * <code>WriterBufferServer</code> sirve los datos desde un IBuffer para el
 * driver de escritura. El driver de escritura que va a volcar un
 * <code>IBuffer</code> a imagen en disco va solicitando los datos por bloques
 * a medida que va salvando los anteriores. Esta clase es la encargada de hacer
 * la división en bloques del buffer a volcar e ir sirviendolos a medida que el
 * driver pide más datos.
 * </P>
 * <P>
 * Implementa el interfaz <code>IDataWriter</code> que es el que define
 * métodos necesarios para el driver. Además es una tarea incrementable por lo
 * que deberá implementar el interfaz <code>IIncrementable</code> para poder
 * mostrar el dialogo de incremento de tarea.
 * </P>
 *
 * @author Nacho Brodin (nachobrodin@gmail.com)
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 * @version 27/04/2007
 */
public class WriterBufferServer implements IDataWriter {
	private IBuffer      buffer      = null;
	private IBuffer      alphaBuffer = null;
	private int          row         = 0;
	/**
	 * Tamaño de bloque. Este se asignará en la primera petición
	 */
	private int          block       = 0;

	private double       percent     = 0;
	private double       increment   = 0;
	private int          nBand       = 0;

	/**
	 * Crea un nuevo <code>WriterBufferServer</code>
	 */
	public WriterBufferServer() {
	}

	/**
	 * Crea un nuevo <code>WriterBufferServer</code> con el buffer de datos.
	 * @param buffer
	 */
	public WriterBufferServer(IBuffer buffer) {
		setBuffer(buffer, -1);
	}

	/**
	 * Asigna el buffer de datos e inicializa variables de
	 * @param buffer
	 * @param nband Si es menor que cero sirve datos de todas las bandas. Si es
	 * mayor que cero sirve datos de la banda indicada por el valor.
	 */
	public void setBuffer(IBuffer buffer, int nband) {
		this.buffer = buffer;
		nBand = nband;
		row = 0;
		block = 0;
		percent = 0;
		increment = 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IDataWriter#readARGBData(int, int, int)
	 */
	public int[] readARGBData(int sizeX, int sizeY, int nBand) {
		return null;
	}

	/**
	 * Acciones de inicialización para la lectura de de un bloque
	 * @param sizeY Tamaño en Y del bloque
	 */
	private void initRead(int sizeY) {
		//Si es la primera linea se asigna el tamaño de bloque y el incremento
		if (row == 0) {
			block = sizeY;
			//nblocks = (int)Math.ceil(((double) buffer.getHeight() / (double) sizeY));
			increment = 100D / ((double) buffer.getHeight() / (double) sizeY);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IDataWriter#readByteData(int, int)
	 */
	public byte[][] readByteData(int sizeX, int sizeY) {
		initRead(sizeY);
		percent += increment;
		int len = buffer.getWidth() * sizeY;
		int nbands = (nBand < 0) ? buffer.getBandCount() : 1;
		byte[][] b;
		if (alphaBuffer != null) 
			b = new byte[nbands + 1][len];
		else
			b = new byte[nbands][len];
		if(nBand < 0) {
			for (int iBand = 0; iBand < nbands; iBand++)
				for (int j = row; j < (row + sizeY); j++)
					for (int i = 0; i < buffer.getWidth(); i++)
						b[iBand][(j % block) * buffer.getWidth() + i] = buffer.getElemByte(j, i, iBand);
		} else {
			for (int j = row; j < (row + sizeY); j++)
				for (int i = 0; i < buffer.getWidth(); i++)
					b[0][(j % block) * buffer.getWidth() + i] = buffer.getElemByte(j, i, nBand);
		}
		if (alphaBuffer != null) {
			for (int j = row; j < (row + sizeY); j++)
				for (int i = 0; i < alphaBuffer.getWidth(); i++) {
					b[nbands][(j % block) * alphaBuffer.getWidth() + i] = alphaBuffer.getElemByte(j, i, 0);
				}
		}
		row += sizeY;
		return b;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IDataWriter#readShortData(int, int)
	 */
	public short[][] readShortData(int sizeX, int sizeY) {
		initRead(sizeY);
		percent += increment;
		int len = buffer.getWidth() * sizeY;
		int nbands = (nBand < 0) ? buffer.getBandCount() : 1;
		short[][] b = new short[nbands][len];
		if(nBand < 0) {
			for (int iBand = 0; iBand < nbands; iBand++)
				for (int j = row; j < (row + sizeY); j++)
					for (int i = 0; i < buffer.getWidth(); i++)
						b[iBand][(j % block) * buffer.getWidth() + i] = buffer.getElemShort(j, i, iBand);
		} else {
			for (int j = row; j < (row + sizeY); j++)
				for (int i = 0; i < buffer.getWidth(); i++)
					b[0][(j % block) * buffer.getWidth() + i] = buffer.getElemShort(j, i, nBand);
		}
		row += sizeY;
		return b;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IDataWriter#readIntData(int, int)
	 */
	public int[][] readIntData(int sizeX, int sizeY) {
		initRead(sizeY);
		percent += increment;
		int len = buffer.getWidth() * sizeY;
		int nbands = (nBand < 0) ? buffer.getBandCount() : 1;
		int[][] b = new int[nbands][len];
		if(nBand < 0) {
			for (int iBand = 0; iBand < buffer.getBandCount(); iBand++)
				for (int j = row; j < (row + sizeY); j++)
					for (int i = 0; i < buffer.getWidth(); i++)
						b[iBand][(j % block) * buffer.getWidth() + i] = buffer.getElemInt(j, i, iBand);
		} else {
			for (int j = row; j < (row + sizeY); j++)
				for (int i = 0; i < buffer.getWidth(); i++)
					b[0][(j % block) * buffer.getWidth() + i] = buffer.getElemInt(j, i, nBand);
		}
		row += sizeY;
		return b;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IDataWriter#readFloatData(int, int)
	 */
	public float[][] readFloatData(int sizeX, int sizeY) {
		initRead(sizeY);
		percent += increment;
		int len = buffer.getWidth() * sizeY;
		int nbands = (nBand < 0) ? buffer.getBandCount() : 1;
		float[][] b = null;
		if (alphaBuffer != null) 
			b = new float[nbands + 1][len];
		else
			b = new float[nbands][len];
		if(nBand < 0) {
			for (int iBand = 0; iBand < buffer.getBandCount(); iBand++)
				for (int j = row; j < (row + sizeY); j++)
					for (int i = 0; i < buffer.getWidth(); i++)
						b[iBand][(j % block) * buffer.getWidth() + i] = buffer.getElemFloat(j, i, iBand);
		} else {
			for (int j = row; j < (row + sizeY); j++)
				for (int i = 0; i < buffer.getWidth(); i++)
					b[0][(j % block) * buffer.getWidth() + i] = buffer.getElemFloat(j, i, nBand);
		}
		
		if (alphaBuffer != null) {
			for (int j = row; j < (row + sizeY); j++)
				for (int i = 0; i < alphaBuffer.getWidth(); i++) {
					b[nbands][(j % block) * alphaBuffer.getWidth() + i] = alphaBuffer.getElemByte(j, i, 0);
				}
		}
		
		row += sizeY;
		return b;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.IDataWriter#readDoubleData(int, int)
	 */
	public double[][] readDoubleData(int sizeX, int sizeY) {
		initRead(sizeY);
		percent += increment;
		int len = buffer.getWidth() * sizeY;
		int nbands = (nBand < 0) ? buffer.getBandCount() : 1;
		double[][] b = new double[nbands][len];
		if(nBand < 0) {
			for (int iBand = 0; iBand < buffer.getBandCount(); iBand++)
				for (int j = row; j < (row + sizeY); j++)
					for (int i = 0; i < buffer.getWidth(); i++)
						b[iBand][(j % block) * buffer.getWidth() + i] = buffer.getElemDouble(j, i, iBand);
		} else {
			for (int j = row; j < (row + sizeY); j++)
				for (int i = 0; i < buffer.getWidth(); i++)
					b[0][(j % block) * buffer.getWidth() + i] = buffer.getElemDouble(j, i, nBand);
		}
		row += sizeY;
		return b;
	}

	/**
	 * Obtiene el porcentaje de incremento de la lectura de datos
	 * @return
	 */
	public int getPercent() {
		return Math.min((int)percent, 100);
	}

	/**
	 * @param alphaBuffer the alphaBuffer to set
	 */
	public void setAlphaBuffer(IBuffer alphaBuffer) {
		this.alphaBuffer = alphaBuffer;
	}
}