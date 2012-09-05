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
import java.util.ArrayList;

import org.gvsig.dwg.lib.DwgHandleReference;
import org.gvsig.dwg.lib.DwgObject;
import org.gvsig.dwg.lib.DwgUtil;
import org.gvsig.dwg.lib.IDwg3DTestable;
import org.gvsig.dwg.lib.IDwgExtrusionable;
import org.gvsig.dwg.lib.util.AcadExtrusionCalculator;


/**
 * The DwgAttrib class represents a DWG Attrib
 * 
 * @author jmorell
 */
public class DwgAttrib extends DwgObject 
implements IDwgExtrusionable, IDwg3DTestable{
	public DwgAttrib(int index) {
		super(index);
		// TODO Auto-generated constructor stub
	}
	private int dataFlag;
	private double elevation;
	private Point2D insertionPoint;
	private Point2D alignmentPoint;
	private double[] extrusion;
	private double thickness;
	private double obliqueAngle;
	private double rotationAngle;
	private double height;
	private double widthFactor;
	private String text;
	private int generation;
	private int halign;
	private int valign;
	private String tag;
	private int fieldLength;
	private int flags;
	private String prompt;
	private DwgHandleReference styleHandle = null;
	
	
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
     * @return Returns the extrusion.
     */
    public double[] getExtrusion() {
        return extrusion;
    }
    /**
     * @return Returns the insertionPoint.
     */
    public Point2D getInsertionPoint() {
        return insertionPoint;
    }
    /**
     * @param insertionPoint The insertionPoint to set.
     */
    public void setInsertionPoint(Point2D insertionPoint) {
        this.insertionPoint = insertionPoint;
    }

	/**
	 * @return Returns the alignmentPoint.
	 */
	public Point2D getAlignmentPoint() {
		return alignmentPoint;
	}
	/**
	 * @param alignmentPoint The alignmentPoint to set.
	 */
	public void setAlignmentPoint(Point2D alignmentPoint) {
		this.alignmentPoint = alignmentPoint;
	}
	/**
	 * @return Returns the dataFlag.
	 */
	public int getDataFlag() {
		return dataFlag;
	}
	/**
	 * @param dataFlag The dataFlag to set.
	 */
	public void setDataFlag(int dataFlag) {
		this.dataFlag = dataFlag;
	}
	/**
	 * @return Returns the fieldLength.
	 */
	public int getFieldLength() {
		return fieldLength;
	}
	/**
	 * @param fieldLength The fieldLength to set.
	 */
	public void setFieldLength(int fieldLength) {
		this.fieldLength = fieldLength;
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
	 * @return Returns the generation.
	 */
	public int getGeneration() {
		return generation;
	}
	/**
	 * @param generation The generation to set.
	 */
	public void setGeneration(int generation) {
		this.generation = generation;
	}
	/**
	 * @return Returns the halign.
	 */
	public int getHalign() {
		return halign;
	}
	/**
	 * @param halign The halign to set.
	 */
	public void setHalign(int halign) {
		this.halign = halign;
	}
	/**
	 * @return Returns the height.
	 */
	public double getHeight() {
		return height;
	}
	/**
	 * @param height The height to set.
	 */
	public void setHeight(double height) {
		this.height = height;
	}
	/**
	 * @return Returns the obliqueAngle.
	 */
	public double getObliqueAngle() {
		return obliqueAngle;
	}
	/**
	 * @param obliqueAngle The obliqueAngle to set.
	 */
	public void setObliqueAngle(double obliqueAngle) {
		this.obliqueAngle = obliqueAngle;
	}
	/**
	 * @return Returns the prompt.
	 */
	public String getPrompt() {
		return prompt;
	}
	/**
	 * @param prompt The prompt to set.
	 */
	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}
	/**
	 * @return Returns the rotationAngle.
	 */
	public double getRotationAngle() {
		return rotationAngle;
	}
	/**
	 * @param rotationAngle The rotationAngle to set.
	 */
	public void setRotationAngle(double rotationAngle) {
		this.rotationAngle = rotationAngle;
	}
	/**
	 * @return Returns the styleHandle.
	 */
	public DwgHandleReference getStyleHandle() {
		return styleHandle;
	}
	/**
	 * @param styleHandle The styleHandle to set.
	 */
	public void setStyleHandle(DwgHandleReference styleHandle) {
		this.styleHandle = styleHandle;
	}
	/**
	 * @return Returns the tag.
	 */
	public String getTag() {
		return tag;
	}
	/**
	 * @param tag The tag to set.
	 */
	public void setTag(String tag) {
		this.tag = tag;
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
	 * @return Returns the thickness.
	 */
	public double getThickness() {
		return thickness;
	}
	/**
	 * @param thickness The thickness to set.
	 */
	public void setThickness(double thickness) {
		this.thickness = thickness;
	}
	/**
	 * @return Returns the valign.
	 */
	public int getValign() {
		return valign;
	}
	/**
	 * @param valign The valign to set.
	 */
	public void setValign(int valign) {
		this.valign = valign;
	}
	/**
	 * @return Returns the widthFactor.
	 */
	public double getWidthFactor() {
		return widthFactor;
	}
	/**
	 * @param widthFactor The widthFactor to set.
	 */
	public void setWidthFactor(double widthFactor) {
		this.widthFactor = widthFactor;
	}
	/**
	 * @param extrusion The extrusion to set.
	 */
	public void setExtrusion(double[] extrusion) {
		this.extrusion = extrusion;
	}
	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.IDwgExtrusionable#applyExtrussion()
	 */
	public void applyExtrussion() {
		  Point2D attribInsertionPoint = getInsertionPoint();
		  if(attribInsertionPoint == null){
			  System.out.println("ATTRIB sin insertion point");
			  return;
		  }
          double attribElevation = getElevation();
          double[] attribInsertionPoint3D = new double[]{
          		attribInsertionPoint.getX(), 
				attribInsertionPoint.getY(), 
				attribElevation};
          double[] attribExt = getExtrusion();
          attribInsertionPoint3D = AcadExtrusionCalculator.
		  	extrude2(attribInsertionPoint3D, attribExt);
          setInsertionPoint(new Point2D.Double(attribInsertionPoint3D[0], 
          									attribInsertionPoint3D[1]));
         setElevation(attribInsertionPoint3D[2]);
	}
	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.IDwg3DTestable#has3DData()
	 */
	public boolean has3DData() {
		return (getElevation() != 0.0);
	}
	public double getZ() {
		return getElevation();
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
public Object clone(){
		DwgAttrib obj = new DwgAttrib(index);
		this.fill(obj);
		return obj;
	}
	
	protected void fill(DwgObject obj){
		super.fill(obj);
		DwgAttrib myObj = (DwgAttrib)obj;

		myObj.setAlignmentPoint(alignmentPoint);
		myObj.setDataFlag(dataFlag);
		myObj.setElevation(elevation);
		myObj.setExtrusion(extrusion);
		myObj.setFieldLength(fieldLength);
		myObj.setFlags(flags);
		myObj.setGeneration(generation);
		myObj.setHalign(halign);
		myObj.setHeight(height);
		myObj.setInsertionPoint(insertionPoint);
		myObj.setObliqueAngle(obliqueAngle);
		myObj.setPrompt(prompt);
		myObj.setRotationAngle(rotationAngle);
		myObj.setStyleHandle(styleHandle);
		myObj.setTag(tag);
		myObj.setText(text);
		myObj.setThickness(thickness);
		myObj.setValign(valign);
		myObj.setWidthFactor(widthFactor);
	}

}
