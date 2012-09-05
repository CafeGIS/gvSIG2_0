/*
 * Created on 09-ene-2007
 *
 * gvSIG. Sistema de Informaciï¿½n Geogrï¿½fica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibï¿½ï¿½ez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */

package org.gvsig.dwg.lib.readers;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import org.gvsig.dwg.lib.CorruptedDwgEntityException;
import org.gvsig.dwg.lib.DwgClass2004;
import org.gvsig.dwg.lib.DwgFile;
import org.gvsig.dwg.lib.DwgHandleReference;
import org.gvsig.dwg.lib.DwgObject;
import org.gvsig.dwg.lib.DwgObjectFactory;
import org.gvsig.dwg.lib.DwgObjectOffset;
import org.gvsig.dwg.lib.DwgUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @author alzabord
 *
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class DwgFileVR2004Reader implements IDwgFileReader {

	protected DwgFile dwgFile;

	protected ByteBuffer bb;

	static byte[] magicNumber = null;

	private String version = "";

	private byte[] unknowSixBytes;

	private byte[] threeBytes0;

	private byte[] unknowTwoBytes;

	private byte[] eightyTwoBytes0;

	private byte[] encryptedData;

	private byte[] decryptedData;

	private String fileID = "";

	private int rootTreeNodeGap;

	private int lowermostLeftTreeNodeGap;

	private int lowermostRightTreeNodeGap;

	private int lastSectionId; // Last Section identifier

	private int lastSectionAddress; // Last Section Address

	private int SecondHeaderAddress; // Second Header Address

	private int gapAmount; // Number of Gaps

	private int sectionAmount; // Number of Sections

	private int sectionMapId; // Section Map identifier

	private int sectionMapAddress; // Section Map address

	private int sectionInfoId; // Section Info identifier

	private int sectionInfoIndex = 0; // Section Info identifier into the

	// Sections Array

	private int sectionArraySize;

	private int gapArraySize;

	private byte[] generatedSequence;

	private Section[] sections;

	private Gap[] gaps;

	static final int TYPE_SECTION_MAP = 1;

	static final int TYPE_SECTION_INFO = 2;

	private static Logger logger = LoggerFactory.getLogger(DwgFileVR2004Reader.class.getName());

	/**
	 * Reads the DWG version 2004 format
	 *
	 * @param dwgFile
	 *            Represents the DWG file that we want to read
	 * @throws IOException
	 *             When DWG file path is wrong
	 */
	public void read(DwgFile dwgFile, ByteBuffer bb) throws IOException {

		this.dwgFile = dwgFile;
		this.bb = bb;

		readDwgR2004FileHeader(bb);
		readDwgR2004SectionMap(bb);
		readDwgR2004InfoSection(bb);
		readDwgR2004Headers(bb);
		readDwgR2004Classes(bb);
		readDwgR2004ObjectOffsets(bb);

		readDwgR2004Objects();

	}

	protected void readDwgR2004InfoSection(ByteBuffer bb) {
		int numDescriptions;

		int sizeOfSection;
		int numSections;
		int maxDecompressedSize;
		int compressed;
		int sectionType;
		int encrypted;
		String sectionName = "";

		int sectionNumber;
		int dataSize;
		int startOffset;

		char c;
		int pos;

		if (sectionInfoIndex == 0) {
			logger.error("No se ha encontrado la Section Info en el array de secciones.");
		}

		pos = sections[sectionInfoIndex].getAddress(); // we get the address for the Section Info

		bb.position(pos);

		ByteBuffer dData = readSystemSection(bb);
		dData.order(ByteOrder.LITTLE_ENDIAN);
		dData.position(0);

		/*
		 * We start reading the Section Info once decompressed We will find data
		 * blocks depending on numDescriptions value
		 */
		numDescriptions = dData.getInt();
		dData.getInt(); // 0x02
		dData.getInt(); // 0x00007400
		dData.getInt(); // 0x00
		dData.getInt(); // unknown

		for (int i = 0; i < numDescriptions; i++) {
			sizeOfSection = dData.getInt();
			dData.getInt(); // unknown
			numSections = dData.getInt();
			maxDecompressedSize = dData.getInt();
			dData.getInt(); // unknown
			compressed = dData.getInt();
			sectionType = dData.getInt();
			encrypted = dData.getInt();
			sectionName = "";
			for (int j = 0; j < 64; j++) {
				c = (char) dData.get();
				sectionName = sectionName + c;
			}

			for (int k = 0; k < numSections; k++) {
				sectionNumber = dData.getInt(); // index into the Sections Array
				dataSize = dData.getInt();
				startOffset = dData.getInt();
				dData.getInt(); // unknown


				// We set the name for the Section, so we can localize it by the name
				for (int j = 0; j < sectionAmount; j++) {
					Section section = sections[j];
					if (section.getNumber() == sectionNumber) {
						section.setName(sectionName);
						section.setSizeOfLogicalSection(sizeOfSection);
						section.setDataSize(dataSize);
						section.setMaxDecompressedSize(maxDecompressedSize);
						section.setCompressed(compressed);
						section.setType(sectionType);
						section.setEncrypted(encrypted);
						section.setStartOffset(startOffset);
					}
				}

			}
		}
	}

	/**
	 * This function reads the header section of the file, with the encrypted
	 * data
	 *
	 * @param bb
	 *            ByteBuffer containing the opened DWG file
	 */
	protected void readDwgR2004FileHeader(ByteBuffer bb) {

		logger.info("STARTING TO READ DWG FILE.");
		bb.order(ByteOrder.LITTLE_ENDIAN);
		for (int i = 0; i < 6; i++) {
			version = version + (char) bb.get();
		}
		if (!version.equalsIgnoreCase("AC1018")) {
			logger.error("Error en la version: " + version);
		}

		/*
		 * OpenAlliance documentation says we will read 6 0x00 bytes but the
		 * sixth one is always 0x4C
		 */
		unknowSixBytes = new byte[6];
		int pos = 6;
		for (int i = 0; i < 5; i++) {
			unknowSixBytes[i] = bb.get();
			if (unknowSixBytes[i] != 0) {
				logger.warn("(unknowSixBytes) En la posicion 0x"
						+ Integer.toHexString(pos + i)
						+ " deberÃ­a haber un 0, pero hay un 0x"
						+ Integer.toHexString(unknowSixBytes[i]));
			}
		}
		pos = 0xB;
		unknowSixBytes[5] = bb.get();
		if (unknowSixBytes[5] != 0x4C) {
			logger.warn("(unknowSixBytes) En la posicion 0x"
					+ Integer.toHexString(pos)
					+ " deberÃ­a haber un 0x4C, pero hay un 0x"
					+ Integer.toHexString(unknowSixBytes[5]));
		}

		bb.get(); // Unknown Byte
		bb.getInt(); // previewAddress
		bb.get(); // dwgVer
		bb.get(); // maintReleaseVer
		bb.getShort(); // codepage

		/*
		 * OpenAlliance documentation says we will read 3 0x00 bytes but what we
		 * always find is 0x00, 0x19 and then 0x4C
		 */
		threeBytes0 = new byte[3];
		pos = 0x15;
		bb.get(threeBytes0);
		if (threeBytes0[0] != 0x0) {
			logger.warn("Posible error leyendo la cabecera del archivo DWG: " +
					"En la posicion 0x" + Integer.toHexString(pos)
					+ " debería haber un 0x0, pero hay un 0x"
					+ Integer.toHexString(threeBytes0[0]));
		}
		pos = 0x16;
		if (threeBytes0[1] != 0x19) {
			logger.warn("Posible error leyendo la cabecera del archivo DWG: " +
					"En la posicion 0x" + Integer.toHexString(pos)
					+ " debería haber un 0x19, pero hay un 0x"
					+ Integer.toHexString(threeBytes0[1]));
		}
		pos = 0x17;
		if (threeBytes0[2] != 0x4C) {
			logger.warn("Posible error leyendo la cabecera del archivo DWG: " +
					"En la posicion 0x" + Integer.toHexString(pos)
					+ " debería haber un 0x4C, pero hay un 0x"
					+ Integer.toHexString(threeBytes0[2]));
		}

		bb.getInt(); // security Type
		bb.getInt(); // unknownLong
		bb.getInt(); // dwgPropertyAddr
		bb.getInt(); // vbaProjectAddr
		bb.getInt(); // 0x00000080

		pos = 0x2C;

		unknowTwoBytes = new byte[2];
		for (int i = 0; i < 2; i++) {
			unknowTwoBytes[i] = bb.get();
		}
		pos = 0x2E;
		eightyTwoBytes0 = new byte[82];
		for (int i = 0; i < eightyTwoBytes0.length; i++) {
			eightyTwoBytes0[i] = bb.get();
			if (eightyTwoBytes0[i] != 0) {
				logger.warn("Posible error leyendo la cabecera del archivo DWG: " +
						"(eightyTwoBytes0) En la posicion 0x"
						+ Integer.toHexString(pos + i)
						+ " debería haber un 0, pero hay un 0x"
						+ Integer.toHexString(eightyTwoBytes0[i]));
			}
		}

		int len = 0x6C;
		encryptedData = new byte[len];
		decryptedData = new byte[len];
		if (magicNumber == null) {
			DwgFileVR2004Reader.setMagicNumber();
		}

		// decryptedData will contain the decrypted sequence after xor'ing the
		// encrypted data
		for (int i = 0; i < len; i++) {
			encryptedData[i] = bb.get();
			decryptedData[i] = (byte) (encryptedData[i] ^ magicNumber[i]);
		}

		ByteBuffer decryptedDataBB = ByteBuffer.allocate(len);
		decryptedDataBB.put(decryptedData);
		decryptedDataBB.position(0);
		decryptedDataBB.order(ByteOrder.LITTLE_ENDIAN);

		// we start intercepting the decrypted variables
		for (int i = 0; i < 12; i++) {
			fileID = fileID + (char) decryptedDataBB.get();
		}
		decryptedDataBB.getInt(); // 0x00

		decryptedDataBB.getInt(); // 0x6C

		decryptedDataBB.getInt(); // 0x04

		rootTreeNodeGap = decryptedDataBB.getInt();
		lowermostLeftTreeNodeGap = decryptedDataBB.getInt();
		lowermostRightTreeNodeGap = decryptedDataBB.getInt();
		decryptedDataBB.getInt(); // unknown
		lastSectionId = decryptedDataBB.getInt();

		lastSectionAddress = decryptedDataBB.getInt();
		decryptedDataBB.getInt(); // 0x00

		SecondHeaderAddress = decryptedDataBB.getInt();
		decryptedDataBB.getInt(); // 0x00

		gapAmount = decryptedDataBB.getInt();
		sectionAmount = decryptedDataBB.getInt(); // Very important: this is
		// the number of sections in the file
		decryptedDataBB.getInt(); // 0x20
		decryptedDataBB.getInt(); // 0x80
		decryptedDataBB.getInt(); // 0x40

		sectionMapId = decryptedDataBB.getInt();
		sectionMapAddress = decryptedDataBB.getInt() + 0x100; // the Section Map Address
		decryptedDataBB.getInt(); // 0x00
		sectionInfoId = decryptedDataBB.getInt(); // Very important: the id of the Section Info
		sectionArraySize = decryptedDataBB.getInt();
		gapArraySize = decryptedDataBB.getInt();
		decryptedDataBB.getInt(); // CRC
		len = 0x14;
		generatedSequence = new byte[len];
		bb.get(generatedSequence);
		// Done reading the encrypted variables

	}

	/*************************************************************************************
	 * **********************************************************************/

	/**
	 * Read and store all the header variables into the
	 * <code> HeaderVars </code> Map
	 *
	 * @param bb
	 *            ByteBuffer containing the opened DWG file
	 */
	private void readDwgR2004Headers(ByteBuffer bb) {

		/*
		 * NOTA (Paco) He encontrado un sitio web con explicacion de Â¿todas?
		 * estas variables: http://www.hispacad.com/variables
		 */

		ByteBuffer decompressedBB = readSection(bb, "AcDb:Header");

		byte[] sentinel = new byte[16];
		decompressedBB.order(ByteOrder.nativeOrder());
		decompressedBB.position(0);
		decompressedBB.get(sentinel); // we get the sentinel that ALWAYS
		// appears before the data

		if ((sentinel[0] & 0xFF) != 0xcf)
			logger.warn("sentinel[0] != 0xcf found " + (sentinel[0] & 0xFF));
		if ((sentinel[1] & 0xFF) != 0x7b)
			logger.warn("sentinel[1] != 0x7b found " + (sentinel[1] & 0xFF));
		if ((sentinel[2] & 0xFF) != 0x1f)
			logger.warn("sentinel[2] != 0x1f found " + (sentinel[2] & 0xFF));
		if ((sentinel[3] & 0xFF) != 0x23)
			logger.warn("sentinel[3] != 0x23 found " + (sentinel[3] & 0xFF));
		if ((sentinel[4] & 0xFF) != 0xfd)
			logger.warn("sentinel[4] != 0xfd found " + (sentinel[4] & 0xFF));
		if ((sentinel[5] & 0xFF) != 0xde)
			logger.warn("sentinel[5] != 0xde found " + (sentinel[5] & 0xFF));
		if ((sentinel[6] & 0xFF) != 0x38)
			logger.warn("sentinel[6] != 0x38 found " + (sentinel[6] & 0xFF));
		if ((sentinel[7] & 0xFF) != 0xa9)
			logger.warn("sentinel[7] != 0xa9 found " + (sentinel[7] & 0xFF));
		if ((sentinel[8] & 0xFF) != 0x5f)
			logger.warn("sentinel[8] != 0x5f found " + (sentinel[8] & 0xFF));
		if ((sentinel[9] & 0xFF) != 0x7c)
			logger.warn("sentinel[9] != 0x7c found " + (sentinel[9] & 0xFF));
		if ((sentinel[10] & 0xFF) != 0x68)
			logger.warn("sentinel[10] != 0x68 found " + (sentinel[10] & 0xFF));
		if ((sentinel[11] & 0xFF) != 0xb8)
			logger.warn("sentinel[11] != 0xb8 found " + (sentinel[11] & 0xFF));
		if ((sentinel[12] & 0xFF) != 0x4e)
			logger.warn("sentinel[12] != 0x4e found " + (sentinel[12] & 0xFF));
		if ((sentinel[13] & 0xFF) != 0x6d)
			logger.warn("sentinel[13] != 0x6d found " + (sentinel[13] & 0xFF));
		if ((sentinel[14] & 0xFF) != 0x33)
			logger.warn("sentinel[14] != 0x33 found " + (sentinel[14] & 0xFF));
		if ((sentinel[15] & 0xFF) != 0x5f)
			logger.warn("sentinel[15] != 0x5f found " + (sentinel[15] & 0xFF));

		// we start working with data that comes after the beginning sentinel
		decompressedBB.order(ByteOrder.LITTLE_ENDIAN);
		int size = decompressedBB.getInt(); // this size is never correct

		decompressedBB.order(ByteOrder.nativeOrder());
		byte[] data = new byte[size];
		decompressedBB.get(data);

		int[] intData = DwgUtil.toIntArray(data);

		bb.order(ByteOrder.LITTLE_ENDIAN);
		decompressedBB.getShort(); // CRC

		decompressedBB.order(ByteOrder.nativeOrder());
		byte[] lastSentinnel = new byte[16]; // we know it comes there because we already found it
		decompressedBB.get(lastSentinnel);

		if ((lastSentinnel[0] & 0xFF) != 0x30)
			logger.warn("lastSentinnel[0] != 0x30 , is: " + lastSentinnel[0]);
		if ((lastSentinnel[1] & 0xFF) != 0x84)
			logger.warn("lastSentinnel[1] != 0x84 , is: " + lastSentinnel[1]);
		if ((lastSentinnel[2] & 0xFF) != 0xe0)
			logger.warn("lastSentinnel[2] != 0xe0 , is: " + lastSentinnel[2]);
		if ((lastSentinnel[3] & 0xFF) != 0xdc)
			logger.warn("lastSentinnel[3] != 0xdc , is: " + lastSentinnel[3]);
		if ((lastSentinnel[4] & 0xFF) != 0x02)
			logger.warn("lastSentinnel[4] != 0x02 , is: " + lastSentinnel[4]);
		if ((lastSentinnel[5] & 0xFF) != 0x21)
			logger.warn("lastSentinnel[5] != 0x21 , is: " + lastSentinnel[5]);
		if ((lastSentinnel[6] & 0xFF) != 0xc7)
			logger.warn("lastSentinnel[6] != 0xc7 , is: " + lastSentinnel[6]);
		if ((lastSentinnel[7] & 0xFF) != 0x56)
			logger.warn("lastSentinnel[7] != 0x56 , is: " + lastSentinnel[7]);
		if ((lastSentinnel[8] & 0xFF) != 0xa0)
			logger.warn("lastSentinnel[8] != 0xa0 , is: " + lastSentinnel[8]);
		if ((lastSentinnel[9] & 0xFF) != 0x83)
			logger.warn("lastSentinnel[9] != 0x83 , is: " + lastSentinnel[9]);
		if ((lastSentinnel[10] & 0xFF) != 0x97)
			logger.warn("lastSentinnel[10] != 0x97 , is: " + lastSentinnel[10]);
		if ((lastSentinnel[11] & 0xFF) != 0x47)
			logger.warn("lastSentinnel[11] != 0x47 , is: " + lastSentinnel[11]);
		if ((lastSentinnel[12] & 0xFF) != 0xb1)
			logger.warn("lastSentinnel[12] != 0xb1 , is: " + lastSentinnel[12]);
		if ((lastSentinnel[13] & 0xFF) != 0x92)
			logger.warn("lastSentinnel[13] != 0x92 , is: " + lastSentinnel[13]);
		if ((lastSentinnel[14] & 0xFF) != 0xcc)
			logger.warn("lastSentinnel[14] != 0xcc , is: " + lastSentinnel[14]);
		if ((lastSentinnel[15] & 0xFF) != 0xa0)
			logger.warn("lastSentinnel[15] != 0xa0 , is: " + lastSentinnel[15]);

		int bitPos = 0;
		try {

			List val = DwgUtil.getBitDouble(intData, bitPos); // Unknown
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("VAL1", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos); // Unknown
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("VAL2", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos); // Unknown
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("VAL3", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos); // Unknown
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("VAL4", val.get(1));

			val = DwgUtil.getTextString(intData, bitPos); // Unknown
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("STRING1", val.get(1));

			val = DwgUtil.getTextString(intData, bitPos); // Unknown
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("STRING2", val.get(1));

			val = DwgUtil.getTextString(intData, bitPos); // Unknown
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("STRING3", val.get(1));

			val = DwgUtil.getTextString(intData, bitPos); // Unknown
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("STRING4", val.get(1));

			val = DwgUtil.getBitLong(intData, bitPos); // Unknown
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("LONG1", val.get(1));

			val = DwgUtil.getBitLong(intData, bitPos); // Unknown
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("LONG2", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMASO", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMSHO", val.get(1));

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
			dwgFile.setHeader("UNDOCUMENTED1", val.get(1));

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
			dwgFile.setHeader("MIRRTEXT", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("WORLDVIEW", val.get(1));

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
			dwgFile.setHeader("DISPSILH", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("PELLISE", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("PROXYGRAPH", val.get(1));

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
			dwgFile.setHeader("ATTMODE", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("PDMODE", val.get(1));

			val = DwgUtil.getBitLong(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("UNKNOWN1", val.get(1));

			val = DwgUtil.getBitLong(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("UNKNOWN2", val.get(1));

			val = DwgUtil.getBitLong(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("UNKNOWN3", val.get(1));

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

			val = DwgUtil.getBitLong(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("UNKNOWN4", val.get(1));

			val = DwgUtil.getBitLong(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("UNKNOWN5", val.get(1));

			val = DwgUtil.getBitLong(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("UNKNOWN6", val.get(1));

			int[] tdindwg = new int[2];

			val = DwgUtil.getBitLong(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			tdindwg[0] = ((Integer) val.get(1)).intValue();

			val = DwgUtil.getBitLong(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			tdindwg[1] = ((Integer) val.get(1)).intValue();

			int[] tdusrtime = new int[2];

			val = DwgUtil.getBitLong(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			tdusrtime[0] = ((Integer) val.get(1)).intValue();

			val = DwgUtil.getBitLong(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			tdusrtime[1] = ((Integer) val.get(1)).intValue();

			dwgFile.setHeader("TDUSRTIME", tdusrtime);

			//FIXME: En versiones anteriores solo había una variable
			// "CECOLOR" de tipo entero, habría que comprobar donde se
			// utiliza para, si es necesario, utilizar apropiadamente
			// las nuevas.
			val = DwgUtil.getCmColor(intData, bitPos, dwgFile.getDwgVersion());
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("CECOLOR", val.get(1));
			dwgFile.setHeader("CECOLOR_RGBVALUE", val.get(2));
			Object cecolorColorByte = val.get(3);
			dwgFile.setHeader("CECOLOR_COLORBYTE", cecolorColorByte);
			dwgFile.setHeader("CECOLOR_NAME", val.get(4));

			dwgFile.getDwgVersion();

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			int intHandle = DwgUtil.handleToInt(val);
			dwgFile.setHeader("HANDSEED", new Integer(intHandle));

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

			// DWG 2000+
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("PSVPSCALE", val.get(1));

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

			double[] spaces6 = new double[6];

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

			double[] spaces7 = new double[6];
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

			double[] spaces8 = new double[6];
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

			// R2000+ Only

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			intHandle = DwgUtil.handleToInt(val);
			dwgFile.setHeader("PUCSBASE", new Integer(intHandle));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("PUCSORTHOVIEW", val.get(1));

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			intHandle = DwgUtil.handleToInt(val);
			dwgFile.setHeader("PUCSORTHOREF", new Integer(intHandle));

			double[] pucsorgtop = new double[3];
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			pucsorgtop[0] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			pucsorgtop[1] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			pucsorgtop[2] = ((Double) val.get(1)).doubleValue();
			dwgFile.setHeader("PUCSORGTOP", pucsorgtop);

			double[] pucsorgbottom = new double[3];
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			pucsorgbottom[0] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			pucsorgbottom[1] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			pucsorgbottom[2] = ((Double) val.get(1)).doubleValue();
			dwgFile.setHeader("PUCSORGBOTTOM", pucsorgbottom);

			double[] pucsorgleft = new double[3];
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			pucsorgleft[0] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			pucsorgleft[1] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			pucsorgleft[2] = ((Double) val.get(1)).doubleValue();
			dwgFile.setHeader("PUCSORGLEFT", pucsorgleft);

			double[] pucsorgright = new double[3];
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			pucsorgright[0] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			pucsorgright[1] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			pucsorgright[2] = ((Double) val.get(1)).doubleValue();
			dwgFile.setHeader("PUCSORGRIGHT", pucsorgright);

			double[] pucsorgfront = new double[3];
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			pucsorgfront[0] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			pucsorgfront[1] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			pucsorgfront[2] = ((Double) val.get(1)).doubleValue();
			dwgFile.setHeader("PUCSORGFRONT", pucsorgfront);

			double[] pucsorgback = new double[3];
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			pucsorgback[0] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			pucsorgback[1] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			pucsorgback[2] = ((Double) val.get(1)).doubleValue();
			dwgFile.setHeader("PUCSORGBACK", pucsorgback);

			// Common

			double[] spaces9 = new double[6];
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

			double[] spaces10 = new double[6];
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

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			intHandle = DwgUtil.handleToInt(val);
			dwgFile.setHeader("UCSBASE", new Integer(intHandle));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("UCSORTHOVIEW", val.get(1));

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			intHandle = DwgUtil.handleToInt(val);
			dwgFile.setHeader("UCSORTHOREF", new Integer(intHandle));

			double[] ucsorgtop = new double[3];
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			ucsorgtop[0] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			ucsorgtop[1] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			ucsorgtop[2] = ((Double) val.get(1)).doubleValue();
			dwgFile.setHeader("UCSORGTOP", ucsorgtop);

			double[] ucsorgbottom = new double[3];
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			ucsorgbottom[0] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			ucsorgbottom[1] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			ucsorgbottom[2] = ((Double) val.get(1)).doubleValue();
			dwgFile.setHeader("UCSORGBOTTOM", ucsorgbottom);

			double[] ucsorgleft = new double[3];
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			ucsorgleft[0] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			ucsorgleft[1] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			ucsorgleft[2] = ((Double) val.get(1)).doubleValue();
			dwgFile.setHeader("UCSORGLEFT", ucsorgleft);

			double[] ucsorgright = new double[3];
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			ucsorgright[0] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			ucsorgright[1] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			ucsorgright[2] = ((Double) val.get(1)).doubleValue();
			dwgFile.setHeader("UCSORGRIGHT", ucsorgright);

			double[] ucsorgfront = new double[3];
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			ucsorgfront[0] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			ucsorgfront[1] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			ucsorgfront[2] = ((Double) val.get(1)).doubleValue();
			dwgFile.setHeader("UCSORGFRONT", ucsorgfront);

			double[] ucsorgback = new double[3];
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			ucsorgback[0] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			ucsorgback[1] = ((Double) val.get(1)).doubleValue();
			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			ucsorgback[2] = ((Double) val.get(1)).doubleValue();
			dwgFile.setHeader("UCSORGBACK", ucsorgback);

			val = DwgUtil.getTextString(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMPOST", val.get(1));

			val = DwgUtil.getTextString(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMAPOST", val.get(1));

			// Common

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
			dwgFile.setHeader("DIMAND", val.get(1)); //FIXME: Â¿o es DIMRND?

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMDLE", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMTP", val.get(1));

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMTM", val.get(1));

			// DWG 2000 +
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
			dwgFile.setHeader("DIMSE2", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMTAD", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMZIN", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMAZIN", val.get(1));

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

			// DWG 2000+

			val = DwgUtil.getBitDouble(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMALTRND", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMALT", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMALTD", val.get(1));

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

			// common

			//FIXME: En versiones anteriores solo había una variable
			// "DIMCLRD" de tipo entero, habría que comprobar donde se
			// utiliza para, si es necesario, utilizar apropiadamente
			// las nuevas. Lo mismo con "DIMCLRE" y "DIMCLRT".

			val = DwgUtil.getCmColor(intData, bitPos, dwgFile.getDwgVersion());
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMCLRD", val.get(1));
			dwgFile.setHeader("DIMCLRD_RGBVALUE", val.get(2));
			Object dimclrdColorByte = val.get(3);
			dwgFile.setHeader("DIMCLRD_COLORBYTE", dimclrdColorByte);
			dwgFile.setHeader("DIMCLRD_NAME", val.get(4));

			val = DwgUtil.getCmColor(intData, bitPos, dwgFile.getDwgVersion());
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMCLRE", val.get(1));
			dwgFile.setHeader("DIMCLRE_RGBVALUE", val.get(2));
			Object dimclreColorByte = val.get(3);
			dwgFile.setHeader("DIMCLRE_COLORBYTE", dimclreColorByte);
			dwgFile.setHeader("DIMCLRE_NAME", val.get(4));

			val = DwgUtil.getCmColor(intData, bitPos, dwgFile.getDwgVersion());
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMCLRT", val.get(1));
			dwgFile.setHeader("DIMCLRT_RGBVALUE", val.get(2));
			Object dimclrtColorByte = val.get(3);
			dwgFile.setHeader("DIMCLRT_COLORBYTE", dimclrtColorByte);

			dwgFile.setHeader("DIMCLRT_NAME", val.get(4));

			// dwg2000 +

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMADEC", val.get(1));

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

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMAUNIT", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMFRAC", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMLUNIT", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMDSEP", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMTMOVE", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMJUST", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMSD1", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMSD2", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMTOLJ", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMTZIN", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMALTZ", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMALTTZ", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMUPT", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMFIT", val.get(1));

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			intHandle = DwgUtil.handleToInt(val);
			dwgFile.setHeader("DIMTXTSTY", new Integer(intHandle));

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			intHandle = DwgUtil.handleToInt(val);
			dwgFile.setHeader("DIMLDRBLK", new Integer(intHandle));

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			intHandle = DwgUtil.handleToInt(val);
			dwgFile.setHeader("DIMBLK", new Integer(intHandle));

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			intHandle = DwgUtil.handleToInt(val);
			dwgFile.setHeader("DIMBLK1", new Integer(intHandle));

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			intHandle = DwgUtil.handleToInt(val);
			dwgFile.setHeader("DIMBLK2", new Integer(intHandle));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMLWD", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMLWE", val.get(1));

			// Common

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
			dwgFile.setHeader("LINETYPE_CONTROL_OBJECT", new Integer(intHandle));

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
			dwgFile.setHeader("DIMSTYLE_CONTROL_OBJECT", new Integer(intHandle));

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
			dwgFile.setHeader("NAMED_OBJECT_DICTIONARY", new Integer(intHandle));
			// DWG 2000+

			// unknown bitshort
			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();

			// unknown bitshort
			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();

			val = DwgUtil.getTextString(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("HYPERLINKBASE", val.get(1));

			val = DwgUtil.getTextString(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("STYLESHEET", val.get(1));

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			intHandle = DwgUtil.handleToInt(val);
			dwgFile.setHeader("LAYOUT_DICTIONARY", new Integer(intHandle));

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			intHandle = DwgUtil.handleToInt(val);
			dwgFile.setHeader("PLOT_SETTINGS_DICTIONARY",
					new Integer(intHandle));

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			intHandle = DwgUtil.handleToInt(val);
			dwgFile.setHeader("PLOT_STYLES_DICTIONARY", new Integer(intHandle));

			// DWG 2004+
			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			intHandle = DwgUtil.handleToInt(val);
			dwgFile.setHeader("MATERIALS_DICTIONARY", new Integer(intHandle));

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			intHandle = DwgUtil.handleToInt(val);
			dwgFile.setHeader("COLORS_DICTIONARY", new Integer(intHandle));

			// DWG 2000 +

			/*
			 * Flags:
			 * CELWEIGHT	Flags & 0x001F
			 * ENDCAPS		Flags & 0x0060
			 * JOINSTYLE	Flags & 0x0180
			 * LWDISPLAY	!(Flags & 0x0200)
			 * XEDIT		!(Flags & 0x0400)
			 * EXTNAMES		Flags & 0x0800
			 * PSTYLEMODE	Flags & 0x2000
			 * OLESTARTUP	Flags & 0x4000
			 */
			val = DwgUtil.getBitLong(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("FLAGS", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("INSUNITS", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("CEPSNTYPE", val.get(1));

			if (((Integer) val.get(1)).intValue() == 3) {
				val = DwgUtil.getHandle(intData, bitPos);
				bitPos = ((Integer) val.get(0)).intValue();
				intHandle = DwgUtil.handleToInt(val);
				dwgFile.setHeader("CPSNID", new Integer(intHandle));
			}

			val = DwgUtil.getTextString(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("FINGERPRINTGUID", val.get(1));

			val = DwgUtil.getTextString(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("VERSIONGUID", val.get(1));

			// DWG 2004+

			val = DwgUtil.getRawChar(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("SORTENTS", val.get(1));

			val = DwgUtil.getRawChar(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("INDEXCTL", val.get(1));

			val = DwgUtil.getRawChar(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("HIDETEXT", val.get(1));

			val = DwgUtil.getRawChar(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("XCLIPFRAME", val.get(1));

			val = DwgUtil.getRawChar(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMASSOC", val.get(1));

			val = DwgUtil.getRawChar(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("HALOGAP", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("OBSCUREDCOLOR", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("INTERSECTION_COLOR", val.get(1));

			val = DwgUtil.getRawChar(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("OBSCUREDLTYPE", val.get(1));

			val = DwgUtil.getRawChar(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("INTERSECTION_DISPLAY", val.get(1));

			val = DwgUtil.getTextString(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("PROJECTNAME", val.get(1));

			// Common

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

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

	/**
	 * decrypts the encrypted data
	 *
	 * @param encrypted
	 *            encrypted data
	 * @param offset
	 *            current position in file
	 * @return vector with the decrypted data
	 */
	private int[] decryptSectionHeader(byte[] encrypted, int offset) {
		int secMask = 0x4164536b ^ offset;
		int[] decrypted = new int[8];
		ByteBuffer Hdr = ByteBuffer.allocate(32);
		Hdr.position(0);
		Hdr.put(encrypted);
		Hdr.order(ByteOrder.LITTLE_ENDIAN);
		Hdr.position(0);

		for (int i = 0; i < 8; i++) {
			int aux = Hdr.getInt();
			aux = aux ^ secMask;
			decrypted[i] = aux;
		}
		return decrypted;
	}

	/**
	 * Find all subsections of a section named "name" in the section's array sorted by startOffset
	 *
	 * @param name
	 *            String name of the section to find
	 *
	 * @return vector with sections named 2name"
	 */
	private Section[] findSections(String name) {

		Comparator cmp = new Comparator(){

			public int compare(Object arg0, Object arg1) {
				if (((Section)arg0).getStartOffset() > ((Section)arg1).getStartOffset()) {
					return 1;
                }
				return -1;
			}

		};
		TreeSet foundSections = new TreeSet(cmp);

		for (int i = 0; i < sectionAmount; i++) {
			if (sections[i].getName() != null
					&& sections[i].getName().contains(name)) {
				foundSections.add(sections[i]);
			}
			if (i == sectionAmount)
				logger.warn("No se han encontrado secciones llamadas \"" + name + "\"");
		}
		return (Section[]) foundSections.toArray(new Section[0]);
	}


	/**
	 * Contains the defined classes for drawing
	 *
	 * @param bb
	 *            ByteBuffer containing the opened DWG file
	 */
	private void readDwgR2004Classes(ByteBuffer bb) {

		ByteBuffer decompressedBB = readSection(bb, "AcDb:Classes");

		byte[] sentinel = new byte[16];
		decompressedBB.order(ByteOrder.nativeOrder());
		decompressedBB.position(0);
		decompressedBB.get(sentinel); // we get the sentinel that ALWAYS
		// appears before the data

		if ((sentinel[0] & 0XFF) != 0x8d)
			logger.warn(" sentinel[0] != 0x8d");
		if ((sentinel[1] & 0XFF) != 0xa1)
			logger.warn("sentinel[1] != 0xa1");
		if ((sentinel[2] & 0XFF) != 0xc4)
			logger.warn("sentinel[2] != 0xc4");
		if ((sentinel[3] & 0XFF) != 0xb8)
			logger.warn("sentinel[3] != 0xb8");
		if ((sentinel[4] & 0XFF) != 0xc4)
			logger.warn("sentinel[4] != 0xc4");
		if ((sentinel[5] & 0XFF) != 0xa9)
			logger.warn("sentinel[5] != 0xa9");
		if ((sentinel[6] & 0XFF) != 0xf8)
			logger.warn("sentinel[6] != 0xf8");
		if ((sentinel[7] & 0XFF) != 0xc5)
			logger.warn("sentinel[7] != 0xc5");
		if ((sentinel[8] & 0XFF) != 0xc0)
			logger.warn("sentinel[8] != 0xc0");
		if ((sentinel[9] & 0XFF) != 0xdc)
			logger.warn("sentinel[9] != 0xdc");
		if ((sentinel[10] & 0XFF) != 0xf4)
			logger.warn("sentinel[10] != 0xf4");
		if ((sentinel[11] & 0XFF) != 0x5f)
			logger.warn("sentinel[11] != 0x5f");
		if ((sentinel[12] & 0XFF) != 0xe7)
			logger.warn("sentinel[12] != 0xe7");
		if ((sentinel[13] & 0XFF) != 0xcf)
			logger.warn("sentinel[13] != 0xcf");
		if ((sentinel[14] & 0XFF) != 0xb6)
			logger.warn("sentinel[14] != 0xb6");
		if ((sentinel[15] & 0XFF) != 0x8a)
			logger.warn("sentinel[15] != 0x8a");

		// we start working with data that comes after the beginning sentinel
		decompressedBB.order(ByteOrder.LITTLE_ENDIAN);
		int size = decompressedBB.getInt();

		decompressedBB.order(ByteOrder.nativeOrder());
		byte[] data = new byte[size];
		decompressedBB.get(data);

		int[] intData = DwgUtil.toIntArray(data);

		bb.order(ByteOrder.LITTLE_ENDIAN);
		short crc = decompressedBB.getShort(); // CRC

		List val = null;
		int bitPos = 0;

		val = DwgUtil.getBitShort(intData, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		int MaxclassNum = ((Integer) val.get(1)).intValue();

		val = DwgUtil.getRawChar(intData, bitPos); // 0x00
		if (((Integer) val.get(1)).intValue() != 0x00) {
			logger.warn("Classes Section RC1 != 0x00");
		}
		bitPos = ((Integer) val.get(0)).intValue();

		val = DwgUtil.getRawChar(intData, bitPos);
		bitPos = ((Integer) val.get(0)).intValue(); // 0x00
		if (((Integer) val.get(1)).intValue() != 0x00) {
			logger.warn("Classes Section RC2 != 0x00");
		}

		val = DwgUtil.testBit(intData, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		if (((Boolean) val.get(1)).booleanValue() != true) {
			logger.warn("Classes Section B != true");
		}

		int maxBit = size * 8;

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

			val = DwgUtil.getBitLong(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			int numberObject = ((Integer) val.get(1)).intValue();

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			int DwgVersion = ((Integer) val.get(1)).intValue();

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			int maintenanceVersion = ((Integer) val.get(1)).intValue();

			val = DwgUtil.getBitLong(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			int unknown1 = ((Integer) val.get(1)).intValue();

			val = DwgUtil.getBitLong(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			int unknown2 = ((Integer) val.get(1)).intValue();

			DwgClass2004 dwgClass = new DwgClass2004(classNum, version,
					appName, cPlusPlusName, dxfName, isZombie, id,
					numberObject, DwgVersion, maintenanceVersion, unknown1,
					unknown2);
			dwgFile.addDwgClass(dwgClass);

		}// while

		decompressedBB.order(ByteOrder.nativeOrder());
		byte[] lastSentinnel = new byte[16];
		decompressedBB.get(lastSentinnel);
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
		if ((lastSentinnel[11] & 0xFF) != 0xa0)
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
	 *
	 * @param bb
	 *            ByteBuffer containing the opened DWG File
	 */
	private void readDwgR2004ObjectOffsets(ByteBuffer bb) {

		ByteBuffer decompressedBB = readSection(bb,"AcDb:Handles");
		decompressedBB.position(0);

		short size;

		byte[] dataBytes;
		int[] data;
		int bitPos = 0;
		int bitMax;


		do {
			bitPos = 0;
			decompressedBB.order(ByteOrder.BIG_ENDIAN);
			size = decompressedBB.getShort();
			decompressedBB.order(ByteOrder.LITTLE_ENDIAN);
			if (size == 2)
				break;
			dataBytes = new byte[size-2];
			decompressedBB.get(dataBytes);
			data = DwgUtil.bytesToMachineBytes(dataBytes);
			bitMax = (size - 2) * 8;
			int lastHandle = 0;
			int lastLoc = 0;
			while (bitPos < bitMax) {
				ArrayList v = DwgUtil.getModularChar(data, bitPos);
				bitPos = ((Integer) v.get(0)).intValue();
				int handle = ((Integer) v.get(1)).intValue();
				if(handle<0){
					logger.warn("offset negativo");
					handle = - handle;
				}
				lastHandle = lastHandle + handle;

				v = DwgUtil.getModularChar(data, bitPos);
				bitPos = ((Integer) v.get(0)).intValue();
				int loc = ((Integer) v.get(1)).intValue();
				lastLoc = lastLoc + loc;

				dwgFile.addDwgObjectOffset(lastHandle, lastLoc);

			}// while
			decompressedBB.getShort(); //crc

		} while (size != 2);

		return;

	}

	/**
	 * @return the decompressed data of a logical section,
	 * joining up all the physical sections belonging to it .
	 * @param ByteBuffer
	 *            Containing the opened dwg file
	 * @param compressedSize
	 *            size of the compressed data
	 * @param decompressedSize
	 *            size of the decompressed data
	 * @param CheckSum
	 */
	protected ByteBuffer readSection(ByteBuffer bb, String name) {

		Section[] foundSections = findSections(name);
		ByteBuffer fullSectionDecompressedBB = null;
		ByteBuffer sectionDecompressedData = null;
		int numberOfFoundSections = foundSections.length;
		if ( numberOfFoundSections == 0) {
			logger.warn("No se han encontrado secciones llamadas " + name);
		} else {
			Section section;
			int decompressedSize = foundSections[0].getMaxDecompressedSize();

			fullSectionDecompressedBB = ByteBuffer.allocate(decompressedSize * numberOfFoundSections);
			fullSectionDecompressedBB.position(0);

			for(int i=0; i<numberOfFoundSections; i++){
				section = (Section)foundSections[i];

				int offset = section.getAddress();
				bb.position(offset);
				bb.order(ByteOrder.nativeOrder());
				byte[] encrypted = new byte[32];
				bb.get(encrypted); // We get the Encrypted Section Header

				int[] decrypted = decryptSectionHeader(encrypted, offset);
				/*
				 * decrypted[0] = SectionType
				 * decrypted[1] = Data Size
				 * decrypted[2] = Section Size
				 * decrypted[3] = Start Offset
				 * decrypted[4] = Unknown
				 * decrypted[5] = CheckSum1
				 * decrypted[6] = CheckSum2
				 * decrypted[7] = ???
				 *
				 * Lo de arriba es lo de la documentacion pero yo creo [Paco]:
				 *
				 * decrypted[0] = ???
				 * decrypted[1] = SectionType
				 * decrypted[2] = Data Size
				 * decrypted[3] = Section Size
				 * decrypted[4] = Start Offset
				 * decrypted[5] = Unknown
				 * decrypted[6] = CheckSum1
				 * decrypted[7] = CheckSum2
				 *
				 */

				if ( decrypted[1] != section.getType()){
					logger.warn("Discrepancia en el tipo de seccion: " + section.getType() +" , " + decrypted[1] );
				}
				if ( decrypted[2] != section.getDataSize()){
					logger.warn("Discrepancia en el tamaÃ±o de los datos de la seccion: " + section.getDataSize() +" , " + decrypted[2] );
				}
				if ( decrypted[3] != section.getSize()){
					logger.warn("Discrepancia en el tamaÃ±o de la seccion: " + section.getSize() +" , " + decrypted[3] );
				}
				if ( decrypted[4] != section.getStartOffset()){
					logger.warn("Discrepancia en el start offset de la seccion: " + section.getStartOffset() +" , " + decrypted[4] );
				}

				decompressedSize = section.getMaxDecompressedSize();
				sectionDecompressedData = readCompressedSection(bb, decrypted[2],	decompressedSize, 0);
				sectionDecompressedData.position(0);
				section.setDecompressedData(sectionDecompressedData);
				if (fullSectionDecompressedBB.position() != section.getStartOffset()){
					logger.warn("Desincronización entre el start offset de la seccion y la posición del ByteBuffer compuesto: " + section.getStartOffset() +" , " + fullSectionDecompressedBB.position() );
				}
				fullSectionDecompressedBB.put(section.getDecompressedData());
			}
		}
		return fullSectionDecompressedBB;

	}

	/**
	 * Reads all the object referenced in the object map section of the DWG file
	 * (using their object file obsets)
	 */
	protected void readDwgR2004Objects() {

		ByteBuffer decompressedBB = readSection(bb, "AcDb:AcDbObjects");
		if (decompressedBB != null){
			decompressedBB.position(0);

			for (int i = 0; i < dwgFile.getDwgObjectOffsets().size(); i++) {
				try {
					DwgObjectOffset doo = (DwgObjectOffset) dwgFile.getDwgObjectOffsets().get(i);

					DwgObject obj = readDwgR2004Object(decompressedBB, doo.getOffset(), i);
					/*
					 * azabala: las entidades DWG no implementadas no nos aportan
					 * nada (aunque la sigo leyendo por si aparecen problemas de
					 * puntero de fichero) No considero por tanto los DwgObject if
					 * (obj != null) { dwgFile.addDwgObject(obj); }
					 *
					 * paco: propongo reconsiderar esto. Si no cargamos todos los
					 * objetos podemos tener problemas con las subentities.
					 */
					if (obj != null && obj.getClass() != DwgObject.class) {
						dwgFile.addDwgObject(obj);
					}
				} catch (Exception e) {
					logger.error(e.getMessage());
					continue;
				}
			}// for
		}

	}

	/**
	 * Read commun parameters for an Object
	 *
	 * @param offset
	 *            Object's address
	 * @param index
	 *            object identifier
	 * @return the object
	 */
	protected DwgObject readDwgR2004Object(ByteBuffer bb, int offset, int index) {
		DwgObject obj = null;
		try {
			if (offset >= bb.capacity()) {
				logger.warn("Posible error leyendo un objeto: capacity = "
						+ bb.capacity() + " offset = " + offset);
				return null;
			}
			bb.position(offset);
			int size = DwgUtil.getModularShort(bb);


			//FIXME: Esto es para saltarse posibles objetos defectuosos que nos
			//impedirían ver el resto del archivo
			if (size == 0 || size>29696) {
				logger.warn("Posible error leyendo un objeto: size = "+size);
				return null;
			}
			if (offset + (size*8) >= bb.capacity()) {
				logger.warn("Posible error leyendo un objeto: capacity = "
						+ bb.capacity() + " offset = " + offset + " size = "+size);
				return null;
			}

			bb.order(ByteOrder.LITTLE_ENDIAN);
			byte[] data = new byte[size];
			int[] intData = new int[size];

			bb.get(data);
			try {
				intData = DwgUtil.toIntArray(data);
			} catch (Exception e) {
				logger.error("Error leyendo un objeto: size = " + size, e);
				return null;
			}

			int bitPos = 0;
			ArrayList v = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) v.get(0)).intValue();
			int type = ((Integer) v.get(1)).intValue(); // the type of object

			obj = DwgObjectFactory.getInstance().create(type, index);

			/*
			 * This if is entered to check if the type of the object is a
			 * non-fixed value
			 */
			if (obj == null) {
				logger.info("dwgFileVR2004Reader: Entrando en Objeto Nulo");
				if (type >= 500) {
					int newIndex = type - 500;
					if (newIndex < (dwgFile.getDwgClasses().size() - 1)) {
						DwgClass2004 dwgClass = (DwgClass2004) dwgFile.getDwgClasses().get(newIndex);
						String dxfEntityName = dwgClass.getDxfName();
						obj = DwgObjectFactory.getInstance().create(dxfEntityName, index);

						if (obj == null) {
							logger.info(dxfEntityName
									+ " todavia no está implementado");
							return null;
						}// if
					}// if newIndex
					else {
						return null;
					}
				} else {
					logger.info("dwgFileVR2004Reader: Encontrado tipo " + type);
					return null;
				}
			}
			obj.setAddress(offset); // we set the address for the object. This
			// time there is no mistake
			obj.setVersion(dwgFile.getDwgVersion());

			v = DwgUtil.getRawLong(intData, bitPos);
			bitPos = ((Integer) v.get(0)).intValue();
			int objBSize = ((Integer) v.get(1)).intValue();
			obj.setSizeInBits(objBSize);

			DwgHandleReference entityHandle = new DwgHandleReference();
			bitPos = entityHandle.read(intData, bitPos);
			obj.setHandle(entityHandle);

			v = DwgUtil.readExtendedData(intData, bitPos);
			bitPos = ((Integer) v.get(0)).intValue();
			ArrayList extData = (ArrayList) v.get(1);
			obj.setExtendedData(extData);

			boolean gflag = obj.isGraphicsFlag();
			if (gflag) {
				//lee un flag boolean
				v = DwgUtil.testBit(intData, bitPos);
				bitPos = ((Integer) v.get(0)).intValue();
				boolean val = ((Boolean) v.get(1)).booleanValue();
				//si hay imagen asociada, se lee por completo
				if (val) {
					v = DwgUtil.getRawLong(intData, bitPos);
					bitPos = ((Integer) v.get(0)).intValue();
					size = ((Integer) v.get(1)).intValue();
					int bgSize = size * 8;
					Integer giData = (Integer) DwgUtil.getBits(intData, bgSize,
							bitPos);
					obj.setGraphicData(giData.intValue());
					bitPos = bitPos + bgSize;
				}
			}
			if(obj.getClass() != DwgObject.class)
				readSpecificObject2004(obj, intData, bitPos);

		} catch (RuntimeException e) {
			e.printStackTrace();
			return null;
		} catch (CorruptedDwgEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return obj;
	}

	/**
	 * Read a specific object depending on its type
	 *
	 * @param obj
	 *            the object to read
	 * @param data
	 *            Data of the object
	 * @param bitPos
	 *            Position where start reading
	 * @throws RuntimeException
	 * @throws CorruptedDwgEntityException
	 */
	private void readSpecificObject2004(DwgObject obj, int[] data, int bitPos)
			throws RuntimeException, CorruptedDwgEntityException {

		DwgObjectReaderPool pool = DwgObjectReaderPool.getInstance();
		IDwgObjectReader reader = pool.get(obj, "2004");
		if (reader != null) {
			// reader.setFileReader(this);
			reader.readSpecificObj(data, bitPos, obj);
		} else {
			logger.warn("No se ha implementado la lectura de "
					+ obj.getClass().getName() + ", code=" + obj.getType());
		}
	}


	/**
	 * Reads the Section Map and fills the Sections Array with the sections
	 * found
	 */
	protected void readDwgR2004SectionMap(ByteBuffer bb) {
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.position(sectionMapAddress);
		ByteBuffer dData = readSystemSection(bb);
		dData.order(ByteOrder.LITTLE_ENDIAN);
		dData.position(0);

		int sectionNumber;
		int gapNumber = 0;
		sections = new Section[sectionAmount + 1];
		gaps = new Gap[gapAmount];

		sections[0] = new Section();
		sections[0].setNumber(0);
		sections[0].setSize(0);
		sections[0].setAddress(0x100);

		int size=0;
		int previousSize=0;

		/*
		 * We start reading the section map once decompressed. this usually
		 * gives a pair of SectionNumber/SectionSize unless a Gap (negative
		 * section number) is found
		 */
		for (int i = 1; i <= sectionAmount; i++) {
			previousSize=size;
			size=0;
			sections[i] = new Section();
			sectionNumber = dData.getInt();
			while (sectionNumber < 0) {
				if (gapNumber >= gapAmount) {
					logger.warn("ERROR: Se han encontrado mas gaps de los definidos");
				}
				gaps[gapNumber] = new Gap();
				int gapSize = dData.getInt();
				size += gapSize;
				gaps[gapNumber].setSize(gapSize);
				gaps[gapNumber].setParent(dData.getInt());
				gaps[gapNumber].setLeft(dData.getInt());
				gaps[gapNumber].setRight(dData.getInt());
				gaps[gapNumber].setZero(dData.getInt());
				gapNumber++;
				sectionNumber = dData.getInt();
			}

			if (sectionNumber == sectionInfoId) {
				// We keep this index so we can localize the Section Info
				sectionInfoIndex = i;
			}
			sections[i].setNumber(sectionNumber);
			int sectionSize = dData.getInt();
			sections[i].setSize(sectionSize);
			size += sectionSize;
			if (i == 1) {
				sections[i].setAddress(0x100);
			} else {
				sections[i].setAddress(sections[i - 1].getAddress()
						+ previousSize);
			}
		}
	}

	/**
	 * @return Returns the decompressed data of SectionMap and Section Info.
	 * @param ByteBuffer
	 *            containing the dwg opened file
	 */
	protected ByteBuffer readSystemSection(ByteBuffer bb) {

		int sectionType = bb.getInt();
		int decompressedSize = bb.getInt();
		int compressedSize = bb.getInt();
		int compressionType = bb.getInt();
		int checksum = bb.getInt();


		return readCompressedSection(bb, compressedSize, decompressedSize, checksum);

	}

	/**
	 * @return the decompressed data of a compressed section.
	 * @param ByteBuffer
	 *            Containing the opend dwg file
	 * @param compressedSize
	 *            size of the compressed data
	 * @param decompressedSize
	 *            size of the decompressed data
	 * @param CheckSum
	 */
	protected ByteBuffer readCompressedSection(ByteBuffer bb, int compressedSize, int decompressedSize, int checksum) {
		long calcSize = 0;
		ByteBuffer decompressedData;
		decompressedData = ByteBuffer.allocate(decompressedSize);
		decompressedData.order(ByteOrder.LITTLE_ENDIAN);
		decompressedData.position(0);

		int opcodes = 0;
		byte opcode1 = bb.get();
		byte opcode2 = 0;
		boolean opcode2valid = false;
		int literalLength = 0;
		int lls = 0;
		int compressedBytes = 0; // Number of "compressed" bytes that are to be copied to this
		                         // location from a previous location in the uncompressed data stream

		int compOffset = 0; // Offset backwards from the current location in the decompressed data stream,
		                    // where the "compressed" bytes should be copied from.

		int litCount = 0; // Number of uncompressed or literal bytes to be copied from the input stream,
		                  // following the addition of the compressed bytes.
		int[] twoBytesOffset;
		byte b;

		try {
			if (((opcode1 & 0xFF) >= 0x00) && ((opcode1 & 0xFF) <= 0x0F)) {
				literalLength = readLiteralLength(bb, opcode1);
				lls++;
				if (literalLength != 0) { // Copy the first sequence of uncompressed(literal) data
					for (int i = 0; i < literalLength; i++) {
						b = bb.get();
						decompressedData.put(b);
					}
					opcode1 = bb.get(); // get the first opcode
				}
				calcSize = calcSize + literalLength;

			} else {
				logger.error("Error leyendo el literal Length");
			}

			// We start treating the opcodes

			while (opcode1 != 0x11) {

				if ((opcode1 & 0xFF) >= 0x00 && (opcode1 & 0xFF) <= 0x0F) {

					logger.warn("Posible error leyendo el opcode " + opcode1);
					decompressedData.put(opcode1); // ???
					if (opcode2valid) {
						opcode1 = opcode2;
						opcode2valid = false;
					} else {
						opcode1 = bb.get();
					}
					continue;
				} else {
					compressedBytes = 0;
					compOffset = 0;
					litCount = 0;
					opcodes++;
					if ((opcode1 & 0xFF) == 0x10) {
						compressedBytes = readLongCompressionOffset(bb) + 9;
						twoBytesOffset = readTwoByteOffset(bb);
						compOffset = twoBytesOffset[0] + 0x3FFF;
						litCount = twoBytesOffset[1]; // this can take values from 0 to 3.
						opcode2 = bb.get(); // we get the next opcode
						opcode2valid = true;
						if (litCount == 0) { // if litCount is 0, it is read as the next literal length
							litCount = readLiteralLength(bb, opcode2);
							opcode2valid = false;
							if (litCount != 0) {
								opcode2 = bb.get();
								opcode2valid = true;
							}
						}
					} else if ((opcode1 & 0xFF) >= 0x12
							&& (opcode1 & 0xFF) <= 0x17) {
						compressedBytes = (opcode1 & 0x0F) + 2;
						twoBytesOffset = readTwoByteOffset(bb);
						compOffset = twoBytesOffset[0] + 0x3FFF;
						litCount = twoBytesOffset[1]; // this can take values from 0 to 3.
						opcode2 = bb.get(); // we get the next opcode
						opcode2valid = true;
						if (litCount == 0) { // if litCount is 0, it is read
							// as the next literal length
							litCount = readLiteralLength(bb, opcode2);
							opcode2valid = false;
							if (litCount != 0) {
								opcode2 = bb.get();
								opcode2valid = true;
							}
						}
					} else if ((opcode1 & 0xFF) == 0x18) {
						// Fuera de las especificaciones de OpenDWG pero
						// segun chiuinan2, un usuario del foro (en japones):
						// http://www.programmer-club.com/pc2020v5/Forum/ShowSameTitleN.asp?URL=N&board_pc2020=general&index=16&id=4664&mode=&type_pc2020=sametitleLevel-2
						// hay que hacer este tratamiento al recibir estos opcodes

						compressedBytes = readLongCompressionOffset(bb) + 9;
						twoBytesOffset = readTwoByteOffset(bb);
						compOffset = twoBytesOffset[0] + 0x7FFF;
						litCount = twoBytesOffset[1]; // this can take values from 0 to 3.
						opcode2 = bb.get(); // we get the next opcode
						opcode2valid = true;
						if (litCount == 0) { // if litCount is 0, it is read as the next literal length
							litCount = readLiteralLength(bb, opcode2);
							opcode2valid = false;
							if (litCount != 0) {
								opcode2 = bb.get();
								opcode2valid = true;
							}
						}
					} else if ((opcode1 & 0xFF) >= 0x19
							&& (opcode1 & 0xFF) <= 0x1f) {
						// Fuera de las especificaciones de OpenDWG pero
						// segun chiuinan2, un usuario del foro (en japones):
						// http://www.programmer-club.com/pc2020v5/Forum/ShowSameTitleN.asp?URL=N&board_pc2020=general&index=16&id=4664&mode=&type_pc2020=sametitleLevel-2
						// hay que hacer este tratamiento al recibir estos opcodes

						compressedBytes = (opcode1 & 0x0F) + 2;
						twoBytesOffset = readTwoByteOffset(bb);
						compOffset = twoBytesOffset[0] + 0x7FFF;
						litCount = twoBytesOffset[1]; // this can take values from 0 to 3.
						opcode2 = bb.get(); // we get the next opcode
						opcode2valid = true;
						if (litCount == 0) { // if litCount is 0, it is read as the next literal length
							litCount = readLiteralLength(bb, opcode2);
							opcode2valid = false;
							if (litCount != 0) {
								opcode2 = bb.get();
								opcode2valid = true;
							}
						}
					} else if ((opcode1 & 0xFF) == 0x20) {
						// Fuera de las especificaciones de OpenDWG pero
						// segun kermit un usuario del foro de openDesign hay
						// que aÃ±adirle 0x21
						compressedBytes = readLongCompressionOffset(bb) + 0x21;
						twoBytesOffset = readTwoByteOffset(bb);
						compOffset = twoBytesOffset[0];
						litCount = twoBytesOffset[1]; // this can take values from 0 to 3.
						opcode2 = bb.get(); // we get the next opcode
						opcode2valid = true;
						if (litCount == 0) { // if litCount is 0, it is read as the next literal length
							litCount = readLiteralLength(bb, opcode2);
							opcode2valid = false;
							if (litCount != 0) {
								opcode2 = bb.get();
								opcode2valid = true;
							}
						}
					} else if ((opcode1 & 0xFF) >= 0x21
							&& (opcode1 & 0xFF) <= 0x3f) {
						compressedBytes = (opcode1 & 0xFF) - 0x1E;

						twoBytesOffset = readTwoByteOffset(bb);
						compOffset = twoBytesOffset[0];
						litCount = twoBytesOffset[1]; // this can take values from 0 to 3.
						opcode2 = bb.get(); // we get the next opcode
						opcode2valid = true;
						if (litCount == 0) { // if litCount is 0, it is read as the next literal length
							litCount = readLiteralLength(bb, opcode2);
							opcode2valid = false;
							if (litCount != 0) {
								opcode2 = bb.get();
								opcode2valid = true;
							}
						}
					} else if ((opcode1 & 0xFF) >= 0x40
							&& (opcode1 & 0xFF) <= 0xFF) {
						compressedBytes = ((opcode1 & 0xF0) >> 4) - 1;
						opcode2 = bb.get(); // we read the next byte
						opcode2valid = true;
						compOffset = ((opcode2 & 0xFF) << 2)
								| ((opcode1 & 0x0C) >> 2);
						opcode2valid = false;
						if ((opcode1 & 0x03) == 0x00) { // if litCount is 0, it is read as the next literal length
							litCount = 0;
							byte opcode3 = bb.get();
							litCount = readLiteralLength(bb, opcode3);
							if (litCount == 0) {
								opcode2 = opcode3; // if litCount = 0, the actual byte is the next opcode
							} else {
								opcode2 = bb.get(); // we get the next opcode
							}
							opcode2valid = true;
						} else {
							litCount = opcode1 & 0x03;
							opcode2 = bb.get();
							opcode2valid = true;
						}
					} else {
						logger.warn("Posible error de lectura de una seccion comprimida: " +
								"Ha aparecido un opcode1 con valor "
								+ Integer.toHexString(opcode1 & 0xFF));
						opcode2 = bb.get();
						opcode2valid = true;
					}

					if (compressedBytes < 0) {
						logger.warn("Posible error de lectura de una seccion comprimida: compressedBytes < 0");
					}
					if (compOffset < 0) {
						logger.warn("Posible error de lectura de una seccion comprimida: compOffset < 0");
					}
					if (litCount < 0) {
						logger.warn("Posible error de lectura de una seccion comprimida: litCount < 0");
					}

					if (compressedBytes > compOffset) {
						logger.warn("Posible error en la lectura de una seccion comprimida: "
										+ "No parece logico que compressedBytes > compOffset");
					}
					if (compOffset >= decompressedData.position()) {
						logger.warn("Posible error en la lectura de una seccion comprimida:"
										+ " el compOffset se sale");
						compressedBytes = 0; // ???? Para no leer bytes comprimidos ni que se
						// incluyan en el tamaÃ±o calculado.
					}
					/*
					 * We first copy the compressed bytes in our
					 * decompressedData buffer, from a position compOffset bytes
					 * backward from the current position
					 */

					for (int i = 0; i < compressedBytes; i++) {
						b = decompressedData.get(decompressedData.position() - compOffset - 1);
						decompressedData.put(b);
					}

					/*
					 * Then we copy the literal bytes to our decompressedData
					 * from the input stream
					 */
					for (int i = 0; i < litCount; i++) {
						decompressedData.put(opcode2);
						opcode2valid = false;
						opcode2 = bb.get();
						opcode2valid = true;
					}

					calcSize = calcSize + compressedBytes + litCount;
				}
				opcode1 = opcode2;
				opcode2valid = false;
			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		return decompressedData;
	}

	/**
	 * @return the value of the Literal Length.
	 * @param ByteBuffer
	 *            Containing the opened dwg File.
	 * @param byte
	 *            b The opcode to calculate the Literal Length
	 */
	private int readLiteralLength(ByteBuffer bb, byte b) {
		int total = 0;
		if (0x01 <= b && b <= 0x0F) {
			total = (b & 0xFF) + 3;
			return total;
		} else if (b == 0x00) {
			total = 0x0F;
			b = bb.get();
			while (b == 0x00) {
				total += 0xFF;
				b = bb.get();
			}
			total += (b & 0xFF) + 3;
			return total;
		} else if ((b & 0xF0) != 0) {
			/*
			 * Any bit set in the high nibble indicates that the literal lenght
			 * is 0, and the byte is actually the next compression opcode. This
			 * is case is not possible when the first literal length is read
			 */
			return 0;
		}
		logger.warn("Posible error de lectura: Se ha encontrado un Literal Lenght: "
				+ Integer.toHexString(b & 0xFF));
		return total;
	}

	/**
	 * Common compression for different opcodes
	 *
	 * @param bb
	 *            ByteBuffer reading the opened DWG file
	 * @return the value for <code> offset </code> and <code> litCount </code>
	 */
	private int[] readTwoByteOffset(ByteBuffer bb) {
		byte firstByte = bb.get();
		int[] values = new int[2];
		int offset = (((firstByte & 0xFF) >> 2) | ((bb.get() & 0xFF) << 6));
		int litCount = firstByte & 0x03; // This will make litCount taking values from 0 to 3
		values[0] = offset;
		values[1] = litCount;
		return values;
	}

	/**
	 * Common compression for different opcodes
	 *
	 * @param bb
	 *            ByteBuffer reading the opened DWG file
	 * @return the value for <code> compressedBytes </code>
	 */
	private int readLongCompressionOffset(ByteBuffer bb) {
		int value = 0;
		byte b = bb.get();
		while (b == 0x00) {
			value = value + 0xFF;
			b = bb.get();
		}
		value = value + (b & 0xFF);
		return value;
	}

	/**
	 * Generate a sequence into magicNumber to decrypt the encrypted data in the
	 * header
	 */
	static void setMagicNumber() {
		int size = 0x6C;
		magicNumber = new byte[size];
		int randseed = 1;
		for (int i = 0; i < size; i++) {
			randseed *= 0x343fd;
			randseed += 0x269ec3;
			magicNumber[i] = (byte) (randseed >> 0x10);
		}
	}


	public int readObjectHeader(int[] data, int offset, DwgObject dwgObject)
			throws RuntimeException, CorruptedDwgEntityException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
		// return 0;
	}

	public int readObjectTailer(int[] data, int offset, DwgObject dwgObject)
			throws RuntimeException, CorruptedDwgEntityException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
		// return 0;
	}

}

/**
 * A section structure contains the number (identifier),size, address and name
 * of the section
 *
 */
class Section {
	private int number;

	private int size;

	private int address;

	private String name;

	private int sizeOfLogicalSection;

	private int dataSize;

	private int maxDecompressedSize;

	private int compressed;

	private int type;

	private int encrypted;

	private int startOffset;

	private ByteBuffer decompressedData = null;

	/**
	 *
	 * @return number of Section
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * Sets number of section
	 *
	 * @param number
	 */
	public void setNumber(int number) {
		this.number = number;
	}

	/**
	 *
	 * @return size of Section
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Sets size2 of Section
	 *
	 * @param size
	 */
	public void setSizeOfLogicalSection(int size) {
		this.sizeOfLogicalSection = size;
	}

	/**
	 *
	 * @return size of Section
	 */
	public int getSizeOfLogicalSection() {
		return sizeOfLogicalSection;
	}

	/**
	 * Sets size of Section
	 *
	 * @param size
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 *
	 * @return address of Section
	 */
	public int getAddress() {
		return address;
	}

	/**
	 * Sets address of Section
	 *
	 * @param address
	 */
	public void setAddress(int address) {
		this.address = address;
	}

	/**
	 *
	 * @return name of Section
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the name of Section
	 *
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 *
	 * @return size of data of the Section
	 */
	public int getDataSize() {
		return dataSize;
	}

	/**
	 * Sets size of data of the Section
	 *
	 * @param size
	 */
	public void setDataSize(int dataSize) {
		this.dataSize = dataSize;
	}

	/**
	 *
	 * @return the maximum decompressed size of section
	 */
	public int getMaxDecompressedSize() {
		return maxDecompressedSize;
	}

	/**
	 * Sets the maximum decompressed size of section
	 *
	 * @param number
	 */
	public void setMaxDecompressedSize(int maxDecompressedSize) {
		this.maxDecompressedSize = maxDecompressedSize;
	}

	/**
	 *
	 * @return compressed of section
	 */
	public int getCompressed() {
		return compressed;
	}

	/**
	 * Sets compressed of section
	 *
	 * @param number
	 */
	public void setCompressed(int compressed) {
		this.compressed = compressed;
	}

	/**
	 *
	 * @return type of Section
	 */
	public int getType() {
		return type;
	}

	/**
	 * Sets type of section
	 *
	 * @param number
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 *
	 * @return encrypted of Section
	 */
	public int getEncrypted() {
		return encrypted;
	}

	/**
	 * Sets encrypted of section
	 *
	 * @param number
	 */
	public void setEncrypted(int encrypted) {
		this.encrypted = encrypted;
	}

	/**
	 * Sets startOffset of Section
	 *
	 * @param size
	 */
	public void setStartOffset(int startOffset) {
		this.startOffset = startOffset;
	}

	/**
	 *
	 * @return startOffset of Section
	 */
	public int getStartOffset() {
		return startOffset;
	}

	/**
	 * Sets decompressedData of Section
	 *
	 * @param decompressedData
	 */
	public void setDecompressedData(ByteBuffer decompressedData) {
		this.decompressedData = decompressedData;
	}

	/**
	 *
	 * @return decompressedData of Section
	 */
	public ByteBuffer getDecompressedData() {
		return decompressedData;
	}


}

/**
 * A gap structure contains the size, parent, left, right and zero
 * of the gap
 *
 */
class Gap {
	private int size;

	private int parent;

	private int left;

	private int right;

	private int zero;

	public int getParent() {
		return parent;
	}

	/**
	 * Sets parent of section
	 *
	 * @param parent
	 */
	public void setParent(int parent) {
		this.parent = parent;
	}

	/**
	 *
	 * @return left of Section
	 */
	public int getLeft() {
		return left;
	}

	/**
	 * Sets left of section
	 *
	 * @param left
	 */
	public void setLeft(int left) {
		this.left = left;
	}

	/**
	 *
	 * @return right of Section
	 */
	public int getRight() {
		return right;
	}

	/**
	 * Sets right of section
	 *
	 * @param right
	 */
	public void setRight(int right) {
		this.right = right;
	}

	/**
	 *
	 * @return zero of Section
	 */
	public int getZero() {
		return zero;
	}

	/**
	 * Sets zero of section
	 *
	 * @param zero
	 */
	public void setZero(int zero) {
		this.zero = zero;
	}

	/**
	 *
	 * @return size of Section
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Sets size of section
	 *
	 * @param size
	 */
	public void setSize(int size) {
		this.size = size;
	}
}
