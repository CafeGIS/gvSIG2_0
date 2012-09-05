
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
* ${year} IVER T.I. S.A.   {{Task}}
*/

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
* ${year} IVER T.I. S.A.   {{Task}}
*/
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
* ${year} IVER T.I. S.A.   {{Task}}
*/
package org.gvsig.fmap.geom.primitive.impl;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.Geometry.TYPES;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.primitive.Surface;


/**
 * A minimum bounding box or rectangle. Regardless of dimension, an Envelope
 * can be represented without ambiguity as two direct positions (coordinate
 * points). To encode an Envelope, it is sufficient to encode these two
 * points. This is consistent with all of the data types in this
 * specification, their state is represented by their publicly accessible
 * attributes.

 * @author Vicente Caballero Navarro
 */
public abstract class DefaultEnvelope implements Envelope{
	protected Point min;
    protected Point max;

    protected static GeometryManager manager = GeometryLocator.getGeometryManager();

    public DefaultEnvelope(){
    	super();
    }

    public DefaultEnvelope(Point min, Point max){
    	super();
    	this.min = min;
    	this.max = max;
    }

    /**
     * Returns the center ordinate along the specified dimension.
     *
     * @param dimension DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getCenter(int dimension) {
        return (min.getCoordinateAt(dimension) + max.getCoordinateAt(dimension)) * 0.5;
    }

    /**
     * Returns the envelope length along the specified dimension.
     *
     * @param dimension
     *
     * @return
     */
    public double getLength(int dimension) {
        if (max.getCoordinateAt(dimension) > min.getCoordinateAt(dimension)) {
            return max.getCoordinateAt(dimension) - min.getCoordinateAt(dimension);
        }

        return min.getCoordinateAt(dimension) - max.getCoordinateAt(dimension);
    }

    /**
     * A coordinate position consisting of all the minimal ordinates for each
     * dimension for all points within the Envelope.
     *
     * @return
     */
    public Point getLowerCorner() {
        return min;
    }

    /**
     * Returns the maximal ordinate along the specified dimension.
     *
     * @param dimension
     *
     * @return
     */
    public double getMaximum(int dimension) {
        return max.getCoordinateAt(dimension);
    }

    /**
     * Returns the minimal ordinate along the specified dimension.
     *
     * @param dimension
     *
     * @return
     */
    public double getMinimum(int dimension) {
        return min.getCoordinateAt(dimension);
    }

    /**
     * A coordinate position consisting of all the maximal ordinates for each
     * dimension for all points within the Envelope.
     *
     * @return
     */
    public Point getUpperCorner() {
        return max;
    }

	public void add(Envelope envelope) {
		int maxDimension = Math.min(getDimension(), envelope.getDimension());
		int i;
		for (i=0;i<maxDimension;i++){
			this.min.setCoordinateAt(i,
					Math.min(this.min.getCoordinateAt(i), envelope.getMinimum(i)));
			this.max.setCoordinateAt(i,
					Math.max(this.max.getCoordinateAt(i), envelope.getMaximum(i)));
		}
	}

	public Geometry getGeometry() {
		GeneralPathX gpx=new GeneralPathX();
		gpx.moveTo(getMinimum(0),getMinimum(1));
		gpx.lineTo(getMaximum(0),getMinimum(1));
		gpx.lineTo(getMaximum(0),getMaximum(1));
		gpx.lineTo(getMinimum(0),getMaximum(1));
		gpx.closePath();
		Surface surface;
		try {
			surface = (Surface)manager.create(TYPES.SURFACE, SUBTYPES.GEOM2D);
			surface.setGeneralPath(gpx);
			return surface;
		} catch (CreateGeometryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public boolean contains(Envelope envelope) {
		if(envelope == null) {
			return false;
		}
		for (int i = 0; i < getDimension(); i++) {
			if (getMinimum(i) > envelope.getMinimum(i)
					|| getMaximum(i) < envelope.getMaximum(i)) {
					return false;
			}
		}
		return true;
	}

	public boolean intersects(Envelope envelope) {
		if(envelope == null) {
			return false;
		}
		for (int i = 0; i < getDimension(); i++) {
			if (getMinimum(i)>envelope.getMaximum(i)){
				return false;
			} else if (getMaximum(i)<envelope.getMinimum(i)){
				return false;
			}
		}
		return true;
	}



	public boolean equals(Object other) {
		if (!(other instanceof Envelope) || other == null) {
			return false;
		}
		Envelope otherEnv = (Envelope) other;
		if (otherEnv.getDimension() != this.getDimension()) {
			return false;
		}
		for (int i = 0; i < this.getDimension(); i++) {
			if (otherEnv.getMinimum(i) != this.getMinimum(i)) {
				return false;
			}
			if (otherEnv.getMaximum(i) != this.getMaximum(i)) {
				return false;
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Envelope#setLowerCorner(org.gvsig.fmap.geom.primitive.Point)
	 */
	public void setLowerCorner(Point lowerCorner) {
		this.min = lowerCorner;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Envelope#setUpperCorner(org.gvsig.fmap.geom.primitive.Point)
	 */
	public void setUpperCorner(Point upperCorner) {
		this.max = upperCorner;
	}
}
