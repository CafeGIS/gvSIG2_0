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
import java.awt.Component;
import java.awt.Dimension;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.cresques.cts.IProjection;
import org.gvsig.crs.CrsFactory;
import org.gvsig.crs.ICrs;
import org.gvsig.crs.persistence.RecentTrsPersistence;
import org.gvsig.crs.persistence.TrData;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.gui.TableSorter;

import es.idr.teledeteccion.connection.EpsgConnection;
import es.idr.teledeteccion.connection.Query;

/**
 * Clase para la creacin y el manejo del panel de transformacin
 * para el caso de la EPSG
 * 
 * @author Jos Luis Gmez Martnez (jolugomar@gmail.com)
 * @author Luisa Marina Fernndez (luisam.fernandez@uclm.es)
 *
 */
public class TransformationEpsgPanel extends JPanel  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IProjection firstProj;
	
	String[] transformations = {"9603", "9606", "9607", "9613", "9615", "9633"};
	
	private int transformation_code = -1;
	private String[] values;
	private String params = "+towgs84=";
	
	private JTable transformationTable;	
	private JScrollPane jScrollPane = null;
	public boolean inverseTranformation = false;
	
	public EpsgConnection connect = null;	
	public int crs_target = -1;
	
	public DefaultTableModel dtm = null;
	
	private int crs_source_code;
	private String cadWKT = "";
	private ListSelectionModel lsm2 = null;
	public int selectedRowTable = -1;
	boolean tra = false;
	int real_target;
	String authority_target = "";
	
	private JTextArea info;	
	public TableSorter sorter = null;
	
	public TransformationEpsgPanel(String aut_target) {
		connect = new EpsgConnection();
		connect.setConnectionEPSG();
		String[] authority_target = aut_target.split(":");
		setAuthorityTarget(authority_target[0]);
		int target = Integer.parseInt(authority_target[1]);			
		real_target = target;
		String sentence = "SELECT source_geogcrs_code, coord_ref_sys_kind " +
			"FROM epsg_coordinatereferencesystem " +
			"WHERE coord_ref_sys_code = "+ target ;
		ResultSet result = Query.select(sentence,connect.getConnection());		
		try {
			result.next();
			String kind = result.getString("coord_ref_sys_kind");
			if (kind.equals("projected"))
				target = result.getInt("source_geogcrs_code");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}        
		crs_target = target;
		initialize();
	}
	
	private void initialize(){
			setLayout(new BorderLayout(1,50));
			setBorder(
				    BorderFactory.createCompoundBorder(
							BorderFactory.createCompoundBorder(
									BorderFactory.createTitledBorder("Transformacion EPSG"),
									BorderFactory.createEmptyBorder(12,2,80,2)),
									getBorder()));
			add(getJScrollPane(),BorderLayout.CENTER);
	}
	
	private Component getInfo() {
		if (info == null){
			info = new JTextArea();
			info.setLineWrap(true);
			info.setWrapStyleWord(true);
			info.setPreferredSize(new Dimension(400, 240));
			info.setEditable(false);
			info.append(getWKT());
		}
		info.setText(getWKT());
		return info;
	}
	
	public JTable getJTable() {
		if (transformationTable == null) {
			String[] columnNames= {PluginServices.getText(this,"code_transformation"),
					PluginServices.getText(this,"name_transformation"),
					PluginServices.getText(this,"type_transformation"),
					PluginServices.getText(this,"source_crs"),
					PluginServices.getText(this,"target_crs"),
					PluginServices.getText(this,"description_area")};
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
			    if (i == 0 || i == 3 || i == 4) {
			        column.setPreferredWidth(40);			     	
			    }else if (i == 2) {
			    	column.setPreferredWidth(80);
			    }
			    else {			    
			        column.setPreferredWidth(160);
			    }
			}			
		}
		return transformationTable;
	}
	
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setPreferredSize(new Dimension(525,200));
			jScrollPane.setBorder(
				    BorderFactory.createCompoundBorder(
					BorderFactory.createCompoundBorder(
							BorderFactory.createTitledBorder(PluginServices.getText(this,"transformations")),
							BorderFactory.createEmptyBorder(5,5,5,5)),
							jScrollPane.getBorder()));
			jScrollPane.setViewportView(getJTable());
		}
		return jScrollPane;
	}
		
	/**
	 * Busca las tranformaciones directas entre el CRS de la capa y el CRS de la
	 * vista.
	 * @param crsCode
	 */
	private void callTransformation(int crsCode){		
        
        
        String sentence = "SELECT source_geogcrs_code " +
						"FROM epsg_coordinatereferencesystem " +
						"WHERE coord_ref_sys_code = "+ crsCode ;
		ResultSet result = Query.select(sentence,connect.getConnection());
		int source = 0;
		try {
			result.next();
			source = result.getInt("source_geogcrs_code");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
        ResultSet result2 = null;
        ResultSet result3 = null;
        if (source != 0){
        		crsCode = source;		            	
        }
        
        ArrayList codecs = new ArrayList();
        codecs.add(String.valueOf(crsCode));		
		
		for (int j=0; j< codecs.size(); j++){
			sentence = "SELECT coord_op_code, coord_op_name, coord_op_type, source_crs_code, target_crs_code, area_of_use_code, coord_op_method_code " +
					"FROM epsg_coordoperation " +                        
					"WHERE source_crs_code = " + codecs.get(j) + "AND target_crs_code = " + crs_target;

            result = Query.select(sentence,connect.getConnection());	
            
            try {
            	while(result.next()) {
            		Object[]data = new Object[6];				            		
            		data[0] = String.valueOf(result.getInt("coord_op_code"));
            		data[1] = result.getString("coord_op_name");
            		data[2] = result.getString("coord_op_type");
            		data[3] = String.valueOf(result.getInt("source_crs_code"));
            		data[4] = String.valueOf(result.getInt("target_crs_code"));
            		/*codecs.add(data[4]);
            		codecs = deleteItems(codecs);*/
            		
            		int aouc = Integer.parseInt(result.getString("area_of_use_code"));
					
					sentence = "SELECT area_of_use FROM epsg_area " +
									"WHERE area_code = "+ aouc ;
					
					result2 = Query.select(sentence,connect.getConnection());
            		while (result2.next())
            			data[5] = result2.getString("area_of_use");
            		
            		String coord_op_method = result.getString("coord_op_method_code");				            		
	            		
            		sentence = "SELECT reverse_op FROM epsg_coordoperationmethod "+
            					"WHERE coord_op_method_code LIKE " + coord_op_method;
            		result3 = Query.select(sentence,connect.getConnection());
            		
            		while(result3.next()){
            			if (Integer.parseInt(result3.getString("reverse_op")) == 1){
            				for (int i=0; i< transformations.length; i++){
            					if (coord_op_method.equals(transformations[i])){
            						dtm.addRow(data);
            					}
            				}            				
            			}
            		}
            	}
            } 	
            catch (SQLException e1) {
            	e1.printStackTrace();
            }
		}
	}
	
	/**
	 * Mtodo auxiliar para eliminar valores repetidos dentro del ArrayList
	 * de codecs.
	 * @param codecs
	 * @return
	 */
	private ArrayList deleteItems(ArrayList codecs) {
		ArrayList cod = new ArrayList();
		boolean equal = false;
		for (int i = 0; i< codecs.size(); i++){
			String c = (String) codecs.get(i);
			for (int j = 0; j<cod.size(); j++){
				if (cod.get(j).equals(c)){
					equal = true;
				}
			}
			if (!equal){
				cod.add(c);				
			}
			equal = false;
		}
		return cod;
	}
	
	/**
	 * Busca las tranformaciones inversas entre el CRS de la capa y el CRS de la
	 * vista. Aquellas cuyo valor de reverse_op sean iguales a uno.
	 * @param crsCode
	 */
	private void callInverseTransformation(int crsCode){
		//inverseTranformation = true;
        
        String sentence = "SELECT source_geogcrs_code " +
						"FROM epsg_coordinatereferencesystem " +
						"WHERE coord_ref_sys_code = "+ crsCode ;
		ResultSet result = Query.select(sentence,connect.getConnection());
		int source = 0;
		try {
			result.next();
			source = result.getInt("source_geogcrs_code");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
        ResultSet result2 = null;
        ResultSet result3 = null;
        if (source != 0){
        		crsCode = source;		            	
        }
        
        ArrayList codecs = new ArrayList();
        codecs.add(String.valueOf(crsCode));
       
		codecs = deleteItems(codecs);
		
		for (int j=0; j< codecs.size(); j++){
			sentence = "SELECT coord_op_code, coord_op_name, coord_op_type, source_crs_code, target_crs_code, area_of_use_code, coord_op_method_code " +
					"FROM epsg_coordoperation " +                        
					"WHERE source_crs_code = " + codecs.get(j) + "AND target_crs_code = " + crs_target;

            result = Query.select(sentence,connect.getConnection());	
            
            try {
            	while(result.next()) {
            		Object[]data = new Object[6];				            		
            		data[0] = String.valueOf(result.getInt("coord_op_code"));
            		data[1] = result.getString("coord_op_name");
            		data[2] = result.getString("coord_op_type");
            		data[4] = String.valueOf(result.getInt("source_crs_code"));
            		data[3] = String.valueOf(result.getInt("target_crs_code"));
            		/*codecs.add(data[4]);
            		codecs = deleteItems(codecs);*/
            		
            		int aouc = Integer.parseInt(result.getString("area_of_use_code"));
					
					sentence = "SELECT area_of_use FROM epsg_area " +
									"WHERE area_code = "+ aouc ;
					
					result2 = Query.select(sentence,connect.getConnection());
            		while (result2.next())
            			data[5] = result2.getString("area_of_use");
            		
            		String coord_op_method = result.getString("coord_op_method_code");				            		
	            		
            		sentence = "SELECT reverse_op FROM epsg_coordoperationmethod "+
            					"WHERE coord_op_method_code LIKE " + coord_op_method;
            		result3 = Query.select(sentence,connect.getConnection());
            		
            		while(result3.next()){
            			if (Integer.parseInt(result3.getString("reverse_op")) == 1){
            				for (int i=0; i< transformations.length; i++){
            					if (coord_op_method.equals(transformations[i])){
            						dtm.addRow(data);
            					}
            				}
            			}
            		}
            	}
            } 	
            catch (SQLException e1) {
            	e1.printStackTrace();
            }
		}
	}
	
	/**
	 * 
	 * @param val
	 */
	public void setValues(String[] val) {
		values = val;
	}
	
	/**
	 * 
	 * @return
	 */
	public String[] getValues() {
		return values;
	}
	
	/**
	 * 
	 * @param t_cod
	 */
	public void setTrasformation_code(int t_cod) {
		transformation_code = t_cod;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getTransformation_code() {
		return transformation_code;
	}
	
	/**
	 * 
	 * @return
	 */
	public ICrs getProjection() {
		params += values[0];
		for(int i = 1; i < values.length; i++)
			params +=","+values[i];
		try {
			//ICrs crs = new CrsFactory().getCRS(crs_source_code,cadWKT,params);
			ICrs crs = new CrsFactory().getCRS("EPSG:"+crs_source_code);
			crs.setTransformationParams(params,null);
			return crs;
		} catch (org.gvsig.crs.CrsException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param proj
	 */
	public void setProjection(IProjection proj) {
		firstProj = proj;
	}
	
	/**
	 * 
	 * @param cad
	 */
	public void setWKT(String cad){
		cadWKT = cad;
		getInfo();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getWKT(){
		return cadWKT;
	}
	
	/**
	 * Indica el CRS que se utilizar en la capa y busca las transformaciones
	 * directas e inversas que pueda tener
	 * @param code
	 */
	public void setSource(String fuente, int code){
		connect = new EpsgConnection();
		connect.setConnectionEPSG();
		inverseTranformation = false;
		crs_source_code = code;
		int numRow = dtm.getRowCount();
		
        while (numRow != 0) {
			numRow = numRow - 1;
			dtm.removeRow(numRow);
		}
		if (PluginServices.getText(this, fuente).equals(PluginServices.getText(this, "EPSG")) && PluginServices.getText(this, getAuthorityTarget()).equals(PluginServices.getText(this, "EPSG"))) {
			callTransformation(crs_source_code);
			
			int new_target = crs_target;
			int base_target = code;
			String sentence = "SELECT source_geogcrs_code, coord_ref_sys_kind " +
						"FROM epsg_coordinatereferencesystem " +
						"WHERE coord_ref_sys_code = "+ code;
			ResultSet result = Query.select(sentence,connect.getConnection());		
			try {
				result.next();
				String kind = result.getString("coord_ref_sys_kind");
				if (kind.equals("projected"))
					base_target = result.getInt("source_geogcrs_code");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}        
			crs_target = base_target;
			
			crs_source_code = new_target;
			callInverseTransformation(crs_source_code);
			crs_target = new_target;
			crs_source_code = code;
		    
			int numr = dtm.getRowCount();
			if (numr > 0 ){
				this.getJTable().setRowSelectionInterval(0,0);
			}
		}
	}
	
	/**
	 * Indica el CRS que se utilizar en la capa y busca las transformaciones
	 * directas e inversas que pueda tener para las transformaciones compuestas
	 * @param code
	 */
	public void setSourceCompuesta(String aut){
		connect = new EpsgConnection();
		connect.setConnectionEPSG();
		inverseTranformation = false;
		String[] authority = aut.split(":");
		int code = Integer.parseInt(authority[1]);
		crs_source_code = code;
		int numRow = dtm.getRowCount();
		
        while (numRow != 0) {
			numRow = numRow - 1;
			dtm.removeRow(numRow);
		}
		if (PluginServices.getText(this,authority[0]).equals(PluginServices.getText(this, "EPSG"))) {
			callTransformationCompuesta(crs_source_code);
			
			int new_target = crs_target;
			int base_target = code;
			String sentence = "SELECT source_geogcrs_code, coord_ref_sys_kind " +
						"FROM epsg_coordinatereferencesystem " +
						"WHERE coord_ref_sys_code = "+ code;
			ResultSet result = Query.select(sentence,connect.getConnection());		
			try {
				result.next();
				String kind = result.getString("coord_ref_sys_kind");
				if (kind.equals("projected"))
					base_target = result.getInt("source_geogcrs_code");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}        
			crs_target = base_target;
			
			crs_source_code = new_target;
			callInverseTransformation(crs_source_code);
			crs_target = new_target;
			crs_source_code = code;
		    
			int numr = dtm.getRowCount();
			if (numr > 0 ){
				this.getJTable().setRowSelectionInterval(0,0);
			}
		}
	}
	
	/**
	 * Busca las tranformaciones directas entre el CRS de la capa y el CRS de la
	 * vista.
	 * @param crsCode
	 */
	private void callTransformationCompuesta(int crsCode){		
        int numRow = dtm.getRowCount();
		
        while (numRow != 0) {
			numRow = numRow - 1;
			dtm.removeRow(numRow);
		}
        
        String sentence = "SELECT source_geogcrs_code " +
						"FROM epsg_coordinatereferencesystem " +
						"WHERE coord_ref_sys_code = "+ crsCode ;
		ResultSet result = Query.select(sentence,connect.getConnection());
		int source = 0;
		try {
			result.next();
			source = result.getInt("source_geogcrs_code");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
        ResultSet result2 = null;
        ResultSet result3 = null;
        if (source != 0){
        		crsCode = source;		            	
        }
        
        ArrayList codecs = new ArrayList();
        codecs.add(String.valueOf(crsCode));		
		
		for (int j=0; j< codecs.size(); j++){
			sentence = "SELECT coord_op_code, coord_op_name, coord_op_type, source_crs_code, target_crs_code, area_of_use_code, coord_op_method_code " +
					"FROM epsg_coordoperation " +                        
					"WHERE source_crs_code = " + codecs.get(j) + " OR target_crs_code = "+ codecs.get(j);

            result = Query.select(sentence,connect.getConnection());	
            
            try {
            	while(result.next()) {
            		Object[]data = new Object[6];				            		
            		data[0] = String.valueOf(result.getInt("coord_op_code"));
            		data[1] = result.getString("coord_op_name");
            		data[2] = result.getString("coord_op_type");
            		data[3] = String.valueOf(result.getInt("source_crs_code"));
            		data[4] = String.valueOf(result.getInt("target_crs_code"));
            		/*codecs.add(data[4]);
            		codecs = deleteItems(codecs);*/
            		
            		int aouc = Integer.parseInt(result.getString("area_of_use_code"));
					
					sentence = "SELECT area_of_use FROM epsg_area " +
									"WHERE area_code = "+ aouc ;
					
					result2 = Query.select(sentence,connect.getConnection());
            		while (result2.next())
            			data[5] = result2.getString("area_of_use");
            		
            		String coord_op_method = result.getString("coord_op_method_code");				            		
	            		
            		sentence = "SELECT reverse_op FROM epsg_coordoperationmethod "+
            					"WHERE coord_op_method_code LIKE " + coord_op_method;
            		result3 = Query.select(sentence,connect.getConnection());
            		
            		while(result3.next()){
            			if (Integer.parseInt(result3.getString("reverse_op")) == 1){
            				for (int i=0; i< transformations.length; i++){
            					if (coord_op_method.equals(transformations[i])){
            						dtm.addRow(data);
            					}
            				}            				
            			}
            		}
            	}
            } 	
            catch (SQLException e1) {
            	e1.printStackTrace();
            }
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public int getSource(){
		return crs_source_code;
	}
	
	public int getTarget(){
		return real_target;
	}
	
	public void setAuthorityTarget(String aut_target) {
		authority_target = aut_target;
	}
	
	public String getAuthorityTarget() {
		return authority_target;
	}
	
	/**
	 * cuando utilizamos crs+transformacion, cargamos los paneles para que
	 * el usuario pueda consultar la transformacion utilizada...
	 * @param details
	 */
	public void fillData(String details) {
		RecentTrsPersistence trPersistence = new RecentTrsPersistence();
		TrData crsTrDataArray[] = trPersistence.getArrayOfTrData();
		
		for (int iRow = crsTrDataArray.length-1; iRow >= 0; iRow--) {
			if (details.equals(crsTrDataArray[iRow].getAuthority()+":"+crsTrDataArray[iRow].getCode()+" <--> "+crsTrDataArray[iRow].getDetails()) && crsTrDataArray[iRow].getAuthority().equals(PluginServices.getText(this, "EPSG"))) {
				String code = String.valueOf(crsTrDataArray[iRow].getCode());
				
				for (int i=0; i< getJTable().getRowCount(); i++) {
					if (code.equals((String)getJTable().getValueAt(i, 0))) {
						//seleccionarlo						
						this.getJTable().setRowSelectionInterval(i,i);
						break;						
					}
				}
				break;
			}
		}
	}
	
	/**
	 * metodo que resetea el formulario
	 * @return
	 */
	public void resetData() {
		if (dtm.getRowCount()>0) {
			this.getJTable().setRowSelectionInterval(0,0);
		}
		
        
	}
	
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo=new WindowInfo(WindowInfo.MODALDIALOG);
   		m_viewinfo.setTitle("Transformation EPSG");
		return m_viewinfo;
	}	

}
