package org.gvsig.crs.gui.panels.wizard;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.iver.utiles.swing.JComboBox;

public class ComboBoxRenderer extends JComboBox implements TableCellRenderer{


	private static final long serialVersionUID = 1L;

	public ComboBoxRenderer(String[] items)
    {
        super(items);
    }
 
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row, int column)
    {
        if (isSelected)
        {
            setForeground(table.getSelectionForeground());
            super.setBackground(table.getSelectionBackground());
        }
        else
        {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }
        setSelectedItem(value);
        return this;
    }

}
