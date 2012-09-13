/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SettingsPanel.java
 *
 * Created on 16-mar-2009, 12:41:50
 */

package org.gvsig.geocoding.gui.results;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.EditableFeature;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureStoreNotification;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.impl.DefaultEditableFeature;
import org.gvsig.fmap.dal.feature.impl.DefaultFeatureStore;
import org.gvsig.fmap.dal.feature.spi.AbstractFeatureStoreProvider;
import org.gvsig.fmap.dal.feature.spi.DefaultFeatureData;
import org.gvsig.fmap.dal.feature.spi.FeatureData;
import org.gvsig.fmap.dal.store.shp.SHPStoreParameters;
import org.gvsig.fmap.dal.store.shp.SHPStoreProvider;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.geocoding.extension.GeocodingController;
import org.gvsig.geocoding.gui.TableResultsModel;
import org.gvsig.geocoding.utils.GeocodingTags;
import org.gvsig.tools.evaluator.Evaluator;
import org.gvsig.tools.observer.Observable;
import org.gvsig.tools.observer.Observer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;

/**
 * 
 * @author vsanjaime
 */
public class ResultsPanel extends JPanel implements IWindow, Observer,
		MouseListener {

	private static final long serialVersionUID = 1L;
	private Logger log = LoggerFactory.getLogger(ResultsPanel.class);

	private GeocodingController control = null;
	private FeatureStore allstore = null;
	private FeatureStore shpstore = null;
	private JButton jButUpdate;
	private JButton jButCancel;
	private JPanel jPanButtons;
	private JPanel jPanResults;
	private JScrollPane jScrollResults;
	private JTable jTableResults;

	/**
	 * Constructor
	 */
	public ResultsPanel(GeocodingController control) {
		this.control = control;
		initComponents();

		setImages();
		setMesages();
	}

	/**
	 * Initialize panel components
	 */
	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;

		jPanResults = new JPanel();
		jScrollResults = new JScrollPane();
		jTableResults = new JTable();
		jPanButtons = new JPanel();
		jButUpdate = new JButton();
		jButCancel = new JButton();

		setLayout(new java.awt.GridBagLayout());

		jPanResults.setLayout(new java.awt.GridBagLayout());

		jTableResults.setModel(new TableResultsModel(control));
		jTableResults.addMouseListener(this);

		jScrollResults.setViewportView(jTableResults);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		jPanResults.add(jScrollResults, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		add(jPanResults, gridBagConstraints);

		jPanButtons.setLayout(new java.awt.GridBagLayout());

		jButUpdate.setText("Update");
		jButUpdate.setEnabled(false);
		jButUpdate.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				evUpdate(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 10);
		jPanButtons.add(jButUpdate, gridBagConstraints);

		jButCancel.setText("cancel");
		jButCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				evCancel(evt);
			}
		});
		jPanButtons.add(jButCancel, new java.awt.GridBagConstraints());

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
		add(jPanButtons, gridBagConstraints);
	}

	/**
	 * 
	 * @param evt
	 */
	private void evUpdate(java.awt.event.ActionEvent evt) {
		//updateStoreSelectedResults();
		updateStoreAllResults();

	}

	/**
	 * 
	 * @param evt
	 */
	private void evCancel(java.awt.event.ActionEvent evt) {
		IWindow[] iws = PluginServices.getMDIManager().getAllWindows();
		for (int i = 0; i < iws.length; i++) {
			if (iws[i] instanceof ResultsPanel) {
				PluginServices.getMDIManager().closeWindow(iws[i]);
			}
		}
	}

	/**
	 * 
	 * @param nres
	 */
	public void loadResults(int nres) {
		DataManager manager = DALLocator.getDataManager();
		try {
			FeatureQuery query = allstore.createFeatureQuery();
			String exp = GeocodingTags.gID_RESUL + " like " + nres;
			Evaluator eval = manager.createExpresion(exp);
			query.setFilter(eval);

			FeatureSet set = allstore.getFeatureSet(query);
			FeatureType type = allstore.getDefaultFeatureType();
			TableResultsModel model = new TableResultsModel(type, set);
			jTableResults.setModel(model);
			jTableResults.validate();

		} catch (DataException e) {
			log.error("ERROR loading selected results in table", e);
		}
	}

	/**
	 * 
	 */
	private void setImages() {
		PluginServices ps = PluginServices.getPluginServices(this);
		if (ps != null) {
			String baseDir = ps.getClassLoader().getBaseDir();
			jButUpdate.setIcon(new ImageIcon(baseDir + File.separator
					+ "images" + File.separator + "icons16" + File.separator
					+ "open.png"));
			jButCancel.setIcon(new ImageIcon(baseDir + File.separator
					+ "images" + File.separator + "icons16" + File.separator
					+ "out.png"));
		}
	}

	/**
	 * 
	 */
	private void setMesages() {
		PluginServices ps = PluginServices.getPluginServices(this);
		if (ps != null) {
			this.jButUpdate.setToolTipText(ps.getText("butloadpattern"));
			this.jButCancel.setText(ps.getText("exit"));
		}
	}

	/**
	 * get window info
	 */
	public WindowInfo getWindowInfo() {
		WindowInfo info = new WindowInfo(WindowInfo.RESIZABLE);
		info.setMinimumSize(this.getMinimumSize());
		info.setWidth(500);
		info.setHeight(200);
		info.setTitle(PluginServices.getText(this, "geocoding_all_results"));
		return info;
	}

	public Object getWindowProfile() {

		return null;
	}

	public void update(Observable observable, Object notification) {
		// observable.equals(getFeatureStore()
		if (notification instanceof FeatureStoreNotification) {
			FeatureStoreNotification fsNotification = (FeatureStoreNotification) notification;
			String type = fsNotification.getType();

			// If there are new, updated or deleted features
			// reload the table data
			if (FeatureStoreNotification.SELECTION_CHANGE.equals(type)) {
				FeatureSet sel = null;
				control.clearGeocodingPoint();
				jButUpdate.setEnabled(false);
				try {
					sel = (FeatureSet) fsNotification.getSource()
							.getSelection();

					if (sel.getSize() > 0) {
						Iterator<Feature> it = sel.iterator(0);
						Feature feat = it.next();
						reloadResults(feat);
					} else {
						jTableResults.setModel(new TableResultsModel(control));
					}
				} catch (DataException e) {
					log.error("ERROR updateing the posible results table", e);
				}

			}

		}
	}

	private void reloadResults(Feature feature) {
		int nres = feature.getInt(GeocodingTags.gID_RESUL);
		loadResults(nres);
	}

	public void setAllStore(FeatureStore allstore) {
		this.allstore = allstore;
	}

	public void mouseClicked(MouseEvent e) {
		// nothing to do

	}

	public void mouseEntered(MouseEvent e) {
		// nothing to do

	}

	public void mouseExited(MouseEvent e) {
		// nothing to do

	}

	public void mousePressed(MouseEvent e) {
		// nothing to do

	}

	public void mouseReleased(MouseEvent e) {
		int row = jTableResults.getSelectedRow();
		TableResultsModel model = (TableResultsModel) jTableResults.getModel();
		if (row != -1) {
			// active update button
			int isselect = (Integer) model.getValueAt(row, 2);
			if (isselect == 1) {
				jButUpdate.setEnabled(false);
			} else {
				jButUpdate.setEnabled(true);
			}

			// draw and zoom to geom

			Point pto = model.getGeometry(row, 4, 5);
			try {
				control.drawPositionsOnView(pto);
				control.zoomToPoint(pto.getX(), pto.getY());
			} catch (Exception e1) {
				log.error("Doing zoom to point", e1);
			}
		}

	}

	/**
	 * 
	 * @param shpstore
	 */
	public void setSelectedStore(DefaultFeatureStore shpstore) {
		this.shpstore = shpstore;
	}

	/**
	 * 
	 */
	private void updateStoreSelectedResults() {
		int rowsel = jTableResults.getSelectedRow();
		if (rowsel >= 0) {
			TableResultsModel model = (TableResultsModel) jTableResults
					.getModel();
			double score = (Double) model.getValueAt(rowsel, 3);
			double x = (Double) model.getValueAt(rowsel, 4);
			double y = (Double) model.getValueAt(rowsel, 5);

			try {				
				shpstore.edit(FeatureStore.MODE_FULLEDIT);
				FeatureSet selfeats = (FeatureSet) shpstore.getSelection();				
				if (selfeats.getSize() > 0) {
					Iterator<Feature> it = selfeats.iterator(0);
					EditableFeature shpfeat = (EditableFeature)it.next().getEditable();
					shpfeat.set(GeocodingTags.gSCORE, score);
					shpfeat.set(GeocodingTags.gX, x);
					shpfeat.set(GeocodingTags.gY, y);
					selfeats.update(shpfeat);
					selfeats.dispose();
				}
				shpstore.finishEditing();
				shpstore.dispose();

			} catch (DataException e) {
				log.error("", e);
			}
		}
	}

	/**
	 * 
	 */
	private void updateStoreAllResults() {
		int rowsel = jTableResults.getSelectedRow();
		if (rowsel >= 0) {
			TableResultsModel model = (TableResultsModel) jTableResults
					.getModel();
			String idres = (String) model.getValueAt(rowsel, 0);

			try {
				allstore.edit(FeatureStore.MODE_FULLEDIT);
				FeatureSet feats = (FeatureSet) allstore.getDataSet();
				if (feats.getSize() > 0) {
					Iterator<Feature> it = feats.iterator(0);
					while (it.hasNext()) {
						EditableFeature efeat = it.next().getEditable();
						String strId = (String)efeat.get(GeocodingTags.gID);
						String strSel = (String)efeat.get(GeocodingTags.gSELECT);
						//selected row change value to one
						if(strId.compareTo(idres)==0){
							efeat.set(GeocodingTags.gSELECT, 1);
							feats.update(efeat);
						}
						// no selected row with selected value zero, nothing to do
						else if(strSel.compareTo("0")==0){
							
						}
						// no selected row with selected value one, change to zero
						else{
							efeat.set(GeocodingTags.gSELECT, 0);
							feats.update(efeat);
						}						
					}					
				}
				feats.dispose();
				allstore.dispose();
				allstore.finishEditing();
				
			} catch (DataException e) {
				log.error("", e);
			}
		}
	}

}
