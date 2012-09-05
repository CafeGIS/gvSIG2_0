package org.gvsig.fmap.dal.feature;

import java.util.List;

import org.gvsig.fmap.dal.DataSet;
import org.gvsig.fmap.dal.exception.DataException;

/**
 * A FeatureSet represents a set of {@link Feature}(s). These sets of features
 * are typically obtained directly from a {@link FeatureStore}, or through a
 * {@link FeatureQuery}.
 * 
 * A FeatureSet may contain subsets of {@link Feature}(s) of different
 * {@link FeatureType}(s). It allows iterating and editing the features.
 * 
 * FIXME: Actualizar javadoc
 * 
 * Si el store en el que esta basado el featureset es modificado, se realiza un
 * update, insert, delete o se modifican los featuretype de este, el FeatureSet
 * quedara invalidado, y cualquier acceso a el provocara que se lance una
 * excepcion de tiempo de ejecucion ConcurrentModificationException.
 * 
 * Habria que indicar que invocar al metodo accept del interface visitor
 * provocara la excepcion ConcurrentModificationException si el store a sido
 * modificado desde otro punto.
 * 
 * Indicar que los metodos insert/delete/update ademas de actuar sobre el set,
 * actuan sobre el store en el que este esta basado, pero que no invalidan el
 * set sobre el que se ejecutan. No se si esto deberia hacerse mencion en esos
 * metodos o en la doc general del featureset.
 * 
 */
public interface FeatureSet extends DataSet {

	/**
	 * Returns the default {@link FeatureType} of this FeatureSet.
	 * 
	 * @return default {@link FeatureType} in this FeatureSet.
	 */
	public FeatureType getDefaultFeatureType();

	/**
	 * Returns a list of the {@link FeatureType}(s) in this FeatureSet.
	 * 
	 * @return list of the {@link FeatureType}(s) in this FeatureSet.
	 */
	public List getFeatureTypes();

	/**
	 * Returns the number of {@link Feature}(s) contained in this FeatureSet.
	 * 
	 * @return number of {@link Feature}(s) contained in this FeatureSet.
	 * 
	 * @throws DataException
	 */
    public long getSize() throws DataException;

	/**
	 * Returns an iterator over the elements in this collection, in the order
	 * (if any) defined when the collection was obtained.
	 *
	 * The iterator starts at the specified position in this collection. The
	 * specified index indicates the first element that would be returned by an
	 * initial call to the <tt>next</tt> method. An initial call to the
	 * <tt>previous</tt> method would return the element with the specified
	 * index minus one.
	 *
	 * @param index
	 *            index of first element to be returned from the iterator (by a
	 *            call to the <tt>next</tt> method).
	 * @return an iterator of the elements in this collection (in proper
	 *         sequence), starting at the specified position in the collection.
	 * @throws DataException
	 *             if the index is out of range (index &lt; 0 || index &gt;
	 *             size()).
	 */
	DisposableIterator iterator(long index) throws DataException;

	/**
	 * Returns an iterator over the elements in this collection, in the order
	 * (if any) defined when the collection was obtained.
	 * 
	 * @return an iterator of the elements in this collection (in proper
	 *         sequence).
	 * 
	 * @throws DataException
	 */
	DisposableIterator iterator() throws DataException;

	/**
	 * Indicates whether this FeatureSet contains zero features.
	 * 
	 * @return true if this FeatureSet is empty, false otherwise.
	 * 
	 * @throws DataException
	 */
	boolean isEmpty() throws DataException;

	/**
	 * Updates a {@link Feature} with the given {@link EditableFeature}.<br>
	 * 
	 * Any {@link DisposableIterator} from this store that was still in use can will not
	 * reflect this change.
	 * 
	 * @param feature
	 *            an instance of {@link EditableFeature} with which to update
	 *            the associated {@link Feature}.
	 * 
	 * @throws DataException
	 */
	public void update(EditableFeature feature) throws DataException;

	/**
	 * Deletes a {@link Feature} from this FeatureSet.<br>
	 * 
	 * Any {@link DisposableIterator} from this store that was still in use will be
	 * <i>unsafe</i>. Use {@link DisposableIterator#remove()} instead.
	 * 
	 * @param feature
	 *            the {@link Feature} to delete.
	 * 
	 * @throws DataException
	 */
	public void delete(Feature feature) throws DataException;

	/**
	 * Inserts a new feature in this set. It needs to be an instance of
	 * {@link EditableFeature} as it has not been stored yet.<br>
	 * 
	 * Any {@link DisposableIterator} from this store that was still in use can will not
	 * reflect this change.
	 * 
	 * @param feature
	 *            the {@link EditableFeature} to insert.
	 * 
	 * @throws DataException
	 */
	public void insert(EditableFeature feature) throws DataException;

	/**
	 * Returns a fast iterator over this set.
	 * 
	 * @return an iterator over this set.
	 * 
	 * @throws DataException
	 */
	public DisposableIterator fastIterator() throws DataException;

	/**
	 * Returns a fast iterator over this set, starting from the given index.
	 * 
	 * @param index
	 *            position in which the iterator is initially located.
	 * 
	 * @return an iterator initially located at the position indicated by the
	 *         given index
	 * 
	 * @throws DataException
	 */
	public DisposableIterator fastIterator(long index) throws DataException;
}
