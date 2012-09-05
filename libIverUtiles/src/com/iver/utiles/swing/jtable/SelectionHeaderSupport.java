package com.iver.utiles.swing.jtable;

import java.awt.Component;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.BitSet;

import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

/**
 * @author Fernando González Cortés
 */
public class SelectionHeaderSupport {
    private SelectionCellRenderer cellRenderer = new SelectionCellRenderer();
    private FieldSelectionListenerSupport listenerSupport = new FieldSelectionListenerSupport();

    public void setTableHeader(final JTableHeader header){
		header.setDefaultRenderer(cellRenderer);
		header.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                for (int i = 0; i < header.getColumnModel().getColumnCount(); i++) {
                    if (header.getHeaderRect(i).contains(e.getPoint())){
                        if ((e.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) == MouseEvent.CTRL_DOWN_MASK){
                            cellRenderer.toggleSelection(i);
                        }else{
                            cellRenderer.setSelection(i);
                        }
                        
                        FieldSelectionEvent evt = new FieldSelectionEvent();
                        evt.setSelectedField(i);
                        evt.setSelectedIndices(cellRenderer.selectedColumnIndices);
                        callFieldSelected(evt);
                        break;
                    }
                }
            }
        });
    }
    
    public BitSet getSelectedColumns(){
        return cellRenderer.selectedColumnIndices;
    }

    /**
     * Deja todas las columnas sin seleccionar.
     */
    public void clearSelectedColumns()
    {
        cellRenderer.selectedColumnIndices.clear();
    }
    public class SelectionCellRenderer extends JToggleButton implements TableCellRenderer {
        
        private BitSet selectedColumnIndices = new BitSet();
        
        public SelectionCellRenderer(){
            this.setMargin(new Insets(0, 0, 0, 0));
        }
        
        /**
         * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
         */
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            this.setText(table.getModel().getColumnName(column));
            this.setSelected(false);
            for (int i = 0; i < selectedColumnIndices.cardinality(); i++) {
                if (selectedColumnIndices.get(column)){
                    this.setSelected(true);
                }
            }
            return this;
        }
    
        /**
         * @param i
         */
        public void setSelection(int i) {
            selectedColumnIndices.clear();
            selectedColumnIndices.set(i);
        }
        
        public void toggleSelection(int i) {
            selectedColumnIndices.flip(i);
        }
        
    }
    public void addFieldSelectionListener(FieldSelectionListener listener) {
        listenerSupport.addFieldSelectionListener(listener);
    }
    private void callFieldSelected(FieldSelectionEvent arg0) {
        listenerSupport.callFieldSelected(arg0);
    }
    public void removeFieldSelectionListener(FieldSelectionListener listener) {
        listenerSupport.removeFieldSelectionListener(listener);
    }
}
