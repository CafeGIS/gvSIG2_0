/*
 * Created on 05-feb-2007
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
* $Id: DwgDictionaryReader1314.java 28969 2009-05-25 13:23:12Z jmvivo $
* $Log$
* Revision 1.2.2.2  2007-03-21 19:49:16  azabala
* implementation of dwg 12, 13, 14.
*
* Revision 1.2  2007/02/06 20:17:29  azabala
* *** empty log message ***
*
* Revision 1.1  2007/02/05 19:44:00  azabala
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
import org.gvsig.dwg.lib.objects.DwgDictionary;


public class DwgDictionaryReader1314 extends AbstractDwg1314Reader {

	public void readSpecificObj(int[] data, int offset, DwgObject dwgObj)
			throws RuntimeException, CorruptedDwgEntityException {
		
		if(! (dwgObj instanceof DwgDictionary))
			throw new RuntimeException("ArcReader 15 solo puede leer DwgDictionary");
		DwgDictionary dic = (DwgDictionary) dwgObj;
		
		int bitPos = offset;
		List val = DwgUtil.getBitShort(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		int nr = ((Integer) val.get(1)).intValue();
		
		val = DwgUtil.getBitLong(data, bitPos);
		bitPos = ((Integer) val.get(0)).intValue();
		int ni = ((Integer) val.get(1)).intValue();
		int u = 0;
		//TODO Revisar que valores puede tomar el campo version
		if(dic.getVersion() == "Autocad R14"){
			val = DwgUtil.getRawChar(data, bitPos);
			bitPos = ((Integer) val.get(0)).intValue();
			u = ((Integer) val.get(1)).intValue();
		}
		String[] texts = null;
		if(ni > 0){
			texts = new String[ni];
			for(int i = 0; i < ni; i++){
				val = DwgUtil.getTextString(data, bitPos);
				bitPos = ((Integer) val.get(0)).intValue();
				texts[i] = (String) val.get(1);
			}//for
		}
		
		DwgHandleReference handle = new DwgHandleReference();
		bitPos = handle.read(data, bitPos);
		dic.setParentHandle(handle);
		
		for (int i = 0; i < nr; i++){
			handle = new DwgHandleReference();
			bitPos = handle.read(data, bitPos);
			dic.addReactorHandle(handle);
		}
		
		handle = new DwgHandleReference();
		bitPos = handle.read(data, bitPos);
		dic.setXDicObjHandle(handle);
		
		if(ni > 0){
			for(int i = 0; i < ni; i++){
				handle = new DwgHandleReference();
				bitPos = handle.read(data, bitPos);
				
				dic.put(texts[i], handle);
			}
		}
	}


}

