package org.gvsig.tools.visitor;

import java.util.HashMap;
import java.util.Map;

import org.gvsig.tools.exception.BaseException;

/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Government (CIT)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 */

/*
 * AUTHORS (In addition to CIT):
 * 2008 IVER T.I. S.A.   {{Task}}
 */
/**
 * @author Jorge Piera Llodrá (jorge.piera@iver.es)
 */
public class NotSupportedOperationException extends BaseException{
	private static final long serialVersionUID = 1L;
	private String visitorClassName = null;
	private String objectClassName = null;

	public NotSupportedOperationException(Visitor visitor, Object object){
		this.visitorClassName = visitor.getClass().getName();
		this.objectClassName = object.getClass().getName();
	}

	/**
	 * Initializes some values
	 */
	public void init() {
		messageKey="geometries_reader_not_exists";
		formatString="The visitor %(visitorClassName) doesn't implements any " +
			"operation for the object %(objectClassName)";
		code = serialVersionUID;
	}

	protected Map values() {
		HashMap map = new HashMap();
		map.put("visitorClassName", visitorClassName);
		map.put("objectClassName", objectClassName);
		return map;
	}

}
