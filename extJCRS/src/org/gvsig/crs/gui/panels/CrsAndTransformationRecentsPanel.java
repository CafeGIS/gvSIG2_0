/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
import java.util.ArrayList;
import java.util.Date;

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
import org.gvsig.crs.CrsWkt;
import org.gvsig.crs.ICrs;
import org.gvsig.crs.persistence.CrsData;
import org.gvsig.crs.persistence.RecentCRSsPersistence;
import org.gvsig.crs.persistence.RecentTrsPersistence;
import org.gvsig.crs.persistence.TrData;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.gui.TableSorter;

public class CrsAndTransformationRecentsPanel extends JPanel implements ActionListener {

	/**
	 * Panel para la selección del CRS de la vista y la transformacion si hubiera
	 * alguna entre el crs elegido para la capa con algun crs de destino que
	 * haya sido utilizada recientemente
	 * 
	 * @author Jos� Luis G�mez Mart�nez (JoseLuis.Gomez@uclm.es)
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public JTable jTable = null;
	private JScrollPane jScrollPane = null;
	private JButton infoCrs=null;
	public DefaultTableModel dtm = null;
	public TableSorter sorter = null;
	private CrsData crsDataArray[] = null;
	private TrData crsTrDataArray[] = null;
	
	public int selectedRowTable = -1;
	private String authority = null;
	private int codeCRS = -1;
	private ICrs crs = null;
	private ArrayList recents = null;
		
	public CrsAndTransformationRecentsPanel() {
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
			String[] columnNames= {PluginServices.getText(this,"fuente"),
					PluginServices.getText(this,"codigo"),
					PluginServices.getText(this,"nombre"),
					PluginServices.getText(this,"transformation")};
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
			for (int i = 0; i < columnNames.length; i++) {
			    column = jTable.getColumnModel().getColumn(i);
			    if (i == 0) {
			        column.setPreferredWidth(60); //code column is shorter			     	
			    }else if (i == 1) {
			    	column.setPreferredWidth(60);
			    }
			    else {			    
			        column.setPreferredWidth(240);
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
	public void loadRecents(CrsWkt crsWkttarget, ICrs curCrs){
		RecentTrsPersistence trPersistence = new RecentTrsPersistence();
		crsTrDataArray = trPersistence.getArrayOfTrData();
		RecentCRSsPersistence persistence = new RecentCRSsPersistence();
		CrsData crsData = new CrsData(curCrs.getCrsWkt().getAuthority()[0], curCrs.getCode(),curCrs.getCrsWkt().getName());
		if (curCrs.getSourceTransformationParams() == null && curCrs.getTargetTransformationParams() == null)
			persistence.addCrsData(crsData);
		crsDataArray = persistence.getArrayOfCrsData();	
		boolean available = true;
		
		// Eliminar filas en cada nueva bsqueda
		int numRow = dtm.getRowCount();
		while (numRow != 0) {
			numRow = numRow - 1;
			dtm.removeRow(numRow);
		}
		
		/**
		 * Tendremos que hacer la distincion de si lleva o no transformacion
		 * para la persistencia de la misma.
		 */
		int initialCrsTr = crsTrDataArray.length-1;
		int initialCrs = crsDataArray.length-1;
		
		if (curCrs.getSourceTransformationParams() != null || curCrs.getTargetTransformationParams() != null) {
			initialCrsTr = crsTrDataArray.length-1;
			Object first[] ={crsTrDataArray[initialCrsTr].getCrsSource().split(":")[0],crsTrDataArray[initialCrsTr].getCrsSource().split(":")[1],crsTrDataArray[initialCrsTr].getName(),crsTrDataArray[initialCrsTr].getAuthority()+":"+crsTrDataArray[initialCrsTr].getCode()+" <--> "+crsTrDataArray[initialCrsTr].getDetails()};
			if (crsTrDataArray[initialCrsTr].getAuthority().equals(PluginServices.getText(this, "USR"))) {
				available = isUSR(crsTrDataArray[initialCrsTr].getCode());
			}
			if (available) {
				dtm.addRow(first);
				initialCrsTr = crsTrDataArray.length-2;
			} else {
				available = true;
			}
		}		
		else {
			initialCrs = crsDataArray.length-1;
			Object first[] ={crsDataArray[initialCrs].getAuthority(),Integer.toString(crsDataArray[initialCrs].getCode()),crsDataArray[initialCrs].getName(),PluginServices.getText(this, "sin_transformacion")};
			if (crsDataArray[initialCrs].getAuthority().equals(PluginServices.getText(this, "USR"))) {
				available = isUSR(crsDataArray[initialCrs].getCode());
			}
			if (available) {
				dtm.addRow(first);			
				initialCrs =crsDataArray.length-2;
			} else {
				available = true;
			}
		}			
		
		int iRowCrs = initialCrs;
		int iRowTr = initialCrsTr;
		
		while (iRowCrs>=0 && iRowTr>=0) {
			if (crsDataArray[iRowCrs].getDate().after(crsTrDataArray[iRowTr].getDate())){
				if (crsDataArray[iRowCrs].getAuthority().equals(PluginServices.getText(this, "USR"))) {
					available = isUSR(crsDataArray[iRowCrs].getCode());
				}
				if (available) {
					Object row[] ={crsDataArray[iRowCrs].getAuthority(),Integer.toString(crsDataArray[iRowCrs].getCode()),crsDataArray[iRowCrs].getName(),PluginServices.getText(this, "sin_transformacion")};
					dtm.addRow(row);
				} else {
					available = true;
				}
				iRowCrs--;
			} else {
				String target = crsWkttarget.getAuthority()[0]+":"+crsWkttarget.getAuthority()[1];
				String crsSource = ((String)crsTrDataArray[iRowTr].getCrsSource());			
				String crsTarget = ((String)crsTrDataArray[iRowTr].getCrsTarget());			
				if(target.equals(crsTarget)){
					if (crsTrDataArray[iRowTr].getAuthority().equals(PluginServices.getText(this, "USR"))) {
						available = isUSR(crsTrDataArray[iRowTr].getCode());
					}
					if (available) {
						Object row[] = {crsSource.split(":")[0],crsSource.split(":")[1],crsTrDataArray[iRowTr].getName(),crsTrDataArray[iRowTr].getAuthority()+":"+crsTrDataArray[iRowTr].getCode()+" <--> "+crsTrDataArray[iRowTr].getDetails()};
						dtm.addRow(row);
					} else {
						available = true;
					}
				}
				iRowTr--;
			}
		}
		if (iRowTr>=0) {
			for (;iRowTr>=0;iRowTr--){
				String target = crsWkttarget.getAuthority()[0]+":"+crsWkttarget.getAuthority()[1];
				String crsSource = ((String)crsTrDataArray[iRowTr].getCrsSource());			
				String crsTarget = ((String)crsTrDataArray[iRowTr].getCrsTarget());			
				if(target.equals(crsTarget)){					
					if (crsTrDataArray[iRowTr].getAuthority().equals(PluginServices.getText(this, "USR"))) {
						available = isUSR(crsTrDataArray[iRowTr].getCode());
					}
					if (available) {
						Object row[] = {crsSource.split(":")[0],crsSource.split(":")[1],crsTrDataArray[iRowTr].getName(),crsTrDataArray[iRowTr].getAuthority()+":"+crsTrDataArray[iRowTr].getCode()+" <--> "+crsTrDataArray[iRowTr].getDetails()};
						dtm.addRow(row);
					} else {
						available = true;
					}
				}
			}
		}
		else {
			for (;iRowCrs>=0;iRowCrs--){
				if (crsDataArray[iRowCrs].getAuthority().equals(PluginServices.getText(this, "USR"))) {
					available = isUSR(crsDataArray[iRowCrs].getCode());
				}
				if (available) {
					Object row[] ={crsDataArray[iRowCrs].getAuthority(),Integer.toString(crsDataArray[iRowCrs].getCode()),crsDataArray[iRowCrs].getName(),PluginServices.getText(this, "sin_transformacion")};
					dtm.addRow(row);
				} else {
					available = true;
				}
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
			InfoCRSPanel info = new InfoCRSPanel(aut[0], getCodeCRS(), (String)getJTable().getValueAt(getJTable().getSelectedRow(), 3));
			PluginServices.getMDIManager().addWindow(info);
		}
		
	}	
}
