/*
 * Created on 10-mar-2006
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
 * $Id: UndefinedProgressMonitor.java 8515 2006-11-06 07:30:00Z jaume $
 * $Log$
 * Revision 1.4  2006-11-06 07:29:59  jaume
 * organize imports
 *
 * Revision 1.3  2006/04/18 15:16:14  azabala
 * añadida la posibilidad de personalizar el titulo del dialogo de proceso
 *
 * Revision 1.2  2006/03/20 16:04:21  azabala
 * *** empty log message ***
 *
 * Revision 1.1  2006/03/14 19:23:58  azabala
 * *** empty log message ***
 *
 *
 */
package com.iver.utiles.swing.threads;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * Dialog that shows the evolution of the execution of a
 * ITask.
 * If this ITask is a defined task (we know the number of steps
 * it must do) it shows a progress bar.
 * 
 * If it doesnt, progress bar is filling and emptying
 * @author azabala
 *
 */
public class UndefinedProgressMonitor extends JDialog 
	implements IProgressMonitorIF{

	
	private static final long serialVersionUID = 8776505862813807891L;
	private JPanel jContentPane = null;
	private JLabel mainTitleLabel = null;
	private JLabel noteLabel = null;
	private JProgressBar jProgressBar = null;
	private JButton cancelButton = null;
	boolean canceled = false;
	private String title = "Processing...";

	/**
	 * This is the default constructor
	 */
	public UndefinedProgressMonitor() {
		super();
		initialize();
	}
	/**
	 * Constructor which specify the dialog title 
	 * (for example:processing, etc)
	 * @param parent
	 * @param title
	 */
	public UndefinedProgressMonitor(Frame parent, String title){
		super(parent, false);
		this.title = title;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 * 
	 * FIXME Internationalize this
	 */
	private void initialize() {
		this.setSize(318, 181);
		this.setTitle(title);
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.insets = new java.awt.Insets(6,97,18,125);
			gridBagConstraints3.gridy = 3;
			gridBagConstraints3.ipadx = 4;
			gridBagConstraints3.ipady = -4;
			gridBagConstraints3.gridx = 0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.insets = new java.awt.Insets(11,43,5,32);
			gridBagConstraints2.gridy = 2;
			gridBagConstraints2.ipadx = 109;
			gridBagConstraints2.ipady = 4;
			gridBagConstraints2.fill = java.awt.GridBagConstraints.NONE;
			gridBagConstraints2.gridwidth = 2;
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.CENTER;
			gridBagConstraints2.gridx = 0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.insets = new java.awt.Insets(9,43,8,32);
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.ipadx = 180;
			gridBagConstraints1.ipady = 0;
			gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.gridx = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.insets = new java.awt.Insets(10,43,8,32);
			gridBagConstraints.gridy = 0;
			gridBagConstraints.ipadx = 148;
			gridBagConstraints.ipady = 11;
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.gridx = 0;
			noteLabel = new JLabel();
			noteLabel.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
			noteLabel.setText("noteLabel");
			mainTitleLabel = new JLabel();
			mainTitleLabel.setText("mainTittleLabel");
			mainTitleLabel.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 12));
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(mainTitleLabel, gridBagConstraints);
			jContentPane.add(noteLabel, gridBagConstraints1);
			jContentPane.add(getJProgressBar(), gridBagConstraints2);
			jContentPane.add(getCancelButton(), gridBagConstraints3);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jProgressBar	
	 * 	
	 * @return javax.swing.JProgressBar	
	 */
	private JProgressBar getJProgressBar() {
		if (jProgressBar == null) {
			jProgressBar = new JProgressBar();
		}
		return jProgressBar;
	}

	/**
	 * This method initializes cancelButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setText("cancelar");
			cancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					cancel();
				}
			});
			cancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					cancel();
				}
			});
		}
		return cancelButton;
	}

	public void setInitialStep(int step) {
		jProgressBar.setMinimum(step);
	}

	public void setLastStep(int step) {
		jProgressBar.setMaximum(step);
	}

	public void setCurrentStep(int step) {
		jProgressBar.setValue(step);
	}

	public int getInitialStep() {
		return jProgressBar.getMinimum();
	}

	public int getLastStep() {
		return jProgressBar.getMaximum();
	}

	public int getCurrentStep() {
		return jProgressBar.getValue();
	}

	public void setIndeterminated(boolean indeterminated) {
		jProgressBar.setIndeterminate(indeterminated);
	}

	public boolean isIndeterminated() {
		return jProgressBar.isIndeterminate();
	}

	public void setBarStringDrawed(boolean stringDrawed) {
		jProgressBar.setStringPainted(stringDrawed);
	}

	public void setBarString(String barString) {
		jProgressBar.setString(barString);
	}

	public void setMainTitleLabel(String text) {
		mainTitleLabel.setText(text);
	}

	public void setNote(String note) {
		noteLabel.setText(note);
	}

	public void cancel() {
		canceled = true;
	}

//	public void taskInBackground() {
//		//setModal(false);
//	}

	public boolean isCanceled() {
		return canceled == true;
	}

	public void close() {
		this.dispose();
	}

	public void open() {
		this.setVisible(true);
	}

}  //  @jve:decl-index=0:visual-constraint="30,10"
