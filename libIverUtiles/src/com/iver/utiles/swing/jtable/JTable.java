package com.iver.utiles.swing.jtable;

import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

/**
 * @author Fernando González Cortés
 */
public class JTable extends javax.swing.JTable{
    protected boolean processKeyBinding(KeyStroke ks, KeyEvent e,
            int condition, boolean pressed) {
        boolean value = super.processKeyBinding( ks, e, 
                condition, pressed );

        // Make sure that the editor component has focus.
        if ( isEditing() ) {
            getEditorComponent().requestFocus();
        }
        return value;
    }

}
