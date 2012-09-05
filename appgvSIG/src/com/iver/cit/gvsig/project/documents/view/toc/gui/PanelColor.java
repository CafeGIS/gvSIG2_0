/*
 * Created on 27-feb-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
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
package com.iver.cit.gvsig.project.documents.view.toc.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;


/**
 * DOCUMENT ME!
 *
 * @author vcn To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 *         Comments
 */
public class PanelColor extends JPanel {
    public Color[] m_colores = new Color[100];
    public Color m_color;
    private Integer numReg;
    private int numreg;
    public int a = 155;
    public int b = 205;
    public int c = 255;

    /**
     * Creates a new PanelColor object.
     *
     * @param numReg DOCUMENT ME!
     */
    public PanelColor(int numReg) {
        //numReg=numReg+1;
        //m_color = new Color((numReg*numReg+100)%255,(numReg+(3*numReg+100))%255,numReg%255);
        //this.setBackground(m_color);
        setLayout(new BorderLayout());
		
        //add(oneColorPanel);
        setMaximumSize(new Dimension(12, 12));
        setMinimumSize(new Dimension(12, 12));
        setPreferredSize(new Dimension(12, 12));
    }

    //private ColorPanel oneColorPanel = new ColorPanel();
    //private FourColorPanel fourColorPanel = new LayerColorPanel.FourColorPanel();
    public void setColor(Color color) {
        m_color = color;
        a=color.getRed();
        System.out.println("a=color.getRed();"+a);
		b=color.getGreen();
		System.out.println("b=color.getGreen();"+b);
        c=color.getBlue();
		System.out.println("c=color.getBlue();"+c);
        this.setBackground(color);
    }

    /**
     * DOCUMENT ME!
     */
    public void setColorinicial() {
        a=155;
		b=205;
		c=255;
        m_color = new Color(a, b, c);
        this.setBackground(m_color);
    }

    /**
     * DOCUMENT ME!
     */
    public void setColorfin() {
        this.m_color = new Color(45, 95, 145);
        a=45;
        b=95;
        c=145;
        this.setBackground(m_color);
    }
}
