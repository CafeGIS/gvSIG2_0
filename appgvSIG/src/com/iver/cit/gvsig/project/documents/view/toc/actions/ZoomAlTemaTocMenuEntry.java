package com.iver.cit.gvsig.project.documents.view.toc.actions;

import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateEnvelopeException;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.gui.styling.SymbolSelector;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;

/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
/* CVS MESSAGES:
 *
 * $Id: ZoomAlTemaTocMenuEntry.java 27419 2009-03-16 15:55:32Z jpiera $
 * $Log$
 * Revision 1.6  2007-09-19 15:52:16  jaume
 * ReadExpansionFileException removed from this context
 *
 * Revision 1.5  2007/03/06 16:37:08  caballero
 * Exceptions
 *
 * Revision 1.4  2007/01/04 07:24:31  caballero
 * isModified
 *
 * Revision 1.3  2006/10/18 16:01:13  sbayarri
 * Added zoomToExtent method to MapContext.
 *
 * Revision 1.2  2006/10/02 13:52:34  jaume
 * organize impots
 *
 * Revision 1.1  2006/09/15 10:41:30  caballero
 * extensibilidad de documentos
 *
 * Revision 1.1  2006/09/12 15:58:14  jorpiell
 * "Sacadas" las opcines del menú de FPopupMenu
 *
 *
 */
public class ZoomAlTemaTocMenuEntry extends AbstractTocContextMenuAction {
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private static final Logger logger = LoggerFactory.getLogger(ZoomAlTemaTocMenuEntry.class);
	
	public String getGroup() {
		return "group2"; //FIXME
	}

	public int getGroupOrder() {
		return 20;
	}

	public int getOrder() {
		return 1;
	}

	public String getText() {
		return PluginServices.getText(this, "Zoom_a_la_capa");
	}

	public boolean isEnabled(ITocItem item, FLayer[] selectedItems) {
		return true;
	}

	public boolean isVisible(ITocItem item, FLayer[] selectedItems) {
		if (isTocItemBranch(item) && ! (selectedItems == null || selectedItems.length <= 0)) {
			return true;
		}
		return false;

	}


	public void execute(ITocItem item, FLayer[] selectedItems) {


		// 050209, jmorell: Para que haga un zoom a un grupo de capas seleccionadas.

		if (selectedItems.length==1) {
	        try {
	        	if (!selectedItems[0].isAvailable()) {
					return;
				}
	        	getMapContext().zoomToEnvelope(selectedItems[0].getFullEnvelope());
			} catch (ReadException e1) {
				e1.printStackTrace();
			}
		} else {
			try {
				Envelope maxExtent = setMaxExtent(selectedItems);
				getMapContext().zoomToEnvelope(maxExtent);
			} catch (ReadException e1) {
				e1.printStackTrace();
			}
		}
		Project project=((ProjectExtension)PluginServices.getExtension(ProjectExtension.class)).getProject();
		project.setModified(true);
	}

	private Envelope setMaxExtent(FLayer[] actives)
			throws ReadException {
		Envelope extRef = null;
			extRef = actives[0].getFullEnvelope();

			double minXRef = extRef.getMinimum(0);
			double maxYRef = extRef.getMaximum(1);
			double maxXRef = extRef.getMaximum(0);
			double minYRef = extRef.getMinimum(1);
			for (int i = 0; i < actives.length; i++) {
				if (actives[i].isAvailable()) {
					Envelope extVar = actives[i].getFullEnvelope();
					double minXVar = extVar.getMinimum(0);
					double maxYVar = extVar.getMaximum(1);
					double maxXVar = extVar.getMaximum(0);
					double minYVar = extVar.getMinimum(1);
					if (minXVar <= minXRef) {
						minXRef = minXVar;
					}
					if (maxYVar >= maxYRef) {
						maxYRef = maxYVar;
					}
					if (maxXVar >= maxXRef) {
						maxXRef = maxXVar;
					}
					if (minYVar <= minYRef) {
						minYRef = minYVar;
					}
					try {
						extRef = geomManager.createEnvelope(minXRef, minYRef, maxXRef, maxYRef, SUBTYPES.GEOM2D);
					} catch (CreateEnvelopeException e) {
						logger.error("Error creating the envelope", e);
					}
				}
			}
		return extRef;
	}
}
