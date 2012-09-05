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
package org.gvsig.fmap.dal.raster.impl;

import java.util.Iterator;
import java.util.Set;

import org.gvsig.fmap.dal.DataQuery;
import org.gvsig.fmap.dal.DataServerExplorer;
import org.gvsig.fmap.dal.DataSet;
import org.gvsig.fmap.dal.DataStore;
import org.gvsig.fmap.dal.DataStoreNotification;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.exception.CloseException;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.OpenException;
import org.gvsig.fmap.dal.exception.ValidateDataParametersException;
import org.gvsig.fmap.dal.feature.FeatureStoreNotification;
import org.gvsig.fmap.dal.feature.impl.DefaultFeatureStore;
import org.gvsig.fmap.dal.feature.impl.DefaultFeatureStoreNotification;
import org.gvsig.fmap.dal.impl.DataStoreImplementation;
import org.gvsig.fmap.dal.impl.DefaultDataManager;
import org.gvsig.fmap.dal.raster.CoverageSelection;
import org.gvsig.fmap.dal.raster.CoverageStore;
import org.gvsig.fmap.dal.raster.spi.CoverageStoreProvider;
import org.gvsig.fmap.dal.raster.spi.CoverageStoreProviderServices;
import org.gvsig.fmap.dal.resource.Resource;
import org.gvsig.fmap.dal.spi.DataStoreProvider;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DelegatedDynObject;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynObject;
import org.gvsig.tools.dynobject.DynObjectManager;
import org.gvsig.tools.dynobject.exception.DynFieldNotFoundException;
import org.gvsig.tools.dynobject.exception.DynMethodException;
import org.gvsig.tools.observer.Observer;
import org.gvsig.tools.observer.impl.DelegateWeakReferencingObservable;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.PersistentState;
import org.gvsig.tools.undo.command.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jmvivo
 *
 */
public class DefaultCoverageStore implements CoverageStore,
		CoverageStoreProviderServices, DataStoreImplementation {



	final static private Logger logger = LoggerFactory
			.getLogger(DefaultFeatureStore.class);

	private DataStoreParameters parameters = null;
	private CoverageSelection selection;

	private long versionOfUpdate = 0;

	private DefaultDataManager dataManager = null;

	private CoverageStoreProvider provider = null;

	private DelegatedDynObject dynObject;

	private DelegateWeakReferencingObservable delegateObservable = new DelegateWeakReferencingObservable(
			this);

	public DefaultCoverageStore() {

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.gvsig.fmap.dal.impl.DataStoreImplementation#intializePhase1(org.gvsig
	 * .fmap.dal.impl.DefaultDataManager,
	 * org.gvsig.fmap.dal.DataStoreParameters)
	 */
	public void intializePhase1(DefaultDataManager dataManager,
			DataStoreParameters parameters) throws InitializeException {
		DynObjectManager dynManager = ToolsLocator.getDynObjectManager();

		this.dynObject = (DelegatedDynObject) dynManager
				.createDynObject(dynManager.get(DataStore.DYNCLASS_NAME));
		this.dataManager = dataManager;
		this.parameters = parameters;

	}



	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.gvsig.fmap.dal.impl.DataStoreImplementation#intializePhase2(org.gvsig
	 * .fmap.dal.spi.DataStoreProvider)
	 */
	public void intializePhase2(DataStoreProvider provider)
			throws InitializeException {

		this.provider = (CoverageStoreProvider) provider;
		this.delegate(provider);

	}


	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.DataStore#createSelection()
	 */
	public DataSet createSelection() throws DataException {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.DataStore#getChildren()
	 */
	public Iterator getChildren() {
		return this.provider.getChilds();
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.DataStore#getDataSet()
	 */
	public DataSet getDataSet() throws DataException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.DataStore#getDataSet(org.gvsig.fmap.dal.DataQuery)
	 */
	public DataSet getDataSet(DataQuery dataQuery) throws DataException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.DataStore#getDataSet(org.gvsig.tools.observer.Observer)
	 */
	public void getDataSet(Observer observer) throws DataException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.DataStore#getDataSet(org.gvsig.fmap.dal.DataQuery, org.gvsig.tools.observer.Observer)
	 */
	public void getDataSet(DataQuery dataQuery, Observer observer)
			throws DataException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.DataStore#getExplorer()
	 */
	public DataServerExplorer getExplorer() throws DataException,
			ValidateDataParametersException {
		return provider.getExplorer();
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.DataStore#getName()
	 */
	public String getName() {
		return provider.getName();
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.DataStore#getParameters()
	 */
	public DataStoreParameters getParameters() {
		return this.parameters;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.DataStore#getSelection()
	 */
	public DataSet getSelection() throws DataException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.DataStore#refresh()
	 */
	public void refresh() throws DataException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.DataStore#setSelection(org.gvsig.fmap.dal.DataSet)
	 */
	public void setSelection(DataSet selection) throws DataException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.gvsig.tools.observer.ComplexObservable#beginComplexNotification()
	 */
	public void beginComplexNotification() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.gvsig.tools.observer.ComplexObservable#disableNotifications()
	 */
	public void disableNotifications() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.gvsig.tools.observer.ComplexObservable#enableNotifications()
	 */
	public void enableNotifications() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.gvsig.tools.observer.ComplexObservable#endComplexNotification()
	 */
	public void endComplexNotification() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.gvsig.tools.observer.Observable#addObserver(org.gvsig.tools.observer.Observer)
	 */
	public void addObserver(Observer o) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.gvsig.tools.observer.Observable#deleteObserver(org.gvsig.tools.observer.Observer)
	 */
	public void deleteObserver(Observer o) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.gvsig.tools.observer.Observable#deleteObservers()
	 */
	public void deleteObservers() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.gvsig.tools.persistence.Persistent#getState()
	 */
	public PersistentState getState() throws PersistenceException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.tools.persistence.Persistent#loadState(org.gvsig.tools.persistence.PersistentState)
	 */
	public void saveToState(PersistentState state) throws PersistenceException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.gvsig.tools.persistence.Persistent#setState(org.gvsig.tools.persistence.PersistentState)
	 */
	public void loadFromState(PersistentState state) throws PersistenceException {
		// TODO Auto-generated method stub

	}

	//
	// ====================================================================
	// Metadata related methods
	//

	public Object getMetadataID() {
		return this.provider.getSourceId();
	}

	public void delegate(DynObject dynObject) {
		this.dynObject.delegate(dynObject);
	}

	public DynClass getDynClass() {
		return this.dynObject.getDynClass();
	}

	public Object getDynValue(String name) throws DynFieldNotFoundException {
		return this.dynObject.getDynValue(name);
	}

	public boolean hasDynValue(String name) {
		return this.dynObject.hasDynValue(name);
	}

	public void implement(DynClass dynClass) {
		this.dynObject.implement(dynClass);
	}

	public Object invokeDynMethod(String name, DynObject context)
			throws DynMethodException {
		return this.dynObject.invokeDynMethod(this, name, context);
	}

	public Object invokeDynMethod(int code, DynObject context)
			throws DynMethodException {
		return this.dynObject.invokeDynMethod(this, code, context);
	}

	public void setDynValue(String name, Object value)
			throws DynFieldNotFoundException {
		this.setDynValue(name, value);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.gvsig.metadata.Metadata#getMetadataChildren()
	 */
	public Set getMetadataChildren() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.gvsig.metadata.Metadata#getMetadataName()
	 */
	public String getMetadataName() {
		return this.provider.getName();
	}


	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.raster.spi.CoverageStoreProviderServices#createDefaultCoverageSelection()
	 */
	public CoverageSelection createDefaultCoverageSelection()
			throws DataException {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.raster.spi.CoverageStoreProviderServices#getManager()
	 */
	public DefaultDataManager getManager() {
		return this.dataManager;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.raster.spi.CoverageStoreProviderServices#getProvider()
	 */
	public CoverageStoreProvider getProvider() {
		return this.provider;
	}

	public void notifyChange(String notification) {
		delegateObservable
				.notifyObservers(new DefaultCoverageStoreNotification(
				this, notification));

	}


	public void notifyChange(String notification, Command command) {
		delegateObservable
				.notifyObservers(new DefaultCoverageStoreNotification(
				this, notification, command));
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.gvsig.fmap.dal.feature.spi.FeatureStoreProviderServices#notifyChange
	 * (java.lang.String, org.gvsig.fmap.dal.resource.Resource)
	 */
	public void notifyChange(String notification, Resource resource) {
		delegateObservable.notifyObservers(new DefaultFeatureStoreNotification(
				this, FeatureStoreNotification.RESOURCE_CHANGED));
	}

	public void open() throws OpenException {
		// TODO: Se puede hacer un open estando en edicion ?
		this.notifyChange(DataStoreNotification.BEFORE_OPEN);
		this.provider.open();
		this.notifyChange(DataStoreNotification.AFTER_OPEN);
	}

	public void close() throws CloseException {
		// TODO: Se puede hacer un close estando en edicion ?
		this.notifyChange(DataStoreNotification.BEFORE_CLOSE);
		this.provider.close();
		this.notifyChange(DataStoreNotification.AFTER_CLOSE);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.gvsig.fmap.dal.DataStore#dispose()
	 */
	public void dispose() throws CloseException {
		this.notifyChange(DataStoreNotification.BEFORE_DISPOSE);
		this.provider.dispose();
		if (this.selection != null) {
			this.selection.dispose();
			this.selection = null;
		}

		this.parameters = null;
		this.notifyChange(DataStoreNotification.AFTER_DISPOSE);
		this.delegateObservable.deleteObservers();
		this.delegateObservable = null;
	}

	public boolean allowWrite() {
		return this.provider.allowWrite();
	}

	public DataQuery createQuery() {
		// TODO Auto-generated method stub
		return null;
	}

	public DataStore getStore() {
		return this;
	}

	public CoverageStore getCoverageStore() {
		return this;
	}

}
