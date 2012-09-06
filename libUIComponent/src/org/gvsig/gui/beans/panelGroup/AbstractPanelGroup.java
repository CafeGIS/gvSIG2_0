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

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.gvsig.tools.exception.BaseException;
import org.gvsig.gui.beans.Messages;
import org.gvsig.gui.beans.buttonspanel.IButtonsPanel;
import org.gvsig.gui.beans.panelGroup.exceptions.EmptyPanelGroupException;
import org.gvsig.gui.beans.panelGroup.exceptions.EmptyPanelGroupGUIException;
import org.gvsig.gui.beans.panelGroup.exceptions.ListCouldntAddPanelException;
import org.gvsig.gui.beans.panelGroup.exceptions.PanelBaseException;
import org.gvsig.gui.beans.panelGroup.exceptions.PanelWithNoPreferredSizeDefinedException;
import org.gvsig.gui.beans.panelGroup.loaders.IPanelGroupLoader;
import org.gvsig.gui.beans.panelGroup.panels.AbstractPanel;
import org.gvsig.gui.beans.panelGroup.panels.IPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Represents a {@link JPanel JPanel} adapted to work with as a group of {@link IPanel IPanel}'s.</p>
 *
 * @see IPanelGroup
 *
 * @version 15/10/2007
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es)
 */
public abstract class AbstractPanelGroup extends JPanel implements ChangeListener, IPanelGroup, IButtonsPanel {
	final static private Logger logger = LoggerFactory.getLogger(AbstractPanelGroup.class);

	/**
	 * <p>If the last notification received has been {@linkplain AbstractPanelGroup#accept()}.</p>
	 *
	 * @see #accept()
	 */
	protected boolean accepted;

	/**
	 * <p>Properties associated to this group of panels.</p>
	 *
	 * @see #getProperties()
	 */
	private Hashtable properties;

	/**
	 * <p>Object that has a  a ''semantically'' or ''conceptually'' relation to this panel.</p>
	 *
	 *  @see #getReference()
	 */
	protected Object reference;

	/**
	 * <p>Array list with all {@link IPanel IPanel} of this group.</p>
	 *
	 * @see #loadPanels(IPanelGroupLoader)
	 * @see #addPanel(IPanel)
	 * @see #values()
	 */
	protected ArrayList<IPanel> registeredPanels = null;

	/**
	 * <p>Accept action identifier.</p>
	 */
	protected final byte ACCEPT_ACTION = 0;

	/**
	 * <p>Apply action identifier.</p>
	 */
	protected final byte APPLY_ACTION = 1;

	/**
	 * <p>Cancel action identifier.</p>
	 */
	protected final byte CANCEL_ACTION = 2;

	/**
	 * <p>Reference to another <code>IPanelGroup</code> component which contains this one.</p>
	 */
	protected IPanelGroup parentPanelGroup;

	/**
	 * <p>Default constructor of a group of {@link IPanel IPanel}.</p>
	 *
	 * @param reference object that is ''semantically' or 'contextually' related to the group of panels
	 */
	public AbstractPanelGroup(Object reference) {
		this.reference = reference;

		initialize();
	}

	/**
	 * <p>Returns all panels of this group.</p>
	 *
	 * @return panels of this group
	 *
	 * @see #loadPanels(IPanelGroupLoader)
	 * @see #addPanel(IPanel)
	 */
	public final Collection<IPanel> values() {
		return registeredPanels;
	}

	/**
	 * <p>This method is used by each concrete implementation of <code>AbstractPanelGroup</code> to
	 *  execute its particular initialization tasks.</p>
	 */
	protected void initialize() {
		accepted = false;
		parentPanelGroup = null;
		properties = new Hashtable();

		// Resizes automatically this and its components, and when a new panel is added, gets adjust to
		//  the new component could be displayed at least with its preferred size
		setLayout(new BorderLayout());
		registeredPanels = new ArrayList<IPanel>();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.IPanelGroup#loadPanels(org.gvsig.gui.beans.panelGroup.loaders.IPanelGroupLoader)
	 */
	public void loadPanels(IPanelGroupLoader loader) throws ListCouldntAddPanelException, EmptyPanelGroupException, EmptyPanelGroupGUIException {
		ArrayList<IPanel> panels = new ArrayList<IPanel>();
		ListCouldntAddPanelException listBaseException = null;

		// Don't reload the panels
		if (registeredPanels.size() > 0)
			return;

		try {
			// Tries to load as most as possible panels
			loader.loadPanels(panels);
		} catch (Exception e) {
			listBaseException = addCouldntAddPanelException(listBaseException, e);
		}

		if (panels.size() == 0) {
			if (listBaseException == null)
				throw new EmptyPanelGroupException();
			else {
				// If there were other exceptions, launch all them with the 'EmptyPanelGroupException' as the first of them.
				listBaseException.add(new EmptyPanelGroupException());
				throw listBaseException;
			}
		}

		Iterator<IPanel> iterator = panels.iterator();
		IPanel panel;
		PanelWithNoPreferredSizeDefinedException exception;

		while (iterator.hasNext()) {
			panel = iterator.next();

			try {
				if (panel.remainsWithItsDefaultPreferredSize()) {
					exception = new PanelWithNoPreferredSizeDefinedException(panel.getLabel());

					throw exception;
				}
				else {
					loadPanel(panel);
				}
			} catch( PanelWithNoPreferredSizeDefinedException e ) {
				logger.debug(Messages.getText("panel_adding_exception"), e);
				listBaseException = addCouldntAddPanelException(listBaseException, e);
			} catch (Exception e) {
				logger.debug(Messages.getText("panel_adding_exception"), e);

				if (panel == null)
					listBaseException = addCouldntAddPanelException(listBaseException, new PanelBaseException(e, ""));
				else
					listBaseException = addCouldntAddPanelException(listBaseException, new PanelBaseException(e, panel.getLabel()));
			}
		}

		if (getPanelCount() == 0) {
			if (listBaseException != null) {
				// If there were other exceptions, launch all them with the 'EmptyPanelGroupException' as the first of them.
				listBaseException = addCouldntAddPanelException(listBaseException, new EmptyPanelGroupException());
				throw listBaseException;
			}
			else {
				throw new EmptyPanelGroupException();
			}
		}

		if (getPanelInGUICount() == 0) {
			if (listBaseException != null) {
				// If there were other exceptions, launch all them with the 'EmptyPanelGroupGUIException' as the first of them.
				listBaseException = addCouldntAddPanelException(listBaseException, new EmptyPanelGroupGUIException());
				throw listBaseException;
			}
			else {
				throw new EmptyPanelGroupGUIException();
			}
		}

		if (listBaseException != null)
			throw listBaseException;
	}

	private ListCouldntAddPanelException addCouldntAddPanelException(ListCouldntAddPanelException l, Exception e) {
		if( l == null ) {
			l = new ListCouldntAddPanelException();
		}

		l.add(e);

		return l;
	}

	/**
	 * <p>Loads a particular panel. It's supposed that this panel is valid.</p>
	 *
	 * @param panel the panel to add
	 *
	 * @see #addPanel(IPanel)
	 * @see #loadPanels(IPanelGroupLoader)
	 */
	protected void loadPanel(IPanel panel) {
		panel.setPanelGroup(this);
		panel.setReference(reference);
		registeredPanels.add(panel);
	}

	/**
	 * <p>Validates if the panel as parameter belongs this panel group.</p>
	 * <p>One panel belongs this panel group if have the same references, or
	 * both references are <code>null</code>. </p>
	 *
	 * @param panel the panel to validate
	 * @return <code>true</code> if belong to this group; otherwise <code>false</code>
	 */
	protected boolean belongsThisGroup(IPanel panel) {
		if (panel == null)
			return false;

		if (getReference() == null) {
			if (panel.getReference() == null)
				return true;
			else
				return false;
		}
		else {
			if (panel.getReference() == null)
				return false;
			else {
				if (getReference().equals(panel.getReference()))
					return true;
				else
					return false;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.IPanelGroup#isAccepted()
	 */
	public boolean isAccepted() {
	  	return accepted;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.IPanelGroup#getProperties()
	 */
	public Hashtable getProperties() {
		return this.properties;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.IPanelGroup#getReference()
	 */
	public Object getReference() {
		return reference;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.IPanelGroup#updateReference(java.lang.Object)
	 */
	public void updateReference(Object reference) {
		this.reference = reference;

		for (Object panel : values().toArray()) {
			((AbstractPanel)panel).setReference(reference);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.IPanelGroup#accept()
	 */
	public void accept() {
		this.executeAction(ACCEPT_ACTION);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.IPanelGroup#apply()
	 */
	public void apply() {
		this.executeAction(APPLY_ACTION);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.IPanelGroup#cancel()
	 */
	public void cancel() {
		this.executeAction(CANCEL_ACTION);
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	public abstract void stateChanged(ChangeEvent e);

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.IPanelGroup#addPanel(org.gvsig.gui.beans.panelGroup.panels.IPanel)
	 */
	public void addPanel(IPanel panel) throws BaseException {
		BaseException baseException = null;
		ListCouldntAddPanelException listBaseException = null;

		if (panel.remainsWithItsDefaultPreferredSize()) {
			baseException = new PanelWithNoPreferredSizeDefinedException(panel.getLabel());
		}
		else {
			try {
				if ((panel.getLabel() != null) && (belongsThisGroup(panel))) {
					// Load the panel
					loadPanel(panel);
				}
			}
			catch (Exception e) {
				logger.debug(Messages.getText("panel_adding_exception"), e);

				if (panel == null)
					baseException = new PanelBaseException(e, "");
				else
					baseException = new PanelBaseException(e, panel.getLabel());
			}
		}

		if (registeredPanels.size() == 0) {
			if (baseException == null) {
				throw new EmptyPanelGroupException();
			}
			else {
				listBaseException = new ListCouldntAddPanelException();
				listBaseException.add(baseException);
				listBaseException.add(new EmptyPanelGroupException());
			}
		}

		if (getPanelInGUICount() == 0) {
			if (listBaseException == null) {
				if (baseException == null)
					throw new EmptyPanelGroupGUIException();
				else {
					listBaseException = new ListCouldntAddPanelException();
					listBaseException.add(baseException);
					listBaseException.add(new EmptyPanelGroupGUIException());
				}
			}
			else {
				listBaseException.add(new EmptyPanelGroupGUIException());
			}
		}

		if (listBaseException != null)
			throw listBaseException;
		else {
			if (baseException != null)
				throw baseException;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.IPanelGroup#removePanel(org.gvsig.gui.beans.panelGroup.panels.AbstractPanel)
	 */
	public void removePanel(IPanel panel) {
		if ((belongsThisGroup(panel)) && (panel.getLabel() != null)) {
			unLoadPanel(panel);
		}
	}

	/**
	 * <p>Unloads a particular panel. It's supposed that this panel is valid.</p>
	 *
	 * @param panel the panel to add
	 *
	 * @see #removePanel(IPanel)
	 * @see #loadPanel(IPanel)
	 * @see #loadPanels(IPanelGroupLoader)
	 */
	protected void unLoadPanel(IPanel panel) {
		registeredPanels.remove(panel);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.IPanelGroup#getPanelCount()
	 */
	public int getPanelCount() {
		return registeredPanels.size();
	}

	/**
	 * <p>Executes an <i>accept</i>, <i>apply</i>, or <i>cancel</i> action. This implies notify that action
	 *  to all panels of the group.</p>
	 *
	 * @param action the action to execute
	 *
	 * @see IPanel#accept()
	 * @see IPanel#apply()
	 * @see IPanel#cancel()
	 */
	private void executeAction(byte action) {
		Iterator<IPanel> iterator = this.registeredPanels.iterator();

		while (iterator.hasNext()) {
			IPanel panel = iterator.next();

			if ((panel.hasChanged()) || ((!panel.hasChanged()) && (panel.isAlwaysApplicable()))) {
				switch (action) {
					case ACCEPT_ACTION:
						accepted = true;
						panel.accept();
						panel.resetChangedStatus();
						break;
					case APPLY_ACTION:
						accepted = false;
						panel.apply();
						panel.resetChangedStatus();
						break;
					case CANCEL_ACTION:
						accepted = false;
						panel.cancel();
						panel.resetChangedStatus();
						break;
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.IPanelGroup#setParentPanelGroup(org.gvsig.gui.beans.panelGroup.IPanelGroup)
	 */
	public void setParentPanelGroup(IPanelGroup parent) {
		parentPanelGroup = parent;
	}

	///// BEGIN: Buttons Enable/Disable functionality /////

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#isEnabledAcceptButton()
	 */
	public boolean isEnabledAcceptButton() {
		if (parentPanelGroup != null)
			return ((IButtonsPanel)parentPanelGroup).isEnabledAcceptButton();

		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#isEnabledApplyButton()
	 */
	public boolean isEnabledApplyButton() {
		if (parentPanelGroup != null)
			return ((IButtonsPanel)parentPanelGroup).isEnabledApplyButton();

		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#isEnabledCancelButton()
	 */
	public boolean isEnabledCancelButton() {
		if (parentPanelGroup != null)
			return ((IButtonsPanel)parentPanelGroup).isEnabledCancelButton();

		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#isEnabledCloseButton()
	 */
	public boolean isEnabledCloseButton() {
		if (parentPanelGroup != null)
			return ((IButtonsPanel)parentPanelGroup).isEnabledCloseButton();

		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#isEnabledExitButton()
	 */
	public boolean isEnabledExitButton() {
		if (parentPanelGroup != null)
			return ((IButtonsPanel)parentPanelGroup).isEnabledExitButton();

		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#isEnabledHideDetailsButton()
	 */
	public boolean isEnabledHideDetailsButton() {
		if (parentPanelGroup != null)
			return ((IButtonsPanel)parentPanelGroup).isEnabledHideDetailsButton();

		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#isEnabledNoButton()
	 */
	public boolean isEnabledNoButton() {
		if (parentPanelGroup != null)
			return ((IButtonsPanel)parentPanelGroup).isEnabledNoButton();

		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#isEnabledPauseButton()
	 */
	public boolean isEnabledPauseButton() {
		if (parentPanelGroup != null)
			return ((IButtonsPanel)parentPanelGroup).isEnabledPauseButton();

		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#isEnabledRestartButton()
	 */
	public boolean isEnabledRestartButton() {
		if (parentPanelGroup != null)
			return ((IButtonsPanel)parentPanelGroup).isEnabledRestartButton();

		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#isEnabledSaveButton()
	 */
	public boolean isEnabledSaveButton() {
		if (parentPanelGroup != null)
			return ((IButtonsPanel)parentPanelGroup).isEnabledSaveButton();

		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#isEnabledSeeDetailsButton()
	 */
	public boolean isEnabledSeeDetailsButton() {
		if (parentPanelGroup != null)
			return ((IButtonsPanel)parentPanelGroup).isEnabledSeeDetailsButton();

		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#isEnabledYesButton()
	 */
	public boolean isEnabledYesButton() {
		if (parentPanelGroup != null)
			return ((IButtonsPanel)parentPanelGroup).isEnabledYesButton();

		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#setEnabledAcceptButton(boolean)
	 */
	public void setEnabledAcceptButton(boolean b) {
		if (parentPanelGroup != null)
			((IButtonsPanel)parentPanelGroup).setEnabledAcceptButton(b);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#setEnabledApplyButton(boolean)
	 */
	public void setEnabledApplyButton(boolean b) {
		if (parentPanelGroup != null)
			((IButtonsPanel)parentPanelGroup).setEnabledApplyButton(b);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#setEnabledCancelButton(boolean)
	 */
	public void setEnabledCancelButton(boolean b) {
		if (parentPanelGroup != null)
			((IButtonsPanel)parentPanelGroup).setEnabledCancelButton(b);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#setEnabledCloseButton(boolean)
	 */
	public void setEnabledCloseButton(boolean b) {
		if (parentPanelGroup != null)
			((IButtonsPanel)parentPanelGroup).setEnabledCloseButton(b);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#setEnabledExitButton(boolean)
	 */
	public void setEnabledExitButton(boolean b) {
		if (parentPanelGroup != null)
			((IButtonsPanel)parentPanelGroup).setEnabledExitButton(b);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#setEnabledHideDetailsButton(boolean)
	 */
	public void setEnabledHideDetailsButton(boolean b) {
		if (parentPanelGroup != null)
			((IButtonsPanel)parentPanelGroup).setEnabledHideDetailsButton(b);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#setEnabledNoButton(boolean)
	 */
	public void setEnabledNoButton(boolean b) {
		if (parentPanelGroup != null)
			((IButtonsPanel)parentPanelGroup).setEnabledNoButton(b);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#setEnabledPauseButton(boolean)
	 */
	public void setEnabledPauseButton(boolean b) {
		if (parentPanelGroup != null)
			((IButtonsPanel)parentPanelGroup).setEnabledPauseButton(b);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#setEnabledRestartButton(boolean)
	 */
	public void setEnabledRestartButton(boolean b) {
		if (parentPanelGroup != null)
			((IButtonsPanel)parentPanelGroup).setEnabledRestartButton(b);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#setEnabledSaveButton(boolean)
	 */
	public void setEnabledSaveButton(boolean b) {
		if (parentPanelGroup != null)
			((IButtonsPanel)parentPanelGroup).setEnabledSaveButton(b);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#setEnabledSeeDetailsButton(boolean)
	 */
	public void setEnabledSeeDetailsButton(boolean b) {
		if (parentPanelGroup != null)
			((IButtonsPanel)parentPanelGroup).setEnabledSeeDetailsButton(b);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#setEnabledYesButton(boolean)
	 */
	public void setEnabledYesButton(boolean b) {
		if (parentPanelGroup != null)
			((IButtonsPanel)parentPanelGroup).setEnabledYesButton(b);
	}

	///// END: Buttons Enable/Disable functionality /////
}
