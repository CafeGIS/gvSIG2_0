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

package com.iver.cit.gvsig.gui.dialogs;

import java.awt.event.ActionEvent;
import java.util.Map;

import org.gvsig.fmap.dal.serverexplorer.wfs.WFSServerExplorer;
import org.gvsig.gui.beans.panelGroup.AbstractPanelGroup;
import org.gvsig.gui.beans.panelGroup.IPanelGroup;
import org.gvsig.gui.beans.panelGroup.exceptions.EmptyPanelGroupException;
import org.gvsig.gui.beans.panelGroup.exceptions.EmptyPanelGroupGUIException;
import org.gvsig.gui.beans.panelGroup.exceptions.ListCouldntAddPanelException;
import org.gvsig.gui.beans.panelGroup.loaders.IPanelGroupLoader;

import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.gui.panels.WFSAreaPanel;
import com.iver.cit.gvsig.gui.panels.WFSFilterPanel;
import com.iver.cit.gvsig.gui.panels.WFSParamsPanel;
import com.iver.cit.gvsig.gui.panels.WFSSelectFieldsPanel;
import com.iver.cit.gvsig.panelGroup.PanelGroupDialog;

/**
 * <p>Dialog with the properties of a WFS layer.</p>
 * 
 * @version 19/12/2007
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es) 
 */
public class WFSPropertiesDialog extends PanelGroupDialog {
	private static final long serialVersionUID = 3879484158536237827L;

	private WFSPropertiesDialogListener listener;

	/**
	 * <p>Creates a <code>WFSPropertiesDialog</code> using two parameters.</p>
	 * 
	 * @see PanelGroupDialog#PanelGroupDialog(String, AbstractPanelGroup)
	 */
	public WFSPropertiesDialog(String windowTitle, IPanelGroup panelGroup) {
		super("", windowTitle, defaultWidth, defaultHeight, (byte)WindowInfo.MODALDIALOG, panelGroup);
		
		listener = new WFSPropertiesDialogListener(this);
	}

	/**
	 * <p>Creates a <code>WFSPropertiesDialog</code> using five parameters.</p>
	 * 
	 * @see PanelGroupDialog#PanelGroupDialog(String, String, int, int, byte, AbstractPanelGroup)
	 */
	public WFSPropertiesDialog(String additionalInfo, String windowTitle, int width, int height, byte windowInfoProperties,	IPanelGroup panelGroup) {
		super(additionalInfo, windowTitle, width, height, windowInfoProperties,	panelGroup);
		
		listener = new WFSPropertiesDialogListener(this);
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.panelGroup.PanelGroupDialog#accept()
	 */
	public void accept() {
		super.accept();

		listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, WFSPropertiesDialogListener.ACCEPT_BUTTON_ACTION_COMMAND));
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.panelGroup.PanelGroupDialog#apply()
	 */
	public void apply() {
		super.apply();

		listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, WFSPropertiesDialogListener.APPLY_BUTTON_ACTION_COMMAND));
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.panelGroup.PanelGroupDialog#cancel()
	 */
	public void cancel() {
		super.cancel();

		listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, WFSPropertiesDialogListener.CANCEL_BUTTON_ACTION_COMMAND));
	}

	/**
	 * @see WFSParamsPanel#getServerExplorer()
	 */
	public WFSServerExplorer getServerExplorer() {
		return ((WFSParamsPanel)panelGroup).getServerExplorer();
	}

	/**
	 * @see WFSFilterPanel#getQuery()
	 */
	public String getFilterQuery() {
		return ((WFSParamsPanel)panelGroup).getFilterPanel().getQuery();
	}

	/**
	 * @see WFSAreaPanel#setUserHasntDefineAnArea()
	 */
	public void setUserHasntDefinedAnArea() {
		((WFSParamsPanel)panelGroup).getAreaPanel().setUserHasntDefineAnArea();
	}

	/**
	 * @see WFSSelectFieldsPanel#getFieldsSelectedOfSameLayerHasChanged()
	 */
	public boolean getFieldsSelectedOfSameLayerHasChanged() {
		return ((WFSParamsPanel)panelGroup).getFieldsPanel().getFieldsSelectedOfSameLayerHasChanged();
	}

	/**
	 * @see WFSSelectFieldsPanel#resetFieldsSelectedOfSameLayerHasChanged()
	 */
	public void resetFieldsSelectedOfSameLayerHasChanged() {
		((WFSParamsPanel)panelGroup).getFieldsPanel().resetFieldsSelectedOfSameLayerHasChanged();
	}

	/**
	 * @see WFSFilterPanel#updateFieldValues()
	 */
	public void updateWFSFilterFieldValues()  {
		((WFSParamsPanel)panelGroup).getFilterPanel().updateFieldValues();
	}

	/**
	 * @see WFSAreaPanel#updateWFSArea()
	 */
	public void updateWFSArea() {
		((WFSParamsPanel)panelGroup).getAreaPanel().updateWFSArea();
	}

	/**
	 * @see WFSFilterPanel#getAllFieldsAndValuesKnownOfCurrentLayer(Map)
	 */
	public Map getAllFieldsAndValuesKnownOfCurrentLayer() {
		return ((WFSParamsPanel)panelGroup).getFilterPanel().getAllFieldsAndValuesKnownOfCurrentLayer();
	}

	/**
	 * @see WFSFilterPanel#setWFSFilterPanelIsAsTabForWFSLayersLoad(boolean)
	 */
	public void setWFSFilterPanelIsAsTabForWFSLayersLoad(boolean b) {
		((WFSParamsPanel)panelGroup).getFilterPanel().setWFSFilterPanelIsAsTabForWFSLayersLoad(b);
	}

	/**
	 * @see WFSFilterPanel#getWFSFilterPanelIsAsTabForWFSLayersLoad()
	 */
	public boolean getWFSFilterPanelIsAsTabForWFSLayersLoad() {
		return ((WFSParamsPanel)panelGroup).getFilterPanel().getWFSFilterPanelIsAsTabForWFSLayersLoad();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.panelGroup.PanelGroupDialog#loadPanels(org.gvsig.gui.beans.panelGroup.loaders.IPanelGroupLoader)
	 */
	public void loadPanels(IPanelGroupLoader loader) throws ListCouldntAddPanelException, EmptyPanelGroupException,	EmptyPanelGroupGUIException {
		super.loadPanels(loader);
		
		setWFSFilterPanelIsAsTabForWFSLayersLoad(false);
		setEnabledApplyButton(false);
	}
}
