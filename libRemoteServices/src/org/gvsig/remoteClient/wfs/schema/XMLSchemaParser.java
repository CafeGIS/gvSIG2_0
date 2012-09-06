package org.gvsig.remoteClient.wfs.schema;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.gvsig.remoteClient.utils.CapabilitiesTags;
import org.gvsig.remoteClient.utils.EncodingXMLParser;
import org.gvsig.remoteClient.wfs.schema.type.XMLComplexType;
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
 * $Id: XMLSchemaParser.java 18271 2008-01-24 09:06:43Z jpiera $
 * $Log$
 * Revision 1.7  2007-01-15 13:11:00  csanchez
 * Sistema de Warnings y Excepciones adaptado a BasicException
 *
 * Revision 1.6  2006/12/29 17:15:48  jorpiell
 * Se tienen en cuenta los simpleTypes y los choices, además de los atributos multiples
 *
 * Revision 1.5  2006/12/22 11:25:44  csanchez
 * Nuevo parser GML 2.x para gml's sin esquema
 *
 * Revision 1.4  2006/10/10 12:52:28  jorpiell
 * Soporte para features complejas.
 *
 * Revision 1.3  2006/10/02 08:33:49  jorpiell
 * Cambios del 10 copiados al head
 *
 * Revision 1.1.2.1  2006/09/19 12:23:15  jorpiell
 * Ya no se depende de geotools
 *
 * Revision 1.2  2006/09/18 12:08:55  jorpiell
 * Se han hecho algunas modificaciones que necesitaba el WFS
 *
 * Revision 1.1  2006/08/10 12:00:49  jorpiell
 * Primer commit del driver de Gml
 *
 * Revision 1.1  2006/05/16 14:12:56  jorpiell
 * Añadido el parseador de Esquemas
 * 
 * 
 */
/**
 * Thas class is used to parse a schema XSD
 * 
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 * @author Carlos Sánchez Periñán (sanchez_carper@gva.es)
 * 
 */
public class XMLSchemaParser extends EncodingXMLParser {
	private String targetNameSpace = null;
	private String schema = "";
	private String encoding = "UTF-8";	
	private String nameSpace = "";
	private String version = null;
	
	
	public XMLSchemaParser(){
		super();		
	}
	
	public XMLSchemaParser(String schema){
		super();
		//schema instace is named with the string in "schema"
		this.schema = schema;		
	}
	
	/**
	 * It gets the schema from a tag. The schema is separated
	 * of the tag name by ":".
	 * @param tag
	 */
	public void setSchemaFromMainTag(String tag){
		//set the name string of the namespace.
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
		//get the tag without the namespace
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
	
	/*
	 *  (non-Javadoc)
	 * @see org.xmlpull.v1.XmlPullParser#getName()
	 */
	public String getNameSpace(){
		try{
		String name = super.getName();
		if ((name!=null)&&(name.split(":").length > 1)){
			return name.split(":")[0];
		}
		return "";
		}catch (NullPointerException e){
			return "";
		}
	}
	
	/********************************************************************************
	 *  FUNCTION PARSE(FILE,NAMESPACE) PARSING THE XML SCHEMA ".xsd"			   	*
	 * 																			   	*
	 *  	-gml version													 		*
	 *  	-elementFormDefault {qualified,non-qualified} (not implemented yet)		*
	 *  	-namespaces "xmlns" (nor implemented,only in gml file)		 			*
	 *  	-targetNamespace (the name of this schema)					 			*
	 *		-imports (not implemented)									   			*  
	 *		-features -> "elements"="types"										  	*
	 *																				*	
	 * @param f (file ".xsd" to parse)												*
	 * @param nameSpace (Schemas name)												*
	 * @throws IOException 															*
	 * @throws XmlPullParserException 												*
	 *																				*
	 ********************************************************************************/
	public void parse(File f,String nameSpace) throws XmlPullParserException, IOException{
		this.nameSpace = nameSpace;
		FileReader reader = null;       
		int tag;
		
		setInput(f);
		nextTag();
			
		//Parsing attributes from the head tag...
		if ( getEventType() != KXmlParser.END_DOCUMENT ) 
		{     
			/************************
			 * Etiqueta <schema>	*
			 ************************/
			setSchemaFromMainTag(getName());
			//Searching the init tag "schema"
			require(KXmlParser.START_TAG, null, CapabilitiesTags.WFS_SCHEMAROOT); 
			for (int i=0 ; i<getAttributeCount() ; i++){
				//getting attributes and values
				String attName = getAttributeName(i);
				String attValue = getAttributeValue(i);
				
				/********************************
				 * Atributo <targetNamespace>	*
				 ********************************/
				//Target Namespace (URI)
				//this is the namespace of all components in the schema
				//setTargetNamespace(); 
				if (attName.compareTo(GMLTags.XML_TARGET_NAMESPACE)==0){
					targetNameSpace = attValue;
				}
				
				/************************************
				 * Atributo <elementFormDefault>	*
				 ************************************/
				//Qualified--> Los elementos del espacio de nombres de destino deben cualificarse 
				//con el prefijo del espacio de nombres.
				//Unqualified--> No es necesario que los elementos del 
				//espacio de nombres de destino estén cualificados con el prefijo del espacio de nombres.
				//(Espacion_de_Nombres:Elemento)
				//elementFormDefault(); 
				if (attName.compareTo(GMLTags.XML_ELEMENT_FORM_DEFAULT)==0){
				}
				
				/************************************
				 * Atributo <attributeFormDefault>	*
				 ************************************/
				//Lo mismo que el anterior pero con los atributos...
				//(Espacio_de_Nombres:Atributo)
				//attributeFormDefault();
				if (attName.compareTo(GMLTags.XML_ATTRIBUTE_FORM_DEFAULT)==0){
				}
				
				/************************
				 * Atributo <Version>	*
				 ************************/
				//Gets gml version to parse by the right parser.
				//getversion();
				if (attName.compareTo(GMLTags.VERSION)==0){
					version=attValue;
				}
			}
			tag = nextTag();
			
			while(tag != KXmlParser.END_DOCUMENT)
			{
				switch(tag)
				{
				case KXmlParser.START_TAG:
					
					/************************
					 * Etiqueta <import>	*
					 ************************/
					//imports elements from other schemas (other schema  ".xsd" files)
					
					/****************************
					 * Etiqueta <complexType>	*
					 ****************************/
					if (getName().compareTo(CapabilitiesTags.COMPLEXTYPE)==0){							
						for (int i=0 ; i<getAttributeCount() ; i++){
							/********************
							 * Atributo <name>	*
							 ********************/
							if (getAttributeName(i).compareTo(GMLTags.GML_NAME) == 0){
								// inserts a new complex type inside the namespace
								XMLComplexType complexType = XMLTypesFactory.addComplexType(nameSpace,getAttributeValue(i));
				    			parseComplexType(complexType);					    			
				    		}
				    		
						}
					}
					/****************************
					 * Etiqueta <simpleType>	*
					 ****************************/
					// SIMPLE TYPE elements like enumerations not implemented 
					else if (getName().compareTo(CapabilitiesTags.SIMPLETYPE)==0){
						parseSimpleType();
					}
					/************************
					 * Etiqueta <element>	*
					 ************************/
					else if (getName().compareTo(CapabilitiesTags.ELEMENT)==0){							
						XMLElement entity = XMLElementsFactory.addType(this);						
					}
					break;
					case KXmlParser.END_TAG:                            
						break;
					//Show the Text on the screen
					case KXmlParser.TEXT:
						                
						break;
				}
				tag = next();
			}
			require(KXmlParser.END_DOCUMENT, null, null);
		}
	}
	
	/****************************************************
	 *  FUNCTION PARSE SIMPLE TYPE()		 		   	*
	 * 											    	*
	 *  Parse simple types and its restrictions		 	*
	 *													*	
	 ****************************************************/
    private void parseSimpleType() throws IOException, XmlPullParserException{   
		int currentTag;
		boolean end = false;		
		currentTag = getEventType();
		
		String typeName = null;
		String typeValue = null;

		for (int i=0 ; i<getAttributeCount() ; i++){			
			if (getAttributeName(i).compareTo(CapabilitiesTags.ELEMENT_NAME) == 0){
				typeName = getAttributeValue(i);
			}
		}
		
		while (!end){
			switch(currentTag){
			case KXmlParser.START_TAG:
				if (getName().compareTo(CapabilitiesTags.RESTRICTION)==0){
					for (int i=0 ; i<getAttributeCount() ; i++){
						if (getAttributeName(i).compareTo(CapabilitiesTags.BASE) == 0){
							typeValue = getAttributeValue(i);
						}
					}					
				}   
				//Falta parsear los tipos enumerados
				break;
			case KXmlParser.END_TAG:
				if (getName().compareTo(CapabilitiesTags.SIMPLETYPE) == 0)
					end = true;
				break;
			case KXmlParser.TEXT:                   
				break;
			}
			if (!end){
				currentTag = next();
			}			
		}
		if ((typeName != null) && (typeValue != null)){
			XMLTypesFactory.addSimpleType(typeName,typeValue);
		}
	}
    
    /****************************************************
	 *  FUNCTION PARSE COMPLEX TYPE(COMPLEX TYPE) 	   	*
	 * 												   	*
	 *  Parse the attributes from a complex type		*
	 *													*	
	 * @param complexType								*
	 *													*
	 ****************************************************/
	
	private void parseComplexType(XMLComplexType complexType) throws IOException, XmlPullParserException{   
		int currentTag;
		boolean end = false;		
		
		require(KXmlParser.START_TAG, null, CapabilitiesTags.COMPLEXTYPE);
		currentTag = next();
		
		while (!end) 
		{
			switch(currentTag)
			{
			case KXmlParser.START_TAG:
				/********************************
				 * Etiqueta <complexContent>	*
				 ********************************/
				if (getName().compareTo(CapabilitiesTags.COMPLEXCONTENT)==0){
					parseComplexContent(complexType); 
				}
				/************************
				 * Etiqueta <sequence>	*
				 ************************/
				else if(getName().compareTo(CapabilitiesTags.SEQUENCE)==0){
					parseSequence(complexType);
				}
				/************************
				 * Etiqueta <choice>	*
				 ************************/
				else if(getName().compareTo(CapabilitiesTags.CHOICE)==0){
					parseChoice(complexType);
				}
				break;
			case KXmlParser.END_TAG:
				if (getName().compareTo(CapabilitiesTags.COMPLEXTYPE) == 0)
					end = true;
				break;
			case KXmlParser.TEXT:                   
				break;
			}
			if (!end){
				currentTag = next();
			}	
		}		
	}
	
	/************************************************************************************
	 *  FUNCION PARSE COMPLEX CONTENT(COMPLEX TYPE)									   	*
	 * 																				   	*
	 *  Parsea los atributos que componen un contenido complejo 					 	*
	 *																					*	
	 * @param complexType (elemento complejo a parsear)									*
	 *																					*
	 ************************************************************************************/
	
	private void parseComplexContent(XMLComplexType complexType) throws IOException, XmlPullParserException
	{   
		int currentTag;
		boolean end = false;
		
		//Establece como requisito abrir una etiqueta <complexContent>
		require(KXmlParser.START_TAG, null, CapabilitiesTags.COMPLEXCONTENT);
		currentTag = next();
		
		while (!end) 
		{
			switch(currentTag)
			{
			//Comparo las posibles etiquetas de apertura, las cuales solo nos interesa <extension>
			case KXmlParser.START_TAG:
				if (getName().compareTo(CapabilitiesTags.EXTENSION )==0){
					parseExtension(complexType); 
				}else if (getName().compareTo(CapabilitiesTags.RESTRICTION)==0){
					parseRestriction(complexType); 
				} 
				break;
			//encuentro una etiqueta de cierre de complexContent con lo cual cierro el tipo complejo
			case KXmlParser.END_TAG:
				if (getName().compareTo(CapabilitiesTags.COMPLEXCONTENT) == 0)
					end = true;
				break;
			//Siempre puede existir texto por el medio que ignoro
			case KXmlParser.TEXT:                   
				break;
			}
			if (!end){
				currentTag = next();
			}	
		}
	}
	
	//Comprueba que las etiquetas extension se abren y se cierran
	private void parseExtension(XMLComplexType complexType) throws IOException, XmlPullParserException
	{   
		int currentTag;
		boolean end = false;
				
		require(KXmlParser.START_TAG, null, CapabilitiesTags.EXTENSION);
		for (int i=0 ; i<getAttributeCount() ; i++){
			String attName = getAttributeName(i);
			String attValue = getAttributeValue(i);
			if (CapabilitiesTags.BASE.equals(attName)){
				complexType.setBaseType(attValue);				
			}
		}
		
		currentTag = next();		
		while (!end) 
		{
			switch(currentTag)
			{
			case KXmlParser.START_TAG:
				if (getName().compareTo(CapabilitiesTags.SEQUENCE)==0)
				{
					parseSequence(complexType); 
				}   
				break;
			case KXmlParser.END_TAG:
				if (getName().compareTo(CapabilitiesTags.EXTENSION) == 0)
					end = true;
				break;
			case KXmlParser.TEXT:                   
				break;
			}
			if (!end){
				currentTag = next();
			}			
		}
	}
	
	//Comprueba que las etiquetas extension se abren y se cierran
	private void parseRestriction(XMLComplexType complexType) throws IOException, XmlPullParserException
	{   
		int currentTag;
		boolean end = false;
		
		
		require(KXmlParser.START_TAG, null, CapabilitiesTags.RESTRICTION);
		currentTag = next();
		
		while (!end) 
		{
			switch(currentTag)
			{
			case KXmlParser.START_TAG:
				if (getName().compareTo(CapabilitiesTags.SEQUENCE)==0)
				{
					parseSequence(complexType); 
				}   
				break;
			case KXmlParser.END_TAG:
				if (getName().compareTo(CapabilitiesTags.RESTRICTION) == 0)
					end = true;
				break;
			case KXmlParser.TEXT:                   
				break;
			}
			if (!end){
				currentTag = next();
			}			
		}
	}
	
	private void parseSequence(XMLComplexType complexType) throws IOException, XmlPullParserException
	{   
		int currentTag;
		boolean end = false;
		XMLElement elemento_previo=null;
		
		require(KXmlParser.START_TAG, null, CapabilitiesTags.SEQUENCE);
		currentTag = next();
		
		while (!end) 
		{
			switch(currentTag)
			{
			case KXmlParser.START_TAG:
				/************************
				 * Etiqueta <Element>	*
				 ************************/
				if (getName().compareTo(CapabilitiesTags.ELEMENT)==0){
					XMLElement element = new XMLElement(this);
					if (element != null){
						complexType.addElements(element);
					}
					elemento_previo=element;
				}
				/****************************
				 * Etiqueta <ComplexType>	*
				 ****************************/
				else if(getName().compareTo(CapabilitiesTags.COMPLEXTYPE)==0){
					for (int i=0 ; i<getAttributeCount() ; i++){
			    		if (getAttributeName(i).compareTo(GMLTags.GML_NAME) == 0){
			    			XMLComplexType complexTypeChild = XMLTypesFactory.addComplexType(nameSpace,getAttributeValue(i));
			    			parseComplexType(complexTypeChild);					    			
			    		}			    		
					}
				}
				else if(getName().compareTo(CapabilitiesTags.SIMPLETYPE)==0){
					elemento_previo.parseSimpleType(this);
				}
				break;
			case KXmlParser.END_TAG:
				if (getName().compareTo(CapabilitiesTags.SEQUENCE) == 0)
					end = true;
				break;
			case KXmlParser.TEXT:                   
				break;
			}
			if (!end){
				currentTag = next();
			}
		}		
	}

	private void parseChoice(XMLComplexType complexType) throws IOException, XmlPullParserException
	{   
		int currentTag;
		boolean end = false;
			
		require(KXmlParser.START_TAG, null, CapabilitiesTags.CHOICE);
		currentTag = next();
		
		complexType.setAttributesType(XMLComplexType.CHOICE_TYPE);
		
		while (!end) 
		{
			switch(currentTag)
			{
			case KXmlParser.START_TAG:
				/************************
				 * Etiqueta <Element>	*
				 ************************/
				if (getName().compareTo(CapabilitiesTags.ELEMENT)==0){
					XMLElement element = new XMLElement(this);
					if (element != null){
						complexType.addElements(element);
					}					
				}				
				break;
			case KXmlParser.END_TAG:
				if (getName().compareTo(CapabilitiesTags.CHOICE) == 0)
					end = true;
				break;
			case KXmlParser.TEXT:                   
				break;
			}
			if (!end){
				currentTag = next();
			}
		}		
	}

	public String getversion() {
		if (version == null){
			//return the default GML version
			return "99.99.99";
		}
		return version;		
	}
	public String getTargetNamespace() {
		return targetNameSpace;		
	}
}
