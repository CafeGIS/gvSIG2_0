package org.gvsig.gpe.gml.writer.v2.geometries;

import org.gvsig.gpe.gml.utils.GMLTags;

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
 * $Id: MultiPointWriter.java 352 2008-01-09 13:51:51Z jpiera $
 * $Log$
 * Revision 1.5  2007/05/16 13:00:48  csanchez
 * ActualizaciÃ³n de libGPE-GML
 *
 * Revision 1.4  2007/05/14 11:18:12  jorpiell
 * Add the ErrorHandler to all the methods
 *
 * Revision 1.3  2007/05/08 10:24:16  jorpiell
 * Add comments to create javadocs
 *
 * Revision 1.2  2007/04/14 16:07:30  jorpiell
 * The writer has been created
 *
 * Revision 1.1  2007/04/13 13:16:00  jorpiell
 * Add the multiple geometries
 *
 *
 */
/**
 * It writes a gml:MultiPointType object. Example:
 * <p>
 * <pre>
 * <code> 
 * &lt;MultiPoint gid="c731" srsName="http://www.opengis.net/gml/srs/epsg.xml#4326"&gt;
 * &lt;pointMember&gt;
 * &lt;Point gid="P6776"&gt;
 * &lt;coord&gt;&lt;X&gt;50.0&lt;/X&gt;&lt;Y&gt;50.0&lt;/Y&gt;&lt;/coord&gt;
 * &lt;/Point&gt;
 * &lt;/pointMember&gt;
 * &lt;pointMember&gt;
 * &lt;Point gid="P6777"&gt;
 * &lt;coord&gt;&lt;X&gt;50.0&lt;/X&gt;&lt;Y&gt;50.0&lt;/Y&gt;&lt;/coord&gt;
 * &lt;/Point&gt;
 * &lt;/pointMember&gt;
 * &lt;/MultiPoint&gt;
 * </code>
 * </pre>
 * </p> 
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class MultiPointWriter extends GeometryWriter{
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.gml.writer.v2.geometries.GeometriesWriter#getGeometryName()
	 */
	public String getGeometryName() {
		return GMLTags.GML_MULTIPOINT.getLocalPart();
	}
}
