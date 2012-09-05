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

import java.io.*;
import java.nio.ByteBuffer;

import javax.swing.JOptionPane;


public class LASVariableLengthRecord_V11 extends LASVariableLengthRecord_1X {	
	
	/**
	 * a reserved field.
	 */
	private int reserved;
	
	/**
	 * Default constructor, without arguments.
	 * Initializes all components to zero.
	 */ 
	public LASVariableLengthRecord_V11() {
		super();
		reserved = 0;
	}
	
	/**
	 * This field is reserved in LAS1.1
	 * 
	 * @return record signature
	 */
	public int getReserved() {	
		return reserved;
	}
	
	/**
	 * This field is reserved in LAS1.1
	 * 
	 * param r new record signature
	 */
	public void setReserved(int r) {
		
		try{
			if(r>=0 && r<=LidarHeader.UNSIGNED_SHORT_MAX)
				reserved = r;
			else
				throw new OutOfRangeLidarException("Out of range of reserved field (Variable Length Record)");
			
		} catch(OutOfRangeLidarException e) {
			
			e.printStackTrace();
		}
	}

	/**
	 * Read the variable length record
	 * 
	 * @param input input file to read
	 * @param VarLengthRecord buffer into which the data is read 
	 * @return true if success else return false 
	 */
	protected boolean readVarLegthRecordData(InputStream input) {
		if(RecordLengthAfterHeader <= 0) {
			return false;
		}
		
		try {
			
			data = new byte[RecordLengthAfterHeader];
			int offset=0,numRead=0;

			while (offset < RecordLengthAfterHeader && (numRead = input.read(data, offset, RecordLengthAfterHeader-offset) ) >= 0) {
			     offset += numRead;
			}
			
		    if (offset < RecordLengthAfterHeader) {
		    	JOptionPane.showMessageDialog(null, "Bad input format");
			    return false;
			}
		} catch(IOException e) {
			
			e.printStackTrace();
			return false;
		}
		
		return true;			
	}
	
	/**
	 * Read the Variable length record header of LAS file
	 * 
	 * @param input input file to read
	 * @return true if success else return false 
	 */
	public boolean readVarLegthRecord(InputStream input) {
		
		int offset=0,numRead=0, j; 
		byte[] VarLengthRecord = new byte[54];
		
		//read VarLenhRecord
	      try {
	    	
			while (offset < 54 && (numRead = input.read(VarLengthRecord, offset, VarLengthRecord.length-offset) ) >= 0) {
				
			     offset += numRead;
			}
			
		    if (offset < VarLengthRecord.length) {
		    	JOptionPane.showMessageDialog(null, "Bad input format");
			    return false;
			}
		    
		
		    reserved = ByteUtilities.arr2Unsignedshort(VarLengthRecord, 0);
    	  
		    for(j=0;j<16;j++)
				userID[j] = (char)(VarLengthRecord[j+2] & 0xFF);
			
			recordID = ByteUtilities.arr2Unsignedshort(VarLengthRecord, 18);
			RecordLengthAfterHeader = ByteUtilities.arr2Unsignedshort(VarLengthRecord, 20);
			
			for(j=0;j<32;j++)
				description[j] = (char)(VarLengthRecord[j+22] & 0xFF);

			// read data of VLR
			readVarLegthRecordData(input);
	    	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	public boolean writeVarLegthRecord(ByteBuffer bb) {
		
		int i;
		int index=0;
		byte[] VL_Buffer = new byte[54];
	    
		// reserved bytes 0-2
		ByteUtilities.unsignedShort2Arr(getReserved(), VL_Buffer, 0);
	    
		// userID bytes 2-18
		i = 0;
		for(index=2;index<18;index++) {
			VL_Buffer[index] = ((byte)(userID[i] & 0XFF));
			i++;
		}
		
		// recordID bytes 18-20
		ByteUtilities.unsignedShort2Arr(getRecordID(), VL_Buffer, 18);
		
		// RecordLengthAfterHeader bytes 20-22
		ByteUtilities.unsignedShort2Arr(getRecordLengthAfterHeader(), VL_Buffer, 20);

		// description bytes 22-54
		i = 0;
		for(index=22;index<54;index++) {
			VL_Buffer[index] = ((byte)(description[i] & 0XFF));
			i++;
		}
		
		// write header VLR
		bb.put(VL_Buffer);
		// write data VLR
		bb.put(getData());
		
		return true;
	}
}
