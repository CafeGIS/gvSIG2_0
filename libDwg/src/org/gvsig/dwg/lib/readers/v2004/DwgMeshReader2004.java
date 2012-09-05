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
* $Id: DwgMeshReader15.java,v 1.1.2.1 2007/03/21 19:49:16 azabala Exp $
* $Log: DwgMeshReader15.java,v $
* Revision 1.1.2.1  2007/03/21 19:49:16  azabala
* implementation of dwg 12, 13, 14.
*
* Revision 1.1  2007/03/20 19:57:08  azabala
* source code cleaning
*
*
*/
package org.gvsig.dwg.lib.readers.v2004;

import java.util.ArrayList;
import java.util.List;

import org.gvsig.dwg.lib.CorruptedDwgEntityException;
import org.gvsig.dwg.lib.DwgHandleReference;
import org.gvsig.dwg.lib.DwgObject;
import org.gvsig.dwg.lib.DwgUtil;
import org.gvsig.dwg.lib.objects.DwgMeshPolyline;


public class DwgMeshReader2004 extends AbstractDwg2004Reader {

	public void readSpecificObj(int[] data, int offset, DwgObject dwgObj)
			throws RuntimeException, CorruptedDwgEntityException {
		if(! (dwgObj instanceof DwgMeshPolyline))
	    	throw new RuntimeException("Meshr 2004 solo puede leer DwgMeshPolyline");
		DwgMeshPolyline m = (DwgMeshPolyline) dwgObj;
		int bitPos = offset;
		ArrayList v;
//		boolean dontRead = false;
//
//
//		v = DwgUtil.getRawLong(data, bitPos);
//		bitPos = ((Integer) v.get(0)).intValue();
//		int objBSize = ((Integer) v.get(1)).intValue();
//		m.setSizeInBits(objBSize);
//
//		DwgHandleReference entityHandle = new DwgHandleReference();
//		entityHandle.read(data, bitPos);
//		if(entityHandle.getCode()!=0 && entityHandle.getCounter()!=1)
//			dontRead=true;
//		if(!dontRead){
//			entityHandle = new DwgHandleReference();
//			bitPos = entityHandle.read(data, bitPos);
//			m.setHandle(entityHandle);
//			}
//		if(dontRead) bitPos=bitPos + 16;
//
//
//		v=DwgUtil.getBitShort(data, bitPos);
//		double newBitPos=((Integer)v.get(0)).intValue();
//		newBitPos=newBitPos-bitPos;
//		newBitPos=Math.ceil((newBitPos/8));
//		bitPos=(int) newBitPos;
//
//
//		boolean gflag = false;
//		gflag = m.isGraphicsFlag();
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
//				m.setGraphicData(giData.intValue());
//				bitPos = bitPos + bgSize;
//			}
//		}
//

		bitPos = readObjectHeader(data, bitPos, m);

		List val = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		int flags = ((Integer) val.get(1)).intValue();
		m.setFlags(flags);

		val = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		int curveType = ((Integer) val.get(1)).intValue();
		m.setCurveType(curveType);

		val = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		int mVerticies = ((Integer) val.get(1)).intValue();
		m.setMVerticies(mVerticies);

		val = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		int nVerticies = ((Integer) val.get(1)).intValue();
		m.setNVerticies(nVerticies);

		val = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		int mDensity = ((Integer) val.get(1)).intValue();
		m.setMDensity(mDensity);

		val = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		int nDensity = ((Integer) val.get(1)).intValue();
		m.setNDensity(nDensity);

		val = DwgUtil.getBitLong(data, bitPos); //OWNED OBJECT COUNT
		bitPos = ((Integer)val.get(0)).intValue();
		int OwnedObj = ((Integer)val.get(1)).intValue();

		bitPos = readObjectTailer(data, bitPos, m);

		DwgHandleReference handle;

		if (OwnedObj>0) {
			for (int i=0;i<OwnedObj;i++) {
				handle = new DwgHandleReference();
				bitPos = handle.read(data, bitPos);
				m.addOwnedObjectHandle(handle);
			}
		}

		handle = new DwgHandleReference();
		bitPos = handle.read(data, bitPos);
		m.setSeqendHandle(handle);
	}

}

