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
package org.gvsig.dwg.lib;

import java.util.ArrayList;
import java.util.List;

/**
 * The DwgObject class represents a DWG object
 * 
 * @author jmorell
 */
public class DwgObject implements Cloneable{
	protected int type;

	protected DwgHandleReference handle;

	protected String version;

	protected int mode;

	/**
	 * code of the layer handle
	 */
	//protected int layerHandleCode;

	/**
	 * layer handle as an integer
	 */
	protected DwgHandleReference layerHandle;

	protected int color;

	protected int numReactors;

	protected boolean noLinks;

	protected int linetypeFlags;

	protected int plotstyleFlags;

	protected int sizeInBits;

	protected List extendedData;

	protected int graphicData;
	
	protected int address;

	protected DwgHandleReference plotStyleHandle = null;

	protected DwgHandleReference subEntityHandle = null;

	protected DwgHandleReference xDicObjHandle = null;

	protected boolean graphicsFlag;
	protected boolean xDicObjFlag;
	protected boolean avanzarFlag=false;
	
	/**
	 * Index of the dwg object in the object's map section
	 *  
	 */
	protected int index = 0;

	/*
	 * Previous and Next Handle (this stuff has allowed us to solve the problem
	 * of layer handles
	 */
	private DwgHandleReference nextHandle = null;

	private DwgHandleReference previousHandle = null;

	//private ArrayList reactorsHandlesCodes = new ArrayList();

	private ArrayList reactorsHandles = new ArrayList();

	private boolean insertar = false;
	
	
	public DwgObject(int index) {
		this.index = index;
	}
	
	public void inserta(){
		this.insertar=true;
	}
	
	public boolean insertar(){
		return this.insertar;
	}
	
	public void setAddress(int address){
		this.address=address;
	}
	
	public int getAddress(){
		return this.address;
	}

	public void setNextHandle(DwgHandleReference hr) {
		this.nextHandle = hr;

	}

	public void setPreviousHandle(DwgHandleReference hr) {
		this.previousHandle = hr;
	}

	/* 
	public void setReactorsHandles(ArrayList reactorsHandles) {
		this.reactorsHandlesCodes=reactorsHandles;
	}
	*/
	
	
	public void setAvanzar(boolean avanza){
		this.avanzarFlag=avanza;
	}
	public boolean getAvanzar(){
		return this.avanzarFlag;
	}
	
	
	
	

	public void addReactorHandle(DwgHandleReference hr) {
		if (this.reactorsHandles == null){
			this.reactorsHandles = new ArrayList();
		}
		this.reactorsHandles.add(hr);
	}


	public DwgHandleReference getNextHandle() {
		return this.nextHandle;

	}

	public DwgHandleReference getPreviousHandle() {
		return this.previousHandle;
	}

	public ArrayList getReactorsHandles() {
		return this.reactorsHandles;
	}

	//TODO Todo esto no vale si handle puede tomar valor -1
	public boolean hasLayerHandle() {
		return this.layerHandle != null;
	}
	public boolean hasNextHandle() {
		return this.nextHandle != null;
	}

	public boolean hasPreviousHandle() {
		return this.previousHandle != null;
	}

	public boolean hasSubEntityHandle(){
		return this.subEntityHandle != null;
	}

	public boolean hasXDicObjHandle(){
		return this.xDicObjHandle != null;
	}

	public boolean hasReactorsHandles(){
		return this.reactorsHandles.size() != 0;
	}

	public int reactorsHandlesQuantity(){
		return this.reactorsHandles.size();
	}

	public int getIndex() {
		return index;
	}

	/**
	 * @return Returns the sizeInBits.
	 */
	public int getSizeInBits() {
		return sizeInBits;
	}

	/**
	 * @param sizeInBits
	 *            The sizeInBits to set.
	 */
	public void setSizeInBits(int sizeInBits) {
		this.sizeInBits = sizeInBits;
	}

	/**
	 * @return Returns the extendedData.
	 */
	public List getExtendedData() {
		return extendedData;
	}

	/**
	 * @param extData
	 *            The extendedData to set.
	 */
	public void setExtendedData(List extData) {
		this.extendedData = extData;
	}

	/**
	 * @return Returns the graphicData.
	 */
	public int getGraphicData() {
		return graphicData;
	}

	/**
	 * @param graphicData
	 *            The graphicData to set.
	 */
	public void setGraphicData(int graphicData) {
		this.graphicData = graphicData;
	}

	/**
	 * @return Returns the version.
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param linetypeFlags
	 *            The linetypeFlags to set.
	 */
	public void setLinetypeFlags(int linetypeFlags) {
		this.linetypeFlags = linetypeFlags;
	}

	/**
	 * @param plotstyleFlags
	 *            The plotstyleFlags to set.
	 */
	public void setPlotstyleFlags(int plotstyleFlags) {
		this.plotstyleFlags = plotstyleFlags;
	}

	/**
	 * @return Returns the subEntityHandle.
	 */
	public DwgHandleReference getSubEntityHandle() {
		return subEntityHandle;
	}

	/**
	 * @param subEntityHandle
	 *            The subEntityHandle to set.
	 */
	public void setSubEntityHandle(DwgHandleReference subEntityHandle) {
		this.subEntityHandle = subEntityHandle;
	}

	/**
	 * @return Returns the xDicObjHandle.
	 */
	public DwgHandleReference getXDicObjHandle() {
		return xDicObjHandle;
	}

	/**
	 * @param dicObjHandle
	 *            The xDicObjHandle to set.
	 */
	public void setXDicObjHandle(DwgHandleReference dicObjHandle) {
		xDicObjHandle = dicObjHandle;
	}

	/**
	 * @return Returns the color.
	 */
	public int getColor() {
		return color;
	}

	/**
	 * @param color
	 *            The color to set.
	 */
	public void setColor(int color) {
		this.color = color;
	}

	/**
	 * @return Returns the handle.
	 */
	public DwgHandleReference getHandle() {
		return handle;
	}

	/**
	 * @param handle
	 *            The handle to set.
	 */
	public void setHandle(DwgHandleReference handle) {
		this.handle = handle;
	}

	/**
	 * @return Returns the layerHandle.
	 */
	public DwgHandleReference getLayerHandle() {
		return layerHandle;
	}

	/**
	 * @param layerHandle
	 *            The layerHandle to set.
	 */
	public void setLayerHandle(DwgHandleReference layerHandle) {
		this.layerHandle = layerHandle;
	}

	/**
	 * @return Returns the mode.
	 */
	public int getMode() {
		return mode;
	}

	/**
	 * @param mode
	 *            The mode to set.
	 */
	public void setMode(int mode) {
		this.mode = mode;
	}

	/**
	 * @return Returns the noLinks.
	 */
	public boolean isNoLinks() {
		return noLinks;
	}

	/**
	 * @param noLinks
	 *            The noLinks to set.
	 */
	public void setNoLinks(boolean noLinks) {
		this.noLinks = noLinks;
	}

	/**
	 * @return Returns the numReactors.
	 */
	public int getNumReactors() {
		return numReactors;
	}

	/**
	 * @param numReactors
	 *            The numReactors to set.
	 */
	public void setNumReactors(int numReactors) {
		this.numReactors = numReactors;
	}

	/**
	 * @return Returns the type.
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            The type to set.
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return Returns the linetypeFlags.
	 */
	public int getLinetypeFlags() {
		return linetypeFlags;
	}

	/**
	 * @return Returns the plotstyleFlags.
	 */
	public int getPlotstyleFlags() {
		return plotstyleFlags;
	}

	/**
	 * @param version
	 *            The version to set.
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return Returns the graphicsFlag.
	 */
	public boolean isGraphicsFlag() {
		return graphicsFlag;
	}

	/**
	 * @param graphicsFlag
	 *            The graphicsFlag to set.
	 */
	public void setGraphicsFlag(boolean graphicsFlag) {
		this.graphicsFlag = graphicsFlag;
	}
	
	
	/**
	 * @return Returns the xDicObjFlag.
	 */
	public boolean isXDicObjFlag() {
		return xDicObjFlag;
	}

	/**
	 * @param xDicObjFlag
	 *            The xDicObjFlag to set.
	 */
	public void setXDicObjFlag(boolean xDicObjFlag) {
		this.xDicObjFlag = xDicObjFlag;
	}

	/*
	 * This property exists in 13-14 versions, but not in 2000 version
	 */
	private boolean lyrByLineType = false;

	public void setLyrByLineType(boolean lyrByLineType) {
		this.lyrByLineType = lyrByLineType;
	}

	public boolean isLyrByLineType() {
		return lyrByLineType;
	}

	public void setPlotStyleHandle(DwgHandleReference hr) {
		this.plotStyleHandle = hr;

	}
	
	public boolean hasPlotStyleHandle() {
		return this.plotStyleHandle != null;
	}

	/*
	 * Esto solo se usa para la version 13-14
	 */
	private DwgHandleReference lineTypeHandle = null;

	/**
	 * Sets the handle of the line type of this drawing entity.
	 * 
	 * TODO Ver si conviene guardar tambien el handleCode de este handle
	 * 
	 * @param handle2
	 */
	public void setLineTypeHandle(DwgHandleReference hr) {
		this.lineTypeHandle = hr;

	}

	public DwgHandleReference getLineTypeHandle() {
		return this.lineTypeHandle;
	}
	
	public boolean hasLineTypeHandle() {
		return this.lineTypeHandle != null;
	}

	
	public Object clone(){
		DwgObject obj = new DwgObject(this.index);
		this.fill(obj);
		return obj;
	}
	
	protected void fill(DwgObject obj){

		obj.setColor(color);
		obj.setExtendedData(extendedData);
		obj.setGraphicData(graphicData);
		obj.setGraphicsFlag(graphicsFlag);
		obj.setHandle(handle);
		obj.setLayerHandle(layerHandle);
		obj.setLinetypeFlags(linetypeFlags);
		obj.setLineTypeHandle(lineTypeHandle);
		obj.setLyrByLineType(lyrByLineType);
		obj.setMode(mode);
		obj.setNextHandle(nextHandle);
		obj.setNoLinks(noLinks);
		obj.setNumReactors(numReactors);
		obj.setPlotstyleFlags(plotstyleFlags);
		obj.setPlotStyleHandle(plotStyleHandle);
		obj.setPreviousHandle(previousHandle);
		obj.setSizeInBits(sizeInBits);
		obj.setSubEntityHandle(subEntityHandle);
		obj.setType(type);
		obj.setVersion(version);
		obj.setXDicObjHandle(xDicObjHandle);

	}
}