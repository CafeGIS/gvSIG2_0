/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Government (CIT)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 * 
 */

/*
 * AUTHORS (In addition to CIT):
 * 2009 {Iver T.I.}   {Task}
 */

package org.gvsig.fmap.geom.primitive;

import org.cresques.cts.ICoordTrans;
import org.gvsig.fmap.geom.Geometry;

/**
 * <p>
 * This interface is equivalent to the GM_Envelope specified in 
 * <a href="http://www.iso.org/iso/iso_catalogue/catalogue_tc/catalogue_detail.htm?csnumber=26012">ISO 19107</a>.
 * A minimum bounding box or rectangle. Regardless of dimension, an Envelope
 * can be represented without ambiguity as two direct positions (coordinate
 * points). To encode an Envelope, it is sufficient to encode these two
 * points. This is consistent with all of the data types in this
 * specification, their state is represented by their publicly accessible
 * attributes.
 * </p>
 * @see <a href="http://www.iso.org/iso/iso_catalogue/catalogue_tc/catalogue_detail.htm?csnumber=26012">ISO 19107</a>
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public interface Envelope {
    /**
     * Returns the center ordinate along the specified dimension.
     * @param dimension.
     * The dimension
     * @return 
     * The value of the ordinate.
     */
    double getCenter(int dimension);

    /**
     * The length of coordinate sequence (the number of entries) in this
     * envelope.
     * @return 
     * The dimension of the envelope.
     */
    int getDimension();

    /**
     * Returns the envelope length along the specified dimension.
     * @param dimension
     * The dimension.
     * @return
     * The envelope length along a dimension.
     */
    double getLength(int dimension);

    /**
     * A coordinate position consisting of all the minimal ordinates for each
     * dimension for all points within the Envelope.
     * @return
     * The lower corner.
     */
    Point getLowerCorner();
    
    /**
     * Sets the coordinate position consisting of all the minimal ordinates for each
     * dimension for all points within the Envelope.
     * @param point
     * The lower corner.
     */
    void setLowerCorner(Point point);

    /**
     * Returns the maximal ordinate along the specified dimension.
     * @param dimension
     * The dimension.
     * @return
     * The maximum value
     */
    double getMaximum(int dimension);

    /**
     * Returns the minimal ordinate along the specified dimension.
     * @param dimension     
     * The dimension.
     * @return
     * The minimum value.
     */
    double getMinimum(int dimension);

    /**
     * A coordinate position consisting of all the maximal ordinates for each
     * dimension for all points within the Envelope.
     * @return
     * The upper corner
     */
    Point getUpperCorner();
    
    /**
     * Sets the coordinate position consisting of all the maximal ordinates for each
     * dimension for all points within the Envelope.
     * @param point
     * The upper corner.
     */
    void setUpperCorner(Point upperCorner);

    /**
     * Adds a envelope to the current envelope.
     * @param envelope
     * The envelope to add.
     */
	void add(Envelope envelope);

	/**
	 * This method exists by historical reasons. It return the same
	 * instance of the Envelope.
	 * @deprecated
	 * @return
	 * The envelope.
	 */
	Geometry getGeometry();

	/**
	 * Returns <code>true</code> if the new envelope is contained in the 
	 * current envelope.
	 * @param envelope
	 * The envelope to compare.
	 * @return
	 * If the current envelope contains the new envelope
	 */
	boolean contains(Envelope envelope);

	/**
	 * Returns <code>true</code> if the new envelope intersects with the 
	 * current envelope.
	 * @param envelope
	 * The envelope to compare.
	 * @return
	 * If the current envelope intersects with the new envelope
	 */
	boolean intersects(Envelope envelope);

	/**
	 * Converts the envelope to other coordinate reference system
	 * @param trans
	 * The CRS conversor
	 * @return
	 * A new envelope in other CRS 
	 */
	Envelope convert(ICoordTrans trans);
}
