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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.cresques.cts.IProjection;
import org.gvsig.crs.CrsException;
import org.gvsig.crs.CrsFactory;
import org.gvsig.crs.ICrs;
import org.gvsig.crs.ogr.Epsg2wkt;
import org.gvsig.crs.ogr.CrsEPSG;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.gui.TableSorter;

import es.idr.teledeteccion.connection.EpsgConnection;
import es.idr.teledeteccion.connection.Query;

/**
 * Clase perteneciente al panel de eleccin de CRS del repositorio de la
 * EPSG, realiza la bsqueda y filtra los vlidos.
 * 
 * @author Jos Luis Gmez Martnez (jolugomar@gmail.com)
 * @author Luisa Marina Fernndez (luisam.fernandez@uclm.es)
 *
 */
public class EPSGpanel extends JPanel implements KeyListener, ActionListener{

	private static final long serialVersionUID = 1L;
	
	String cadWKT = "";
	String cad_valida = "";
	public String key;
	public String crs_kind = null;
	String[] soported_crs = {"projected","geographic 2D","geographic 3D", "engineering", "vertical", "compound", "geocentric"};
	String[] not_soported_crs = {};
	
	int iteracion = 0;
	int transf = 0;
	int source_cod = 0;
	int method_code = 0;
	int datum_code = 0;
	int projection_conv_code = 0;
	public int epsg_code  = 0;
	public int selectedRowTable = -1;
	private int codeCRS = -1;
	int[] valid_method_code = {9800, 9801, 9802, 9803, 9804, 9805, 9806, 9807, 9808, 9809, 9810, 
			9811, 9812, 9813, 9814, 9815, 9816, 9817, 9602, 9659, 9818, 9819, 9820, 9822, 
			9827};
	
	
	boolean tecla_valida = false;
	boolean source_yn = false;
	

	private JRadioButton codeRadioButton = null;
	private JRadioButton nameRadioButton = null;
	private JRadioButton areaRadioButton = null;
	private ButtonGroup optGroup;
	private JPanel groupRadioButton = null;
	
	public EpsgConnection connect = null;
	public JPanel EPSGpanel = null;
	private JLabel lblCriterio;
	private JButton infoCrs = null;
	private JButton searchButton = null;
	private JTextField searchTextField = null;
	public JTable jTable = null;
	private JScrollPane jScrollPane = null;
	public DefaultTableModel dtm = null;	
	public TableSorter sorter = null;
	
	private int projectionCode=-1;
	
	/**
	 * Construye el Objeto EpsgPanel
	 *
	 */
	public EPSGpanel(){
		initialize();
	}
	/*
	 * Define las propiedades y agrega los componentes del EPSGPanel
	 */
	private void initialize(){
		this.setLayout(new BorderLayout());
		JPanel pNorth=new JPanel();
		pNorth.setLayout(new GridLayout(2,1));
		pNorth.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
		pNorth.add(getGroupRadioButton());
		JPanel pInNorth = new JPanel();
		pInNorth.setLayout(new FlowLayout(FlowLayout.LEFT,10,1));
		pInNorth.add(getSearchButton());
		pInNorth.add(getSearchTextField());
		pNorth.add(pInNorth);
		this.add(pNorth,BorderLayout.NORTH);
		this.add(getJScrollPane(), BorderLayout.CENTER);	
		JPanel pSouth=new JPanel(new FlowLayout(FlowLayout.RIGHT,10,3));
		pSouth.add(getInfoCrs());
		this.add(pSouth,BorderLayout.SOUTH);
	}
	/**
	 * Establece la conexin con la Base de Datos de la Epsg
	 *
	 */
	public void connection(){
		connect = new EpsgConnection();
		connect.setConnectionEPSG();
	}
	 /**
	  * Inicializa el botn de opcin 'cdigo' 
	  * @return
	  */   
	private JRadioButton getCodeRadioButton() {
		if (codeRadioButton == null) {
			codeRadioButton = new JRadioButton();
			codeRadioButton.setText(PluginServices.getText(this,"por_codigo"));//setText("By Code EPSG");
			codeRadioButton.setSelected(true);
			codeRadioButton.addActionListener(this);
		}
		return codeRadioButton;
	}
	/**
	 * Inicializa el botn de opcin 'nombre'
	 * @return
	 */ 
	private JRadioButton getNameRadioButton() {
		if (nameRadioButton == null) {
			nameRadioButton = new JRadioButton();
			nameRadioButton.setText(PluginServices.getText(this,"por_nombre"));
			nameRadioButton.addActionListener(this);
		}
		return nameRadioButton;
	}
	/**
	 * Inicializa el botn de opcin 'Area'
	 * @return
	 */
	private JRadioButton getAreaRadioButton() {
		if (areaRadioButton == null) {
			areaRadioButton = new JRadioButton();
			areaRadioButton.setText(PluginServices.getText(this,"por_area"));
			areaRadioButton.addActionListener(this);
		}
		return areaRadioButton;
	}
	 /**
	  * Obtiene el panel con todos los botones de opcin
	  * @return
	  */
	private JPanel getGroupRadioButton() {
		if (groupRadioButton == null) {
			groupRadioButton = new JPanel();
			groupRadioButton.setLayout(new GridLayout(1,4));
			groupRadioButton.add(getLblCriterio());
			groupRadioButton.add(getCodeRadioButton());
			groupRadioButton.add(getNameRadioButton());
			groupRadioButton.add(getAreaRadioButton());
			//agrupar los botones
			getOptGroup();
		}
		return groupRadioButton;
	}
	/**
	 * Inicializa el laber 'Criterio de bsqueda'
	 * @return
	 */
	private JLabel getLblCriterio(){
		lblCriterio = new JLabel();
		lblCriterio.setText(PluginServices.getText(this, "criterio_busqueda")+":");
		return lblCriterio;
	}

	/**
	 * Mtodo que controla la bsqueda de los CRS siguiendo los criterios
	 * de bsqueda que le hemos definido. Tambin gestiona los casos en que
	 * no encuentre CRS, o que los parmetros de bsqueda sean errneos. Si
	 * encuentra algn CRS pero no es soportado por la aplicacin
	 * aparecer el mensaje de informacin correspondiente.
	 *
	 */
	private void searchButton() {
		boolean not_valid = false;
		boolean not_numeric = false;
		searchTextField.setBackground(Color.white);
		
		if (searchTextField.getText().equals("")) {
			searchTextField.setBackground(new Color(255,204,204));
			JOptionPane.showMessageDialog(this, PluginServices.getText(this,"fill_name"), "Warning...", JOptionPane.WARNING_MESSAGE);
		}		
		
		else {
            //Eliminar filas en cada nueva bsqueda
			int numRow = dtm.getRowCount();
			while (numRow != 0) {
				numRow = numRow - 1;
				dtm.removeRow(numRow);
			}
			
			if (codeRadioButton.isSelected() && (searchTextField.getText().length()!=searchTextField.getText().replaceAll("[^0-9]", "").length())){
				not_numeric = true;
			}
			
			//Dependiendo de la opcion se realizada una busqueda
			ResultSet result = null;
			ResultSet result2 = null;
			/*
			 * variable que indicara si la busqueda se hace primero en epsg_coordinatereferencesystem
			 * o en epsg_area; esto es debido a que HSQLDB no soporta la bsqueda simultnea en ambas
			 * tablas, por lo cual hay que separar la bsqueda
			 */
			int bus = 2;  			
			if (codeRadioButton.isSelected() && !not_numeric) {
				bus=0;
				key = searchTextField.getText();
				int code = Integer.parseInt(key);
				String sentence = "SELECT coord_ref_sys_code, coord_ref_sys_name, coord_ref_sys_kind, area_of_use_code, " +
								  "source_geogcrs_code, projection_conv_code  " +
								  "FROM epsg_coordinatereferencesystem " +	                              
	                              "WHERE coord_ref_sys_code = " + code;
								
				result = Query.select(sentence,connect.getConnection());	
				
			}
			
			else if (nameRadioButton.isSelected()) {
				bus=0;
				key = searchTextField.getText();
				key = key.toLowerCase();
				String key2 = key.substring(0,1);
				String key3 = key.substring(1,key.length());
				key2 = key2.toUpperCase();
				
				String sentence = "SELECT coord_ref_sys_code, coord_ref_sys_name, coord_ref_sys_kind, " +
									"area_of_use_code, source_geogcrs_code, projection_conv_code " +
				  					"FROM epsg_coordinatereferencesystem " +	                              
				  					"WHERE (coord_ref_sys_name LIKE '%" + key + "%') OR (coord_ref_sys_name LIKE '%"+ 
				  					key.toUpperCase() +"%') " +
				  					"OR (coord_ref_sys_name LIKE '%" + key2+key3 +"%')";
				result = Query.select(sentence,connect.getConnection());
			}
			
			else if (areaRadioButton.isSelected()) {
				bus=1;
				key = searchTextField.getText();
				key = key.toLowerCase();				
				String key2 = key.substring(0,1);
				String key3 = key.substring(1,key.length());
				key2 = key2.toUpperCase();				
				
				String sentence = "SELECT area_name, area_of_use, area_code " +
	                              "FROM epsg_area " +
	                              "WHERE (area_name LIKE '%" + key + "%') OR (area_of_use LIKE '%" + key + "%') "+
	                              "OR (area_name LIKE '%" + key.toUpperCase() + "%') OR (area_of_use LIKE '%" + key.toUpperCase() + "%') "+
	                              "OR (area_name LIKE '%" + key2+key3 + "%') OR (area_of_use LIKE '%" + key2+key3 + "%') ";
				result = Query.select(sentence,connect.getConnection());				
			}
			
			if (bus==0){
				try {
					while(result.next()) {
						Object[]data = new Object[5];
						data[0] = String.valueOf(result.getInt("coord_ref_sys_code"));
						data[1] = result.getString("coord_ref_sys_name");
						crs_kind = result.getString("coord_ref_sys_kind");
						data[2] = crs_kind;
						projection_conv_code = result.getInt("projection_conv_code");
						setProjectionCode(projection_conv_code);
						
						int area_of_use_code = Integer.parseInt(result.getString("area_of_use_code"));
										
						String sentence = "SELECT area_name, area_of_use FROM epsg_area " +
										"WHERE area_code = "+ area_of_use_code ;
						
						result2 = Query.select(sentence,connect.getConnection());
						while(result2.next()){
							data[3] = result2.getString("area_name");
							data[4] = result2.getString("area_of_use");					
						}
						if (data[0]!=null /*&& validCRS(projection_conv_code)*/ && valid(crs_kind)){
							dtm.addRow(data);
						}
					}
				}
				
				catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			else if (bus==1){
				try {
					while(result.next()) {
						Object[]data = new Object[5];
						data[3] = result.getString("area_name");
						data[4] = result.getString("area_of_use");	
											
						int area_of_use_code = Integer.parseInt(result.getString("area_code"));
										
						String sentence = "SELECT coord_ref_sys_code, coord_ref_sys_name, " +
										"coord_ref_sys_kind, source_geogcrs_code, projection_conv_code " +
										"FROM epsg_coordinatereferencesystem " +
										"WHERE area_of_use_code = "+ area_of_use_code ;
						
						result2 = Query.select(sentence,connect.getConnection());
						while(result2.next()){
							data[0] = String.valueOf(result2.getInt("coord_ref_sys_code"));
							data[1] = result2.getString("coord_ref_sys_name");
							data[2] = result2.getString("coord_ref_sys_kind");
							crs_kind = (String)data[2];
							projection_conv_code = result2.getInt("projection_conv_code");
							setProjectionCode(projection_conv_code);
						}
						/*
						 * Buscaremos solo aquellos CRS cuyas proyecciones esten entre las
						 * 16 soportadas por proj4 para ello creamos un metodo para validar
						 * si esta entre estas proyecciones
						 */						
						if (data[0]!=null /*&& validCRS(projection_conv_code)*/ && valid(crs_kind)){
							dtm.addRow(data);
						}	
						if (notValid(crs_kind)){
							not_valid = true;
						}
					}
				} 
				
				catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			int numr = dtm.getRowCount();			
			if (not_valid){
				JOptionPane.showMessageDialog(this, PluginServices.getText(this,"crs_not_soported"), "Warning...",
						JOptionPane.WARNING_MESSAGE);
				not_valid = false;
			}
			else if (not_numeric) {
				JOptionPane.showMessageDialog(EPSGpanel.this, 
						PluginServices.getText(this,"numeric_format"), 
						"Warning...", JOptionPane.WARNING_MESSAGE);
				searchTextField.setText("");
			}
			else if (numr == 0){
			JOptionPane.showMessageDialog(this, PluginServices.getText(this,"no_results"), "Warning...",
					JOptionPane.WARNING_MESSAGE);
			}
			else{
				this.getJTable().setRowSelectionInterval(0,0);
			}
		}		
	}	
	
	/**
	 * metodo auxiliar que hara la comprobacion de si el CRS buscado pertenecera
	 * a los no soportados por proj4
	 */
	private boolean notValid(String kind){		
		for (int i = 0; i< not_soported_crs.length; i++){
			if (kind.equals(not_soported_crs[i])){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Metodo que comprueba si el CRS buscado pertenece a algun tipo 
	 * de los que soporta la aplicacion
	 */
	private boolean valid(String kind){		
		for (int i = 0; i< soported_crs.length; i++){
			if (kind.equals(soported_crs[i])){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * metodo auxiliar que nos servira para comprobar si la proyeccion del CRS seleccionado
	 * esta entre las 16 con las que trabaja proj4
	 */
	private boolean validCRS(int projection_conv_code2) {
		if (projection_conv_code2 == 0) return true;
		String sentence = "SELECT coord_op_method_code " +
							"FROM epsg_coordoperation " +
							"WHERE coord_op_code = " + projection_conv_code;
		ResultSet result = Query.select(sentence,connect.getConnection());
		try {
			while (result.next()){
				method_code = result.getInt("coord_op_method_code");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i< valid_method_code.length; i++){
			if (method_code == valid_method_code[i] ){
				return true;
			}
		}		
		return false;
	}

	/**
	 * Inicializa el botn de bsqueda
	 * @return
	 */
	private JButton getSearchButton() {
		if (searchButton == null) {
			searchButton = new JButton();
			searchButton.setPreferredSize(new Dimension(75,20));
			searchButton.setText(PluginServices.getText(this,"buscar"));
			searchButton.setMnemonic('S');
			searchButton.setToolTipText(PluginServices.getText(this,"buscar_por_criterio_seleccion"));
			searchButton.addActionListener(this);			
		}
		return searchButton;
	}	
	/**
	 * Inicializa el cuadro de texto en el que se introduce el dato a buscar
	 * @return
	 */
	private JTextField getSearchTextField() {
		if (searchTextField == null) {
			searchTextField = new JTextField();
			searchTextField.setPreferredSize(new Dimension(350,20));
			searchTextField.addKeyListener(this);			
		}
		return searchTextField;
	}
	/**
	 * Inicializa la tabla que se utiliza para mostrar 
	 * los resultados de la bsqueda
	 * @return
	 */
	public JTable getJTable() {
		if (jTable == null) {
			String[] columnNames= {PluginServices.getText(this,"codigo"),
					PluginServices.getText(this,"nombre"),
					PluginServices.getText(this,"tipo"),
					PluginServices.getText(this,"area"),
			PluginServices.getText(this,"descripcion")};
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
			//jTable.setPreferredSize(new Dimension(800, 100));
			sorter.setTableHeader(jTable.getTableHeader());
			jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			jTable.setCellSelectionEnabled(false);
			jTable.setRowSelectionAllowed(true);
			jTable.setColumnSelectionAllowed(false);
			jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			TableColumn column = null;
			for (int i = 0; i < columnNames.length; i++) {
			    column = jTable.getColumnModel().getColumn(i);
			    if (i == 0) {
			        column.setPreferredWidth(50); //code column is shorter			     	
			    }			    	
			    else if (i == 2) {
			    	column.setPreferredWidth(80);
			    }	
			    else if (i==4){
			    	column.setPreferredWidth(300);
			    }
			    else {
			        column.setPreferredWidth(140);
			    }
			}			
		}
		
		return jTable;
		
	}
	/**
	 * Establece el cdigo se CRS
	 * @param code
	 */
	public void setCodeCRS(int code) {
		codeCRS = code;
	}
	/**
	 * Obtiene el cdigo de CRS
	 * @return
	 */
	public int getCodeCRS() {
		return codeCRS;
	}	
	/**
	 * Inicializa el panel que contiene la tabla de resultados
	 * @return
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane(getJTable(),JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			jScrollPane.setPreferredSize(new Dimension(500,150));
			jScrollPane.setBorder(
				    BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(3,3,3,3),jScrollPane.getBorder()));
			
			jScrollPane.setViewportView(getJTable());
		}
		return jScrollPane;
	}
	/**
	 * Inicializa el botn que muestra la informacin de CRS seleccionado
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
		try {
			String txt = getWKT();			
			//ICrs crs = new CrsFactory().getCRS(epsg_code, txt);
			ICrs crs = new CrsFactory().getCRS("EPSG:"+epsg_code);
			return crs ;
		} catch (CrsException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Genera la cadena WKT una vez que escogemos el CRS de la tabla.
	 * Dicha cadena la generamos con los parmetros cogidos directamente
	 * del repositorio de la EPSG, y ms adelante volveremos a tratarla
	 * para pasarla a una cadena legible por la proj4.
	 *
	 */
	public void setWKT(){
		Epsg2wkt wkt = null;
						
		epsg_code = getCodeCRS();
		if (epsg_code != -1){		
			/*
			 * ahora que he escogido, recojo toda la informacion que me va a hacer falta
			 */
			String sentence = "SELECT source_geogcrs_code, projection_conv_code, " +
					"coord_ref_sys_kind, datum_code " +
							"FROM epsg_coordinatereferencesystem " +
							"WHERE coord_ref_sys_code = "+ epsg_code ;
			ResultSet result = Query.select(sentence,connect.getConnection());
			
			try {
				result.next();
				source_cod = result.getInt("source_geogcrs_code");
				projection_conv_code = result.getInt("projection_conv_code");
				crs_kind = result.getString("coord_ref_sys_kind");
				datum_code = result.getInt("datum_code");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			if (datum_code != 0){
				source_yn = true;
			}
			else if (source_cod != 0){
				source_yn = false;
			}
			else source_yn = true;
			
			CrsEPSG ep = new CrsEPSG(epsg_code, source_yn, source_cod, projection_conv_code, connect);
			
			if (crs_kind.equals("geographic 2D") || crs_kind.equals("geographic 3D")){
				wkt = new Epsg2wkt(ep , "geog");			
			}
			else if (crs_kind.equals("projected")){
				wkt = new Epsg2wkt(ep, "proj");
			}
			else if (crs_kind.equals("compound")){
				wkt = new Epsg2wkt(ep,"comp");
			}
			else if (crs_kind.equals("geocentric")){
				wkt = new Epsg2wkt(ep,"geoc");
			}
		}
		else {
			JOptionPane.showMessageDialog(this, 
					PluginServices.getText(this,"crs_no_selected."), 
					"Warning...", JOptionPane.WARNING_MESSAGE);
			
		}
		cadWKT = wkt.getWKT();
	}
		
	public String getWKT(){
		return cadWKT;
	}
	
	public void setProjection(IProjection crs) {
		//setCrs((ICrs) crs);
	}
	
	/**
	 * Metodo para cargar en el CRS de la capa el CRS de la vista.
	 * Se utilizar a la hora de arrancar la definicin del CRS de la nueva capa
	 */	
	public void loadViewCRS(int code){
		connection();
		String sentence = "SELECT coord_ref_sys_code, coord_ref_sys_name, coord_ref_sys_kind, area_of_use_code, " +
						  "source_geogcrs_code, projection_conv_code  " +
						  "FROM epsg_coordinatereferencesystem " +	                              
				        "WHERE coord_ref_sys_code = " + code;
		ResultSet result = Query.select(sentence,connect.getConnection());
		
		try {
			result.next();
			Object[]data = new Object[5];
			data[0] = String.valueOf(result.getInt("coord_ref_sys_code"));
			data[1] = result.getString("coord_ref_sys_name");
			crs_kind = result.getString("coord_ref_sys_kind");
			data[2] = crs_kind;
			projection_conv_code = result.getInt("projection_conv_code");
			
			int area_of_use_code = Integer.parseInt(result.getString("area_of_use_code"));
							
			sentence = "SELECT area_name, area_of_use FROM epsg_area " +
							"WHERE area_code = "+ area_of_use_code ;
			
			ResultSet result2 = Query.select(sentence,connect.getConnection());
			result2.next();
			data[3] = result2.getString("area_name");
			data[4] = result2.getString("area_of_use");					
			
			dtm.addRow(data);					
		}
		
		catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	/**
	 * Controla si se pulsa intro en el cuadro de texto,
	 * en ese caso se interpreta como si hubiese pulsado el botn 'Buscar'
	 */
	public void keyPressed(KeyEvent e) {	
		if (e.getSource() == this.getSearchTextField()) {
			if (e.getKeyCode() == 10) {
				searchButton();
			}			
		}		
	}

	public void keyReleased(KeyEvent e) {
		
	}

	public void keyTyped(KeyEvent e) {
		
	}

	/**
	 * Maneja los eventos de los botones y los radioButtons del panel
	 * de la EPSG.
	 */
	public void actionPerformed(ActionEvent e) {
		/*Si el objeto que genera el evento es el RadioButton 'Codigo'-->
		resetea el texto de bsqueda*/
		if (e.getSource() == this.getCodeRadioButton()) {
			searchTextField.setText("");
		}
		/*Si el objeto que genera el evento es el RadioButton 'Nombre'-->
		resetea el texto de bsqueda*/
		if (e.getSource() == this.getNameRadioButton()) {
			searchTextField.setText("");
		}
		/*Si el objeto que genera el evento es el RadioButton 'Area'-->
		resetea el texto de bsqueda*/
		if (e.getSource() == this.getAreaRadioButton()) {
			searchTextField.setText("");
		}
		/*Si el objeto que genera el evento es el JButton 'Buscar'-->
		Comprueba que no est vacio*/
		if (e.getSource() == this.getSearchButton()) {
			searchTextField.setBackground(Color.white);
			if (searchTextField.getText().equals("")) {
				searchTextField.setBackground(new Color(255,204,204));
				JOptionPane.showMessageDialog(EPSGpanel.this, 
						PluginServices.getText(this,"fill_name"), 
						"Warning...", JOptionPane.WARNING_MESSAGE);
			}
			else {
				searchButton();
			}
		}
		/*Si el objeto que genera el evento es el JButton 'InfoCrs'
		se muestra la informacin ralicionada con el Crs seleccionado en la tabla*/
		if (e.getSource() == this.getInfoCrs()) {
			InfoCRSPanel info = new InfoCRSPanel("EPSG", getCodeCRS());
			PluginServices.getMDIManager().addWindow(info);
		}
	}
	/**
	 * Agrupa los botones de opcin
	 * @return
	 */
	public ButtonGroup getOptGroup() {
		if (optGroup==null){
			optGroup=new ButtonGroup();
			optGroup.add(getCodeRadioButton());
			optGroup.add(getNameRadioButton());
			optGroup.add(getAreaRadioButton());
		}
		return optGroup;
	}
	
	public void setProjectionCode(int projCode) {
		String sentence = "SELECT coord_op_method_code " +
			"FROM epsg_coordoperation " +
			"WHERE coord_op_code = " + projCode;
		ResultSet result = Query.select(sentence,connect.getConnection());
		try {
			while (result.next()){
				projectionCode = result.getInt("coord_op_method_code");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int getProjectionCode(String code) {
		int cod= Integer.parseInt(code);
		
		String sentence = "SELECT projection_conv_code  " +
						  "FROM epsg_coordinatereferencesystem " +	                              
				        "WHERE coord_ref_sys_code = " + code;
		
		ResultSet result = Query.select(sentence,connect.getConnection());
		int projCode = 0;
		try {
			while (result.next()){
				projCode = result.getInt("projection_conv_code");
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		sentence = "SELECT coord_op_method_code " +
					"FROM epsg_coordoperation " +
					"WHERE coord_op_code = " + projCode;
		
		result = Query.select(sentence,connect.getConnection());
		try {
			while (result.next()){
				projectionCode = result.getInt("coord_op_method_code");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return projectionCode;
	}
}