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

import com.dielmo.lidar.fieldsDescription.fieldsDescription.ColumnDescription;
import com.dielmo.lidar.fieldsDescription.fieldsDescription.ContainerColumnDescription;


/**
 * LAS point that implement the LAS point data version LAS1.0
 * in format 1
 * 
 * @author Oscar Garcia
 */
public class LASPoint11F1 extends LASPoint11F0{	
	
	/**
	 * The GPS time is the double floating point time tag value at which
	 * the point was acquired.
	 */
	protected double timeGPS;
	
	/**
	 * Default constructor, without arguments.
	 * Initializes all components to zero.
	 */ 
	public LASPoint11F1() {
		super();
		timeGPS = 0;
		sizeFormat = 28;
		lasIndex=-1;
	}
	
	/**
	 * Get GPS time as double floating point time tag value at which
	 * the point was acquired.
	 */
	public double getTimeGPS() {
		return timeGPS;
	}
	
	/**
	 * Set GPS time to double floating point time tag value at which
	 * the point was acquired.
	 */
	public void setTimeGPS(double tGPS) {
		timeGPS = tGPS;
	}
	
	/**
	 * Read a point of LAS file
	 * 
	 * @param input input buffer to read
	 * @param Offset Offset to data
	 * @param index index of points to read
	 * @return true if success else return false 
	 */
	public void readPoint(BigByteBuffer2 input, LidarHeader hdr, long index) {
		
		try{
			if(index>hdr.getNumPointsRecord() || index < 0) {
				throw new UnexpectedPointException("Out of index"); 
			}
			
			if(index==lasIndex)
				return;
			else
				lasIndex=index;
			
			byte[] punto = new byte[getSizeFormat()];
			
			input.position(hdr.getOffsetData()+getSizeFormat()*index);
		    input.get(punto);
		    
		    setX(ByteUtilities.arr2Int(punto, 0));
		    setY(ByteUtilities.arr2Int(punto, 4));
		    setZ(ByteUtilities.arr2Int(punto, 8));
		    setIntensity(ByteUtilities.arr2Unsignedshort(punto, 12));
			
		    setReturnNumber((byte)(punto[14] & 0x07)); // 3 primeros bits del byte 14
		    setNumberOfReturn((byte)((punto[14] & 0x38) >> 3));  // 3 siguintes bits
		    setScanDirectionFlag((byte)((punto[14] & 0x40) >> 6)); // 1 bit
		    setEdgeOfFlightLine((byte)((punto[14] & 0x80) >> 7)); // 1 bit
		    
			setClassification((byte) (punto[15] & 0X1F)); // 5 bits del byte 15
			setSynthetic((byte)((punto[15] & 0X20) >> 5 )); // 1 bit
			setKeyPoint((byte)((punto[15] & 0X40) >> 6 )); // 1 bit
			setWithheld((byte)((punto[15] & 0X80) >> 7 )); // 1 bit
			
			setScanAngleRank(punto[16]);
			setUserData((punto[17]));
			setPointSourceID(ByteUtilities.arr2Unsignedshort(punto, 18));
			setTimeGPS(ByteUtilities.arr2Double(punto, 20));
			
		} catch (UnexpectedPointException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * get by field the Value:
	 * 
	 * 0 return X
	 * 1 return Y
	 * 2 return Z
	 * 3 return intensity
	 * 4 return returnNumber
	 * 5 return numberOfReturn
	 * 6 return scanDirectionFlag
	 * 7 return edgeOfFlightLine
	 * 8 return classification
	 * 9 return synthetic
	 * 10 return keyPoint
	 * 11 return withheld
	 * 12 return scanAngleRank
	 * 13 return userData
	 * 14 return pointSourceID
	 * 15 return Time GPS
	 * 
	 * @param bb byte buffer of data 
 	 * @param indexField index of field
	 * @param hdr LiDAR header
	 * @param index asked point index. (row)
	 * @return Object of row and column indicated
	 */
	public Object getFieldValueByIndex(BigByteBuffer2 bb, int indexField,
			LidarHeader hdr, long index) {
	
		if (indexField == 15) {
			readPoint(bb, hdr, index);
			return getTimeGPS();
		}
		return super.getFieldValueByIndex(bb, indexField, hdr, index);
		
	}
	
	public Object getFieldValueByName(BigByteBuffer2 bb, String nameField, LidarHeader hdr,
			long index) {
		
		if (nameField.equalsIgnoreCase("GPS_Time")) {
			readPoint(bb, hdr, index);
			return getTimeGPS();
		}
		return super.getFieldValueByName(bb, nameField, hdr, index);
	}
	
	public ContainerColumnDescription getColumnsDescription(ContainerColumnDescription fields) {

		super.getColumnsDescription(fields);
		fields.add("GPS_Time", ColumnDescription.DOUBLE, 20, 3, 0.0);
		
		return fields;
	}
	
	public void WritePoint(ByteBuffer bb) {

		byte auxByte;
		byte[] punto = new byte[getSizeFormat()];

		// X bytes 0-4
		ByteUtilities.int2Arr(getX(), punto, 0);
		
		// Y bytes 4-8
		ByteUtilities.int2Arr(getY(), punto, 4);
		
		// bytes 8-12
		ByteUtilities.int2Arr(getZ(), punto, 8);
		
		// bytes 12-14
		ByteUtilities.unsignedShort2Arr(getIntensity(), punto, 12);
		
		// byte 14
		auxByte = getReturnNumber();
		auxByte |= (byte)((getNumberOfReturn()) << 3);
		auxByte |= (byte)((getScanDirectionFlag()) << 6);
		auxByte |= (byte)((getEdgeOfFlightLine()) << 7);
		punto[14] = auxByte;
		
		// byte 15
		auxByte = (byte)(getClassification());
		auxByte |= (byte)((getSynthetic()) << 5);
		auxByte |= (byte)((getKeyPoint()) << 6);
		auxByte |= (byte)((getWithheld()) << 7);
		punto[15] = auxByte;
		
		// byte 16
		punto[16] = (byte)((getScanAngleRank() & 0xFF));
		
		// byte 17
		punto[17] = (byte)((getUserData() & 0xFF));
		
		// bytes 18-20
		ByteUtilities.unsignedShort2Arr(getPointSourceID(), punto, 18);
		
		// bytes 20-28
		ByteUtilities.double2Arr(getTimeGPS(), punto, 18);
		
		bb.put(punto);
	}
	
	/*
	 * Set Point from a row
	 * @see com.dielmo.gvsig.lidar.LidarPoint#setPoint(com.hardcode.gdbms.engine.values.Value[], com.dielmo.gvsig.lidar.LidarHeader)
	 */
	public void setPoint(Object[] row, LidarHeader hdr) {
		
		super.setPoint(row, hdr);
		setTimeGPS(((Double)(row[15])));
	}
}
