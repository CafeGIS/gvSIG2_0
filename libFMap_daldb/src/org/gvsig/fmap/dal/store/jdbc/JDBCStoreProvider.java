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
package org.gvsig.fmap.dal.store.jdbc;

import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataServerExplorer;
import org.gvsig.fmap.dal.DataStoreNotification;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.exception.CloseException;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.OpenException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.exception.ValidateDataParametersException;
import org.gvsig.fmap.dal.feature.EditableFeatureType;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.spi.AbstractFeatureStoreProvider;
import org.gvsig.fmap.dal.feature.spi.FeatureProvider;
import org.gvsig.fmap.dal.feature.spi.FeatureReferenceProviderServices;
import org.gvsig.fmap.dal.feature.spi.FeatureSetProvider;
import org.gvsig.fmap.dal.resource.Resource;
import org.gvsig.fmap.dal.resource.exception.ResourceBeginException;
import org.gvsig.fmap.dal.resource.spi.ResourceProvider;
import org.gvsig.fmap.dal.spi.DataStoreProviderServices;
import org.gvsig.fmap.dal.store.jdbc.exception.InvalidResultSetIdException;
import org.gvsig.fmap.dal.store.jdbc.exception.JDBCException;
import org.gvsig.fmap.dal.store.jdbc.exception.JDBCExecuteSQLException;
import org.gvsig.fmap.dal.store.jdbc.exception.JDBCSQLException;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.operation.towkb.ToWKB;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynObject;
import org.gvsig.tools.dynobject.DynObjectManager;
import org.gvsig.tools.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author jmvivo
 *
 */
public class JDBCStoreProvider extends AbstractFeatureStoreProvider
		implements JDBCHelperUser {

	final static private Logger logger = LoggerFactory
			.getLogger(JDBCStoreProvider.class);

	private List resulsetList;

	public static String NAME = "JDBC";
	private static final String DYNCLASS_NAME = "JDBCStore";
	public static String DESCRIPTION = "JDBC source";
	private static DynClass DYNCLASS = null;

	private long mlsecondsToZombie = 1000 * 60 * 10; // 10 Min

	protected JDBCHelper helper;

	protected boolean directSQLMode;

	private Long totalCount = null;


	public JDBCStoreProvider(JDBCStoreParameters params,
			DataStoreProviderServices storeServices) throws InitializeException {
		this(params, storeServices, ToolsLocator.getDynObjectManager()
				.createDynObject(DYNCLASS));
	}

	protected JDBCStoreProvider(JDBCStoreParameters params,
			DataStoreProviderServices storeServices, DynObject metadata)
			throws InitializeException {
		super(params, storeServices, metadata);

		resulsetList = new ArrayList(10);

		helper = createHelper();
		if (params.getSQL() != null && (params.getSQL()).trim().length() > 0) {
			directSQLMode = true;
		}


		this.setDynValue("DefaultSRS", null);
		this.setDynValue("Envelope", null);
		this.initFeatureType();
	}



	protected JDBCStoreParameters getJDBCParameters() {
		return (JDBCStoreParameters) this.getParameters();
	}


	protected static void registerDynClass() {
		DynObjectManager dynman = ToolsLocator.getDynObjectManager();
		DynClass dynClass;
		if (DYNCLASS == null) {
			dynClass = dynman.add(DYNCLASS_NAME, DESCRIPTION);

			dynClass.extend(dynman.get(FeatureStore.DYNCLASS_NAME));
			DYNCLASS = dynClass;
		}
	}

	/**
	 * Load data form a resulset.<br>
	 *
	 * <strong>Note:</strong><br>
	 * this method have to perform <code>resouceBegin</code> at the begining and
	 * <code>resourceEnd</code> at the end of execution.
	 *
	 *
	 * @param data
	 * @param resulsetID
	 *
	 * @return
	 * @throws DataException
	 */
	public void loadFeatureProvider(FeatureProvider data, int resultsetID)
			throws DataException {
		this.resourceBegin();
		try {
			ResultSet rs = getResultSet(resultsetID);
			FeatureAttributeDescriptor attr;
			Iterator iter = data.getType().iterator();
			while (iter.hasNext()) {
				attr = (FeatureAttributeDescriptor) iter.next();
				loadFeatureProviderValue(data, rs, attr);
			}
		} finally {
			this.resourceEnd();
		}



	}


	protected void loadFeatureProviderValue(FeatureProvider data, ResultSet rs,
			FeatureAttributeDescriptor attr) throws DataException {
		if (attr.getDataType() == DataTypes.GEOMETRY) {
			byte[] buffer;
			try {
				buffer = rs.getBytes(attr.getIndex() + 1);
				if (buffer == null) {
					data.set(attr.getIndex(), null);
				} else {
					data.set(attr.getIndex(), this.helper.getGeometry(buffer));
				}
			} catch (SQLException e) {
				throw new JDBCSQLException(e);
			} catch (BaseException e) {
				throw new ReadException(getName(), e);
			}

		} else {
			try {
				data.set(attr.getIndex(), rs.getObject(attr.getIndex() + 1));
			} catch (SQLException e) {
				throw new JDBCSQLException(e);
			}
		}
	}

	public long getTimeToResulSetZombie() {
		return mlsecondsToZombie;
	}

	public void setTimeToResulSetZombie(long mlSeconds) {
		mlsecondsToZombie = mlSeconds;
	}

	private class ResultSetInfo{
		private ResultSet resultSet = null;
		private long lastUse = 0;

		public ResultSetInfo(ResultSet resulSet) {
			this.resultSet = resulSet;
			used();
		}

		private void used() {
			lastUse = System.currentTimeMillis();
		}


		public ResultSet get() {
			used();
			return resultSet;
		}

		public boolean isZombie() {
			return System.currentTimeMillis() - lastUse > mlsecondsToZombie;
		}
	}

	public final int createResultSet(String sql)
			throws DataException {
		return createResultSet(sql, null);
	}

	public final int createResultSet(String sql, Object[] values)
			throws DataException {
		synchronized (this) {
			checksResulsets();
			resourceBegin();
			try {
				ResultSetInfo newRs = new ResultSetInfo(createNewResultSet(sql,
						values));
				int newId = getNewId();
				if (newId < 0) {
					newId = resulsetList.size();
					resulsetList.add(newRs);
				} else {
					resulsetList.set(newId, newRs);
				}
				if (logger.isDebugEnabled()) {
					logger.debug(" id: {} (total {})", newId,
							getResultsetOpenCount());
				}

				return newId;
			} finally {
				resourceEnd();
			}

		}
	}

	private int getNewId() {
		int newId;
		if (resulsetList.size() < 1) {
			return -1;
		}
		for (newId = 0; newId < resulsetList.size(); newId++) {
			if (resulsetList.get(newId) == null) {
				return newId;
			}
		}
		return -1;
	}

	protected final void forceCloseAllResultSet()
			throws ResourceBeginException,
			JDBCException {
		synchronized (this) {
			Iterator iter = resulsetList.iterator();
			Integer rsID = null;
			while (iter.hasNext()) {
				rsID = (Integer) iter.next();
				if (rsID != null) {
					try {
						forceCloseResultSet(rsID.intValue());
					} catch (InvalidResultSetIdException e) {
						continue;
					}
				}
				iter.remove();
			}

		}
	}

	protected final void forceCloseResultSet(int rsID)
			throws ResourceBeginException, JDBCException,
			InvalidResultSetIdException {
		logger.warn("Close forced of resultSet ({})", rsID);
		closeResulset(rsID);
	}

	protected final ResultSet getResultSet(int resultsetID)
			throws InvalidResultSetIdException {
		if (resultsetID >= resulsetList.size()) {
			throw new InvalidResultSetIdException(resultsetID);
		}
		ResultSetInfo rsInfo = (ResultSetInfo) resulsetList.get(resultsetID);
		if (rsInfo == null) {
			throw new InvalidResultSetIdException(resultsetID);
		}
		return rsInfo.get();

	}

	private ResultSet dropResultSet(int resultsetID)
			throws InvalidResultSetIdException {
		if (resultsetID >= resulsetList.size()) {
			throw new InvalidResultSetIdException(resultsetID);
		}
		ResultSetInfo rsInfo = (ResultSetInfo) resulsetList.get(resultsetID);
		if (rsInfo == null) {
			throw new InvalidResultSetIdException(resultsetID);
		}
		if (resultsetID == resulsetList.size() - 1) {
			resulsetList.remove(resultsetID);
		} else {
			resulsetList.set(resultsetID, null);
		}
		return rsInfo.get();
	}


	public final boolean resulsetNext(int resultsetID) throws JDBCException,
			InvalidResultSetIdException, ResourceBeginException {
		ResultSet rs = getResultSet(resultsetID);
		resourceBegin();
		try {
			return rs.next();
		} catch (SQLException e) {
			throw new JDBCSQLException(e);
		} finally {
			resourceEnd();
		}
	}

	public final void closeResulset(int resultsetID)
			throws JDBCException,
			InvalidResultSetIdException, ResourceBeginException {
		synchronized (this) {
			resourceBegin();
			try {
				ResultSet rs = dropResultSet(resultsetID);
				closeResulset(rs);
				if (logger.isDebugEnabled()) {
					logger.debug(" id: " + resultsetID + " (total "
							+ getResultsetOpenCount() + ")");
				}

			} finally {
				resourceEnd();
			}
			checksResulsets();
		}
	}

	public final void checksResulsets() throws JDBCException,
			InvalidResultSetIdException, ResourceBeginException {
		synchronized (this) {
			resourceBegin();
			try {
				ResultSetInfo rsInfo;
				for (int i = 0; i < resulsetList.size(); i++) {
					rsInfo = (ResultSetInfo) resulsetList.get(i);
					if (rsInfo == null) {
						continue;
					}
					if (rsInfo.isZombie()) {
						forceCloseResultSet(i);
					}
				}

			} finally {
				resourceEnd();
			}

		}
	}


	private int getResultsetOpenCount() {
		int count = 0;
		Iterator iter = resulsetList.iterator();
		while (iter.hasNext()) {
			if (iter.next() != null) {
				count++;
			}
		}
		return count;
	}

	protected void closeResulset(ResultSet rs) throws JDBCException,
			ResourceBeginException {
		resourceBegin();
		try {
			Statement st = rs.getStatement();
			Connection con = st.getConnection();
			try {
				rs.close();
			} finally {
				// TODO revisar esto
				try{ st.close();  } catch (Exception ex){ };
				try{ con.close(); } catch (Exception ex){ };
			}
		} catch (SQLException e) {
			throw new JDBCSQLException(e);
		} finally {
			resourceEnd();
		}

	}

	protected final int openResulsetCount() {
		int count = 0;
		Iterator iter = resulsetList.iterator();
		while (iter.hasNext()) {
			if (iter.next() != null) {
				count++;
			}
		}
		return count;
	}

	public boolean closeResourceRequested(ResourceProvider resource) {
		try {
			checksResulsets();
			return openResulsetCount() == 0 && closeResource(resource);
		} catch (DataException e) {
			logger.error("Exception throws", e);
			return false;
		}
	}


	protected String fixFilter(String filter) {
		if (filter == null) {
			return null;
		}

		return filter;
	}

	protected JDBCHelper createHelper() throws InitializeException {
		return new JDBCHelper(this, getJDBCParameters());
	}

	protected JDBCHelper getHelper() {
		return helper;
	}

	protected void resetCount() {
		totalCount = null;
	}

	/**
	 * Get feature count for a <code>filter</code>.<br>
	 *
	 * <code>filter</code> can be <code>null</code>.<br>
	 *
	 * <strong>Note:</strong><br>
	 * this method have to perform <code>resouceBegin</code> at the begining and
	 * <code>resourceEnd</code> at the end of execution.
	 *
	 *
	 * @param filter
	 * @return
	 * @throws DataException
	 */
	protected long getCount(String filter) throws DataException {
		this.open();
		if (filter == null && totalCount != null) {
			return totalCount.longValue();
		}
		long count = 0;
		String sql = compoundCountSelect(filter);
		resourceBegin();
		try {
			ResultSet rs = createNewResultSet(sql, null);
			try {
				if (rs.next()) {
					count = rs.getLong(1);
				}
			} catch (SQLException e) {
				throw new JDBCSQLException(e);
			} finally {
				closeResulset(rs);
			}
		} finally {
			resourceEnd();
		}
		if (filter == null) {
			totalCount = new Long(count);
		}
		return count;
	}

	public void close() throws CloseException {
		helper.close();
	}

	public void open() throws OpenException {
		helper.open();
	}


	public FeatureProvider getFeatureProviderByReference(
			FeatureReferenceProviderServices reference) throws DataException {
		return getFeatureProviderByReference(reference, getFeatureStore()
				.getDefaultFeatureType());
	}

	public FeatureProvider getFeatureProviderByReference(
			FeatureReferenceProviderServices reference, FeatureType featureType)
			throws DataException {
		open();
		resourceBegin();
		try {
			StringBuilder filter = new StringBuilder();
			FeatureAttributeDescriptor[] pk = getFeatureStore().getFeatureType(
					featureType.getId()).getPrimaryKey();

			List values = new ArrayList();

			int i;
			for (i = 0; i < pk.length - 1; i++) {
				values.add(helper.dalValueToJDBC(pk[i], reference
						.getKeyValue(pk[i].getName())));
				filter.append(helper.getSqlFieldName(pk[i]));
				filter.append(" = ? AND ");
			}
			values.add(helper.dalValueToJDBC(pk[i], reference.getKeyValue(pk[i]
					.getName())));
			filter.append(helper.getSqlFieldName(pk[i]));
			filter.append(" = ? ");

			String sql = compoundSelect(featureType, filter.toString(), null,
					1, 0);

			FeatureProvider data;
			int rsId = createResultSet(sql, values.toArray());
			try {
				if (!resulsetNext(rsId)) {
					// FIXME Exception
					throw new RuntimeException("Reference Not found");
				}
				data = createFeatureProvider(featureType);
				loadFeatureProvider(data, rsId);
			} finally {
				closeResulset(rsId);
			}

			return data;

		} finally {
			resourceEnd();
		}

	}

	public int getOIDType() {
		return DataTypes.UNKNOWN;
	}

	protected void initFeatureType() throws InitializeException {

		EditableFeatureType edFType = null;
		try {
			edFType = this.getStoreServices().createFeatureType();

			helper.loadFeatureType(edFType, getJDBCParameters());

		} catch (DataException e) {
			throw new InitializeException(this.getName(), e);
		}

		FeatureType defaultType = edFType.getNotEditableCopy();
		List types = new ArrayList(1);
		types.add(defaultType);
		this.getStoreServices().setFeatureTypes(types, defaultType);
		try {
			loadMetadata();
		} catch (DataException e) {
			throw new InitializeException(e);
		}
	}

	protected ResultSet createNewResultSet(String sql, Object[] values)
			throws DataException {
		this.open();
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		this.resourceBegin();
		try {
			conn = this.helper.getConnection();
			st = conn.prepareStatement(sql);

			if (values != null) {
				Object value;
				for (int i = 0; i < values.length; i++) {
					value = values[i];
					if (value instanceof Geometry) {
						byte[] bytes;
						try {
							bytes = (byte[]) ((Geometry) value)
									.invokeOperation(ToWKB.CODE, null);
						} catch (BaseException e) {
							// FIXME
							throw new InvalidParameterException();
						}
						st.setBytes(i + 1, bytes);
					}
					st.setObject(i + 1, value);
				}

			}

			try {
				rs = st.executeQuery();
			} catch (SQLException e1) {
				try {st.close();} catch (Exception e2) {};
				try {conn.close();} catch (Exception e2) {}	;
				throw new JDBCExecuteSQLException(sql, e1);
			}
			rs.setFetchSize(5000); // TODO add to params?
			return rs;
		} catch (SQLException e) {
			// TODO throw exception ???
			try {rs.close();} catch (Exception e1) {};
			try {st.close();} catch (Exception e1) {};
			try {conn.close();} catch (Exception e1) {};
			throw new JDBCSQLException(e);
		} finally {
			this.resourceEnd();
		}
	}

	/**
	 * Permform a {@link Resource#begin()} to the current provider resources.
	 *
	 */

	protected void resourceBegin() throws ResourceBeginException {
		this.helper.begin();

	}

	/**
	 * Permform a {@link Resource#end()} to the current provider resources.
	 */

	protected void resourceEnd() {
		this.helper.end();
	}

	protected boolean closeResource(ResourceProvider resource) {
		try {
			this.helper.close();
		} catch (CloseException e) {
			logger.error("Exception in close Request", e);
		}
		return !this.helper.isOpen();
	}

	protected String compoundCountSelect(String filter) {
		if (this.directSQLMode) {
			return null;
		}
		// Select
		StringBuilder sql = new StringBuilder();
		sql.append("Select count(");
		String[] pkFields = getJDBCParameters().getPkFields();
		if (pkFields != null && pkFields.length == 1) {
			sql.append(helper.escapeFieldName(pkFields[0]));
		} else {
			sql.append('*');

		}
		sql.append(") ");

		sql.append("from ");

		sql.append(getJDBCParameters().tableID());
		sql.append(' ');

		appendWhere(sql, filter);

		return sql.toString();
	}

	protected void appendWhere(StringBuilder sql, String filter) {
		filter = fixFilter(filter);
		String initialFilter = getJDBCParameters().getInitialFilter();
		if ((initialFilter != null && initialFilter.length() != 0)
				|| (filter != null && filter.length() != 0)) {
			sql.append("where (");

			if (initialFilter != null && initialFilter.length() != 0
					&& filter != null && filter.length() != 0) {
				// initialFilter + filter
				sql.append('(');
				sql.append(initialFilter);
				sql.append(") and (");
				sql.append(filter);
				sql.append(')');
			} else if (initialFilter != null && initialFilter.length() != 0) {
				// initialFilter only
				sql.append(initialFilter);
			} else {
				// filter only
				sql.append(filter);
			}
			sql.append(") ");
		}
	}

	protected void loadMetadata() throws DataException {
		IProjection srs = getJDBCParameters().getSRS();

		if (srs == null) {
			srs = getFeatureStore().getDefaultFeatureType().getDefaultSRS();
		}

		this.setDynValue("DefaultSRS", srs);

		String defGeomName = this.getFeatureStore().getDefaultFeatureType()
				.getDefaultGeometryAttributeName();
		Envelope env = null;
		if (defGeomName != null && defGeomName.length() > 0) {
			env = this.helper.getFullEnvelopeOfField(this.getJDBCParameters(),
					defGeomName, this.getJDBCParameters().getWorkingArea());

		}
		this.setDynValue("Envelope", env);

	}

	public void closeDone() throws DataException {
		clearMetadata();

	}

	public void opendDone() throws DataException {
		// Nothing to do
	}

	public Envelope getEnvelope() throws DataException {
		this.open();
		return (Envelope) this.getDynValue("Envelope");
	}

	public void resourceChanged(ResourceProvider resource) {
		this.getStoreServices().notifyChange(
				DataStoreNotification.RESOURCE_CHANGED,
				resource);
	}

	protected void clearMetadata() {
		this.setDynValue("DefaultSRS", null);
		this.setDynValue("Envelope", null);
	}

	public boolean allowAutomaticValues() {
		return this.helper.allowAutomaticValues();
	}

	public DataServerExplorer getExplorer() throws ReadException {
		DataManager manager = DALLocator.getDataManager();
		JDBCServerExplorerParameters exParams;
		JDBCStoreParameters params = getJDBCParameters();
		try {
			exParams = (JDBCServerExplorerParameters) manager
					.createServerExplorerParameters(JDBCServerExplorer.NAME);
			exParams.setHost(params.getHost());
			exParams.setPort(params.getPort());
			exParams.setDBName(params.getDBName());
			exParams.setUser(params.getUser());
			exParams.setPassword(params.getPassword());
			exParams.setUrl(params.getUrl());
			exParams.setCatalog(params.getCatalog());
			exParams.setSchema(params.getSchema());
			exParams.setJDBCDriverClassName(params.getJDBCDriverClassName());

			return manager.createServerExplorer(exParams);
		} catch (DataException e) {
			throw new ReadException(this.getName(), e);
		} catch (ValidateDataParametersException e) {
			// TODO Auto-generated catch block
			throw new ReadException(this.getName(), e);
		}
	}

	public void dispose() throws CloseException {
		this.close();
		resulsetList = null;
		this.helper.dispose();
		super.dispose();
	}

	public Object createNewOID() {
		return null;
	}

	public String compoundSelect(FeatureType type, String filter, String order,
			long limit, long offset) throws DataException {
		StringBuilder sql = new StringBuilder();
		JDBCStoreParameters params = getJDBCParameters();
		if (directSQLMode) {
			if (filter != null || order != null) {
				// FIXME Exception
				throw new UnsupportedOperationException();
			}
			sql.append(params.getSQL());
			sql.append(' ');
		} else {
			FeatureAttributeDescriptor[] fields = type
					.getAttributeDescriptors();

			// Select
			sql.append("Select ");
			for (int i = 0; i < fields.length - 1; i++) {
				sql.append(helper.getSqlFieldName(fields[i]));
				sql.append(", ");
			}
			sql.append(helper.getSqlFieldName(fields[fields.length - 1]));
			sql.append(' ');

			FeatureAttributeDescriptor[] pkFields = getStoreServices()
					.getProviderFeatureType(type.getId()).getPrimaryKey();

			if (pkFields != null && pkFields.length > 0) {
				// checks for pk fields are in select
				boolean toAdd;
				for (int i = 0; i < pkFields.length; i++) {
					toAdd = true;
					for (int j = 0; j < fields.length; j++) {
						if (pkFields[i].getName().equals(fields[j].getName())) {
							toAdd = false;
							break;
						}
						if (toAdd) {
							sql.append(", ");
							sql.append(helper.getSqlFieldName(pkFields[i]));
						}
					}
				}
				sql.append(' ');
			}

			// table
			sql.append("from ");
			sql.append(params.tableID());
			sql.append(' ');

			// Where
			appendWhere(sql, filter);

			// Order
			if ((params.getInitialOrder() != null && params.getInitialOrder()
					.length() != 0)
					|| (order != null && order.length() != 0)) {
				sql.append("order by ");

				if (order != null && order.length() != 0) {
					// order
					sql.append(order);
				} else {
					// initial order
					sql.append(params.getInitialOrder());
				}
				sql.append(' ');
			}
		}
		// limit offset
		if (limit >= 1 || offset >= 1) {
			sql.append(helper.compoundLimitAndOffset(limit,offset));
		}
		return sql.toString();
	}

	public long getFeatureCount() throws DataException {
		return getCount(null);
	}

	public String getName() {
		return NAME;
	}

	public boolean hasGeometrySupport() {
		return false;
	}

	public FeatureSetProvider createSet(FeatureQuery query,
			FeatureType featureType) throws DataException {

		return new JDBCSetProvider(this, query, featureType);
	}

	public Object getSourceId() {
		return this.getJDBCParameters().getSourceId();
	}

}
