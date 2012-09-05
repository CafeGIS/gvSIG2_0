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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.exception.PerformEditingException;
import org.gvsig.fmap.dal.feature.spi.FeatureProvider;
import org.gvsig.fmap.dal.feature.spi.FeatureReferenceProviderServices;
import org.gvsig.fmap.dal.spi.DataStoreProviderServices;
import org.gvsig.fmap.dal.store.jdbc.exception.JDBCExecutePreparedSQLException;
import org.gvsig.fmap.dal.store.jdbc.exception.JDBCExecuteSQLException;
import org.gvsig.fmap.dal.store.jdbc.exception.JDBCPreparingSQLException;
import org.gvsig.fmap.dal.store.jdbc.exception.JDBCSQLException;
import org.gvsig.fmap.dal.store.jdbc.exception.JDBCUpdateWithoutChangesException;
import org.gvsig.tools.dynobject.DynObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jmvivo
 *
 */
public class JDBCStoreProviderWriter extends JDBCStoreProvider {

	final static private Logger logger = LoggerFactory
			.getLogger(JDBCStoreProviderWriter.class);


	protected String appendModeSql;
	protected List appendModeAttributes;


	public JDBCStoreProviderWriter(JDBCStoreParameters params,
			DataStoreProviderServices storeServices)
			throws InitializeException {
		super(params, storeServices);
	}

	protected JDBCStoreProviderWriter(JDBCStoreParameters params,
			DataStoreProviderServices storeServices, DynObject metadata)
			throws InitializeException {
		super(params, storeServices, metadata);
	}



	protected void addToListFeatureValues(FeatureProvider featureProvider,
			FeatureAttributeDescriptor attrOfList,
			FeatureAttributeDescriptor attr, List values) throws DataException {
		if (attr == null) {
			if (attrOfList.isPrimaryKey()) {
				// FIXME excepton
				throw new RuntimeException("pk attribute '"
						+ attrOfList.getName() + "' not found in feature");
			}
			values.add(helper
					.dalValueToJDBC(attr, attrOfList.getDefaultValue()));
		} else {
			values.add(helper.dalValueToJDBC(attr, featureProvider.get(attr
					.getIndex())));
		}

	}

	protected void addToListFeatureValues(FeatureProvider featureProvider,
			List attributes, List values) throws DataException {
		FeatureAttributeDescriptor attr, attrOfList;
		FeatureType fType = featureProvider.getType();
		for (int i = 0; i < attributes.size(); i++) {
			attrOfList = (FeatureAttributeDescriptor) attributes.get(i);
			attr = fType.getAttributeDescriptor(attrOfList.getName());
			addToListFeatureValues(featureProvider, attrOfList, attr, values);
		}
	}

	protected void appendToSQLPreparedPkWhereClause(StringBuilder sql,
			List pkAttributes) {
		sql.append(" Where ");
		FeatureAttributeDescriptor attr;
		for (int i = 0; i < pkAttributes.size() - 1; i++) {
			attr = (FeatureAttributeDescriptor) pkAttributes.get(i);
			sql.append(helper.escapeFieldName(attr.getName()));
			sql.append(" = ? AND ");
		}
		attr = (FeatureAttributeDescriptor) pkAttributes.get(pkAttributes
				.size() - 1);
		sql.append(helper.escapeFieldName(attr.getName()));
		sql.append(" = ? ");
	}

	protected void executeRemovePreparedStatement(Connection conn, String sql,
			List attributes, Iterator featureReferences) throws DataException {
				PreparedStatement st;
				try {
					st = conn.prepareStatement(sql);
				} catch (SQLException e) {
					throw new JDBCPreparingSQLException(sql, e);
				}
				try {
					List values = new ArrayList();
					FeatureReferenceProviderServices featureRef;
					FeatureType featureType;
					while (featureReferences.hasNext()) {
						st.clearParameters();
						featureRef = (FeatureReferenceProviderServices) featureReferences
								.next();
						values.clear();
						featureType = this.getFeatureStore().getFeatureType(
						featureRef
								.getFeatureTypeId());

						Iterator iter = attributes.iterator();
						FeatureAttributeDescriptor attr;
						while (iter.hasNext()) {
							attr = (FeatureAttributeDescriptor) iter.next();
							values.add(helper.dalValueToJDBC(attr, featureRef
									.getKeyValue(attr.getName())));
						}

						for (int i = 0; i < values.size(); i++) {
							st.setObject(i + 1, values.get(i));
						}
						try {
							int nAffected =st.executeUpdate();
							if (nAffected == 0) {
								throw new JDBCUpdateWithoutChangesException(sql, values);
							}
							if (nAffected > 1){
								logger.warn("Remove statement affectst to {} rows: {}",
										nAffected, sql);
							}

						} catch (SQLException e) {
							throw new JDBCExecutePreparedSQLException(sql, values, e);
						}

					}
				} catch (SQLException e) {
					throw new JDBCSQLException(e);
				} finally {
					try {st.close();} catch (SQLException e) {	};
				}

			}

	protected void executeUpdatePreparedStatement(Connection conn, String sql,
			List attributes, Iterator featureProviders) throws DataException {
				PreparedStatement st;
				try {
					st = conn.prepareStatement(sql);
				} catch (SQLException e) {
					throw new JDBCPreparingSQLException(sql, e);
				}
				try {
					List values = new ArrayList();
					FeatureProvider featureProvider;
					while (featureProviders.hasNext()) {
						st.clearParameters();
						featureProvider = (FeatureProvider) featureProviders.next();
						values.clear();
						addToListFeatureValues(featureProvider, attributes, values);
						for (int i = 0; i < values.size(); i++) {
							st.setObject(i + 1, values.get(i));
						}
						try {
							if (st.executeUpdate() == 0) {
								throw new JDBCUpdateWithoutChangesException(sql, values);
							}
						} catch (SQLException e) {
							throw new JDBCExecutePreparedSQLException(sql, values, e);
						}

					}
				} catch (SQLException e) {
					throw new JDBCSQLException(e);
				} finally {
					try {st.close();} catch (SQLException e) {	};
				}

			}

	protected void performDeletes(Connection conn, Iterator deleteds, List pkAttributes)
			throws DataException {

				if (pkAttributes.size() < 0) {
					// FIXME Exception
					throw new RuntimeException("Operation requires missing pk");
				}

				// ************ Prepare SQL ****************
				StringBuilder sqlb = new StringBuilder();
				sqlb.append("Delete from ");
				sqlb.append(getJDBCParameters().tableID());
				appendToSQLPreparedPkWhereClause(sqlb, pkAttributes);
				String sql = sqlb.toString();
				// ************ Prepare SQL (end) ****************

				executeRemovePreparedStatement(conn, sql, pkAttributes, deleteds);
			}

	protected String getSqlStatementAddField(FeatureAttributeDescriptor attr,
			List additionalStatement) throws DataException {
		StringBuilder strb = new StringBuilder();
		strb.append("ADD ");
		strb.append(this.helper.getSqlFieldDescription(attr));
		return strb.toString();
	}

	protected String getSqlStatementDropField(FeatureAttributeDescriptor attr,List additionalStatement) {
		// DROP [ COLUMN ] column
		return " DROP COLUMN "
				+ this.helper.escapeFieldName(attr.getName());

	}

	public boolean supportsAppendMode() {
		return true;
	}

	public void endAppend() throws DataException {
		this.loadMetadata();
		appendModeSql = null;
		appendModeAttributes = null;
	}

	protected List getSqlStatementAlterField(
			FeatureAttributeDescriptor attrOrg,
			FeatureAttributeDescriptor attrTrg, List additionalStatement)
			throws DataException {
		//
		List actions = new ArrayList();
		StringBuilder strb;
		if (attrOrg.getDataType() != attrTrg.getDataType()) {
			// ALTER COLUMN {col} TYPE {type} character varying(35)
			strb = new StringBuilder();
			strb.append("ALTER COLUMN ");
			strb.append(helper.escapeFieldName(attrTrg.getName()));
			strb.append(" ");
			strb.append(helper.getSqlColumnTypeDescription(attrTrg));

			actions.add(strb.toString());
		}

		if (attrOrg.allowNull() != attrTrg.allowNull()) {
			// ALTER [ COLUMN ] column { SET | DROP } NOT NULL

			strb = new StringBuilder();
			strb.append("ALTER COLUMN ");
			strb.append(helper.escapeFieldName(attrTrg.getName()));
			strb.append(' ');
			if (attrTrg.allowNull()) {
				strb.append("SET ");
			} else {
				strb.append("DROP ");
			}
			strb.append("NOT NULL");
			actions.add(strb.toString());
		}

		if (attrOrg.getDefaultValue() != attrTrg.getDefaultValue()) {
			if (attrTrg.getDefaultValue() == null) {
				// ALTER [ COLUMN ] column DROP DEFAULT

				strb = new StringBuilder();
				strb.append("ALTER COLUMN ");
				strb.append(helper.escapeFieldName(attrTrg.getName()));
				strb.append(" DROP DEFAULT");
				actions.add(strb.toString());
			} else if (!attrTrg.getDefaultValue().equals(
					attrOrg.getDefaultValue())) {
				// ALTER [ COLUMN ] column DROP DEFAULT

				strb = new StringBuilder();
				strb.append("ALTER COLUMN ");
				strb.append(helper.escapeFieldName(attrTrg.getName()));
				strb.append(" SET DEFAULT");
				strb.append(helper.dalValueToJDBC(attrTrg, attrTrg
						.getDefaultValue()));
				actions.add(strb.toString());
			}
		}

		return actions;
	}

	protected void performUpdateTable(Connection conn, FeatureType original,
			FeatureType target) throws DataException {

		/*
		 *
		 * ALTER TABLE [ ONLY ] name [ * ] action [, ... ]
		 */

		List toDrop = new ArrayList();
		List toAdd = new ArrayList();
		List toAlter = new ArrayList();

		List additionalStatement = new ArrayList();

		FeatureAttributeDescriptor attrOrg;
		FeatureAttributeDescriptor attrTrg;
		Iterator attrs = original.iterator();
		while (attrs.hasNext()) {
			attrOrg = (FeatureAttributeDescriptor) attrs.next();
			attrTrg = target.getAttributeDescriptor(attrOrg.getName());
			if (attrTrg == null) {
				toDrop.add(getSqlStatementDropField(attrOrg,
						additionalStatement));
			} else {
				toAlter.addAll(getSqlStatementAlterField(attrOrg, attrTrg,
						additionalStatement));
			}

		}
		attrs = target.iterator();
		while (attrs.hasNext()) {
			attrTrg = (FeatureAttributeDescriptor) attrs.next();
			if (original.getAttributeDescriptor(attrTrg.getName()) == null) {
				toAdd
						.add(getSqlStatementAddField(attrTrg,
								additionalStatement));
			}
		}

		StringBuilder sqlb = new StringBuilder();

		sqlb.append("ALTER TABLE ");
		sqlb.append(getJDBCParameters().tableID());
		sqlb.append(' ');

		List actions = new ArrayList();
		actions.addAll(toDrop);
		actions.addAll(toAlter);
		actions.addAll(toAdd);

		Iterator it = actions.iterator();
		while (it.hasNext()) {
			if (it.next() == null) {
				it.remove();
			}
		}

		it = additionalStatement.iterator();
		while (it.hasNext()) {
			if (it.next() == null) {
				it.remove();
			}
		}

		if (actions.size() < 1) {
			return;
		}

		helper.stringJoin(actions, ", ", sqlb);

		String sql = sqlb.toString();

		Statement st = null;

		try {
			st = conn.createStatement();
		} catch (SQLException e1) {
			throw new JDBCSQLException(e1);
		}
		try {
			st.execute(sql);
			Iterator iter = additionalStatement.iterator();
			while (iter.hasNext()) {
				sql = (String) iter.next();
				st.execute(sql);
			}
		} catch (SQLException e1) {
			throw new JDBCExecuteSQLException(sql, e1);
		} finally {
			try {
				st.close();
			} catch (Exception e) {
				logger.error("Exception closing statement", e);
			}
			;
		}

	}


	private void perfomInsert(Connection conn, PreparedStatement insertSt,
			String sql, FeatureProvider feature, List attributes)
			throws DataException {

		try {
			insertSt.clearParameters();
			List values = new ArrayList();
			addToListFeatureValues(feature, attributes, values);
			FeatureAttributeDescriptor attr;
			int j = 1;
			for (int i = 0; i < values.size(); i++) {
				insertSt.setObject(j, values.get(i));
				j++;
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Executing insert. sql={} value={}", new Object[] {
						sql, values });
			}
			try {
				insertSt.execute();
			} catch (SQLException e) {
				throw new JDBCExecutePreparedSQLException(sql, values, e);
			}

		} catch (SQLException e1) {
			throw new JDBCSQLException(e1);
		}
	}

	public void append(final FeatureProvider featureProvider) throws DataException {
		TransactionalAction action = new TransactionalAction() {
			public Object action(Connection conn) throws DataException {

				PreparedStatement st;
				try {
					st = conn.prepareStatement(appendModeSql);
				} catch (SQLException e) {
					throw new JDBCPreparingSQLException(appendModeSql, e);
				}
				try {
					perfomInsert(conn, st, appendModeSql, featureProvider,
							appendModeAttributes);
				} finally {
					try {
						st.close();
					} catch (SQLException e) {
					}
					;
				}
				return null;
			}

			public boolean continueTransactionAllowed() {
				return false;
			}
		};
		try {
			this.helper.doConnectionAction(action);

			resetCount();

		} catch (Exception e) {
			throw new PerformEditingException(this.getName(), e);
		}
	}

	protected void prepareAttributeForUpdate(FeatureAttributeDescriptor attr,
			List values) {
		values.add(helper.escapeFieldName(attr.getName()) + " = ?");
	}

	protected void prepareAttributeForInsert(FeatureAttributeDescriptor attr,
			List fields, List values) {

		fields.add(helper.escapeFieldName(attr.getName()));
		values.add("?");

	}


	protected void prepareSQLAndAttributeListForInsert(StringBuilder sqlb,
			List attributes) throws DataException {
		/*
		 * INSERT INTO table [ ( column [, ...] ) ] { DEFAULT VALUES | VALUES (
		 * { expression | DEFAULT } [, ...] ) [, ...] | query } [ RETURNING * |
		 * output_expression [ AS output_name ] [, ...] ]
		 */

		sqlb.append("INSERT INTO ");
		sqlb.append(getJDBCParameters().tableID());

		sqlb.append(" (");

		FeatureType type = this.getFeatureStore().getDefaultFeatureType();

		List fields = new ArrayList();
		List values = new ArrayList();

		Iterator iter = type.iterator();
		FeatureAttributeDescriptor attr;
		while (iter.hasNext()) {
			attr = (FeatureAttributeDescriptor) iter.next();
			if (attr.isAutomatic() || attr.isReadOnly()) {
				continue;
			}
			attributes.add(attr);
			prepareAttributeForInsert(attr, fields, values);

		}
		if (attributes.size() < 1) {
			// FIXME exception
			throw new RuntimeException("no fields to set");
		}

		helper.stringJoin(fields, ", ", sqlb);

		sqlb.append(") VALUES (");
		helper.stringJoin(values, ", ", sqlb);

		sqlb.append(") ");

	}


	protected void performInserts(Connection conn, Iterator inserteds)
			throws DataException {

		StringBuilder sqlb = new StringBuilder();
		List attrs = new ArrayList();

		prepareSQLAndAttributeListForInsert(sqlb, attrs);

		String sql = sqlb.toString();
		PreparedStatement st;
		try {
			st = conn.prepareStatement(sql);
		} catch (SQLException e) {
			throw new JDBCPreparingSQLException(sql, e);
		}
		try {
			while (inserteds.hasNext()) {
				perfomInsert(conn, st, sql, (FeatureProvider) inserteds.next(),
						attrs);
			}
		} finally {
			try {st.close();} catch (SQLException e) {logger.error("Error closing statement", e);};
		}
	}

	protected void performUpdates(Connection conn, Iterator updateds,
			List pkAttributes) throws DataException {
		/*
		 * UPDATE [ ONLY ] table [ [ AS ] alias ] SET { column = { expression |
		 * DEFAULT } | ( column [, ...] ) = ( { expression | DEFAULT } [, ...] )
		 * } [, ...] [ FROM fromlist ] [ WHERE condition ] [ RETURNING * |
		 * output_expression [ AS output_name ] [, ...] ]
		 */

		if (pkAttributes.size() < 0) {
			// FIXME Exception
			throw new RuntimeException("Operation requires missing pk");
		}

		// ************ Prepare SQL ****************

		StringBuilder sqlb = new StringBuilder();
		sqlb.append("UPDATE ");
		sqlb.append(getJDBCParameters().tableID());

		sqlb.append(" SET ");

		List values = new ArrayList();

		FeatureType type = this.getFeatureStore().getDefaultFeatureType();

		Iterator iter = type.iterator();
		FeatureAttributeDescriptor attr;
		List updateAttrs = new ArrayList();
		while (iter.hasNext()) {
			attr = (FeatureAttributeDescriptor) iter.next();
			if (attr.isPrimaryKey() || attr.isAutomatic() || attr.isReadOnly()) {
				continue;
			}
			updateAttrs.add(attr);
			prepareAttributeForUpdate(attr, values);

		}
		if (updateAttrs.size() < 1) {
			// FIXME exception
			throw new RuntimeException("no fields to set");
		}

		helper.stringJoin(values, ", ", sqlb);

		sqlb.append(' ');
		appendToSQLPreparedPkWhereClause(sqlb, pkAttributes);

		String sql = sqlb.toString();
		// ************ Prepare SQL (end) ****************

		updateAttrs.addAll(pkAttributes);

		executeUpdatePreparedStatement(conn, sql, updateAttrs, updateds);
	}


	public void beginAppend() throws DataException {
		StringBuilder sqlb = new StringBuilder();
		List attrs = new ArrayList();

		prepareSQLAndAttributeListForInsert(sqlb, attrs);

		appendModeSql = sqlb.toString();
		appendModeAttributes = attrs;
	}


	protected TransactionalAction getPerformChangesAction(
			final Iterator deleteds, final Iterator inserteds,
			final Iterator updateds, final Iterator featureTypesChanged) {

		TransactionalAction action = new TransactionalAction() {

			public Object action(Connection conn) throws DataException {

				if (featureTypesChanged.hasNext()) {

					FeatureTypeChanged item = (FeatureTypeChanged) featureTypesChanged
							.next();
					performUpdateTable(conn, item.getSource(), item.getTarget());
				}

				List pkAttributes = null;
				if (deleteds.hasNext() || updateds.hasNext()) {
					pkAttributes = Arrays.asList(getFeatureStore()
							.getDefaultFeatureType()
							.getPrimaryKey());
				}

				if (deleteds.hasNext()) {
					performDeletes(conn, deleteds, pkAttributes);
				}

				if (updateds.hasNext()) {
					performUpdates(conn, updateds, pkAttributes);
				}

				if (inserteds.hasNext()) {
					performInserts(conn, inserteds);
				}

				return null;
			}

			public boolean continueTransactionAllowed() {
				return false;
			}

		};

		return action;

	}

	public void performChanges(Iterator deleteds, Iterator inserteds,
			Iterator updateds, Iterator featureTypesChanged)
			throws PerformEditingException {

		boolean countChanged = deleteds.hasNext() || inserteds.hasNext();

		try {
			this.helper.doConnectionAction(getPerformChangesAction(deleteds,
					inserteds, updateds, featureTypesChanged));

			this.initFeatureType();
			if (countChanged) {
				resetCount();
			}

		} catch (Exception e) {
			throw new PerformEditingException(this.getName(), e);
		}
	}


	public boolean allowWrite() {
		if (directSQLMode) {
			return false;
		}
		if (getJDBCParameters().getPkFields() == null
				|| getJDBCParameters().getPkFields().length > 0) {
			FeatureType ft = null;
			try {
				ft = this.getFeatureStore().getDefaultFeatureType();
			} catch (DataException e) {
				logger.error("Excepton get default Feature Type", e);
			}

			if (ft == null) {
				return false;
			}
			FeatureAttributeDescriptor attr;
			Iterator iter = ft.iterator();
			while (iter.hasNext()) {
				attr = (FeatureAttributeDescriptor) iter.next();
				if (attr.isPrimaryKey()) {
					return true;
				}
			}
			return false;

		} else {
			return true;
		}
	}
}
