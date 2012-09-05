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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureQueryOrder;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.FeatureQueryOrder.FeatureQueryOrderMember;
import org.gvsig.fmap.dal.feature.spi.FeatureSetProvider;
import org.gvsig.tools.evaluator.Evaluator;
import org.gvsig.tools.evaluator.EvaluatorFieldValue;
import org.gvsig.tools.evaluator.EvaluatorFieldsInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jmvivo
 *
 */
public class JDBCSetProvider implements FeatureSetProvider {

	final static private Logger logger = LoggerFactory
			.getLogger(JDBCSetProvider.class);



	protected JDBCStoreProvider store;
	protected FeatureQuery query;
	protected FeatureType featureType;
	protected String filter;
	protected String order;
	protected Long size = null;
	protected Boolean isEmpty = null;

	protected List resultSetIDReferenced;

	private JDBCHelper helper = null;

	public JDBCSetProvider(JDBCStoreProvider store, FeatureQuery query,
			FeatureType featureType) throws DataException {
		this.store = store;
		this.query = query;
		this.featureType = featureType;
		this.helper = store.getHelper();
		this.resultSetIDReferenced = new ArrayList();

		if (query.hasFilter() && this.canFilter()) {
			setFilter(query.getFilter());
		} else {
			setFilter(null);
		}

		if (query.hasOrder() && canOrder()) {
			setOrder(query.getOrder());
		} else {
			setOrder(null);
		}
	}

	protected String getSqlForEvaluator(Evaluator filter) {
		if (filter == null) {
			return null;
		}
		EvaluatorFieldsInfo info = filter.getFieldsInfo();
		String filterString = filter.getCQL();
		if (info == null) {
			return filterString;
		}
		String[] filterNames = info.getFieldNames();
		String[] finalNames = new String[filterNames.length];
		EvaluatorFieldValue[] fValues;

		List values = new ArrayList();

		FeatureAttributeDescriptor attr;
		for (int i = 0; i < filterNames.length; i++) {
			attr = featureType.getAttributeDescriptor(filterNames[i]);
			if (attr == null) {
				finalNames[i] = filterNames[i];
				continue;
			}
			finalNames[i] = getEscapedFieldName(attr.getName());

		}

		for (int i = 0; i < filterNames.length; i++) {
			if (!filterNames[i].equals(finalNames[i])) {
				filterString.replaceAll("\\b" + filterNames[i] + "\\b",
						finalNames[i]);
			}
		}

		return filterString;
	}


	protected String getEscapedFieldName(String fieldName) {
		if (helper == null) {
			helper = (store).getHelper();
		}
		return helper.escapeFieldName(fieldName);
	}


	protected void setOrder(FeatureQueryOrder order) {
		if (order == null || order.size() == 0) {
			this.order = null;
			return;
		}

		StringBuilder buffer = new StringBuilder();
		Iterator iter = order.iterator();
		FeatureQueryOrderMember menber;
		while (true) {
			menber = (FeatureQueryOrderMember) iter.next();
			if (menber.hasEvaluator()) {
				buffer.append(getSqlForEvaluator(menber.getEvaluator()));
			} else {
				buffer.append(getEscapedFieldName(menber.getAttributeName()));
			}
			if (menber.getAscending()) {
				buffer.append(" ASC");
			} else {
				buffer.append(" DESC");
			}
			if (iter.hasNext()) {
				buffer.append(", ");
			} else {
				buffer.append(' ');
				break;
			}
		}

		this.order = buffer.toString();
	}

	protected void setFilter(Evaluator filter) {
		this.filter = getSqlForEvaluator(filter);
	}



	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureSetProvider#canFilter()
	 */
	public boolean canFilter() {
		Evaluator filter = query.getFilter();
		if (filter != null) {
			if (filter.getCQL() == null || filter.getCQL().length() == 0) {
				return false;
			} else {
				// TODO Check Geom fields if
				EvaluatorFieldsInfo fInfo = filter.getFieldsInfo();
				if (fInfo == null || fInfo.getFieldNames() == null) {
					return true;
				}
				Iterator names = Arrays.asList(fInfo.getFieldNames())
						.iterator();
				String name;
				int type;
				while (names.hasNext()) {
					name = (String) names.next();
					type = this.featureType.getAttributeDescriptor(name)
							.getDataType();
					if (type == DataTypes.GEOMETRY
							&& !this.helper.hasGeometrySupport()) {
						return false;
					}



				}

				return true;
			}

		} else{
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureSetProvider#canIterateFromIndex()
	 */
	public boolean canIterateFromIndex() {
		return helper.supportOffset();
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureSetProvider#canOrder()
	 */
	public boolean canOrder() {
		// TODO Check Geom fields if postgis not are available
		if (query.hasOrder()) {
			Iterator iter = query.getOrder().iterator();
			FeatureQueryOrderMember menber;
			String cql;
			while (iter.hasNext()){
				menber = (FeatureQueryOrderMember) iter.next();
				if (menber.hasEvaluator()){
					cql =menber.getEvaluator().getCQL();
					if (cql == null || cql.length() == 0) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public void dispose() {
		if (resultSetIDReferenced != null) {
			Iterator iter = resultSetIDReferenced.iterator();
			Integer resID;
			while (iter.hasNext()) {
				resID = (Integer) iter.next();
				if (resID != null) {
					logger.warn(
						"ResultSet (ID {}) not closed on dispose, will close",
						resID);
					try {
						this.store.closeResulset(resID.intValue());
					} catch (DataException e) {
						logger.error("Close resulset Exception", e);
					}
				}
				iter.remove();
			}
		}
		resultSetIDReferenced = null;
		store = null;
		query = null;
		featureType = null;
		filter = null;
		order = null;
		size = null;
		isEmpty = null;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureSetProvider#fastIterator()
	 */
	public DisposableIterator fastIterator() throws DataException {
		return this.fastIterator(0);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureSetProvider#fastIterator(long)
	 */
	public DisposableIterator fastIterator(long index) throws DataException {
		if (isEmpty != null && isEmpty.booleanValue()) {
			return new EmptyJDBCIterator();
		}
		JDBCIterator iter = createFastIterartor(index);
		return iter;
	}

	protected String getSQL(long fromIndex) throws DataException {
		return store.compoundSelect(featureType, filter, order, 0, fromIndex);

	}

	protected JDBCIterator createFastIterartor(long index) throws DataException {
		int rsID = store.createResultSet(getSQL(index));
		return createDefaultFastIterartor(rsID);
	}

	protected JDBCIterator createIterator(long index) throws DataException {
		int rsID = store.createResultSet(getSQL(index));
		return createDefaultIterartor(rsID);
	}
	protected JDBCIterator createDefaultFastIterartor(int resultSetID)
			throws DataException {
		return new JDBCFastIterator(store, this, featureType, resultSetID);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureSetProvider#getSize()
	 */
	public long getSize() throws DataException {
		if (size == null) {
			size = new Long(store.getCount(filter));
		}
		return size.longValue();
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureSetProvider#isEmpty()
	 */
	public boolean isEmpty() throws DataException {
		if (isEmpty == null) {
			if (size == null) {
				String sql = store
						.compoundSelect(featureType, filter, null, 1,
						0);
				int rsID = store.createResultSet(sql);
				isEmpty = new Boolean(store.resulsetNext(rsID));
				store.closeResulset(rsID);
			} else {
				isEmpty = new Boolean(size.longValue() < 1);
			}
		}
		return isEmpty.booleanValue();
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureSetProvider#iterator()
	 */
	public DisposableIterator iterator() throws DataException {
		return iterator(0);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureSetProvider#iterator(long)
	 */
	public DisposableIterator iterator(long index) throws DataException {
		if (isEmpty != null && isEmpty.booleanValue()) {
			return new EmptyJDBCIterator();
		}

		JDBCIterator iter = createIterator(index);
		return iter;
	}


	protected JDBCIterator createDefaultIterartor(int resultSetID)
			throws DataException {
		return new JDBCIterator(store, this, featureType, resultSetID);
	}

	private class EmptyJDBCIterator extends JDBCIterator {

		protected EmptyJDBCIterator() throws DataException {
			super(null, null, null, -1);
		}

		public boolean hasNext() {
			return false;
		}

		public Object next() {
			throw new NoSuchElementException();
		}

		public void dispose() {

		}

	}

	public void addResulsetReference(int resulsetID) {
		this.resultSetIDReferenced.add(new Integer(resulsetID));
	}

	public void removeResulsetReference(int resulsetID) {
		this.resultSetIDReferenced.remove(new Integer(resulsetID));
	}


}
