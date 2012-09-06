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
package org.gvsig.gui.beans.progresspanel;

import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener;
/**
 * <code>TestProgressPanel</code>. Test para comprobar el funcionamiento del
 * objeto <code>TestProgressPanel</code>
 *
 * @version 20/03/2007
 * @author BorSanZa - Borja Sanchez Zamorano (borja.sanchez@iver.es)
 */
public class TestProgressPanel {

	private tryPanel frame = null;

	public class tryPanel implements Runnable, ButtonsPanelListener {
		private ProgressPanel frame = new ProgressPanel();
		private volatile Thread blinker;

		public void showWindow() {
			frame.getButtonsPanel().getButton(ButtonsPanel.BUTTON_PAUSE).setVisible(false);
			frame.setTitle("Actualizando datos");
			frame.showLog(false);
			frame.getButtonsPanel().addButtonPressedListener(this);
			frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
			frame.setVisible(true);
		}

		private boolean threadSuspended = false;

		public void start() {
			blinker = new Thread(this);
			blinker.start();
		}

		public synchronized void stop() {
			blinker = null;
			notify();
		}

		public void run() {
			Thread thisThread = Thread.currentThread();
			int i = 0;
			frame.addLineLog("Realizando testeo...");
			while ((blinker == thisThread) && (i < 1000)) {
				try {
					Thread.sleep(1);

					synchronized(this) {
						while (threadSuspended && blinker==thisThread)
							wait();
					}
				} catch (InterruptedException e) {
				}

				if (i==0)
					frame.addLineLog("Testeo 1 completado al 0%");
				if ((i>=0) && (i<=100))
					frame.replaceLastLineLog("Testeo 1 completado al " + i + "%");
				if (i==100) {
					frame.replaceLastLineLog("Testeo 1 completado");
					frame.addLineLog("Testeo 2 completado al 0%");
				}
				if ((i>=100) && (i<=800))
					frame.replaceLastLineLog("Testeo 2 completado al " + (int)((i-100)*100)/700 + "%");
				if (i==800) {
					frame.replaceLastLineLog("Testeo 2 completado");
					frame.addLineLog("Testeo 3 completado al 0%");
				}
				if ((i>=800) && (i<=1000))
					frame.replaceLastLineLog("Testeo 3 completado al " + (int)((i-800)*100)/200 + "%");
				i++;

				frame.setPercent((int) (i*100)/1000);
			}
			// Cerramos la ventana
			frame.setVisible(false);
			frame = null;
		}

		public void actionButtonPressed(ButtonsPanelEvent e) {
			if (e.getButton() == ButtonsPanel.BUTTON_CANCEL) {
				this.stop();
			}
		}
	}

	public static void main(String[] args) {
		new TestProgressPanel();
	}

	/**
	 * This is the default constructor
	 */
	public TestProgressPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		frame = new tryPanel();
		frame.showWindow();
		frame.start();
	}
}