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
 */
package com.iver.cit.gvsig.addlayer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.cresques.cts.IProjection;
import org.gvsig.gui.beans.AcceptCancelPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.gui.WizardPanel;
import com.iver.cit.gvsig.gui.wizards.WizardListener;
import com.iver.cit.gvsig.project.Project;
/**
 * Frame del cuadro de dialogo que contendra los tabs de aperturas de ficheros
 *
 * @version 04/09/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class AddLayerDialog extends JPanel implements com.iver.andami.ui.mdiManager.IWindow {
	static private IProjection proj           = null;
	private JTabbedPane        jTabbedPane    = null;
	private AcceptCancelPanel  jPanel         = null;
	private boolean            accepted       = false;
	private String             title          = PluginServices.getText(this, "add_layer");
	private WizardListener     wizardListener = new DialogWizardListener();

	/**
   * Creates a new FOpenDialog object.
   * @param view Vista que vamos a refrescar
   * @param mapControl MapControl que recibe la capa (te puede interesar
   *          añadirla al principal o al Overview.
   */
	public AddLayerDialog() {
		initialize();
	}

	/**
	 * Constructor con un nombre de Frame
	 * @param title
	 */
	public AddLayerDialog(String title) {
		this.title = title;
		initialize();
	}

	/**
   * This method initializes this
   */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setSize(523, 385);
		this.setPreferredSize(new Dimension(523, 385));
		this.add(getJTabbedPane(), BorderLayout.CENTER);
		this.add(getJPanel(), BorderLayout.SOUTH);
	}

	/**
   * This method initializes jTabbedPane
   * @return javax.swing.JTabbedPane
   */
	private JTabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();
			jTabbedPane.setBounds(0, 0, getWindowInfo().getWidth() - 10, getWindowInfo().getHeight() - 10);
			jTabbedPane.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					JTabbedPane tabs = (JTabbedPane) e.getSource();
					getJPanel().setOkButtonEnabled(!(tabs.getSelectedComponent() instanceof WizardPanel));
				}
			});
		}

		return jTabbedPane;
	}

	/**
	 * Añade en una pestaña un Jpanel con un titulo
	 * @param title
	 * @param panel
	 */
	public void addTab(String title, JPanel panel) {
		getJTabbedPane().addTab(title, panel);
	}

	/**
	 * Añade en una pestaña un WizardPanel con un titulo
	 * @param title
	 * @param panel
	 */
	public void addWizardTab(String title, WizardPanel panel) {
		panel.addWizardListener(wizardListener);
		getJTabbedPane().addTab(title, panel);
	}

	/**
	 * Devuelve el JPanel seleccionado en las pestañas
	 * @return
	 */
	public JPanel getSelectedTab() {
		return (JPanel) getJTabbedPane().getSelectedComponent();
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.ui.mdiManager.IWindow#getWindowInfo()
	 */
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODALDIALOG);
		m_viewinfo.setTitle(this.title);
		m_viewinfo.setHeight(500);
		m_viewinfo.setWidth(520);
		return m_viewinfo;
	}

	/**
   * This method initializes jPanel
   * @return javax.swing.JPanel
   */
	private AcceptCancelPanel getJPanel() {
		if (jPanel == null) {
			ActionListener okAction = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					accepted = true;
					if (PluginServices.getMainFrame() == null) {
						((JDialog) (getParent().getParent().getParent().getParent())).dispose();
					} else {
						PluginServices.getMDIManager().closeWindow(AddLayerDialog.this);
					}
				}
			};
			ActionListener cancelAction = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (PluginServices.getMainFrame() != null) {
						PluginServices.getMDIManager().closeWindow(AddLayerDialog.this);
					} else {
						((JDialog) (getParent().getParent().getParent().getParent())).dispose();
					}
				}
			};
			jPanel = new AcceptCancelPanel(okAction, cancelAction);
		}
		return jPanel;
	}

	/**
	 * @return
	 */
	public boolean isAccepted() {
		return accepted;
	}

	/**
	 * Listener para el Wizard de apertura de fichero
	 * @version 05/09/2007
	 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
	 */
	public class DialogWizardListener implements WizardListener {
		/*
		 * (non-Javadoc)
		 * @see com.iver.cit.gvsig.gui.wizards.WizardListener#error(java.lang.Exception)
		 */
		public void error(Exception e) {}

		/*
		 * (non-Javadoc)
		 * @see com.iver.cit.gvsig.gui.wizards.WizardListener#wizardStateChanged(boolean)
		 */
		public void wizardStateChanged(boolean finishable) {
			getJPanel().setOkButtonEnabled(finishable);
		}
	}

	/**
	 * Devuelve la ultima proyección usada
	 * @return
	 */
	public static IProjection getLastProjection() {
		if (proj == null) {
			proj = Project.getDefaultProjection();
		}
		return proj;
	}

	/**
	 * Define la ultima proyeccion
	 * @param proj
	 */
	public static void setLastProjection(IProjection proj) {
		AddLayerDialog.proj = proj;
	}

	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}
}