package org.gvsig.remoteClient.wfs.filters;


/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
/* CVS MESSAGES:
 *
 * $Id: BinaryTree.java 18271 2008-01-24 09:06:43Z jpiera $
 * $Log$
 * Revision 1.3  2006-11-13 11:35:02  jorpiell
 * Comitados los cambios que tienen en cuanta las comillas simples y dobles
 *
 * Revision 1.2  2006/11/06 13:09:39  jorpiell
 * Hay que quitarle las cmillas dobles a los nombres de los atributos en el Filtro
 *
 * Revision 1.1  2006/10/05 10:25:15  jorpiell
 * Implementado el filter encoding
 *
 *
 */
/**
 * This class implements a binary tree (not ordered) that is
 * used to build a sintactic tree for the SQL language.
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es)
 */
public class BinaryTree {
	private Node root;
	private Node currentNode;
	
	public BinaryTree(){
		root = null;
		currentNode = null;
	}

	/**
	 * This method is called for each term that is on 
	 * the SQL query. It's supposed that the value is a valid expression, otherwise will launch an exception.
	 * 
	 * @param value expression formatted
	 */
	public void addTerm(String value) {
		if (value.equals("(")) {
			if (currentNode == null) {
				currentNode = new Node();
				
				if (root == null) {
					root = currentNode;
				}
			}
			else {
				if (currentNode.leftNode == null) {
					currentNode.leftNode = new Node(currentNode);
					currentNode = currentNode.leftNode;
				}
				else {
					currentNode.rigthNode = new Node(currentNode);
					currentNode = currentNode.rigthNode;
				}
			}
		}
		else {
			if (value.equals(")")) {
				// Do nothing
			}
			else {
				// Add the expression or the operand

				// Seeks for the first parent node without a value defined, if there isn't any, creates a new one
				while (currentNode.value != null) {
					if (currentNode.parentNode == null) {
						currentNode.parentNode = new Node();
						currentNode.parentNode.leftNode = root;
						root = currentNode.parentNode;
						currentNode = root;
						break;
					}

					currentNode = currentNode.parentNode;
				}
				
				// Sets the expression or operand value
				currentNode.value = value;
			}
		}
	}
	
	/**
	 * Adds a new term to the tree
	 * @param name
	 * @param value
	 * @param operationPair
	 * @param operationTree
	 */
	public void addTerm(String value, String operationTree ) {
		Node operationNode = new Node();
		operationNode.value = value;
		if (root == null) {
			root = operationNode;			
		}else{
			Node newRoot = new Node();
			newRoot.value = operationTree;
			newRoot.leftNode = root;
			newRoot.rigthNode = operationNode;
			root = newRoot;
		}
	}

	/**
	 * Print all the tree
	 *
	 */
	public void printTree(){
		printNode(root,0);
	}	
	
	/**
	 * Print one node
	 * @param node
	 * Node to print
	 * @param level
	 * Level node
	 */
	private void printNode(Node node,int level){
		if (node != null){
			String tab = "";
			for (int i=0 ; i<level ; i++){
				tab = tab + "\t";
			}
			level++;
			if (node.isField()){
				System.out.print(tab + node.value + "\n");
			}else{
				System.out.print(tab + node.value + "\n");
				printNode(node.leftNode,level);
				printNode(node.rigthNode,level);
				System.out.print(tab + "\\" + node.value + "\n");
			}
		}		
	}
	
	/**
	 * @return Returns the root.
	 */
	public Node getRoot() {
		return root;
	}	
	
	/**
	 * This class represents a binary tree node.
	 * @author Jorge Piera Llodrá (piera_jor@gva.es)
	 *
	 */
	public class Node{
		private String value;
		private Node leftNode;
		private Node rigthNode;
		private Node parentNode;
		
		private Node(){
			leftNode = null;
			rigthNode = null;
			parentNode = null;
		}
		
		private Node(Node parentNode){
			leftNode = null;
			rigthNode = null;
			this.parentNode = parentNode;
		}
		
		private Node(Node parentNode, String value){
			leftNode = null;
			rigthNode = null;
			this.value = value;
			this.parentNode = parentNode;
		}
		
		public boolean isField(){
			if ((leftNode == null)&&
					(rigthNode == null)){
				return true;
			}
			return false;
		}

		/**
		 * @return Returns the leftNode.
		 */
		public Node getLeftNode() {
			return leftNode;
		}

		/**
		 * @return Returns the rigthNode.
		 */
		public Node getRigthNode() {
			return rigthNode;
		}

		/**
		 * @return Returns the value.
		 */
		public String getValue() {
			return value;
		}
	}

	
}

