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
package org.gvsig.fmap.dal.store.postgresql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.crs.CRSFactory;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.NewDataStoreParameters;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.EditableFeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.EditableFeatureType;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.exception.UnsupportedDataTypeException;
import org.gvsig.fmap.dal.feature.exception.UnsupportedGeometryException;
import org.gvsig.fmap.dal.resource.spi.ResourceManagerProviderServices;
import org.gvsig.fmap.dal.store.jdbc.ConnectionAction;
import org.gvsig.fmap.dal.store.jdbc.JDBCHelper;
import org.gvsig.fmap.dal.store.jdbc.JDBCHelperUser;
import org.gvsig.fmap.dal.store.jdbc.JDBCStoreParameters;
import org.gvsig.fmap.dal.store.jdbc.exception.JDBCException;
import org.gvsig.fmap.dal.store.jdbc.exception.JDBCExecutePreparedSQLException;
import org.gvsig.fmap.dal.store.jdbc.exception.JDBCExecuteSQLException;
import org.gvsig.fmap.dal.store.jdbc.exception.JDBCPreparingSQLException;
import org.gvsig.fmap.dal.store.jdbc.exception.JDBCSQLException;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.operation.fromwkb.FromWKB;
import org.gvsig.fmap.geom.operation.fromwkb.FromWKBGeometryOperationContext;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.tools.exception.BaseException;
import org.postgresql.PGResultSetMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jmvivo
 *
 */
public class PostgreSQLHelper extends JDBCHelper {

	private static Logger logger = LoggerFactory
			.getLogger(PostgreSQLHelper.class);

	private Map pgSR2SRSID = new TreeMap();
	private Map srsID2pgSR = new TreeMap();


	PostgreSQLHelper(JDBCHelperUser consumer,
			PostgreSQLConnectionParameters params)
			throws InitializeException {

		super(consumer, params);
	}

	protected void initializeResource() throws InitializeException {
		ResourceManagerProviderServices manager = (ResourceManagerProviderServices) DALLocator
		.getResourceManager();
		PostgreSQLResource resource = (PostgreSQLResource) manager
		.createResource(
				PostgreSQLResource.NAME, new Object[] {
						params.getUrl(), params.getHost(),
						params.getPort(), params.getDBName(), params.getUser(),
						params.getPassword(),
						params.getJDBCDriverClassName(),
						((PostgreSQLConnectionParameters) params).getUseSSL() });
		this.setResource(resource);
	}


	protected String getDefaultSchema(Connection conn)
			throws JDBCException {
		if (defaultSchema == null) {
			String sql = "Select current_schema()";
			ResultSet rs = null;
			Statement st = null;
			String schema = null;
			try {
				st = conn.createStatement();
				try {
					rs = st.executeQuery(sql);
				} catch (java.sql.SQLException e) {
					throw new JDBCExecuteSQLException(sql, e);
				}
				rs.next();
				schema = rs.getString(1);
			} catch (java.sql.SQLException e) {
				throw new JDBCSQLException(e);
			} finally {
				try {rs.close();} catch (Exception e) {logger.error("Exception clossing resulset", e);};
				try {st.close();} catch (Exception e) {logger.error("Exception clossing statement", e);};
				rs = null;
				st = null;
			}
			defaultSchema = schema;
		}

		return defaultSchema;
	}

	public Envelope getFullEnvelopeOfField(
			JDBCStoreParameters storeParams,
			String geometryAttrName, Envelope limit)
			throws DataException {

		StringBuilder strb = new StringBuilder();
		strb.append("Select asbinary(extent(");
		strb.append(escapeFieldName(geometryAttrName));
		strb.append(")) from ");

		if (storeParams.getSQL() != null
				&& storeParams.getSQL().trim().length() == 0) {
			strb.append('(');
			strb.append(storeParams.getSQL());
			strb.append(") as __extentfield__ ");
		} else {
			strb.append(storeParams.tableID());
		}


		if (limit != null){
			strb.append(" where  intersects(GeomFromText('");
			strb.append(limit.toString());
			strb.append("')), boundary(");
			strb.append(escapeFieldName(geometryAttrName));
			strb.append(")) ");
		}

		String sql = strb.toString();


		ResultSet rs = null;
		Statement st = null;
		String schema = null;
		Connection conn = null;

		GeometryManager geoMan = GeometryLocator.getGeometryManager();

		Envelope fullEnvelope = null;
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
			if (!rs.next()) {
				return null;
			}

			byte[] data = rs.getBytes(1);
			if (data == null) {
				return null;
			}
			initializeFromWKBOperation();
			fromWKBContext.setData(data);
			Geometry geom = (Geometry) fromWKB.invoke(null, fromWKBContext);

			fullEnvelope = geom.getEnvelope();

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

	/**
	 * Fill <code>featureType</code> geometry attributes with SRS and ShapeType
	 * information stored in the table GEOMETRY_COLUMNS
	 *
	 * @param conn
	 * @param rsMetadata
	 * @param featureType
	 * @throws ReadException
	 */
	protected void loadSRS_and_shapeType(Connection conn,
			ResultSetMetaData rsMetadata, EditableFeatureType featureType,
			String baseSchema, String baseTable)
			throws JDBCException {

		Statement st = null;
		ResultSet rs = null;
		try {
			// Sacamos la lista de los attributos geometricos
			EditableFeatureAttributeDescriptor attr;
			List geoAttrs = new ArrayList();

			Iterator iter = featureType.iterator();
			while (iter.hasNext()) {
				attr = (EditableFeatureAttributeDescriptor) iter.next();
				if (attr.getDataType() == DataTypes.GEOMETRY) {
					geoAttrs.add(attr);
				}
			}
			if (geoAttrs.size() < 1) {
				return;
			}


			// preparamos un set con las lista de tablas de origen
			// de los campos
			class TableId {
				public String schema=null;
				public String table=null;
				public String field = null;

				public void appendToSQL(StringBuilder strb) {
					if (schema == null || schema.length() == 0) {
						strb
								.append("( F_TABLE_SCHEMA = current_schema() AND F_TABLE_NAME = '");
					} else {
						strb.append("( F_TABLE_SCHEMA = '");
						strb.append(schema);
						strb.append("' AND F_TABLE_NAME = '");
					}
					strb.append(table);
					strb.append("' AND F_GEOMETRY_COLUMN = '");
					strb.append(field);
					strb.append("' )");
				}

			}
			Comparator cmp = new Comparator(){
				public int compare(Object arg0, Object arg1) {
					TableId a0 = (TableId) arg0;
					TableId a1 = (TableId) arg1;

					if (!a0.field.equals(a1.field)) {
						return -1;
					}
					if (!a0.table.equals(a1.table)) {
						return -1;
					}
					if (!a0.schema.equals(a1.schema)) {
						return -1;
					}
					return 0;
				}
			};
			TreeSet set = new TreeSet(cmp);
			TableId tableId;
			iter = geoAttrs.iterator();
			int rsIndex;
			while (iter.hasNext()) {
				attr = (EditableFeatureAttributeDescriptor) iter.next();
				tableId = new TableId();
				rsIndex = attr.getIndex() + 1;

				if (baseSchema == null && baseTable == null) {
					if (rsMetadata instanceof PGResultSetMetaData) {
						tableId.schema = ((PGResultSetMetaData) rsMetadata)
								.getBaseSchemaName(rsIndex);
						tableId.table = ((PGResultSetMetaData) rsMetadata)
								.getBaseTableName(rsIndex);
						tableId.field = ((PGResultSetMetaData) rsMetadata)
								.getBaseColumnName(rsIndex);

					} else {
						tableId.schema = rsMetadata.getSchemaName(rsIndex);
						tableId.table = rsMetadata.getTableName(rsIndex);
						tableId.field = rsMetadata.getColumnName(rsIndex);
					}
				} else {
					tableId.schema = baseSchema;
					tableId.table = baseTable;
					tableId.field = rsMetadata.getColumnName(rsIndex);
				}
				if (tableId.table == null || tableId.table.length() == 0) {
					// Si no tiene tabla origen (viene de algun calculo por ej.)
					// lo saltamos ya que no estara en la tabla GEOMETRY_COLUMNS
					continue;
				}
				set.add(tableId);
			}

			if (set.size() == 0) {
				return;
			}

			// Preparamos una sql para que nos saque el resultado
			StringBuilder strb = new StringBuilder();
			strb.append("Select geometry_columns.*,auth_name || ':' || auth_srid as SRSID ");
			strb.append("from geometry_columns left join spatial_ref_sys on ");
			strb.append("geometry_columns.srid = spatial_ref_sys.srid WHERE ");
			iter = set.iterator();
			for (int i=0;i<set.size()-1;i++) {
				tableId = (TableId) iter.next();
				tableId.appendToSQL(strb);
				strb.append(" OR ");
			}
			tableId = (TableId) iter.next();
			tableId.appendToSQL(strb);
			String sql = strb.toString();


			st = conn.createStatement();
			try {
				rs = st.executeQuery(sql);
			} catch (SQLException e) {
				throw new JDBCExecuteSQLException(sql, e);
			}
			String srsID;
			int pgSrid;
			int geometryType;
			int geometrySubtype;
			String geomTypeStr;
			int dimensions;
			IProjection srs;

			while (rs.next()){
				srsID = rs.getString("SRSID");
				pgSrid = rs.getInt("SRID");
				geomTypeStr = rs.getString("TYPE").toUpperCase();
				geometryType = Geometry.TYPES.GEOMETRY;
				if (geomTypeStr.startsWith("POINT")) {
					geometryType = Geometry.TYPES.POINT;
				} else if (geomTypeStr.startsWith("LINESTRING")) {
					geometryType = Geometry.TYPES.CURVE;
				} else if (geomTypeStr.startsWith("POLYGON")) {
					geometryType = Geometry.TYPES.SURFACE;
				} else if (geomTypeStr.startsWith("MULTIPOINT")) {
					geometryType = Geometry.TYPES.MULTIPOINT;
				} else if (geomTypeStr.startsWith("MULTILINESTRING")) {
					geometryType = Geometry.TYPES.MULTICURVE;
				} else if (geomTypeStr.startsWith("MULTIPOLYGON")) {
					geometryType = Geometry.TYPES.MULTISURFACE;
				}
				dimensions = rs.getInt("coord_dimension");
				geometrySubtype = Geometry.SUBTYPES.GEOM2D;
				if (dimensions > 2) {
					if (dimensions == 3) {
						if (geomTypeStr.endsWith("M")) {
							geometrySubtype = Geometry.SUBTYPES.GEOM2DM;
						} else {
							geometrySubtype = Geometry.SUBTYPES.GEOM3D;
						}

					} else {
						geometrySubtype = Geometry.SUBTYPES.GEOM3DM;
					}
				}
				addToPgSRToSRSID(pgSrid, srsID);


				iter = geoAttrs.iterator();
				while (iter.hasNext()) {
					attr = (EditableFeatureAttributeDescriptor) iter.next();
					rsIndex = attr.getIndex() + 1;
					if (!rsMetadata.getColumnName(rsIndex).equals(
							rs.getString("f_geometry_column"))) {
						continue;
					}

					if (baseSchema == null && baseTable == null) {

						if (rsMetadata instanceof PGResultSetMetaData) {
							if (!((PGResultSetMetaData) rsMetadata)
									.getBaseTableName(rsIndex).equals(
											rs.getString("f_table_name"))) {
								continue;
							}
							String curSchema = rs.getString("f_table_schema");
							String metaSchema = ((PGResultSetMetaData) rsMetadata)
									.getBaseSchemaName(rsIndex);
							if (!metaSchema.equals(curSchema)) {
								if (metaSchema.length() == 0
										&& metaSchema == getDefaultSchema(conn)) {
								} else {
									continue;
								}
							}

						} else {

							if (!rsMetadata.getTableName(rsIndex).equals(
									rs.getString("f_table_name"))) {
								continue;
							}
							String curSchema = rs.getString("f_table_schema");
							String metaSchema = rsMetadata
									.getSchemaName(rsIndex);
							if (!metaSchema.equals(curSchema)) {
								if (metaSchema.length() == 0
										&& metaSchema == getDefaultSchema(conn)) {
								} else {
									continue;
								}
							}
						}
					}
					attr.setGeometryType(geometryType);
					attr.setGeometrySubType(geometrySubtype);
					if (srsID != null && srsID.length() > 0) {
						attr.setSRS(CRSFactory.getCRS(srsID));
					}
					iter.remove();
				}
				iter = geoAttrs.iterator();
				while (iter.hasNext()) {
					attr = (EditableFeatureAttributeDescriptor) iter.next();
					attr.setSRS(null);
					attr.setGeometryType(Geometry.TYPES.GEOMETRY);

				}
			}

		} catch (java.sql.SQLException e) {
			throw new JDBCSQLException(e);
		} finally {
			try {rs.close();} catch (Exception e) {	};
			try {st.close();} catch (Exception e) {	};
		}

	}


	public String getSqlColumnTypeDescription(FeatureAttributeDescriptor attr) {
		if (attr.getDataType() == DataTypes.GEOMETRY) {
			return "geometry";
		}
		return super.getSqlColumnTypeDescription(attr);
	}


	public int getPostgisGeomDimensions(int geometrySubType) {
		switch (geometrySubType) {
		case Geometry.SUBTYPES.GEOM2D:
			return 2;
		case Geometry.SUBTYPES.GEOM2DM:
		case Geometry.SUBTYPES.GEOM3D:
			return 3;

		case Geometry.SUBTYPES.GEOM3DM:
			return 4;
		default:
			throw new UnsupportedDataTypeException(
					DataTypes.TYPE_NAMES[DataTypes.GEOMETRY],
					DataTypes.GEOMETRY);
		}
	}

	public String getPostgisGeomType(int geometryType, int geometrySubType) {
		String pgGeomType;
		switch (geometryType) {
		case Geometry.TYPES.GEOMETRY:
			pgGeomType = "GEOMETRY";
			break;
		case Geometry.TYPES.POINT:
			pgGeomType = "POINT";
			break;
		case Geometry.TYPES.CURVE:
			pgGeomType = "LINESTRING";
			break;
		case Geometry.TYPES.SURFACE:
			pgGeomType = "POLYGON";
			break;
		case Geometry.TYPES.MULTIPOINT:
			pgGeomType = "MULTIPOINT";
			break;
		case Geometry.TYPES.MULTICURVE:
			pgGeomType = "MULTILINESTRING";
			break;
		case Geometry.TYPES.MULTISURFACE:
			pgGeomType = "MULTIPOLYGON";
			break;
		default:
			throw new UnsupportedGeometryException(geometryType,
					geometrySubType);
		}
		if (geometrySubType == Geometry.SUBTYPES.GEOM2DM
				|| geometrySubType == Geometry.SUBTYPES.GEOM3DM) {
			pgGeomType = pgGeomType + "M";
		} else if (geometrySubType == Geometry.SUBTYPES.GEOM2DZ) {
			throw new UnsupportedGeometryException(geometryType,
					geometrySubType);
		}
		return pgGeomType;
	}

	public int getProviderSRID(String srs) {
		if (srs != null) {
			Integer pgSRID = (Integer) srsID2pgSR.get(srs);
			if (pgSRID != null) {
				return pgSRID.intValue();
			}

			return searchpgSRID(srs);

		}
		return -1;
	}


	public int getProviderSRID(IProjection srs) {
		if (srs != null) {
			Integer pgSRID = (Integer) srsID2pgSR.get(srs.getAbrev());
			if (pgSRID != null) {
				return pgSRID.intValue();
			}

			return searchpgSRID(srs);

		}
		return -1;
	}

	private int searchpgSRID(final IProjection srs) {
		if (srs == null) {
			return -1;
		}
		return searchpgSRID(srs.getAbrev());
	}

	private int searchpgSRID(final String srsID) {
		if (srsID == null) {
			return -1;
		}

		ConnectionAction action = new ConnectionAction(){

			public Object action(Connection conn) throws DataException {
				// select srid from spatial_ref_sys where auth_name = 'EPSG' and
				// auth_srid = 23030
				String[] abrev = srsID.split(":");
				StringBuilder sqlb = new StringBuilder();
				sqlb.append("select srid from spatial_ref_sys where ");
				if (abrev.length > 1) {
					sqlb.append("auth_name = ? and ");
				}
				sqlb.append("auth_srid = ?");

				String sql = sqlb.toString();
				PreparedStatement st;
				try {
					st = conn.prepareStatement(sql);
				} catch (SQLException e){
					throw new JDBCPreparingSQLException(sql,e);
				}
				ResultSet rs = null;
				try{
					int i=0;
					if (abrev.length > 1){
						st.setString(i+1, abrev[i]);
						i++;
					}
					st.setInt(i + 1, Integer.parseInt(abrev[i]));

					try{
						rs = st.executeQuery();
					} catch (SQLException e){
						throw new JDBCExecutePreparedSQLException(sql, abrev, e);
					}

					if (!rs.next()) {
						return null;
					}

					return new Integer(rs.getInt(1));

				} catch (SQLException e){
					throw new JDBCSQLException(e);
				} finally{
					try {rs.close(); } catch (Exception e) {};
					try {st.close(); } catch (Exception e) {};
				}

			}

		};

		Integer pgSRSID = null;
		try {
			pgSRSID = (Integer) doConnectionAction(action);
		} catch (Exception e) {
			logger.error("Excetion searching pgSRS", e);
			return -1;
		}

		if (pgSRSID != null) {
			addToPgSRToSRSID(pgSRSID.intValue(), srsID);
			return pgSRSID.intValue();
		}
		return -1;

	}

	private void addToPgSRToSRSID(int pgSRID, String srsId) {
		if (pgSRID < 0 || srsId == null || srsId.length() == 0) {
			return;
		}
		Integer pgSRIDInteger = new Integer(pgSRID);
		pgSR2SRSID.put(pgSRIDInteger, srsId);
		srsID2pgSR.put(srsId, pgSRIDInteger);
	}

	public List getSqlGeometyFieldAdd(FeatureAttributeDescriptor attr,
			String table, String schema) {
		// SELECT AddGeometryColumn({schema}, {table}, {field}, {srid}(int),
		// {geomType}(Str), {dimensions}(int))

		// gemoType:
		/*
		 * POINT, LINESTRING, POLYGON, MULTIPOINT, MULTILINESTRING,
		 * MULTIPOLYGON, GEOMETRYCOLLECTION POINTM, LINESTRINGM, POLYGONM,
		 * MULTIPOINTM, MULTILINESTRINGM, MULTIPOLYGONM, GEOMETRYCOLLECTIONM
		 */

		List sqls = new ArrayList();

		StringBuilder strb = new StringBuilder();
		strb.append("SELECT AddGeometryColumn('");
		if (schema != null && schema.length() > 0) {
			strb.append(schema);
			strb.append("', '");
		}
		strb.append(table);
		strb.append("', '");
		strb.append(attr.getName());
		strb.append("', ");
		// strb.append("-1");
		strb.append(getProviderSRID(attr.getSRS()));
		strb.append(", '");
		strb.append(getPostgisGeomType(attr.getGeometryType(), attr
				.getGeometrySubType()));
		strb.append("', ");
		strb.append(getPostgisGeomDimensions(attr.getGeometrySubType()));
		strb.append(")");


		sqls.add(strb.toString());

		/*ALTER TABLE muni10000_peq_test DROP CONSTRAINT enforce_srid_the_geom;*/
		/*
		strb = new StringBuilder();
		strb.append("Alter table ");
		if (schema != null && schema.length() > 0) {
			strb.append(schema);
			strb.append(".");
		}
		strb.append("f_table_name = '");
		strb.append(table);
		strb.append("' AND f_geometry_column = '");
		strb.append(attr.getName());
		strb.append("' AND srid = -1");


		sqls.add(strb.toString());
		*/
		return sqls;
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
		if (rsMetadata.getColumnType(colIndex) == java.sql.Types.OTHER) {
			if (rsMetadata.getColumnTypeName(colIndex).equalsIgnoreCase(
					"geometry")) {
				return type.add(rsMetadata.getColumnName(colIndex),
						DataTypes.GEOMETRY);

			}
		}

		return super.createAttributeFromJDBC(type, conn, rsMetadata, colIndex);
	}

	public List getAdditionalSqlToCreate(NewDataStoreParameters ndsp,
			FeatureType fType) {
		FeatureAttributeDescriptor attr;
		Iterator iter = fType.iterator();
		List result = new ArrayList();
		PostgreSQLNewStoreParameters pgNdsp = (PostgreSQLNewStoreParameters) ndsp;
		while (iter.hasNext()){
			attr = (FeatureAttributeDescriptor) iter.next();
			if (attr.getDataType() == DataTypes.GEOMETRY){
				result.addAll(getSqlGeometyFieldAdd(attr, pgNdsp.getTable(),
						pgNdsp
						.getSchema()));
			}
		}

		return result;
	}

	public String getSqlFieldDescription(FeatureAttributeDescriptor attr)
			throws DataException {
		if (attr.getDataType() == DataTypes.GEOMETRY){
			return null;
		}
		return super.getSqlFieldDescription(attr);
	}


	public boolean allowAutomaticValues() {
		return Boolean.TRUE;
	}

	public boolean supportOffset() {
		return true;
	}

	public boolean supportsUnion() {
		return true;
	}

}
