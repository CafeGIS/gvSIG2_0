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

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.BitSet;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import org.gvsig.fmap.data.feature.swing.FeatureTable;
import org.gvsig.fmap.data.feature.swing.table.notification.ColumnHeaderSelectionChangeNotification;
import org.gvsig.tools.observer.impl.BaseWeakReferencingObservable;

/**
 * A header cell renderer for JTables, which allows to select column headers by
 * rendering a JToggleButton on each header cell.
 *
 * When the selection of column headers changes, Observers are notified through
 * a {@link ColumnHeaderSelectionChangeNotification}.
 *
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public class JToggleButtonHeaderCellRenderer extends
        BaseWeakReferencingObservable implements TableCellRenderer,
        MouseListener {

    private BitSet selectedColumns = new BitSet();

    // Component to render on each header cell
    private JToggleButton button;

    private FeatureTable table;

    /**
     * Create a new JToggleButtonCellRenderer for a JTable.
     */
    public JToggleButtonHeaderCellRenderer(FeatureTable table) {
        this.table = table;
        JTableHeader header = table.getTableHeader();
        header.addMouseListener(this);
        button = new JToggleButton();

        // Clone renderization of the default table header
        button.setBackground(header.getBackground());
        button.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, table
                .getGridColor()));
        button.setFont(header.getFont());
        button.setForeground(header.getForeground());
        button.setMargin(new Insets(0, 0, 0, 0));
    }

    /**
     * Returns the selected columns table model position.
     *
     * @return an array of selected column indices
     */
    public int[] getSelectedColumns() {
        int[] columns = new int[selectedColumns.cardinality()];
        int pos = 0;
        for (int i = selectedColumns.nextSetBit(0); i >= 0; i = selectedColumns
                .nextSetBit(i + 1)) {
            columns[pos] = i;
            pos++;
        }
        return columns;
    }
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        // Get the the model position of the column being rendered
        int columnModel = table.convertColumnIndexToModel(column);

        // Set the column header text and selected status
        button.setText(table.getColumnName(column));
        button.setSelected(selectedColumns.get(columnModel));
        if (selectedColumns.get(columnModel)){
        	button.setBackground(Color.gray);
        }else{
        	button.setBackground(Color.lightGray);
        }
        return button;
    }

    public void deselectAll() {
        selectedColumns.clear();
        notifyObservers(new ColumnHeaderSelectionChangeNotification(table));
    }

    public void mouseClicked(MouseEvent event) {
        // Look for the clicked column
        JTable jtable = ((JTableHeader) event.getSource()).getTable();
        if (jtable.equals(table)) {
            int columnViewIndex = table.columnAtPoint(event.getPoint());
            int columnModelIndex = table
                    .convertColumnIndexToModel(columnViewIndex);

            // Set or add the selected column, depending on the CTRL key being
            // pressed or not
            if (ctrlKeyPressed(event)) {
                reverseSelection(columnModelIndex);
            } else {
                setSelection(columnModelIndex);
            }
        }
    }

    public void mouseEntered(MouseEvent e) {
        // Nothing to do
    }

    public void mouseExited(MouseEvent e) {
        // Nothing to do
    }

    public void mousePressed(MouseEvent e) {
        // Nothing to do
    }

    public void mouseReleased(MouseEvent e) {
        // Nothing to do
    }

    private boolean ctrlKeyPressed(MouseEvent event) {
        final int ctrlDownMask = MouseEvent.CTRL_DOWN_MASK;
        return (event.getModifiersEx() & ctrlDownMask) == ctrlDownMask;
    }

    /**
     * Sets the selection to only the given column.
     */
    private void setSelection(int column) {
        selectedColumns.clear();
        selectedColumns.set(column);

        notifyObservers(new ColumnHeaderSelectionChangeNotification(table));
    }

    /**
     * Reverses the selection of a column.
     */
    private void reverseSelection(int column) {
        selectedColumns.flip(column);

        notifyObservers(new ColumnHeaderSelectionChangeNotification(table));
    }
}