package com.iver.utiles.swing.jtable;

import java.util.BitSet;

/**
 * @author Fernando González Cortés
 */
public class FieldSelectionEvent {
    private BitSet selectedIndices;
    private int selectedField;
    public int getSelectedField() {
        return selectedField;
    }
    public void setSelectedField(int selectedField) {
        this.selectedField = selectedField;
    }
    public BitSet getSelectedIndices() {
        return selectedIndices;
    }
    public void setSelectedIndices(BitSet selectedIndices) {
        this.selectedIndices = selectedIndices;
    }
}
