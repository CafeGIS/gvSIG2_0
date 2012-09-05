package org.gvsig.gpe.containers;

import java.util.ArrayList;

/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
 * $Id: Bbox.java 54 2007-04-14 16:06:35Z jorpiell $
 * $Log$
 * Revision 1.1  2007/04/14 16:06:35  jorpiell
 * Add the container classes
 *
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class Bbox {
	private String id = null;
	private ArrayList coordinates = null;
	private int dimension = 0;
	private String srs;
	
	public Bbox(int dimension){
		this.dimension = dimension;
		coordinates = new ArrayList();
	}
	
	public void addCoordinate(double[] coordinate){
		coordinates.add(coordinate);
	}
	
	/**
	 * @return the dimension
	 */
	public int getDimension() {
		return dimension;
	}

	public double getMinX(){
		return ((double[])(coordinates.get(0)))[0];
	}
	
	public double getMaxX(){
		return ((double[])(coordinates.get(1)))[0];
	}
	
	public double getMinY(){
		return ((double[])(coordinates.get(0)))[1];
	}
	
	public double getMaxY(){
		return ((double[])(coordinates.get(1)))[1];
	}
	
	public double getMinZ(){
		if (dimension < 3){
			return 0.0;
		}
		return ((double[])(coordinates.get(0)))[2];
	}
	
	public double getMaxZ(){
		if (dimension < 3){
			return 0.0;
		}
		return ((double[])(coordinates.get(1)))[2];
	}
	
	public double getMinCoordinate(int dimension){
		return ((double[])(coordinates.get(0)))[dimension];
	}
	
	public double getMaxCoordinate(int dimension){
		return ((double[])(coordinates.get(1)))[dimension];
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the srs
	 */
	public String getSrs() {
		return srs;
	}
	/**
	 * @param srs the srs to set
	 */
	public void setSrs(String srs) {
		this.srs = srs;
	}
}
