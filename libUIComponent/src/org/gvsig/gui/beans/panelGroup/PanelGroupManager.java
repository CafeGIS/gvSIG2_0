/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2007 IVER T.I. and Generalitat Valenciana.
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 */

package org.gvsig.gui.beans.panelGroup;

import java.io.Serializable;
import java.util.ArrayList;

import org.gvsig.gui.beans.panelGroup.panels.AbstractPanel;

/**
 * <p>Implemented to use the {@link AbstractPanelGroup AbstractPanelGroup} type desired.</p>
 * <p>This class allows having different implementations of {@link AbstractPanelGroup AbstractPanelGroup}
 * and only load in memory the class of some of them, whom have previously been imported to future use. Then, when
 * a graphical interface would need use a <code>AbstractPanelGroup</code> instance, will instance an object of the 
 * class <i>selected</i> (set previously as <i>default</i>) of the registered types in this manager.</p> 
 * 
 * @version 18/10/2007
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es) 
 */
public class PanelGroupManager implements Serializable {
	private static final long serialVersionUID = 2059085515913026734L;

	/**
	 * Array list with the {@link AbstractPanelGroup AbstractPanelGroup} registered.
	 * 
	 * @see #registerPanelGroup(Class)
	 * @see #deregisterPanelGroup(Class)
	 */
	private ArrayList<Class> registeredPanelGroups = null;
	
	/**
	 * The {@link AbstractPanelGroup AbstractPanelGroup} type set as default.
	 * 
	 * @see #getPanelGroup()
	 * @see #setDefaultType(Class)
	 */
	private Class defaultPanelGroup = null;

	/**
	 * <p>Reference to the current manager with the <i>PanelGroup</i> registered.</p>
	 */
	private static PanelGroupManager manager = null;

	/**
	 * <p>Default constructor.</p>
	 */
	protected PanelGroupManager() {
		 registeredPanelGroups = new ArrayList<Class>();
	}
	
	/**
	 * <p>The singleton <code>PanelGroupManager</code> instance.</p>
	 * 
	 * @return current manager used, or if didn't exist any, a new one
	 */
	public static synchronized PanelGroupManager getManager()
	{
		if( manager == null ) {
			manager = new PanelGroupManager ();
		}

		return manager;
	}
	
	/**
	 * <p>Registers a new {@link AbstractPanelGroup AbstractPanelGroup} class type if it
	 * wasn't already done.</p>
	 * 
	 * @param panelGroup the new <code>AbstractPanelGroup</code> class type to register
	 * 
	 * @see #deregisterPanelGroup(Class)
	 */
	public synchronized void registerPanelGroup(Class panelGroup) {
		// Only adds if isn't already registered
		if (!registeredPanelGroups.contains(panelGroup))
			registeredPanelGroups.add(panelGroup);
	}

	/**
	 * <p>Unregisters a {@link AbstractPanelGroup AbstractPanelGroup} class type if it
	 * was previously registered.</p>
	 * <p>If the type to unregister is the <i>default</i>, sets <i>default</i> to <code>null</code>.</p>
	 * 
	 * @param panelGroup the <code>AbstractPanelGroup</code> class type to unregister
	 * 
	 * @see #registerPanelGroup(Class)
	 */
	public synchronized void deregisterPanelGroup(Class panelGroup) {
		// Only removes if already registered
		if (registeredPanelGroups.contains(panelGroup)) {
			registeredPanelGroups.remove(panelGroup);

			if (defaultPanelGroup == null)
				return;

			if (defaultPanelGroup.equals(panelGroup)) {
				defaultPanelGroup = null;
			}
		}
	}
	
	/**
	 * <p>Sets as <i>default</i> one of the {@link AbstractPanelGroup AbstractPanelGroup} class type
	 *  previously registered.</p>
	 * 
	 * @param panelGroup a <code>AbstractPanelGroup</code> class type
	 * 
	 * @see #getPanelGroup()
	 */
	public synchronized void setDefaultType(Class panelGroup) {
		if (registeredPanelGroups.contains(panelGroup))
			defaultPanelGroup = panelGroup;
	}
	
	/**
	 * <p>Gets the <i>default</i> {@link AbstractPanelGroup AbstractPanelGroup} class type.</p>
	 * 
	 * return the <i>default</i> <code>AbstractPanelGroup</code> class type or <code>null</code> if isn't any
	 * 
	 * @throws Exception any exception produced loading the {@link AbstractPanel AbstractPanel}
	 * 
	 * @see #setDefaultType(Class)
	 */	
	public synchronized AbstractPanelGroup getPanelGroup(Object reference) throws Exception {
		try {
			if( defaultPanelGroup != null ) {
				return (AbstractPanelGroup) defaultPanelGroup.getConstructor(Object.class).newInstance(reference);
			}
		}
		catch (Exception e) {
			throw e;
		}

		return null;
	}
}
