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
package org.gvsig.dwg.lib;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.gvsig.dwg.lib.objects.DwgBlockHeader;
import org.gvsig.dwg.lib.objects.DwgInsert;
import org.gvsig.dwg.lib.objects.DwgLayer;
import org.gvsig.dwg.lib.readers.DwgFileV12Reader;
import org.gvsig.dwg.lib.readers.DwgFileV14Reader;
import org.gvsig.dwg.lib.readers.DwgFileV15Reader;
import org.gvsig.dwg.lib.readers.DwgFileVR2004Reader;
import org.gvsig.dwg.lib.readers.IDwgFileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The DwgFile class provides a revision-neutral interface for reading and handling
 * DWG files
 * Reading methods are useful for reading DWG files, and handling methods like
 * calculateDwgPolylines() are useful for handling more complex
 * objects in the DWG file
 *
 * @author jmorell
 * @author azabala
 */
public class DwgFile {
	/**
	 * It has all known DWG version's header.
	 * Extracted from Autodesk web.
	 */
	private static HashMap acadVersions = new HashMap();
	static{
		acadVersions.put("AC1004", "Autocad R9");
		acadVersions.put("AC1006", "Autocad R10");
		acadVersions.put("AC1007", "Autocad pre-R11");
		acadVersions.put("AC1007", "Autocad pre-R11");
		acadVersions.put("AC1008", "Autocad pre-R11b");
		acadVersions.put("AC1009", "Autocad R12");
		acadVersions.put("AC1010", "Autocad pre-R13 a");
		acadVersions.put("AC1011", "Autocad pre-R13 b");
		acadVersions.put("AC1012", "Autocad R13");
		acadVersions.put("AC1013", "Autocad pre-R14");
		acadVersions.put("AC1014", "Autocad R14");
		acadVersions.put("AC1500", "Autocad pre-2000");
		acadVersions.put("AC1015", "Autocad R2000, R2000i, R2002");
		acadVersions.put("AC402a", "Autocad pre-2004a");
		acadVersions.put("AC402b", "Autocad pre-2004b");
		acadVersions.put("AC1018", "Autocad R2004, R2005, R2006");
		acadVersions.put("AC1021", "Autocad R2007");

	}

	private static Logger logger = LoggerFactory.getLogger(DwgFile.class.getName());
	/**
	 * Path and name of the dwg file
	 * */
	private String fileName;
	/**
	 * DWG version of the file (AC1013, AC1018, etc.)
	 * */
	private String dwgVersion;

	/**
	 * Offsets to the DWG sections
	 */
	private ArrayList dwgSectionOffsets;
	/**
	 * Header vars readed from the HEADER section of the DWG file
	 * */
	private Map headerVars;

	/**
	 * This list contains what in OpenDWG specification is called
	 * the object map ( a collection of entries where each entry contains
	 * the seek of each object, and its size)
	 * */
	private ArrayList dwgObjectOffsets;

	/**
	 * For each entry in dwgObjectOffsets, we have an instance of
	 * DwgObject in the dwgObjects collection
	 *
	 * */
	private List dwgObjects;
	private HashMap handle_objects;


	private ArrayList dwgClasses;


	/**
	 * hash map that indexes all DwgLayer objects
	 * by its handle property
	 * */
	private HashMap layerTable;

	/**
	 * Specific reader of the DWG file version (12, 13, 14, 2000, etc., each
	 * version will have an specific reader)
	 * */
	private IDwgFileReader dwgReader;
	private boolean dwg3DFile = false;
	/**
	 * Memory mapped byte buffer of the whole DWG file
	 * */
	private ByteBuffer bb;

	/*
	 * Optimizaciones para evitar leer el fichero completo en cada
	 * pasada.
	 * */
	/**
	 * Contains all IDwgPolyline implementations
	 * */
	private List dwgPolylines;
	/**
	 * Contains all INSERT entities of the dwg file
	 * (these entities must be processed in a second pass)
	 *
	 * */
	private List insertList;

	private List blockList;


	/**
	 * Constructor.
	 * @param fileName an absolute path to the DWG file
	 */
	public DwgFile(String fileName) {
		this.fileName = fileName;

		dwgSectionOffsets = new ArrayList();
		dwgObjectOffsets = new ArrayList();
		headerVars = new HashMap();

		dwgClasses = new ArrayList();

		dwgObjects = new ArrayList();
		handle_objects = new HashMap();

		layerTable = new HashMap();

		dwgPolylines = new ArrayList();
		insertList = new ArrayList();

		blockList = new ArrayList();

	}

	public String getDwgVersion() {
		return dwgVersion;
	}

	/**
	 * Reads a DWG file and put its objects in the dwgObjects Vector
	 * This method is version independent
	 *
	 * @throws IOException If the file location is wrong
	 */
	public void read() throws IOException,
				DwgVersionNotSupportedException
	{
		setDwgVersion();
		if (dwgVersion.equalsIgnoreCase("Autocad R2000, R2000i, R2002")) {
			dwgReader = new DwgFileV15Reader();
		}else if (dwgVersion.equalsIgnoreCase("Autocad pre-R14")||
				dwgVersion.equalsIgnoreCase("Autocad R14") ||
				dwgVersion.equalsIgnoreCase("Autocad R13")) {
			dwgReader = new DwgFileV14Reader();
		}else if (dwgVersion.equalsIgnoreCase("Autocad R2004, R2005, R2006")){
			dwgReader = new DwgFileVR2004Reader();
		} else if (dwgVersion.equalsIgnoreCase("Autocad R12")||
				dwgVersion.equalsIgnoreCase("Autocad pre-R13 a") ||
				dwgVersion.equalsIgnoreCase("Autocad pre-R13 b")) {

			boolean isR13 = true;
			if(dwgVersion.equalsIgnoreCase("Autocad R12"))
					isR13 = false;
			dwgReader = new DwgFileV12Reader(isR13);
		}else {
			DwgVersionNotSupportedException exception =
				new DwgVersionNotSupportedException("Version de DWG no soportada");
			exception.setDwgVersion(dwgVersion);
			throw exception;
		}
		try{
		dwgReader.read(this, bb);
		}catch(Throwable t){
			t.printStackTrace();
			throw new IOException("Error leyendo dwg");
		}
	}

	private void setDwgVersion() throws IOException {
		File file = new File(fileName);
		FileInputStream fileInputStream = new FileInputStream(file);
		FileChannel fileChannel = fileInputStream.getChannel();
		long channelSize = fileChannel.size();
		bb = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, channelSize);
		byte[] versionBytes = {bb.get(0),
								bb.get(1),
								bb.get(2),
								bb.get(3),
								bb.get(4),
								bb.get(5)};
		ByteBuffer versionByteBuffer = ByteBuffer.wrap(versionBytes);
		String[] bs = new String[versionByteBuffer.capacity()];
		String versionString = "";
		for (int i=0; i<versionByteBuffer.capacity(); i++) {
			bs[i] = new String(new byte[]{(byte)(versionByteBuffer.get(i))});
			versionString = versionString + bs[i];
		}
		String version = (String) acadVersions.get(versionString);
		if(version == null)
			version = "Unknown Dwg format";
		this.dwgVersion = version;
	}

	public void setHeader(String key, Object value){
		headerVars.put(key, value);
	}

	public Object getHeader(String key){
		return headerVars.get(key);
	}

	/*
	 * A DwgLayer is a DWG drawing entity, so all DwgLayer objects are
	 * saved in dwgObjects collection.
	 * Instead of iterate over the full collection, is faster check every object
	 * and save all the DwgLayer instances in their own collection.
	 *
	 */
	protected void addDwgLayer(DwgLayer dwgLayer){
		layerTable.put(new Integer(dwgLayer.getHandle().getOffset()),
				dwgLayer);
	}

	private void printInfoOfAObject (DwgObject entity){
		logger.info("index = "+entity.getIndex() + " entity.type = " + entity.type + " entityClassName = "+ entity.getClass().getName());
		logger.info("handleCode = "+entity.getHandle().getCode());
		logger.info("entityLayerHandle = "+entity.getHandle().getOffset());
		if(entity.hasLayerHandle()){
			logger.info("layerHandleCode = "+entity.getLayerHandle().getCode());
			logger.info("layerHandle = "+entity.getLayerHandle().getOffset());
		}
		if(entity.hasSubEntityHandle()){
			logger.info("subEntityHandleCode = "+entity.getSubEntityHandle().getCode());
			logger.info("subEntityHandle = "+entity.getSubEntityHandle().getOffset());
		}
		if(entity.hasNextHandle()){
			logger.info("nextHandleCode = "+entity.getNextHandle().getCode());
			logger.info("nextHandle = "+entity.getNextHandle().getOffset());
		}
		if(entity.hasPreviousHandle()){
			logger.info ("previousHandleCode = "+entity.getPreviousHandle().getCode());
			logger.info ("previousHandle = "+entity.getPreviousHandle().getOffset());
		}
		if(entity.hasXDicObjHandle()){
			logger.info ("xDicObjHandleCode = "+entity.getXDicObjHandle());
			logger.info ("xDicObjHandle = "+entity.getXDicObjHandle());
		}
		if(entity.hasReactorsHandles()){
			ArrayList reactorsHandles = entity.getReactorsHandles();
			int size = reactorsHandles.size();
			logger.info ("NUMERO DE reactors = "+size);
			DwgHandleReference hr;
			for(int i=0; i<size; i++){
				hr=(DwgHandleReference)reactorsHandles.get(i);
				logger.info ("reactorHandleCode = "+hr.getCode());
				logger.info (" reactorHandle = "+hr.getOffset());
			}
		}

	}
	public DwgLayer getDwgLayer(DwgObject entity){
		DwgHandleReference handle = entity.getLayerHandle();
		if(handle == null){
			return null;
		}
		int	handleCode = handle.getCode();
		int	entityLayerHandle = entity.getLayerHandle().getOffset();
		int layerHandle = -1;

		int entityRecord;
		DwgObject object;


		/*
		 * OpenDWG spec, v3, page 11. HANDLE REFERENCES
		 *
		 * A handle is formed by this fields:
		 * a) CODE (4 bits)
		 * b) COUNTER (4 bits)
		 * c) OFFSET of the object (handle itself).
		 *
		 * CODE could take these values:
		 * 1) 0x2, 0x3, 0x4, 0x5 -> offset is the handle of the layer
		 * 2) 0x6 -> offset is the next object handle
		 * 3) 0x8 -> offset is the previous object handle
		 * 4) 0xA -> result is reference handle plus offset
 		 * 5) 0xC -> result is reference handle minus offset
		 *
		 * */

		// FIXME: Esto no esta terminado. Falta considerar los codigo
		// 0x2, 0x3, 0x6, 0xA que no han aparecido en los archivos de prueba.
		switch(handleCode){
		case 0x4:
			if (entity.hasNextHandle()){
				int nextHandleCode = entity.getNextHandle().getCode();
				if (nextHandleCode == 0x5) {
					layerHandle = entity.getNextHandle().getOffset();
				} else {
					//TODO: No se han previsto nextHandleCode != 0x5
//					System.out.println ("DwgFile.getDwgLayer: handleCode "+handleCode+
//							" con nextHandleCode "+ nextHandleCode +" no implementado.");
				}
			} else {
				layerHandle = entity.getLayerHandle().getOffset();
			}
			break;
		case 0x5:
			layerHandle = entity.getLayerHandle().getOffset();
			break;
		case 0x8:
			if (entity.hasNextHandle()){
				int nextHandleCode = entity.getNextHandle().getCode();
				if (nextHandleCode == 0x5) {
					layerHandle = entity.getNextHandle().getOffset();
				} else {
					//TODO No se han previsto nextHandleCode != 0x5
//					System.out.println ("DwgFile.getDwgLayer: handleCode "+handleCode+
//							" con nextHandleCode "+ nextHandleCode +" no implementado.");
				}
			} else {
				layerHandle = entity.getHandle().getOffset() - 1;
			}
			break;
		case 0xC:
			if (entity.hasNextHandle()){
				int nextHandleCode = entity.getNextHandle().getCode();
				if (nextHandleCode == 0x5) {
					layerHandle = entity.getNextHandle().getOffset();
				} else {
					//TODO: No se han previsto nextHandleCode != 0x5
//					System.out.println ("DwgFile.getDwgLayer: handleCode "+handleCode+
//							" con nextHandleCode "+ nextHandleCode +" no implementado.");
				}
			} else {
				layerHandle = entity.getHandle().getOffset() - entity.getLayerHandle().getOffset() + 1;
			}
			break;
		default:
//			System.out.println ("DwgFile.getDwgLayer: handleCode "+handleCode+" no implementado. entityLayerHandle="+entityLayerHandle);
		}

		if(layerHandle != -1){
			Iterator lyrIterator = layerTable.values().iterator();
			while(lyrIterator.hasNext()){
				DwgLayer lyr = (DwgLayer) lyrIterator.next();
				int lyrHdl = lyr.getHandle().getOffset();
				if (lyrHdl == layerHandle){
					return lyr;
				}
			}
		}
//		System.out.println("NO SE HA ENCONTRADO UNA CAPA CON HANDLE " + layerHandle);
//		printInfoOfAObject(entity);
		return null;
	}

	public DwgObject getDwgSuperEntity(DwgObject entity) {
		if(entity.hasSubEntityHandle()){
			int handleCode = entity.subEntityHandle.getCode();
			int offset = entity.subEntityHandle.getOffset();
			int handle = -1;

			DwgObject object;

			switch(handleCode){
			// TODO: case 0x2:
			// TODO: case 0x3:
			case 0x4:
			case 0x5:
				handle = offset;
				break;
			// TODO: case 0x6:
			case 0x8:
				handle=entity.getHandle().getOffset() - 1;
				break;
			case 0xA:
				handle = entity.getHandle().getOffset() + offset;
				break;
			case 0xC:
				handle = entity.getHandle().getOffset() - offset;
				break;
			default:
				logger.warn ("DwgObject.getDwgSuperEntity: handleCode "+handleCode+" no implementado. offset = "+offset);
			}
			if(handle != -1){
				object = getDwgObjectFromHandle(handle);
				if(object != null)
					return object;
			}
		}
		return null;
	}

	public String getLayerName(DwgObject entity) {
		DwgLayer dwgLayer = getDwgLayer(entity);
		if(dwgLayer == null){
			return "";
		}else{
			return dwgLayer.getName();
		}
	}

	/**
     * Returns the color of the layer of a DWG object
	 *
     * @param entity DWG object which we want to know its layer color
	 * @return int Layer color of the DWG object in the Autocad color code
	 */
	public int getColorByLayer(DwgObject entity) {
		int colorByLayer;
		DwgLayer dwgLyr = getDwgLayer(entity);
		if(dwgLyr == null)
			colorByLayer = 0;
		else
			colorByLayer = dwgLyr.getColor();
		return colorByLayer;
	}
	/**
	 * Configure the geometry of the polylines in a DWG file from the vertex list in
	 * this DWG file. This geometry is given by an array of Points.
	 * Besides, manage closed polylines and polylines with bulges in a GIS Data model.
     * It means that the arcs of the polylines will be done through a set of points and
     * a distance between these points.
	 */
	public void calculateGisModelDwgPolylines() {
		/*
		 * In Dwg 12 format, all vertices of a polyline are between polyline entity
		 * and endsec entity. Also, polylines dont save any handle of their vertices (first and last).
		 *
		 * After Dwg 12 version, we have the opposite case: polylines have handles/pointers to their
		 * first and last vertices.
		 *
		 * If dwg file is not V12, we must look for all polyline vertices (and we must to wait until
		 * all entities are readed).
		 * If dwg file is V12, all polylines already have their vertices.
		 * */
		if(! (dwgReader instanceof DwgFileV12Reader)){
			for (int i = 0; i < dwgPolylines.size(); i++){
				DwgObject pol = (DwgObject)dwgPolylines.get(i);
				if (pol instanceof IDwgPolyline) {
					((IDwgPolyline)pol).calculateGisModel(this);
				}//if
			}//for
		}
	}

    /**
     * Configure the geometry of the polylines in a DWG file from the vertex list in
     * this DWG file. This geometry is given by an array of Points
     * Besides, manage closed polylines and polylines with bulges in a GIS Data model.
     * It means that the arcs of the polylines will be done through a curvature
     * parameter called bulge associated with the points of the polyline.
     */
	//TODO Refactorizar para que solo se aplique a las Polilineas

//	public void calculateCadModelDwgPolylines() {
//		for (int i=0;i<dwgObjects.size();i++) {
//			DwgObject pol = (DwgObject)dwgObjects.get(i);
//			if (pol instanceof DwgPolyline2D) {
//				int flags = ((DwgPolyline2D)pol).getFlags();
//				int firstHandle = ((DwgPolyline2D)pol).getFirstVertexHandle().getOffset();
//				int lastHandle = ((DwgPolyline2D)pol).getLastVertexHandle().getOffset();
//				ArrayList pts = new ArrayList();
//				ArrayList bulges = new ArrayList();
//				double[] pt = new double[3];
//				for (int j=0;j<dwgObjects.size();j++) {
//					DwgObject firstVertex = (DwgObject)dwgObjects.get(j);
//					if (firstVertex instanceof DwgVertex2D) {
//						int vertexHandle = firstVertex.getHandle().getOffset();
//						if (vertexHandle==firstHandle) {
//							int k=0;
//							while (true) {
//								DwgObject vertex = (DwgObject)dwgObjects.get(j+k);
//								int vHandle = vertex.getHandle().getOffset();
//								if (vertex instanceof DwgVertex2D) {
//									pt = ((DwgVertex2D)vertex).getPoint();
//									pts.add(new Point2D.Double(pt[0], pt[1]));
//									double bulge = ((DwgVertex2D)vertex).getBulge();
//									bulges.add(new Double(bulge));
//									k++;
//									if (vHandle==lastHandle && vertex instanceof DwgVertex2D) {
//										break;
//									}
//								} else if (vertex instanceof DwgSeqend) {
//									break;
//								}
//							}
//						}
//					}
//				}
//				if (pts.size()>0) {
//					/*Point2D[] newPts = new Point2D[pts.size()];
//					if ((flags & 0x1)==0x1) {
//						newPts = new Point2D[pts.size()+1];
//						for (int j=0;j<pts.size();j++) {
//							newPts[j] = (Point2D)pts.get(j);
//						}
//						newPts[pts.size()] = (Point2D)pts.get(0);
//						bulges.add(new Double(0));
//					} else {
//						for (int j=0;j<pts.size();j++) {
//							newPts[j] = (Point2D)pts.get(j);
//						}
//					}*/
//					double[] bs = new double[bulges.size()];
//					for (int j=0;j<bulges.size();j++) {
//						bs[j] = ((Double)bulges.get(j)).doubleValue();
//					}
//					((DwgPolyline2D)pol).setBulges(bs);
//					//Point2D[] points = GisModelCurveCalculator.calculateGisModelBulge(newPts, bs);
//					Point2D[] points = new Point2D[pts.size()];
//					for (int j=0;j<pts.size();j++) {
//					    points[j] = (Point2D)pts.get(j);
//					}
//					((DwgPolyline2D)pol).setPts(points);
//				} else {
////					System.out.println("Encontrada polilï¿½nea sin puntos ...");
//					// TODO: No se debe mandar nunca una polilï¿½nea sin puntos, si esto
//					// ocurre es porque existe un error que hay que corregir ...
//				}
//			} else if (pol instanceof DwgPolyline3D) {
//			} else if (pol instanceof DwgLwPolyline && ((DwgLwPolyline)pol).getVertices()!=null) {
//			}
//		}
//	}

	/*
	 * TODO Revisar los bloques que son XREF
	 * */
	public void blockManagement2(){
		Iterator it = null;


		//dwg v12 blocks already has their entities added
		//for the rest of versions, we add block entities to their owner block
		if(! (dwgReader instanceof DwgFileV12Reader)){
			//once we have read all dwg entities, we look for all of them
			//that has a superentity that is a block (to fill in the entity list
			//of each block
			it = dwgObjects.iterator();
			int i = 0;
			while(it.hasNext()){
				DwgObject entity = (DwgObject)it.next();
				DwgObject superEnt = getDwgSuperEntity(entity);
				if(superEnt instanceof DwgBlockHeader){
					DwgBlockHeader blk = (DwgBlockHeader)superEnt;
					blk.addObject(entity);
					it.remove();//TODO Creo que esto es lento, mejor
					//el metodo original (en java solo se duplican las referencias)
					i++;
				}
			}//while
		}//if dwgfilev12

		//after that, we process the INSERTs
		it = insertList.iterator();
		while(it.hasNext()){
			DwgInsert insert = (DwgInsert) it.next();
			if(insert.isProcessed()){
				//It has been processed nexted to other insert
				continue;
			}
			insert.setProcessed(true);
			double[] p = insert.getInsertionPoint();
			Point2D point = new Point2D.Double(p[0], p[1]);
			double[] scale = insert.getScale();
			double rot = insert.getRotation();
			int blockHandle = insert.getBlockHeaderHandle().getOffset();
			manageInsert2(point, scale,
					rot, blockHandle,
					dwgObjects, handle_objects);
		}

}



	public void manageInsert2(Point2D insPoint, double[] scale,
			double rot, int bHandle,
			List dwgObjectsWithoutBlocks,
			Map handleObjectsWithoutBlocks) {

		DwgObject object = (DwgObject) handle_objects.get(new Integer(bHandle));
		if(object == null){
			logger.error("No hemos encontrado el BlockHeader cuyo handle es "+bHandle);
			return;
		}else if(! (object instanceof DwgBlockHeader)){
			//Hay un problema con la asignaciï¿½n de handle
			//Un INSERT tiene como handle de Block una entidad que no es block
			logger.error("handle incorrecto." + object.getClass().getName() + " no es un blockheader");
			return;
		}

		DwgBlockHeader blockHeader = (DwgBlockHeader)object;
		double[] bPoint = blockHeader.getBasePoint();
		String bname = blockHeader.getName();
		if (bname.startsWith("*"))
			return;

		//TODO Cambiar por List. Done.
		List entities = blockHeader.getObjects();
		if(entities.size() == 0){
			logger.warn("El bloque "+blockHeader.getName()+" no tiene ninguna entidad");
		}
		Iterator blkEntIt = entities.iterator();
		while(blkEntIt.hasNext()){
			DwgObject obj = (DwgObject) blkEntIt.next();
			 manageBlockEntity(obj, bPoint,
			 		  insPoint, scale,
			 		  rot, dwgObjectsWithoutBlocks,
			 		  handleObjectsWithoutBlocks);
		}//while
	}


	public int getIndexOf(DwgObject dwgObject){
		return dwgObjects.indexOf(dwgObject);
	}



    /**
     * Changes the location of an object extracted from a block. This location will be
     * obtained through the insertion parameters from the block and the corresponding
     * insert.
     * @param entity, the entity extracted from the block.
     * @param bPoint, offset for the coordinates of the entity.
     * @param insPoint, coordinates of the insertion point for the entity.
     * @param scale, scale for the entity.
     * @param rot, rotation angle for the entity.
     * @param id, a count as a id.
     * @param dwgObjectsWithoutBlocks, a object list with the elements extracted from
     * the blocks.
     */
	private void manageBlockEntity(DwgObject entity,
									double[] bPoint,
									Point2D insPoint,
									double[] scale,
									double rot,
									List dwgObjectsWithoutBlocks,
									Map handleObjectsWithoutBlocks) {

		if(entity instanceof IDwgBlockMember){
			IDwgBlockMember blockMember = (IDwgBlockMember)entity;
			blockMember.transform2Block(bPoint, insPoint, scale, rot,
					dwgObjectsWithoutBlocks,
					handleObjectsWithoutBlocks,this);
		}

	}


	/**
	 * Add a DWG section offset to the dwgSectionOffsets vector
	 *
	 * @param key Define the DWG section
	 * @param seek Offset of the section
	 * @param size Size of the section
	 */
	public void addDwgSectionOffset(String key, int seek, int size) {
		DwgSectionOffset dso = new DwgSectionOffset(key, seek, size);
		dwgSectionOffsets.add(dso);
	}

	/**
     * Returns the offset of DWG section given by its key
	 *
     * @param key Define the DWG section
	 * @return int Offset of the section in the DWG file
	 */
	public int getDwgSectionOffset(String key) {
		int offset = 0;
		for (int i=0; i<dwgSectionOffsets.size(); i++) {
			DwgSectionOffset dso = (DwgSectionOffset)dwgSectionOffsets.get(i);
			String ikey = dso.getKey();
			if (key.equals(ikey)) {
				offset = dso.getSeek();
				break;
			}
		}
		return offset;
	}

	/**
	 * Add a DWG object offset to the dwgObjectOffsets vector
	 *
	 * @param handle Object handle
	 * @param offset Offset of the object data in the DWG file
	 */
	public void addDwgObjectOffset(int handle, int offset) {
		DwgObjectOffset doo = new DwgObjectOffset(handle, offset);
		dwgObjectOffsets.add(doo);
	}

	/**
	 *
	 * Add a DWG object to the dwgObject vector
	 *
	 * @param dwgObject DWG object
	 */
	public void addDwgObject(DwgObject dwgObject){
		//TODO Ver si puedo inicializar las listas especificas
		//(IDwgPolyline, etc) aquí
		dwgObjects.add(dwgObject);
		/*
		 * TODO Quitar todos estos if-then y sustituirlos por un metodo callback
		 *
		 *
		 * (dwgObject.init(this), y que cada objeto haga lo que tenga que hacer
		 * */
		if(dwgObject instanceof DwgLayer){
			this.addDwgLayer((DwgLayer) dwgObject);
		}

		//Probamos a no aplicar las extrusiones

		if(dwgObject instanceof IDwgExtrusionable){
			((IDwgExtrusionable)dwgObject).applyExtrussion();

		}

		if(dwgObject instanceof IDwgPolyline){
			dwgPolylines.add(dwgObject);
		}
		if(dwgObject instanceof IDwg3DTestable){
			if(!isDwg3DFile()){//if its true, we dont check again
				setDwg3DFile(((IDwg3DTestable)dwgObject).has3DData());
			}
		}
		if(dwgObject instanceof DwgInsert){
			insertList.add(dwgObject);
		}

		if(dwgObject instanceof DwgBlockHeader){
			blockList.add(dwgObject);
		}
		handle_objects.put(new Integer(dwgObject.getHandle().getOffset()), dwgObject);


	}

	/**
	 * Returns dwgObjects from its insertion order (position
	 * in the dwg file)
	 *
	 * @param index order in the dwg file
	 * @return position
	 * */
	public DwgObject getDwgObject(int index){
		return (DwgObject) dwgObjects.get(index);
	}

	public DwgObject getDwgObjectFromHandle(int handle){
		return (DwgObject) handle_objects.get(new Integer(handle));
	}

	/**
	 * Add a DWG class to the dwgClasses vector
	 *
	 * @param dwgClass DWG class
	 */
	public void addDwgClass(DwgClass dwgClass){
		dwgClasses.add(dwgClass);
	}

	/**
	 * Add a DWG class to the dwgClasses vector
	 *
	 * @param dwgClass DWG class
	 */
	public void addDwgClass(DwgClass2004 dwgClass){
		dwgClasses.add(dwgClass);
	}


	public void printClasses(){
		logger.info("#### CLASSES ####");
		for(int i = 0; i < dwgClasses.size(); i++){
			DwgClass clazz = (DwgClass) dwgClasses.get(i);
			logger.info(clazz.toString());
		}
		logger.info("#############");
	}

	public List getDwgClasses(){
		return dwgClasses;
	}

    /**
     * @return Returns the dwgObjectOffsets.
     */
    public ArrayList getDwgObjectOffsets() {
        return dwgObjectOffsets;
    }
    /**
     * @return Returns the dwgObjects.
     */
    public List getDwgObjects() {
        return dwgObjects;
    }
    /**
     * @return Returns the fileName.
     */
    public String getFileName() {
        return fileName;
    }
    /**
     * @return Returns the dwg3DFile.
     */
    public boolean isDwg3DFile() {
        return dwg3DFile;
    }
    /**
     * @param dwg3DFile The dwg3DFile to set.
     */
    public void setDwg3DFile(boolean dwg3DFile) {
        this.dwg3DFile = dwg3DFile;
    }
}
