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

package org.gvsig.gui.beans.panelGroup.tabbedPanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.io.Serializable;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.gvsig.gui.beans.panelGroup.AbstractPanelGroup;
import org.gvsig.gui.beans.panelGroup.PanelGroupManager;
import org.gvsig.gui.beans.panelGroup.panels.AbstractPanel;
import org.gvsig.gui.beans.panelGroup.panels.IPanel;


/**
 * <p>Graphical interface that's a {@link JPanel JPanel} with an inner {@link JTabbedPane JTabbedPane} that
 * contains the {@link IPanel IPanel}'s of this group, and supports work with them.</p>
 * 
 * @see AbstractPanelGroup
 * 
 * @version 15/10/2007
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es) 
 */
public class TabbedPanel extends AbstractPanelGroup implements Serializable {
	private static final long serialVersionUID = -7979646227156641693L;

	/**
	 * <p>Graphical interface where the panels will be drawn.</p>
	 * 
	 * @see #getJTabbedPane()
	 */
	private JTabbedPane jTabbedPane;

	static {
		// Registers this class as a "PanelGroup" type
		PanelGroupManager.getManager().registerPanelGroup(TabbedPanel.class);
		
		// Sets this class as the default "PanelGroup" type
		PanelGroupManager.getManager().setDefaultType(TabbedPanel.class);
	}

	/**
	 * <p>Default constructor.</p>
	 * 
	 * @param reference object that is ''semantically' or 'contextually' related to the group of panels
	 */
	public TabbedPanel(Object reference) {
		super(reference);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.AbstractPanelGroup#initialize()
	 */
	protected void initialize() {
		super.initialize();
		this.setLayout(new BorderLayout());
		this.add(getJTabbedPane(), BorderLayout.CENTER);
	}

	/**
	 * This method initializes jTabbedPane
	 *
	 * @return JTabbedPane
	 */
	protected JTabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();
			jTabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

			jTabbedPane.addContainerListener(new ContainerListener() {
				/*
				 * (non-Javadoc)
				 * @see java.awt.event.ContainerListener#componentAdded(java.awt.event.ContainerEvent)
				 */
				public void componentAdded(ContainerEvent e) {
					// Propagates the event -> this allows notify that a component has been added into this TabbedPanel
					dispatchEvent(e);
				}

				/*
				 * (non-Javadoc)
				 * @see java.awt.event.ContainerListener#componentRemoved(java.awt.event.ContainerEvent)
				 */
				public void componentRemoved(ContainerEvent e) {
					// Propagates the event -> this allows notify that a component has been removed from this TabbedPanel
					dispatchEvent(e);
				}
			});

			jTabbedPane.addChangeListener(new ChangeListener() {
				/*
				 * (non-Javadoc)
				 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
				 */
				public void stateChanged(ChangeEvent e) {
					Object source = e.getSource();
					
					if (!((source != null) && (source instanceof JTabbedPane)))
						return;

					Object parent = ((JTabbedPane)source).getParent();
					
					if (!(parent instanceof TabbedPanel))
						return;
					
					Object object = ((JTabbedPane)source).getSelectedComponent();

					if ((object != null) && (object instanceof AbstractPanel)) {
		        		// Notifies the new panel selected
						((TabbedPanel)parent).stateChanged(new ChangeEvent(object));
					}
				}
			});
		}

		return jTabbedPane;
	}

	/**
	 * @see JTabbedPane#getSelectedIndex()
	 * @see #setSelectedIndex(int)
	 */
	public int getSelectedIndex() {
		return getJTabbedPane().getSelectedIndex();
	}

	/**
	 * @see JTabbedPane#setSelectedIndex(int)
	 * @see #getSelectedIndex()
	 */
	public void setSelectedIndex(int position) {
		getJTabbedPane().setSelectedIndex(position);
	}

//	/*
//	 * (non-Javadoc)
//	 * @see org.gvsig.gui.beans.panelGroup.AbstractPanelGroup#addPanel(org.gvsig.gui.beans.panelGroup.panels.IPanel)
//	 */
//	public void addPanel(IPanel panel) throws EmptyPanelGroupException, EmptyPanelGroupGUIException, PanelWithNoPreferredSizeDefinedException {
//		if ((belongsThisGroup(panel)) && (panel.getLabel() != null)) {
//			super.loadPanel(panel);
//			
//			if (((AbstractPanel)panel).isVisible())
//				getJTabbedPane().add(panel.getLabel(), (JPanel)panel);
//		}
//	}


	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.AbstractPanelGroup#loadPanel(org.gvsig.gui.beans.panelGroup.panels.IPanel)
	 */
	protected void loadPanel(IPanel panel) {
		super.loadPanel(panel);
		
		if (((AbstractPanel)panel).isVisible()) {
			getJTabbedPane().add(panel.getLabel(), (JPanel)panel);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.AbstractPanelGroup#unLoadPanel(org.gvsig.gui.beans.panelGroup.panels.IPanel)
	 */
	protected void unLoadPanel(IPanel panel) {
		super.unLoadPanel(panel);

		getJTabbedPane().remove((Component) panel);
	}
	
//	/*
//	 * (non-Javadoc)
//	 * @see org.gvsig.gui.beans.panelGroup.IPanelGroup#removePanel(org.gvsig.gui.beans.panelGroup.panels.AbstractPanel)
//	 */
//	public void removePanel(IPanel panel) {
//		
//		if ((belongsThisGroup(panel)) && (panel.getLabel() != null)) {
//			super.removePanel(panel);
//			getJTabbedPane().remove((Component) panel);
//		}
//	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.IPanelGroup#getActivePanel()
	 */
	public IPanel getActivePanel() {
		if (getJTabbedPane().getSelectedComponent() instanceof IPanel)
			return (IPanel)getJTabbedPane().getSelectedComponent(); 		
		else
			return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.AbstractPanelGroup#stateChanged(javax.swing.event.ChangeEvent)
	 */
	public void stateChanged(ChangeEvent e) {
		if (registeredPanels.size() == 0)
			return;

		Object object = e.getSource();

		if (object != null)
			((IPanel)object).selected();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.IPanelGroup#setPanelInGUI(org.gvsig.gui.beans.panelGroup.panels.IPanel, boolean)
	 */
	public synchronized void setPanelInGUI(IPanel panel, boolean b) {
		if (registeredPanels.size() == 0)
			return;

		if (registeredPanels.indexOf(panel) == -1)
			return;
		
		// Remove all tabs and re-insert the necessary
		Vector<Component> tabPanels = new Vector<Component>(0, getJTabbedPane().getTabCount());

		for (int i = getJTabbedPane().getTabCount() -1; i >= 0; i--){
			if (getJTabbedPane().getComponentAt(i) instanceof AbstractPanel) {
				tabPanels.add(getJTabbedPane().getComponentAt(i));
				getJTabbedPane().removeTabAt(i);
			}
		}

		if (b == true) {
			for (IPanel regPanel: registeredPanels) {
				if (regPanel.equals(panel)) {
					getJTabbedPane().add(panel.getLabel(), (JPanel)panel);
				}
				else {
					if (tabPanels.indexOf((Component)regPanel) != -1) 
						getJTabbedPane().add(regPanel.getLabel(), (JPanel)regPanel);
				}
			}
		}
		else {
			for (IPanel regPanel: registeredPanels) {
				if (tabPanels.indexOf((Component)regPanel) != -1) {
					if (!(regPanel.equals(panel))) {
						getJTabbedPane().add(regPanel.getLabel(), (JPanel)regPanel);
					}
				}
			}
		}

		repaint();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.IPanelGroup#isPanelInGUI(org.gvsig.gui.beans.panelGroup.panels.IPanel)
	 */
	public synchronized boolean isPanelInGUI(IPanel panel) {
		if (!registeredPanels.contains(panel))
			return false;

		for (int i = getJTabbedPane().getTabCount() -1; i >= 0; i--){
			Object object = getJTabbedPane().getComponentAt(i);
			if (object instanceof AbstractPanel) {
				if (object.equals(panel))
					return true;
			}
		}
		
		return false;
	}

	/**
	 * @see JTabbedPane#setEnabledAt(int, boolean)
	 */
	public void setEnabledAt(int index, boolean enabled) {
		getJTabbedPane().setEnabledAt(index, enabled);
	}

	/**
	 * @see JTabbedPane#isEnabledAt(int)
	 */
	public boolean isEnabledAt(int index) {
		return getJTabbedPane().isEnabledAt(index);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.IPanelGroup#getPanelInGUICount()
	 */
	public int getPanelInGUICount() {
		return getJTabbedPane().getTabCount();
	}

	/**
     * @see JTabbedPane#addChangeListener(ChangeListener)
     */
    public void addChangeListener(ChangeListener l) {
		getJTabbedPane().addChangeListener(l);
	}

    /**
     * @see JTabbedPane#removeChangeListener(ChangeListener)
     */
    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }

    /**
     * @see JTabbedPane#getChangeListeners()
     */
    public ChangeListener[] getChangeListeners() {
        return (ChangeListener[])listenerList.getListeners(ChangeListener.class);
    }
}
