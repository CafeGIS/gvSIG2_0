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
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JOptionPane;


/**
 * Inicizliza Lidar points and header
 *
 * @author Oscar Garcia
 */
public class InicializeLidar {

	public static int testLASFormat(File name) throws IOException,
			UnexpectedLidarHeaderException {

		File f = name;
		InputStream input;
		int offset = 0,numRead = 0;
		char[] FileSignatureLASF = new char[4];
		char  versionMajor, versionMinor, format;
		byte[] cabecera = new byte[105]; // lleemos hasta el byte que nos indica la version de LAS en la que estamos


	    	input = new FileInputStream(f);
			while (offset < cabecera.length && (numRead = input.read(cabecera, offset, cabecera.length-offset) ) >= 0) {
			     offset += numRead;
			}

		    FileSignatureLASF[0] = (char)(cabecera[0] & 0xFF);
		    FileSignatureLASF[1] = (char)(cabecera[1] & 0xFF);
		    FileSignatureLASF[2] = (char)(cabecera[2] & 0xFF);
		    FileSignatureLASF[3] = (char)(cabecera[3] & 0xFF);

		    if(FileSignatureLASF[0] == 'L'&& FileSignatureLASF[1] == 'A'&& FileSignatureLASF[2] == 'S' && FileSignatureLASF[3] == 'F') {

			    if (offset < cabecera.length) {
			    	JOptionPane.showMessageDialog(null, "Bad input format");
				}

			    versionMajor = (char)(cabecera[24] & 0xFF);
			    versionMinor = (char)(cabecera[25] & 0xFF);
			    format = (char)(cabecera[104] & 0xFF);

		    	if(versionMajor == 1 && versionMinor == 0) {

		    		if(format == 0) {
						return LidarHeader.LAS10F0;
					} else if(format == 1) {
						return LidarHeader.LAS10F1;
					}
		    	} else if(versionMajor == 1 && versionMinor == 1) {

		    		if(format == 0) {
						return LidarHeader.LAS11F0;
					} else if(format == 1) {
						return LidarHeader.LAS11F1;
					}
		    	} else if (versionMajor == 1 && versionMinor == 2){

		    		if(format == 0) {
						return LidarHeader.LAS12F0;
					} else if(format == 1) {
						return LidarHeader.LAS12F1;
					} else if(format == 2) {
						return LidarHeader.LAS12F2;
					} else if(format == 3) {
						return LidarHeader.LAS12F3;
					}

		    	} else {
		    		throw new UnexpectedLidarHeaderException("Header not expected");
		    	}
		    }


		return LidarHeader.UNEXPECTED;
	}

	public static int testBINFormat(File name) throws IOException {

		File f = name;
		InputStream input;
		int offset = 0,numRead = 0;
		byte[] cabecera = new byte[8];


		input = new FileInputStream(f);
		while (offset < cabecera.length
				&& (numRead = input.read(cabecera, offset, cabecera.length
						- offset)) >= 0) {
			offset += numRead;
		}

		int HdrVersionBin;
		HdrVersionBin = ByteUtilities.arr2Int(cabecera, 4);
		if (HdrVersionBin == 20020715) {

			return LidarHeader.BIN20020715;
		} else if (HdrVersionBin == 20010712) {

			return LidarHeader.BIN20010712;
		} // ( HdrVersionBin == 20010129 || HdrVersionBin == 970404)



		return LidarHeader.UNEXPECTED;
	}

	public static int testFormat(File name) throws IOException,
			UnexpectedLidarHeaderException {

		int f = testLASFormat(name);
		if(f != LidarHeader.UNEXPECTED) {
			return f;
		}

		f = testBINFormat(name);
		if(f != LidarHeader.UNEXPECTED) {
			return f;
		}

		if (f == LidarHeader.UNEXPECTED) {
	    	JOptionPane.showMessageDialog(null, "Unsupported format");
		}

		return LidarHeader.UNEXPECTED;

	}

	public static LidarPoint InizializeLidarPoint(File file)
			throws IOException, UnexpectedLidarHeaderException {
		boolean binColor, binTime;

		int type = testFormat(file);
		switch(type) {

			case LidarHeader.LAS10F0:
				return new LASPoint10F0();

			case LidarHeader.LAS10F1:
				return new LASPoint10F1();

			case LidarHeader.LAS11F0:
				return new LASPoint11F0();

			case LidarHeader.LAS11F1:
				return new LASPoint11F1();

			case LidarHeader.LAS12F0:
				return new LASPoint12F0();

			case LidarHeader.LAS12F1:
				return new LASPoint12F1();

			case LidarHeader.LAS12F2:
				return new LASPoint12F2();

			case LidarHeader.LAS12F3:
				return new LASPoint12F3();

			case LidarHeader.BIN20010712:

				BINHeader headerBin2001 = new BINHeader(file);
				headerBin2001.readLidarHeader();

				binColor = false;
				binTime = false;


				if(headerBin2001.getColor()>0) {
					binColor = true;
				}

				if(headerBin2001.getTime()>0) {
					binTime = true;
				}

				return new BINPoint2001(binColor, binTime);

			case LidarHeader.BIN20020715:
				BINHeader headerBin2002 = new BINHeader(file);
				headerBin2002.readLidarHeader();

				binColor = false;
				binTime = false;


				if(headerBin2002.getColor()>0) {
					binColor = true;
				}

				if(headerBin2002.getTime()>0) {
					binTime = true;
				}

				return new BINPoint2002(binColor, binTime);
		}

		return null;
	}


	public static LidarHeader InizializeLidarHeader(File file)
			throws IOException, UnexpectedLidarHeaderException {
		int type = testFormat(file);
		switch(type) {

			case LidarHeader.LAS10F0:
			case LidarHeader.LAS10F1:
				LASHeader_V10 header10 = new LASHeader_V10(file);
				header10.readLidarHeader();
				return header10;

			case LidarHeader.LAS11F0:
			case LidarHeader.LAS11F1:
				LASHeader_V11 header11 = new LASHeader_V11(file);
				header11.readLidarHeader();
				return header11;

			case LidarHeader.LAS12F0:
			case LidarHeader.LAS12F1:
			case LidarHeader.LAS12F2:
			case LidarHeader.LAS12F3:
				LASHeader_V12 header12 = new LASHeader_V12(file);
				header12.readLidarHeader();
				return header12;

			case LidarHeader.BIN20010712:

				BINHeader headerBin2001 = new BINHeader(file);
				headerBin2001.readLidarHeader();
				return headerBin2001;

			case LidarHeader.BIN20020715:
				BINHeader headerBin2002 = new BINHeader(file);
				headerBin2002.readLidarHeader();
				return headerBin2002;
		}

		return null;
	}
}