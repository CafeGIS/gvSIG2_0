package org.gvsig.gpe.gml.warnings;

import java.util.Hashtable;
import java.util.Map;

import org.gvsig.tools.exception.BaseException;

/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
 *   Av. Blasco Ib��ez, 50
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
 * $Id: GMLWarningWrongNamespace.java 6 2007-02-28 11:49:30Z csanchez $
 * $Log$
 * Revision 1.1  2007/02/28 11:48:31  csanchez
 * *** empty log message ***
 *
 * Revision 1.1  2007/02/20 10:53:20  jorpiell
 * Añadidos los proyectos de kml y gml antiguos
 *
 * Revision 1.1  2007/01/15 13:08:06  csanchez
 * Sistema de Warnings y Excepciones adaptado a BasicException
 *
 *
 */
/**
 * @author Carlos S�nchez Peri��n (sanchez_carper@gva.es)
 */
public class GMLWarningWrongNamespace extends BaseException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5960588828358796702L;
	private String namespace;
	
	public GMLWarningWrongNamespace(String key){
		super();
		this.init();
		this.namespace = key;
	}
	
	protected Map values() {
		Hashtable params;
		params = new Hashtable();
		params.put("namespace",namespace);
		return params;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public void init() {
		messageKey="gml_warning_wrong_Namespace";
		formatString="Invalid Tag Ignored, not found in %(namespace) namespace";
		code = serialVersionUID;
	}

}
