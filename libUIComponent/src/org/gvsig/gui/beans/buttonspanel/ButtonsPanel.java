package org.gvsig.gui.beans.buttonspanel;

/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Government (CIT)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 * 
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;

import org.gvsig.gui.beans.Messages;
import org.gvsig.gui.beans.swing.JButton;

/**
 * <code>ButtonsPanel</code> ofrece un widget con un conjunto de botones
 * preestablecidos, aunque tambi�n se pueden a�adir botones con el m�todo
 * {@link #addButton(String, int)}
 *
 * @version 09/05/2008
 *
 * @author BorSanZa - Borja Sanchez Zamorano (borja.sanchez@iver.es)
 * @author Pablo Piqueras Bartolom� (pablo.piqueras@iver.es)
 */
public class ButtonsPanel extends JPanel {
	private static final long serialVersionUID = -1660559792086305063L;

	private ArrayList<ButtonsPanelListener> actionCommandListeners = new ArrayList<ButtonsPanelListener>();
	private ArrayList<JButton> buttonsList = new ArrayList<JButton>();

	static private int      eventId                   = Integer.MIN_VALUE;

	public static final int BUTTON_ACCEPT             = 1;
	public static final int BUTTON_CANCEL             = 2;
	public static final int BUTTON_APPLY              = 3;
	public static final int BUTTON_YES                = 4;
	public static final int BUTTON_NO                 = 5;
	public static final int BUTTON_CLOSE              = 6;
	public static final int BUTTON_EXIT               = 7;
	public static final int BUTTON_SEEDETAILS         = 8;
	public static final int BUTTON_HIDEDETAILS        = 9;
	public static final int BUTTON_PAUSE              = 10;
	public static final int BUTTON_RESTART            = 11;
	public static final int BUTTON_SAVE               = 12;
	/**
	 * Sirve para cuando se crean botones nuevos, saber el �ltimo n�mero usado
	 * internamente, as� '<code>new_id = BUTTON_LAST + 1;</code>' podr�a ser
	 * el �ndice del nuevo bot�n.
	 */
	public static final int BUTTON_LAST               = 12;
	public static final int BUTTONS_ACCEPT            = 1;
	public static final int BUTTONS_ACCEPTCANCEL      = 2;
	public static final int BUTTONS_ACCEPTCANCELAPPLY = 3;
	public static final int BUTTONS_CANCEL            = 4;
	public static final int BUTTONS_YESNO             = 5;
	public static final int BUTTONS_CLOSE             = 6;
	public static final int BUTTONS_EXIT              = 7;
	public static final int BUTTONS_NONE              = 8;
	public static final int BUTTONS_APPLYCLOSE        = 9;

	/**
	 * Crea un ButtonsPanel con un Layout por defecto.
	 */
	public ButtonsPanel() {
		initialize();
	}

	private void initialize() {
		setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 7, 0));
		setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 0, 0, 0));
	}
	/**
	 * Crea un ButtonsPanel con un Layout por defecto.
	 * @param items Que botones vamos a usar en la creaci�n.
	 */
	public ButtonsPanel(int items) {
		initialize();
		switch (items) {
			case BUTTONS_ACCEPT:
				addAccept();
				break;
			case BUTTONS_ACCEPTCANCEL:
				addAccept();
				addCancel();
				break;
			case BUTTONS_ACCEPTCANCELAPPLY:
				addApply();
				addAccept();
				addCancel();
				break;
			case BUTTONS_APPLYCLOSE:
				addApply();
				addClose();
				break;
			case BUTTONS_CANCEL:
				addCancel();
				break;
			case BUTTONS_YESNO:
				addYes();
				addNo();
				break;
			case BUTTONS_CLOSE:
				addClose();
				break;
			case BUTTONS_EXIT:
				addExit();
				break;
			case BUTTONS_NONE:
				break;
		}
	}

	/**
	 * A�adir el disparador de cuando se pulsa un bot�n.
	 * @param listener
	 */
	public void addButtonPressedListener(ButtonsPanelListener listener) {
		if (!actionCommandListeners.contains(listener))
			actionCommandListeners.add(listener);
	}

	/**
	 * Devuelve el array de listeners del componente
	 * @return
	 */
	public Object[] getButtonPressedListeners() {
		return actionCommandListeners.toArray();
	}

	/**
	 * Borrar el disparador de eventos de los botones.
	 * @param listener
	 */
	public void removeButtonPressedListener(ButtonsPanelListener listener) {
		actionCommandListeners.remove(listener);
	}

	private void callActionCommandListeners(int buttonID) {
		Iterator<ButtonsPanelListener> acIterator = actionCommandListeners.iterator();
		while (acIterator.hasNext()) {
			ButtonsPanelListener listener = (ButtonsPanelListener) acIterator.next();
			listener.actionButtonPressed(new ButtonsPanelEvent(this, buttonID));
		}
		eventId++;
	}

	/**
	 * A�adir el boton Aceptar.
	 */
	public void addAccept() {
		addButton(Messages.getText("aceptar"), BUTTON_ACCEPT);
	}

	/**
	 * A�adir el boton Guardar.
	 */
	public void addSave() {
		addButton(Messages.getText("guardar"), BUTTON_SAVE);
	}

	/**
	 * A�adir el boton Cancelar.
	 */
	public void addCancel() {
		addButton(Messages.getText("cancelar"), BUTTON_CANCEL);
	}

	/**
	 * A�adir el boton S�.
	 */
	public void addYes() {
		addButton(Messages.getText("si"), BUTTON_YES);
	}

	/**
	 * A�adir el boton No.
	 */
	public void addNo() {
		addButton(Messages.getText("no"), BUTTON_NO);
	}

	/**
	 * A�adir el boton Aplicar.
	 */
	public void addApply() {
		addButton(Messages.getText("aplicar"), BUTTON_APPLY);
	}

	/**
	 * A�adir el boton Cerrar.
	 */
	public void addClose() {
		addButton(Messages.getText("cerrar"), BUTTON_CLOSE);
	}

	/**
	 * A�adir el boton Salir.
	 */
	public void addExit() {
		addButton(Messages.getText("salir"), BUTTON_EXIT);
	}

	/**
	 * A�adir el boton Ver detalles.
	 */
	public void addSeeDetails() {
		addButton(Messages.getText("verdetalles"), BUTTON_SEEDETAILS);
	}

	/**
	 * A�adir el boton Ocultar detalles.
	 */
	public void addHideDetails() {
		addButton(Messages.getText("ocultardetalles"), BUTTON_HIDEDETAILS);
	}

	/**
	 * A�adir el boton Pausar.
	 */
	public void addPause() {
		addButton(Messages.getText("pausar"), BUTTON_PAUSE);
	}

	/**
	 * A�adir el boton Reanudar.
	 */
	public void addRestart() {
		addButton(Messages.getText("reanudar"), BUTTON_RESTART);
	}

	/**
	 * A�adimos un bot�n definido por el usuario.
	 *
	 * @param text Texto que contendr� el bot�n
	 * @param id Entero para identificar los eventos del bot�n
	 */
	public void addButton(String text, int id) {
		JButton button = new JButton();
		button.setText(text);

		buttonsList.add(button);
		button.setActionCommand(id + "");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				callActionCommandListeners(Integer.parseInt(e.getActionCommand()));
			}
		});

		add(button);
	}

	/**
	 * Obtener un bot�n por su Entero
	 * @param id N�mero del disparador del bot�n
	 * @return El bot�n especificado o <code>null</code> si no se encontr� el bot�n.
	 */
	public JButton getButton(int id) {
		Iterator<JButton> acIterator = buttonsList.iterator();
		while (acIterator.hasNext()) {
			JButton button = (JButton) acIterator.next();
			if (Integer.parseInt(button.getActionCommand()) == id)
				return button;
		}
		return null;
	}

	/**
	 * <p>Removes the button identified by <code>id</code>.</p>
	 * 
	 * @param id identifier of the button
	 * @return <code>true</code> if has removed the button; otherwise <code>false</code>
	 */
	public boolean removeButton(int id) {
		String b_text = getButtonText(id);
		
		Iterator<JButton> acIterator = buttonsList.iterator();
		while (acIterator.hasNext()) {
			JButton button = (JButton) acIterator.next();
			if (button.getText().compareTo(b_text) == 0) {
				buttonsList.remove(button);
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * <p>Returns the text of the button identified by <code>id</code>.</p>
	 * 
	 * @param id identifier of the button
	 * 
	 * @return text of the identified button
	 */
	protected String getButtonText(int id) {
		switch (id) {
			case BUTTON_ACCEPT:
				return Messages.getText("aceptar");
			case BUTTON_CANCEL:
				return Messages.getText("cancelar");
			case BUTTON_APPLY:
				return Messages.getText("aplicar");
			case BUTTON_YES:
				return Messages.getText("si");
			case BUTTON_NO:
				return Messages.getText("no");
			case BUTTON_CLOSE:
				return Messages.getText("cerrar");
			case BUTTON_EXIT:
				return Messages.getText("salir");
			case BUTTON_SEEDETAILS:
				return Messages.getText("verdetalles");
			case BUTTON_HIDEDETAILS:
				return Messages.getText("ocultardetalles");
			case BUTTON_PAUSE:
				return Messages.getText("pausar");
			case BUTTON_RESTART:
				return Messages.getText("reanudar");
			case BUTTON_SAVE:
				return Messages.getText("guardar");
		}

		return null;
	}

	/**
	 * <p>Enables (or disables) the button identified by <code>id</code>.</p>
	 * 
	 * @param id identifier of the button
	 * @param b <code>true</code> to enable the button, otherwise <code>false</code>
	 * 
	 * @return <code>true</code> if there was a button of that kind in this group, otherwise <code>false</code>
	 */
	public boolean setEnabled(int id, boolean b) {
		String b_text = getButtonText(id);
		
		Iterator<JButton> acIterator = buttonsList.iterator();
		while (acIterator.hasNext()) {
			JButton button = (JButton) acIterator.next();
			if (button.getText().compareTo(b_text) == 0) {
				button.setEnabled(b);
				return true;
			}
		}
		
		return false;
	}
}