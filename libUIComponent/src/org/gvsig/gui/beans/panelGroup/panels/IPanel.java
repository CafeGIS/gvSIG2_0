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

package org.gvsig.gui.beans.panelGroup.panels;

import org.gvsig.gui.beans.panelGroup.AbstractPanelGroup;
import org.gvsig.gui.beans.panelGroup.IPanelGroup;

/**
 * <p>Common interface for all kinds of {@link AbstractPanel AbstractPanel}.</p>
 * <p>An <i>IPanel</i> has:
 *  <ul>
 *   <li>Three identifiers:</li>
 *    <ul>
 *     <li><b>ID</b>: (optional) identifies neutrally the panel.</li>
 *     <li><b>Label</b>: identifier of the panel, used by a {@link AbstractPanelGroup AbstractPanelGroup}.</li>
 *     <li><b>LabelGroup</b>: identifier of the panel used to group together different <code>IPanel</code> panels
 *      of a {@link AbstractPanelGroup AbstractPanelGroup}.</li>
 *    </ul>
 *   <li>An optional reference to an object that is related ''semantically'' or ''conceptually'' to this panel (Ex. this panel is about some properties of a raster layer).</li>
 *  </ul>
 * </p>
 * 
 * @version 15/10/2007
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es) 
 */
public interface IPanel {
	/**
	 * <p>Gets the panel identifier.</p>.
	 * 
	 * @return panel identifier, or <code>null</code> if undefined
	 * 
	 * @see #setID(String)
	 */
	public abstract String getID();

	/**
	 * <p>Gets the identifier of the panel used by a {@link AbstractPanelGroup AbstractPanelGroup}.</p>
	 * 
	 * @return panel label identifier, or <code>null</code> if undefined
	 * 
	 * @see #setLabel(String)
	 */
	public abstract String getLabel();

	/**
	 * <p>Gets the identifier of the panel used to group together different <code>IPanel</code> panels
     *      in a {@link AbstractPanelGroup AbstractPanelGroup}.</p>
	 * 
	 * @return panel label group identifier, or <code>null</code> if undefined
	 * 
	 * @see #setLabelGroup(String)
	 */
	public abstract String getLabelGroup();

	/**
	 * <p>Sets the identifier of the panel used by a {@link AbstractPanelGroup AbstractPanelGroup}.</p>
	 * 
	 * @param id panel label identifier, or <code>null</code> if undefined
	 * 
	 * @see #getID()
	 */
	public abstract void setID(String id);

	/**
	 * <p>Sets the identifier of the panel used by a {@link AbstractPanelGroup AbstractPanelGroup}.</p>
	 * 
	 * @param label panel label identifier, or <code>null</code> if undefined
	 * 
	 * @see #getLabel()
	 */
	public abstract void setLabel(String label);

	/**
	 * <p>Sets the identifier of the panel used to group together different <code>IPanel</code> panels
     *      of a {@link AbstractPanelGroup AbstractPanelGroup}.</p>
	 * 
	 * @param labelGroup label group identifier, or <code>null</code> if undefined
	 * 
	 * @see #getLabelGroup()
	 */
	public abstract void setLabelGroup(String labelGroup);

	/**
	 * <p>Gets the object that has a ''semantically'' or ''conceptually'' relation to this panel, this is used to group
	 *  <code>IPanel</code> panels on a {@link IPanelGroup IPanelGroup} object.</p>
	 * 
	 * @return object that has reference this panel, or <code>null</code> if there has reference
	 *  to no object
	 * 
	 * @see #setReference(Object)
	 */
	public abstract Object getReference();

	/**
	 * <p>Sets a reference to an object that is ''semantically' or 'contextually' related to this panel.</p>
	 * 
	 * @param ref an object
	 * 
	 * @see #getReference()
	 */
	public abstract void setReference(Object ref);

	/**
	 * <p>Gets a reference to the object that contains the group which this panel is a member.</p>
	 * 
	 * @return an object that contains this panel's group
	 * 
	 * @see #setPanelGroup(AbstractPanelGroup)
	 */
	public abstract AbstractPanelGroup getPanelGroup();

	/**
	 * <p>Sets a reference to the object that contains the group which this panel is a member.</p>
	 * 
	 * @param panelGroup object that contains this panel's group
	 * 
	 * @see #getPanelGroup()
	 */
	public abstract void setPanelGroup(AbstractPanelGroup panelGroup);

	/**
	 * <p>If this panel belongs to a 'panel group', changes its visibility at the interface of the panel group.
	 *  If changes to invisible, then this panel won't be accessible by the user interface. But if changes to visible
	 *  and was invisible before, then will appear at the same position it was in the user interface. The position is
	 *  according the order of the insertion at the group. Anyway, the group will have this panel.</p>
	 * 
	 * @param b the new visibility for that panel.
	 */
	public abstract void setInGroupGUI(boolean b);

	/**
	 * <p>Returns <code>true</code> if this panel belongs to a 'panel group' and it's loaded in the graphical user interface
	 *  of that component; otherwise returns <code>false</code>.</p>
	 * 
	 * @return <code>true</code> if this panel belongs to a 'panel group' and it's loaded in the graphical user interface
	 *  of that component
	 */
	public abstract boolean isInGroupGUI();

	/**
	 * <p>Returns if this panel remains with its initial preferred size of it has been changed.</p>
	 * 
	 * @return if this panel remains with its initial preferred size of it has been changed
	 */
	public abstract boolean remainsWithItsDefaultPreferredSize();

	/**
	 * <p>Notifies to this panel of an <i>accept</i> action.</p>
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

	/**
	 * <p>Notifies this panel that has been selected at the {@link AbstractPanelGroup AbstractPanelGroup}</p>
	 */
	public abstract void selected();

	/**
	 * <p>Determines if this panel has changed since it was created, or applied (or accepted or cancelled). The 
	 *  programmer of each panel will be whom would set to <code>true</code> that a panel has changed.</p>
	 *
	 * @return <code>true</code> if this panel has changed since it was created, or applied (or accepted or cancelled); otherwise <code>false</code> 
	 */
	public abstract boolean hasChanged();

	/**
	 * <p>Resets this panel <i>changed status</i> to its initial value.</p>
	 */
	public abstract void resetChangedStatus();

	/**
	 * <p>Sets if this panel will always be applied and accepted (by default), or only when has changed.</i></p>
	 * 
	 * @param b if this panel will always be applied and accepted, or only when has changed
	 * 
	 * @see #isAlwaysApplicable()
	 * @see #hasChanged()
	 */
	public abstract void setAlwaysApplicable(boolean b);
	
	/**
	 * <p>Gets if this panel will always be applied and accepted (by default), or only when has changed.</i></p>
	 * 
	 * @return if this panel will always be applied and accepted, or only when has changed
	 * 
	 * @see #setAlwaysApplicable(boolean)
	 * @see #hasChanged()
	 */
	public abstract boolean isAlwaysApplicable();
}
