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
package com.iver.cit.gvsig.gui.styling;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.mapcontext.rendering.symbols.IFillSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.ILineSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.IMarkerSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.MultiShapeSymbol;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.view.legend.gui.ISymbolSelector;

/**
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */
public class MultiShapeSymbolSelector extends JPanel implements ISymbolSelector {
	private SymbolSelector markerSelector;
	private SymbolSelector lineSelector;
	private SymbolSelector fillSelector;
	private WindowInfo wi;
	private JTabbedPane tabbedPane;

	MultiShapeSymbolSelector(Object currSymbol) {
		MultiShapeSymbol sym = (MultiShapeSymbol) currSymbol;
		markerSelector = (SymbolSelector) SymbolSelector.
							createSymbolSelector(
									sym.getMarkerSymbol(), Geometry.TYPES.POINT);
		lineSelector = (SymbolSelector) SymbolSelector.
							createSymbolSelector(
									sym.getLineSymbol(), Geometry.TYPES.CURVE);
		fillSelector = (SymbolSelector) SymbolSelector.
							createSymbolSelector(
									sym.getFillSymbol(), Geometry.TYPES.SURFACE);
		initialize();
	}


	private void initialize() {
		setLayout(new BorderLayout());
		add(getJTabbedPane(), BorderLayout.CENTER);
	}


	private JTabbedPane getJTabbedPane() {
		if (tabbedPane == null) {
			tabbedPane = new JTabbedPane();
			tabbedPane.addTab(PluginServices.getText(this, "marker"), markerSelector);
			tabbedPane.addTab(PluginServices.getText(this, "line"), lineSelector);
			tabbedPane.addTab(PluginServices.getText(this, "fill"), fillSelector);
			tabbedPane.setPreferredSize(getWindowInfo().getMinimumSize());
		}

		return tabbedPane;
	}


	public Object getSelectedObject() {
		MultiShapeSymbol sym = new MultiShapeSymbol();
		sym.setMarkerSymbol((IMarkerSymbol) markerSelector.getSelectedObject());
		sym.setLineSymbol((ILineSymbol) lineSelector.getSelectedObject());
		sym.setFillSymbol((IFillSymbol) fillSelector.getSelectedObject());
		return sym;
	}

	public void setSymbol(Object symbol) {
		MultiShapeSymbol sym = (MultiShapeSymbol) symbol;
		markerSelector.setSymbol(sym.getMarkerSymbol());
		lineSelector.setSymbol(sym.getLineSymbol());
		fillSelector.setSymbol(sym.getFillSymbol());
	}

	public WindowInfo getWindowInfo() {
		if (wi == null) {
			wi = new WindowInfo(WindowInfo.MODALDIALOG | WindowInfo.RESIZABLE);
			wi.setWidth(706);
			wi.setHeight(500);
			wi.setTitle(PluginServices.getText(this, "symbol_selector"));
		}
		return wi;
	}

	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}
}
