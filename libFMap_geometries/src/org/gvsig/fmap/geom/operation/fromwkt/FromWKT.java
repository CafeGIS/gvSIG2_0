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
* 2008 IVER T.I. S.A.   {{Task}}
*/
 
/**
 * 
 */
package org.gvsig.fmap.geom.operation.fromwkt;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.operation.GeometryOperation;
import org.gvsig.fmap.geom.operation.GeometryOperationContext;
import org.gvsig.fmap.geom.operation.GeometryOperationException;

import com.vividsolutions.jts.io.ParseException;

/**
 * @author jmvivo
 *
 */
public class FromWKT extends GeometryOperation {
	public static final int CODE = GeometryLocator.getGeometryManager()
			.registerGeometryOperation("fromWKT", new FromWKT());

	private static WKTParser parser = new WKTParser();
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.gvsig.fmap.geom.operation.GeometryOperation#getOperationIndex()
	 */
	public int getOperationIndex() {
        return CODE;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.geom.operation.GeometryOperation#invoke(org.gvsig.fmap.geom.Geometry, org.gvsig.fmap.geom.operation.GeometryOperationContext)
	 */
	public Object invoke(Geometry geom, GeometryOperationContext ctx)
			throws GeometryOperationException {
		FromWKTGeometryOperationContext myCtx = (FromWKTGeometryOperationContext) ctx;
		
		try {
			return parser.read(myCtx.getText());
		} catch (ParseException e) {
			throw new GeometryOperationException(geom.getType(),CODE,e);
		}
	}

}
