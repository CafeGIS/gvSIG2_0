package org.gvsig.remoteClient.wfs;

import java.io.IOException;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParserException;

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
 * $Id: WFSSchemaParser.java 5215 2006-05-16 14:12:56Z jorpiell $
 * $Log$
 * Revision 1.1  2006-05-16 14:12:56  jorpiell
 * Añadido el parseador de Esquemas
 *
 *
 */
/**
 * 
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class WFSSchemaParser extends KXmlParser {
	private String schema = "";
	
	public WFSSchemaParser(){
		super();
	}
	
	public WFSSchemaParser(String schema){
		this.schema = schema;
	}
	
	/**
	 * It gets the schema from a tag. The schema is separated
	 * of the tag name by ":".
	 * @param tag
	 */
	public void setSchemaFromMainTag(String tag){
		int pos = tag.indexOf(":");
		if (pos > 0){
			this.schema = tag.substring(0,pos);
		}else{
			this.schema = "";
		}
	}	

	/**
	 * @return Returns the schema.
	 */
	public String getSchema() {
		return schema;
	}

	/**
	 * @param schema The schema to set.
	 */
	public void setSchema(String schema) {
		this.schema = schema;
	}	
	
	/**
	 * Returns a SCHEMA:TAG
	 * @param tag
	 * @return SCHEMA:TAG
	 */
	private String getTag(String tag){
		if (tag == null){
			return null;
		}
		if ((schema == null) || (schema.equals(""))){
			return tag;
		}
		return schema + ":" + tag;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.xmlpull.v1.XmlPullParser#require(int, java.lang.String, java.lang.String)
	 */
	public void require(int type, String namespace, String name)
		throws XmlPullParserException, IOException{
		super.require(type,namespace,getTag(name));
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.xmlpull.v1.XmlPullParser#getName()
	 */
	public String getName(){
		try{
		String name = super.getName();
		if ((schema != null) || (!(schema.equals("")))){
			return name.substring(name.indexOf(":") + 1,name.length());
		}
		return name;
		}catch (NullPointerException e){
			return "";
		}
	}
	
}
