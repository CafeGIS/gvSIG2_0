package org.gvsig.fmap.dal.feature;

import java.util.Iterator;
import java.util.List;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.dal.DataServerExplorer;
import org.gvsig.fmap.dal.DataStore;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.exception.FeatureIndexException;
import org.gvsig.fmap.dal.feature.exception.NeedEditingModeException;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.tools.observer.Observer;
import org.gvsig.tools.undo.UndoRedoStack;

/**
 * <p>
 * A FeatureStore is a type of store whose data consists on sets of
 * {@link Feature}(s). {@link Feature}(s) from the same FeatureStore can be of
 * different {@link FeatureType}(s) (as in GML format for instance).
 * </p>
 *
 * <p>
 * FeatureStore allows:
 * </p>
 * <ul>
 * <li>Obtaining the default {@link FeatureType}. A FeatureStore always has one
 * and only one default FeatureType.
 * <li>Obtaining the list of {@link FeatureType}(s) defined in the FeatureStore.
 * <li>Obtaining, filtering and sorting subsets of data ({@link FeatureSet})
 * through {@link FeatureQuery}, as well as background loading.
 * <li>Obtaining the total {@link Envelope} (AKA bounding box or extent) of the
 * store.
 * <li>Support for editing {@link FeatureType}(s).
 * <li>Obtaining information about contained {@link Geometry} types.
 * <li>Exporting to another store.
 * <li>Indexing.
 * <li>Selection.
 * <li>Locks management.
 * </ul>
 *
 */
public interface FeatureStore extends DataStore, UndoRedoStack {

	public static final String DYNCLASS_NAME = "FeatureStore";

	/** Indicates that this store is in query mode */
	final static int MODE_QUERY = 0;

	/** Indicates that this store is in full edit mode */
	final static int MODE_FULLEDIT = 1;

	/** Indicates that this store is in append mode */
	final static int MODE_APPEND = 2;

	/*
	 * =============================================================
	 *
	 * information related services
	 */

	/**
	 * Indicates whether this store allows writing.
	 *
	 * @return
	 * 		true if this store can be written, false if not.
	 */
	public boolean allowWrite();

	/**
	 * Returns this store's default {@link FeatureType}.
	 *
	 * @return
	 * 		this store's default {@link FeatureType}.
	 *
	 * @throws DataException
	 */
	public FeatureType getDefaultFeatureType() throws DataException;

	/**
	 * Returns this store's featureType {@link FeatureType} matches with
	 * featureTypeId.
	 *
	 * @param featureTypeId
	 *
	 * @return this store's default {@link FeatureType}.
	 *
	 * @throws DataException
	 */
	public FeatureType getFeatureType(String featureTypeId)
			throws DataException;

	/**
	 * Returns this store's {@link FeatureType}(s).
	 *
	 * @return a list with this store's {@link FeatureType}(s).
	 *
	 * @throws DataException
	 */
	public List getFeatureTypes() throws DataException;

	/**
	 * Returns this store's parameters.
	 *
	 * @return
	 * 		{@link DataStoreParameters} containing this store's parameters
	 */
	public DataStoreParameters getParameters();

	/**
	 *@throws DataException
	 * @deprecated Mirar de cambiarlo a metadatos
	 */
	public boolean canWriteGeometry(int gvSIGgeometryType) throws DataException;

	/**
	 * Returns this store's total envelope (extent).
	 * 
	 * @return this store's total envelope (extent) or <code>null</code> if
	 *         store not have geometry information
	 */
	public Envelope getEnvelope() throws DataException;

	/**
	 *
	 * @deprecated use getDefaultFeatureType().getDefaultSRS()
	 * @return
	 * @throws DataException
	 */
	public IProjection getSRSDefaultGeometry() throws DataException;

	/**
	 * Exports this store to another store.
	 *
	 * @param explorer
	 *            {@link DataServerExplorer} target
	 * @param params
	 *            New parameters of this store that will be used on the target
	 *            explorer
	 *
	 * @throws DataException
	 *
	 * @Deprecated this method is unstable
	 */
	public void export(DataServerExplorer explorer, NewFeatureStoreParameters params)
			throws DataException;

	/*
	 * =============================================================
	 *
	 * Query related services
	 */

	/**
	 * Returns all available features in the store.
	 *
	 * @return a collection of features
	 * @throws ReadException
	 *             if there is any error while reading the features
	 */
	FeatureSet getFeatureSet() throws DataException;

	/**
	 * Returns a subset of features taking into account the properties and
	 * restrictions of the FeatureQuery.
	 *
	 * @param featureQuery
	 *            defines the characteristics of the features to return
	 * @return a collection of features
	 * @throws ReadException
	 *             if there is any error while reading the features
	 */
	FeatureSet getFeatureSet(FeatureQuery featureQuery)
	 throws DataException;

	/**
	 * Loads a subset of features taking into account the properties and
	 * restrictions of the FeatureQuery. Feature loading is performed by calling
	 * the Observer, once each loaded Feature.
	 *
	 * @param featureQuery
	 *            defines the characteristics of the features to return
	 * @param observer
	 *            to be notified of each loaded Feature
	 * @throws DataException
	 *             if there is any error while loading the features
	 */
	void getFeatureSet(FeatureQuery featureQuery, Observer observer)
	throws DataException;

	/**
	 * Loads all available feature in the store. The loading of Features is
	 * performed by calling the Observer, once each loaded Feature.
	 *
	 * @param observer
	 *            to be notified of each loaded Feature
	 * @throws DataException
	 *             if there is any error while loading the features
	 */
	void getFeatureSet(Observer observer) throws DataException;

	/**
	 * Returns the feature given its reference.
	 *
	 * @param reference
	 *            a unique FeatureReference
	 * @return
	 * 		The Feature
	 *
	 * @throws DataException
	 *
	 */
	public Feature getFeatureByReference(FeatureReference reference) throws DataException;

	/**
	 * Returns the feature given its reference and feature type.
	 *
	 * @param reference
	 *            a unique FeatureReference
	 *
	 * @param featureType
	 *            FeatureType to which the requested Feature belongs
	 *
	 * @return
	 * 		The Feature
	 *
	 * @throws DataException
	 *
	 */
	public Feature getFeatureByReference(FeatureReference reference, FeatureType featureType)
			throws DataException;

	/*
	 * =============================================================
	 *
	 * Editing related services
	 */

	/**
	 * Enters editing state.
	 */
	public void edit() throws DataException;

	/**
	 * Enters editing state specifying the editing mode.
	 *
	 * @param mode
	 *
	 * @throws DataException
	 */
	public void edit(int mode) throws DataException;

	/**
	 * Cancels all editing since the last edit().
	 *
	 * @throws DataException
	 */
	public void cancelEditing() throws DataException;

	/**
	 * Exits editing state.
	 *
	 * @throws DataException
	 */
	public void finishEditing() throws DataException;

	/**
	 * Indicates whether this store is in editing state.
	 *
	 * @return
	 * 		true if this store is in editing state, false if not.
	 */
	public boolean isEditing();

	/**
	 * Indicates whether this store is in appending state. In this state the new
	 * features are automatically inserted at the end of the {@link FeatureSet}.
	 *
	 * @return true if this store is in appending state.
	 */
	public boolean isAppending();

	/**
	 * Updates a {@link FeatureType} in the store with the changes in the
	 * {@link EditableFeatureType}.<br>
	 *
	 * Any {@link FeatureSet} from this store that are used will be invalidated.
	 *
	 * @param featureType
	 *            an {@link EditableFeatureType} with the changes.
	 *
	 * @throws DataException
	 */
	public void update(EditableFeatureType featureType) throws DataException;

	/**
	 * Updates a {@link Feature} in the store with the changes in the
	 * {@link EditableFeature}.<br>
	 *
	 * Any {@link FeatureSet} from this store that was still in use will be
	 * invalidated. You can override this using
	 * {@link FeatureSet#update(EditableFeature)}.
	 *
	 * @param feature
	 *            the feature to be updated
	 *
	 * @throws DataException
	 */
	public void update(EditableFeature feature) throws DataException;

	/**
	 * Deletes a {@link Feature} from the store.<br>
	 *
	 * Any {@link FeatureSet} from this store that was still in use will be
	 * invalidated. You can override this using {@link Iterator#remove()} from
	 * {@link FeatureSet}.
	 *
	 * @param feature
	 *            The feature to be deleted.
	 *
	 * @throws DataException
	 */
	public void delete(Feature feature) throws DataException;

	/**
	 * Inserts a {@link Feature} in the store.<br>
	 *
	 * Any {@link FeatureSet} from this store that was still in use will be
	 * invalidated. You can override this using
	 * {@link FeatureSet#insert(EditableFeature)}.
	 *
	 * @param feature
	 *            The feature to be inserted
	 *
	 * @throws DataException
	 */
	public void insert(EditableFeature feature) throws DataException;

	/**
	 * Creates a new feature using the default feature type and returns it as an
	 * {@link EditableFeature}
	 *
	 * @return a new feature in editable state
	 *
	 * @throws DataException
	 */
	public EditableFeature createNewFeature() throws DataException;

	/**
	 * Creates a new feature of the given {@link FeatureType} and uses the given
	 * {@link Feature} as default values to initialize it.
	 *
	 * @param type
	 *            the new feature's feature type
	 *
	 * @param defaultValues
	 *            a feature whose values are used as default values for the new
	 *            feature.
	 *
	 * @return the new feature.
	 *
	 * @throws DataException
	 */
	public EditableFeature createNewFeature(FeatureType type,
			Feature defaultValues) throws DataException;

	/**
	 * Creates a new feature of the given {@link FeatureType}. The flag
	 * defaultValues is used to indicate whether the new feature should be
	 * initialized with default values or not.
	 *
	 * @param type
	 *            the new feature's feature type
	 *
	 * @param defaultValues
	 * 			if true the new feature is initialized with each attribute's default value.
	 *
	 * @return
	 * 		the new feature
	 *
	 * @throws DataException
	 */
	public EditableFeature createNewFeature(FeatureType type,
			boolean defaultValues) throws DataException;

	/**
	 * Creates a new feature of default {@link FeatureType}. The flag
	 * defaultValues is used to indicate whether the new feature should be
	 * initialized with default values or not.
	 *
	 * @param defaultValues
	 * 				if true the new feature is initialized with each attribute's default value.
	 *
	 * @return
	 * 		the new feature
	 *
	 * @throws DataException
	 */
	public EditableFeature createNewFeature(boolean defaultValues)
			throws DataException;

	/**
	 * Applies the validation rules associated to the given mode to the active {@link FeatureSet}.
	 *
	 * @param mode
	 *            can be one of {MODE_QUERY, MODE_FULLEDIT, MODE_APPEND}
	 *
	 * @throws DataException
	 */
	public void validateFeatures(int mode) throws DataException;

	/**
	 * Indicates whether this store supports append mode.
	 *
	 * @return
	 * 		true if this store supports append mode.
	 */
	public boolean isAppendModeSupported();

	/**
	 * Initiates an editing group. This is typically used to group series of
	 * store editing operations.
	 *
	 * @param description
	 *            Description of the editing group.
	 *
	 * @throws NeedEditingModeException
	 */
	public void beginEditingGroup(String description)
			throws NeedEditingModeException;

	/**
	 * Finishes an editing group.
	 *
	 * @throws NeedEditingModeException
	 */
	public void endEditingGroup() throws NeedEditingModeException;

	/*
	 * =============================================================
	 *
	 * Index related services
	 */

	/**
	 * Creates a FeatureIndex with name indexName, given the attributeName and
	 * its featureType.
	 *
	 * @param featureType
	 *            The FeatureType to which the indexed attribute belongs.
	 *
	 * @param attributeName
	 *            The name of the attributed to be indexed
	 *
	 * @param indexName
	 *            The index name
	 *
	 * @return the resulting FeatureIndex
	 *
	 *
	 * @throws FeatureIndexException
	 */
	public FeatureIndex createIndex(FeatureType featureType,
			String attributeName, String indexName) throws DataException;

	/**
	 * Creates a FeatureIndex with name indexName, given the attributeName and
	 * its featureType. This method is intended for background indexing. It
	 * receives an observer that will be notified by the process.
	 *
	 * @param featureType
	 *            The FeatureType to which the indexed attribute belongs.
	 *
	 * @param attributeName
	 *            The name of the attributed to be indexed
	 *
	 * @param indexName
	 *            The index name
	 *
	 * @param observer
	 *            an observer that will be notified of the indexing process
	 *            progress.
	 *
	 * @return the resulting FeatureIndex
	 *
	 *
	 * @throws FeatureIndexException
	 */
	public FeatureIndex createIndex(FeatureType featureType,
			String attributeName, String indexName, Observer observer) throws DataException;
	/**
	 * Returns a FeatureIndexes structure containing all available indexes in
	 * the store.
	 *
	 * @return
	 */
	public FeatureIndexes getIndexes();

	/*
	 * =============================================================
	 *
	 * Selection related services
	 */

	/**
	 * Sets the selection to the passed {@link FeatureSet}
	 *
	 * @param selection
	 *            A {@link FeatureSet} with the requested selection
	 */
	public void setSelection(FeatureSet selection) throws DataException;

	/**
	 * Creates a {@link FeatureSelection}
	 *
	 * @return
	 * 		a {@link FeatureSelection}
	 *
	 * @throws DataException
	 */
	public FeatureSelection createFeatureSelection() throws DataException;

	/**
	 * Returns the current {@link FeatureSelection}.
	 *
	 * @return
	 * 		current {@link FeatureSelection}.
	 *
	 * @throws DataException
	 */
	public FeatureSelection getFeatureSelection() throws DataException;

	/*
	 * =============================================================
	 *
	 * Lock related services
	 */

	/**
	 * Indicates whether this store supports locks.
	 *
	 * @return
	 * 		true if this store supports locks, false if not.
	 */
	public boolean isLocksSupported();

	/**
	 * Returns the set of locked features
	 *
	 * @return
	 * 		set of locked features
	 *
	 * @throws DataException
	 */
	public FeatureLocks getLocks() throws DataException;

	/*
	 * =============================================================
	 * Transforms related services
	 * =============================================================
	 */

	/**
	 * Returns this store transforms
	 *
	 * @return
	 * 		this store transforms
	 */
	public FeatureStoreTransforms getTransforms();

	/**
	 * Returns a new {@link FeatureQuery} associated to this store.
	 *
	 * @return
	 * 		a new {@link FeatureQuery} associated to this store.
	 */
	public FeatureQuery createFeatureQuery();

	/**
	 * Returns featue count of this store.
	 *
	 * @return
	 * @throws DataException
	 */
	public long getFeatureCount() throws DataException;
}

