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
 
package org.gvsig.fmap.geom.operation.println;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.Geometry.TYPES;
import org.gvsig.fmap.geom.aggregate.MultiPoint;
import org.gvsig.fmap.geom.operation.GeometryOperation;
import org.gvsig.fmap.geom.operation.GeometryOperationContext;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.operation.GeometryOperationNotSupportedException;
import org.gvsig.fmap.geom.type.GeometryTypeNotSupportedException;
import org.gvsig.fmap.geom.type.GeometryTypeNotValidException;
import org.gvsig.tools.locator.LocatorException;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class PrintMultiPoint extends GeometryOperation {

	public PrintMultiPoint() {
		super();
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.operation.GeometryOperation#getOperationIndex()
	 */
	public int getOperationIndex() {
		try {
			return GeometryLocator.getGeometryManager()
			.registerGeometryOperation("println", new PrintPoint(), TYPES.MULTIPOINT, SUBTYPES.GEOM2D);
		} catch (LocatorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GeometryTypeNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GeometryTypeNotValidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.operation.GeometryOperation#invoke(org.gvsig.fmap.geom.Geometry, org.gvsig.fmap.geom.operation.GeometryOperationContext)
	 */
	public Object invoke(Geometry geom, GeometryOperationContext ctx)
			throws GeometryOperationException {
		MultiPoint multiPoint = (MultiPoint)geom;
		System.out.println("MultiPoint");
		for (int i=0 ; i<multiPoint.getPrimitivesNumber() ; i++){
			try {
				System.out.print(" - ");
				multiPoint.getPointAt(i).invokeOperation(getOperationIndex(), ctx);
			} catch (GeometryOperationNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "println-multipoint";
	}	
	
	
}



