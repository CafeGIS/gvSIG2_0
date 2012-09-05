/*
 * Created on 19-mar-2007
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
* $Id: DwgPFaceReader15.java,v 1.1.2.1 2007/03/21 19:49:16 azabala Exp $
* $Log: DwgPFaceReader15.java,v $
* Revision 1.1.2.1  2007/03/21 19:49:16  azabala
* implementation of dwg 12, 13, 14.
*
* Revision 1.1  2007/03/20 19:57:08  azabala
* source code cleaning
*
*
*/
package org.gvsig.dwg.lib.readers.v2004;

import java.util.List;

import org.gvsig.dwg.lib.CorruptedDwgEntityException;
import org.gvsig.dwg.lib.DwgHandleReference;
import org.gvsig.dwg.lib.DwgObject;
import org.gvsig.dwg.lib.DwgUtil;
import org.gvsig.dwg.lib.objects.DwgPFacePolyline;


public class DwgPFaceReader2004 extends AbstractDwg2004Reader {
	public void readSpecificObj(int[] data, int offset, DwgObject dwgObj)
			throws RuntimeException, CorruptedDwgEntityException {

		if(! (dwgObj instanceof DwgPFacePolyline))
	    	throw new RuntimeException("ArcReader 2004 solo puede leer DwgPFacePolyline");
		DwgPFacePolyline pface = (DwgPFacePolyline) dwgObj;
		int bitPos = offset;


//		boolean dontRead = false;
//
//
//		ArrayList v = DwgUtil.getRawLong(data, bitPos);
//		bitPos = ((Integer) v.get(0)).intValue();
//		int objBSize = ((Integer) v.get(1)).intValue();
//		pface.setSizeInBits(objBSize);
//
//		DwgHandleReference entityHandle = new DwgHandleReference();
//		entityHandle.read(data, bitPos);
//		if(entityHandle.getCode()!=0 && entityHandle.getCounter()!=1)
//			dontRead=true;
//		if(!dontRead){
//			entityHandle = new DwgHandleReference();
//			bitPos = entityHandle.read(data, bitPos);
//			pface.setHandle(entityHandle);
//			}
//		if(dontRead) bitPos=bitPos + 16;
//
//
//
//		v = DwgUtil.readExtendedData(data, bitPos);
//		bitPos = ((Integer) v.get(0)).intValue();
//		ArrayList extData = (ArrayList) v.get(1);
//		pface.setExtendedData(extData);
//
//		boolean gflag = false;
//		gflag = pface.isGraphicsFlag();
//		if (gflag) {
//			//lee un flag boolean
//			v = DwgUtil.testBit(data, bitPos);
//			bitPos = ((Integer) v.get(0)).intValue();
//			boolean val = ((Boolean) v.get(1)).booleanValue();
//			//si hay imagen asociada, se lee por completo
//			if (val) {
//				v = DwgUtil.getRawLong(data, bitPos);
//				bitPos = ((Integer) v.get(0)).intValue();
//				int size = ((Integer) v.get(1)).intValue();
//				int bgSize = size * 8;
//				Integer giData = (Integer) DwgUtil.getBits(data, bgSize,
//						bitPos);
//				pface.setGraphicData(giData.intValue());
//				bitPos = bitPos + bgSize;
//			}
//		}
//
//
		bitPos = readObjectHeader(data, bitPos, pface);

		List val = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		int vertexCount = ((Integer) val.get(1)).intValue();
		pface.setVertexCount(vertexCount);

		val = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		int faceCount = ((Integer) val.get(1)).intValue();
		pface.setFaceCount(faceCount);

		val = DwgUtil.getBitLong(data, bitPos); //OWNED OBJECT COUNT
		bitPos = ((Integer)val.get(0)).intValue();
		int OwnedObj = ((Integer)val.get(1)).intValue();

		bitPos = readObjectTailer(data, bitPos, pface);
		DwgHandleReference handle; // = new DwgHandleReference();


		if (OwnedObj>0) {
			for (int i=0;i<OwnedObj;i++) {
				handle = new DwgHandleReference();
				bitPos = handle.read(data, bitPos);
				pface.addOwnedObjectHandle(handle);
			}

		}

//		DwgHandleReference handle = new DwgHandleReference();
//		bitPos = handle.read(data, bitPos);
//		pface.setFirstVertexHandle(handle);
//
//		handle = new DwgHandleReference();
//		bitPos = handle.read(data, bitPos);
//		pface.setLastVertexHandle(handle);

		handle = new DwgHandleReference();
		bitPos = handle.read(data, bitPos);
		pface.setSeqendHandle(handle);
	}

}

