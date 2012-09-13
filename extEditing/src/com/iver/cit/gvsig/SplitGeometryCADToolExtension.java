/*
 * Created on 10-abr-2006
 *
 * gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
* $Id:
* $Log:
*/
package com.iver.cit.gvsig;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.fmap.mapcontrol.MapControl;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.gui.cad.tools.SplitGeometryCADTool;
import com.iver.cit.gvsig.project.documents.view.gui.View;

/**
 * CAD extension to split geometries from a digitized linear geometry.
 *
 * @author Alvaro Zabala
 *
 */
public class SplitGeometryCADToolExtension extends Extension {

	private View view;
	private MapControl mapControl;
	private SplitGeometryCADTool cadTool;


	public void execute(String actionCommand) {
		CADExtension.initFocus();
		if (actionCommand.equals(SplitGeometryCADTool.SPLIT_GEOMETRY_TOOL_NAME)) {
        	CADExtension.setCADTool(SplitGeometryCADTool.SPLIT_GEOMETRY_TOOL_NAME, true);
        }
		CADExtension.getEditionManager().setMapControl(mapControl);
		CADExtension.getCADToolAdapter().configureMenu();
	}

	public void initialize() {
		cadTool = new SplitGeometryCADTool();
		CADExtension.addCADTool(SplitGeometryCADTool.SPLIT_GEOMETRY_TOOL_NAME, cadTool);
		registerIcons();
	}

	private void registerIcons(){
		PluginServices.getIconTheme().registerDefault("split-geometry",
													   this.getClass().
													   getClassLoader().
													   getResource("images/split-poly.png")
		);
	}

	/**
	 * Returns if this Edit CAD tool is visible.
	 * For this, there must be an active vectorial editing lyr in the TOC, which geometries'
	 * dimension would must be linear or polygonal, and with at least one selected geometry.
	 *
	 */
	public boolean isEnabled() {
		try {
			if (EditionUtilities.getEditionStatus() ==
				EditionUtilities.EDITION_STATUS_ONE_VECTORIAL_LAYER_ACTIVE_AND_EDITABLE) {
					this.view = (View) PluginServices.getMDIManager().getActiveWindow();
					mapControl = view.getMapControl();
					if (CADExtension.getEditionManager().getActiveLayerEdited() == null)
						return false;
					FLyrVect lv = (FLyrVect) CADExtension.
											getEditionManager().
											getActiveLayerEdited().
											getLayer();
					int geometryDimensions = getDimensions(lv.getShapeType());
					if(geometryDimensions <= 0)
						return false;

					return !lv.getFeatureStore().getFeatureSelection().isEmpty();
			}
		} catch (DataException e) {
			NotificationManager.addError(e.getMessage(),e);
			return false;
		}
		return true;
	}
	private static int getDimensions(int shapeType) {
		switch (shapeType) {
		case Geometry.TYPES.ARC:
		case Geometry.TYPES.CURVE:
			return 1;

		case Geometry.TYPES.CIRCLE:
		case Geometry.TYPES.ELLIPSE:
		case Geometry.TYPES.SURFACE:
		case Geometry.TYPES.GEOMETRY:
			return 2;

		case Geometry.TYPES.MULTIPOINT:
		case Geometry.TYPES.POINT:
		case Geometry.TYPES.TEXT:
			return 0;
		default:
			return -1;
		}
	}

	public boolean isVisible() {
		if (EditionUtilities.getEditionStatus() == EditionUtilities.EDITION_STATUS_ONE_VECTORIAL_LAYER_ACTIVE_AND_EDITABLE)
			return true;
		return false;
	}
}
