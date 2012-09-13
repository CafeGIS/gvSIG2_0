package org.gvsig.remotesensing.decisontrees;

import java.awt.Color;
import java.util.HashMap;

import junit.framework.TestCase;

import org.gvsig.remotesensing.decisiontrees.DecisionTree;
import org.gvsig.remotesensing.decisiontrees.DecisionTreeNode;

import com.iver.utiles.XMLEntity;

public class DecisionTreePersistenceTest extends TestCase {
	protected void setUp(){
	}
	
	public void start() {
		this.setUp();
		this.testStack();
	}

	public void testStack() {
		DecisionTreeNode root = new DecisionTreeNode();
		root.setChildren();
		root.setExpression("a<b");
		DecisionTreeNode left = root.getLeftChild();
		left.setChildren();
		left.setExpression("c>d");
		left.getLeftChild().setClassID(0);
		left.getRightChild().setClassID(1);
		root.getRightChild().setClassID(2);
		
		DecisionTree decisionTree = new DecisionTree(root);
		
		HashMap varTable = new HashMap();
		varTable.put("a", "layer1[Band0]");
		varTable.put("b", "layer1[Band1]");
		varTable.put("c", "layer2[Band0]");
		varTable.put("d", "layer2[Band4]");
		decisionTree.setVariablesTable(varTable);
		
		HashMap colorTable = new HashMap();
		Color color = new Color(100,34,54);
		colorTable.put(new Integer(0), color);
		color = new Color(10,34,124);
		colorTable.put(new Integer(1), color);
		color = new Color(67,98,34);
		colorTable.put(new Integer(2), color);
		decisionTree.setColorTable(colorTable);
		
		XMLEntity xmlDecisionTree = decisionTree.getXMLEntity();
		
		decisionTree.setXMLEntity(xmlDecisionTree);
		assertEquals(xmlDecisionTree.toString(), decisionTree.getXMLEntity().toString());
		
/*		
		FileOutputStream fos;
		try {
			//fos = new FileOutputStream("//home//dguerrero//Desktop//aa.xml");
			//OutputStreamWriter writer = new OutputStreamWriter(fos, "ISO-8859-1");
			FileWriter writer = new FileWriter("//home//dguerrero//Desktop//aa.xml");
			//xmlTag.marshal(writer);
			Marshaller m = new Marshaller(writer);
			m.setEncoding("ISO-8859-1");
			m.marshal(xmlDecisionTree.getXmlTag());
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MarshalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/
	}

}

