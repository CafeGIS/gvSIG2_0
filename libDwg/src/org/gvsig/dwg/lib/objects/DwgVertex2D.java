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

import org.gvsig.dwg.lib.DwgObject;
import org.gvsig.dwg.lib.IDwgVertex;

/**
 * The DwgVertex2D class represents a DWG Vertex2D
 * 
 * @author jmorell
 */
public class DwgVertex2D extends DwgObject implements IDwgVertex {
	public DwgVertex2D(int index) {
		super(index);
		// TODO Auto-generated constructor stub
	}
	private int flags;
	private double[] point;
	private double initWidth;
	private double endWidth;
	private double bulge;
	private double tangentDir;
	
	
	/**
	 * @return Returns the bulge.
	 */
	public double getBulge() {
		return bulge;
	}
	/**
	 * @param bulge The bulge to set.
	 */
	public void setBulge(double bulge) {
		this.bulge = bulge;
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
	 * @return Returns the point.
	 */
	public double[] getPoint() {
		return point;
	}
	/**
	 * @param point The point to set.
	 */
	public void setPoint(double[] point) {
		this.point = point;
	}
	public double getEndWidth() {
		return endWidth;
	}
	public void setEndWidth(double endWidth) {
		this.endWidth = endWidth;
	}
	public double getInitWidth() {
		return initWidth;
	}
	public void setInitWidth(double initWidth) {
		this.initWidth = initWidth;
	}
	public double getTangentDir() {
		return tangentDir;
	}
	public void setTangentDir(double tangentDir) {
		this.tangentDir = tangentDir;
	}
	public Object clone(){
		DwgVertex2D obj = new DwgVertex2D(index);
		this.fill(obj);
		return obj;
	}
	
	protected void fill(DwgObject obj){
		super.fill(obj);
		DwgVertex2D myObj = (DwgVertex2D)obj;

		myObj.setBulge(bulge);
		myObj.setEndWidth(endWidth);
		myObj.setFlags(flags);
		myObj.setInitWidth(initWidth);
		myObj.setPoint(point);
		myObj.setTangentDir(tangentDir);
	}

}
