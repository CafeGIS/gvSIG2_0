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
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.gvsig.crs.CrsException;
import org.gvsig.crs.CrsFactory;
import org.gvsig.crs.ICrs;
import org.gvsig.crs.ogr.TransEPSG;
import org.gvsig.crs.persistence.CompTrData;
import org.gvsig.crs.persistence.RecentCRSsPersistence;
import org.gvsig.crs.persistence.RecentTrsPersistence;
import org.gvsig.crs.persistence.TrData;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.gui.TableSorter;

import es.idr.teledeteccion.connection.EpsgConnection;

/**
 * Panel para la gestin de las transformaciones recientes
 * @author Jos Luis Gmez Martnez (jolugomar@gmail.com)
 * @author Luisa Marina Fernndez (luisam.fernandez@uclm.es)
 *
 */
public class TransformationRecentsPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	int code = 0;
	private JTable transformationTable;	
	private JScrollPane jScrollPane = null;
	public DefaultTableModel dtm = null;
	public TableSorter sorter = null;
	JButton JButtonInfo = null;
	public int selectedRowTable = -1;
	private TrData[] trDataArray;
	
	private String cadWKT = "";
	private String params ="";
	public EpsgConnection connect = null;
	boolean inverseTransformation = false;
	int trCode;
	boolean targetNad = false;
	String sourceAbrev = null;

	public TransformationRecentsPanel() {
		initialize();
	}
	
	/**
	 * Genera el panel contenedor de las transformaciones recientes
	 * @return
	 */
	private void initialize(){
		connect = new EpsgConnection();
		connect.setConnectionEPSG();
			//**setLayout(new GridLayout(2,1));
			//**setLayout(new FlowLayout(FlowLayout.LEADING,5,10));
			//**setPreferredSize(new Dimension(525, 100));
		setLayout(new BorderLayout());
		setBorder(
				    BorderFactory.createCompoundBorder(
							BorderFactory.createCompoundBorder(
									BorderFactory.createTitledBorder(PluginServices.getText(this,"recents_transformation")),
									BorderFactory.createEmptyBorder(2,2,2,2)),
									getBorder()));
		JPanel p=new JPanel();
		p.setLayout(new FlowLayout(FlowLayout.LEFT,10,10));
		p.add(getJButtonInfo());
		add(p,BorderLayout.NORTH);
		add(getJScrollPane(),BorderLayout.CENTER);	
			//loadRecents();
	}
	
	public JTable getJTable() {
		if (transformationTable == null) {
			String[] columnNames= {PluginServices.getText(this,"transformation"),
					PluginServices.getText(this,"name"),
					PluginServices.getText(this,"source_crs"),
					PluginServices.getText(this,"target_crs"),
					PluginServices.getText(this,"detalles"),};					
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
				 * actual al seleccionar una fila
				 * @see javax.swing.table.TableModel#getColumnClass(int)
				 */
				public Class getColumnClass(int column)
				{
					return getValueAt(0, column).getClass();
				}
			};
			sorter = new TableSorter(dtm);			

			transformationTable = new JTable(sorter);
			sorter.setTableHeader(transformationTable.getTableHeader());	
			transformationTable.setCellSelectionEnabled(false);
			transformationTable.setRowSelectionAllowed(true);
			transformationTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			transformationTable.setColumnSelectionAllowed(false);
			transformationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			TableColumn column = null;
			for (int i = 0; i < columnNames.length; i++) {
			    column = transformationTable.getColumnModel().getColumn(i);
			    if (i == 0) {
			    	column.setPreferredWidth(80);
			    }
			    else if(i == 4) {
			    	column.setPreferredWidth(200);
			    }
			    else {			    
			        column.setPreferredWidth(100);
			    }
			}			
		}
		return transformationTable;
	}
	
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			//jScrollPane.setPreferredSize(new Dimension(525,200));
			jScrollPane.setBorder(
				    BorderFactory.createCompoundBorder(
					BorderFactory.createCompoundBorder(
							BorderFactory.createTitledBorder(PluginServices.getText(this,"transformations")),
							BorderFactory.createEmptyBorder(12,5,70,5)),
							jScrollPane.getBorder()));
			jScrollPane.setViewportView(getJTable());
		}
		return jScrollPane;
	}
	
	public JButton getJButtonInfo() {
		if(JButtonInfo == null) {
			JButtonInfo = new JButton();
			//Poner: JButtonInfo.setPreferredSize(new Dimension(100,20));			
			JButtonInfo.setText(PluginServices.getText(this,"info_transformations"));			
			JButtonInfo.setMnemonic('I');
			JButtonInfo.setEnabled(false);			
		}
		return JButtonInfo;
	}
	
	/**
	 * Consigue el crs elegido de las transformaciones
	 * recientes cuando el se utiliza transformacin EPSG o manual
	 * @return
	 */
	public ICrs getProjection() {
		ICrs crs = null;
		try {
			String[] sourceAuthority = getSourceAbrev().split(":");
			//crs = new CrsFactory().getCRS(getCode(), getWKT(),getParams());
			crs = new CrsFactory().getCRS(sourceAuthority[0]+":"+getCode());
			crs.setTransformationParams(getParams(),null);
		} catch (CrsException e) {
			e.printStackTrace();
		}
		return crs;
	}
	
	/**
	 * Consigue el crs elegido de las transformaciones recientes compuestas
	 * cuando se utiliza una transformacion compuesta
	 * @param info
	 * @return
	 */
	public ICrs getProjectionComplex(String details) {
		ICrs crs = null;
		RecentTrsPersistence trPersistence = new RecentTrsPersistence();
		TrData crsTrDataArray[] = trPersistence.getArrayOfTrData();
		CompTrData comp = null;
		String sourceTransformation = null;
		String targetTransformation = null;
		for (int iRow = crsTrDataArray.length-1; iRow >= 0; iRow--) {
			if (details.equals(crsTrDataArray[iRow].getDetails())) {
				if (crsTrDataArray[iRow] instanceof CompTrData) {
					comp = (CompTrData) crsTrDataArray[iRow];
					break;
				}
			}
		}
		
		if (comp.getFirstTr().getAuthority().equals(PluginServices.getText(this, "EPSG"))) {
			TransEPSG epsgParams = new TransEPSG(comp.getFirstTr().getCode(),connect, getInverseTransformation());
			setParamsEPGS(epsgParams.getParamValue());
			sourceTransformation = getParams();
		} else if (comp.getFirstTr().getAuthority().equals(PluginServices.getText(this, "USR"))) {
			//mirar si funciona y lleva los [  ]
			setParamsManual(comp.getFirstTr().getDetails());
			sourceTransformation = getParams();
		} else if (comp.getFirstTr().getAuthority().equals(PluginServices.getText(this, "NADGR"))) {
			String info = comp.getFirstTr().getDetails();
			String[] partes = info.split("\\(");
			String nadFile = partes[0];		
			sourceTransformation = "+nadgrids="+nadFile;			
		}
		
		if (comp.getSecondTr().getAuthority().equals(PluginServices.getText(this, "EPSG"))) {
			TransEPSG epsgParams = new TransEPSG(comp.getSecondTr().getCode(),connect, getInverseTransformation());
			setParamsEPGS(epsgParams.getParamValue());
			targetTransformation = getParams();
		} else if (comp.getSecondTr().getAuthority().equals(PluginServices.getText(this, "USR"))) {
			//mirar si funciona y lleva los [  ]
			setParamsManual(comp.getSecondTr().getDetails());
			targetTransformation = getParams();
		} else if (comp.getSecondTr().getAuthority().equals(PluginServices.getText(this, "NADGR"))) {
			String info = comp.getSecondTr().getDetails();
			String[] partes = info.split("\\(");
			String nadFile = partes[0];		
			targetTransformation = "+nadgrids="+nadFile;			
		}
		
		try {
			crs = new CrsFactory().getCRS(getSourceAbrev());
			crs.setTransformationParams(sourceTransformation,targetTransformation);//nadFile);
			return crs;
		} catch (org.gvsig.crs.CrsException e) {
			e.printStackTrace();
		}
				
		return crs;
	}
	
	/**
	 * Consigue el crs elegido de las transformaciones
	 * recientes cuando se utiliza transformacin de rejillas
	 * @param info
	 * @return
	 */
	public ICrs getProjectionNad(String info) {
		String[] partes = info.split("\\(");
		String nadFile = partes[0];		
		int codigoNad = Integer.parseInt((partes[1].substring(0,partes[1].length()-1)).split(":")[1]);
		ICrs crs;
		String[] sourceAuthority = getSourceAbrev().split(":");
		
		if (getCode() == codigoNad){
			try {
				setNad(false);
				//Siempre EPSG porque solo permitimos transformaciones
				//en el caso en que source y target sean de la EPSG
				crs = new CrsFactory().getCRS(sourceAuthority[0]+":"+getCode());
				crs.setTransformationParams("+nadgrids="+nadFile,null);//nadFile);
				return crs;
			} catch (org.gvsig.crs.CrsException e) {
				e.printStackTrace();
			}
			return null;
		}
		else {	
			setNad(true);
			try {
				crs = new CrsFactory().getCRS(sourceAuthority[0]+":"+getCode());
				crs.setTransformationParams(null,"+nadgrids="+nadFile);//nadFile);
				
				return crs;
			} catch (CrsException e) {				
				e.printStackTrace();
			}
			return null;
		}	
	}
	
	/**
	 * 
	 * @param nadg Define si el fichero rejillas se calcula en el crs fuente o destino
	 */
	public void setNad(boolean nadg){
		targetNad = nadg;		
	}
	
	/**
	 * 
	 * @return Devuelve si el fichero de rejillas se calcula en el crs fuente o destino
	 */
	public boolean getNad(){
		return targetNad;		
	}
	
	/**
	 * 
	 * @param cod Cdigo del CRS elegido
	 */
	public void setCode(int cod){
		code = cod;
	}
	
	/**
	 * 
	 * @return Devuelve el cdigo del CRS elegido
	 */
	public int getCode(){
		return code;
	}
	
	/**
	 * 
	 * @param cad Cadena wkt del crs fuente
	 */
	public void setWKT(String cad){
		cadWKT = cad;		
	}	
	
	/**
	 * 
	 * @return Devuelve la cadena wkt del crs fuente
	 */
	public String getWKT(){
		return cadWKT;
	}
	
	/**
	 * 
	 * @param param Hacemos la cadena con los parmetros de la transformacin manual
	 */
	public void setParamsManual(String param){
		params = "+towgs84="+param.substring(1,param.length()-1)+" ";
	}
	
	/**
	 * 
	 * @param values Hacemos la cadena con los parmetros de la transformacin EPSG
	 */
	public void setParamsEPGS(String[] values){
		params = "+towgs84="+values[0];
		for(int i = 1; i < values.length; i++)
			params +=","+values[i];
		params += " ";
	}
	
	/**
	 * 
	 * @param nadfile
	 */
	public void setParamsNads(String nadfile){
		
	}
	
	/**
	 * 
	 * @return Deuelve una cadena con los parmetros de la transformacin
	 */
	public String getParams(){
		return params;
	}
	
	/**
	 * 
	 * @param inverse Parmetro que define si la transformacin es directa o inversa
	 */
	public void setInverseTransformation(boolean inverse){
		inverseTransformation = inverse;
	}
	
	/**
	 * 
	 * @return Devuelve si es una transformacin directa o inversa
	 */
	public boolean getInverseTransformation(){
		return inverseTransformation;
	}
	
	/**
	 * 
	 * @param code Cdigo de la transformacin EPGS elegida
	 */
	public void setTrCode(int code){
		trCode = code;
	}
	
	/**
	 * 
	 * @return Devuelve el cdigo de la transformacin
	 */
	public int getTrCode(){
		return trCode;
	}
	
	/**
	 * Carga en la tabla los CRSs leidos del sistema de persistencia.
	 */
	public void loadRecents(String source, String target){
		setSourceAbrev(source);
		//		Eliminar filas en cada nueva bsqueda
		int numRow = dtm.getRowCount();
		while (numRow != 0) {
			numRow = numRow - 1;
			dtm.removeRow(numRow);
		}
		RecentTrsPersistence persistence = new RecentTrsPersistence();
		trDataArray = persistence.getArrayOfTrData();
		

		for (int iRow = trDataArray.length-1;iRow>=0;iRow--){
			String crsSource = ((String)trDataArray[iRow].getCrsSource());
			String crsTarget = ((String)trDataArray[iRow].getCrsTarget());
			if(source.equals(crsSource) && target.equals(crsTarget)){
				Object row[] ={trDataArray[iRow].getAuthority()+":"+trDataArray[iRow].getCode(),trDataArray[iRow].getName(),trDataArray[iRow].getCrsSource(),
					trDataArray[iRow].getCrsTarget(),trDataArray[iRow].getDetails()};
				dtm.addRow(row);
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
	 * Carga en la tabla los CRSs leidos del sistema de persistencia, para
	 * las transformaciones compuestas
	 */
	public void loadRecentsCompuesta(String source){
		
		//		Eliminar filas en cada nueva bsqueda
		int numRow = dtm.getRowCount();
		while (numRow != 0) {
			numRow = numRow - 1;
			dtm.removeRow(numRow);
		}
		RecentTrsPersistence persistence = new RecentTrsPersistence();
		trDataArray = persistence.getArrayOfTrData();
		

		for (int iRow = trDataArray.length-1;iRow>=0;iRow--){
			if (trDataArray[iRow] instanceof CompTrData) {
				continue;
			} else {
				String crsSource = ((String)trDataArray[iRow].getCrsSource());
				String crsTarget = ((String)trDataArray[iRow].getCrsTarget());
				if(source.equals(crsSource) || source.equals(crsTarget)){
					Object row[] ={trDataArray[iRow].getAuthority()+":"+trDataArray[iRow].getCode(),trDataArray[iRow].getName(),trDataArray[iRow].getCrsSource(),
						trDataArray[iRow].getCrsTarget(),trDataArray[iRow].getDetails()};
					dtm.addRow(row);
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
	
	public void setSourceAbrev(String source) {
		sourceAbrev = source;
	}
	
	public String getSourceAbrev() {
		return sourceAbrev;
	}

}
