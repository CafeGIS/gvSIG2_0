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
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.operation.GeometryOperationNotSupportedException;
import org.gvsig.fmap.geom.operation.fromwkt.FromWKT;
import org.gvsig.fmap.geom.operation.fromwkt.FromWKTGeometryOperationContext;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.medfoster.sqljep.ASTFunNode;
import org.medfoster.sqljep.JepRuntime;
import org.medfoster.sqljep.ParseException;
import org.medfoster.sqljep.function.PostfixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeomFromText extends PostfixCommand {
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private static final Logger logger = LoggerFactory.getLogger(GeomFromText.class);
	
	private static Geometry nullGeometry = null;

	public static final String NAME = "GeomFromText";
	private static String lastTextGeometry="";
	private static String lastSRS="";
	private static Geometry lastGeometry=null;

	final public int getNumberOfParameters() {
		return 2;
	}

	public GeomFromText() {
		super();
		if (nullGeometry == null) {
			try {
				nullGeometry = geomManager.createNullGeometry(SUBTYPES.GEOM2D);
			} catch (CreateGeometryException e) {
				logger.error("Error creating a null geometry", e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.medfoster.sqljep.function.PostfixCommand#evaluate(org.medfoster.sqljep.ASTFunNode,
	 *      org.medfoster.sqljep.JepRuntime)
	 */
	public void evaluate(ASTFunNode node, JepRuntime runtime)
			throws ParseException {
		node.childrenAccept(runtime.ev, null);
		String srs = (String) runtime.stack.pop();
		String text = (String) runtime.stack.pop();
		runtime.stack.push(geometryFromText(text, srs));
	}

	private static Geometry geometryFromText(String text, String srs)
			throws ParseException {
		if (!lastTextGeometry.equals(text) || !lastSRS.equals(srs)) {
			try {
				lastGeometry = (Geometry) nullGeometry.invokeOperation(
						FromWKT.CODE,
						new FromWKTGeometryOperationContext(text,srs)
					);
			} catch (GeometryOperationNotSupportedException e) {
				throw new ParseException(NAME, e);
			} catch (GeometryOperationException e) {
				throw new ParseException(NAME, e);
			}
			lastTextGeometry = text;
			lastSRS = srs;
		}
		return lastGeometry;
	}

}
