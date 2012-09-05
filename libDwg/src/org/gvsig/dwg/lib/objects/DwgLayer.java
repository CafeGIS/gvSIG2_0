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

import org.gvsig.dwg.lib.DwgHandleReference;
import org.gvsig.dwg.lib.DwgObject;

/**
 * The DwgLayer class represents a DWG Layer
 * 
 * @author jmorell
 */
public class DwgLayer extends DwgObject {
	public DwgLayer(int index) {
		super(index);
		// TODO Auto-generated constructor stub
	}
	private String name;
	private boolean flag64;
	private int xRefPlus;
	private boolean xdep;
	private int flags;
	private int color;
	private DwgHandleReference layerControlHandle = null;
	private DwgHandleReference nullHandle = null;
	private DwgHandleReference plotstyleHandle = null;
	private DwgHandleReference linetypeHandle = null;
	private boolean frozen;
	private boolean on;
	private boolean frozenInNew;
	private boolean locked;
	
	/**
	 * @return Returns the color.
	 */
	public int getColor() {
		return color;
	}
	/**
	 * @param color The color to set.
	 */
	public void setColor(int color) {
		this.color = color;
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
	 * @return Returns the flags.
	 */
	public int getFlags() {
		return flags;
	}
	/**
	 * @param flags The flags to set.
	 */
	public void setFlags(int flags) {
		this.flags = flags;
	}
	/**
	 * @return Returns the layerControlHandle.
	 */
	public DwgHandleReference getLayerControlHandle() {
		return layerControlHandle;
	}
	/**
	 * @param layerControlHandle The layerControlHandle to set.
	 */
	public void setLayerControlHandle(DwgHandleReference layerControlHandle) {
		this.layerControlHandle = layerControlHandle;
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
	 * @return Returns the linetypeHandle.
	 */
	public DwgHandleReference getLinetypeHandle() {
		return linetypeHandle;
	}
	/**
	 * @param linetypeHandle The linetypeHandle to set.
	 */
	public void setLinetypeHandle(DwgHandleReference linetypeHandle) {
		this.linetypeHandle = linetypeHandle;
	}
	/**
	 * @return Returns the plotstyleHandle.
	 */
	public DwgHandleReference getPlotstyleHandle() {
		return plotstyleHandle;
	}
	/**
	 * @param plotstyleHandle The plotstyleHandle to set.
	 */
	public void setPlotstyleHandle(DwgHandleReference plotstyleHandle) {
		this.plotstyleHandle = plotstyleHandle;
	}
	public void setFrozen(boolean frozen) {
		this.frozen = frozen;
	}
	public void setOn(boolean on) {
		this.on = on;
	}
	public void setFrozenInNew(boolean frozenInNew) {
		this.frozenInNew = frozenInNew;
	}
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	public boolean isFrozen() {
		return frozen;
	}
	public boolean isFrozenInNew() {
		return frozenInNew;
	}
	public boolean isLocked() {
		return locked;
	}
	public boolean isOn() {
		return on;
	}
	
	public Object clone(){
		DwgLayer obj = new DwgLayer(index);
		this.fill(obj);
		return obj;
	}
	
	protected void fill(DwgObject obj){
		super.fill(obj);
		DwgLayer myObj = (DwgLayer)obj;

		myObj.setColor(color);
		myObj.setFlag64(flag64);
		myObj.setFlags(flags);
		myObj.setFrozen(frozen);
		myObj.setFrozenInNew(frozenInNew);
		myObj.setLayerControlHandle(layerControlHandle);
		myObj.setLinetypeHandle(linetypeHandle);
		myObj.setLocked(locked);
		myObj.setName(name);
		myObj.setNullHandle(nullHandle);
		myObj.setOn(on);
		myObj.setPlotstyleHandle(plotstyleHandle);
		myObj.setXdep(xdep);
		myObj.setXRefPlus(xRefPlus);

	}

}
