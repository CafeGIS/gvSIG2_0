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
 * 2008 {DiSiD Technologies}  {{Task}}
 */
package org.gvsig.fmap.data.feature.swing.table;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.EventObject;

import javax.swing.DefaultCellEditor;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.operation.fromwkt.FromWKT;
import org.gvsig.fmap.geom.operation.fromwkt.FromWKTGeometryOperationContext;
import org.gvsig.fmap.geom.operation.towkt.ToWKT;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.gvsig.i18n.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Editor for cells of type Geometry in WKT format.
 * <p>
 * If the WKT to represent a Geometry is too big, editing is not allowed, as the
 * rendering of that big text is too slow.
 * </p>
 * 
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public class GeometryWKTCellEditor extends TextAreaCellEditor {
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private static final Logger logger = LoggerFactory.getLogger(GeometryWKTCellEditor.class);
    public static final int DEFAULT_MAX_WKT_LENGTH = 10000;

    private static final long serialVersionUID = -2296004227902843851L;
    private Geometry nullGeometry = null;
    
    private int maxRowHeight;

    /**
     * Creates a new editor for Geometries in WKT format.
     */
    public GeometryWKTCellEditor() {
        this(DEFAULT_MAX_WKT_LENGTH, 160);
    }

    /**
     * Creates a new editor for Geometries in WKT format.
     * 
     * @param maxWKTLength
     *            the maximum WTK length allowed to be edited.
     * @param maxRowHeight
     *            the maximum row height for the rows with cells rendered with
     *            this component
     */
    public GeometryWKTCellEditor(int maxWKTLength, int maxRowHeight) {
        super();
        delegate = new GeometryToWKTDelegate(delegate, maxWKTLength);
        this.maxRowHeight = maxRowHeight;
        try {
			nullGeometry = geomManager.createNullGeometry(SUBTYPES.GEOM2D);
		} catch (CreateGeometryException e) {
			logger.error("Error creating a null geometry", e);
		}
    }

    @Override
    public Object getCellEditorValue() {
        String wkt = (String) super.getCellEditorValue();
        FromWKTGeometryOperationContext context = new FromWKTGeometryOperationContext(
                wkt, null);

        try {
            return nullGeometry.invokeOperation(FromWKT.CODE, context);
        } catch (Exception ex) {
            throw new WKTToGeometryException(wkt, ex);
        }
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        delegate.setValue(value);
        if (((GeometryToWKTDelegate) delegate).isWtkTextTooLong()) {
            JOptionPane
                    .showMessageDialog(
                            table.getParent(),
                            Messages
                    .getText("Geometria_no_editable_WKT"), Messages
                    .getText("Error_editar_geometria"),
                            JOptionPane.ERROR_MESSAGE);

            return null;
        } else {
            int height_wanted = (int) getTextArea().getPreferredSize()
                    .getHeight();

            height_wanted = height_wanted > maxRowHeight ? maxRowHeight
                    : height_wanted;

            if (height_wanted > table.getRowHeight(row)) {
                table.setRowHeight(row, height_wanted);
            }

            return editorComponent;
        }
    }

    @SuppressWarnings("serial")
    private class GeometryToWKTDelegate extends
            DefaultCellEditor.EditorDelegate {
        private DefaultCellEditor.EditorDelegate delegate;

        private boolean wtkTextTooLong = false;

        private int maxWKTLength;

        public GeometryToWKTDelegate(DefaultCellEditor.EditorDelegate delegate,
                int maxWKTLength) {
            this.delegate = delegate;
            this.maxWKTLength = maxWKTLength;
        }

        /**
         * @return the wtkTextTooLong
         */
        public boolean isWtkTextTooLong() {
            return wtkTextTooLong;
        }

        public void setValue(Object value) {
            wtkTextTooLong = false;
            String strValue = "";

            if (value != null) {
                try {
                    Geometry geometry = (Geometry) value;
                    strValue = (String) geometry.invokeOperation(ToWKT.CODE,
                            null);

                    if (strValue.length() > maxWKTLength) {
                        wtkTextTooLong = true;
                        delegate.setValue(null);
                    } else {
                        delegate.setValue(strValue);
                    }
                } catch (Exception ex) {
                    throw new GeometryToWKTException(ex);
                }
            }
        }

        public Object getCellEditorValue() {
            if (wtkTextTooLong) {
                return null;
            } else {
                return delegate.getCellEditorValue();
            }
        }

        public void actionPerformed(ActionEvent e) {
            delegate.actionPerformed(e);
        }

        public void cancelCellEditing() {
            delegate.cancelCellEditing();
        }

        public boolean equals(Object obj) {
            return delegate.equals(obj);
        }

        public int hashCode() {
            return delegate.hashCode();
        }

        public boolean isCellEditable(EventObject anEvent) {
            return delegate.isCellEditable(anEvent);
        }

        public void itemStateChanged(ItemEvent e) {
            delegate.itemStateChanged(e);
        }

        public boolean shouldSelectCell(EventObject anEvent) {
            return delegate.shouldSelectCell(anEvent);
        }

        public boolean startCellEditing(EventObject anEvent) {
            return delegate.startCellEditing(anEvent);
        }

        public boolean stopCellEditing() {
            return delegate.stopCellEditing();
        }

        public String toString() {
            return delegate.toString();
        }
    }
}