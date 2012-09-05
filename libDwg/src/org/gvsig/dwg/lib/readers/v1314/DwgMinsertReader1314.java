/*
 * Created on 02-feb-2007
 *
 * gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
 *   Av. Blasco Ibáñez, 50
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
* $Id: DwgMinsertReader1314.java 28969 2009-05-25 13:23:12Z jmvivo $
* $Log$
* Revision 1.2.2.2  2007-03-21 19:49:16  azabala
* implementation of dwg 12, 13, 14.
*
* Revision 1.2  2007/02/07 12:45:21  fdiaz
* *** empty log message ***
*
* Revision 1.1  2007/02/05 07:03:22  azabala
* *** empty log message ***
*
*
*/
package org.gvsig.dwg.lib.readers.v1314;

import java.util.List;

import org.gvsig.dwg.lib.CorruptedDwgEntityException;
import org.gvsig.dwg.lib.DwgHandleReference;
import org.gvsig.dwg.lib.DwgObject;
import org.gvsig.dwg.lib.DwgUtil;
import org.gvsig.dwg.lib.objects.DwgInsert;
import org.gvsig.dwg.lib.objects.DwgMinsert;


public class DwgMinsertReader1314 extends AbstractDwg1314Reader {

	public void readSpecificObj(int[] data, int offset, DwgObject dwgObj)
			throws RuntimeException, CorruptedDwgEntityException {
		if(! (dwgObj instanceof DwgInsert))
	    	throw new RuntimeException("ArcReader 14 solo puede leer DwgMInsert");
		DwgMinsert insert = (DwgMinsert) dwgObj;
		int bitPos = offset;
		bitPos = headTailReader.readObjectHeader(data, bitPos, insert);
		
		List val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		double x = ((Double)val.get(1)).doubleValue();
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		double y = ((Double)val.get(1)).doubleValue();

		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		double z = ((Double)val.get(1)).doubleValue();
		insert.setInsertionPoint(new double[]{x, y, z});
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		x = ((Double)val.get(1)).doubleValue();
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		y = ((Double)val.get(1)).doubleValue();

		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		z = ((Double)val.get(1)).doubleValue();
		insert.setScale(new double[]{x, y, z});
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		double rotation = ((Double)val.get(1)).doubleValue();
		insert.setRotation(rotation);
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		x = ((Double)val.get(1)).doubleValue();
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		y = ((Double)val.get(1)).doubleValue();

		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		z = ((Double)val.get(1)).doubleValue();
		insert.setExtrusion(new double[]{x, y, z});
		
		val = DwgUtil.testBit(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		boolean hasAttr = ((Boolean)val.get(1)).booleanValue();
		
		bitPos = headTailReader.readObjectTailer(data, bitPos, insert);
		
		DwgHandleReference handle = new DwgHandleReference();
		bitPos = handle.read(data, bitPos);
		insert.setBlockHeaderHandle(handle);
		
		if(hasAttr){
			handle = new DwgHandleReference();
			bitPos = handle.read(data, bitPos);
			insert.setFirstAttribHandle(handle);
			
			handle = new DwgHandleReference();
			bitPos = handle.read(data, bitPos);
			insert.setLastAttribHandle(handle);
			
			handle = new DwgHandleReference();
			bitPos = handle.read(data, bitPos);
			insert.setSeqEndHandle(handle);
		}//if
	}

}

