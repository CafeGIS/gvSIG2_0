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
package com.iver.utiles.xmlViewer.sample;

import javax.swing.JFrame;

import org.xml.sax.SAXException;

import com.iver.utiles.xmlViewer.MultipleXMLContent;
import com.iver.utiles.xmlViewer.XMLViewer;
public class Sample extends JFrame {

	private javax.swing.JPanel jContentPane = null;

	private XMLViewer XMLViewer = null;
	/**
	 * This method initializes XMLViewer	
	 * 	
	 * @return com.iver.utiles.xmlViewer.XMLViewer	
	 */    
	private XMLViewer getXMLViewer() {
		if (XMLViewer == null) {
			XMLViewer = new XMLViewer();
			XMLViewer.getXmlTree().setRootVisible(false);
			MultipleXMLContent model = new MultipleXMLContent();
			model.addXML("<?xml version='1.0'?><hola><adios/></hola>");
			model.addXML("<?xml version='1.0'?><adios><hola/></adios>");
			try {
				XMLViewer.setModel(model);
			} catch (SAXException e) {
				e.printStackTrace();
			}
		}
		return XMLViewer;
	}
 	public static void main(String[] args) {
 		Sample s = new Sample();
 		s.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 		s.show();
	}
	/**
	 * This is the default constructor
	 */
	public Sample() {
		super();
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300,200);
		this.setContentPane(getJContentPane());
		this.setTitle("JFrame");
	}
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if(jContentPane == null) {
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(new java.awt.BorderLayout());
			jContentPane.add(getXMLViewer(), java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
	}
}
