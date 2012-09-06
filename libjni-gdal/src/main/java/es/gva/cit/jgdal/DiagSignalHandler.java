/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 IVER T.I. and Generalitat Valenciana.
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
package es.gva.cit.jgdal;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import sun.misc.Signal;
import sun.misc.SignalHandler;

class DiagSignalHandler implements SignalHandler {
			 
		private SignalHandler oldHandler;
		private static boolean active = false;

		//Static method to install the signal handler
		public static DiagSignalHandler install(String signalName) {
			Signal diagSignal = new Signal(signalName);
				DiagSignalHandler diagHandler = new DiagSignalHandler();
			diagHandler.oldHandler = Signal.handle(diagSignal,diagHandler);
				return diagHandler;
		}
		// Signal handler method
		public void handle(Signal sig) {
	if(active)
		return;
	active = true;
	JFrame frame = new JFrame();
	frame.setSize(400, 150);
	JPanel p = new JPanel();
	JLabel l = new JLabel("SIGSEGV signal handler. Signal: " + sig);
	p.setLayout(new BorderLayout());
	JButton b = new JButton("Close");
	b.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			System.out.println("Handler test");
		}
	});
	p.add(l, BorderLayout.NORTH);
	p.add(b, BorderLayout.SOUTH);

	frame.getContentPane().add(p);
	frame.show();
		 }
}
