/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
 *   Av. Blasco Ibáñez, 50
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

package org.gvsig.remotesensing.calculator.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.iver.andami.PluginServices;

/**
 *  Clase que define el modelo de tabla variable- valor.
 *  La tabla contiene dos columnas una con el identificador de la variable y otra
 *  con la banda asociada a la variable.
 *  
 * @author Alejandro Muñoz Sanchez	(alejandro.munoz@uclm.es)
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 * @version 19/10/2007 
 */
public class TableFormat{

	private static final long serialVersionUID = 1L;
	private	JTable jTableVariables;
	private DefaultTableModel tableModel;
	private String nombreVar="b";
	private int cont=0;
	private int  filaSeleccionada;
	private boolean  enEspera;
	private JTextArea textExpresion;
	Object datos[][]=new Object[0][0];		  
	final String[] headers = {PluginServices.getText(this, "variable"),PluginServices.getText(this, "valor")};
  
	
	/**
	 * Constructor
	 */
	TableFormat(JTextArea Text) {
		 super();
		 // Inicializamos a -1 la fila  seleccionada de la tabla
		setFilaSeleccionada(-1);
		// Inicializamos enEspera a false
		setEnEspera(false);
		//Inicializa la tabla
		getTableFormat();
		textExpresion= Text;
	 }
		
	
	 /**
	 * @return tabla con las variables
	 */
	public JTable getTableFormat(){
		if (jTableVariables==null){
			//Crear el table model que define la tabla
			tableModel=new DefaultTableModel(datos, headers){
				private static final long serialVersionUID = 1L;
				public boolean isCellEditable(int row, int column) {
					return false;}
				}; 
			jTableVariables = new JTable(tableModel);	 
			jTableVariables.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jTableVariables.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
			jTableVariables.getTableHeader().setReorderingAllowed( false ); 
			jTableVariables.getColumn(PluginServices.getText(this, "variable")).setResizable(true);
			jTableVariables.getColumn(PluginServices.getText(this, "variable")).setPreferredWidth(60);
			jTableVariables.getColumn(PluginServices.getText(this, "valor")).setResizable(true);
			jTableVariables.setAutoscrolls(true);	 
				
			MouseListener ml = new MouseAdapter() {
				 
				public void mouseClicked(MouseEvent e) { 
				   	int iRow=jTableVariables.getSelectedRow();
				   	if (e.getClickCount()==2){  		
				   		String s= (String)tableModel.getValueAt(iRow,0);
				   		textExpresion.insert(s,textExpresion.getCaretPosition());
				   	}
				   	else{
				   		setFilaSeleccionada(iRow);
				   		setEnEspera(true);
				   	}
				} };		
			jTableVariables.addMouseListener(ml);
			}
		return jTableVariables;
	  }
	 

	/**
	 * Agrega en la tabla una nueva pareja Variable-Valor
	 *
	 * @param var   nombre de la variable
	 * @param value  valor de la variable
	 * @return el nombre de la variable asignada
	 */
	  public String InsertRow(String var, String value){
		//los nombres de las variables empiezan por 1
		  String nombre=nombreVar+(getCont()+1);
		  if (var != null)
			   nombre=var;
		  //Actualizar los cambios en el Jtable
		  Object[] newRow={nombre, value};
		  tableModel.addRow(newRow);
		  setCont(getCont()+1);
		  jTableVariables.updateUI();
		  return nombre;
	  }
	  
	  
	  /**
	   * obtiene la variable asignada a una capa-banda
	   * @param valor
	   * @return la variable asignada
	   */
	   public String getVariableOf(String valor){
		  String variable="";
		  //localiza si el valor ya tiene una variable asignada
		  for(int i=0;i<tableModel.getRowCount();i++){
				if(tableModel.getValueAt(i,1)!= null){
					if (tableModel.getValueAt(i,1).equals(valor)){
						variable=(String)tableModel.getValueAt(i,0);
						//devuelve la variable que tiene asignada ese valor
						return variable;
					}
				}
			}
			//devuelve "" si no está incluido el valor en la tabla
			return variable;
		}
			
	   
		/**
		 * @return contador de variables en la table
		 */
		public int getCont() {
			return cont;
		}		
		
		
		/**
		 * Asigna cont
		 * @param cont
		 */
		public void setCont(int cont) {
				this.cont = cont;
		}

		
		/**
		 * obtiene la fila de la tabla que está seleccionada
		 * @return la fila seleccionada
		 */
		public int getFilaSeleccionada() {
			return filaSeleccionada;
		}

		/**
		 * Establece la fila seleccionada de la tabla
		 * @param filaSeleccionada
		 */	
		protected void setFilaSeleccionada(int filaSeleccionada) {
			this.filaSeleccionada = filaSeleccionada;
		}

		/**
		 * Indica si la tabla está en espera 
		 * de un valor para asignar a una variable
		 * @return true si se esta a la espera de asignar la variabla
		 */
		public boolean isEnEspera() {
			return enEspera;
		}
		
		/**
		 * Establece el valor de la propiedad que indica si se espera un valor para la tabla
		 * @param enEspera
		 */
		public void setEnEspera(boolean enEspera) {
				this.enEspera = enEspera;
			}
		
		/**
		 * Modifica el valor de una variable
		 * @param row fila en la que está la variable
		 * @param valor nuevo valor que se le asigna
		 *
		 */
		public void actualizarVariable(int row,String valor){
				//inserta el valor en la fila indicada
				tableModel.setValueAt(valor,row,1);
				jTableVariables.updateUI();
			}
}
