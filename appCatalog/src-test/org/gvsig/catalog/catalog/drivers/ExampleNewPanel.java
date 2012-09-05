package org.gvsig.catalog.catalog.drivers;

import java.util.Properties;

import javax.swing.JLabel;
import javax.swing.JTextField;

import org.gvsig.catalog.ui.search.SearchAditionalPropertiesPanel;


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
/* CVS MESSAGES:
 *
 * $Id: ExampleNewPanel.java 537 2007-07-26 11:21:10Z jpiera $
 * $Log$
 * Revision 1.1.2.1  2007/07/13 12:00:35  jorpiell
 * Add the posibility to add a new panel
 *
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class ExampleNewPanel extends SearchAditionalPropertiesPanel{
	JLabel label = null;
	JTextField text = null;
	
	public ExampleNewPanel(){
		label = new JLabel();
		text = new JTextField();		
		setLayout(new java.awt.BorderLayout());
		label.setText("Label");
		add(label, java.awt.BorderLayout.WEST);		
		add(text, java.awt.BorderLayout.CENTER);
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.gva.cit.gazetteer.ui.search.SearchAditionalPropertiesPanel#getProperties()
	 */
	public Properties getProperties() {
		Properties properties = new Properties();
		properties.put("PROP1", text.getText());
		return properties;
	}

}
