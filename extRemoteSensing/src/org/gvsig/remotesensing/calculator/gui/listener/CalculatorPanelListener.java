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

package org.gvsig.remotesensing.calculator.gui.listener;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;

import org.gvsig.remotesensing.calculator.gui.CalculatorPanel;
import org.nfunk.jep.Variable;

import com.iver.andami.PluginServices;


/**
 * Listener para el panel de la calculadora de bandas
 * 
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 * @author Alejandro Muñoz Sánchez	(alejandro.munoz@uclm.es)
 * @version 19/10/2007 
 */
public class CalculatorPanelListener implements ActionListener, KeyListener{
	
	private CalculatorPanel calculatorPanel = null;
	private boolean canClose = false;

	
	/**
	 * Constructor
	 * @param calculatorPanel
	 */
	public CalculatorPanelListener(CalculatorPanel calculatorPanel) {
		this.calculatorPanel = calculatorPanel;
	
	}

	/**
	* Fija las acciones que se realizan cuando se produce un evento
	* @param e Objeto que genera el evento
	*/
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource().equals(calculatorPanel.getSalvar())){
			//realizar acciones de salvar
			JFileChooser openFileChooser;
			
			openFileChooser = new JFileChooser();			
			openFileChooser.setEnabled(false);
			openFileChooser.addChoosableFileFilter(new ExpresionFileFilter());
			int returnVal = openFileChooser.showSaveDialog(calculatorPanel);
	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	        	String fileName = openFileChooser.getSelectedFile().toString();
	        	if (!fileName.endsWith(".exp"))
	        		fileName = fileName + ".exp";
	        	File outFile = new File(fileName);
	        	if (outFile.exists()){
	        		int resp = JOptionPane.showConfirmDialog(
							(Component) PluginServices.getMainFrame(),PluginServices.getText(this,"fichero_ya_existe_seguro_desea_guardarlo"),
							PluginServices.getText(this,"guardar"), JOptionPane.YES_NO_OPTION);
					if (resp != JOptionPane.YES_OPTION) {
						return;
					}
	        	}
	            try {
	            	OutputStream outputStream = new FileOutputStream(outFile);
					outputStream.write(calculatorPanel.getJTextExpression().getText().getBytes());
					outputStream.close();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
	        }
			//Desactiva click sobre tabla 
	        calculatorPanel.getJTableVariables().setEnEspera(false);		
		}else if (e.getSource().equals(calculatorPanel.getRecuperar())){
			//realizar acciones de recuperar			
			JFileChooser openFileChooser;
			openFileChooser = new JFileChooser();			
			openFileChooser.setEnabled(false);
			openFileChooser.addChoosableFileFilter(new ExpresionFileFilter());
			int returnVal = openFileChooser.showOpenDialog(calculatorPanel);
	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File inFile = openFileChooser.getSelectedFile();
	            InputStream inputStream = null;
	            byte[] buf = new byte[1];
	            calculatorPanel.getJTextExpression().setText("");
	            try {
					inputStream = new FileInputStream(inFile);
					while (inputStream.read(buf)> 0) {
						calculatorPanel.getJTextExpression().setText(calculatorPanel.getJTextExpression().getText()+new String(buf));
					}
					inputStream.close();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				KeyListener[] kls = (KeyListener[])(calculatorPanel.getJTextExpression().getListeners(KeyListener.class));
				kls[0].keyReleased(new KeyEvent(calculatorPanel,1,1L,1,1,(char)1));
	        }
	        
	        //	Desactiva click sobre tabla
	        calculatorPanel.getJTableVariables().setEnEspera(false);
		}
		
		if(e.getSource().equals(calculatorPanel.getJCheckExtent())){
					
			int cont=((DefaultTableModel)calculatorPanel.getJTableVariables().getTableFormat().getModel()).getRowCount();
			if (cont>0){
			
				for(int row=0;row<cont;row++)
					((DefaultTableModel)calculatorPanel.getJTableVariables().getTableFormat().getModel()).removeRow(0);
		
				calculatorPanel.getQWindowsHash().clear();
				calculatorPanel.initializeParser();
				calculatorPanel.getJTextExpression().setText("");
				calculatorPanel.getJTableVariables().getTableFormat().updateUI();
			}	
		}	
	}	
	
	public void keyPressed(KeyEvent e) {

	}

	
	public void keyReleased(KeyEvent e) {
		/*
		 * Se Actualiza la tabla de variables de acuerdo a la exprexión.
		 */
		JTable table = null;
		HashMap qWindowsHash = null;
		String expression = null;
		
		expression= calculatorPanel.getJTextExpression().getText();
		table = calculatorPanel.getJTableVariables().getTableFormat();
		qWindowsHash = calculatorPanel.getQWindowsHash();
		
		calculatorPanel.getParser().getSymbolTable().clear();
			calculatorPanel.getParser().parseExpression(expression);
			//if(!panel.getParser().hasError()){
				//Actualizar la tabla de variables:
				for (Iterator vars = calculatorPanel.getParser().getSymbolTable().values().iterator(); vars.hasNext();) {
					Variable var = (Variable) vars.next();
					if (!qWindowsHash.containsKey(var.getName())){
						qWindowsHash.put(var.getName(),null);
						calculatorPanel.getJTableVariables().InsertRow(var.getName(),"");
					}
				}
				
				for(int i=0;i<table.getRowCount();i++){
					String var = table.getValueAt(i,0).toString();
					if 	(!calculatorPanel.getParser().getSymbolTable().keySet().contains(var))
						/*
						 * Si la variable se encuentra en el HashMap de persistenciacc c(*) no se elimina de la tabla.
						 */
						if(calculatorPanel.getPersistentVarTable()== null || !calculatorPanel.getPersistentVarTable().containsKey(var)){
							((DefaultTableModel)table.getModel()).removeRow(i);
							qWindowsHash.remove(var);
							i--;
					}
				}
				table.updateUI();
	}

	public void keyTyped(KeyEvent e) {}
}


/**
 * Filtro para el selector de expresiones matemáticas.
 * @author Alejandro Muñoz (alejandro.munoz@uclm.es)
 *
 */
class ExpresionFileFilter extends FileFilter {

	final static String exp = "exp";
	public boolean accept(File f) {
		if (f.isDirectory()) {
           return true;
       }
       String s = f.getName();
       int i = s.lastIndexOf('.');

       if (i > 0 &&  i < s.length() - 1) {
           String extension = s.substring(i+1).toLowerCase();
           if (exp.equals(extension)){
                   return true;
           } else {
               return false;
           }
       }
       return false;
	}

	public String getDescription() {
		 return "Archivos .exp";
	}
	
}
