package org.gvsig.dwg.lib.readers.v2004;

import java.util.ArrayList;

import org.gvsig.dwg.lib.DwgHandleReference;
import org.gvsig.dwg.lib.DwgObject;
import org.gvsig.dwg.lib.DwgUtil;
import org.gvsig.dwg.lib.objects.DwgDictionary;


public class DwgDictionaryReader2004 extends AbstractDwg2004Reader{

	public void readSpecificObj(int[] data, int offset, DwgObject dwgObj) {
		if(! (dwgObj instanceof DwgDictionary))
			throw new RuntimeException("DictionaryReader2004 solo puede leer DwgDictionary");
		DwgDictionary dict = (DwgDictionary) dwgObj;
		try {
			int bitPos = offset;

			ArrayList v;

			v = DwgUtil.getBitShort(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			int numReactors = ((Integer)v.get(1)).intValue();
			dict.setNumReactors(numReactors);

			v = DwgUtil.testBit(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			boolean XdicFlag = ((Boolean) v.get(1)).booleanValue();

			v = DwgUtil.getBitLong(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			int numItems = ((Integer)v.get(1)).intValue();

			v = DwgUtil.getBitShort(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			int cloningFlag = ((Integer)v.get(1)).intValue();
			dict.setCloningFlag(cloningFlag);

			v = DwgUtil.getRawChar(data, bitPos);
			bitPos = ((Integer)v.get(0)).intValue();
			int hardOwnerFlag = ((Integer)v.get(1)).intValue();
			dict.setHardOwnerFlag(hardOwnerFlag);

			dict.inserta();

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
				// A la vez que leemos los handles, rellenamos el diccionario.
				dict.put(entradasTexto.get(i),handle);
			}
		}catch (Exception e){
			System.out.println("Se ha producido una excepion leyendo un diccionario."+e.getMessage());
		}
	}
}
