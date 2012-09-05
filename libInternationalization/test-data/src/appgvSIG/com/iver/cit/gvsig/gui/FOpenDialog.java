/*
 * Created on 23-abr-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
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
package com.iver.cit.gvsig.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.cresques.cts.IProjection;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.View;
import com.iver.andami.ui.mdiManager.ViewInfo;
import com.iver.cit.gvsig.gui.wizards.WizardListener;
import com.iver.cit.gvsig.project.Project;
/**
 * Diálogo para cargar las dieferentes capas a la vista.
 *
 * @author Vicente Caballero Navarro
 */
public class FOpenDialog extends JPanel implements com.iver.andami.ui.mdiManager.View {
	//private javax.swing.JDialog jDialog = null;  //  @jve:visual-info  decl-index=0 visual-constraint="19,27"
    
	static private IProjection proj = Project.getProjection();
	
	private javax.swing.JTabbedPane jTabbedPane = null;
    private javax.swing.JPanel psegundo = null;
    private DefaultListModel m_lstModel;
    private JFileChooser fileChooser;

	private JPanel jPanel = null;
	private JButton btnAceptar = null;
	private JButton jButton = null;
	
	private boolean accepted = false;
	
	private WizardListener wizardListener = new DialogWizardListener();
    /**
     * Creates a new FOpenDialog object.
     *
     * @param view Vista que vamos a refrescar
     * @param mapControl MapControl que recibe la capa (te puede interesar
     *        añadirla al principal o al Overview.
     */
    public FOpenDialog() {
    	initialize();
    	
        // Cada vez que se abre, se borran las capas que pudiera haber.
        m_lstModel = new DefaultListModel(); 
    }

    /**
     * This method initializes this
     */
    private void initialize() {
        this.setLayout(new BorderLayout());
        this.setSize(523, 385);
        this.setPreferredSize(new java.awt.Dimension(523, 385));
        this.add(getJTabbedPane(), BorderLayout.CENTER);
        this.add(getJPanel(), java.awt.BorderLayout.SOUTH);
    }

    /**
     * This method initializes jTabbedPane
     *
     * @return javax.swing.JTabbedPane
     */
    private javax.swing.JTabbedPane getJTabbedPane() {
        if (jTabbedPane == null) {
            jTabbedPane = new javax.swing.JTabbedPane();
            jTabbedPane.setBounds(0, 0, getViewInfo().getWidth()-10, getViewInfo().getHeight()-10);
            jTabbedPane.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					JTabbedPane tabs = (JTabbedPane) e.getSource();
					getBtnAceptar().setEnabled(!(tabs.getSelectedComponent() instanceof WizardPanel));
				}
			});
        }

        return jTabbedPane;
    }

    public void addTab(String title, JPanel panel){
    	getJTabbedPane().addTab(title, panel);
    }
    
    public void addWizardTab(String title, WizardPanel panel){
    	panel.addWizardListener(wizardListener);
    	getJTabbedPane().addTab(title, panel);
    }
    
    public JPanel getSelectedTab(){
    	return (JPanel) getJTabbedPane().getSelectedComponent();
    }
    
    public ViewInfo getViewInfo() {
    	ViewInfo m_viewinfo = new ViewInfo(ViewInfo.MODALDIALOG);
   		m_viewinfo.setTitle(PluginServices.getText(this,"Abrir_una_capa"));
   		 m_viewinfo.setHeight(500);
   		 m_viewinfo.setWidth(520);
        return m_viewinfo;
    }

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJPanel() {
		if (jPanel == null) {
			FlowLayout flowLayout1 = new FlowLayout();
			jPanel = new JPanel();
			jPanel.setLayout(flowLayout1);
			flowLayout1.setHgap(30);
			jPanel.add(getBtnAceptar(), null);
			jPanel.add(getJButton(), null);
		}
		return jPanel;
	}
	/**
	 * This method initializes btnAceptar	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton();
			btnAceptar.setText(PluginServices.getText(this,"Aceptar"));
			btnAceptar.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {
					accepted = true;
                    if (PluginServices.getMainFrame() == null) {
                        ((JDialog) (getParent().getParent().getParent()
                                .getParent())).dispose();
                    } else {
                        PluginServices.getMDIManager().closeView((View) FOpenDialog.this);
                    }
				}
			});
		}
		return btnAceptar;
	}
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setText(PluginServices.getText(this,"Cancelar"));
			jButton.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
                    if (PluginServices.getMainFrame() != null) {
                        PluginServices.getMDIManager().closeView((View) FOpenDialog.this);
                    } else {
                        ((JDialog) (getParent().getParent().getParent()
                                        .getParent())).dispose();
                    }
				}
			});
		}
		return jButton;
	}
	public boolean isAccepted() {
		return accepted;
	}
	
	public class DialogWizardListener implements WizardListener {

		/**
		 * @see com.iver.cit.gvsig.gui.wms.WizardListener#error(java.lang.Exception)
		 */
		public void error(Exception e) {
		}

		/**
		 * @see com.iver.cit.gvsig.gui.wms.WizardListener#wizardStateChanged(boolean)
		 */
		public void wizardStateChanged(boolean finishable) {
			getBtnAceptar().setEnabled(finishable);
		}
		
	}
	public static IProjection getLastProjection() {
		return proj;
	}
	public static void setLastProjection(IProjection proj) {
		FOpenDialog.proj = proj;
	}
   }  //  @jve:decl-index=0:visual-constraint="10,10"
 //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
//  @jve:visual-info  decl-index=0 visual-constraint="10,10"
