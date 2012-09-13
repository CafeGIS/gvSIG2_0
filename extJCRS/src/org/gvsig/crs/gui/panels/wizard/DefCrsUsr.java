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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.gvsig.crs.CrsException;
import org.gvsig.crs.CrsFactory;
import org.gvsig.crs.ICrs;
import org.gvsig.crs.gui.dialog.ImportNewCrsDialog;

import com.iver.andami.PluginServices;


/**
 * Panel de definicin del Crs por el usuario
 * 
 *  @author Luisa Marina Fernandez Ruiz (luisam.fernandez@uclm.es)
 *  @author Jose Luis Gomez Martinez (joseluis.gomez@uclm.es)
 *
 */
public class DefCrsUsr extends JPanel implements ActionListener{
	private JPanel panel;
	//jbuton definido por gvsig
	private JButton btnCrsExistente;
	private JLabel lblCrs;
	private JRadioButton rbCrsExistente;
	private JRadioButton rbNuevoCrs;
	private JRadioButton rbCadenaWkt;
	private ButtonGroup crsGroup;
	private JTextArea txtAreaWkt;
	private JButton btnImportarWkt;
	private JScrollPane scrollWkt;
	private ICrs currentCrs;
	private int width=380;
	//290
	
	private static final long serialVersionUID = 1L;
	
	ICrs crs = null;
	boolean hasChanged = false;

	public DefCrsUsr(ICrs crs) {
		super();
		currentCrs = crs;
		initialize();
		
	}

	/*
	 * Inicializa el panel de Definicin del Crs por el usuario
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.add(getPanel(),BorderLayout.CENTER);
		habilitarWkt(false);
			
	}
	
	/*
	 * Agrega todos los elementos
	 */
	public JPanel getPanel() {
		if(panel==null){
			panel=new JPanel();
			panel.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			c.weighty=0.1;
			//c.ipady=100;
			//establece el espacio superior, inferior, izquierdo y derecho
			//entre cada componente
			c.insets=new Insets(8,8,8,8);
			//c.ipady =60;      //make this component tall
			c.weightx = 0.75;
			c.gridx = 0;
			c.gridy = 1;
			panel.add(getRbCrsExistente(),c);
			c.weightx = 0.25;
			c.gridx = 1;
			c.gridy = 1;
			panel.add(getLblCrs(),c);
			
			c.fill = GridBagConstraints.EAST;
			c.weightx=0.0;
			c.gridx = 2;
			c.gridy = 1;
			panel.add(getBtnCrsExistente(),c);
			
			c.fill = GridBagConstraints.BOTH;
			c.weightx = 0.75;
			c.gridx = 0;
			c.gridy = 0;
			panel.add(getRbNuevoCrs(),c);
	
			c.gridx = 0;
			c.gridy = 2;
			panel.add(getRbCadenaWkt(),c);
//			Agrupa los botones de opcion
			agruparRadioButtons();
			//Seleccin por defecto
			getRbNuevoCrs().setSelected(true);
			
			c.gridx=2;
		    c.gridy=2;
		    c.weightx=0.0;
		    c.weighty=0.0;
		    c.fill = GridBagConstraints.NONE;
		    //panel.add(getBtnImportarWkt(),c);
		    		
			//vertical
			c.fill=GridBagConstraints.VERTICAL;
			c.anchor=GridBagConstraints.EAST;
			c.gridx = 0;
			c.gridy = 4;
			c.gridwidth=3;
			c.weighty=0.75;
			panel.add(getScrollWkt(),c);
			
			
		}
		return panel;
	}
	
	/*
	 * Agrupa los botones de opcin
	 */
	private void agruparRadioButtons() {
		if (crsGroup==null){
				crsGroup=new ButtonGroup();
				//Agrupar los botones de opcion
				crsGroup.add(getRbCrsExistente());
				crsGroup.add(getRbNuevoCrs());
				crsGroup.add(getRbCadenaWkt());
			}
			
	}

	public JRadioButton getRbCadenaWkt() {
		if (rbCadenaWkt==null){
			rbCadenaWkt=new JRadioButton(PluginServices.getText(this,"DefCrsUsr_wkt"));
			rbCadenaWkt.addActionListener(this);
			//rbCadenaWkt.setEnabled(false);
		}
		return rbCadenaWkt;
	}

	public JRadioButton getRbCrsExistente() {
		if (rbCrsExistente==null){
			rbCrsExistente=new JRadioButton(PluginServices.getText(this,"DefCrsUsr_existente"));
			rbCrsExistente.addActionListener(this);
		}
		return rbCrsExistente;
	}
	
	public JRadioButton getRbNuevoCrs() {
		if (rbNuevoCrs==null){
			rbNuevoCrs=new JRadioButton(PluginServices.getText(this,"DefCrsUsr_nuevo"));
			rbNuevoCrs.addActionListener(this);
		}
		return rbNuevoCrs;
	}


	public JLabel getLblCrs() {
		if (lblCrs==null){
			lblCrs=new JLabel(currentCrs.getAbrev());
		}
		return lblCrs;
	}

    /**
     * Activa o desactiva el botn y el cuadro de texto
     * @param b Si es False--> deshabilita
     */
	public void habilitarExistente(boolean b) {
			getBtnCrsExistente().setEnabled(b);
			getLblCrs().setEnabled(b);
	}
	public void habilitarWkt(boolean b){
			//getBtnImportarWkt().setEnabled(b);
			getTxtAreaWkt().setEnabled(b);
	}
	/**
	 * Inicilizar el boton importar la cadena WKT
	 * @return
	 */
	public JButton getBtnImportarWkt() {
		if(btnImportarWkt==null){
			btnImportarWkt=new JButton();
			btnImportarWkt.setText("...");
			btnImportarWkt.addActionListener(this);
			btnImportarWkt.setToolTipText(PluginServices.getText(this,"DefCrsUsr_importar_wkt"));
			btnImportarWkt.setEnabled(false);
		}
		return btnImportarWkt;
	}
	public JButton getBtnCrsExistente() {
		if (btnCrsExistente==null){
			btnCrsExistente=new JButton("...");
			Dimension d=new Dimension(btnCrsExistente.getPreferredSize());
			d.width=100;
			btnCrsExistente.setSize(d);
			btnCrsExistente.addActionListener(this);
			btnCrsExistente.setToolTipText(PluginServices.getText(this,"DefCrsUsr_Seleccionar_Crs"));
		}
		return btnCrsExistente;
	}
	/**
	 * Inicializar el area de texto donde se introduce la cadena WKT
	 * @return
	 */
	public JTextArea getTxtAreaWkt() {
		if(txtAreaWkt==null){
			txtAreaWkt=new JTextArea();
			txtAreaWkt.setLineWrap(true);
			txtAreaWkt.setWrapStyleWord(true);
			Dimension d=new Dimension(txtAreaWkt.getPreferredSize());
			d.width=width;
			txtAreaWkt.setSize(d);
		}
		return txtAreaWkt;
	}
	/*
	 *Crea un panel de desplazamineto en el que se 
	 *agrega el area de texto para que ste tenga barras de desplazamiento 
	 */
	public JScrollPane getScrollWkt() {
		if (scrollWkt==null){
			scrollWkt=new JScrollPane();
			scrollWkt.setViewportView(getTxtAreaWkt());
			scrollWkt.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			Dimension d=new Dimension(txtAreaWkt.getPreferredSize());
			d.width=width;
			scrollWkt.setSize(d);
		}
		return scrollWkt;
	}

	public void actionPerformed(ActionEvent e) {
		 if(e.getSource().equals(getRbCrsExistente())){
			 setHasChange(true);
	      	  habilitarExistente(true);
	      	  habilitarWkt(false);
         }else  if(e.getSource().equals(getRbNuevoCrs())){
        	 setHasChange(true);
      	   habilitarExistente(false);
      	   habilitarWkt(false);      	   
         }else  if(e.getSource().equals(getRbCadenaWkt())){
        	 setHasChange(true);
      	   habilitarExistente(false);
      	   habilitarWkt(true);
         }else if(e.getSource().equals(getBtnCrsExistente())){
        	 ImportNewCrsDialog newCrs = new ImportNewCrsDialog("crs");
        	 PluginServices.getMDIManager().addWindow(newCrs);
        	 if (newCrs.getCode() != -1) {
	        	 getLblCrs().setText("EPSG:"+newCrs.getCode());
	        	 setCrs(newCrs.getCode());
	        	 setHasChange(true);
        	 }
        	 
         }else if(e.getSource().equals(getBtnImportarWkt())){
        	 System.out.println("Boton Importar Wkt");
        	 
         }
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
	
	public void setHasChange(boolean change) {
		hasChanged = change;
	}
	
	public boolean getHasChanged() {
		return hasChanged;
	}

}
