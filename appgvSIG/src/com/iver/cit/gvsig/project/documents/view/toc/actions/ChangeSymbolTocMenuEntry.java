package com.iver.cit.gvsig.project.documents.view.toc.actions;

import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.fmap.mapcontext.rendering.legend.*;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.MultiLayerFillSymbol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.gui.styling.SymbolSelector;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.view.legend.gui.ISymbolSelector;
import com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;
import com.iver.cit.gvsig.project.documents.view.toc.TocItemLeaf;
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
 * $Id: ChangeSymbolTocMenuEntry.java 28307 2009-04-30 08:44:36Z nbrodin $
 * $Log$
 * Revision 1.3  2007-09-19 15:50:42  jaume
 * refactor name IVectorialLegend -> IVectorLegend
 *
 * Revision 1.2  2007/09/17 09:22:21  jaume
 * view draw frame rate now customizable
 *
 * Revision 1.1  2007/09/10 15:34:39  jaume
 * improvements on usability (double-clicks on TOC and some minor shorcuts)
 *
 * Revision 1.5  2007/01/04 07:24:31  caballero
 * isModified
 *
 * Revision 1.4  2006/10/02 13:52:34  jaume
 * organize impots
 *
 * Revision 1.3  2006/09/29 07:07:41  caballero
 * llamada a listener
 *
 * Revision 1.2  2006/09/28 15:00:45  fjp
 * Usar siempre que se pueda ISymbol en lugar de FSymbol
 *
 * Revision 1.1  2006/09/15 10:41:30  caballero
 * extensibilidad de documentos
 *
 * Revision 1.1  2006/09/12 15:58:14  jorpiell
 * "Sacadas" las opcines del menú de FPopupMenu
 *
 *
 */
/**
 * Directly opens the Symbol Selector if it is invoked from the TOC avoiding
 * by-passing the Layer Properties window
 */
public class ChangeSymbolTocMenuEntry extends AbstractTocContextMenuAction {
    private static final Logger logger = LoggerFactory
            .getLogger(ChangeSymbolTocMenuEntry.class);

	public String getGroup() {
		return "group1"; //FIXME
	}

	public int getGroupOrder() {
		return 10;
	}

	public int getOrder() {
		return 0;
	}

	public String getText() {
		return PluginServices.getText(this, "change_symbol");
	}

	public boolean isEnabled(ITocItem item, FLayer[] selectedItems) {
		return true;
	}

	public boolean isVisible(ITocItem item, FLayer[] selectedItems) {
		return isTocItemLeaf(item);
	}

	public void execute(ITocItem item, FLayer[] selectedItems) {

		boolean showDialog = isTocItemLeaf(item);

		if (!showDialog) {
			return;
		}

		try {
			if(!(selectedItems[0] instanceof FLyrVect))
				return;
			FLyrVect layer = (FLyrVect) selectedItems[0];
			ISymbol oldSymbol = ((TocItemLeaf) item).getSymbol();
			// patch, figure out an ellegant way to solve this
			if (oldSymbol instanceof MultiLayerFillSymbol) {
				MultiLayerFillSymbol ms = (MultiLayerFillSymbol) oldSymbol;
				for (int i = 0; i < ms.getLayerCount(); i++) {
					if (ms.getLayer(i).getClassName().equals("org.gvsig.symbology.symbols.DotDensityFillSymbol")) {
						/*
						 * since dot density symbol works together with the
						 * legend, there is no way to edit it from the symbol selector and editor
						 * we have to break now!
						 */
						return;
					}
				}
			}
			// end patch


			int shapeType = ((IVectorLegend) layer.getLegend()).getShapeType();
			if (shapeType == 0) {
				logger.debug("Error legend " + layer.getLegend()
                        + " does not have shapetype initialized");
				shapeType = layer.getShapeType();
			}

			ISymbolSelector symSel = null;

			try {
				symSel = SymbolSelector.createSymbolSelector(oldSymbol, shapeType);
			} catch (IllegalArgumentException iaEx) {
				/*
				 * this usually happens when the Legend has a composed
				 * symbol that collides with the normal operation and
				 * it only can be changed from the panel of the legend
				 */
				// TODO throw a signal and show a warning message box telling
				// that the operation cannot be performed
				return;
			}
			PluginServices.getMDIManager().addWindow(symSel);
			ISymbol newSymbol = (ISymbol) symSel.getSelectedObject();

			if (newSymbol == null) {
				return;
			}

			newSymbol.setDescription(oldSymbol.getDescription());

			for (int i = 0; i < selectedItems.length; i++) {
				FLyrVect theLayer = ((FLyrVect) selectedItems[i]);

				try {
					ILegend legend = theLayer.getLegend();
					if (legend instanceof IClassifiedVectorLegend) {
						IClassifiedVectorLegend cv = (IClassifiedVectorLegend) legend;
						cv.replace(oldSymbol, newSymbol);
					} else if (legend instanceof SingleSymbolLegend) {
						SingleSymbolLegend ss = (SingleSymbolLegend) legend;
						ss.setDefaultSymbol(newSymbol);
					}
				} catch (Exception e) {
					NotificationManager.addWarning(PluginServices.getText(this, "skipped_symbol_changed_for_layer")+": "+theLayer.getName(), e);
				}

			}
		     // refresh view treak
			MapContext mc = layer.getMapContext();
			mc.invalidate();
			Project project=((ProjectExtension)PluginServices.getExtension(ProjectExtension.class)).getProject();
			project.setModified(true);
			mc.callLegendChanged();

		} catch (ReadException e) {
			NotificationManager.addError(PluginServices.getText(this, "getting_shape_type"), e);
		}
	}
}

