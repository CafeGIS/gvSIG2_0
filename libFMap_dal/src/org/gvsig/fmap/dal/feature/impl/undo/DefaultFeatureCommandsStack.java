package org.gvsig.fmap.dal.feature.impl.undo;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.*;
import org.gvsig.fmap.dal.feature.impl.*;
import org.gvsig.fmap.dal.feature.impl.undo.command.*;
import org.gvsig.tools.undo.command.impl.DefaultUndoRedoCommandStack;

/**
 * Clase en memoria para registrar y gestionar los comandos que vamos
 * realizando. La forma en que ha sido implementada esta clase, en vez de una
 * única lista para albergar los comandos de deshacer(undos) y los de
 * rehacer(redos), se ha optado por dos pilas una para deshacer(undos) y otra
 * para rehacer(redos), de esta forma : Cuando se añade un nuevo comando, se
 * inserta este a la pila de deshacer(undos) y se borra de la de rehacer(redos).
 * Si se realiza un deshacer se desapila este comando de la pila deshacer(undos)
 * y se apila en la de rehacer(redos). Y de la misma forma cuando se realiza un
 * rehacer se desapila este comando de la pila de rehacer(redos) y pasa a la de
 * deshacer(undos).
 *
 * @author Vicente Caballero Navarro
 */
public class DefaultFeatureCommandsStack extends DefaultUndoRedoCommandStack
        implements FeatureCommandsStack {
    private FeatureManager expansionManager;
    private SpatialManager spatialManager;
    private FeatureTypeManager featureTypeManager;

    public DefaultFeatureCommandsStack(FeatureManager expansionManager,
            SpatialManager spatialManager, FeatureTypeManager featureTypeManager) {
        this.expansionManager = expansionManager;
        this.spatialManager = spatialManager;
        this.featureTypeManager = featureTypeManager;
    }

    public void clear() {
        super.clear();
        expansionManager.clear();
        featureTypeManager.clear();
        spatialManager.clear();
    }

    public void deselect(DefaultFeatureReferenceSelection selection,
            FeatureReference reference) {
        SelectionCommandSelect command = new SelectionCommandSelect(selection,
                reference, false, "_selectionDeselect");
        add(command);
    }

    public void deselectAll(DefaultFeatureReferenceSelection selection)
            throws DataException {
        SelectionCommandSelectAll command = new SelectionCommandSelectAll(
                selection, false, "_selectionDeselectAll");
        add(command);
    }

    public void select(DefaultFeatureReferenceSelection selection,
            FeatureReference reference) {
        SelectionCommandSelect command = new SelectionCommandSelect(selection,
                reference, true, "_selectionSelect");
        add(command);
    }

    public void selectAll(DefaultFeatureReferenceSelection selection)
            throws DataException {
        SelectionCommandSelectAll command = new SelectionCommandSelectAll(
                selection, true, "_selectionSelectAll");
        add(command);
    }

    public void selectionReverse(DefaultFeatureReferenceSelection selection) {
        SelectionCommandReverse command = new SelectionCommandReverse(
                selection, "_selectionReverse");
        add(command);
    }

    public void selectionSet(DefaultFeatureStore store,
            FeatureSelection oldSelection, FeatureSelection newSelection) {
        SelectionCommandSet command = new SelectionCommandSet(store,
                oldSelection, newSelection, "_selectionSet");
        add(command);
    }

    public void delete(Feature feature) {
        FeatureDeleteCommand command = new FeatureDeleteCommand(
                expansionManager, spatialManager, feature, "_featureDelete");
        add(command);
        command.execute();
    }

//    public void delete(FeatureType featureType) {
//        FTypeDeleteCommand command = new FTypeDeleteCommand(
//                featureTypeManager, featureType, "_typeDelete");
//        add(command);
//        command.execute();
//    }

    public void insert(Feature feature) {
        FeatureInsertCommand command = new FeatureInsertCommand(
                expansionManager, spatialManager, feature, "_featureInsert");
        add(command);
        command.execute();
    }

//    public void insert(FeatureType featureType) {
//        FTypeInsertCommand command = new FTypeInsertCommand(
//                featureTypeManager, featureType, "_typeInsert");
//        add(command);
//        command.execute();
//    }

    public void update(Feature feature, Feature oldFeature) {
        FeatureUpdateCommand command = new FeatureUpdateCommand(
                expansionManager, spatialManager, feature, oldFeature,
                "_featureUpdate");
        add(command);
        command.execute();
    }

    public void update(FeatureType featureType, FeatureType oldFeatureType) {
        FTypeUpdateCommand command = new FTypeUpdateCommand(
                featureTypeManager, featureType, oldFeatureType,
                "_typeUpdate");
        add(command);
        command.execute();
    }

}