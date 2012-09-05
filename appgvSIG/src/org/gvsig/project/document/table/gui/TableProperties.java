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
package org.gvsig.project.document.table.gui;


import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JTable;

import org.gvsig.gui.beans.AcceptCancelPanel;
import org.gvsig.project.document.table.FeatureTableDocument;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.SingletonWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;

/**
 * Dialogo de propiedades de la tabla
 *
 * @author Fernando González Cortés
 */
public class TableProperties extends JPanel implements SingletonWindow {
    /**
	 *
	 */
	private static final long serialVersionUID = -3280622997496976100L;

	private javax.swing.JLabel jLabel = null;
    private javax.swing.JTextField txtName = null;
    private javax.swing.JLabel jLabel1 = null;
    private javax.swing.JTextField txtDate = null;
    private javax.swing.JTextField txtOwner = null;
    private javax.swing.JLabel jLabel2 = null;
    private javax.swing.JLabel jLabel3 = null;
    private javax.swing.JScrollPane jScrollPane = null;
    private javax.swing.JTextArea txtComments = null;
    private javax.swing.JScrollPane jScrollPane1 = null;
    private javax.swing.JTable tabla = null;
    private FeatureTableDocument table;
    private AcceptCancelPanel okCancelPanel = null;
    private javax.swing.JPanel jPanel = null;
    private javax.swing.JPanel jPanel1 = null;
    /**
     * This is the default constructor
     *
     * @param f Frame padre del diálogo
     * @param t Tabla que se representa en el diálogo
     * @throws DriverException
     */
    public TableProperties(FeatureTableDocument t) {
        table = t;

        initialize();
    }

    /**
     * This method initializes this
     * @throws DriverException
     */
    private void initialize() {

		java.awt.FlowLayout layFlowLayout7 = new java.awt.FlowLayout();
		layFlowLayout7.setHgap(0);
        setLayout(layFlowLayout7);
		add(getJPanel(), null);
		add(getJPanel1(), null);
		add(getJLabel3(), null);
		add(getJScrollPane(), null);
		add(getJScrollPane1(), null);
		add(getOkCancelPanel(),null);
		//add(getBtnOk(), null);
		//add(getJLabel4(), null);
		//add(getBtnCancel(), null);

        txtName.setText(table.getName());
        txtDate.setText(table.getCreationDate());
        txtOwner.setText(table.getOwner());
        txtComments.setText(table.getComment());

        tabla.setModel(new TableModelProperties(table));

        // TODO: provisional hasta que revisemos lo del mapeado de campos.
        tabla.setVisible(true);
        tabla.getTableHeader().setVisible(true);
    }

    /**
     * This method initializes jLabel
     *
     * @return javax.swing.JLabel
     */
    private javax.swing.JLabel getJLabel() {
        if (jLabel == null) {
            jLabel = new javax.swing.JLabel();
            jLabel.setText(PluginServices.getText(this, "nombre") + ":");
        }

        return jLabel;
    }

    /**
     * This method initializes txtName
     *
     * @return javax.swing.JTextField
     */
    private javax.swing.JTextField getTxtName() {
        if (txtName == null) {
            txtName = new javax.swing.JTextField();
            txtName.setPreferredSize(new java.awt.Dimension(190, 20));
        }

        return txtName;
    }

    /**
     * This method initializes jLabel1
     *
     * @return javax.swing.JLabel
     */
    private javax.swing.JLabel getJLabel1() {
        if (jLabel1 == null) {
            jLabel1 = new javax.swing.JLabel();
            jLabel1.setText(PluginServices.getText(this, "creation_date") + ":");
        }

        return jLabel1;
    }

    /**
     * This method initializes txtDate
     *
     * @return javax.swing.JTextField
     */
    private javax.swing.JTextField getTxtDate() {
        if (txtDate == null) {
            txtDate = new javax.swing.JTextField();
            txtDate.setPreferredSize(new java.awt.Dimension(190, 20));
            txtDate.setEditable(false);
            txtDate.setBackground(java.awt.Color.white);
        }

        return txtDate;
    }

    /**
     * This method initializes txtOwner
     *
     * @return javax.swing.JTextField
     */
    private javax.swing.JTextField getTxtOwner() {
        if (txtOwner == null) {
            txtOwner = new javax.swing.JTextField();
            txtOwner.setPreferredSize(new java.awt.Dimension(190, 20));
        }

        return txtOwner;
    }

    /**
     * This method initializes jLabel2
     *
     * @return javax.swing.JLabel
     */
    private javax.swing.JLabel getJLabel2() {
        if (jLabel2 == null) {
            jLabel2 = new javax.swing.JLabel();
            jLabel2.setText(PluginServices.getText(this, "owner") + ":");
        }

        return jLabel2;
    }

    /**
     * This method initializes jLabel3
     *
     * @return javax.swing.JLabel
     */
    private javax.swing.JLabel getJLabel3() {
        if (jLabel3 == null) {
            jLabel3 = new javax.swing.JLabel();
            jLabel3.setText(PluginServices.getText(this, "comentarios") + ":");
            jLabel3.setPreferredSize(new java.awt.Dimension(320,16));
        }

        return jLabel3;
    }

    /**
     * This method initializes jScrollPane
     *
     * @return javax.swing.JScrollPane
     */
    private javax.swing.JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new javax.swing.JScrollPane();
            jScrollPane.setViewportView(getTxtComments());
            jScrollPane.setPreferredSize(new java.awt.Dimension(320, 70));
        }

        return jScrollPane;
    }

    /**
     * This method initializes txtComments
     *
     * @return javax.swing.JTextArea
     */
    private javax.swing.JTextArea getTxtComments() {
        if (txtComments == null) {
            txtComments = new javax.swing.JTextArea();
        }

        return txtComments;
    }

    /**
     * This method initializes jTable
     *
     * @return javax.swing.JTable
     */
    private javax.swing.JTable getJTable() {
        if (tabla == null) {
            tabla = new javax.swing.JTable();
            tabla.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        }

        return tabla;
    }

    /**
     * This method initializes jScrollPane1
     *
     * @return javax.swing.JScrollPane
     */
    private javax.swing.JScrollPane getJScrollPane1() {
        if (jScrollPane1 == null) {
            jScrollPane1 = new javax.swing.JScrollPane();
            jScrollPane1.setViewportView(getJTable());
            jScrollPane1.setPreferredSize(new java.awt.Dimension(320, 160));
        }

        return jScrollPane1;
    }

    private AcceptCancelPanel getOkCancelPanel() {
    	if (okCancelPanel == null) {
    		okCancelPanel = new AcceptCancelPanel(
    				new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent e) {
                            table.setName(txtName.getText());
                            table.setCreationDate(txtDate.getText());
                            table.setOwner(txtOwner.getText());
                            table.setComment(txtComments.getText());

                            // FIXME
							// TableModel tm = getJTable().getModel();
							// if (tm instanceof TableModelProperties) {
							// table.setAliases(((TableModelProperties)tm).getAliases());
							// table.setMapping(((TableModelProperties)tm).getFieldMapping());
							// }
                            PluginServices.getMDIManager().closeWindow(TableProperties.this);
                        }
                    }
    				,
    				new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent e) {
                        	PluginServices.getMDIManager().closeWindow(TableProperties.this);
                        }
                    }
    		);
    		okCancelPanel.setPreferredSize(new java.awt.Dimension(339, 30));
    	}
    	return okCancelPanel;
    }


    /**
     * This method initializes jPanel
     *
     * @return javax.swing.JPanel
     */
    private javax.swing.JPanel getJPanel() {
        if (jPanel == null) {
            jPanel = new javax.swing.JPanel();

            /*
            java.awt.FlowLayout layFlowLayout9 = new java.awt.FlowLayout();
            layFlowLayout9.setAlignment(java.awt.FlowLayout.LEFT);
            layFlowLayout9.setVgap(9);
            jPanel.setLayout(layFlowLayout9);

            */

            GridLayout layout = new GridLayout(3,1);
            layout.setVgap(5);
            jPanel.setLayout(layout);


            jPanel.add(getJLabel(), null);
            jPanel.add(getJLabel1(), null);
            jPanel.add(getJLabel2(), null);
            jPanel.setPreferredSize(new java.awt.Dimension(130, 80));
        }

        return jPanel;
    }

    /**
     * This method initializes jPanel1
     *
     * @return javax.swing.JPanel
     */
    private javax.swing.JPanel getJPanel1() {
        if (jPanel1 == null) {
            jPanel1 = new javax.swing.JPanel();

            java.awt.FlowLayout layFlowLayout8 = new java.awt.FlowLayout();
            layFlowLayout8.setAlignment(java.awt.FlowLayout.LEFT);
            jPanel1.setLayout(layFlowLayout8);
            jPanel1.add(getTxtName(), null);
            jPanel1.add(getTxtDate(), null);
            jPanel1.add(getTxtOwner(), null);
            jPanel1.setPreferredSize(new java.awt.Dimension(200, 80));
        }

        return jPanel1;
    }

   /**
	 * @see com.iver.mdiApp.ui.MDIManager.IWindow#getWindowInfo()
	 */
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo=new WindowInfo();
   		m_viewinfo.setTitle(PluginServices.getText(this, "propiedades_tabla"));
		m_viewinfo.setWidth(349);
		m_viewinfo.setHeight(375);

		return m_viewinfo;
	}

	/**
	 * @see com.iver.mdiApp.ui.MDIManager.IWindow#windowActivated()
	 */
	public void viewActivated() {
	}

    /**
     * @see com.iver.andami.ui.mdiManager.SingletonWindow#getWindowModel()
     */
    public Object getWindowModel() {
        return table;
    }

	public Object getWindowProfile() {
		return WindowInfo.PROPERTIES_PROFILE;
	}
}


//  @jve:visual-info  decl-index=0 visual-constraint="10,10"
