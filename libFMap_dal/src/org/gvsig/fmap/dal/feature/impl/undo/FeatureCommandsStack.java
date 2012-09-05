package org.gvsig.fmap.dal.feature.impl.undo;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.*;
import org.gvsig.fmap.dal.feature.impl.DefaultFeatureReferenceSelection;
import org.gvsig.fmap.dal.feature.impl.DefaultFeatureStore;
import org.gvsig.tools.undo.command.UndoRedoCommandStack;

public interface FeatureCommandsStack extends UndoRedoCommandStack {

    public void insert(Feature feature);

    public void update(Feature feature, Feature oldFeature);

    public void delete(Feature feature);

//    public void insert(FeatureType featureType);

    public void update(FeatureType featureType, FeatureType oldFeatureType);

//    public void delete(FeatureType featureType);

    public void select(DefaultFeatureReferenceSelection selection,
            FeatureReference reference);

    public void deselect(DefaultFeatureReferenceSelection selection,
            FeatureReference reference);

    public void selectAll(DefaultFeatureReferenceSelection selection)
            throws DataException;

    public void deselectAll(DefaultFeatureReferenceSelection selection)
            throws DataException;

    public void selectionReverse(DefaultFeatureReferenceSelection selection);

    public void selectionSet(DefaultFeatureStore store,
            FeatureSelection oldSelection, FeatureSelection newSelection);

}