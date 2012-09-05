package org.gvsig.gpe.containers;

import java.io.IOException;

import org.gvsig.gpe.parser.ICoordinateIterator;
import org.gvsig.gpe.writer.ICoordinateSequence;

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
 * $Id$
 * $Log$
 *
 */
/**
 * @author Jorge Piera Llodrá (jorge.piera@iver.es)
 */
public class CoordinatesSequence implements ICoordinateSequence, ICoordinateIterator{
	private double[][] coordinates;
	private int next = 0;
	
	public CoordinatesSequence(double x, double y, double z){
		coordinates = new double[3][1];
		coordinates[0][0] = x;
		coordinates[1][0] = y;
		coordinates[2][0] = z;		
	}
	
	public CoordinatesSequence(double[] x, double[] y, double[] z){
		coordinates = new double[3][1];
		coordinates[0] = x;
		coordinates[1] = y;
		coordinates[2] = z;		
	}	
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.parser.ICoordinateIterator#getDimension()
	 */
	public int getDimension() {
		return coordinates.length;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.parser.ICoordinateIterator#hasNext()
	 */
	public boolean hasNext() throws IOException {
		return (next < coordinates[0].length);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.parser.ICoordinateIterator#next(double[])
	 */
	public void next(double[] buffer) throws IOException {
		for (int i=0 ; i<buffer.length ; i++){
			buffer[i] = coordinates[i][next];			
		}
		next++;
	}

	public int getSize() {
		return coordinates[0].length;
	}

	public ICoordinateIterator iterator() {
		return this;
	}

}
