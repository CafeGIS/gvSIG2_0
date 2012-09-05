package org.gvsig.gpe.gml.parser.v2.geometries;

import java.io.IOException;
import java.util.Map;

import javax.xml.namespace.QName;

import org.gvsig.gpe.gml.parser.GPEDefaultGmlParser;
import org.gvsig.gpe.gml.utils.GMLProjectionFactory;
import org.gvsig.gpe.gml.utils.GMLTags;
import org.gvsig.gpe.gml.utils.GMLUtilsParser;
import org.gvsig.gpe.parser.IGPEErrorHandler;
import org.gvsig.gpe.xml.stream.IXmlStreamReader;
import org.gvsig.gpe.xml.stream.XmlStreamException;
import org.gvsig.gpe.xml.utils.CompareUtils;
import org.gvsig.gpe.xml.utils.XMLAttributesIterator;

/* gvSIG. Sistema de InformaciÛn Geogr·fica de la Generalitat Valenciana
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
 *   Av. Blasco Ib·Òez, 50
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
 * $Id:GeometryBinding.java 195 2007-11-26 09:02:22Z jpiera $
 * $Log$
 * Revision 1.7  2007/05/24 07:29:47  csanchez
 * A√±adidos Alias GML2
 *
 * Revision 1.6  2007/05/18 10:41:01  csanchez
 * Actualizaci√≥n libGPE-GML eliminaci√≥n de clases inecesarias
 *
 * Revision 1.5  2007/05/15 11:55:11  jorpiell
 * MultiGeometry is now supported
 *
 * Revision 1.4  2007/05/15 07:30:38  jorpiell
 * Add the geometryProperties tags
 *
 * Revision 1.3  2007/05/14 11:18:51  jorpiell
 * ProjectionFactory updated
 *
 * Revision 1.2  2007/05/14 09:31:06  jorpiell
 * Add the a new class to compare tags
 *
 * Revision 1.1  2007/05/08 10:24:16  jorpiell
 * Add comments to create javadocs
 *
 * Revision 1.1  2007/05/07 12:58:42  jorpiell
 * Add some methods to manage the multigeometries
 *
 * Revision 1.1  2007/05/07 07:06:46  jorpiell
 * Add a constructor with the name and the description fields
 *
 *
 */
/**
 * This class parses a geometry. It is also used 
 * to get the attributes for a GML geometry. 
 * @author Jorge Piera LLodr· (jorge.piera@iver.es)
 */
public class GeometryBinding {
	protected String srsName = null;
	protected String id = null;
	
	/**
	 * It parses the gml:Geometry tag
	 * @param parser
	 * The XML parser
	 * @param handler
	 * The GPE parser that contains the content handler and
	 * the error handler
	 * @return
	 * A geometry
	 * @throws XmlStreamException
	 * @throws IOException
	 */
	public Object parse(IXmlStreamReader parser,GPEDefaultGmlParser handler) throws XmlStreamException, IOException {
		int currentTag;
		Object geometry = null;		

		QName tag = parser.getName();
		currentTag = parser.getEventType();


		switch(currentTag){
		case IXmlStreamReader.START_ELEMENT:
			geometry = parseTag(parser, handler, tag);
			break;
		}					
		return geometry;	
	}
	
	/**
	 * It parses an XML tag
	 * @param parser
	 * @param handler
	 * @param tag
	 * @return
	 * @throws XmlStreamException
	 * @throws IOException
	 */
	protected Object parseTag(IXmlStreamReader parser,GPEDefaultGmlParser handler, QName tag) throws XmlStreamException, IOException{
		Object geometry = null;
		//PROPERTIES
		if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_GEOMETRYPROPERTY)){
			geometry = handler.getProfile().getGeometryPropertyTypeBinding().
			parse(parser, handler);
		}else if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_POINTPROPERTY)){
			geometry = handler.getProfile().getPointPropertyTypeBinding().
			parse(parser, handler);
		}else if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_LINESTRINGPROPERTY)){
			geometry = handler.getProfile().getLineStringPropertyTypeBinding().
			parse(parser, handler);
		}else if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_POLYGONPROPERTY)){
			geometry = handler.getProfile().getPolygonPropertyTypeBinding().
			parse(parser, handler);
		}else if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_MULTIPOINTPROPERTY)){
			geometry = handler.getProfile().getMultiPointPropertyTypeBinding().
			parse(parser, handler);
		}else if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_MULTILINESTRINGPROPERTY)){
			geometry = handler.getProfile().getMultiLineStringPropertyTypeBinding().
			parse(parser, handler);
		}else if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_MULTIPOLYGONPROPERTY)){
			geometry = handler.getProfile().getMultiPolygonPropertyTypeBinding().
			parse(parser, handler);
		}else if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_MULTIGEOMETRYPROPERTY)){
			geometry = handler.getProfile().getMultiGeometryPropertyTypeBinding().
			parse(parser, handler);
		}
		//GEOMETRIES
		else if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_POINTMEMBER)){
			geometry = handler.getProfile().getPointMemberTypeBinding().
			parse(parser, handler);
		}else if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_POINT)){
			geometry = handler.getProfile().getPointTypeBinding().
			parse(parser, handler);
		}else if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_LINESTRINGMEMBER)){
			geometry = handler.getProfile().getLineStringMemberTypeBinding().
			parse(parser, handler);
		}else if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_LINESTRING)){
			geometry = handler.getProfile().getLineStringTypeBinding().
			parse(parser, handler);
		}else if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_LINEARRING)){
			geometry = handler.getProfile().getLinearRingTypeBinding().
			parse(parser, handler);
		}else if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_POLYGONMEMBER)){
			geometry = handler.getProfile().getPointMemberTypeBinding().
			parse(parser, handler);
		}else if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_POLYGON)){
			geometry = handler.getProfile().getPolygonTypeBinding().
			parse(parser, handler);
		}else if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_GEOMETRYMEMBER)){
			geometry = handler.getProfile().getGeometryMemberTypeBinding().
			parse(parser, handler);
		}else if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_MULTIPOINT)){
			geometry = handler.getProfile().getMultiPointTypeBinding().
			parse(parser, handler);
		}else if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_MULTILINESTRING)){
			geometry = handler.getProfile().getMultiLineStringTypeBinding().
			parse(parser, handler);
		}else if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_MULTIPOLYGON)){
			geometry = handler.getProfile().getMultiPolygonTypeBinding().
			parse(parser, handler);
		}else if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_MULTIGEOMETRY)){
			geometry = handler.getProfile().getMultiGeometryTypeBinding().
			parse(parser, handler);
		}
		//POINT PROPERTY ALIASES
		else if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_LOCATION) ||
				CompareUtils.compareWithNamespace(tag,GMLTags.GML_CENTEROF) ||
				CompareUtils.compareWithNamespace(tag,GMLTags.GML_POSITION)){
			geometry = handler.getProfile().getPointTypeBinding().
			parse(parser, handler);
		}
		//LINESTRING PROPERTY ALIASES
		else if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_EDGEOF) ||
				CompareUtils.compareWithNamespace(tag,GMLTags.GML_CENTERLINEOF)){
			geometry = handler.getProfile().getLineStringPropertyTypeBinding().
			parse(parser, handler);
		}
		//POLYGON PROPERTY ALIASES
		else if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_EXTENTOF) ||
				CompareUtils.compareWithNamespace(tag,GMLTags.GML_COVERAGE)){
			geometry = handler.getProfile().getPolygonPropertyTypeBinding().
			parse(parser, handler);
		}
		//MULTIPOINT PROPERTY ALIASES
		else if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_MULTILOCATION) ||
				CompareUtils.compareWithNamespace(tag,GMLTags.GML_MULTIPOSITION)){
			geometry = handler.getProfile().getMultiPointPropertyTypeBinding().
			parse(parser, handler);
		}
		//MULTILINESTRING PROPERTY ALIASES
		else if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_MULTIEDGEOF) ||
				CompareUtils.compareWithNamespace(tag,GMLTags.GML_MULTICENTERLINEOF)){
			geometry =handler.getProfile().getMultiLineStringPropertyTypeBinding().
			parse(parser, handler);
		}
		//MULTIPOLYGON PROPERTY ALIASES
		else if (CompareUtils.compareWithNamespace(tag,GMLTags.GML_MULTIEXTENTOF) ||
				CompareUtils.compareWithNamespace(tag,GMLTags.GML_MULTICOVERAGE)){
			geometry = handler.getProfile().getMultiPolygonTypeBinding().
			parse(parser, handler);
		}
		return geometry;
	}
	
	/**
	 * This method has to be invoked by all the geometries
	 * before to make the parse process. It retrieves
	 * the common geometry attributes
	 * @param parser
	 * The root geometry tag
	 * @param errorHandler
	 * To add the errors
	 * @throws XmlStreamException 
	 */
	protected void setAtributtes(IXmlStreamReader parser, IGPEErrorHandler errorHandler) throws XmlStreamException{
		XMLAttributesIterator attributesIteartor = new XMLAttributesIterator(parser);
		Map attributes = attributesIteartor.getAttributes();
		Object objId = attributes.get(GMLTags.GML_GID);
		if (objId != null){
			id = (String)objId;
		}else{
			id = null;
		}
		Object objSrs = attributes.get(GMLTags.GML_SRS_NAME);
		if (objSrs != null){
			srsName = GMLProjectionFactory.fromGMLToGPE((String)objSrs, errorHandler);
		}else{
			srsName = null;
		}
	}
	
	/**
	 * It returns a the geometry id attribute
	 * @return
	 * The id
	 */
	public String getID(){
		return id;
	}	

	/**
	 * It returns a the geometry srs attribute
	 * @return
	 * The srs
	 */
	public String getSrs(){
		return srsName;
	}

}
