/*
 * Created on 09-ene-2007
 *
 * gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
 *   Av. Blasco Ib��ez, 50
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

import java.util.HashMap;
import java.util.Map;

import org.gvsig.dwg.lib.DwgObject;
import org.gvsig.dwg.lib.objects.DwgArc;
import org.gvsig.dwg.lib.objects.DwgAttdef;
import org.gvsig.dwg.lib.objects.DwgAttrib;
import org.gvsig.dwg.lib.objects.DwgBlock;
import org.gvsig.dwg.lib.objects.DwgBlockControl;
import org.gvsig.dwg.lib.objects.DwgBlockHeader;
import org.gvsig.dwg.lib.objects.DwgCircle;
import org.gvsig.dwg.lib.objects.DwgDictionary;
import org.gvsig.dwg.lib.objects.DwgDictionaryVar;
import org.gvsig.dwg.lib.objects.DwgDimOrd;
import org.gvsig.dwg.lib.objects.DwgEllipse;
import org.gvsig.dwg.lib.objects.DwgEndblk;
import org.gvsig.dwg.lib.objects.DwgFace3D;
import org.gvsig.dwg.lib.objects.DwgHatch;
import org.gvsig.dwg.lib.objects.DwgIdBuffer;
import org.gvsig.dwg.lib.objects.DwgImage;
import org.gvsig.dwg.lib.objects.DwgImageDef;
import org.gvsig.dwg.lib.objects.DwgImageDefReactor;
import org.gvsig.dwg.lib.objects.DwgInsert;
import org.gvsig.dwg.lib.objects.DwgLayer;
import org.gvsig.dwg.lib.objects.DwgLayerControl;
import org.gvsig.dwg.lib.objects.DwgLayerIndex;
import org.gvsig.dwg.lib.objects.DwgLine;
import org.gvsig.dwg.lib.objects.DwgLinearDimension;
import org.gvsig.dwg.lib.objects.DwgLwPolyline;
import org.gvsig.dwg.lib.objects.DwgMText;
import org.gvsig.dwg.lib.objects.DwgMeshPolyline;
import org.gvsig.dwg.lib.objects.DwgMinsert;
import org.gvsig.dwg.lib.objects.DwgOle2Frame;
import org.gvsig.dwg.lib.objects.DwgPFacePolyline;
import org.gvsig.dwg.lib.objects.DwgPoint;
import org.gvsig.dwg.lib.objects.DwgPolyline2D;
import org.gvsig.dwg.lib.objects.DwgPolyline3D;
import org.gvsig.dwg.lib.objects.DwgRasterVariables;
import org.gvsig.dwg.lib.objects.DwgSeqend;
import org.gvsig.dwg.lib.objects.DwgSolid;
import org.gvsig.dwg.lib.objects.DwgSortEntStable;
import org.gvsig.dwg.lib.objects.DwgSpline;
import org.gvsig.dwg.lib.objects.DwgText;
import org.gvsig.dwg.lib.objects.DwgVertex2D;
import org.gvsig.dwg.lib.objects.DwgVertex3D;
import org.gvsig.dwg.lib.objects.DwgVertexMesh;
import org.gvsig.dwg.lib.objects.DwgVertexPFace;
import org.gvsig.dwg.lib.objects.DwgVertexPFaceFace;
import org.gvsig.dwg.lib.objects.DwgXRecord;
import org.gvsig.dwg.lib.readers.v1314.DwgArcReader1314;
import org.gvsig.dwg.lib.readers.v1314.DwgAttdefReader1314;
import org.gvsig.dwg.lib.readers.v1314.DwgAttribReader1314;
import org.gvsig.dwg.lib.readers.v1314.DwgBlockControlReader1314;
import org.gvsig.dwg.lib.readers.v1314.DwgBlockHeaderReader1314;
import org.gvsig.dwg.lib.readers.v1314.DwgBlockReader1314;
import org.gvsig.dwg.lib.readers.v1314.DwgCircleReader1314;
import org.gvsig.dwg.lib.readers.v1314.DwgDictionaryReader1314;
import org.gvsig.dwg.lib.readers.v1314.DwgDictionaryVarReader1314;
import org.gvsig.dwg.lib.readers.v1314.DwgDimOrdReader1314;
import org.gvsig.dwg.lib.readers.v1314.DwgEllipseReader1314;
import org.gvsig.dwg.lib.readers.v1314.DwgEndBlkReader1314;
import org.gvsig.dwg.lib.readers.v1314.DwgFace3DReader1314;
import org.gvsig.dwg.lib.readers.v1314.DwgHatchReader1314;
import org.gvsig.dwg.lib.readers.v1314.DwgIdBufferReader1314;
import org.gvsig.dwg.lib.readers.v1314.DwgImageDefReactor1314;
import org.gvsig.dwg.lib.readers.v1314.DwgImageDefReader1314;
import org.gvsig.dwg.lib.readers.v1314.DwgImageReader1314;
import org.gvsig.dwg.lib.readers.v1314.DwgInsertReader1314;
import org.gvsig.dwg.lib.readers.v1314.DwgLayerControlReader1314;
import org.gvsig.dwg.lib.readers.v1314.DwgLayerIndexReader1314;
import org.gvsig.dwg.lib.readers.v1314.DwgLayerReader1314;
import org.gvsig.dwg.lib.readers.v1314.DwgLineReader1314;
import org.gvsig.dwg.lib.readers.v1314.DwgLinearDimensionReader1314;
import org.gvsig.dwg.lib.readers.v1314.DwgLwPolylineReader1314;
import org.gvsig.dwg.lib.readers.v1314.DwgMTextReader1314;
import org.gvsig.dwg.lib.readers.v1314.DwgMeshReader1314;
import org.gvsig.dwg.lib.readers.v1314.DwgMinsertReader1314;
import org.gvsig.dwg.lib.readers.v1314.DwgOle2FrameReader1314;
import org.gvsig.dwg.lib.readers.v1314.DwgPFaceReader1314;
import org.gvsig.dwg.lib.readers.v1314.DwgPointReader1314;
import org.gvsig.dwg.lib.readers.v1314.DwgPolyline2DReader1314;
import org.gvsig.dwg.lib.readers.v1314.DwgPolyline3DReader1314;
import org.gvsig.dwg.lib.readers.v1314.DwgRasterVariablesReader1314;
import org.gvsig.dwg.lib.readers.v1314.DwgSeqEndReader1314;
import org.gvsig.dwg.lib.readers.v1314.DwgSolidReader1314;
import org.gvsig.dwg.lib.readers.v1314.DwgSortEntsTableReader1314;
import org.gvsig.dwg.lib.readers.v1314.DwgSplineReader1314;
import org.gvsig.dwg.lib.readers.v1314.DwgTextReader1314;
import org.gvsig.dwg.lib.readers.v1314.DwgVertex2DReader1314;
import org.gvsig.dwg.lib.readers.v1314.DwgVertex3DReader1314;
import org.gvsig.dwg.lib.readers.v1314.DwgVertexMeshReader1314;
import org.gvsig.dwg.lib.readers.v1314.DwgVertexPFaceFaceReader1314;
import org.gvsig.dwg.lib.readers.v1314.DwgVertexPFaceReader1314;
import org.gvsig.dwg.lib.readers.v1314.DwgXrecordReader1314;
import org.gvsig.dwg.lib.readers.v15.DwgArcReader15;
import org.gvsig.dwg.lib.readers.v15.DwgAttdefReader15;
import org.gvsig.dwg.lib.readers.v15.DwgAttribReader15;
import org.gvsig.dwg.lib.readers.v15.DwgBlockControlReader15;
import org.gvsig.dwg.lib.readers.v15.DwgBlockHeaderReader15;
import org.gvsig.dwg.lib.readers.v15.DwgBlockReader15;
import org.gvsig.dwg.lib.readers.v15.DwgCircleReader15;
import org.gvsig.dwg.lib.readers.v15.DwgDictionaryReader15;
import org.gvsig.dwg.lib.readers.v15.DwgEllipseReader15;
import org.gvsig.dwg.lib.readers.v15.DwgEndBlkReader15;
import org.gvsig.dwg.lib.readers.v15.DwgInsertReader15;
import org.gvsig.dwg.lib.readers.v15.DwgLayerControlReader15;
import org.gvsig.dwg.lib.readers.v15.DwgLayerReader15;
import org.gvsig.dwg.lib.readers.v15.DwgLineReader15;
import org.gvsig.dwg.lib.readers.v15.DwgLinearDimensionReader15;
import org.gvsig.dwg.lib.readers.v15.DwgLwPolylineReader15;
import org.gvsig.dwg.lib.readers.v15.DwgMTextReader15;
import org.gvsig.dwg.lib.readers.v15.DwgMeshReader15;
import org.gvsig.dwg.lib.readers.v15.DwgPFaceReader15;
import org.gvsig.dwg.lib.readers.v15.DwgPointReader15;
import org.gvsig.dwg.lib.readers.v15.DwgPolyline2DReader15;
import org.gvsig.dwg.lib.readers.v15.DwgPolyline3DReader15;
import org.gvsig.dwg.lib.readers.v15.DwgSeqEndReader15;
import org.gvsig.dwg.lib.readers.v15.DwgSolidReader15;
import org.gvsig.dwg.lib.readers.v15.DwgSplineReader15;
import org.gvsig.dwg.lib.readers.v15.DwgTextReader15;
import org.gvsig.dwg.lib.readers.v15.DwgVertex2DReader15;
import org.gvsig.dwg.lib.readers.v15.DwgVertex3DReader15;
import org.gvsig.dwg.lib.readers.v15.DwgVertexMeshReader15;
import org.gvsig.dwg.lib.readers.v15.DwgVertexPFaceFaceReader15;
import org.gvsig.dwg.lib.readers.v15.DwgVertexPFaceReader15;
import org.gvsig.dwg.lib.readers.v2004.DwgArcReader2004;
import org.gvsig.dwg.lib.readers.v2004.DwgAttdefReader2004;
import org.gvsig.dwg.lib.readers.v2004.DwgAttribReader2004;
import org.gvsig.dwg.lib.readers.v2004.DwgBlockControlReader2004;
import org.gvsig.dwg.lib.readers.v2004.DwgBlockHeaderReader2004;
import org.gvsig.dwg.lib.readers.v2004.DwgBlockReader2004;
import org.gvsig.dwg.lib.readers.v2004.DwgCircleReader2004;
import org.gvsig.dwg.lib.readers.v2004.DwgDictionaryReader2004;
import org.gvsig.dwg.lib.readers.v2004.DwgEllipseReader2004;
import org.gvsig.dwg.lib.readers.v2004.DwgEndBlkReader2004;
import org.gvsig.dwg.lib.readers.v2004.DwgInsertReader2004;
import org.gvsig.dwg.lib.readers.v2004.DwgLayerControlReader2004;
import org.gvsig.dwg.lib.readers.v2004.DwgLayerReader2004;
import org.gvsig.dwg.lib.readers.v2004.DwgLineReader2004;
import org.gvsig.dwg.lib.readers.v2004.DwgLinearDimensionReader2004;
import org.gvsig.dwg.lib.readers.v2004.DwgLwPolylineReader2004;
import org.gvsig.dwg.lib.readers.v2004.DwgMTextReader2004;
import org.gvsig.dwg.lib.readers.v2004.DwgMeshReader2004;
import org.gvsig.dwg.lib.readers.v2004.DwgPFaceReader2004;
import org.gvsig.dwg.lib.readers.v2004.DwgPointReader2004;
import org.gvsig.dwg.lib.readers.v2004.DwgPolyline2DReader2004;
import org.gvsig.dwg.lib.readers.v2004.DwgPolyline3DReader2004;
import org.gvsig.dwg.lib.readers.v2004.DwgSeqEndReader2004;
import org.gvsig.dwg.lib.readers.v2004.DwgSolidReader2004;
import org.gvsig.dwg.lib.readers.v2004.DwgSplineReader2004;
import org.gvsig.dwg.lib.readers.v2004.DwgTextReader2004;
import org.gvsig.dwg.lib.readers.v2004.DwgVertex2DReader2004;
import org.gvsig.dwg.lib.readers.v2004.DwgVertex3DReader2004;
import org.gvsig.dwg.lib.readers.v2004.DwgVertexMeshReader2004;
import org.gvsig.dwg.lib.readers.v2004.DwgVertexPFaceFaceReader2004;
import org.gvsig.dwg.lib.readers.v2004.DwgVertexPFaceReader2004;

/**
 * @author azabala
 */
public class DwgObjectReaderPool {
	
	private static final DwgObjectReaderPool _instance = 
		new DwgObjectReaderPool();
	
	private Map obj_readers;
	
	static{
		
//		READERS FOR DWG V2004
		_instance.put(DwgArc.class, "2004", new DwgArcReader2004());
		_instance.put(DwgAttdef.class, "2004", new DwgAttdefReader2004());
		_instance.put(DwgAttrib.class, "2004", new DwgAttribReader2004());
		_instance.put(DwgBlock.class, "2004", new DwgBlockReader2004());
		_instance.put(DwgBlockControl.class, "2004", new DwgBlockControlReader2004());
		_instance.put(DwgBlockHeader.class, "2004", new DwgBlockHeaderReader2004());
		_instance.put(DwgCircle.class, "2004", new DwgCircleReader2004());
		_instance.put(DwgEllipse.class, "2004", new DwgEllipseReader2004());
		_instance.put(DwgEndblk.class, "2004", new DwgEndBlkReader2004());
		_instance.put(DwgInsert.class, "2004", new DwgInsertReader2004());
		_instance.put(DwgLayer.class, "2004", new DwgLayerReader2004());
		_instance.put(DwgLayerControl.class, "2004", new DwgLayerControlReader2004());
		_instance.put(DwgLine.class, "2004", new DwgLineReader2004());
		_instance.put(DwgLinearDimension.class, "2004", new DwgLinearDimensionReader2004());
		_instance.put(DwgLwPolyline.class, "2004", new DwgLwPolylineReader2004());
		_instance.put(DwgMText.class, "2004", new DwgMTextReader2004());
		_instance.put(DwgPoint.class, "2004", new DwgPointReader2004());
		_instance.put(DwgPolyline2D.class, "2004", new DwgPolyline2DReader2004());
		_instance.put(DwgPolyline3D.class, "2004", new DwgPolyline3DReader2004());
		_instance.put(DwgSeqend.class, "2004", new DwgSeqEndReader2004());
		_instance.put(DwgSpline.class, "2004", new DwgSplineReader2004());
		_instance.put(DwgText.class, "2004", new DwgTextReader2004());
		_instance.put(DwgVertex2D.class, "2004", new DwgVertex2DReader2004());
		_instance.put(DwgVertex3D.class, "2004", new DwgVertex3DReader2004());
		_instance.put(DwgDictionary.class, "2004", new DwgDictionaryReader2004());
		_instance.put(DwgVertexPFace.class, "2004", new DwgVertexPFaceReader2004());
		_instance.put(DwgVertexPFaceFace.class, "2004", new DwgVertexPFaceFaceReader2004());
		_instance.put(DwgPFacePolyline.class, "2004", new DwgPFaceReader2004());
		_instance.put(DwgMeshPolyline.class, "2004", new DwgMeshReader2004());
		_instance.put(DwgVertexMesh.class, "2004", new DwgVertexMeshReader2004());
		_instance.put(DwgSolid.class, "2004", new DwgSolidReader2004());

		//READERS FOR DWG V15
		_instance.put(DwgArc.class, "15", new DwgArcReader15());
		_instance.put(DwgAttdef.class, "15", new DwgAttdefReader15());
		_instance.put(DwgAttrib.class, "15", new DwgAttribReader15());
		_instance.put(DwgBlock.class, "15", new DwgBlockReader15());
		_instance.put(DwgBlockControl.class, "15", new DwgBlockControlReader15());
		_instance.put(DwgBlockHeader.class, "15", new DwgBlockHeaderReader15());
		_instance.put(DwgCircle.class, "15", new DwgCircleReader15());
		_instance.put(DwgEllipse.class, "15", new DwgEllipseReader15());
		_instance.put(DwgEndblk.class, "15", new DwgEndBlkReader15());
		_instance.put(DwgInsert.class, "15", new DwgInsertReader15());
		_instance.put(DwgLayer.class, "15", new DwgLayerReader15());
		_instance.put(DwgLayerControl.class, "15", new DwgLayerControlReader15());
		_instance.put(DwgLine.class, "15", new DwgLineReader15());
		_instance.put(DwgLinearDimension.class, "15", new DwgLinearDimensionReader15());
		_instance.put(DwgLwPolyline.class, "15", new DwgLwPolylineReader15());
		_instance.put(DwgMText.class, "15", new DwgMTextReader15());
		_instance.put(DwgPoint.class, "15", new DwgPointReader15());
		_instance.put(DwgPolyline2D.class, "15", new DwgPolyline2DReader15());
		_instance.put(DwgPolyline3D.class, "15", new DwgPolyline3DReader15());
		_instance.put(DwgSeqend.class, "15", new DwgSeqEndReader15());
//		_instance.put(DwgSolid.class, "15", new DwgSolidReader15());
		_instance.put(DwgSpline.class, "15", new DwgSplineReader15());
		_instance.put(DwgText.class, "15", new DwgTextReader15());
		_instance.put(DwgVertex2D.class, "15", new DwgVertex2DReader15());
		_instance.put(DwgVertex3D.class, "15", new DwgVertex3DReader15());
		_instance.put(DwgDictionary.class, "15", new DwgDictionaryReader15());
		_instance.put(DwgVertexPFace.class, "15", new DwgVertexPFaceReader15());
		_instance.put(DwgVertexPFaceFace.class, "15", new DwgVertexPFaceFaceReader15());
		_instance.put(DwgPFacePolyline.class, "15", new DwgPFaceReader15());
		_instance.put(DwgMeshPolyline.class, "15", new DwgMeshReader15());
		_instance.put(DwgVertexMesh.class, "15", new DwgVertexMeshReader15());

		//READERS FOR DWG V13 AND V14
		_instance.put(DwgArc.class, "1314", new DwgArcReader1314());
		_instance.put(DwgAttdef.class, "1314", new DwgAttdefReader1314());
		_instance.put(DwgAttrib.class, "1314", new DwgAttribReader1314());
		_instance.put(DwgBlock.class, "1314", new DwgBlockReader1314());
		_instance.put(DwgBlockControl.class, "1314", new DwgBlockControlReader1314());
		_instance.put(DwgBlockHeader.class, "1314", new DwgBlockHeaderReader1314());
		_instance.put(DwgCircle.class, "1314", new DwgCircleReader1314());
		_instance.put(DwgDictionary.class, "1314", new DwgDictionaryReader1314());
		_instance.put(DwgDictionaryVar.class, "1314", new DwgDictionaryVarReader1314());
		_instance.put(DwgDimOrd.class, "1314", new DwgDimOrdReader1314());
		_instance.put(DwgEllipse.class, "1314", new DwgEllipseReader1314());
		_instance.put(DwgEndblk.class, "1314", new DwgEndBlkReader1314());
		_instance.put(DwgFace3D.class, "1314", new DwgFace3DReader1314());
		_instance.put(DwgHatch.class, "1314", new DwgHatchReader1314());
		_instance.put(DwgIdBuffer.class, "1314", new DwgIdBufferReader1314());
		_instance.put(DwgImageDefReactor.class, "1314", new DwgImageDefReactor1314());
		_instance.put(DwgImageDef.class, "1314", new DwgImageDefReader1314());
		_instance.put(DwgImage.class, "1314", new DwgImageReader1314());
		_instance.put(DwgInsert.class, "1314", new DwgInsertReader1314());
		_instance.put(DwgLayer.class, "1314", new DwgLayerReader1314());
		_instance.put(DwgLayerControl.class, "1314", new DwgLayerControlReader1314());
		_instance.put(DwgLayerIndex.class, "1314", new DwgLayerIndexReader1314());
		_instance.put(DwgLine.class, "1314", new DwgLineReader1314());
		_instance.put(DwgLinearDimension.class, "1314", new DwgLinearDimensionReader1314());
		_instance.put(DwgLwPolyline.class, "1314", new DwgLwPolylineReader1314());
		_instance.put(DwgMeshPolyline.class, "1314", new DwgMeshReader1314());
		_instance.put(DwgMinsert.class, "1314", new DwgMinsertReader1314());
		_instance.put(DwgMText.class, "1314", new DwgMTextReader1314());
		_instance.put(DwgOle2Frame.class, "1314", new DwgOle2FrameReader1314());
		_instance.put(DwgPFacePolyline.class, "1314", new DwgPFaceReader1314());
		_instance.put(DwgPoint.class, "1314", new DwgPointReader1314());
		_instance.put(DwgPolyline2D.class, "1314", new DwgPolyline2DReader1314());
		_instance.put(DwgPolyline3D.class, "1314", new DwgPolyline3DReader1314());
		_instance.put(DwgRasterVariables.class, "1314", new DwgRasterVariablesReader1314());
		_instance.put(DwgSeqend.class, "1314", new DwgSeqEndReader1314());
		_instance.put(DwgSortEntStable.class, "1314", new DwgSortEntsTableReader1314());
//		_instance.put(DwgSolid.class, "1314", new DwgSolidReader1314());
		_instance.put(DwgSpline.class, "1314", new DwgSplineReader1314());
		_instance.put(DwgText.class, "1314", new DwgTextReader1314());
		_instance.put(DwgVertex2D.class, "1314", new DwgVertex2DReader1314());
		_instance.put(DwgVertex3D.class, "1314", new DwgVertex3DReader1314());
		_instance.put(DwgXRecord.class, "1314", new DwgXrecordReader1314());
		_instance.put(DwgDictionary.class, "1314", new DwgDictionaryReader1314());
		_instance.put(DwgVertexPFace.class, "1314", new DwgVertexPFaceReader1314());
		_instance.put(DwgVertexPFaceFace.class, "1314", new DwgVertexPFaceFaceReader1314());
		_instance.put(DwgVertexMesh.class, "1314", new DwgVertexMeshReader1314());
	
	}
	
	
	
	public final static DwgObjectReaderPool getInstance(){
		return _instance;
	}
	private DwgObjectReaderPool(){
		obj_readers = new HashMap();
	}
	
	/**Get the specific reader for an object
	 * 
	 * @param obj Object we want to read
	 * @param dwgVersion version of dwg file
	 * @return The specific reader
	 */
	public IDwgObjectReader get(DwgObject obj, String dwgVersion){
		
		IDwgObjectReader reader = (IDwgObjectReader) obj_readers.
				get(obj.getClass().getName() + dwgVersion);
//		if(reader == null)
//			throw new RuntimeException("No se encuentra la clase para leer "+obj.getClass().getName());
		return reader;
		
	}
	
	/**
	 * Stores in a map the different readers for objects
	 * @param dwgObjClass Object's class
	 * @param dwgVersion version of dwg file
	 * @param reader Specific reader for the object
	 */
	public void put(Class dwgObjClass, String dwgVersion, IDwgObjectReader reader){
		if(DwgObject.class.isAssignableFrom(dwgObjClass))
			obj_readers.put(dwgObjClass.getName()+ dwgVersion, reader);
		else
			throw new RuntimeException("Este pool solo admite como entrada clases de entidad de dibujo gr�fica");
	}
	
}
