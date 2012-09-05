/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
package com.iver.utiles.swing;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicComboBoxEditor;


/**
 * ComboBox autocompletable
 *
 * @author Fernando González Cortés
 */
public class JComboBox extends javax.swing.JComboBox {
    /**
     * Construye un combobox
     */
    public JComboBox() {
        super();
        init();
    }

    /**
     * Construye un combobox
     *
     * @param arg0
     */
    public JComboBox(Object[] arg0) {
        super(arg0);
        init();
    }

    /**
     * Construye un combobox
     *
     * @param arg0
     */
    public JComboBox(Vector arg0) {
        super(arg0);
        init();
    }

    /**
     * Construye un combobox
     *
     * @param arg0
     */
    public JComboBox(ComboBoxModel arg0) {
        super(arg0);
        init();
    }

    /**
     * Inicializa el combo
     */
    private void init() {
        this.setEditor(new BasicComboBoxEditor());

        JTextField jtext = (JTextField) JComboBox.this.getEditor()
                                                      .getEditorComponent();
        jtext.addKeyListener(new MyKeyListener());
        jtext.setText("");
    }

    /**
     * Manejador de los eventos Key para hacer el autocompletado
     *
     * @author Fernando González Cortés
     */
    public class MyKeyListener implements KeyListener {
        private int lastCaretPosition = 0;
        private String lastText = "";
        private boolean bAutocompletar=true;

        /**
         * Devuelve dada una String, la String del modelo que empieza por dicha
         * String si hay alguna, o null si no hay ninguna
         *
         * @param text texto que se busca en el modelo
         *
         * @return String del modelo o null
         */
        private String isInModel(String text) {
            DefaultComboBoxModel model = (DefaultComboBoxModel) JComboBox.this.getModel();

            for (int i = 0; i < model.getSize(); i++) {
                if ((model.getElementAt(i).toString()).startsWith(text)) {
                    return model.getElementAt(i).toString();
                }
            }

            return null;
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent arg0) {
            JTextField jtext = (JTextField) JComboBox.this.getEditor()
                                                          .getEditorComponent();
            String texto = jtext.getText();

            String text = isInModel(texto);
            
            if (text != null) {
                int caretPos = texto.length();
                jtext.setText(text);
                jtext.setCaretPosition(caretPos);
                jtext.setSelectionStart(caretPos);
                jtext.setSelectionEnd(text.length());
            }
        }

        /**
         * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
         */
        public void keyPressed(KeyEvent e) {
            JTextField jtext = (JTextField) JComboBox.this.getEditor()
                                                          .getEditorComponent();
            bAutocompletar = true;
            if ((e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
                    || (e.getKeyCode() == KeyEvent.VK_DELETE))
            {
                bAutocompletar = false;
                /* if ((lastCaretPosition >=0) && 
                        (lastCaretPosition <= jtext.getText().length()))
                        	jtext.setCaretPosition(lastCaretPosition); */
            }
        }

        /**
         * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
         */
        public void keyReleased(KeyEvent e) {
            JTextField jtext = (JTextField) JComboBox.this.getEditor()
                                                          .getEditorComponent();
            String texto = jtext.getText().substring(0, jtext.getCaretPosition());

            if (!bAutocompletar) return;
            String text = isInModel(texto);

            if (!jtext.getText().equals(lastText) && (text != null)) {
                int caretPos = texto.length();
                jtext.setText(text);
                jtext.setCaretPosition(text.length());
                jtext.moveCaretPosition(caretPos);
                lastText = text;
                lastCaretPosition = caretPos;
                JComboBox.this.setSelectedItem(text);
            } else {
                lastCaretPosition = jtext.getCaretPosition();
            }
        }

        /**
         * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
         */
        public void keyTyped(KeyEvent e) {
        }
    }
}
