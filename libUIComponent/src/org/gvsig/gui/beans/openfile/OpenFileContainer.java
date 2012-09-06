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
 */
package org.gvsig.gui.beans.openfile;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.gvsig.gui.beans.Messages;
import org.gvsig.gui.beans.openfile.listeners.OpenFileListener;
/**
 *
 * @version 13/07/2007
 */
public class OpenFileContainer extends JPanel {
	private static final long serialVersionUID = 5823371652872582451L;
	private int 					wComp = 500, hComp = 55;
	private int						wButton = 165;
	private int						wText = (int)Math.floor(0.63 * wComp);
	private int						hButton = 22;

	private JButton jButton = null;
	private JTextField tOpen = null;
	private OpenFileListener listener = null;

	private boolean isButtonVisible = true;

	public OpenFileContainer() {
		super();
		listener = new OpenFileListener(this);
		initialize();
		listener.setButton(this.getJButton());
	}

	/**
	 * Constructor
	 * @param WIDTH
	 * Window width
	 * @param HEIGHT
	 * Window height
	 * @param isButtonVisible
	 * If the open button is visible
	 */
	public OpenFileContainer(int WIDTH,int HEIGHT,boolean isButtonVisible){
		super();
		this.isButtonVisible = isButtonVisible;
		this.wComp = WIDTH;
		this.hComp = HEIGHT;
		listener = new OpenFileListener(this);
		initialize();
		listener.setButton(this.getJButton());
	}

	/**
	 * This method initializes this
	 *
	 */
	private void initialize() {
				GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
				gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
				gridBagConstraints1.gridx = 1;
				gridBagConstraints1.gridy = 0;
				gridBagConstraints1.weightx = 10.0;
				gridBagConstraints1.insets = new java.awt.Insets(0,2,0,0);
				GridBagConstraints gridBagConstraints = new GridBagConstraints();
				gridBagConstraints.insets = new java.awt.Insets(0,0,0,2);
				gridBagConstraints.gridy = 0;
				gridBagConstraints.gridx = 0;
				gridBagConstraints.weightx = 1.0;
				this.setLayout(new GridBagLayout());
				this.setSize(new java.awt.Dimension(500,50));
				this.setBorder(javax.swing.BorderFactory.createTitledBorder(null, Messages.getText("open_file"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
				this.add(getJButton(), gridBagConstraints);
				this.add(getTOpen(), gridBagConstraints1);

	}

	/**
	 * This method initializes jButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setPreferredSize(new java.awt.Dimension(wButton,hButton));
			jButton.addActionListener(listener);
			jButton.setSize(wButton, hButton);
			jButton.setText(Messages.getText("abrir..."));
			jButton.setVisible(isButtonVisible);
			getTOpen().setEnabled(isButtonVisible);
		}
		return jButton;
	}

	/**
	 * This method initializes jTextField
	 *
	 * @return javax.swing.JTextField
	 */
	public JTextField getTOpen() {
		if (tOpen == null) {
			tOpen = new JTextField();
			if (isButtonVisible){
				tOpen.setPreferredSize(new java.awt.Dimension(wText,hButton));
			}else{
				tOpen.setPreferredSize(new java.awt.Dimension((int)Math.floor(wComp * 0.95),hButton));
			}
		}
		return tOpen;
	}

	/**
	 * Pone el texto que le pasamos como texto del borde del panel.
	 * @param text
	 */
/*
	public void setBorderText(String text){
		this.borderText = text;
	}
*/


	public void setComponentSize(int w, int h){
		wComp = w;
		hComp = 55;
		wText = (int)Math.floor(0.63 * wComp);

		this.setPreferredSize(new java.awt.Dimension(wComp, hComp));
		if (isButtonVisible){
			this.getTOpen().setPreferredSize(new java.awt.Dimension(wText, hButton));
		}else{
			this.getTOpen().setPreferredSize(new java.awt.Dimension((int)Math.floor(wComp * 0.95), hButton));
		}
	}


	/**
	 * Devuelve el file cuya ruta corresponde con el campo de texto.
	 * @return
	 */
	public File getFile(){
		File fil = null;
		if(this.getTOpen().getText() != ""){
			try{
				fil = new File(this.getTOpen().getText());
			}catch(Exception exc){
				System.err.println("Ruta o archivo no válido");
			}
		}
		return fil;
	}

}