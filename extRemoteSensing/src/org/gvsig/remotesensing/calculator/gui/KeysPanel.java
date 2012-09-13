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

import info.clearthought.layout.TableLayout;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

/**
 * Panel numérico para la calculadora.
 * 
 * @author Alejandro Muñoz (alejandro.munoz@uclm.es)
 * @author Luisa Marina Fernandez Ruiz (luisam.fernandez@uclm.es)
 * @version 22/10/2006 
 */
public class KeysPanel extends JPanel implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	private JButton jButtonMinus;
	private JButton jButtonDivide;
	private JButton jButton2;
	private JButton jButtonDot;
	private JButton jButtonBrackets;
	private JButton jButton0;
	private JButton jButton9;
	private JButton jButton8;
	private JButton jButton7;
	private JButton jButton6;
	private JButton jButton5;
	private JButton jButton4;
	private JButton jButton3;
	private JButton jButton1;
	private JButton jButtonMultiply;
	private JButton jButtonPlus;
	
	Dimension dButton;
	private JTextArea textExpression = null;
	
	
	/**
	 * Constructor
	 * @param m_TextExpression  area de texto donde se reliza la escritura de los caracteres
	 */
	public KeysPanel(JTextArea m_TextExpression) {
		super();
		textExpression=m_TextExpression;
		dButton=new Dimension();
		dButton=getJButtonBrackets().getPreferredSize();
		Inicializar();
		
	}
	
	/**Inicializacion del teclado de la calculadora */
	private void Inicializar(){
		
		TableLayout thisLayout = new TableLayout(new double[][] {
				{ TableLayout.PREFERRED,1.0, TableLayout.PREFERRED, TableLayout.PREFERRED,
						TableLayout.PREFERRED },
				{ TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED,
						TableLayout.PREFERRED } });
		//Establece la separación entre los elementos
		thisLayout.setHGap(3);
		thisLayout.setVGap(3);
		//asignar el layout al panel actual(this)		
		this.setLayout(thisLayout);
		TitledBorder topBorder = BorderFactory.createTitledBorder("");
	    topBorder.setTitlePosition(TitledBorder.TOP);
	    this.setBorder(new CompoundBorder(topBorder,new EmptyBorder(1,1,1,1)));
		
		//añadir los componentes al panel
		this.add(getJButtonPlus(),"0,0");
		this.add(getJButtonMinus(),"0,1");
		this.add(getJButtonMultiply(),"0,2");
		this.add(getJButtonDivide(),"0,3");
		this.add(getJButton7(),"2,0");
		this.add(getJButton4(),"2,1");
		this.add(getJButton1(),"2,2");
		this.add(getJButton0(),"2,3");
		this.add(getJButton8(),"3,0");
		this.add(getJButton5(),"3,1");
		this.add(getJButton2(),"3,2");
		this.add(getJButtonBrackets(),"3,3");
		this.add(getJButton9(),"4,0");
		this.add(getJButton6(),"4,1");
		this.add(getJButton3(),"4,2");
		this.add(getJButtonDot(),"4,3");
	}
	
	/**
	 * Define todos los eventos de los botones de la calculadora
	 */
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource().equals(jButton0))  {
			// Completar con acciones de boton 0
			textExpression.insert("0", textExpression.getCaretPosition());
			
		}else if (e.getSource().equals(jButton1))  {
			// Completar con acciones de boton 1
			textExpression.insert("1", textExpression.getCaretPosition());
		}
		else if (e.getSource().equals(jButton2))  {
			// Completar con acciones de boton 2
			textExpression.insert("2", textExpression.getCaretPosition());	
		}
		else if (e.getSource().equals(jButton3))  {
			// Completar con acciones de boton 3
			textExpression.insert("3", textExpression.getCaretPosition());
		}
		else if (e.getSource().equals(jButton4))  {
			// Completar con acciones de boton 4
			textExpression.insert("4", textExpression.getCaretPosition());
		}
		else if (e.getSource().equals(jButton5))  {
			// Completar con acciones de boton 5
			textExpression.insert("5", textExpression.getCaretPosition());
		}
		else if (e.getSource().equals(jButton6))  {
			// Completar con acciones de boton 6
			textExpression.insert("6", textExpression.getCaretPosition());
		}
		else if (e.getSource().equals(jButton7))  {
			// Completar con acciones de boton 7
			textExpression.insert("7", textExpression.getCaretPosition());	
		}
		else if (e.getSource().equals(jButton8))  {
			// Completar con acciones de boton 8
			textExpression.insert("8", textExpression.getCaretPosition());
		}
		else if (e.getSource().equals(jButton9))  {
			// Completar con acciones de boton 9
			textExpression.insert("9", textExpression.getCaretPosition());
		}	
		else if (e.getSource().equals(jButtonPlus))  {
			// Completar con acciones de boton +
			textExpression.insert("+", textExpression.getCaretPosition());
		}
		else if (e.getSource().equals(jButtonMinus))  {
			// Completar con acciones de boton -
			textExpression.insert("-", textExpression.getCaretPosition());
		}
		else if (e.getSource().equals(jButtonMultiply))  {
			// Completar con acciones de boton *
			textExpression.insert("*", textExpression.getCaretPosition());
		}
		else if (e.getSource().equals(jButtonDot))  {
			// Completar con acciones de boton .
			textExpression.insert(".", textExpression.getCaretPosition());
		}
		else if (e.getSource().equals(jButtonDivide))  {
			// Completar con acciones de boton /
			textExpression.insert("/", textExpression.getCaretPosition());
		}
		else if (e.getSource().equals(jButtonBrackets))  {
			// Completar con acciones de boton ()
			textExpression.insert("()", textExpression.getCaretPosition());
			//coloca le cursor en el centro de los dos paréntesis
			textExpression.setCaretPosition(textExpression.getCaretPosition() - 1);
		}
	}

	
	/**
	 * @return boton correspondiente al 0
	 */
	public JButton getJButton0() {
		if (jButton0==null){
			jButton0=new JButton();
			jButton0.setText("0");
			jButton0.addActionListener(this);
			jButton0.setPreferredSize(dButton);
		}
		return jButton0;
	}

	/**
	 * @return boton correspondiente al 1
	 */
	public JButton getJButton1() {
		if (jButton1==null){
			jButton1=new JButton();
			jButton1.setText("1");
			jButton1.setPreferredSize(dButton);
			jButton1.addActionListener(this);
		}
		return jButton1;
	}

	/**
	 * @return boton correspondiente al 2
	 */
	public JButton getJButton2() {
		if (jButton2==null){
			jButton2=new JButton();
			jButton2.setText("2");
			jButton2.setPreferredSize(dButton);
			jButton2.addActionListener(this);
		}
		return jButton2;
	}

	/**
	 * @return boton correspondiente al 3
	 */
	public JButton getJButton3() {
		
		if (jButton3==null){
			jButton3=new JButton();
			jButton3.setText("3");
			jButton0.setPreferredSize(dButton);
			jButton3.addActionListener(this);
		}	
		return jButton3;
	}

	/**
	 * @return boton correspondiente al 4
	 */
	public JButton getJButton4() {
		
		if (jButton4==null){
			jButton4=new JButton();
			jButton4.setText("4");
			jButton4.setPreferredSize(dButton);
			jButton4.addActionListener(this);
		}
		return jButton4;
	}

	/**
	 * @return boton correspondiente al 5
	 */
	public JButton getJButton5() {
		
		if (jButton5==null){
			jButton5=new JButton();
			jButton5.setText("5");
			jButton5.setPreferredSize(dButton);
			jButton5.addActionListener(this);
		}
		return jButton5;
	}

	
	/**
	 * @return boton correspondiente al 6
	 */
	public JButton getJButton6() {
		
		if (jButton6==null){
			jButton6=new JButton();
			jButton6.setText("6");
			jButton6.setPreferredSize(dButton);
			jButton6.addActionListener(this);
		}
		return jButton6;
	}

	/**
	 * @return boton correspondiente al 7
	 */
	public JButton getJButton7() {
		
		if (jButton7==null){
			jButton7=new JButton();
			jButton7.setText("7");
			jButton7.setPreferredSize(dButton);
			jButton7.addActionListener(this);
		}
		return jButton7;
	}

	/**
	 * @return boton correspondiente al 8
	 */
	public JButton getJButton8() {
		
		if (jButton8==null){
			jButton8=new JButton();
			jButton8.setText("8");
			jButton8.setPreferredSize(dButton);
			jButton8.addActionListener(this);
		}
		
		return jButton8;
	}

	
	/**
	 * @return boton correspondiente al 9
	 */
	public JButton getJButton9() {
		
		if (jButton9==null){
			jButton9=new JButton();
			jButton9.setText("9");
			jButton9.setPreferredSize(dButton);
			jButton9.addActionListener(this);
		}
		return jButton9;
	}

	
	/**
	 * @return boton correspondiente a los parentesis
	 */
	public JButton getJButtonBrackets() {
		
		if (jButtonBrackets==null){
			jButtonBrackets=new JButton();
			jButtonBrackets.setText("( )");
			jButtonBrackets.addActionListener(this);	
		}
		return jButtonBrackets;
	}

	/**
	 * @return boton correspondiente la operacion de division
	 */
	public JButton getJButtonDivide() {
		
		if (jButtonDivide==null){
			jButtonDivide=new JButton();
			jButtonDivide.setText("/");
			jButtonDivide.setPreferredSize(dButton);
			jButtonDivide.addActionListener(this);
		}
		return jButtonDivide;
	}

	/**
	 * @return boton correspondiente al separador decimal
	 */
	public JButton getJButtonDot() {
		
		if (jButtonDot==null){
			jButtonDot=new JButton();
			jButtonDot.setText(".");
			jButtonDot.setPreferredSize(dButton);
			jButtonDot.addActionListener(this);
		}
	
		return jButtonDot;
	}

	/**
	 * @return boton correspondiente la operación de resta
	 */
	public JButton getJButtonMinus() {
		if (jButtonMinus==null){
			jButtonMinus=new JButton();
			jButtonMinus.setText("-");
			jButtonMinus.setPreferredSize(dButton);
			jButtonMinus.addActionListener(this);
		}
	
		return jButtonMinus;
	}

	/**
	 * @return boton correspondiente a la operacion de multiplicacion
	 */
	public JButton getJButtonMultiply() {
		if (jButtonMultiply==null){
			jButtonMultiply=new JButton();
			jButtonMultiply.setText("*");
			jButtonMultiply.setPreferredSize(dButton);
			jButtonMultiply.addActionListener(this);
		}
	
		return jButtonMultiply;
	}

	
	/**
	 * @return boton correspondiente a la  operacion de suma
	 */
	public JButton getJButtonPlus() {
		
		if (jButtonPlus==null){
			jButtonPlus=new JButton();
			jButtonPlus.setText("+");
			jButtonPlus.setPreferredSize(dButton);
			jButtonPlus.addActionListener(this);
		}
		return jButtonPlus;
	}
	

}
