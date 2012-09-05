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

package org.gvsig.fmap.dal.store.postgresql;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataServerExplorer;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.exception.ValidateDataParametersException;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.spi.FeatureProvider;
import org.gvsig.fmap.dal.feature.spi.FeatureSetProvider;
import org.gvsig.fmap.dal.spi.DataStoreProviderServices;
import org.gvsig.fmap.dal.store.jdbc.JDBCHelper;
import org.gvsig.fmap.dal.store.jdbc.JDBCStoreProviderWriter;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynObjectManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostgreSQLStoreProvider extends JDBCStoreProviderWriter {

	public final static Logger logger = LoggerFactory
			.getLogger(PostgreSQLStoreProvider.class);

	public static String NAME = "PostgreSQL";
	public static String DESCRIPTION = "PostgreSQL source";
	private static final String DYNCLASS_NAME = "PostgreSQLStore";
	private static DynClass DYNCLASS = null;


	protected static void registerDynClass() {
		DynObjectManager dynman = ToolsLocator.getDynObjectManager();
		DynClass dynClass;
		if (DYNCLASS == null) {
			dynClass = dynman.add(DYNCLASS_NAME, DESCRIPTION);

			dynClass.extend(dynman.get(FeatureStore.DYNCLASS_NAME));
			DYNCLASS = dynClass;
		}
	}

	public PostgreSQLStoreProvider(PostgreSQLStoreParameters params,
			DataStoreProviderServices storeServices)
			throws InitializeException {
		super(params, storeServices, ToolsLocator.getDynObjectManager()
				.createDynObject(DYNCLASS));
	}

	private PostgreSQLStoreParameters getPGParameters() {
		return (PostgreSQLStoreParameters) this.getParameters();
	}

	protected JDBCHelper createHelper() throws InitializeException {
		return new PostgreSQLHelper(this, getPGParameters());
	}

	protected String fixFilter(String filter) {
		if (filter == null) {
			return null;
		}

		// Transform SRS to code
		// GeomFromText\s*\(\s*'[^']*'\s*,\s*('[^']*')\s*\)
		Pattern pattern = Pattern
				.compile("GeomFromText\\s*\\(\\s*'[^']*'\\s*,\\s*'([^']*)'\\s*\\)");
		Matcher matcher = pattern.matcher(filter);
		StringBuilder strb = new StringBuilder();
		int pos = 0;
		String srsCode;
		while (matcher.find(pos)) {
			strb.append(filter.substring(pos, matcher.start(1)));
			srsCode = matcher.group(1).trim();
			if (srsCode.startsWith("'")) {
				srsCode = srsCode.substring(1);
			}
			if (srsCode.endsWith("'")) {
				srsCode = srsCode.substring(0, srsCode.length() - 1);
			}
			strb.append(helper.getProviderSRID(srsCode));
			strb.append(filter.substring(matcher.end(1), matcher.end()));
			pos = matcher.end();

		}
		strb.append(filter.substring(pos));

		return strb.toString();
	}

	public String getName() {
		return NAME;
	}

	public FeatureSetProvider createSet(FeatureQuery query,
			FeatureType featureType) throws DataException {

		return new PostgreSQLSetProvider(this, query, featureType);
	}


	public DataServerExplorer getExplorer() throws ReadException {
		DataManager manager = DALLocator.getDataManager();
		PostgreSQLServerExplorerParameters exParams;
		PostgreSQLStoreParameters params = getPGParameters();
		try {
			exParams = (PostgreSQLServerExplorerParameters) manager
					.createServerExplorerParameters(PostgreSQLServerExplorer.NAME);
			exParams.setUrl(params.getUrl());
			exParams.setHost(params.getHost());
			exParams.setPort(params.getPort());
			exParams.setDBName(params.getDBName());
			exParams.setUser(params.getUser());
			exParams.setPassword(params.getPassword());
			exParams.setCatalog(params.getCatalog());
			exParams.setSchema(params.getSchema());
			exParams.setJDBCDriverClassName(params.getJDBCDriverClassName());
			exParams.setUseSSL(params.getUseSSL());

			return manager.createServerExplorer(exParams);
		} catch (DataException e) {
			throw new ReadException(this.getName(), e);
		} catch (ValidateDataParametersException e) {
			// TODO Auto-generated catch block
			throw new ReadException(this.getName(), e);
		}
	}

	public boolean allowAutomaticValues() {
		return true;
	}


	public boolean hasGeometrySupport() {
		return true;
	}

	// ************************************************************************************//


	// ************************************************************************************//



	protected PostgreSQLHelper getPgHelper() {
		return (PostgreSQLHelper) getHelper();
	}

	// ************************************************************************************//

	// ************************************************************************************//



	public boolean canWriteGeometry(int geometryType, int geometrySubtype)
			throws DataException {
		FeatureType type = getFeatureStore().getDefaultFeatureType();
		FeatureAttributeDescriptor geomAttr = type.getAttributeDescriptor(type
								.getDefaultGeometryAttributeName());
		if (geomAttr == null) {
			return false;
		}
		if (geometrySubtype != geomAttr.getGeometrySubType()) {
			return false;
		}
		switch (geomAttr.getGeometryType()) {
		case Geometry.TYPES.GEOMETRY:
			return true;

		case Geometry.TYPES.MULTISURFACE:
			return geometryType == Geometry.TYPES.MULTISURFACE
					|| geometryType == Geometry.TYPES.SURFACE;

		case Geometry.TYPES.MULTIPOINT:
			return geometryType == Geometry.TYPES.MULTIPOINT
					|| geometryType == Geometry.TYPES.POINT;

		case Geometry.TYPES.MULTICURVE:
			return geometryType == Geometry.TYPES.MULTICURVE
					|| geometryType == Geometry.TYPES.CURVE;

		case Geometry.TYPES.MULTISOLID:
			return geometryType == Geometry.TYPES.MULTISOLID
					|| geometryType == Geometry.TYPES.SOLID;

		default:
			return geometryType == geomAttr.getGeometryType();
		}

	}


	protected void addToListFeatureValues(FeatureProvider featureProvider,
			FeatureAttributeDescriptor attrOfList,
			FeatureAttributeDescriptor attr,
			List values) throws DataException {

		super.addToListFeatureValues(featureProvider, attrOfList, attr, values);
		if (attr.getDataType() == DataTypes.GEOMETRY) {
			values.add(helper.getProviderSRID(attr.getSRS()));
		}
	}

	protected void prepareAttributeForInsert(
			FeatureAttributeDescriptor attr, List fields, List values) {

		if (attr.getDataType() == DataTypes.GEOMETRY) {
			fields.add(helper.escapeFieldName(attr.getName()));
			values.add("GeomFromWKB(?,?)");
		} else {
			super.prepareAttributeForInsert(attr, fields, values);
		}

	}

	protected void prepareAttributeForUpdate(FeatureAttributeDescriptor attr,
			List values) {
		if (attr.getDataType() == DataTypes.GEOMETRY) {
			values.add(helper.escapeFieldName(attr.getName())
					+ " = GeomFromWKB(?,?)");
		} else {
			super.prepareAttributeForUpdate(attr, values);
		}
	}

	protected String getSqlStatementAddField(FeatureAttributeDescriptor attr,
			List additionalStatement) throws DataException {
		if (attr.getDataType() == DataTypes.GEOMETRY) {
			PostgreSQLStoreParameters params = getPGParameters();
			additionalStatement.add(((PostgreSQLHelper) helper)
					.getSqlGeometyFieldAdd(attr, params.getTable(), params
							.getSchema()));

		}
		return super.getSqlStatementAddField(attr, additionalStatement);

	}
	private Object getSqlGeometyFieldDrop(FeatureAttributeDescriptor attr) {
		StringBuilder strb = new StringBuilder();
		PostgreSQLStoreParameters params = getPGParameters();
		strb.append("Delete from geometry_columns where f_geometry_column = '");
		strb.append(attr.getName());
		strb.append("' and f_table_nam = '");
		strb.append(params.getTable());
		strb.append("' and f_table_schema = ");
		if (params.getSchema() == null || params.getSchema().length() == 0) {
			strb.append("current_schema()");
		} else {
			strb.append("'");
			strb.append(params.getSchema());
			strb.append("'");
		}
		if (params.getCatalog() != null && params.getCatalog().length() > 0) {
			strb.append(" and f_table_catalog = '");
			strb.append(params.getCatalog());
			strb.append("'");
		}
		return strb.toString();
	}

	protected String getSqlStatementDropField(FeatureAttributeDescriptor attr,
			List additionalStatement) {
		String result = super.getSqlStatementDropField(attr,
				additionalStatement);
		if (attr.getDataType() == DataTypes.GEOMETRY) {
			additionalStatement.add(getSqlGeometyFieldDrop(attr));
		}
		return result;
	}

	protected List getSqlStatementAlterField(
			FeatureAttributeDescriptor attrOrg,
			FeatureAttributeDescriptor attrTrg, List additionalStatement)
			throws DataException {
		//
		List actions = super.getSqlStatementAlterField(attrOrg, attrTrg,
				additionalStatement);
		StringBuilder strb;
		PostgreSQLStoreParameters params = getPGParameters();
		if (attrOrg.getDataType() != attrTrg.getDataType()) {
			if (attrOrg.getDataType() == DataTypes.GEOMETRY) {
				additionalStatement.add(getSqlGeometyFieldDrop(attrOrg));
			}
			if (attrTrg.getDataType() == DataTypes.GEOMETRY) {
				additionalStatement.addAll(((PostgreSQLHelper) helper)
						.getSqlGeometyFieldAdd(attrTrg, params.getTable(),
								params.getSchema()));
			}
		}
		if (attrOrg.getDataType() == attrTrg.getDataType()
				&& attrTrg.getDataType() == DataTypes.GEOMETRY) {
			// TODO Checks SRS and GeomType/Subtype
		}

		return actions;
	}

}
