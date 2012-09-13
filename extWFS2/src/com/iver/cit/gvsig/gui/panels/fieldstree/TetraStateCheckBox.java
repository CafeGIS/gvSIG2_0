package com.iver.cit.gvsig.gui.panels.fieldstree;

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
/* CVS MESSAGES:
 *
 * $Id$
 * $Log$
 * Revision 1.1  2006-12-26 09:12:48  ppiqueras
 * Cambiado "atttibutes" en todas las aparaciones en atributos, mÃ©todos, clases, paquetes o comentarios por "fields". (SÃ³lo a aquellas que afectan a clases dentro del proyecto extWFS2). (En este caso se ha cambiado el nombre del paquete aparte de alguno otro nombre que puede haber cambiado).
 *
 * Revision 1.1  2006/10/27 11:33:19  jorpiell
 * Añadida la treetable con los check box para seleccionar los atributos
 *
 *
 */
/**
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeListener;

public class TetraStateCheckBox extends JCheckBox {
	public static final int WHITE = 0;
	public static final int GREY = 1;

	private final TetraStateDecorator model;

	public TetraStateCheckBox(String text, Icon icon, int color) {
		super(text, icon);
		model = new TetraStateDecorator(getModel());
		setModel(model);
	}

	public TetraStateCheckBox(String text){
		this(text,null,WHITE);
	}
	
	public TetraStateCheckBox() {
		this(null, null, WHITE);
	}

	/**
	 * Set the new state to either SELECTED, NOT_SELECTED or DONT_CARE. If state ==
	 * null, it is treated as DONT_CARE.
	 */
	public void setColor(int color) {
		model.setColor(color, isSelected());
	}

	/**
	 * Return the current state, which is determined by the selection status of
	 * the model.
	 */
	public int getColor() {
		return model.getColor();
	}

	/**
	 * Exactly which Design Pattern is this? Is it an Adapter, a Proxy or a
	 * Decorator? In this case, my vote lies with the Decorator, because we are
	 * extending functionality and "decorating" the original model with a more
	 * powerful model.
	 */
	private class TetraStateDecorator implements ButtonModel {
		private final ButtonModel other;

		private TetraStateDecorator(ButtonModel other) {
			this.other = other;
		}

		private void setColor(int color, boolean isSelected) {
			if (color == WHITE) {
				other.setArmed(false);
				setPressed(false);
				setSelected(isSelected);
			} else {
				other.setArmed(true);
				setPressed(true);
				setSelected(isSelected);
			}
		}

		/**
		 * The current state is embedded in the selection / armed state of the
		 * model.
		 * 
		 * We return the SELECTED state when the checkbox is selected but not
		 * armed, DONT_CARE state when the checkbox is selected and armed (grey)
		 * and NOT_SELECTED when the checkbox is deselected.
		 */
		private int getColor() {
			if (!isArmed()) {
				return GREY;
			} else {
				return WHITE;
			}
		}

		/** Filter: No one may change the armed status except us. */
		public void setArmed(boolean b) {
		}

		/**
		 * We disable focusing on the component when it is not enabled.
		 */
		public void setEnabled(boolean b) {
			setFocusable(b);
			other.setEnabled(b);
		}

		public boolean isArmed() {
			return other.isArmed();
		}

		public boolean isSelected() {
			return other.isSelected();
		}

		public boolean isEnabled() {
			return other.isEnabled();
		}

		public boolean isPressed() {
			return other.isPressed();
		}

		public boolean isRollover() {
			return other.isRollover();
		}

		public void setSelected(boolean b) {
			other.setSelected(b);
		}

		public void setPressed(boolean b) {
			other.setPressed(b);
		}

		public void setRollover(boolean b) {
			other.setRollover(b);
		}

		public void setMnemonic(int key) {
			other.setMnemonic(key);
		}

		public int getMnemonic() {
			return other.getMnemonic();
		}

		public void setActionCommand(String s) {
			other.setActionCommand(s);
		}

		public String getActionCommand() {
			return other.getActionCommand();
		}

		public void setGroup(ButtonGroup group) {
			other.setGroup(group);
		}

		public void addActionListener(ActionListener l) {
			other.addActionListener(l);
		}

		public void removeActionListener(ActionListener l) {
			other.removeActionListener(l);
		}

		public void addItemListener(ItemListener l) {
			other.addItemListener(l);
		}

		public void removeItemListener(ItemListener l) {
			other.removeItemListener(l);
		}

		public void addChangeListener(ChangeListener l) {
			other.addChangeListener(l);
		}

		public void removeChangeListener(ChangeListener l) {
			other.removeChangeListener(l);
		}

		public Object[] getSelectedObjects() {
			return other.getSelectedObjects();
		}
	}
}
