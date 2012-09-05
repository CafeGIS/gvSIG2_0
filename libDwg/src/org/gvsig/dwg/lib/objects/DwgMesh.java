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
* $Id: DwgMesh.java 28969 2009-05-25 13:23:12Z jmvivo $
* $Log$
* Revision 1.2.2.1  2007-02-28 07:35:10  jmvivo
* Actualizado desde el HEAD.
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

public class DwgMesh extends DwgObject {

	private int flags;
	private int curveType;
	private int mVerticies;
	private int nVerticies;
	private int mDensity;
	private int nDensity;
	private DwgHandleReference firstVertexHandle;
	private DwgHandleReference lastVertexHandle;
	private DwgHandleReference seqendHandle;

	public DwgMesh(int index) {
		super(index);
		// TODO Auto-generated constructor stub
	}

	public void setFlags(int flags) {
		this.flags = flags;
	}

	public void setCurveType(int curveType) {
		this.curveType = curveType;
	}

	public void setMVerticies(int verticies) {
		this.mVerticies = verticies;
	}

	public void setNVerticies(int verticies) {
		this.nVerticies = verticies;
	}

	public void setMDensity(int density) {
		this.mDensity = density;
	}

	public void setNDensity(int density) {
		this.nDensity = density;
	}

	public void setFirstVertexHandle(DwgHandleReference handle) {
		this.firstVertexHandle = handle;
	}

	public void setLastVertexHandle(DwgHandleReference handle) {
		this.lastVertexHandle = handle;
	}

	public void setSeqendHandle(DwgHandleReference handle) {
		this.seqendHandle = handle;
	}

	public int getCurveType() {
		return curveType;
	}

	public DwgHandleReference getFirstVertexHandle() {
		return firstVertexHandle;
	}

	public int getFlags() {
		return flags;
	}

	public DwgHandleReference getLastVertexHandle() {
		return lastVertexHandle;
	}

	public int getMDensity() {
		return mDensity;
	}

	public int getMVerticies() {
		return mVerticies;
	}

	public int getNDensity() {
		return nDensity;
	}

	public int getNVerticies() {
		return nVerticies;
	}

	public DwgHandleReference getSeqendHandle() {
		return seqendHandle;
	}
	public Object clone(){
		DwgMesh obj = new DwgMesh(index);
		this.fill(obj);
		return obj;
	}
	
	protected void fill(DwgObject obj){
		super.fill(obj);
		DwgMesh myObj = (DwgMesh)obj;

		myObj.setCurveType(curveType);
		myObj.setFirstVertexHandle(firstVertexHandle);
		myObj.setFlags(flags);
		myObj.setLastVertexHandle(lastVertexHandle);
		myObj.setMDensity(mDensity);
		myObj.setMVerticies(mVerticies);
		myObj.setNDensity(nDensity);
		myObj.setNVerticies(nVerticies);
		myObj.setSeqendHandle(seqendHandle);
	}

}

