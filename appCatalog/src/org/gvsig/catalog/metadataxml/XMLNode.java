package org.gvsig.catalog.metadataxml;
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
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.Writer;
import java.util.Hashtable;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Esta clase representa un XMLNode simplificado
 * Contiene una lista de subnodos y una lista de atributos.
 * También tiene una cadena.
 * Soporta la lectura y escritura de y desde un fichero XML.
 * También soporta la lectura desde Internet
 * Modificado por jaume
 * 
 */
public class XMLNode {
	public static final String ISNOTXML = "NOTXML";
	private Vector subNodes = new Vector();
	private String cdata = null;
	private Hashtable attr = new Hashtable();
	private String nodeName;
	private String text = null;
	private Vector attrKeys = new Vector();
	private String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";

	/**
	 * Constructor, lee de un fichero
	 * @param file 
	 * @author jaume - jaume.dominguez@iver.es
	 * Modified by Jorge Piera Llodra - piera_jor@gva.es
	 */
	public  XMLNode(File file) throws Exception {        
		this(factory.newDocumentBuilder().parse(file));
	} 

	/**
	 * Constructor. Usando url.openStream() se puede usar
	 * para leer info descargada de internet.
	 * 
	 * 
	 * @param inputstream 
	 * @throws Exception
	 */
	public  XMLNode(InputStream inputstream) throws Exception {        
		this(factory.newDocumentBuilder().parse(inputstream));
	} 

	/**
	 * Contructor, constructor desde un documento DOM
	 * 
	 * 
	 * @param dom 
	 * @author jaume - jaume.dominguez@iver.es
	 */
	public  XMLNode(Document dom) throws Exception {        
		this((Element)dom.getFirstChild());
	} 

	/**
	 * Contructor, crea un nodo con su nombre
	 * 
	 * 
	 * @param name 
	 */
	public  XMLNode(String name) throws Exception {        
		nodeName = name;
	} 

	/**
	 * Contructor, crea un nodo con su nombre y el texto
	 * 
	 * 
	 * @param name 
	 * @param text 
	 */
	public  XMLNode(String name, String text) throws Exception {        
		nodeName = name;
		this.text = text;
	} 

	/**
	 * Contructor, desde un elemento DOM
	 * 
	 * 
	 * @param dom 
	 */
	public  XMLNode(Element dom) throws Exception {        
		nodeName = dom.getNodeName();
		NamedNodeMap map = dom.getAttributes();
		for (int i = 0; i < map.getLength(); i++) {
			Node att = map.item(i);
			addAtrribute(att.getNodeName(), att.getNodeValue());

		}
		NodeList nodeList = dom.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node sub = nodeList.item(i);
			if (sub.getNodeType() == Node.ELEMENT_NODE) {
				addSubNode(new XMLNode((Element)sub));
			}else if (sub.getNodeType() == Node.CDATA_SECTION_NODE){
				String sCdata = sub.getNodeValue().trim();   
				if (sCdata.length() > 0) {
					cdata = sCdata; 
				}
			}else if (sub.getNodeType() == Node.TEXT_NODE) {
				String s = sub.getNodeValue().trim();
				if (s.length() > 0) {
					if (text != null) {
						throw new Exception("XMLNode '" + nodeName + "' has 2 Textblocks");
					}
					text = s;
				}
			}
		}
	} 

	/**
	 * 
	 * 
	 * 
	 * @param s 
	 */
	public void setText(String s) {        
		text = s;
	} 

	/**
	 * 
	 * 
	 * 
	 * @param s 
	 */
	public void addSubNode(XMLNode s) {        
		subNodes.add(s);
	} 

	/**
	 * 
	 * 
	 * 
	 * @param name 
	 * @param value 
	 */
	public void addAtrribute(String name, String value) throws Exception {        
		if (attr.containsKey(name)) {
			throw new Exception(
					"XMLNode '" + nodeName + "' already contains Attribute '" + name + "'");
		}
		attr.put(name, value);
		attrKeys.add(name);
	} 

	/**
	 * 
	 * 
	 * 
	 * @return 
	 */
	public int getNumSubNodes() {        
		return subNodes.size();
	} 

	/**
	 * 
	 * 
	 * 
	 * @return 
	 */
	public String getName() {        
		return nodeName;
	} 

	/**
	 * 
	 * 
	 * 
	 * @return 
	 */
	public String getText() {        
		return text;
	} 

	/**
	 * 
	 * 
	 * 
	 * @return 
	 */
	public String getCdata() {        
		return cdata;
	} 

	/**
	 * 
	 * 
	 * 
	 * @return 
	 * @param index 
	 */
	public XMLNode getSubNode(int index) {        
		return (XMLNode)subNodes.get(index);
	} 

	/**
	 * 
	 * 
	 * 
	 * @return 
	 */
	public XMLNode[] getSubnodes() {        
		XMLNode[] xmlNodes = new XMLNode[getNumSubNodes()];
		for (int i=0 ; i<getNumSubNodes() ; i++){
			xmlNodes[i] = getSubNode(i);
		}
		return xmlNodes;
	} 

	/**
	 * 
	 * 
	 * 
	 * @return 
	 */
	public Vector getAttributeNames() {        
		return attrKeys;
	} 

	/**
	 * 
	 * 
	 * 
	 * @param wr 
	 */
	public void write(Writer wr) throws Exception {        
		wwrite("", wr);
	} 

	/**
	 * Escribe el código XML de este objeto con su identación
	 * 
	 * 
	 * @param pre 
	 * @param wr 
	 */
	private void wwrite(String pre, Writer wr) throws Exception {        
		wr.write(pre + "<" + nodeName);
		for (int i = 0; i < attrKeys.size(); i++) {
			String name = (String)attrKeys.get(i);
			String val = (String)attr.get(name);
			wr.write(" " + name + "='" + val + "'");
		}
		if (getNumSubNodes() == 0 && text == null) {
			wr.write("/>\n");
		}
		else {
			wr.write(">");
			if (text != null) {
				wr.write(text);
			}
			if (getNumSubNodes() > 0) {
				wr.write("\n");
				for (int i = 0; i < subNodes.size(); i++) {
					if (getSubNode(i) != null) {
						getSubNode(i).wwrite(pre + "  ", wr);
					}
				}
				wr.write(pre + "</" + nodeName + ">\n");
			}
			else {
				wr.write("</" + nodeName + ">\n");
			}
		}
	} 

	/**
	 * 
	 * 
	 * 
	 * @return 
	 * @param key 
	 */
	public String getAttribute(String key) {        
		return (String)attr.get(key);
	} 

	/**
	 * 
	 * 
	 * 
	 * @return 
	 * @param key 
	 */
	public double getDoubleAttribute(String key) {        
		return Double.parseDouble((String)attr.get(key));
	} 

	/**
	 * 
	 * 
	 * 
	 * @return 
	 * @param key 
	 */
	public boolean getBoolAttribute(String key) {        
		if (!hasAttribute(key)) {
			return false;
		}
		return new Boolean((String)attr.get(key)).booleanValue();
	} 

	/**
	 * 
	 * 
	 * 
	 * @return 
	 * @param key 
	 */
	public int getIntAttribute(String key) {        
		return Integer.parseInt((String)attr.get(key));
	} 

	/**
	 * 
	 * 
	 * 
	 * @return 
	 * @param key 
	 */
	public boolean hasAttribute(String key) {        
		return attr.containsKey(key);
	} 
//	escribe al fichero

	/**
	 * 
	 * 
	 * 
	 * @param f 
	 */
	public void write(File f) throws Exception {        
		FileWriter fw = new FileWriter(f);
		fw.write(header);
		write(fw);
		fw.flush();
		fw.close();
	} 

	/**
	 * @return 
	 */
	public String toString() {        
		return this.getName();
	} 

	static DocumentBuilderFactory factory;
	static {        
		factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setNamespaceAware(false);
		factory.setIgnoringComments(true);
	}  

	/**
	 * @param header 
	 */
	public void setHeader(String header) {        
		this.header = header;
	} 

	/**
	 * This method prints all the child nodes. It is used only for
	 * fixing bugs.
	 * 
	 */
	public void printSubNodes() {        
		for (int i=0 ; i<getNumSubNodes() ; i++){
			System.out.println(getSubNode(i).getName() + " = " + getSubNode(i).getText());
		}
	} 

	/**
	 * This method prints a node in the standard output. Just for degug
	 * @param node
	 */
	public void printNode() {        
		printNode(this);
	} 

	/**
	 * Busca un Nodo dado una ruta de nodo del tipo "nodoRaiz:nodoPrimerNivel:...:nodoNivelN":
	 * @return Devuelve el Nodo que corresponde a la ruta correcta o 'null' si no
	 * lo encuentra
	 * @param etiqueta Ruta del campo que queremos buscar, separando los niveles por ':'
	 */
	public XMLNode searchNode(String etiqueta) {
		return XMLTree.searchNode(this, etiqueta);
	}

	/**
	 * Hace una busqueda de un atributo de un nodo
	 * 
	 * @param nombreAtributo Nombre del atributo
	 * @return Valor del atributo, o null si no lo ha encontrado
	 * @param attributeName 
	 */
	public String searchAtribute(String attributeName) {  
		return XMLTree.searchAtribute(this, attributeName);
	}

	/**
	 * Hace una busqueda de una etiqueta en un nodo y devuelve
	 * su valor
	 * @return Valor de la etiqueta
	 * @param etiqueta Nombre de la etiqueta
	 */
	public String searchNodeValue(String etiqueta) {
		return XMLTree.searchNodeValue(this,etiqueta);
	}

	/**
	 * Hace una busqueda de una etiqueta en un nodo y devuelve
	 * el valor del atributo correspondiente
	 * @return Valor del atributo de la etiqueta o null	
	 * @param etiqueta Nombre de la etiqueta
	 * @param atributo 
	 */
	public String searchNodeAtribute(XMLNode node, String etiqueta, String atributo) {
		return XMLTree.searchNodeAtribute(this, etiqueta, atributo);	
	}

	/**
	 * Hace una busqueda de nodos que se llaman igual y devuleve el valor
	 * @return Un vector con valores de las etiquetas
	 * @param rootNode Nodo a partir del cual se quiere hacer la búsqueda
	 * @param label Node label
	 */
	public String[] searchMultipleNodeValue(String label) {        
		return XMLTree.searchMultipleNodeValue(this, label);
	}
	
	/**
	 * Hace una busqueda de nodos que se llaman igual desde uno dado(sin recursividad)
	 * @return Un vector con los nodos que ha encontrado
	 * @param nodoRaiz Nodo a partir del cual se quiere hacer la búsqueda
	 * @param label 
	 */
	public XMLNode[] searchMultipleNode(String label) {
		return XMLTree.searchMultipleNode(this, label);
	}
	
	/**
	 * @return The XML tree like a String
	 */
	public String getXmlTree(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("<" + getName());
		for (int i = 0; i < getAttributeNames().size(); i++) {
			String name = (String)getAttributeNames().get(i);
			String val = (String)getAttribute(name);
			buffer.append(" " + name + "='" + val + "'");
		}
		if (getNumSubNodes() == 0 && getText() == null) {
			buffer.append("/>\n");
		}
		else {
			buffer.append(">");
			if (getText() != null) {
				buffer.append(getText());
			}
			if (getNumSubNodes() > 0) {
				buffer.append("\n");
				for (int i = 0; i < getSubnodes().length; i++) {
					if (getSubnodes()[i] != null) {
						buffer.append(getSubnodes()[i].getXmlTree());
					}
				}
				buffer.append("</" + getName() + ">\n");
			}
			else {
				buffer.append("</" + getName() + ">\n");
			}
		}
		return buffer.toString();
	}
	
	/**
	 * Print a node by the default exit
	 * @param node
	 */
	private void printNode(XMLNode node){
		System.out.print(getXmlTree());
	}
}
