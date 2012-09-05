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
* $Id: DwgFace3D.java 28969 2009-05-25 13:23:12Z jmvivo $
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

import org.gvsig.dwg.lib.DwgObject;

public class DwgFace3D extends DwgObject {

	private double[] corner1;
	private double[] corner2;
	private double[] corner3;
	private double[] corner4;
	private int flags;

	public DwgFace3D(int index) {
		super(index);
		// TODO Auto-generated constructor stub
	}

	public void setCorner1(double[] ds) {
		this.corner1 = ds;
	}

	public void setCorner2(double[] ds) {
		this.corner2 = ds;
	}

	public void setCorner3(double[] ds) {
		this.corner3 = ds;
	}

	public void setCorner4(double[] ds) {
		this.corner4 = ds;
	}

	public void setFlags(int flags) {
		this.flags = flags;
	}

	public double[] getCorner1() {
		return corner1;
	}

	public double[] getCorner2() {
		return corner2;
	}

	public double[] getCorner3() {
		return corner3;
	}

	public double[] getCorner4() {
		return corner4;
	}

	public int getFlags() {
		return flags;
	}

	public Object clone(){
		DwgFace3D obj = new DwgFace3D(index);
		this.fill(obj);
		return obj;
	}
	
	protected void fill(DwgFace3D obj){
		super.fill(obj);
		DwgFace3D myObj = (DwgFace3D)obj;

		myObj.setCorner1(corner1);
		myObj.setCorner2(corner2);
		myObj.setCorner3(corner3);
		myObj.setCorner4(corner4);
		myObj.setFlags(flags);
	}

}

