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
package com.iver.cit.gvsig.project.documents.layout.fframes.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JPanel;

public class JPRotationView extends JPanel {
	private double rotation=0;
	public void setRotation(double rot){
		rotation=rot;
	}
	public double getRotation(){
		return rotation;
	}
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Rectangle r=new Rectangle(35,15,30,25);
		((Graphics2D)g).rotate(Math.toRadians(getRotation()), r.x + (r.width / 2),
				r.y + (r.height / 2));

		g.setColor(new Color(230,150,50));
		((Graphics2D)g).setStroke(new BasicStroke(4));
		g.drawRect((int)r.getX(),(int)r.getY(),(int)r.getWidth(),(int)r.getHeight());
		((Graphics2D)g).setStroke(new BasicStroke(1));
		g.setColor(Color.RED);
		g.fillRect((int)r.getX(),(int)r.getY(),(int)r.getWidth(),(int)r.getHeight());
		g.setColor(Color.black);
		g.drawRect((int)r.getX(),(int)r.getY(),(int)r.getWidth(),(int)r.getHeight());
		((Graphics2D)g).rotate(Math.toRadians(-getRotation()),
				r.x + (r.width / 2), r.y + (r.height / 2));
	}

	/**
	 * This is the default constructor
	 */
	public JPRotationView() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setSize(106, 67);
		this.setBackground(java.awt.SystemColor.controlShadow);
		this.setBackground(java.awt.Color.white);
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
