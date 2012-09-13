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

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.gvsig.crs.CrsException;
import org.gvsig.crs.CrsFactory;
import org.gvsig.crs.ICrs;
import org.gvsig.crs.gui.panels.wizard.MainPanel;
import org.gvsig.crs.ogr.Iau2wkt;
import org.gvsig.crs.persistence.CrsData;
import org.gvsig.crs.persistence.RecentCRSsPersistence;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.gui.TableSorter;

import es.idr.teledeteccion.connection.EpsgConnection;
import es.idr.teledeteccion.connection.Query;

/**
 * Crea la interfaz de definicin de un nuevo crs por el usuario
 * @author Luisa Marina Fernandez Ruiz (luisam.fernandez@uclm.es)
 * @author Jose Luis Gomez Martinez (joseluis.gomez@uclm.es)
 *
 */
public class NewCRSPanel extends JPanel implements ActionListener,KeyListener{
	private static final long serialVersionUID = 1L;
	
	private JRadioButton codeRadioButton = null;
	private JRadioButton nameRadioButton = null;	
	private JLabel lblCriterio=null;
	private JButton searchButton = null;
	private JTextField searchTextField = null;
	private JButton infoCrs;
	private JButton btnNuevo;
	private JButton btnEditar;
	private JButton btnEliminar;
	private JScrollPane jScrollPane=null;   
	private JTable jTable;
	public TableSorter sorter = null;
	public DefaultTableModel dtm = null;
	
	public String key;
	public EpsgConnection connect = null;
	public int selectedRowTable = -1;	
	String cadWkt = "";
	private int codeCRS = -1;
	private ICrs currentCrs;
	
	public NewCRSPanel(){}
	
	public NewCRSPanel(ICrs crs) {
		super();
		currentCrs = crs;
		initialize();
		//fijar los listener de todos los componentes
		setListener();
		habilitarJbuttons(false);
	}
	/**
	 * Inicializa this
	 *
	 */
	private void initialize(){
		this.setLayout(new BorderLayout());
		this.setBorder(new EmptyBorder(1,1,1,1));
		//Este panel contiene los componentes de radio button
		JPanel radio=new JPanel();
		radio.setLayout(new GridLayout(1,4,10,0));
		radio.add(getLblCriterio());
		radio.add(getCodeRadioButton());
		radio.add(getNameRadioButton());
		//Agrupar los radioButtons
		agruparRadioButtons();
		//Este panel contiene los componentes relacionados con la bsqueda
		JPanel busqueda=new JPanel();
		busqueda.setLayout(new FlowLayout(FlowLayout.LEFT,10,1));
		busqueda.add(getSearchButton());
		busqueda.add(getSearchTextField());
		//busqueda.setBackground(Color.red);
		JPanel pNorth=new JPanel();
		pNorth.setLayout(new GridLayout(2,1));
		pNorth.add(radio);
		pNorth.add(busqueda);
		this.add(pNorth,BorderLayout.NORTH);
		//agregar los botones de la tabla
		JPanel pInSouth=new JPanel();
		pInSouth.setLayout(new FlowLayout(FlowLayout.LEFT,10,0));
		pInSouth.add(getInfoCrs());
		pInSouth.add(getBtnNuevo());
		pInSouth.add(getBtnEditar());
		pInSouth.add(getBtnEliminar());
		JPanel pCenter=new JPanel();
		pCenter.setLayout(new BorderLayout());
		pCenter.add(getJScrollPane(),BorderLayout.CENTER);
		pCenter.add(pInSouth,BorderLayout.SOUTH);
		this.add(pCenter,BorderLayout.CENTER);
				
	}
	
	public void connection(){
		connect = new EpsgConnection();
		connect.setConnectionUsr();
		
	}
	
	/**
	 * Inicializa el botn que muestra la informacin de CRS seleccionado
	 * @return
	 */
	public JButton getInfoCrs() {
		if(infoCrs == null) {
			infoCrs = new JButton();
			infoCrs.setPreferredSize(new Dimension(85,23));
			infoCrs.setText(PluginServices.getText(this,"infocrs"));			
			infoCrs.setMnemonic('I');
			infoCrs.setToolTipText(PluginServices.getText(this,"more_info"));
			
		}
		return infoCrs;
	}
	/**
	 * Inicializa el panel que contiene la tabla de resultados
	 * @return
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane(getJTable(),JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			jScrollPane.setPreferredSize(new Dimension(500,150));
			jScrollPane.setBorder(new CompoundBorder(new EmptyBorder(3,10,3,10),jScrollPane.getBorder()));
			jScrollPane.setViewportView(getJTable());
		}
		return jScrollPane;
	}
	/**
	 * Inicializa la tabla que se utiliza para mostrar 
	 * los resultados de la bsqueda
	 * @return
	 */
	public JTable getJTable() {
		if (jTable == null) {
			//TODO: Poner los titulos de las columnas correspondientes
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
			initializeTable();
		}
		
		return jTable;
		
	}
	
	public void initializeTable() {
		// Eliminar filas en cada nueva bsqueda
		int numRow = dtm.getRowCount();
		while (numRow != 0) {
			numRow = numRow - 1;
			dtm.removeRow(numRow);
		}
		
		String sentence = "SELECT usr_code, usr_wkt, usr_proj, usr_geog, usr_datum " +							 
		  "FROM USR ORDER BY usr_code ASC";
		
		connect = new EpsgConnection();
		connect.setConnectionUsr();
		ResultSet result = Query.select(sentence,connect.getConnection());	
		try {
			connect.shutdown();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Object[] data = new Object[4];
		try {
			while (result.next()){
				data[0]	= result.getString("usr_code");
				data[1] = result.getString("usr_wkt");
				String proj = result.getString("usr_proj");
				if (!proj.equals("")){
					data[1] = proj;
					data[2] = PluginServices.getText(this,"si");
				} 
				else 
				{
					data[1] = result.getString("usr_geog");
					data[2] = PluginServices.getText(this,"no");
				}
		
				data[3] = result.getString("usr_datum");
				dtm.addRow(data);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		int numr = dtm.getRowCount();
		if (numr > 0){
			this.getJTable().setRowSelectionInterval(0,0);
			//getBtnEditar().setEnabled(true);
			//getBtnEliminar().setEnabled(true);
		}
						
	}
	/**
	 * Inicializa el radioButton 'Codigo'
	 * @return jRadioButton
	 */
	public JRadioButton getCodeRadioButton() {
		if (codeRadioButton == null) {
			codeRadioButton = new JRadioButton();
			codeRadioButton.setText(PluginServices.getText(this,"por_codigo"));
			codeRadioButton.setSelected(true);
			codeRadioButton.addActionListener(this);
		}
		return codeRadioButton;
	}
	/**
	 * Inicializa el radioButton 'Name'
	 * @return jRadioButton
	 */
	public JRadioButton getNameRadioButton() {
		if (nameRadioButton == null) {
			nameRadioButton = new JRadioButton();
			nameRadioButton.setText(PluginServices.getText(this,"por_nombre"));
			nameRadioButton.addActionListener(this);
		}
		return nameRadioButton;
	}
	
	/**
	 * Agrupa los radioButtons
	 */
	private void agruparRadioButtons(){
		ButtonGroup group= new ButtonGroup();
		group.add(getCodeRadioButton());
		group.add(getNameRadioButton());		
	}
	/**
	 * Inicializa el label 'Criterio de Bsqueda'
	 * @return jLabel
	 */
	public JLabel getLblCriterio() {
		lblCriterio = new JLabel();
		lblCriterio.setText(PluginServices.getText(this, "criterio_busqueda")+":");
		return lblCriterio;
	}
	/**
	 * Inicializa el botn 'Buscar'
	 * @return jButton
	 */
	public JButton getSearchButton() {
		if (searchButton == null) {
			searchButton = new JButton();
			searchButton.setPreferredSize(new Dimension(75,20));
			searchButton.setText(PluginServices.getText(this,"buscar"));
			searchButton.setMnemonic('S');
			searchButton.setToolTipText(PluginServices.getText(this,"buscar_por_criterio_seleccion"));
						
		}
		return searchButton;
	}
	/**
	 * Inicializa el TextField 
	 * en el que se incluye el texto a buscar
	 * @return jTextField
	 */
	public JTextField getSearchTextField() {
		if (searchTextField == null) {
			searchTextField = new JTextField();
			searchTextField.setPreferredSize(new Dimension(340,20));
				
		}
		return searchTextField;
	}
	/**
	 * Manejador de eventos
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(getCodeRadioButton())){
			getSearchTextField().setText("");
		}else if (e.getSource().equals(getNameRadioButton())){
			getSearchTextField().setText("");
		}else if (e.getSource().equals(getBtnEditar())){
			//editar la fila seleccionada de la tabla (si hay)
			ICrs crs = null;
			try {
				crs = new CrsFactory().getCRS("USR:"+getCodeCRS());
			} catch (CrsException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}			
			MainPanel wizard = new MainPanel(crs);
			wizard.setEditing(true);
			wizard.setEditingPanel();
			PluginServices.getMDIManager().addWindow(wizard);
			getNewCrs(wizard.getNewCrsCode());
			
			/**
			 * Actualizamos recientes para que coja los cambios del crs
			 */
			/*
    		 * Actualizar recientes...
    		 */
    		String authority = "USR";
    		String name = "";
    		if (wizard.getPSistCoord().getTxtNombreProy().getText().equals("")) {
    			name = wizard.getPDatum().getTxtNombreCrs().getText();
    		} else {
    			name = wizard.getPSistCoord().getTxtNombreProy().getText();
    		}
    		int code = getCodeCRS();
    		CrsData crsData = new CrsData(authority,code,name);
    		RecentCRSsPersistence persistence = new RecentCRSsPersistence();
    		persistence.addCrsData(crsData);
    		
		}else if (e.getSource().equals(getBtnEliminar())){
			//eliminar la fila seleccionada de la tabla
			int i = JOptionPane.showConfirmDialog(NewCRSPanel.this, PluginServices.getText(this, "deleteUsr"));
			if (i==0) {
				connect = new EpsgConnection();
				connect.setConnectionUsr();
				String sentence = "DELETE FROM USR WHERE usr_code =" + getCodeCRS();
				ResultSet result = Query.select(sentence,connect.getConnection());	
				try {
					connect.shutdown();
				} catch (SQLException arg0) {
					// TODO Auto-generated catch block
					arg0.printStackTrace();
				}
				dtm.removeRow(getJTable().getSelectedRow());
			}
			//searchButton();
		}else if (e.getSource().equals(getBtnNuevo())){
			//mostrar el asistente de nuevo crs
		
			MainPanel wizard = new MainPanel(currentCrs);
			if ((wizard.getPCard().getSelectedIndex() == 0) && wizard.getPCrsUsr().getRbCrsExistente().isSelected() ){
				ICrs crs = wizard.getPCrsUsr().getCrs();
				if (crs != null){
					wizard.fillData(crs);
				}
				else wizard.fillData(wizard.getCrs());
			}
			else if ((wizard.getPCard().getSelectedIndex() == 0)  && wizard.getPCrsUsr().getRbNuevoCrs().isSelected() ){
				wizard.cleanData();
			}
			PluginServices.getMDIManager().addWindow(wizard);
			getNewCrs(wizard.getNewCrsCode());
			
		}else if (e.getSource().equals(getSearchButton())){
			searchButton();			
		}		
		/*Si el objeto que genera el evento es el JButton 'InfoCrs'
		se muestra la informacin ralicionada con el Crs seleccionado en la tabla*/
		else if (e.getSource().equals(getInfoCrs())) {
			InfoCRSPanel info = new InfoCRSPanel("USR", getCodeCRS());
			PluginServices.getMDIManager().addWindow(info);
		}
	}
	public void keyPressed(KeyEvent e) {
		//Si se pulsa intro-->Buscar
		if (e.getSource() == this.getSearchTextField()) {
			if (e.getKeyCode() == 10) {
				searchTextField.setBackground(Color.white);
				if (searchTextField.getText().equals("")) {
					initializeTable();
				}
				else {
					searchButton();
				}
			}			
		}		
	}
	public void keyReleased(KeyEvent e) {
		
	}
	public void keyTyped(KeyEvent e) {
		
	}
	/**
	 * Inicializa el botn 'Editar'  una fila de la tabla
	 * @return jButton
	 */
	public JButton getBtnEditar() {
		if (btnEditar==null){
			btnEditar=new JButton();
			btnEditar.setText(PluginServices.getText(this,"editar"));
		}
		return btnEditar;
	}
	/**
	 * Inicializa el botn 'Eliminar' una fila de la tabla
	 * @return
	 */
	public JButton getBtnEliminar() {
		if (btnEliminar==null){
			btnEliminar=new JButton();
			btnEliminar.setText(PluginServices.getText(this,"eliminar"));
		}
		return btnEliminar;
	}
	/**
	 * Inicializa el botn 'Nuevo' para la creacin de un nuevo Crs
	 * @return
	 */
	public JButton getBtnNuevo() {
		if (btnNuevo==null){
			btnNuevo=new JButton();
			btnNuevo.setText(PluginServices.getText(this,"nuevo"));
		}
		return btnNuevo;
	}
	/**
	 * Establece los listener de todos los componentes
	 */
	private void setListener(){
		getBtnEditar().addActionListener(this);
		getBtnEliminar().addActionListener(this);
		getBtnNuevo().addActionListener(this);
		getInfoCrs().addActionListener(this);
		getCodeRadioButton().addActionListener(this);
		getNameRadioButton().addActionListener(this);
		getSearchButton().addActionListener(this);
		getSearchTextField().addKeyListener(this);
		
	}
	private void habilitarJbuttons(boolean b){
		getInfoCrs().setEnabled(b);
		getBtnEditar().setEnabled(b);
		getBtnEliminar().setEnabled(b);
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
		searchTextField.setBackground(Color.white);
				
		if (codeRadioButton.isSelected() && (searchTextField.getText().length()!=searchTextField.getText().replaceAll("[^0-9]", "").length())){
			JOptionPane.showMessageDialog(NewCRSPanel.this, 
					PluginServices.getText(this,"numeric_format"), 
					"Warning...", JOptionPane.WARNING_MESSAGE);
			searchTextField.setText("");
			return;
		}
		
        //Eliminar filas en cada nueva bsqueda
		int numRow = dtm.getRowCount();
		while (numRow != 0) {
			numRow = numRow - 1;
			dtm.removeRow(numRow);
		}
//			Dependiendo de la opcion se realizada una busqueda
		ResultSet result = null;
		
		if (searchTextField.getText().equals("")) {
			String sentence = "SELECT usr_code, usr_wkt, usr_proj, usr_geog, usr_datum " +							 
			  "FROM USR ORDER BY usr_code ASC";
			connect = new EpsgConnection();
			connect.setConnectionUsr();
			result = Query.select(sentence,connect.getConnection());	
			try {
				connect.shutdown();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Object[] data = new Object[4];
			try {
				while (result.next()){
					data[0]	= result.getString("usr_code");
					data[1] = result.getString("usr_wkt");
					String proj = result.getString("usr_proj");
					if (!proj.equals("")){
						data[1] = proj;
						data[2] = PluginServices.getText(this,"si");
					} 
					else 
					{
						data[1] = result.getString("usr_geog");
						data[2] = PluginServices.getText(this,"no");
					}
					
					data[3] = result.getString("usr_datum");
					dtm.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		
		else if (codeRadioButton.isSelected()) {
				
			key = searchTextField.getText();
			int code = Integer.parseInt(key);
			String sentence = "SELECT usr_code, usr_wkt, usr_proj, usr_geog, usr_datum " +							 
							  "FROM USR " +	                              
                              "WHERE usr_code = " + code;
			connect = new EpsgConnection();
			connect.setConnectionUsr();
			result = Query.select(sentence,connect.getConnection());	
			try {
				connect.shutdown();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Object[] data = new Object[4];
			try {
				while (result.next()){
					data[0]	= result.getString("usr_code");
					data[1] = result.getString("usr_wkt");
					String proj = result.getString("usr_proj");
					if (!proj.equals("")){
						data[1] = proj;
						data[2] = PluginServices.getText(this,"si");
					} 
					else 
					{
						data[1] = result.getString("usr_geog");
						data[2] = PluginServices.getText(this,"no");
					}
					
					data[3] = result.getString("usr_datum");
					dtm.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		else if (nameRadioButton.isSelected()) {
			key = searchTextField.getText();
			String key2 = key.substring(0,1);
			String key3 = key.substring(1,key.length());
			key2 = key2.toUpperCase();
			
			String sentence = "SELECT usr_code, usr_wkt, usr_proj, usr_geog, usr_datum " +							 
				"FROM USR " +	                              
				"WHERE (usr_proj LIKE '%" + key + "%') OR (usr_proj LIKE '%"+ 
				key.toUpperCase() +"%') " +
				"OR (usr_proj LIKE '%" + key2+key3 +"%') OR " +
						"(usr_geog LIKE '%" + key + "%') OR (usr_geog LIKE '%"+ 
				key.toUpperCase() +"%') " +
				"OR (usr_geog LIKE '%" + key2+key3 +"%')";
			
			connect = new EpsgConnection();
			connect.setConnectionUsr();
			result = Query.select(sentence,connect.getConnection());
			try {
				connect.shutdown();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
							
			Object[] data = new Object[4];
			try {
				while (result.next()){
					data[0]	= result.getString("usr_code");
					data[1] = result.getString("usr_wkt");
					String proj = result.getString("usr_proj");
					if (!proj.equals("")){
						data[1] = proj;
						data[2] = PluginServices.getText(this,"si");
					} 
					else 
					{
						data[1] = result.getString("usr_geog");
						data[2] = PluginServices.getText(this,"no");
					}
					
					data[3] = result.getString("usr_datum");
					dtm.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
					
		int numr = dtm.getRowCount();
		if (numr == 0){
			JOptionPane.showMessageDialog(this, PluginServices.getText(this,"no_results"), "Warning...",
					JOptionPane.WARNING_MESSAGE);
		}
		else {
			this.getJTable().setRowSelectionInterval(0,0);				
		}					
	}
	
	public void setCodeCRS(int code) {
		codeCRS = code;
	}
	
	public int getCodeCRS() {
		return codeCRS;
	}	
	
	/**
	 * Consigue la cadena wkt del CRS seleccionado, y genera la cadena que ms
	 * tarde volver a ser tratada para la consecucin de una cadena wkt
	 * legible por la proj4.
	 *
	 */
	public void setWKT(){
		int code = getCodeCRS();
		String sentence = "SELECT usr_wkt " +							 
						  "FROM USR " +	                              
                          "WHERE usr_code = " + code;
		
		
		connect = new EpsgConnection();
		connect.setConnectionUsr();
		ResultSet result = Query.select(sentence,connect.getConnection());
		try {
			connect.shutdown();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			result.next();			
			cadWkt = result.getString("usr_wkt");			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}		
		cadWkt = cadWkt.substring(0, cadWkt.length()-1) + ", AUTHORITY[\"USR\","+ getCodeCRS()+"]]";
		
	}
	
	public String getWKT(){
		return cadWkt;
	}
	
	public ICrs getProjection() {
		try {
			ICrs crs = new CrsFactory().getCRS("USR:"+getCodeCRS());
			return crs ;
		} catch (CrsException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void getNewCrs(int code) {
		if (code != -1) {
			//		Eliminar filas en cada nueva bsqueda
			int numRow = dtm.getRowCount();
			while (numRow != 0) {
				numRow = numRow - 1;
				dtm.removeRow(numRow);
			}
			String sentence = "SELECT usr_code, usr_wkt, usr_proj, usr_geog, usr_datum " +							 
								"FROM USR " +	                              
								"WHERE usr_code = " + code;
			connect = new EpsgConnection();
			connect.setConnectionUsr();
			ResultSet result = Query.select(sentence,connect.getConnection());	
			try {
				connect.shutdown();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Object[] data = new Object[4];
			try {
				while (result.next()){
					data[0]	= result.getString("usr_code");
					data[1] = result.getString("usr_wkt");
					String proj = result.getString("usr_proj");
					if (!proj.equals("")){
						data[1] = proj;
						data[2] = PluginServices.getText(this,"si");
					} 
					else 
					{
						data[1] = result.getString("usr_geog");
						data[2] = PluginServices.getText(this,"no");
					}
					
					data[3] = result.getString("usr_datum");
					dtm.addRow(data);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			int numr = dtm.getRowCount();
			if (numr == 0){
				searchButton();
			}
			else {
				this.getJTable().setRowSelectionInterval(0,0);				
			}			
		}	
	}
	
	public boolean isInBD (int code) {
		String sentence = "SELECT usr_code " +							 
						"FROM USR " +	                              
						"WHERE usr_code ="+code;		
	
		connect = new EpsgConnection();
		connect.setConnectionUsr();
		ResultSet result = Query.select(sentence,connect.getConnection());
		try {
			connect.shutdown();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		try {
			if (result.next()) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
