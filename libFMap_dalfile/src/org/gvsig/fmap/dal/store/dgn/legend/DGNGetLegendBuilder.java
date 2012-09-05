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

package org.gvsig.fmap.dal.store.dgn.legend;

import org.gvsig.fmap.dal.store.dgn.LegendBuilder;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynMethod;
import org.gvsig.tools.dynobject.DynObject;
import org.gvsig.tools.dynobject.exception.DynMethodException;
import org.gvsig.tools.dynobject.exception.DynMethodNotSupportedException;

public class DGNGetLegendBuilder implements DynMethod {
	private static Integer code = null;

	static void register(DynClass dgnLegendDynClass) {
		if (code != null) {
			return;
		}
		code = new Integer(ToolsLocator.getDynObjectManager()
				.registerDynMethod(
						dgnLegendDynClass, new DGNGetLegendBuilder()));

	}
	public int getCode() throws DynMethodNotSupportedException {
		return code.intValue();
	}

	public String getDescription() {
		return "DGN LegendBuilder creator";
	}

	public String getName() {
		return LegendBuilder.DYNMETHOD_BUILDER_NAME;
	}

	public Object invoke(Object self, DynObject context)
			throws DynMethodException {
		return new DGNLegendBuilder();
	}

}
