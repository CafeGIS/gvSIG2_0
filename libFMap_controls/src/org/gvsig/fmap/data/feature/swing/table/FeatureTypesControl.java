package org.gvsig.fmap.data.feature.swing.table;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;

public class FeatureTypesControl extends JPanel{

	private JScrollPane jScrollPane = null;  //  @jve:decl-index=0:visual-constraint="10,94"
	private JList jList = null;
	private ArrayList<FeatureTypeChangeListener> featureTypeChangeListeners=new ArrayList<FeatureTypeChangeListener>();
	private FeatureStore featureStore;

	public FeatureTypesControl(FeatureStore fs) {
		featureStore=fs;
		initialize();
	}

	private void initialize() {
		GridBagConstraints gridBagConstraints = new GridBagConstraints() ;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.gridx = 0;
		this.setLayout(new GridBagLayout());
		this.setSize(new Dimension(146, 281));
		this.add(getJScrollPane(), gridBagConstraints);
	}

	/**
	 * This method initializes jScrollPane
	 *
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setSize(new Dimension(154, 421));
			jScrollPane.setViewportView(getJList());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jList
	 *
	 * @return javax.swing.JList
	 */
	private JList getJList() {
		if (jList == null) {
			ListFeatureTypeModel listmodel = new ListFeatureTypeModel();

			List<FeatureType> list=null;
			try {
				list = featureStore.getFeatureTypes();
			} catch (DataException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			Iterator<FeatureType> iterator= list.iterator();

			while (iterator.hasNext()) {
				FeatureType ft = iterator.next();
				listmodel.addFeatureType(ft);
			}

			jList = new javax.swing.JList();
			jList.setModel(listmodel);
			jList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
					public void valueChanged(
								javax.swing.event.ListSelectionEvent e) {
						int selectInt =((JList) e.getSource())
							.getSelectedIndex();
						FeatureType fType;
							try {
								fType = featureStore
										.getFeatureType((String) jList
												.getModel().getElementAt(
														selectInt));
							} catch (DataException e1) {
								throw new FeatureTypeSelectionChangeException(
										e1);
							}
						for (int i = 0; i < featureTypeChangeListeners.size(); i++) {

							featureTypeChangeListeners.get(i).change(
										featureStore, fType, true);
						}
					}
			});
		}

		return jList;
	}

	public void addFeatureTypeChangeListener(FeatureTypeChangeListener ftcl){
		featureTypeChangeListeners.add(ftcl);
	}


}
