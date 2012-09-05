/*
 * Created on 10-jun-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
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
package org.gvsig.fmap.geom.primitive;

import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;

/*
 * @(#)GeneralPathXIterator.java	1.21 03/01/23
 *
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/**
 * This class represents the iterator for General Paths X.
 * It can be used to retrieve all of the elements in a GeneralPathX.
 * The {@link GeneralPathX#getPathIterator}
 *  method is used to create a
 * GeneralPathXIterator for a particular GeneralPathX.
 * The iterator can be used to iterator the path only once.
 * Subsequent iterations require a new iterator.
 *
 * @see GeneralPathX
 *
 * @version 10 Feb 1997
 * @author	Jim Graham
 */
public class GeneralPathXIterator implements PathIterator {
    int typeIdx = 0;
    int pointIdx   = 0;
    GeneralPathX path;
    AffineTransform affine;

    private static final int curvesize[] = {2, 2, 4, 6, 0};

    /**
     * Constructs an iterator given a GeneralPathX.
     * @see GeneralPathX#getPathIterator
     */
    protected GeneralPathXIterator(GeneralPathX path) {
	this(path, null);
    }

    /**
     * Constructs an iterator given a GeneralPathX and an optional
     * AffineTransform.
     * @see GeneralPathX#getPathIterator
     */
    GeneralPathXIterator(GeneralPathX path, AffineTransform at) {
        this.path = path;
	this.affine = at;
    }

    /**
     * Return the winding rule for determining the interior of the
     * path.
     * @see PathIterator#WIND_EVEN_ODD
     * @see PathIterator#WIND_NON_ZERO
     */
    public int getWindingRule() {
	return path.getWindingRule();
    }

    /**
     * Tests if there are more points to read.
     * @return true if there are more points to read
     */
    public boolean isDone() {
        return (typeIdx >= path.getNumTypes());
    }

    /**
     * Moves the iterator to the next segment of the path forwards
     * along the primary direction of traversal as long as there are
     * more points in that direction.
     */
    public void next() {
	int type = path.getPointTypes()[typeIdx++];
	pointIdx += curvesize[type];
    }

    /**
     * Returns the coordinates and type of the current path segment in
     * the iteration.
     * The return value is the path segment type:
     * SEG_MOVETO, SEG_LINETO, SEG_QUADTO, SEG_CUBICTO, or SEG_CLOSE.
     * A float array of length 6 must be passed in and may be used to
     * store the coordinates of the point(s).
     * Each point is stored as a pair of float x,y coordinates.
     * SEG_MOVETO and SEG_LINETO types will return one point,
     * SEG_QUADTO will return two points,
     * SEG_CUBICTO will return 3 points
     * and SEG_CLOSE will not return any points.
     * @see PathIterator#SEG_MOVETO
     * @see PathIterator#SEG_LINETO
     * @see PathIterator#SEG_QUADTO
     * @see PathIterator#SEG_CUBICTO
     * @see PathIterator#SEG_CLOSE
     */
    public int currentSegment(float[] coords) {
	int type = path.getPointTypes()[typeIdx];
	int numCoords = curvesize[type];
	if (numCoords > 0 && affine != null) {
	    affine.transform(path.getPointCoords(), pointIdx,
			     coords, 0,
			     numCoords / 2);
	} else {
	    for (int i=0; i < numCoords; i++) {
			coords[i] = (float) path.getPointCoords()[pointIdx + i];
		    }

	}
        return type;
    }

    /**
     * Returns the coordinates and type of the current path segment in
     * the iteration.
     * The return value is the path segment type:
     * SEG_MOVETO, SEG_LINETO, SEG_QUADTO, SEG_CUBICTO, or SEG_CLOSE.
     * A double array of length 6 must be passed in and may be used to
     * store the coordinates of the point(s).
     * Each point is stored as a pair of double x,y coordinates.
     * SEG_MOVETO and SEG_LINETO types will return one point,
     * SEG_QUADTO will return two points,
     * SEG_CUBICTO will return 3 points
     * and SEG_CLOSE will not return any points.
     * @see PathIterator#SEG_MOVETO
     * @see PathIterator#SEG_LINETO
     * @see PathIterator#SEG_QUADTO
     * @see PathIterator#SEG_CUBICTO
     * @see PathIterator#SEG_CLOSE
     */
    public int currentSegment(double[] coords) {
	int type = path.getPointTypes()[typeIdx];
	int numCoords = curvesize[type];
	if (numCoords > 0 && affine != null) {
	    affine.transform(path.getPointCoords(), pointIdx,
			     coords, 0,
			     numCoords / 2);
	} else {
	    System.arraycopy(path.getPointCoords(), pointIdx, coords, 0, numCoords);
	}
        return type;
    }
}
