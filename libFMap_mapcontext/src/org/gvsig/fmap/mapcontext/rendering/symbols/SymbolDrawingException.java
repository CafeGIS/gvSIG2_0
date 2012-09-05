package org.gvsig.fmap.mapcontext.rendering.symbols;

import org.gvsig.fmap.mapcontext.Messages;

/**
 * Exception thrown when a symbol cannot be drawn. The type of the exception
 * will determine the cause.
 * <ol>
 * 	<li><b>Unsupported set of settings</b>: getType() == SymbolDrawingException.UNSUPPORTED_SET_OF_SETTINGS</li>
 * 	<li><b>Shape type mismatch</b>: getType() == SymbolDrawingException.SHAPETYPE_MISMATCH</li>
 * </ol>
 * 
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 *
 */
public class SymbolDrawingException extends Exception {
	private static final long serialVersionUID = 4059410328246182074L;
	public static final int UNSUPPORTED_SET_OF_SETTINGS = 0;
	public static final int SHAPETYPE_MISMATCH = 1;
	public static final String STR_UNSUPPORTED_SET_OF_SETTINGS = Messages.getString("unsupported_set_of_settings");
	
	private int type;
	
	public int getType() {
		return type;
	}
	/**
	 * Creates a new instance of this exception.
	 * <ol>
	 * 	<li><b>Unsupported set of settings</b>: getType() == SymbolDrawingException.UNSUPPORTED_SET_OF_SETTINGS</li>
	 * 	<li><b>Shape type mismatch</b>: getType() == SymbolDrawingException.SHAPETYPE_MISMATCH</li>
	 * </ol>
	 */ 
	public SymbolDrawingException(int type) {
		this.type = type;
	}
}
