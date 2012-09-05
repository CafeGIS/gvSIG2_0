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

import org.gvsig.dwg.lib.ByteUtils;
import org.gvsig.dwg.lib.CorruptedDwgEntityException;
import org.gvsig.dwg.lib.DwgClass;
import org.gvsig.dwg.lib.DwgFile;
import org.gvsig.dwg.lib.DwgHandleReference;
import org.gvsig.dwg.lib.DwgObject;
import org.gvsig.dwg.lib.DwgObjectFactory;
import org.gvsig.dwg.lib.DwgObjectOffset;
import org.gvsig.dwg.lib.DwgUtil;
import org.gvsig.dwg.lib.util.HexUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * The DwgFileV15Reader reads the DWG version 15 format
 *
 * @author jmorell
 */
public class DwgFileV15Reader implements IDwgFileReader {
	protected DwgFile dwgFile;

	protected ByteBuffer bb;
	
	private static Logger logger = LoggerFactory.getLogger(DwgFileV15Reader.class
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
			//checkSuperentities();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}

	}

	/*
	 *TODO: Eliminar esto cuando terminemos con el testeo
	 */
	protected void checkSuperentities(){
		logger.info("***** CHEQUEANDO LAS SUPERENTIDADES *****");
		List objects = dwgFile.getDwgObjects();
		DwgObject obj;
		DwgObject superEnt;
		boolean buscado=false;
		boolean perdidos=false;
		int j=0;
		for (int i=0; i<objects.size(); i++){
			obj = (DwgObject)objects.get(i);
			if (obj.hasSubEntityHandle()){
				buscado = true;
				superEnt = dwgFile.getDwgSuperEntity(obj);
				if (superEnt == null){
					j++;
				}
			}
		}
		if(j!=0) {
			logger.warn("+++++ SE HAN PERDIDO LAS SUPERENTIDADES DE "+ j+" OBJETOS +++++");
		}
		logger.info("***** FIN DEL CHEQUEO DE LAS SUPERENTIDADES *****");

	}


	/**
	 * It read the SECTIONS from the header of the DWG file
	 */
	protected void readDwgSectionOffsets() {
		bb.position(19);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		short codePage = bb.getShort();
		int count = bb.getInt();
		for (int i = 0; i < count; i++) {
			byte rec = bb.get();
			int seek = bb.getInt();
			int size = bb.getInt();
			if (rec == 0) {
				dwgFile.addDwgSectionOffset("HEADERS", seek, size);
			} else if (rec == 1) {
				dwgFile.addDwgSectionOffset("CLASSES", seek, size);
			} else if (rec == 2) {
				dwgFile.addDwgSectionOffset("OBJECTS", seek, size);
			} else if (rec == 3) {
				dwgFile.addDwgSectionOffset("UNKNOWN", seek, size);
			} else if (rec == 4) {
				dwgFile.addDwgSectionOffset("R14DATA", seek, size);
			} else if (rec == 5) {
				dwgFile.addDwgSectionOffset("R14REC5", seek, size);
			} else {
				//				System.out.println("ERROR: C�digo de n�mero de registro
				// no soportado: " + rec);
			}
		}
	}

	/**
	 * OpenDWG spec says: This section -object map- is a table which gives the
	 * location of each object in the DWG file. This table is broken into
	 * sections. It is basically a list of handle/file loc pairs. It could be
	 * readed with this pseudocode:
	 *
	 * Set lastHandle to all 0 and last loc to 0L. Repeat until section size ==
	 * 2 section size = read short (in bigendian order) Repeat until out of data
	 * for this section offset of this handle form last handle as modular char
	 * offset of location in file from last location as modular char End repeat
	 * End repeat
	 *
	 */
	protected void readDwgObjectOffsets() throws Exception {
		int offset = dwgFile.getDwgSectionOffset("OBJECTS");
		bb.position(offset);
		while (true) {
			bb.order(ByteOrder.BIG_ENDIAN);
			/*
			 * We read the size of the next section. If size == 2, break (it is
			 * the last empty -except crc- section)
			 */
			short size = bb.getShort();
			if (size == 2)
				break;
			bb.order(ByteOrder.LITTLE_ENDIAN);
			byte[] dataBytes = new byte[size];
			bb.get(dataBytes);
			int[] data = DwgUtil.bytesToMachineBytes(dataBytes);
			int lastHandle = 0;
			int lastLoc = 0;
			int bitPos = 0;
			int bitMax = (size - 2) * 8;
			while (bitPos < bitMax) {
				ArrayList v = DwgUtil.getModularChar(data, bitPos);
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

	protected void readDwgClasses2() throws Exception {
		int offset = dwgFile.getDwgSectionOffset("CLASSES");
		// Por ahora nos saltamos los 16 bytes de control
		bb.position(offset + 16);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		int size = bb.getInt();
		byte[] dataBytes = new byte[size];
		for (int i = 0; i < dataBytes.length; i++) {
			dataBytes[i] = bb.get();
		}
		int[] data = DwgUtil.bytesToMachineBytes(dataBytes);
		for (int i = 0; i < data.length; i++) {
			data[i] = (byte) ByteUtils.getUnsigned((byte) data[i]);
		}
		bb.position(bb.position() + 2 + 16);
		int maxbit = size * 8;
		int bitPos = 0;
		while ((bitPos + 8) < maxbit) {
			ArrayList v = DwgUtil.getBitShort(data, bitPos);
			bitPos = ((Integer) v.get(0)).intValue();
			v = DwgUtil.getBitShort(data, bitPos);
			bitPos = ((Integer) v.get(0)).intValue();
			v = DwgUtil.getTextString(data, bitPos);
			bitPos = ((Integer) v.get(0)).intValue();
			v = DwgUtil.getTextString(data, bitPos);
			bitPos = ((Integer) v.get(0)).intValue();
			v = DwgUtil.getTextString(data, bitPos);
			bitPos = ((Integer) v.get(0)).intValue();
			v = DwgUtil.testBit(data, bitPos);
			bitPos = ((Integer) v.get(0)).intValue();
			v = DwgUtil.getBitShort(data, bitPos);
			bitPos = ((Integer) v.get(0)).intValue();
		}
	}

	/**
	 * Reads all the object referenced in the object map section of the DWG file
	 * (using their object file obsets)
	 */
	protected void readDwgObjects() {
		for (int i = 0; i < dwgFile.getDwgObjectOffsets().size(); i++) {
				 try{
					DwgObjectOffset doo = (DwgObjectOffset) dwgFile
							.getDwgObjectOffsets().get(i);

					DwgObject obj = readDwgObject(doo.getOffset(), i);
					/*
					 * azabala: las entidades DWG no implementadas no nos aportan nada
					 * (aunque la sigo leyendo por si aparecen problemas de puntero de
					 * fichero) No considero por tanto los DwgObject if (obj != null) {
					 * dwgFile.addDwgObject(obj); }
					 *
					 * paco: propongo reconsiderar esto. Si no cargamos todos los objetos
					 * podemos tener problemas con las subentities.
					 */
					if (obj != null && obj.getClass() != DwgObject.class) {
						dwgFile.addDwgObject(obj);
					}
				}catch(Exception e){
					logger.error(e.getMessage());
					continue;
				}
			}//for

	}



	/**
	 * Reads the header of an object in a DWG file Version 15
	 *
	 * @param data
	 *            Array of unsigned bytes obtained from the DWG binary file
	 * @param offset
	 *            The current bit offset where the value begins
	 * @return int New offset
	 */
	public int readObjectHeader(int[] data, int offset, DwgObject dwgObject) {
		int bitPos = offset;
		Integer mode = (Integer) DwgUtil.getBits(data, 2, bitPos);
		bitPos = bitPos + 2;
		dwgObject.setMode(mode.intValue());

		/*
		ArrayList v = DwgUtil.getBitLong(data, bitPos);
		bitPos = ((Integer) v.get(0)).intValue();
		int rnum = ((Integer) v.get(1)).intValue();
		dwgObject.setNumReactors(rnum);
		*/
		ArrayList v = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer) v.get(0)).intValue();
		int rnum = ((Integer) v.get(1)).intValue();
		dwgObject.setNumReactors(rnum);

		v = DwgUtil.testBit(data, bitPos);
		bitPos = ((Integer) v.get(0)).intValue();
		boolean nolinks = ((Boolean) v.get(1)).booleanValue();
		dwgObject.setNoLinks(nolinks);

		v = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer) v.get(0)).intValue();
		int color = ((Integer) v.get(1)).intValue();
		dwgObject.setColor(color);

		v = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer) v.get(0)).intValue();
		float ltscale = ((Double) v.get(1)).floatValue();

		Integer ltflag = (Integer) DwgUtil.getBits(data, 2, bitPos);
		bitPos = bitPos + 2;

		Integer psflag = (Integer) DwgUtil.getBits(data, 2, bitPos);
		bitPos = bitPos + 2;

		v = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer) v.get(0)).intValue();
		int invis = ((Integer) v.get(1)).intValue();

		v = DwgUtil.getRawChar(data, bitPos);
		bitPos = ((Integer) v.get(0)).intValue();
		int weight = ((Integer) v.get(1)).intValue();

		return bitPos;
	}

	/**
	 * Reads the tail of an object in a DWG file Version 15
	 *
	 * @param data
	 *            Array of bytes obtained from the DWG binary file
	 * @param offset
	 *            Offset for this array of bytes
	 * @return int New offset
	 * @throws CorruptedDwgEntityException
	 * @throws RuntimeException
	 */
	public int readObjectTailer(int[] data, int offset, DwgObject dwgObject) throws RuntimeException, CorruptedDwgEntityException {
		int bitPos = offset;
		/*
		 * Subentity ref handle. Esto se aplica sobre VERTEX, ATTRIB, SEQEND
		 */
		if (dwgObject.getMode() == 0x0) {
			DwgHandleReference subEntityHandle = new DwgHandleReference();
			bitPos = subEntityHandle.read(data, bitPos);
			dwgObject.setSubEntityHandle(subEntityHandle);
		}

		/*
		 * Reactors handles TODO No se est�n usando para setear nada en
		 * DwgObject
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

		/*
		 * Layer Handle code
		 */

		DwgHandleReference handle = new DwgHandleReference();
		bitPos = handle.read(data, bitPos);
		dwgObject.setLayerHandle(handle);

		if (!dwgObject.isNoLinks()) {

			DwgHandleReference previousHandle = new DwgHandleReference();
			bitPos = previousHandle.read(data, bitPos);
			dwgObject.setPreviousHandle(previousHandle);

			DwgHandleReference nextHandle = new DwgHandleReference();
			bitPos = nextHandle.read(data, bitPos);
			dwgObject.setNextHandle(nextHandle);

		}

		if (dwgObject.getLinetypeFlags() == 0x3) {
			DwgHandleReference lineTypeHandle = new DwgHandleReference();
			bitPos = lineTypeHandle.read(data, bitPos);
			dwgObject.setLineTypeHandle(lineTypeHandle);

		}

		if (dwgObject.getPlotstyleFlags() == 0x3) {
			DwgHandleReference plotStyleHandle = new DwgHandleReference();
			bitPos = plotStyleHandle.read(data, bitPos);
			dwgObject.setPlotStyleHandle(plotStyleHandle);

		}
		return bitPos;
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


	/**
	 * Reads a dwg drawing entity (dwg object) given its offset in the file
	 */

	protected DwgObject readDwgObject(int offset, int index) {
		DwgObject obj = null;
		try {
			bb.position(offset);
			int size = DwgUtil.getModularShort(bb);

			bb.order(ByteOrder.LITTLE_ENDIAN);
			byte[] dataBytes = new byte[size];
			String[] dataMachValString = new String[size];
			int[] data = new int[size];
			for (int i = 0; i < size; i++) {
				dataBytes[i] = bb.get();
				dataMachValString[i] = HexUtil
						.bytesToHex(new byte[] { dataBytes[i] });
				Integer dataMachValShort = Integer.decode("0x"
						+ dataMachValString[i]);
				data[i] = dataMachValShort.byteValue();
				data[i] = ByteUtils.getUnsigned((byte) data[i]);
			}

			int bitPos = 0;
			ArrayList v = DwgUtil.getBitShort(data, bitPos);
			bitPos = ((Integer) v.get(0)).intValue();
			int type = ((Integer) v.get(1)).intValue();
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


			v = DwgUtil.getRawLong(data, bitPos);
			bitPos = ((Integer) v.get(0)).intValue();
			int objBSize = ((Integer) v.get(1)).intValue();
			obj.setSizeInBits(objBSize);

			DwgHandleReference entityHandle = new DwgHandleReference();
			bitPos = entityHandle.read(data, bitPos);
			obj.setHandle(entityHandle);

			v = DwgUtil.readExtendedData(data, bitPos);
			bitPos = ((Integer) v.get(0)).intValue();
			ArrayList extData = (ArrayList) v.get(1);
			obj.setExtendedData(extData);

			boolean gflag = false;
			gflag = obj.isGraphicsFlag();
			if (gflag) {
				//lee un flag boolean
				v = DwgUtil.testBit(data, bitPos);
				bitPos = ((Integer) v.get(0)).intValue();
				boolean val = ((Boolean) v.get(1)).booleanValue();
				//si hay imagen asociada, se lee por completo
				if (val) {
					v = DwgUtil.getRawLong(data, bitPos);
					bitPos = ((Integer) v.get(0)).intValue();
					size = ((Integer) v.get(1)).intValue();
					int bgSize = size * 8;
					Integer giData = (Integer) DwgUtil.getBits(data, bgSize,
							bitPos);
					obj.setGraphicData(giData.intValue());
					bitPos = bitPos + bgSize;
				}
			}
				if(obj.getClass() != DwgObject.class)
					readSpecificObject(obj, data, bitPos);
		} catch (RuntimeException e) {
			e.printStackTrace();
		} catch (CorruptedDwgEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return obj;
	}

	/*
	 * TODO Esto está pesimamente diseñado. Cada objeto DwgObject debe tener un
	 * metodo readSpecificObject(data,bitPos)
	 *
	 */
	protected void readSpecificObject(DwgObject obj, int[] data, int bitPos)
			throws RuntimeException, CorruptedDwgEntityException {
			DwgObjectReaderPool pool = DwgObjectReaderPool.getInstance();
			IDwgObjectReader reader = pool.get(obj, "15");
			if(reader != null){
				reader.setFileReader(this);
				reader.readSpecificObj(data, bitPos, obj);
			}else{
				logger.warn("No se ha implementado la lectura de "+obj.getClass().getName()+", code="+obj.getType());
			}
	}


	//TODO PROBAR, ESTA COPIADO A PELO DE DWG13-14. SI VALE, LLEVAR A UNA
	//CLASE ABSTRACTA
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

//			val = DwgUtil.getBitShort(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("SHORT1", val.get(1));

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

//			val = DwgUtil.testBit(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("DIMSAV", val.get(1));

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

//			val = DwgUtil.testBit(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("BLIPMODE", val.get(1));

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

//			val = DwgUtil.testBit(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("ATTREQ", val.get(1));
//
//			val = DwgUtil.testBit(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("ATTDIA", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("MIRRTEXT", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("WORLDVIEW", val.get(1));

//			val = DwgUtil.testBit(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("WIREFRAME", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("TILEMODE", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("PLIMCHECK", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("VISRETAIN", val.get(1));

//			val = DwgUtil.testBit(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("DELOBJ", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DISPSILH", val.get(1));

			val = DwgUtil.testBit(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("PELLISE", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
//			if (dwgFile.getDwgVersion() == "R14")
			dwgFile.setHeader("PROXYGRAPH", val.get(1));
//			else
//				dwgFile.setHeader("SAVEIMAGES", val.get(1));

//			val = DwgUtil.getBitShort(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("DRAGMODE", val.get(1));

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

//			val = DwgUtil.getBitShort(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("OSMODE", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("ATTMODE", val.get(1));

//			val = DwgUtil.getBitShort(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("COORDS", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("PDMODE", val.get(1));

//			val = DwgUtil.getBitShort(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("PICKSTYLE", val.get(1));

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

			//this property is exclusive of DWG 2000
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

			///DWG 2000 ONLY

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


			//COMMON
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

			//DWG 2000

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


			//Not readed in dwg 2000

//			val = DwgUtil.testBit(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("DIMTOL", val.get(1));
//
//			val = DwgUtil.testBit(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("DIMLIM", val.get(1));
//
//			val = DwgUtil.testBit(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("DIMTIH", val.get(1));
//
//			val = DwgUtil.testBit(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("DIMTOH", val.get(1));
//
//			val = DwgUtil.testBit(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("DIMSE1", val.get(1));
//
//			val = DwgUtil.testBit(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("DIMTSE2", val.get(1));
//
//			val = DwgUtil.testBit(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("DIMALT", val.get(1));
//
//			val = DwgUtil.testBit(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("DIMTOFL", val.get(1));
//
//			val = DwgUtil.testBit(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("DIMSAH", val.get(1));
//
//			val = DwgUtil.testBit(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("DIMTIX", val.get(1));
//
//			val = DwgUtil.testBit(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("DIMSOXD", val.get(1));
//
//			val = DwgUtil.getRawChar(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("DIMALTD", val.get(1));
//
//			val = DwgUtil.getRawChar(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("DIMZIN", val.get(1));
//
//			val = DwgUtil.testBit(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("DIMSD1", val.get(1));
//
//			val = DwgUtil.testBit(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("DIMSD2", val.get(1));
//
//			val = DwgUtil.getRawChar(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("DIMTOLJ", val.get(1));
//
//			val = DwgUtil.getRawChar(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("DIMJUST", val.get(1));
//
//			val = DwgUtil.getRawChar(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("DIMFINT", val.get(1));
//
//			val = DwgUtil.testBit(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("DIMUPT", val.get(1));
//
//			val = DwgUtil.getRawChar(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("DIMZIN", val.get(1));
//
//			val = DwgUtil.getRawChar(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("DIMALTZ", val.get(1));
//
//			val = DwgUtil.getRawChar(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("DIMALTTZ", val.get(1));
//
//			val = DwgUtil.getRawChar(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("DIMTAD", val.get(1));
//
//			val = DwgUtil.getBitShort(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("DIMUNIT", val.get(1));
//
//			val = DwgUtil.getBitShort(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("DIMAUNIT", val.get(1));
//
//			val = DwgUtil.getBitShort(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("DIMDEC", val.get(1));
//
//			val = DwgUtil.getBitShort(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("DIMTDEC", val.get(1));
//
//			val = DwgUtil.getBitShort(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("DIMALTU", val.get(1));
//
//			val = DwgUtil.getBitShort(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("DIMALTTD", val.get(1));
//
//			val = DwgUtil.getHandle(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			intHandle = DwgUtil.handleToInt(val);
//			dwgFile.setHeader("DIMTXSTY", new Integer(intHandle));

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

			//DWG 2000 only

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

			//common

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

			//not readed in DWG 2000

//			val = DwgUtil.getTextString(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("DIMPOST", val.get(1));
//
//			val = DwgUtil.getTextString(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("DIMAPOST", val.get(1));
//
//			val = DwgUtil.getTextString(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("DIMBLK", val.get(1));
//
//			val = DwgUtil.getTextString(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("DIMBLK1", val.get(1));
//
//			val = DwgUtil.getTextString(intData, bitPos);
//			bitPos = ((Integer) val.get(0)).intValue();
//			dwgFile.setHeader("DIMBLK2", val.get(1));

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

			//common
			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMCLRD", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMCLRE", val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("DIMCLRT", val.get(1));

			//dwg 2000
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

			//common
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
			dwgFile.setHeader("LINETYPE_CONTROL_OBJECT",new Integer(intHandle));

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
			dwgFile.setHeader("DIMSTYLE_CONTROL_OBJECT",
							new Integer(intHandle));

			//TODO Se lee para 2000? (en la doc. dice r13-r15)
			//Se supone que sí r15 == 2000
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
			dwgFile.setHeader("NAMED_OBJECT_DICTIONARY",
							new Integer(intHandle));

			//only dwg 2000

			//unknown bitshort
			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();

//			unknown bitshort
			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();

			val = DwgUtil.getTextString(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("HYPERLINKBASE",
					val.get(1));

			val = DwgUtil.getTextString(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("STYLESHEET",
					val.get(1));

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			intHandle = DwgUtil.handleToInt(val);
			dwgFile.setHeader("LAYOUT_DICTIONARY",
							new Integer(intHandle));

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			intHandle = DwgUtil.handleToInt(val);
			dwgFile.setHeader("PLOT_SETTINGS_DICTIONARY",
							new Integer(intHandle));

			val = DwgUtil.getHandle(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			intHandle = DwgUtil.handleToInt(val);
			dwgFile.setHeader("PLOT_STYLES_DICTIONARY",
							new Integer(intHandle));

			/*
			 Flags:
				CELWEIGHT	Flags & 0x001F
				ENDCAPS		Flags & 0x0060
				JOINSTYLE	Flags & 0x0180
				LWDISPLAY	!(Flags & 0x0200)
				XEDIT		!(Flags & 0x0400)
				EXTNAMES	Flags & 0x0800
				PSTYLEMODE	Flags & 0x2000
				OLESTARTUP	Flags & 0x4000
			 * */
			val = DwgUtil.getBitLong(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("FLAGS",
					val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("INSUNITS",
					val.get(1));

			val = DwgUtil.getBitShort(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("CEPSNTYPE",
					val.get(1));

			if(((Integer)val.get(1)).intValue() == 3){
				val = DwgUtil.getHandle(intData, bitPos);
				bitPos = ((Integer) val.get(0)).intValue();
				intHandle = DwgUtil.handleToInt(val);
				dwgFile.setHeader("CPSNID",
								new Integer(intHandle));
			}

			val = DwgUtil.getTextString(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("FINGERPRINTGUID",
					val.get(1));

			val = DwgUtil.getTextString(intData, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			dwgFile.setHeader("VERSIONGUID",
					val.get(1));


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




}