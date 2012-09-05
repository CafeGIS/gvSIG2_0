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
 * Plaza Vicente Andr�s Estell�s 1 Bajo E
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
 * Av. Blasco Ib��ez, 50
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

import com.dielmo.lidar.fieldsDescription.fieldsDescription.ColumnDescription;
import com.dielmo.lidar.fieldsDescription.fieldsDescription.ContainerColumnDescription;

/**
 * LAS point that implement the LAS point data version LAS1.0
 * in format 0
 *
 * @author Oscar Garcia
 */
public class LASPoint10F0 implements LidarPoint{

	protected int sizeFormat;
	/**
	 * X value
	 */
	protected int x;

	/**
	 * Y value
	 */
	protected int y;

	/**
	 * Z value
	 */
	protected int z;

	/**
	 * The intensity value is the integer representation of the
	 * pulse return magnitude.
	 */
	protected int intensity;

	/**
	 * The return number is the pulse return number for a given output
	 * pulse.
	 */
	protected byte returnNumber;

	/**
	 * Total number of returns for a given pulse.
	 */
	protected byte numberOfReturn;

	/**
	 * direction at which the scanner mirror was traveling at the time
	 * of the output pulse.
	 */
	protected byte scanDirectionFlag;

	/**
	 * The edge of flight line data bit has a value of 1 only when
	 * the point is at the end of a scan. It is the last point on a
	 * given scan line before it changes direction.
	 */
	protected byte edgeOfFlightLine;

	/**
	 * The classification field is a number to signify a given
	 * classification during filter processing.
	 */
	protected byte classification;

	/**
	 * Angle at which the laser point was output from the laser system
	 * including the roll of the aircraft. The scan angle is within 1
	 * degree of accuracy from +90 to -90 degrees. The scan angle is an
	 * angle based on 0 degrees being NADIR, end -90 degrees to the left
	 * side of the aircraft in the direction of flight.
	 */
	protected byte scanAngleRank;

	/**
	 * The file marker is an optional field that should be used in
	 * conjunction with the variable length records.
	 * The file marker allows for the LAS flight-line based files
	 * to be combined into single files with more than one flight-line.
	 */
	protected byte fileMarker;

	/**
	 * A bit field that is to be used at the users discretion.
	 */
	protected int UserBitField;
	
	protected long lasIndex;

	/**
	 * Default constructor, without arguments.
	 * Initializes all components to zero.
	 */
	public LASPoint10F0() {

		x = 0;
		y = 0;
		z = 0;
		intensity = 0;
		returnNumber = 0;
		numberOfReturn = 0;
		scanDirectionFlag = 0;
		edgeOfFlightLine = 0;
		classification = 0;
		scanAngleRank = 0;
		fileMarker = 0;
		UserBitField = 0;
		sizeFormat = 20;
		lasIndex=-1;
	}

	// GET METHODS
	/**
	 * Return X value that is stored as integer. The corresponding
	 * X scale from the public header block change this long integer to
	 * true floating point value. The corresponding offset value can
	 * also be used for projections with very large numbers.
	 *
	 * the coordinate = X*Xscale+Xoffset
	 *
	 * @return x value
	 */
	public int getX() {
		return x;
	}

	/**
	 * Return Y value that is stored as integer. The corresponding
	 * Y scale from the public header block change this long integer to
	 * true floating point value. The corresponding offset value can
	 * also be used for projections with very large numbers.
	 *
	 * * the coordinate = Y*Yscale+Yoffset
	 *
	 * @return y value
	 */
	public int getY() {
		return y;
	}

	/**
	 * Return Z value that is stored as long integer. The corresponding
	 * Z scale from the public header block change this long integer to
	 * true floating point value. The corresponding offset value can
	 * also be used for projections with very large numbers.
	 *
	 * * the coordinate = Z*Zscale+Zoffset
	 *
	 * @return z value
	 */
	public int getZ() {
		return z;
	}

	/**
	 * Get the intensity value as the integer representation of the pulse
	 * return magnitude. This value is optional and system specific
	 *
	 * @return intensity value
	 */
	public int getIntensity() {
		return intensity;
	}

	/**
	 * Get the return number as the pulse return number for a given output
	 * pulse.
	 *
	 * @return pulse return number
	 */
	public byte getReturnNumber() {
		return returnNumber;
	}

	/**
	 * Get total number of returns for a given pulse.
	 *
	 * @return number of return
	 */
	public byte getNumberOfReturn() {
		return numberOfReturn;
	}

	/**
	 * Get direction at which the scanner mirror was traveling at the time
	 * of the output pulse.
	 *
	 * @return direction
	 */
	public byte getScanDirectionFlag() {
		return scanDirectionFlag;
	}

	/**
	 * Get the edge of flight line data bit.
	 *
	 * @return edge of flight
	 */
	public byte getEdgeOfFlightLine() {
		return edgeOfFlightLine;
	}

	/**
	 * Get a given classification during filter processing.
	 *
	 * @return classification
	 */
	public byte getClassification() {
		return classification;
	}

	/**
	 * Get angle at which the laser point was output from the laser system
	 * including the roll of the aircraft. The scan angle is within 1
	 * degree of accuracy from +90 to 90 degrees. The scan angle is an
	 * angle based on 0 degrees being NADIR, end -90 degrees to the left
	 * side of the aircraft in the direction of flight.
	 *
	 * @return scan angle rank
	 */
	public byte getScanAngleRank() {
		return scanAngleRank;
	}

	/**
	 * Get file marker.
	 * The file marker allows for the LAS flight-line based files
	 * to be combined into single files with more than one flight-line.
	 *
	 * @return file marker
	 */
	public byte getFileMarker() {
		return fileMarker;
	}

	/**
	 * Get a bit field that is to be used at the users discretion.
	 *
	 * @return user bit field
	 */
	public int getUserBitField(){
		return UserBitField;
	}

	/**
	 * Get a bit size of point format
	 *
	 * @return sizeFormat
	 */
	public int getSizeFormat() {
		return sizeFormat;
	}

	// SET METHODS

	/**
	 * set X value that is stored as integer.
	 *
	 * @param newx new value of x
	 */
	public void setX(int newx) {

		x = newx;
	}

	/**
	 * set Y value that is stored as integer.
	 *
	 * @param newy new value of y
	 */
	public void setY(int newy) {
		y = newy;
	}

	/**
	 * set Z value that is stored as integer.
	 *
	 * @param newz new value of z
	 */
	public void setZ(int newz) {
		z = newz;
	}

	/**
	 * Set the intensity value as the integer representation of the pulse
	 * return magnitude. This value is optional and system specific
	 *
	 * @param inten new intensity
	 */
	public void setIntensity(int inten) {

		try{

			if(inten >=0 && inten <= UNSIGNED_SHORT_MAX) {
				intensity = inten;
			} else {
				throw new OutOfRangeLidarException("Out of range of intensity");
			}

		} catch(OutOfRangeLidarException e) {

			e.printStackTrace();
		}

	}

	/**
	 * Set the return number.
	 *
	 * @param rn new pulse return number
	 */
	public void setReturnNumber(byte rn) {

		try{
			if(rn>=0 && rn <= 7) {
				returnNumber = rn;
			} else {
				throw new OutOfRangeLidarException("Out of range of return number");
			}

		} catch(OutOfRangeLidarException e) {

			e.printStackTrace();
		}
	}

	/**
	 * Set total number of returns for a given pulse.
	 *
	 * @param nof new number of return
	 */
	public void setNumberOfReturn(byte nof) {

		try{
			if(nof>=0 && nof <= 7) {
				numberOfReturn = nof;
			} else {
				throw new OutOfRangeLidarException("Out of range of number of return");
			}

		} catch(OutOfRangeLidarException e) {

			e.printStackTrace();
		}
	}

	/**
	 * Set direction at which the scanner mirror was traveling at the time
	 * of the output pulse.
	 *
	 * @param sdf new direction
	 */
	public void setScanDirectionFlag(byte sdf) {

		try{
			if(sdf>=0 && sdf<=1) {
				scanDirectionFlag = sdf;
			} else {
				throw new OutOfRangeLidarException("Out of range of scan direction flag");
			}

		} catch(OutOfRangeLidarException e) {

			e.printStackTrace();
		}
	}

	/**
	 * Set the edge of flight line data bit.
	 *
	 * @param eofl new edge of flight
	 */
	public void setEdgeOfFlightLine(byte eofl) {

		try{
			if(eofl>=0 && eofl <= 1) {
				edgeOfFlightLine = eofl;
			} else {
				throw new OutOfRangeLidarException("Out of range of flight line");
			}

		} catch(OutOfRangeLidarException e) {

			e.printStackTrace();
		}
	}

	/**
	 * Set a given classification during filter processing.
	 *
	 * @return c new classification
	 */
	public void setClassification(byte c) {

		try{
			if(c>=0 && c<=255) {
				classification = c;
			} else {
				throw new OutOfRangeLidarException("Out of range of classification");
			}

		} catch(OutOfRangeLidarException e) {

			e.printStackTrace();
		}
	}

	/**
	 * Set angle at which the laser point was output from the laser system
	 * including the roll of the aircraft.
	 *
	 * @param sar new scan angle rank
	 */
	public void setScanAngleRank(byte sar) {

	//	try{
	//		if(sar>=-90 && sar<=90)
				scanAngleRank = sar;
	//		else
	//			throw new OutOfRangeLidarException("Out of range of scan angle rank");

	//	} catch(OutOfRangeLidarException e) {

	//		e.printStackTrace();
	//	}
	}

	/**
	 * Set file marker.
	 * The file marker allows for the LAS flight-line based files
	 * to be combined into single files with more than one flight-line.
	 *
	 * @param fm new file marker
	 */
	public void setFileMarker(byte fm) {

		try{
			if(fm>=0 && fm<=255) {
				fileMarker = fm;
			} else {
				throw new OutOfRangeLidarException("Out of range of file marker");
			}

		} catch(OutOfRangeLidarException e) {

			e.printStackTrace();
		}
	}

	/**
	 * Set a bit field that be used at the users discretion.
	 *
	 * @param ubf new user bit field
	 */
	public void setUserBitField(int ubf) {

		try{
			if(ubf>=0 && ubf <= UNSIGNED_SHORT_MAX) {
				UserBitField = ubf;
			} else {
				throw new OutOfRangeLidarException("Out of range of user bit field");
			}

		} catch(OutOfRangeLidarException e) {

			e.printStackTrace();
		}
	}

	/**
	 * Read a point of LAS file
	 *
	 * @param input input file to read
	 * @param Offset Offset to data
	 * @param index index of points to read
	 * @return true if success else return false
	 */
	public void readPoint(BigByteBuffer2 input, LidarHeader hdr, long index) {

		try {

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

		    setClassification((punto[15]));
		    setScanAngleRank((punto[16]));
		    setFileMarker((punto[17]));
		    setUserBitField(ByteUtilities.arr2Unsignedshort(punto, 18));

		} catch (UnexpectedPointException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Point2D.Double readPoint2D(BigByteBuffer2 input, LidarHeader hdr, long index){

		try{
			if(index>hdr.getNumPointsRecord() || index < 0) {
				throw new UnexpectedPointException("Out of index");
			}

			byte[] punto = new byte[8];

			input.position(hdr.getOffsetData()+getSizeFormat()*index);
			input.get(punto);

			setX(ByteUtilities.arr2Int(punto, 0));
		    setY(ByteUtilities.arr2Int(punto, 4));

		    return new Point2D.Double(getX()*hdr.getXScale()+hdr.getXOffset(), getY()*hdr.getYScale()+hdr.getYOffset());

		} catch (UnexpectedPointException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Read a x, y and z in point of LAS file
	 *
	 * @param input input buffer to read
	 * @param Offset Offset to data
	 * @param index index of points to read
	 * @return true if success else return false
	 */
	public void readPoint3D(BigByteBuffer2 input, LidarHeader hdr, long index) throws UnexpectedPointException {

		try{
			if(index>hdr.getNumPointsRecord() || index < 0) {
				throw new UnexpectedPointException("Out of index");
			}

			byte[] punto = new byte[12];

			input.position(hdr.getOffsetData()+getSizeFormat()*index);
			input.get(punto);

			setX(ByteUtilities.arr2Int(punto, 0));
		    setY(ByteUtilities.arr2Int(punto, 4));
		    setZ(ByteUtilities.arr2Int(punto, 8));

		} catch (UnexpectedPointException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * get field value by index:
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
	 * 9 return scanAngleRank
	 * 10 return fileMarker
	 * 11 return UserBitField
	 *
	 * @param bb byte buffer of data
 	 * @param indexField index of field
	 * @param hdr LiDAR header
	 * @param index asked point index. (row)
	 * @return Value of row and column indicated
	 */
	public Object getFieldValueByIndex(BigByteBuffer2 bb, int indexField,
			LidarHeader hdr, long index) {

		readPoint(bb, hdr, index);

		switch (indexField) {
			case 0:
			return getX() * hdr.getXScale() + hdr.getXOffset();
		case 1:
			return getY() * hdr.getYScale() + hdr.getYOffset();

		case 2:
			return getZ() * hdr.getZScale() + hdr.getZOffset();

		case 3:
			return getIntensity();

		case 4:
			return getReturnNumber();

		case 5:
			return getNumberOfReturn();

		case 6:
			return getScanDirectionFlag();

		case 7:
			return getEdgeOfFlightLine();

		case 8:
			return getClassification();

		case 9:
			return getScanAngleRank();

		case 10:

			return getFileMarker();

		case 11:
			return getUserBitField();

		}

		return null;
	}

	public Object getFieldValueByName(BigByteBuffer2 bb, String nameField,
			LidarHeader hdr, long index) {

		readPoint(bb, hdr, index);

		if(nameField.equalsIgnoreCase("X")) {
			return getX() * hdr.getXScale() + hdr.getXOffset();
		} else if(nameField.equalsIgnoreCase("Y")) {
			return getY() * hdr.getYScale() + hdr.getYOffset();
		} else if(nameField.equalsIgnoreCase("Z")) {
			return getZ() * hdr.getZScale() + hdr.getZOffset();
		} else if(nameField.equalsIgnoreCase("Intensity")) {
			return getIntensity();
		} else if(nameField.equalsIgnoreCase("Return_Number")) {
			return getReturnNumber();
		} else if(nameField.equalsIgnoreCase("Number_of_Returns")) {
			return getNumberOfReturn();
		} else if(nameField.equalsIgnoreCase("Scan_Direction_Flag")) {
			return getScanDirectionFlag();
		} else if(nameField.equalsIgnoreCase("Edge_of_Flight_Line")) {
			return getEdgeOfFlightLine();
		} else if(nameField.equalsIgnoreCase("Classification")) {
			return getClassification();
		} else if(nameField.equalsIgnoreCase("Scan_Angle_Rank")) {
			return getScanAngleRank();
		} else if(nameField.equalsIgnoreCase("File_Marker")) {
			return getFileMarker();
		} else if(nameField.equalsIgnoreCase("User_Bit_Field")) {
			return getUserBitField();
		}

		return null;
	}


	public ContainerColumnDescription getColumnsDescription(ContainerColumnDescription fields) {

		fields.add("X", ColumnDescription.DOUBLE, 20, 3, 0.0);
		fields.add("Y", ColumnDescription.DOUBLE, 20, 3, 0.0);
		fields.add("Z", ColumnDescription.DOUBLE, 20, 3, 0.0);
		fields.add("Intensity", ColumnDescription.INT, 5, 0, new Integer(0));
		fields.add("Return_Number", ColumnDescription.BYTE, 1, 0, new Byte((byte) 0) );
		fields.add("Number_of_Returns", ColumnDescription.BYTE, 1, 0, new Byte((byte) 0) );
		fields.add("Scan_Direction_Flag", ColumnDescription.BYTE, 1, 0, new Byte((byte) 0));
		fields.add("Edge_of_Flight_Line", ColumnDescription.BYTE, 1, 0, new Byte((byte) 0));
		fields.add("Classification", ColumnDescription.BYTE, 1, 0, new Byte((byte) 0));
		fields.add("Scan_Angle_Rank", ColumnDescription.BYTE, 3, 0, new Byte((byte) 0));
		fields.add("File_Marker", ColumnDescription.BYTE, 1, 0, new Byte((byte) 0));
		fields.add("User_Bit_Field", ColumnDescription.INT, 10, 0, new Integer(0));
		
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
		punto[15] = (byte)((getClassification() & 0xFF));

		// byte 16
		punto[16] = (byte)((getScanAngleRank() & 0xFF));

		// byte 17
		punto[17] = (byte)((getFileMarker() & 0xFF));

		// bytes 18-20
		ByteUtilities.unsignedShort2Arr(getUserBitField(), punto, 18);


		bb.put(punto);
	}

	/*
	 * Set Point from a row
	 */
	public void setPoint(Object[] row, LidarHeader hdr) {
		double auxX = ((Double) (row[0]));
		double auxY = ((Double) (row[1]));
		double auxZ = ((Double) (row[2]));

		setX((int) ((auxX - hdr.getXOffset()) / hdr.getXScale()));
		setY((int) ((auxY - hdr.getYOffset()) / hdr.getYScale()));
		setZ((int) ((auxZ - hdr.getZOffset()) / hdr.getZScale()));

		setIntensity((Integer) row[3]);
		setReturnNumber(((Integer) row[4]).byteValue());
		setNumberOfReturn(((Integer) (row[5])).byteValue());
		setScanDirectionFlag(((Integer) (row[6])).byteValue());
		setEdgeOfFlightLine(((Integer) (row[7])).byteValue());
		setClassification( (((Integer) (row[8])).byteValue()));
		setScanAngleRank(((Integer) (row[9])).byteValue());
		setFileMarker( (((Integer) (row[10])).byteValue()));
		setUserBitField(((Integer) (row[11])));
	}
}