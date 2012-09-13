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
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.gvsig.crs.CrsFactory;
import org.gvsig.crs.CrsWkt;
import org.gvsig.crs.ICrs;
import org.gvsig.crs.persistence.RecentTrsPersistence;
import org.gvsig.crs.persistence.TrData;

import com.iver.andami.PluginServices;

/**
 * Clase que define el panel de tranformaciones manuales, as como su
 * funcionamiento.
 * 
 * @author Jos Luis Gmez Martnez (jolugomar@gmail.com)
 * @author Luisa Marina Fernndez (luisam.fernandez@uclm.es)
 *
 */
public class TransformationManualPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private static boolean pressed = true;
	
	private JLabel x_Translation;
	private JLabel y_Translation;
	private JLabel z_Translation;
	private JLabel x_Rotation;
	private JLabel y_Rotation;
	private JLabel z_Rotation;
	private JLabel scale;
	
	private JTextField tx_Translation;
	private JTextField ty_Translation;
	private JTextField tz_Translation;
	private JTextField tx_Rotation;
	private JTextField ty_Rotation;
	private JTextField tz_Rotation;
	private JTextField tScale;
	
	private JLabel domainTranslation;
	private JLabel domainRotation;
	private JLabel domainScale;
	
	//private JLabel wkt;
	private JTextArea info;	
	int codeEpsg;
	String targetAbrev = "";
	String sourceAbrev = "";
	String[] targetAuthority;
	
	private String cadWKT = "";	
	
	public TransformationManualPanel() {		
		initialize();	
	}
	
	private void initialize(){
		JPanel p=new JPanel();
		p.setLayout(new GridLayout(7,3,10,10));
		
		p.add(getX_Translation());
		p.add(getTx_Translation());
		p.add(getDomainTranslation());
			
		p.add(getY_Translation());
		p.add(getTy_Translation());
		p.add(getDomainTranslation());
			
		p.add(getZ_Translation());
		p.add(getTz_Translation());
		p.add(getDomainTranslation());
			
		p.add(getX_Rotation());
		p.add(getTx_Rotation());
		p.add(getDomainRotation());
			
		p.add(getY_Rotation());
		p.add(getTy_Rotation());
		p.add(getDomainRotation());
			
		p.add(getZ_Rotation());
		p.add(getTz_Rotation());
		p.add(getDomainRotation());
			
		p.add(getScale());
		p.add(getTscale());
		p.add(getDomainScale());
		p.setBorder(new EmptyBorder(50,20,50,10));
		this.setLayout(new BorderLayout(1,30));
		this.setBorder(
			    BorderFactory.createCompoundBorder(
						BorderFactory.createCompoundBorder(
								BorderFactory.createTitledBorder(PluginServices.getText(this,"transformacion_manual")),
								BorderFactory.createEmptyBorder(2,2,2,2)),
								getBorder()));
		this.add(p,BorderLayout.CENTER);
	}
	
	private JLabel getDomainTranslation(){
		domainTranslation = new JLabel();
		domainTranslation.setText("[-1000.0, 1000.0]");
		return domainTranslation;
	}
	
	private JLabel getDomainRotation(){
		domainRotation = new JLabel();
		domainRotation.setText("[-60.0, 60.0]");
		return domainRotation;
	}
	
	private JLabel getDomainScale(){
		domainScale = new JLabel();
		domainScale.setText("[-20.0, 20.0]");
		return domainScale;
	}

	private JLabel getX_Translation() {
		if(x_Translation == null ) {
			x_Translation = new JLabel();
			//x_Translation.setFont(new Font("x_Translation:",Font.BOLD,15));
			x_Translation.setText(PluginServices.getText(this,"x_Translation")+":");	
		}
		return x_Translation;
	}
	
	private JLabel getY_Translation() {
		if(y_Translation == null ) {
			y_Translation = new JLabel();
			//y_Translation.setFont(new Font("y_Translation:",Font.BOLD,15));
			y_Translation.setText(PluginServices.getText(this,"y_Translation")+":");
		}
		return y_Translation;
	}
	
	private JLabel getZ_Translation() {
		if(z_Translation == null ) {
			z_Translation = new JLabel();
			//z_Translation.setFont(new Font("z_Translation:",Font.BOLD,15));
			z_Translation.setText(PluginServices.getText(this,"z_Translation")+":");
			
		}
		return z_Translation;
	}
	
	private JLabel getX_Rotation() {
		if(x_Rotation == null ) {
			x_Rotation = new JLabel();
			//x_Rotation.setFont(new Font("x_Rotation:",Font.BOLD,15));
			x_Rotation.setText(PluginServices.getText(this,"x_Rotation")+":");
			
		}
		return x_Rotation;
	}
	
	private JLabel getY_Rotation() {
		if(y_Rotation == null ) {
			y_Rotation = new JLabel();
			//y_Rotation.setFont(new Font("x_Rotation:",Font.BOLD,15));
			y_Rotation.setText(PluginServices.getText(this,"y_Rotation")+":");
			
		}
		return y_Rotation;
	}
	
	private JLabel getZ_Rotation() {
		if(z_Rotation == null ) {
			z_Rotation = new JLabel();
			//z_Rotation.setFont(new Font("x_Translation:",Font.BOLD,15));
			z_Rotation.setText(PluginServices.getText(this,"z_Rotation")+":");
			
		}
		return z_Rotation;
	}
	
	private JLabel getScale() {
		if(scale == null ) {
			scale = new JLabel();
			//scale.setFont(new Font("scale:",Font.BOLD,15));
			scale.setText(PluginServices.getText(this,"scale")+":");
			
		}
		return scale;
	}
	
	public JTextField getTx_Translation() {
		if(tx_Translation == null ) {
			tx_Translation = new JTextField();
			//tx_Translation.setFont(new Font("",Font.ITALIC,10));
			tx_Translation.setText("0");
			tx_Translation.setEditable(true);
			//tx_Translation.addKeyListener(this);
		}
		return tx_Translation;
	}
	
	public JTextField getTy_Translation() {
		if(ty_Translation == null ) {
			ty_Translation = new JTextField();
			//ty_Translation.setFont(new Font("",Font.ITALIC,10));
			ty_Translation.setText("0");
			ty_Translation.setEditable(true);
			//ty_Translation.addKeyListener(this);
		}
		return ty_Translation;
	}
	
	public JTextField getTz_Translation() {
		if(tz_Translation == null ) {
			tz_Translation = new JTextField();
			//tz_Translation.setFont(new Font("",Font.ITALIC,10));
			tz_Translation.setText("0");
			tz_Translation.setEditable(true);
			//tz_Translation.addKeyListener(this);
		}
		return tz_Translation;
	}
	
	public JTextField getTx_Rotation() {
		if(tx_Rotation == null ) {
			tx_Rotation = new JTextField();
			//tx_Rotation.setFont(new Font("",Font.ITALIC,10));
			tx_Rotation.setText("0");
			tx_Rotation.setEditable(true);
			//tx_Rotation.addKeyListener(this);
		}
		return tx_Rotation;
	}
	
	public JTextField getTy_Rotation() {
		if(ty_Rotation == null ) {
			ty_Rotation = new JTextField();
			//ty_Rotation.setFont(new Font("",Font.ITALIC,10));
			ty_Rotation.setText("0");
			ty_Rotation.setEditable(true);
			//ty_Rotation.addKeyListener(this);
		}
		return ty_Rotation;
	}
	
	public JTextField getTz_Rotation() {
		if(tz_Rotation == null ) {
			tz_Rotation = new JTextField();
			//tz_Rotation.setFont(new Font("",Font.ITALIC,10));
			tz_Rotation.setText("0");
			tz_Rotation.setEditable(true);
			//tz_Rotation.addKeyListener(this);
		}
		return tz_Rotation;
	}
	
	public JTextField getTscale() {
		if(tScale == null ) {
			tScale = new JTextField();
			//tScale.setFont(new Font("",Font.ITALIC,10));
			tScale.setText("0");
			tScale.setEditable(true);
			//tScale.addKeyListener(this);
		}
		return tScale;
	}
			
	/**
	 * 
	 * @return
	 */
	public ICrs getProjection() {
		if(tx_Translation.getText().equals("")){
			tx_Translation.setText("0");
		}
		else if (ty_Translation.getText().equals("")){
			ty_Translation.setText("0");
		}
		else if (tz_Translation.getText().equals("")){
			tz_Translation.setText("0");
		}
		else if (tx_Rotation.getText().equals("")){
			tx_Rotation.setText("0");
		}
		else if (ty_Rotation.getText().equals("")){
			ty_Rotation.setText("0");
		}
		else if (tz_Rotation.getText().equals("")){
			tz_Rotation.setText("0");
		}
		else if (tScale.getText().equals("")){
			tScale.setText("0");
		}		
		String param = "+towgs84="+ tx_Translation.getText()+","+
					   ty_Translation.getText()+","+
					   tz_Translation.getText()+","+
					   tx_Rotation.getText()+","+
					   ty_Rotation.getText()+","+
					   tz_Rotation.getText()+","+
					   tScale.getText() + " ";
		
		try {
			String[] sourceAuthority = getSourceAbrev().split(":");
			//ICrs crs = new CrsFactory().getCRS(getCode(), getWKT(),param);
			ICrs crs = new CrsFactory().getCRS(sourceAuthority[0]+":"+getCode());
			crs.setTransformationParams(param,null);
			return crs;
		} catch (org.gvsig.crs.CrsException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param cod
	 */
	public void setCode(int cod){
		codeEpsg = cod;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getCode(){
		return codeEpsg;
	}
	
	/**
	 * 
	 * @param cad
	 */
	public void setWKT(String cad){
		cadWKT = cad;
		CrsWkt parser = new CrsWkt(cad);
		setSourceAbrev(parser.getAuthority()[0], parser.getAuthority()[1]);		
	}	
	
	/**
	 * 
	 * @param cad
	 */
	public void setWKT(ICrs crs){
		cadWKT = crs.getWKT();		
		setSourceAbrev(crs.getCrsWkt().getAuthority()[0], crs.getCrsWkt().getAuthority()[1]);		
	}
	
	/**
	 * 
	 * @return
	 */
	public String getWKT(){
		return cadWKT;
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean isPressed() { return pressed; }
	
	
	/**
	 * metodo para comprobar si los JTextfile estan escritos 
	 * correctamente los campos de la transformacion manual
	 */
	public boolean correctJTextField(){
		boolean correct = true;
		
		if ((tx_Translation.getText().length()==0) || (tx_Translation.getText().length()!=verify(tx_Translation.getText()).length())){
			tx_Translation.setText("0");
			tx_Translation.setBackground(new Color(255,204,204));
			correct = false;
		}
		else tx_Translation.setBackground(new Color(255,255,255));
		if ((ty_Translation.getText().length()==0) || (ty_Translation.getText().length()!=verify(ty_Translation.getText()).length())){
			ty_Translation.setText("0");
			ty_Translation.setBackground(new Color(255,204,204));
			correct = false;
		}
		else ty_Translation.setBackground(new Color(255,255,255));
		if ((tz_Translation.getText().length()==0) || (tz_Translation.getText().length()!=verify(tz_Translation.getText()).length())){
			tz_Translation.setText("0");
			tz_Translation.setBackground(new Color(255,204,204));
			correct = false;
		}
		else tz_Translation.setBackground(new Color(255,255,255));
		if ((tx_Rotation.getText().length() == 0) || (tx_Rotation.getText().length()!=verify(tx_Rotation.getText()).length())){
			tx_Rotation.setText("0");
			tx_Rotation.setBackground(new Color(255,204,204));
			correct = false;
		}
		else tx_Rotation.setBackground(new Color(255,255,255));
		if ((ty_Rotation.getText().length() == 0) || (ty_Rotation.getText().length()!=verify(ty_Rotation.getText()).length())){
			ty_Rotation.setText("0");
			ty_Rotation.setBackground(new Color(255,204,204));
			correct = false;
		}
		else ty_Rotation.setBackground(new Color(255,255,255));
		if ((tz_Rotation.getText().length() == 0) || (tz_Rotation.getText().length()!=verify(tz_Rotation.getText()).length())){
			tz_Rotation.setText("0");
			tz_Rotation.setBackground(new Color(255,204,204));
			correct = false;
		}
		else tz_Rotation.setBackground(new Color(255,255,255));
		if ((tScale.getText().length() == 0) || (tScale.getText().length()!=verify(tScale.getText()).length())){
			tScale.setText("0");
			tScale.setBackground(new Color(255,204,204));
			correct = false;
		}
		else tScale.setBackground(new Color(255,255,255));
		return correct;
	}
	
	/**
	 * Verifica que el valor insertado en el campo JTextField correspondiente
	 * se corresponde con un valor nmerico y de tipo double
	 * 
	 * @param cad
	 * @return
	 */
	private String verify(String cad) {
		String num_cad = "";
		int pto = 0;
		char[] nums = {'0','1','2','3','4','5','6','7','8','9'};
		if (cad.startsWith("-")) num_cad += "-";
		for (int i=0; i< cad.length(); i++){						
			if (cad.charAt(i) == '.'){				
				if (cad.startsWith(".") || cad.endsWith(".") || pto>0);
				else {
					pto++;
					num_cad += cad.charAt(i);
				}
			}			
			else
				for (int j=0; j< nums.length; j++)
					if (cad.charAt(i) == nums[j])
						num_cad += cad.charAt(i);				
		}		
		return num_cad;
	}
	
	/**
	 * Mira si los JTextField tienen dominios correctos
	 * @return
	 */
	public boolean correctDomain(){
		boolean correctDomain = true;
		double tx = Double.parseDouble(tx_Translation.getText());
		double ty = Double.parseDouble(ty_Translation.getText());
		double tz = Double.parseDouble(tz_Translation.getText());
		double rx = Double.parseDouble(tx_Rotation.getText());
		double ry = Double.parseDouble(ty_Rotation.getText());
		double rz = Double.parseDouble(tz_Rotation.getText());
		double sc = Double.parseDouble(tScale.getText());
		
		if (tx < -1000.0 || tx > 1000.0) {
			tx_Translation.setText("0");
			tx_Translation.setBackground(new Color(255,204,204));
			correctDomain = false;
		}
		else tx_Translation.setBackground(new Color(255,255,255));
		if (ty < -1000.0 || ty > 1000.0) {
			ty_Translation.setText("0");
			ty_Translation.setBackground(new Color(255,204,204));
			correctDomain = false;
		}
		else ty_Translation.setBackground(new Color(255,255,255));
		if (tz < -1000.0 || tz > 1000.0) {
			tz_Translation.setText("0");
			tz_Translation.setBackground(new Color(255,204,204));
			correctDomain = false;
		}
		else tz_Translation.setBackground(new Color(255,255,255));
		if (rx < -60.0 || rx > 60.0) {
			tx_Rotation.setText("0");
			tx_Rotation.setBackground(new Color(255,204,204));
			correctDomain = false;
		}
		else tx_Rotation.setBackground(new Color(255,255,255));
		if (ry < -60.0 || ry > 60.0) {
			ty_Rotation.setText("0");
			ty_Rotation.setBackground(new Color(255,204,204));
			correctDomain = false;
		}
		else ty_Rotation.setBackground(new Color(255,255,255));
		if (rz < -60.0 || rz > 60.0) {
			tz_Rotation.setText("0");
			tz_Rotation.setBackground(new Color(255,204,204));
			correctDomain = false;
		}
		else tz_Rotation.setBackground(new Color(255,255,255));
		if (sc < -20.0 || sc > 20.0) {
			tScale.setText("0");
			tScale.setBackground(new Color(255,204,204));
			correctDomain = false;
		}
		else tScale.setBackground(new Color(255,255,255));
		return correctDomain;
	}
	
	/**
	 * Consigue el estado actual de los parmetros insertados. En caso
	 * de que todos los parmetros sean 0, o haya parmetros sin valor
	 * deshabilitar el botn de aceptar
	 * @return
	 */
	public boolean getStatus() {
		if ((tx_Translation.getText().equals("0") || tx_Translation.getText().equals("")) &&
				(ty_Translation.getText().equals("0")  || ty_Translation.getText().equals(""))&&
				(tz_Translation.getText().equals("0")  || tz_Translation.getText().equals(""))&&
				(tx_Rotation.getText().equals("0")  || tx_Rotation.getText().equals(""))&&
				(ty_Rotation.getText().equals("0")  || ty_Rotation.getText().equals(""))&&
				(tz_Rotation.getText().equals("0")  || tz_Rotation.getText().equals(""))&&
				(tScale.getText().equals("0")  || tScale.getText().equals("")))
			return false;
		else if (tx_Translation.getText().equals("") ||
				(ty_Translation.getText().equals(""))||
				(tz_Translation.getText().equals(""))||
				(tx_Rotation.getText().equals(""))||
				(ty_Rotation.getText().equals(""))||
				(tz_Rotation.getText().equals(""))||
				(tScale.getText().equals("")))
			return false;
		return true;
	}
	
	public void setTargetAuthority(String[] authority){
		targetAuthority = authority;
		setTargetAbrev(targetAuthority[0], targetAuthority[1]);		
	}
	
	public void setTargetAbrev(String fuente, String codigo){
		targetAbrev = fuente + ":" + codigo;
	}
	
	public String getTargetAbrev() {
		return targetAbrev;
	}
	
	public void setSourceAbrev(String fuente, String codigo){
		sourceAbrev = fuente + ":" + codigo;
	}
	
	public String getSourceAbrev(){
		return sourceAbrev;
	}
	
	public String getValues(){
		return "["+tx_Translation.getText()+","+ty_Translation.getText()+
		","+tz_Translation.getText()+","+tx_Rotation.getText()+","+ty_Rotation.getText()+","
		+tz_Rotation.getText()+","+tScale.getText()+"]";
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
			if (details.equals(crsTrDataArray[iRow].getAuthority()+":"+crsTrDataArray[iRow].getCode()+" <--> "+crsTrDataArray[iRow].getDetails()) && crsTrDataArray[iRow].getAuthority().equals(PluginServices.getText(this, "USR"))) {
				String data = crsTrDataArray[iRow].getDetails();
				data = data.trim().substring(1, data.length()-1);
				String values[] = data.split(",");
				tx_Translation.setText(values[0]);
				ty_Translation.setText(values[1]);
				tz_Translation.setText(values[2]);
				tx_Rotation.setText(values[3]);
				ty_Rotation.setText(values[4]);
				tz_Rotation.setText(values[5]);
				tScale.setText(values[6]);				
				break;
			}
		}
	}
	
	/**
	 * metodo para eliminar la informacion previamente cargada
	 *
	 */
	public void resetData() {
		tx_Translation.setText("0");
		ty_Translation.setText("0");
		tz_Translation.setText("0");
		tx_Rotation.setText("0");
		ty_Rotation.setText("0");
		tz_Rotation.setText("0");
		tScale.setText("0");
	}

}
