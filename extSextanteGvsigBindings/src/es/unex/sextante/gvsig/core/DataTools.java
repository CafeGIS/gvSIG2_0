package es.unex.sextante.gvsig.core;

import java.sql.Types;
import java.util.Date;

import org.gvsig.fmap.dal.DataTypes;

public class DataTools {

	public static int[] getgvSIGTypes(Class[] types) {

		int iTypes[] = new int[types.length];
		for (int i = 0; i < types.length; i++) {
			if (types[i].equals(Integer.class)){
				iTypes[i] = DataTypes.INT;
			}
			else if (types[i].equals(Double.class)){
				iTypes[i] = DataTypes.DOUBLE;
			}
			else if (types[i].equals(Long.class)){
				iTypes[i] = DataTypes.LONG;
			}
			else if (types[i].equals(Float.class)){
				iTypes[i] = DataTypes.FLOAT;
			}
			else if (types[i].equals(String.class)){
				iTypes[i] = DataTypes.CHAR;
			}
			else if (types[i].equals(Date.class)){
				iTypes[i] = DataTypes.DATE;
			}
			else if (types[i].equals(Boolean.class)){
				iTypes[i] = DataTypes.BOOLEAN;
			}
			else if (types[i].equals(Object.class)){
				iTypes[i] = DataTypes.OBJECT;
			}
		}

		return iTypes;

	}

	public static Class getTypeClass(int dataType) {

		switch (dataType) {
		case DataTypes.DOUBLE:
			return Double.class;
		case DataTypes.FLOAT:
			return Float.class;
		case DataTypes.LONG:
			return Long.class;
		case DataTypes.INT:
			return Integer.class;
		case DataTypes.STRING:
			return String.class;
		case DataTypes.CHAR:
			return String.class;
		case DataTypes.DATE:
			return Date.class;
		case DataTypes.BOOLEAN:
			return Boolean.class;
		}

		return String.class;

	}


//	public static Object[] getSextanteValues(Value[] record) {
//
//		Object[] values = new Object[record.length];
//
//		for (int i = 0; i < record.length; i++) {
//			if (record[i] instanceof IntValue){
//				values[i] = new Integer(((IntValue)record[i]).getValue());
//			}
//			else if (record[i] instanceof DoubleValue){
//				values[i] = new Double(((DoubleValue)record[i]).getValue());
//			}
//			else if (record[i] instanceof FloatValue){
//				values[i] = new Float(((FloatValue)record[i]).getValue());
//			}
//			else if (record[i] instanceof LongValue){
//				values[i] = new Long(((LongValue)record[i]).getValue());
//			}
//			else if (record[i] instanceof DateValue){
//				values[i] = ((DateValue)record[i]).getValue();
//			}
//			else if (record[i] instanceof StringValue){
//				values[i] = ((StringValue)record[i]).getValue();
//			}
//			else if (record[i] instanceof BooleanValue){
//				values[i] = new Boolean(((BooleanValue)record[i]).getValue());
//			}
//		}
//
//		return values;
//
//	}

//	public static Value[] getGVSIGValues(Object[] record) {
//
//		Value[] values = new Value[record.length];
//
//		for (int i = 0; i < record.length; i++) {
//			if (record[i] instanceof Integer){
//				values[i] = ValueFactory.createValue(((Integer)record[i]).intValue());
//			}
//			else if (record[i] instanceof Double){
//				values[i] = ValueFactory.createValue(((Double)record[i]).doubleValue());
//			}
//			else if (record[i] instanceof Float){
//				values[i] = ValueFactory.createValue(((Float)record[i]).longValue());
//			}
//			else if (record[i] instanceof Long){
//				values[i] = ValueFactory.createValue(((Long)record[i]).longValue());
//			}
//			else if (record[i] instanceof Date){
//				values[i] = ValueFactory.createValue(((Date)record[i]));
//			}
//			else if (record[i] instanceof String){
//				values[i] = ValueFactory.createValue(((String)record[i]));
//			}
//			else if (record[i] instanceof Boolean){
//				values[i] = ValueFactory.createValue(((Boolean)record[i]).booleanValue());
//			}
//			else if (record[i] == null){
//				values[i] = ValueFactory.createNullValue();
//			}
//		}
//
//		return values;
//
//	}

}
