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

package org.gvsig.crs.gui.panels.wizard;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.units.ConversionException;
import javax.units.Unit;

import org.geotools.referencing.crs.DefaultProjectedCRS;
import org.gvsig.crs.CrsException;
import org.gvsig.crs.CrsFactory;
import org.gvsig.crs.ICrs;
import org.gvsig.crs.Proj4;
import org.gvsig.crs.gui.dialog.ImportNewCrsDialog;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.iver.andami.PluginServices;

import es.idr.teledeteccion.connection.EpsgConnection;
import es.idr.teledeteccion.connection.Query;



/**
 * Panel de Definicion del Sistema de Coordenadas
 * 
 * @author Luisa Marina Fernandez Ruiz (luisam.fernandez@uclm.es)
 * @author Jose Luis Gomez Martinez (joseluis.gomez@uclm.es)
 *
 */
public class DefSistCoordenadas extends JPanel implements ActionListener, ItemListener{
	
	private static final long serialVersionUID = 1L;
	private JPanel top;
	private JPanel proyectadoPanel;
	//private JPanel geograficoPanel;
	private JPanel cardPanel;
	private JRadioButton rbGeografico;
	private JRadioButton rbProyectado;
	private ButtonGroup coordGroup;
	
	private JButton btnImportar=null;
	private JLabel lblProyeccion;
	private JLabel lblNombreProy;
	private JTextField txtNombreProy;
	private JComboBox cbProyeccion;
	private JComboBox cbUnits;
	private JTable tableParametros;
	private JScrollPane scrollTable;
	
	private DefaultTableModel model = null;
	
	private int theigth=140;
	private int twidth=300;
	
	final static String PROYECTADOPANEL = "Proyectado";
	//final static String GEOGRAFICOPANEL = "Geografico";
	private String sourceUnit = null;
	
	private ArrayList projections = null;
	private ArrayList trueParametersNames = null;
	
	ICrs crs;
	Proj4 proj4 = null;
	private int pos;
	int divider=10000;
	
	/**
     * Small tolerance factor for rounding errors.
     */
    private static final double EPS = 1E-8;

	
	public DefSistCoordenadas() {
		super();
		BorderLayout bl=new BorderLayout();
		try {
			proj4 = new Proj4();
		} catch (CrsException e) {
			e.printStackTrace();
		}
		bl.setVgap(5);
		bl.setHgap(5);
		this.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		this.setLayout(bl);
		this.add(getTop(),BorderLayout.NORTH);
		
		//agregar cardPanel en el this
		this.add(getCardPanel(),BorderLayout.CENTER);
		
	}
	
	public JPanel getCardPanel() {		
		if (cardPanel==null) {
			cardPanel=new JPanel();
			cardPanel.setLayout(new CardLayout());
			//agregar los elementos correspondientes en el cardPanel
			cardPanel.add(PROYECTADOPANEL,getProyectadoPanel());
			//cardPanel.add(GEOGRAFICOPANEL,getGeograficoPanel());			
		}
		return cardPanel;
	}
	/**
	 * Inicializa el radio button  Geogr�fico 2D
	 * @return
	 */
	public JRadioButton getRbGeografico() {
		if (rbGeografico==null){
			rbGeografico=new JRadioButton(PluginServices.getText(this,"SistCoor_Geografico2D"));
			rbGeografico.addActionListener(this);
		}
		return rbGeografico;
	}
	/**
	 * Inicializa el radio button Proyectado
	 * @return
	 */
	public JRadioButton getRbProyectado() {
		if (rbProyectado==null){
			rbProyectado=new JRadioButton(PluginServices.getText(this,"SistCoor_Proyactado"));
			rbProyectado.addActionListener(this);
		}
		return rbProyectado;
	}
	/**
	 * Inicializa el panel que contiene las opciones 
	 * si el crs seleccionado es proyectado
	 * 
	 */
	public JPanel getProyectadoPanel() {
		if(proyectadoPanel==null){
			proyectadoPanel=new JPanel(new BorderLayout(5,5));
			proyectadoPanel.setBorder(BorderFactory.createEmptyBorder(0,3,0,3));
			//Tendra que alinearlo todo a la izquierda
			JPanel p1=new JPanel(new FlowLayout(FlowLayout.LEFT,3,5));
			p1.add(getLblNombreProy());
			p1.add(getTxtNombreProy());
			p1.add(getBtnImportar());
			JPanel p2=new JPanel(new FlowLayout(FlowLayout.RIGHT,3,5));
			p2.add(getLblProyeccion());
			p2.add(getCbProyeccion());
			JPanel pNorth=new JPanel(new GridLayout(2,1));
			pNorth.add(p1);
			pNorth.add(p2);
			proyectadoPanel.add(pNorth,BorderLayout.NORTH);
			proyectadoPanel.add(getScrollTable(),BorderLayout.CENTER);
		}
		return proyectadoPanel;
	}
	/**
	 * Inicializa el panel superior donde se define el sistema 
	 * de coordenadas
	 * 
	 */
	public JPanel getTop() {
		if(top==null){
			top=new JPanel();
			top.add(getRbGeografico());
			top.add(getRbProyectado());
			//Agrupar las opciones
			agruparRadioButtons();
			top.setBorder(new TitledBorder(PluginServices.getText(this,"SistCoor_titmarco")));
		}
		return top;
	}
	/**
	 * Agrupa los radio button
	 *
	 */
	private void agruparRadioButtons() {
		if (coordGroup==null){
				coordGroup=new ButtonGroup();
				//Agrupar los botones de opcion
				coordGroup.add(getRbProyectado());
				coordGroup.add(getRbGeografico());
				getRbProyectado().setSelected(true);
			}	
	}
	/**
	 * Inicializa el label Proyecci�n
	 * @return
	 */
	public JLabel getLblProyeccion() {
		if (lblProyeccion==null){
			lblProyeccion=new JLabel();
			lblProyeccion.setText(PluginServices.getText(this,"SistCoor_Proyeccion"));
		}
		return lblProyeccion;
	}
	/**
	 * Crea la tabla donde se definen los par�metros de la
	 * proyecci�n seleccionada en el combobox
	 * @return
	 */
	public JTable getTableParametros() {
		
		if(tableParametros==null){
			tableParametros = new JTable();
			tableParametros.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		    model = (DefaultTableModel)tableParametros.getModel();		    
		    //Crea la tabla con 7 filas
		    Object[][] data = {
					{"", "", "Metros"},
					{"", "", "Metros"},
					{"", "", "Metros"},
					{"", "", "Metros"},
					{"", "", "Metros"},
					{"", "", "Metros"},
					{"", "", "Metros"}};
		    
			String col1=PluginServices.getText(this,"SistCoor_Parametro");
			String col2=PluginServices.getText(this,"SistCoor_Valor");
			String col3=PluginServices.getText(this,"SistCoor_Unidades");
			Object[] headers = {col1, col2, col3};
		    
		     /*Agrega otra fila
			model.addRow(new Object[]{"fila","","Metros"});*/
		    //define los items del combo
		    ArrayList units = obtenerItemsUnidades();
			String[] items = new String[units.size()];
			for (int i=0;i<units.size();i++){
				items[i] = units.get(i).toString();
			}
			
			model = new DefaultTableModel(data, headers) {
				private static final long serialVersionUID = 1L;
				public boolean isCellEditable(int row, int column) {
					if (column == 0 || column == 2)
						return false;
					else return true;
				}
			 };
			 tableParametros.setModel(model);
	        //String[] items = new String[] { "Metros", "Grados", "Kilometros", "Decimetros", "Hect�metros" };
	       /* TableColumn col = tableParametros.getColumnModel().getColumn(2);
	        
	        //ComboBoxEditor editor = new ComboBoxEditor(items);
	        col.setCellEditor(new DefaultCellEditor(getCbUnits()));
	        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
	       
	        col.setCellRenderer(renderer);*/
	        
	        
	        //Define el tama�o de la tabla
			tableParametros.setPreferredScrollableViewportSize(new Dimension(twidth,theigth));
			//la posicion de las columnas es fija
			tableParametros.getTableHeader().setReorderingAllowed( false );
			//Ajustar ancho y alto de las filas y columnas
			ajustarTamanoTabla();
			fillTable(getProjection(0));			
		}

		return tableParametros;
	}
	
	/**
	 * Contiene los nombres de las distintas unidades
	 * @return
	 */
	public JComboBox getCbUnits() {
		if (cbUnits==null){
			ArrayList units = obtenerItemsUnidades();
			String[] items = new String[units.size()];
			for (int i=0;i<units.size();i++){
				items[i] = units.get(i).toString();
			}
			cbUnits=new JComboBox(items);
			cbUnits.setEditable(false);
			cbUnits.setToolTipText(PluginServices.getText(this,"SistCoor_cbToolTip"));
			sourceUnit = (String)cbUnits.getItemAt(0);
			cbUnits.addItemListener(this);
		}
		return cbUnits;
	}
	
	/**
	 * Accede al la base de datos y obtiene los items de
	 * los combobox
	 * @return
	 */
	private ArrayList obtenerItemsUnidades(){ //unidades de longitud...
		ArrayList items = new ArrayList();
				
		String sentence = "SELECT unit_of_meas_name " +
		  				"FROM epsg_unitofmeasure ";// +	                              
		  				//"WHERE unit_of_meas_type = 'length'";
		EpsgConnection connect = new EpsgConnection();
		connect.setConnectionEPSG();
		ResultSet result = Query.select(sentence,connect.getConnection());
		try {
			while (result.next()) {
				String item = result.getString("unit_of_meas_name");
				items.add(PluginServices.getText(this, item));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return items;
	}
	
	/**
	 * Accede al la base de datos y obtiene los items de
	 * los combobox
	 * @return
	 */
	private ArrayList obtenerItemsUnidadesAngular(){ //unidades angulares...
		ArrayList items = new ArrayList();
				
		String sentence = "SELECT unit_of_meas_name " +
		  				"FROM epsg_unitofmeasure " +	                              
		  				"WHERE unit_of_meas_type = 'angle'";
		EpsgConnection connect = new EpsgConnection();
		connect.setConnectionEPSG();
		ResultSet result = Query.select(sentence,connect.getConnection());
		try {
			while (result.next()) {
				String item =result.getString("unit_of_meas_name"); 
				items.add(PluginServices.getText(this, item));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return items;
	}
	
	/**
	 * Accede al la base de datos y obtiene los items de
	 * los combobox
	 * @return
	 */
	private ArrayList obtenerItemsUnidadesScale(){ //unidades angulares...
		ArrayList items = new ArrayList();
				
		String sentence = "SELECT unit_of_meas_name " +
		  				"FROM epsg_unitofmeasure " +	                              
		  				"WHERE unit_of_meas_type = 'scale'";
		EpsgConnection connect = new EpsgConnection();
		connect.setConnectionEPSG();
		ResultSet result = Query.select(sentence,connect.getConnection());
		try {
			while (result.next()) {
				String item = result.getString("unit_of_meas_name");
				items.add(PluginServices.getText(this, item));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return items;
	}

	/**
	 * Crear scrollPane y agregar la tabla en �l
	 */
	public JScrollPane getScrollTable() {
		if(scrollTable==null){
			scrollTable = new JScrollPane(getTableParametros());
			scrollTable.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		}
		return scrollTable;
	}
	/**
	 * Contiene los nombres de las distintas proyecciones
	 * @return
	 */
	public JComboBox getCbProyeccion() {
		if (cbProyeccion==null){
			ArrayList projections = obtainProjections();
			String[] items = new String[projections.size()];
			for (int i=0;i<projections.size();i++){
				items[i] = projections.get(i).toString();
			}
			cbProyeccion=new JComboBox(items);
			cbProyeccion.setEditable(false);
			cbProyeccion.setToolTipText(PluginServices.getText(this,"SistCoor_cbToolTip"));
			cbProyeccion.addItemListener(this);			
		}
		return cbProyeccion;
	}
	/**
	 * Inicializa el label que contiene el Nombre de la Proyeccin
	 * @return
	 */
	public JLabel getLblNombreProy() {
		if(lblNombreProy==null){
			lblNombreProy=new JLabel(PluginServices.getText(this,"nombre_proyeccion"));
		}
		return lblNombreProy;
	}
	/**
	 * Inicializa el cuadro de texto que contiene el nombre de la proyeccin
	 * @return
	 */
	public JTextField getTxtNombreProy() {
		if(txtNombreProy==null){
			txtNombreProy=new JTextField();
			Dimension d=new Dimension(320,20);
			txtNombreProy.setPreferredSize(d);
			txtNombreProy.setMinimumSize(d);
		}
		return txtNombreProy;
	}
	private ArrayList obtainProjections() {
		ArrayList items = new ArrayList();
		ArrayList util = new ArrayList();
		for (int i = 0;i<proj4.getProjectionNameList().size(); i++) {
			try {
				items.add(i, PluginServices.getText(this,proj4.getProj4ProjectionName(i)));
				util.add(i, proj4.getProj4ProjectionName(i));
			} catch (CrsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		
		/*String sentence = "SELECT coord_op_method_name " +
		  				"FROM epsg_coordoperationmethod " +	                              
		  				"WHERE coord_op_method_code > 9800 "+
		  				"ORDER BY coord_op_method_name ASC";
		EpsgConnection connect = new EpsgConnection();
		connect.setConnectionEPSG();
		ResultSet result = Query.select(sentence,connect.getConnection());
		int in=0;
		try {
			while (result.next()) {
				String item = result.getString("coord_op_method_name"); 
				items.add(in, PluginServices.getText(this, item.replaceAll(" ", "_")));
				util.add(in, item);
				in++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}*/
		
		/*
		 * Ordenamos los ArrayList por si con las traducciones
		 * se producen cambios de orden. Pensar en la implementacion...
		 */
		String tempItem1,tempItem2, tempUtil1, tempUtil2;
		for (int i=0; i< items.size()-1; i++) {
			for (int j=i+1; j< items.size();j++) {
				tempItem1 = (String)items.get(i);				
				tempItem2 = (String)items.get(j);
				tempUtil1 = (String)util.get(i);
				tempUtil2 = (String)util.get(j);
				if (((String)items.get(j)).compareTo(tempItem1) < 0) {
					items.remove(i);
					items.add(i, tempItem2);
					items.remove(j);
					items.add(j, tempItem1);
					util.remove(i);
					util.add(i, tempUtil2);
					util.remove(j);
					util.add(j, tempUtil1);
				}
			}
		}
		setProjection(util);
		return items;
	}
	/*
	 * Redimensiona el tama�o de las filas y columnas de la tabla
	 *
	 */
	public void ajustarTamanoTabla(){
	    TableColumn column = null;
	    //Fijar el alto de las filas
	    getTableParametros().setRowHeight(20);
	    //Fijar el ancho de las columnas
	    column = getTableParametros().getColumnModel().getColumn(0);
	    column.setPreferredWidth(30);
	    column = getTableParametros().getColumnModel().getColumn(1);
	    column.setPreferredWidth(90);
	    column = getTableParametros().getColumnModel().getColumn(2);
	    column.setPreferredWidth(120);

	}
	
	/**
	 * Fija los eventos de los RadioButtons y dem�s controles
	 */
	public void actionPerformed(ActionEvent e) {
		//MOSTRAR UN PANEL U OTRO
        CardLayout cl = (CardLayout)(cardPanel.getLayout());
		if (e.getSource().equals(getRbProyectado())){
			/*Si est� seleccionada la opcion de Proyectado 
			se muestra el panel de seleccion de la proyecci�n con sus par�metros*/
			 //cl.show(cardPanel, PROYECTADOPANEL);
			habilitarControles();
		}else if(e.getSource().equals(getRbGeografico())){
			/*Se muestra el panel de Sistema de Coordenadas Geografico*/
			 //cl.show(cardPanel, GEOGRAFICOPANEL);
			deshabilitarControles();
		} else if (e.getSource().equals(getBtnImportar())) {
			System.out.println("Importar Proyeccion");
			ImportNewCrsDialog newCrs = new ImportNewCrsDialog(PluginServices.getText(this, "projection"));
       	 	PluginServices.getMDIManager().addWindow(newCrs);
       	 	if (newCrs.getCode() != -1) {
	       	 	setCrs(newCrs.getCode());
	       	 	fillData(getCrs());
       	 	}
		}
	}

	/*
	 * Crear el panel que contiene los componentes de 
	 * un sistema de coordenadas geografico
	 * */
	/*public JPanel getGeograficoPanel() {
		if(geograficoPanel==null){
			BorderLayout b=new BorderLayout();
			b.setVgap(5);
			b.setHgap(5);
			geograficoPanel=new JPanel(b);
			geograficoPanel.add(new JButton("Crear panel Geogr�fico 2D"));
		}
		return geograficoPanel;
	}*/
	
	public void fillData(ICrs crs) {
		if (!crs.getCrsWkt().getProjcs().equals("")) {
			getRbProyectado().setSelected(true);
			int index = proj4.findProjection(crs.getCrsWkt().getProjection());
			String proj2Compare = "";
			try {
				proj2Compare = proj4.getProj4ProjectionName(index);
			} catch (CrsException e1) {
				e1.printStackTrace();
			}
			for (int i = 0; i < getCbProyeccion().getItemCount(); i++) {
				if (getProjection(i).equals(proj2Compare)) {
					getCbProyeccion().setSelectedIndex(i);
					break;
				}
			}
			getTxtNombreProy().setText(crs.getCrsWkt().getProjcs());
			setPos(proj4.findProjection(crs.getCrsWkt().getProjection()));
			int numRow = model.getRowCount();
			while (numRow != 0) {
				numRow = numRow - 1;
				model.removeRow(numRow);
			}
			Object[] data = new Object[3];
			data[2] = "Metros";
			ArrayList trueParametersNames = new ArrayList();			
			
			try {				
			
				for (int i = 0; i < crs.getCrsWkt().getParam_name().length; i++) {
					String param = crs.getCrsWkt().getParam_name()[i].trim();
					if (param.equals("semi_major") || param.equals("semi_minor")) {
						continue;
					}
					trueParametersNames.add(param);
					data[0] = PluginServices.getText(this, param);
					data[1] = crs.getCrsWkt().getParam_value()[i];
					int pos = proj4.findProjectionParameter(param);
					String unit = proj4.getProjectionParameterUnitList(pos);
					if (unit.equals("Angular")) 
						data[2] = "Degree";
					else if (unit.equals("Unitless"))
						data[2] = "Unitless";
					else if (unit.equals("Linear"))
						data[2] = "Meters";
					model.addRow(data);
				}
				setTrueParametersNames(trueParametersNames);
			} catch (CrsException e) {
				e.printStackTrace();
			}
		} else {
			getRbGeografico().setSelected(true);
			 CardLayout cl = (CardLayout)(cardPanel.getLayout());
			//cl.show(cardPanel, GEOGRAFICOPANEL);
			 deshabilitarControles();
		}
	}
	
	public void fillData(CoordinateReferenceSystem crs) {
		if (crs instanceof DefaultProjectedCRS) {
			DefaultProjectedCRS sour = (DefaultProjectedCRS) crs;
			String[] val = sour.getName().toString().split(":");
			
			if (val.length<2)
				getTxtNombreProy().setText(val[0]);
			else
				getTxtNombreProy().setText(val[1]);
			
			val = sour.getConversionFromBase().getMethod().getName().toString().split(":");
			String proj;
			if (val.length<2) 
				proj=val[0];
			else
				proj=val[1];			
			
			int index = proj4.findProjection(proj);
			String proj2Compare = "";
			try {
				proj2Compare = proj4.getProj4ProjectionName(index);
			} catch (CrsException e1) {
				e1.printStackTrace();
			}
			for (int i = 0; i < getCbProyeccion().getItemCount(); i++) {
				if (getProjection(i).equals(proj2Compare)) {
					getCbProyeccion().setSelectedIndex(i);
					break;
				}
			}
			String[] param_name = new String[sour.getConversionFromBase().getParameterValues().values().size()];
			String[] param_value= new String[sour.getConversionFromBase().getParameterValues().values().size()];
			for (int i=0; i< sour.getConversionFromBase().getParameterValues().values().size();i++) {
				String str = sour.getConversionFromBase().getParameterValues().values().get(i).toString();
				Unit u = sour.getConversionFromBase().getParameterValues().parameter(str.split("=")[0]).getUnit();
				double value = sour.getConversionFromBase().getParameterValues().parameter(str.split("=")[0]).doubleValue();
				value = convert(value, u.toString());
				param_name[i] = str.split("=")[0];
				param_value [i] = String.valueOf(value);
			}
			
			setPos(proj4.findProjection(proj));
			int numRow = model.getRowCount();
			while (numRow != 0) {
				numRow = numRow - 1;
				model.removeRow(numRow);
			}
			
			Object[] data = new Object[3];
			ArrayList trueParametersNames = new ArrayList();			
			try {				
			
				for (int i = 0; i < param_name.length; i++) {
					String param = param_name[i].trim();				
					if (param.equals("semi_major") || param.equals("semi_minor")) {
						continue;
					}
					trueParametersNames.add(param);
					data[0] = PluginServices.getText(this, param);
					data[1] = param_value[i];
					int pos = proj4.findProjectionParameter(param);
					String unit = proj4.getProjectionParameterUnitList(pos);
					if (unit.equals("Angular")) 
						data[2] = "Degree";
					else if (unit.equals("Unitless"))
						data[2] = "Unitless";
					else if (unit.equals("Linear"))
						data[2] = "Meters";
					model.addRow(data);
				}
				setTrueParametersNames(trueParametersNames);
			} catch (CrsException e) {
				e.printStackTrace();
			}
		}
		else {
			getRbGeografico().setSelected(true);
			 CardLayout cl = (CardLayout)(cardPanel.getLayout());
			//cl.show(cardPanel, GEOGRAFICOPANEL);
			 deshabilitarControles();
		}
	}
	
	public void fillTable(String projection) {
		try {			
			int index = proj4.findProjection(projection);
			ArrayList params = proj4.getProj4ProjectionParameters(index);
			ArrayList defaultValuesParams = proj4.getProj4ProjectionParameterDefaultValues(index);
			setPos(index);
			String[] parameters = new String[params.size()];
			String[] defaultValues = new String[params.size()];
			String[] units = new String[params.size()];
			for (int i = 0; i < params.size(); i++) {
				parameters[i] = params.get(i).toString();
				int pos = proj4.findProjectionParameter(parameters[i]);
				defaultValues[i] = defaultValuesParams.get(i).toString();
				units[i] = proj4.getProjectionParameterUnitList(pos);
				
			}			
			int numRow = model.getRowCount();
			while (numRow != 0) {
				numRow = numRow - 1;
				model.removeRow(numRow);
			}
			Object[] data = new Object[3];
			data[1] = ""+0;
			data[2] = "Metros";
			String[] items = null;
			//Estas 5 lineas se eliminaran cuando se pueda utilizar el codigo
			//del siguiente comentario
			ArrayList unit = obtenerItemsUnidades();
			items = new String[unit.size()];
			for (int j=0;j<unit.size();j++){
				items[j] = unit.get(j).toString();
			}
			/**
			 * Esto debera ser utilizado cuando podamos distinguir
			 * entre las unidades. De momento vamos a coger todas
			 */
			ArrayList trueParametersNames = new ArrayList();
			for (int i = 0; i < parameters.length; i++) {
				trueParametersNames.add(i,parameters[i]);
				data[0] = PluginServices.getText(this, parameters[i]);
				data[1] = defaultValues[i];
				if (units[i].equals("Angular")) {
					data[2] = "Degree";
					//define los items del combo
				    /*unit = obtenerItemsUnidadesAngular();
					items = new String[unit.size()];
					for (int j=0;j<unit.size();j++){
						items[j] = unit.get(j).toString();
					}*/					
				}
				else if (units[i].equals("Unitless")) {
					data[2] = "Unitless";
					//define los items del combo
				    /*unit = obtenerItemsUnidadesScale();
					items = new String[unit.size()];
					for (int j=0;j<unit.size();j++){
						items[j] = unit.get(j).toString();
					}*/				
				}
				else {					
					data[2] = "Meters";
					//define los items del combo
				    /*unit = obtenerItemsUnidades();
					items = new String[unit.size()];
					for (int j=0;j<unit.size();j++){
						items[j] = unit.get(j).toString();
					}*/					
				}
				model.addRow(data);				
				/*TableColumn col = getTableParametros().getColumnModel().getColumn(2);
		        
		        col.setCellEditor(new DefaultCellEditor(getCbUnits()));
		        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		       
		        col.setCellRenderer(renderer);
		        getTableParametros().setCellEditor(new DefaultCellEditor(getCbUnits()));	
		        */
			}			
			setTrueParametersNames(trueParametersNames);			
		} catch (CrsException e) {
			e.printStackTrace();
		}
	}
	public void itemStateChanged(ItemEvent e) {
		if (e.getItemSelectable().equals(cbProyeccion) ) {
			String op = (String)e.getItem();			
			int option = ((JComboBox)e.getSource()).getSelectedIndex();
			fillTable(getProjection(option));
		}
		
		if (e.getItemSelectable().equals(cbUnits) ) {
			 System.out.println( (String)e.getItem());
			 double value = Double.valueOf((String)getTableParametros().getValueAt(0, 1)).doubleValue();
			 value = convertFromMeters(sourceUnit, (String)e.getItem(), value);
			 sourceUnit = (String)e.getItem();
			 String val = String.valueOf(value);
			 getTableParametros().setValueAt(val, 0, 1);
		}
	}
	
	public void cleanData() {
		getTxtNombreProy().setText("");
		getCbProyeccion().setSelectedIndex(0);
	}
	
	public double convert(double value, String measure) throws ConversionException {
		if (measure.equals("D.MS")) {		
			value *= this.divider;
	        int deg,min;
	        deg = (int) (value/10000); value -= 10000*deg;
	        min = (int) (value/  100); value -=   100*min;
	        if (min<=-60 || min>=60) {  // Accepts NaN
	            if (Math.abs(Math.abs(min) - 100) <= EPS) {
	                if (min >= 0) deg++; else deg--;
	                min = 0;
	            } else {
	                throw new ConversionException("Invalid minutes: "+min);
	            }
	        }
	        if (value<=-60 || value>=60) { // Accepts NaN
	            if (Math.abs(Math.abs(value) - 100) <= EPS) {
	                if (value >= 0) min++; else min--;
	                value = 0;
	            } else {
	                throw new ConversionException("Invalid secondes: "+value);
	            }
	        }
	        value = ((value/60) + min)/60 + deg;
	        return value;
		}
		if (measure.equals("grad") || measure.equals("grade")) 
			return ((value * 180.0) / 200.0);			
		if (measure.equals(""+(char)176)) 
			return value;		
		if (measure.equals("DMS") ) 
			return value;		
		if (measure.equals("m")) 
			return value;	
		if (measure.equals("")) 
			return value;
		
		throw new ConversionException("Conversion no contemplada: "+measure);
    }
	
	public double convert2Meters(String unit, double value) {
		double factor_b = 0;
		double factor_c = 0;
		String sentence = "SELECT factor_b, factor_c " +
			"FROM epsg_unitofmeasure " +	                              
			"WHERE unit_of_meas_name = '" +unit + "'";
		EpsgConnection connect = new EpsgConnection();
		connect.setConnectionEPSG();
		ResultSet result = Query.select(sentence,connect.getConnection());
		try {
			result.next();
			factor_b = result.getDouble("factor_b");
			factor_c = result.getDouble("factor_c");			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return ((value*factor_b)/factor_c);
	}
	
	public double convert2Unitless(String unit, double value) {
		double factor_b = 0;
		double factor_c = 0;
		String sentence = "SELECT factor_b, factor_c " +
			"FROM epsg_unitofmeasure " +	                              
			"WHERE unit_of_meas_name = '" +unit + "'";
		EpsgConnection connect = new EpsgConnection();
		connect.setConnectionEPSG();
		ResultSet result = Query.select(sentence,connect.getConnection());
		try {
			result.next();
			factor_b = result.getDouble("factor_b");
			factor_c = result.getDouble("factor_c");			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return ((value*factor_b)/factor_c);
	}
	
	public double convert2Degree(String unit, double value) {
		double factor_b = 0;
		double factor_c = 0;
		String sentence = "SELECT factor_b, factor_c " +
			"FROM epsg_unitofmeasure " +	                              
			"WHERE unit_of_meas_name = '" +unit + "'";
		EpsgConnection connect = new EpsgConnection();
		connect.setConnectionEPSG();
		ResultSet result = Query.select(sentence,connect.getConnection());
		try {
			result.next();
			factor_b = result.getDouble("factor_b");
			factor_c = result.getDouble("factor_c");			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		value = ((value*factor_b)/factor_c);
		return ((value * 180.0) / 200.0);
	}
	
	public double convertFromMeters(String unitSource, String unitTarget, double value) {
		double factor_b = 0;
		double factor_c = 0;
		String sentence = "SELECT factor_b, factor_c " +
			"FROM epsg_unitofmeasure " +	                              
			"WHERE unit_of_meas_name = '" +unitSource + "'";
		EpsgConnection connect = new EpsgConnection();
		connect.setConnectionEPSG();
		ResultSet result = Query.select(sentence,connect.getConnection());
		try {
			result.next();
			factor_b = result.getDouble("factor_b");
			factor_c = result.getDouble("factor_c");			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		value = ((value*factor_b)/factor_c);
		
		sentence = "SELECT factor_b, factor_c " +
		"FROM epsg_unitofmeasure " +	                              
		"WHERE unit_of_meas_name = '" +unitTarget + "'";
		connect = new EpsgConnection();
		connect.setConnectionEPSG();
		result = Query.select(sentence,connect.getConnection());
		try {
			result.next();
			factor_b = result.getDouble("factor_b");
			factor_c = result.getDouble("factor_c");			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return ((value*factor_c)/factor_b);
	}
	
	public double convertFromUnitless(String unitSource, String unitTarget, double value) {
		double factor_b = 0;
		double factor_c = 0;
		String sentence = "SELECT factor_b, factor_c " +
			"FROM epsg_unitofmeasure " +	                              
			"WHERE unit_of_meas_name = '" +unitSource + "'";
		EpsgConnection connect = new EpsgConnection();
		connect.setConnectionEPSG();
		ResultSet result = Query.select(sentence,connect.getConnection());
		try {
			result.next();
			factor_b = result.getDouble("factor_b");
			factor_c = result.getDouble("factor_c");	
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		value = ((value*factor_b)/factor_c);
		
		sentence = "SELECT factor_b, factor_c " +
		"FROM epsg_unitofmeasure " +	                              
		"WHERE unit_of_meas_name = '" +unitTarget + "'";
		connect = new EpsgConnection();
		connect.setConnectionEPSG();
		result = Query.select(sentence,connect.getConnection());
		try {
			result.next();
			factor_b = result.getDouble("factor_b");
			factor_c = result.getDouble("factor_c");			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return ((value*factor_c)/factor_b);
	}
	
	public double convertFromDegree(String unitSource, String unitTarget, double value) {
		double factor_b = 0;
		double factor_c = 0;
		String sentence = "SELECT factor_b, factor_c " +
			"FROM epsg_unitofmeasure " +	                              
			"WHERE unit_of_meas_name = '" +unitSource + "'";
		EpsgConnection connect = new EpsgConnection();
		connect.setConnectionEPSG();
		ResultSet result = Query.select(sentence,connect.getConnection());
		try {
			result.next();
			factor_b = result.getDouble("factor_b");
			factor_c = result.getDouble("factor_c");			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		value = ((value*factor_b)/factor_c);
		
		sentence = "SELECT factor_b, factor_c " +
		"FROM epsg_unitofmeasure " +	                              
		"WHERE unit_of_meas_name = '" +unitTarget + "'";
		connect = new EpsgConnection();
		connect.setConnectionEPSG();
		result = Query.select(sentence,connect.getConnection());
		try {
			result.next();
			factor_b = result.getDouble("factor_b");
			factor_c = result.getDouble("factor_c");			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return ((value*factor_c)/factor_b);
	}
	
	public Proj4 getProj4() {
		return proj4;
	}
	
	/**
	 * Contiene la posición de la proyección seleccionada dentro de la lista
	 * de proyecciones de proj4.
	 * @param pos
	 */
	public void setPos(int pos) {
		this.pos = pos;
	}
	
	/** Obtiene la posición de la proyección seleccionada dentro de la lista
	 * de proyecciones de proj4.
	 */
	public int getPos() {
		return this.pos;
	}
	
	/**
	 * Deshabilita todos los controles del panel
	 *
	 */
	private void deshabilitarControles() {
		getProyectadoPanel().setEnabled(false);
		getTableParametros().setEnabled(false);
		getTableParametros().getTableHeader().setEnabled(false);
		getTxtNombreProy().setEnabled(false);
		getLblNombreProy().setEnabled(false);
		getLblProyeccion().setEnabled(false);
		getCbProyeccion().setEnabled(false);	
		getBtnImportar().setEnabled(false);
	}
	
	/**
	 * Habilita todos los controles del panel.
	 *
	 */
	private void habilitarControles() {	
		getProyectadoPanel().setEnabled(true);
		getTableParametros().setEnabled(true);
		getTableParametros().getTableHeader().setEnabled(true);
		getTxtNombreProy().setEnabled(true);
		getLblNombreProy().setEnabled(true);
		getLblProyeccion().setEnabled(true);
		getCbProyeccion().setEnabled(true);
		getBtnImportar().setEnabled(true);
	}
	/**
	 * Inicializa el boton que importa la proyeccion actual
	 * @return
	 */
	public JButton getBtnImportar() {
		if(btnImportar==null){
			btnImportar=new JButton("...");
			btnImportar.addActionListener(this);
			
		}
		return btnImportar;
	}
	
	/**
	 * CRS que estamos cogiendo para poder rellenar los datos
	 * @param code
	 */
	public void setCrs(int code) {
		try {
			crs = new CrsFactory().getCRS("EPSG:"+code);
		} catch (CrsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Obtiene el CRS actual
	 * @return
	 */
	public ICrs getCrs() {
		return crs;
	}
	
	/**
	 * Seleccion de la proyeccion que estamos utilizando. Esto lo hacemos para
	 * obtener la cadena correcta de la proyeccion en lugar de su traduccion
	 * que se utiliza en el combobox.
	 * @param proj
	 */
	public void setProjection(ArrayList proj) {
		projections = proj;
	}
	
	/**
	 * Obtencion de la proyecion utilizada a traves del indice.
	 * @param indice
	 * @return
	 */
	public String getProjection(int indice) {
		return (String)projections.get(indice);
	}
	
	/**
	 * define los parametros utilizados por una proyeccion. Al igual que la
	 * proyeccion, sirve para coger el valor correcto del parametro en lugar
	 * de su traduccion en la tabla.
	 * @param names
	 */
	public void setTrueParametersNames(ArrayList names) {
		this.trueParametersNames = names;
	}
	
	/**
	 * Consigue los nombres buenos de los parametros utilizados en una proyeccion
	 * @param indice
	 * @return
	 */
	public String getTrueParametersNames(int indice) {
		return (String)trueParametersNames.get(indice);
	}


}

