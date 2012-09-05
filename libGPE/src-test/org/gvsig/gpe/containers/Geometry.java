package org.gvsig.gpe.containers;
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
 * $Id: Geometry.java 180 2007-11-21 11:19:46Z csanchez $
 * $Log$
 * Revision 1.3  2007/05/09 10:25:45  jorpiell
 * Add the multiGeometries
 *
 * Revision 1.2  2007/04/19 07:23:20  jorpiell
 * Add the add methods to teh contenhandler and change the register mode
 *
 * Revision 1.1  2007/04/14 16:06:35  jorpiell
 * Add the container classes
 *
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class Geometry {
	private String id = null;
	private Bbox bbox = null;
	private String srs = null;
	private int dimension = 0;


	/**
	 * Constructor
	 */
	public Geometry(String id, String srs, int dimension) {
		super();
		this.id = id;
		this.srs = srs;
		this.dimension = dimension;
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

	/**
	 * @return the bbox
	 */
	public Bbox getBbox() {
		return bbox;
	}

	/**
	 * @param bbox the bbox to set
	 */
	public void setBbox(Bbox bbox) {
		this.bbox = bbox;
	}
	
	/**
	 * @param bbox the bbox to set
	 */
	public void setBbox(Object bbox) {
		if (bbox instanceof Bbox){
			this.bbox = (Bbox)bbox;
		}
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

	public Geometry() {
		super();
	}

	/**
	 * @return the dimension
	 */
	public int getDimension() {
		return dimension;
	}

	/**
	 * @param dimension the dimension to set
	 */
	public void setDimension(int dimension) {
		this.dimension = dimension;
	}
	
}
