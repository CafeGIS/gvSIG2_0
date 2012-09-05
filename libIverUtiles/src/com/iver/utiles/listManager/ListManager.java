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
package com.iver.utiles.listManager;

import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


/**
 * Control de gestión de una lista
 *
 * @author Fernando González Cortés
 */
public class ListManager {
    private JScrollPane jScrollPane = null;
    private JList list = null;
    private JButton btnAdd = null;
    private JButton btnDel = null;
    private JPanel jPanel = null;
    private JButton btnProperties = null;
    private JButton btnUp = null;
    private JButton btnDown = null;
    private ListModel listModel;// = new DefaultListModel();
    private ListManagerListener listener;
    private ResourceBundle bundle;

    /**
     * This is the default constructor
     * down==true if list is in order.
     */
    public ListManager(boolean down) {
        super();
        listModel = new DefaultListModel(down);
    }

    /**
     * This method initializes this
     */
    public void initialize() {
        actualizar();
    }

    /**
     * Obtiene la traducción de una palabra
     *
     * @param key Clave en el resource bundle
     *
     * @return valor de la clave
     */
    private String getText(String key) {
        if (bundle == null) {
            return key;
        }

        String ret = bundle.getString(key);

        if (ret == null) {
            ret = key;
        }

        return ret;
    }

    /**
     * Habilita/Deshabilita los botones
     */
    private void actualizar() {
        int index = list.getSelectedIndex();

        if (index != -1) {
            if (btnUp != null) {
                btnUp.setEnabled(index > 0);
            }

            if (btnDown != null) {
                btnDown.setEnabled(index < (listModel.getSize() - 1));
            }

            if (btnDel != null) {
                btnDel.setEnabled(true);
            }

            if (btnProperties != null) {
                btnProperties.setEnabled(listModel.getElementAt(index) instanceof Propertiable);
            }
        } else {
            if (btnUp != null) {
                btnUp.setEnabled(false);
            }

            if (btnDown != null) {
                btnDown.setEnabled(false);
            }

            if (btnDel != null) {
                btnDel.setEnabled(false);
            }

            if (btnProperties != null) {
                btnProperties.setEnabled(false);
            }
        }
    }

    /**
     * This method initializes list
     *
     * @param list DOCUMENT ME!
     */
    public void setList(JList list) {
        this.list = list;
        list.setModel(listModel);
        list.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
                public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                    actualizar();
                }
            });
    }

    /**
     * This method initializes btnAdd
     *
     * @param btn DOCUMENT ME!
     */
    public void setBtnAdd(JButton btn) {
        btnAdd = btn;
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    Object[] objs = listener.addObjects();

                    for (int i = 0; i < objs.length; i++) {
                        listModel.add(objs[i]);
                    }
                    actualizar();
                }
            });
    }

    /**
     * This method initializes btnDel
     *
     * @param btn DOCUMENT ME!
     */
    public void setBtnDel(JButton btn) {
        btnDel = btn;
        btnDel.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    int i = list.getSelectedIndex();
                    listModel.remove(i);

                    if (i < listModel.getSize()) {
                        list.setSelectedIndex(i);
                    }

                    actualizar();
                }
            });
    }

    /**
     * This method initializes btnProperties
     *
     * @param btn DOCUMENT ME!
     */
    public void setBtnProperties(JButton btn) {
        btnProperties = btn;
        btnProperties.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    int index = list.getSelectedIndex();
                    Object prop = listener.getProperties(listModel.getElementAt(
                                index));
                    ((Propertiable) listModel.getElementAt(index)).setProperties(prop);
                    list.repaint();
                }
            });
    }

    /**
     * This method initializes btnUp
     *
     * @param btn DOCUMENT ME!
     */
    public void setBtnUp(JButton btn) {
        btnUp = btn;
        btnUp.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    int index = list.getSelectedIndex();
                    Object o1=listModel.getElementAt(index-1);
                    Object o2=listModel.getElementAt(index);
                    listModel.remove(index-1);
                    listModel.insertAt(index-1,o2);
                    listModel.remove(index);
                    listModel.insertAt(index,o1);
                    list.setSelectedIndex(index - 1);
                }
            });
    }

    /**
     * This method initializes btnDown
     *
     * @param btn DOCUMENT ME!
     */
    public void setBtnDown(JButton btn) {
        btnDown = btn;
        btnDown.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    int index = list.getSelectedIndex();
                    Object o = listModel.remove(index);
                    listModel.insertAt(index + 1, o);
                    list.setSelectedIndex(index + 1);
                }
            });
    }

    /**
     * Obtiene el listModel
     *
     * @return Returns the listModel.
     */
    public ListModel getListModel() {
        return listModel;
    }

    /**
     * Establece el listModel
     *
     * @param listModel The listModel to set.
     */
    public void setListModel(ListModel listModel) {
        this.listModel = listModel;
    }

    /**
     * Obtiene el listener de eventos del control
     *
     * @return Returns the listener.
     */
    public ListManagerListener getListener() {
        return listener;
    }

    /**
     * Establece el listener de eventos del control
     *
     * @param listener The listener to set.
     */
    public void setListener(ListManagerListener listener) {
        this.listener = listener;
    }

    /**
     * DOCUMENT ME!
     *
     * @param bundle The bundle to set.
     */
    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }
}
