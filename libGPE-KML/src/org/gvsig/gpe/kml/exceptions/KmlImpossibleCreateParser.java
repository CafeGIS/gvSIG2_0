package org.gvsig.gpe.kml.exceptions;

import java.util.Hashtable;
import java.util.Map;

import org.gvsig.tools.exception.BaseException;

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
 * $Id: KmlImpossibleCreateParser.java 9 2007-03-07 08:19:11Z jorpiell $
 * $Log$
 * Revision 1.1  2007/03/07 08:19:10  jorpiell
 * Pasadas las clases de KML de libGPE-GML a libGPE-KML
 *
 * Revision 1.1  2007/02/28 11:48:31  csanchez
 * *** empty log message ***
 *
 * Revision 1.1  2007/02/20 10:53:20  jorpiell
 * AÃ±adidos los proyectos de kml y gml antiguos
 *
 * Revision 1.1  2007/02/12 13:49:18  jorpiell
 * Añadido el driver de KML
 *
 *
 */
/**
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class KmlImpossibleCreateParser extends BaseException{
	private static final long serialVersionUID = 8959337892045840876L;
	private String nameSpace = null;
	
	public KmlImpossibleCreateParser(String nameSpace) {
		this.nameSpace = nameSpace;
		init();		
	}
	
	public KmlImpossibleCreateParser(String nameSpace,Throwable exception) {
		this.nameSpace = nameSpace;
		init();
		initCause(exception);
	}
	
	private void init() {
		messageKey = "error_kml_impossibleCreateParser";
		formatString = "It has been impossibleThe to create a parser for the namespace %(nameSpace)";
		code = serialVersionUID;		
	}

	protected Map values() {
		Hashtable params = new Hashtable();
		params.put("nameSpace",nameSpace);
		return params;		
	}
}
