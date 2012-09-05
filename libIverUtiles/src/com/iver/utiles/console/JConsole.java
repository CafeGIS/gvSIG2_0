/*
 * Created on 31-ene-2005
 *
 * gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
   USA.
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
package com.iver.utiles.console;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import com.iver.utiles.console.jedit.ConsoleInputHandler;
import com.iver.utiles.console.jedit.JEditTextArea;
import com.iver.utiles.console.jedit.TextAreaDefaults;
import com.iver.utiles.console.jedit.TokenMarker;


/**
 * DOCUMENT ME!
 *
 * @author Fernando González Cortés
 */
public class JConsole extends JPanel {
	private JEditTextArea txt;
	private int startingCaretPosition = 0;
	private ArrayList entries = new ArrayList();
	private int currentEntry = -1;
	//private JScrollPane jScrollPane = null;
	private ResponseListenerSupport listenerSupport = new ResponseListenerSupport();
	public static int MESSAGE=0;
	public static int COMMAND=1;
	public static int INSERT=2;
	public static int ERROR=3;
	private static TextAreaDefaults defaults;
	private JConsole theContainer = null;
	/**
	 * This is the default constructor
	 */
	public JConsole() {
		super();
		initialize();
		defaults=TextAreaDefaults.getDefaults();
		((ConsoleInputHandler)defaults.inputHandler).addConsoleListener(this);
		theContainer = this;
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setSize(300, 200);
		this.setPreferredSize(new Dimension(300,200));
		this.add(getTxt(), java.awt.BorderLayout.CENTER);
	}

	public void setTokenMarker(TokenMarker tm){
		txt.setTokenMarker(tm);
	}
	/**
	 * Obtiene una referencia al JTextArea de la consola
	 *
	 * @return javax.swing.JTextArea
	 */
	public JEditTextArea getTxt() {
		if (txt == null) {
			txt = new JEditTextArea();
			//txt.setTokenMarker(new JavaTokenMarker());
			txt.addCaretListener(new CaretListener() {
					public void caretUpdate(CaretEvent e) {
						if (txt.getCaretPosition() < startingCaretPosition) {
							if (startingCaretPosition <= txt.getText().length()) {
								txt.setCaretPosition(startingCaretPosition);
							}
						}
					}
				});
			txt.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
						if (startingCaretPosition >= txt.getCaretPosition()) {
							int caretPos = txt.getCaretPosition();
							String text = txt.getText();
							text = text.substring(0, caretPos) + " " +
								text.substring(caretPos);
							txt.setText(text);

							txt.setCaretPosition(caretPos);

						}else{
						}
					}


				}

				public void keyReleased(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						String texto = txt.getText();
						String response = texto.substring(startingCaretPosition);

						listenerSupport.callAcceptResponse(response);

						if (response.trim().length() > 0) {
							entries.add(response.trim());
						}

						currentEntry = -1;

					} else if (e.getKeyCode() == KeyEvent.VK_UP) {
						if (entries.size() == 0) {
							return;
						}

						if (currentEntry == -1) {
							currentEntry = entries.size() - 1;
						} else {
							currentEntry--;

							if (currentEntry < 0) {
								currentEntry = 0;
							}
						}

						putEntry();
					} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
						if (entries.size() == 0) {
							return;
						}

						if (currentEntry != -1) {
							currentEntry++;

							if (currentEntry >= entries.size()) {
								currentEntry = entries.size() - 1;
							}
						}

						putEntry();
					} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						txt.setText(txt.getText());
						listenerSupport.callAcceptResponse(null);
					}
				}

				private void putEntry() {
					String anterior = txt.getText();
					anterior = anterior.substring(0, startingCaretPosition);
					txt.setText(anterior + entries.get(currentEntry));
				}
			});
			addKeyListener(new java.awt.event.KeyAdapter() {
					public void keyPressed(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
							if (startingCaretPosition >= txt.getCaretPosition()) {
								int caretPos = txt.getCaretPosition();
								String text = txt.getText();
								text = text.substring(0, caretPos) + " " +
									text.substring(caretPos);
								txt.setText(text);

								txt.setCaretPosition(caretPos);

							}else{
							}
						}


					}

					public void keyReleased(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_ENTER) {
							String texto = txt.getText();
							String response = texto.substring(startingCaretPosition);

							listenerSupport.callAcceptResponse(response);

							if (response.trim().length() > 0) {
								entries.add(response.trim());
							}

							currentEntry = -1;

						} else if (e.getKeyCode() == KeyEvent.VK_UP) {
							if (entries.size() == 0) {
								return;
							}

							if (currentEntry == -1) {
								currentEntry = entries.size() - 1;
							} else {
								currentEntry--;

								if (currentEntry < 0) {
									currentEntry = 0;
								}
							}

							putEntry();
						} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
							if (entries.size() == 0) {
								return;
							}

							if (currentEntry != -1) {
								currentEntry++;

								if (currentEntry >= entries.size()) {
									currentEntry = entries.size() - 1;
								}
							}

							putEntry();
						} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
							txt.setText(txt.getText());
							listenerSupport.callAcceptResponse(null);
						}
					}

					private void putEntry() {
						String anterior = txt.getText();
						anterior = anterior.substring(0, startingCaretPosition);
						txt.setText(anterior + entries.get(currentEntry));
					}
				});
		}

		return txt;
	}

	/**
	 * Añade un texto a la consola
	 *
	 * @param text Texto que se añade a la consola
	 */
	public void addText(String text,int type) {
		txt.setText(txt.getText() + text);
		txt.setCaretPosition(txt.getText().length());
		startingCaretPosition = txt.getText().length();

	}
	/**
	 * Añade un texto a la consola que es tomado como si lo
	 * hubiese escrito el usuario. Formará parte de la respuesta
	 *
	 * @param text Texto que se añade a la consola
	 */
	public void addResponseText(String text) {
		txt.setText(txt.getText() + text);
		txt.setCaretPosition(txt.getText().length());
	}

	/**
	 * This method initializes jScrollPane
	 *
	 * @return javax.swing.JScrollPane
	 */
	/*private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getTxt());
		}

		return jScrollPane;
	}
	*/
	public void addResponseListener(ResponseListener listener) {
		listenerSupport.addResponseListener(listener);
	}
	public void removeResponseListener(ResponseListener listener) {
		listenerSupport.removeResponseListener(listener);
	}

	/**
	 * Useful to know from where it comes a key event. See CADExtension
	 * @param name
	 */
	public void setJTextName(String name)
	{
		txt.setName(name);
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			if (startingCaretPosition >= txt.getCaretPosition()) {
				int caretPos = txt.getCaretPosition();
				String text = txt.getText();
				text = text.substring(0, caretPos) + " " +
					text.substring(caretPos);
				txt.setText(text);

				txt.setCaretPosition(caretPos);

			}else{
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			String texto = txt.getText();
			String response = texto.substring(startingCaretPosition);

			listenerSupport.callAcceptResponse(response);

			if (response.trim().length() > 0) {
				entries.add(response.trim());
			}

			currentEntry = -1;

		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			if (entries.size() == 0) {
				return;
			}

			if (currentEntry == -1) {
				currentEntry = entries.size() - 1;
			} else {
				currentEntry--;

				if (currentEntry < 0) {
					currentEntry = 0;
				}
			}

			putEntry();
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			if (entries.size() == 0) {
				return;
			}

			if (currentEntry != -1) {
				currentEntry++;

				if (currentEntry >= entries.size()) {
					currentEntry = entries.size() - 1;
				}
			}

			putEntry();
		} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			txt.setText(txt.getText());
			listenerSupport.callAcceptResponse(null);
		}
	}
	private void putEntry() {
		String anterior = txt.getText();
		anterior = anterior.substring(0, startingCaretPosition);
		txt.setText(anterior + entries.get(currentEntry));
	}


}
