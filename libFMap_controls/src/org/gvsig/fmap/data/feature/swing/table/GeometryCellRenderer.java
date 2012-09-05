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

import javax.swing.table.DefaultTableCellRenderer;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.operation.GeometryOperationNotSupportedException;
import org.gvsig.fmap.geom.operation.towkt.ToWKT;

/**
 * Simple renderer for cells of type {@link Geometry} .
 * 
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public class GeometryCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = -2470239399517077869L;

    protected void setValue(Object value) {
        if (value == null) {
            setText("");
        } else {
            Geometry geometry = (Geometry) value;
            try {
                String geomTxt = (String) geometry.invokeOperation(ToWKT.CODE,
                        null);
                setText(geomTxt);
            } catch (GeometryOperationNotSupportedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (GeometryOperationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            setToolTipText(geometry.getGeometryType().getName());
        }
    }
}