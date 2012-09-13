package org.gvsig.fmap.dal.store.gpe.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.gvsig.fmap.mapcontext.layers.FLayer;
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
 * $Id$
 * $Log$
 *
 */
/**
 * Thrown when there is an unknown problem loading 
 * a GPE layer
 * @author Jorge Piera LLodr� (jorge.piera@iver.es)
 */
public class GPELayerException extends BaseException {
	private static final long serialVersionUID = -5810461892751191682L;
	private FLayer layer = null;
	
	public GPELayerException(FLayer layer, Throwable exception) {
		this.layer = layer;
		initialize();
		initCause(exception);
	}
	
	/**
	 * Initialize the properties
	 */
	private void initialize() {
		messageKey = "gpe_gvsig_load_layer_error";
		formatString = "Error loading the layer " +
				"%(layerName).";
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.exceptions.BaseException#values()
	 */
	protected Map values() {
		HashMap params = new HashMap();
		params.put("layerName", layer.getName());
		return params;	
	}
	
}
