/* jdwglib. Java Library for reading Dwg files.
 *
 * Author: Jose Morell Rama (jose.morell@gmail.com).
 * Port from the Pythoncad Dwg library by Art Haas.
 *
 * Copyright (C) 2005 Jose Morell, IVER TI S.A. and Generalitat Valenciana
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 * Jose Morell (jose.morell@gmail.com)
 *
 * or
 *
 * IVER TI S.A.
 *  C/Salamanca, 50
 *  46005 Valencia
 *  Spain
 *  +34 963163400
 *  dac@iver.es
 */
package org.gvsig.dwg.lib.readers;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import org.gvsig.dwg.lib.CorruptedDwgEntityException;
import org.gvsig.dwg.lib.DwgClass;
import org.gvsig.dwg.lib.DwgFile;
import org.gvsig.dwg.lib.DwgHandleReference;
import org.gvsig.dwg.lib.DwgObject;
import org.gvsig.dwg.lib.DwgObjectFactory;
import org.gvsig.dwg.lib.DwgObjectOffset;
import org.gvsig.dwg.lib.DwgUtil;
import org.gvsig.dwg.lib.objects.DwgPolyline2D;
import org.gvsig.dwg.lib.objects.DwgPolyline3D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * The DwgFileV14Reader reads the DWG version 14 format
 *
 * @author jmorell
 */
public class DwgFileV14Reader implements IDwgFileReader {

	protected DwgFile dwgFile;

	protected ByteBuffer bb;

	private static Logger logger = LoggerFactory.getLogger(DwgFileV14Reader.class
			.getName());
	
	/**
	 * Reads the DWG version 15 format
	 *
	 * @param dwgFile
	 *            Represents the DWG file that we want to read
	 * @throws IOException
	 *             When DWG file path is wrong
	 */
	public void read(DwgFile dwgFile, ByteBuffer bb) throws IOException {
		this.dwgFile = dwgFile;
		this.bb = bb;
		try {
			readDwgSectionOffsets();
			readHeaders();
			readDwgClasses();
			readDwgObjectOffsets();
			readDwgObjects();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}

	}

	/**
	 * It reads all HEADER section of a DWG 13-14 file.
	 *
	 * This method must be called only for debug purposes because we dont the
	 * meaning (or the practical application) of the fields readed with it
	 *
	 * TODO Pasarlo a la clase abstracta comun que se haga de las versiones 15,
	 * 13-14 (la cabecera es la misma practicamente)
	 *
	 *
	 */
	protected void readHeaders() {

		int offset = dwgFile.getDwgSectionOffset("HEADERS");
		bb.position(offset);

		//1� leemos el sentinnel inicial
		bb.order(ByteOrder.nativeOrder());
		byte[] sentinel = new byte[16];
		bb.get(sentinel);

		if (sentinel[0] != 0xcf)
			logger.warn("sentinel[0] != 0xcf");
		if (sentinel[1] != 0x7b)
			logger.warn("sentinel[1] != 0x7b");
		if (sentinel[2] != 0x1f)
			logger.warn("sentinel[2] != 0x1f");
		if (sentinel[3] != 0x23)
			logger.warn("sentinel[3] != 0x23");
		if (sentinel[4] != 0xfd)
			logger.warn("sentinel[4] != 0xfd");
		if (sentinel[5] != 0xde)
			logger.warn("sentinel[5] != 0xde");
		if (sentinel[6] != 0x38)
			logger.warn("sentinel[6] != 0x38");
		if (sentinel[7] != 0xa9)
			logger.warn("sentinel[7] != 0xa9");
		if (sentinel[8] != 0x5f)
			logger.warn("sentinel[8] != 0x5f");
		if (sentinel[9] != 0x7c)
			logger.warn("sentinel[9] != 0x7c");
		if (sentinel[10] != 0x68)
			logger.warn("sentinel[10] != 0x68");
		if (sentinel[11] != 0xb8)
			logger.warn("sentinel[11] != 0xb8");
		if (sentinel[12] != 0x4e)
			logger.warn("sentinel[12] != 0x4e");
		if (sentinel[13] != 0x6d)
			logger.warn("sentinel[13] != 0x6d");
		if (sentinel[14] != 0x33)
			logger.warn("sentinel[14] != 0x33");
		if (sentinel[15] != 0x5f)
			logger.warn("sentinel[15] != 0x5f");

		//2� seguidamente leemos los datos
		bb.order(ByteOrder.LITTLE_ENDIAN);
		int size = bb.getInt();

		bb.order(ByteOrder.nativeOrder());
		byte[] data = new byte[size];
		bb.get(data);

		int[] intData = DwgUtil.toIntArray(data);

		//3� a continuacion el CRC de la seccion HEADER
		bb.order(ByteOrder.LITTLE_ENDIAN);
		short crc = bb.getShort();

		//Por ultimo, el sentinnel final
		bb.order(ByteOrder.nativeOrder());
		byte[] lastSentinnel = new byte[16];
		bb.get(lastSentinnel);
		if (lastSentinnel[0] != 0x30)
			logger.warn("lastSentinnel[0] != 0x30");
		if (lastSentinnel[1] != 0x84)
			logger.warn("lastSentinnel[1] != 0x84");
		if (lastSentinnel[2] != 0xe0)
			logger.warn("lastSentinnel[2] != 0xe0");
		if (lastSentinnel[3] != 0xdc)
			logger.warn("lastSentinnel[3] != 0xdc");
		if (lastSentinnel[4] != 0x02)
			logger.warn("lastSentinnel[4] != 0x02");
		if (lastSentinnel[5] != 0x21)
			logger.warn("lastSentinnel[5] != 0x21");
		if (lastSentinnel[6] != 0xc7)
			logger.warn("lastSentinnel[6] != 0xc7");
		if (lastSentinnel[7] != 0x56)
			logger.warn("lastSentinnel[7] != 0x56");
		if (lastSentinnel[8] != 0xa0)
			logger.warn("lastSentinnel[8] != 0xa0");
		if (lastSentinnel[9] != 0x83)
			logger.warn("lastSentinnel[9] != 0x83");
		if (lastSentinnel[10] != 0x97)
			logger.warn("lastSentinnel[10] != 0x97");
		if (lastSentinnel[11] != 0x47)
			logger.warn("lastSentinnel[11] != 0x47");
		if (lastSentinnel[12] != 0xb1)
			logger.warn("lastSentinnel[12] != 0xb1");
		if (lastSentinnel[13] != 0x92)
			logger.warn("lastSentinnel[13] != 0x92");
		if (lastSentinnel[14] != 0xcc)
			logger.warn("lastSentinnel[14] != 0xcc");
		if (lastSentinnel[15] != 0xa0)
			logger.warn("lastSentinnel[15] != 0xa0");

		//Ahora interpretamos los datos en bruto
		int bitPos = 0;
		try {
			List val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("VAL1", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("VAL2", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("VAL3", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("VAL4", val.get(1));

			val = DwgUtil.getTextString(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("STRING1", val.get(1));

			val = DwgUtil.getTextString(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("STRING2", val.get(1));

			val = DwgUtil.getTextString(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("STRING3", val.get(1));

			val = DwgUtil.getTextString(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("STRING4", val.get(1));

			val = DwgUtil.getBitLong(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("LONG1", val.get(1));

			val = DwgUtil.getBitLong(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("LONG2", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("SHORT1", val.get(1));

			//TODO REFACTORIZAR ESTO PARA USAR DWGHANDLEREFERENCE

			//TODO Los handle se leen asi??
			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("HANDLE1", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMASO", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMSHO", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMSAV", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("PLINEGEN", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("ORTHOMODE", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("REGENMODE", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("FILLMODE", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("QTEXTMODE", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("PSLTSCALE", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("LIMCHECK", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("BLIPMODE", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("USER_TIMER", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("SKPOLY", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("ANGDIR", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("SPLFRAME", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("ATTREQ", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("ATTDIA", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("MIRRTEXT", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("WORLDVIEW", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("WIREFRAME", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("TILEMODE", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("PLIMCHECK", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("VISRETAIN", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DELOBJ", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DISPSILH", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("PELLISE", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			if (dwgFile.getDwgVersion() == "R14")
				dwgFile.setHeader("PROXYGRAPH", val.get(1));
			else
				dwgFile.setHeader("SAVEIMAGES", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DRAGMODE", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("TREEDEPTH", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("LUNITS", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("LUPREC", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("AUNITS", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("AUPREC", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("OSMODE", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("ATTMODE", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("COORDS", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("PDMODE", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("PICKSTYLE", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("USERI1", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("USERI2", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("USERI3", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("USERI4", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("USERI5", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("SPLINESEGS", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("SURFU", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("SURFV", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("SURFTYPE", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("SURFTAB1", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("SURFTAB2", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("SPLINETYPE", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("SHADEDGE", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("SHADEDIF", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("UNITMODE", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("MAXACTVP", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("ISOLINES", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("CMLJUST", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("TEXTQLTY", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("LTSCALE", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("TEXTSIZE", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("TRACEWID", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("SKETCHINC", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("FILLETRAD", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("THICKNESS", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("ANGBASE", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("PDSIZE", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("PLINEWID", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("USERR1", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("USERR2", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("USERR3", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("USERR4", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("USERR5", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("CHAMFERA", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("CHAMFERB", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("CHAMFERC", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("CHAMFERD", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("FACETRES", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("CMLSCALE", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("CELTSCALE", val.get(1));

			val = DwgUtil.getTextString(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("MENUNAME", val.get(1));

			int[] tdcreate = new int[2];
			val = DwgUtil.getBitLong(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			tdcreate[0] = ((Integer) val.get(1)).intValue();
			val = DwgUtil.getBitLong(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			tdcreate[1] = ((Integer) val.get(1)).intValue();
			dwgFile.setHeader("TDCREATE", tdcreate);

			int[] tdupdate = new int[2];
			val = DwgUtil.getBitLong(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			tdupdate[0] = ((Integer) val.get(1)).intValue();
			val = DwgUtil.getBitLong(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			tdupdate[1] = ((Integer) val.get(1)).intValue();
			dwgFile.setHeader("TDUPDATE", tdupdate);

			int[] tdindwg = new int[2];
			val = DwgUtil.getBitLong(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			tdindwg[0] = ((Integer) val.get(1)).intValue();
			val = DwgUtil.getBitLong(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			tdindwg[1] = ((Integer) val.get(1)).intValue();
			dwgFile.setHeader("TDINDWG", tdindwg);

			int[] tdusrtime = new int[2];
			val = DwgUtil.getBitLong(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			tdusrtime[0] = ((Integer) val.get(1)).intValue();
			val = DwgUtil.getBitLong(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			tdusrtime[1] = ((Integer) val.get(1)).intValue();
			dwgFile.setHeader("TDUSRTIME", tdusrtime);

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("CECOLOR", val.get(1));

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			int intHandle = DwgUtil.handleToInt(val);
			dwgFile.setHeader("HANDSEED", new Integer(intHandle));

			//creo que CLAYER marca la capa actualmente seleccionada en el menu
			// de
			// autocad

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			intHandle = DwgUtil.handleToInt(val);
			dwgFile.setHeader("CLAYER", new Integer(intHandle));

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			intHandle = DwgUtil.handleToInt(val);
			dwgFile.setHeader("TEXSTYLE", new Integer(intHandle));

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			intHandle = DwgUtil.handleToInt(val);
			dwgFile.setHeader("CELLTYPE", new Integer(intHandle));

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			intHandle = DwgUtil.handleToInt(val);
			dwgFile.setHeader("DIMSTYLE", new Integer(intHandle));

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			intHandle = DwgUtil.handleToInt(val);
			dwgFile.setHeader("CMLSTYLE", new Integer(intHandle));

			double[] spaces1 = new double[3];
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces1[0] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces1[1] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces1[2] = ((Double) val.get(1)).doubleValue();
			dwgFile.setHeader("PSPACE_INSBASE", spaces1);

			double[] spaces2 = new double[3];
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces2[0] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces2[1] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces2[2] = ((Double) val.get(1)).doubleValue();
			dwgFile.setHeader("PSPACE_EXTMIN", spaces2);

			double[] spaces3 = new double[3];
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces3[0] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces3[1] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces3[2] = ((Double) val.get(1)).doubleValue();
			dwgFile.setHeader("PSPACE_EXTMAX", spaces2);

			double[] spaces4 = new double[2];
			val = DwgUtil.getRawDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces4[0] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getRawDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces4[1] = ((Double) val.get(1)).doubleValue();
			dwgFile.setHeader("PSPACE_LIMMIN", spaces4);

			double[] spaces5 = new double[2];
			val = DwgUtil.getRawDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces5[0] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getRawDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces5[1] = ((Double) val.get(1)).doubleValue();
			dwgFile.setHeader("PSPACE_LIMMAX", spaces5);

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("PSPACE_ELEVATION", val.get(1));

			double[] spaces6 = new double[3];
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces6[0] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces6[1] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces6[2] = ((Double) val.get(1)).doubleValue();
			dwgFile.setHeader("PSPACE_UCSORG", spaces6);

			double[] spaces7 = new double[3];
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces7[0] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces7[1] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces7[2] = ((Double) val.get(1)).doubleValue();
			dwgFile.setHeader("PSPACE_UCSXDIR", spaces7);

			double[] spaces8 = new double[3];
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces8[0] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces8[1] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces8[2] = ((Double) val.get(1)).doubleValue();
			dwgFile.setHeader("PSPACE_UCSYDIR", spaces8);

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			intHandle = DwgUtil.handleToInt(val);
			dwgFile.setHeader("PSPACE_UCSNAME", new Integer(intHandle));

			double[] spaces9 = new double[3];
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces9[0] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces9[1] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces9[2] = ((Double) val.get(1)).doubleValue();
			dwgFile.setHeader("MSPACE_INSBASE", spaces9);

			double[] spaces10 = new double[3];
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces10[0] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces10[1] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces10[2] = ((Double) val.get(1)).doubleValue();
			dwgFile.setHeader("MSPACE_EXTMIN", spaces10);

			double[] spaces11 = new double[3];
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces11[0] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces11[1] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces11[2] = ((Double) val.get(1)).doubleValue();
			dwgFile.setHeader("MSPACE_EXTMAX", spaces11);

			double[] spaces12 = new double[2];
			val = DwgUtil.getRawDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces12[0] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getRawDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces12[1] = ((Double) val.get(1)).doubleValue();
			dwgFile.setHeader("MSPACE_LIMMIN", spaces12);

			double[] spaces13 = new double[2];
			val = DwgUtil.getRawDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces13[0] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getRawDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces13[1] = ((Double) val.get(1)).doubleValue();
			dwgFile.setHeader("MSPACE_LIMMAX", spaces13);

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("MSPACE_ELEVATION", (Double) val.get(1));

			double[] spaces14 = new double[3];
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces14[0] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces14[1] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces14[2] = ((Double) val.get(1)).doubleValue();
			dwgFile.setHeader("MSPACE_UCSORG", spaces14);

			double[] spaces15 = new double[3];
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces15[0] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces15[1] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces15[2] = ((Double) val.get(1)).doubleValue();
			dwgFile.setHeader("MSPACE_UCSXDIR", spaces15);

			double[] spaces16 = new double[3];
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces16[0] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces16[1] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			spaces16[2] = ((Double) val.get(1)).doubleValue();
			dwgFile.setHeader("MSPACE_UCSYDIR", spaces16);

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			intHandle = DwgUtil.handleToInt(val);
			dwgFile.setHeader("MSPACE_UCSNAME", new Integer(intHandle));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMTOL", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMLIM", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMTIH", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMTOH", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMSE1", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMTSE2", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMALT", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMTOFL", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMSAH", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMTIX", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMSOXD", val.get(1));

			val = DwgUtil.getRawChar(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMALTD", val.get(1));

			val = DwgUtil.getRawChar(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMZIN", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMSD1", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMSD2", val.get(1));

			val = DwgUtil.getRawChar(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMTOLJ", val.get(1));

			val = DwgUtil.getRawChar(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMJUST", val.get(1));

			val = DwgUtil.getRawChar(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMFINT", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMUPT", val.get(1));

			val = DwgUtil.getRawChar(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMZIN", val.get(1));

			val = DwgUtil.getRawChar(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMALTZ", val.get(1));

			val = DwgUtil.getRawChar(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMALTTZ", val.get(1));

			val = DwgUtil.getRawChar(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMTAD", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMUNIT", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMAUNIT", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMDEC", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMTDEC", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMALTU", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMALTTD", val.get(1));

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			intHandle = DwgUtil.handleToInt(val);
			dwgFile.setHeader("DIMTXSTY", new Integer(intHandle));

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMSCALE", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMASZ", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMEXO", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMDLI", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMEXE", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMAND", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMDLE", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMTP", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMTM", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMTXT", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMCEN", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMSZ", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMALTF", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMLFAC", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMTVP", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMTFAC", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMGAP", val.get(1));

			val = DwgUtil.getTextString(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMPOST", val.get(1));

			val = DwgUtil.getTextString(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMAPOST", val.get(1));

			val = DwgUtil.getTextString(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMBLK", val.get(1));

			val = DwgUtil.getTextString(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMBLK1", val.get(1));

			val = DwgUtil.getTextString(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMBLK2", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMCLRD", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMCLRE", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMCLRT", val.get(1));

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			intHandle = DwgUtil.handleToInt(val);
			dwgFile.setHeader("BLOCK_CONTROL_OBJECT", new Integer(intHandle));

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			intHandle = DwgUtil.handleToInt(val);
			dwgFile.setHeader("LAYER_CONTROL_OBJECT", new Integer(intHandle));

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			intHandle = DwgUtil.handleToInt(val);
			dwgFile.setHeader("STYLE_CONTROL_OBJECT", new Integer(intHandle));

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			intHandle = DwgUtil.handleToInt(val);
			dwgFile
					.setHeader("LINETYPE_CONTROL_OBJECT",
							new Integer(intHandle));

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			intHandle = DwgUtil.handleToInt(val);
			dwgFile.setHeader("VIEW_CONTROL_OBJECT", new Integer(intHandle));

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			intHandle = DwgUtil.handleToInt(val);
			dwgFile.setHeader("UCS_CONTROL_OBJECT", new Integer(intHandle));

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			intHandle = DwgUtil.handleToInt(val);
			dwgFile.setHeader("VPORT_CONTROL_OBJECT", new Integer(intHandle));

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			intHandle = DwgUtil.handleToInt(val);
			dwgFile.setHeader("APPID_CONTROL_OBJECT", new Integer(intHandle));

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			intHandle = DwgUtil.handleToInt(val);
			dwgFile
					.setHeader("DIMSTYLE_CONTROL_OBJECT",
							new Integer(intHandle));

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			intHandle = DwgUtil.handleToInt(val);
			dwgFile.setHeader("VIEWPORT_ENTITY_HEADER", new Integer(intHandle));

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			intHandle = DwgUtil.handleToInt(val);
			dwgFile.setHeader("ACAD_GROUP_DICTIONARY", new Integer(intHandle));

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			intHandle = DwgUtil.handleToInt(val);
			dwgFile.setHeader("ACAD_MLINE_DICTIONARY", new Integer(intHandle));

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			intHandle = DwgUtil.handleToInt(val);
			dwgFile
					.setHeader("NAMED_OBJECT_DICTIONARY",
							new Integer(intHandle));

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			intHandle = DwgUtil.handleToInt(val);
			dwgFile.setHeader("PAPER_BLOCK_RECORD", new Integer(intHandle));

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			intHandle = DwgUtil.handleToInt(val);
			dwgFile.setHeader("MODEL_BLOCK_RECORD", new Integer(intHandle));

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			intHandle = DwgUtil.handleToInt(val);
			dwgFile.setHeader("LTYPE_BYLAYER", new Integer(intHandle));

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			intHandle = DwgUtil.handleToInt(val);
			dwgFile.setHeader("LTYPE_BYBLOCK", new Integer(intHandle));

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			intHandle = DwgUtil.handleToInt(val);
			dwgFile.setHeader("LTYPE_CONTINUOUS", new Integer(intHandle));

			//		    # remaing bits are unknown, and they end with possible
			//		    # padding bits so that 16-bit CRC value after the data
			//		    # is on a byte boundary - ignore them for now ...

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * It read the SECTIONS from the header of the DWG file
	 *
	 * TODO Mover esto a una clase abstracta Reader, pues es similar para DWG 15
	 * (o hacer que esta herede de DWG 15)
	 *
	 *
	 *
	 */
	protected void readDwgSectionOffsets() {
		//6 primeros bytes: version de autocad

		//7 siguientes bytes: 6 ceros y un 1 (0000001)
		//No obstante, la especificaci�n Python dice que los bytes que lee
		//con _buf.fromfile(handle, 7) son bytes de la maquina. REVISAR

		/*
		 * Asi se hace copiando integramente Python. Ver si funciona NIO byte[]
		 * chunk = {bb.get(), bb.get(), bb.get(), bb.get(), bb.get(), bb.get(),
		 * bb.get()};
		 */

		bb.position(6);

		bb.order(ByteOrder.nativeOrder());
		byte[] chunk = new byte[7];
		bb.get(chunk);

		if (chunk[0] != 0)
			logger.warn("chunk[0] != 0");
		if (chunk[1] != 0)
			logger.warn("chunk[1] != 0");
		if (chunk[2] != 0)
			logger.warn("chunk[2] != 0");
		if (chunk[3] != 0)
			logger.warn("chunk[3] != 0");
		if (chunk[4] != 0)
			logger.warn("chunk[4] != 0");
		if (chunk[5] != 0)
			logger.warn("chunk[5] != 0");
		if (chunk[6] != 0)
			logger.warn("chunk[6] != 0");

		/*
		 *
		 */

		//Siempre que en python una lectura se haga as� '<'+loquesea
		//hay que poner little_endian. Si no, se dejan los de la maquina
		// (y si aparece > se pone big endian)
		bb.order(ByteOrder.LITTLE_ENDIAN);
		byte _ub1 = bb.get();
		byte _ub2 = bb.get();
		int imageSeeker = bb.getInt();
		/*
		 * TRATAMIENTO DEL IMAGE_SEEKER: LECTURA DE IMAGENES BMP y WMF
		 * incrustadas if _image_seeker != 0: _offsets['IMAGE'] = _image_seeker
		 * _offset = handle.tell() _bmpdata, _wmfdata = read_image(handle,
		 * _image_seeker) handle.seek(_offset, 0) print "image seeker to %#x" %
		 * _image_seeker print "offset at %#x" % handle.tell()
		 */

		bb.position(19);//en realidad ya hemos leido 20 bytes

		short codePage = bb.getShort();
		int count = bb.getInt();

		for (int i = 0; i < count; i++) {

			byte rec = bb.get();
			int seek = bb.getInt();
			int size = bb.getInt();

			String sectionName = "";
			switch (rec) {
			case 0:
				sectionName = "HEADERS";
				break;

			case 1:
				sectionName = "CLASSES";
				break;

			case 2:
				sectionName = "OBJECTS";
				break;
			case 3:
				sectionName = "UNKNOWN";
				break;
			case 4:
				sectionName = "R14DATA";
				break;
			case 5:
				sectionName = "R14_REC5";
				break;
			default:
				logger.warn("Seccion con codigo desconocido:" + rec);
				break;

			}//switch
			dwgFile.addDwgSectionOffset(sectionName, seek, size);
		}//for

		//finalmente se lee el CRC
		short _crc = bb.getShort();
		logger.info("CRC=" + _crc);

		//Seguidamente aparece una seccion fija de 16 bytes
		//Esto hay que leerlo como "bytes de la maquina"
		//TODO Ver si esto afecta
		bb.order(ByteOrder.nativeOrder());

		byte[] c2 = new byte[16];
		bb.get(c2);
		if (c2[0] != 0x95)
			logger.warn("c2[0] != 0x95");
		if (c2[1] != 0xa0)
			logger.warn("c2[1] != 0xa0");
		if (c2[2] != 0x4e)
			logger.warn("c2[2] != 0x4e");
		if (c2[3] != 0x28)
			logger.warn("c2[3] != 0x28");
		if (c2[4] != 0x99)
			logger.warn("c2[4] != 0x99");
		if (c2[5] != 0x82)
			logger.warn("c2[5] != 0x82");
		if (c2[6] != 0x1a)
			logger.warn("c2[6] != 0x1a");
		if (c2[7] != 0xe5)
			logger.warn("c2[7] != 0xe5");
		if (c2[8] != 0x5e)
			logger.warn("c2[8] != 0x5e");
		if (c2[9] != 0x41)
			logger.warn("c2[9] != 0x41");
		if (c2[10] != 0xe0)
			logger.warn("c2[10] != 0xe0");
		if (c2[11] != 0x5f)
			logger.warn("c2[11] != 0x5f");
		if (c2[12] != 0x9d)
			logger.warn("c2[12] != 0x9d");
		if (c2[13] != 0x3a)
			logger.warn("c2[13] != 0x3a");
		if (c2[14] != 0x4d)
			logger.warn("c2[14] != 0x4d");
		if (c2[15] != 0x00)
			logger.warn("c2[15] != 0x00");
	}

	/*
	 * TODO Probar, y si no va, meter el codigo del lector de V15 (es SIMILAR,
	 * llevar a clase abstracta comun)
	 */
	protected void readDwgObjectOffsets() throws Exception {
		int offset = dwgFile.getDwgSectionOffset("OBJECTS");
		bb.position(offset);
		while (true) {
			bb.order(ByteOrder.BIG_ENDIAN);
			short size = bb.getShort();
			if (size == 2)
				break;
			//TODO Cuando en Python no pone < ni >, es nativeOrder o
			// little_endian??
			//			bb.order(ByteOrder.LITTLE_ENDIAN);
			bb.order(ByteOrder.nativeOrder());
			byte[] dataBytes = new byte[size];
			bb.get(dataBytes);

			/*
			 * TODO Creo q esto no hace falta, al estar en bytes nativos (no en
			 * LITTLE_ENDIAN) int[] data =
			 * DwgUtil.bytesToMachineBytes(dataBytes);
			 */
			int[] data = DwgUtil.toIntArray(dataBytes);
			/*
			 * the spec says 'last_handle' and 'last_loc' are initialized
			 * outside the outer for loop - postings on OpenDWG forum say these
			 * variables must be initialized for each section
			 */
			int lastHandle = 0;
			int lastLoc = 0;
			int bitPos = 0;
			int bitMax = (size - 2) * 8;//se quitan 2 bytes por el CRC final
			while (bitPos < bitMax) {
				List v = DwgUtil.getModularChar(data, bitPos);
				bitPos = ((Integer) v.get(0)).intValue();
				lastHandle = lastHandle + ((Integer) v.get(1)).intValue();
				v = DwgUtil.getModularChar(data, bitPos);
				bitPos = ((Integer) v.get(0)).intValue();
				lastLoc = lastLoc + ((Integer) v.get(1)).intValue();
				dwgFile.addDwgObjectOffset(lastHandle, lastLoc);
			}//while
		}//while
	}

	protected void readDwgClasses() throws Exception {
		int offset = dwgFile.getDwgSectionOffset("CLASSES");
		bb.position(offset);
		//1� leemos el sentinnel inicial
		bb.order(ByteOrder.nativeOrder());
		byte[] sentinel = new byte[16];
		bb.get(sentinel);
		if (sentinel[0] != 0x8d)
			logger.warn("sentinel[0] != 0x8d");
		if (sentinel[1] != 0xa1)
			logger.warn("sentinel[1] != 0xa1");
		if (sentinel[2] != 0xc4)
			logger.warn("sentinel[2] != 0xc4");
		if (sentinel[3] != 0xb8)
			logger.warn("sentinel[3] != 0xb8");
		if (sentinel[4] != 0xc4)
			logger.warn("sentinel[4] != 0xc4");
		if (sentinel[5] != 0xa9)
			logger.warn("sentinel[5] != 0xa9");
		if (sentinel[6] != 0xf8)
			logger.warn("sentinel[6] != 0xf8");
		if (sentinel[7] != 0xc5)
			logger.warn("sentinel[7] != 0xc5");
		if (sentinel[8] != 0xc0)
			logger.warn("sentinel[8] != 0xc0");
		if (sentinel[9] != 0xdc)
			logger.warn("sentinel[9] != 0xdc");
		if (sentinel[10] != 0xf4)
			logger.warn("sentinel[10] != 0xf4");
		if (sentinel[11] != 0x5f)
			logger.warn("sentinel[11] != 0x5f");
		if (sentinel[12] != 0xe7)
			logger.warn("sentinel[12] != 0xe7");
		if (sentinel[13] != 0xcf)
			logger.warn("sentinel[13] != 0xcf");
		if (sentinel[14] != 0xb6)
			logger.warn("sentinel[14] != 0xb6");
		if (sentinel[15] != 0x8a)
			logger.warn("sentinel[15] != 0x8a");

		bb.order(ByteOrder.LITTLE_ENDIAN);
		int size = bb.getInt();
		byte[] data = new byte[size];
		bb.get(data);
		int[] intData = DwgUtil.toIntArray(data);
		short crc = bb.getShort();

		int maxBit = size * 8;
		int bitPos = 0;
		List val = null;
		while ((bitPos + 8) < maxBit) {
			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			int classNum = ((Integer) val.get(1)).intValue();

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			int version = ((Integer) val.get(1)).intValue();

			val = DwgUtil.getTextString(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			String appName = (String) val.get(1);

			val = DwgUtil.getTextString(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			String cPlusPlusName = (String) val.get(1);

			val = DwgUtil.getTextString(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			String dxfName = (String) val.get(1);

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			boolean isZombie = ((Boolean) val.get(1)).booleanValue();

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			int id = ((Integer) val.get(1)).intValue();

			DwgClass dwgClass = new DwgClass(classNum, version, appName,
					cPlusPlusName, dxfName, isZombie, id);
			dwgFile.addDwgClass(dwgClass);

		}//while
		//		Por ultimo, el sentinnel final
		bb.order(ByteOrder.nativeOrder());
		byte[] lastSentinnel = new byte[16];
		bb.get(lastSentinnel);
		if (lastSentinnel[0] != 0x72)
			logger.warn("lastSentinnel[0] != 0x72");
		if (lastSentinnel[1] != 0x5e)
			logger.warn("lastSentinnel[1] != 0x5e");
		if (lastSentinnel[2] != 0x3b)
			logger.warn("lastSentinnel[2] != 0x3b");
		if (lastSentinnel[3] != 0x47)
			logger.warn("lastSentinnel[3] != 0x47");
		if (lastSentinnel[4] != 0x3b)
			logger.warn("lastSentinnel[4] != 0x3b");
		if (lastSentinnel[5] != 0x56)
			logger.warn("lastSentinnel[5] != 0x56");
		if (lastSentinnel[6] != 0x07)
			logger.warn("lastSentinnel[6] != 0x07");
		if (lastSentinnel[7] != 0x3a)
			logger.warn("lastSentinnel[7] != 0x3a");
		if (lastSentinnel[8] != 0x3f)
			logger.warn("lastSentinnel[8] != 0x3f");
		if (lastSentinnel[9] != 0x23)
			logger.warn("lastSentinnel[9] != 0x23");
		if (lastSentinnel[10] != 0x0b)
			logger.warn("lastSentinnel[10] != 0x0b");
		if (lastSentinnel[11] != 0xa0)
			logger.warn("lastSentinnel[11] != 0xa0");
		if (lastSentinnel[12] != 0x18)
			logger.warn("lastSentinnel[12] != 0x18");
		if (lastSentinnel[13] != 0x30)
			logger.warn("lastSentinnel[13] != 0x30");
		if (lastSentinnel[14] != 0x49)
			logger.warn("lastSentinnel[14] != 0x49");
		if (lastSentinnel[15] != 0x75)
			logger.warn("lastSentinnel[15] != 0x75");
	}

	/**
	 * Reads all the object referenced in the object map section of the DWG file
	 * (using their object file obsets)
	 */
	protected void readDwgObjects() {
		logger.info("Vamos a leer " + dwgFile.getDwgObjectOffsets().size()
				+ " objetos");

		for (int i = 0; i < dwgFile.getDwgObjectOffsets().size(); i++) {
			DwgObjectOffset doo = (DwgObjectOffset) dwgFile
					.getDwgObjectOffsets().get(i);
			DwgObject obj = readDwgObject(doo.getOffset(), i);
			if (obj != null && obj.getClass() != DwgObject.class) {
				dwgFile.addDwgObject(obj);
			}
		}//for
	}

	/**
	 * Return a dwg object from its index in the dwg file
	 *
	 * @param index
	 *            of the requested dwg object in the dwg file
	 *
	 */
	public DwgObject getDwgObjectByIndex(int index) {
		DwgObjectOffset doo = (DwgObjectOffset) dwgFile.getDwgObjectOffsets()
				.get(index);
		return readDwgObject(doo.getOffset(), index);
	}

	void dumpEntity(int[] intData){
		String outtxt= "int[] data = new int[]{";
		for(int z = 0; z < intData.length -1; z++){
			outtxt += intData[z] + ",";
		}
		outtxt += intData[intData.length -1] + "}";
		logger.info(outtxt);
	}

	/**
	 * Reads a dwg drawing entity (dwg object) given its offset in the file
	 *
	 * @param offset
	 *            offset of the dwg object in the file
	 * @param index
	 *            order of the entity in the objects map (1, 2, etc.)
	 */

	protected DwgObject readDwgObject(int offset, int index) {
		DwgObject obj = null;
		try {
			bb.position(offset);
			int size = DwgUtil.getModularShort(bb);
			bb.order(ByteOrder.nativeOrder());
			byte[] data = new byte[size];
			bb.get(data);
			int[] intData = DwgUtil.toIntArray(data);
			int bitPos = 0;
			List val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			int type = ((Integer) val.get(1)).intValue();

			obj = DwgObjectFactory.getInstance().create(type, index);
			if (obj == null) {
				if (type >= 500) {
					int newIndex = type - 500;
					if (newIndex < (dwgFile.getDwgClasses().size() - 1)) {
						DwgClass dwgClass = (DwgClass) dwgFile.getDwgClasses()
								.get(newIndex);
						String dxfEntityName = dwgClass.getDxfName();
						obj = DwgObjectFactory.getInstance().create(
								dxfEntityName, index);
						if (obj == null) {
							logger.info(dxfEntityName
									+ " todavia no est� implementado");
							return null;
						}//if
					}//if newIndex
					else{
						return null;
					}
				}else {
					logger.info("Encontrado tipo " + type);
					return null;
				}
			}//if obj == null
			obj.setVersion(dwgFile.getDwgVersion());

			DwgHandleReference hr = new DwgHandleReference();
			bitPos = hr.read(intData, bitPos);
			obj.setHandle(hr);

			//TODO Si funciona, mover el metodo de esta clase a DwgUtil
			val = readExtendedData(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			List extData = (List) val.get(1);
			obj.setExtendedData(extData);

			//Graphics data
			boolean gflag = false;
			gflag = obj.isGraphicsFlag();
			if (gflag) {
				val = DwgUtil.testBit(intData, bitPos);
				bitPos = ((Integer) val.get(0)).intValue();
				boolean hasGraphicsData = ((Boolean) val.get(1))
						.booleanValue();
				//si hay imagen asociada, se lee por completo
				if (hasGraphicsData) {
					val = DwgUtil.getRawLong(intData, bitPos);
					bitPos = ((Integer) val.get(0)).intValue();
					size = ((Integer) val.get(1)).intValue();
					int bgSize = size * 8;
					Integer giData = (Integer) DwgUtil.getBits(intData,
							bgSize, bitPos);
					obj.setGraphicData(giData.intValue());
					bitPos = bitPos + bgSize;
				}
			}//if gflag

			//size in bits
			val = DwgUtil.getRawLong(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			int sizeInBits = ((Integer) val.get(1)).intValue();
			obj.setSizeInBits(sizeInBits);

			readSpecificObject(obj, intData, bitPos);

			return obj;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * TODO Esto est� pesimamente dise�ado. Cada objeto DwgObject debe tener un
	 * metodo readSpecificObject(data,bitPos)
	 *
	 */
	protected void readSpecificObject(DwgObject obj, int[] data, int bitPos)
			throws Exception {
		DwgObjectReaderPool pool = DwgObjectReaderPool.getInstance();
		IDwgObjectReader reader = pool.get(obj, "1314");
		if (reader != null) {
			reader.setFileReader(this);
			reader.readSpecificObj(data, bitPos, obj);
		} else {
			logger.info("No se ha implementado la lectura de "
					+ obj.getClass().getName() + ", code=" + obj.getType());
		}
	}

	/*
	 * TODO En DwgUtil se dice que este metodo tiene graves errores. Intento de
	 * reimplementarlo a partir del codigo Python original
	 */
	List readExtendedData(int[] data, int bitPos) throws Exception {
		List solution = new ArrayList();
		//TODO Ver si el array est� bien, o hay que cambiarlo por un
		// stringbuffer
		List extendedData = new ArrayList();
		int size;
		List val;
		while (true) {//TODO VER SI HAY PROBLEMAS DE NO SALIDA

			val = DwgUtil.getBitShort(data, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			size = ((Integer) val.get(1)).intValue();
			if (size == 0)
				break;

			DwgHandleReference hr = new DwgHandleReference();
			bitPos = hr.read(data, bitPos);
//			logger.debug("Handle del EXTENDED ENTITY DATA:" + hr.getCode()
//					+ " " + hr.getOffset());

			int count = 0;
			while (count < size) {
				val = DwgUtil.getRawChar(data, bitPos);
				bitPos = ((Integer) val.get(0)).intValue();
				int codeByte = ((Integer) val.get(1)).intValue();
				count++;
				if (codeByte == 0x0) {
					val = DwgUtil.getRawChar(data, bitPos);
					bitPos = ((Integer) val.get(0)).intValue();
					int slen = ((Integer) val.get(1)).intValue();
					val = DwgUtil.getRawShort(data, bitPos);
					bitPos = ((Integer) val.get(0)).intValue();
					int codePage = ((Integer) val.get(1)).intValue();
//					logger.debug("Extended Data (0x0): code page = "
//									+ codePage);
					char[] chars = new char[slen];
					for (int i = 0; i < slen; i++) {
						val = DwgUtil.getRawChar(data, bitPos);
						bitPos = ((Integer) val.get(0)).intValue();
						chars[i] = (char) ((Integer) val.get(1)).intValue();
					}//for
					String str = new String(chars);
//					logger.debug("Chars:" + str);
					extendedData.add(str);
					count += (3 + slen);
				} else if (codeByte == 0x1) {
					logger.debug("Invalid extended data code byte: 0x1");
				} else if (codeByte == 0x2) {
					val = DwgUtil.getRawChar(data, bitPos);
					bitPos = ((Integer) val.get(0)).intValue();
					int character = ((Integer) val.get(1)).intValue();
					if (character == 0x0)
						extendedData.add("{");
					else if (character == 0x1)
						extendedData.add("}");
					else {
						logger.warn("Invalid extended data char:" + character);
					}
					count++;
				} else if (codeByte == 0x3) {
					char[] chars = new char[8];
					for (int i = 0; i < 8; i++) {
						val = DwgUtil.getRawChar(data, bitPos);
						bitPos = ((Integer) val.get(0)).intValue();
						chars[i] = (char) ((Integer) val.get(1)).intValue();
					}
					String str = new String(chars);
//					logger.debug("Chars:" + str);
					extendedData.add(str);
					count += 8;
				} else if (codeByte == 0x4) {
					//binary chunk in extended data
					val = DwgUtil.getRawChar(data, bitPos);
					bitPos = ((Integer) val.get(0)).intValue();
					int length = ((Integer) val.get(1)).intValue();
					char[] chars = new char[length];
					for (int i = 0; i < length; i++) {
						val = DwgUtil.getRawChar(data, bitPos);
						bitPos = ((Integer) val.get(0)).intValue();
						chars[i] = (char) ((Integer) val.get(1)).intValue();
					}
					String str = new String(chars);
//					logger.debug("Chars:" + str);
					extendedData.add(str);
					count += (1 + length);
				} else if (codeByte == 0x5) {
					//entity handle reference
					char[] chars = new char[8];
					for (int i = 0; i < 8; i++) {
						val = DwgUtil.getRawChar(data, bitPos);
						bitPos = ((Integer) val.get(0)).intValue();
						chars[i] = (char) ((Integer) val.get(1)).intValue();
					}
					String str = new String(chars);
//					logger.debug("Chars:" + str);
					extendedData.add(str);
					count += 8;
				} else if ((codeByte >= 0xa) && (codeByte <= 0xd)) {
					//three doubles
					double[] dValues = new double[3];
					val = DwgUtil.getRawDouble(data, bitPos);
					bitPos = ((Integer) val.get(0)).intValue();
					dValues[0] = ((Double) val.get(1)).doubleValue();

					val = DwgUtil.getRawDouble(data, bitPos);
					bitPos = ((Integer) val.get(0)).intValue();
					dValues[1] = ((Double) val.get(1)).doubleValue();

					val = DwgUtil.getRawDouble(data, bitPos);
					bitPos = ((Integer) val.get(0)).intValue();
					dValues[2] = ((Double) val.get(1)).doubleValue();

//					logger.debug("Doubles:" + dValues);
					extendedData.add(dValues);
					count += 24;
				} else if ((codeByte >= 0x28) && (codeByte <= 0x2a)) {
					//one double
					val = DwgUtil.getRawDouble(data, bitPos);
					bitPos = ((Integer) val.get(0)).intValue();
					double value = ((Double) val.get(1)).doubleValue();
//					logger.debug("Double value:" + value);
					extendedData.add(val.get(1));
					count += 8;
				} else if (codeByte == 0x46) {
					//a short value
					val = DwgUtil.getRawShort(data, bitPos);
					bitPos = ((Integer) val.get(0)).intValue();
					int value = ((Integer) val.get(1)).intValue();
//					logger.debug("Short value:" + value);
					extendedData.add(val.get(1));
					count += 2;
				} else if (codeByte == 0x47) {
					//int value
					val = DwgUtil.getRawLong(data, bitPos);
					bitPos = ((Integer) val.get(0)).intValue();
					int value = ((Integer) val.get(1)).intValue();
//					logger.debug("Int value:" + value);
					extendedData.add(val.get(1));
					count += 4;
				} else {
					logger.debug("Unexpected code byte in EXTENDED DATA "
							+ codeByte);
				}
			}//while
		}//while
		solution.add(new Integer(bitPos));
		solution.add(extendedData);
		return solution;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.jdwglib.dwg.readers.IDwgFileReader#readObjectHeader(int[],
	 *      int, com.iver.cit.jdwglib.dwg.DwgObject)
	 */
	public int readObjectHeader(int[] data, int offset, DwgObject dwgObject) {
		int bitPos = offset;
		Integer mode = (Integer) DwgUtil.getBits(data, 2, bitPos);
		bitPos += 2;
		dwgObject.setMode(mode.intValue());

		List val = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		int rnum = ((Integer) val.get(1)).intValue();
		dwgObject.setNumReactors(rnum);


		val = DwgUtil.testBit(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		boolean isLyrByLineType = ((Boolean) val.get(1)).booleanValue();
		//TODO En la 15 es un flag, no un boolean. REVISAR
		dwgObject.setLyrByLineType(isLyrByLineType);

		val = DwgUtil.testBit(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		boolean noLinks = ((Boolean) val.get(1)).booleanValue();
		dwgObject.setNoLinks(noLinks);

		val = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		int color = ((Integer) val.get(1)).intValue();
		dwgObject.setColor(color);

		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		float ltscale = ((Double) val.get(1)).floatValue();
		//TODO tampoco se setea en la 15 (en su lugar, un flag de ints)
		val = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		int invis = ((Integer) val.get(1)).intValue();

		return bitPos;
	}

	/*
	 *
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.jdwglib.dwg.readers.IDwgFileReader#readObjectTailer(int[],
	 *      int, com.iver.cit.jdwglib.dwg.DwgObject)
	 */
	public int readObjectTailer(int[] data, int offset, DwgObject dwgObject)
			throws RuntimeException, CorruptedDwgEntityException {
		int bitPos = offset;
		List val = null;

		/*
		 * Subentity ref handle. Esto se aplica sobre VERTEX, ATTRIB, SEQEND
		 */
		if (dwgObject.getMode() == 0x0) {
			DwgHandleReference subEntityHandle = new DwgHandleReference();
			bitPos = subEntityHandle.read(data, bitPos);
			dwgObject.setSubEntityHandle(subEntityHandle);
		}

		/*
		 * Reactors handles DwgObject
		 */
		DwgHandleReference reactorHandle;
		for (int i = 0; i < dwgObject.getNumReactors(); i++) {
			reactorHandle = new DwgHandleReference();
			bitPos = reactorHandle.read(data, bitPos);
			dwgObject.addReactorHandle(reactorHandle);
		}

		/*
		 * XDICOBJHANDLE
		 */
		DwgHandleReference xDicObjHandle = new DwgHandleReference();
		bitPos = xDicObjHandle.read(data, bitPos);
		dwgObject.setXDicObjHandle(xDicObjHandle);

		//	  TODO Hasta aqui igual que en la 15

		/*
		 * Layer Handle code
		 */

		DwgHandleReference layerHandle = new DwgHandleReference();
		bitPos = layerHandle.read(data, bitPos);
		dwgObject.setLayerHandle(layerHandle);

		if (!dwgObject.isLyrByLineType()) {
			DwgHandleReference lineTypeHandle = new DwgHandleReference();
			bitPos = lineTypeHandle.read(data, bitPos);
			dwgObject.setLineTypeHandle(lineTypeHandle);
		}
		String cadena = "";
		if (!dwgObject.isNoLinks()) {

			/*
			 * Previous Handle
			 */
			DwgHandleReference previousHandle = new DwgHandleReference();
			bitPos = previousHandle.read(data, bitPos);
			dwgObject.setPreviousHandle(previousHandle);

			/*
			 * Next Handle
			 */
			DwgHandleReference nextHandle = new DwgHandleReference();
			bitPos = nextHandle.read(data, bitPos);
			dwgObject.setNextHandle(nextHandle);
		}
		return bitPos;
	}

}