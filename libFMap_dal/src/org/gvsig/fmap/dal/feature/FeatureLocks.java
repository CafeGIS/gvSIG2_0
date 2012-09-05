/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Gobernment (CIT)
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
 * 2008 {DiSiD Technologies}  { }
 * 2008 {IVER TI}  { }
 */
package org.gvsig.fmap.dal.feature;

import java.util.Iterator;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.tools.observer.WeakReferencingObservable;

/**
 * Manages locks of Features.
 * 
 * @author <a href="mailto:jjdelcerro@gvsig.org">Joaquin del Cerro</a>
 */

public interface FeatureLocks extends WeakReferencingObservable {

	public void apply();
	
	public Iterator getGroups();
	
	public int getGroupsCount();
	
	public boolean isLocked(FeatureReference reference);

	public boolean isLocked(Feature feature);
    
	public long getLocksCount();

	public Iterator getLocks();
	
    void setDefaultTimeout(long milisecs);
    
    long getDefaultTimeout();
    
	
	
    boolean lock(FeatureReference reference);

    boolean lock(Feature feature);

    boolean lock(FeatureSet features) throws DataException;

    void lockAll() throws DataException;

    boolean unlock(FeatureReference reference);

    boolean unlock(Feature feature);

    boolean unlock(FeatureSet features) throws DataException;

    void unlockAll() throws DataException;

	public long getUnappliedLocksCount();

    Iterator getUnappliedLocks();

}
