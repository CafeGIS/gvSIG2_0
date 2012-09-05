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

import javax.swing.*;

/**
 * <p>
 * Taken and adapted from the Article <strong>"Multi-line cells in JTable in JDK
 * 1.4+"</strong> by Dr. Heinz M. Kabutz:
 * </p>
 * <p>
 * http://www.javaspecialists.eu/archive/Issue106.html
 * </p>
 * 
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public class TextAreaCellEditor extends DefaultCellEditor {

    private static final long serialVersionUID = -5614806192749314509L;

    private final JTextArea textArea;

    @SuppressWarnings("serial")
    public TextAreaCellEditor() {
        super(new JTextField());
        textArea = new JTextArea();
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(null);
        editorComponent = scrollPane;

        delegate = new DefaultCellEditor.EditorDelegate() {
            public void setValue(Object value) {
                textArea.setText((value != null) ? value.toString() : "");
            }

            public Object getCellEditorValue() {
                return textArea.getText();
            }
        };
    }
    
    public JTextArea getTextArea() {
        return textArea;
    }
}