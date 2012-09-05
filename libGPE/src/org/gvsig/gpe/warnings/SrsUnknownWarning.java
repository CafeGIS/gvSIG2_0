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
 * $Id: SrsUnknownWarning.java 124 2007-05-16 09:27:40Z jorpiell $
 * $Log$
 * Revision 1.2  2007/05/16 09:27:40  jorpiell
 * Add some warnings
 *
 * Revision 1.1  2007/05/14 11:21:14  jorpiell
 * ProjectionFactory updated
 *
 *
 */
/**
 * This warning is throwed when the parser doesn't
 * know the SRS
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class SrsUnknownWarning extends BaseException{
	private static final long serialVersionUID = 5269899884175171849L;
	private String srs = null;
	
	/**
	 * Cosntructor
	 * @param srs
	 * Unknown SRS
	 */
	public SrsUnknownWarning(String srs){
		this.srs = srs;
		initialize();
	}

	/**
	 * Initialize the properties
	 */
	private void initialize() {
		messageKey = "gpe_srs_unknown_warning";
		formatString = "The SRS %(srs) is unknokn.";				
		code = serialVersionUID;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.tools.exception.BaseException#values()
	 */
	protected Map values() {
		Hashtable hash = new Hashtable();
		hash.put("srs", srs);
		return hash;
	}

}
