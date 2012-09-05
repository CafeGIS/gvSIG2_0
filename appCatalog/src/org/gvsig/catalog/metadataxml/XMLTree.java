
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
package org.gvsig.catalog.metadataxml;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.gvsig.catalog.utils.Strings;
import org.w3c.dom.Document;


/**
 * Utils to parse XML trees using DOM
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class XMLTree {
	public static final String SEPARATOR = "->";

	/**
	 * Create a XML node from a File
	 * @return XML node
	 * @param file File name
	 */
	public static XMLNode xmlToTree(File file) {        
		try {
			return new XMLNode(file);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	} 

	/**
	 * Create a XML node from a InputStream
	 * @return XML node
	 * @param stream InputStream
	 */
	public static XMLNode xmlToTree(InputStream stream) {        
		try {
			return new XMLNode(stream);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return null;
		}
	} 

	/**
	 * Create a XML node from a String
	 * @return XML node
	 * @param stream InputStream
	 */
	public static XMLNode xmlToTree(String string) {        
		try {
			return new XMLNode(new ByteArrayInputStream(string.getBytes()));
		} catch (Exception e) {
			// Unconvertible UTF-8 character 
			string = Strings.replace(string,
					"<?xml version=\"1.0\" encoding=\"UTF-8\"?>",
			"<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
			try {
				return new XMLNode(new ByteArrayInputStream(string.getBytes()));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return null;
			}           
		}
	} 

	/**
	 * Devuelve un fichero que crea a partir de un arbol XML
	 * @return Devuelve el fichero escrito
	 * @param dom Documento en XML
	 * @param nombreFichero Nombre del fichero.
	 */
	public static File treeToXML(Document dom, String nombreFichero) {        
		OutputFormat format = null;
		StringWriter stringOut = null;
		XMLSerializer serial = null;
		FileWriter file = null;
		//Creamos un fichero para almacenar la respuesta
		File file_answer = new File(nombreFichero);
		format = new OutputFormat(dom);
		format.setEncoding("ISO-8859-1");
		format.setIndent(5);
		stringOut = new StringWriter();
		serial = new XMLSerializer(stringOut, format);
		try {
			serial.asDOMSerializer();
			serial.serialize(dom);
			file = new FileWriter(file_answer);
			file.write(stringOut.toString());
			file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return file_answer;
	} 

	/**
	 * Busca un Nodo dado una ruta de nodo del tipo "nodoRaiz:nodoPrimerNivel:...:nodoNivelN":
	 * @return Devuelve el Nodo que corresponde a la ruta correcta o 'null' si no
	 * lo encuentra
	 * @param nodoRaiz Nodo a partir del cual se quiere hacer la búsqueda
	 * @param etiqueta Ruta del campo que queremos buscar, separando los niveles por ':'
	 */
	public static XMLNode searchNode(XMLNode nodoRaiz, String etiqueta) {        
		XMLNode[] nodes = searchMultipleNode(nodoRaiz, etiqueta);
		if ((nodes != null) && (nodes.length > 0)) {
			return nodes[0];
		} else {
			return null;
		}
	} 

	/**
	 * Busca el padre de un Nodo dado una ruta de nodo del tipo "nodoRaiz:nodoPrimerNivel:...:nodoNivelN":
	 * 
	 * @param nodoRaiz Nodo a partir del cual se quiere hacer la búsqueda
	 * @param etiqueta Ruta del campo que queremos buscar, separando los niveles por ':'
	 * 
	 * @return Devuelve el Nodo padre que corresponde a la ruta correcta o 'null' si no
	 * lo encuentra
	 * @param rootNode 
	 * @param label 
	 */
	public static XMLNode searchParentNode(XMLNode rootNode, String label) {        
		StringTokenizer sti = new StringTokenizer(label, "->");
		if (rootNode == null) {
			return null;
		}

		XMLNode currentNode = rootNode.getSubNode(0);
		XMLNode parentNode = rootNode;

		//A cuantos niveles está el TOKEN
		int niveles = sti.countTokens();
		String nombreNodo = cutNamespace(sti.nextToken());
		int nivelActual = 1;
		int i = 0;
		while (i < parentNode.getNumSubNodes()) {
			if (nombreNodo.equals(cutNamespace(currentNode.getName()))) {
				if (niveles == nivelActual) {
					return parentNode;
				}
				parentNode = currentNode;
				currentNode = currentNode.getSubNode(0);
				nombreNodo = sti.nextToken();
				nivelActual++;
				i = 0;
			} else {
				currentNode = currentNode.getSubNode(i);
				i ++;
			}
		}
		return null;
	} 

	/**
	 * Hace una busqueda de un atributo de un nodo
	 * 
	 * @param nodo Nodo del que se quiere buscar el atributo
	 * @param nombreAtributo Nombre del atributo
	 * 
	 * @return Valor del atributo, o null si no lo ha encontrado
	 * @param node 
	 * @param attributeName 
	 */
	public static String searchAtribute(XMLNode node, String attributeName) {        
		return node.getAttribute(attributeName);
	} 

	/**
	 * Hace una busqueda de una etiqueta en un nodo y devuelve
	 * su valor
	 * 
	 * @param nodo Nodo del que se quiere buscar el atributo
	 * 
	 * @return Valor de la etiqueta
	 * @param node 
	 * @param etiqueta Nombre de la etiqueta
	 */
	public static String searchNodeValue(XMLNode node, String etiqueta) {        
		XMLNode nodoB = searchNode(node, etiqueta);
		if (nodoB == null)
			return null;
		return nodoB.getText();

	} 

	/**
	 * Hace una busqueda de una etiqueta en un nodo y devuelve
	 * el valor del atributo correspondiente
	 * 
	 * @param nodo Nodo del que se quiere buscar el atributo
	 * 
	 * @return Valor del atributo de la etiqueta o null
	 * @param node 
	 * @param etiqueta Nombre de la etiqueta
	 * @param atributo 
	 */
	public static String searchNodeAtribute(XMLNode node, String etiqueta, String atributo) {        
		XMLNode nodoB = searchNode(node, etiqueta);
		if (nodoB == null) {
			return null;
		} else {
			return searchAtribute(nodoB, atributo);
		}
	} 

	/**
	 * Hace una busqueda de nodos que se llaman igual y devuleve el valor
	 * 
	 * @param parentLabel Ruta del campo que queremos buscar, separando los niveles por '->'
	 * 
	 * @return Un vector con valores de las etiquetas
	 * @param rootNode Nodo a partir del cual se quiere hacer la búsqueda
	 * @param label Node label
	 */
	public static String[] searchMultipleNodeValue(XMLNode rootNode, String label) {        
		XMLNode[] nodes = searchMultipleNode(rootNode, label);
		if ((nodes == null) || (nodes.length == 0)) {
			return null;
		}
		String[] values = new String[nodes.length];
		for (int i = 0; i < nodes.length; i++)
			//if (nodes[i].getFirstChild() != null) {
			values[i] = nodes[i].getText();
		//}
		return values;
	} 

	/**
	 * Hace una busqueda de nodos que se llaman igual desde uno dado(sin recursividad)
	 * 
	 * @param etiqueta Ruta del campo que queremos buscar, separando los niveles por ':'
	 * 
	 * @return Un vector con los nodos que ha encontrado
	 * @param nodoRaiz Nodo a partir del cual se quiere hacer la búsqueda
	 * @param label 
	 */
	public static XMLNode[] searchMultipleNode(XMLNode nodoRaiz, String label) {        
		ArrayList rootNodes = new ArrayList();
		ArrayList leafNodes = new ArrayList();
		String firstLabel = null;
		leafNodes.add(nodoRaiz);
		int level = getLevelNumber(label);
		int k = 1;
		while (k <= level) {
			firstLabel = cutNamespace(getParentLabel(label));
			label = getChildLabel(label);
			rootNodes = new ArrayList(leafNodes);
			leafNodes.clear();
			for (int i = 0; i < rootNodes.size(); i++) {
				XMLNode root = (XMLNode) rootNodes.get(i);
				if (root != null) {
					XMLNode[] nodes = root.getSubnodes();
					for (int j = 0; j < nodes.length; j++) {
						if (cutNamespace(nodes[j].getName()).equals(firstLabel)) {
							leafNodes.add(nodes[j]);
						}
					}
				}
			}
			k++;
		}
		XMLNode[] nodes = new XMLNode[leafNodes.size()];

		for (int i = 0; i < leafNodes.size(); i++)
			nodes[i] = (XMLNode) leafNodes.get(i);
		return nodes;
	} 

	/**
	 * Gets the parent node label
	 * 
	 * 
	 * @return The parent node label
	 * @param nodeLabel Node label
	 */
	private static String getParentLabel(String nodeLabel) {        
		return separateParams(nodeLabel,SEPARATOR)[0];
	} 

	/**
	 * It cuts an String in an array of Strings separated by a pattern
	 * 
	 * 
	 * @return An array of Strings
	 * @param text Text to cut
	 * @param separator Pattent to find      *
	 */
	private static String[] separateParams(String text, String separator) {        
		return text.split(separator);	
	} 

	/**
	 * Gets the node label
	 * 
	 * 
	 * @return The node label
	 * @param nodeLabel Node label
	 */
	private static String getChildLabel(String nodeLabel) {        
		String st = null;
		String[] labels = separateParams(nodeLabel,SEPARATOR);

		if (labels.length  == 1){
			return labels[0];
		}

		st = labels[1];

		for (int i=2 ; i<labels.length ; i++)
			st = st + SEPARATOR + labels[i];

		return st;
	} 

	/**
	 * @return 
	 * @param nodeLabel 
	 */
	private static int getLevelNumber(String nodeLabel) {        
		String[] labels = separateParams(nodeLabel,SEPARATOR);
		return labels.length;
	} 

	/**
	 * Remove the namespace from a label
	 * @param label
	 */
	private static String cutNamespace(String label){
		if (label == null){
			return null;
		}
		int i =  label.indexOf(":");
		if (i > 0){
			return label.substring(i + 1, label.length());
		}
		return label;
	}
}
