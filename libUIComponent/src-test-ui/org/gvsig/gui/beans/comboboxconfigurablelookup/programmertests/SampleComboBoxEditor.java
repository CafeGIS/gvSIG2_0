/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2007 IVER T.I. and Generalitat Valenciana.
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 */

package org.gvsig.gui.beans.comboboxconfigurablelookup.programmertests;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.lang.reflect.Method;

import javax.swing.ComboBoxEditor;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicComboBoxEditor;

import org.gvsig.gui.beans.editabletextcomponent.JEditableTextField;

/**
 * <p>The default editor for editable combo boxes. The editor is implemented as a JTextField.</p>
 *
 * @version 12/02/08
 * @author Arnaud Weber
 * @author Mark Davidson
 * @author Pablo Piqueras Bartolomé
 */
public class SampleComboBoxEditor implements ComboBoxEditor, FocusListener {
    protected JTextField editor;
    private Object oldValue;

    public SampleComboBoxEditor() {
        editor = new BorderlessTextField("",9);
        editor.setBorder(null);
    }

    public Component getEditorComponent() {
        return editor;
    }

    /** 
     * Sets the item that should be edited. 
     *
     * @param anObject the displayed value of the editor
     */
    public void setItem(Object anObject) {
        if ( anObject != null )  {
            editor.setText(anObject.toString());
            
            oldValue = anObject;
        } else {
            editor.setText("");
        }
    }

    public Object getItem() {
        Object newValue = editor.getText();
        
        if (oldValue != null && !(oldValue instanceof String))  {
            // The original value is not a string. Should return the value in it's
            // original type.
            if (newValue.equals(oldValue.toString()))  {
                return oldValue;
            } else {
                // Must take the value from the editor and get the value and cast it to the new type.
                Class cls = oldValue.getClass();
                try {
                    Method method = cls.getMethod("valueOf", new Class[]{String.class});
                    newValue = method.invoke(oldValue, new Object[] { editor.getText()});
                } catch (Exception ex) {
                    // Fail silently and return the newValue (a String object)
                }
            }
        }
        return newValue;
    }

    public void selectAll() {
        editor.selectAll();
        editor.requestFocus();
    }

    // This used to do something but now it doesn't.  It couldn't be
    // removed because it would be an API change to do so.
    public void focusGained(FocusEvent e) {}
    
    // This used to do something but now it doesn't.  It couldn't be
    // removed because it would be an API change to do so.
    public void focusLost(FocusEvent e) {}

    public void addActionListener(ActionListener l) {
        editor.addActionListener(l);
    }

    public void removeActionListener(ActionListener l) {
        editor.removeActionListener(l);
    }

    /** 
     * Inner text field editable as a component of a ComboBox editor.
     * 
     * @see JEditableTextField
     * 
     * @version 12/02/08
     * @author Arnaud Weber
     * @author Mark Davidson
     * @author Pablo Piqueras Bartolomé
     */
    private class BorderlessTextField extends JEditableTextField {
		private static final long serialVersionUID = -3333236529430569318L;

		public BorderlessTextField(String value,int n) {
            super(value,n);
        }

        // workaround for 4530952
        public void setText(String s) {
            if (getText().equals(s)) {
                return;
            }
            super.setText(s);
        }

        public void setBorder(Border b) {}
    }
    
    /**
     * A subclass of BasicComboBoxEditor that implements UIResource.
     * BasicComboBoxEditor doesn't implement UIResource
     * directly so that applications can safely override the
     * cellRenderer property with BasicListCellRenderer subclasses.
     * <p>
     * <strong>Warning:</strong>
     * Serialized objects of this class will not be compatible with
     * future Swing releases. The current serialization support is
     * appropriate for short term storage or RMI between applications running
     * the same version of Swing.  As of 1.4, support for long term storage
     * of all JavaBeans<sup><font size="-2">TM</font></sup>
     * has been added to the <code>java.beans</code> package.
     * Please see {@link java.beans.XMLEncoder}.
     */
    public static class UIResource extends BasicComboBoxEditor
    implements javax.swing.plaf.UIResource {
    }
}
