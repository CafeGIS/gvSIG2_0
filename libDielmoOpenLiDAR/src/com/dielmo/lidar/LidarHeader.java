/* DielmoOpenLiDAR
 *
 * Copyright (C) 2008 DIELMO 3D S.L. (DIELMO) and Infrastructures
 * and Transports Department of the Valencian Government (CIT)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 * For more information, contact:
 *
 * DIELMO 3D S.L.
 * Plaza Vicente Andrés Estellés 1 Bajo E
 * 46950 Xirivella, Valencia
 * SPAIN
 *
 * +34 963137212
 * dielmo@dielmo.com
 * www.dielmo.com
 *
 * or
 *
 * Generalitat Valenciana
 * Conselleria d'Infraestructures i Transport
 * Av. Blasco Ibáñez, 50
 * 46010 VALENCIA
 * SPAIN
 *
 * +34 963862235
 * gvsig@gva.es
 * www.gvsig.gva.es
 */

/*
 * AUTHORS (In addition to DIELMO and CIT):
 *
 */

package com.dielmo.lidar;

import java.nio.ByteBuffer;


/**
 * Interface Lidar point
 *
 * @author Oscar Garcia
 */
public interface LidarHeader{

	// max value for an unsigned int variable.
	final int UNSIGNED_INT_MAX = (int)(Math.pow(2, 32)-1);
	// max value for an unsigned short variable.
	final int UNSIGNED_SHORT_MAX = (int)(Math.pow(2, 16)-1);

	public static final int LAS10F0 = 0;
	public static final int LAS10F1 = 1;
	public static final int LAS11F0 = 2;
	public static final int LAS11F1 = 3;
	public static final int LAS12F0 = 4;
	public static final int LAS12F1 = 5;
	public static final int LAS12F2 = 6;
	public static final int LAS12F3 = 7;
	public static final int BIN20010712 = 8;
	public static final int BIN20020715 = 9;
	public static final int UNEXPECTED= 9999;

	/**
	 * Read the header file
	 *
	 * @return true if success else return false
	 */
	public boolean readLidarHeader();

	/**
	 * Write the header file
	 *
	 * @return true if success else return false
	 */
	public boolean writeLidarHeader(ByteBuffer bb);

	// GET METHODS
	/**
	 * Return Size, in bytes, of the header file
	 *
	 *  @return header size
	 */
	public int getHdrSize();

	/**
	 * Return the total number of points records within the file
	 *
	 * @return number of points of file
	 */
	public long getNumPointsRecord();

	/**
	 * Return Number of bytes from the beginning of the file
	 * to the first point record data.
	 *
	 * @return offset to data
	 */
	public long getOffsetData();

	/**
	 * Return the x scale factor for the scale corresponding X values
	 * within the point record.
	 *
	 * @return X scale
	 */
	public double getXScale();

	/**
	 * Return the y scale factor for the scale corresponding Y values
	 * within the point record.
	 *
	 * @return Y scale
	 */
	public double getYScale();

	/**
	 * Return the z scale factor for the scale corresponding Z values
	 * within the point record.
	 *
	 * @return Z scale
	 */
	public double getZScale();

	/**
	 * Return the x offset used to set the overall offset for the point record
	 * @return X offset
	 */
	public double getXOffset();

	/**
	 * Return the y offset used to set the overall offset for the point record
	 * @return Y offset
	 */
	public double getYOffset();

	/**
	 * Return the z offset used to set the overall offset for the point record
	 * @return Z offset
	 */
	public double getZOffset();

	/**
	 * Return maximum x of coordinate extent of the file.
	 *
	 * @return X max
	 */
	public double getMaxX();

	/**
	 * Return maximum y of coordinate extent of the file.
	 *
	 * @return Y max
	 */
	public double getMaxY();

	/**
	 * Return maximum z of coordinate extent of the file.
	 *
	 * @return Z max
	 */
	public double getMaxZ();

	/**
	 * Return minimum x of coordinate extent of the file.
	 *
	 * @return X min
	 */
	public double getMinX();

	/**
	 * Return minimum y of coordinate extent of the file.
	 *
	 * @return Y min
	 */
	public double getMinY();

	/**
	 * Return minimum z of coordinate extent of the file.
	 *
	 * @return Z min
	 */
	public double getMinZ();


	// SET METHODS

	/**
	 * Set size of the header file
	 */
	public void setHdrSize(int newValue);

	/**
	 * Set the total number of points records within the file
	 */
	public void setNumPointsRecord(long newValue);

	/**
	 * Set number of bytes from the beginning of the file
	 * to the first point record data.
	 */
	public void setOffsetData(long newValue);

	/**
	 * Set the x scale factor for the scale corresponding X values
	 * within the point record.
	 */
	public void setXScale(double newValue);

	/**
	 * Set the y scale factor for the scale corresponding Y values
	 * within the point record.
	 */
	public void setYScale(double newValue);

	/**
	 * Set the z scale factor for the scale corresponding Z values
	 * within the point record.
	 */
	public void setZScale(double newValue);

	/**
	 * Set the x offset used to set the overall offset for the point record
	 */
	public void setXOffset(double newValue);

	/**
	 * Set the y offset used to set the overall offset for the point record
	 */
	public void setYOffset(double newValue);

	/**
	 * Set the z offset used to set the overall offset for the point record
	 */
	public void setZOffset(double newValue);

	/**
	 * Set maximum x of coordinate extent of the file.
	 */
	public void setMaxX(double newValue);

	/**
	 * Set maximum y of coordinate extent of the file.
	 */
	public void setMaxY(double newValue);

	/**
	 * Set maximum z of coordinate extent of the file.
	 */
	public void setMaxZ(double newValue);

	/**
	 * Set minimum x of coordinate extent of the file.
	 */
	public void setMinX(double newValue);

	/**
	 * Set minimum y of coordinate extent of the file.
	 */
	public void setMinY(double newValue);

	/**
	 * Set minimum z of coordinate extent of the file.
	 */
	public void setMinZ(double newValue);

	/**
	 * Return the version type of Lidar Header
	 */
	public int getVersion();

}
