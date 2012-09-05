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
* 2008 IVER T.I   {{Task}}
*/

/**
 *
 */
package org.gvsig.tools.evaluator.sqljep.function;

import org.gvsig.fmap.geom.Geometry;
import org.medfoster.sqljep.ASTFunNode;
import org.medfoster.sqljep.JepRuntime;
import org.medfoster.sqljep.ParseException;
import org.medfoster.sqljep.function.PostfixCommand;

/**
 * @author jmvivo
 *
 */
public class Equals extends PostfixCommand {

	public static final String NAME = "equals";
	
	final public int getNumberOfParameters() {
		return 2;
	}
	/* (non-Javadoc)
	 * @see org.medfoster.sqljep.function.PostfixCommand#evaluate(org.medfoster.sqljep.ASTFunNode, org.medfoster.sqljep.JepRuntime)
	 */
	public void evaluate(ASTFunNode node, JepRuntime runtime)
			throws ParseException {
		node.childrenAccept(runtime.ev, null);
		Comparable param2 = (Comparable)runtime.stack.pop();
		Comparable param1 = (Comparable)runtime.stack.pop();
		if (param1 == null || param2 == null) {
			runtime.stack.push(Boolean.FALSE);
		} else if ((param1 instanceof Geometry)
				&& (param2 instanceof Geometry)) {
			runtime.stack.push(new Boolean(isEquals((Geometry)param1, (Geometry)param2)));
		} else{
			runtime.stack.push(new Boolean(param1.equals(param2)));
		}
	}

	private static boolean isEquals(Geometry g1, Geometry g2) throws ParseException {
		return (g1.equals(g2));
	}

}

