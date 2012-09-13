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
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.geotools.referencing.wkt.Parser;
import org.gvsig.crs.Crs;
import org.gvsig.crs.CrsException;
import org.gvsig.crs.CrsFactory;
import org.gvsig.crs.ICrs;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;

import es.idr.teledeteccion.connection.EpsgConnection;
import es.idr.teledeteccion.connection.Query;


/**
 * 
 * @author Luisa Marina Fernndez (luisam.fernandez@uclm.es)
 * @author Jose Luis Gomez Martinez (joseluis.gomez@uclm.es)
 *
 */
public class MainPanel extends JPanel implements ActionListener, ChangeListener, IWindow {
	
	private static final long serialVersionUID = 1L;
	private JTabbedPane pCard;
	private JPanel pSouth;
	
	private JButton btnCancelar;
	private JButton btnSiguiente;
	private JButton btnAnterior;
	private JButton btnFinalizar;
	
	private DefCrsUsr pCrsUsr;
	private DefinirDatum pDatum;
	private DefSistCoordenadas pSistCoord;
	private ICrs currentCrs;
	private String cadWkt = "";
	private int newCrsCode = -1;
	private boolean edit = false;
	
	public MainPanel(ICrs crs) {
		super();
		setCrs(crs);
		this.setLayout(new BorderLayout());
		this.add(getPCard(),BorderLayout.CENTER);
		this.add(getPSouth(),BorderLayout.SOUTH);
		getPCrsUsr().getRbCadenaWkt().addActionListener(this);
		getPCrsUsr().getRbCrsExistente().addActionListener(this);
		getPCrsUsr().getRbNuevoCrs().addActionListener(this);
	}
	/**
	 * Inicilizar el botn Anterior
	 * @return
	 */
	public JButton getBtnAnterior() {
		if(btnAnterior==null){
			btnAnterior=new JButton();
			btnAnterior.setText(PluginServices.getText(this,"wz_anterior"));
			btnAnterior.addActionListener(this);
			
		}
		return btnAnterior;
	}
	/**
	 * Inicilizar el botn Cancelar
	 * @return
	 */
	public JButton getBtnCancelar() {
		if(btnCancelar==null){
			btnCancelar=new JButton();
			btnCancelar.setText(PluginServices.getText(this,"wz_cancel"));
			btnCancelar.addActionListener(this);
		}
		return btnCancelar;
	}
	/**
	 * Inicilizar el botn Finalizar
	 * @return
	 */
	public JButton getBtnFinalizar() {
		if(btnFinalizar==null){
			btnFinalizar=new JButton();
			btnFinalizar.setText(PluginServices.getText(this,"wz_fin"));
			btnFinalizar.addActionListener(this);
		}
		return btnFinalizar;
	}
	/**
	 * Inicilizar el botn Siguiente
	 * @return
	 */
	public JButton getBtnSiguiente() {
		if(btnSiguiente==null){
			btnSiguiente=new JButton();
			btnSiguiente.setText(PluginServices.getText(this,"wz_siguiente"));
			btnSiguiente.addActionListener(this);
		}
		return btnSiguiente;
	}
	/**
	 * Inicilizar el panel que maneja los subpaneles del asistente
	 * @return
	 */
	public JTabbedPane getPCard() {
		if(pCard==null){
			pCard = new JTabbedPane();			
			pCard.addTab(PluginServices.getText(this,"crs_usuario"),getPCrsUsr());
			pCard.addTab(PluginServices.getText(this,"Dat_nDatum"),getPDatum());
			pCard.addTab(PluginServices.getText(this,"SistCoor_titmarco"),getPSistCoord());
			//Por defecto se muestra la primera pestaa
			pCard.setSelectedIndex(0);
			pCard.addChangeListener(this);
		}
		return pCard;
	}
	/**
	 * Iniciliza el panel que contiene los botones del asistente
	 * @return
	 */
	public JPanel getPSouth() {
		if(pSouth==null){
			pSouth=new JPanel();
			pSouth.setLayout(new FlowLayout(FlowLayout.RIGHT,5,5));
			pSouth.add(getBtnCancelar());
			pSouth.add(getBtnAnterior());
			pSouth.add(getBtnSiguiente());
			pSouth.add(getBtnFinalizar());
			getBtnFinalizar().setVisible(false);
			getBtnAnterior().setVisible(false);
		}
		return pSouth;
	}
	public DefCrsUsr getPCrsUsr() {
		if(pCrsUsr==null){
			pCrsUsr=new DefCrsUsr(getCrs());
		}
		return pCrsUsr;
	}
	public DefinirDatum getPDatum() {
		if(pDatum==null){
			pDatum=new DefinirDatum();
		}
		return pDatum;
	}
	public DefSistCoordenadas getPSistCoord() {
		if(pSistCoord==null){
			pSistCoord=new DefSistCoordenadas();
		}
		return pSistCoord;
	}
	public void actionPerformed(ActionEvent e) {
	
		if(e.getSource().equals(getBtnAnterior())){

			//Establecer los botones del Wizard
			getBtnFinalizar().setVisible(false);
			getBtnSiguiente().setVisible(true);
			
			if (getPSistCoord().isShowing() && isEditing()) {
				getBtnAnterior().setVisible(false);
			}
			if(getPDatum().isShowing()){
				//Se va a mostrar el primer panel del Wizard
				getBtnAnterior().setVisible(false);
				if (getPCrsUsr().getRbCadenaWkt().isSelected()) {
					getBtnFinalizar().setVisible(true);
				}
				getPCard().setSelectedComponent(getPCrsUsr());
			}
			else if (getPSistCoord().isShowing()) {
				getPCard().setSelectedComponent(getPDatum());
			}
			
		}else if(e.getSource().equals(getBtnSiguiente())){
			if (getPCrsUsr().isShowing() && getPCrsUsr().getRbCrsExistente().isSelected() ){
				getBtnFinalizar().setVisible(false);
				ICrs crs = getPCrsUsr().getCrs();
				if (crs != null){
					fillData(crs);
				}
				else fillData(getCrs());
			}
			else if (getPCrsUsr().isShowing() && getPCrsUsr().getRbNuevoCrs().isSelected() ){
				getBtnFinalizar().setVisible(false);
				cleanData();
			}
			else if (getPCrsUsr().isShowing() && getPCrsUsr().getRbCadenaWkt().isSelected()) {
				if (getPCrsUsr().getTxtAreaWkt().getText().equals("")) {
					JOptionPane.showMessageDialog(MainPanel.this, 
							PluginServices.getText(this,"white_Textbox"), 
							"Warning...", JOptionPane.WARNING_MESSAGE);
					return;
				} else {
					CoordinateReferenceSystem crs = null;
					String wkt = getPCrsUsr().getTxtAreaWkt().getText();
					Parser parser = new Parser();
			        
					try {
						crs = parser.parseCoordinateReferenceSystem(wkt);
					} catch (ParseException e1) {
						//System.out.println("Cadena WKT no ha podido ser parseada");
						//e1.printStackTrace();
						JOptionPane.showMessageDialog(MainPanel.this, 
								PluginServices.getText(this, "problem_with_wkt_try_manually"), 
								"Warning...", JOptionPane.WARNING_MESSAGE);					
						return;					
					} catch (UnsupportedOperationException e2) {
						JOptionPane.showMessageDialog(MainPanel.this, 
								PluginServices.getText(this, "problem_with_wkt_try_manually"), 
								"Warning...", JOptionPane.WARNING_MESSAGE);					
						return;
					}
					
					fillData(crs);
				}
			}
			if (getPCrsUsr().isShowing())			
				getPCard().setSelectedComponent(getPDatum());
			else if (getPDatum().isShowing())
				getPCard().setSelectedComponent(getPSistCoord());
			
			getBtnAnterior().setVisible(true);
			//Si aparece el ultimo panel visualizar el botn finalizar
			if (getPDatum().isShowing()){
				getBtnFinalizar().setVisible(true);
				getBtnSiguiente().setVisible(false);
				getBtnAnterior().setVisible(true);
			}
		}else if(e.getSource().equals(getBtnFinalizar())){
			if (getPCrsUsr().getRbCadenaWkt().isSelected()) {
				CoordinateReferenceSystem crs = null;
				String wkt = getPCrsUsr().getTxtAreaWkt().getText();
				Parser parser = new Parser();
				if (getPCrsUsr().getTxtAreaWkt().getText().equals("")) {
						JOptionPane.showMessageDialog(MainPanel.this, 
								PluginServices.getText(this,"white_Textbox"), 
								"Warning...", JOptionPane.WARNING_MESSAGE);
						return;
				}
				try {
					crs = parser.parseCoordinateReferenceSystem(wkt);
				} catch (ParseException e1) {
					//System.out.println("Cadena WKT no ha podido ser parseada");
					//e1.printStackTrace();
					JOptionPane.showMessageDialog(MainPanel.this, 
							PluginServices.getText(this, "problem_with_wkt_try_manually"), 
							"Warning...", JOptionPane.WARNING_MESSAGE);					
					return;
				} catch (UnsupportedOperationException e2) {
					JOptionPane.showMessageDialog(MainPanel.this, 
							PluginServices.getText(this, "problem_with_wkt_try_manually"), 
							"Warning...", JOptionPane.WARNING_MESSAGE);					
					return;
				}
				
				fillData(crs);
			}
			
			//Realizar las acciones de fin del Wizard
			getDataAndUpdate();	
			
		}else if(e.getSource().equals(getBtnCancelar())){
			//Cerrar el asistente
			PluginServices.getMDIManager().closeWindow(this);
		} else if (e.getSource().equals(getPCrsUsr().getRbCadenaWkt())) {			
			getPCrsUsr().habilitarExistente(false);
			getPCrsUsr().habilitarWkt(true);
			getBtnFinalizar().setVisible(true);
		}
		else if(e.getSource().equals(getPCrsUsr().getRbCrsExistente())){
			 getPCrsUsr().habilitarExistente(true);
			 getPCrsUsr().habilitarWkt(false);
			 getBtnFinalizar().setVisible(false);
        }else  if(e.getSource().equals(getPCrsUsr().getRbNuevoCrs())){       	 
       	 	getPCrsUsr().habilitarExistente(false);
       	 	getPCrsUsr().habilitarWkt(false);
       	 	getBtnFinalizar().setVisible(false);
        }
	}
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo=new WindowInfo(WindowInfo.MODALDIALOG);
   		m_viewinfo.setTitle(PluginServices.getText(this,"wz_titulo"));
   		m_viewinfo.setWidth(560);
   		//m_viewinfo.setHeight(425);
   		m_viewinfo.setHeight(400);
		return m_viewinfo;
	}
	
	public ICrs getCrs () {
		return currentCrs;
	}
	
	public void setCrs (ICrs crs) {
		currentCrs = crs;
	}
	
	public void fillData(ICrs crs) {
		getPDatum().fillData(crs);
		getPSistCoord().fillData(crs);
	}
	
	public void fillData(ICrs crs, boolean editing) {
		getPDatum().fillData(crs, editing);
		getPSistCoord().fillData(crs);
	}
	
	public void cleanData() {
		getPDatum().cleanData();
		getPSistCoord().cleanData();
	}
	
	public void fillData(CoordinateReferenceSystem crs) {		
		getPDatum().fillData(crs);
		getPSistCoord().fillData(crs);
	}
	
	private void getDataAndUpdate() {
		EpsgConnection conn = new EpsgConnection();
		conn.setConnectionUsr();
		String codeCrs = "";
		String sentence;
		ResultSet result;
		
		if (getPDatum().getTxtSemMay().getText().equals("")) {
			JOptionPane.showMessageDialog(MainPanel.this, 
					PluginServices.getText(this,"white_Textbox")+": Semieje Mayor", 
					"Warning...", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		if (getPDatum().getTxtLong().getText().equals("")) {
			JOptionPane.showMessageDialog(MainPanel.this, 
					PluginServices.getText(this,"white_Textbox")+": Longitud", 
					"Warning...", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		if (getPDatum().getTxtCodigoCrs().getText().equals("")) {
			JOptionPane.showMessageDialog(MainPanel.this, 
					PluginServices.getText(this,"white_Textbox")+": Codigo CRS", 
					"Warning...", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		String cadenaNumerica = getPDatum().getTxtSemMay().getText().replaceAll("[^0-9.E-]", "");
						
		if (getPDatum().getTxtSemMay().getText().length() != cadenaNumerica.length() || notANumber(cadenaNumerica)) {
			JOptionPane.showMessageDialog(MainPanel.this, 
					PluginServices.getText(this,"numeric_format")+": Semieje Mayor", 
					"Warning...", JOptionPane.WARNING_MESSAGE);
			return;
		}		
		
		cadenaNumerica = getPDatum().getTxtLong().getText().replaceAll("[^0-9.E-]", "");
		
		if (getPDatum().getTxtLong().getText().length() != cadenaNumerica.length() || notANumber(cadenaNumerica)) {
		
			JOptionPane.showMessageDialog(MainPanel.this, 
					PluginServices.getText(this,"numeric_format")+": Longitud", 
					"Warning...", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		codeCrs = getPDatum().getTxtCodigoCrs().getText().replaceAll("[^0-9]", "");
		
		if (getPDatum().getTxtCodigoCrs().getText().length() != codeCrs.length()) {
			
			JOptionPane.showMessageDialog(MainPanel.this, 
					PluginServices.getText(this,"numeric_format")+": Codigo CRS", 
					"Warning...", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		double value = Double.valueOf(getPDatum().getTxtSemMay().getText()).doubleValue();
		//Comprobar si es numerico, o quedarnos con la parte numerica...
		//searchTextField.getText().length()!=searchTextField.getText().replaceAll("[^0-9]", "").length())
		String unit = getPDatum().getLengthUnit(getPDatum().getCbSemMay().getSelectedIndex());
		double semMay = getPDatum().convert2Meters(unit, value);
		value = Double.valueOf(getPDatum().getTxtLong().getText()).doubleValue();
		unit = getPDatum().getAngularUnit(getPDatum().getCbLong().getSelectedIndex());
		double longitude = getPDatum().convert2Degree(unit, value);
		String spheroidName = getPDatum().getTxtElipsoide().getText();
		if (spheroidName.equals(""))
			spheroidName = PluginServices.getText(this, "no_name");
		String[] spheroid = {spheroidName,
							""+semMay,getPDatum().getTxtInvF().getText()};
		String meridianName = getPDatum().getTxtMeridian().getText();
		if (meridianName.equals(""))
			meridianName = PluginServices.getText(this, "no_name");
		String[] primem = {meridianName, ""+longitude};
		
		String[] authority = {"\"USR\"", codeCrs};
		if (!isEditing()){
			sentence = "SELECT usr_code FROM USR WHERE usr_code = " +authority[1];
			result = Query.select(sentence, conn.getConnection());
			try {
				if (result.next()) {
					JOptionPane.showMessageDialog(MainPanel.this, 
							PluginServices.getText(this,"crsRepeat")+": "+authority[1], 
							"Warning...", JOptionPane.WARNING_MESSAGE);
					return;
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}				
		}
		String datum = getPDatum().getTxtDatum().getText();
		if (datum.equals(""))
			datum = PluginServices.getText(this, "no_name");		
		
		if (getPSistCoord().getRbGeografico().isSelected()) {
			String name = getPDatum().getTxtNombreCrs().getText();
			if (name.equals(""))
				name = PluginServices.getText(this, "no_name");
			cadWkt = "GEOGCS[\"" + name + "\", DATUM[\""+ datum +
			"\", SPHEROID[\""+ spheroid[0] + "\", "+ spheroid[1] + ", "+ spheroid[2] +"]], " +
			"PRIMEM[\""+ primem[0] + "\", "+ primem[1] +"], UNIT[\"Degree\", " + (Math.PI/180) +
			"]]";
			if (!isEditing()){
				sentence = "INSERT INTO USR VALUES("+authority[1]+",'" +
							cadWkt +"','','"+name+"','"+datum+"')";
			} else { 
				sentence = "UPDATE USR SET usr_wkt='" +cadWkt +"'," +
						"usr_proj='',usr_geog='"+getPDatum().getTxtNombreCrs().getText()+"'," +
						"usr_datum='"+datum+"' " +
						"WHERE usr_code = " +authority[1];
			}
	
		} else {
			String name = getPDatum().getTxtNombreCrs().getText();
			if (name.equals(""))
				name = PluginServices.getText(this, "no_name");
			String projection = getPSistCoord().getTxtNombreProy().getText();
			if (projection.equals(""))
				projection = PluginServices.getText(this, "no_name");
			cadWkt = "PROJCS[\""+projection+
				"\", GEOGCS[\"" + name + "\", DATUM[\""+ datum +
				"\", SPHEROID[\""+ spheroid[0] + "\", "+ spheroid[1] + ", "+ spheroid[2] +"]], " +
				"PRIMEM[\""+ primem[0] + "\", "+ primem[1] +"], UNIT[\"Degree\", " + (Math.PI/180) +
				"]], PROJECTION[\""+ getPSistCoord().getProjection(getPSistCoord().getCbProyeccion().getSelectedIndex()) + "\"], ";
				
			
			ArrayList maxValues = null;
			ArrayList minValues = null;
			int paramPos = 0;
			try {
				maxValues = getPSistCoord().getProj4().getProj4ProjectionParameterMaxValues(getPSistCoord().getPos());
				minValues = getPSistCoord().getProj4().getProj4ProjectionParameterMinValues(getPSistCoord().getPos());
				
			} catch (CrsException e) {
				e.printStackTrace();
			}
			/*
			 * falta la parte de los parámetros... metodo para nombres...
			 */		
			int j=0;
			for (int i= 0; i< getPSistCoord().getTableParametros().getRowCount();i++){
				if (((String)(getPSistCoord().getTableParametros().getValueAt(i,1))).equals("")) {
					getPSistCoord().getTableParametros().setValueAt("0",i,1);
				}
				cadenaNumerica = ((String)(getPSistCoord().getTableParametros().getValueAt(i,1))).replaceAll("[^0-9.E-]", "");
				
				if (((String)(getPSistCoord().getTableParametros().getValueAt(i,1))).length() != cadenaNumerica.length() || notANumber(cadenaNumerica)) {
			
					JOptionPane.showMessageDialog(MainPanel.this, 
							PluginServices.getText(this,"numeric_format")+": Parametro "+(String)(getPSistCoord().getTableParametros().getValueAt(i,0)), 
							"Warning...", JOptionPane.WARNING_MESSAGE);
					return;
				}
				String condition = (String)getPSistCoord().getTableParametros().getValueAt(i, 0);
				String param = getPSistCoord().getTrueParametersNames(j);
				paramPos = findPositionParameter(param);
				if (paramPos != -1) {
					value = Double.parseDouble(cadenaNumerica);
					if ((PluginServices.getText(this, condition).trim().equals("semi_major") || (PluginServices.getText(this, condition).trim().equals("semi_minor")))) {
						continue;
					}
					else if (!(param.trim().equals("semi_major") || param.trim().equals("semi_minor"))) {
						double maxValue = Double.parseDouble((String)maxValues.get(paramPos));
						double minValue = Double.parseDouble((String) minValues.get(paramPos));
						if (value > maxValue || value < minValue) {
							JOptionPane.showMessageDialog(MainPanel.this, 
									PluginServices.getText(this,"incorrect_domain")+": Parametro "+(String)(getPSistCoord().getTableParametros().getValueAt(i,0)), 
									"Warning...", JOptionPane.WARNING_MESSAGE);
							return;
						}
						j++;
						//paramPos ++;
					}
									
					value = 0;
					unit = "";
					/**
					 * Esto está hecho en general, habrá que hacerlo dependiendo del tipo de
					 * unidad que tenga el parametro
					 */
					String type = (String) getPSistCoord().getTableParametros().getValueAt(i, 2);
					value = Double.parseDouble((String)getPSistCoord().getTableParametros().getValueAt(i, 1));
					/*if (type.equals("Meters")) {
						value = Double.parseDouble((String)getPSistCoord().getTableParametros().getValueAt(i, 1));
						unit = (String)getPSistCoord().getCbUnits().getSelectedItem();
						value = getPSistCoord().convert2Meters(unit, value);
					}
					else if (type.equals("Degree")) {
						value = Double.parseDouble((String)getPSistCoord().getTableParametros().getValueAt(i, 1));
						unit = (String)getPSistCoord().getCbUnits().getSelectedItem();
						value = getPSistCoord().convert2Degree(unit, value);
					}
					else if (type.equals("Unitless")) {
						value = Double.parseDouble((String)getPSistCoord().getTableParametros().getValueAt(i, 1));
						unit = (String)getPSistCoord().getCbUnits().getSelectedItem();
						value = getPSistCoord().convert2Unitless(unit, value);
					}*/
					
					cadWkt += "PARAMETER[\""+getPSistCoord().getTrueParametersNames(i)+"\", " + 
								value+ "], ";				
				} else {
					j++;
				}
			}
			
			cadWkt += "UNIT[\"Meters\", 1.0]]";
			
			if (!isEditing()) {
				sentence = "INSERT INTO USR VALUES("+authority[1]+",'" +
				cadWkt +"','"+projection+"','"+name+"','"+datum+"')";
			}
			else { 
				sentence = "UPDATE USR SET usr_wkt='" + cadWkt +"'," +
						"usr_proj='"+projection+"'," +
						"usr_geog='"+name+"'," +
						"usr_datum='"+datum+"' " +
						"WHERE usr_code = " +authority[1];
			}
		}		
		try {
			conn.update(sentence);
			conn.shutdown();
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		setNewCrsCode(Integer.parseInt(authority[1]));
		try {
			ICrs crs = new CrsFactory().getCRS("USR:"+authority[1]);			
			crs.getProj4String();
		} catch (CrsException e1) {
			JOptionPane.showMessageDialog(this, PluginServices.getText(this,e1.getMessage()), "Warning...",
					JOptionPane.WARNING_MESSAGE);
			conn.setConnectionUsr();			
			sentence = "DELETE FROM USR WHERE usr_code =" + authority[1];
			result = Query.select(sentence,conn.getConnection());	
			try {
				conn.shutdown();
			} catch (SQLException arg0) {
				// TODO Auto-generated catch block
				arg0.printStackTrace();
			}
			return;
		}
		PluginServices.getMDIManager().closeWindow(this);
	}
	
	private int findPositionParameter(String param) {
		ArrayList parameters = new ArrayList();
		try {
			parameters = getPSistCoord().getProj4().getProj4ProjectionParameters(getPSistCoord().getPos());
			int pos = -1;
			int i = 0;
			do {
				pos = getPSistCoord().getProj4().findProjectionParameters(param, parameters.get(i).toString());
				i++;
				if (i==parameters.size()) break;
			} while (pos == -1);
			if (pos!=-1)
				param = getPSistCoord().getProj4().getProj4ProjectionParameterName(pos);			
		} catch (CrsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i=0; i< parameters.size(); i++) {
			String actualParam = ((String)parameters.get(i)).trim();
			if (actualParam.equals(param)) return i;
		}
		return -1;
	}
	
	public void setNewCrsCode(int code) {
		newCrsCode = code;
	}
	
	public int getNewCrsCode() {
		return newCrsCode;
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
	
	public void setEditing(boolean edit) {
		this.edit = edit;
	}
	
	public boolean isEditing() {
		return this.edit;
	}
	
	public void setEditingPanel() {
		pCard.setSelectedComponent(getPDatum());
		pCard.setEnabledAt(0, false);
		fillData(getCrs(), true);
		getPDatum().getTxtCodigoCrs().setEnabled(false);
		
	}
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == getPCard()) {
			if (getPCrsUsr().getHasChanged()) {
				if (getPCrsUsr().getRbCrsExistente().isSelected()) {
					ICrs crs = getPCrsUsr().getCrs();
					if (crs != null) fillData(crs);					
					else fillData(getCrs());
					getPCrsUsr().setHasChange(false);
				}
				else if (getPCrsUsr().getRbNuevoCrs().isSelected()) {
					cleanData();
					getPCrsUsr().setHasChange(false);
				}
				else if (getPCrsUsr().getRbCadenaWkt().isSelected()) {
					cleanData();
					if (getPCrsUsr().getTxtAreaWkt().getText().equals("")) {
						JOptionPane.showMessageDialog(MainPanel.this, 
								PluginServices.getText(this,"white_Textbox"), 
								"Warning...", JOptionPane.WARNING_MESSAGE);
						getPCrsUsr().setHasChange(false);
						getPCard().setSelectedComponent(getPCrsUsr());
						return;
					} else {
						CoordinateReferenceSystem crs = null;
						String wkt = getPCrsUsr().getTxtAreaWkt().getText();
						Parser parser = new Parser();
				        Crs crs2 = null;
						try {
							crs = parser.parseCoordinateReferenceSystem(wkt);
						} catch (ParseException e1) {
							//System.out.println("Cadena WKT no ha podido ser parseada");
							//e1.printStackTrace();
							JOptionPane.showMessageDialog(MainPanel.this, 
									PluginServices.getText(this, "problem_with_wkt_try_manually"), 
									"Warning...", JOptionPane.WARNING_MESSAGE);					
							getPCrsUsr().setHasChange(false);
							getPCard().setSelectedComponent(getPCrsUsr());
							return;
						} catch (UnsupportedOperationException e2) {
							JOptionPane.showMessageDialog(MainPanel.this, 
									PluginServices.getText(this, "problem_with_wkt_try_manually"), 
									"Warning...", JOptionPane.WARNING_MESSAGE);					
							getPCrsUsr().setHasChange(false);
							getPCard().setSelectedComponent(getPCrsUsr());
							return;
						}						
						fillData(crs);
					}
					getPCrsUsr().setHasChange(false);
				}
			}
			int i = ((JTabbedPane)e.getSource()).getSelectedIndex();
			if (i==0){
				getBtnAnterior().setVisible(false);
				getBtnSiguiente().setVisible(true);
				if (getPCrsUsr().getRbCadenaWkt().isSelected()){
					getPCrsUsr().setHasChange(true);
					getBtnFinalizar().setVisible(true);
				}
				else
					getBtnFinalizar().setVisible(false);
			}
			else if (i==1 && !isEditing()) {
				getBtnAnterior().setVisible(true);
				getBtnSiguiente().setVisible(true);
				getBtnFinalizar().setVisible(false);
				if (getPCrsUsr().getRbCadenaWkt().isSelected()){
					//getPCrsUsr().setHasChange(true);
					getBtnFinalizar().setVisible(true);
				}
			}
			else if (i==1 && isEditing()) {
				getBtnAnterior().setVisible(false);
				getBtnSiguiente().setVisible(true);
				getBtnFinalizar().setVisible(false);
			}
			else if (i == 2) {				
				getBtnAnterior().setVisible(true);
				getBtnSiguiente().setVisible(false);
				getBtnFinalizar().setVisible(true);				
			}
		}
		
	}
	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}
}
