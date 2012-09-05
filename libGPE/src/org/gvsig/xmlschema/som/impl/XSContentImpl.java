package org.gvsig.xmlschema.som.impl;

import org.gvsig.xmlschema.som.IXSContentType;
import org.gvsig.xmlschema.som.IXSExtension;
import org.gvsig.xmlschema.som.IXSNode;
import org.gvsig.xmlschema.som.IXSRestriction;
import org.gvsig.xmlschema.som.IXSSchema;
import org.gvsig.xmlschema.utils.SchemaCollection;
import org.gvsig.xmlschema.utils.SchemaObjectsMapping;
import org.gvsig.xmlschema.utils.SchemaTags;

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
 * $Id: XSContentImpl.java 157 2007-06-22 12:22:53Z jorpiell $
 * $Log$
 * Revision 1.2  2007/06/22 12:20:48  jorpiell
 * The typeNotFoundException has been deleted. It never was thrown
 *
 * Revision 1.1  2007/06/14 16:15:03  jorpiell
 * builds to create the jars generated and add the schema code to the libGPEProject
 *
 * Revision 1.1  2007/06/14 13:50:07  jorpiell
 * The schema jar name has been changed
 *
 * Revision 1.2  2007/06/08 06:55:05  jorpiell
 * Fixed some bugs
 *
 * Revision 1.1  2007/06/07 14:54:13  jorpiell
 * Add the schema support
 *
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class XSContentImpl extends XSComponentImpl implements IXSContentType {

	public XSContentImpl(IXSSchema schema) {
		super(schema);		
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.som.IXSComplexContent#getExtension()
	 */
	public IXSExtension getExtension(){
		IXSNode node = new SchemaCollection(getSchema(),getElement(),getExtensionMapping()).getFirstNode();
		if (node != null){
			return (IXSExtension)node;
		}
		return null;
	}
	
	/**
	 * @return The extension mapping
	 * @throws TypeNotFoundException
	 */
	private SchemaObjectsMapping getExtensionMapping(){
		SchemaObjectsMapping elementTm = new SchemaObjectsMapping(getSchema());
		elementTm.addType(SchemaTags.EXTENSION, XSExtensionImpl.class);
		return elementTm;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.schema.som.IXSContentType#getRestriction()
	 */
	public IXSRestriction getRestriction(){
		IXSNode node = new SchemaCollection(getSchema(),getElement(),getRestrictionMapping()).getFirstNode();
		if (node != null){
			return (IXSRestriction)node;
		}
		return null;
	}
	
	/**
	 * @return The restriction mapping
	 * @throws TypeNotFoundException
	 */
	private SchemaObjectsMapping getRestrictionMapping(){
		SchemaObjectsMapping elementTm = new SchemaObjectsMapping(getSchema());
		elementTm.addType(SchemaTags.RESTRICTION, XSRestrictionImpl.class);
		return elementTm;
	}
	
}
