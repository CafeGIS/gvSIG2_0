package com.iver.cit.gvsig.gui.panels;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.gui.panels.model.WFSSelectedFeature;

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
 * $Id: WFSFormatPanel.java 5856 2006-06-15 07:50:58Z jorpiell $
 * $Log$
 * Revision 1.1  2006-06-15 07:50:58  jorpiell
 * Añadida la funcionalidad de reproyectar y hechos algunos cambios en la interfaz
 *
 *
 */
/**
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class WFSFormatPanel extends JPanel {
	private JPanel srsPanel = null;
	private JScrollPane jScrollPane2 = null;
	private JList lstSRSs = null;

	/**
	 * This method initializes 
	 * 
	 */
	public WFSFormatPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setLayout(null);
		this.setBounds(10, 5, 481, 427);
		this.add(getSrsPanel(), null);
			
	}

	/**
	 * This method initializes srsPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getSrsPanel() {
		if (srsPanel == null) {
			srsPanel = new JPanel();
			srsPanel.setLayout(null);
			srsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
                    null, PluginServices.getText(this, "seleccionar_srs"),
                    javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                    javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
			srsPanel.setBounds(7, 10, 453, 168);
			srsPanel.add(getJScrollPane2(), null);
		}
		return srsPanel;
	}
	
	/**
	 * This method initializes jScrollPane2
	 *
	 * @return javax.swing.JScrollPane
	 */
	private javax.swing.JScrollPane getJScrollPane2() {
		if (jScrollPane2 == null) {
			jScrollPane2 = new javax.swing.JScrollPane();
			jScrollPane2.setBounds(5, 23, 442, 141);
			jScrollPane2.setViewportView(getLstSRSs());
			jScrollPane2.setPreferredSize(new java.awt.Dimension(100, 200));
		}

		return jScrollPane2;
	}
	
	/**
	 * This method initializes lstSRSs
	 *
	 * @return javax.swing.JList
	 */
	public javax.swing.JList getLstSRSs() {
		if (lstSRSs == null) {
			lstSRSs = new javax.swing.JList();
			lstSRSs.setModel(new SRSListModel());
			lstSRSs.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			lstSRSs.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
				public void valueChanged(
						javax.swing.event.ListSelectionEvent e) {
					int i = lstSRSs.getSelectedIndex();					
				}
			});
		}

		return lstSRSs;
	}

	/**
	 * Refresh the panel
	 * @param feature
	 */
	public void refresh(WFSSelectedFeature feature) {
		setFormats(feature);	
	}
	
	/**
	 * Sets a SRS list
	 * @param feature
	 */
	private void setFormats(WFSSelectedFeature feature){
		getLstSRSs().setListData(new Object[0]);
		Vector srs = feature.getSrs();
		getLstSRSs().setListData(srs.toArray());
	}
	
	public class SRSListModel extends AbstractListModel {
		private static final long serialVersionUID = -6134561791965083588L;
		ArrayList srs = new ArrayList();
		
		public int getSize() {
			return srs.size();
		}

		public Object getElementAt(int index) {
			return srs.get(index);
		}
		
		public void setAll(Collection c) {
			
			srs.clear();
			srs.addAll(c);
		}
		
		public Collection intersect(Collection c) {
			TreeSet resul = new TreeSet();
	    	for (int i = 0; i < srs.size(); i++) {
				if (c.contains(srs.get(i)))
					resul.add(srs.get(i));
			}
	    	return resul;	
		}
	}
}
