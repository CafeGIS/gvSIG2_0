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
package com.iver.core;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.MessageEvent;
import com.iver.andami.messages.NotificationListener;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.SingletonWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;


/**
 * Graphic interface that appears to show an error.
 *
 * @author Vicente Caballero Navarro
 */
public class NotificationDialog extends JPanel implements IWindow, SingletonWindow,
    NotificationListener {
    private JButton bDetails = null;
    private JPanel pDescription = null;
    private JTextArea txtDescription = null;
    private JButton bNoDetails = null;
    private JButton bAcept = null;
    private JScrollPane pScrollDescription = null;

    /**
     * This is the default constructor
     */
    public NotificationDialog() {
        super();
        initialize();
    }

    /**
     * This method initializes this
     */
    private void initialize() {
        Consola.consolaFrame.setSize(new java.awt.Dimension(457, 150));
        Consola.consolaFrame.setLocation(new java.awt.Point(11, 140));
        Consola.consolaFrame.setVisible(false);
        this.setLayout(null);
        this.setSize(470, 175);
        this.add(getBDetails(), null);
        this.add(getPDescription(), null);
        this.add(Consola.consolaFrame, null);
        this.add(getDNoDetails(), null);
        this.add(getBAcept(), null);
    }

    /**
     * This method initializes bDetails
     *
     * @return javax.swing.JButton
     */
    private JButton getBDetails() {
        if (bDetails == null) {
            bDetails = new JButton();
            bDetails.setBounds(new java.awt.Rectangle(315, 110, 129, 24));
            bDetails.setText(PluginServices.getText(this,"detalles") + "   >>>");
            bDetails.setVisible(true);
            bDetails.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent e) {
                        Consola.consolaFrame.setVisible(true);
                        bDetails.setVisible(false);
                        getDNoDetails().setVisible(true);
                        PluginServices.getMDIManager()
                                      .getWindowInfo(NotificationDialog.this)
                                      .setHeight(325);
                        setSize(460, 325);
                    }
                });
        }

        return bDetails;
    }

    /**
     * This method initializes pDescription
     *
     * @return javax.swing.JPanel
     */
    private JPanel getPDescription() {
        if (pDescription == null) {
            pDescription = new JPanel();
            pDescription.setBounds(new java.awt.Rectangle(7, 5, 437, 99));
            pDescription.setBorder(javax.swing.BorderFactory.createTitledBorder(
                    null, PluginServices.getText(this,"descripcion"),
                    javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                    javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
            pDescription.add(getPScrollDescription(), null);
        }

        return pDescription;
    }

    /**
     * This method initializes txtDescription
     *
     * @return javax.swing.JTextArea
     */
    private JTextArea getTxtDescription() {
        if (txtDescription == null) {
            txtDescription = new JTextArea();
            //txtDescription.setPreferredSize(new java.awt.Dimension(420, 65));
            txtDescription.setForeground(java.awt.Color.blue);
            txtDescription.setBackground(java.awt.SystemColor.control);
            txtDescription.setEditable(false);
        }

        return txtDescription;
    }

    /**
     * @see com.iver.mdiApp.ui.MDIManager.IWindow#getModel()
     */
    public Object getWindowModel() {
        return "consola";
    }

    /**
     * @see com.iver.andami.ui.mdiManager.IWindow#getWindowInfo()
     */
    public WindowInfo getWindowInfo() {
        WindowInfo info = new WindowInfo(WindowInfo.MODELESSDIALOG |
                WindowInfo.ICONIFIABLE);
        info.setTitle(PluginServices.getText(this, "titulo_consola"));

        return info;
    }

    /**
     * @see com.iver.mdiApp.NotificationListener#errorEvent(java.lang.String)
     */
    public void errorEvent(MessageEvent e) {
        if (e.getMessages() != null) {
            for (int i = 0; i < e.getMessages().length; i++) {
                txtDescription.setText(e.getMessages()[i]);
            }
        }

        PluginServices.getMDIManager().restoreCursor();
        if (SwingUtilities.isEventDispatchThread()) {
            PluginServices.getMDIManager().addWindow(this);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        PluginServices.getMDIManager().addWindow(NotificationDialog.this);
                    }
                });
        }

    }

    /**
     * @see com.iver.mdiApp.NotificationListener#warningEvent(java.lang.String)
     */
    public void warningEvent(MessageEvent e) {
    }

    /**
     * @see com.iver.mdiApp.NotificationListener#infoEvent(java.lang.String)
     */
    public void infoEvent(MessageEvent e) {
    }

    /**
     * This method initializes dNoDetails
     *
     * @return javax.swing.JButton
     */
    private JButton getDNoDetails() {
        if (bNoDetails == null) {
            bNoDetails = new JButton();
            bNoDetails.setVisible(false);
            bNoDetails.setBounds(new java.awt.Rectangle(315, 110, 128, 24));
            bNoDetails.setText("<<<   " + PluginServices.getText(this,"detalles"));
            bNoDetails.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent e) {
                        bDetails.setVisible(true);
                        bNoDetails.setVisible(false);
                        Consola.consolaFrame.setVisible(false);
                        PluginServices.getMDIManager()
                                      .getWindowInfo(NotificationDialog.this)
                                      .setHeight(175);
                        setSize(460, 175);
                    }
                });
        }

        return bNoDetails;
    }

    /**
     * This method initializes bAcept
     *
     * @return javax.swing.JButton
     */
    private JButton getBAcept() {
        if (bAcept == null) {
            bAcept = new JButton();
            bAcept.setBounds(new java.awt.Rectangle(10, 110, 296, 24));
            bAcept.setText(PluginServices.getText(this,"aceptar"));
            bAcept.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent e) {
                        PluginServices.getMDIManager().closeWindow(NotificationDialog.this);
                    }
                });
        }

        return bAcept;
    }

    /**
     * This method initializes pScrollDescription
     *
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getPScrollDescription() {
        if (pScrollDescription == null) {
            pScrollDescription = new JScrollPane();
            pScrollDescription.setPreferredSize(new java.awt.Dimension(420,67));
            pScrollDescription.setAutoscrolls(true);
            pScrollDescription.setViewportView(getTxtDescription());
        }

        return pScrollDescription;
    }
	public Object getWindowProfile() {
		return WindowInfo.PROPERTIES_PROFILE;
	}

} //  @jve:decl-index=0:visual-constraint="10,10"
