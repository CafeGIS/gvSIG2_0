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

import com.dielmo.lidar.fieldsDescription.fieldsDescription.ColumnDescription;
import com.dielmo.lidar.fieldsDescription.fieldsDescription.ContainerColumnDescription;

/**
 * BIN point that implement the BIN point data version BIN 20010712
 *
 * @author Oscar Garcia
 */
public class BINPoint2001 implements LidarPoint{

	/**
	 * size of point format
	 */
	private int sizeFormat;

	/**
	 * indicates if content color.
	 */
	private boolean isColor;

	/**
	 * indicates if content time GPS.
	 */
	private boolean isTimeGPS;

	/**
	 * Color RGB
	 */
	private char color[] = new char[4];

	/**
	 * indicates if content color.
	 */
	private int time;


	/**
	 * X value
	 */
	private int x;

	/**
	 * Y value
	 */
	private int y;

	/**
	 * Z value
	 */
	private int z;

	/**
	 * The intensity value is the integer representation of the
	 * pulse return magnitude.
	 */
	private int intensity;

	/**
	 * The echo information of BIN is the pulse return number for a given output
	 * pulse.
	 *
	 * 0 Only echo
	 * 1 First of many echo
	 * 2 Intermediate echo
	 * 3 Last if many echo
	 */
	private byte echoInformation;

	/**
	 * The flight line number.
	 */
	private char flightLine;

	/**
	 * The classification field is a number to signify a given
	 * classification during filter processing.
	 */
	private char classification;
	
	protected long lasIndex;


	/**
	 * Default constructor, without arguments.
	 * Initializes all components to zero.
	 */
	public BINPoint2001(boolean c, boolean t) {

		x = 0;
		y = 0;
		z = 0;
		intensity = 0;
		echoInformation = 0;
		flightLine = 0;
		classification = 0;
		isColor = c;
		isTimeGPS = t;
		color[0] = 0;
		color[1] = 0;
		color[2] = 0;
		color[3] = 0;
		time = 0;
		sizeFormat = 16;
		lasIndex=-1;

		if(c) {
			sizeFormat += 4;
		}
		if(t) {
			sizeFormat += 4;
		}
	}

	// GET METHODS
	/**
	 * Get GPS time as long.
	 */
	public int getTime() {
		return time;
	}

	/**
	 * Get Color RGB
	 */
	public char[] getcolor() {
		return color;
	}

	/**
	 * Get red value of color
	 */
	public char getR() {
		return color[0];
	}

	/**
	 * Get green value of color
	 */
	public char getG() {
		return color[1];
	}

	/**
	 * Get blue value of color
	 */
	public char getB() {
		return color[2];
	}

	/**
	 * Get infrared value of color
	 */
	public char getI() {
		return color[3];
	}

	/**
	 * Return X value that is stored as long integer. The corresponding
	 * X scale from the public header block change this long integer to
	 * true floating point value. The corresponding offset value can
	 * also be used for projections with very large numbers.
	 *
	 * the coordinate = (X-OrgX)/Units
	 *
	 * @return x value
	 */
	public int getX() {
		return x;
	}

	/**
	 * Return Y value that is stored as long integer. The corresponding
	 * Y scale from the public header block change this long integer to
	 * true floating point value. The corresponding offset value can
	 * also be used for projections with very large numbers.
	 *
	 * the coordinate = (Y-OrgY)/Units
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
	 * the coordinate = (Z-OrgZ)/Units
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
	 * Get The echo information of BIN is the pulse return number for
	 * a given output pulse.
	 *
	 * 0 Only echo
	 * 1 First of many echo
	 * 2 Intermediate echo
	 * 3 Last if many echo
	 *
	 * @return echo information
	 */
	public byte getEchoInformation() {
		return echoInformation;
	}

	/**
	 * Get flight line number
	 *
	 * @return flight line number
	 */
	public char getFlightLine() {
		return flightLine;
	}

	/**
	 * Get a given classification during filter processing.
	 *
	 * @return classification
	 */
	public char getClassification() {
		return classification;
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
	 * Set GPS time as integer.
	 */
	public void setTime(int t) {
		time=t;
	}

	/**
	 * Set Color RGB
	 *
	 * @param c this array contains 4 chars for represents the RGBI
	 */
	public void setcolor(char[] c) {
		if(c.length == 4) {
			color = c;
		}
	}

	/**
	 * set X value that is stored as long integer.
	 *
	 * @param newx new value of x
	 */
	public void setX(int newx) {
		x = newx;
	}

	/**
	 * set Y value that is stored as long integer.
	 *
	 * @param newy new value of y
	 */
	public void setY(int newy) {
		y = newy;
	}

	/**
	 * set Z value that is stored as long integer.
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
			// intensity is formed by 14 bits, 16383 represents (2^14)-1
			if(inten >=0 && inten <= 16383) {
				intensity = inten;
			} else {
				throw new OutOfRangeLidarException("Out of range of intensity");
			}

		} catch(OutOfRangeLidarException e) {

			e.printStackTrace();
		}
	}

	/**
	 * Set the echo information of BIN is the pulse return number for
	 * a given output pulse.
	 *
	 * 0 Only echo
	 * 1 First of many echo
	 * 2 Intermediate echo
	 * 3 Last if many echo
	 *
	 * @param echo new echo information
	 */
	public void setEchoInformation(byte echo) {

		try{
			if(echo >=0 && echo <= 3) {
				echoInformation = echo;
			} else {
				throw new OutOfRangeLidarException("Out of range of echo information");
			}

		} catch(OutOfRangeLidarException e) {

			e.printStackTrace();
		}
	}

	/**
	 * Get flight line number
	 *
	 * @param fl new flight line number
	 */
	public void setFlightLine(char fl) {

		try{
			if(fl>=0 && fl<=255) {
				flightLine = fl;
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
	public void setClassification(char c) {

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
	 * Read a point of BIN file
	 *
	 * @param input input file to read
	 * @param Offset Offset to data
	 * @param index index of points to read
	 * @return true if success else return false
	 */
	public void readPoint(BigByteBuffer2 input, LidarHeader hdr, long index) {

		try{
			if(index>hdr.getNumPointsRecord() || index < 0) {
				throw new UnexpectedPointException("Out of Index");
			}
			
			if(index==lasIndex)
				return;
			else
				lasIndex=index;

			int auxIndex;
			byte[] punto = new byte[getSizeFormat()];
			byte[] aux = new byte[2];

			input.position(hdr.getOffsetData()+getSizeFormat()*index);
		    input.get(punto);

		    setClassification((char)(punto[0] & 0xFF));
		    setFlightLine((char)(punto[1] & 0xFF));

		    aux[0] = punto[2];
		    aux[1] = (byte)(punto[3] & 0X3F);
			setIntensity(ByteUtilities.arr2Unsignedshort(aux, 0));  // bits de 0-13
			setEchoInformation((byte)((punto[3] & 0xC0) >> 2)); // bits 14 y 15

			setX(ByteUtilities.arr2Int(punto, 4));
			setY(ByteUtilities.arr2Int(punto, 8));
			setZ(ByteUtilities.arr2Int(punto, 12));

			auxIndex = 16;

			// si hay gps leelo
			if(isTimeGPS) {
				setTime(ByteUtilities.arr2Int(punto, auxIndex));
				auxIndex+=4;
			}

			// si hay color leelo
			if(isColor) {

				char[] c = new char[4];
				c[0] = (char)(punto[auxIndex] & 0xFF);
				c[1] = (char)(punto[auxIndex+1] & 0xFF);
				c[2] = (char)(punto[auxIndex+2] & 0xFF);
				c[3] = (char)(punto[auxIndex+3] & 0xFF);
				setcolor(c);
			}

		} catch (UnexpectedPointException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Read x and y in point of BIN file
	 *
	 * @param input input buffer to read
	 * @param Offset Offset to data
	 * @param index index of points to read
	 * @return true if success else return false
	 */
	public Point2D.Double readPoint2D(BigByteBuffer2 input, LidarHeader hdr,  long index) {

		byte[] punto = new byte[12];

		input.position((hdr.getOffsetData()+getSizeFormat()*index)+4);
		input.get(punto);

		setX(ByteUtilities.arr2Int(punto, 4));
		setY(ByteUtilities.arr2Int(punto, 8));

		return new Point2D.Double((getX()-hdr.getXOffset())/hdr.getXScale(), (getY()-hdr.getYOffset())/hdr.getYScale());
	}

	/**
	 * Read a x, y and z in point of LAS file
	 *
	 * @param input input buffer to read
	 * @param Offset Offset to data
	 * @param index index of points to read
	 * @return true if success else return false
	 */
	public void readPoint3D(BigByteBuffer2 input, LidarHeader hdr, long index) {

		byte[] punto = new byte[16];

	    input.position((hdr.getOffsetData()+getSizeFormat()*index)+4);
		input.get(punto);

		setX(ByteUtilities.arr2Int(punto, 4));
		setY(ByteUtilities.arr2Int(punto, 8));
		setZ(ByteUtilities.arr2Int(punto, 12));
	}

	/**
	 * get field value by index:
	 *
	 * 0 return X
	 * 1 return Y
	 * 2 return Z
	 * 3 return intensity
	 * 4 return classification
	 * 5 return Line
	 * 6 return echo information
	 * 7-11 Time and Color RGBI
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

		switch(indexField) {

			case 0:
				return (getX()-hdr.getXOffset())/hdr.getXScale();

			case 1:
				return (getY()-hdr.getYOffset())/hdr.getYScale();

			case 2:
				return (getZ()-hdr.getZOffset())/hdr.getZScale();

			case 3:
				return getIntensity();

			case 4:
				return getClassification();

			case 5:
				return getFlightLine();

			case 6:
				return getEchoInformation();

			case 7:

				if(isTimeGPS) {
					return getTime();
				} else if(isColor) {
					return getR();
				}

			case 8:

				if(isTimeGPS) {
					return getR();
				} else if(isColor) {
					return getG();
				}

			case 9:

				if(isTimeGPS) {
					return getG();
				} else if(isColor) {
					return getB();
				}

			case 10:

				if(isTimeGPS) {
					return getB();
				} else if(isColor) {
					return getI();
				}

			case 11:
				if(isTimeGPS) {
					return getI();
				}
		}

		return null;
	}

	public Object getFieldValueByName(BigByteBuffer2 bb, String nameField, LidarHeader hdr,
			long index) {

		readPoint(bb, hdr, index);

		if(nameField.equalsIgnoreCase("X")) {
			return (getX()-hdr.getXOffset())/hdr.getXScale();
		} else if(nameField.equalsIgnoreCase("Y")) {
			return (getY()-hdr.getYOffset())/hdr.getYScale();
		} else if(nameField.equalsIgnoreCase("Z")) {
			return (getZ()-hdr.getZOffset())/hdr.getZScale();
		} else if(nameField.equalsIgnoreCase("Intensity")) {
			return getIntensity();
		} else if(nameField.equalsIgnoreCase("Classification")) {
			return getClassification();
		} else if(nameField.equalsIgnoreCase("Line")) {
			return getFlightLine();
		} else if(nameField.equalsIgnoreCase("Echo")) {
			return getEchoInformation();
		} else if(nameField.equalsIgnoreCase("Time")) {
			return getTime();
		} else if(nameField.equalsIgnoreCase("R")) {
			return getR();
		} else if(nameField.equalsIgnoreCase("G")) {
			return getG();
		} else if(nameField.equalsIgnoreCase("B")) {
			return getB();
		} else if(nameField.equalsIgnoreCase("I")) {
			return getI();
		}

		return null;
	}

	public ContainerColumnDescription getColumnsDescription(ContainerColumnDescription fields) {

		int index;
		int fieldsAddByColor, fieldsAddByTime;

		if(isColor) {
			fieldsAddByColor = 4;
		} else {
			fieldsAddByColor = 0;
		}

		if(isTimeGPS) {
			fieldsAddByTime = 1;
		} else {
			fieldsAddByTime = 0;
		}

		fields.add("X", ColumnDescription.DOUBLE, 20, 3, 0.0);
		fields.add("Y", ColumnDescription.DOUBLE, 20, 3, 0.0);
		fields.add("Z", ColumnDescription.DOUBLE, 20, 3, 0.0);
		fields.add("Intensity", ColumnDescription.INT, 5, 0, 0);
		fields.add("Classification", ColumnDescription.BYTE, 1, 0, 0);
		fields.add("Line", ColumnDescription.BYTE, 1, 0, 0);
		fields.add("Echo", ColumnDescription.BYTE, 1, 0, 0);

		index = 7;

		if(isTimeGPS) {

			fields.add("Time", ColumnDescription.DOUBLE, 20, 5, 0.0);
			index++;
		}

		if(isColor) {

			fields.add("R", ColumnDescription.INT, 3, 0, 0);
			fields.add("G", ColumnDescription.INT, 3, 0, 0);
			fields.add("B", ColumnDescription.INT, 3, 0, 0);
			fields.add("I", ColumnDescription.INT, 3, 0, 0);
			index+=4;
		}

		return fields;
	}

	public void WritePoint(ByteBuffer bb) {

		byte[] punto = new byte[getSizeFormat()];

		// byte 1
		punto[0] = (byte) (getClassification() & 0xFF);

		// byte 2
		punto[1] = (byte) (getFlightLine() & 0xFF);

		// byte 3-4
		ByteUtilities.unsignedShort2Arr(getIntensity(), punto, 2);

		// bits 7 y 8 del byte 4
		punto[3] >>= 2;
		punto[3]+=getEchoInformation();

		// bytes 4-8
		ByteUtilities.int2Arr(getX(), punto, 4);


		// bytes 8-12
		ByteUtilities.int2Arr(getY(), punto, 8);

		// bytes 12-16
		ByteUtilities.int2Arr(getZ(), punto, 12);

		// si hay gps leelo
		if(isTimeGPS) {

			// bytes 16-20
			ByteUtilities.int2Arr(time, punto, 16);

			if(isColor) {
				// bytes 20-24
				punto[20]= (byte) (color[0] & 0xFF);
				punto[21] = (byte) (color[1] & 0xFF);
				punto[22] = (byte) (color[2] & 0xFF);
				punto[23] = (byte) (color[3] & 0xFF);
			}
		} else {

			// si hay color leelo
			if(isColor) {

				// bytes 16-20
				punto[16]= (byte) (color[0] & 0xFF);
				punto[17] = (byte) (color[1] & 0xFF);
				punto[18] = (byte) (color[2] & 0xFF);
				punto[19] = (byte) (color[3] & 0xFF);
			}
		}

		bb.put(punto);

		return;
	}

	/*
	 * Set Point from a row
	 * @see com.dielmo.gvsig.lidar.LidarPoint#setPoint(com.hardcode.gdbms.engine.values.Value[], com.dielmo.gvsig.lidar.LidarHeader)
	 */
	public void setPoint(Object[] row, LidarHeader hdr) {

		double auxX = (Double) row[0];
		double auxY = (Double) row[1];
		double auxZ = (Double) row[2];

		setX((int) (auxX * (hdr.getXScale()) + hdr.getXOffset()));
		setY((int) (auxY * (hdr.getYScale()) + hdr.getYOffset()));
		setZ((int) (auxZ * (hdr.getZScale()) + hdr.getZOffset()));

		setIntensity((Integer) row[3]);
		setClassification((char) (((Integer) (row[4])).byteValue() & 0xFF));
		setFlightLine((char) (((Integer) (row[5])).byteValue() & 0xFF));
		setEchoInformation(((Integer) (row[6])).byteValue());

		if(hdr instanceof BINHeader) {

			BINHeader hdrBin = (BINHeader) hdr;
			if(hdrBin.getTime()>0) {

				setTime((Integer) row[7]);

				if(hdrBin.getColor()>0) {

					color[0] = (char) (((Integer) (row[8])).byteValue() & 0xFF);
					color[1] = (char) (((Integer) (row[9])).byteValue() & 0xFF);
					color[2] = (char) (((Integer) (row[10])).byteValue() & 0xFF);
					color[3] = (char) (((Integer) (row[11])).byteValue() & 0xFF);
				}
			} else {

				if(hdrBin.getColor()>0) {

					color[0] = (char) (((Integer) (row[7])).byteValue() & 0xFF);
					color[1] = (char) (((Integer) (row[8])).byteValue() & 0xFF);
					color[2] = (char) (((Integer) (row[9])).byteValue() & 0xFF);
					color[3] = (char) (((Integer) (row[10])).byteValue() & 0xFF);
				}
			}
		}
	}

}