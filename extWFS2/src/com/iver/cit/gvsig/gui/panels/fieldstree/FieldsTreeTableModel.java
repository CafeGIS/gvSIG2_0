package com.iver.cit.gvsig.gui.panels.fieldstree;

import java.util.Vector;

import org.gvsig.gui.beans.swing.treeTable.AbstractTreeTableModel;
import org.gvsig.gui.beans.swing.treeTable.TreeTableModel;
import org.gvsig.remoteClient.wfs.schema.XMLElement;
import org.gvsig.remoteClient.wfs.schema.type.IXMLType;
import org.gvsig.remoteClient.wfs.schema.type.XMLComplexType;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.gui.panels.model.WFSUtils;

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
 * $Id$
 * $Log$
 * Revision 1.3  2007-01-23 13:11:04  ppiqueras
 * Cambios sin importancia.
 *
 * Revision 1.2  2006/12/26 10:25:37  ppiqueras
 * Corregidas las dependencias con las nuevas ubicaciones de clases: IXMLType, XMLElement, IXMLComplexType, etc. (en libRemoteServices)
 *
 * Revision 1.1  2006/12/26 09:12:48  ppiqueras
 * Cambiado "atttibutes" en todas las aparaciones en atributos, mÃ©todos, clases, paquetes o comentarios por "fields". (SÃ³lo a aquellas que afectan a clases dentro del proyecto extWFS2). (En este caso se ha cambiado el nombre del paquete aparte de alguno otro nombre que puede haber cambiado).
 *
 * Revision 1.7  2006/12/14 12:29:58  ppiqueras
 * AÃ±adido mÃ©todo para obtener todas las hojas del Ã¡rbol
 *
 * Revision 1.6  2006/11/10 13:14:47  ppiqueras
 * Corregido un bug: caos que haya campos de una feature de una capa WFS, que no tengan tipo (tipo == null).
 *
 * Revision 1.5  2006/11/10 09:04:33  ppiqueras
 * La geometrÃ­a no se visualiza en el Ã¡rbol del panel de filtro, pero sÃ­ en el de atributos
 *
 * Revision 1.4  2006/11/01 17:29:07  jorpiell
 * Se ha elimiado el nodo virtual de la raiz. Además ya se cargan los valores de un campo complejo en la pestaña del filtro
 *
 * Revision 1.3  2006/10/31 13:51:15  ppiqueras
 * Soporte parent Element
 *
 * Revision 1.2  2006/10/31 12:24:04  jorpiell
 * Comprobado el caso en el que los atributos no tienen tipo
 *
 * Revision 1.1  2006/10/27 11:33:19  jorpiell
 * Añadida la treetable con los check box para seleccionar los atributos
 *
 * Revision 1.2  2006/10/24 07:58:14  jorpiell
 * Eliminado el parametro booleano que hacía que apareciesen mas de una columna en el tree table
 *
 * Revision 1.1  2006/10/24 07:27:56  jorpiell
 * Algunos cambios en el modelo que usa la tabla
 *
 * Revision 1.2  2006/10/23 08:17:18  jorpiell
 * Algunos cambios realizados para tener un árbol com más de una raíz
 *
 * Revision 1.1  2006/10/23 07:37:04  jorpiell
 * Ya funciona el filterEncoding
 *
 *
 */
/**
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
 */
public class FieldsTreeTableModel extends AbstractTreeTableModel {
	private boolean showGeometry;	
	private String[]  cNames = {PluginServices.getText(this,"attributeName"),
			PluginServices.getText(this,"attributeType")};
	private Class[]  cTypes = {TreeTableModel.class,String.class};
			
	public FieldsTreeTableModel(Object root) { 
		super(root);
		showGeometry = true;
	}
	
	public FieldsTreeTableModel(Object root, boolean show_Geometry) { 
		super(root);
		showGeometry = show_Geometry;
	}
	
	public FieldsTreeTableModel() { 
		super(null);
		showGeometry = true;
	}
	
	public boolean getShowGeometry() {
		return showGeometry;
	}
	
	//
	// The TreeModel interface
	//
	
	public int getChildCount(Object node) { 
		XMLElement element = (XMLElement)node;			
		if (element.getEntityType().getType() == IXMLType.COMPLEX){

			// It's supposed that geometry is on level 1
			Vector fields = ((XMLComplexType)element.getEntityType()).getAttributes();
			int size = fields.size();
			
			if (!showGeometry) {
				for (int i = 0; i < fields.size(); i++) {
					if ((((XMLElement)fields.get(i)).getEntityType() == null) || (((XMLElement)fields.get(i)).getEntityType().getType() == IXMLType.GML_GEOMETRY))
						size --;
				}
			}
			
			return size;
		}else{
			return 0;
		}
	}
	
	public Object getChild(Object node, int i) { 
		XMLElement element = (XMLElement)node;			
		if (element.getEntityType().getType() == IXMLType.COMPLEX){
			Vector fields = ((XMLComplexType)element.getEntityType()).getAttributes();
			XMLElement child = null;
			int k = -1;
			
			if (!showGeometry) {
				for (int j = 0; j < fields.size(); j++) {
					child = (XMLElement) fields.get(j);

					if ((child.getEntityType() == null) || (child.getEntityType().getType() != IXMLType.GML_GEOMETRY))
						k++;					
					
					if (i == k)
						break;
				}	
			}
			else {
				child = (XMLElement) fields.get(i);
			}
			
			child.setParentElement(element);
			
			return child;
		}
		return null;
	}
	
	// The superclass's implementation would work, but this is more efficient. 
	public boolean isLeaf(Object node) { 
		if (node == null){
			return true;
		}
		XMLElement element = (XMLElement)node;	
		if (element.getEntityType() != null){
			if (element.getEntityType().getType() == IXMLType.COMPLEX){
				return false;
			}
		}
		return true;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.gui.beans.swing.treeTable.TreeTableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return cNames.length;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.gui.beans.swing.treeTable.TreeTableModel#getColumnName(int)
	 */
	public String getColumnName(int column) {
		return cNames[column];
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.gui.beans.swing.treeTable.TreeTableModel#getColumnClass(int)
	 */
	public Class getColumnClass(int column) {
		return cTypes[column];
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.gui.beans.swing.treeTable.TreeTableModel#getValueAt(java.lang.Object, int)
	 */
	public Object getValueAt(Object node, int column) {
		XMLElement element = (XMLElement)node;	
		try {
			switch(column) {
			case 0:
				return element.getName();
			case 1:
				return PluginServices.getText(this,WFSUtils.getFieldType(element.getEntityType()));
			}
		}
		catch  (SecurityException se) { }
		
		return null; 
	}
	
	/**
	 * Returns leafs from a node branch
	 * 
	 * @param node A node in this tree model
	 * @return A vector with leafs
	 */
	public Vector<Object> getLeafsFromNodeBranch(Object node) {
		Vector<Object> leafs = new Vector<Object>();
		Object parent;
		
		if (this.isLeaf(node))
			leafs.add( ((XMLElement)node).getName() );
		else {
			int childrenCount = this.getChildCount(node);
			parent = node;
			
			for (int i = 0; i < childrenCount; i ++) {
				node = this.getChild(parent, i);
				
				if ( (this.isLeaf(node)) && (! leafs.contains(node)) )
					leafs.add(node);
				else
					leafs.addAll(getLeafsFromNodeBranch(node));
			}
		}
		
		return leafs;
	}
}