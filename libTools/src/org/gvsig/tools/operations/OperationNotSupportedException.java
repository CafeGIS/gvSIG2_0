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


public class OperationNotSupportedException extends OperationException {


	/**
	 *
	 */
	private static final long serialVersionUID = -6662679326425178742L;
	private static final String MESSAGE_KEY = "_OperationNotSupportedException";
	private static final String MESSAGE_FORMAT = "Operation %(operationName)s with code %(operationCode)s not supported in %(theClass)s.";

	public OperationNotSupportedException(Class theClass, String operationName) {
		super(MESSAGE_FORMAT, MESSAGE_KEY, serialVersionUID);
		setValue("operationCode", new Integer(-1));
		setValue("operationName", operationName);
		setValue("theClass", theClass);
	}

	public OperationNotSupportedException(Class theClass, int code) {
		super(MESSAGE_FORMAT, MESSAGE_KEY, serialVersionUID);
		setValue("operationCode", new Integer(code));
		setValue("operationName", "unknow");
		setValue("theClass", theClass);
	}

}
