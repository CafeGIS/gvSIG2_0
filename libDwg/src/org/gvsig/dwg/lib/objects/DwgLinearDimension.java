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

import java.awt.geom.Point2D;

import org.gvsig.dwg.lib.DwgHandleReference;
import org.gvsig.dwg.lib.DwgObject;


/**
 * The DwgLinearDimension class represents a DWG Linear dimension
 * 
 * @author jmorell
 */
public class DwgLinearDimension extends DwgObject {
	public DwgLinearDimension(int index) {
		super(index);
		// TODO Auto-generated constructor stub
	}
	private double[] extrusion;
	private Point2D textMidpoint;
	private double elevation;
	private int flags;
	private String text;
	private double rotation;
	private double horizDir;
	private double[] insScale;
	private double insRotation;
	private int attachmentPoint;
	private int linespaceStyle;
	private double linespaceFactor;
	private double actualMeasurement;
	private Point2D pt12;
	private double[] pt10;
	private double[] pt13;
	private double[] pt14;
	private double extRotation;
	private double dimensionRotation;
	private DwgHandleReference dimstyleHandle = null;
	private DwgHandleReference anonBlockHandle = null;
    /**
     * @return Returns the elevation.
     */
    public double getElevation() {
        return elevation;
    }
    /**
     * @param elevation The elevation to set.
     */
    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    /**
	 * @return Returns the actualMeasurement.
	 */
	public double getActualMeasurement() {
		return actualMeasurement;
	}
	/**
	 * @param actualMeasurement The actualMeasurement to set.
	 */
	public void setActualMeasurement(double actualMeasurement) {
		this.actualMeasurement = actualMeasurement;
	}
	/**
	 * @return Returns the anonBlockHandle.
	 */
	public DwgHandleReference getAnonBlockHandle() {
		return anonBlockHandle;
	}
	/**
	 * @param anonBlockHandle The anonBlockHandle to set.
	 */
	public void setAnonBlockHandle(DwgHandleReference anonBlockHandle) {
		this.anonBlockHandle = anonBlockHandle;
	}
	/**
	 * @return Returns the attachmentPoint.
	 */
	public int getAttachmentPoint() {
		return attachmentPoint;
	}
	/**
	 * @param attachmentPoint The attachmentPoint to set.
	 */
	public void setAttachmentPoint(int attachmentPoint) {
		this.attachmentPoint = attachmentPoint;
	}
	/**
	 * @return Returns the dimensionRotation.
	 */
	public double getDimensionRotation() {
		return dimensionRotation;
	}
	/**
	 * @param dimensionRotation The dimensionRotation to set.
	 */
	public void setDimensionRotation(double dimensionRotation) {
		this.dimensionRotation = dimensionRotation;
	}
	/**
	 * @return Returns the dimstyleHandle.
	 */
	public DwgHandleReference getDimstyleHandle() {
		return dimstyleHandle;
	}
	/**
	 * @param dimstyleHandle The dimstyleHandle to set.
	 */
	public void setDimstyleHandle(DwgHandleReference dimstyleHandle) {
		this.dimstyleHandle = dimstyleHandle;
	}
	/**
	 * @return Returns the extRotation.
	 */
	public double getExtRotation() {
		return extRotation;
	}
	/**
	 * @param extRotation The extRotation to set.
	 */
	public void setExtRotation(double extRotation) {
		this.extRotation = extRotation;
	}
	/**
	 * @return Returns the extrusion.
	 */
	public double[] getExtrusion() {
		return extrusion;
	}
	/**
	 * @param extrusion The extrusion to set.
	 */
	public void setExtrusion(double[] extrusion) {
		this.extrusion = extrusion;
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
	 * @return Returns the horizDir.
	 */
	public double getHorizDir() {
		return horizDir;
	}
	/**
	 * @param horizDir The horizDir to set.
	 */
	public void setHorizDir(double horizDir) {
		this.horizDir = horizDir;
	}
	/**
	 * @return Returns the insRotation.
	 */
	public double getInsRotation() {
		return insRotation;
	}
	/**
	 * @param insRotation The insRotation to set.
	 */
	public void setInsRotation(double insRotation) {
		this.insRotation = insRotation;
	}
	/**
	 * @return Returns the insScale.
	 */
	public double[] getInsScale() {
		return insScale;
	}
	/**
	 * @param insScale The insScale to set.
	 */
	public void setInsScale(double[] insScale) {
		this.insScale = insScale;
	}
	/**
	 * @return Returns the linespaceFactor.
	 */
	public double getLinespaceFactor() {
		return linespaceFactor;
	}
	/**
	 * @param linespaceFactor The linespaceFactor to set.
	 */
	public void setLinespaceFactor(double linespaceFactor) {
		this.linespaceFactor = linespaceFactor;
	}
	/**
	 * @return Returns the linespaceStyle.
	 */
	public int getLinespaceStyle() {
		return linespaceStyle;
	}
	/**
	 * @param linespaceStyle The linespaceStyle to set.
	 */
	public void setLinespaceStyle(int linespaceStyle) {
		this.linespaceStyle = linespaceStyle;
	}
	/**
	 * @return Returns the pt10.
	 */
	public double[] getPt10() {
		return pt10;
	}
	/**
	 * @param pt10 The pt10 to set.
	 */
	public void setPt10(double[] pt10) {
		this.pt10 = pt10;
	}
	/**
	 * @return Returns the pt12.
	 */
	public Point2D getPt12() {
		return pt12;
	}
	/**
	 * @param pt12 The pt12 to set.
	 */
	public void setPt12(Point2D pt12) {
		this.pt12 = pt12;
	}
	/**
	 * @return Returns the pt13.
	 */
	public double[] getPt13() {
		return pt13;
	}
	/**
	 * @param pt13 The pt13 to set.
	 */
	public void setPt13(double[] pt13) {
		this.pt13 = pt13;
	}
	/**
	 * @return Returns the pt14.
	 */
	public double[] getPt14() {
		return pt14;
	}
	/**
	 * @param pt14 The pt14 to set.
	 */
	public void setPt14(double[] pt14) {
		this.pt14 = pt14;
	}
	/**
	 * @return Returns the rotation.
	 */
	public double getRotation() {
		return rotation;
	}
	/**
	 * @param rotation The rotation to set.
	 */
	public void setRotation(double rotation) {
		this.rotation = rotation;
	}
	/**
	 * @return Returns the text.
	 */
	public String getText() {
		return text;
	}
	/**
	 * @param text The text to set.
	 */
	public void setText(String text) {
		this.text = text;
	}
	/**
	 * @return Returns the textMidpoint.
	 */
	public Point2D getTextMidpoint() {
		return textMidpoint;
	}
	/**
	 * @param textMidpoint The textMidpoint to set.
	 */
	public void setTextMidpoint(Point2D textMidpoint) {
		this.textMidpoint = textMidpoint;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone(){
		DwgLinearDimension obj = new DwgLinearDimension(index);
		this.fill(obj);
		return obj;
	}
	
	protected void fill(DwgObject obj){
		super.fill(obj);
		DwgLinearDimension myObj = (DwgLinearDimension)obj;

		myObj.setActualMeasurement(actualMeasurement);
		myObj.setAnonBlockHandle(anonBlockHandle);
		myObj.setAttachmentPoint(attachmentPoint);
		myObj.setDimensionRotation(dimensionRotation);
		myObj.setDimstyleHandle(dimstyleHandle);
		myObj.setElevation(elevation);
		myObj.setExtRotation(extRotation);
		myObj.setExtrusion(extrusion);
		myObj.setFlags(flags);
		myObj.setHorizDir(horizDir);
		myObj.setInsRotation(insRotation);
		myObj.setInsScale(insScale);
		myObj.setLinespaceFactor(linespaceFactor);
		myObj.setLinespaceStyle(linespaceStyle);
		myObj.setPt10(pt10);
		myObj.setPt12(pt12);
		myObj.setPt13(pt13);
		myObj.setPt14(pt14);
		myObj.setRotation(rotation);
		myObj.setText(text);
		myObj.setTextMidpoint(textMidpoint);
	}

}
