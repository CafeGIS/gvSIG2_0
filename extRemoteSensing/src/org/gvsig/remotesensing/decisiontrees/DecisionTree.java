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

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;

import com.iver.utiles.IPersistence;
import com.iver.utiles.XMLEntity;

/**
 * Clase que representa un árbol de decisión
 * 
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 *
 */
public class DecisionTree implements IPersistence{
	
	private DecisionTreeNode 		root 			= null;
	/**
	 * Tabla de colores: Integer,Color
	 */
	private HashMap					colorTable 		= null;
	/**
	 * Tabla de variables: "varname","layerName[BandN]"
	 */
	private HashMap					variablesTable 	= null;

	
	public DecisionTree(){
		
	}
	
	public DecisionTree(DecisionTreeNode root) {
		this.root = root;
	}
	
	public String getClassName() {
		return getClass().getName();
	}

	public XMLEntity getXMLEntity() {
		
		if (root!=null){
			XMLEntity xml = new XMLEntity();
			XMLEntity xmlDedisionTree = new XMLEntity();
			xmlDedisionTree.setName("decision_tree");
			
			XMLEntity xmlTree = getXmlTree(root); 
			xmlDedisionTree.addChild(xmlTree);
			
			if (variablesTable!=null){
				XMLEntity xmlVars = getXmlVariablesTable();
				xmlDedisionTree.addChild(xmlVars);
			}
			
			if (colorTable != null){
				XMLEntity xmlColorTable = getXmlColorTable(); 
				xmlDedisionTree.addChild(xmlColorTable);
			}
			
			xml.addChild(xmlDedisionTree);
			return xml;
		}
		else return null;
	}

	public void setXMLEntity(XMLEntity xml) {
		XMLEntity xmlDecisionTree = xml.getChild(0);
		XMLEntity xmlTree = xmlDecisionTree.firstChild("name", "tree");
		if (xmlTree!=null){
			root = setXMLTree(xmlTree);
		}
		
		XMLEntity xmlVars = xmlDecisionTree.firstChild("name", "variables");
		if (xmlVars!=null){
			getVariablesTable().clear();
			XMLEntity xmlVar = null;
			String layerBand = "";
			for (int i=0; i<xmlVars.getChildrenCount();i++){
				xmlVar = xmlVars.getChild(i);
				layerBand = xmlVar.getStringProperty("layer")+"["+"Band"+xmlVar.getStringProperty("band")+"]";
				getVariablesTable().put(xmlVar.getObjectProperty("var"), layerBand);
			}
		}
		
		XMLEntity xmlColorTable = xmlDecisionTree.firstChild("name", "color_table");
		if (xmlColorTable!=null){
			getColorTable().clear();
			
			XMLEntity xmlColor = null;
			Integer classID = null; 
			Color color = null;
			int r,g,b;
			for (int i=0; i<xmlColorTable.getChildrenCount();i++){
				xmlColor = xmlColorTable.getChild(i);
				r = xmlColor.getIntProperty("red");
				g = xmlColor.getIntProperty("green");
				b = xmlColor.getIntProperty("blue");
				color = new Color(r,g,b);
				classID = new Integer(xmlColor.getIntProperty("class_id"));
				getColorTable().put(classID, color);
			}
		}
	}

	private DecisionTreeNode setXMLTree(XMLEntity xmlTree) {
		DecisionTreeNode treeNode = new DecisionTreeNode();
		if (xmlTree.contains("expression")){
			treeNode.setExpression(xmlTree.getStringProperty("expression"));
			DecisionTreeNode leftChild = setXMLTree(xmlTree.getChild(0));
			DecisionTreeNode rightChild = setXMLTree(xmlTree.getChild(1));
			treeNode.setChildren(leftChild,rightChild);
		}
		else
			treeNode.setClassID(xmlTree.getIntProperty("value"));
		
		return treeNode;
	}

	public HashMap getColorTable() {
		if (colorTable == null)
			colorTable = new HashMap();
		return colorTable;
	}

	public void setColorTable(HashMap colorTable) {
		this.colorTable = colorTable;
	}

	public DecisionTreeNode getRoot() {
		if (root == null)
			root = new DecisionTreeNode();
		return root;
	}

	public void setRoot(DecisionTreeNode root) {
		this.root = root;
	}

	public HashMap getVariablesTable() {
		if (variablesTable==null)
			variablesTable = new HashMap();
		return variablesTable;
	}

	public void setVariablesTable(HashMap variablesTable) {
		this.variablesTable = variablesTable;
	}
	
	private XMLEntity getXmlTree(DecisionTreeNode treeNode){
		XMLEntity xml = new XMLEntity ();
		xml.setName("tree");
		if (treeNode.isFinal()){
			xml.putProperty("value", treeNode.getClassID());
		}
		else{
			xml.putProperty("expression", treeNode.getExpression());
			xml.addChild(getXmlTree(treeNode.getLeftChild()));
			xml.addChild(getXmlTree(treeNode.getRightChild()));
		}
		return xml;
	}
	
	private XMLEntity getXmlVariablesTable(){
		XMLEntity xml = new XMLEntity ();
		xml.setName("variables");
		for (Iterator iter = variablesTable.keySet().iterator(); iter.hasNext();) {
			String var  = (String) iter.next();
			String layer = (String)variablesTable.get(var);
			String layerName = layer.substring(0,layer.indexOf("["));
			int band = Integer.valueOf(layer.substring(layer.lastIndexOf("Band")+4,layer.lastIndexOf("]"))).intValue();
			XMLEntity variableXml = new XMLEntity();
			variableXml.setName("variable");
			variableXml.putProperty("var", var);
			variableXml.putProperty("layer", layerName);
			variableXml.putProperty("band", band);
			xml.addChild(variableXml);
		}
		return xml;
	}
	
	private XMLEntity getXmlColorTable(){
		XMLEntity xml = new XMLEntity ();
		xml.setName("color_table");
		for (Iterator iterator = colorTable.keySet().iterator(); iterator.hasNext();) {
			Integer classId = (Integer) iterator.next();
			Color classColor = (Color)colorTable.get(classId);
			XMLEntity colorXml = new XMLEntity();
			colorXml.setName("color_entry");
			colorXml.putProperty("class_id", classId.intValue());
			colorXml.putProperty("red", classColor.getRed());
			colorXml.putProperty("green", classColor.getGreen());
			colorXml.putProperty("blue", classColor.getBlue());
			xml.addChild(colorXml);
		}
		return xml;
	}
	
}
