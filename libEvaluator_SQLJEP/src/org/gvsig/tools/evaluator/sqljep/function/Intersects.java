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

package org.gvsig.tools.evaluator.sqljep.function;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.operation.GeometryOperationNotSupportedException;
import org.gvsig.fmap.geom.operation.relationship.DefaultRelationshipGeometryOperationContext;
import org.medfoster.sqljep.ASTFunNode;
import org.medfoster.sqljep.JepRuntime;
import org.medfoster.sqljep.ParseException;
import org.medfoster.sqljep.function.PostfixCommand;

public class Intersects extends PostfixCommand {

	public static final String NAME = "intersects";

	final public int getNumberOfParameters() {
		return 2;
	}

	/* (non-Javadoc)
	 * @see org.medfoster.sqljep.function.PostfixCommand#evaluate(org.medfoster.sqljep.ASTFunNode, org.medfoster.sqljep.JepRuntime)
	 */
	public void evaluate(ASTFunNode node, JepRuntime runtime)
			throws ParseException {
		node.childrenAccept(runtime.ev, null);
		Geometry geom1 = (Geometry)runtime.stack.pop();
		Geometry geom2 = (Geometry)runtime.stack.pop();
		runtime.stack.push(intersects(geom1, geom2));
	}

	private static Boolean intersects(Geometry geom1, Geometry geom2)
			throws ParseException {
		try {
//			if (!geom1.getBounds2D().intersects(geom2.getBounds2D())){
//				return new Boolean(false);
//			}
			return (Boolean) geom1.invokeOperation(
					org.gvsig.fmap.geom.operation.relationship.Intersects.CODE,
					new DefaultRelationshipGeometryOperationContext(geom2));
		} catch (GeometryOperationNotSupportedException e) {
			throw new ParseException(NAME, e);
		} catch (GeometryOperationException e) {
			throw new ParseException(NAME, e);
		}
	}

}
