package org.gvsig.fmap.mapcontext.rendering.legend.driver;

public class LegendDriverException extends Exception {
	
	// Classification Exceptions
	public static final int CLASSIFICATION_FIELDS_NOT_FOUND = 1;
	public static final int CLASSIFICATION_FIELDS_TYPE_MISMATCH = 2;
	
	// Legend exceptions
	public static final int LEGEND_TYPE_NOT_YET_SUPPORTED = 16;
	public static final int SYMBOL_TYPE_NOT_YET_SUPPORTED = 32;
	
	// Geometry exceptions
	public static final int LAYER_SHAPETYPE_MISMATCH = 64;
	
	// Driver exceptions
	public static final int PARSE_LEGEND_FILE_ERROR = 1024;
	public static final int UNSUPPORTED_LEGEND_FILE_VERSION = 2048;
	public static final int READ_DRIVER_EXCEPTION = 4096;
	public static final int SAVE_LEGEND_ERROR = 8192;
	public static final int UNSUPPORTED_LEGEND_CREATION =16384;
	public static final int UNSUPPORTED_LEGEND_READING =32768;
	
	// Filesystem Exceptions
	public static final int SYSTEM_ERROR = 1048576;
	
	// Parsing Errors
	public static final int LAYER_NAME_NOT_SPECIFIED = 2097152;
	public static final int LAYER_NAME_NOT_FOUND = 4194304;
	public static final int UNSUPPORTED_LEGEND_CREATION_FOR_DRIVER = 8388608;
	
	private int type;
	
	public LegendDriverException(int type) {
		super();
		this.type = type;
	}
	
	public int getType() {
		return type;
	}

}
