
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
package org.gvsig.catalog.utils.xmltreetable;
import java.awt.Font;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import next.swing.AbstractTreeTableModel;
import next.swing.TreeTableModel;

import org.amic.util.date.Date;
import org.gvsig.catalog.metadataxml.XMLNode;
import org.gvsig.catalog.utils.Strings;


/**
 * 
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class XMLTreeTableModel extends AbstractTreeTableModel {

/**
 * 
 * 
 */
    private Vector columns = null;

/**
 * 
 * 
 */
    private String[] allowed = null;

/**
 * 
 * 
 */
    private String[] forbidden = null;

/**
 * 
 * 
 */
    private boolean dynamic = false;

/**
 * Creates new XmlTreeTableModel
 * 
 * 
 * @param node 
 */
    public  XMLTreeTableModel(XMLNode node) {        
        this(node, -1, null, null);
    } 

/**
 * 
 * 
 * 
 * @param node 
 * @param level 
 * @param allowed 
 * @param forbidden 
 */
    public  XMLTreeTableModel(XMLNode node, int level, String[] allowed, String[] forbidden) {        
        super(new DefaultMutableTreeNode(node));
        this.allowed = allowed;
        this.forbidden = forbidden;
        processChildren((DefaultMutableTreeNode) getRoot(), level);
    } 

/**
 * 
 * 
 */
    public final int ALIGN_LEFT = JLabel.LEFT;

/**
 * 
 * 
 */
    public final int ALIGN_RIGHT = JLabel.RIGHT;

/**
 * 
 * 
 */
    public final int ALIGN_CENTER = JLabel.CENTER;

/**
 * 
 * 
 * 
 * @param value 
 */
    public void setDynamic(boolean value) {        
        dynamic = value;
    } 

/**
 * 
 * 
 * 
 * @return 
 * @param node 
 */
    public boolean isLeaf(Object node) {        
        return (getChildCount(node) == 0) && !dynamic;
    } 

/**
 * 
 * 
 * 
 * @return 
 * @param node 
 * @param level 
 */
    public boolean processChildren(DefaultMutableTreeNode node, int level) {        
        if (level == 0) {
            return true;
        }
        boolean doIt = true;
        XMLNode n = (XMLNode) node.getUserObject();
        if (allowed != null) {
            String name = n.getName();
            doIt = false;
            for (int i = 0; i < allowed.length; i++)
                if (allowed[i].equals(name)) {
                    doIt = true;
                    break;
                }
        }
        if (forbidden != null) {
            String name = n.getName();
            for (int i = 0; i < forbidden.length; i++)
                if (forbidden[i].equals(name)) {
                    doIt = false;
                    break;
                }
        }
        if (!doIt) {
            return false;
        }
        XMLNode[] nodes = n.getSubnodes();
        DefaultMutableTreeNode newNode;
        for (int i=0 ; i<nodes.length ; i++) {
            newNode = new DefaultMutableTreeNode(nodes[i]);
            if (processChildren(newNode, level - 1)) {
                node.add(newNode);
            }
        }
        return true;
    } 

/**
 * 
 * 
 * 
 * @param ctt 
 */
    public void configureView(XMLTreeTable ctt) {        
        Column col;
        javax.swing.table.TableColumnModel tcm = ctt.getColumnModel();
        ctt.setRowHeight(20);
        int total = 0;
        for (int i = 1; i < tcm.getColumnCount(); i++) {
            col = ((Column) columns.elementAt(i - 1));
            tcm.getColumn(i).setPreferredWidth(50);
            total += col.width;
            tcm.getColumn(i).setCellRenderer(new XMLTreeTableCellRenderer());
        }
        tcm.getColumn(0).setWidth(100);
        setExpandedTree((DefaultMutableTreeNode) getRoot(), ctt.getTree());
    } 

/**
 * 
 * 
 * 
 * @return 
 * @param node 
 * @param tree 
 */
    public boolean setExpandedTree(DefaultMutableTreeNode node, JTree tree) {        
        boolean isExpanded = false;
        Enumeration children = node.children();
        while (children.hasMoreElements()) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) children.nextElement();
            if (setExpandedTree(child, tree) && (isExpanded == false)) {
                isExpanded = true;
            }
        }
        if (node.isLeaf() && (isValueAt(node) || isAttributeAt(node))) {
            tree.expandPath(new TreePath(node.getPath()));
            return true;
        }
        if (isExpanded) {
            tree.expandPath(new TreePath(node.getPath()));
        }
        return isExpanded;
    } 

/**
 * 
 * 
 * 
 * @param name 
 * @param attribute 
 * @param label 
 * @param width 
 */
    public void addJScrollPaneColumn(String name, String attribute, String label, int width) {        
        addColumn(new Column(name, attribute, label, ALIGN_LEFT, width,
                JScrollPane.class, null));
    } 

/**
 * 
 * 
 * 
 * @param name 
 * @param attribute 
 * @param label 
 * @param width 
 */
    public void addStringColumn(String name, String attribute, String label, int width) {        
        addColumn(new Column(name, attribute, label, ALIGN_LEFT, width,
                String.class, null));
    } 

/**
 * 
 * 
 * 
 * @param name 
 * @param attribute 
 * @param label 
 * @param width 
 * @param format 
 */
    public void addNumberColumn(String name, String attribute, String label, int width, String format) {        
        addColumn(new Column(name, attribute, label, ALIGN_RIGHT, width,
                Double.class, format));
    } 

/**
 * 
 * 
 * 
 * @param name 
 * @param attribute 
 * @param label 
 * @param width 
 * @param format 
 */
    public void addDateColumn(String name, String attribute, String label, int width, String format) {        
        addColumn(new Column(name, attribute, label, ALIGN_LEFT, width,
                Date.class, format));
    } 

/**
 * 
 * 
 * 
 * @param column 
 */
    public void addColumn(Column column) {        
        if (columns == null) {
            columns = new Vector();
        }
        columns.add(column);
    } 

/**
 * 
 * 
 * 
 * @return 
 * @param parent 
 */
    public int getChildCount(Object parent) {        
        return ((DefaultMutableTreeNode) parent).getChildCount();
    } 

/**
 * 
 * 
 * 
 * @return 
 * @param parent 
 * @param index 
 */
    public Object getChild(Object parent, int index) {        
        return ((DefaultMutableTreeNode) parent).getChildAt(index);
    } 

/**
 * 
 * 
 * 
 * @return 
 * @param column 
 */
    public String getColumnName(int column) {        
        if (column == 0) {
            return "";
        }
        return ((Column) columns.elementAt(column - 1)).label;
    } 

/**
 * 
 * 
 * 
 * @return 
 * @param column 
 */
    public Class getColumnClass(int column) {        
        if (column == 0) {
            return TreeTableModel.class;
        }
        return ((Column) columns.elementAt(column - 1)).jClass;
    } 

/**
 * 
 * 
 * 
 * @return 
 * @param node 
 */
    public boolean isValueAt(Object node) {        
        DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) node;
        if (currentNode.getUserObject() instanceof XMLNode) {
            XMLNode xr = (XMLNode) currentNode.getUserObject();
            if (xr != null) {
                if (xr.getText() != null) {
                    if (!(Strings.replace(xr.getText(), " ", "").equals(""))) {
                        return true;
                    }
                }
            }
        }
        return false;
    } 

/**
 * 
 * 
 * 
 * @return 
 * @param node 
 */
    public boolean isAttributeAt(Object node) {        
        DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) node;
        if (currentNode.getUserObject() instanceof XMLNode) {
            XMLNode xr = (XMLNode) currentNode.getUserObject();
            if (xr != null) {
                if (getAttribute(xr) != null) {
                    if (!(Strings.replace(getAttribute(xr),
                                " ", "").equals(""))) {
                        return true;
                    }
                }
            }
        }
        return false;
    } 

/**
 * 
 * 
 * 
 * @return 
 * @param node 
 */
    public String getValueAt(Object node) {        
        DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) node;
        if (currentNode.getUserObject() instanceof XMLNode) {
            XMLNode xr = (XMLNode) currentNode.getUserObject();
            
            return xr.getText();
        }
        return null;
    } 

/**
 * 
 * 
 * 
 * @return 
 * @param node 
 * @param column 
 */
    public Object getValueAt(Object node, int column) {        
        //JScrollPane
        JEditorPane editor = new JEditorPane();
        editor.setEditable(false);
        JScrollPane scroll = new JScrollPane(editor);
        scroll.setBorder(null);
        //JLabel
        JLabel text = new JLabel();
        text.setFont(new Font(null, Font.PLAIN, 12));
        DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) node;
        if (currentNode.getUserObject() instanceof XMLNode) {
            XMLNode xr = (XMLNode) currentNode.getUserObject();
            if (column == 0) {
                return xr.toString();
            }
            Column col = (Column) columns.elementAt(column - 1);
            String att = col.attribute;
            try {
                if (att.equals("tag-name")) {
                    if (xr.getName().trim().length() > 40) {
                        editor.setText(xr.getName());
                        editor.setCaretPosition(0);
                        return scroll;
                    }
                    text.setText(xr.getName());
                    
                    return text;
                } else if (att.equals("tag-value")) {
                    if (xr.getText().trim().length() > 40) {
                        editor.setText(xr.getText());
                        editor.setCaretPosition(0);
                        return scroll;
                    }
                    text.setText(xr.getText());
                    return text;
                } else {
                    if (getAttribute(xr).trim().length() > 40) {
                        editor.setText(getAttribute(xr));
                        editor.setCaretPosition(0);
                        return scroll;
                    }
                    text.setText(getAttribute(xr));
                    return text;
                }
            } catch (Exception ex) {
            }
        }
        return null;
    } 

/**
 * 
 * 
 * 
 * @return 
 * @param node 
 */
    public String getAttribute(XMLNode node) {        
        String attributes = "";
        Vector vAttributes = node.getAttributeNames();
        
        for (int i = 0; i < vAttributes.size(); i++){
            String att = (String)vAttributes.get(i);
            String val = node.getAttribute(att);
            attributes = attributes +
                att + "=" + val + " ";
        }
        return attributes;
    } 

/**
 * 
 * 
 * 
 * @return 
 */
    public int getColumnCount() {        
        if (columns == null) {
            return 1;
        }
        return columns.size() + 1;
    } 
/**
 * 
 * 
 */
public class Column {

/**
 * 
 * 
 */
    int align;

/**
 * 
 * 
 */
    int width;

/**
 * 
 * 
 */
    Class jClass;

/**
 * 
 * 
 */
    String name;

/**
 * 
 * 
 */
    String attribute;

/**
 * 
 * 
 */
    String label;

/**
 * 
 * 
 */
    String format;

/**
 * 
 * 
 * 
 * @param name 
 * @param attribute 
 * @param label 
 * @param align 
 * @param width 
 * @param jClass 
 * @param format 
 */
     Column(String name, String attribute, String label, int align, int width, Class jClass, String format) {        
            this.name = name;
            this.attribute = attribute;
            this.label = label;
            this.align = align;
            this.width = width;
            this.jClass = jClass;
            this.format = format;
    } 
 }
 }
