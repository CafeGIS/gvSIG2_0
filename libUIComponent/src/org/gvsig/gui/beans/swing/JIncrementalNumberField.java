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
package org.gvsig.gui.beans.swing;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.gvsig.gui.beans.Messages;
import org.gvsig.gui.beans.swing.ValidatingTextField.Cleaner;
import org.gvsig.gui.beans.swing.ValidatingTextField.Validator;

/**
 * This class represents a JTextField-like component that allows to input numbers
 * and featuring a built-in increment or decrease of the number using the mouse.
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */
public class JIncrementalNumberField extends JPanel {

	private static final long serialVersionUID = 5225633490545230468L;
	private boolean acceptsDoubles;
	private ValidatingTextField vtf;
	private double step;
	private double maxValue;
	private double minValue;

	private ActionListener propage = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if (!isEnabled()) return;
			if (acceptsDoubles) {
				double v = getDouble();
				if (v>maxValue)
					v = maxValue;
				if (v<minValue)
					v = minValue;
				setDouble(v);
			} else {
				int v = getInteger();
				if (v>maxValue)
					v = (int) maxValue;
				if (v<minValue)
					v = (int) minValue;
				setInteger(v);
			}
			fireActionPerformed(e);
		}
	};

	private FocusListener propageFocus = new FocusListener() {
		public void focusGained(FocusEvent e) {
			if (!isEnabled()) return;
			Iterator<FocusListener> iter = Arrays.asList(JIncrementalNumberField.this.getFocusListeners()).iterator();
			e.setSource(JIncrementalNumberField.this);
			while (iter.hasNext()){
				iter.next().focusGained(e);
			}
		}

		public void focusLost(FocusEvent e) {
			if (!isEnabled()) return;
			if (acceptsDoubles) {
				double v = getDouble();
				if (v>maxValue)
					v = maxValue;
				if (v<minValue)
					v = minValue;
				setDouble(v);
			} else {
				int v = getInteger();
				if (v>maxValue)
					v = (int) maxValue;
				if (v<minValue)
					v = (int) minValue;
				setInteger(v);
			}
			Iterator<FocusListener> iter = Arrays.asList(JIncrementalNumberField.this.getFocusListeners()).iterator();
			e.setSource(JIncrementalNumberField.this);
			while (iter.hasNext()){
				iter.next().focusLost(e);
			}
		}
	};


	private ActionListener accum = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if (!isEnabled()) return;
			String command = e.getActionCommand();
			if ("UP".equals(command)) {
				if (acceptsDoubles) {
					double v = getDouble() + step;
					if (v>maxValue)
						v = maxValue;
					setDouble(v);
				} else {
					int v = getInteger() + (int) Math.round(step);
					if (v>maxValue)
						v = (int) maxValue;
					setInteger(v);
				}
			} else if ("DOWN".equals(command)) {
				if (acceptsDoubles) {
					double v = getDouble();// - step;
					v = v - step;
					if (v<minValue)
						v = minValue;
					setDouble(v);

				} else {
					int v = getInteger() - (int) Math.round(step);
					if (v<minValue)
						v = (int) minValue;
					setInteger(v);
				}
			}
			fireActionPerformed();
		}
	};

	private JButton down;
	private JButton up;
	private ArrayList<EventListener> listeners = new ArrayList<EventListener>();

	private class MousePressedTask extends TimerTask {
		private MouseEvent e;
		private ButtonMouseListener ml;

		private MousePressedTask(ButtonMouseListener ml, MouseEvent e){
			super();
			this.ml = ml;
			this.e = e;
		}

		public void run() {
			JButton b = (JButton) e.getComponent();

			long time = System.currentTimeMillis();
			long delay = 200;

			while (ml.pressed) {
				if (ml.in) {
					accum.actionPerformed(new ActionEvent(b, 12431, b.getActionCommand()));

					long currTime = System.currentTimeMillis();
					if (delay > 5 && ((currTime - time) > 1000)) {
						delay /= 2;
						time = currTime;
					}
				} else time = System.currentTimeMillis();
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e1) {
					e.consume();
				}

			}
			e.consume();
		}
	}

	private class ButtonMouseListener implements MouseListener{
		boolean in = false;
		boolean pressed = false;

		public void mouseClicked(MouseEvent e) { /* nothing (managed by the ActionListener) */ }

		public void mouseEntered(MouseEvent e) {
			in = true;
		}

		public void mouseExited(MouseEvent e) {
			in = false;
		}

		public void mousePressed(MouseEvent e) {
			MousePressedTask task;
			synchronized (this) {
				pressed = true;
				Timer timer = new Timer();
				task = new MousePressedTask(this, e);
				timer.schedule(task, 500);

			}
			if (!pressed) {
				task.cancel();
			}

		}

		public void mouseReleased(MouseEvent e) {
			pressed = false;
		}

	};


	public JIncrementalNumberField() {
		this("");
	}

	public JIncrementalNumberField(String text) {
		this(text, 7);
	}

	public JIncrementalNumberField(String text, int columns) {
		this(text, columns, ValidatingTextField.DOUBLE_VALIDATOR, ValidatingTextField.NUMBER_CLEANER_2_DECIMALS, -Double.MAX_VALUE, Double.MAX_VALUE, 1);
	}

	public JIncrementalNumberField(String text, int columns, double minValue, double maxValue, double step) {
		this(text, columns, ValidatingTextField.DOUBLE_VALIDATOR, ValidatingTextField.NUMBER_CLEANER_2_DECIMALS, minValue, maxValue, step);
	}

	public JIncrementalNumberField(String text, int columns, Validator validator, Cleaner cleaner, double minValue, double maxValue, double step) {
		super();
		if (text == null) text = "";

		this.minValue = minValue;
		this.maxValue = maxValue;
		this.step = step;
		acceptsDoubles = validator.getClass().equals(ValidatingTextField.DOUBLE_VALIDATOR.getClass());

		JPanel lateralButtons = new JPanel();
		Icon upIcon = new Icon() {

			public int getIconHeight() {
				return 5;
			}

			public int getIconWidth() {
				return 9;
			}

			public void paintIcon(Component c, Graphics g, int x, int y) {
				g.setColor( isEnabled() ? Color.DARK_GRAY : Color.RED);
				((Graphics2D) g).setStroke(new BasicStroke(2));
				g.drawLine(isEnabled() ? 3 : 1,
						   isEnabled() ? 6 : 4,
						   isEnabled() ? 5 : 3,
						   isEnabled() ? 3 : 1);

				g.drawLine(isEnabled() ? 5 : 3,
						   isEnabled() ? 3 : 1,
						   isEnabled() ? 8 : 6,
						   isEnabled() ? 6 : 4);

			}
		};
		Icon downIcon = new Icon() {
			public int getIconHeight() {
				return 5;
			}

			public int getIconWidth() {
				return 9;
			}

			public void paintIcon(Component c, Graphics g, int x, int y) {
				g.setColor(isEnabled() ? Color.DARK_GRAY : Color.RED);
				((Graphics2D) g).setStroke(new BasicStroke(2));


				g.drawLine(isEnabled() ? 3 : 1,
						   isEnabled() ? 3 : 1,
						   isEnabled() ? 5 : 3,
						   isEnabled() ? 6 : 4);

				g.drawLine(isEnabled() ? 5 : 3,
						   isEnabled() ? 6 : 4,
						   isEnabled() ? 8 : 6,
						   isEnabled() ? 3 : 1);

			}
		};
		up = new JButton(upIcon);
		up.setActionCommand("UP");
		up.addActionListener(accum);
		up.addMouseListener(new ButtonMouseListener());
		up.setBounds(0, 0, 11, 11);
		up.setFocusable(false);

		down = new JButton(downIcon);
		down.setActionCommand("DOWN");
		down.addActionListener(accum);
		down.addMouseListener(new ButtonMouseListener());
		down.setBounds(0, 11, 11, 11);
		down.setFocusable(false);



		lateralButtons.setLayout(null);
		lateralButtons.setSize(13, 20);
		lateralButtons.add(up);
		lateralButtons.add(down);
		lateralButtons.setSize(new Dimension(11, 22));
		lateralButtons.setPreferredSize(new Dimension(11, 22));
		lateralButtons.setBorder(BorderFactory.createLineBorder(Color.GREEN));
		vtf = new ValidatingTextField(
				text,
				columns,
				SwingConstants.RIGHT,
				validator,
				cleaner) ;
		setLayout(new BorderLayout(0, 0));
		vtf.addActionListener(propage);
		vtf.addFocusListener(propageFocus);
		add(vtf, BorderLayout.CENTER);
		add(lateralButtons, BorderLayout.EAST);
	}

	public int getInteger() {
		return vtf.getInteger();
	}

	public double getDouble() {
		if (!acceptsDoubles)
			throw new Error(Messages.getText(
					"cannot_get_double_value_from_an_integer_number_field_use_getInteger()_instead"));
		return vtf.getDouble();
	}

	public void setDouble(double v) {
		if (!acceptsDoubles)
			throw new Error(Messages.getText(
					"cannot_set_a_double_value_from_an_integer_number_field_use_setInteger(int)_instead"));
		vtf.setText(String.valueOf(v));
	}

	public void setInteger(int v) {
		vtf.setText(String.valueOf(v));
	}

	public void addActionListener(ActionListener l) {
//		vtf.addActionListener(l);
		listeners.add(l);
	}

	public void removeActionListener(ActionListener l) {
//		vtf.removeActionListener(l);
		listeners.remove(l);
	}

	private void fireActionPerformed() {
		ActionEvent evt = new ActionEvent(this, 0, null);
		for (int i = 0; i < listeners.size(); i++) {
			((ActionListener) listeners.get(i)).actionPerformed(evt);
		}
	}

	private void fireActionPerformed(ActionEvent e) {
		e.setSource(this);
		for (int i = 0; i < listeners.size(); i++) {
			((ActionListener) listeners.get(i)).actionPerformed(e);
		}
	}

	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	public double getMinValue() {
		return minValue;
	}

	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}

	public double getStep() {
		return step;
	}

	public void setStep(double step) {
		this.step = step;
	}

	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		up.setEnabled(enabled);
		down.setEnabled(enabled);
		vtf.setEnabled(enabled);
	}
}
