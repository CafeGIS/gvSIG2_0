/*
 * Created on 30-ene-2007
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
* $Id: DwgDictionaryVarReader1314.java 28969 2009-05-25 13:23:12Z jmvivo $
* $Log$
* Revision 1.4.2.2  2007-03-21 19:49:16  azabala
* implementation of dwg 12, 13, 14.
*
* Revision 1.4  2007/02/07 12:45:04  fdiaz
* *** empty log message ***
*
* Revision 1.3  2007/02/02 11:46:35  azabala
* changes in throwed exceptions
*
* Revision 1.2  2007/01/31 18:11:06  fdiaz
* Refactorizacion para usar la clase DwgHandleReference
*
* Revision 1.1  2007/01/30 19:40:23  azabala
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
import org.gvsig.dwg.lib.objects.DwgDictionaryVar;


public class DwgDictionaryVarReader1314 extends AbstractDwg1314Reader {

	public void readSpecificObj(int[] data, int offset, DwgObject dwgObj) throws RuntimeException, CorruptedDwgEntityException {
		
		 if(! (dwgObj instanceof DwgDictionaryVar))
		    	throw new RuntimeException("ArcReader 14 solo puede leer DwgDictionaryVar");
		
		DwgDictionaryVar dic = (DwgDictionaryVar) dwgObj;
		int bitPos = offset;
		List val = DwgUtil.getBitLong(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		int numReactors = ((Integer)val.get(1)).intValue();
		dic.setNumReactors(numReactors);
		
		val = DwgUtil.getRawChar(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		int intval = ((Integer)val.get(0)).intValue();
		dic.setIntval(intval);
		
		val = DwgUtil.getTextString(data, bitPos);
		bitPos = ((Integer)val.get(0)).intValue();
		String text = ((String)val.get(1));
		dic.setText(text);
		
		DwgHandleReference parentHandle = new DwgHandleReference();
		bitPos = parentHandle.read(data, bitPos);
		dic.setParentHandle(parentHandle);

		DwgHandleReference reactorHandle;
		for (int i = 0; i < numReactors; i++) {
			reactorHandle = new DwgHandleReference();
			bitPos = reactorHandle.read(data, bitPos);
			dic.addReactorHandle(reactorHandle);
		}

		DwgHandleReference xDicObjHandle = new DwgHandleReference();
		bitPos = xDicObjHandle.read(data, bitPos);
		dic.setXDicObjHandle(xDicObjHandle);

	}

}

