/* gvSIG. Sistema de Informacin Geogrfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 Instituto de Desarrollo Regional and Generalitat Valenciana.
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
 *   Av. Blasco Ibez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   Instituto de Desarrollo Regional (Universidad de Castilla La-Mancha)
 *   Campus Universitario s/n
 *   02071 Alabacete
 *   Spain
 *
 *   +34 967 599 200
 */

package org.gvsig.crs.gui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.cresques.cts.IProjection;
import org.gvsig.crs.CrsException;
import org.gvsig.crs.CrsFactory;
import org.gvsig.crs.ICrs;
import org.gvsig.crs.persistence.CrsData;
import org.gvsig.crs.persistence.RecentCRSsPersistence;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.gui.TableSorter;

/**
 * Clase que genera el panel de recientes
 * 
 * @author Jos Luis Gmez Martnez (jolugomar@gmail.com)
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 * @author Luisa Marina Fernndez (luisam.fernandez@uclm.es)
 *
 */
public class CrsRecentsPanel extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public JTable jTable = null;
	private JScrollPane jScrollPane = null;
	private JButton infoCrs=null;
	public DefaultTableModel dtm = null;
	public TableSorter sorter = null;
	private CrsData crsDataArray[] = null;
	
	public int selectedRowTable = -1;
	private String authority = null;
	private int codeCRS = -1;
	private ICrs crs = null;
	
	public CrsRecentsPanel() {
		super();
		initialize();
	}
	
	private void initialize(){
		this.setLayout(new BorderLayout());
		JPanel p=new JPanel(new FlowLayout(FlowLayout.LEFT,15,15));
		p.add(getJLabel());
		this.add(p, BorderLayout.NORTH);
		this.add(getJScrollPane(), BorderLayout.CENTER);
		JPanel pSouth=new JPanel(new FlowLayout(FlowLayout.RIGHT,5,5));
		pSouth.add(getInfoCrs());
		this.add(pSouth,BorderLayout.SOUTH);
	}
	
	private JLabel getJLabel(){
		JLabel label = new JLabel();
		label.setText(PluginServices.getText(this, "ultimos_crs_utilizados")+":");
		return label;
	}
	/**
	 * Inicializa el panel que contiene la tabla con los crs
	 * @return
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setBorder(
				    BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(3,3,3,3),jScrollPane.getBorder()));
			jScrollPane.setViewportView(getJTable());
		}
		return jScrollPane;
	}
	/**
	 * Inicializa la tabla que contiene los crs
	 * @return
	 */
	public JTable getJTable() {
		if (jTable == null) {
			String[] columnNames= {PluginServices.getText(this,"repository"),
					PluginServices.getText(this,"codigo"),
					PluginServices.getText(this,"nombre")};
			Object[][]data = {};			
			dtm = new DefaultTableModel(data, columnNames)
			 {
				private static final long serialVersionUID = 1L;
				public boolean isCellEditable(int row, int column) {
					return false;
				}
				/*
				 * metodo necesario para cuando utilizamos tablas ordenadas
				 * ya que sino al ordenar por algun campo no se queda con el orden
				 * actual al seleccionar una fila (non-Javadoc)
				 * @see javax.swing.table.TableModel#getColumnClass(int)
				 */
				public Class getColumnClass(int column)
				{
					return getValueAt(0, column).getClass();
				}
				};
			sorter = new TableSorter(dtm);			

			jTable = new JTable(sorter);
			sorter.setTableHeader(jTable.getTableHeader());
			jTable.setCellSelectionEnabled(false);
			jTable.setRowSelectionAllowed(true);
			jTable.setColumnSelectionAllowed(false);
			jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			TableColumn column = null;
			for (int i = 0; i < 3; i++) {
			    column = jTable.getColumnModel().getColumn(i);
			    if (i == 0) {
			        column.setPreferredWidth(80); //code column is shorter			     	
			    }else if (i == 2) {
			    	column.setPreferredWidth(275);
			    }
			    else {			    
			        column.setPreferredWidth(140);
			    }
			}	
		}
		return jTable;		
	}
	
	/**
	 * Inicializa el botn que obtiene la informacin del CRS
	 * @return
	 */
	public JButton getInfoCrs() {
		if(infoCrs == null) {
			infoCrs = new JButton();
			infoCrs.setPreferredSize(new Dimension(85,20));
			infoCrs.setText(PluginServices.getText(this,"infocrs"));
			infoCrs.setMnemonic('I');
			infoCrs.setEnabled(false);
			infoCrs.setToolTipText(PluginServices.getText(this,"more_info"));
			infoCrs.addActionListener(this);
		}
		return infoCrs;
	}
	public ICrs getProjection() {		
		return crs;
	}
	
	public void setCodeCRS(int code) {
		codeCRS = code;
	}
	
	public int getCodeCRS() {
		return codeCRS;
	}
	
	public void setProjection(IProjection crs) {
		//setCrs((ICrs) crs);
	}
	
	/**
	 * Carga en la tabla los CRSs leidos del sistema de persistencia.
	 */
	public void loadRecents(){
		
//		 Eliminar filas en cada nueva bsqueda
		int numRow = dtm.getRowCount();
		while (numRow != 0) {
			numRow = numRow - 1;
			dtm.removeRow(numRow);
		}
		
		RecentCRSsPersistence persistence = new RecentCRSsPersistence();
		crsDataArray = persistence.getArrayOfCrsData();
		boolean available = true;
		for (int iRow = crsDataArray.length-1;iRow>=0;iRow--){
			Object row[] ={crsDataArray[iRow].getAuthority(),Integer.toString(crsDataArray[iRow].getCode()),crsDataArray[iRow].getName()};
			if (crsDataArray[iRow].getAuthority().equals(PluginServices.getText(this, "USR"))) {
				available = isUSR(crsDataArray[iRow].getCode());
			}			
			if (available) {
				dtm.addRow(row);
			} else {
				available = true;
			}
		}
		
		/*
		/*Seleccionar el primer registro.
		 */
		int numr = dtm.getRowCount();
		if (numr != 0 )
			this.getJTable().setRowSelectionInterval(0,0);
	}
	
	/**
	 * metodo que nos indica si un crs de usuario esta o no en la base de datos de usuario
	 * @param code
	 * @return
	 */
	public boolean isUSR(int code) {
		NewCRSPanel usr = new NewCRSPanel();		
		return usr.isInBD(code);
	}

	public ICrs getCrs() {
		return crs;
	}
	
	public void initCrs(){
		
		selectedRowTable = getJTable().getSelectedRow();		        			        	
    	Integer.parseInt((String)sorter.getValueAt(selectedRowTable,1));
		setAuthority((String)sorter.getValueAt(selectedRowTable,0)+":"+(String)sorter.getValueAt(selectedRowTable,1));
    	
		codeCRS = Integer.parseInt((String)sorter.getValueAt(selectedRowTable,1));
		try {
			crs = new CrsFactory().getCRS(getAuthority());
		} catch (CrsException e) {
			e.printStackTrace();
		}
	}
	
	public String getAuthority() {
		return authority;
	}
	
	public void setAuthority(String aut) {
		this.authority = aut;
	}

	public void actionPerformed(ActionEvent e) {
		/*Si el objeto que genera el evento es el JButton 'InfoCrs'
		se muestra la informacin ralicionada con el Crs seleccionado en la tabla*/
		if (e.getSource() == this.getInfoCrs()) {
			String[] aut = getAuthority().split(":");			
			InfoCRSPanel info = new InfoCRSPanel(aut[0], getCodeCRS());
			PluginServices.getMDIManager().addWindow(info);
		}
		
	}

}
