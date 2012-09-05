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

package org.gvsig.fmap.dal.impl;

import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.feature.EditableFeatureType;
import org.gvsig.fmap.dal.feature.impl.DefaultEditableFeatureType;
import org.gvsig.fmap.dal.resource.Resource;
import org.gvsig.fmap.dal.resource.spi.ResourceManagerProviderServices;
import org.gvsig.fmap.dal.resource.spi.ResourceProvider;
import org.gvsig.fmap.dal.spi.DataServerExplorerProviderServices;

public class DefaultDataServerExplorerProviderServices implements
		DataServerExplorerProviderServices {

	public EditableFeatureType createNewFeatureType() {
		return new DefaultEditableFeatureType();
	}

	public Resource createResource(String name, Object[] params)
			throws InitializeException {
		ResourceManagerProviderServices manager = (ResourceManagerProviderServices) DALLocator
				.getResourceManager();
		ResourceProvider resource = manager.createResource(name, params);
		return resource;
	}
}
