package org.gvsig.gpe.parser;
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
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public interface IGPEContentHandlerSFP0 {

	/**
	 * This method indicates that the parser has found a curve. 
	 * @param id
	 * curve identifier
	 * @param coords
	 * A coordinates iterator
	 * @param srs
	 * Spatial reference system
	 * @return
	 * Consumer application object that represents a lineString 
	 */
	public Object startCurve(String id, ICoordinateIterator coords, String srs);
	
	/**
	 * This method indicates that the parser has found a curve. 
	 * @param id
	 * curve identifier
	 * @param srs
	 * Spatial reference system
	 * @return
	 * Consumer application object that represents a lineString 
	 */
	public Object startCurve(String id, String srs);

	/**
     * It is thrown when the Curve has been completely parsed.
	 * @param Curve
	 * Consumer application object that represents a Curve 
	 */
	public void endCurve(Object Curve);
	
	/**
	 * It adds a segment to a curve.
	 * @param segment
	 * The segment to add
	 * @param curve
	 * The curve
	 */
	public void addSegmentToCurve(Object segment, Object curve);
	
	/**
	 * This method indicates that the parser has found a multiCurve. 
	 * @param id
	 * MultyCurve identifier
	 * @param srs
	 * Spatial reference system
	 * @return
	 * Consumer application object that represents a multiCurve 
	 */
	public Object startMultiCurve(String id, String srs);
	
	/**
	 * It is thrown when the multiCurve has been completely parsed
	 * @param multiCurve
	 * Consumer application object that represents a multiCurve
	 */
	public void endMultiCurve(Object multiCurve);
	
	/**
	 * It is thrown to add a curve to one multiCurve.
	 * @param curve
     * Consumer application object that represents a curve
	 * @param multiCurve
	 * Consumer application object that represents a multiCurve
	 */
	public void addCurveToMultiCurve(Object curve, Object multiCurve);
		
	
}
