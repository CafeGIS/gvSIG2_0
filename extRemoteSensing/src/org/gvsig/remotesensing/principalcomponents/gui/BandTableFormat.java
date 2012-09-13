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
	 *   Av. Blasco Ibañez, 50
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

package org.gvsig.remotesensing.principalcomponents.gui;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import com.iver.andami.PluginServices;

/**
 *  Clase que define el modelo de tabla contenida PrincipalComponentPanel.
 *  La tabla contiene dos columnas una con el identificador de la banda y otra
 *  con el check box para su seleccion
 *  
 * @author Alejandro Muñoz Sanchez (alejandro.munoz@uclm.es)
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es) 
 * @version 22/10/2007  
 */
public class BandTableFormat extends AbstractTableModel {
   
	private static final long serialVersionUID = 1L;
	private boolean DEBUG=true;
	private boolean multipleSelection= true;

	final String[] columnNames = {PluginServices.getText(this,"banda"), ""};
	Object[][] data = new Object[0][0];
	
	
	 /**
     * @return numero de columnas
     */
	public int getColumnCount() {
        return columnNames.length;
    }
    
	 /**
     * @return numero de filas
     */
    public int getRowCount() {
        return data.length;
    }

    /**
     * @param  numero de columna 
     * @return nombre de la columna correspondiente al indice que se pasa como parámetro  
     */
    public String getColumnName(int col) {
        return columnNames[col];
    }
    
    /**
     * @param colum
     * @param row	
     * @return valor de la celda de la tabla
     */
    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    
    /**
     * @return clase de la columna cuyo indice se pasa como parametro
     */
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    /**
     * @return  true si los elementos de la columna son editables
     */
    public boolean isCellEditable(int row, int col) {
        if (col < 1) { 
            return false;
        } else {
            return true;
        }
    }
    

    /**
     * Asina el valor a la celda
     * @param value valor a asignar
     * @param row 
     * @param col
     */
    public void setValueAt(Object value, int row, int col) {
    	if (DEBUG) {
           // Nothing
        }
    	if(multipleSelection==false){
    		for (int i=0; i<data.length;i++)
        			data[i][1]=new Boolean(false);
    		fireTableDataChanged();
    	}
    		
  
        if (data[0][col] instanceof Integer) {
            try {
                data[row][col] = new Integer((String)value);
            } catch (NumberFormatException e) {
                if (SwingUtilities.isEventDispatchThread()) {
                } else {
                    System.err.println("User attempted to enter non-integer"
                                   + " value (" + value 
                                   + ") into an integer-only column.");
                }
            }
        } else {
            data[row][col] = value;
        }

       
    }

  
    /**
     * Añade un elemento a la tabla
     * @param indetificador de la banda
     */
    public void addRow(String band, boolean active){
    	Object[][] dataux=new Object [data.length+1][];
    	// Objeto a añadir
    	Object [] newRow= {band, new Boolean(active)};
    	for (int i=0; i<data.length; i++)
    		dataux[i]=data[i];
    	dataux[data.length]=newRow;
    	data=dataux;
    }
      
    
    /**
     * @return array con bandas seleccionadas a true
     */
    public boolean[] getSeleccionadas(){
    	boolean a[]=new boolean[data.length];
    	for (int i=0; i<data.length;i++)
    		if(data[i][1].equals(new Boolean(true)))
    			a[i]=true;
    	return a;
    }
    
   
    /**
     * @return numero de bandas seleccionadas
     */
    public int getNumSelected(){
    	int cont =0;
    	for (int i=0;i<data.length;i++)
    		 if(data[i][1].equals(new Boolean(true)))
    			cont++;
    	return cont;
    }
    

    /**
     * Seleccion de todas las bandas de la tabla
     */
    public void seleccionarTodas(){
    	for (int i=0;i<data.length;i++)
    			setValueAt(new Boolean(true), i, 1); 	
    }
    
  
    /**
     *  No selecciona ninguna banda de la tabla
     */
    public void seleccionarNinguna(){
    	for (int i=0;i<data.length;i++)
    			setValueAt(new Boolean(false), i, 1);
    }
    

    /**
     * Inicialización de la tabla 
     */
    public void LimpiarLista(){
    	data=new Object[0][0];	
    }
  
    
    /**
     * Asigna el titulo de la columna
     * */
    public void setColumnName(String newName){
    	columnNames[0]= newName;
    }

    
    public void setMultipleSelection(boolean multipleSeleccion){
    	this.multipleSelection=multipleSeleccion;
    }

    
    /**
     * @return indice de la primera fila seleccionada
     */
    public int getFirstSelected(){
    	for (int i=0; i<data.length;i++)
    		if(data[i][1].equals(new Boolean(true)))
    			return i;
    	return -1;
   
    }
    
}