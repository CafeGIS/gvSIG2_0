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
 * $Id: ProgressDialog.java 22756 2008-08-07 09:49:05Z vcaballero $
 * $Log$
 * Revision 1.2  2007-09-12 16:28:23  bsanchez
 * *** empty log message ***
 *
 * Revision 1.1  2007/08/20 08:34:45  evercher
 * He fusionado LibUI con LibUIComponents
 *
 * Revision 1.6  2006/11/28 13:00:54  jmvivo
 * quitadas dependencias de andami
 *
 * Revision 1.5  2006/09/14 08:31:58  cesar
 * Replace PluginServices.getText by the new Messages bridge class from libUI
 *
 * Revision 1.4.2.1  2006/09/13 13:13:19  cesar
 * Replace PluginServices.getText by the new Messages bridge class from libUI
 *
 * Revision 1.4  2006/08/29 07:56:08  cesar
 * Rename the *View* family of classes to *Window* (ie: SingletonView to SingletonWindow, ViewInfo to WindowInfo, etc)
 *
 * Revision 1.3  2006/06/15 15:47:25  jaume
 * *** empty log message ***
 *
 * Revision 1.2  2006/05/19 06:27:09  jaume
 * *** empty log message ***
 *
 * Revision 1.1  2006/05/17 17:20:11  jaume
 * *** empty log message ***
 *
 *
 */
package org.gvsig.gui.beans;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/** 
 * It is aimed to have an easy way to show a progres bar dialog. 
 * Unfortunatelly, it is not finished. Next step would be to
 * let ProgressDialog implement ProgressListener, to completely
 * encapsulate the component. Until then, the user has to call
 * setProgress(int) to make the bar grow up.
 * 
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 *
 */
public class ProgressDialog extends JDialog {  
  private static final long serialVersionUID = 2325230864829072756L;
	private JProgressBar jProgressBar = null;
	private JButton btnCancel = null;
	private JLabel lblStatus = null;
	private JLabel lblTask = null;
	private String jobName;
	private CancellableComponent cc;
	private String textMessage;

	public ProgressDialog(CancellableComponent owner, String jobName, int stepAmount) {
		this(owner, jobName, jobName, stepAmount);
	}
	
	public ProgressDialog(CancellableComponent owner, String jobName, String textMessage, int stepAmount) {
		super();
		setTitle(jobName);
		this.textMessage = textMessage;
		getJProgressBar().setMinimum(0);
		getJProgressBar().setMaximum(stepAmount);
		cc = owner;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		lblTask = new JLabel();
		lblTask.setBounds(10, 12, 280, 20);
		lblTask.setFont(new java.awt.Font("MS Sans Serif", java.awt.Font.BOLD, 11));
		lblTask.setText(textMessage);
		lblStatus = new JLabel();
		lblStatus.setBounds(10, 63, 280, 20);
		this.getContentPane().setLayout(null);
		this.setSize(308, 168);
		this.getContentPane().add(getJProgressBar(), null);
		this.getContentPane().add(getBtnCancel(), null);
		this.getContentPane().add(lblStatus, null);
		this.getContentPane().add(lblTask, null);
	}

	/**
	 * This method initializes jProgressBar	
	 * 	
	 * @return javax.swing.JProgressBar	
	 */    
	private JProgressBar getJProgressBar() {
		if (jProgressBar == null) {
			jProgressBar = new JProgressBar();
			jProgressBar.setBounds(10, 38, 280, 20);
		}
		return jProgressBar;
	}

	/**
	 * This method initializes btnCancel	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new JButton();
			btnCancel.setBounds(210, 96, 80, 20);
			btnCancel.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {
					//NotificationManager.addInfo("The job was cancelled", null);
					if (cc!=null)
						cc.cancel();
				}
			});
			btnCancel.setText(Messages.getText("cancel"));
		}
		return btnCancel;
	}
	
	public void setProgress(int step) {
		jProgressBar.setValue(step);
	}
	
	public void setStatusMessage(String message) {
		lblStatus.setText(message);
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
