package org.gvsig.fmap.dal.feature.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
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
import org.gvsig.fmap.dal.exception.ProviderNotRegisteredException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.exception.ValidateDataParametersException;
import org.gvsig.fmap.dal.feature.EditableFeature;
import org.gvsig.fmap.dal.feature.EditableFeatureType;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureIndex;
import org.gvsig.fmap.dal.feature.FeatureIndexes;
import org.gvsig.fmap.dal.feature.FeatureLocks;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureReference;
import org.gvsig.fmap.dal.feature.FeatureReferenceSelection;
import org.gvsig.fmap.dal.feature.FeatureSelection;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureStoreNotification;
import org.gvsig.fmap.dal.feature.FeatureStoreTransforms;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.NewFeatureStoreParameters;
import org.gvsig.fmap.dal.feature.exception.AlreadyEditingException;
import org.gvsig.fmap.dal.feature.exception.ConcurrentDataModificationException;
import org.gvsig.fmap.dal.feature.exception.CreateFeatureException;
import org.gvsig.fmap.dal.feature.exception.DataExportException;
import org.gvsig.fmap.dal.feature.exception.FeatureIndexException;
import org.gvsig.fmap.dal.feature.exception.FinishEditingException;
import org.gvsig.fmap.dal.feature.exception.GetFeatureTypeException;
import org.gvsig.fmap.dal.feature.exception.IllegalFeatureException;
import org.gvsig.fmap.dal.feature.exception.IllegalFeatureTypeException;
import org.gvsig.fmap.dal.feature.exception.NeedEditingModeException;
import org.gvsig.fmap.dal.feature.exception.NoNewFeatureInsertException;
import org.gvsig.fmap.dal.feature.exception.NullFeatureTypeException;
import org.gvsig.fmap.dal.feature.exception.SelectionNotAllowedException;
import org.gvsig.fmap.dal.feature.exception.StoreCancelEditingException;
import org.gvsig.fmap.dal.feature.exception.StoreDeleteEditableFeatureException;
import org.gvsig.fmap.dal.feature.exception.StoreDeleteFeatureException;
import org.gvsig.fmap.dal.feature.exception.StoreEditException;
import org.gvsig.fmap.dal.feature.exception.StoreInsertFeatureException;
import org.gvsig.fmap.dal.feature.exception.StoreUpdateFeatureException;
import org.gvsig.fmap.dal.feature.exception.StoreUpdateFeatureTypeException;
import org.gvsig.fmap.dal.feature.exception.ValidateFeaturesException;
import org.gvsig.fmap.dal.feature.exception.WriteNotAllowedException;
import org.gvsig.fmap.dal.feature.impl.expansionadapter.MemoryExpansionAdapter;
import org.gvsig.fmap.dal.feature.impl.featureset.DefaultFeatureSet;
import org.gvsig.fmap.dal.feature.impl.undo.DefaultFeatureCommandsStack;
import org.gvsig.fmap.dal.feature.impl.undo.FeatureCommandsStack;
import org.gvsig.fmap.dal.feature.spi.DefaultFeatureProvider;
import org.gvsig.fmap.dal.feature.spi.FeatureProvider;
import org.gvsig.fmap.dal.feature.spi.FeatureReferenceProviderServices;
import org.gvsig.fmap.dal.feature.spi.FeatureStoreProvider;
import org.gvsig.fmap.dal.feature.spi.FeatureStoreProviderServices;
import org.gvsig.fmap.dal.feature.spi.index.FeatureIndexProviderServices;
import org.gvsig.fmap.dal.impl.DataStoreImplementation;
import org.gvsig.fmap.dal.impl.DefaultDataManager;
import org.gvsig.fmap.dal.resource.Resource;
import org.gvsig.fmap.dal.spi.DataStoreProvider;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DelegatedDynObject;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynObject;
import org.gvsig.tools.dynobject.DynObjectManager;
import org.gvsig.tools.dynobject.exception.DynFieldNotFoundException;
import org.gvsig.tools.dynobject.exception.DynMethodException;
import org.gvsig.tools.exception.NotYetImplemented;
import org.gvsig.tools.observer.Observable;
import org.gvsig.tools.observer.Observer;
import org.gvsig.tools.observer.impl.DelegateWeakReferencingObservable;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.PersistenceValueNotFoundException;
import org.gvsig.tools.persistence.PersistentState;
import org.gvsig.tools.task.Executor;
import org.gvsig.tools.undo.RedoException;
import org.gvsig.tools.undo.UndoException;
import org.gvsig.tools.undo.command.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final public class DefaultFeatureStore implements DataStoreImplementation,
		FeatureStoreProviderServices, FeatureStore, Observer {

	final static private Logger logger = LoggerFactory
			.getLogger(DefaultFeatureStore.class);

	private DataStoreParameters parameters = null;
	private FeatureSelection selection;
	private FeatureLocks locks;

	private DelegateWeakReferencingObservable delegateObservable = new DelegateWeakReferencingObservable(this);

	private FeatureCommandsStack commands;
	private FeatureTypeManager featureTypeManager;
	private FeatureManager featureManager;
	private SpatialManager spatialManager;

	private FeatureType defaultFeatureType = null;
	private List featureTypes = new ArrayList();

	private int mode = MODE_QUERY;
	private long versionOfUpdate = 0;
	private boolean hasStrongChanges = true;
	private boolean hasInserts = true;

	private DefaultDataManager dataManager = null;

	private FeatureStoreProvider provider = null;

	private DefaultFeatureIndexes indexes;

	private DefaultFeatureStoreTransforms transforms;

	private DelegatedDynObject metadata;

	private Long featureCount = null;

	private long temporalOid = 0;

	/*
	 * TODO:
	 *
	 * - Comprobar que solo se pueden a�adir reglas de validacion sobre un
	 * EditableFeatureType. - Comprobar que solo se puede hacer un update con un
	 * featureType al que se le han cambiado las reglas de validacion cuando
	 * hasStrongChanges=false.
	 */

	public DefaultFeatureStore() {

	}

	public void intializePhase1(DefaultDataManager dataManager,
			DataStoreParameters parameters) throws InitializeException {

		DynObjectManager dynManager = ToolsLocator.getDynObjectManager();

		this.metadata = (DelegatedDynObject) dynManager
				.createDynObject(dynManager.get(DataStore.DYNCLASS_NAME));
		this.dataManager = dataManager;

		this.parameters = parameters;
		this.transforms = new DefaultFeatureStoreTransforms(this);
		try {
			indexes = new DefaultFeatureIndexes(this);
		} catch (DataException e) {
			throw new InitializeException(e);
		}

	}

	public void intializePhase2(DataStoreProvider provider) {
		this.provider = (FeatureStoreProvider) provider;
		this.delegate(provider);
	}

	public String getName() {
		return this.parameters.getDataStoreName();
	}

	public DataStoreParameters getParameters() {
		return parameters;
	}

	public int getMode() {
		return this.mode;
	}

	public DataManager getManager() {
		return this.dataManager;
	}

	public Iterator getChildren() {
		return this.provider.getChilds();
	}

	public FeatureStoreProvider getProvider() {
		return this.provider;
	}

	public FeatureManager getFeatureManager() {
		return this.featureManager;
	}

	public void setFeatureTypes(List types, FeatureType defaultType) {
		this.featureTypes = types;
		this.defaultFeatureType = defaultType;
	}

	public void open() throws OpenException {
		// TODO: Se puede hacer un open estando en edicion ?
		this.notifyChange(FeatureStoreNotification.BEFORE_OPEN);
		this.provider.open();
		this.notifyChange(FeatureStoreNotification.AFTER_OPEN);
	}

	public void refresh() throws OpenException, InitializeException {
		if (this.mode != MODE_QUERY) {
			throw new IllegalStateException();
		}
		this.notifyChange(FeatureStoreNotification.BEFORE_REFRESH);
		this.featureCount = null;
		this.provider.refresh();
		this.notifyChange(FeatureStoreNotification.AFTER_REFRESH);
	}

	public void close() throws CloseException {
		// TODO: Se puede hacer un close estando en edicion ?
		this.notifyChange(FeatureStoreNotification.BEFORE_CLOSE);
		this.featureCount = null;
		this.provider.close();
		this.notifyChange(FeatureStoreNotification.AFTER_CLOSE);
	}

	public void dispose() throws CloseException {
		this.notifyChange(FeatureStoreNotification.BEFORE_DISPOSE);
		this.provider.dispose();
		if (this.selection != null) {
			this.selection.dispose();
			this.selection = null;
		}
		this.commands = null;
		this.featureCount = null;
		if (this.locks != null) {
			//this.locks.dispose();
			this.locks = null;
		}

		if (this.featureTypeManager != null) {
			this.featureTypeManager.dispose();
			this.featureTypeManager = null;
		}

		this.featureManager = null;
		this.spatialManager = null;

		this.parameters = null;
		this.notifyChange(FeatureStoreNotification.AFTER_DISPOSE);
		this.delegateObservable.deleteObservers();
		this.delegateObservable = null;
	}

	public boolean allowWrite() {
		return this.provider.allowWrite();
	}

	public boolean canWriteGeometry(int geometryType) throws DataException {
		return this.provider.canWriteGeometry(geometryType, 0);
	}

	public DataServerExplorer getExplorer() throws ReadException,
			ValidateDataParametersException {
		return this.provider.getExplorer();
	}

	/*
	public Metadata getMetadata() throws MetadataNotFoundException {
		// TODO:
		// Si el provider devuelbe null habria que ver de construir aqui
		// los metadatos basicos, como el Envelope y el SRS.

		// TODO: Estando en edicion el Envelope deberia de
		// actualizarse usando el spatialManager
		return this.provider.getMetadata();
	}
	 */

	public Envelope getEnvelope() throws DataException {
		if (this.mode == MODE_FULLEDIT) {
			return this.spatialManager.getEnvelope();
		}
		return this.provider.getEnvelope();
	}

	/**
	 * @deprecated use getDefaultFeatureType().getDefaultSRS()
	 */
	public IProjection getSRSDefaultGeometry() throws DataException {
		return this.getDefaultFeatureType().getDefaultSRS();
	}

	public FeatureSelection createDefaultFeatureSelection()
	throws DataException {
		return new DefaultFeatureSelection(this);
	}

	public FeatureProvider createDefaultFeatureProvider(FeatureType type)
	throws DataException {
		if( type.hasOID() ) {
			return new DefaultFeatureProvider(type, this.provider.createNewOID());
		}
		return new DefaultFeatureProvider(type);
	}

	public void saveToState(PersistentState state) throws PersistenceException {
		state.set("dataStoreName", this.getName());
		state.set("parameters", this.parameters);
		state.set("provider", this.provider);
		if (this.selection != null) {
			state.set("selection", this.selection);
		}
	}

	public void loadFromState(PersistentState state) throws PersistenceException {
		if (this.provider != null) {
			throw new PersistenceException("Provider not set");
		}
		if (this.getManager() == null) {
			this.dataManager = (DefaultDataManager) DALLocator.getDataManager();
		}

		DataStoreParameters params = (DataStoreParameters) state.get("parameters");

		try {

			this.dataManager.intializeDataStore(this, params);
			try {
				setSelection((FeatureSelection) state.get("selection"));
			} catch (PersistenceValueNotFoundException e) {
				setSelection(null);
			}

		} catch (InitializeException e) {
			throw new PersistenceException(e);
		} catch (DataException e) {
			throw new PersistenceException(e);
		}

	}

	//
	// ====================================================================
	// Gestion de la seleccion
	//

	public void setSelection(DataSet selection)
	throws DataException {
		this.setSelection((FeatureSet) selection);
	}

	public DataSet createSelection() throws DataException {
		return createFeatureSelection();
	}

	public DataSet getSelection() throws DataException {
		return this.getFeatureSelection();
	}

    public void setSelection(FeatureSet selection) throws DataException {
        setSelection(selection, true);
    }

    /**
     * @see #setSelection(FeatureSet)
     * @param undoable
     *            if the action must be undoable
     */
    public void setSelection(FeatureSet selection, boolean undoable)
	throws DataException {
		if (selection.equals(this.selection)) {
			return;
		}
		if (!selection.isFromStore(this)) {
			throw new SelectionNotAllowedException(getName());
		}

		this.selection.deleteObserver(this);
		if (selection instanceof FeatureSelection) {
			if (undoable && isEditing()) {
				commands.selectionSet(this, this.selection,
						(FeatureSelection) selection);
			}
			this.selection = (FeatureSelection) selection;
		} else {
			if (undoable && isEditing()) {
				commands.startComplex("_selectionSet");
			}
			if (selection instanceof DefaultFeatureSelection) {
                DefaultFeatureSelection defSelection = (DefaultFeatureSelection) selection;
                defSelection.deselectAll(undoable);
                defSelection.select(selection, undoable);
            } else {
                this.selection.deselectAll();
                this.selection.select(selection);
            }
			if (undoable && isEditing()) {
				commands.endComplex();
			}
		}
		this.selection.addObserver(this);

		this.notifyChange(DataStoreNotification.SELECTION_CHANGE);
	}

	public FeatureSelection createFeatureSelection() throws DataException {
		return this.provider.createFeatureSelection();
	}

	public FeatureSelection getFeatureSelection() throws DataException {
		if (selection == null) {
			this.selection = createFeatureSelection();
			this.selection.addObserver(this);
		}
		return selection;
	}

	//
	// ====================================================================
	// Gestion de notificaciones
	//

	public void notifyChange(String notification) {
		delegateObservable.notifyObservers(new DefaultFeatureStoreNotification(this,
				notification));

	}

	public void notifyChange(String notification, Feature feature) {
		delegateObservable.notifyObservers(new DefaultFeatureStoreNotification(
				this, notification, feature));
	}

	public void notifyChange(String notification, Command command) {
		delegateObservable.notifyObservers(new DefaultFeatureStoreNotification(
				this, notification, command));
	}

	public void notifyChange(String notification, EditableFeatureType type) {
		delegateObservable.notifyObservers(new DefaultFeatureStoreNotification(this,
				notification, type));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.gvsig.fmap.dal.feature.spi.FeatureStoreProviderServices#notifyChange
	 * (java.lang.String, org.gvsig.fmap.dal.resource.Resource)
	 */
	public void notifyChange(String notification, Resource resource) {
		delegateObservable.notifyObservers(new DefaultFeatureStoreNotification(this,
				FeatureStoreNotification.RESOURCE_CHANGED));
	}


	//
	// ====================================================================
	// Gestion de bloqueos
	//

	public boolean isLocksSupported() {
		return this.provider.isLocksSupported();
	}

	public FeatureLocks getLocks() throws DataException {
		if (!this.provider.isLocksSupported()) {
			logger.warn("Locks not supporteds");
			return null;
		}
		if (locks == null) {
			this.locks = this.provider.createFeatureLocks();
		}
		return locks;
	}

	//
	// ====================================================================
	// Interface Observable
	//

	public void disableNotifications() {
		this.delegateObservable.disableNotifications();

	}

	public void enableNotifications() {
		this.delegateObservable.enableNotifications();
	}

	public void beginComplexNotification() {
		this.delegateObservable.beginComplexNotification();

	}

	public void endComplexNotification() {
		this.delegateObservable.endComplexNotification();

	}

	public void addObserver(Observer observer) {
		this.delegateObservable.addObserver(observer);

	}

	public void deleteObserver(Observer observer) {
		this.delegateObservable.deleteObserver(observer);
	}

	public void deleteObservers() {
		this.delegateObservable.deleteObservers();

	}

	//
	// ====================================================================
	// Interface Observer
	//
	// Usado para observar:
	// - su seleccion
	// - sus bloqueos
	// - sus recursos
	//

	public void update(Observable observable, Object notification) {
		if (observable instanceof FeatureSet) {
			if (observable == this.selection) {
				this.notifyChange(FeatureStoreNotification.SELECTION_CHANGE);
			} else if (observable == this.locks) {
				this.notifyChange(FeatureStoreNotification.LOCKS_CHANGE);
			}

		} else if (observable instanceof FeatureStoreProvider) {
			if (observable == this.provider) {

			}

		}
	}

	//
	// ====================================================================
	// Edicion
	//

	private void newVersionOfUpdate() {
		this.versionOfUpdate++;
	}

	private long currentVersionOfUpdate() {
		return this.versionOfUpdate;
	}

	private void checkInEditingMode()
	throws NeedEditingModeException {
		if (mode != MODE_FULLEDIT) {
			throw new NeedEditingModeException(this.getName());
		}
	}

	private void checkNotInAppendMode() throws IllegalStateException {
		if (mode == MODE_APPEND) {
			throw new IllegalStateException(this.getName());
		}
	}

	private void checkIsOwnFeature(Feature feature)
	throws IllegalFeatureException {
		if (((DefaultFeature) feature).getStore() != this) {
			throw new IllegalFeatureException(this.getName());
		}
		// FIXME: fixFeatureType no vale para el checkIsOwnFeature
		// fixFeatureType((DefaultFeatureType) feature.getType());
	}

	private void exitEditingMode() {
		if (commands != null) {
			commands.clear();
			commands = null;
		}

		if (featureTypeManager != null) {
			featureTypeManager.dispose();
			featureTypeManager = null;

		}

		// TODO implementar un dispose para estos dos
		featureManager = null;
		spatialManager = null;

		featureCount = null;

		mode = MODE_QUERY;
		hasStrongChanges = true; // Lo deja a true por si las moscas
		hasInserts = true;
	}

	synchronized public void edit() throws DataException {
		edit(MODE_FULLEDIT);
	}

	synchronized public void edit(int mode) throws DataException {
		try {
			if ( this.mode != MODE_QUERY ) {
				throw new AlreadyEditingException(this.getName());
			}
			if (!this.provider.supportsAppendMode()) {
				mode = MODE_FULLEDIT;
			}
			switch (mode) {
			case MODE_QUERY:
				throw new IllegalStateException(this.getName());

			case MODE_FULLEDIT:
				if (!this.transforms.isEmpty()) {
					throw new IllegalStateException(this.getName());
				}
				notifyChange(FeatureStoreNotification.BEFORE_STARTEDITING);
				featureManager = new FeatureManager(new MemoryExpansionAdapter());
				featureTypeManager = new FeatureTypeManager(this,
						new MemoryExpansionAdapter());
				spatialManager = new SpatialManager(this, provider
						.getEnvelope());

				commands = new DefaultFeatureCommandsStack(featureManager,
						spatialManager, featureTypeManager);
				this.mode = MODE_FULLEDIT;
				hasStrongChanges = false;
				hasInserts = false;
				notifyChange(FeatureStoreNotification.AFTER_STARTEDITING);
				break;
			case MODE_APPEND:
				if (!this.transforms.isEmpty()) {
					throw new IllegalStateException(this.getName());
				}
				notifyChange(FeatureStoreNotification.BEFORE_STARTEDITING);
				this.provider.beginAppend();
				this.mode = MODE_APPEND;
				hasInserts = false;
				notifyChange(FeatureStoreNotification.AFTER_STARTEDITING);
				break;
			}
		} catch (Exception e) {
			throw new StoreEditException(e, this.getName());
		}
	}

	public boolean isEditing() {
		return mode == MODE_FULLEDIT;
	}

	public boolean isAppending() {
		return mode == MODE_APPEND;
	}

	synchronized public void update(EditableFeatureType type)
	throws DataException {
		try {
			checkInEditingMode();
			if (type == null) {
				throw new NullFeatureTypeException(getName());
			}
			// FIXME: Comprobar que es un featureType aceptable.
			notifyChange(FeatureStoreNotification.BEFORE_UPDATE_TYPE, type);
			newVersionOfUpdate();

			FeatureType oldt = type.getSource().getCopy();
			FeatureType newt = type.getNotEditableCopy();
			commands.update(newt, oldt);

			if (((DefaultEditableFeatureType) type).hasStrongChanges()) {
				hasStrongChanges = true;
			}
			notifyChange(FeatureStoreNotification.AFTER_UPDATE_TYPE, type);
		} catch (Exception e) {
			throw new StoreUpdateFeatureTypeException(e, this.getName());
		}
	}

	synchronized public void delete(Feature feature) throws DataException {
		try {
			checkInEditingMode();
			checkIsOwnFeature(feature);
			if (feature instanceof EditableFeature) {
				throw new StoreDeleteEditableFeatureException(getName());
			}
			notifyChange(FeatureStoreNotification.BEFORE_DELETE, feature);
			this.commands.delete(feature);
			newVersionOfUpdate();
			hasStrongChanges = true;
			notifyChange(FeatureStoreNotification.AFTER_DELETE, feature);
		} catch (Exception e) {
			throw new StoreDeleteFeatureException(e, this.getName());
		}
	}

	private static EditableFeature lastChangedFeature = null;

	synchronized public void insert(EditableFeature feature)
	throws DataException {
		try {
			switch (mode) {
			case MODE_QUERY:
				throw new NeedEditingModeException(this.getName());

			case MODE_APPEND:
				checkIsOwnFeature(feature);
				if (feature.getSource() != null) {
					throw new NoNewFeatureInsertException(this.getName());
				}
				this.featureCount = null;
				notifyChange(FeatureStoreNotification.BEFORE_INSERT, feature);
				feature.validate(Feature.UPDATE);
				provider.append(((DefaultEditableFeature) feature).getData());
				hasStrongChanges = true;
				hasInserts = true;
				notifyChange(FeatureStoreNotification.AFTER_INSERT, feature);
				break;

			case MODE_FULLEDIT:
				checkIsOwnFeature(feature);
				if (feature.getSource() != null) {
					throw new NoNewFeatureInsertException(this.getName());
				}
				notifyChange(FeatureStoreNotification.BEFORE_INSERT, feature);
				newVersionOfUpdate();
				if (lastChangedFeature ==null || lastChangedFeature.getSource() != feature.getSource()) {
					lastChangedFeature = feature;
					feature.validate(Feature.UPDATE);
					lastChangedFeature = null;
				}
				commands.insert(feature.getNotEditableCopy());
				hasStrongChanges = true;
				hasInserts = true;
				notifyChange(FeatureStoreNotification.AFTER_INSERT, feature);
				break;
			}
		} catch (Exception e) {
			throw new StoreInsertFeatureException(e, this.getName());
		}
	}

	synchronized public void update(EditableFeature feature)
	throws DataException {
		try {
			if ((feature).getSource() == null) {
				insert(feature);
				return;
			}
			checkInEditingMode();
			checkIsOwnFeature(feature);
			notifyChange(FeatureStoreNotification.BEFORE_UPDATE, feature);
			newVersionOfUpdate();
			if (lastChangedFeature==null || lastChangedFeature.getSource() != feature.getSource()) {
				lastChangedFeature = feature;
				feature.validate(Feature.UPDATE);
				lastChangedFeature = null;
			}

			Feature oldf = feature.getSource();
			Feature newf = feature.getNotEditableCopy();
			commands.update(newf, oldf);

			hasStrongChanges = true;
			notifyChange(FeatureStoreNotification.AFTER_UPDATE, feature);
		} catch (Exception e) {
			throw new StoreUpdateFeatureException(e, this.getName());
		}
	}

	synchronized public void redo() throws RedoException {
		Command redo = commands.getNextRedoCommand();
		try {
			checkInEditingMode();
		} catch (NeedEditingModeException ex) {
			throw new RedoException(redo, ex);
		}
		notifyChange(FeatureStoreNotification.BEFORE_REDO, redo);
		newVersionOfUpdate();
		commands.redo();
		hasStrongChanges = true;
		notifyChange(FeatureStoreNotification.AFTER_REDO, redo);
	}

	synchronized public void undo() throws UndoException {
		Command undo = commands.getNextUndoCommand();
		try {
			checkInEditingMode();
		} catch (NeedEditingModeException ex) {
			throw new UndoException(undo, ex);
		}
		notifyChange(FeatureStoreNotification.BEFORE_UNDO, undo);
		newVersionOfUpdate();
		commands.undo();
		hasStrongChanges = true;
		notifyChange(FeatureStoreNotification.AFTER_UNDO, undo);
	}

	public List getRedoInfos() {
		if (isEditing() && commands != null) {
			return commands.getRedoInfos();
		} else {
			return null;
		}
	}

	public List getUndoInfos() {
		if (isEditing() && commands != null) {
			return commands.getUndoInfos();
		} else {
			return null;
		}
	}

	public synchronized FeatureCommandsStack getCommandsStack()
	throws DataException {
		checkInEditingMode();
		return commands;
	}

	synchronized public void cancelEditing() throws DataException {
		spatialManager.cancelModifies();
		try {
			checkInEditingMode();

			boolean clearSelection = this.hasStrongChanges;
			if (this.selection instanceof FeatureReferenceSelection) {
				clearSelection = this.hasInserts;
			}
			notifyChange(FeatureStoreNotification.BEFORE_CANCELEDITING);
			exitEditingMode();
			if (clearSelection) {
				((FeatureSelection) this.getSelection()).deselectAll();
			}
			notifyChange(FeatureStoreNotification.AFTER_CANCELEDITING);
		} catch (Exception e) {
			throw new StoreCancelEditingException(e, this.getName());
		}
	}

	synchronized public void finishEditing() throws DataException {
		try {
			switch (mode) {
			case MODE_QUERY:
				throw new NeedEditingModeException(this.getName());

			case MODE_APPEND:
				notifyChange(FeatureStoreNotification.BEFORE_FINISHEDITING);
				provider.endAppend();
				exitEditingMode();
				notifyChange(FeatureStoreNotification.AFTER_FINISHEDITING);
				break;

			case MODE_FULLEDIT:
				if (!hasStrongChanges) {
					performLightEditing();
					return;
				}
				if (!this.allowWrite()) {
					throw new WriteNotAllowedException(getName());
				}

				notifyChange(FeatureStoreNotification.BEFORE_FINISHEDITING);
				validateFeatures(Feature.FINISH_EDITING);
				provider.performChanges(featureManager.getDeleted(),
						featureManager.getInserted(),
						featureManager.getUpdated(),
						featureTypeManager.getFeatureTypesChanged());
				exitEditingMode();
				notifyChange(FeatureStoreNotification.AFTER_FINISHEDITING);
				break;
			}
		} catch (Exception e) {
			throw new FinishEditingException(e);
		}
	}

	private void performLightEditing() throws DataException {
		throw new NotYetImplemented(
		"lightFinishEdition not yet implemented");

		// TODO: implementar
		// notifyChange(FeatureStoreNotification.BEFORE_FINISHEDITING);
		// exitEditingMode();
		// notifyChange(FeatureStoreNotification.AFTER_FINISHEDITING);
	}


	public void beginEditingGroup(String description)
	throws NeedEditingModeException {
		checkInEditingMode();
		commands.startComplex(description);
	}

	public void endEditingGroup() throws NeedEditingModeException {
		checkInEditingMode();
		commands.endComplex();
	}

	public boolean isAppendModeSupported() {
		return this.provider.supportsAppendMode();
	}


	public void export(DataServerExplorer explorer, NewFeatureStoreParameters params)
	throws DataException {

		if (this.getFeatureTypes().size() != 1) {
			throw new NotYetImplemented(
			"export whith more than one type not yet implemented");
		}
		FeatureSelection featureSelection=(FeatureSelection)getSelection();
		try {
			FeatureType type = this.getDefaultFeatureType();
			if (params.getDefaultFeatureType() == null
					|| params.getDefaultFeatureType().size() == 0) {
				params.setDefaultFeatureType(type.getEditable());

			}
			explorer.add(params, true);

			DataManager manager = DALLocator.getDataManager();
			FeatureStore target = (FeatureStore) manager
			.createStore(params);
			FeatureType targetType = target.getDefaultFeatureType();

			target.edit(MODE_APPEND);
			FeatureSet features=null;
			FeatureAttributeDescriptor[] pk = type.getPrimaryKey();
			if (featureSelection.getSize()>0){
				features = this.getFeatureSelection();
			}else{
				if (pk != null && pk.length > 0){
					FeatureQuery query = createFeatureQuery();
					for (int i = 0; i < pk.length; i++) {
						query.getOrder().add(pk[i].getName(), true);
					}
					features = this.getFeatureSet(query);
				} else {
					features = this.getFeatureSet();
				}
			}
			Iterator it1 = features.iterator();
			while (it1.hasNext()) {
				DefaultFeature feature = (DefaultFeature) it1.next();
				target.insert(target.createNewFeature(targetType, feature));
			}
			features.dispose();
			target.finishEditing();
			target.dispose();
		} catch (Exception e) {
			throw new DataExportException(e, params.toString());
		}
	}

	//
	// ====================================================================
	// Obtencion de datos
	// getDataCollection, getFeatureCollection
	//

	public DataSet getDataSet() throws DataException {
		checkNotInAppendMode();
		FeatureQuery query = new DefaultFeatureQuery(this
				.getDefaultFeatureType());
		return new DefaultFeatureSet(this, query);
	}

	public DataSet getDataSet(DataQuery dataQuery)
	throws DataException {
		checkNotInAppendMode();
		return new DefaultFeatureSet(this, (FeatureQuery) dataQuery);
	}

	public void getDataSet(Observer observer) throws DataException {
		checkNotInAppendMode();
		this.getFeatureSet(null, observer);
	}

	public void getDataSet(DataQuery dataQuery, Observer observer)
	throws DataException {
		checkNotInAppendMode();
		this.getFeatureSet((FeatureQuery) dataQuery, observer);
	}

	public FeatureSet getFeatureSet() throws DataException {
		checkNotInAppendMode();
		FeatureQuery query = new DefaultFeatureQuery(this
				.getDefaultFeatureType());
		return new DefaultFeatureSet(this, query);
	}

	public FeatureSet getFeatureSet(FeatureQuery featureQuery)
	throws DataException {
		checkNotInAppendMode();
		return new DefaultFeatureSet(this, featureQuery);
	}


	public FeatureType getFeatureType(FeatureQuery featureQuery)
			throws DataException {
		DefaultFeatureType fType = (DefaultFeatureType) this
				.getFeatureType(featureQuery.getFeatureTypeId());
		if (featureQuery.getAttributeNames() != null){
			return fType.getSubtype(featureQuery.getAttributeNames());
		}
		return fType;
	}

	public void getFeatureSet(Observer observer)
	throws DataException {
		checkNotInAppendMode();
		this.getFeatureSet(null, observer);
	}

	public void getFeatureSet(FeatureQuery query, Observer observer)
	throws DataException {
		class LoadInBackGround implements Runnable {
			private FeatureStore store;
			private FeatureQuery query;
			private Observer observer;
			private Executor executor;
			private FeatureStoreNotification notification;

			public LoadInBackGround(FeatureStore store, FeatureQuery query,
					Observer observer, Executor executor) {
				this.store = store;
				this.query = query;
				this.observer = observer;
				this.executor = executor;
			}

			void notify(FeatureStoreNotification theNotification) {
				if (executor == null) {
					observer.update(store, theNotification);
					return;
				}
				this.notification = theNotification;
				executor.execute(new Runnable() {
					public void run() {
						observer.update(store, notification);
					}
				});

			}

			public void run() {
				try {
					FeatureSet set = store.getFeatureSet(query);
					notify(new DefaultFeatureStoreNotification(store,
							FeatureStoreNotification.LOAD_FINISHED, set));
				} catch (Exception e) {
					notify(new DefaultFeatureStoreNotification(store,
							FeatureStoreNotification.LOAD_FINISHED, e));
				}
			}
		}

		checkNotInAppendMode();
		if (query == null) {
			query = new DefaultFeatureQuery(this.getDefaultFeatureType());
		}
		Executor executor = ToolsLocator.getTaskManager().getExecutor();
		LoadInBackGround task = new LoadInBackGround(this, query, observer,
				executor);
		Thread thread = new Thread(task);
		thread.run();
	}

	public Feature getFeatureByReference(FeatureReference reference) throws DataException {
		checkNotInAppendMode();
		DefaultFeatureReference ref = (DefaultFeatureReference) reference;
		FeatureType featureType;
		if (ref.getFeatureTypeId() == null) {
			featureType = this.getDefaultFeatureType();
		} else {
			featureType = this.getFeatureType(ref.getFeatureTypeId());
		}
		return this.getFeatureByReference(reference, featureType);
	}

	public Feature getFeatureByReference(FeatureReference reference, FeatureType featureType)
	throws DataException {
		checkNotInAppendMode();
		featureType = fixFeatureType((DefaultFeatureType) featureType);
		if (!this.transforms.isEmpty()) {

			featureType = this.transforms
			.getSourceFeatureTypeFrom(featureType);

		}
		// TODO comprobar que el id es de este store

		if (this.mode == MODE_FULLEDIT) {
			Feature f = featureManager.get(reference, this, featureType);
			if (f!=null) {
				return f;
			}
		}
		DefaultFeature feature = new DefaultFeature(
				this,
				this.provider
				.getFeatureProviderByReference(
						(FeatureReferenceProviderServices) reference,
						featureType));

		if (!this.transforms.isEmpty()) {
			return this.transforms.applyTransform(feature, featureType);
		}
		return feature;
	}

	//
	// ====================================================================
	// Gestion de features
	//

	private FeatureType fixFeatureType(DefaultFeatureType type)
	throws DataException {
		FeatureType original = this.getDefaultFeatureType();

		if (type == null || type.equals(original)) {
			return original;
		} else {
			if (!type.isSubtypeOf(original)) {
				Iterator iter = this.getFeatureTypes().iterator();
				FeatureType tmpType;
				boolean found = false;
				while (iter.hasNext()) {
					tmpType = (FeatureType) iter.next();
					if (type.equals(tmpType)) {
						return type;

					}else if (type.isSubtypeOf(tmpType)) {
						found = true;
						original = tmpType;
						break;
					}

				}
				if (!found) {
					throw new IllegalFeatureTypeException(getName());
				}
			}
		}

		// Checks that type has all fields of pk
		// else add the missing attributes at the end.
		if (!original.hasOID()) {
			// Gets original pk attributes
			DefaultEditableFeatureType edOriginal = (DefaultEditableFeatureType) original
					.getEditable();
			FeatureAttributeDescriptor orgAttr;
			Iterator edOriginalIter = edOriginal.iterator();
			while (edOriginalIter.hasNext()) {
				orgAttr = (FeatureAttributeDescriptor) edOriginalIter.next();
				if (!orgAttr.isPrimaryKey()) {
					edOriginalIter.remove();
				}
			}

			// Checks if all pk attributes are in type
			Iterator typeIterator;
			edOriginalIter = edOriginal.iterator();
			FeatureAttributeDescriptor attr;
			while (edOriginalIter.hasNext()) {
				orgAttr = (FeatureAttributeDescriptor) edOriginalIter.next();
				typeIterator = type.iterator();
				while (typeIterator.hasNext()) {
					attr = (FeatureAttributeDescriptor) typeIterator.next();
					if (attr.getName().equals(orgAttr.getName())) {
						edOriginalIter.remove();
						break;
					}
				}
			}

			// add missing pk attributes if any
			if (edOriginal.size() > 0) {
				boolean isEditable = type instanceof DefaultEditableFeatureType;
				DefaultEditableFeatureType edType = (DefaultEditableFeatureType) original
						.getEditable();
				edType.clear();
				edType.addAll(type);
				edType.addAll(edOriginal);
				if (!isEditable) {
					type = (DefaultFeatureType) edType.getNotEditableCopy();
				}
			}

		}


		return type;
	}

	public void validateFeatures(int mode) throws DataException {
		try {
			checkNotInAppendMode();
			FeatureSet collection = this.getFeatureSet();
			Iterator iter = collection.iterator();
			long previousVersionOfUpdate = currentVersionOfUpdate();
			while (iter.hasNext()) {
				((DefaultFeature) iter.next()).validate(mode);
				if (previousVersionOfUpdate != currentVersionOfUpdate()) {
					throw new ConcurrentDataModificationException(getName());
				}
			}
		} catch (Exception e) {
			throw new ValidateFeaturesException(e, getName());
		}
	}

	public FeatureType getDefaultFeatureType() throws DataException {
		try {
			if (isEditing()) {
				FeatureType auxFeatureType=featureTypeManager.getType(defaultFeatureType.getId());
				if (auxFeatureType!=null) {
					return auxFeatureType;
				}
			}
			FeatureType type = this.transforms.getDefaultFeatureType();
			if (type != null) {
				return type;
			}
			return defaultFeatureType;
		} catch (Exception e) {
			throw new GetFeatureTypeException(e, getName());
		}
	}

	public FeatureType getFeatureType(String featureTypeId)
			throws DataException {
		if (featureTypeId == null) {
			return this.getDefaultFeatureType();
		}
		try {
			if (isEditing()) {
				FeatureType auxFeatureType = featureTypeManager
						.getType(featureTypeId);
				if (auxFeatureType != null) {
					return auxFeatureType;
				}
			}
			FeatureType type = this.transforms.getFeatureType(featureTypeId);
			if (type != null) {
				return type;
			}
			Iterator iter = this.featureTypes.iterator();
			while (iter.hasNext()){
				type = (FeatureType) iter.next();
				if (type.getId().equals(featureTypeId)) {
					return type;
				}
			}
			return null;
		} catch (Exception e) {
			throw new GetFeatureTypeException(e, getName());
		}
	}


	public FeatureType getProviderDefaultFeatureType() {
		return defaultFeatureType;
	}

	public List getFeatureTypes() throws DataException {
		try {
			List types;
			if (isEditing()) {
				types=new ArrayList();
				Iterator it=featureTypes.iterator();
				while (it.hasNext()) {
					FeatureType type = (FeatureType) it.next();
					FeatureType typeaux = featureTypeManager.getType(type.getId());
					if (typeaux!=null) {
						types.add(typeaux);
					}else{
						types.add(type);
					}
				}
				it = featureTypeManager.newsIterator();
				while (it.hasNext()) {
					FeatureType type = (FeatureType) it.next();
					types.add(type);
				}
			} else {
				types = this.transforms.getFeatureTypes();
				if (types == null) {
					types = featureTypes;
				}
			}
			return Collections.unmodifiableList(types);
		} catch (Exception e) {
			throw new GetFeatureTypeException(e, getName());
		}
	}

	public List getProviderFeatureTypes() throws DataException {
		return Collections.unmodifiableList(this.featureTypes);
	}

	public Feature createFeature(FeatureProvider data)
	throws DataException {
		DefaultFeature feature = new DefaultFeature(this, data);
		return feature;
	}

	public Feature createFeature(FeatureProvider data, FeatureType type)
	throws DataException {
		// FIXME: falta por implementar
		// Comprobar si es un subtipo del feature de data
		// y construir un feature usando el subtipo.
		// Probablemente requiera generar una copia del data.
		throw new NotYetImplemented();
	}

	public EditableFeature createNewFeature(FeatureType type,
			Feature defaultValues)
	throws DataException {
		try {
			FeatureProvider data = createNewFeatureProvider(type);
			DefaultEditableFeature feature = new DefaultEditableFeature(this, data);
			feature.initializeValues(defaultValues);
			return feature;
		} catch (Exception e) {
			throw new CreateFeatureException(e, getName());
		}
	}

	private FeatureProvider createNewFeatureProvider(FeatureType type)
			throws DataException {
		type = this.fixFeatureType((DefaultFeatureType) type);
		FeatureProvider data = this.provider.createFeatureProvider(type);
		data.setNew(true);
		if (type.hasOID() && data.getOID() == null) {
			data.setOID(this.provider.createNewOID());
		} else {
			data.setOID(this.getTemporalOID());
		}
		return data;

	}

	public EditableFeature createNewFeature(FeatureType type,
			boolean defaultValues)
	throws DataException {
		try {
			FeatureProvider data = createNewFeatureProvider(type);
			DefaultEditableFeature feature = new DefaultEditableFeature(this, data);
			if (defaultValues) {
				feature.initializeValues();
			}
			return feature;
		} catch (Exception e) {
			throw new CreateFeatureException(e, getName());
		}
	}

	public EditableFeature createNewFeature(boolean defaultValues)
	throws DataException {
		return this.createNewFeature(this.getDefaultFeatureType(), defaultValues);
	}

	public EditableFeature createNewFeature() throws DataException {
		return this.createNewFeature(this.getDefaultFeatureType(), true);
	}

	public EditableFeatureType createFeatureType() {
		DefaultEditableFeatureType ftype = new DefaultEditableFeatureType();
		return ftype;
	}

	public EditableFeatureType createFeatureType(String id) {
		DefaultEditableFeatureType ftype = new DefaultEditableFeatureType(id);
		return ftype;
	}


	//
	// ====================================================================
	// Index related methods
	//

	public FeatureIndexes getIndexes() {
		return this.indexes;
	}

	public FeatureIndex createIndex(FeatureType featureType,
			String attributeName, String indexName) throws ProviderNotRegisteredException, InitializeException {
		checkNotInAppendMode();
		FeatureIndexProviderServices index = null;
		index = dataManager.createFeatureIndexProvider(null, this, featureType,
                indexName, featureType.getAttributeDescriptor(attributeName));
		try {
			index.fill();
		} catch (FeatureIndexException e) {
			throw new InitializeException(index.getName(), e);
		}
		((DefaultFeatureIndexes) getIndexes()).addIndex(index);
		return index;
	}

	public FeatureIndex createIndex(FeatureType featureType,
			String attributeName, String indexName, Observer observer) {
		// TODO Implement observer interaction
		throw new UnsupportedOperationException();
	}

	//
	// ====================================================================
	// Transforms related methods
	//

	public FeatureStoreTransforms getTransforms() {
		return this.transforms;
	}

	public FeatureQuery createFeatureQuery() {
		return new DefaultFeatureQuery();
	}

	public DataQuery createQuery() {
		return createFeatureQuery();
	}

	//
	// ====================================================================
	// UndoRedo related methods
	//

	public boolean canRedo() {
		return commands.canRedo();
	}

	public boolean canUndo() {
		return commands.canUndo();
	}

	public void redo(int num) throws RedoException {
		commands.redo(num);
	}

	public void undo(int num) throws UndoException {
		commands.undo(num);
	}

	//
	// ====================================================================
	// Metadata related methods
	//

	public Object getMetadataID() {
		return this.provider.getSourceId();
	}

	public void delegate(DynObject dynObject) {
		this.metadata.delegate(dynObject);
	}

	public DynClass getDynClass() {
		return this.metadata.getDynClass();
	}

	public Object getDynValue(String name) throws DynFieldNotFoundException {
		return this.metadata.getDynValue(name);
	}

	public boolean hasDynValue(String name) {
		return this.metadata.hasDynValue(name);
	}

	public void implement(DynClass dynClass) {
		this.metadata.implement(dynClass);
	}

	public Object invokeDynMethod(String name, DynObject context)
	throws DynMethodException {
		return this.metadata.invokeDynMethod(this, name, context);
	}

	public Object invokeDynMethod(int code, DynObject context)
	throws DynMethodException {
		return this.metadata.invokeDynMethod(this, code, context);
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

	public FeatureTypeManager getFeatureTypeManager() {
		return this.featureTypeManager;
	}

	public long getFeatureCount() throws DataException {
		if (featureCount == null) {
			featureCount = new Long(this.provider.getFeatureCount());
		}
		if (this.isEditing() && !this.isAppending()) {
			return featureCount.longValue()
					- this.featureManager.getDeltaSize();
		}
		return featureCount.longValue();
	}

	private Long getTemporalOID() {
		return new Long(this.temporalOid++);
	}

	public FeatureType getProviderFeatureType(String featureTypeId) {
		if (featureTypeId == null) {
			return this.defaultFeatureType;
		}
		FeatureType type;
		Iterator iter = this.featureTypes.iterator();
		while (iter.hasNext()) {
			type = (FeatureType) iter.next();
			if (type.getId().equals(featureTypeId)) {
				return type;
			}
		}
		return null;
	}

	public FeatureProvider getFeatureProviderFromFeature(Feature feature) {
		return ((DefaultFeature) feature).getData();
	}

	public DataStore getStore() {
		return this;
	}

	public FeatureStore getFeatureStore() {
		return this;
	}

}