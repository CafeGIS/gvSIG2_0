/*
 * Created on 22-jun-2005
 *
 * gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
package com.iver.gvsig.datalocator.gui;

import java.awt.FlowLayout;
import java.awt.event.ItemListener;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;
import java.util.prefs.Preferences;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureSelection;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.layers.CancelationException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.mapcontext.layers.LayerCollectionEvent;
import org.gvsig.fmap.mapcontext.layers.LayerCollectionListener;
import org.gvsig.fmap.mapcontext.layers.LayerPositionEvent;
import org.gvsig.fmap.mapcontext.layers.LayersIterator;
import org.gvsig.fmap.mapcontext.layers.operations.LayerCollection;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.fmap.mapcontext.rendering.strategies.SelectedZoomVisitor;
import org.gvsig.gui.beans.swing.JButton;
import org.gvsig.tools.evaluator.Evaluator;
import org.gvsig.tools.evaluator.EvaluatorData;
import org.gvsig.tools.evaluator.EvaluatorException;
import org.gvsig.tools.evaluator.EvaluatorFieldsInfo;
import org.gvsig.tools.exception.BaseException;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.IWindowListener;
import com.iver.andami.ui.mdiManager.SingletonWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.gvsig.datalocator.DataLocatorExtension;

/**
 * @author jmorell
 */
public class DataSelectionPanel extends JPanel implements IWindow, IWindowListener, SingletonWindow {

	private static final long serialVersionUID = 1L;
    private JComboBox jComboBox = null;
	private JLabel jLabel = null;  //  @jve:decl-index=0:visual-constraint="597,16"
	private JLabel jLabel1 = null;  //  @jve:decl-index=0:visual-constraint="873,44"
	private JComboBox jComboBox1 = null;
	private JLabel jLabel2 = null;  //  @jve:decl-index=0:visual-constraint="847,16"
	private JComboBox jComboBox2 = null;
    private WindowInfo viewInfo = null;
	private JButton jButton = null;
	private JButton jButton1 = null;
	private FLayer layerToZoom = null;
	private int fieldToZoomIndex = 0;
	private Object itemToZoom = null;
	private MapContext mapContext = null;
	private Preferences prefUsuario = null;
	private Vector<LayersListener> layersListenerList = new Vector<LayersListener>();
    private Vector<FLayer> vectorialLayers = null;
	private JCheckBox jChkBoxOpenFirstTime = null;
	private JPanel jPanelButtons = null;

	/**
	 * This method initializes
	 *
	 */
	public DataSelectionPanel(MapContext mapContext) {
		super();
		this.mapContext = mapContext;
		prefUsuario = Preferences.userRoot();
		initializeVectorialLayers();
        initializeLayerToZoom();
		initializeFieldToZoomIndex();
        initialize();

        int userOpen = prefUsuario.getInt("gvSIG.DataLocator.open_first_time", -1);
        if (userOpen == 1)
        	getJChkBoxOpenFirstTime().setSelected(true);
	}
    private void initializeVectorialLayers() {
    	unregisterLayersListener();
        vectorialLayers = new Vector<FLayer>();
        LayersIterator iter = DataLocatorExtension.newValidLayersIterator(mapContext.getLayers());


        while (iter.hasNext()) {
        	vectorialLayers.add(iter.nextLayer());
        }

        registerLayersListener();
    }
	private void initializeLayerToZoom() {
		String layerName = prefUsuario.get("LAYERNAME_FOR_DATA_LOCATION", "");
        if (layerName.equals("")) layerToZoom = (FLayer)vectorialLayers.get(0);
        boolean layerFound = false;
        for (int i=0;i<vectorialLayers.size();i++) {
            if (((FLayer)vectorialLayers.get(i)).getName().equals(layerName)) {
                layerFound = true;
                layerToZoom = (FLayer)vectorialLayers.get(i);
                break;
            }
        }
        if (!layerFound) layerToZoom = (FLayer)vectorialLayers.get(0);
        prefUsuario.put("LAYERNAME_FOR_DATA_LOCATION", layerToZoom.getName());

	}
	private void initializeFieldToZoomIndex() {
        fieldToZoomIndex = prefUsuario.getInt("FIELDINDEX_FOR_DATA_LOCATION", 0);
		FLyrVect lyr = (FLyrVect)layerToZoom;
		FeatureStore featureStore;
        try {
            featureStore = lyr.getFeatureStore();
            if (fieldToZoomIndex > (featureStore.getDefaultFeatureType().size()-1)) {
            	fieldToZoomIndex = 0;
            } else if (featureStore.getDefaultFeatureType().size() == 0) {
            	fieldToZoomIndex = -1;
            }
        } catch (DataException e) {
			fieldToZoomIndex = -1;
            e.printStackTrace();
		}
	}
	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
        jLabel2 = new JLabel();
        jLabel1 = new JLabel();
        jLabel = new JLabel();
        this.setLayout(null);
        this.setSize(350, 161);
        jLabel.setBounds(6, 6, 80, 23);
        jLabel.setText(PluginServices.getText(this,"Capa") + ":");
        jLabel1.setBounds(6, 34, 80, 23);
        jLabel1.setText(PluginServices.getText(this,"Campo") + ":");
        jLabel2.setBounds(6, 61, 80, 23);
        jLabel2.setText(PluginServices.getText(this,"Valor") + ":");
        this.add(getJComboBox(), null);
        this.add(jLabel, null);
        this.add(jLabel1, null);
        this.add(getJComboBox1(), null);
        this.add(jLabel2, null);
        this.add(getJComboBox2(), null);

        jPanelButtons = new JPanel();
        FlowLayout flowLayor = new FlowLayout(FlowLayout.RIGHT);
        flowLayor.setHgap(5);

        jPanelButtons.setLayout(flowLayor);
        jPanelButtons.setBounds(15,121,335,35);
        jPanelButtons.add(getJButton(), null);
        jPanelButtons.add(getJButton1(), null);

        this.add(jPanelButtons);

        this.add(getJChkBoxOpenFirstTime(), null);


	}
	private void registerLayersListener() {
		int i,j;
		FLayer layer;
		LayersListener listener;
		boolean found;
		for (i=0;i< vectorialLayers.size();i++) {
			found = false;
			layer = ((FLayer)vectorialLayers.get(i));
			for (j=0;j < layersListenerList.size(); j++) {
				listener = (LayersListener)layersListenerList.get(j);
				if ( layer.getParentLayer() == listener.getLayerCollection()) {
					found = true;
					break;
				}
			}
			if (!found) {
				listener = new LayersListener(layer.getParentLayer());
				layer.getParentLayer().addLayerCollectionListener(listener);
				layersListenerList.add(listener);
			}

		}
	}


	private void unregisterLayersListener() {
		int i;
		LayersListener listener;
		for (i=0;i<layersListenerList.size();i++) {
			listener = (LayersListener)layersListenerList.get(i);
			listener.getLayerCollection().removeLayerCollectionListener(listener);
		}
	}

	private String[] getLayerNames() {
		String[] layerNames = new String[vectorialLayers.size()];
	    for (int i=0;i<vectorialLayers.size();i++) {
	        layerNames[i] = ((FLayer)vectorialLayers.get(i)).getName();
	    }
	    return layerNames;
	}
	private String[] getFieldNames() {
		FLyrVect lyr = (FLyrVect)layerToZoom;
		FeatureStore featureStore;
        String[] fieldNames = null;
		try {
            featureStore = lyr.getFeatureStore();
			fieldNames = new String[featureStore.getDefaultFeatureType().size()];
			for (int i = 0; i < featureStore.getDefaultFeatureType().size(); i++) {
				fieldNames[i] = ((FeatureAttributeDescriptor)featureStore.getDefaultFeatureType().get(i)).getName();
			}
        } catch (DataException e) {
			e.printStackTrace();
		}
		return fieldNames;
	}


	private Object[] getNewValues() {
		FLyrVect lyr = (FLyrVect)layerToZoom;
		FeatureStore featureStore;
		Object[] newValues = null;
		if (fieldToZoomIndex < 0)
			return null;
		try {
			featureStore = lyr.getFeatureStore();

//		String sql = "select " + ((FeatureAttributeDescriptor)featureStore.getDefaultFeatureType().get(fieldToZoomIndex)).getName() + " from " + featureStore.getName() + " where " + featureStore.getFieldName(fieldToZoomIndex) + " is not null;";
		FeatureQuery query=featureStore.createFeatureQuery();
		String field=((FeatureAttributeDescriptor)featureStore.getDefaultFeatureType().get(fieldToZoomIndex)).getName();
		Evaluator myEvaluator=new MyEvaluator(field);
		query.setFilter(myEvaluator);
		query.setAttributeNames(new String[]{field});
		FeatureSet set = featureStore.getFeatureSet(query);
//		ds = ds.getDataSourceFactory().executeSQL(sql, DataSourceFactory.AUTOMATIC_OPENING);

		//Quitar los nombres repetidos y ordenarlos
		TreeSet<Object> treeSet = new TreeSet<Object>(new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				if (o1 instanceof Number && o2 instanceof Number){
					if (((Number)o1).doubleValue()<((Number)o2).doubleValue())
						return -1;
					if (((Number)o1).doubleValue()>((Number)o2).doubleValue())
						return 1;
				}else if (o1 instanceof String && o2 instanceof String){
					return ((String)o1).compareTo((String)o2);
				}else if (o1 instanceof Date && o2 instanceof Date){
					return ((Date)o1).compareTo((Date)o2);
				}
				return 0;
			}
		});
		Iterator<Feature> features=set.iterator();
		while (features.hasNext()) {
			Feature feature = (Feature) features.next();
			Object obj=feature.get(0);
			treeSet.add(obj);
		}
		newValues = (Object[])treeSet.toArray(new Object[0]);
		} catch (DataException e) {
			e.printStackTrace();
		}
		return newValues;
	}
	private class LayersListener implements LayerCollectionListener {
		private LayerCollection theLayerCollection;
	    public LayersListener(FLayers layers){
	    	theLayerCollection = layers;
	    }

	    public LayerCollection getLayerCollection() {
	    	return theLayerCollection;
	    }
        /* (non-Javadoc)
         * @see com.iver.cit.gvsig.fmap.layers.LayerCollectionListener#layerAdded(com.iver.cit.gvsig.fmap.layers.LayerCollectionEvent)
         */
        public void layerAdded(LayerCollectionEvent e) {
            initializeVectorialLayers();
            ((ChangeLayerToZoomItemListener)jComboBox.getItemListeners()[0]).setLayers(vectorialLayers);
            jComboBox.removeAllItems();
		    DefaultComboBoxModel defaultModel = new DefaultComboBoxModel(getLayerNames());
		    jComboBox.setModel(defaultModel);
			jComboBox.setSelectedItem(layerToZoom.getName());
        }
        /* (non-Javadoc)
         * @see com.iver.cit.gvsig.fmap.layers.LayerCollectionListener#layerRemoved(com.iver.cit.gvsig.fmap.layers.LayerCollectionEvent)
         */
        public void layerRemoved(LayerCollectionEvent e) {
            initializeVectorialLayers();
            ((ChangeLayerToZoomItemListener)jComboBox.getItemListeners()[0]).setLayers(vectorialLayers);
		    if (vectorialLayers.size()>0) {
	            jComboBox.removeAllItems();
				String[] layerNames = new String[vectorialLayers.size()];
			    boolean currentLayerRemoved = true;
				for (int i=0;i<vectorialLayers.size();i++) {
			        layerNames[i] = ((FLayer)vectorialLayers.get(i)).getName();
			        if (layerToZoom.getName().equals(layerNames[i])) currentLayerRemoved = false;
			    }
			    DefaultComboBoxModel defaultModel = new DefaultComboBoxModel(layerNames);
			    jComboBox.setModel(defaultModel);
			    if (currentLayerRemoved) {
			        layerToZoom = ((FLayer)vectorialLayers.get(0));
	    			defaultModel = new DefaultComboBoxModel(getFieldNames());
	    		    jComboBox1.setModel(defaultModel);
	    		    fieldToZoomIndex = 0;
	    		    jComboBox1.setSelectedIndex(fieldToZoomIndex);
	    		    Object[] values =getNewValues();
	    			defaultModel = new DefaultComboBoxModel(values);
	    			jComboBox2.setModel(defaultModel);
	    			if (values != null) {
	    				itemToZoom = getNewValues()[0];
	    				jComboBox2.setSelectedItem(itemToZoom);
	    			}else {
	    				itemToZoom = null;
	    			}

			    }
			    jComboBox.setSelectedItem(layerToZoom.getName());
		    }else {
            	if (PluginServices.getMainFrame() == null)
            		((JDialog) (getParent().getParent().getParent().getParent())).dispose();
            	else
            		PluginServices.getMDIManager().closeWindow(DataSelectionPanel.this);
		    }
        }
        /* (non-Javadoc)
         * @see com.iver.cit.gvsig.fmap.layers.LayerCollectionListener#layerMoved(com.iver.cit.gvsig.fmap.layers.LayerPositionEvent)
         */
        public void layerMoved(LayerPositionEvent e) {
            // TODO Auto-generated method stub

        }
        /* (non-Javadoc)
         * @see com.iver.cit.gvsig.fmap.layers.LayerCollectionListener#layerAdding(com.iver.cit.gvsig.fmap.layers.LayerCollectionEvent)
         */
        public void layerAdding(LayerCollectionEvent e) throws CancelationException {
            // TODO Auto-generated method stub

        }
        /* (non-Javadoc)
         * @see com.iver.cit.gvsig.fmap.layers.LayerCollectionListener#layerMoving(com.iver.cit.gvsig.fmap.layers.LayerPositionEvent)
         */
        public void layerMoving(LayerPositionEvent e) throws CancelationException {
            // TODO Auto-generated method stub

        }
        /* (non-Javadoc)
         * @see com.iver.cit.gvsig.fmap.layers.LayerCollectionListener#layerRemoving(com.iver.cit.gvsig.fmap.layers.LayerCollectionEvent)
         */
        public void layerRemoving(LayerCollectionEvent e) throws CancelationException {
            // TODO Auto-generated method stub

        }
        /* (non-Javadoc)
         * @see com.iver.cit.gvsig.fmap.layers.LayerCollectionListener#activationChanged(com.iver.cit.gvsig.fmap.layers.LayerCollectionEvent)
         */
        public void activationChanged(LayerCollectionEvent e) throws CancelationException {
            // TODO Auto-generated method stub

        }
        /* (non-Javadoc)
         * @see com.iver.cit.gvsig.fmap.layers.LayerCollectionListener#visibilityChanged(com.iver.cit.gvsig.fmap.layers.LayerCollectionEvent)
         */
        public void visibilityChanged(LayerCollectionEvent e) throws CancelationException {
            // TODO Auto-generated method stub

        }
	}
    /* (non-Javadoc)
     * @see com.iver.andami.ui.mdiManager.View#getViewInfo()
     */
    public WindowInfo getWindowInfo() {
        if (viewInfo == null) {
            viewInfo=new WindowInfo(WindowInfo.MODELESSDIALOG | WindowInfo.PALETTE);
            viewInfo.setTitle(PluginServices.getText(this,"Localizador_por_atributo"));
            viewInfo.setHeight(getPreferredSize().height);
            viewInfo.setWidth(getPreferredSize().width);
        }
        return viewInfo;
    }
	/**
	 * This method initializes jComboBox
	 *
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getJComboBox() {
		if (jComboBox == null) {
			jComboBox = new JComboBox();
            DefaultComboBoxModel defaultModel = new DefaultComboBoxModel(getLayerNames());
            jComboBox.setModel(defaultModel);
			jComboBox.setBounds(90, 6, 250, 23);
			jComboBox.setSelectedItem(layerToZoom.getName());
			ChangeLayerToZoomItemListener changeLayerToZoomItemListener = new ChangeLayerToZoomItemListener(vectorialLayers);
			jComboBox.addItemListener(changeLayerToZoomItemListener);
		}
		return jComboBox;
	}
	private class ChangeLayerToZoomItemListener implements ItemListener {
	    private Vector<FLayer> layers;
	    public ChangeLayerToZoomItemListener(Vector<FLayer> layers) {
	        this.layers = layers;
	    }
		public void itemStateChanged(java.awt.event.ItemEvent e) {
		    if (jComboBox.getItemCount()>0) {
                for (int i=0;i<layers.size();i++) {
                    if (((FLayer)layers.get(i)).getName().equals((String)jComboBox.getSelectedItem())) {
                        layerToZoom = (FLayer)layers.get(i);
                        break;
                    }
                }
    			fieldToZoomIndex = 0;
				prefUsuario.put("LAYERNAME_FOR_DATA_LOCATION", (String)jComboBox.getSelectedItem());
    		    DefaultComboBoxModel defaultModel = new DefaultComboBoxModel(getFieldNames());
    		    jComboBox1.setModel(defaultModel);
    		    Object[] values =getNewValues();
    		    defaultModel = new DefaultComboBoxModel(values);
    			jComboBox2.setModel(defaultModel);
    			if ( values != null) {
    				jComboBox2.setSelectedIndex(0);
    				itemToZoom = values[0];
    			} else {
    				itemToZoom = null;
    			}
		    }
		}
        /**
         * @param layers The layers to set.
         */
        public void setLayers(Vector<FLayer> layers) {
            this.layers = layers;
        }
	}
	/**
	 * This method initializes jComboBox1
	 *
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getJComboBox1() {
		if (jComboBox1 == null) {
			jComboBox1 = new JComboBox();
            DefaultComboBoxModel defaultModel = new DefaultComboBoxModel(getFieldNames());
            jComboBox1.setModel(defaultModel);
			jComboBox1.setBounds(90, 34, 250, 23);
			jComboBox1.setSelectedIndex(fieldToZoomIndex);
			ChangeFieldItemListener changeFieldItemListener = new ChangeFieldItemListener(vectorialLayers);
			jComboBox1.addItemListener(changeFieldItemListener);
		}
		return jComboBox1;
	}
	private class ChangeFieldItemListener implements ItemListener {
		public ChangeFieldItemListener(Vector<FLayer> layers) {
		}
		public void itemStateChanged(java.awt.event.ItemEvent itemEvent) {
			String fieldToZoom = ((String)jComboBox1.getSelectedItem());
			FLyrVect lyr = (FLyrVect)layerToZoom;
			FeatureStore featureStore;
			try {
				featureStore = lyr.getFeatureStore();
				fieldToZoomIndex = featureStore.getDefaultFeatureType().getIndex(fieldToZoom);
				prefUsuario.putInt("FIELDINDEX_FOR_DATA_LOCATION", fieldToZoomIndex);

				//Quitar los nombres repetidos y ordenarlos
				TreeSet<Object> treeSet = new TreeSet<Object>(new Comparator<Object>() {
					public int compare(Object o1, Object o2) {
						if (o1 instanceof Number && o2 instanceof Number){
							if (((Number)o1).doubleValue()<((Number)o2).doubleValue())
								return -1;
							if (((Number)o1).doubleValue()>((Number)o2).doubleValue())
								return 1;
						}else if (o1 instanceof String && o2 instanceof String){
							return ((String)o1).compareTo((String)o2);
						}else if (o1 instanceof Date && o2 instanceof Date){
							return ((Date)o1).compareTo((Date)o2);
						}
						return 0;
					}
				});

				Evaluator myEvaluator=new MyEvaluator(fieldToZoom);
				FeatureQuery query=featureStore.createFeatureQuery();
				query.setFilter(myEvaluator);
				FeatureSet set=featureStore.getFeatureSet(query);
				Iterator<Feature> features=set.iterator();
				while (features.hasNext()) {
					Feature feature = (Feature) features.next();
					treeSet.add(feature.get(fieldToZoomIndex));
				}
				Object[] newValues = (Object[])treeSet.toArray(new Object[0]);
				DefaultComboBoxModel defaultModel = new DefaultComboBoxModel(newValues);
				jComboBox2.setModel(defaultModel);
				if (newValues.length>0) jComboBox2.setSelectedIndex(0);
				if (newValues.length>0) {
					itemToZoom = newValues[0];
				} else {
					itemToZoom = null;
				}
			} catch (DataException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * This method initializes jComboBox2
	 *
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getJComboBox2() {
		if (jComboBox2 == null) {
			jComboBox2 = new JComboBox();
            DefaultComboBoxModel defaultModel = new DefaultComboBoxModel(getNewValues());
            jComboBox2.setModel(defaultModel);
			jComboBox2.setSelectedIndex(-1);
			jComboBox2.setBounds(90, 61, 250, 23);
			ChangeItemToZoomItemListener changeItemToZoomItemListener = new ChangeItemToZoomItemListener(vectorialLayers);
			jComboBox2.addItemListener(changeItemToZoomItemListener);
		}
		return jComboBox2;
	}
	private class ChangeItemToZoomItemListener implements ItemListener {
	    private Vector<FLayer> layers;
	    public ChangeItemToZoomItemListener(Vector<FLayer> layers) {
	        this.layers = layers;
	    }
		public void itemStateChanged(java.awt.event.ItemEvent e) {
            for (int i=0;i<layers.size();i++) {
                if (((FLayer)layers.get(i)).getName().equals((String)jComboBox.getSelectedItem())) {
                    layerToZoom = (FLayer)layers.get(i);
                    break;
                }
            }
			itemToZoom = ((Object)jComboBox2.getSelectedItem());
		}
	}
	/**
	 * This method initializes jButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			//jButton.setBounds(8, 121, 128, 23);
			jButton.setText(PluginServices.getText(this,"Zoom"));
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (layerToZoom == null || fieldToZoomIndex < 0 || itemToZoom == null)
						return;
				    if (jComboBox2.getSelectedIndex()!=-1) {
						FLyrVect lyr = (FLyrVect)layerToZoom;
						FeatureStore featureStore;
						try {
			                featureStore = lyr.getFeatureStore();
							FeatureSelection selection = (FeatureSelection)featureStore.createFeatureSelection();
							Iterator<Feature> features = featureStore.getFeatureSet().iterator();
							while (features.hasNext()) {
								Feature feature = (Feature) features.next();
								if (itemToZoom.equals(feature.get(fieldToZoomIndex))){
									selection.select(feature);
								}
							}
//							featureStore.setSelection(selection);
							SelectedZoomVisitor visitor = new SelectedZoomVisitor();
	                        selection.accept(visitor);
							mapContext.getViewPort().setEnvelope(visitor.getSelectBound());

			            } catch (DataException e1) {
							e1.printStackTrace();
						} catch (BaseException e1) {
							e1.printStackTrace();
						}
                    } else if (itemToZoom == null) {
                        System.out.println("Localizador por atributo: El campo valor debe tener elementos no nulos para hacer el Zoom.");
                    } else {
				        System.out.println("Localizador por atributo: El campo valor debe estar inicializado antes de hacer Zoom.");
				    }
				}
			});
		}
		return jButton;
	}
	/**
	 * This method initializes jButton1
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJButton1() {
		if (jButton1 == null) {
			jButton1 = new JButton();
			//jButton1.setBounds(141, 121, 128, 23);
			jButton1.setText(PluginServices.getText(this,"Salir"));
			jButton1.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
	            	if (PluginServices.getMainFrame() == null)
	            		((JDialog) (getParent().getParent().getParent().getParent())).dispose();
	            	else
	            		PluginServices.getMDIManager().closeWindow(DataSelectionPanel.this);
				}
			});
		}
		return jButton1;
	}
    /* (non-Javadoc)
     * @see com.iver.andami.ui.mdiManager.ViewListener#viewActivated()
     */
    public void windowActivated() {
        // TODO Auto-generated method stub

    }
    /* (non-Javadoc)
     * @see com.iver.andami.ui.mdiManager.ViewListener#viewClosed()
     */
    public void windowClosed() {
		unregisterLayersListener();
		// Guardamos la posición en las preferencias del usuario.
		WindowInfo vi = PluginServices.getMDIManager().getWindowInfo(this);
		prefUsuario.putInt("gvSIG.DataLocator.x", vi.getX());
		prefUsuario.putInt("gvSIG.DataLocator.y", vi.getY());
		prefUsuario.putInt("gvSIG.DataLocator.w", vi.getWidth());
		prefUsuario.putInt("gvSIG.DataLocator.h", vi.getHeight());
		vi.setClosed(true);
    }
    /* (non-Javadoc)
     * @see com.iver.andami.ui.mdiManager.SingletonView#getViewModel()
     */
    public Object getWindowModel() {
        // Debe devolver una cadena. Mirar Console del CorePlugin
        return "DataSelectionPanel";
    }
	/**
	 * This method initializes jChkBoxOpenFirstTime
	 *
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getJChkBoxOpenFirstTime() {
		if (jChkBoxOpenFirstTime == null) {
			jChkBoxOpenFirstTime = new JCheckBox();
			jChkBoxOpenFirstTime.setBounds(new java.awt.Rectangle(90,89,179,23));
			jChkBoxOpenFirstTime.setText(PluginServices.getText(this, "open_first_time"));
			jChkBoxOpenFirstTime.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
					if (jChkBoxOpenFirstTime.isSelected())
					{
						prefUsuario.putInt("gvSIG.DataLocator.open_first_time",1);
					}
					else
					{
						prefUsuario.putInt("gvSIG.DataLocator.open_first_time",0);
					}
				}
			});
		}
		return jChkBoxOpenFirstTime;
	}
	public Object getWindowProfile() {
		return WindowInfo.TOOL_PROFILE;
	}
	class MyEvaluator implements Evaluator{
		private String name=null;
		private EvaluatorFieldsInfo info = null;

		public MyEvaluator(String name){
			this.name=name;
			info = new EvaluatorFieldsInfo();
			this.info.addFieldValue(name);
		}

		public Object evaluate(EvaluatorData data)
				throws EvaluatorException {
			Feature feature = (Feature) data.getContextValue("feature");
			if (feature.get(name)!=null)
				return new Boolean(true);
			return new Boolean(false);
		}

		public String getCQL() {
			return name + " is not null;";
		}

		public String getDescription() {
			return "Evaluates if a field is not null";
		}

		public EvaluatorFieldsInfo getFieldsInfo() {
			return this.info;
		}

		public String getName() {
			return this.getClass().getName();
		}

    }
}  //  @jve:decl-index=0:visual-constraint="10,10"
