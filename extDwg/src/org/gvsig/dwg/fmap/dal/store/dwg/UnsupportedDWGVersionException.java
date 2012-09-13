package org.gvsig.dwg.fmap.dal.store.dwg;

import org.gvsig.dwg.lib.DwgVersionNotSupportedException;
import org.gvsig.fmap.dal.exception.DataException;

public class UnsupportedDWGVersionException extends DataException {

	/**
	 *
	 */
	private static final long serialVersionUID = -7335241420683266251L;
	private final static String MESSAGE_FORMAT = "Unssuported version of DWG of file '%(file)'. Try to convert using Autodesk convertor from '%(autodeskurl)'";
	private final static String MESSAGE_KEY = "_UnsupportedDWGVersionException";

	public final static String AUTODESK_URL = "http://usa.autodesk.com/adsk/servlet/item?siteID=123112&id=7024151";

	public UnsupportedDWGVersionException(String file,
			DwgVersionNotSupportedException cause) {
		super(MESSAGE_FORMAT, cause, MESSAGE_KEY, serialVersionUID);
		setValue("file", file);
		setValue("autodeskurl", AUTODESK_URL);
	}
}