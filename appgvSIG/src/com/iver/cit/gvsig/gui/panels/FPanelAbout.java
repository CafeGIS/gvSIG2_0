/*
 * Created on 02-sep-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
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
package com.iver.cit.gvsig.gui.panels;

import java.awt.BorderLayout;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.HyperlinkEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.Version;
import com.iver.utiles.BrowserControl;
/**
 * @author FJP
 *
 * A Window with panels showing web pages. A developer
 * can write a web page with information about his/her plugin
 * and call addAboutUrl to add his/her about to gvSIG's about and
 * others.
 */
public class FPanelAbout extends JPanel implements IWindow {
    private static final Logger logger = LoggerFactory
    .getLogger(FPanelAbout.class);
    
	private JEditorPane jEditorPane = null;
	private JScrollPane jScrollPane = null;
	private JEditorPane jEditorPane1 = null;
	private JPanel jPanel = null;
	private JButton jButton = null;

	private JLabel jLblVersion = null;
	private JLabel jLblJavaVersion = null;
    private JTabbedPane jTabbedPane = null;
	/**
	 * This is the default constructor
	 * @throws FileNotFoundException
	 */
	public FPanelAbout(){
		super();
		initialize();
	}
	/**
	 * This method initializes this
	 *
	 * @return void
	 * @throws FileNotFoundException
	 */
	private  void initialize(){
		this.setLayout(new BorderLayout());
		this.setSize(600, 450);
		this.add(getJTabbedPane(), java.awt.BorderLayout.CENTER);

		this.add(getJPanel(), java.awt.BorderLayout.SOUTH);
	}

	/**
	 * This method initializes jScrollPane
	 *
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane(URL url) {
		// if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setPreferredSize(new java.awt.Dimension(300,400));
			jScrollPane.setViewportView(getJEditorPane(url));
		// }
		return jScrollPane;
	}
	/**
	 * This method initializes jEditorPane1
	 *
	 * @return javax.swing.JEditorPane
	 */
		private JEditorPane getJEditorPane(URL aboutURL) {
			// if (jEditorPane == null) {
				jEditorPane = new JEditorPane();

				jEditorPane.setEditable(false);
				jEditorPane.setContentType("text/html");
				jEditorPane.setPreferredSize(new java.awt.Dimension(300,200));

				if (aboutURL != null) {
				    try {
				    	jEditorPane.setPage(aboutURL);
				    	jEditorPane.addHyperlinkListener(new javax.swing.event.HyperlinkListener() {
				    		public void hyperlinkUpdate(javax.swing.event.HyperlinkEvent e) {
				    			if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
				    			{
				    	 		      JEditorPane pane = (JEditorPane) e.getSource();
				    	 		      System.out.println("hyperlinkUpdate()"); // TODO Auto-generated Event stub hyperlinkUpdate()
				    	 		      BrowserControl.displayURL(e.getURL().toString());
				    	 		      // if (e instanceof HTMLFrameHyperlinkEvent) {
				    			}


				    		}
				    	});
				    } catch (IOException e) {
				        System.err.println("Attempted to read a bad URL: " + aboutURL);
				    }
				} else {
				    System.err.println("Couldn't find file: about.html" + aboutURL.getPath());
				}

			// }
			return jEditorPane;
		}
	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jLblVersion = new JLabel();
			jLblJavaVersion = new JLabel();
			jPanel = new JPanel();
			jPanel.setLayout(null);
			jPanel.setPreferredSize(new java.awt.Dimension(10,50));
			jLblVersion.setBounds(10, 16, 205, 17);
			jLblVersion.setText("Version "+Version.longFormat());
			jLblJavaVersion.setBounds(415, 16, 150, 17);
			jLblJavaVersion.setText("Java "+System.getProperties().get("java.version"));
			jPanel.add(getJButton(), null);
			jPanel.add(jLblVersion, null);
			jPanel.add(jLblJavaVersion, null);
		}
		return jPanel;
	}
	/**
	 * This method initializes jButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			jButton.setText(PluginServices.getText(this,"Cerrar"));
			jButton.setBounds(266, 12, 94, 25);
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
					if (PluginServices.getMainFrame() != null)
					{
						PluginServices.getMDIManager().closeWindow(FPanelAbout.this);
					}
					else
					{
						((JDialog) (getParent().getParent().getParent().getParent())).dispose();
					}

				}
			});
		}
		return jButton;
	}
	/* (non-Javadoc)
	 * @see com.iver.mdiApp.ui.MDIManager.View#getViewInfo()
	 */
	public WindowInfo getWindowInfo() {
		WindowInfo m_ViewInfo = new WindowInfo(WindowInfo.MODALDIALOG|WindowInfo.RESIZABLE);
		m_ViewInfo.setTitle(PluginServices.getText(this,"acerca_de"));

		return m_ViewInfo;
	}
	/* (non-Javadoc)
	 * @see com.iver.mdiApp.ui.MDIManager.View#viewActivated()
	 */
	public void viewActivated() {
		// TODO Auto-generated method stub

	}
    public void addAboutUrl(String pluginName, URL url)
    {
        getJTabbedPane().addTab(pluginName, getJScrollPane(url));
        // this.add(getJScrollPane(url), java.awt.BorderLayout.CENTER);
    }
    /**
     * This method initializes jTabbedPane
     *
     * @return javax.swing.JTabbedPane
     */
    private JTabbedPane getJTabbedPane() {
    	if (jTabbedPane == null) {
    		jTabbedPane = new JTabbedPane();
    		jTabbedPane.setPreferredSize(new java.awt.Dimension(5,50));
    	}
    	return jTabbedPane;
    }
	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
