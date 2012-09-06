package org.gvsig.tools.dataTypes;

/**
 * This interface defines a set of constants for all data types supported by the
 * DAL.
 */
public interface DataTypes {
	public static final int UNKNOWN = 0;
	public static final int BOOLEAN = 1;
	public static final int BYTE = 2;
	public static final int CHAR = 3;
	public static final int INT = 4;
	public static final int LONG = 5;
	public static final int FLOAT = 6;
	public static final int DOUBLE = 7;
	public static final int STRING = 8;
	public static final int DATE = 9;
	public static final int TIME = 10;
	public static final int TIMESTAMP = 11;

	/** {@link org.gvsig.fmap.geom.Geometry} */
	public static final int GEOMETRY = 12;
	public static final int OBJECT = 13;
	/** {@link org.gvsig.fmap.dal.feature.Feature} */
	public static final int FEATURE = 14;
	/** String for SRS */
	public static final int SRS = 15;
	/** {@link java.io.File} */
	public static final int FILE = 16;

	public static final int BYTEARRAY = 17;


	/** Array with the name of each data type. Use the constants to access this array. */
	public static final String[] TYPE_NAMES = new String[] {
		"UNKNOWN",
		"BOOLEAN",
		"BYTE",
		"CHAR",
		"INT",
		"LONG",
		"FLOAT",
		"DOUBLE",
		"STRING",
		"DATE",
		"TIME",
		"TIMESTAMP",
		"GEOMETRY",
		"OBJECT",
		"FEATURE",
		"SRSID",
		"FILE",
		"BYTEARRAY"
	};


}
