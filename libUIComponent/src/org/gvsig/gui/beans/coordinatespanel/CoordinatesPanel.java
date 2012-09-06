/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.gui.beans.coordinatespanel;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.gvsig.gui.beans.datainput.DataInputContainer;
import org.gvsig.gui.beans.datainput.DataInputContainerListener;
/**
 * 
 * @version 06/09/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class CoordinatesPanel extends JPanel implements DataInputContainerListener {
	private static final long  serialVersionUID       = 3336324382874763427L;
	private String             pathToImages           = "images/";
	private ArrayList          actionChangedListeners = new ArrayList();

	private JPanel             pCoord                 = null;
	private JLabel             lTitulo1               = null;
	private JLabel             lTitulo2               = null;
	private DataInputContainer dataInputContainer11   = null;
	private DataInputContainer dataInputContainer12   = null;
	private DataInputContainer dataInputContainer21   = null;
	private DataInputContainer dataInputContainer22   = null;

	/**
	 * This is the default constructor
	 */
	public CoordinatesPanel() {
		initialize();
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		setLayout(new BorderLayout());
		add(getPCoord(), BorderLayout.CENTER);
	}

	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getPCoord() {
		if (pCoord == null) {
			GridBagConstraints gridBagConstraints;
			pCoord = new JPanel();
			pCoord.setLayout(new GridBagLayout());

			int y = 0;
			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = y;
			gridBagConstraints.insets = new Insets(5, 5, 2, 2);
			pCoord.add(getLTitulo1(), gridBagConstraints);

			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = y;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.insets = new Insets(5, 2, 2, 2);
			pCoord.add(getDataInputContainer11(), gridBagConstraints);

			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 2;
			gridBagConstraints.gridy = y;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.insets = new Insets(5, 2, 2, 5);
			pCoord.add(getDataInputContainer12(), gridBagConstraints);

			y++;
			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = y;
			gridBagConstraints.insets = new Insets(2, 5, 5, 2);
			pCoord.add(getLTitulo2(), gridBagConstraints);

			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = y;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.insets = new Insets(2, 2, 5, 2);
			pCoord.add(getDataInputContainer21(), gridBagConstraints);


			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 2;
			gridBagConstraints.gridy = y;
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.insets = new Insets(2, 2, 5, 5);
			pCoord.add(getDataInputContainer22(), gridBagConstraints);		
		}
		return pCoord;
	}

	private JLabel getLTitulo1(){
		if (lTitulo1 == null){
			lTitulo1 = new JLabel(new ImageIcon(getClass().getResource(pathToImages + "upleft.png")));
		}
		return lTitulo1;
	}

	private JLabel getLTitulo2(){
		if (lTitulo2 == null){
			lTitulo2 = new JLabel(new ImageIcon(getClass().getResource(pathToImages + "downright.png")));
		}
		return lTitulo2;
	}

	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	public DataInputContainer getDataInputContainer11() {
		if (dataInputContainer11 == null) {
			dataInputContainer11 = new DataInputContainer();
			dataInputContainer11.setLabelText("X");
			dataInputContainer11.addValueChangedListener(this);
		}
		return dataInputContainer11;
	}

	/**
	 * This method initializes jPanel1
	 *
	 * @return javax.swing.JPanel
	 */
	public DataInputContainer getDataInputContainer12() {
		if (dataInputContainer12 == null) {
			dataInputContainer12 = new DataInputContainer();
			dataInputContainer12.setLabelText("Y");
			dataInputContainer12.addValueChangedListener(this);
		}
		return dataInputContainer12;
	}

	/**
	 * This method initializes jPanel2
	 *
	 * @return javax.swing.JPanel
	 */
	public DataInputContainer getDataInputContainer21() {
		if (dataInputContainer21 == null) {
			dataInputContainer21 = new DataInputContainer();
			dataInputContainer21.setLabelText("X");
			dataInputContainer21.addValueChangedListener(this);
		}
		return dataInputContainer21;
	}

	/**
	 * This method initializes jPanel3
	 *
	 * @return javax.swing.JPanel
	 */
	public DataInputContainer getDataInputContainer22() {
		if (dataInputContainer22 == null) {
			dataInputContainer22 = new DataInputContainer();
			dataInputContainer22.setLabelText("Y");
			dataInputContainer22.addValueChangedListener(this);
		}
		return dataInputContainer22;
	}

	/**
	 * Método para poner un titulo al panel.
	 * 
	 * @param titlePanel
	 */
	public void setTitlePanel(String titlePanel) {
		getPCoord().setBorder(BorderFactory.createTitledBorder(null, titlePanel, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.PLAIN, 10), null));
	}

	/**
	 * Devuelve los valores del panel por orden de izquierda a derecha
	 * y de arriba a bajo.
	 * @return
	 */
	public String[] getValues(){
		String[] values = {
			getDataInputContainer11().getValue(),
			getDataInputContainer12().getValue(),
			getDataInputContainer21().getValue(),
			getDataInputContainer22().getValue()
		};
		return values;
	}

	/**
	 * Asigna los valores del panel por orden de izquierda a derecha
	 * y de arriba a bajo.
	 * @return
	 */
	public void setValues(String[] values){
		getDataInputContainer11().setValue(values[0]);
		getDataInputContainer12().setValue(values[1]);
		getDataInputContainer21().setValue(values[2]);
		getDataInputContainer22().setValue(values[3]);
	}
	
	/**
	 * Añadir un listener a la lista de eventos
	 * @param listener
	 */
	public void addValueChangedListener(CoordinatesListener listener) {
		if (!actionChangedListeners.contains(listener))
			actionChangedListeners.add(listener);
	}

	/**
	 * Borrar un listener de la lista de eventos
	 * @param listener
	 */
	public void removeValueChangedListener(CoordinatesListener listener) {
		actionChangedListeners.remove(listener);
	}
	
	/**
	 * Invocar a los eventos asociados al componente
	 */
	private void callValueChangedListeners(String component) {
		Iterator acIterator = actionChangedListeners.iterator();
		while (acIterator.hasNext()) {
			CoordinatesListener listener = (CoordinatesListener) acIterator.next();
			listener.actionValueChanged(new CoordinatesEvent(this, component));
		}
	}

	public void actionValueChanged(EventObject e) {
		if (e.getSource() == getDataInputContainer11().getDataInputField()) {
			callValueChangedListeners("11");
			return;
		}
		if (e.getSource() == getDataInputContainer12().getDataInputField()) {
			callValueChangedListeners("12");
			return;
		}
		if (e.getSource() == getDataInputContainer21().getDataInputField()) {
			callValueChangedListeners("21");
			return;
		}
		if (e.getSource() == getDataInputContainer22().getDataInputField()) {
			callValueChangedListeners("22");
			return;
		}
  }
	
	public String getValue11() {
		return getDataInputContainer11().getValue();
	}
	
	public String getValue12() {
		return getDataInputContainer12().getValue();
	}
	
	public String getValue21() {
		return getDataInputContainer21().getValue();
	}
	
	public String getValue22() {
		return getDataInputContainer22().getValue();
	}
	
	public void setValue11(String value) {
		getDataInputContainer11().setValue(value);
	}
	
	public void setValue12(String value) {
		getDataInputContainer12().setValue(value);
	}
	
	public void setValue21(String value) {
		getDataInputContainer21().setValue(value);
	}
	
	public void setValue22(String value) {
		getDataInputContainer22().setValue(value);
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled) {
	  super.setEnabled(enabled);
	  getPCoord().setEnabled(enabled);
	  getLTitulo1().setEnabled(enabled);
	  getLTitulo2().setEnabled(enabled);
	  getDataInputContainer11().setControlEnabled(enabled);
	  getDataInputContainer12().setControlEnabled(enabled);
	  getDataInputContainer21().setControlEnabled(enabled);
	  getDataInputContainer22().setControlEnabled(enabled);
  }
}