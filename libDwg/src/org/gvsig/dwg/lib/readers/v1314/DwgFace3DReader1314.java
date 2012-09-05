/*
 * Created on 03-feb-2007
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
* $Id: DwgFace3DReader1314.java 28969 2009-05-25 13:23:12Z jmvivo $
* $Log$
* Revision 1.1.2.2  2007-03-21 19:49:16  azabala
* implementation of dwg 12, 13, 14.
*
* Revision 1.1  2007/02/05 07:03:22  azabala
* *** empty log message ***
*
*
*/
package org.gvsig.dwg.lib.readers.v1314;

import java.util.List;

import org.gvsig.dwg.lib.CorruptedDwgEntityException;
import org.gvsig.dwg.lib.DwgObject;
import org.gvsig.dwg.lib.DwgUtil;
import org.gvsig.dwg.lib.objects.DwgFace3D;


public class DwgFace3DReader1314 extends AbstractDwg1314Reader {

	public void readSpecificObj(int[] data, int offset, DwgObject dwgObj)
			throws RuntimeException, CorruptedDwgEntityException {
		if(! (dwgObj instanceof DwgFace3D))
	    	throw new RuntimeException("ArcReader 14 solo puede leer DwgFace3D");
		DwgFace3D f = (DwgFace3D)dwgObj;
		int bitPos = offset;
		bitPos = headTailReader.readObjectHeader(data, offset, dwgObj );
		
		List val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		double x = ((Double) val.get(1)).doubleValue();
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		double y = ((Double) val.get(1)).doubleValue();
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		double z = ((Double) val.get(1)).doubleValue();
		f.setCorner1(new double[]{x, y, z});
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		x = ((Double) val.get(1)).doubleValue();
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		y = ((Double) val.get(1)).doubleValue();
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		z = ((Double) val.get(1)).doubleValue();
		f.setCorner2(new double[]{x, y, z});
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		x = ((Double) val.get(1)).doubleValue();
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		y = ((Double) val.get(1)).doubleValue();
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		z = ((Double) val.get(1)).doubleValue();
		f.setCorner3(new double[]{x, y, z});
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		x = ((Double) val.get(1)).doubleValue();
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		y = ((Double) val.get(1)).doubleValue();
		
		val = DwgUtil.getBitDouble(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		z = ((Double) val.get(1)).doubleValue();
		f.setCorner4(new double[]{x, y, z});
		
		val = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		int flags = ((Integer) val.get(1)).intValue();
		f.setFlags(flags);
		
		bitPos = headTailReader.readObjectTailer(data, bitPos, f);
	}

}

