/* gvSIG. Geographic Information System of the Valencian Government
*
* Copyright (C) 2007-2008 Infrastructures and Transports Department
* of the Valencian Government (CIT)
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
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
* MA  02110-1301, USA.
*
*/

/*
* AUTHORS (In addition to CIT):
* 2009 IVER T.I   {{Task}}
*/

/**
 *
 */
package org.gvsig.fmap.dal.store.mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.EditableFeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.EditableFeatureType;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.exception.UnsupportedDataTypeException;
import org.gvsig.fmap.dal.resource.spi.ResourceManagerProviderServices;
import org.gvsig.fmap.dal.store.jdbc.JDBCHelper;
import org.gvsig.fmap.dal.store.jdbc.JDBCHelperUser;
import org.gvsig.fmap.dal.store.jdbc.JDBCStoreParameters;
import org.gvsig.fmap.dal.store.jdbc.exception.JDBCException;
import org.gvsig.fmap.dal.store.jdbc.exception.JDBCExecuteSQLException;
import org.gvsig.fmap.dal.store.jdbc.exception.JDBCSQLException;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.operation.fromwkb.FromWKB;
import org.gvsig.fmap.geom.operation.fromwkb.FromWKBGeometryOperationContext;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.tools.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jmvivo
 *
 */
public class MySQLHelper extends JDBCHelper {

	private static Logger logger = LoggerFactory
			.getLogger(MySQLHelper.class);


	MySQLHelper(JDBCHelperUser consumer,
			MySQLConnectionParameters params)
			throws InitializeException {

		super(consumer, params);
	}

	protected void initializeResource() throws InitializeException {
		ResourceManagerProviderServices manager = (ResourceManagerProviderServices) DALLocator
		.getResourceManager();
		MySQLResource resource = (MySQLResource) manager
		.createResource(
				MySQLResource.NAME, new Object[] {
						params.getUrl(), params.getHost(),
						params.getPort(), params.getDBName(), params.getUser(),
						params.getPassword(),
						params.getJDBCDriverClassName(),
						((MySQLConnectionParameters) params).getUseSSL() });
		this.setResource(resource);
	}


	protected String getDefaultSchema(Connection conn)
			throws JDBCException {
		if (defaultSchema == null) {
			defaultSchema = params.getDBName();
		}

		return defaultSchema;
	}

	public Envelope getFullEnvelopeOfField(
			JDBCStoreParameters storeParams,
			String geometryAttrName, Envelope limit)
			throws DataException {

		StringBuilder strb = new StringBuilder();
		strb.append("Select asbinary(envelope(");
		strb.append(geometryAttrName);
		strb.append(")) from ");

		if (storeParams.getSQL() != null
				&& storeParams.getSQL().trim().length() == 0) {
			strb.append('(');
			strb.append(storeParams.getSQL());
			strb.append(") as __tmp__ ");
		} else {
			strb.append(storeParams.tableID());
		}


		if (limit != null){
			strb.append(" where  intersects(GeomFromText('");
			strb.append(limit.toString());
			strb.append("')), boundary(");
			strb.append(geometryAttrName);
			strb.append(")) ");
		}

		String sql = strb.toString();


		ResultSet rs = null;
		Statement st = null;
		String schema = null;
		Connection conn = null;

		GeometryManager geoMan = GeometryLocator.getGeometryManager();

		Envelope fullEnvelope = null;
		try {
			initializeFromWKBOperation();
		} catch (BaseException e1) {
			throw new ReadException(this.name, e1);
		}

		this.open();
		this.begin();
		try{
			conn = getConnection();
			st = conn.createStatement();
			try {
				rs = st.executeQuery(sql);
			} catch (java.sql.SQLException e) {
				throw new JDBCExecuteSQLException(sql, e);
			}
			while (rs.next()) {

				byte[] data = rs.getBytes(1);
				if (data == null) {
					continue;
				}

				fromWKBContext.setData(data);
				Geometry geom = (Geometry) fromWKB.invoke(null, fromWKBContext);

				if (fullEnvelope == null) {
					fullEnvelope = geom.getEnvelope();
				} else {
					fullEnvelope.add(geom.getEnvelope());
				}
			}

			return fullEnvelope;
		} catch (java.sql.SQLException e) {
			throw new JDBCSQLException(e);
		} catch (BaseException e) {
			throw new ReadException(user.getName(), e);
		} finally {
			try{ rs.close(); } catch (Exception e){};
			try{ st.close(); } catch (Exception e){};
			try{ conn.close(); } catch (Exception e){};
			rs = null;
			st = null;
			conn = null;
			end();
		}


	}

	protected void initializeFromWKBOperation() throws BaseException {
		if (fromWKB == null) {
			fromWKB = (FromWKB) GeometryLocator.getGeometryManager()
					.getGeometryOperation(FromWKB.CODE,
							Geometry.TYPES.GEOMETRY, Geometry.SUBTYPES.GEOM2D);
			fromWKBContext = new FromWKBGeometryOperationContext();

		}
	}

	public Geometry getGeometry(byte[] buffer) throws BaseException {
		if (buffer == null) {
			return null;
		}
		initializeFromWKBOperation();
		Geometry geom;
		try {
			fromWKBContext.setData(buffer);

			geom = (Geometry) fromWKB.invoke(null, fromWKBContext);
		} finally {
			fromWKBContext.setData(null);
		}
		return geom;
	}

	public String getSqlColumnTypeDescription(FeatureAttributeDescriptor attr) {

		switch (attr.getDataType()) {
		case DataTypes.STRING:
			return "VARCHAR(" + attr.getSize() + ")";
		case DataTypes.BOOLEAN:
			return "BOOL";

		case DataTypes.BYTE:
			return "TINYINT UNSIGNED";

		case DataTypes.DATE:
			return "DATE";

		case DataTypes.TIMESTAMP:
			return "TIMESTAMP";

		case DataTypes.TIME:
			return "TIME";

		case DataTypes.BYTEARRAY:
			if (attr.getSize() > 0) {
				return "BLOB(" + attr.getSize() + ")";
			} else {
				return "BLOB";
			}

		case DataTypes.DOUBLE:
			if (attr.getSize() > 0) {
				return "DOUBLE(" + attr.getSize() + "," + attr.getPrecision()
						+ ")";
			} else {
				return "DOBLE";
			}
		case DataTypes.FLOAT:
			return "FLOAT";

		case DataTypes.GEOMETRY:
			switch (attr.getGeometryType()) {
			case Geometry.TYPES.POINT:
				return "POINT";
			case Geometry.TYPES.CURVE:
				return "LINESTRING";
			case Geometry.TYPES.SURFACE:
				return "SURFACE";
			case Geometry.TYPES.SOLID:
				return "POLYGON";

			case Geometry.TYPES.MULTIPOINT:
				return "MULTIPOIN";
			case Geometry.TYPES.MULTICURVE:
				return "MULTILINESTRING";
			case Geometry.TYPES.MULTISURFACE:
				return "MULTISURFACE";
			case Geometry.TYPES.MULTISOLID:
				return "MULTIPOLYGON";

			default:
				return "GEOMETRY";
			}
		case DataTypes.INT:
			if (attr.getSize() > 0) {
				return "INT(" + attr.getSize() + ")";
			}
		case DataTypes.LONG:
			return "BIGINT";

		default:
			String typeName = (String) attr.getAdditionalInfo("SQLTypeName");
			if (typeName != null) {
				return typeName;
			}

			throw new UnsupportedDataTypeException(attr.getDataTypeName(), attr
					.getDataType());
		}
	}


	public String getSqlFieldName(FeatureAttributeDescriptor attribute) {
		if (attribute.getDataType() == DataTypes.GEOMETRY) {
			return "asBinary(" + super.getSqlFieldName(attribute) + ")";
		}
		return super.getSqlFieldName(attribute);
	}

	protected EditableFeatureAttributeDescriptor createAttributeFromJDBC(
			EditableFeatureType type, Connection conn,
			ResultSetMetaData rsMetadata, int colIndex) throws SQLException {
		int colType = rsMetadata.getColumnType(colIndex);
		if (colType == java.sql.Types.OTHER || colType == java.sql.Types.STRUCT
				|| colType == java.sql.Types.BLOB
				|| colType == java.sql.Types.BINARY) {
			Integer geoType = null;

			if (rsMetadata.getColumnTypeName(colIndex).equalsIgnoreCase(
					"geometry")) {
				geoType = new Integer(Geometry.TYPES.GEOMETRY);
			} else if (rsMetadata.getColumnTypeName(colIndex).equalsIgnoreCase("POINT")) {
				geoType = new Integer(Geometry.TYPES.POINT);
			} else if (rsMetadata.getColumnTypeName(colIndex).equalsIgnoreCase("LINESTRING")) {
				geoType = new Integer(Geometry.TYPES.CURVE);
			} else if (rsMetadata.getColumnTypeName(colIndex).equalsIgnoreCase("SURFACE")) {
				geoType = new Integer(Geometry.TYPES.SURFACE);
			} else if (rsMetadata.getColumnTypeName(colIndex).equalsIgnoreCase("POLYGON")) {
				geoType = new Integer(Geometry.TYPES.SOLID);
			} else if (rsMetadata.getColumnTypeName(colIndex).equalsIgnoreCase("MULTIPOIN")) {
				geoType = new Integer(Geometry.TYPES.MULTIPOINT);
			} else if (rsMetadata.getColumnTypeName(colIndex).equalsIgnoreCase("MULTILINESTRING")) {
				geoType = new Integer(Geometry.TYPES.MULTICURVE);
			} else if (rsMetadata.getColumnTypeName(colIndex).equalsIgnoreCase(
					"MULTISURFACE")) {
				geoType = new Integer(Geometry.TYPES.MULTISURFACE);
			} else if (rsMetadata.getColumnTypeName(colIndex).equalsIgnoreCase(
					"MULTIPOLYGON")) {
				geoType = new Integer(Geometry.TYPES.MULTISOLID);
			}
			if (geoType != null){
				EditableFeatureAttributeDescriptor attr = type.add(rsMetadata
						.getColumnName(colIndex), DataTypes.GEOMETRY);
				attr.setGeometryType(geoType.intValue());
				attr.setGeometrySubType(Geometry.SUBTYPES.GEOM2D);

				return attr;
			}

		}

		return super.createAttributeFromJDBC(type, conn, rsMetadata, colIndex);
	}

	public boolean allowAutomaticValues() {
		return Boolean.TRUE;
	}


	public String getSqlFieldDescription(FeatureAttributeDescriptor attr)
			throws DataException {

		/**
			column_definition:
			    data_type [NOT NULL | NULL] [DEFAULT default_value]
			      [AUTO_INCREMENT] [UNIQUE [KEY] | [PRIMARY] KEY]
			      [COMMENT 'string'] [reference_definition]

		 */

		StringBuilder strb = new StringBuilder();
		// name
		strb.append(escapeFieldName(attr.getName()));
		strb.append(" ");

		// Type
		strb.append(this.getSqlColumnTypeDescription(attr));
		strb.append(" ");

		boolean allowNull = attr.allowNull()
				&& !(attr.isPrimaryKey() || attr.isAutomatic());

		// Null
		if (allowNull) {
			strb.append("NULL ");
		} else {
			strb.append("NOT NULL ");
		}
		if (attr.isAutomatic()) {
			strb.append("AUTO_INCREMENT ");
		}

		// Default
		if (attr.getDefaultValue() == null) {
			if (allowNull) {
				strb.append("DEFAULT NULL ");
			}
		} else {
			String value = getDefaltFieldValueString(attr);
			strb.append("DEFAULT '");
			strb.append(value);
			strb.append("' ");
		}

		// Primery key
		if (attr.isPrimaryKey()) {
			strb.append("PRIMARY KEY ");
		}
		return strb.toString();
	}

	public boolean supportOffset() {
		return true;
	}

	public boolean supportsUnion() {
		return true;
	}


}
