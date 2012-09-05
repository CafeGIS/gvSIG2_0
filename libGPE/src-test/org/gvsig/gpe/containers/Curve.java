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

/**
 * This class represetnts a Curve
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class Curve extends Geometry {
	private ArrayList coordinates = null;
	private ArrayList segments = null;
	
	public Curve(){
		segments = new ArrayList();
		coordinates = new ArrayList();
	}
		
	/**
	 * Adds a segment 
	 * @param segment
	 * The segment to add
	 */
	public void addSegment(LineString segment){
		segments.add(segment);
	}
	
	/**
	 * Gets a segment
	 * @param i
	 * The segment position
	 * @return
	 * A Segment
	 */
	public LineString getSegmentAt(int i){
		return (LineString)segments.get(i);
	}
	
	/**
	 * @return the number of seg
	 */
	public int getSegmentsSize(){
		return segments.size();
	}
	
	public void addCoordinate(double[] coordinate){
		coordinates.add(coordinate);
	}
	
	public double geCoordinateAt(int index, int dimension){
		return ((double[])(coordinates.get(index)))[dimension];
	}
	
	public int getCoordinatesNumber(){
		return coordinates.size();
	}	
}


