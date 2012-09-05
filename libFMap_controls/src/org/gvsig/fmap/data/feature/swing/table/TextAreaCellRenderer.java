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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

/**
 * Renders a text value into a text area.
 * <p>
 * Taken and adapted from the Article <strong>"Multi-line cells in JTable in JDK
 * 1.4+"</strong> by Dr. Heinz M. Kabutz:
 * </p>
 * <p>
 * http://www.javaspecialists.eu/archive/Issue106.html
 * </p>
 * 
 * @author <a href="mailto:cordin@disid.com">C�sar Ordi�ana</a>
 */
public class TextAreaCellRenderer extends JTextArea implements TableCellRenderer {

    private static final long serialVersionUID = 3269365116036190589L;

    private final DefaultTableCellRenderer adaptee = new DefaultTableCellRenderer();
    /** map from table to map of rows to map of column heights */
    private final Map<JTable, Map<Integer, Map<Integer, Integer>>> cellSizes = new HashMap<JTable, Map<Integer, Map<Integer, Integer>>>();
    
    private int maxTxtLength = Integer.MAX_VALUE;
    
    private int maxRowHeight = Integer.MAX_VALUE;

    /**
     * Creates a new TextAreaRenderer.
     */
    public TextAreaCellRenderer() {
        this(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    /**
     * Creates a new TextAreaRenderer.
     * 
     * @param maxTXTLength
     *            the maximum text length to be rendered.
     * @param maxRowHeight
     *            the maximum row height for the rows with cells rendered with
     *            this component
     */
    public TextAreaCellRenderer(int maxTxtLength, int maxRowHeight) {
        setLineWrap(true);
        setWrapStyleWord(true);
        this.maxTxtLength = maxTxtLength;
        this.maxRowHeight = maxRowHeight;
    }

    public Component getTableCellRendererComponent(
            JTable table, Object obj, boolean isSelected, boolean hasFocus,
            int row, int column) {
        // set the colours, etc. using the standard for that platform
        adaptee.getTableCellRendererComponent(table, obj, isSelected, hasFocus,
                row, column);
        setForeground(adaptee.getForeground());
        setBackground(adaptee.getBackground());
        setBorder(adaptee.getBorder());
        setFont(adaptee.getFont());

        String cellTxt = getCellText(obj, row, column);
        if (cellTxt.length() > maxTxtLength) {
            cellTxt = cellTxt.substring(0, maxTxtLength - 4).concat(" ...");
        }

        setText(cellTxt);

        // This line was very important to get it working with JDK1.4
        TableColumnModel columnModel = table.getColumnModel();
        setSize(columnModel.getColumn(column).getWidth(), 100000);
        int height_wanted = (int) getPreferredSize().getHeight();
        
        height_wanted = height_wanted > maxRowHeight ? maxRowHeight
                : height_wanted;
        
        addSize(table, row, column, height_wanted);
        height_wanted = findTotalMaximumRowSize(table, row);
        if (height_wanted != table.getRowHeight(row)) {
            table.setRowHeight(row, height_wanted);
        }
        return this;
    }

    /**
     * Returns the text for the cell.
     * 
     * @param value
     *            the cell object value
     * @return the text to render on the cell
     */
    protected String getCellText(Object value, int row, int column) {
        return adaptee.getText();
    }

    private void addSize(JTable table, int row, int column, int height) {
        Map<Integer, Map<Integer, Integer>> rows = cellSizes.get(table);
        if (rows == null) {
            cellSizes.put(table,
                    rows = new HashMap<Integer, Map<Integer, Integer>>());
        }
        Map<Integer, Integer> rowheights = rows.get(new Integer(row));
        if (rowheights == null) {
            rows.put(new Integer(row),
                    rowheights = new HashMap<Integer, Integer>());
        }
        rowheights.put(new Integer(column), new Integer(height));
    }

    /**
     * Look through all columns and get the renderer. If it is also a
     * TextAreaRenderer, we look at the maximum height in its hash table for
     * this row.
     */
    private int findTotalMaximumRowSize(JTable table, int row) {
        int maximum_height = 0;
        for (int column = 0; column < table.getColumnCount(); column++) {
            TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
            if (cellRenderer instanceof TextAreaCellRenderer) {
                TextAreaCellRenderer tar = (TextAreaCellRenderer) cellRenderer;
                maximum_height = Math.max(maximum_height, tar
                        .findMaximumRowSize(table, row));
            }
        }
        return maximum_height;
    }

    private int findMaximumRowSize(JTable table, int row) {
        Map<Integer, Map<Integer, Integer>> rows = cellSizes.get(table);
        if (rows == null)
            return 0;
        Map<Integer, Integer> rowheights = rows.get(new Integer(row));
        if (rowheights == null)
            return 0;
        int maximum_height = 0;
        for (Iterator<Map.Entry<Integer, Integer>> it = rowheights.entrySet()
                .iterator(); it.hasNext();) {
            Map.Entry<Integer, Integer> entry = it.next();
            int cellHeight = ((Integer) entry.getValue()).intValue();
            maximum_height = Math.max(maximum_height, cellHeight);
        }
        return maximum_height;
    }

}