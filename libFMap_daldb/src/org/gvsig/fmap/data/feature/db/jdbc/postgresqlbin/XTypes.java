/*
 * Created on 26-oct-2005
 *
 * gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
package org.gvsig.fmap.data.feature.db.jdbc.postgresqlbin;

import java.sql.Types;

public class XTypes {
	public final static double NUM_msSecs2000 = 9.466776E11;
	public final static int POINT2D = 100;

	public final static int LINE2D = 101;

	public final static int POLYGON2D = 102;

	public final static int MULTI2D = 103;

	public static String fieldTypeToString(int fieldType) {
		String aux = "text"; // Por defecto.
		switch (fieldType) {
		case Types.SMALLINT:
			aux = "integer";
			break;
		case Types.INTEGER:
			aux = "integer";
			break;
		case Types.BIGINT:
			aux = "integer";
			break;
		case Types.BOOLEAN:
			aux = "boolean";
			break;
		case Types.DECIMAL:
			aux = "numeric";
			break;
		case Types.DOUBLE:
			aux = "float8";
			break;
		case Types.FLOAT:
			aux = "float";
			break;
		case Types.CHAR:
			aux = "char";
			break;
		case Types.VARCHAR:
			aux = "text";
			break;
		case Types.LONGVARCHAR:
			aux = "text";
			break;

		case POINT2D:
			aux = "POINT";
			break;
		case LINE2D:
			aux = "MULTILINESTRING";
			break;
		case POLYGON2D:
			aux = "MULTIPOLYGON";
			break;
		case MULTI2D:
			aux = "GEOMETRY";
			break;

		}

		return aux;
	}

//	/**
//	 * @param fieldId
//	 * @throws SQLException
//	 */
//	public static Value getValue(ResultSet rs, int fieldId) throws SQLException {
//		byte[] data;
//		data = rs.getBytes(fieldId);
//		Value val = null;
//		ResultSetMetaData metaData = rs.getMetaData();
//		if (data == null)
//			val = ValueFactory.createNullValue();
//		else {
//			ByteBuffer buf = ByteBuffer.wrap(data);
//			if (metaData.getColumnType(fieldId) == Types.VARCHAR) {
//				val = ValueFactory.createValue(rs.getString(fieldId));
//			}else if (metaData.getColumnType(fieldId) == Types.FLOAT) {
//				val = ValueFactory.createValue(buf.getFloat());
//			}else if (metaData.getColumnType(fieldId) == Types.DOUBLE) {
//				val = ValueFactory.createValue(buf.getDouble());
//			}else if (metaData.getColumnType(fieldId) == Types.INTEGER){
//				val = ValueFactory.createValue(buf.getInt());
//			}else if (metaData.getColumnType(fieldId) == Types.SMALLINT){
//				val = ValueFactory.createValue(buf.getShort());
//			}else if (metaData.getColumnType(fieldId) == Types.TINYINT){
//				val = ValueFactory.createValue(buf.getShort());
//			}else if (metaData.getColumnType(fieldId) == Types.BIGINT){
//				val = ValueFactory.createValue(buf.getLong());
//			}else if (metaData.getColumnType(fieldId) == Types.BIT || metaData.getColumnType(fieldId) == Types.BOOLEAN ){
//				val = ValueFactory.createValue(rs.getBoolean(fieldId));
//			}else if (metaData.getColumnType(fieldId) == Types.DATE){
//				val = ValueFactory.createValue(rs.getDate(fieldId));
//			}else if (metaData.getColumnType(fieldId) == Types.TIME){
//				val = ValueFactory.createValue(rs.getTime(fieldId));
//			}else if (metaData.getColumnType(fieldId) == Types.TIMESTAMP){
//				val = ValueFactory.createValue(rs.getTimestamp(fieldId));
//			}
//
//		}
//		return val;
//	}
//
//	public static void updateValue(ResultSet rs, int fieldId_ceroBased,
//			Value val) throws SQLException {
//		if (val instanceof NullValue)
//			return;
//		// byte[] data;
//		ResultSetMetaData metaData = rs.getMetaData();
//		int fieldId = fieldId_ceroBased + 1;
//		// System.out.println("EScritrua: " + metaData.isDefinitelyWritable(1));
//		switch (val.getSQLType()) {
//		case Types.VARCHAR:
//		case Types.LONGVARCHAR:
//			// Para evitar escribir en el campo geometria:
//			if (metaData.getColumnType(fieldId) == Types.OTHER)
//				return;
//			StringValue valStr = (StringValue) val;
//			rs.updateString(fieldId, valStr.getValue());
//			// System.out.println("Field " + fieldId + " :" +
//			// metaData.getColumnTypeName(fieldId));
//			break;
//		case Types.FLOAT:
//			FloatValue vFloat = (FloatValue) val;
//			rs.updateFloat(fieldId, vFloat.getValue());
//			break;
//		case Types.DOUBLE:
//			DoubleValue vDouble = (DoubleValue) val;
//			rs.updateDouble(fieldId, vDouble.getValue());
//			break;
//		case Types.INTEGER:
//			IntValue vInt = (IntValue) val;
//			rs.updateInt(fieldId, vInt.getValue());
//		case Types.SMALLINT:
//			ShortValue vShort = (ShortValue) val;
//			rs.updateShort(fieldId, vShort.shortValue());
//			break;
//		case Types.BIGINT:
//			LongValue vLong = (LongValue) val;
//			rs.updateLong(fieldId, vLong.getValue());
//			break;
//		case Types.BIT:
//		case Types.BOOLEAN:
//			BooleanValue vBool = (BooleanValue) val;
//			rs.updateBoolean(fieldId, vBool.getValue());
//			break;
//		case Types.DATE:
//			DateValue vDate = (DateValue) val;
//			rs.updateDate(fieldId, vDate.getValue());
//			break;
//		case Types.TIME:
//			TimeValue vTime = (TimeValue) val;
//			rs.updateTime(fieldId, vTime.getValue());
//			break;
//		case Types.TIMESTAMP:
//			TimestampValue vTimeStamp = (TimestampValue) val;
//			rs.updateTimestamp(fieldId, vTimeStamp.getValue());
//			break;
//
//		default:
//			System.err.println("Tipo no soportado:"
//					+ metaData.getColumnType(fieldId) + ". Field:" + fieldId
//					+ ": " + metaData.getColumnName(fieldId));
//		// throw new UnsupportedOperationException();
//
//		}
//	}

}
