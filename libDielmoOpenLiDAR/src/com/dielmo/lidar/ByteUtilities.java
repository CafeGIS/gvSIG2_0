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

/**
 * Static class to work with a given vector of bytes.
 *
 * @author Oscar Garcia
 */
public class ByteUtilities {

	// operaciones de vectores de bytes a tipos de java
	/**
	 * From an array of bytes (byte[]), takes over from the position given as start
	 * and gives value to int variable using the following 4 bytes.
	 * 
	 * @param arr array of bytes
	 * @param start initial position
	 */
	public static int arr2Int (byte[] arr, int start) {
		int i = 0;
		int length = 4;
		int cnt = 0;
		byte[] tmp = new byte[length];
		for (i = start; i < (start + length); i++) {
			tmp[cnt] = arr[i];
			//System.out.println(java.lang.Byte.toString(arr[i]) + " " + i);
			cnt++;
		}
		int accum = 0;
		i = 0;
		for ( int shiftBy = 0; shiftBy < 32; shiftBy += 8 ) {
			accum |= ( (int)( tmp[i] & 0xff ) ) << shiftBy;
			i++;
		}
		return accum;
	}

	/**
	 * From an array of bytes (byte[]), takes over from the position given as start
	 * and gives value to double variable using the following 8 bytes.
	 * 
	 * @param arr array of bytes
	 * @param start initial position
	 */
	public static double arr2Double (byte[] arr, int start) {
		int i = 0;
		int length = 8;
		int cnt = 0;
		byte[] tmp = new byte[length];
		for (i = start; i < (start + length); i++) {
			tmp[cnt] = arr[i];
			//System.out.println(java.lang.Byte.toString(arr[i]) + " " + i);
			cnt++;
		}
		long accum = 0;
		i = 0;
		for ( int shiftBy = 0; shiftBy < 64; shiftBy += 8 ) {
			accum |= ( (long)( tmp[i] & 0xff ) ) << shiftBy;
			i++;
		}
		return Double.longBitsToDouble(accum);
	}

	/**
	 * From an array of bytes (byte[]), takes over from the position given as start
	 * and gives value to long variable using the following 4 bytes.
	 * 
	 * @param arr array of bytes
	 * @param start initial position
	 */
	public static long arr2UnsignedInt (byte[] arr, int start) {
		int i = 0;
		int length = 4;
		int cnt = 0;
		byte[] tmp = new byte[length];
		for (i = start; i < (start + length); i++) {
			tmp[cnt] = arr[i];
			//System.out.println(java.lang.Byte.toString(arr[i]) + " " + i);
			cnt++;
		}
		long accum = 0;
		i = 0;
		for ( int shiftBy = 0; shiftBy < 32; shiftBy += 8 ) {
			accum |= ( (long)( tmp[i] & 0xff ) ) << shiftBy;
			i++;
		}
		return accum;
	}
	
	/**
	 * From an array of bytes (byte[]), takes over from the position given as start
	 * and gives value to long variable using the following 8 bytes.
	 * 
	 * @param arr array of bytes
	 * @param start initial position
	 */
	public static long arr2Long(byte[] arr, int start) {
		int i = 0;
		int length = 8;
		int cnt = 0;
		byte[] tmp = new byte[length];
		for (i = start; i < (start + length); i++) {
			tmp[cnt] = arr[i];
			//System.out.println(java.lang.Byte.toString(arr[i]) + " " + i);
			cnt++;
		}
		long accum = 0;
		i = 0;
		for ( int shiftBy = 0; shiftBy < 64; shiftBy += 8 ) {
			accum |= ( (long)( tmp[i] & 0xff ) ) << shiftBy;
			i++;
		}
		return accum;
	}
	
	/**
	 * From an array of bytes (byte[]), takes over from the position given as start
	 * and gives value to int variable using the following 2 bytes.
	 * 
	 * @param arr array of bytes
	 * @param start initial position
	 */
	public static int arr2Unsignedshort (byte[] arr, int start) {
		
		int i = 0;
		int length = 2;
		int cnt = 0;
		byte[] tmp = new byte[length];
		for (i = start; i < (start + length); i++) {
			tmp[cnt] = arr[i];
			//System.out.println(java.lang.Byte.toString(arr[i]) + " " + i);
			cnt++;
		}
		int accum = 0;
		i = 0;
		for ( int shiftBy = 0; shiftBy < 16; shiftBy += 8 ) {
			accum |= ( (int)( tmp[i] & 0xff ) ) << shiftBy;
			i++;
		}
		return accum;
	}	

	/**
	 * Write the bytes of "var" into new byte array.
	 *
	 * @param var the int to encode
	 * @param arrayBytes The byte array to store into.
	 * @param startIndex index data to begin write.
	 */
	public static void int2Arr(int var, byte[] arrayBytes, int startIndex) {

		int length = 4;
	
		if (arrayBytes != null && startIndex+length <= arrayBytes.length) {
			for (int j = startIndex; j < startIndex+length; j++) {
				arrayBytes[j] = (byte) var; // se copian los 8 primeros bits de la variable
				var >>= 8;
			}
		}
	}
	
	/**
	 * Write the bytes of "var" into new byte array.
	 *
	 * @param var the int that represents a unsigned short of 2 bytes to encode
	 * @param arrayBytes The byte array to store into.
	 * @param startIndex index data to begin write.
	 */
	public static void unsignedShort2Arr(int var, byte[] arrayBytes, int startIndex) {

		int length = 2;
	
		if (arrayBytes != null && startIndex+length <= arrayBytes.length) {
			for (int j = startIndex; j < startIndex+length; j++) {
				arrayBytes[j] = (byte) var; // se copian los 8 primeros bits de la variable
				var >>= 8;
			}
		}
	}
	
	/**
	 * Write the bytes of "var" into new byte array.
	 *
	 * @param var the long that represents a unsigned int of 4 bytes to encode
	 * @param arrayBytes The byte array to store into.
	 * @param startIndex index data to begin write.
	 */
	public static void unsignedInt2Arr(long var, byte[] arrayBytes, int startIndex) {

		int length = 4;
	
		if (arrayBytes != null && startIndex+length <= arrayBytes.length) {
			for (int j = startIndex; j < startIndex+length; j++) {
				arrayBytes[j] = (byte) var; // se copian los 8 primeros bits de la variable
				var >>= 8;
			}
		}
	}
	
	/**
	 * Write the bytes of "var" into new byte array.
	 *
	 * @param var the long of 8 bytes to encode
	 * return tmp The byte array to store into.
	 */
	public static void long2Arr(long var, byte[] arrayBytes, int startIndex) {
		
		int length = 8;
		
		if (arrayBytes != null && startIndex+length <= arrayBytes.length) {
			for (int j = startIndex; j < startIndex+length; j++) {
				arrayBytes[j] = (byte) var; // se copian los 8 primeros bits de la variable
				var >>= 8;
			}
		}
	}
	
	/**
	 * Write the bytes of "var" into new byte array.
	 *
	 * @param var the double of 8 bytes to encode
	 * return tmp The byte array.
	 */
	public static void double2Arr(double var, byte[] arrayBytes, int startIndex) {
	
		long bits = Double.doubleToLongBits(var);
		long2Arr(bits, arrayBytes, startIndex);
	}
}