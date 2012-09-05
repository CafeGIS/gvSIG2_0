package org.gvsig.gpe.warnings;

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
 * $Id: NotSupportedLayerWarning.java 162 2007-06-29 12:19:48Z jorpiell $
 * $Log$
 * Revision 1.1  2007/06/29 12:19:14  jorpiell
 * The schema validation is made independently of the concrete writer
 *
 *
 */
/**
 * It is thrown when there is an layer that can not
 * be validated using the XML Schema
 * @author Jorge Piera Llodrá (jorge.piera@iver.es)
 * @author Carlos Sánchez Periñán (sanchez_carper@gva.es)
 */
public class NotSupportedLayerWarning extends BaseException{
	private static final long serialVersionUID = -6929364975278666066L;
	private String layerName = null;
	private String xsLayerType = null;
	private String parentLayerName = null;
	private String xsParentLayerType = null;	
	private boolean hasParent = false;

	public NotSupportedLayerWarning(String layerName, String xsLayerType, String parentLayerName, String xsParentLayerType) {
		super();
		this.layerName = layerName;
		this.xsLayerType = xsLayerType;
		this.parentLayerName = parentLayerName;
		this.xsParentLayerType = xsParentLayerType;
		hasParent = true;
		initialize();
	}
	
	public NotSupportedLayerWarning(String layerName, String xsLayerType) {
		super();
		this.layerName = layerName;
		this.xsLayerType = xsLayerType;
		hasParent = false;
		initialize();
	}

	/**
	 * Initialize the properties
	 */
	private void initialize() {
		messageKey = "gpe_not_supported_layer_warning";
		if (hasParent){
			formatString = "The layer '%(layerName)' with a XML schema " + 
			"type '%(xsLayerType)' is not contained in the parent layer " +
			"'%(parentLayerName)' with a XML Schema type '%(xsParentLayerType)'";
		}else{
			formatString = "The layer '%(layerName)' with a XML schema " + 
			"type '%(xsLayerType)' is not valid in the XML Schema";
		}
		code = serialVersionUID;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.tools.exception.BaseException#values()
	 */
	protected Map values() {
		Hashtable hash = new Hashtable();
		hash.put("layerName", layerName);
		hash.put("xsLayerType", xsLayerType);
		if (hasParent){
			hash.put("parentLayerName", parentLayerName);
			hash.put("xsParentLayerType", xsParentLayerType);
		}
		return hash;
	}
}
