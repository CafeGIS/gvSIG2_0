/*
 * Created on 30-ene-2007
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
* $Id: DwgDictionaryVar.java 28969 2009-05-25 13:23:12Z jmvivo $
* $Log$
* Revision 1.3.2.2  2007-03-21 19:49:16  azabala
* implementation of dwg 12, 13, 14.
*
* Revision 1.3  2007/02/07 12:44:27  fdiaz
* Añadido o modificado el metodo clone para que el DwgObject se encargue de las propiedades comunes a todos los objetos.
* Añadido el metodo fill.
*
* Revision 1.2  2007/01/31 17:56:40  fdiaz
* Refactorizacion para usar la clase DwgHandleReference
*
* Revision 1.1  2007/01/30 19:40:23  azabala
* *** empty log message ***
*
*
*/
package org.gvsig.dwg.lib.objects;

import org.gvsig.dwg.lib.DwgHandleReference;
import org.gvsig.dwg.lib.DwgObject;

public class DwgDictionaryVar extends DwgObject {
	private int intval;
	private String text;
	private DwgHandleReference parentHandle;

	public DwgDictionaryVar(int index) {
		super(index);
	}

	
	public void setText(String text) {
		this.text = text;
	}

	public void setParentHandle(DwgHandleReference hr) {
		this.parentHandle = hr;
		
	}

	public int getIntval() {
		return intval;
	}

	public void setIntval(int intval) {
		this.intval = intval;
	}

	public DwgHandleReference getParentHandle() {
		return parentHandle;
	}

	public String getText() {
		return text;
	}
	public Object clone(){
		DwgDictionaryVar obj = new DwgDictionaryVar(index);
		this.fill(obj);
		return obj;
	}
	
	protected void fill(DwgObject obj){
		super.fill(obj);
		DwgDictionaryVar myObj = (DwgDictionaryVar)obj;

		myObj.setIntval(intval);
		myObj.setParentHandle(parentHandle);
		myObj.setText(text);
	}

}

