package org.gvsig.fmap.data.feature.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.data.feature.swing.table.ConfigurableFeatureTableModel;
import org.gvsig.fmap.data.feature.swing.table.FeatureTypeChangeListener;
import org.gvsig.fmap.data.feature.swing.table.FeatureTypesControl;

public class FeatureTypesTablePanel extends JPanel implements
		FeatureTypeChangeListener {

	/**
	 *
	 */
private static final long serialVersionUID = 5857146295213412304L;
private FeatureTablePanel tablePanel;
private FeatureTypesControl typesControl;
private JSplitPane jSplitPane = null;

/**
 * Constructs a Panel to show a table with the features of a FeatureStore.
 *
 * @param featureStore
 *            to extract the features from
 * @throws DataException
 *             if there is an error reading data from the FeatureStore
 */
public FeatureTypesTablePanel(FeatureStore featureStore) throws DataException {
    this(featureStore, true);
}

/**
 * Constructs a Panel to show a table with the features of a FeatureStore.
 *
 * @param featureStore
 *            to extract the features from
 * @param isDoubleBuffered
 *            a boolean, true for double-buffering, which uses additional
 *            memory space to achieve fast, flicker-free updates
 * @throws DataException
 *             if there is an error reading data from the FeatureStore
 */
public FeatureTypesTablePanel(FeatureStore featureStore, boolean isDoubleBuffered)
        throws DataException {
   this(featureStore, null, isDoubleBuffered);
}

/**
 * @throws DataException
 *
 */
public FeatureTypesTablePanel(FeatureStore featureStore,
        FeatureQuery featureQuery) throws DataException {
    this(featureStore, featureQuery, true);
}

/**
 * @param isDoubleBuffered
 * @throws DataException
 */
public FeatureTypesTablePanel(FeatureStore featureStore,
        FeatureQuery featureQuery,
        boolean isDoubleBuffered)
        throws DataException {
	super();
    tablePanel=new FeatureTablePanel(featureStore,featureQuery,isDoubleBuffered);
    typesControl=new FeatureTypesControl(featureStore);
    this.intializeUI();

}


public void change(FeatureStore featureStore, FeatureType featureType, boolean isDoubleBuffered) {
	try {
		FeatureQuery featureQuery = featureStore.createFeatureQuery();
		featureQuery.setFeatureType(featureType);
		tablePanel=new FeatureTablePanel(featureStore, featureQuery, isDoubleBuffered);
	} catch (DataException e) {
		e.printStackTrace();
	}
}


public FeatureTablePanel getTablePanel() {
	return tablePanel;
}


public FeatureTypesControl getTypesControl() {
	return typesControl;
}
/**
 * Returns the internal Table Model for the Features of the FeatureStore.
 *
 * @return the internal Table Model
 */
protected ConfigurableFeatureTableModel getTableModel() {
    return getTablePanel().getTableModel();
}

/**
 * This method initializes jPanel
 *
 * @return javax.swing.JPanel
 */
	private void intializeUI() {
		this.setLayout(new BorderLayout());
		this.setSize(new Dimension(331, 251));
		this.add(getJSplitPane(), BorderLayout.CENTER);
		hideTypes();
}

/**
 * This method initializes jSplitPane
 *
 * @return javax.swing.JSplitPane
 */
private JSplitPane getJSplitPane() {
	if (jSplitPane == null) {
		jSplitPane = new JSplitPane();
		jSplitPane.setRightComponent(getTablePanel());
		jSplitPane.setLeftComponent(getTypesControl());
	}
	return jSplitPane;
}
public void hideTypes(){
	jSplitPane.remove(getTypesControl());
}
public void showTypes(){
	jSplitPane.setLeftComponent(getTypesControl());
}
}
