/*
 * Created on 25-ene-2007 by azabala
 *
 */
package org.gvsig.dwg.lib.readers.v1314;

import java.util.List;

import org.gvsig.dwg.lib.CorruptedDwgEntityException;
import org.gvsig.dwg.lib.DwgHandleReference;
import org.gvsig.dwg.lib.DwgObject;
import org.gvsig.dwg.lib.DwgUtil;
import org.gvsig.dwg.lib.objects.DwgBlockHeader;


/**
 * @author alzabord
 *
 */
public class DwgBlockHeaderReader1314 extends AbstractDwg1314Reader{

	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.readers.IDwgObjectReader#readSpecificObj(int[], int, com.iver.cit.jdwglib.dwg.DwgObject)
	 */
	
	//TODO REVISAR COMO TRATAR LAS REFERENCIAS EXTERNAS
	public void readSpecificObj(int[] data, int offset, DwgObject dwgObj) throws RuntimeException, CorruptedDwgEntityException {
		 if(! (dwgObj instanceof DwgBlockHeader))
		    	throw new RuntimeException("ArcReader 14 solo puede leer DwgBlockHeader");
		 DwgBlockHeader blk = (DwgBlockHeader) dwgObj;
		 int bitPos = offset;
		 
		 List val = DwgUtil.getBitLong(data, bitPos);
		 bitPos = ((Integer) val.get(0)).intValue();
		 int numReactors = ((Integer) val.get(1)).intValue();
		 blk.setNumReactors(numReactors);
		
		 val = DwgUtil.getTextString(data, bitPos);
		 bitPos = ((Integer) val.get(0)).intValue();
		 String name = (String) val.get(1);
		 blk.setName(name);
		 
		 val = DwgUtil.testBit(data, bitPos);
		 bitPos = ((Integer) val.get(0)).intValue();
		 boolean flag64 = ((Boolean) val.get(1)).booleanValue();
		 blk.setFlag64(flag64);
		 
		 val = DwgUtil.getBitShort(data, bitPos);
		 bitPos = ((Integer) val.get(0)).intValue();
		 int xRefPlus = ((Integer) val.get(1)).intValue();
		 blk.setXRefPlus(xRefPlus);
		 
		 val = DwgUtil.testBit(data, bitPos);
		 bitPos = ((Integer) val.get(0)).intValue();
		 boolean xdep = ((Boolean) val.get(1)).booleanValue();
		 blk.setXdep(xdep);
		 
		 val = DwgUtil.testBit(data, bitPos);
		 bitPos = ((Integer) val.get(0)).intValue();
		 boolean anonymous = ((Boolean) val.get(1)).booleanValue();
		 blk.setAnonymous(anonymous);
		 
		 val = DwgUtil.testBit(data, bitPos);
		 bitPos = ((Integer) val.get(0)).intValue();
		 boolean hasAttr = ((Boolean) val.get(1)).booleanValue();
		 blk.setHasAttrs(hasAttr);
		 
		 val = DwgUtil.testBit(data, bitPos);
		 bitPos = ((Integer) val.get(0)).intValue();
		 boolean blkIsXref = ((Boolean) val.get(1)).booleanValue();
		 blk.setBlkIsXRef(blkIsXref);
		 
		 val = DwgUtil.testBit(data, bitPos);
		 bitPos = ((Integer) val.get(0)).intValue();
		 boolean xOver = ((Boolean) val.get(1)).booleanValue();
		 blk.setXRefOverLaid(xOver);
		 
		 val = DwgUtil.getBitDouble(data, bitPos);
		 bitPos = ((Integer) val.get(0)).intValue();
		 double x = ((Double) val.get(1)).doubleValue();
		 
		 val = DwgUtil.getBitDouble(data, bitPos);
		 bitPos = ((Integer) val.get(0)).intValue();
		 double y = ((Double) val.get(1)).doubleValue();
		 
		 val = DwgUtil.getBitDouble(data, bitPos);
		 bitPos = ((Integer) val.get(0)).intValue();
		 double z = ((Double) val.get(1)).doubleValue();
		 blk.setBasePoint(new double[]{x, y, z});
		 
		 val = DwgUtil.getTextString(data, bitPos);
		 bitPos = ((Integer) val.get(0)).intValue();
		 String xRefPname = (String) val.get(1);
		 blk.setXRefPName(xRefPname);
		 
		 DwgHandleReference handle = new DwgHandleReference();
		 bitPos = handle.read(data, bitPos);
		 blk.setBlockControlHandle(handle);
		 
		 for( int i = 0; i < numReactors; i++){
			 handle = new DwgHandleReference();
			 bitPos = handle.read(data, bitPos);
			 blk.addReactorHandle(handle);
		 }
		 
		 handle = new DwgHandleReference();
		 bitPos = handle.read(data, bitPos);
		 blk.setXDicObjHandle(handle);
		 
		 handle = new DwgHandleReference();
		 bitPos = handle.read(data, bitPos);
		 blk.setNullHandle(handle);
		 
		 //En python no se llama a head ni tailer,
		 //luego este debe ser el handle
		 handle = new DwgHandleReference();
		 bitPos = handle.read(data, bitPos);
		 blk.setHandle(handle);
		 
		 /*
		  * Esto aclara algo: un bloque solo mantiene referencias
		  * a sus entidades SI NO ES UNA REFERENCIA EXTERNA
		  * */
		 if(! blkIsXref && ! xOver){
			 handle = new DwgHandleReference();
			 bitPos = handle.read(data, bitPos);
			 blk.setFirstEntityHandle(handle);
			 
			 handle = new DwgHandleReference();
			 bitPos = handle.read(data, bitPos);
			 blk.setLastEntityHandle(handle);
		 }
		 handle = new DwgHandleReference();
		 bitPos = handle.read(data, bitPos);
		 blk.setEndBlkEntityHandle(handle);
	}
}
