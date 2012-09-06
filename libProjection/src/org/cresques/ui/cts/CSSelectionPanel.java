/*
 * Cresques Mapping Suite. Graphic Library for constructing mapping applications.
 *
 * Copyright (C) 2004-5.
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
 * cresques@gmail.com
 */
package org.cresques.ui.cts;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.cresques.Messages;
import org.cresques.cts.IProjection;
import org.cresques.ui.LoadableComboBox;


//import es.gva.cit.geoexplorer.ui.LoadableComboBox;

/**
 * Panel de edición de Sistemas de referencia
 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>
 */
public class CSSelectionPanel extends JPanel {
    final private static long serialVersionUID = -3370601314380922368L;
    private LoadableComboBox datumComboBox = null;
    private LoadableComboBox projComboBox = null;
    private LoadableComboBox huseComboBox = null;
    private JLabel jLabel = null;
    private JLabel jLabel1 = null;
    private JLabel jLabel2 = null;
    private String tit;
    private CSSelectionModel model;

    /**
     * Constructor de la clase.
     */
    public CSSelectionPanel(String tit) {
        super();

        if (tit == null) {
            //tit = "Sistema de referencia";
            //TODO: add com.iver.andami.PluginServices to this project
            //change all the labels from fix text got from the internationalitation
            tit = Messages.getText("reference_system");
            if (tit == null) tit="Reference System";
        }

        this.tit = tit;
        setModel(new CSSelectionModel());
        initialize();
    }

    /**
     * Inicializa el panel.
     * @return javax.swing.JPanel
     */
    private void initialize() {
        setPreferredSize(new java.awt.Dimension(295, 170));
        setLayout(null);

        /*javax.swing.border.Border border = javax.swing.BorderFactory.createCompoundBorder(
        javax.swing. BorderFactory.createTitledBorder("Sistema de coordenadas"),
        javax.swing.BorderFactory.createEmptyBorder(5,5,5,5)); */
        setBorder(javax.swing.BorderFactory.createCompoundBorder(null,
                                                                 javax.swing.BorderFactory.createTitledBorder(null,
                                                                		 										Messages.getText("reference_system"),
                                                                                                              javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                                                                                                              javax.swing.border.TitledBorder.DEFAULT_POSITION,
                                                                                                              null,
                                                                                                              null)));

        jLabel = new JLabel();
        jLabel.setBounds(15, 15, 77, 23);
        jLabel.setText(Messages.getText("datum") + ":");
        add(jLabel, null);
        add(getDatumComboBox(), null);

        jLabel1 = new JLabel();
        jLabel1.setBounds(15, 60, 77, 23);
        jLabel1.setText(Messages.getText("projection") + ":");
        add(jLabel1, null);
        add(getProjComboBox(), null);

        jLabel2 = new JLabel();
        jLabel2.setBounds(15, 105, 77, 23);
        jLabel2.setText(Messages.getText("zone") + ":");
        add(jLabel2, null);
        add(getHuseComboBox(), null);

        setHuseComboBoxEnabled(false);
    }

    public void setModel(CSSelectionModel model) {
        this.model = model;

        getHuseComboBox().loadData(model.getZoneList());
        getDatumComboBox().loadData(model.getDatumList());
        getProjComboBox().loadData(model.getProjectionList());
    }

    private void setHuseComboBoxEnabled(boolean enabled) {
        if (jLabel2 != null) {
            jLabel2.setEnabled(enabled);
        }

        getHuseComboBox().setEnabled(enabled);
    }

    private void setDatumComboBoxEnabled(boolean enabled) {
        if (jLabel != null) {
            jLabel.setEnabled(enabled);
        }

        getDatumComboBox().setEnabled(enabled);
    }

    public void setProjection(IProjection proj) {
        model.setProjection(proj);

        setDatumComboBoxEnabled(true);
        getDatumComboBox().setSelectedIndex(model.getSelectedDatum());
 
        getProjComboBox().removeAllItems();
        getProjComboBox().loadData(model.getProjectionList());

        model.setProjection(proj);
        getProjComboBox().setSelectedIndex(model.getSelectedProj());
        model.setProjection(proj);
        
        if (model.getSelectedZone() >= 0) {
            setHuseComboBoxEnabled(true);
            getHuseComboBox().removeAllItems();
            getHuseComboBox().loadData(model.getZoneList());

            model.setProjection(proj);
            getHuseComboBox().setSelectedIndex(model.getSelectedZone());
        } else {
            setHuseComboBoxEnabled(false);
            getHuseComboBox().setSelectedIndex(0);
        }
    }

    /**
     * Inicializa datumComboBox
     *
     * @return javax.swing.JComboBox
     */
    private LoadableComboBox getDatumComboBox() {
        if (datumComboBox == null) {
            datumComboBox = new LoadableComboBox();
            datumComboBox.setBounds(14, 35, 250, 23);

            //			((LoadableComboBox) datumComboBox).loadData(model.getDatumList());
            datumComboBox.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent e) {
                        model.setSelectedDatum(e.getItem());
                        getProjComboBox().removeAllItems();
                        getProjComboBox().loadData(model.getProjectionList());
                    }
                });
        }

        return datumComboBox;
    }

    /**
     * Inicializa projComboBox
     *
     * @return javax.swing.JComboBox
     */
    private LoadableComboBox getProjComboBox() {
        if (projComboBox == null) {
            projComboBox = new LoadableComboBox();
            projComboBox.setBounds(14, 80, 250, 23);
            projComboBox.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent e) {
                        model.setSelectedProj(e.getItem());

                        if (model.getSelectedProjType() == CSSelectionModel.TRANSVERSAL) {
                            setHuseComboBoxEnabled(true);
                            getHuseComboBox().removeAllItems();
                            getHuseComboBox().loadData(model.getZoneList());

                        } else {
                            setHuseComboBoxEnabled(false);
                        }

                       // if (model.getSelectedProjType() == CSSelectionModel.NONE) {
                       //     setDatumComboBoxEnabled(false);
                       // } else {
                       //     setDatumComboBoxEnabled(true);
                       // }
                    }
                });
        }

        return projComboBox;
    }

    /**
     * Inicializa usoComboBox
     *
     * @return javax.swing.JComboBox
     */
    private LoadableComboBox getHuseComboBox() {
        if (huseComboBox == null) {
            huseComboBox = new LoadableComboBox();
            huseComboBox.setBounds(14, 125, 250, 23);
            huseComboBox.addItemListener(new java.awt.event.ItemListener() {
                    public void itemStateChanged(java.awt.event.ItemEvent e) {
                        model.setSelectedZone(e.getItem());
                    }
                });
        }

        return huseComboBox;
    }

    /**
     * @return
     */
    public IProjection getProjection() {
        return model.getProjection();
    }
}
