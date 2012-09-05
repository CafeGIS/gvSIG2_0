package org.gvsig.gpe.warnings;

import java.util.Hashtable;
import java.util.Map;

import org.gvsig.tools.exception.BaseException;

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
 * $Id: PolygonAutomaticallyClosedWarning.java 124 2007-05-16 09:27:40Z jorpiell $
 * $Log$
 * Revision 1.1  2007/05/16 09:27:40  jorpiell
 * Add some warnings
 *
 *
 */
/**
 * This warning is throwed when in a writting process,
 * there is a polygon that is not closed. If the format
 * doesn't support this, the parser must be close the
 * polygon automatically.
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class PolygonAutomaticallyClosedWarning extends BaseException {
	private static final long serialVersionUID = -4652019336977819445L;
	private double[] x  = null;
	private double[] y  = null;
	private double[] z  = null;
	
	/**
	 * Constructor
	 * @param coordinates
	 * Polygon coordinates
	 */
	public PolygonAutomaticallyClosedWarning(double[][] coordinates){
		this.x = coordinates[0];
		this.y = coordinates[1];
		this.z = coordinates[2];
		initialize();
	}
	
	/**
	 * Costructor
	 * @param x
	 * Coordinate X
	 * @param y
	 * Coordinate Y
	 * @param z
	 * Coordinate Z
	 */
	public PolygonAutomaticallyClosedWarning(double[] x, double[] y, double[] z){
		this.x = x;
		this.y = y ;
		this.z = z;
		initialize();
	}

	/**
	 * Initialize the properties
	 */
	private void initialize() {
		messageKey = "gpe_polygon_automatically_closed_warning";
		formatString = "The polygon is not closed. The first coordinate is" +
				" %(X0),%(Y0),%(Z0) and the last one is %(X1),%(Y1),%(Z1). The" +
				" writting process will close it automatically";				
		code = serialVersionUID;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.tools.exception.BaseException#values()
	 */
	protected Map values() {
		Hashtable hash = new Hashtable();
		hash.put("X0", String.valueOf(x[0]));
		hash.put("Y0", String.valueOf(y[0]));
		hash.put("Z0", String.valueOf(z[0]));
		hash.put("X1", String.valueOf(x[x.length - 1]));
		hash.put("Y1", String.valueOf(y[y.length - 1]));
		hash.put("Z1", String.valueOf(z[z.length - 1]));
		return hash;
	}
}
