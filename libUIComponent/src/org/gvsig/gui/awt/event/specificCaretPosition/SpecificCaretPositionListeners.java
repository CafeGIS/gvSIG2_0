package org.gvsig.gui.awt.event.specificCaretPosition;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

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

/**
 * This class has a method which adds the necessary listeners for convert a JTextComponent to another which can be configurated
 *   its 'text visible positions when isn't edited' behaviour: left positions, right positions or like JTextComponent (doesn't change text visible positions)
 * 
 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
 */
public class SpecificCaretPositionListeners {
	/**
	 * Adds three listeners to a JTextComponent component:
	 *  - A FocusListener -> if this component loses its focus -> set caret position to 0
	 *  - A DocumentListener -> if this component doesn't have the focus and its caret position has changed -> set caret position to 0
	 *  - A CaretListener -> sometimes DocumentListener doesn't take effect, but this listener does
	 * @param component
	 */
	public static void setListeners(final JTextComponent component) {
		// The Focus Listener
		component.addFocusListener(new FocusAdapter() {
			/*
			 *  (non-Javadoc)
			 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
			 */
			public void focusLost(FocusEvent e) {
				switch (((ISpecificCaretPosition)component).getCaretPositionMode()) {
					case ISpecificCaretPosition.LEFT_POSITIONS:
						(component).setCaretPosition(0);
						break;
					case ISpecificCaretPosition.RIGHT_POSITIONS:
						(component).setCaretPosition(component.getDocument().getLength());
						break;
					case ISpecificCaretPosition.LIKE_JTEXTCOMPONENT: // Do nothing
						break;
				}
			}
		});
		
		// The Document Listener
		component.getDocument().addDocumentListener(new DocumentListener() {
			/*
			 *  (non-Javadoc)
			 * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
			 */
			public void changedUpdate(DocumentEvent e) {				
				if (!component.isFocusOwner()) {
					switch (((ISpecificCaretPosition)component).getCaretPositionMode()) {
						case ISpecificCaretPosition.LEFT_POSITIONS:
							(component).setCaretPosition(0);
							break;
						case ISpecificCaretPosition.RIGHT_POSITIONS:
							(component).setCaretPosition(component.getDocument().getLength());
							break;
						case ISpecificCaretPosition.LIKE_JTEXTCOMPONENT: // Do nothing
							break;
					}
				}
			}

			/*
			 *  (non-Javadoc)
			 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
			 */
			public void insertUpdate(DocumentEvent e) {
				if (!component.isFocusOwner()) {
					switch (((ISpecificCaretPosition)component).getCaretPositionMode()) {
						case ISpecificCaretPosition.LEFT_POSITIONS:
							(component).setCaretPosition(0);
							break;
						case ISpecificCaretPosition.RIGHT_POSITIONS:
							(component).setCaretPosition(component.getDocument().getLength());
							break;
						case ISpecificCaretPosition.LIKE_JTEXTCOMPONENT: // Do nothing
							break;
					}
				}
			}

			/*
			 *  (non-Javadoc)
			 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
			 */
			public void removeUpdate(DocumentEvent e) {
				if (!component.isFocusOwner()) {
					switch (((ISpecificCaretPosition)component).getCaretPositionMode()) {
						case ISpecificCaretPosition.LEFT_POSITIONS:
							(component).setCaretPosition(0);
							break;
						case ISpecificCaretPosition.RIGHT_POSITIONS:
							(component).setCaretPosition(component.getDocument().getLength());
							break;
						case ISpecificCaretPosition.LIKE_JTEXTCOMPONENT: // Do nothing
							break;
					}
				}
			}			
		});

		// The Caret Listener
		component.addCaretListener(new CaretListener() {
			/*
			 *  (non-Javadoc)
			 * @see javax.swing.event.CaretListener#caretUpdate(javax.swing.event.CaretEvent)
			 */
			public void caretUpdate(CaretEvent e) {				
				if (!component.isFocusOwner()) {
					switch (((ISpecificCaretPosition)component).getCaretPositionMode()) {
						case ISpecificCaretPosition.LEFT_POSITIONS:
							if ((component).getCaretPosition() != 0)
								(component).setCaretPosition(0);
							break;
						case ISpecificCaretPosition.RIGHT_POSITIONS:
							if ((component).getCaretPosition() != component.getDocument().getLength())
							(component).setCaretPosition(component.getDocument().getLength());
							break;
						case ISpecificCaretPosition.LIKE_JTEXTCOMPONENT: // Do nothing
							break;
					}
				}
			}
		});
	}
}