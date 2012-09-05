package org.gvsig.fmap.data.feature.db.jdbc;

import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;

public class JDBCTypes {
	public static final String POINT2D = "POINT2D";
	public static final String LINE2D = "LINE2D";
	public static final String POLYGON2D = "POLYGON2D";
	public static final String MULTI2D = "MULYI2D";

	public static String fieldTypeToString(String fieldType) {
		String aux = "text"; // Por defecto.
		if (FeatureAttributeDescriptor.INT.equals(fieldType)){
			return "integer";
		}else if (FeatureAttributeDescriptor.BOOLEAN.equals(fieldType)){
			return "boolean";
		}else if (FeatureAttributeDescriptor.DOUBLE.equals(fieldType)){
			return "float8";
		}else if (FeatureAttributeDescriptor.FLOAT.equals(fieldType)){
			return "float";
		}else if (FeatureAttributeDescriptor.STRING.equals(fieldType)){
			return "text";
		}else if (FeatureAttributeDescriptor.GEOMETRY.equals(fieldType)){
			return "GEOMETRY";
		}else if (POINT2D.equals(fieldType)){
			return "POINT";
		}else if (LINE2D.equals(fieldType)){
			return "MULTILINESTRING";
		}else if (POLYGON2D.equals(fieldType)){
			return "MULTIPOLYGON";
		}else if (MULTI2D.equals(fieldType)){
			return "GEOMETRY";
		}
//		case Types.DECIMAL:
//			aux = "numeric";
//			break;
//		case Types.CHAR:
//			aux = "char";
//			break;
//		case POINT2D:
//			aux = "POINT";
//			break;
//		case LINE2D:
//			aux = "MULTILINESTRING";
//			break;
//		case POLYGON2D:
//			aux = "MULTIPOLYGON";
//			break;
//		case MULTI2D:
//			aux = "GEOMETRY";
//			break;
//
//		}

		return aux;
	}
}
