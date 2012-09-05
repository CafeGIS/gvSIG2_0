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


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

import javax.swing.JOptionPane;

//The structure of the TerraScan file header is:
public class BINHeader implements LidarHeader{
	
	/**
	 * path of LAS file 
	 */
	private File m_Fich;
	
	/**
	 * Size, in bytes, of the header file 
	 */
	private int hdrSize ; // sizeof(ScanHdr)
	
	/**
	 * version number
	 */
	private int hdrVersion ; // Version 20020715, 20010712, 20010129 or 970404
	
	/**
	 * RecogVal value, always 970401
	 */
	private int RecogVal ; // Always 970401
	
	/**
	 * RecogStr value, always 970401
	 */
	private char[] RecogStr = new char[4]; // CXYZ
	
	/**
	 * total number of points records within the file
	 */
	private long PntCnt ; // Number of points stored
	
	/**
	 * unit of measure
	 */
	private int Units ; // Units per meter = subpermast * uorpersub
	
	/**
	 * X coordinate system origin
	 */
	private double OrgX ; // Coordinate system origin
	
	/**
	 * Y coordinate system origin
	 */
	private double OrgY ;
	
	/**
	 * Z coordinate system origin
	 */
	private double OrgZ ;
	
	/**
	 * time stamps appended to points
	 */
	private int Time ; // 32 bit integer time stamps appended to points
	
	/**
	 * color appended to points
	 */
	private int Color ; // Color values appended to points
	
	/**
	 * The actual maximum X of coordinate extents of the file
	 */
	private double maxX;
	
	/**
	 * The actual minimum X of coordinate extents of the file
	 */
	private double minX;
	
	/**
	 * The actual maximum Y of coordinate extents of the file
	 */
	private double maxY;
	
	/**
	 * The actual minimum Y of coordinate extents of the file
	 */
	private double minY;
	
	/**
	 * The actual maximum Z of coordinate extents of the file
	 */
	private double maxZ;
	
	/**
	 * The actual minimum Z of coordinate extents of the file
	 */
	private double minZ;
	
	/**
	 * Default constructor, without arguments.
	 * Initializes all components to zero.
	 */  
	public BINHeader(File file) {

		m_Fich = file;

		hdrSize = 56; // sizeof(ScanHdr)
		hdrVersion = 20020715; // Version 20020715, 20010712, 20010129 or 970404
		RecogVal = 970401; // Always 970401

		RecogStr[0] = 'C';
		RecogStr[1] = 'X';
		RecogStr[2] = 'Y';
		RecogStr[3] = 'Z';
		
		PntCnt = 0; // Number of points stored
		Units = 0; // Units per meter = subpermast * uorpersub
		OrgX = 0; // Coordinate system origin
		OrgY = 0;
		OrgZ = 0;
		Time = 0; // 32 bit integer time stamps appended to points
		Color = 0; // Color values appended to points		
		
		maxX=Double.MIN_VALUE; 
		minX=Double.MAX_VALUE;
		maxY=Double.MIN_VALUE; 
		minY=Double.MAX_VALUE;
		maxZ=Double.MIN_VALUE;
		minZ=Double.MAX_VALUE;
	}
	
	// GET METHODS
	/**
	 * Return Size, in bytes, of the header file
	 * 
	 *  @return header size
	 */
	public int getHdrSize() {
		return hdrSize;
	}
	
	/**
	 * Return BIN version of the file
	 * 
	 * @return version
	 */
	public int getHdrVersion() {
		return hdrVersion;
	}

	/**
	 * Return value RecogVal
	 * 
	 * @return RecogVal
	 */
	public int getRecogVal() {
		return RecogVal;
	}

	/**
	 * Return value RecogStr
	 * 
	 * @return RecogStr
	 */
	public void getRecogStr(char[] str) {
		
		str[0] = RecogStr[0];
		str[1] = RecogStr[1];
		str[2] = RecogStr[2];
		str[3] = RecogStr[3];
		return;
	}

	/**
	 * Return the total number of points records within the file
	 * 
	 * @return number of points of file
	 */
	public long getNumPointsRecord() {
		return PntCnt;
	}

	/**
	 * Return the unit of measure
	 * 
	 * @return unit of measure
	 */
	public int getUnits() {
		return Units;
	}

	/**
	 * Return X coordinate system origin
	 * 
	 * @return X coordinate
	 */
	public double getOrgX() {
		return OrgX;
	}

	/**
	 * Return Y coordinate system origin
	 * 
	 * @return Y coordinate
	 */
	public double getOrgY() {
		return OrgY;
	}

	/**
	 * Return Z coordinate system origin
	 * 
	 * @return Z coordinate
	 */
	public double getOrgZ() {
		return OrgZ;
	}

	/**
	 * Return time stamps appended to points
	 * 
	 * @return time stamps
	 */
	public int getTime(){
		return Time;
	}

	/**
	 * Return color values appended to points
	 * 
	 * @return color
	 */
	public int getColor() {
		return Color;
	}

	public long getOffsetData() {
		return getHdrSize();
	}

	public int getVersion() {
		
		if(getHdrVersion()==20010712)
			return LidarHeader.BIN20010712;
		else if(getHdrVersion()==20020715)
			return LidarHeader.BIN20020715;
		else 
			return LidarHeader.UNEXPECTED;
	}

	public double getXScale() {
		return (double)getUnits();
	}

	public double getYScale() {
		return (double)getUnits();
	}

	public double getZScale() {
		return (double)getUnits();
	}
	
	public double getXOffset() {
		return (double)getOrgX();
	}

	public double getYOffset() {
		return (double)getOrgY();
	}

	public double getZOffset() {
		return (double)getOrgZ();
	}
	
	public double getMaxX() {
		
		return maxX;
	}

	public double getMaxY() {
		
		return maxY;
	}

	public double getMaxZ() {
		
		return maxZ;
	}

	public double getMinX() {
		
		return minX;
	}

	public double getMinY() {
		
		return minY;
	}

	public double getMinZ() {
		
		return minZ;
	}
	
	
	
	// SET METHODS
	/**
	 * Set Size, in bytes, of the header file
	 * 
	 * @param value new value of header file
	 */
	public void setHdrSize(int value) {
		hdrSize=value;
	}
	
	/**
	 * Set BIN version of the file
	 * 
	 * @param value new version
	 */
	public void setHdrVersion(int value) {
		hdrVersion=value;
	}

	/**
	 * Set value RecogVal
	 * 
	 * @param value new RecogVal
	 */
	public void setRecogVal(int value) {
		RecogVal=value;
	}

	/**
	 * Set value RecogStr
	 * 
	 * @param str new RecogStr
	 */
	public void setRecogStr(char[] str) {
		RecogStr[0]=str[0];
		RecogStr[1]=str[1];
		RecogStr[2]=str[2];
		RecogStr[3]=str[3];
		return;
	}

	/**
	 * Set point stored
	 * 
	 * @param value new point stored
	 */
	public void setNumPointsRecord(long value) {
		
		try{
			if(value>=0 && value <= UNSIGNED_INT_MAX)
				PntCnt=value;
			else
				throw new OutOfRangeLidarException("Out of range of num points record");
			
		} catch(OutOfRangeLidarException e) {
			
			e.printStackTrace();
		}
	}

	/**
	 * Set unit of measure
	 * 
	 * @param value new unit
	 */
	public void setUnits(int value) {
		Units=value;
	}

	/**
	 * Set X coordinate system origin
	 * 
	 * @param value new X coordinate
	 */
	public void setOrgX(double value) {
		OrgX=value;
	}

	/**
	 * Set Y coordinate system origin
	 * 
	 * @param value new Y coordinate
	 */
	public void setOrgY(double value) {
		OrgY=value;
	}

	/**
	 * Set Z coordinate system origin
	 * 
	 * @param value new Z coordinate
	 */
	public void setOrgZ(double value) {
		OrgZ=value;
	}

	/**
	 * Set time stamps appended to points
	 * 
	 * @param value new time stamps
	 */
	public void setTime(int value) {
		Time=value;
	}

	/**
	 * Set color appended to points
	 * 
	 * @param value new color
	 */
	public void setColor(int value) {
		Color=value;
	}
	
	public void setMaxX(double newValue) {
		maxX = newValue;
	}

	public void setMaxY(double newValue) {
		maxY = newValue;
	}

	public void setMaxZ(double newValue) {
		maxZ = newValue;
	}

	public void setMinX(double newValue) {
		minX = newValue;
	}

	public void setMinY(double newValue) {
		minY = newValue;
	}

	public void setMinZ(double newValue) {
		minZ = newValue;
	}

	public void setOffsetData(long newValue) {
		setHdrSize((int) newValue);
	}

	public void setXOffset(double newValue) {
		OrgX = newValue;
	}

	public void setXScale(double newValue) {
		setUnits((int) newValue);
	}

	public void setYOffset(double newValue) {
		OrgY = newValue;
	}

	public void setYScale(double newValue) {
		setUnits((int) newValue);
	}

	public void setZOffset(double newValue) {
		OrgZ = newValue;
	}

	public void setZScale(double newValue) {
		setUnits((int) newValue);
	}

	/**
	 * Read the header of BIN file
	 * 
	 * @param input input file to read
	 * @return true if success else return false 
	 */
	public boolean readLidarHeader() {		
		
		int offset = 0,numRead = 0;
		byte[] cabecera = new byte[56];
		File file = m_Fich;
		InputStream input;
		
		//leemos la cabecera
	      try {
	    	  
	    	input = new FileInputStream(file);
			while (offset < 56 && (numRead = input.read(cabecera, offset, cabecera.length-offset) ) >= 0) {
			     offset += numRead;
			}
			
		    if (offset < cabecera.length) {
		    	JOptionPane.showMessageDialog(null, "Bad Input Format");
			    return false;
			}
		    
		    input.close();
		    
		} catch (IOException e) {
			e.printStackTrace();
		}

		setHdrSize(ByteUtilities.arr2Int(cabecera, 0));
		if (getHdrSize() != 56) {
	    	JOptionPane.showMessageDialog(null, "Bad input format");
		    return false;
		}

		setHdrVersion(ByteUtilities.arr2Int(cabecera, 4));
		setRecogVal(ByteUtilities.arr2Int(cabecera, 8));
		RecogStr[0] = (char)cabecera[12];
		RecogStr[1] = (char)cabecera[13];
		RecogStr[2] = (char)cabecera[14];
		RecogStr[3] = (char)cabecera[15];
		setNumPointsRecord(ByteUtilities.arr2UnsignedInt(cabecera, 16));
		setUnits(ByteUtilities.arr2Int(cabecera, 20));
		setOrgX(ByteUtilities.arr2Double(cabecera, 24));
		setOrgY(ByteUtilities.arr2Double(cabecera, 32));
		setOrgZ(ByteUtilities.arr2Double(cabecera, 40));
		setTime(ByteUtilities.arr2Int(cabecera, 48));
		setColor(ByteUtilities.arr2Int(cabecera, 52));
		
		getMaxMinXYZ();

		return true;
	}
	
	private void getMaxMinXYZ() {
		
		long auxMaxX=Long.MIN_VALUE, auxMinX=Long.MAX_VALUE, x;
		long auxMaxY=Long.MIN_VALUE, auxMinY=Long.MAX_VALUE, y;
		long auxMaxZ=Long.MIN_VALUE, auxMinZ=Long.MAX_VALUE, z;
		long j;
		long inc=1;
		BigByteBuffer2 bb;
		FileChannel channel;
		FileInputStream fin;
		
		
		try {
			fin = new FileInputStream(m_Fich);
		
			// Open the file and then get a channel from the stream
			channel = fin.getChannel();

			// Get the file's size and then map it into memory
			// bb = channel.map(FileChannel.MapMode.READ_ONLY, 0, size);
	        bb = new BigByteBuffer2(channel, FileChannel.MapMode.READ_ONLY);
	        bb.order(ByteOrder.LITTLE_ENDIAN);
	        
	        inc = getNumPointsRecord() / 5000;
			if (inc < 1)
				inc = 1;
			
			if(getVersion()==LidarHeader.BIN20010712) {
				
				for (j=0 ; j<getNumPointsRecord(); j+=inc) {
				
					BINPoint2001 pointBin2001 = new BINPoint2001(getColor()>0, getTime()>0);
					pointBin2001.readPoint3D(bb,this,j);
					x=pointBin2001.getX();
					y=pointBin2001.getY();
					z=pointBin2001.getZ();
					
					if (auxMinX > x)
						auxMinX = x;
					
					if (auxMaxX < x)
						auxMaxX = x;
					
					if (auxMinY > y)
						auxMinY = y;
					
					if (auxMaxY < y)
						auxMaxY = y;
					
					if (auxMinZ > z)
						auxMinZ = z;
					
					if (auxMaxZ < z)
						auxMaxZ = z;
				}
				
				setMaxX((auxMaxX - getOrgX())/getUnits());
				setMinX((auxMinX - getOrgX())/getUnits());
				setMaxY((auxMaxY - getOrgY())/getUnits());
				setMinY((auxMinY - getOrgY())/getUnits());
				setMaxZ((auxMaxZ - getOrgZ())/getUnits());
				setMinZ((auxMinZ - getOrgZ())/getUnits());
			} else if(getVersion()==LidarHeader.BIN20020715) {
				
				for (j=0 ; j<getNumPointsRecord(); j+=inc) {
					
					BINPoint2002 pointBin2002 = new BINPoint2002(getColor()>0, getTime()>0);
					pointBin2002.readPoint3D(bb,this,j);
					x=pointBin2002.getX();
					y=pointBin2002.getY();
					z=pointBin2002.getZ();
					
					if (auxMinX > x)
						auxMinX = x;
					
					if (auxMaxX < x)
						auxMaxX = x;
					
					if (auxMinY > y)
						auxMinY = y;
					
					if (auxMaxY < y)
						auxMaxY = y;
					
					if (auxMinZ > z)
						auxMinZ = z;
					
					if (auxMaxZ < z)
						auxMaxZ = z;
				}
				
				setMaxX((auxMaxX - getOrgX())/getUnits());
				setMinX((auxMinX - getOrgX())/getUnits());
				setMaxY((auxMaxY - getOrgY())/getUnits());
				setMinY((auxMinY - getOrgY())/getUnits());
				setMaxZ((auxMaxZ - getOrgZ())/getUnits());
				setMinZ((auxMinZ - getOrgZ())/getUnits());
			}
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean writeLidarHeader(ByteBuffer bb) {
		
		byte[] hdr = new byte[56];
		int i, index;
		
		// hdrSize bytes 0-4
		ByteUtilities.int2Arr(getHdrSize(), hdr, 0);
		
		// hdrVersion bytes 4-8
		ByteUtilities.int2Arr(getHdrVersion(), hdr, 4);
		
		// RecogVal bytes 8-12
		ByteUtilities.int2Arr(getRecogVal(), hdr, 8);
		
		// RecogStr bytes 12-16
		i=0;
		for(index=12;index<16;index++){
			hdr[index] = ((byte)(RecogStr[i] & 0XFF));
			i++;
		}
		
		// PntCnt bytes 16-20
		ByteUtilities.unsignedInt2Arr(getNumPointsRecord(), hdr, 16);
		
		// Units bytes 20-24
		ByteUtilities.int2Arr(getUnits(), hdr, 20);
		
		// OrgX bytes 24-32
		ByteUtilities.double2Arr(getOrgX(), hdr, 24);
				
		// OrgY bytes 32-40
		ByteUtilities.double2Arr(getOrgY(), hdr, 32);
		
		// OrgZ bytes 40-48
		ByteUtilities.double2Arr(getOrgZ(), hdr, 40);
		
		// Time bytes 48-52
		ByteUtilities.int2Arr(getTime(), hdr, 48);
		
		// Color bytes 52-56
		ByteUtilities.int2Arr(getColor(), hdr, 52);
		
		bb.put(hdr);
		
		return false;
	}
}
