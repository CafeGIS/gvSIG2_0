/*
 * Created on 07-oct-2005
 *
 * gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
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
package com.iver.cit.gvsig.vectorialdb;

import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;

public class JPasswordDlg extends JPanel implements IWindow {

    private JPanel jContentPane = null;

    private JButton jButtonOK = null;

    private JButton jButton1 = null;

    private JPasswordField jPasswordField = null;

    private String password = null;

    private JEditorPane jEditorPane = null;

    private JTextArea jLblMensaje = null;

//    private WindowEventsHandler windowHandler = new WindowEventsHandler();

    /**
     * This is the default constructor
     */
    public JPasswordDlg() {
        super();
        initialize();
    }

//    class WindowEventsHandler extends WindowAdapter
//    {
//
//        /* (non-Javadoc)
//         * @see java.awt.event.WindowAdapter#windowActivated(java.awt.event.WindowEvent)
//         */
//        public void windowActivated(WindowEvent e) {
//            // TODO Auto-generated method stub
//            super.windowActivated(e);
//            jPasswordField.requestFocus();
//        }
//
//    }
    /**
     * This method initializes this
     *
     * @return void
     */
private void initialize() {
        this.setSize(287, 172);
        this.setLayout(null);
        this.add(getJContentPane());
//        this.setModal(true);
//        this.setResizable(false);
//        this.setTitle("Enter Password");
//        this.setContentPane(getJContentPane());
//
//        addWindowListener(windowHandler);
    }
    /**
     * This method initializes jContentPane
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getJButton1(), null);
            jContentPane.add(getJButtonOK(), null);
            jContentPane.add(getJPasswordField(), null);
            jContentPane.add(getJLblMensaje(), null);
            jContentPane.setBounds(0,0,300,200);
        }
        return jContentPane;
    }

    /**
     * This method initializes jButton
     *
     * @return javax.swing.JButton
     */
    private JButton getJButtonOK() {
        if (jButtonOK == null) {
            jButtonOK = new JButton();
            jButtonOK.setText("OK");
            jButtonOK.setPreferredSize(new java.awt.Dimension(65, 23));
            jButtonOK.setBounds(35, 110, 101, 22);
            jButtonOK.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.out.println("actionPerformed()"); // TODO
                    // Auto-generated
                    // Event stub
                    // actionPerformed()
                    password = String.copyValueOf(getJPasswordField()
                            .getPassword());
                    PluginServices.getMDIManager().closeWindow(JPasswordDlg.this);
//                    dispose();
                }
            });
        }
        return jButtonOK;
    }

    /**
     * This method initializes jButton1
     *
     * @return javax.swing.JButton
     */
    private JButton getJButton1() {
        if (jButton1 == null) {
            jButton1 = new JButton();
            jButton1.setText("Cancel");
            jButton1.setBounds(136, 110, 99, 22);
            jButton1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    password = null;
                    PluginServices.getMDIManager().closeWindow(JPasswordDlg.this);
//                    dispose();
                }
            });
        }
        return jButton1;
    }

    /**
     * This method initializes jPasswordField
     *
     * @return javax.swing.JPasswordField
     */
    private JPasswordField getJPasswordField() {
        if (jPasswordField == null) {
            jPasswordField = new JPasswordField();
            jPasswordField.setPreferredSize(new java.awt.Dimension(60, 22));
            jPasswordField.setBounds(65, 78, 145, 21);
            jPasswordField.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent e) {
                    if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                        // System.out.println("INTRO");
                        getJButtonOK().doClick();
                    }
                }
            });
        }
        return jPasswordField;
    }

    /**
     * @return Returns the password.
     */
    public String getPassword() {
        return password;
    }

    public void setMessage(String str) {
        getJLblMensaje().setText(str);
    }

    /**
     * This method initializes jTextArea
     *
     * @return javax.swing.JTextArea
     */
    private JTextArea getJLblMensaje() {
        if (jLblMensaje == null) {
            jLblMensaje = new JTextArea();
            jLblMensaje.setEditable(false);
            jLblMensaje.setForeground(java.awt.Color.black);
            jLblMensaje.setBackground(java.awt.SystemColor.control);
            jLblMensaje.setText("Mensaje");
            jLblMensaje.setLineWrap(true);
            jLblMensaje.setFont(new java.awt.Font("SansSerif",
                    java.awt.Font.PLAIN, 12));
            jLblMensaje.setPreferredSize(new java.awt.Dimension(270, 50));
            jLblMensaje.setBounds(9, 6, 266, 68);
            jLblMensaje.setWrapStyleWord(true);
        }
        return jLblMensaje;
    }
	public WindowInfo getWindowInfo() {
		WindowInfo wi=new WindowInfo(WindowInfo.MODALDIALOG|WindowInfo.RESIZABLE);
		wi.setTitle(PluginServices.getText(this,"enter_password"));
		return wi;
	}
	public Object getWindowProfile() {
		// TODO Auto-generated method stub
		return WindowInfo.DIALOG_PROFILE;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
