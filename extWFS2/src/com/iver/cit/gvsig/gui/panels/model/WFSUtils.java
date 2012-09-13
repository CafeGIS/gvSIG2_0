package com.iver.cit.gvsig.gui.panels.model;

import java.util.ArrayList;
import java.util.Vector;

import org.gvsig.remoteClient.wfs.WFSStatus;
import org.gvsig.remoteClient.wfs.schema.XMLElement;
import org.gvsig.remoteClient.wfs.schema.type.IXMLType;

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
 * $Id: WFSUtils.java 9916 2007-01-25 16:11:35Z jorpiell $
 * $Log$
 * Revision 1.1.2.4  2007-01-25 16:11:15  jorpiell
 * Se han cambiado los imports que hacían referencia a remoteServices. Esto es así, porque se han renombrado las clases del driver de GML
 *
 * Revision 1.1.2.3  2006/11/17 11:28:45  ppiqueras
 * Corregidos bugs y aÃ±adida nueva funcionalidad.
 *
 * Revision 1.4  2006/10/11 11:19:58  jorpiell
 * Una nullPointerException que no se comprobaba
 *
 * Revision 1.3  2006/10/10 12:55:06  jorpiell
 * Se ha añadido el soporte de features complejas
 *
 * Revision 1.2  2006/10/02 09:09:45  jorpiell
 * Cambios del 10 copiados al head
 *
 * Revision 1.1.2.1  2006/09/19 12:28:32  jorpiell
 * Ya no se depende de geotools
 *
 * Revision 1.1  2006/09/18 12:07:31  jorpiell
 * Se ha sustituido geotools por el driver de remoteservices
 *
 * Revision 1.1  2006/07/24 07:30:33  jorpiell
 * Se han eliminado las partes duplicadas y se está usando el parser de GML de FMAP.
 *
 * Revision 1.5  2006/06/21 12:35:45  jorpiell
 * Se ha añadido la ventana de propiedades. Esto implica añadir listeners por todos los paneles. Además no se muestra la geomatría en la lista de atributos y se muestran únicamnete los que se van a descargar
 *
 * Revision 1.4  2006/06/14 07:57:19  jorpiell
 * Ya no se usa la estrategia ni se usa geotools para hacer el getFeature. Ahora se usa únicamente para el parseo de GML
 *
 * Revision 1.3  2006/05/25 15:32:01  jorpiell
 * Se ha añadido la funcionalidad para eliminar el namespace de los tipos de atributos
 *
 * Revision 1.2  2006/05/25 10:32:21  jorpiell
 * Se ha renombrado la clase WFSFields por WFSAttributes porque era algo confusa
 *
 * Revision 1.1  2006/05/23 08:07:00  jorpiell
 * Añadido un método para detectar el tipo de geometría
 *
 *
 */
/**
 * This class implements some utils to manage GML
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class WFSUtils {
	
	
	/**
	 * Return the layer geometry
	 * @param layer
	 * Layer with a set of Fields
	 * @return
	 */
	public static String getGeometry(WFSSelectedFeature layer){
   		if (layer.getGeometry() != null){
   			return layer.getGeometry().getName().split(":")[1];
   		}
   		return "";
  
	}
	
	/**
	 * It returns the field type. It has to remove the
	 * namespace to can use the translation mechanism 
	 * translations
	 * @param field
	 * Field to translate
	 */
	public static String getFieldType(IXMLType field){
		if (field == null){
			return "";
		}
		
		if (field.getType() == IXMLType.GML_GEOMETRY){
			return field.getName().split(":")[1];
		}
		
		String sfield = "";
		if (field.getName().split(":").length > 1){
			sfield = field.getName().split(":")[1];
		}else{
			sfield = field.getName();
		}		
		return sfield;
	}
	
	
	public static ArrayList getFields(WFSSelectedFeature[] featuresList,WFSStatus status){
		ArrayList fields = new ArrayList();
		WFSSelectedFeature layer = null;
		for (int i=0 ; i<featuresList.length ; i++){
			if (featuresList[i].getName().equals(status.getFeatureName())){
				layer = featuresList[i];
			}
		}
		Vector selectedFileds = layer.getSelectedFields();
		for (int i=0 ; i<selectedFileds.size() ; i++){
			IXMLType field = (IXMLType)selectedFileds.get(i);
			if (!(field.getType() == IXMLType.GML_GEOMETRY)){
				fields.add(field);
			}
		}
		return fields;
	}

	public static boolean getHasGeometry(WFSSelectedFeature[] featuresList, WFSStatus wfsStatus) {
		WFSSelectedFeature layer = null;
		for (int i=0 ; i<featuresList.length ; i++){
			if (featuresList[i].getName().equals(wfsStatus.getFeatureName())){
				layer = featuresList[i];
			}
		}
		Vector selectedFileds = layer.getSelectedFields();
		for (int i=0 ; i<selectedFileds.size() ; i++){
			XMLElement field = (XMLElement)selectedFileds.get(i);
			if (!((field.getEntityType() != null) && (field.getEntityType().getType() == IXMLType.GML_GEOMETRY))){
				return true;
			}
		}
		return false;
	}
	
	
}
