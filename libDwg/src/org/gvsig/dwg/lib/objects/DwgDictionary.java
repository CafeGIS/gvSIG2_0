package org.gvsig.dwg.lib.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.gvsig.dwg.lib.DwgHandleReference;
import org.gvsig.dwg.lib.DwgObject;
import org.gvsig.dwg.lib.DwgUtil;


public class DwgDictionary extends DwgObject implements Cloneable, Map, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5594420471375074362L;
	private HashMap map;
	private int hardOwnerFlag;
	private int cloningFlag;
	private DwgHandleReference parentHandle;
	
	public DwgDictionary(int index) {
		super(index);
		map = new HashMap();
	}

	public int size() {
		return map.size();
	}

	public void clear() {
		map.clear();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return map.containsKey(value);
	}

	public Collection values() {
		return map.values();
	}

	public void putAll(Map t) {
		map.putAll(t);
		
	}

	public Set entrySet() {
		return map.entrySet();
	}

	public Set keySet() {
		return map.keySet();
	}

	public Object get(Object key) {
		return map.get(key);
	}

	public Object remove(Object key) {
		return map.remove(key);
	}

	public Object put(Object key, Object value) {
		return map.put(key, value);
	}

	
	public void setCloningFlag(int cloningFlag) {
		this.cloningFlag = cloningFlag;
		
	}

	private int getCloningFlag() {
		return this.cloningFlag;
		
	}

	public void setHardOwnerFlag(int flag){
		this.hardOwnerFlag = flag;
	}
	
	public int getHardOwnerFlag(){
		return this.hardOwnerFlag;
	}

	public void setParentHandle(DwgHandleReference handle) {
		this.parentHandle = handle;
	}
	public Object clone(){
		DwgDictionary obj = new DwgDictionary(index);
		this.fill(obj);
		return obj;
	}
	
	protected void fill(DwgObject obj){
		super.fill(obj);
		DwgDictionary myObj = (DwgDictionary)obj;

		myObj.setCloningFlag(cloningFlag);
		myObj.setHardOwnerFlag(hardOwnerFlag);
		myObj.setParentHandle(parentHandle);
	}

}
