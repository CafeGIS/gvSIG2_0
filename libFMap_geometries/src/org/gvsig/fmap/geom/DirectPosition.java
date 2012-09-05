package org.gvsig.fmap.geom;
/* gvSIG. Sistema de Informaci�nn Geográfica de la Generalitat Valenciana
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

/*
 * AUTHORS (In addition to CIT):
 * 2009 {Iver T.I.}   {Task}
 */

/**
 * <p>
 * DirectPosition object data types hold the coordinates for 
 * a position within some coordinate reference system. 
 * The coordinate reference system is described in 
 * <a href="http://www.iso.org/iso/iso_catalogue/catalogue_tc/catalogue_detail.htm?csnumber=41126">ISO 19111</a>.
 * </p>
 * @see <a href="http://www.iso.org/iso/iso_catalogue/catalogue_tc/catalogue_detail.htm?csnumber=26012">ISO 19107</a>
 * @see <a http://www.iso.org/iso/iso_catalogue/catalogue_tc/catalogue_detail.htm?csnumber=41126">ISO 19111</a>
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public interface DirectPosition {
	
	/**
	 * Returns the length of coordinate. This is determined by 
	 * the reference system.
	 * @return
	 * The dimension
	 */
	public int getDimension();
	
	/**
	 * Returns the ordinate of the given dimension.
	 * @param dimension
	 * The dimension to retrieve
	 * @return
	 * The value of the ordinate
	 */
	public double getOrdinate(int dimension);

}