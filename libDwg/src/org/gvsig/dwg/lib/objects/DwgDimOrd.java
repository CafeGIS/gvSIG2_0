/*
 * Created on 03-feb-2007
 *
 * gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
/* CVS MESSAGES:
*
* $Id: DwgDimOrd.java 28969 2009-05-25 13:23:12Z jmvivo $
* $Log$
* Revision 1.2.2.2  2007-03-21 19:49:16  azabala
* implementation of dwg 12, 13, 14.
*
* Revision 1.2  2007/02/07 12:44:27  fdiaz
* Añadido o modificado el metodo clone para que el DwgObject se encargue de las propiedades comunes a todos los objetos.
* Añadido el metodo fill.
*
* Revision 1.1  2007/02/05 07:03:22  azabala
* *** empty log message ***
*
*
*/
package org.gvsig.dwg.lib.objects;

import org.gvsig.dwg.lib.DwgHandleReference;
import org.gvsig.dwg.lib.DwgObject;

public class DwgDimOrd extends DwgObject {

	private double[] extrusion;
	private double[] textMidPoint;
	private double elevation;
	private int flags;
	private String text;
	private double rotation;
	private double horizDir;
	private double[] insScale;
	private double insRotation;
	private double[] pt12;
	private double[] pt10;
	private double[] pt13;
	private double[] pt14;
	private int flags2;
	private DwgHandleReference dimStyleHandle;
	private DwgHandleReference anonBlockHandle;

	public DwgDimOrd(int index) {
		super(index);
	}

	public void setExtrusion(double[] ds) {
		this.extrusion = ds;
	}


	public void setTextMidPoint(double[] ds) {
		this.textMidPoint = ds;
	}

	public void setElevation(double elevation) {
		this.elevation = elevation;
	}

	public void setFlags(int flags) {
		this.flags = flags;
	}

	public void setText(String txt) {
		this.text = txt;
	}

	public void setTextRotation(double textRotation) {
		this.rotation = textRotation;
	}

	public void setHorizDir(double horizDir) {
		this.horizDir = horizDir;
	}

	public void setInsScale(double[] ds) {
		this.insScale = ds;
	}

	public void setInsRotation(double insRotation) {
		this.insRotation = insRotation;
	}

	public void set12Pt(double[] ds) {
		this.pt12 = ds;
	}

	public void set10Pt(double[] ds) {
		this.pt10 = ds;
	}

	public void set13Pt(double[] ds) {
		this.pt13 = ds;
	}

	public void set14Pt(double[] ds) {
		this.pt14 = ds;
	}

	public void setFlags2(int flags2) {
		this.flags2 = flags2;
	}

	public void setDimStyleHandle(DwgHandleReference handle) {
		this.dimStyleHandle = handle;
	}

	public void setAnonBlockHandle(DwgHandleReference handle) {
		this.anonBlockHandle = handle;
	}

	public double[] getPt10() {
		return pt10;
	}

	public void setPt10(double[] pt10) {
		this.pt10 = pt10;
	}

	public double[] getPt12() {
		return pt12;
	}

	public void setPt12(double[] pt12) {
		this.pt12 = pt12;
	}

	public double[] getPt13() {
		return pt13;
	}

	public void setPt13(double[] pt13) {
		this.pt13 = pt13;
	}

	public double[] getPt14() {
		return pt14;
	}

	public void setPt14(double[] pt14) {
		this.pt14 = pt14;
	}

	public double getRotation() {
		return rotation;
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	public DwgHandleReference getAnonBlockHandle() {
		return anonBlockHandle;
	}

	public DwgHandleReference getDimStyleHandle() {
		return dimStyleHandle;
	}

	public double getElevation() {
		return elevation;
	}

	public double[] getExtrusion() {
		return extrusion;
	}

	public int getFlags() {
		return flags;
	}

	public int getFlags2() {
		return flags2;
	}

	public double getHorizDir() {
		return horizDir;
	}

	public double getInsRotation() {
		return insRotation;
	}

	public double[] getInsScale() {
		return insScale;
	}

	public String getText() {
		return text;
	}

	public double[] getTextMidPoint() {
		return textMidPoint;
	}
	public Object clone(){
		DwgDimOrd obj = new DwgDimOrd(index);
		this.fill(obj);
		return obj;
	}
	
	protected void fill(DwgObject obj){
		super.fill(obj);
		DwgDimOrd myObj = (DwgDimOrd)obj;

		myObj.setAnonBlockHandle(anonBlockHandle);
		myObj.setDimStyleHandle(dimStyleHandle);
		myObj.setElevation(elevation);
		myObj.setExtrusion(extrusion);
		myObj.setFlags(flags);
		myObj.setFlags2(flags2);
		myObj.setHorizDir(horizDir);
		myObj.setInsRotation(insRotation);
		myObj.setInsScale(insScale);
		myObj.setPt10(pt10);
		myObj.setPt12(pt12);
		myObj.setPt13(pt13);
		myObj.setPt14(pt14);
		myObj.setRotation(rotation);
		myObj.setText(text);
		myObj.setTextMidPoint(textMidPoint);
	}

}

