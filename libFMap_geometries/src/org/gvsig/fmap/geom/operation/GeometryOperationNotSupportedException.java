package org.gvsig.fmap.geom.operation;

import java.util.HashMap;
import java.util.Map;

import org.gvsig.fmap.geom.type.GeometryType;
import org.gvsig.tools.exception.BaseException;

public class GeometryOperationNotSupportedException extends BaseException {

	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = -8394502194572892834L;

	/**
	 * Constants specific to this BaseException subclass
	 */
	private static final String MESSAGE_KEY = "Operation_opCode_is_not_registered_for_geomTypeName_type";
	private static final String FORMAT_STRING = "Operation %(opCode)s is not registered for '%(geomTypeName)s' type.";
	
	private String geomTypeName = null;
	private int opCode = -1;
	
	/**
	 * Constructor with the operation code of a common operation.
	 * @param opCode
	 */	
	public GeometryOperationNotSupportedException(int opCode) {
		this(opCode, null);
	}
	
	/**
	 * Constructor with the operation code related to a geometry type
	 * @param opCode
	 * @param geomType
	 */
	public GeometryOperationNotSupportedException(int opCode, GeometryType geomType) {
		this(opCode, geomType, null);
	}

	/**
	 * Constructor to use when <code>this</code> is caused by another Exception 
	 * but there is not further context data available.
	 * @param e
	 */
	public GeometryOperationNotSupportedException(Exception e) {
		this(-1, null, e);
	}
	
	/**
	 * Main constructor
	 * @param opCode
	 * @param geomType
	 * @param e
	 */
	public GeometryOperationNotSupportedException(int opCode, GeometryType geomType, Exception e) {
		messageKey = MESSAGE_KEY;
		formatString = FORMAT_STRING;
		code = serialVersionUID;

		if (e != null) {
			initCause(e);
		}
		if (geomType != null) {
			geomTypeName = geomType.getName();
		} else {
			geomTypeName = "ANY";
		}
		this.opCode = opCode;
	}
		
	protected Map values() {
		HashMap map = new HashMap();
		map.put("opCode", Integer.toString(opCode));
		map.put("geomTypeName", geomTypeName);
		return map;
	}
	
}
