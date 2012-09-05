package com.iver.utiles;

import java.sql.Types;


public class NumberUtilities {
	public static boolean isNumeric(int sqlType) {

		switch (sqlType) {
		case Types.DOUBLE:
		case Types.FLOAT:
		case Types.INTEGER:
		case Types.SMALLINT:
		case Types.BIGINT:
		case Types.NUMERIC:
		case Types.REAL:
		case Types.TINYINT:
			return true;
		}

		return false;
	}
	public static boolean isNumericInteger(int sqlType) {

		switch (sqlType) {
		
		case Types.INTEGER:
		case Types.SMALLINT:
		case Types.BIGINT:
		case Types.TINYINT:
			return true;
		}

		return false;
	}

}
