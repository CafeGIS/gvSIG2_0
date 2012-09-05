/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Gobernment (CIT)
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
 * 2008 {DiSiD Technologies}  {Create a JTable TableModel for a FeatureCollection}
 */
package org.gvsig.fmap.data.feature.swing.table;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.operation.towkt.ToWKT;

/**
 * Renderer for cells of type {@link Geometry}, showing it in WKT format.
 * 
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public class GeometryWKTCellRenderer extends TextAreaCellRenderer {

    private static final long serialVersionUID = -2470239399517077869L;
    
    public static final int DEFAULT_MAX_WKT_LENGTH = 800;
    
    public static final int DEFAULT_MAX_ROW_HEIGHT = 80;

    /**
     * Creates a new GeometryWKTCellRenderer, with the default MAX LENGTH fot
     * the WKT Strings and the default MAX HEIGHT for rows.
     */
    public GeometryWKTCellRenderer() {
        this(DEFAULT_MAX_WKT_LENGTH, DEFAULT_MAX_ROW_HEIGHT);
    }

    /**
     * Creates a new GeometryWKTCellRenderer.
     * 
     * @param maxWKTLength
     *            the maximum WTK length to be rendered.
     * @param maxRowHeight
     *            the maximum row height for the rows with cells rendered with
     *            this component
     */
    public GeometryWKTCellRenderer(int maxWKTLength, int maxRowHeight) {
        super(maxWKTLength, maxRowHeight);
    }

    @Override
    protected String getCellText(Object value, int row, int column) {
        if (value != null) {
            try {
                Geometry geometry = (Geometry) value;
                String geomTxt = (String) geometry.invokeOperation(ToWKT.CODE,
                        null);
                if (geomTxt.length() > 200) {
                    geomTxt = geomTxt.substring(0, 200);
                }

                return geomTxt;
            } catch (Exception ex) {
                throw new GeometryToWKTException(ex);
            }
        }
        return "";
    }
}