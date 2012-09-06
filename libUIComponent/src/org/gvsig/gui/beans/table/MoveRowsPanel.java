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
package org.gvsig.gui.beans.table;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.gvsig.gui.beans.Messages;
import org.gvsig.gui.beans.table.listeners.TableListener;
import org.gvsig.gui.util.StatusComponent;

public class MoveRowsPanel extends JPanel {
  private static final long serialVersionUID = -4496318143555472677L;

	private int             HEIGHT_BUTTONS  = 19;       // 16 estaria bien
	private JButton         bUp             = null;
	private JButton         bDown           = null;
	private int             selected        = -1;
	private int             cont            = 0;
	private String          pathToImages    = "images/"; // "/com/iver/cit/gvsig/gui/panels/images/";

	/**
	 * Objeto para controlar el estado de los componentes visuales 
	 */
	private StatusComponent statusComponent = null;
	
	/**
	 * This is the default constructor
	 */
	public MoveRowsPanel(TableListener tableListener) {
		initialize(tableListener);
	}
	
	private void initialize(TableListener tableListener) {
		statusComponent = new StatusComponent(this);

		setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 1, 0));
		add(getBUp());
		add(getBDown());

		getBUp().addActionListener(tableListener);
		getBDown().addActionListener(tableListener);
	}

	public void setSelectedIndex(int i, int cont) {
		selected = i;
		this.cont = cont;
		checkArrows();
	}
	
	/**
	 * Comprueba la posición del combo para ver si tiene que
	 * habilitar o deshabilitar las flechas de delante y detrás.
	 */
	private void checkArrows(){
		if (!statusComponent.isEnabled())
			return;

		if (selected == -1) {
			getBUp().setEnabled(false);
			getBDown().setEnabled(false);
			return;
		}

		if (selected == 0) {
			getBUp().setEnabled(false);
		} else {
			getBUp().setEnabled(true);
		}

		if (selected == (cont - 1)) {
			getBDown().setEnabled(false);
		} else {
			getBDown().setEnabled(true);
		}
	}	

	/**
	 * This method initializes bUp
	 * @return javax.swing.JButton
	 */
	public JButton getBUp() {
		if (bUp == null) {
			bUp = new JButton("");
			bUp.setEnabled(true);
			bUp.setPreferredSize(new Dimension(22, HEIGHT_BUTTONS));
			bUp.setIcon(new ImageIcon(getClass().getResource(pathToImages + "up-16x16.png")));
			bUp.setActionCommand("");
			bUp.setToolTipText(Messages.getText("subir"));
		}
		return bUp;
	}
	

	/**
	 * This method initializes bDown
	 * @return javax.swing.JButton
	 */
	public JButton getBDown() {
		if (bDown == null) {
			bDown = new JButton("");
			bDown.setEnabled(true);
			bDown.setPreferredSize(new Dimension(22, HEIGHT_BUTTONS));
			bDown.setIcon(new ImageIcon(getClass().getResource(pathToImages + "down-16x16.png")));
			bDown.setActionCommand("");
			bDown.setToolTipText(Messages.getText("bajar"));
		}
		return bDown;
	}
	
	/**
	 * Esta función deshabilita todos los controles y guarda sus valores
	 * de habilitado o deshabilitado para que cuando se ejecute restoreControlsValue
	 * se vuelvan a quedar como estaba
	 */
	public void disableAllControls(){
		statusComponent.setEnabled(false);
	}

	/**
	 * Esta función deja los controles como estaban al ejecutar la función
	 * disableAllControls
	 */
	public void restoreControlsValue(){
		statusComponent.setEnabled(true);
	}	
}