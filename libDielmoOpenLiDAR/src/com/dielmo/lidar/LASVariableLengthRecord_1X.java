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

import java.io.InputStream;
import java.nio.ByteBuffer;


public abstract class LASVariableLengthRecord_1X {	
	
	
	/**
	 * data after the variable length record.
	 * 
	 * Make sure that maximum size is 65536 bytes
	 */
	protected byte[] data;
	
	/**
	 * The userID field is ASCII character data that identifies the
	 * user which created the variable length record.
	 */
	protected char[] userID = new char[16];
	
	/**
	 * Record ID is dependent upon the User ID. There can be 0 to 65535
	 * record Ids for every UserID. The ASPRS standard will manage its own record Ids (User Ids owned by the
	 * specification), otherwise record Ids will be managed by the owner of the given User ID. So each	 
	 * User ID is allowed to assign 0 to 65535 record Ids as they wish. Publicizing the meaning of a
	 * given record ID will be left to the owner of the given User ID. Unknown User ID/Record ID
	 * combinations should be ignored.
	 */
	protected int recordID; 
	
	/**
	 * The record length is the number of bytes for the record after the
	 * end of the standard part of the header.
	 */
	protected int RecordLengthAfterHeader;
	
	/**
	 * Optional null terminated text description of the data.
	 */
	protected char[] description = new char[32]; 

	
	/**
	 * Default constructor, without arguments.
	 * Initializes all components to zero.
	 */ 
	public LASVariableLengthRecord_1X() {
		int i;
		
		for(i=0;i<16;i++)
			userID[i] = 0;
		
		recordID = 0;
		RecordLengthAfterHeader=0;
		
		for(i=0;i<32;i++)
			description[i] = 0;
	}
	

	/**
	 * Return the userID field as string that identifies the
	 * user which created the variable length record.
	 * 
	 * @return user ID
	 */
	public String getUserID() {	
		String s;
		char[] G = new char[16];
		int i;
		for(i=0;i<16;i++)
		{
			if(userID[i]!=0)
				G[i] = userID[i];
			else
				G[i] = ' ';
		}
				
		s = new String(G);
		return s;
	}
	
	/**
	 * set the userID field as string that identifies the
	 * user which created the variable length record.
	 * 
	 * Only accept 16 characters
	 * 
	 * @param u new userID
	 */
	public void setUserID(String u) {	

		int i;
		for(i=0;i<16 && i<u.length() ;i++){
			
			userID[i] = u.charAt(i);
		}
	
		// put to zero.
		while(i<16){
			
			userID[i] = 0;
			i++;
		}
	}
	
	/**
	 * Return record ID.
	 * 
	 * Record ID is dependent upon the User ID. There can be 0 to 65535 
	 * record Ids for every UserID. The ASPRS standard will manage its own
	 * record Ids (User Ids owned by the specification), otherwise record
	 * Ids will be managed by the owner of the given User ID. So each User
	 * ID is allowed to assign 0 to 65535 record Ids as they wish. 
	 * Publicizing the meaning of a given record ID will be left to the
	 * owner of the given User ID. Unknown User ID/Record ID combinations
	 * should be ignored. 
	 *  
	 * @return record ID
	 */
	public int getRecordID() {	
		return recordID;
	}
	
	/**
	 * set record ID.
	 * 
	 * Record ID is dependent upon the User ID. There can be 0 to 65535 
	 * record Ids for every UserID. The ASPRS standard will manage its own
	 * record Ids (User Ids owned by the specification), otherwise record
	 * Ids will be managed by the owner of the given User ID. So each User
	 * ID is allowed to assign 0 to 65535 record Ids as they wish. 
	 * Publicizing the meaning of a given record ID will be left to the
	 * owner of the given User ID. Unknown User ID/Record ID combinations
	 * should be ignored. 
	 *  
	 * @param r new record ID
	 */
	public void setRecordID( int r) {	

		try{
			if(r>=0 && r<=LidarHeader.UNSIGNED_SHORT_MAX)
				recordID=r;
			else
				throw new OutOfRangeLidarException("Out of range of record ID (Variable Length Record)");
			
		} catch(OutOfRangeLidarException e) {
			
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Return record length after header. This is the number of bytes
	 * for the record after the end of the standard part of the header.
	 *  
	 * @return record length after header
	 */
	public int getRecordLengthAfterHeader() {	
		return RecordLengthAfterHeader;
	}
	
	/**
	 * set record length after header. This is the number of bytes
	 * for the record after the end of the standard part of the header.
	 *  
	 * @param r new record length after header
	 */
	public void setRecordLengthAfterHeader(int r) {	
		
		try{
			if(r>=0 && r<=LidarHeader.UNSIGNED_SHORT_MAX)
				RecordLengthAfterHeader = r;
			else
				throw new OutOfRangeLidarException("Out of range of record length after header (Variable Length Record)");
			
		} catch(OutOfRangeLidarException e) {
			
			e.printStackTrace();
		}
	}
	
	/**
	 * Return the text description of the data.
	 * 
	 * @return description
	 */
	public String getDescription() {	
		String s;
		char[] D = new char[32];
		int i;
		for(i=0;i<32;i++) {
			if(description[i]!=0)
				D[i] = description[i];
			else
				D[i] = ' ';
		}
				
		s = new String(D);
		return s;
	}
	
	/**
	 * set the text description of the data.
	 * Only accept 32 characters
	 * 
	 * @param d new description
	 */
	public void setDescription(String d) {	
		int i;
		for(i=0;i<32 && i<d.length();i++) {
		
			description[i] = d.charAt(i);
		}
	
		// put to zero.
		while(i<32){
			
			description[i] = 0;
			i++;
		}
	}

	/**
	 * Read the collection of bytes of variable length record 
	 * 
	 * @param input input file to read
	 * @param VarLengthRecord buffer into which the data is read 
	 * @return true if success else return false 
	 */
	protected abstract boolean readVarLegthRecordData(InputStream input);
	
	/**
	 * Read the Variable length record header of LAS file
	 * 
	 * @param input input file to read
	 * @return true if success else return false 
	 */
	public abstract boolean readVarLegthRecord(InputStream input);
	
	/**
	 * Write the Variable length record header of LAS file
	 * 
	 * @param bb byte buffer to write
	 * @return true if success else return false 
	 */
	public abstract boolean writeVarLegthRecord(ByteBuffer bb);

	
	/**
	 * return data after the variable length record.
	 * 
	 * maximum size is 65536 bytes
	 * 
	 * @return data after variable length record.
	 */
	public byte[] getData() {
		return data;
	}


	/**
	 * set data after the variable length record.
	 * 
	 * Make sure that maximum size is 65536 bytes
	 * 
	 * @return data after variable length record.
	 */
	public void setData(byte[] data) {
		this.data = data;
	}
}
