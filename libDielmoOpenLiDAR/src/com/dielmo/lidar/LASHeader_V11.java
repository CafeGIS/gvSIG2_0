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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.swing.JOptionPane;


public class LASHeader_V11 extends LASHeader_1X{	
	
	/**
	 * vector of variable length record header
	 */
	private LASVariableLengthRecord_V11[] VLR;
	
	/**
	 * Flight line number; unsigned short
	 */
	protected int fileSourceID;
	
	/**
	 * reserved, must be 0; unsigned short
	 */
	protected int reserved;
	
	/**
	 * Default constructor, without arguments.
	 * Initializes all components to zero.
	 */ 
	public LASHeader_V11(File file)	{
		super(file);
		
		fileSourceID = 0;
		reserved = 0;
	}
	
	// GET METHOD
	/**
	 * Return file source ID
	 * 
	 * @return file Source ID if version is LAS_1.1 else return -1
	 */
	public int getFileSourceID() {
		return fileSourceID;
	}
	
	/**
	 * Return file source ID
	 * 
	 * @return file Source ID if version is LAS_1.1 else return -1
	 */
	public int getReserved() {
		return reserved;
	}
	
	
	// SET METHOD
	/**
	 * set new file source ID
	 * 
	 * @param f new file source ID
	 */
	public void setFileSourceID(int f) {
		
		if(f>=0 && f <= UNSIGNED_SHORT_MAX)
			fileSourceID = f;
	}
	
	/**
	 * set new reserved value
	 * 
	 * @param r new reserved value
	 */
	public void setReserved(int r) {
		if(r>=0 && r <= UNSIGNED_SHORT_MAX)
			reserved = r;
		else
			reserved = 0;
	}
	
	/**
	 * Read the header of LAS file
	 * 
	 * @param input input file to read
	 * @return true if success else return false 
	 */
	public boolean readLidarHeader() {
		
		int offset = 0,numRead = 0, i;
		long j;
		byte[] cabecera = new byte[227];
		File file = m_Fich;
		InputStream input; 
		
		//leemos la cabecera
	      try {
	    	 
	    	input = new FileInputStream(file);
	    	  
			while (offset < 227 && (numRead = input.read(cabecera, offset, cabecera.length-offset) ) >= 0) {
			     offset += numRead;
			}
			
		    if (offset < cabecera.length) {
		    	JOptionPane.showMessageDialog(null, "Bad input format");
			    return false;
			}
		    
		    // set parameters
		    for(i=0;i<4;i++)
				fileSignature[i] = (char)(cabecera[i] & 0xFF);
			
			setFileSourceID(ByteUtilities.arr2Unsignedshort(cabecera, 4));
			setReserved(ByteUtilities.arr2Unsignedshort(cabecera, 6));
			setGUID1(ByteUtilities.arr2UnsignedInt(cabecera, 8));
			setGUID2(ByteUtilities.arr2Unsignedshort(cabecera, 12));
			setGUID3(ByteUtilities.arr2Unsignedshort(cabecera, 14));
			
			for(i=0;i<8;i++)
				GUID4[i] = (char)(cabecera[i+16] & 0xFF);
			
			setVersionMayor((char)(cabecera[24] & 0xFF));
			setVersionMinor((char)(cabecera[25] & 0xFF));
			
			for(i=0;i<32;i++)
				systemIDentifier[i] = (char)(cabecera[i+26] & 0xFF);
			for(i=0;i<32;i++)
				generatingSoftware[i] = (char)(cabecera[i+58] & 0xFF);
			
			setDay(ByteUtilities.arr2Unsignedshort(cabecera, 90));
			setYear(ByteUtilities.arr2Unsignedshort(cabecera, 92));		
			
			setHdrSize(ByteUtilities.arr2Unsignedshort(cabecera, 94));
			if (getHdrSize() < 227) {
		    	JOptionPane.showMessageDialog(null, "Bad input format");
			    return false;
			}		
			
			setOffsetData(ByteUtilities.arr2UnsignedInt(cabecera, 96));
			setNumVarLengthRecord(ByteUtilities.arr2UnsignedInt(cabecera, 100));		
			SetPointDataFormatID((char)(cabecera[104] & 0xFF));
			SetPointDataRecordLength(ByteUtilities.arr2Unsignedshort(cabecera, 105));		
			setNumPointsRecord(ByteUtilities.arr2UnsignedInt(cabecera, 107));
			numPointsByReturn[0] = ByteUtilities.arr2UnsignedInt(cabecera, 111);
			numPointsByReturn[1] = ByteUtilities.arr2UnsignedInt(cabecera, 115);
			numPointsByReturn[2] = ByteUtilities.arr2UnsignedInt(cabecera, 119);
			numPointsByReturn[3] = ByteUtilities.arr2UnsignedInt(cabecera, 123);
			numPointsByReturn[4] = ByteUtilities.arr2UnsignedInt(cabecera, 127);
			setXScale(ByteUtilities.arr2Double(cabecera, 131));
			setYScale(ByteUtilities.arr2Double(cabecera, 139));
			setZScale(ByteUtilities.arr2Double(cabecera, 147));
			setXOffset(ByteUtilities.arr2Double(cabecera, 155));
			setYOffset(ByteUtilities.arr2Double(cabecera, 163));
			setZOffset(ByteUtilities.arr2Double(cabecera, 171));
			setMaxX(ByteUtilities.arr2Double(cabecera, 179));
			setMinX(ByteUtilities.arr2Double(cabecera, 187));
			setMaxY(ByteUtilities.arr2Double(cabecera, 195));
			setMinY(ByteUtilities.arr2Double(cabecera, 203));
			setMaxZ(ByteUtilities.arr2Double(cabecera, 211));
			setMinZ(ByteUtilities.arr2Double(cabecera, 219));
			
			if(getHdrSize() > 227)
				input.skip(getHdrSize()-227);
			
			VLR = new LASVariableLengthRecord_V11[(int) getNumVarLengthRecord()];
			
			// read Variable lengh record
		    for(j=0;j<getNumVarLengthRecord();j++) {
		    	
		    	VLR[(int) j] = new LASVariableLengthRecord_V11();
		    	VLR[(int) j].readVarLegthRecord(input);
		    }
		    input.close();
		    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}
	
	public LASVariableLengthRecord_1X[] getLASHeaderVLR() {
		return VLR;
	}
	
	public LASVariableLengthRecord_1X getLASHeaderVLRByIndex(long i) {
		
		if(i<getNumVarLengthRecord())
			return VLR[(int) i];
		
		return null;
	}
	
	public int getVersion() {
		
		if(getPointDataFormatID()==0)
			return LidarHeader.LAS11F0;
		else if(getPointDataFormatID()==1)
			return LidarHeader.LAS11F1;
		else 
			return LidarHeader.UNEXPECTED;
	}
	
	/**
	 * set new Variable length record
	 * 
	 * @param l new vector of Variable length record
	 */ 
	public void setLASHeaderVLR(LASVariableLengthRecord_1X[] v) {
		
		int i;
		long countBytes=0;
		long oldOffsetData=0;
		
		// new VLR size
		VLR = new LASVariableLengthRecord_V11[v.length];
		for(i=0;i<v.length;i++){
			
			// cuentaBytes lleva la cuenta de los bytes que consumen cada una
			// de las variable lengthRecord. Esto es el numero de bytes que
			// a continuacion de esta vienen mas los 54 bytes de la cabecera de la variable en si
			LASVariableLengthRecord_V11 vlrAux = (LASVariableLengthRecord_V11) v[i];
			countBytes += vlrAux.getRecordLengthAfterHeader()+54;
			VLR[i] = vlrAux;
		}
		
		// change number of VLR in LAS HEADER
		setNumVarLengthRecord(v.length);
		oldOffsetData = getOffsetData();
		
		// countBytes is number of bytes of all variable length record
		if(countBytes> (oldOffsetData - getHdrSize()) ){
			
			// OffsetData change 
			setOffsetData(countBytes+getHdrSize());
			
			// temporal folder to make the new las file. This is because the Variable Length Records overlaps the data points
			String tempDirectoryPath = System.getProperty("java.io.tmpdir");
			
			// escribe en un temporal el fichero.
			int aux = (int)(Math.random() * 1000);
			File fTemp = new File(tempDirectoryPath + "/tmpLidar" + aux + ".las");
			try {
				
				FileChannel fcoutLidar = new FileOutputStream(fTemp).getChannel();
				FileChannel fcinLidar = new FileInputStream(m_Fich).getChannel();
				
				// Write the new LAS header adn VLR.
				ByteBuffer bb;
				bb = ByteBuffer.allocateDirect((int) (countBytes+getHdrSize()));
				writeLidarHeader(bb);
				bb.flip();

				while (bb.remaining() > 0)
					fcoutLidar.write(bb);

				bb.flip().limit(bb.capacity());
				
				
				long sizeOfFile = oldOffsetData + getNumPointsRecord()*getPointDataRecordLength();
				long startToWrite = oldOffsetData;
				
				// write data points
				while(startToWrite < sizeOfFile){
					
					startToWrite += fcinLidar.read(bb, startToWrite);
					bb.flip();

					while (bb.remaining() > 0)
						fcoutLidar.write(bb);

					bb.flip().limit(bb.capacity());
				}
				fcoutLidar.close();
				fcinLidar.close();
				
				FileChannel fcinNewLidar = new FileInputStream(fTemp).getChannel();
				FileChannel fcoutNewLidar = new FileOutputStream(m_Fich).getChannel();
				// copy temporal file to the original file
				copyFile(fcinNewLidar, fcoutNewLidar);
				
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

	public boolean writeLidarHeader(ByteBuffer bb) {
		
		byte[] hdr = new byte[227];
		int i, index;	
	    
		// fileSignature bytes 0-4
	    for(i=0;i<4;i++)
			hdr[i] = ((byte)(fileSignature[i] & 0XFF));

	    // reserved bytes 4-6
	    ByteUtilities.unsignedShort2Arr(getFileSourceID(), hdr, 4);
		
		// reserved bytes 6-8
	    ByteUtilities.unsignedShort2Arr(getReserved(), hdr, 6);
		
	 // GUID1 bytes 8-12
	    ByteUtilities.unsignedInt2Arr(getGUID1(), hdr, 8);
		
		// GUID2 bytes 12-14
	    ByteUtilities.unsignedShort2Arr(getGUID2(), hdr, 12);

		// GUID3 bytes 14-16
	    ByteUtilities.unsignedShort2Arr(getGUID3(), hdr, 14);
		
		// GUID4 bytes 16-24
		i=0;
		for(index=16;index<24;index++) {
			
			hdr[index] = (byte)(GUID4[i] & 0xFF);
			i++;
		}
		
		// Version, byte 24 and 25
		hdr[24] = (byte)(getVersionMayor() & 0xFF);
		hdr[25] = (byte)(getVersionMinor() & 0xFF);
		
		// Sytem Identifier bytes 26-58
		i=0;
		for(index=26;index<58;index++) {
			
			hdr[index] = (byte)(systemIDentifier[i] & 0xFF);
			i++;
		}
		
		// Generating Software bytes 58-90
		i=0;
		for(index=58;index<90;index++) {
			
			hdr[index] = (byte)(generatingSoftware[i] & 0xFF);
			i++;
		}
		
		// day bytes 90-92
		ByteUtilities.unsignedShort2Arr(getDay(), hdr, 90);
		
		// year bytes 92-94
		ByteUtilities.unsignedShort2Arr(getYear(), hdr, 92);
		
		// hdrSize bytes 94-96
		ByteUtilities.int2Arr(getHdrSize(), hdr, 94);
		
		if (getHdrSize() < 227) {
	    	JOptionPane.showMessageDialog(null, "bad input format");
		    return false;
		}
		
		// offsetData bytes 96-100
		ByteUtilities.unsignedInt2Arr(getOffsetData(), hdr, 96);
		
		// numVarLengthRecord bytes 100-104
		ByteUtilities.unsignedInt2Arr(getNumVarLengthRecord(), hdr, 100);
		
		// pointDataFormatID bytes 104
		hdr[104] = (byte)(getPointDataFormatID() & 0xFF);
		
		// pointDataRecordLength bytes 105-107
		ByteUtilities.unsignedShort2Arr(getPointDataRecordLength(), hdr, 105);
	
		// numPointsRecord bytes 107-111
		ByteUtilities.unsignedInt2Arr(getNumPointsRecord(), hdr, 107);
		
		// numPointsByReturn[0] bytes 111-115
		ByteUtilities.unsignedInt2Arr(getNumPointsByReturnIndex(0), hdr, 111);
		
		// numPointsByReturn[1] bytes 115-119
		ByteUtilities.unsignedInt2Arr(getNumPointsByReturnIndex(1), hdr, 115);
		
		// numPointsByReturn[2] bytes 119-123
		ByteUtilities.unsignedInt2Arr(getNumPointsByReturnIndex(2), hdr, 119);
		
		// numPointsByReturn[3] bytes 123-127
		ByteUtilities.unsignedInt2Arr(getNumPointsByReturnIndex(3), hdr, 123);
		
		// numPointsByReturn[4] bytes 127-131
		ByteUtilities.unsignedInt2Arr(getNumPointsByReturnIndex(4), hdr, 127);
		
		// xScale bytes 131-139
		ByteUtilities.double2Arr(getXScale(), hdr, 131);
		
		// yScale bytes 139-147
		ByteUtilities.double2Arr(getYScale(), hdr, 139);
		
		// zScale bytes 147-155
		ByteUtilities.double2Arr(getZScale(), hdr, 147);
		
		// xOffset bytes 155-163
		ByteUtilities.double2Arr(getXOffset(), hdr, 155);
		
		// yOffset bytes 163-171
		ByteUtilities.double2Arr(getYOffset(), hdr, 163);
		
		// zOffset bytes 171-179
		ByteUtilities.double2Arr(getZOffset(), hdr, 171);
		
		// maxX bytes 179-187
		ByteUtilities.double2Arr(getMaxX(), hdr, 179);
		
		// minX bytes 187-195
		ByteUtilities.double2Arr(getMinX(), hdr, 187);
		
		// maxY bytes 195-203
		ByteUtilities.double2Arr(getMaxY(), hdr, 195);
		
		// minY bytes 203-211
		ByteUtilities.double2Arr(getMinY(), hdr, 203);

		// maxZ bytes 211-219
		ByteUtilities.double2Arr(getMaxZ(), hdr, 211);

		// minZ bytes 219-227
		ByteUtilities.double2Arr(getMinZ(), hdr, 219);
		
		bb.put(hdr);

		// write VARIABLE LENGTH RECORD
		for(int j = 0;j<getNumVarLengthRecord();j++) {
			// escribimos la cabecera de la Variable lengh record
			VLR[(int) j].writeVarLegthRecord(bb);	    	
		}
		
		return true;
	}
}
