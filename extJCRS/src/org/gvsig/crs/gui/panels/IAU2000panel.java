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
import org.gvsig.crs.ogr.Iau2wkt;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.gui.TableSorter;

import es.idr.teledeteccion.connection.EpsgConnection;
import es.idr.teledeteccion.connection.Query;

/**
 * Clase que genera el panel para la bsqueda de CRS del repositorio
 * de la IAU2000
 * 
 * @author Jos Luis Gmez Martnez (jolugomar@gmail.com)
 * @author Luisa Marina Fernndez (luisam.fernandez@uclm.es)
 *
 */
public class IAU2000panel extends JPanel implements KeyListener, ActionListener {

	private static final long serialVersionUID = 1L;
	public JPanel IAU2000panel = null;
	
	private JRadioButton codeRadioButton = null;
	private JRadioButton nameRadioButton = null;	
	private JPanel groupRadioButton = null;
	private JButton infoCrs=null;
	
	JLabel jLabel = null;
	private JLabel lblCriterio=null;
	JTextField crsTextFld = null;	
	
	String cadWKT = "";
	
	int transf = 0;	
	boolean source_yn = false;
	int source_cod = 0;
	int method_code = 0;
	int datum_code = 0;
	int projection_conv_code = 0;
	public String crs_kind = null;

	public TableSorter sorter = null;
	
	public EpsgConnection connect = null;
	

	private JButton searchButton = null;
	private JTextField searchTextField = null;
	public JTable jTable = null;
	private JScrollPane jScrollPane = null;
	public DefaultTableModel dtm = null;
	
	public String key;
	public int selectedRowTable = -1;	
	private int codeCRS = -1;	
	
	public IAU2000panel() {		
		initialize();
	}
	/*
	 * Establece las propiedades y los componentes del panel de la IAU2000
	 */
	private void initialize(){
		this.setLayout(new BorderLayout());
		JPanel pNorth=new JPanel();
		JPanel pInNorth=new JPanel();
		JPanel pSouth=new JPanel();
		//Agregar las opciones de bsqueda
		pInNorth.setLayout(new FlowLayout(FlowLayout.CENTER,10,1));
		pInNorth.add(getSearchButton());
		pInNorth.add(getSearchTextField());
		
		pNorth.setLayout(new GridLayout(0,1));
		pNorth.add(getGroupRadioButton());
		pNorth.add(pInNorth);
		
		pSouth.setLayout(new FlowLayout(FlowLayout.RIGHT,5,5));
		pSouth.add(getInfoCrs());
		//Agregar estos elementos al panel principal
		this.add(pNorth,BorderLayout.NORTH);
		this.add(getJScrollPane(),BorderLayout.CENTER);
		this.add(pSouth,BorderLayout.SOUTH);
	}
	
	public void connection(){
		connect = new EpsgConnection();
		connect.setConnectionIAU2000();
		
	}
	/**
	 * Inicializa el Radio Button 'Cdigo' 
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
	 * Inicializa el Radio Button  'Nombre'
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
	 * Agrega todos los Radio Buttons a un panel
	 * @return
	 */
	private JPanel getGroupRadioButton() {
		if (groupRadioButton == null) {
			groupRadioButton = new JPanel();
			groupRadioButton.setLayout(new GridLayout(1,0));
			groupRadioButton.setPreferredSize(new Dimension(500,30));
			groupRadioButton.add(getLblCriterio());
			groupRadioButton.add(getCodeRadioButton());
			groupRadioButton.add(getNameRadioButton());	
			//Agrupar los Radio Buttons
			ButtonGroup group=new ButtonGroup();
			group.add(getCodeRadioButton());
			group.add(getNameRadioButton());
		}
		return groupRadioButton;
	}
	/**
	 * Inicializa el Label que nombra el Criterio de Bsqueda
	 * @return
	 */
	private JLabel getLblCriterio(){
		if(lblCriterio==null){
			lblCriterio = new JLabel();
			lblCriterio.setPreferredSize(new Dimension(100, 20));
			lblCriterio.setText(PluginServices.getText(this, "criterio_busqueda"));
		}
		return lblCriterio;
	}
	
	/**
	 * Mtodo que controla la bsqueda de CRS del repositorio de IAU2000.
	 * Tambien maneja los errores en caso de que los parmetros de bsqueda
	 * sean errneos, o que no se encuentren resultados.
	 *
	 */
	private void searchButton() {		
		searchTextField.setBackground(Color.white);
		boolean not_numeric = false;
		
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
								  			
			if (codeRadioButton.isSelected() && !not_numeric) {
					
				key = searchTextField.getText();
				int code = Integer.parseInt(key);
				String sentence = "SELECT iau_code, iau_wkt, iau_proj, iau_geog, iau_datum " +							 
								  "FROM IAU2000 " +	                              
	                              "WHERE iau_code = " + code;
								
				result = Query.select(sentence,connect.getConnection());	
				
				
				Object[] data = new Object[4];
				try {
					while (result.next()){
						data[0]	= result.getString("iau_code");
						data[1] = result.getString("iau_wkt");
						String proj = result.getString("iau_proj");
						if (!proj.equals("")){
							data[1] = proj;
							data[2] = PluginServices.getText(this,"si");
						} 
						else 
						{
							data[1] = result.getString("iau_geog");
							data[2] = PluginServices.getText(this,"no");
						}
						
						data[3] = result.getString("iau_datum");
						dtm.addRow(data);
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			else if (nameRadioButton.isSelected()) {
				key = searchTextField.getText();
				key = key.toLowerCase();
				String key2 = key.substring(0,1);
				String key3 = key.substring(1,key.length());
				key2 = key2.toUpperCase();
				
				String sentence = "SELECT iau_code, iau_wkt, iau_proj, iau_geog, iau_datum " +							 
					"FROM IAU2000 " +	                              
					"WHERE (iau_proj LIKE '%" + key + "%') OR (iau_proj LIKE '%"+ 
					key.toUpperCase() +"%') " +
					"OR (iau_proj LIKE '%" + key2+key3 +"%') OR " +
							"(iau_geog LIKE '%" + key + "%') OR (iau_geog LIKE '%"+ 
					key.toUpperCase() +"%') " +
					"OR (iau_geog LIKE '%" + key2+key3 +"%')";
								
				result = Query.select(sentence,connect.getConnection());	
								
				Object[] data = new Object[4];
				try {
					while (result.next()){
						data[0]	= result.getString("iau_code");
						data[1] = result.getString("iau_wkt");
						String proj = result.getString("iau_proj");
						if (!proj.equals("")){
							data[1] = proj;
							data[2] = PluginServices.getText(this,"si");
						} 
						else 
						{
							data[1] = result.getString("iau_geog");
							data[2] = PluginServices.getText(this,"no");
						}
						
						data[3] = result.getString("iau_datum");
						dtm.addRow(data);
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
						
			int numr = dtm.getRowCount();
			if (not_numeric) {
				JOptionPane.showMessageDialog(IAU2000panel.this, 
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
	private JTextField getSearchTextField() {
		if (searchTextField == null) {
			searchTextField = new JTextField();
			searchTextField.setPreferredSize(new Dimension(300,20));
			searchTextField.addKeyListener(this);
		}		
		return searchTextField;
	}
	
	public JTable getJTable() {
		if (jTable == null) {
			String[] columnNames= {PluginServices.getText(this,"codigo"),
					PluginServices.getText(this,"nombre"),
					PluginServices.getText(this,"projected"),
					PluginServices.getText(this,"datum")};
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
			        column.setPreferredWidth(80); //code column is shorter			     	
			    }else if (i ==2) {
			    	column.setPreferredWidth(50);
			    } else {
			    	column.setPreferredWidth(175);
			    }
			    
			}			
	}
		return jTable;
		
	}
	
	public void setCodeCRS(int code) {
		codeCRS = code;
	}
	
	public int getCodeCRS() {
		return codeCRS;
	}	
	
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setPreferredSize(new Dimension(500,150));
			/*jScrollPane.setBorder(
				    BorderFactory.createCompoundBorder(
					BorderFactory.createCompoundBorder(
							BorderFactory.createTitledBorder(PluginServices.getText(this,"IAU2000")),
							BorderFactory.createEmptyBorder(5,5,5,5)),
							jScrollPane.getBorder()));*/
			jScrollPane.setBorder(
				    BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(3,3,3,3),jScrollPane.getBorder()));
			
			jScrollPane.setViewportView(getJTable());
		}
		return jScrollPane;
	}
	
	public ICrs getProjection() {
		try {
			String txt = getWKT();			
			//ICrs crs = new CrsFactory().getCRS("IAU2000:"+getCodeCRS(), txt); 
			ICrs crs = new CrsFactory().getCRS("IAU2000:"+getCodeCRS());
			return crs ;
		} catch (CrsException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Consigue la cadena wkt del CRS seleccionado, y genera la cadena que ms
	 * tarde volver a ser tratada para la consecucin de una cadena wkt
	 * legible por la proj4.
	 *
	 */
	public void setWKT(){
		int code = getCodeCRS();
		String sentence = "SELECT iau_wkt " +							 
						  "FROM IAU2000 " +	                              
                          "WHERE iau_code = " + code;
		
		
		ResultSet result = Query.select(sentence,connect.getConnection());	
	
		try {
			result.next();			
			cadWKT = result.getString("iau_wkt");			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}		
		cadWKT = cadWKT.substring(0, cadWKT.length()-1) + ", AUTHORITY[\"IAU2000\","+ getCodeCRS()+"]]";
		if (cadWKT.charAt(0) == 'P'){
			Iau2wkt wk = new Iau2wkt(cadWKT);
			cadWKT = wk.getWkt();
		}
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
		String sentence = "SELECT iau_code, iau_wkt, iau_proj, iau_geog, iau_datum " +							 
						  "FROM IAU2000 " +	                              
                          "WHERE iau_code = " + code;
				
		ResultSet result = Query.select(sentence,connect.getConnection());	
				
		Object[] data = new Object[4];
		try {
			while (result.next()){
				data[0]	= result.getString("iau_code");
				data[1] = result.getString("iau_wkt");
				String proj = result.getString("iau_proj");
				if (!proj.equals("")){
					data[1] = proj;
					data[2] = PluginServices.getText(this,"si");
				} 
				else 
				{
					data[1] = result.getString("iau_geog");
					data[2] = PluginServices.getText(this,"no");
				}
				
				data[3] = result.getString("iau_datum");
				dtm.addRow(data);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	public void keyPressed(KeyEvent e) {
		if (e.getSource() == this.getSearchTextField()){
			if (e.getKeyCode() == 10) {
				searchTextField.setBackground(Color.white);
				if (searchTextField.getText().equals("")) {
					searchTextField.setBackground(new Color(255,204,204));
					JOptionPane.showMessageDialog(IAU2000panel.this, 
							PluginServices.getText(this,"fill_name"), 
							"Warning...", JOptionPane.WARNING_MESSAGE);
				}
				else {
					searchButton();
				}
			}			
		}		
	}

	public void keyReleased(KeyEvent arg0) {
	
	}

	public void keyTyped(KeyEvent arg0) {
	
	}

	/**
	 * Maneja los eventos de los botones y los radioButtons del panel
	 * del repositorio IAU2000.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.getSearchButton()){
			searchTextField.setBackground(Color.white);
			if (searchTextField.getText().equals("")) {
				searchTextField.setBackground(new Color(255,204,204));
				JOptionPane.showMessageDialog(IAU2000panel.this, 
						PluginServices.getText(this,"fill_name"), 
						"Warning...", JOptionPane.WARNING_MESSAGE);
			}
			else {
				searchButton();
			}
		}
		if (e.getSource() == this.getCodeRadioButton()) {
			searchTextField.setText("");
		}
		
		if (e.getSource() == this.getNameRadioButton()) {
			searchTextField.setText("");
		
		}	
		
		/*Si el objeto que genera el evento es el JButton 'InfoCrs'
		se muestra la informacin ralicionada con el Crs seleccionado en la tabla*/
		if (e.getSource() == this.getInfoCrs()) {
			InfoCRSPanel info = new InfoCRSPanel("IAU2000", getCodeCRS());
			PluginServices.getMDIManager().addWindow(info);
		}
	}

}
