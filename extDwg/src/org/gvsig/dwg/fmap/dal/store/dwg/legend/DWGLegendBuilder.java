/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Government (CIT)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 */

/*
* AUTHORS (In addition to CIT):
* 2008 IVER T.I. S.A.   {{Task}}
*/

package org.gvsig.dwg.fmap.dal.store.dwg.legend;

import java.awt.Color;

import org.cresques.px.dxf.AcadColor;
import org.gvsig.dwg.fmap.dal.store.dwg.DWGStoreProvider;
import org.gvsig.dwg.fmap.dal.store.dwg.LegendBuilder;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.feature.spi.FeatureProvider;
import org.gvsig.fmap.dal.feature.spi.FeatureStoreProvider;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.mapcontext.rendering.legend.LegendFactory;
import org.gvsig.fmap.mapcontext.rendering.legend.VectorialUniqueValueLegend;
import org.gvsig.fmap.mapcontext.rendering.legend.styling.AttrInTableLabelingStrategy;
import org.gvsig.fmap.mapcontext.rendering.symbols.IFillSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbologyFactory;

public class DWGLegendBuilder implements LegendBuilder {


	private VectorialUniqueValueLegend defaultLegend = null;
	private AttrInTableLabelingStrategy labelingStragegy = null;

	public void begin() {
		// Nothing to do
	}

	public void end() {
		// Nothing to do
	}

	public Object getLegend() {
		return defaultLegend;
	}

	public LegendBuilder initialize(FeatureStoreProvider store) {
		defaultLegend = LegendFactory
				.createVectorialUniqueValueLegend(Geometry.TYPES.GEOMETRY);
		defaultLegend
				.setClassifyingFieldNames(new String[] { DWGStoreProvider.NAME_FIELD_COLOR });
		defaultLegend.setClassifyingFieldTypes(new int[] { DataTypes.INT });

		ISymbol myDefaultSymbol = SymbologyFactory
				.createDefaultSymbolByShapeType(Geometry.TYPES.GEOMETRY,
						Color.BLACK);

		defaultLegend.setDefaultSymbol(myDefaultSymbol);


		labelingStragegy = new AttrInTableLabelingStrategy();
		labelingStragegy.setTextField(DWGStoreProvider.NAME_FIELD_TEXT);
		labelingStragegy
				.setRotationField(DWGStoreProvider.NAME_FIELD_ROTATIONTEXT);
		labelingStragegy.setHeightField(DWGStoreProvider.NAME_FIELD_HEIGHTTEXT);
		labelingStragegy.setUnit(1); // MapContext.NAMES[1] (meters)
		return this;
	}

	public void process(FeatureProvider feature) {
		Integer clave = (Integer) feature.get("Color");
		if (clave == null) {
			return;
		}
		if (defaultLegend.getSymbolByValue(clave) == null) {
			ISymbol theSymbol;
			Color color = null;
			try {
				color = AcadColor.getColor(clave.intValue());
			} catch (ArrayIndexOutOfBoundsException e) {
				color = AcadColor.getColor(255);
			}
			// jaume, moved to ISymbol
			theSymbol = SymbologyFactory.createDefaultSymbolByShapeType(
					Geometry.TYPES.GEOMETRY, color);
			theSymbol.setDescription(clave.toString());
			// Asigna los colores de Autocad a los
			// bordes
			// de los polígonos.
			if (theSymbol instanceof IFillSymbol) {
				((IFillSymbol) theSymbol).getOutline().setLineColor(color);

			}
			if (theSymbol != null) {
				defaultLegend.addSymbol(clave, theSymbol);
			}
		}

	}

	public Object getLabeling() {
		return labelingStragegy;
	}


}
