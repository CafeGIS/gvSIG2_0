package org.gvsig.dwg.lib.readers.v15;

import java.util.ArrayList;

import org.gvsig.dwg.lib.DwgHandleReference;
import org.gvsig.dwg.lib.DwgObject;
import org.gvsig.dwg.lib.DwgUtil;
import org.gvsig.dwg.lib.objects.DwgDictionary;


public class DwgDictionaryReader15 extends AbstractDwg15Reader{

	public void readSpecificObj(int[] data, int offset, DwgObject dwgObj) {
		// TODO Auto-generated method stub
		if(! (dwgObj instanceof DwgDictionary))
			throw new RuntimeException("DictionaryReader15 solo puede leer DwgDictionary");
		DwgDictionary dict = (DwgDictionary) dwgObj;
		try {
			//System.out.println("LEYENDO UN DICCIONARIO");
			int bitPos = offset;
			ArrayList v = DwgUtil.getBitShort(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			int numReactors = ((Integer)v.get(1)).intValue();
			dict.setNumReactors(numReactors);
			//System.out.println("numReactors="+numReactors);
			
			v = DwgUtil.getBitLong(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			int numItems = ((Integer)v.get(1)).intValue();
			//System.out.println("numItems="+numItems);
			
			
			v = DwgUtil.getBitShort(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			int cloningFlag = ((Integer)v.get(1)).intValue();
			dict.setCloningFlag(cloningFlag);
			//System.out.println("cloningFlag="+cloningFlag);
			
			v = DwgUtil.getRawChar(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			int hardOwnerFlag = ((Integer)v.get(1)).intValue();
			dict.setHardOwnerFlag(hardOwnerFlag);
			//System.out.println("hardOwnerFlag="+hardOwnerFlag);
			
			int handleCode;
			int handleOffset;
			ArrayList entradasTexto = new ArrayList();
			for(int i=0; i<numItems; i++){
				v = DwgUtil.getTextString(data, bitPos);
				bitPos = ((Integer)v.get(0)).intValue();
				String text = ((String)v.get(1));
				entradasTexto.add(text);
			}
			DwgHandleReference handle;
			for(int i=0; i<numItems; i++){
				handle = new DwgHandleReference();
				bitPos = handle.read(data, bitPos);
				//System.out.println("ENTRADA DE DICCIONARIO text= "+entradasTexto.get(i)+" handleCode ="+handle.getCode()+" handleOffset ="+handle.getOffset());
				// A la vez que leemos los handles, rellenamos el diccionario.
				dict.put(entradasTexto.get(i),handle);
			}
			//System.out.println("map.size ="+dict.size());
		}catch (Exception e){
			System.out.println("Se ha producido una excepion leyendo un diccionario."+e.getMessage());
		}
	}
}
