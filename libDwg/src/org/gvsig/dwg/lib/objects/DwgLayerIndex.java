/*
 * Created on 02-feb-2007 by azabala
 *
 */
package org.gvsig.dwg.lib.objects;

import java.util.ArrayList;
import java.util.List;

import org.gvsig.dwg.lib.DwgHandleReference;
import org.gvsig.dwg.lib.DwgObject;


/**
 * @author alzabord
 *
 * TODO Posiblemente esta clase no haga falta leerla, porque ya la construimos
 * nosotros luego recorriendo todos los DwgLayer cargados
 */
public class DwgLayerIndex extends DwgObject {

	private int timestamp1;
	private int timestamp2;
	
	private List indexEntries;
	private List indexHandles;
	
	private DwgHandleReference parentHandle;

	/**
	 * @param index
	 */
	public DwgLayerIndex(int index) {
		super(index);
		indexEntries = new ArrayList();
		indexHandles = new ArrayList();
	}

	public void setTimestamp1(int timestamp1) {
		this.timestamp1 = timestamp1;
	}

	public void setTimestamp2(int timestamp12) {
		this.timestamp2 = timestamp12;
	}

	public void addIndex(int indexLong, String indexStr) {
		IndexEntry entry = new IndexEntry();
		entry.indexLong = indexLong;
		entry.indexStr = indexStr;
		indexEntries.add(entry);
		
	}
	
	public void addHandleEntry(DwgHandleReference handle){
		this.indexHandles.add(handle);
	}
	
	class IndexEntry{
		int indexLong;
		String indexStr;
	}

	public List getIndexEntries() {
		return indexEntries;
	}

	public void setIndexEntries(List indexEntries) {
		this.indexEntries = indexEntries;
	}

	public int getTimestamp1() {
		return timestamp1;
	}

	public int getTimestamp2() {
		return timestamp2;
	}

	public void setParentHandle(DwgHandleReference handle) {
		this.parentHandle = handle;
	}

	public List getIndexHandles() {
		return indexHandles;
	}

	public void setIndexHandles(List indexHandles) {
		this.indexHandles = indexHandles;
	}

	public DwgHandleReference getParentHandle() {
		return parentHandle;
	}
	public Object clone(){
		DwgLayerIndex obj = new DwgLayerIndex(index);
		this.fill(obj);
		return obj;
	}
	
	protected void fill(DwgObject obj){
		super.fill(obj);
		DwgLayerIndex myObj = (DwgLayerIndex)obj;

		myObj.setIndexEntries(indexEntries);
		myObj.setIndexHandles(indexHandles);
		myObj.setParentHandle(parentHandle);
		myObj.setTimestamp1(timestamp1);
		myObj.setTimestamp2(timestamp2);

	}

}
