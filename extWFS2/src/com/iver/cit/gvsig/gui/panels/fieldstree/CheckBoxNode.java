package com.iver.cit.gvsig.gui.panels.fieldstree;

import java.awt.Color;
import java.util.Vector;

import org.gvsig.remoteClient.wfs.schema.XMLElement;
import org.gvsig.remoteClient.wfs.schema.type.IXMLType;
import org.gvsig.remoteClient.wfs.schema.type.XMLComplexType;



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
 * Revision 1.3  2007-09-19 16:14:50  jaume
 * removed unnecessary imports
 *
 * Revision 1.2  2006/12/26 10:25:37  ppiqueras
 * Corregidas las dependencias con las nuevas ubicaciones de clases: IXMLType, XMLElement, IXMLComplexType, etc. (en libRemoteServices)
 *
 * Revision 1.1  2006/12/26 09:12:48  ppiqueras
 * Cambiado "atttibutes" en todas las aparaciones en atributos, mÃ©todos, clases, paquetes o comentarios por "fields". (SÃ³lo a aquellas que afectan a clases dentro del proyecto extWFS2). (En este caso se ha cambiado el nombre del paquete aparte de alguno otro nombre que puede haber cambiado).
 *
 * Revision 1.2  2006/10/31 12:24:04  jorpiell
 * Comprobado el caso en el que los atributos no tienen tipo
 *
 * Revision 1.1  2006/10/27 11:33:19  jorpiell
 * Añadida la treetable con los check box para seleccionar los atributos
 *
 *
 */
/**
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class CheckBoxNode extends TetraStateCheckBox{
	private XMLElement element = null;	
	private CheckBoxNode parent = null;
	private Vector children = new Vector();	
	
	public CheckBoxNode(XMLElement element,CheckBoxNode parent){
		super();
		this.parent = parent;
		initialize(element);
	}
	
	private void initialize(XMLElement element){		
		this.setBackground(Color.WHITE);
		this.setText(fillNameWithBlancs(element.getName()));
		this.element = element;
		if (element.getEntityType() != null){
			if (element.getEntityType().getType() == IXMLType.COMPLEX){
				Vector subElements = ((XMLComplexType)element.getEntityType()).getAttributes();
				for (int i=0 ; i<subElements.size() ; i++){
					XMLElement child = (XMLElement)subElements.get(i);
					children.add(new CheckBoxNode(child,this));					
				}
			}else if(element.getEntityType().getType() == IXMLType.GML_GEOMETRY){
				this.setSelected(true);			
			}
		}
	}
	
	/**
	 * Return the max size for the combo labels
	 * @return
	 */
	public static int getTextLength(){
		return 200;
	}
	
	/**
	 * Fill the string with blancs
	 * @param name
	 * @return
	 */
	public static String fillNameWithBlancs(String name){
		String withBlancs = name;
		for (int i=name.length() ; i<getTextLength() ; i++){
			name = name + " ";
		}
		return name;
	}
	
	/**
	 * @return Returns the element.
	 */
	public XMLElement getElement() {
		return element;
	}
	
	/**
	 * @return Returns the children.
	 */
	public Vector getChildren() {
		return children;
	}
	
	public String toString(){
		return getElement().getName();
	}
	
	/**
	 * @return Returns the parent.
	 */
	public CheckBoxNode getParentNode() {
		return parent;
	}
}
//public class CheckBoxNode extends JPanel{
//private XMLElement element = null;	
//private CheckBoxNode parent = null;
//private Vector children = new Vector();	
//private TetraStateCheckBox checkBox = null;
//
//public CheckBoxNode(XMLElement element,CheckBoxNode parent){
//super();
//this.parent = parent;
//initialize(element);
//}
//
//private void initialize(XMLElement element){	
//this.setSize(new Dimension(300,400));
//checkBox = new TetraStateCheckBox();
//this.add(checkBox);		
//this.add(new JLabel(element.getName()));
//this.setBackground(Color.BLUE);		
//this.element = element;
//if (element.getEntityType().getType() == IXMLType.COMPLEX){
//Vector subElements = ((XMLComplexType)element.getEntityType()).getAttributes();
//for (int i=0 ; i<subElements.size() ; i++){
//XMLElement child = (XMLElement)subElements.get(i);
//children.add(new CheckBoxNode(child,this));					
//}
//}
//}
//
//public boolean isSelected(){
//return checkBox.isSelected();
//}
//
//public void setSelected(boolean selected){
//checkBox.setSelected(selected);
//}
//
//public void setColor(int color){
//checkBox.setColor(color);
//}
//
///**
//* @return Returns the element.
//*/
//public XMLElement getElement() {
//return element;
//}
//
///**
//* @return Returns the children.
//*/
//public Vector getChildren() {
//return children;
//}
//
//public String toString(){
//return getElement().getName();
//}
//
///**
//* @return Returns the parent.
//*/
//public CheckBoxNode getParentNode() {
//return parent;
//}
//}