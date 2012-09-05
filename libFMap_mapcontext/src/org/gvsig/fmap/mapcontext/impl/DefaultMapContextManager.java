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
 * 2009 {DiSiD Technologies}  {Create Manager to register MapContextDrawer implementation}
 */
package org.gvsig.fmap.mapcontext.impl;

import org.gvsig.fmap.mapcontext.MapContextDrawer;
import org.gvsig.fmap.mapcontext.MapContextException;
import org.gvsig.fmap.mapcontext.MapContextManager;

/**
 * Default implementation of the {@link MapContextManager}.
 * 
 * @author <a href="mailto:cordinyana@gvsig.org">Cèsar Ordiñana</a>
 */
public class DefaultMapContextManager implements MapContextManager {

    private Class drawerClazz = DefaultMapContextDrawer.class;
    
    public MapContextDrawer createMapContextDrawerInstance(Class drawerClazz)
            throws MapContextException {

        try {
            return (MapContextDrawer) drawerClazz.newInstance();
        } catch (Exception ex) {
            throw new MapContextDrawerInstantiationException(drawerClazz, ex);
        }
    }

    public MapContextDrawer createDefaultMapContextDrawerInstance()
            throws MapContextException {
        
        return createMapContextDrawerInstance(drawerClazz);
    }

    public void setDefaultMapContextDrawer(Class drawerClazz)
            throws MapContextException {
        
        if (!MapContextDrawer.class.isAssignableFrom(drawerClazz)) {
            throw new InvalidMapContextDrawerClassException(drawerClazz);
        }
        this.drawerClazz = drawerClazz;
    }

}