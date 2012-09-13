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
package org.gvsig.remotesensing.decisiontrees;

import java.util.ArrayList;

import org.nfunk.jep.JEP;

/**
 * Clase que representa un nódo en un árbol de decisión
 * 
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 *
 */
public class DecisionTreeNode {
	
	public static final int finalNode 			= 0;
	public static final int decisionNode 		= 1;

	private JEP 				parser 			= null;
	private String 				expression 		= null;
	private DecisionTreeNode 	leftChild 		= null;
	private DecisionTreeNode 	rightChild 		= null;
	private int 				classID 		= -1;
	
	public DecisionTreeNode() {
		parser = new JEP();
		parser.setAllowUndeclared(true);
		parser.addStandardFunctions();
	}
	
	private DecisionTreeNode(JEP parser) {
		this.parser = parser;
	}

	public String toString() {
		if(expression!=null)
			return expression;
		else 
			return String.valueOf(classID);
	}
	
	/**
	 * 
	 * @return <code>true</code> si es un node final, <code>false</code> en caso 
	 * contrario.
	 */
	public boolean isFinal(){
		if (getExpression() == null)
			return true;
		else
			return false;
	}
	
	/**
	 * 
	 * @return <code>true</code> si la evaluación de la expresión da como resultado
	 * un valor distinto de 0;
	 */
	public boolean evaluate(){
		return(parser.getValue()!=0.0);
	}

	/**
	 * Evalua el árbol que radica en este nodo.
	 * 
	 * @return Clase resultante de la evaluación. 
	 */
	public int execute (){
		if (expression!=null)
			if (evaluate()){
				if (rightChild!=null) 
					return rightChild.execute();
			}
			else if (rightChild!=null)
				return leftChild.execute();
		
		return classID;
	}
	
	/** 
	 * 
	 * @return <code>true</code> si todas las expresiones del subárbol que radica en 
	 * este nodo están libres de errores de sintaxis.
	 */
	public boolean hasError(){
		if(expression==null)
			return true;
		else
			return (parser.hasError() && getLeftChild().hasError() && getRightChild().hasError());
	}

	public JEP getParser() {
		return parser;
	}

	/**
	 * 
	 * @return expresión asociada al nodo
	 */
	public String getExpression() {
		return expression;
	}
	
	/**
	 * Asigna la expresión asociada al nodo
	 * 
	 * @param expression 
	 */
	public void setExpression(String expression) {
		this.expression = expression;
		parser.parseExpression(expression);
		classID = -1;
	}

	/**
	 * 
	 * @return Clase asociada al nodo
	 */
	public int getClassID() {
		return classID;
	}

	/**
	 * Establece el identificador de clase
	 * 
	 * @param classID
	 */
	public void setClassID(int classID) {
		this.classID = classID;
	}

	public void setChildren() {
		leftChild = new DecisionTreeNode();
		rightChild = new DecisionTreeNode();
		setExpression("");
	}
	
	/**
	 * Elimina los hijos del nodo
	 *
	 */
	public void deleteChildren(){
		leftChild = null;
		rightChild = null;
		expression = null;
	}

	/**
	 * 
	 * @return Hijo izquierdo
	 */
	public DecisionTreeNode getLeftChild() {
		return leftChild;
	}

	/**
	 * 
	 * @return Hijo Derecho
	 */
	public DecisionTreeNode getRightChild() {
		return rightChild;
	}
	
	/**
	 * Asigna a la variable con nombre <code>varName</code> el valor <code>value</code>
	 * 
	 * @param varName Nombre de la variable
	 * @param value Valor a asignar
	 */
	public void setVarValue (String varName, Object value){
		if (expression!=null){
			if(parser.getSymbolTable().keySet().contains(varName))
				parser.setVarValue(varName, value);
			rightChild.setVarValue(varName, value);
			leftChild.setVarValue(varName, value);
		}
	}
	
	/**
	 * 
	 * @return Número de nodos del árbol que radica en este nodo.
	 */
	public int size(){
		
		int leftSize = 0;
		int rightSize = 0;
		if (leftChild != null)
			leftSize = leftChild.size();
		if (rightChild != null)
			rightSize = rightChild.size();
		return 1+leftSize+rightSize;
	}
	
	/**
	 * 
	 * @return Número de niveles del árbol que radica en este nodo.
	 */
	public int getLevelsCount(){
		int levels = 1;
		int leftLevels = 0;
		int rightLevels = 0;
		if (leftChild!=null)
			leftLevels = leftChild.getLevelsCount();
		if (rightChild!=null)
			rightLevels = rightChild.getLevelsCount();
		
		if (leftLevels>rightLevels)
			levels = levels+leftLevels;
		else
			levels = levels+rightLevels;
		
		return levels;
	}
	
	/**
	 * 
	 * @return Lista de los identificadores de los nodos finales
	 */
	public void getFinals(ArrayList finals){
		if (isFinal()){
			finals.add(new Integer(getClassID()));
		}
		else{
			getLeftChild().getFinals(finals);
			getRightChild().getFinals(finals);
		}
	}

	public void setChildren(DecisionTreeNode leftChild, DecisionTreeNode rightChild) {
		this.leftChild = leftChild;
		this.rightChild = rightChild;
		if (getExpression()==null)
			setExpression("");
	}
}
