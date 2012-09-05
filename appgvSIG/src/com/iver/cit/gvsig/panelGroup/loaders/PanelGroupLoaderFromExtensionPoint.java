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

package com.iver.cit.gvsig.panelGroup.loaders;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.gvsig.gui.beans.panelGroup.exceptions.ListCouldntLoadPanelException;
import org.gvsig.gui.beans.panelGroup.exceptions.PanelBaseException;
import org.gvsig.gui.beans.panelGroup.loaders.IPanelGroupLoader;
import org.gvsig.gui.beans.panelGroup.panels.AbstractPanel;
import org.gvsig.gui.beans.panelGroup.panels.IPanel;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;
import org.gvsig.tools.extensionpoint.ExtensionPoint.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;

/**
 * <p>
 * Panel loader version that loads {@link AbstractPanel AbstractPanel} classes
 * registered as a extension point.
 * </p>
 *
 * @see IPanelGroupLoader
 *
 * @version 15/10/2007
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es)
 */
public class PanelGroupLoaderFromExtensionPoint implements IPanelGroupLoader, Serializable {
    private static final Logger logger = LoggerFactory
            .getLogger(PanelGroupLoaderFromExtensionPoint.class);
    
	private static final long serialVersionUID = 6810333457209196344L;

	/**
	 * Extension point id
	 */
	private String id;

	/**
	 * <p>
	 * Initializes this loader.
	 * </p>
	 *
	 * @param id
	 *            extension point identifier
	 */
	public PanelGroupLoaderFromExtensionPoint(String id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.loaders.IPanelGroupLoader#loadPanels(java.util.ArrayList)
	 */
	public void loadPanels(ArrayList<IPanel> panels) throws ListCouldntLoadPanelException {
		if (id == null) {
			return;
		}

		ExtensionPointManager ePManager = ToolsLocator
				.getExtensionPointManager();
		ListCouldntLoadPanelException lCLPException = null;
		Iterator<Extension> iterator = null;

		try {
			if (!ePManager.has(id)) {
				return;
			}

			iterator = ePManager.get(id).iterator();
		} catch (Exception e) {
			logger.debug(PluginServices
                    .getText(this, "panel_loading_exception"), e);

			if ( lCLPException == null ) {
				lCLPException = new ListCouldntLoadPanelFromExtensionPointException();
			}

			lCLPException.add(e);
		}

		AbstractPanel panel = null;

		while (iterator.hasNext()) {
			try {

				panel = (AbstractPanel) ((iterator.next()).create());
				panels.add(panel);
			} catch (Exception e) {
				logger.debug(PluginServices.getText(this,
                        "panel_loading_exception"), e);

				if ( lCLPException == null ) {
					lCLPException = new ListCouldntLoadPanelFromExtensionPointException();
				}

				PanelBaseException bew = null;

				if (panel == null) {
					bew = new PanelBaseException(e, "");
				} else {
					bew = new PanelBaseException(e, panel.getLabel());
				}

				lCLPException.add(bew);
			}
		}

		if ( lCLPException != null ) {
			throw lCLPException;
		}

	}

	/**
	 * <p>
	 * Exception produced when fails the load of a panel by a loader of type
	 * <code>PanelGroupLoaderFromExtensionPoint</code>.
	 * </p>
	 *
	 * @version 27/11/2007
	 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es)
	 */
	public class ListCouldntLoadPanelFromExtensionPointException extends ListCouldntLoadPanelException {
		private static final long serialVersionUID = -1513294728326593385L;

		private static final String formatString = "Couldn't load some panels from an extension point of classes:";
		private static final String messageKey = "couldnt_load_panels_from_extension_point_exception";

		/**
		 * <p>
		 * Creates an initializes a new instance of
		 * <code>ListCouldntLoadPanelFromExtensionPointException</code>.
		 * </p>
		 */
		public ListCouldntLoadPanelFromExtensionPointException() {
			super(formatString, messageKey, serialVersionUID);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.gvsig.tools.exceptions.BaseException#values()
		 */
		protected Map<String, String> values() {
			return null;
		}
	}
}
