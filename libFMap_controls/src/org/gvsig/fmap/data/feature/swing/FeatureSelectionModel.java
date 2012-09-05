/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Gobernment (CIT)
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
 * 2008 {DiSiD Technologies}  {{Task}}
 */
package org.gvsig.fmap.data.feature.swing;

import javax.swing.DefaultListSelectionModel;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureSelection;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureStoreNotification;
import org.gvsig.fmap.data.feature.swing.table.FeatureTableModel;
import org.gvsig.tools.observer.Observable;
import org.gvsig.tools.observer.Observer;

/**
 * ListSelectionModel implementation based on the FeatureSelection of a
 * FeatureStore of a FeatureTableModel used to show Features of a FeatureSet.
 *
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public class FeatureSelectionModel extends DefaultListSelectionModel implements
		Observer {

    private static final long serialVersionUID = 7643472991526133823L;

    private final FeatureTableModel featureTableModel;

    /**
     * Creates a new FeatureSelectionModel with a FeatureTableModel used to get
     * the Features by position in the table.
     *
     * @param featureTableModel
     *            to get Features from
     * @throws DataException
     *             if there is an error getting the store selection
     */
    public FeatureSelectionModel(FeatureTableModel featureTableModel)
            throws DataException {
        this.featureTableModel = featureTableModel;
        this.featureTableModel.getFeatureStore().addObserver(this);
    }

    @Override
    public boolean isSelectedIndex(int index) {
        // if (getValueIsAdjusting()) {
        // return super.isSelectedIndex(index);
        // } else {
    	if (index==-1){
    		return false;
    	}
        Feature feature = featureTableModel.getFeatureAt(index);
        return getSelection().isSelected(feature);
        // }
    }

    @Override
    public void setSelectionInterval(int index0, int index1) {
        // if (!getValueIsAdjusting()) {
        try {
        	getSelection().deselectAll();
        } catch (DataException ex) {
            throw new SelectionChangeException(ex);
        }
        selectFeatureInterval(index0, index1);
        // }
        super.setSelectionInterval(index0, index1);
    }

    @Override
    public void addSelectionInterval(int index0, int index1) {
        // if (!getValueIsAdjusting()) {
        selectFeatureInterval(index0, index1);
        // }
        super.addSelectionInterval(index0, index1);
    }

    @Override
    public void removeSelectionInterval(int index0, int index1) {
        // if (!getValueIsAdjusting()) {
        selectFeatureInterval(index0, index1, false);
        // }
        super.removeSelectionInterval(index0, index1);
    }

    /**
     * Selects the features between an interval of rows.
     */
    private void selectFeatureInterval(int index0, int index1) {
        selectFeatureInterval(index0, index1, true);
    }

    /**
     * Selects the features between an interval of rows.
     *
     * @param index0
     *            the first row index
     * @param index1
     *            the second row index (index0 > index1 not required)
     * @param select
     *            if to select or deselect
     */
    private void selectFeatureInterval(int index0, int index1, boolean select) {
        // If only single selection is allowed, the valid value is index1
        if (getSelectionMode() == SINGLE_SELECTION) {
            selectFeatureAt(index1, select, getSelection());
        } else {
            // index0 < index1 is not guaranteed
            int first = index0 <= index1 ? index0 : index1;
            int last = index0 <= index1 ? index1 : index0;

            // Its a full de/selection
            if (first == 0 && last == featureTableModel.getRowCount() - 1) {
                try {
                    if (select) {
                    	getSelection().selectAll();
                    } else {
                    	getSelection().deselectAll();
                    }
                } catch (DataException ex) {
                    throw new SelectionChangeException(ex);
                }
            } else {
            	FeatureSelection selection;
				try {
					selection = this.getFeatureStore().createFeatureSelection();
				} catch (DataException e) {
					throw new SelectionChangeException(e);
				}
				for (int i = first; i <= last; i++) {
					selectFeatureAt(i, select, selection);
                }
				try {
					this.getFeatureStore().setSelection(selection);
				} catch (DataException e) {
					throw new SelectionChangeException(e);
				}
            }
        }
    }

	/**
	 * Selects a feature at a row position.
	 *
	 * @param index
	 *            the Feature row index
	 * @param select
	 *            if to select or deselect
	 * @param selection
	 *            selection instance to use
	 */
    private void selectFeatureAt(int index, boolean select,
			FeatureSelection selection) {
        Feature feature = getFeature(index);
        if (select) {
            selection.select(feature);
        } else {
        	selection.deselect(feature);
        }
    }

    /**
     * Returns a Feature by table row position.
     */
    private Feature getFeature(int index) {
        return featureTableModel.getFeatureAt(index);
    }

    /**
     * Returns the FeatureStore.
     */
    private FeatureStore getFeatureStore() {
        return featureTableModel.getFeatureStore();
    }

    /**
	 * Returns the FeatureStore.
	 */
	private FeatureSelection getSelection() {
        try {
			return (FeatureSelection) getFeatureStore().getSelection();
		} catch (DataException ex) {
			throw new SelectionChangeException(ex);
		}
	}

	public void update(Observable observable, Object notification) {
		if (notification instanceof FeatureStoreNotification) {
			FeatureStoreNotification fnotification = (FeatureStoreNotification) notification;
			if (!fnotification.getSource().equals(getFeatureStore())) {
				return;
			}
			if (FeatureStoreNotification.SELECTION_CHANGE.equals(fnotification
					.getType())) {
				this.fireValueChanged(-1, -1, false);

			}
		}

	}

}