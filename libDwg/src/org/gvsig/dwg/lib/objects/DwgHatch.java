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
* $Id: DwgHatch.java 28969 2009-05-25 13:23:12Z jmvivo $
* $Log$
* Revision 1.4.2.2  2007-03-21 19:49:16  azabala
* implementation of dwg 12, 13, 14.
*
* Revision 1.4  2007/02/07 12:44:27  fdiaz
* Añadido o modificado el metodo clone para que el DwgObject se encargue de las propiedades comunes a todos los objetos.
* Añadido el metodo fill.
*
* Revision 1.3  2007/02/02 12:39:52  azabala
* Added new dwg entities
*
* Revision 1.2  2007/02/01 20:00:27  azabala
* *** empty log message ***
*
* Revision 1.1  2007/01/30 19:40:23  azabala
* *** empty log message ***
*
*
*/
package org.gvsig.dwg.lib.objects;

import java.util.ArrayList;
import java.util.Map;

import org.gvsig.dwg.lib.DwgHandleReference;
import org.gvsig.dwg.lib.DwgObject;


public class DwgHatch extends DwgObject {

	private double z;
	private double[] extrusion;
	private String name;
	private boolean solidFill;
	private boolean associative;
	private Map paths;
	private int style;
	private int patternType;
	private double fillAngle;
	private double fillScaleOrSpace;
	private boolean fileDoubleHath;
	private ArrayList[] lines;
	private double pixelSize;
	private double[][] seedPoints;
	private DwgHandleReference[] handles;

	public DwgHatch(int index) {
		super(index);
	}

	public void setZ(double coord) {
		this.z = coord;
	}

	public void setExtrusion(double[] ds) {
		this.extrusion = ds;
	}

	public void setName(String txt) {
		this.name = txt;
	}

	public void setSolidFill(boolean solidFill) {
		this.solidFill = solidFill;
	}

	public void setAssociative(boolean associative) {
		this.associative = associative;
	}

	public void setPaths(Map paths) {
		this.paths = paths;
	}

	public void setStyle(int style) {
		this.style = style;
	}

	/**
	 * @param patternType
	 */
	public void setPatterType(int patternType) {
		this.patternType = patternType;
	}

	/**
	 * @param fillAngle
	 */
	public void setFillAngle(double fillAngle) {
		this.fillAngle = fillAngle;
	}

	/**
	 * @param fillScaleOrSpacion
	 */
	public void setFillScale(double fillScaleOrSpacion) {
		this.fillScaleOrSpace = fillScaleOrSpacion;
	}

	/**
	 * @param fileDoubleHath
	 */
	public void setFileDoubleHath(boolean fileDoubleHath) {
		this.fileDoubleHath = fileDoubleHath;
	}

	/**
	 * @param lines
	 */
	public void setFillLines(ArrayList[] lines) {
		this.lines = lines;
	}

	/**
	 * @param pixelSize
	 */
	public void setPixelSize(double pixelSize) {
		this.pixelSize = pixelSize;
	}

	/**
	 * @param seedPoints
	 */
	public void setSeedPoints(double[][] seedPoints) {
		this.seedPoints = seedPoints;
	}

	/**
	 * @param handles
	 */
	public void setBoundaryHandles(DwgHandleReference[] handles) {
		this.handles = handles;
	}

	public Object clone(){
		DwgHatch obj = new DwgHatch(index);
		this.fill(obj);
		return obj;
	}
	
	protected void fill(DwgObject obj){
		super.fill(obj);
		DwgHatch myObj = (DwgHatch)obj;

		myObj.setAssociative(associative);
		myObj.setBoundaryHandles(handles);
		myObj.setExtrusion(extrusion);
		myObj.setFileDoubleHath(fileDoubleHath);
		myObj.setFillAngle(fillAngle);
		myObj.setFillLines(lines);
		myObj.setFillScale(fillScaleOrSpace);
		myObj.setName(name);
		myObj.setPaths(paths);
		myObj.setPatterType(patternType);
		myObj.setPixelSize(pixelSize);
		myObj.setSeedPoints(seedPoints);
		myObj.setSolidFill(solidFill);
		myObj.setStyle(style);
		myObj.setZ(z);
	}

}

