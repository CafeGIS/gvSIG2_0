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
*   Instituto de Desarrollo Regional (Universidad de Castilla La-Mancha
*   Campus Universitario s/n
*   02071 Alabacete
*   Spain
*
*   +34 967 599 200
*/

package org.gvsig.crs.persistence;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import com.iver.andami.PluginServices;
import com.iver.utiles.DateTime;
import com.iver.utiles.XMLEntity;

/**
 * This class is used to save a list of CRSs (using the
 * Andami persistence model) to the plugins-persistence.xml file.
 * It has methods to create a set of CrsData objects  from an
 * xml file. It can also save a set of CRSs objects in an
 * xml file.
 *
 * @author Diego Guerrero Sevilla diego.guerrero@uclm.es
 */
public class RecentCRSsPersistence {
	private XMLEntity xml = null;
	private XMLEntity recentsXml = null;
	private PluginServices ps = null;
	private static final String CRS_RECENTS = "recentCrss";
	private static final String CRS_AUTHORITY = "authority";
	private static final String CRS_NAME = "name";
	private static final String CRS_CODE = "code";
	private static final String CRS_DATE = "date";
	
	/**
	 * Constructor
	 */
	public RecentCRSsPersistence(){
		ps = PluginServices.getPluginServices("org.gvsig.crs");
		xml = ps.getPersistentXML();
		for (int child = 0; child<xml.getChildrenCount();child++)
			if (xml.getChild(child).getPropertyValue(0).equals(CRS_RECENTS))
				recentsXml = xml.getChild(child); 
		if (recentsXml == null){
			XMLEntity xmlEntity = new XMLEntity();
			xmlEntity.putProperty("groupName", CRS_RECENTS);
			xml.addChild(xmlEntity);
			recentsXml = xmlEntity;
		}
	}
	

	/**
	 * This methos is used to save the information in an XML file
	 */
	public void setPersistent(){
		ps.setPersistentXML(xml);
	}

	/**
	 * This method saves an array of CrsData using the Anadami
	 * persistence model
	 * @param crsss
	 * Array of crss
	 */
	public void setArrayOfCrsData(CrsData[] crss){
		recentsXml.getXmlTag().removeAllXmlTag();

		for (int i=0 ; i<crss.length ; i++){
			recentsXml.addChild(crsDataToXml(crss[i]));
		}

	}

	/**
	 * This method adds a CrsData using the Anadami
	 * persistence model. If the Crs exists just actualizes
	 * the type name and the wkt string.
	 * @param crs
	 * CrsData
	 */
	public void addCrsData(CrsData crs){
		CrsData[] crss = getArrayOfCrsData();

		boolean found = false;

		for (int i = 0; i < crss.length; i++) {
			if (crss[i].getAuthority().equals(crs.getAuthority())&& crss[i].getCode()==crs.getCode()) {
				found = true;
				crss[i].setName(crs.getName());
				crss[i].setProperies(crs.getProperies());
				crss[i].updateLastAccess();
				setArrayOfCrsData(crss);
			}
		}
		
		if (!found) {
			if (crss.length<10){ //Límite de almacenamiento: 10 CRSs
				CrsData[] newCrss = new CrsData[crss.length + 1];
				System.arraycopy(crss, 0, newCrss, 0, crss.length);
				newCrss[crss.length] = crs;
				setArrayOfCrsData(newCrss);
			}
			else {
				Arrays.sort(crss);
				crss[0]=crs;
				setArrayOfCrsData(crss);
			}
		}
	}



	/**
	 * This method returns an array of CrsData objects that
	 * have been saved using the Andami persistence model.
	 * @return
	 * CrsData[]
	 */
	public CrsData[] getArrayOfCrsData(){
		CrsData[] crss = new CrsData[recentsXml.getChildrenCount()];
		for (int i=0 ; i<recentsXml.getChildrenCount() ; i++){
			crss[i] = xmlToCrsData(recentsXml.getChild(i));
		}
		
		/*
		 * Ordenar el array por fechas
		 */
		Arrays.sort(crss);
		
		return crss;
	}


	/**
	 * This method creates and returns a new XMLEntity.
	 * @param crs
	 * CrsData with all the Crs information
	 * @return
	 * XMLEntity
	 */
	public XMLEntity crsDataToXml(CrsData crs){
		String dFormat="Y-m-d H:i:s.Z";
	
		XMLEntity xmlEnt = new XMLEntity();
		xmlEnt.putProperty(CRS_AUTHORITY,crs.getAuthority());
		xmlEnt.putProperty(CRS_CODE,Integer.toString(crs.getCode()));
		xmlEnt.putProperty(CRS_NAME,(crs.getName()));
		xmlEnt.putProperty(CRS_DATE,DateTime.dateToString(crs.getDate(),dFormat));
		Set keys = crs.getProperies().keySet();
		Iterator it = keys.iterator();
	    while (it.hasNext()) {
	    	String next = (String)it.next();
	    	xmlEnt.putProperty(next,
	    			crs.getProperies().get(next));
	    }		   
		return xmlEnt;
	}

	/**
	 * This method creates a new CrsData from a XMLEntity
	 * @param xmlEnt
	 * XMLRntity that contains the Crs information
	 * @return
	 * CrsData
	 */
	public CrsData xmlToCrsData(XMLEntity xmlEnt){
		String authority = "";
		int code = 0;
		String name = "";
		Date date = null;

		authority = xmlEnt.getStringProperty(CRS_AUTHORITY);
		code = Integer.valueOf(xmlEnt.getStringProperty(CRS_CODE)).intValue();
		name = xmlEnt.getStringProperty(CRS_NAME);
		date = DateTime.stringToDate(xmlEnt.getStringProperty(CRS_DATE));
		
		CrsData crsData = new CrsData(authority,code,name,date);
		
		Properties props = new Properties();
		for (int i=0 ; i<xmlEnt.getPropertyCount() ; i++){
			String property = xmlEnt.getPropertyName(i);
			if (!((property.equals(CRS_AUTHORITY)) ||
				(property.equals(CRS_CODE)) ||
				(property.equals(CRS_NAME)) ||
				(property.equals(CRS_DATE)))){
					props.put(property,xmlEnt.getStringProperty(property));
				}					
		}
		crsData.setProperies(props);
		return crsData;
	}

	public XMLEntity getXml() {
		return xml;
	}

	public void setXml(XMLEntity xml) {
		this.xml = xml;
	}
}
