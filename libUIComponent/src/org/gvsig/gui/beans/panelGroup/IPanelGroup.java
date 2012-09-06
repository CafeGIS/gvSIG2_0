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

import java.util.Hashtable;

import org.gvsig.tools.exception.BaseException;
import org.gvsig.gui.beans.panelGroup.exceptions.EmptyPanelGroupException;
import org.gvsig.gui.beans.panelGroup.exceptions.EmptyPanelGroupGUIException;
import org.gvsig.gui.beans.panelGroup.exceptions.ListCouldntAddPanelException;
import org.gvsig.gui.beans.panelGroup.loaders.IPanelGroupLoader;
import org.gvsig.gui.beans.panelGroup.panels.IPanel;

/**
 * <p>All kind of panels which support a group of {@link IPanel IPanel} must
 *  implement this interface.</p>
 * 
 * @version 15/10/2007
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es) 
 */
public interface IPanelGroup {
	/**
	 * <p>Adds another {@link IPanel IPanel} to this group.</p>
	 * 
	 * @param panel the <code>IPanel</code> to add
	 * 
	 * @throws EmptyPanelGroupException if there was no panel loaded and tries to add a new one, launches this exception
	 *  if the panel to add hasn't been registered to this component and then, no remains without panels
	 * @throws EmptyPanelGroupGUIException if there was no panel loaded and tries to add a new one, launches this exception
	 *  if the panel to add hasn't been set to this component graphical interface and then, no remains without panels
	 *  in its graphical interface
	 * 
	 * @see #loadPanels(IPanelGroupLoader)
	 * @see #removePanel(IPanel)
	 */
	public abstract void addPanel(IPanel panel) throws BaseException;

	/**
	 * <p>Removes an {@link IPanel IPanel} from this group.</p>
	 * 
	 * @param panel the <code>IPanel</code> to remove
	 * 
	 * @see #addPanel(IPanel)
	 * @see #loadPanels(IPanelGroupLoader)
	 */
	public abstract void removePanel(IPanel panel);

	/**
	 * <p>Tries to load as most as possible panels using an <code>IPanelGroupLoader</code>.</p>
	 *  
	 * @param loader used to load the panels into this group
	 * 
	 * @throws ListCouldntAddPanelException list with all exceptions produced loading and adding the panels
	 * @throws EmptyPanelGroupException launched if there was no exception during the loading and adding processes
	 *  but there is no panel registered finally in this component
	 * @throws EmptyPanelGroupGUIException launched if there was no exception during the loading and adding processes
	 * there is at least one panel registered, but finally none of them is in the graphical interface of this component
	 * 
	 * @see #addPanel(IPanel)
	 */
	public abstract void loadPanels(IPanelGroupLoader loader) throws ListCouldntAddPanelException, EmptyPanelGroupException, EmptyPanelGroupGUIException;

	/**
	 * <p>Gets a reference to the active panel in this group.</p>
	 * 
	 * @return a reference to the active panel, or <code>null</code> if there is no any active
	 */
	public abstract IPanel getActivePanel();

	/**
	 * <p>Gets the properties associated to this group of panels.</p>
	 * 
	 * @return properties associated to this group of panels
	 */
	public abstract Hashtable getProperties();

	/**
	 * <p>Gets the object that has a  a ''semantically'' or ''conceptually'' relation to this panel.</p>
	 * 
	 * @return type of object that has reference this panel, or <code>null</code> if there has reference
	 *  to no object
	 */
	public abstract Object getReference();
	
	/**
	 * <p>Updates the reference of all elements of the group with the new one.</p>
	 * 
	 * @param reference type of object that has reference this panel, or <code>null</code> if there has reference
	 *  to no object
	 */
	public abstract void updateReference(Object reference);

	/**
	 * <p>Changes the visibility of an existent panel of this group. If a panel changes to invisible, then that
	 *  panel won't be accessible by the user interface. But if changes to visible and was invisible
	 *  before, then will appear at the same position it was in the user interface. The position is
	 *  according the order of the insertion at the group. Anyway, the group will have that panel.</p>
	 * 
	 * @param panel reference to the panel with visibility has changed.
	 * @param b the new visibility for that panel.
	 */
	public abstract void setPanelInGUI(IPanel panel, boolean b);

	/**
	 * <p>Returns <code>true</code> if the panel as parameter belongs to a this group and it's loaded in the graphical user interface;
	 *  otherwise returns <code>false</code>.</p>
	 * 
	 * @return <code>true</code> if the panel as parameter belongs to a this group and it's loaded in the graphical user interface;
	 *  otherwise returns <code>false</code>
	 */
	public abstract boolean isPanelInGUI(IPanel panel);

	/**
	 * <p>Returns the number of panels registered in this group.</p>
	 * 
	 * @return number of panels registered in this group
	 */
	public abstract int getPanelCount();

	/**
	 * <p>Returns the number of panels registered in this group that are in GUI.</p>
	 * 
	 * @return number of panels registered in this group that are in GUI
	 */
	public abstract int getPanelInGUICount();

	/**
	 * <p>Determines if the last notification received has been {@linkplain IPanel#accept()}.</code> 
	 * 
	 * @return <code>true</code> if that has been the last notification received, <code>false</code> otherwise
	 */
	public abstract boolean isAccepted();

	/**
	 * <p>Sets the parent of this component. That object must be another {@link IPanelGroup IPanelGroup} component.</p>
	 * 
	 * @param parent parent of this component
	 */
	public abstract void setParentPanelGroup(IPanelGroup parent);

	/**
	 * <p>Notifies this panel of an <i>accept</i> action.</p>
	 */
	public abstract void accept();

	/**
	 * <p>Notifies this panel of an <i>apply</i> action.</p>
	 */
	public abstract void apply();

	/**
	 * <p>Notifies this panel of a <i>cancel</i> action.</p>
	 */
	public abstract void cancel();
}
