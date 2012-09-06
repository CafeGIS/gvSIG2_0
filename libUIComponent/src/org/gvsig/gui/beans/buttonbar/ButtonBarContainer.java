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
package org.gvsig.gui.beans.buttonbar;

import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ButtonBarContainer extends JPanel {
	private static final long serialVersionUID = -2556987128553063939L;

	private ArrayList<JButton> buttons = new ArrayList<JButton>();

	private int       wComp              = 400;
	private int       hComp              = 26;
	private String    pathToImages       = "images/";
	private boolean   disableAllControls = false;
	private boolean[] buttonsState       = null;


	/**
	 * This is the default constructor
	 */
	public ButtonBarContainer() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setHgap(0);
		flowLayout.setVgap(0);
		this.setLayout(flowLayout);
		this.setSize(wComp, hComp);
		}


	/**
	 * Añade un boton al ArrayList de los botones.
	 *
	 * @param iconName: nombre del icono asignado al boton. La imagen tendrña que
	 * 					estar dentro de la carpeta "images/"
	 * @param tip: tip del boton;
	 * @param order: orden que ocupará el boton dentro del control
	 */
	public void addButton(String iconName, String tip, int order){
		JButton bt = new JButton();

		bt.setPreferredSize(new java.awt.Dimension(22, 22));
		try{
			if (iconName != null)
				bt.setIcon(new ImageIcon(getClass().getResource(pathToImages + iconName)));
		}catch(NullPointerException exc){
			//El icono no existe -> No se añade ninguno
		}

		if(tip != null)
			bt.setToolTipText(tip);

		buttons.add(order, bt);
		addList();

	}

	/**
	 * Elimina el botón correspondiente al indice que le pasamos.
	 * @param index
	 */
	public void delButton(int index){
		buttons.remove(index);
		this.removeAll();
		addList();
	}

	/**
	 * Añade en el panel los botones que tenemos en el ArrayList.
	 *
	 */
	public void addList(){
		for(int i = 0 ; i < buttons.size() ; i++){
			this.add((JButton)buttons.get(i));
		}
	}


	/**
	 * Esta función deshabilita todos los controles y guarda sus valores
	 * de habilitado o deshabilitado para que cuando se ejecute restoreControlsValue
	 * se vuelvan a quedar como estaba
	 */
	public void disableAllControls(){
		if(!disableAllControls){
			disableAllControls = true;

			buttonsState = new boolean[buttons.size()];



			for (int i = 0 ; i < buttons.size() ; i++){

				//Salvamos los estados
				buttonsState[i] = ((JButton)buttons.get(i)).isEnabled();
				//Desactivamos controles
				((JButton)buttons.get(i)).setEnabled(false);
			}
		}
	}

	/**
	 * Esta función deja los controles como estaban al ejecutar la función
	 * disableAllControls
	 */
	public void restoreControlsValue(){
		if(disableAllControls){
			disableAllControls = false;

			for(int i = 0 ; i < buttons.size() ; i++){
				((JButton)buttons.get(i)).setEnabled(buttonsState[i]);
			}
		}
	}

	/**
	 * Método para acceder a los botones del control;
	 * @param index
	 * @return
	 */
	public JButton getButton(int index){
		return (JButton)buttons.get(index);
	}

	/**
	 * Método para establecer la posición de los botones dentro del control.
	 * @param align: "left" o "right"
	 */
	public void setButtonAlignment(String align){
		FlowLayout layout = new FlowLayout();
		layout.setHgap(2);
		layout.setVgap(0);

		if (align.equals("right"))
			layout.setAlignment(FlowLayout.RIGHT);
		else
			layout.setAlignment(FlowLayout.LEFT);

		this.setLayout(layout);
	}

	public void setComponentBorder(boolean br){
		if(br)
			this.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
		if(!br)
			this.setBorder(javax.swing.BorderFactory.createEmptyBorder());
	}

}
