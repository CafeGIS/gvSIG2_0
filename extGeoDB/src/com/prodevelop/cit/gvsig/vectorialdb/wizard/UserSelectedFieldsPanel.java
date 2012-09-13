/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 Prodevelop and Generalitat Valenciana.
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
 *   Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *   +34 963862235
 *   gvsig@gva.es
 *   www.gvsig.gva.es
 *
 *    or
 *
 *   Prodevelop Integración de Tecnologías SL
 *   Conde Salvatierra de Álava , 34-10
 *   46004 Valencia
 *   Spain
 *
 *   +34 963 510 612
 *   +34 963 510 968
 *   gis@prodevelop.es
 *   http://www.prodevelop.es
 */
package com.prodevelop.cit.gvsig.vectorialdb.wizard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.gui.beans.swing.JButton;

import com.iver.andami.PluginServices;


/**
 * Utility class that holds a single table field selection controls.
 *
 * @author jldominguez
 *
 */
public class UserSelectedFieldsPanel extends JPanel implements ActionListener {
    private FeatureAttributeDescriptor[] descriptors = null;
//    private String[] fieldTypes = null;
    private JScrollPane fieldsScrollPane = null;
    private AvailableFieldsCheckBoxList fieldsList = null;
    private JButton selAllFieldsButton = null;
    private JButton deselAllFieldsButton = null;
    private WizardDB parent = null;

    public UserSelectedFieldsPanel(FeatureAttributeDescriptor[] descriptors,
        boolean empty, WizardDB _p) {
        parent = _p;
        this.descriptors = descriptors;
//        fieldTypes = fTypes;
        initialize();

        if (empty) {
            enableControls(false);
        }
        else {
            setAllFieldsInTable(descriptors);
        }
    }

    public void loadValues() {
        setAllFieldsInTable(descriptors);
    }

    public String[] getUserSelectedFields(String idf, String geo) {
        String[] resp = getUserSelectedFields();
        if (idf != null) {
			resp = addBeginningIfNotContained(resp, idf);
		}
        if (geo != null) {
			resp = addBeginningIfNotContained(resp, geo);
		}

        return resp;
    }

    private String[] addBeginningIfNotContained(String[] arr, String item) {
        if (contains(arr, item)) {
            return arr;
        }
        else {
            int size = arr.length;
            String[] resp = new String[size + 1];
            resp[0] = item;

            for (int i = 0; i < size; i++) {
                resp[i + 1] = arr[i];
            }

            return resp;
        }
    }

    private String[] removeIfContained(String[] arr, String item) {
        if (!contains(arr, item)) {
            return arr;
        }
        else {
            int size = arr.length;
            ArrayList aux = new ArrayList();

            for (int i = 0; i < size; i++) {
				aux.add(arr[i]);
			}

            aux.remove(item);

            return (String[]) aux.toArray(new String[0]);
        }
    }

    private boolean contains(String[] arr, String item) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].compareTo(item) == 0) {
                return true;
            }
        }

        return false;
    }

    public void enableControls(boolean enable) {
        getFieldsList().setEnabled(enable);
        getSelAllFieldsButton().setEnabled(enable);
        getDeselAllFieldsButton().setEnabled(enable);
    }

    private void initialize() {
        setLayout(null);
        setBounds(new java.awt.Rectangle(255, 55, 251, 166));
        setBorder(javax.swing.BorderFactory.createTitledBorder(null,
                PluginServices.getText(this, "table_fields"),
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
        add(getFieldsScrollPane(), null);

        add(getSelAllFieldsButton(), null);
        add(getDeselAllFieldsButton(), null);
    }

    private JScrollPane getFieldsScrollPane() {
        if (fieldsScrollPane == null) {
            fieldsScrollPane = new JScrollPane();
            fieldsScrollPane.setBounds(new java.awt.Rectangle(5, 20,
                    101 + (28 * 5), 76 + (7 * 5)));
            fieldsScrollPane.setViewportView(getFieldsList());
        }

        return fieldsScrollPane;
    }

    private AvailableFieldsCheckBoxList getFieldsList() {
        if (fieldsList == null) {
            fieldsList = new AvailableFieldsCheckBoxList();
        }

        return fieldsList;
    }

    private void setAllFieldsInTable(FeatureAttributeDescriptor[] descriptors) {
        DefaultListModel lmodel = new DefaultListModel();

        for (int i = 0; i < descriptors.length; i++) {
            lmodel.addElement(new FieldsListItem(descriptors[i]));
        }

        getFieldsList().setModel(lmodel);
        getFieldsScrollPane().setViewportView(fieldsList);
        getFieldsScrollPane().updateUI();
    }

    private JButton getSelAllFieldsButton() {
        if (selAllFieldsButton == null) {
            selAllFieldsButton = new JButton();
            selAllFieldsButton.addActionListener(this);
            selAllFieldsButton.setBounds(new java.awt.Rectangle(28 + 5, 135,
                    90, 26));
            selAllFieldsButton.setText(PluginServices.getText(this, "all"));
        }

        return selAllFieldsButton;
    }

    /**
     * This method initializes deselAllFieldsButton
     *
     * @return javax.swing.JButton
     */
    private JButton getDeselAllFieldsButton() {
        if (deselAllFieldsButton == null) {
            deselAllFieldsButton = new JButton();
            deselAllFieldsButton.addActionListener(this);
            deselAllFieldsButton.setBounds(new java.awt.Rectangle(28 + 100,
                    135, 90, 26));
            deselAllFieldsButton.setText(PluginServices.getText(this, "none2"));
        }

        return deselAllFieldsButton;
    }

    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == getDeselAllFieldsButton()) {
            getFieldsList().checkAll(false);
        }

        if (src == getSelAllFieldsButton()) {
            getFieldsList().checkAll(true);
        }
    }

    private String[] getUserSelectedFields() {
        Object[] sel = fieldsList.getCheckedItems();
        int size = sel.length;

        String[] resp = new String[size];

        for (int i = 0; i < size; i++) {
            resp[i] = ((FieldsListItem) sel[i]).getName();
        }

        return resp;
    }

    public void repaint() {
        super.repaint();
        getFieldsList().updateUI();
        getFieldsScrollPane().updateUI();
    }
}
