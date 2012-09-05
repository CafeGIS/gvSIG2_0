package org.gvsig.gpe.xml.writer;

import org.gvsig.gpe.writer.GPEWriterHandlerImplementor;
import org.gvsig.gpe.xml.XmlProperties;
import org.gvsig.gpe.xml.parser.GPEXmlParserFactory;
import org.gvsig.gpe.xml.stream.IXmlStreamWriter;
import org.gvsig.gpe.xml.stream.XmlStreamException;

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
 * $Id: GPEXmlWriterHandlerImplementor.java 350 2008-01-09 12:53:07Z jpiera $
 * $Log$
 * Revision 1.12  2007/06/28 13:05:09  jorpiell
 * The Qname has been updated to the 1.5 JVM machine. The schema validation is made in the GPEWriterHandlerImplementor class
 *
 * Revision 1.11  2007/06/22 12:22:40  jorpiell
 * The typeNotFoundException has been deleted. It never was thrown
 *
 * Revision 1.10  2007/06/14 13:50:05  jorpiell
 * The schema jar name has been changed
 *
 * Revision 1.9  2007/06/07 14:53:30  jorpiell
 * Add the schema support
 *
 * Revision 1.8  2007/05/07 07:07:04  jorpiell
 * Add a constructor with the name and the description fields
 *
 * Revision 1.7  2007/04/26 14:40:03  jorpiell
 * Some writer handler methods updated
 *
 * Revision 1.6  2007/04/19 07:25:49  jorpiell
 * Add the add methods to teh contenhandler and change the register mode
 *
 * Revision 1.5  2007/04/17 10:30:11  jorpiell
 * Add a method to compress a file
 *
 * Revision 1.4  2007/04/14 16:07:30  jorpiell
 * The writer has been created
 *
 * Revision 1.3  2007/04/13 07:17:57  jorpiell
 * Add the writting tests for the simple geometries
 *
 * Revision 1.2  2007/04/12 17:06:44  jorpiell
 * First GML writing tests
 *
 * Revision 1.1  2007/04/12 10:23:41  jorpiell
 * Add some writers and the GPEXml parser
 *
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public abstract class GPEXmlWriterHandlerImplementor extends GPEWriterHandlerImplementor {
	protected IXmlStreamWriter writer = null;
	//private String targetNamespace = null;
	private String targetNamespacePrefix = null;
	
	public GPEXmlWriterHandlerImplementor() {
		super();		
	}	
	
	/* (non-Javadoc)
	 * @see org.gvsig.gpe.writer.GPEWriterHandlerImplementor#initialize()
	 */
	public void initialize() {
		super.initialize();
		try {
			writer = GPEXmlParserFactory.getWriter(getFormat(), getOutputStream());
		} catch (IllegalArgumentException e) {
			getErrorHandler().addError(e);
		} catch (XmlStreamException e) {
			getErrorHandler().addError(e);
		}
	}	
	
	/**
	 * @return the schemaLocation tag
	 */
	protected String getSchemaLocations(){
		StringBuffer schemaLocation = new StringBuffer();
//		Enumeration uris = getSchemaDocument().getURIs();
//		int i = 0;
//		while (uris.hasMoreElements()){
//			if (i > 0){
//				schemaLocation.append("\b");
//			}
//			URI uri = (URI)uris.nextElement();
//			IXSSchema schema = getSchemaDocument().getSchema(uri);
//			schemaLocation.append(schema.getTargetNamespace());
//			schemaLocation.append(" ");
//			schemaLocation.append(getSchemaDocument().getSchemaLocation(uri));
//			i++;
//		}
		return schemaLocation.toString();
	}	
	
	
	
	/**
	 * Returns the selected target namespace prefix. If
	 * the namespace doesn't exists it returns the 
	 * default Namespace prefix; 
	 * @return the namespace prefix
	 */
	protected String getTargetNamespacePrefix() {
		if (targetNamespacePrefix == null){
			String prefix = null;
//			if (getSchemaDocument().getTargetNamespace() != null){
//				prefix = getSchemaDocument().getNamespacePrefix(
//						getSchemaDocument().getTargetNamespace());
//			}else{
				prefix = getGpeManager().getStringProperty(XmlProperties.DEFAULT_NAMESPACE_PREFIX);
//			}
			if (prefix != null){
				targetNamespacePrefix = prefix + ":";
			}else{
				targetNamespacePrefix = "";
			}
		}
		return targetNamespacePrefix;
	}		
	
}
	

