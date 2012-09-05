package org.gvsig.fmap.data.feature.db.jdbc.postgresqlbin;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureReference;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.exception.IsNotFeatureSettingException;
import org.gvsig.fmap.data.feature.db.DBAttributeDescriptor;
import org.gvsig.fmap.data.feature.db.DBFeatureType;
import org.gvsig.fmap.data.feature.db.jdbc.JDBCFeature;
import org.gvsig.fmap.data.feature.db.jdbc.JDBCStore;


public class PostgresqlBinFeature extends JDBCFeature{



	/**
	 *
	 */
	private static final long serialVersionUID = -1215877232314904198L;

	PostgresqlBinFeature(FeatureType featureType, JDBCStore store, ResultSet rs) throws ReadException {
		super(featureType, store, rs);
	}


	public FeatureReference getID() {
		return new PostgresqlBinFeatureID(this.store,featureKey);
	}
	protected void loading() {
		// TODO Auto-generated method stub
		super.loading();
	}

	protected void stopLoading() {
		// TODO Auto-generated method stub
		super.stopLoading();
	}


	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.db.jdbc.JDBCFeature#loadValueFromResulset(java.sql.ResultSet, org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor)
	 */
	protected void loadValueFromResulset(ResultSet rs, FeatureAttributeDescriptor attr) throws ReadException {
		String name = attr.getName();
		Object value = null;
		try {
			value = getFieldValueFromBinaryCursor(rs, (DBAttributeDescriptor)attr);
			if (attr.getDataType().equals(FeatureAttributeDescriptor.GEOMETRY)) {
				this.setGeometry(name,value);
			} else {
				this.setAttribute(name, value);
			}
		} catch (java.sql.SQLException e) {
			throw new ReadException("CreateFeature",e);
		} catch (IsNotFeatureSettingException e) {
			throw new ReadException(this.store.getName(),e);
		}

	}

	private Object getFieldValueFromBinaryCursor(ResultSet aRs, DBAttributeDescriptor attrDescriptor) throws java.sql.SQLException {
		int fieldId = attrDescriptor.ordinal();
		int sqlType = attrDescriptor.getSqlType();
		byte[] byteBuf = aRs.getBytes(fieldId+1);
		if (byteBuf == null) {
			return null;
		} else {

			ByteBuffer buf = ByteBuffer.wrap(byteBuf);

			if (attrDescriptor.getDataType().equals(FeatureAttributeDescriptor.GEOMETRY)){
				if (attrDescriptor.getDataType() == FeatureAttributeDescriptor.GEOMETRY){
					if (byteBuf == null) {
						return null;
					}
//					return wkbParser.parse(byteBuf);
					return null;
				}

			}

			switch (sqlType) {
			case Types.VARCHAR:
				//FIXME Error
				return aRs.getString(fieldId);
//				return new String(buf.toString());
			case Types.FLOAT:
				return new Float(buf.getFloat());
			case Types.DOUBLE:
				return new Double(buf.getDouble());
			case Types.REAL:
				return new Float(buf.getFloat());
			case Types.INTEGER:
				return new Integer(buf.getInt());
			case Types.BIGINT:
				return new Long(buf.getLong());
			case Types.BIT:
				return new Boolean(byteBuf[0] == 1);
			case Types.BOOLEAN:
				return new Boolean(aRs.getBoolean(fieldId));
			case Types.DATE:
				long daysAfter2000 = buf.getInt() + 1;
				long msecs = daysAfter2000*24*60*60*1000;
				long real_msecs_date1 = (long) (XTypes.NUM_msSecs2000 + msecs);
				Date realDate1 = new Date(real_msecs_date1);
				return realDate1;
			case Types.TIME:
				// TODO:
				// throw new RuntimeException("TIME type not implemented yet");
				return "NOT IMPLEMENTED YET";
			case Types.TIMESTAMP:
				double segsReferredTo2000 = buf.getDouble();
				long real_msecs = (long) (XTypes.NUM_msSecs2000 + segsReferredTo2000*1000);
				Timestamp valTimeStamp = new Timestamp(real_msecs);
				return valTimeStamp;
			case Types.NUMERIC:
				// System.out.println(metaData.getColumnName(fieldId) + " "
				// + metaData.getColumnClassName(fieldId));
				short ndigits = buf.getShort();
				short weight = buf.getShort();
				short sign = buf.getShort();
				short dscale = buf.getShort();
				String strAux;
				if (sign == 0) {
					strAux = "+";
				} else {
					strAux = "-";
				}

				for (int iDigit = 0; iDigit < ndigits; iDigit++) {
					short digit = buf.getShort();
					strAux = strAux + digit;
					if (iDigit == weight) {
						strAux = strAux + ".";
					}

				}
				strAux = strAux + "0";
				BigDecimal dec;
				dec = new BigDecimal(strAux);
				// System.out.println(ndigits + "_" + weight + "_" + dscale
				// + "_" + strAux);
				// System.out.println(strAux + " Big= " + dec);
				return new Double(dec.doubleValue());


			default:
				//TODO ???
				throw new RuntimeException("Unsuported Type");
			}

		}
	}

	protected Object[] getPkFromResulsetBinary(ResultSet rs, DBFeatureType featureType) throws SQLException {
		String[] fieldsId = featureType.getFieldsId();
		Object[] result = new Object[fieldsId.length];
		for (int i=0;i<fieldsId.length;i++){
			result[i] = getFieldValueFromBinaryCursor(
					rs,
					(DBAttributeDescriptor)featureType.get(fieldsId[i])
				);

		}
		return result;
	}


	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.AbstractFeature#cloneFeature()
	 */
	protected Feature cloneFeature() throws DataException {
		// TODO Auto-generated method stub
		return null;
	}

}
