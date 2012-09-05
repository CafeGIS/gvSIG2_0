/*
 * Created on 13-sep-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
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
package com.iver.utiles.xmlViewer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;


/**
 * Control que visualiza los contenidos de un XML jerarquicamente en un arbol,
 * los attributos de cada nodo en una tabla y el texto contenido entre los
 * tags en un area de texto
 *
 * @author Fernando González Cortés
 */
public class XMLViewer extends JPanel {

    private javax.swing.JScrollPane jScrollPane = null;
    private javax.swing.JTree xmlTree = null;
    private javax.swing.JScrollPane jScrollPane1 = null;
    private javax.swing.JTable attributeTable = null;
    private javax.swing.JSplitPane jSplitPane = null;
    private javax.swing.JScrollPane jScrollPane2 = null;
    private javax.swing.JTextArea txtContent = null;
    private javax.swing.JSplitPane jSplitPane1 = null;
    private DefaultTreeModel treeModel = null;
    private XMLContent content;
    private javax.swing.JPanel jPanel = null;
    private javax.swing.JPanel jPanel1 = null;
    private String[] namesColumn;

    /**
     * This is the default constructor
     */
    public XMLViewer() {
        super();
        initialize();
    }

    /**
     * Establece el origen XML del control
     *
     * @param content Objeto que lanzará los eventos del XML a representar
     *
     * @throws SAXException Si se produce algún error en el parseado del
     *         contenido
     */
    public synchronized void setModel(XMLContent content) throws SAXException {
        this.content = content;
        parse();
    }

    /**
     * Parsea el modelo rellenando los controles con la información
     *
     * @throws SAXException Si se produce un error de SAX parseando el
     *         XMLContent
     */
    private void parse() throws SAXException {
        MyContentHandler contentHandler = new MyContentHandler();
        content.setContentHandler(contentHandler);
        content.parse();
        treeModel = new DefaultTreeModel(contentHandler.getRoot());
        xmlTree.setModel(treeModel);
        xmlTree.clearSelection();
		// getJSplitPane().setDividerLocation(1.0); // Por defecto

    }

    /**
     * This method initializes this
     */
    private void initialize() {
        this.setLayout(new java.awt.BorderLayout());
        this.add(getJSplitPane1(), java.awt.BorderLayout.CENTER);
        //this.setPreferredSize(new Dimension(600, 600));
    }

    /**
     * This method initializes jScrollPane
     *
     * @return javax.swing.JScrollPane
     */
    private javax.swing.JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new javax.swing.JScrollPane();
            jScrollPane.setViewportView(getJPanel());
        }

        return jScrollPane;
    }

    /**
     * This method initializes xmlTree
     *
     * @return javax.swing.JTree
     */
    public javax.swing.JTree getXmlTree() {
        if (xmlTree == null) {
            xmlTree = new javax.swing.JTree();
            xmlTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {

					public void valueChanged(
                        javax.swing.event.TreeSelectionEvent e) {
                        DefaultTableModel tableModel = new DefaultTableModel();
                        for (int i=0;i<namesColumn.length;i++){
                        	tableModel.addColumn(namesColumn[i]);
                        }

                        Node selected = (Node) e.getPath().getLastPathComponent();
                        xmlTree.expandPath(e.getPath());
                        HashMap map = selected.attributes;
                        boolean hasAtts = false;
                        Iterator i = map.keySet().iterator();

                        while (i.hasNext()) {
                            hasAtts = true;

                            Object key = i.next();
                            tableModel.addRow(new Object[] { key, map.get(key) });
                        }

                        getAttributeTable().setModel(tableModel);
                        getAttributeTable().getTableHeader().setVisible(true);

                        if (selected.content == null) {
                            getTxtContent().setVisible( false);
                        } else {
                        	getTxtContent().setVisible(true);
                        	getTxtContent().setText(selected.content);
                        	getTxtContent().setEditable( false);
                        	getJSplitPane().setDividerLocation(0);//(jSplitPane.getHeight());
                        }

                    }
                });
        }
        xmlTree.setShowsRootHandles(true);
        xmlTree.setExpandsSelectedPaths(true);
        // xmlTree.setToggleClickCount(1);
        return xmlTree;
    }

    /**
     * This method initializes attributeTable
     *
     * @return javax.swing.JTable
     */
    private javax.swing.JTable getAttributeTable() {
        if (attributeTable == null) {
            attributeTable = new javax.swing.JTable();
            attributeTable.getTableHeader().setVisible(true);
        }

        return attributeTable;
    }

    /**
     * This method initializes jScrollPane1
     *
     * @return javax.swing.JScrollPane
     */
    private javax.swing.JScrollPane getJScrollPane1() {
        if (jScrollPane1 == null) {
            jScrollPane1 = new javax.swing.JScrollPane();
            jScrollPane1.setViewportView(getAttributeTable());
        }

        return jScrollPane1;
    }

    /**
     * This method initializes jSplitPane
     *
     * @return javax.swing.JSplitPane
     */
    private javax.swing.JSplitPane getJSplitPane() {
        if (jSplitPane == null) {
            jSplitPane = new javax.swing.JSplitPane();
            jSplitPane.setTopComponent(getJScrollPane1());
            jSplitPane.setBottomComponent(getJScrollPane2());
            jSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
            //jSplitPane.setOrientation(javax.swing.JSplitPane.HORIZONTAL_SPLIT);
            jSplitPane.setDividerLocation(0.9);
            jSplitPane.setResizeWeight(0.9);
            jSplitPane.setDividerSize(5);
        }

        return jSplitPane;
    }

    /**
     * This method initializes jScrollPane2
     *
     * @return javax.swing.JScrollPane
     */
    private javax.swing.JScrollPane getJScrollPane2() {
        if (jScrollPane2 == null) {
            jScrollPane2 = new javax.swing.JScrollPane();
            jScrollPane2.setViewportView(getTxtContent());
        }

        return jScrollPane2;
    }

    /**
     * This method initializes txtContent
     *
     * @return javax.swing.JTextArea
     */
    private javax.swing.JTextArea getTxtContent() {
        if (txtContent == null) {
            txtContent = new javax.swing.JTextArea();
        }

        return txtContent;
    }

    /**
     * This method initializes jSplitPane1
     *
     * @return javax.swing.JSplitPane
     */
    private javax.swing.JSplitPane getJSplitPane1() {
        if (jSplitPane1 == null) {
        	jSplitPane1 = new javax.swing.JSplitPane();
        	jSplitPane1.setLeftComponent(getJScrollPane());
        	jSplitPane1.setRightComponent(getJSplitPane());
            jSplitPane1.setDividerSize(5);
            jSplitPane1.setDividerLocation(0.3);
            jSplitPane1.setResizeWeight(0.2);
        }

        return jSplitPane1;
    }

    /**
     * This method initializes jPanel
     *
     * @return javax.swing.JPanel
     */
    private javax.swing.JPanel getJPanel() {
        if (jPanel == null) {
            jPanel = new javax.swing.JPanel();
            jPanel.setLayout(new java.awt.BorderLayout());
            jPanel.add(getXmlTree(), java.awt.BorderLayout.CENTER);
        }

        return jPanel;
    }

    /**
     * This method initializes jPanel1
     *
     * @return javax.swing.JPanel
     */
    /* private javax.swing.JPanel getJPanel1() {
        if (jPanel1 == null) {
            jPanel1 = new javax.swing.JPanel();
            jPanel1.setLayout(new java.awt.BorderLayout());
            jPanel1.add(getAttributeTable(), java.awt.BorderLayout.CENTER);
        }

        return jPanel1;
    } */

    /**
     * Clase nodo que almacena los datos de cada nodo de un XML
     *
     * @author Fernando González Cortés
     */
    public class Node extends DefaultMutableTreeNode {
        public HashMap attributes = new HashMap();
        public String content;
        public String name;

        /**
         * Representación de texto de este objeto
         *
         * @return String
         */
        public String toString() {
            return name;
        }
    }

    /**
     * Handler que recibe los SAXEvents del XMLContent y se guarda la
     * información de forma jerárquica en objetos Node
     *
     * @author Fernando González Cortés
     */
    public class MyContentHandler implements ContentHandler {
        private Stack node = new Stack();
        private boolean betweenTags = false;
        private Node root = null;

        /**
         * @see org.xml.sax.ContentHandler#endDocument()
         */
        public void endDocument() throws SAXException {
        }

        /**
         * @see org.xml.sax.ContentHandler#startDocument()
         */
        public void startDocument() throws SAXException {
            root = new Node();
            node.push(root);
        }

        /**
         * @see org.xml.sax.ContentHandler#characters(char[], int, int)
         */
        public void characters(char[] ch, int start, int length)
            throws SAXException {
            if (!betweenTags) {
                return;
            }

            Node actual = (Node) node.peek();
            actual.content = new String(ch, start, length).trim();
        }

        /**
         * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int,
         *      int)
         */
        public void ignorableWhitespace(char[] ch, int start, int length)
            throws SAXException {
        }

        /**
         * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
         */
        public void endPrefixMapping(String prefix) throws SAXException {
        }

        /**
         * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
         */
        public void skippedEntity(String name) throws SAXException {
        }

        /**
         * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
         */
        public void setDocumentLocator(Locator locator) {
        }

        /**
         * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String,
         *      java.lang.String)
         */
        public void processingInstruction(String target, String data)
            throws SAXException {
        }

        /**
         * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String,
         *      java.lang.String)
         */
        public void startPrefixMapping(String prefix, String uri)
            throws SAXException {
        }

        /**
         * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
         *      java.lang.String, java.lang.String)
         */
        public void endElement(String namespaceURI, String localName,
            String qName) throws SAXException {
            Node n = (Node) node.pop();
            ((Node) node.peek()).add(n);
            betweenTags = false;
        }

        /**
         * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
         *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
         */
        public void startElement(String namespaceURI, String localName,
            String qName, Attributes atts) throws SAXException {
            Node nuevo = new Node();
            nuevo.name = qName;

            for (int i = 0; i < atts.getLength(); i++) {
                nuevo.attributes.put(atts.getQName(i), atts.getValue(i));
            }

            node.push(nuevo);
            betweenTags = true;
        }

        /**
         * Obtiene la raiz del arbol generado o null si no se ha realizado
         * ningún parseado
         *
         * @return
         */
        public Node getRoot() {
            return root;
        }
    }
public void setNamesColumn(String[] names){
	namesColumn=names;
}
}

//  @jve:visual-info  decl-index=0 visual-constraint="10,10"
