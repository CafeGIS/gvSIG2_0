package org.gvsig.crs.gui.panels.wizard;

import javax.swing.DefaultCellEditor;

import com.iver.utiles.swing.JComboBox;

public class ComboBoxEditor extends DefaultCellEditor {

	private static final long serialVersionUID = 1L;

	public ComboBoxEditor(String[] items)
    {
        super(new JComboBox(items));
    }
}
