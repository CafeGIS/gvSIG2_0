package org.gvsig.fmap.geom.operation;

import java.util.HashMap;
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
 * $Id: GeometryOperationException.java,v 1.1 2008/03/12 08:46:21 cvs Exp $
 * $Log: GeometryOperationException.java,v $
 * Revision 1.1  2008/03/12 08:46:21  cvs
 * *** empty log message ***
 *
 *
 */
/**
 * @author Jorge Piera Llodrá (jorge.piera@iver.es)
 */
public class GeometryOperationException extends BaseException {

	/**
	 * Generated serial version UID
	 */
	private static final long serialVersionUID = 8230919748601156772L;
	private static final String MESSAGE_KEY = "geometry_operation_exception";
	private static final String FORMAT_STRING =
		"Exception executing the operation with code %(operationCode) for the geometry with type %(geometryType).";

	private int geometryType = -1;
	private int operationCode = -1;

	/**
	 * Constructor with some context data for cases in which the root cause of
	 * <code>this</code> is internal (usually an unsatisfied logic rule).
	 * @param geometryType
	 * @param operationCode
	 */
	public GeometryOperationException(int geometryType, int operationCode){
		this(geometryType, operationCode, null);
	}

	/**
	 * Constructor to use when <code>this</code> is caused by another Exception
	 * but there is not further context data available.
	 * @param e
	 */
	public GeometryOperationException(Exception e) {
		this(-1, -1, e);
	}

	/**
	 * Main constructor that provides both context data and a cause Exception
	 * @param geometryType
	 * @param operationCode
	 * @param exp
	 */
	public GeometryOperationException(int geometryType, int operationCode, Exception e) {
		super(FORMAT_STRING, e, MESSAGE_KEY, serialVersionUID);

		this.geometryType = geometryType;
		this.operationCode = operationCode;
	}

	protected Map values() {
		HashMap map = new HashMap();
		map.put("geometryType", String.valueOf(geometryType));
		map.put("operationCode", String.valueOf(operationCode));
		return map;
	}
}

