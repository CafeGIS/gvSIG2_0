package org.gvsig.remotesensing.decisontrees;

import junit.framework.TestCase;

import org.gvsig.remotesensing.decisiontrees.DecisionTreeNode;

public class DecisionTreesTest extends TestCase {

	protected void setUp(){
	}
	
	public void start() {
		this.setUp();
		this.testStack();
	}

	public void testStack() {
		/*
		 * Construcci�n del �rbol
		 */
		DecisionTreeNode root = new DecisionTreeNode();
		root.setChildren();
		root.setExpression("a<b");
		DecisionTreeNode left = root.getLeftChild();
		left.setChildren();
		left.setExpression("c>d");
		left.getLeftChild().setClassID(0);
		left.getRightChild().setClassID(1);
		root.getRightChild().setClassID(2);
		
		/*
		 * Asignaci�n de valores a las variables y ejecuci�n
		 */
		root.setVarValue("a", new Integer(0));
		root.setVarValue("b", new Integer(2));
		root.setVarValue("c", new Integer(3));
		root.setVarValue("d", new Integer(4));
		int resultClass = root.execute();
		assertEquals(2,resultClass,0.0);
		
		/*
		 * Asignaci�n de valores a las variables y ejecuci�n
		 */
		root.setVarValue("a", new Integer(3));
		root.setVarValue("b", new Integer(2));
		root.setVarValue("c", new Integer(3));
		root.setVarValue("d", new Integer(4));
		resultClass = root.execute();
		assertEquals(0,resultClass,0.0);
		
		/*
		 * Asignaci�n de valores a las variables y ejecuci�n
		 */
		root.setVarValue("a", new Integer(0));
		root.setVarValue("b", new Integer(2));
		root.setVarValue("c", new Integer(6));
		root.setVarValue("d", new Integer(4));
		resultClass = root.execute();
		assertEquals(2,resultClass,0.0);
		
		/*
		 * C�lculo del tama�o del �rbol;
		 */
		assertEquals(5,root.size(),0.0);
	}
}
