package org.gvsig.xmlschema.som;



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
 * $Id: IXSElementDeclaration.java 162 2007-06-29 12:19:48Z jorpiell $
 * $Log$
 * Revision 1.3  2007/06/29 12:19:14  jorpiell
 * The schema validation is made independently of the concrete writer
 *
 * Revision 1.2  2007/06/22 12:20:48  jorpiell
 * The typeNotFoundException has been deleted. It never was thrown
 *
 * Revision 1.1  2007/06/14 16:15:03  jorpiell
 * builds to create the jars generated and add the schema code to the libGPEProject
 *
 * Revision 1.1  2007/06/14 13:50:07  jorpiell
 * The schema jar name has been changed
 *
 * Revision 1.4  2007/06/08 11:35:16  jorpiell
 * IXSSchema interface updated
 *
 * Revision 1.3  2007/06/07 14:54:13  jorpiell
 * Add the schema support
 *
 * Revision 1.2  2007/05/30 12:25:48  jorpiell
 * Add the element collection
 *
 * Revision 1.1  2007/05/25 11:55:00  jorpiell
 * First update
 *
 *
 */
/**
 * This interface represents a XML schema element declaration. 
 * Example:
 * <p>
 * <pre>
 * <code>
 * &lt;xs:element name="PurchaseOrder" type="PurchaseOrderType"/&gt;
 * </code>
 * </pre>
 * </p> 
 * @see http://www.w3.org/TR/xmlschema-1/#cElement_Declarations
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public interface IXSElementDeclaration extends IXSComponent{
	public static final int MAX_OCCURS_UNBOUNDED = -1;
	public static final int MIN_OCCURS_UNBOUNDED = -1;
	
	public IXSTypeDefinition getTypeDefinition();
	
	public String getTypeName();
	
	public boolean isNillable();
	
	public int getMinOccurs();
	
	public int getMaxOccurs();
	
	public IXSElementDeclaration getSubElementByName(String name);
	
	/**
	 * Add a new XML schema Complex type
	 * @param type
	 * See the IXSComplexType interface for possible values
	 * @param contentType
	 * A complex content or a simple content
	 * See the IXSContentType for possible values
	 * @param contentTypeRestriction
	 * A extension or a restriction
	 * @return
	 * A xML schema complex type
	 */
	public IXSComplexTypeDefinition addComplexType(String type, String contentType, String contentTypeRestriction);
}

