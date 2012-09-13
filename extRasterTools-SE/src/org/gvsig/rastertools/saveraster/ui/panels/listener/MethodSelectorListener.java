/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
*
* Copyright (C) 2007 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.rastertools.saveraster.ui.panels.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.gvsig.rastertools.saveraster.ui.panels.MethodSelectorPanel;
import org.gvsig.rastertools.saveraster.ui.panels.SelectionParamsPanel;

/**
 * Listener para la gestión del método de selección del tamaño de la salida. Este
 * listener se encarga de que cuando se varia el JRadioButton se activen y desactiven
 * los controles correspondientes.
 *  
 * @version 18/04/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class MethodSelectorListener implements ActionListener, ChangeListener, MouseListener, FocusListener,
KeyListener {

	private SelectionParamsPanel            selectionParamsPanel = null;
	private MethodSelectorPanel             methodSelectorPanel = null;
		
	/**
	 * This is the default constructor
	 */
	public MethodSelectorListener(SelectionParamsPanel panel) {
		this.selectionParamsPanel = panel;
		methodSelectorPanel = panel.getSelectorPanel();
		methodSelectorPanel.getRbScale().addActionListener(this);
		methodSelectorPanel.getRbSize().addActionListener(this);
		methodSelectorPanel.getRbMtsPixel().addActionListener(this);
	}

	//*******************************
	//ChangeListener
	
	public void focusGained(FocusEvent e) {		
	}

	public void focusLost(FocusEvent e) {
	}
	
	public void stateChanged(ChangeEvent e) {
	}
	
	//*******************************
	//MouseListener
	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {		
	}

	public void mouseReleased(MouseEvent e) {		
	}

	public void keyPressed(KeyEvent e) {		
	}

	public void keyReleased(KeyEvent e) {		
	}

	public void keyTyped(KeyEvent e) {		
	}

	//*******************************
	//ActionListener
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == methodSelectorPanel.getRbScale()) {
			selectionParamsPanel.getInputScalePanel().setActiveScale(0);
			selectionParamsPanel.getInputSizePanel().setActive(false);
		}
		
		if(e.getSource() == methodSelectorPanel.getRbMtsPixel()) {
			selectionParamsPanel.getInputScalePanel().setActiveScale(1);
			selectionParamsPanel.getInputSizePanel().setActive(false);
		}
		
		if(e.getSource() == methodSelectorPanel.getRbSize()) {
			selectionParamsPanel.getInputScalePanel().setActiveScale(2);
			selectionParamsPanel.getInputSizePanel().setActive(true);
		}
	}

}
