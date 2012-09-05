/*
 * Created on 27-dic-2006
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
/* CVS MESSAGES:
 *
 * $Id: DwgObjectFactory.java 28969 2009-05-25 13:23:12Z jmvivo $
 * $Log$
 * Revision 1.14.2.2  2007-03-21 19:49:16  azabala
 * implementation of dwg 12, 13, 14.
 *
 * Revision 1.18  2007/03/20 19:55:27  azabala
 * source code cleaning
 *
 * Revision 1.17  2007/03/06 19:39:38  azabala
 * Changes to adapt dwg 12 to general architecture
 *
 * Revision 1.16  2007/03/02 20:31:22  azabala
 * *** empty log message ***
 *
 * Revision 1.15  2007/03/01 19:59:46  azabala
 * source code cleaning
 *
 * Revision 1.14  2007/02/22 20:45:51  azabala
 * changes to add dwg 12 entities creation
 *
 * Revision 1.13  2007/02/14 09:20:32  fdiaz
 * Eliminado un comentario y habilitado de nuevo el else del final del metodo create.
 *
 * Revision 1.12  2007/02/08 20:27:06  azabala
 * solved bug with lwpolylines
 *
 * Revision 1.11  2007/02/08 10:27:35  azabala
 * if the type is unknown it returns NULL
 *
 * Revision 1.10  2007/02/06 20:17:29  azabala
 * *** empty log message ***
 *
 * Revision 1.9  2007/02/02 12:39:52  azabala
 * Added new dwg entities
 *
 * Revision 1.8  2007/02/01 20:00:27  azabala
 * *** empty log message ***
 *
 * Revision 1.7  2007/01/31 18:20:58  fdiaz
 * A�adido un return null al final de metodo create por si no entra en ninguna de las condiciones.
 *
 * Revision 1.6  2007/01/31 10:01:11  fdiaz
 * A�adido el DwgDictionary.
 *
 * Revision 1.5  2007/01/30 19:40:23  azabala
 * *** empty log message ***
 *
 * Revision 1.4  2007/01/30 12:37:18  azabala
 * *** empty log message ***
 *
 * Revision 1.3  2007/01/24 20:14:31  azabala
 * implementation of reader of V14
 *
 * Revision 1.2  2007/01/12 19:35:00  azabala
 * *** empty log message ***
 *
 * Revision 1.1  2007/01/09 15:39:15  azabala
 * *** empty log message ***
 *
 *
 */
package org.gvsig.dwg.lib;

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
import org.gvsig.dwg.lib.objects.DwgLwPolyline;
import org.gvsig.dwg.lib.objects.DwgMText;
import org.gvsig.dwg.lib.objects.DwgMeshPolyline;
import org.gvsig.dwg.lib.objects.DwgOle2Frame;
import org.gvsig.dwg.lib.objects.DwgPFacePolyline;
import org.gvsig.dwg.lib.objects.DwgPoint;
import org.gvsig.dwg.lib.objects.DwgPolyline2D;
import org.gvsig.dwg.lib.objects.DwgPolyline3D;
import org.gvsig.dwg.lib.objects.DwgRasterVariables;
import org.gvsig.dwg.lib.objects.DwgSeqend;
import org.gvsig.dwg.lib.objects.DwgSolid;
import org.gvsig.dwg.lib.objects.DwgSortEntStable;
import org.gvsig.dwg.lib.objects.DwgSpatialFilter;
import org.gvsig.dwg.lib.objects.DwgSpatialIndex;
import org.gvsig.dwg.lib.objects.DwgSpline;
import org.gvsig.dwg.lib.objects.DwgText;
import org.gvsig.dwg.lib.objects.DwgVertex2D;
import org.gvsig.dwg.lib.objects.DwgVertex3D;
import org.gvsig.dwg.lib.objects.DwgVertexMesh;
import org.gvsig.dwg.lib.objects.DwgVertexPFace;
import org.gvsig.dwg.lib.objects.DwgVertexPFaceFace;
import org.gvsig.dwg.lib.objects.DwgXRecord;


/**
 * Factory that creates dwg entities from its code (int value).
 * 
 * Some DWG entities dont have a fixed "type code". Instead, their type
 * is specified in the CLASSES section of the dwg file.
 * 
 * 
 * 
 * It's a singleton
 *  
 */
public class DwgObjectFactory {

	private static DwgObjectFactory _instance = new DwgObjectFactory();

	private DwgObjectFactory() {
	}
	
	/*
	 * Constants to represent polyline2d, polyline3d,
	 * vertex2d and vertex3d.
	 * */
//	public static final byte POLYLINE2D = 40;
//	public static final byte POLYLINE3D = 41;
//	public static final byte VERTEX2D = 50;
//	public static final byte VERTEX3D = 51;

	public static final DwgObjectFactory getInstance() {
		return _instance;
	}

	
	/**
	 * Creates a Dwg object instance from its type.
	 * 
	 * This method is used for DWG 13, 14, 2000 and 2004 files.
	 * 
	 * */
	public DwgObject create(int type, int index) {
		DwgObject obj = null;

		 if (type == 0x1) {
			obj = new DwgText(index);
			obj.setGraphicsFlag(true);
		} else if (type == 0x2) {
			obj = new DwgAttrib(index);
			obj.setGraphicsFlag(true);
		} else if (type == 0x3) {
			obj = new DwgAttdef(index);
			obj.setGraphicsFlag(true);
		} else if (type == 0x4) {
			obj = new DwgBlock(index);
			obj.setGraphicsFlag(true);
		} else if (type == 0x5) {
			obj = new DwgEndblk(index);
			obj.setGraphicsFlag(true);
		} else if (type == 0x6) {
			obj = new DwgSeqend(index);
			obj.setGraphicsFlag(true);
		} else if (type == 0x7) {
			obj = new DwgInsert(index);
			obj.setGraphicsFlag(true);
		} else if (type == 0x0A) {
			obj = new DwgVertex2D(index);
			obj.setGraphicsFlag(true);
		} else if (type == 0x0B) {
			obj = new DwgVertex3D(index);
			obj.setGraphicsFlag(true);
		} else if (type == 0x0C) {
			obj = new DwgVertexMesh(index);
			obj.setGraphicsFlag(true);
		} else if (type == 0x0D) {
			obj = new DwgVertexPFace(index);
			obj.setGraphicsFlag(true);
		} else if (type == 0x0E) {
			obj = new DwgVertexPFaceFace(index);
			obj.setGraphicsFlag(true);
		} else if (type == 0x0F) {
			obj = new DwgPolyline2D(index);
			obj.setGraphicsFlag(true);
		} else if (type == 0x10) {
			obj = new DwgPolyline3D(index);
			obj.setGraphicsFlag(true);
		} else if (type == 0x11) {
			obj = new DwgArc(index);
			obj.setGraphicsFlag(true);
		} else if (type == 0x12) {
			obj = new DwgCircle(index);
			obj.setGraphicsFlag(true);
		} else if (type == 0x13) {
			obj = new DwgLine(index);
			obj.setGraphicsFlag(true);
		}  else if (type == 0x1B) {
			obj = new DwgPoint(index);
			obj.setGraphicsFlag(true);
		} else if (type == 0x1D) {
			obj = new DwgPFacePolyline(index);
			obj.setGraphicsFlag(true);
		} else if (type == 0x1E) {
			obj = new DwgMeshPolyline(index);
			obj.setGraphicsFlag(true);
		} else if (type == 0x23) {
			obj = new DwgEllipse(index);
			obj.setGraphicsFlag(true);
		} else if (type == 0x24) {
			obj = new DwgSpline(index);
			obj.setGraphicsFlag(true);
		} else if (type == 0x2A) {
			obj = new DwgDictionary(index);
			obj.setGraphicsFlag(false);
		} else if (type == 0x2C) {
			obj = new DwgMText(index);
			obj.setGraphicsFlag(true);
		} else if (type == 0x30) {
			obj = new DwgBlockControl(index);
			obj.setGraphicsFlag(false);
		} else if (type == 0x31) {
			obj = new DwgBlockHeader(index);
			obj.setGraphicsFlag(false);
		} else if (type == 0x32) {
			obj = new DwgLayerControl(index);
			obj.setGraphicsFlag(false);
		} else if (type == 0x33) {
			obj = new DwgLayer(index);
			obj.setGraphicsFlag(false);
		} else if (type == 0x4D) {
			obj = new DwgLwPolyline(index);
			obj.setGraphicsFlag(true);
		} else if (type == 0x1F) {
			obj = new DwgSolid(index);
			obj.setGraphicsFlag(true);
		} 
		
//		else if (type == 0x15) {
//			obj = new DwgLinearDimension(index);
//			obj.setGraphicsFlag(true);
//		} 
		/*
		 * Segun los foros de OpenDWG:
		 * OLE2FRAME - 74 -> 0X4A
		   LWPOLYLINE - 77 -> 0X4D 
		   HATCH - 78 -> 0X4E
		 * 
		 * 
		 * */
		
		
		/*
		else if (type == 0x4A){
			obj = new DwgOle2Frame(index);
			obj.setGraphicsFlag(false);
		}
		
		
		else if (type == 0x4E){
			obj = new DwgHatch(index);
			obj.setGraphicsFlag(true);
		}
		
		else if (type == 0x4E) {
			obj = new DwgLwPolyline(index);
			obj.setGraphicsFlag(true);
		} else if (type == 0x4F) {
			obj = new DwgLwPolyline(index);
			obj.setGraphicsFlag(true);
		} else if (type == 0x50) {
			obj = new DwgLwPolyline(index);
			obj.setGraphicsFlag(true);
		} else if (type == 0x51) {
			obj = new DwgLwPolyline(index);
			obj.setGraphicsFlag(true);
		} else if (type == 0x52) {
			obj = new DwgLwPolyline(index);
			obj.setGraphicsFlag(true);
		} else if (type == 0x53) {
			obj = new DwgLwPolyline(index);
			obj.setGraphicsFlag(true);
		} 
		*/
		
		else {
//			obj = new DwgObject(index);
			return null;
		}
		obj.setType(type);
		return obj;
	}

	/**
	 * Creates a DwgObject from its DXF name.
	 * 
	 * This method is used to create entities without a fixed dwg code type
	 * (entities whose type is > 500)
	 * 
	 * This method is used for DWG 13, 14 and 2000 files.
	 *  
	 */
	public DwgObject create(String dxfName, int index) {
		//TODO De todas estas ver cuales son graficas,
		//para poner su graphicsFlag a true
		//ESTO MEJOR VA A SER CADA CLASE QUIEN LO HAGA, EN VEZ
		//DE LA FACTORIA
		
//		if (dxfName.equalsIgnoreCase("DICTIONARYVAR"))
//			return new DwgDictionaryVar(index);
//		else if (dxfName.equalsIgnoreCase("HATCH"))
//			return new DwgHatch(index);
//		else if (dxfName.equalsIgnoreCase("IDBUFFER"))
//			return new DwgIdBuffer(index);
//		else if (dxfName.equalsIgnoreCase("IMAGE"))
//			return new DwgImage(index);
//		else if (dxfName.equalsIgnoreCase("IMAGEDEF"))
//			return new DwgImageDef(index);
//		else if (dxfName.equalsIgnoreCase("IMAGEDEFREACTOR"))
//			return new DwgImageDefReactor(index);
//		else if (dxfName.equalsIgnoreCase("LAYER_INDEX"))
//			return new DwgLayerIndex(index);
//		else 
		if (dxfName.equalsIgnoreCase("LWPOLYLINE")){
			DwgLwPolyline solution = new DwgLwPolyline(index);
			solution.setGraphicsFlag(true);
			return solution;
		}
//		else if (dxfName.equalsIgnoreCase("OLE2FRAME"))
//			return new DwgOle2Frame(index);
//		else if (dxfName.equalsIgnoreCase("RASTERVARIABLES"))
//			return new DwgRasterVariables(index);
//		else if (dxfName.equalsIgnoreCase("SORTENTSTABLE"))
//			return new DwgSortEntStable(index);
//		else if (dxfName.equalsIgnoreCase("SPATIALFILTER"))
//			return new DwgSpatialFilter(index);
//		else if (dxfName.equalsIgnoreCase("SPATIALINDEX"))
//			return new DwgSpatialIndex(index);
//		else if (dxfName.equalsIgnoreCase("XRECORD"))
//			return new DwgXRecord(index);
		return null;
	}
	
	/**
	 * Creates a DWG object for DWG 12 files.
	 *  
	 * */
	public DwgObject create(byte kind, int index){
		switch(kind){
		case 1:
			return new DwgLine(index);
		case 2:
			return new DwgPoint(index);
		case 3:
			return new DwgCircle(index);
//		case 4://un dxf shape es un fichero externo que define una forma
			//(un tipo rudimentario de svg)
//			return new DwgShape()
		case 7:
			return new DwgText(index);
		case 8:
			return new DwgArc(index);
//		case 9: //It is a 3D quad
//			return new DwgTrace(index);
			
		case 11:
			return new DwgSolid(index);
		case 12:
			return new DwgBlockHeader(index);
		case 13:
			return new DwgEndblk(index);
		case 14:
			return new DwgInsert(index);
		case 15:
			return new DwgAttdef(index);
		case 16:
			return new DwgAttrib(index);	
		case 17://no estoy seguro de q esto sea SbEnd
			return new DwgSeqend(index);
		case 19://polyline is a particular case. Creation responsability is of Reader
			return null;
		case 20://vertex like polyline
			return null;
		case 22:
			return new DwgFace3D(index);
		case 23: //esto es Dim ??
			return new DwgDimOrd(index);
//		case 25://no implementado
//			return new DwgVPort(index);
			default:
				return null;
		}
	}
}