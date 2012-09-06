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
package org.gvsig.gui.beans.incrementabletask;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import org.gvsig.gui.beans.Messages;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener;
import org.gvsig.gui.beans.progresspanel.ProgressPanel;
/**
 * <code>IncrementableTask</code>. Es un dialogo que contiene un ProgressPanel.
 * Se ejecuta bajo un Thread y va consultando a un objeto de tipo IIncrementable
 * para modificar sus valores.
 *
 * @version 23/04/2008
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class IncrementableTask implements Runnable, ButtonsPanelListener {
	private ArrayList<IncrementableListener> actionCommandListeners = new ArrayList<IncrementableListener>();
	private IIncrementable         iIncrementable   = null;
	private volatile ProgressPanel progressPanel    = null;
	private volatile Thread        blinker          = null;
	private boolean                threadSuspended  = false;
	private boolean                ended            = false;
	private boolean                askOnCancel      = true;
	private boolean                bDoCallListeners = true;
	static private int             eventId          = Integer.MIN_VALUE;

	/**
	 * Constructor del IncrementableTask.
	 * @param incrementable
	 */
	public IncrementableTask(IIncrementable incrementable) {
		iIncrementable = incrementable;
	}

	/**
	 * Inicio del thread para que la ventana vaya consultando por si sola al
	 * iIncrementable
	 */
	public void start() {
		blinker = new Thread(this);
		blinker.start();
	}

	/**
	 * Detiene el proceso de consulta de la ventana.
	 */
	public void stop() {
		ended = true;
	}

	/**
	 * Este thread va leyendo el porcentaje hasta que se completa el histograma.
	 */
	public synchronized void run() {
		while (!ended && (iIncrementable.getPercent() <= 100)) {
			try {
				getProgressPanel().setLabel(iIncrementable.getLabel());
				getProgressPanel().setPercent(iIncrementable.getPercent());
				getProgressPanel().setTitle(iIncrementable.getTitle());
				getProgressPanel().setLog(iIncrementable.getLog());
				Thread.sleep(100);
				synchronized (this) {
					while (threadSuspended && !ended)
						wait(500);
				}
			} catch (InterruptedException e) {
			}
		}
	}

	/**
	 * Termina el proceso de lectura de porcentajes y logs de la ventana y
	 * cierra esta.
	 */
	public void processFinalize() {
		stop();
		while (isAlive());
		hide();
	}

	/**
	 * Ocultar la ventana y parar el proceso
	 */
	private void hide() {
		hideWindow();
		progressPanel = null;
		blinker = null;
	}
	
	/**
	 * Ocultar la ventana
	 */
	public void hideWindow() {
		getProgressPanel().setVisible(false);
	}

	/**
	 * Devuelve un booleano indicando si esta activa la ventana.
	 * @return boolean
	 */
	public boolean isAlive() {
		if (blinker == null)
			return false;
		return blinker.isAlive();
	}

	/**
	 * Muestra la ventana de incremento con el porcentaje de la construcción del
	 * histograma.
	 */
	public void showWindow() {
		getProgressPanel().setTitle(iIncrementable.getTitle());
		getProgressPanel().showLog(false);
		getProgressPanel().setVisible(true);
	}

	/**
	 * Devuelve el componente ProgressPanel de la ventana incrementable.
	 * @return ProgressPanel
	 */
	public ProgressPanel getProgressPanel() {
		if (progressPanel == null) {
			progressPanel = new ProgressPanel(false);
			progressPanel.setAlwaysOnTop(true);
			progressPanel.addButtonPressedListener(this);
			progressPanel.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		}
		return progressPanel;
	}

	private void callActionCommandListeners(int actions) {
		if (!bDoCallListeners)
			return;
		Iterator<IncrementableListener> acIterator = actionCommandListeners.iterator();
		while (acIterator.hasNext()) {
			IncrementableListener listener = acIterator.next();
			switch (actions) {
				case IncrementableEvent.RESUMED:
					listener.actionResumed(new IncrementableEvent(this));
					break;
				case IncrementableEvent.SUSPENDED:
					listener.actionSuspended(new IncrementableEvent(this));
					break;
				case IncrementableEvent.CANCELED:
					listener.actionCanceled(new IncrementableEvent(this));
					break;
			}
		}
		eventId++;
	}

	/**
	 * Añadir el manejador de eventos para atender las peticiones de start,
	 * stop...
	 *
	 * @param listener
	 */
	public void addIncrementableListener(IncrementableListener listener) {
		if (!actionCommandListeners.contains(listener))
			actionCommandListeners.add(listener);
	}

	/**
	 * Borrar un manejador de eventos.
	 * @param listener
	 */
	public void removeIncrementableListener(IncrementableListener listener) {
		actionCommandListeners.remove(listener);
	}

	/**
	 * Definir si queremos que confirme al usuario si realmente desea cancelar el
	 * proceso
	 *
	 * @param value
	 */
	public void setAskCancel(boolean value) {
		askOnCancel = value;
	}

	/**
	 * Metodo para gestionar todos los eventos del objeto.
	 */
	public void actionButtonPressed(ButtonsPanelEvent e) {
		switch (e.getButton()) {
			case ButtonsPanel.BUTTON_CANCEL:
				boolean cancelled = true;
				if (askOnCancel) {
					cancelled = false;
					String string1 = Messages.getText("si");
					String string2 = Messages.getText("no");
					Object[] options = { string1, string2 };
					int answer = JOptionPane.showOptionDialog(getProgressPanel(), Messages
							.getText("msg_cancel_incrementable"), Messages
							.getText("title_cancel_incrementable"),
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
							options, string1);
					if (answer == JOptionPane.YES_OPTION)
						cancelled = true;
				}
				if (cancelled) {
					ended = true;
					callActionCommandListeners(IncrementableEvent.CANCELED);
				}
				break;
			case ButtonsPanel.BUTTON_PAUSE:
				threadSuspended = true;
				callActionCommandListeners(IncrementableEvent.SUSPENDED);
				break;
			case ButtonsPanel.BUTTON_RESTART:
				threadSuspended = false;
				callActionCommandListeners(IncrementableEvent.RESUMED);
				break;
		}
	}

	/**
	 * @see ProgressPanel#getButtonsPanel()
	 */
	public ButtonsPanel getButtonsPanel() {
		return getProgressPanel().getButtonsPanel();
	}
}