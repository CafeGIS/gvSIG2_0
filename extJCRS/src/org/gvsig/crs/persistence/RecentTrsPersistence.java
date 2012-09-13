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
 * This class is used to save a list of Transfomations (using the
 * Andami persistence model) to the plugins-persistence.xml file.
 * It has methods to create a set of TrData objects  from an
 * xml file. It can also save a set of TrData objects in an
 * xml file.
 *
 * @author Diego Guerrero Sevilla diego.guerrero@uclm.es
 */
public class RecentTrsPersistence {
	private XMLEntity xml = null;
	private XMLEntity recentsXml = null;
	private PluginServices ps = null;
	private static final String TR_RECENTS = "recentTransformations";
	private static final String TR_AUTHORITY = "authority";
	private static final String TR_NAME = "name";
	private static final String TR_CODE = "code";
	private static final String TR_CRS_SOURCE = "crsSource";
	private static final String TR_CRS_TARGET = "crsTarget";
	private static final String TR_DETAILS = "details";
	private static final String TR_DATE = "date";
	
	
	/**
	 * Constructor
	 */
	public RecentTrsPersistence(){
		ps = PluginServices.getPluginServices("org.gvsig.crs");
		xml = ps.getPersistentXML();
		for (int child = 0; child<xml.getChildrenCount();child++)
			if (xml.getChild(child).getPropertyValue(0).equals(TR_RECENTS))
				recentsXml = xml.getChild(child); 
		if (recentsXml == null){
			XMLEntity xmlEntity = new XMLEntity();
			xmlEntity.putProperty("groupName", TR_RECENTS);
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
	 * This method saves an array of TrData using the Anadami
	 * persistence model
	 * @param tr
	 * Array of trData to save.
	 */
	public void setArrayOfTrData(TrData[] trs){
		recentsXml.getXmlTag().removeAllXmlTag();

		for (int i=0 ; i<trs.length ; i++){
			recentsXml.addChild(trDataToXml(trs[i]));
		}

	}

	/**
	 * This method adds a TrData using the Anadami
	 * persistence model. If the Transformation exists just actualizes
	 * the type name and the wkt string.
	 * @param tr
	 * TrData
	 */
	public void addTrData(TrData tr){
		TrData[] trs = getArrayOfTrData();

		boolean found = false;

		for (int i = 0; i < trs.length; i++) {
			if (trs[i].getAuthority().equals(tr.getAuthority())&& trs[i].getCode()==tr.getCode() 
					&& trs[i].getCrsSource().equals(tr.getCrsSource()) && trs[i].getCrsTarget().equals(tr.getCrsTarget()) && trs[i].getDetails().equals(tr.getDetails())) {
				found = true;
				trs[i].setName(tr.getName());
				trs[i].setProperies(tr.getProperies());
				trs[i].updateLastAccess();
				setArrayOfTrData(trs);
			}
		}
		
		if (!found) {
			if (trs.length<10){ //Límitie de almacenamiento: 10 Transf.
				TrData[] newTrs = new TrData[trs.length + 1];
				System.arraycopy(trs, 0, newTrs, 0, trs.length);
				newTrs[trs.length] = tr;
				setArrayOfTrData(newTrs);
			}
			else {
				Arrays.sort(trs);
				trs[0]=tr;
				setArrayOfTrData(trs);
			}
		}
	}



	/**
	 * This method returns an array of TrData objects that
	 * have been saved using the Andami persistence model.
	 * @return
	 * TrData[]
	 */
	public TrData[] getArrayOfTrData(){
		TrData[] trs = new TrData[recentsXml.getChildrenCount()];
		for (int i=0 ; i<recentsXml.getChildrenCount() ; i++){
			trs[i] = xmlToTrData(recentsXml.getChild(i));
		}
		
		/*
		 * Ordenar el array por fechas
		 */
		Arrays.sort(trs);
		
		return trs;
	}


	/**
	 * This method creates and returns a new XMLEntity.
	 * @param tr
	 * TrData with all the Transformation information
	 * @return
	 * XMLEntity
	 */
	public XMLEntity trDataToXml(TrData tr){
		String dFormat="Y-m-d H:i:s.Z";
	
		XMLEntity xmlEnt = new XMLEntity();
		xmlEnt.putProperty(TR_AUTHORITY,tr.getAuthority());
		xmlEnt.putProperty(TR_CODE,Integer.toString(tr.getCode()));
		xmlEnt.putProperty(TR_NAME,(tr.getName()));
		xmlEnt.putProperty(TR_CRS_SOURCE,(tr.getCrsSource()));
		xmlEnt.putProperty(TR_CRS_TARGET,(tr.getCrsTarget()));
		xmlEnt.putProperty(TR_DETAILS,(tr.getDetails()));
		xmlEnt.putProperty(TR_DATE,DateTime.dateToString(tr.getDate(),dFormat));
		Set keys = tr.getProperies().keySet();
		Iterator it = keys.iterator();
	    while (it.hasNext()) {
	    	String next = (String)it.next();
	    	xmlEnt.putProperty(next,
	    			tr.getProperies().get(next));
	    }	
	    
	    if (tr instanceof CompTrData) {
	    	CompTrData compTr = (CompTrData) tr;
	    	xmlEnt.addChild(trDataToXml(compTr.getFirstTr()));
	    	xmlEnt.addChild(trDataToXml(compTr.getSecondTr()));
		}
	    
		return xmlEnt;
	}

	/**
	 * This method creates a new TrData from a XMLEntity
	 * @param xmlEnt
	 * XMLRntity that contains the Tr information
	 * @return
	 * TrData
	 */
	public TrData xmlToTrData(XMLEntity xmlEnt){
		
		TrData trData = null;
		
		Properties props = null;
		
		String authority = "";
		int code = 0;
		String name = "";
		String crsSource = "";
		String crsTarget = "";
		String details = "";
		Date date = null;

		authority = xmlEnt.getStringProperty(TR_AUTHORITY);
		code = Integer.valueOf(xmlEnt.getStringProperty(TR_CODE)).intValue();
		name = xmlEnt.getStringProperty(TR_NAME);
		crsSource = xmlEnt.getStringProperty(TR_CRS_SOURCE);
		crsTarget = xmlEnt.getStringProperty(TR_CRS_TARGET);
		details = xmlEnt.getStringProperty(TR_DETAILS);
		date = DateTime.stringToDate(xmlEnt.getStringProperty(TR_DATE));
		
		if (xmlEnt.getChildrenCount()==2){
			authority = xmlEnt.getChild(0).getStringProperty(TR_AUTHORITY);
			code = Integer.valueOf(xmlEnt.getChild(0).getStringProperty(TR_CODE)).intValue();
			name = xmlEnt.getChild(0).getStringProperty(TR_NAME);
			crsSource = xmlEnt.getChild(0).getStringProperty(TR_CRS_SOURCE);
			crsTarget = xmlEnt.getChild(0).getStringProperty(TR_CRS_TARGET);
			details = xmlEnt.getChild(0).getStringProperty(TR_DETAILS);
			date = DateTime.stringToDate(xmlEnt.getChild(0).getStringProperty(TR_DATE));
			TrData firstTr = new TrData(authority,code,name,crsSource,crsTarget,details,date);
			
			props = new Properties();
			for (int i=0 ; i<xmlEnt.getPropertyCount() ; i++){
				String property = xmlEnt.getChild(0).getPropertyName(i);
				if (!((property.equals(TR_AUTHORITY)) ||
					(property.equals(TR_CODE)) ||
					(property.equals(TR_NAME)) ||
					(property.equals(TR_CRS_SOURCE)) ||
					(property.equals(TR_CRS_TARGET)) ||
					(property.equals(TR_DETAILS)) ||
					(property.equals(TR_DATE)))){
						props.put(property,xmlEnt.getChild(0).getStringProperty(property));
					}					
			}
			firstTr.setProperies(props);
			
			authority = xmlEnt.getChild(1).getStringProperty(TR_AUTHORITY);
			code = Integer.valueOf(xmlEnt.getChild(1).getStringProperty(TR_CODE)).intValue();
			name = xmlEnt.getChild(1).getStringProperty(TR_NAME);
			crsSource = xmlEnt.getChild(1).getStringProperty(TR_CRS_SOURCE);
			crsTarget = xmlEnt.getChild(1).getStringProperty(TR_CRS_TARGET);
			details = xmlEnt.getChild(1).getStringProperty(TR_DETAILS);
			date = DateTime.stringToDate(xmlEnt.getChild(1).getStringProperty(TR_DATE));
			TrData secondTr = new TrData(authority,code,name,crsSource,crsTarget,details,date);
			
			props = new Properties();
			for (int i=0 ; i<xmlEnt.getPropertyCount() ; i++){
				String property = xmlEnt.getChild(0).getPropertyName(i);
				if (!((property.equals(TR_AUTHORITY)) ||
					(property.equals(TR_CODE)) ||
					(property.equals(TR_NAME)) ||
					(property.equals(TR_CRS_SOURCE)) ||
					(property.equals(TR_CRS_TARGET)) ||
					(property.equals(TR_DETAILS)) ||
					(property.equals(TR_DATE)))){
						props.put(property,xmlEnt.getChild(0).getStringProperty(property));
					}					
			}
			secondTr.setProperies(props);
			
			trData = new CompTrData(firstTr,secondTr);
		}
		else{
			authority = xmlEnt.getStringProperty(TR_AUTHORITY);
			code = Integer.valueOf(xmlEnt.getStringProperty(TR_CODE)).intValue();
			name = xmlEnt.getStringProperty(TR_NAME);
			crsSource = xmlEnt.getStringProperty(TR_CRS_SOURCE);
			crsTarget = xmlEnt.getStringProperty(TR_CRS_TARGET);
			details = xmlEnt.getStringProperty(TR_DETAILS);
			date = DateTime.stringToDate(xmlEnt.getStringProperty(TR_DATE));	
			
			trData = new TrData(authority,code,name,crsSource,crsTarget,details,date);
		}
		
		props = new Properties();
		for (int i=0 ; i<xmlEnt.getPropertyCount() ; i++){
			String property = xmlEnt.getPropertyName(i);
			if (!((property.equals(TR_AUTHORITY)) ||
				(property.equals(TR_CODE)) ||
				(property.equals(TR_NAME)) ||
				(property.equals(TR_CRS_SOURCE)) ||
				(property.equals(TR_CRS_TARGET)) ||
				(property.equals(TR_DETAILS)) ||
				(property.equals(TR_DATE)))){
					props.put(property,xmlEnt.getStringProperty(property));
				}					
		}
		trData.setProperies(props);
		return trData;
	}

	public XMLEntity getXml() {
		return xml;
	}

	public void setXml(XMLEntity xml) {
		this.xml = xml;
	}
}
