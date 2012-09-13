/* gvSIG. Sistema de Informaciï¿½n Geogrï¿½fica de la Generalitat Valenciana
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
 *   Av. Blasco Ibï¿½ï¿½ez, 50
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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.units.ConversionException;
import javax.units.Unit;

import org.geotools.referencing.crs.AbstractSingleCRS;
import org.geotools.referencing.datum.DefaultEllipsoid;
import org.geotools.referencing.datum.DefaultGeodeticDatum;
import org.geotools.referencing.datum.DefaultPrimeMeridian;
import org.gvsig.crs.CrsException;
import org.gvsig.crs.CrsFactory;
import org.gvsig.crs.ICrs;
import org.gvsig.crs.gui.dialog.ImportNewCrsDialog;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.iver.andami.PluginServices;
import com.iver.utiles.swing.JComboBox;

import es.idr.teledeteccion.connection.EpsgConnection;
import es.idr.teledeteccion.connection.Query;

/**
 * Panel de definicin del Datum 
 * 
 * @author Luisa Marina Fernandez Ruiz (luisam.fernandez@uclm.es)
 * @author Jose Luis Gomez Martinez (joseluis.gomez@uclm.es)
 * 
 *
 */
public class DefinirDatum extends JPanel implements ActionListener, FocusListener, ItemListener, KeyListener{
	
	private JTextField txtDatum;
	private JTextField txtElipsoide;
	private JTextField txtSemMay;
	private JTextField txtSemMen;
	private JTextField txtInvF;
	private JTextField txtMeridian;
	private JTextField txtLong;
	private JTextField txtNombreCrs;
	private JTextField txtCodigoCrs;
	
	private JLabel lblDatum;
	private JLabel lblElipsoide;
	private JLabel lblMeridian;
	private JLabel lblLong;
	private JLabel lblNombreCrs;
	private JLabel lblCodigoCrs;
	private JLabel lblSemiejeMayor;
	private JLabel lblSemiejeMenor;
	private JLabel lblInverseFlat;
	private JLabel lblDefinir;
	
	
	private JButton btnImportDatum;
	private JButton btnImportElipsoide;
	private JButton btnImportMeridian;
	
	private JComboBox cbSemMay;
	private JComboBox cbSemMen;
	private JComboBox cbInvF;
	private JComboBox cbLong;
	
	private JRadioButton rbA_Inv;
	private JRadioButton rbA_B;
	private ButtonGroup groupRadioButton;
	
	private Dimension bigSize;
	private Dimension smallSize;
	private Dimension dimLabels;
//	private ImageIcon imodify;
	
	private static final long serialVersionUID = 1L;
	ICrs crs = null;
	
	private String sourceUnitLong = null;
	private String sourceUnitSemMay = null;
	private String sourceUnitSemMen = null;
	
	ArrayList lengthUnits = null;
	ArrayList angularUnits = null;
	int divider=10000;
	
	boolean primera = true;
	
	/**
     * Small tolerance factor for rounding errors.
     */
    private static final double EPS = 1E-8;

	public DefinirDatum() {
		
		super();
		//imodify = new ImageIcon(DefinirDatum.class.getClassLoader().getResource("images/search.png"));
		dimLabels=new Dimension(110,15);
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
		bigSize=new Dimension();
		bigSize.width=350;
		smallSize=new Dimension();
		smallSize.width=130;
		inicializarNuevo();
		
	}
	private void inicializarNuevo(){
		JPanel pMain=new JPanel();
		JPanel pDatum=new JPanel();
		JPanel pElipsoide=new JPanel();
		JPanel pMeridian=new JPanel();
		JPanel pNorthis=new JPanel(new FlowLayout(FlowLayout.LEFT,10,5));
		pMain.setLayout(new BorderLayout());
		
		pMain.setBorder(BorderFactory.createTitledBorder(PluginServices.getText(this,"Dat_nDatum")));
		pElipsoide.setBorder(BorderFactory.createTitledBorder(PluginServices.getText(this,"Dat_nElipsoide")));
		pMeridian.setBorder(BorderFactory.createTitledBorder(PluginServices.getText(this,"Dat_nMeridian")));
		
		/*Panel que contiene los datos del Datum*/
		pDatum.setLayout(new GridLayout(2,1));
		JPanel pD1=new JPanel(new FlowLayout(FlowLayout.LEFT,10,0));
		pD1.add(getLblDatum());
		JPanel pD2=new JPanel(new FlowLayout(FlowLayout.LEFT,10,0));
		pD2.add(getTxtDatum());
		pD2.add(getBtnImportDatum());
		pDatum.add(pD1);
		pDatum.add(pD2);
		
		/*Panel que contiene los datos del Elipsoide*/
		pElipsoide.setLayout(new GridLayout(6,1));
		//Crear y agregar los componentes a los subpaneles que componen el panel de definicin del elipsoide
		JPanel pE1=new JPanel(new FlowLayout(FlowLayout.LEFT,10,2));
		pE1.add(getLblElipsoide());
		JPanel pE2=new JPanel(new FlowLayout(FlowLayout.LEFT,10,2));
		pE2.add(getTxtElipsoide());
		pE2.add(getBtnImportElipsoide());
		JPanel pE3=new JPanel(new FlowLayout(FlowLayout.LEFT,10,2));
		pE3.add(getLblDefinir());
		pE3.add(getRbA_Inv());
		pE3.add(getRbA_B());
		JPanel pE4=new JPanel(new FlowLayout(FlowLayout.LEFT,10,2));
		//Semieje mayor
		pE4.add(getLblSemiejeMayor());
		pE4.add(getTxtSemMay());
		pE4.add(getCbSemMay());
		JPanel pE5=new JPanel(new FlowLayout(FlowLayout.LEFT,10,2));
		pE5.add(getLblInverseFlat());
		pE5.add(getTxtInvF());
		JPanel pE6=new JPanel(new FlowLayout(FlowLayout.LEFT,10,2));
		//Semieje menor
		pE6.add(getLblSemiejeMenor());
		pE6.add(getTxtSemMen());
		pE6.add(getCbSemMen());
		
		//Agregar los subpaneles al panel del Elipsoide
		pElipsoide.add(pE1);
		pElipsoide.add(pE2);
		pElipsoide.add(pE3);
		pElipsoide.add(pE4);
		pElipsoide.add(pE5);
		pElipsoide.add(pE6);
		//Agrupar los Radio Buttoms
		agruparRadioButtons();
		
		/*Panel que contiene los datos del Meridiano*/
		pMeridian.setLayout(new GridLayout(2,1));
		JPanel pM1=new JPanel(new FlowLayout(FlowLayout.LEFT,10,3));
		pM1.add(getLblMeridian());
		pM1.add(getTxtMeridian());
		pM1.add(getBtnImportMeridian());
		JPanel pM2=new JPanel(new FlowLayout(FlowLayout.LEFT,10,3));
		pM2.add(getLblLong());
		pM2.add(getTxtLong());
		pM2.add(getCbLong());
		pMeridian.add(pM1);
		pMeridian.add(pM2);
		pMain.add(pDatum,BorderLayout.NORTH);
		pMain.add(pElipsoide,BorderLayout.CENTER);
		pMain.add(pMeridian,BorderLayout.SOUTH);
		this.add(pMain,BorderLayout.CENTER);
		//Agregar en norte-this el nombre y el cdigo
		pNorthis.add(getLblNombreCrs());
		pNorthis.add(getTxtNombreCrs());
		pNorthis.add(getLblCodigoCrs());
		pNorthis.add(getTxtCodigoCrs());
		this.add(pNorthis,BorderLayout.NORTH);
		
	}
	/**
	 * Inicializa el botn Importar del datum
	 * @return
	 */
	public JButton getBtnImportDatum() {
		if(btnImportDatum==null){
			btnImportDatum=new JButton();
			btnImportDatum.setText("...");
			//btnImportDatum.setIcon(imodify);
			btnImportDatum.addActionListener(this);
		}
		return btnImportDatum;
	}
	/**
	 * Inicializa el botn Importar del Elipsoide
	 * @return
	 */
	public JButton getBtnImportElipsoide() {
		if(btnImportElipsoide==null){
			btnImportElipsoide=new JButton();
			btnImportElipsoide.setText("...");
			//btnImportElipsoide.setIcon(imodify);
			btnImportElipsoide.addActionListener(this);
		}
		return btnImportElipsoide;
	}
	/**
	 * Inicializa el botn Importar del Meridiano
	 * @return
	 */
	public JButton getBtnImportMeridian() {
		if(btnImportMeridian==null){
			btnImportMeridian=new JButton();
			btnImportMeridian.setText("...");
			//btnImportMeridian.setIcon(imodify);
			btnImportMeridian.addActionListener(this);
		}
		return btnImportMeridian;
	}
	/**
	 * Inicializa el Combo box con las unidades de Inverse Flat
	 * @return
	 */
	public JComboBox getCbInvF() {
		if (cbInvF==null){
			ArrayList units = obtenerItemsUnidades();
			String[] items = new String[units.size()];
			for (int i=0;i<units.size();i++){
				items[i] = units.get(i).toString();
			}
			cbInvF=new JComboBox(items);
			/*Seleccionar un item por defecto*/
			cbInvF.setSelectedIndex(0);
			cbInvF.addItemListener(this);
		}
		return cbInvF;
	}
	/**
	 * Inicializa el Combo box con las unidades de Longitud
	 * @return
	 */
	public JComboBox getCbLong() {
		if (cbLong==null){
			ArrayList units = obtenerItemsUnidadesAngle();
			String[] items = new String[units.size()];
			String[] tooltips = new String[units.size()];
			for (int i=0;i<units.size();i++){
				items[i] = tooltips[i] = units.get(i).toString();
			}
			cbLong=new JComboBox(items);
			cbLong.setRenderer(new ComboBoxTootipRenderer(tooltips));
			cbLong.setPreferredSize(new Dimension(230, 25));
			/*Seleccionar un item por defecto*/
			cbLong.setSelectedIndex(1);
			cbLong.addItemListener(this);
			sourceUnitLong = getAngularUnit(cbLong.getSelectedIndex());
		}
		return cbLong;
	}
	/**
	 * Inicializa el Combo box con las unidades de Semieje Mayor
	 * @return
	 */
	public JComboBox getCbSemMay() {
		if (cbSemMay==null){
			ArrayList units = obtenerItemsUnidades();
			String[] items = new String[units.size()];
			String[] tooltips = new String[units.size()];
			for (int i=0;i<units.size();i++){
				items[i] = tooltips[i] = units.get(i).toString();
			}
			cbSemMay=new JComboBox(items);
			cbSemMay.setRenderer(new ComboBoxTootipRenderer(tooltips));
			cbSemMay.setPreferredSize(new Dimension(230, 25));
			/*Seleccionar un item por defecto*/
			cbSemMay.setSelectedIndex(0);
			cbSemMay.addItemListener(this);
			sourceUnitSemMay = getLengthUnit(cbSemMay.getSelectedIndex());
		}
		return cbSemMay;
	}
	/**
	 * Inicializa el Combo box con las unidades de Semieje Menor
	 * @return
	 */
	public JComboBox getCbSemMen() {
		if (cbSemMen==null){
			ArrayList units = obtenerItemsUnidades();
			String[] items = new String[units.size()];
			for (int i=0;i<units.size();i++){
				items[i] = units.get(i).toString();
			}
			cbSemMen=new JComboBox(items);
			/*Seleccionar un item por defecto*/
			cbSemMen.setSelectedIndex(0);
			cbSemMen.addItemListener(this);
			sourceUnitSemMen = getLengthUnit(cbSemMen.getSelectedIndex());
		}
		return cbSemMen;
	}
	/*
	 * Accede al la base de datos y obtiene los items de
	 * los combobox
	 * @return
	 */
	private ArrayList obtenerItemsUnidades(){ //unidades de longitud...
		//TODO: Obtener los items del combo de la base de datos
		ArrayList items = new ArrayList();
		ArrayList lengthUnits = new ArrayList();
				
		String sentence = "SELECT unit_of_meas_name " +
		  				"FROM epsg_unitofmeasure " +	                              
		  				"WHERE unit_of_meas_type = 'length'";
		EpsgConnection connect = new EpsgConnection();
		connect.setConnectionEPSG();
		ResultSet result = Query.select(sentence,connect.getConnection());
		int in=0;
		try {
			while (result.next()) {
				String item = result.getString("unit_of_meas_name");
				items.add(in,PluginServices.getText(this, item.replaceAll(" ", "_")));
				lengthUnits.add(in, item);
				in++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setLengthUnit(lengthUnits);
		return items;
	}
	
	/*
	 * Accede al la base de datos y obtiene los items de
	 * los combobox
	 * @return
	 */
	private ArrayList obtenerItemsUnidadesAngle(){ //unidades angulares...
		//TODO: Obtener los items del combo de la base de datos
		ArrayList items = new ArrayList();
		ArrayList angularUnit = new ArrayList();
				
		String sentence = "SELECT unit_of_meas_name " +
		  				"FROM epsg_unitofmeasure " +	                              
		  				"WHERE unit_of_meas_type = 'angle' and factor_b is not null and factor_c is not null";
		EpsgConnection connect = new EpsgConnection();
		connect.setConnectionEPSG();
		ResultSet result = Query.select(sentence,connect.getConnection());
		int in=0;
		try {
			while (result.next()) {
				String item = result.getString("unit_of_meas_name");
				items.add(in, PluginServices.getText(this, item.replaceAll(" ", "_")));
				angularUnit.add(in, item);
				in++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setAngularUnit(angularUnit);
		return items;
	}
	/**
	 * Inicializa el JLabel Datum
	 * @return
	 */
	public JLabel getLblDatum() {
		if (lblDatum==null){
			lblDatum=new JLabel();
			lblDatum.setText(PluginServices.getText(this,"Dat_Datum"));
		}
		return lblDatum;
	}
	/**
	 * Inicializa el JLabel Elipsoide
	 * @return
	 */
	public JLabel getLblElipsoide() {
		if (lblElipsoide==null){
			lblElipsoide=new JLabel();
			lblElipsoide.setText(PluginServices.getText(this,"Dat_Elips"));
			//lblElipsoide.setText("Introduce el Elipsoide");
		}
		return lblElipsoide;
	}

	/**
	 * Inicializa el JLabel Longitud
	 * @return
	 */
	public JLabel getLblLong() {
		if (lblLong==null){
			lblLong=new JLabel();
			lblLong.setText(PluginServices.getText(this,"Dat_Long"));
			lblLong.setMinimumSize(dimLabels);
			lblLong.setPreferredSize(dimLabels);
		}
		return lblLong;
	}
	/**
	 * Inicializa el JLabel Meridiano
	 * @return
	 */
	public JLabel getLblMeridian() {
		if (lblMeridian==null){
			lblMeridian=new JLabel();
			lblMeridian.setText(PluginServices.getText(this,"Dat_Meridian"));
			lblMeridian.setMinimumSize(dimLabels);
			lblMeridian.setPreferredSize(dimLabels);
		}
		return lblMeridian;
	}
	/**
	 * Inicializa el JLabel del Cdigo de Crs
	 * @return
	 */
	public JLabel getLblCodigoCrs() {
		if (lblCodigoCrs==null){
			lblCodigoCrs=new JLabel(PluginServices.getText(this,"Dat_CodeCrs")+":");
		}
		return lblCodigoCrs;
	}
	/**
	 * Inicializa el JLabel del Nombre del Crs
	 * @return
	 */
	public JLabel getLblNombreCrs() {
		if (lblNombreCrs==null){
			lblNombreCrs=new JLabel(PluginServices.getText(this,"Dat_NombreCrs")+":");
		}
		return lblNombreCrs;
	}
	public JLabel getLblInverseFlat() {
		if(lblInverseFlat==null){
			lblInverseFlat=new JLabel();
			lblInverseFlat.setText(PluginServices.getText(this,"Dat_InvF")+(" (inv_f)"));
			lblInverseFlat.setMinimumSize(dimLabels);
			lblInverseFlat.setPreferredSize(dimLabels);
		}	
		return lblInverseFlat;
	}
	public JLabel getLblSemiejeMayor() {
		if(lblSemiejeMayor==null){
			lblSemiejeMayor=new JLabel();
			lblSemiejeMayor.setText(PluginServices.getText(this,"Dat_SemMay")+(" (a)"));
			lblSemiejeMayor.setMinimumSize(dimLabels);
			lblSemiejeMayor.setPreferredSize(dimLabels);
			
		}	
		return lblSemiejeMayor;
	}
	public JLabel getLblSemiejeMenor() {
		if(lblSemiejeMenor==null){
			lblSemiejeMenor=new JLabel();
			lblSemiejeMenor.setText(PluginServices.getText(this,"Dat_SemMen")+(" (b)"));
			lblSemiejeMenor.setMinimumSize(dimLabels);
			lblSemiejeMenor.setPreferredSize(dimLabels);
		}	
		return lblSemiejeMenor;
	}
	public JLabel getLblDefinir() {
		if(lblDefinir==null){
			lblDefinir=new JLabel();
			lblDefinir.setText(PluginServices.getText(this,"Dat_DefinirPor"));
			Dimension d=new Dimension(200,15);
			lblDefinir.setMinimumSize(d);
			lblDefinir.setPreferredSize(d);
		}
		return lblDefinir;
	}
	
	
	public JRadioButton getRbA_B() {
		if(rbA_B==null){
			rbA_B=new JRadioButton();
			rbA_B.setText("a, b");
			rbA_B.addActionListener(this);
		}
		return rbA_B;
	}
	public JRadioButton getRbA_Inv() {
		if(rbA_Inv==null){
			rbA_Inv=new JRadioButton();
			rbA_Inv.setText("a, inv_f");
			Dimension d=new Dimension(100,15);
			rbA_Inv.setMinimumSize(d);
			rbA_Inv.setPreferredSize(d);
			rbA_Inv.addActionListener(this);
		}
		return rbA_Inv;
	}
	
	/*
	public JRadioButton getRbInvF() {
		if(rbInvF==null){
			rbInvF=new JRadioButton();
			rbInvF.setToolTipText(PluginServices.getText(this,"Dat_InvFToolTipText"));
			rbInvF.setText(PluginServices.getText(this,"Dat_InvF"));
			rbInvF.addActionListener(this);
		}
		return rbInvF;
	}
	*/
	/**
	 * Inicializa el radio button Semieje Mayor del elipsoide
	 * @return
	 */
	/*
	public JRadioButton getRbSemMay() {
		if(rbSemMay==null){
			rbSemMay=new JRadioButton();
			rbSemMay.setToolTipText(PluginServices.getText(this,"Dat_SemMayToolTipText"));
			rbSemMay.setText(PluginServices.getText(this,"Dat_SemMay"));
			rbSemMay.addActionListener(this);
		}
		return rbSemMay;
	}*/

	/**
	 * Inicializa el radio Button del Semieje Menor del elipsoide
	 * @return
	 */
	/*
	public JRadioButton getRbSemMen() {
		if(rbSemMen==null){
			rbSemMen=new JRadioButton();
			rbSemMen.setToolTipText(PluginServices.getText(this,"Dat_SemMenToolTipText"));			
			rbSemMen.setText(PluginServices.getText(this,"Dat_SemMen"));
			rbSemMen.addActionListener(this);
		}
		return rbSemMen;
	}
	*/

	/**
	 * Agrupa los Radio Button de los parmetros del elipsoide
	 * 
	 */

	private void agruparRadioButtons() {
		if(groupRadioButton==null){
			groupRadioButton=new ButtonGroup();
			//Agrupar los radio Buttons
			groupRadioButton.add(getRbA_B());
			groupRadioButton.add(getRbA_Inv());
			getRbA_Inv().setSelected(true);
			getTxtSemMen().setEditable(false);
			getCbSemMen().setEnabled(false);
			
		}
	}

	/**
	 * Inicializa el cuadro de texto que contiene el datum
	 * @return
	 */
	public JTextField getTxtDatum() {
		if (txtDatum==null){
			txtDatum=new JTextField();
			bigSize.height=txtDatum.getPreferredSize().height;
			txtDatum.setPreferredSize(bigSize);
			txtDatum.addActionListener(this);
		}
		return txtDatum;
	}
	/**
	 * Inicializa el cuadro de texto que contiene el elipsoide
	 * @return
	 */
	public JTextField getTxtElipsoide() {
		if (txtElipsoide==null){
			txtElipsoide=new JTextField();
			bigSize.height=txtElipsoide.getPreferredSize().height;
			txtElipsoide.setPreferredSize(bigSize);
			txtElipsoide.addActionListener(this);
		}
		return txtElipsoide;
	}
	/**
	 * Inicializa el cuadro de texto que contiene el Inverse Flat del Elipsoide
	 * @return
	 */
	public JTextField getTxtInvF() {
		if (txtInvF==null){
			txtInvF=new JTextField();
			smallSize.height=txtInvF.getPreferredSize().height;
			txtInvF.setPreferredSize(smallSize);
			txtInvF.setMinimumSize(smallSize);
			txtInvF.addActionListener(this);
			txtInvF.addKeyListener(this);
			txtInvF.addFocusListener(this);
		}
		return txtInvF;
	}
	/**
	 * Inicializa el cuadro de texto que contiene el dato de longitud del
	 * meridiano
	 * @return
	 */
	public JTextField getTxtLong() {
		if (txtLong==null){
			txtLong=new JTextField();
			smallSize.height=txtLong.getPreferredSize().height;
			txtLong.setPreferredSize(smallSize);
			txtLong.addActionListener(this);
			txtLong.addKeyListener(this);
			txtLong.addFocusListener(this);
		}
		return txtLong;
	}
	/**
	 * Inicializa el cuadro de texto que contiene el meridiano
	 * @return
	 */
	public JTextField getTxtMeridian() {
		if (txtMeridian==null){
			txtMeridian=new JTextField();
			bigSize.height=txtMeridian.getPreferredSize().height;
			Dimension d=new Dimension(280,15);
			d.height=txtMeridian.getPreferredSize().height;
			txtMeridian.setPreferredSize(d);
			txtMeridian.addActionListener(this);
		}
		return txtMeridian;
	}
	/**
	 * Inicializa el cuadro de texto que contiene el Semieje Mayor
	 * del elipsoide
	 * @return
	 */
	public JTextField getTxtSemMay() {
		if (txtSemMay==null){
			txtSemMay=new JTextField();
			smallSize.height=txtSemMay.getPreferredSize().height;
			txtSemMay.setPreferredSize(smallSize);
			txtSemMay.setMinimumSize(smallSize);
			txtSemMay.addActionListener(this);
			txtSemMay.addKeyListener(this);
			txtSemMay.addFocusListener(this);
		}
		return txtSemMay;
	}
	/**
	 * Inicializa el cuadro de texto que contiene el semieje
	 * menor del elipsoide
	 * @return
	 */
	public JTextField getTxtSemMen() {
		if (txtSemMen==null){
			txtSemMen=new JTextField();
			smallSize.height=txtSemMen.getPreferredSize().height;
			txtSemMen.setPreferredSize(smallSize);
			txtSemMen.setMinimumSize(smallSize);
			txtSemMen.addActionListener(this);
			txtSemMen.addKeyListener(this);
			txtSemMen.addFocusListener(this);
		}
		return txtSemMen;
	}
	/**
	 * Inicializa el cuadro de texto que contiene el cdigo del Crs definido por el usuario
	 * @return
	 */
	public JTextField getTxtCodigoCrs() {
		if (txtCodigoCrs==null){
			txtCodigoCrs=new JTextField();
			smallSize.height=txtCodigoCrs.getPreferredSize().height;
			txtCodigoCrs.setPreferredSize(smallSize);
			txtCodigoCrs.addActionListener(this);
			txtCodigoCrs.addFocusListener(this);
		}
		return txtCodigoCrs;
	}
	/**
	 * Inicializa el cuadro de texto que contiene el nombre del Crs
	 * @return
	 */
	public JTextField getTxtNombreCrs() {
		if (txtNombreCrs==null){
			txtNombreCrs=new JTextField();
			smallSize.height=txtNombreCrs.getPreferredSize().height;
			txtNombreCrs.setPreferredSize(smallSize);
			txtNombreCrs.addActionListener(this);			
		}
		return txtNombreCrs;
	}
	/*
	 * Manejador de eventos de los controles
	 */
	public void actionPerformed(ActionEvent e) {
	
		if(e.getSource().equals(getTxtDatum())){
			System.out.println("txt Datum");
		}else if(e.getSource().equals(getTxtElipsoide())){
			System.out.println("txt Elipsoide");
		}else if(e.getSource().equals(getTxtInvF())){
			System.out.println("txt Inverse Flat");
		}else if(e.getSource().equals(getTxtSemMay())){
			System.out.println("txt Semieje Mayor");
		}else if(e.getSource().equals(getTxtSemMen())){
			System.out.println("txt Semieje Menor");
		}else if(e.getSource().equals(getTxtLong())){
			System.out.println("txt Longitud");
		}else if(e.getSource().equals(getTxtMeridian())){
			System.out.println("txt Meridiano");
		}else if(e.getSource().equals(getCbInvF())){
			System.out.println("combo box Inverse Flat");
		}else if(e.getSource().equals(getCbLong())){
			System.out.println("combo box Longitud");
		}else if(e.getSource().equals(getCbSemMay())){
			System.out.println("combo box Semieje Mayor");
		}else if(e.getSource().equals(getCbSemMen())){
			System.out.println("combo box Semieje Menor");
		}else if(e.getSource().equals(getRbA_B())){
			getTxtSemMay().setEditable(true);
			getTxtSemMen().setEditable(true);
			getTxtInvF().setEditable(false);
			getCbSemMen().setEnabled(true);
		}else if(e.getSource().equals(getRbA_Inv())){
			getTxtSemMay().setEditable(true);
			getTxtSemMen().setEditable(false);
			getTxtInvF().setEditable(true);	
			getCbSemMen().setEnabled(false);
		}else if(e.getSource().equals(getBtnImportDatum())){
			System.out.println("Importar Datum");
			ImportNewCrsDialog newCrs = new ImportNewCrsDialog(PluginServices.getText(this, "datum"));
       	 	PluginServices.getMDIManager().addWindow(newCrs);
       	 	if (newCrs.getCode() != -1) {
	       	 	setCrs(newCrs.getCode());
	       	 	fillDatum(getCrs());
       	 	}
		}else if(e.getSource().equals(getBtnImportElipsoide())){
			System.out.println("Importar Elipsoide");
			ImportNewCrsDialog newCrs = new ImportNewCrsDialog(PluginServices.getText(this, "ellips"));
       	 	PluginServices.getMDIManager().addWindow(newCrs);
       	 	if (newCrs.getCode() != -1) {
	       	 	setCrs(newCrs.getCode());
	       	 	fillEllipsoid(getCrs());
       	 	}
		}else if(e.getSource().equals(getBtnImportMeridian())){
			System.out.println("Importar Meridiano");
			ImportNewCrsDialog newCrs = new ImportNewCrsDialog(PluginServices.getText(this, "primem"));
       	 	PluginServices.getMDIManager().addWindow(newCrs);
       	 	if (newCrs.getCode() != -1) {
	       	 	setCrs(newCrs.getCode());
	       	 	fillPrimeMeridian(getCrs());
       	 	}else if(e.getSource().equals(getRbA_Inv())){
			
       	 	}else if(e.getSource().equals(getRbA_B())){
			
       	 	}
		}
	}
	
	public void fillData(ICrs crs){
		getTxtNombreCrs().setText(crs.getCrsWkt().getGeogcs());
		getTxtCodigoCrs().setText(getMaxCode());
		fillDatum(crs);
	}
	
	public void fillData(ICrs crs, boolean editing){
		getTxtNombreCrs().setText(crs.getCrsWkt().getGeogcs());
		getTxtCodigoCrs().setText(""+crs.getCode());
		fillDatum(crs);
	}
	
	public void fillDatum(ICrs crs) {
		getTxtDatum().setText(crs.getCrsWkt().getDatumName());
		fillEllipsoid(crs);
		fillPrimeMeridian(crs);
	}
	
	public void fillData(CoordinateReferenceSystem crsGT) {
		AbstractSingleCRS crs = (AbstractSingleCRS)crsGT;
		String authority = crs.getName().toString().split(":")[0];
		//String code = crs.getName().toString().split(":")[1];
		getTxtNombreCrs().setText(authority);
		getTxtCodigoCrs().setText(getMaxCode());
		DefaultGeodeticDatum d = (DefaultGeodeticDatum) crs.getDatum();
		String[] val = d.getName().toString().split(":");
		if (val.length<2)
			getTxtDatum().setText(d.getName().toString().split(":")[0]);
		else
			getTxtDatum().setText(d.getName().toString().split(":")[1]);
		DefaultEllipsoid ellips = (DefaultEllipsoid)d.getEllipsoid();
		
		Unit u = ellips.getAxisUnit();
		double semi_major = convert(ellips.getSemiMajorAxis(), u.toString());
		double inv_f = ellips.getInverseFlattening();
		val = ellips.getName().toString().split(":");
		if (val.length<2)
			getTxtElipsoide().setText(val[0]);
		else
			getTxtElipsoide().setText(val[1]);
		getTxtSemMay().setText(String.valueOf(semi_major));
		getTxtInvF().setText(String.valueOf(inv_f));
		getTxtSemMen().setText(String.valueOf(calcularSemMen()));
		
		DefaultPrimeMeridian primem = (DefaultPrimeMeridian)d.getPrimeMeridian();
		
		u = primem.getAngularUnit();
		double longitude = convert(primem.getGreenwichLongitude(), u.toString());
		val = primem.getName().toString().split(":");
		if (val.length<2)
			getTxtMeridian().setText(val[0]);
		else
			getTxtMeridian().setText(val[1]);
		
		getTxtLong().setText(""+longitude);		
	}
	
	public void fillEllipsoid(ICrs crs) {
		getTxtElipsoide().setText(crs.getCrsWkt().getSpheroid()[0]);
		String semMay = crs.getCrsWkt().getSpheroid()[1];
		String invF = crs.getCrsWkt().getSpheroid()[2];
		getTxtSemMay().setText(semMay);
		getTxtInvF().setText(invF);
		double semMen = calcularSemMen();
		getTxtSemMen().setText(String.valueOf(semMen));		
	}
	
	public void fillPrimeMeridian(ICrs crs) {
		getTxtMeridian().setText(crs.getCrsWkt().getPrimen()[0]);
		getTxtLong().setText(crs.getCrsWkt().getPrimen()[1]);
	}
	
	
	private double calcularSemMen() {
		String cadenaNumerica = getTxtSemMay().getText().replaceAll("[^0-9.E]", "");
				
		if (getTxtSemMay().getText().length() != cadenaNumerica.length() || notANumber(cadenaNumerica)) {
			JOptionPane.showMessageDialog(DefinirDatum.this, 
					PluginServices.getText(this,"numeric_format")+": "+PluginServices.getText(this,"semMay"), 
					"Warning...", JOptionPane.WARNING_MESSAGE);
			return 0;
		}
		else {
			if (getTxtInvF().getText().equals("Infinity")) {
				getRbA_B().setSelected(true);
				getTxtSemMay().setEditable(true);
				getTxtSemMen().setEditable(true);
				getTxtInvF().setEditable(false);
				getCbSemMen().setEnabled(true);
				return Double.parseDouble((String)getTxtSemMay().getText());
			}
			cadenaNumerica = getTxtInvF().getText().replaceAll("[^0-9.E-]", "");
			
			if (getTxtInvF().getText().length() != cadenaNumerica.length() || notANumber(cadenaNumerica)) {
		
			JOptionPane.showMessageDialog(DefinirDatum.this, 
					PluginServices.getText(this,"numeric_format")+": "+PluginServices.getText(this,"inv_f"), 
					"Warning...", JOptionPane.WARNING_MESSAGE);
			return 0;
			}
		}
		
		double semMay = Double.parseDouble((String)getTxtSemMay().getText());
		double invF = Double.parseDouble((String)getTxtInvF().getText());
		
		if (invF != 0)
			return semMay - (semMay / (invF));
		return Double.NEGATIVE_INFINITY;
	}
	
	private double calcularInvF() {
		String cadenaNumerica = getTxtSemMay().getText().replaceAll("[^0-9.E]", "");
		
		if (getTxtSemMay().getText().length() != cadenaNumerica.length() || notANumber(cadenaNumerica)) {
			JOptionPane.showMessageDialog(DefinirDatum.this, 
					PluginServices.getText(this,"numeric_format")+": "+PluginServices.getText(this,"semMay"), 
					"Warning...", JOptionPane.WARNING_MESSAGE);
			return 0;
		}
		else {
			cadenaNumerica = getTxtSemMen().getText().replaceAll("[^0-9.E-]", "");
			
			if (getTxtSemMen().getText().length() != cadenaNumerica.length() || notANumber(cadenaNumerica)) {
		
			JOptionPane.showMessageDialog(DefinirDatum.this, 
					PluginServices.getText(this,"numeric_format")+": "+PluginServices.getText(this,"semMen"), 
					"Warning...", JOptionPane.WARNING_MESSAGE);
			return 0;
			}
		}
		
		double semMay = Double.parseDouble((String)getTxtSemMay().getText());
		double semMen = Double.parseDouble((String)getTxtSemMen().getText());
		
		
		if ((semMay - semMen) != 0)
			return (semMay ) / (semMay - semMen);
		return Double.POSITIVE_INFINITY;
	}
	
	public void setCrs(int code) {
		try {
			crs = new CrsFactory().getCRS("EPSG:"+code);
		} catch (CrsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public ICrs getCrs() {
		return crs;
	}
	
	public void cleanData() {
		String max = getMaxCode();
		getTxtCodigoCrs().setText(max);
		getTxtNombreCrs().setText("");
		getTxtDatum().setText("");
		getTxtElipsoide().setText("GRS 1980");
		getTxtInvF().setText("298.257222101");
		getTxtLong().setText("0.0");
		getTxtMeridian().setText("Greenwich");
		getTxtSemMay().setText("6378137.0");
		double semMen = calcularSemMen();
		getTxtSemMen().setText(""+semMen);
	}
	
	private String getMaxCode() {
		String sentence = "SELECT usr_code " +
							"FROM USR " +	                              
							"ORDER BY usr_code ASC";
		EpsgConnection connect = new EpsgConnection();
		connect.setConnectionUsr();
		ResultSet result = Query.select(sentence,connect.getConnection());
		int max = 0;
		int lastValue = 0;
		int goodValue = 1;
		boolean firstAccess = true;
		try {
			while (result.next()) {
				max = result.getInt("usr_code");
				if (firstAccess && max != 1) {					
					return ""+goodValue;
				}
				firstAccess = false;
				if ((max - lastValue) == 1) {
					lastValue = max;
				} else {
					goodValue = lastValue+1;
					break;
				}
			}
			if (goodValue == 1) {
				goodValue = max+1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return ""+goodValue;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return ((value*factor_b)/factor_c);
	}
	
	/**
	 * Convierte una valor en las unidades indicadas a grados sexagesimales según los factores 
	 * de conversión obtenidos de la base de datos de la EPSG.
	 * 
	 * @param unit Unidad de entrada.
	 * @param value Valor a convertir
	 * @return Valor en grados sexagesimales.
	 */
	public double convert2Degree(String unit, double value) {
		//*************************************
		//Se realiza la conversión a radianes:
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		if(Math.abs(factor_b/factor_c-Math.PI/180.0)<1.0e-12)
			return value;
		value = ((value*factor_b)/factor_c);
		//**********************************************
		
		//Conversió de radianes a grados sexagesimales:
		//return ((value * 180.0) / 200.0);
		return ((value * 180.0) / Math.PI);
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
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return ((value*factor_c)/factor_b);
	}
	
	public void itemStateChanged(ItemEvent e) {
		if (e.getItemSelectable().equals(cbLong)) {
			String unit = getAngularUnit(((JComboBox)e.getSource()).getSelectedIndex());
			 double value = Double.valueOf(getTxtLong().getText()).doubleValue();
			 value = convertFromDegree(sourceUnitLong, unit, value);
			 sourceUnitLong = unit;
			 String val = String.valueOf(value);
			 getTxtLong().setText(val);
		}
		if (e.getItemSelectable().equals(cbSemMay) ) {
			String unit = getLengthUnit(((JComboBox)e.getSource()).getSelectedIndex());			 
			double value = Double.valueOf(getTxtSemMay().getText()).doubleValue();
			 value = convertFromMeters(sourceUnitSemMay, unit, value);
			 sourceUnitSemMay = unit;
			 String val = String.valueOf(value);
			 getTxtSemMay().setText(val);
		}
		if (e.getItemSelectable().equals(cbSemMen) ) {
			String unit = getLengthUnit(((JComboBox)e.getSource()).getSelectedIndex());
			 double value = Double.valueOf(getTxtSemMen().getText()).doubleValue();
			 value = convertFromMeters(sourceUnitSemMen, unit, value);
			 sourceUnitSemMen = unit;
			 String val = String.valueOf(value);
			 getTxtSemMen().setText(val);
		}		
	}
	
	private boolean notANumber(String cadenaNumerica) {
		int puntos = 0;
		int signos = 0;
		int letras = 0;
		for (int i = 0; i< cadenaNumerica.length(); i++) {
			if (cadenaNumerica.charAt(i) == '.')
				puntos++;
			else if (cadenaNumerica.charAt(i) == '-') {
				if (i==0) {
					signos++;
				}
				else if (i!=0 && cadenaNumerica.charAt(i-1) != 'E') {
					signos = 2;
				}
			}
			else if (cadenaNumerica.charAt(i) == 'E') {
				if (i== 0) {
					letras = 2;
				}
				else letras ++;
			}
		}
		
		if ((letras > 1) || (signos > 1) || (puntos > 1))
			return true;
		return false;
	}
	
	public double convertFromMeters(String unitSource, String unitTarget, double value) {
		double factor_b = 0;
		double factor_c = 0;
		String sentence = "SELECT factor_b, factor_c " +
			"FROM epsg_unitofmeasure " +	                              
			"WHERE unit_of_meas_name = '" +unitSource.replace("'", "'+char(39)+'") + "'";
		EpsgConnection connect = new EpsgConnection();
		connect.setConnectionEPSG();
		ResultSet result = Query.select(sentence,connect.getConnection());
		try {
			result.next();
			factor_b = result.getDouble("factor_b");
			factor_c = result.getDouble("factor_c");			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		value = ((value*factor_b)/factor_c);
		
		sentence = "SELECT factor_b, factor_c " +
		"FROM epsg_unitofmeasure " +	                              
		"WHERE unit_of_meas_name = '" +unitTarget.replace("'", "'+char(39)+'")+ "'";
		connect = new EpsgConnection();
		connect.setConnectionEPSG();
		result = Query.select(sentence,connect.getConnection());
		try {
			result.next();
			factor_b = result.getDouble("factor_b");
			factor_c = result.getDouble("factor_c");			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return ((value*factor_c)/factor_b);
	}
	
	public void keyPressed(KeyEvent e) {
		primera = true;
		if (e.getSource() == this.getTxtSemMay()) {			
			if (e.getKeyCode() == 10) {
				primera = false;
				if (getRbA_Inv().isSelected()) {
					getTxtSemMen().setText(String.valueOf(calcularSemMen()));
				}
				else if (getRbA_B().isSelected()) {
					getTxtInvF().setText(String.valueOf(calcularInvF()));					
				}
			}			
		}
		if (e.getSource() == this.getTxtSemMen()) {
			if (e.getKeyCode() == 10) {
				primera = false;
				getTxtInvF().setText(String.valueOf(calcularInvF()));				
			}			
		}
		if (e.getSource() == this.getTxtInvF()) {
			if (e.getKeyCode() == 10) {
				primera = false;
				getTxtSemMen().setText(String.valueOf(calcularSemMen()));				
			}			
		}
		if (e.getSource() == this.getTxtLong()) {
			if (e.getKeyCode() == 10) {
				primera = false;
				String cadenaNumerica = getTxtLong().getText().replaceAll("[^0-9.E]", "");
				
				if (getTxtLong().getText().length() != cadenaNumerica.length() || notANumber(cadenaNumerica)) {
					JOptionPane.showMessageDialog(DefinirDatum.this, 
							PluginServices.getText(this,"numeric_format")+": "+PluginServices.getText(this,"long"), 
							"Warning...", JOptionPane.WARNING_MESSAGE);
					primera = true;
					return;
				}
			}
		}
		primera = true;
	}
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void setLengthUnit(ArrayList units) {
		this.lengthUnits = units;
	}
	
	public void setAngularUnit(ArrayList units) {
		this.angularUnits = units;
	}
	
	public String getLengthUnit(int indice) {
		return (String)lengthUnits.get(indice);
	}
	
	public String getAngularUnit(int indice) {
		return (String)angularUnits.get(indice);
	}
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void focusLost(FocusEvent e) {
		if (primera) {
			primera = false;
			if (e.getSource() == this.getTxtSemMay()) {
				if (getRbA_Inv().isSelected()) {
					getTxtSemMen().setText(String.valueOf(calcularSemMen()));
				}
				else if (getRbA_B().isSelected()) {
					getTxtInvF().setText(String.valueOf(calcularInvF()));					
				}
							
			}
			if (e.getSource() == this.getTxtSemMen()) {
				getTxtInvF().setText(String.valueOf(calcularInvF()));						
			}
			if (e.getSource() == this.getTxtInvF()) {
				getTxtSemMen().setText(String.valueOf(calcularSemMen()));			
			}
			if (e.getSource() == this.getTxtLong()) {
				String cadenaNumerica = getTxtLong().getText().replaceAll("[^0-9.E]", "");
				
				if (getTxtLong().getText().length() != cadenaNumerica.length() || notANumber(cadenaNumerica)) {
					JOptionPane.showMessageDialog(DefinirDatum.this, 
							PluginServices.getText(this,"numeric_format")+": "+PluginServices.getText(this,"long"), 
							"Warning...", JOptionPane.WARNING_MESSAGE);
					return;
				}
			}
		}
	}
	
	class ComboBoxTootipRenderer extends BasicComboBoxRenderer {
		
		private String[] tooltips = null;
		
	    public ComboBoxTootipRenderer(String[] tooltips) {
	    	super();
	    	this.tooltips = tooltips;
		}

		public Component getListCellRendererComponent(JList list, Object value,
	        int index, boolean isSelected, boolean cellHasFocus) {
	      if (isSelected) {
	        setBackground(list.getSelectionBackground());
	        setForeground(list.getSelectionForeground());
	        if (-1 < index) {
	          list.setToolTipText(tooltips[index]);
	        }
	      } else {
	        setBackground(list.getBackground());
	        setForeground(list.getForeground());
	      }
	      setFont(list.getFont());
	      setText((value == null) ? "" : value.toString());
	      return this;
	    }
	  }
		
}
