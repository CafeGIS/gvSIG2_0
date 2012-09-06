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
package org.gvsig.gui.beans.graphic;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JPanel;

import org.gvsig.gui.beans.textincreaser.TextIncreaserContainer;

/**
 *
 * Nacho Brodin (brodin_ign@gva.es)
 */

public class BoxesPanel extends JPanel {
	private static final long serialVersionUID = -4117483555280497312L;
	private JPanel pLeft = null;
	private JPanel pRight = null;
	private TextIncreaserContainer controlLeft = null;
	private TextIncreaserContainer controlRight = null;
	public BoxesPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 *
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.add(getPLeft(), BorderLayout.WEST);
		this.add(getPRight(), BorderLayout.EAST);
	}

	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getPLeft() {
		if (pLeft == null) {
			FlowLayout flowLayout1 = new FlowLayout();
			flowLayout1.setHgap(0);
			flowLayout1.setAlignment(FlowLayout.LEFT);
			flowLayout1.setVgap(0);
			pLeft = new JPanel();
			pLeft.setLayout(flowLayout1);
			pLeft.add(getControlLeft(), null);
			pLeft.setPreferredSize(new Dimension(100, 30));
		}
		return pLeft;
	}

	/**
	 * This method initializes jPanel1
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getPRight() {
		if (pRight == null) {
			FlowLayout flowLayout2 = new FlowLayout();
			flowLayout2.setHgap(0);
			flowLayout2.setAlignment(FlowLayout.RIGHT);
			flowLayout2.setVgap(0);
			pRight = new JPanel();
			pRight.setLayout(flowLayout2);
			pRight.add(getControlRight(), null);
			pRight.setPreferredSize(new Dimension(100, 30));
		}
		return pRight;
	}

	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	public TextIncreaserContainer getControlLeft() {
		if (controlLeft == null) {
			controlLeft = new TextIncreaserContainer(70, 0, 100, 0.0, true);
		}
		return controlLeft;
	}

	/**
	 * This method initializes jPanel1
	 *
	 * @return javax.swing.JPanel
	 */
	public TextIncreaserContainer getControlRight() {
		if (controlRight == null) {
			controlRight = new TextIncreaserContainer(70, 0, 100, 100.0, false);
		}
		return controlRight;
	}

	//****************************************************
	//MÉTODOS DEL CONTROL

	/**
	 * Obtiene el valor de los controles.
	 * @return Array con los valores de ambos controles. El primer valor del array es el control de la derecha
	 * y el segundo el de la izquierda.
	 */
	public double[] getBoxesValues(){
		double[] v = new double[2];
		v[0] = getControlRight().getValue();
		v[1] = getControlLeft().getValue();
		return v;
	}
}