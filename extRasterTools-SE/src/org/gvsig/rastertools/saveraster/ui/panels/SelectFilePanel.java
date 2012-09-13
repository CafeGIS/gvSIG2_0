/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
*
* Copyright (C) 2007 IVER T.I. and Generalitat Valenciana.
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
*/
package org.gvsig.rastertools.saveraster.ui.panels;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.iver.andami.PluginServices;

/**
 * Panel con los botones de selección de formato y fichero de salida y variación
 * de propiedades de guardado.
 * 
 * @version 18/04/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class SelectFilePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JPanel pFileName = null;
	private JPanel pButtons = null;
	private JLabel lFileTag = null;
	private JLabel lFileName = null;
	private JButton bSelect = null;
	private JButton bProperties = null;

	/**
	 * This is the default constructor
	 */
	public SelectFilePanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setLayout(new GridLayout(2, 1));
		setBorder(javax.swing.BorderFactory.createTitledBorder(null, PluginServices.getText(this, "file"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", java.awt.Font.PLAIN, 10), null));
		this.add(getPanelLabel());
		this.add(getPanelButtons());
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPanelLabel() {
		if (pFileName == null) {
			FlowLayout flowLayout1 = new FlowLayout();
			flowLayout1.setHgap(1);
			flowLayout1.setAlignment(java.awt.FlowLayout.LEFT);
			flowLayout1.setVgap(2);
			lFileName = new JLabel();
			lFileName.setText("");
			lFileTag = new JLabel();
			lFileTag.setText(PluginServices.getText(this, "file") + ":");
			pFileName = new JPanel();
			pFileName.setLayout(flowLayout1);
			pFileName.setPreferredSize(new java.awt.Dimension(347,20));
			pFileName.add(lFileTag, null);
			pFileName.add(lFileName, null);
		}
		return pFileName;
	}

	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	public JPanel getPanelButtons() {
		if (pButtons == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(java.awt.FlowLayout.RIGHT);
			flowLayout.setHgap(2);
			flowLayout.setVgap(2);
			pButtons = new JPanel();
			pButtons.setLayout(flowLayout);
			pButtons.setPreferredSize(new java.awt.Dimension(347,22));
			pButtons.add(getBSelect(), null);
			pButtons.add(getBProperties(), null);
		}
		return pButtons;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	public JButton getBSelect() {
		if (bSelect == null) {
			bSelect = new JButton();
			bSelect.setPreferredSize(new java.awt.Dimension(110,20));
			bSelect.setText("Seleccionar");
		}
		return bSelect;
	}

	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */
	public JButton getBProperties() {
		if (bProperties == null) {
			bProperties = new JButton();
			bProperties.setPreferredSize(new java.awt.Dimension(160,20));
			bProperties.setText("Propiedades GTiff");
		}
		return bProperties;
	}

	/**
	 * Asigna el valor del texto de la etiqueta
	 * @param text cadena con el texto
	 */
	public void setLabelText(String text){
		lFileName.setText(text);
	}
	
	/**
	 * Obtiene el valor del texto de la etiqueta
	 * @return text cadena con el texto
	 */
	public JLabel getLabelText(){
		return lFileName;
	}
	
	/**
	 * Asigna el valor del texto de la etiqueta
	 * @param text cadena con el texto
	 */
	public void setTag(String tag){
		lFileTag.setText(tag);
	}
}
