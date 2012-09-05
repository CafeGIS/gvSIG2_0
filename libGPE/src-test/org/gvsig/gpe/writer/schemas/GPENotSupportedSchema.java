package org.gvsig.gpe.writer.schemas;

import org.gvsig.xmlschema.som.IXSComplexContent;
import org.gvsig.xmlschema.som.IXSComplexTypeDefinition;
import org.gvsig.xmlschema.som.IXSContentType;
import org.gvsig.xmlschema.som.IXSElementDeclaration;

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
 * $Id: GPENotSupportedSchema.java 162 2007-06-29 12:19:48Z jorpiell $
 * $Log$
 * Revision 1.1  2007/06/29 12:19:14  jorpiell
 * The schema validation is made independently of the concrete writer
 *
 *
 */
/**
 * Abstract class that creates a default schema that
 * can be used by other classes to do tests
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public abstract class GPENotSupportedSchema extends GPEWriterWithSchemaBaseTest{
	private String xsLayerName = "cities";
	private String xsLayerType = "citiesType";
	private String xsLayerType_ = IXSComplexTypeDefinition.SEQUENCE;	
	private String xsFeature1Name = "city";
	private String xsFeature1Type = "cityType";	
	private String xsFeature1Type_ = IXSComplexTypeDefinition.SEQUENCE;	
	private String xsFeature1ContentType = IXSContentType.WITOUT_CONTENT;
	private String xsFeature1ContentRestriction = IXSContentType.WITOUT_RESTRICTION;	
	private String xsFeature1Element1Name = "Population";
	private String xsFeature1Element1Type = "xs:integer";
	private String xsFeature1Element2Name = "Country";
	private String xsFeature1Element2Type = "xs:string";
	private String xsFeature1Element3Name = "Capital";
	private String xsFeature1Element3Type = "xs:boolean";
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.schemas.GPEWriterWithSchemaBaseTest#writeSchema()
	 */
	public void writeSchema(){
		getSchema().addElement(xsLayerName, xsLayerType);
		IXSComplexTypeDefinition citiesType = getSchema().addComplexType(
				xsLayerType,
				xsLayerType_, 
				IXSComplexContent.WITOUT_CONTENT ,
				IXSComplexContent.WITOUT_RESTRICTION);
		IXSElementDeclaration city = citiesType.addElement(xsFeature1Name,
				xsFeature1Type,
				true,
				0,
				IXSElementDeclaration.MAX_OCCURS_UNBOUNDED);		
		IXSComplexTypeDefinition cityType = city.addComplexType(xsFeature1Type_, xsFeature1ContentType, xsFeature1ContentRestriction);		
		cityType.addElement(
				xsFeature1Element1Name,
				xsFeature1Element1Type);		
		cityType.addElement(
				xsFeature1Element2Name,
				xsFeature1Element2Type);	
		cityType.addElement(
				xsFeature1Element3Name,
				xsFeature1Element3Type);
	}	
}
