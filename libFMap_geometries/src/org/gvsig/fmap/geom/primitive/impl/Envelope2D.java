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

package org.gvsig.fmap.geom.primitive.impl;

import java.awt.geom.Rectangle2D;

import org.cresques.cts.ICoordTrans;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.primitive.Point;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class Envelope2D extends DefaultEnvelope{

	public Envelope2D() {
		super();
		min = new Point2D(0, 0);
		max = new Point2D(0, 0);
	}

	public Envelope2D(Point min, Point max) {
		super(min, max);
	}

	public Envelope2D(double minX,double minY,double maxX, double maxY){
		this(new Point2D(minX, minY), new Point2D(maxX, maxY));
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Envelope#getDimension()
	 */
	public int getDimension() {
		return 2;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.geom.primitive.Envelope#convert(org.cresques.cts.ICoordTrans)
	 */
	public Envelope convert(ICoordTrans trans) {
		if (this.getDimension() > 2) {
			return null;
		}
		Rectangle2D rect = new Rectangle2D.Double(this.getMinimum(0), this
				.getMinimum(1), this.getLength(0), this.getLength(1));
		Rectangle2D rectDest = trans.convert(rect);
		if (rectDest == null){
			return null;
		}
		Point2D p1 = new Point2D(rectDest.getMinX(), rectDest.getMinY());
		Point2D p2 = new Point2D(rectDest.getMaxX(), rectDest.getMaxY());

		return new Envelope2D(p1, p2);
	}

}

