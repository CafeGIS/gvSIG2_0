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

import java.awt.Dimension;

import javax.swing.JPanel;

import org.gvsig.gui.beans.panelGroup.AbstractPanelGroup;

/**
 * <p>Represents a {@link JPanel JPanel} adapted to work as a panel of an {@link AbstractPanelGroup  AbstractPanelGroup}.</p>
 *  
 * @see JPanel
 * @see IPanel
 * 
 * @see AbstractPanelGroup
 * 
 * @version 16/10/2007
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es) 
 */
public abstract class AbstractPanel extends JPanel implements IPanel {
	/**
	 * <p>If this panel remains with its initial preferred size of it has been changed.</p>
	 * 
	 * @see #remainsWithItsDefaultPreferredSize()
	 */
	protected boolean remainsWithDefaultPreferredSize;

	/**
	 * <p>Optional attribute that identifies this panel.</p>
	 * 
	 * @see #getID()
	 * @see #setID(String)
	 */
	private String id;

	/**
	 * <p>Identifier used by a {@link AbstractPanelGroup AbstractPanelGroup} to identify this panel.</p>
	 * 
	 * @see #getLabel()
	 * @see #setLabel(String)
	 */
	private String label;

	/**
	 * <p>Identifier used to group together different <code>IPanel</code> panels
     *      in a {@link AbstractPanelGroup AbstractPanelGroup}</p>
     *
     * @see #getLabelGroup()
     * @see #setLabelGroup(String)
	 */
	private String labelGroup;

	/**
	 * <p>Reference to an object that is related this panel</p>
	 * 
	 * @see #getReference()
	 * @see #setReference(Object)
	 */
	private Object reference;

	/**
	 * <p>Reference to the container of the group which this panel is a member.</p>
	 * 
	 * @see #getPanelGroup()
	 * @see #setPanelGroup(AbstractPanelGroup)
	 */
	private AbstractPanelGroup panelGroup;
	
	/**
	 * <p>Determines if this component has changed since it was created, or applied (or accepted).</p>
	 */
	protected boolean hasChanged;

	/**
	 * <p>Determines if this panel will always be applied and accepted, or only when has changed.</p>
	 */
	private boolean alwaysApplicable;
	
	/**
	 * <p>Initializes this panel.</p>
	 */
	public AbstractPanel() {
		super();
		
		id = null;
		label = null;
		labelGroup = null;
		remainsWithDefaultPreferredSize = true;
		hasChanged = false;
		alwaysApplicable = true;
	}

	/**
	 * <p>Initializes this panel using three parameters.</p>
	 * 
	 * @param id optional attribute that identifies this panel
	 * @param label identifier used by a {@link AbstractPanelGroup AbstractPanelGroup} to identify this panel
	 * @param labelGroup identifier used to group together different <code>IPanel</code> panels
     *      in a {@link AbstractPanelGroup AbstractPanelGroup}
	 */
	public AbstractPanel(String id, String label, String labelGroup) {
		super();
		
		this.id = id;
		this.label = label;
		this.labelGroup = labelGroup;
		remainsWithDefaultPreferredSize = true;
		hasChanged = false;
		alwaysApplicable = true;
	}

	/**
	 * <p>This method is used by each concrete implementation of <code>AbstractPanel</code> to
	 *  execute its particular initialization tasks.</p>
	 */
	protected abstract void initialize();

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.panels.IPanel#getID()
	 */
	public String getID() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.panels.IPanel#getLabel()
	 */
	public String getLabel() {
		return label;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.panels.IPanel#getLabelGroup()
	 */
	public String getLabelGroup() {
		return labelGroup;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.panels.IPanel#setID(java.lang.String)
	 */
	public void setID(String id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.panels.IPanel#setLabel(java.lang.String)
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.panels.IPanel#setLabelGroup(java.lang.String)
	 */
	public void setLabelGroup(String labelGroup) {
		this.labelGroup = labelGroup;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.panels.IPanel#getReference()
	 */
	public Object getReference() {
		return reference; 
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.panels.IPanel#setReference(java.lang.Object)
	 */
	public void setReference(Object ref) {
		reference = ref;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.panels.IPanel#getPanelGroup()
	 */
	public AbstractPanelGroup getPanelGroup() {
		return panelGroup;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.panels.IPanel#setPanelGroup(org.gvsig.gui.beans.panelGroup.AbstractPanelGroup)
	 */
	public void setPanelGroup(AbstractPanelGroup panelGroup) {
		this.panelGroup = panelGroup;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.panels.IPanel#setInGroupGUI(boolean)
	 */
	public synchronized void setInGroupGUI(boolean visible) {
		if (panelGroup != null)
			panelGroup.setPanelInGUI(this, visible);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.panels.IPanel#isInGroupGUI()
	 */
	public synchronized boolean isInGroupGUI() {
		if (panelGroup == null)
			return false;

		return panelGroup.isPanelInGUI(this);
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.Component#toString()
	 */
	public String toString() {
		return (label == null)? "": label;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.panels.IPanel#remainsWithItsDefaultPreferredSize()
	 */
	public boolean remainsWithItsDefaultPreferredSize() {
		return remainsWithDefaultPreferredSize;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#setPreferredSize(java.awt.Dimension)
	 */
	public void setPreferredSize(Dimension preferredSize) {
		super.setPreferredSize(preferredSize);
		remainsWithDefaultPreferredSize = false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.panels.IPanel#hasChanged()
	 */
	public boolean hasChanged() {
		return hasChanged;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.panels.IPanel#resetChangedStatus()
	 */
	public void resetChangedStatus() {
		hasChanged = false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.panels.IPanel#setAlwaysApplicable(boolean)
	 */
	public void setAlwaysApplicable(boolean b) {
		alwaysApplicable = b;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.panels.IPanel#isAlwaysApplicable()
	 */
	public boolean isAlwaysApplicable() {
		return alwaysApplicable;
	}
}
