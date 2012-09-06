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

package org.gvsig.gui.beans.panelGroup.loaders;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import org.gvsig.gui.beans.Messages;
import org.gvsig.gui.beans.panelGroup.exceptions.ListCouldntLoadPanelException;
import org.gvsig.gui.beans.panelGroup.exceptions.PanelBaseException;
import org.gvsig.gui.beans.panelGroup.panels.AbstractPanel;
import org.gvsig.gui.beans.panelGroup.panels.IPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Panel loader version that doesn't load the panels, only stores and returns them.</p>
 * <p>This loader is useful to mask the load of panels and eliminate the dependence to the extension
 *  points, and, consequently, the dependence between the <i>libUIComponents</i> project to other
 *  projects.</p>
 * <p>This loader is used together with {@link PanelGroupLoaderUtilities PanelGroupLoaderUtilities} (allocated
 * in other project), that is which really loads the panels. First use <code>PanelGroupLoaderUtilities</code>
 * and after <code>PanelGroupLoaderFromList</code>.</p>
 *
 * @see IPanelGroupLoader
 *
 * @version 15/10/2007
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es)
 */
public class PanelGroupLoaderFromList implements IPanelGroupLoader, Serializable {
	private static final long serialVersionUID = 3066607004928429045L;
	final static private Logger logger = LoggerFactory.getLogger(PanelGroupLoaderFromList.class);

	/**
	 * Array with the panels loaded.
	 *
	 * @see #loadPanels()
	 */
	private Class<IPanel>[] list;

	/**
	 * <p>Initializes this loader with the panels.</p>
	 *
	 * @param list array with the panels that this loader supposedly load
	 */
	public PanelGroupLoaderFromList(Class<IPanel>[] list) {
		this.list = list;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.loaders.IPanelGroupLoader#loadPanels(java.util.ArrayList)
	 */
	public void loadPanels(ArrayList<IPanel> panels) throws ListCouldntLoadPanelException {
		if (list == null) {
			return;
		}

		ListCouldntLoadPanelException lCLPException = null;
		AbstractPanel panel = null;

		for (int i = 0; i< list.length; i++) {
			if (list[i] != null) {
				try {
					panel = null;
					panel = (AbstractPanel) list[i].newInstance();
					panels.add(panel);
				} catch (Exception e) {
					logger.debug(Messages.getText("panel_loading_exception"), e);

					if ( lCLPException == null ) {
						lCLPException = new ListCouldntLoadPanelFromListException();
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
		}

		if ( lCLPException != null ) {
			throw lCLPException;
		}
	}

	/**
	 * <p>Exception produced when fails the load of a panel by a loader of type <code>PanelGroupLoaderFromList</code>.</p>
	 *
	 * @version 27/11/2007
	 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es)
	 */
	public class ListCouldntLoadPanelFromListException extends ListCouldntLoadPanelException {
		private static final long serialVersionUID = -8607556361881436022L;

		private static final String formatString = "Couldn't load some panels from a list of classes:";
		private static final String messageKey = "couldnt_load_panels_from_list_exception";

		/**
		 * <p>Creates an initializes a new instance of <code>ListCouldntLoadPanelFromListException</code>.</p>
		 */
		public ListCouldntLoadPanelFromListException() {
			super();

		}

		protected Map<String, String> values() {
			return null;
		}
	}
}

