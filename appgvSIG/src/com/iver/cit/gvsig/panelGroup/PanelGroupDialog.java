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

package com.iver.cit.gvsig.panelGroup;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.Serializable;
import java.util.Hashtable;

import javax.swing.JOptionPane;

import org.gvsig.gui.beans.Messages;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener;
import org.gvsig.gui.beans.buttonspanel.IButtonsPanel;
import org.gvsig.gui.beans.defaultbuttonspanel.DefaultButtonsPanel;
import org.gvsig.gui.beans.panelGroup.AbstractPanelGroup;
import org.gvsig.gui.beans.panelGroup.IPanelGroup;
import org.gvsig.gui.beans.panelGroup.exceptions.EmptyPanelGroupException;
import org.gvsig.gui.beans.panelGroup.exceptions.EmptyPanelGroupGUIException;
import org.gvsig.gui.beans.panelGroup.exceptions.ListCouldntAddPanelException;
import org.gvsig.gui.beans.panelGroup.loaders.IPanelGroupLoader;
import org.gvsig.gui.beans.panelGroup.panels.IPanel;
import org.gvsig.tools.exception.BaseException;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.IWindowListener;
import com.iver.andami.ui.mdiManager.WindowInfo;

/**
 * <p>Graphical component that is represented as an {@link IWindow IWindow}, and that has two immediate internal
 *  panels, the top is a {@link PanelGroup PanelGroup}, and the bottom is a {@link PanelGroupDialog PanelGroupDialog}
 *  of <code>PanelGroupDialog.BUTTONS_ACCEPTCANCELAPPLY</code> type.</p>
 *
 * @see DefaultButtonsPanel
 *
 * @version 15/10/2007
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es)
 */
public class PanelGroupDialog extends DefaultButtonsPanel implements ButtonsPanelListener, IWindow, IWindowListener, IPanelGroup, IButtonsPanel, Serializable {
	private static final long serialVersionUID = 6267404338808012765L;

	/**
	 * <p>Default width.</p>
	 */
	protected static int defaultWidth = 530;

	/**
	 * <p>Default height.</p>
	 */
	protected static int defaultHeight = 432;

	/**
	 * <p>This graphical component has a panel that allows work {@link IPanel IPanel}.</p>
	 *
	 * @see #getPanelGroup()
	 */
	protected IPanelGroup panelGroup;

	/**
	 * <p>Properties about the managing of this dialog by <i>Andami</i>.</p>
	 */
	private WindowInfo windowInfo;

	/**
	 * <p>Default constructor with two parameters, that creates a panel with an {@link AbstractPanelGroup AbstractPanelGroup}
	 *  on top and another with <i>accept</i>, <i>apply</i>, and <i>cancel</i> buttons on bottom.</p>
	 *
	 * @param windowTitle title for this <code>IWindow</code>
	 * @param PanelGroup kind of panel for work with this <code>IWindow</code>
	 */
	public PanelGroupDialog(String windowTitle, IPanelGroup panelGroup) {
		super(ButtonsPanel.BUTTONS_ACCEPTCANCELAPPLY);

		windowInfo = new WindowInfo();
		windowInfo.setTitle(PluginServices.getText(this, windowTitle));
		windowInfo.setWidth(defaultWidth);
		windowInfo.setHeight(defaultHeight);

		this.panelGroup = panelGroup;

		initialize();
	}

	/**
	 * <p>Default constructor with six parameters, that creates a panel with an {@link AbstractPanelGroup AbstractPanelGroup}
	 *  on top and another with <i>accept</i>, <i>apply</i>, and <i>cancel</i> buttons on bottom.</p>
	 * <p>Last parameter is the {@link PanelGroup PanelGroup} graphical component that
	 *  this panel will have, and the previous are information to initialize this {@link IWindow IWindow}.</p>
	 *
	 * @param additionalInfo additional information about this <code>IWindow</code>
	 * @param windowTitle title for this <code>IWindow</code>
	 * @param width width for this component
	 * @param height height for this component
	 * @param windowInfoProperties properties about the managing of this dialog by <i>Andami</i>
	 * @param PanelGroup kind of panel for work with this <code>IWindow</code>
	 */
	public PanelGroupDialog(String additionalInfo, String windowTitle, int  width, int height, byte windowInfoProperties, IPanelGroup panelGroup) {
		super(ButtonsPanel.BUTTONS_ACCEPTCANCELAPPLY);

		windowInfo = new WindowInfo(windowInfoProperties);
		windowInfo.setAdditionalInfo(additionalInfo);
		windowInfo.setTitle(PluginServices.getText(this, windowTitle));
		windowInfo.setWidth(width);
		windowInfo.setHeight(height);

		this.panelGroup = panelGroup;

		initialize();
	}

	/**
	 * <p>This method is used to execute initialization tasks of this graphical component.</p>
	 */
	private void initialize() {
		setLayout(new BorderLayout());
		addButtonPressedListener(this);

		this.panelGroup.setParentPanelGroup(this);

		// Mask possible extra exceptions
		if (panelGroup != null) {
			add((AbstractPanelGroup)panelGroup, BorderLayout.CENTER);

			// Holds the preferred size
			if ((windowInfo.getWidth() > getPreferredSize().width) || (windowInfo.getHeight() > getPreferredSize().height)) {
				setPreferredSize(new Dimension(Math.max(windowInfo.getWidth(), getWidth()), Math.max(windowInfo.getHeight(), getHeight())));
			}
		}
	}

	/**
	 * <p>Determinates if this panel is resizable or not.</p>
	 *
	 * @return <code>true</code> if it's resizable; <code>false</code> otherwise.
	 */
	public boolean isResizable() {
		return windowInfo.isResizable();
	}

	/**
	 * <p>Closes this {@link IWindow IWindow}.</p>
	 */
	public void close() {
		PluginServices.getMDIManager().closeWindow(this);
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.ui.mdiManager.IWindow#getWindowInfo()
	 */
	public WindowInfo getWindowInfo() {
		return windowInfo;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener#actionButtonPressed(org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent)
	 */
	public void actionButtonPressed(ButtonsPanelEvent e) {
		// Masks possible extra exceptions
		if (panelGroup != null) {
			switch (e.getButton()) {
			case ButtonsPanel.BUTTON_ACCEPT:
				accept();
				close();
				break;
			case ButtonsPanel.BUTTON_APPLY:
				apply();
				break;
			case ButtonsPanel.BUTTON_CANCEL:
				cancel();
				close();
				break;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.ui.mdiManager.IWindowListener#windowActivated()
	 */
	public void windowActivated() {}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.ui.mdiManager.IWindowListener#windowClosed()
	 */
	public void windowClosed() {
		// Masks possible extra exceptions
		if (panelGroup != null) {
			if (!panelGroup.isAccepted()) {
				panelGroup.cancel();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.IPanelGroup#accept()
	 */
	public void accept() {
		panelGroup.accept();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.IPanelGroup#addPanel(org.gvsig.gui.beans.panelGroup.panels.IPanel)
	 */
	public void addPanel(IPanel panel) throws BaseException {
		panelGroup.addPanel(panel);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.IPanelGroup#apply()
	 */
	public void apply() {
		panelGroup.apply();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.IPanelGroup#cancel()
	 */
	public void cancel() {
		panelGroup.cancel();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.IPanelGroup#getActivePanel()
	 */
	public IPanel getActivePanel() {
		return panelGroup.getActivePanel();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.IPanelGroup#getProperties()
	 */
	public Hashtable getProperties() {
		return panelGroup.getProperties();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.IPanelGroup#getReference()
	 */
	public Object getReference() {
		return panelGroup.getReference();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.IPanelGroup#updateReference(java.lang.Object)
	 */
	public void updateReference(Object reference) {
		panelGroup.updateReference(reference);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.IPanelGroup#isAccepted()
	 */
	public boolean isAccepted() {
		return panelGroup.isAccepted();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.IPanelGroup#loadPanels(org.gvsig.gui.beans.panelGroup.loaders.IPanelGroupLoader)
	 */
	public void loadPanels(IPanelGroupLoader loader) throws ListCouldntAddPanelException, EmptyPanelGroupException, EmptyPanelGroupGUIException {
		try {
			panelGroup.loadPanels(loader);

			if (panelGroup.getPanelInGUICount() == 0) {
				close();
			}
		}
		catch (BaseException be) {
			be.setTranslator(new Messages());

			JOptionPane.showMessageDialog(this, be.getLocalizedMessageStack());

			// Don't show the dialog if there is no panels in the GUI
			if (panelGroup.getPanelInGUICount() == 0) {
				close();
			}
		}
		catch (Exception e) {
			NotificationManager.showMessageError(e.getMessage(), e);

			// Don't show the dialog if there is no panels in the GUI
			if (panelGroup.getPanelInGUICount() == 0) {
				close();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.IPanelGroup#removePanel(org.gvsig.gui.beans.panelGroup.panels.IPanel)
	 */
	public void removePanel(IPanel panel) {
		panelGroup.removePanel(panel);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.IPanelGroup#isPanelInGUI(org.gvsig.gui.beans.panelGroup.panels.IPanel)
	 */
	public boolean isPanelInGUI(IPanel panel) {
		return panelGroup.isPanelInGUI(panel);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.IPanelGroup#setPanelInGUI(org.gvsig.gui.beans.panelGroup.panels.IPanel, boolean)
	 */
	public void setPanelInGUI(IPanel panel, boolean b) {
		panelGroup.setPanelInGUI(panel, b);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.IPanelGroup#getPanelCount()
	 */
	public int getPanelCount() {
		return panelGroup.getPanelCount();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.IPanelGroup#getPanelInGUICount()
	 */
	public int getPanelInGUICount() {
		return panelGroup.getPanelInGUICount();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#setPreferredSize(java.awt.Dimension)
	 */
	public void setPreferredSize(Dimension preferredSize) {
		super.setPreferredSize(preferredSize);

		windowInfo.setWidth(preferredSize.width);
		windowInfo.setHeight(preferredSize.height);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.IPanelGroup#setParentPanelGroup(org.gvsig.gui.beans.panelGroup.IPanelGroup)
	 */
	public void setParentPanelGroup(IPanelGroup parent) {
		panelGroup.setParentPanelGroup(parent);

//		this.addComponentListener(new ComponentAdapter())
	}

	///// BEGIN: Buttons Enable/Disable functionality /////

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#isEnabledAcceptButton()
	 */
	public boolean isEnabledAcceptButton() {
		return getButtonsPanel().getButton(ButtonsPanel.BUTTON_ACCEPT).isEnabled();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#isEnabledApplyButton()
	 */
	public boolean isEnabledApplyButton() {
		return getButtonsPanel().getButton(ButtonsPanel.BUTTON_APPLY).isEnabled();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#isEnabledCancelButton()
	 */
	public boolean isEnabledCancelButton() {
		return getButtonsPanel().getButton(ButtonsPanel.BUTTON_CANCEL).isEnabled();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#isEnabledCloseButton()
	 */
	public boolean isEnabledCloseButton() {
		/* Unimplemented */
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#isEnabledExitButton()
	 */
	public boolean isEnabledExitButton() {
		/* Unimplemented */
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#isEnabledHideDetailsButton()
	 */
	public boolean isEnabledHideDetailsButton() {
		/* Unimplemented */
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#isEnabledNoButton()
	 */
	public boolean isEnabledNoButton() {
		/* Unimplemented */
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#isEnabledPauseButton()
	 */
	public boolean isEnabledPauseButton() {
		/* Unimplemented */
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#isEnabledRestartButton()
	 */
	public boolean isEnabledRestartButton() {
		/* Unimplemented */
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#isEnabledSaveButton()
	 */
	public boolean isEnabledSaveButton() {
		/* Unimplemented */
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#isEnabledSeeDetailsButton()
	 */
	public boolean isEnabledSeeDetailsButton() {
		/* Unimplemented */
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#isEnabledYesButton()
	 */
	public boolean isEnabledYesButton() {
		/* Unimplemented */
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#setEnabledAcceptButton(boolean)
	 */
	public void setEnabledAcceptButton(boolean b) {
		getButtonsPanel().getButton(ButtonsPanel.BUTTON_ACCEPT).setEnabled(b);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#setEnabledApplyButton(boolean)
	 */
	public void setEnabledApplyButton(boolean b) {
		getButtonsPanel().getButton(ButtonsPanel.BUTTON_APPLY).setEnabled(b);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#setEnabledCancelButton(boolean)
	 */
	public void setEnabledCancelButton(boolean b) {
		getButtonsPanel().getButton(ButtonsPanel.BUTTON_CANCEL).setEnabled(b);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#setEnabledCloseButton(boolean)
	 */
	public void setEnabledCloseButton(boolean b) {
		/* Unimplemented */
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#setEnabledExitButton(boolean)
	 */
	public void setEnabledExitButton(boolean b) {
		/* Unimplemented */
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#setEnabledHideDetailsButton(boolean)
	 */
	public void setEnabledHideDetailsButton(boolean b) {
		/* Unimplemented */
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#setEnabledNoButton(boolean)
	 */
	public void setEnabledNoButton(boolean b) {
		/* Unimplemented */
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#setEnabledPauseButton(boolean)
	 */
	public void setEnabledPauseButton(boolean b) {
		/* Unimplemented */
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#setEnabledRestartButton(boolean)
	 */
	public void setEnabledRestartButton(boolean b) {
		/* Unimplemented */
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#setEnabledSaveButton(boolean)
	 */
	public void setEnabledSaveButton(boolean b) {
		/* Unimplemented */
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#setEnabledSeeDetailsButton(boolean)
	 */
	public void setEnabledSeeDetailsButton(boolean b) {
		/* Unimplemented */
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#setEnabledYesButton(boolean)
	 */
	public void setEnabledYesButton(boolean b) {
		/* Unimplemented */
	}

	public Object getWindowProfile() {
		return WindowInfo.PROPERTIES_PROFILE;
	}
	///// END: Buttons Enable/Disable functionality /////
}
