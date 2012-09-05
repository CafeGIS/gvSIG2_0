/*
 * Created on 02-feb-2007
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
* $Id: DwgMinsert.java 28969 2009-05-25 13:23:12Z jmvivo $
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

public class DwgMinsert extends DwgObject {

	private double[] insertionPoint;
	private double[] scale;
	private double rotation;
	private double[] extrusion;
	private DwgHandleReference blockHeaderHandle;
	private DwgHandleReference firstAttribHandle;
	private DwgHandleReference lastAttribHandle;
	private DwgHandleReference seqEndHandle;

	public DwgMinsert(int index) {
		super(index);
	}

	public void setInsertionPoint(double[] ds) {
		this.insertionPoint = ds;
	}

	public void setScale(double[] ds) {
		this.scale = ds;
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	public void setExtrusion(double[] ds) {
		this.extrusion = ds;
	}

	public void setBlockHeaderHandle(DwgHandleReference handle) {
		this.blockHeaderHandle = handle;
	}

	public void setFirstAttribHandle(DwgHandleReference handle) {
		this.firstAttribHandle = handle;
	}

	public void setLastAttribHandle(DwgHandleReference handle) {
		this.lastAttribHandle = handle;
	}

	public DwgHandleReference getSeqEndHandle() {
		return seqEndHandle;
	}

	public void setSeqEndHandle(DwgHandleReference seqEndHandle) {
		this.seqEndHandle = seqEndHandle;
	}

	public DwgHandleReference getBlockHeaderHandle() {
		return blockHeaderHandle;
	}

	public double[] getExtrusion() {
		return extrusion;
	}

	public DwgHandleReference getFirstAttribHandle() {
		return firstAttribHandle;
	}

	public double[] getInsertionPoint() {
		return insertionPoint;
	}

	public DwgHandleReference getLastAttribHandle() {
		return lastAttribHandle;
	}

	public double getRotation() {
		return rotation;
	}

	public double[] getScale() {
		return scale;
	}
	public Object clone(){
		DwgMinsert obj = new DwgMinsert(index);
		this.fill(obj);
		return obj;
	}
	
	protected void fill(DwgObject obj){
		super.fill(obj);
		DwgMinsert myObj = (DwgMinsert)obj;

		myObj.setBlockHeaderHandle(blockHeaderHandle);
		myObj.setExtrusion(extrusion);
		myObj.setFirstAttribHandle(firstAttribHandle);
		myObj.setInsertionPoint(insertionPoint);
		myObj.setLastAttribHandle(lastAttribHandle);
		myObj.setRotation(rotation);
		myObj.setScale(scale);
		myObj.setSeqEndHandle(seqEndHandle);
	}
}

