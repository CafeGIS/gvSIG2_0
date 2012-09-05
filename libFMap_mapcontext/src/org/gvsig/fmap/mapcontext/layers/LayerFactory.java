package org.gvsig.fmap.mapcontext.layers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.crs.CRSFactory;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataStore;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.ProviderNotRegisteredException;
import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.mapcontext.layers.operations.SingleLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Crea un adaptador del driver que se le pasa como parámetro en los métodos
 * createLayer. Si hay memoria suficiente se crea un FLyrMemory que pasa todas
 * las features del driver a memoria
 */
public class LayerFactory {
	final static private Logger logger = LoggerFactory.getLogger(LayerFactory.class);

	private static LayerFactory instance = null;



	public static LayerFactory getInstance() {
		if (instance == null) {
			instance = new LayerFactory();
		}
		return instance;
	}

	/**
	 * Guarda registro de que clase de capa debe usar para un determinado Store
	 *
	 * como clave usamos el nombre de registro del dataStore
	 */
	private Map layersToUseForStore = new HashMap();

	/**
	 * Registra que clase tiene que usar para un {@link DataStore} determinado. <br>
	 * Por defecto, si el
	 *
	 *
	 * @param dataStoreName
	 *            Nombre de registro del {@link DataStore} dentro del
	 *            {@link DataManager}
	 * @param layerClassToUse
	 *            clase que implementa {@link SingleLayer}
	 * @return
	 */

	public boolean registerLayerToUseForStore(String dataStoreName,
			Class layerClassToUse) {

		DataManager dm = DALLocator.getDataManager();
		DataStoreParameters dsparams;
		try {
			dsparams = dm
			.createStoreParameters(dataStoreName);
		} catch (InitializeException e) {
			e.printStackTrace();
			return false;
		} catch (ProviderNotRegisteredException e) {
			e.printStackTrace();
			return false;
		}
		if (!layerClassToUse.isAssignableFrom(SingleLayer.class)) {
			return false;
		}
		this.layersToUseForStore.put(dsparams.getDataStoreName(),
				layerClassToUse);

		return true;
	}

	public boolean registerLayerToUseForStore(Class storeClass,
			Class layerClassToUse) {

		DataManager dm = DALLocator.getDataManager();
		if (!DataStore.class.isAssignableFrom(storeClass)) {
			return false;
		}

		if (!SingleLayer.class.isAssignableFrom(layerClassToUse)
				|| !FLayer.class.isAssignableFrom(layerClassToUse)) {
			return false;
		}
		this.layersToUseForStore.put(storeClass,
				layerClassToUse);

		return true;
	}

	private Class getLayerClassFor(DataStore store) {
		Class result = (Class) this.layersToUseForStore.get(store.getName());
		if (result == null) {
			Iterator iter = this.layersToUseForStore.entrySet().iterator();
			Map.Entry entry;
			Class key;
			while (iter.hasNext()) {
				entry = (Entry) iter.next();
				if (entry.getKey() instanceof Class) {
					key = (Class) entry.getKey();
					if (key.isAssignableFrom(store.getClass())) {
						result = (Class) entry.getValue();
						break;
					}
				}
			}
		}
		return result;

	}



	/*
	 * TODO Documentation
	 *
	 * @param layerName Nombre de la capa. @param driverName Nombre del driver.
	 *
	 * @param f fichero. @param proj Proyección.
	 *
	 * @return FLayer. @throws DriverException
	 *
	 * @throws DriverException @throws DriverIOException
	 */
	public FLayer createLayer(String layerName,
			DataStoreParameters storeParameters) throws LoadLayerException {
		// Se obtiene el driver que lee
		try{
			DataManager dataManager=DALLocator.getDataManager();
			DataStore dataStore=dataManager.createStore(storeParameters);
			return createLayer(layerName, dataStore);
		}catch (Exception e) {
			throw new LoadLayerException(layerName,e);
		}
	}

	/**
	 * Create a layer form a DataStore.
	 * @param layerName
	 * The name of the layer
	 * @param dataStore
	 * The datastore
	 * @return
	 * The layer to load
	 * @throws LoadLayerException
	 */
	public FLayer createLayer(String layerName, DataStore dataStore) throws LoadLayerException{
		try{	
			Class layerClass = this.getLayerClassFor(dataStore);
			if (layerClass == null) {
				throw new LoadLayerException("No_layer_class_to_use",
						new Exception());
			}
			FLayer layer;
			try {
				layer = (FLayer) layerClass.newInstance();
			} catch (InstantiationException e) {
				throw new LoadLayerException(layerName, e);
			} catch (IllegalAccessException e) {
				throw new LoadLayerException(layerName, e);
			}

			((SingleLayer) layer).setDataStore(dataStore);
			layer.setName(layerName);
			Object srsObj = dataStore.getDynValue("DefaultSRS");
			if (srsObj != null) {
				IProjection proj = null;
				if (srsObj instanceof IProjection) {
					proj = (IProjection) srsObj;
				} else if (srsObj instanceof String) {
					proj = CRSFactory.getCRS((String) srsObj);
				}
				if (proj != null) {
					layer.setProjection(proj);
				}
			}


			return layer;
		} catch (Exception e) {
			throw new LoadLayerException(layerName,e);
		}
	}

}
