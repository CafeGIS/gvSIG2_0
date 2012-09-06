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
package org.gvsig.gui.beans.table.models;

import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.gvsig.gui.beans.slidertext.SliderTextContainer;

/**
 * Ventana con el slider para la selección de Alpha en una entrada de la tabla que representa
 * a la paleta de color.
 *
 * @author Nacho Brodin (brodin_ign@gva.es)
 *
 */

public class JValueSelector extends JPanel {
	private static final long serialVersionUID = -427287107697905904L;

	/**
		 * Creates and returns a new dialog containing the specified
		 * <code>ColorChooser</code> pane along with "OK", "Cancel", and "Reset"
		 * buttons. If the "OK" or "Cancel" buttons are pressed, the dialog is
		 * automatically hidden (but not disposed).  If the "Reset"
		 * button is pressed, the color-chooser's color will be reset to the
		 * color which was set the last time <code>show</code> was invoked on the
		 * dialog and the dialog will remain showing.
		 *
		 * @param c              the parent component for the dialog
		 * @param title          the title for the dialog
		 * @param modal          a boolean. When true, the remainder of the program
		 *                       is inactive until the dialog is closed.
		 * @param valueSelector  the value-selector to be placed inside the dialog
		 * @param okListener     the ActionListener invoked when "OK" is pressed
		 * @param cancelListener the ActionListener invoked when "Cancel" is pressed
		 * @return a new dialog containing the color-chooser pane
		 * @exception HeadlessException if GraphicsEnvironment.isHeadless()
		 * returns true.
		 * @see java.awt.GraphicsEnvironment#isHeadless
		 */
		public static JDialog createDialog(Component c, String title, boolean modal,
				JValueSelector valueSelector, ActionListener okListener,
				ActionListener cancelListener, String initValue) throws HeadlessException {

				return new ValueSelector(c, title, modal, valueSelector,
																								okListener, cancelListener, initValue);
		}

}

class ValueSelector extends JDialog implements ActionListener {
	private static final long serialVersionUID = -1510744508318912657L;
	private int	 				initValue;
	private ActionListener			okListener;
	private ActionListener			cancelListener;

	private int 					HBUTTONS = 30;
	private int 					HSLIDER = 50;
	private JPanel					buttons = null;
	private JPanel					pMain = null;
	private SliderTextContainer 	pSlider = null;
	private int 					width = 315, height = 115;
	private int 					widthMargin = 300;
	private JButton				bAccept = null;
	private JButton				bCancel = null;

	/**
	 * Constructor
	 * @param pp PalettePanel
	 */
	public ValueSelector(Component c, String title, boolean modal,
					JValueSelector valueSelector, ActionListener okListener,
					ActionListener cancelListener, String initValue)throws HeadlessException {

				super(JOptionPane.getFrameForComponent(c), title, modal);
				this.okListener = okListener;
				this.cancelListener = cancelListener;

				try{
					init(Integer.valueOf(initValue).intValue());
					this.initValue = Integer.valueOf(initValue).intValue();
				}catch(NumberFormatException ex){
					init(0);
				}

				setLocationRelativeTo(c);
	}

	public void init(int initValue){
		getPSlider().setValue(initValue);

		Container contentPane = getContentPane();
		FlowLayout layout = new FlowLayout();
		layout.setHgap(0);
		layout.setVgap(0);
				contentPane.setLayout(layout);
				setSize(width, height + 10);
				contentPane.add(getPMain(), null);
	}

	/**
	 * Asigna el valor de inicialización para el Slider
	 * @param value
	 */
	public void setValue(int value){
			this.initValue = value;
			this.getPSlider().setValue((double)value);
		}

	/**
	 * Asigna el valor de inicialización para el Slider
	 * @param value
	 */
	public double getValue(){
		 return initValue;
		}

	/**
	 * Obtiene el panel principal que contiene el panel de botones y el panel
	 * con el slider
	 * @return JPanel
	 */
	private JPanel getPMain(){
		if(pMain == null){
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridy = 0;
			gridBagConstraints.gridx = 0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.gridx = 0;
			pMain = new JPanel();
			pMain.setLayout(new GridBagLayout());
			pMain.setPreferredSize(new java.awt.Dimension(widthMargin, height));
			pMain.add(getPSlider(), gridBagConstraints);
			pMain.add(getPButtons(), gridBagConstraints1);
		}
		return pMain;
	}

	/**
	 * Obtiene el panel de botones
	 * @return JPanel
	 */
	private JPanel getPButtons(){
		if(buttons == null){
			buttons = new JPanel();
			buttons.setPreferredSize(new java.awt.Dimension(widthMargin, HBUTTONS));
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(java.awt.FlowLayout.RIGHT);
			flowLayout.setVgap(0);
			flowLayout.setHgap(2);
			buttons.setLayout(flowLayout);
			buttons.add(getBAccept(), null);
			buttons.add(getBCancel(), null);
		}
		return buttons;
	}

	/**
	 * Obtiene el panel con el slider
	 * @return
	 */
	public SliderTextContainer getPSlider(){
		if(pSlider == null){
			pSlider = new SliderTextContainer(0, 255, this.initValue);
			pSlider.setPreferredSize(new java.awt.Dimension(widthMargin, HSLIDER));
		}
		return pSlider;
	}

	/**
	 * Obtiene el botón de aceptar
	 * @return JButton
	 */
	public JButton getBAccept(){
		if(bAccept == null){
			bAccept = new JButton("aceptar");
			bAccept.addActionListener(okListener);
			bAccept.addActionListener(this);
		}
		return bAccept;
	}

	/**
	 * Obtiene el botón de cancelar
	 * @return JButton
	 */
	public JButton getBCancel(){
		if(bCancel == null){
			bCancel = new JButton("cancelar");
			bAccept.addActionListener(cancelListener);
			bCancel.addActionListener(this);
		}
		return bCancel;
	}

	/**
	 * Al aceptar asignamos el valor del slider al botón de alpha y al cancelar cerramos la ventana
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == this.getBAccept()){
			initValue = (int)getPSlider().getValue();
		}
		this.setVisible(false);
	}

}
