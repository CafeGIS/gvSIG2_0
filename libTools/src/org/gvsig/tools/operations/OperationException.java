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

package org.gvsig.tools.operations;

import java.util.HashMap;
import java.util.Map;

import org.gvsig.tools.exception.BaseException;

public class OperationException extends BaseException {
	/**
	 *
	 */
	private static final long serialVersionUID = -2344772588507587265L;
	private final static String MESSAGE_FORMAT = "Error with operation.";
	private final static String MESSAGE_KEY = "_OperationException";

	protected Map values = new HashMap();

	public OperationException(String messageFormat, Throwable cause,
			String messageKey, long code) {
		super(messageFormat, cause, messageKey, code);
	}

	public OperationException(String messageFormat, String messageKey,
			long code) {
		super(messageFormat, messageKey, code);
	}

	public OperationException(Throwable cause) {
		super(MESSAGE_FORMAT, cause, MESSAGE_KEY, serialVersionUID);
	}

	protected void setValue(String name, Object value) {
		this.values.put(name, value);
	}

	protected Map values() {
		return this.values;
	}

}
