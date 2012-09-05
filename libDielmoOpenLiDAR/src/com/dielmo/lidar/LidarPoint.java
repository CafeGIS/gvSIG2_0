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

import java.awt.geom.Point2D;
import java.nio.ByteBuffer;

import com.dielmo.lidar.fieldsDescription.fieldsDescription.ContainerColumnDescription;


/**
 * Interface LiDAR point
 *
 * @author Oscar Garcia
 */
public interface LidarPoint {

	// max value for an unsigned short variable.
	final int UNSIGNED_SHORT_MAX = (int)(Math.pow(2, 16)-1);

	/**
	 * Read x and y in point2D of LiDAR file
	 *
	 * @param bb byte buffer of data
	 * @param hdr LiDAR header
	 * @param index asked point index. (row)
	 * @return Point2D with coordinates of asked point.
	 */
	public Point2D.Double readPoint2D(BigByteBuffer2 bb, LidarHeader hdr, long index);

	/**
	 * Read point of LiDAR file
	 *
	 * @param bb
	 *            byte buffer of data
	 * @param hdr
	 *            LiDAR header
	 * @param index
	 *            asked point index. (row)
	 * @return Point2D with coordinates of asked point.
	 */
	public void readPoint(BigByteBuffer2 bb, LidarHeader hdr,
			long index);

	/**
	 * Get the value that is in the row and column name specified
	 *
	 * @param bb
	 *            byte buffer of data
	 * @param nameField
	 *            name of field
	 * @param hdr
	 *            LiDAR header
	 * @param index
	 *            asked point index. (row)
	 * @return Value of row and column indicated
	 */
	public Object getFieldValueByName(BigByteBuffer2 bb, String nameField,
			LidarHeader hdr, long index);

	/**
	 * Get the value that is in the row and column specified
	 *
	 * @param bb
	 *            byte buffer of data
	 * @param indexField
	 *            index of field
	 * @param hdr
	 *            LiDAR header
	 * @param index
	 *            asked point index. (row)
	 * @return Value of row and column indicated
	 */
	public Object getFieldValueByIndex(BigByteBuffer2 bb, int indexField, LidarHeader hdr, long index);

	/**
	 * Description of table. That contains the definition of all column that
	 * table contains.
	 *
	 * @return Field description
	 * @deprecated use {@link fillFeatureType(EditableFeatureType) }
	 */
	// public FieldDescription[] getFieldDescription();

	/**
	 * Get the table type of column specified
	 * @param i index of column
	 * @return type of column
	 */
	// public int getFieldType(int i);

	/**
	 * Set Point from a row
	 *
	 * @param row, row to set
	 * @param hdr, LidarHeader of origin file.
	 */
	public void setPoint(Object[] row, LidarHeader hdr);

	/**
	 * Write point in ByteBuffer
	 *
	 * @param bb buffer.
	 */
	public void WritePoint(ByteBuffer bb);

	/**
	 * Get size of point format
	 *
	 * @return sizeFormat
	 */
	public int getSizeFormat();

	/**
	 * Description of table. That contains the definition of all column that
	 * table contains.
	 *
	 * @return ContainerColumnDescription container with columns description
	 */
	public ContainerColumnDescription getColumnsDescription(ContainerColumnDescription fields);
}