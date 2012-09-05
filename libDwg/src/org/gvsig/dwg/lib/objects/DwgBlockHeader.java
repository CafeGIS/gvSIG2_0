/* jdwglib. Java Library for reading Dwg files.
 *
 * Author: Jose Morell Rama (jose.morell@gmail.com).
 * Port from the Pythoncad Dwg library by Art Haas.
 *
 * Copyright (C) 2005 Jose Morell, IVER TI S.A. and Generalitat Valenciana
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
 * Jose Morell (jose.morell@gmail.com)
 *
 * or
 *
 * IVER TI S.A.
 *  C/Salamanca, 50
 *  46005 Valencia
 *  Spain
 *  +34 963163400
 *  dac@iver.es
 */
package org.gvsig.dwg.lib.objects;

import java.util.ArrayList;

import org.gvsig.dwg.lib.DwgHandleReference;
import org.gvsig.dwg.lib.DwgObject;
import org.gvsig.dwg.lib.IDwg3DTestable;


/**
 * The DwgBlockHeader class represents a DWG Block header
 *
 * @author jmorell, azabala
 */
public class DwgBlockHeader extends DwgObject implements IDwg3DTestable {
	private String name;
	private boolean flag64;
	private int xRefPlus;
	private boolean xdep;
	private boolean anonymous;
	private boolean hasAttrs;

	//TODO REVISAR COMO TRATAR UN BLOQUE CUANDO ES UNA REFERENCIA
	//EXTERNA (DwgFile.blockManagement() )
	private boolean blkIsXRef;
	private boolean xRefOverLaid;
	private boolean loaded;
	private double[] basePoint;
	private String xRefPName;
	private String blockDescription;
	private int previewData;

	private DwgHandleReference blockControlHandle;
	private DwgHandleReference nullHandle;
	private DwgHandleReference blockEntityHandle;
	private DwgHandleReference firstEntityHandle;
	private DwgHandleReference lastEntityHandle;
	private DwgHandleReference endBlkEntityHandle;


	private DwgHandleReference[] insertHandles;

	private DwgHandleReference layoutHandle;
	private ArrayList objects;
	private ArrayList ownedObjectsHandles; //For R2004+

	/**
	 * Create new DwgBlockHeader object
	 */
	public DwgBlockHeader(int index) {
		super(index);
		objects = new ArrayList();
		ownedObjectsHandles = new ArrayList();
	}

	/**
	 * @return Returns the basePoint.
	 */
	public double[] getBasePoint() {
		return basePoint;
	}
	/**
	 * @param basePoint The basePoint to set.
	 */
	public void setBasePoint(double[] basePoint) {
		this.basePoint = basePoint;
	}
	/**
	 * @return Returns the firstEntityHandle.
	 */
	public DwgHandleReference getFirstEntityHandle() {
		return firstEntityHandle;
	}
	/**
	 * @param firstEntityHandle The firstEntityHandle to set.
	 */
	public void setFirstEntityHandle(DwgHandleReference firstEntityHandle) {
		this.firstEntityHandle = firstEntityHandle;
	}
	/**
	 * @return Returns the lastEntityHandle.
	 */
	public DwgHandleReference getLastEntityHandle() {
		return lastEntityHandle;
	}
	/**
	 * @param lastEntityHandle The lastEntityHandle to set.
	 */
	public void setLastEntityHandle(DwgHandleReference lastEntityHandle) {
		this.lastEntityHandle = lastEntityHandle;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the blockEntityHandle.
	 */
	public DwgHandleReference getBlockEntityHandle() {
		return blockEntityHandle;
	}
	/**
	 * @param blockEntityHandle The blockEntityHandle to set.
	 */
	public void setBlockEntityHandle(DwgHandleReference blockEntityHandle) {
		this.blockEntityHandle = blockEntityHandle;
	}
	/**
	 * @return Returns the anonymous.
	 */
	public boolean isAnonymous() {
		return anonymous;
	}
	/**
	 * @param anonymous The anonymous to set.
	 */
	public void setAnonymous(boolean anonymous) {
		this.anonymous = anonymous;
	}
	/**
	 * @return Returns the blkIsXRef.
	 */
	public boolean isBlkIsXRef() {
		return blkIsXRef;
	}
	/**
	 * @param blkIsXRef The blkIsXRef to set.
	 */
	public void setBlkIsXRef(boolean blkIsXRef) {
		this.blkIsXRef = blkIsXRef;
	}
	/**
	 * @return Returns the blockControlHandle.
	 */
	public DwgHandleReference getBlockControlHandle() {
		return blockControlHandle;
	}
	/**
	 * @param blockControlHandle The blockControlHandle to set.
	 */
	public void setBlockControlHandle(DwgHandleReference blockControlHandle) {
		this.blockControlHandle = blockControlHandle;
	}
	/**
	 * @return Returns the blockDescription.
	 */
	public String getBlockDescription() {
		return blockDescription;
	}
	/**
	 * @param blockDescription The blockDescription to set.
	 */
	public void setBlockDescription(String blockDescription) {
		this.blockDescription = blockDescription;
	}
	/**
	 * @return Returns the endBlkEntityHandle.
	 */
	public DwgHandleReference getEndBlkEntityHandle() {
		return endBlkEntityHandle;
	}
	/**
	 * @param endBlkEntityHandle The endBlkEntityHandle to set.
	 */
	public void setEndBlkEntityHandle(DwgHandleReference endBlkEntityHandle) {
		this.endBlkEntityHandle = endBlkEntityHandle;
	}
	/**
	 * @return Returns the flag64.
	 */
	public boolean isFlag64() {
		return flag64;
	}
	/**
	 * @param flag64 The flag64 to set.
	 */
	public void setFlag64(boolean flag64) {
		this.flag64 = flag64;
	}
	/**
	 * @return Returns the hasAttrs.
	 */
	public boolean isHasAttrs() {
		return hasAttrs;
	}
	/**
	 * @param hasAttrs The hasAttrs to set.
	 */
	public void setHasAttrs(boolean hasAttrs) {
		this.hasAttrs = hasAttrs;
	}
	/**
	 * @return Returns the insertHandles.
	 */
	public DwgHandleReference[] getInsertHandles() {
		return insertHandles;
	}
	/**
	 * @param insertHandles The insertHandles to set.
	 */
	public void setInsertHandles(DwgHandleReference[] insertHandles) {
		this.insertHandles = insertHandles;
	}
	/**
	 * @return Returns the layoutHandle.
	 */
	public DwgHandleReference getLayoutHandle() {
		return layoutHandle;
	}
	/**
	 * @param handle The layoutHandle to set.
	 */
	public void setLayoutHandle(DwgHandleReference handle) {
		this.layoutHandle = handle;
	}
	/**
	 * @return Returns the loaded.
	 */
	public boolean isLoaded() {
		return loaded;
	}
	/**
	 * @param loaded The loaded to set.
	 */
	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}
	/**
	 * @return Returns the nullHandle.
	 */
	public DwgHandleReference getNullHandle() {
		return nullHandle;
	}
	/**
	 * @param nullHandle The nullHandle to set.
	 */
	public void setNullHandle(DwgHandleReference nullHandle) {
		this.nullHandle = nullHandle;
	}
	/**
	 * @return Returns the previewData.
	 */
	public int getPreviewData() {
		return previewData;
	}
	/**
	 * @param previewData The previewData to set.
	 */
	public void setPreviewData(int previewData) {
		this.previewData = previewData;
	}
	/**
	 * @return Returns the xdep.
	 */
	public boolean isXdep() {
		return xdep;
	}
	/**
	 * @param xdep The xdep to set.
	 */
	public void setXdep(boolean xdep) {
		this.xdep = xdep;
	}
	/**
	 * @return Returns the xRefOverLaid.
	 */
	public boolean isXRefOverLaid() {
		return xRefOverLaid;
	}
	/**
	 * @param refOverLaid The xRefOverLaid to set.
	 */
	public void setXRefOverLaid(boolean refOverLaid) {
		xRefOverLaid = refOverLaid;
	}
	/**
	 * @return Returns the xRefPlus.
	 */
	public int getXRefPlus() {
		return xRefPlus;
	}
	/**
	 * @param refPlus The xRefPlus to set.
	 */
	public void setXRefPlus(int refPlus) {
		xRefPlus = refPlus;
	}
	/**
	 * @return Returns the xRefPName.
	 */
	public String getXRefPName() {
		return xRefPName;
	}
	/**
	 * @param refPName The xRefPName to set.
	 */
	public void setXRefPName(String refPName) {
		xRefPName = refPName;
	}
	/**
	 * @return Returns the objects.
	 */
	public ArrayList getObjects() {
		return objects;
	}
	/**
	 * @param objects The objects to set.
	 */
	public void setObjects(ArrayList objects) {
		this.objects = objects;
	}
	/**
	 * Add a DWG object to the blockObjects vector
	 *
	 * @param object DWG object
	 */
	public void addObject(DwgObject object) {
		this.objects.add(object);
	}

	/**
	 * @return Returns the owned objects handles.
	 */
	public ArrayList getOwnedObjectsHandles() {
		return ownedObjectsHandles;
	}
	/**
	 * @param objects The owned objects handles to set.
	 */
	public void setOwnedObjectsHandles(ArrayList handles) {
		this.ownedObjectsHandles = handles;
	}
	/**
	 * Add a handle to the ownedBbjectsHandle vector
	 *
	 * @param handle handle
	 */
	public void addOwnedObjectHandle(DwgHandleReference handle) {
		this.ownedObjectsHandles.add(handle);
	}


	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.IDwg3DTestable#has3DData()
	 */
	public boolean has3DData() {
		return (getBasePoint()[2] != 0.0);
	}
	public double getZ() {
		return getBasePoint()[2];
	}
	public Object clone(){
		DwgBlockHeader obj = new DwgBlockHeader(index);
		this.fill(obj);
		return obj;
	}

	protected void fill(DwgObject obj){
		super.fill(obj);
		DwgBlockHeader myObj = (DwgBlockHeader)obj;

		myObj.setAnonymous(anonymous);
		myObj.setBasePoint(basePoint);
		myObj.setBlkIsXRef(blkIsXRef);
		myObj.setBlockControlHandle(blockControlHandle);
		myObj.setBlockDescription(blockDescription);
		myObj.setBlockEntityHandle(blockEntityHandle);
		myObj.setEndBlkEntityHandle(endBlkEntityHandle);
		myObj.setFirstEntityHandle(firstEntityHandle);
		myObj.setFlag64(flag64);
		myObj.setHasAttrs(hasAttrs);
		myObj.setInsertHandles(insertHandles);
		myObj.setLastEntityHandle(lastEntityHandle);
		myObj.setLayoutHandle(layoutHandle);
		myObj.setLoaded(loaded);
		myObj.setName(name);
		myObj.setNullHandle(nullHandle);
		myObj.setObjects(objects);
		myObj.setPreviewData(previewData);
		myObj.setXdep(xdep);
		myObj.setXRefOverLaid(xRefOverLaid);
		myObj.setXRefPlus(xRefPlus);
		myObj.setXRefPName(xRefPName);
	}

}
