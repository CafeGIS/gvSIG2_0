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

package org.gvsig.fmap.dal.serverexplorer.db;

import java.util.List;

import org.gvsig.fmap.dal.DataServerExplorer;
import org.gvsig.fmap.dal.DataServerExplorerParameters;
import org.gvsig.fmap.dal.DataStore;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.NewDataStoreParameters;
import org.gvsig.fmap.dal.exception.CloseException;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.OpenException;
import org.gvsig.fmap.dal.exception.RemoveException;
import org.gvsig.fmap.dal.feature.FeatureType;

public interface DBServerExplorer extends DataServerExplorer {

	public abstract DataServerExplorerParameters getParameters();

	public abstract void dispose() throws DataException;

	public abstract List list() throws DataException;

	public abstract void remove(DataStoreParameters dsp) throws RemoveException;

	public abstract boolean add(NewDataStoreParameters ndsp, boolean overwrite)
			throws DataException;

	public abstract boolean canAdd();

	public abstract String getName();

	public abstract NewDataStoreParameters getAddParameters(String storeName)
			throws DataException;

	public abstract NewDataStoreParameters getAddParameters()
			throws DataException;

	public abstract boolean canAdd(String storeName) throws DataException;

	public abstract FeatureType getFeatureType(DataStoreParameters dsp)
			throws DataException;

	public abstract DataStore open(DataStoreParameters dsp)
			throws DataException;

	public abstract void open() throws OpenException;

	public abstract void close() throws CloseException;
}
