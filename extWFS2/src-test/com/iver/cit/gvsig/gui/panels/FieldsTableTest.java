package com.iver.cit.gvsig.gui.panels;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;

import org.gvsig.remoteClient.wfs.schema.XMLElement;
import org.gvsig.remoteClient.wfs.schema.XMLElementsFactory;
import org.gvsig.remoteClient.wfs.schema.XMLSchemaParser;
import org.xmlpull.v1.XmlPullParserException;

import com.iver.cit.gvsig.gui.panels.fieldstree.FieldsTreeTable;
import com.iver.cit.gvsig.gui.panels.fieldstree.TreeTableModelWithCheckBoxes;

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
 * Revision 1.2  2007-01-16 08:47:49  csanchez
 * Sistema de Warnings y Excepciones adaptado a BasicException
 *
 * Revision 1.1  2006/12/26 09:10:37  ppiqueras
 * Cambiado "atttibutes" en todas las aparaciones en atributos, mÃ©todos, clases o comentarios por "fields". (SÃ³lo a aquellas que afectan a clases dentro del proyecto extWFS2).
 *
 * Revision 1.7  2006/11/01 17:29:08  jorpiell
 * Se ha elimiado el nodo virtual de la raiz. Además ya se cargan los valores de un campo complejo en la pestaña del filtro
 *
 * Revision 1.6  2006/10/31 09:37:18  jorpiell
 * Se devuelve el tipo de la entidad completo, y no sus hijos
 *
 * Revision 1.5  2006/10/27 12:25:57  jorpiell
 * Modificado el test
 *
 * Revision 1.3  2006/10/24 07:58:14  jorpiell
 * Eliminado el parametro booleano que hacía que apareciesen mas de una columna en el tree table
 *
 * Revision 1.2  2006/10/24 07:27:56  jorpiell
 * Algunos cambios en el modelo que usa la tabla
 *
 * Revision 1.1  2006/10/23 07:37:04  jorpiell
 * Ya funciona el filterEncoding
 *
 *
 */
/**
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class FieldsTableTest{
	private String gmlFile = "src-test/WFS-DescribeFeatureTypeAves.xml";
	
	public static void main(String[] args) {
		FieldsTableTest att = new FieldsTableTest();
		att.initializeTable();	
		//att.initializeTree();
	}	
	
	private void initializeTree(){
		Vector att = describeFeatureType();
			
		TreeTableModelWithCheckBoxes model = new TreeTableModelWithCheckBoxes(att.get(0));
		JTree tree = new JTree(model);		
		
		createFrame(tree);
	}
	private void initializeTable(){
		Vector att = describeFeatureType();
								
		TreeTableModelWithCheckBoxes model = new TreeTableModelWithCheckBoxes();
		FieldsTreeTable treetable = new FieldsTreeTable(model);
						
		JScrollPane scrollPane = new JScrollPane(); 
		scrollPane.setViewportView(treetable); 	
				
		createFrame(scrollPane);
		
		//Sets the model
		treetable.setModel(new TreeTableModelWithCheckBoxes(att.get(0)));		
	}
	
	private void createFrame(Component component){
		JFrame f = new JFrame();
		f.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
            	System.exit(0);
            }
        });
		f.getContentPane().add(component);	    
		f.setBounds(0,0,600,500);
		f.setVisible(true);
	}
	

	private Vector describeFeatureType(){
		XMLSchemaParser schemaParser = new XMLSchemaParser();
		try {
			schemaParser.parse(new File(gmlFile),"ms");
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		XMLElement entity = XMLElementsFactory.getElement("Blyr");
		if (entity != null){
				Vector vector = new Vector();
				vector.add(entity);
				return vector;
		}
		
		return null;
	}
	
}
