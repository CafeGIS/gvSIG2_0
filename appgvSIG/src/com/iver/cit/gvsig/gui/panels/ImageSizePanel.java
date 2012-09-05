/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
 *   Av. Blasco Ib��ez, 50
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
package com.iver.cit.gvsig.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import org.gvsig.gui.beans.swing.GridBagLayoutPanel;
import org.gvsig.gui.beans.swing.JIncrementalNumberField;

import com.iver.andami.PluginServices;

/**
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */
public class ImageSizePanel extends JPanel implements KeyListener, MouseListener, ActionListener{
	private static final Icon iconSelected = PluginServices.getIconTheme().get("chain");
	private static final Icon iconUnselected = PluginServices.getIconTheme().get("broken-chain");

	private JIncrementalNumberField widthTxt;
	private JIncrementalNumberField heightTxt;
	private double width, height;
	private JToggleButton lock;
	private double ratio;
	private boolean performing;
	private boolean locked = true;
	private ArrayList<ActionListener> listeners = new ArrayList<ActionListener>();
	
	public ImageSizePanel(){
		super();
		initialize();
	}


	/**
	 * This method initializes this
	 *
	 */
	private void initialize() {
		final GridBagLayoutPanel aux = new GridBagLayoutPanel();
		setLayout(new BorderLayout());
		aux.addComponent(PluginServices.getText(this, "width")+":",
				widthTxt  = new JIncrementalNumberField(null, 5, 0, Double.MAX_VALUE, 1));
		aux.addComponent(PluginServices.getText(this, "height")+":",
				heightTxt = new JIncrementalNumberField(null, 5, 0, Double.MAX_VALUE, 1));
		setLayout(new BorderLayout());
		add(aux, BorderLayout.CENTER);
		JPanel lockPanel = new JPanel(null) {
			private static final long serialVersionUID = -415551033800811412L;
			protected void paintComponent(Graphics g) {
				int y1 = widthTxt.getY() + widthTxt.getHeight()/2-3;
				int y2 = heightTxt.getY() + heightTxt.getHeight()/2+3;
				g.setColor(Color.DARK_GRAY);
				g.drawLine(2, y1, getWidth()/2+2, y1);
				g.drawLine(getWidth()/2+2, y1, getWidth()/2+2, y2);
				g.drawLine(2, y2, getWidth()/2+2, y2);
				lock.setBounds(3, widthTxt.getY()+13, getWidth()-2, 22);
			}
		};
		lockPanel.setPreferredSize(new Dimension(20, 20));
		lock = new JToggleButton() {
			private static final long serialVersionUID = 1668046192113822412L;
			protected void paintComponent(Graphics g) {
				setIcon(isSelected() ? iconSelected : iconUnselected);
				super.paintComponent(g);
			}
		};
		lock.setSelected(locked);
		lockPanel.add(lock);
		add(lockPanel, BorderLayout.EAST);
		widthTxt.addKeyListener(this);
		widthTxt.addMouseListener(this);
		widthTxt.addActionListener(this);
		heightTxt.addKeyListener(this);
		heightTxt.addMouseListener(this);
		heightTxt.addActionListener(this);

		lock.addMouseListener(this);
	}

	public void setImageSize(double width, double height) {
		this.width = width;
		widthTxt.setDouble(width);
		this.height = height;
		heightTxt.setDouble(height);
		this.ratio = width / height;
	}
	
	public void setImageSize(Dimension sz) {
		setImageSize(sz.getWidth(), sz.getHeight());
	}
	

	public double[] getImageDimension() {
		double[] d = new double[2];
		d[0] = width;
		d[1] = height;
		return d;
	}

	public void addActionListener(ActionListener l) {
		listeners.add(l);
	}

	public void keyPressed(KeyEvent e) { doIt(); }
	public void keyReleased(KeyEvent e) { doIt(); }
	public void keyTyped(KeyEvent e) { doIt(); }
	public void mouseClicked(MouseEvent e) { e.consume(); doIt(); }
	public void actionPerformed(ActionEvent e) { doIt(); }

	private void doIt() {
		if (!performing) {
			performing = true;
			if (locked != lock.isSelected()) {
				locked = lock.isSelected();
				ratio = widthTxt.getDouble() / heightTxt.getDouble();
			} else if (locked && width != widthTxt.getDouble()){
				width = widthTxt.getDouble();
				height = width/ratio;
				height = Math.round(height*100)/100;
				heightTxt.setDouble(height);
				fireActionPerformed();
			} else if (locked && height != heightTxt.getHeight()) {
				height = heightTxt.getDouble();
				width = height*ratio;
				width = Math.round(width*100)/100;
				widthTxt.setDouble(width);
				fireActionPerformed();

			}

			performing = false;
		}
	}


	private void fireActionPerformed() {
		for (int i = 0; i < listeners.size(); i++) {
			((ActionListener) listeners.get(i)).actionPerformed(
					new ActionEvent(this, 0, "IMAGE_RESIZED"));
		}
	}


	public void mouseEntered(MouseEvent e) { /* nothing */ }
	public void mouseExited(MouseEvent e) { /* nothing */ }
	public void mousePressed(MouseEvent e) {  /* nothing */ }
	public void mouseReleased(MouseEvent e) {  /* nothing */ }
}

