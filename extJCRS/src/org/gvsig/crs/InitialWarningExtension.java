/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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

/* CVS MESSAGES:
*
* $Id: InitialWarningExtension.java 8765 2006-11-15 00:08:29Z jjdelcerro $
* $Log$
* Revision 1.3.4.1  2006-11-15 00:08:08  jjdelcerro
* *** empty log message ***
*
* Revision 1.4  2006/10/02 13:52:34  jaume
* organize impots
*
* Revision 1.3  2006/08/29 07:56:27  cesar
* Rename the *View* family of classes to *Window* (ie: SingletonView to SingletonWindow, ViewInfo to WindowInfo, etc)
*
* Revision 1.2  2006/08/29 07:13:53  cesar
* Rename class com.iver.andami.ui.mdiManager.View to com.iver.andami.ui.mdiManager.IWindow
*
* Revision 1.1  2006/05/25 16:27:21  jaume
* *** empty log message ***
*
*
*/
package org.gvsig.crs;

import java.awt.Color;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.border.TitledBorder;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.WindowInfo;

public class InitialWarningExtension extends Extension {
//	public static Preferences fPrefs = Preferences.userRoot().node( "gvsig.initial_warkning" );
//	private boolean show = fPrefs.getBoolean( "show_panel", true);
	private DlgWaring dlgWarning;
    /**
	 * @see com.iver.andami.plugins.IExtension#isEnabled()
	 */
	public boolean isEnabled() {
		return true;
	}

	/**
	 * @see com.iver.mdiApp.plugins.IExtension#isVisible()
	 */
	public boolean isVisible() {
		return true;
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#initialize()
	 */
	public void initialize() {
		dlgWarning = new DlgWaring();
		dlgWarning.setText(PluginServices.getText(this, "initial_jcrs_warning"));
		dlgWarning.setVisible(true);
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#execute(java.lang.String)
	 */
	public void execute(String actionCommand) {
	}
    
	private class DlgWaring extends JWindow{

		private JButton btnOk = null;
		private JCheckBox chkBoxShowAgain = null;
		private JLabel lblText = null;
		private JPanel panel=null;
		private JPanel panel2;
		/**
		 * This is the default constructor
		 */
		public DlgWaring() {
			super((Window)PluginServices.getMainFrame());
			Window window=(Window)PluginServices.getMainFrame();
			setLocation(window.getWidth()/2+window.getX()-150,window.getHeight()/2+window.getY()-150);
			initialize();
			this.setAlwaysOnTop(true);
		}

		public void setText(String string) {
			lblText.setText(string);
		}

		/**
		 * This method initializes this
		 *
		 * @return void
		 */
		private void initialize() {
			panel=new JPanel();
			panel.setBackground(Color.black);
			panel.setLayout(null);
			panel.setBounds(0,0,300,300);
			panel2=new JPanel();
			panel2.setLayout(null);
			panel2.setBounds(2,2,298,298);
			lblText = new JLabel();
			lblText.setBounds(15, 15, 271, 200);
			lblText.setText("JLabel");
			lblText.setBorder(new TitledBorder(PluginServices.getText(this, "warning")));
			panel2.add(lblText);
			panel2.setSize((int)(panel.getSize().getWidth()-4),(int)(panel.getSize().getHeight()-4));
			panel.add(panel2);
			this.setLayout(null);
			this.setSize(300, 300);
			this.add(getBtnOk(), null);
//			this.add(getChkBoxShowAgain(), null);
			this.add(panel, null);
		}

		/**
		 * This method initializes btnOk
		 *
		 * @return javax.swing.JButton
		 */
		private JButton getBtnOk() {
			if (btnOk == null) {
				btnOk = new JButton();
				btnOk.setBounds(100, 250, 100, 20);
				btnOk.setText(PluginServices.getText(this, "aceptar"));
				btnOk.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						dlgWarning.setVisible(false);
						dlgWarning.dispose();
						//						PluginServices.getMDIManager().closeWindow(dlgWarning);
					}
				});
			}
			return btnOk;
		}

		/**
		 * This method initializes chkBoxShowAgain
		 *
		 * @return javax.swing.JCheckBox
		 */
//		private JCheckBox getChkBoxShowAgain() {
//			if (chkBoxShowAgain == null) {
//				chkBoxShowAgain = new JCheckBox();
//				chkBoxShowAgain.setBounds(44, 116, 241, 21);
//				chkBoxShowAgain.setSelected(true);
//				chkBoxShowAgain.setText(PluginServices.getText(this, "show_this_dialog_next_startup"));
//			}
//			return chkBoxShowAgain;
//		}

		public WindowInfo getWindowInfo() {
			WindowInfo vi = new WindowInfo(WindowInfo.MODALDIALOG);
			vi.setWidth(300+8);
			vi.setHeight(250);
			vi.setTitle(PluginServices.getText(this, "warning"));
			return vi;
		}

	}  //  @jve:decl-index=0:visual-constraint="10,10"

}
