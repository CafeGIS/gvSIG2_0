package org.gvsig.fmap.dal.impl;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataServerExplorer;
import org.gvsig.fmap.dal.DataServerExplorerParameters;
import org.gvsig.fmap.dal.DataStore;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.ProviderNotRegisteredException;
import org.gvsig.fmap.dal.exception.ValidateDataParametersException;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.impl.DefaultFeatureIndex;
import org.gvsig.fmap.dal.feature.impl.DefaultFeatureStore;
import org.gvsig.fmap.dal.feature.spi.FeatureStoreProvider;
import org.gvsig.fmap.dal.feature.spi.FeatureStoreProviderServices;
import org.gvsig.fmap.dal.feature.spi.index.FeatureIndexProvider;
import org.gvsig.fmap.dal.feature.spi.index.FeatureIndexProviderServices;
import org.gvsig.fmap.dal.raster.impl.DefaultCoverageStore;
import org.gvsig.fmap.dal.raster.spi.CoverageStoreProvider;
import org.gvsig.fmap.dal.resource.ResourceManager;
import org.gvsig.fmap.dal.spi.DataManagerProviderServices;
import org.gvsig.fmap.dal.spi.DataServerExplorerProvider;
import org.gvsig.fmap.dal.spi.DataStoreProvider;
import org.gvsig.fmap.dal.spi.DataStoreProviderServices;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.evaluator.Evaluator;
import org.gvsig.tools.extensionpoint.ExtensionPoint.Extension;
import org.gvsig.tools.operations.OperationManager;

public class DefaultDataManager implements DataManager, DataManagerProviderServices {

	final static private String DATA_MANAGER_STORE = "Data.manager.stores";
	final static private String DATA_MANAGER_STORE_DESCRIPTION = "DAL stores providers";

	final static private String DATA_MANAGER_STORE_PARAMS = "Data.manager.stores.params";
	final static private String DATA_MANAGER_STORE_PARAMS_DESCRIPTION = "DAL stores providers parameters";

	final static private String DATA_MANAGER_EXPLORER = "Data.manager.explorers";
	final static private String DATA_MANAGER_EXPLORER_DESCRIPTION = "DAL explorers providers";

	final static private String DATA_MANAGER_EXPLORER_PARAMS = "Data.manager.explorers.params";
	final static private String DATA_MANAGER_EXPLORER_PARAMS_DESCRIPTION = "DAL explorer providers parameters";

	final static private String DATA_MANAGER_INDEX = "Data.manager.indexes";
	final static private String DATA_MANAGER_INDEX_DESCRIPTION = "DAL index providers";

	final static private String DATA_MANAGER_EXPRESION_EVALUATOR = "Data.manager.expresion.evaluator";
	final static private String DATA_MANAGER_EXPRESION_EVALUATOR_DEFAULT = "default";
	final static private String DATA_MANAGER_EXPRESION_EVALUATOR_DESCRIPTION = "DAL expresion evaluators.";

	/** This map contains the name of the default provider for each data type */
	private Map defaultDataIndexProviders;


	public DefaultDataManager() {
		/*
		 * Create te extensions point in te registry.
		 */
		ToolsLocator.getExtensionPointManager().add(DATA_MANAGER_STORE,
				DATA_MANAGER_STORE_DESCRIPTION);

		ToolsLocator.getExtensionPointManager().add(DATA_MANAGER_STORE_PARAMS,
				DATA_MANAGER_STORE_PARAMS_DESCRIPTION);

		ToolsLocator.getExtensionPointManager().add(DATA_MANAGER_EXPLORER,
				DATA_MANAGER_EXPLORER_DESCRIPTION);

		ToolsLocator.getExtensionPointManager().add(
				DATA_MANAGER_EXPLORER_PARAMS,
				DATA_MANAGER_EXPLORER_PARAMS_DESCRIPTION);

		ToolsLocator.getExtensionPointManager().add(DATA_MANAGER_INDEX,
				DATA_MANAGER_INDEX_DESCRIPTION);

		ToolsLocator.getExtensionPointManager().add(
				DATA_MANAGER_EXPRESION_EVALUATOR,
				DATA_MANAGER_EXPRESION_EVALUATOR_DESCRIPTION);

		initializeIndexes();
	}

	/**
	 *
	 * @return ResourceManager
	 */

	public ResourceManager getResourceManager() {
		return DALLocator.getResourceManager();
	}

	public OperationManager getOperationManager() {
		return ToolsLocator.getOperationManager();
	}

	public String getTemporaryDirectory() {
		// FIXME Define a better tempdir solution
		String tmp = System.getenv("TMP");
		if (tmp == null) {
			tmp = System.getenv("TEMP");
		}
		if (tmp == null) {
			File tmp_file = new File(System.getenv("HOME"), "_daltmp_");
			int i=1;
			while (!tmp_file.exists() || !tmp_file.isDirectory()){
				tmp_file = new File(tmp_file.getAbsolutePath()+i);
				i++;
			}
			if (!tmp_file.exists()){
				tmp_file.mkdir();
			}
			tmp = tmp_file.getAbsolutePath();
		}
		return tmp;
	}

	/*
	 * ====================================================================
	 *
	 * Store related services
	 */
	public void registerStoreProvider(String name,
			Class storeProviderClass,
			Class parametersClass) {
		if (name == null || storeProviderClass == null || parametersClass == null){
			// FIXME Exception
			throw new IllegalArgumentException(
					"Any parameters can be null");
		}

		if (!DataStoreParameters.class.isAssignableFrom(parametersClass)) {
			// FIXME Exception
			throw new IllegalArgumentException(parametersClass.getName()
					+ " must implement org.gvsig.fmap.dal.DataStoreParameters");
		}



		if (CoverageStoreProvider.class.isAssignableFrom(storeProviderClass)) {


		} else if (FeatureStoreProvider.class
				.isAssignableFrom(storeProviderClass)) {


		} else{
			// FIXME Exception
			throw new IllegalArgumentException(
					"Not supported implemtation: name=" + name
							+ " provider class="
							+ storeProviderClass.getName());
		}

		ToolsLocator.getExtensionPointManager().add(DATA_MANAGER_STORE,
				DATA_MANAGER_STORE_DESCRIPTION).append(name, null,
				storeProviderClass);

		ToolsLocator.getExtensionPointManager().add(DATA_MANAGER_STORE_PARAMS,
				DATA_MANAGER_STORE_PARAMS_DESCRIPTION).append(name, null,
				parametersClass);
	}

	public DataStoreParameters createStoreParameters(String name)
			throws InitializeException, ProviderNotRegisteredException {
		try {
			DataStoreParameters params = (DataStoreParameters) ToolsLocator
					.getExtensionPointManager().get(DATA_MANAGER_STORE_PARAMS)
					.create(name);
			if (params == null) {
				throw new ProviderNotRegisteredException(name);
			}
			return params;
		} catch (InstantiationException e) {
			throw new InitializeException(e);
		} catch (IllegalAccessException e) {
			throw new InitializeException(e);
		} catch (SecurityException e) {
			throw new InitializeException(e);
		} catch (IllegalArgumentException e) {
			throw new InitializeException(e);
		}
	}

	public DataStore createStore(DataStoreParameters parameters)
			throws InitializeException, ProviderNotRegisteredException,
			ValidateDataParametersException {
		String name = parameters.getDataStoreName();

		parameters.validate();

		DataStore store;
		Extension storeProviderExtension = ToolsLocator
				.getExtensionPointManager().get(DATA_MANAGER_STORE).get(name);



		if (storeProviderExtension == null) {
			throw new ProviderNotRegisteredException(name);
		}

		Class providerClass = storeProviderExtension.getExtension();
		if (providerClass == null) {
			throw new ProviderNotRegisteredException(name);
		}

		if (CoverageStoreProvider.class.isAssignableFrom(providerClass)) {
			store =  new DefaultCoverageStore();


		} else if (FeatureStoreProvider.class.isAssignableFrom(providerClass)) {

			store = new DefaultFeatureStore();

		} else{
			// FIXME Exception
			throw new InitializeException(new RuntimeException(
					"Not supported implemtation: name=" + name
					+ " provider class="
					+ providerClass.getName()));
		}



		this.intializeDataStore((DataStoreImplementation) store, parameters);

		return store;
	}

	public List getStoreProviders() {
		return ToolsLocator.getExtensionPointManager().get(DATA_MANAGER_STORE)
				.getNames();
	}

	/*
	 * ====================================================================
	 *
	 * Explorer related services
	 */
	public void registerExplorerProvider(String name, Class explorerClass,
			Class parametersClass) {

		if (name == null || explorerClass == null || parametersClass == null) {
			// FIXME Exception
			throw new IllegalArgumentException("Any parameters can be null");
		}


		if (!DataServerExplorerParameters.class
				.isAssignableFrom(parametersClass)) {
			// FIXME Exception
			throw new IllegalArgumentException(
					parametersClass.getName()
							+ " must implement org.gvsig.fmap.dal.DataServerExplorerParameters");
		}

		if (!DataServerExplorer.class.isAssignableFrom(explorerClass)) {
			// FIXME Exception
			throw new IllegalArgumentException(explorerClass.getName()
					+ " must implement org.gvsig.fmap.dal.DataServerExplorer");
		}


		ToolsLocator.getExtensionPointManager().add(DATA_MANAGER_EXPLORER,
				DATA_MANAGER_EXPLORER_DESCRIPTION).append(name, null,
				explorerClass);

		ToolsLocator.getExtensionPointManager().add(
				DATA_MANAGER_EXPLORER_PARAMS,
				DATA_MANAGER_EXPLORER_PARAMS_DESCRIPTION).append(name, null,
				parametersClass);
	}

	public DataServerExplorerParameters createServerExplorerParameters(String name)
			throws InitializeException, ProviderNotRegisteredException {
		try {
			DataServerExplorerParameters params = (DataServerExplorerParameters) ToolsLocator
					.getExtensionPointManager()
					.get(
							DATA_MANAGER_EXPLORER_PARAMS)
					.create(name);
			if (params == null) {
				throw new ProviderNotRegisteredException(name);
			}
			return params;
		} catch (InstantiationException e) {
			throw new InitializeException(e);
		} catch (IllegalAccessException e) {
			throw new InitializeException(e);
		} catch (SecurityException e) {
			throw new InitializeException(e);
		} catch (IllegalArgumentException e) {
			throw new InitializeException(e);
		}
	}

	public DataServerExplorer createServerExplorer(DataServerExplorerParameters parameters)
			throws InitializeException, ProviderNotRegisteredException,
			ValidateDataParametersException {

		parameters.validate();

		String name = parameters.getExplorerName();

		try {
			DataServerExplorerProvider server = (DataServerExplorerProvider) ToolsLocator
					.getExtensionPointManager().get(DATA_MANAGER_EXPLORER)
					.create(
							name, new Object[] { parameters });
			if (server == null) {
				throw new ProviderNotRegisteredException(name);
			}
			server.initialize(new DefaultDataServerExplorerProviderServices());
			return server;
		} catch (InstantiationException e) {
			throw new InitializeException(e);
		} catch (IllegalAccessException e) {
			throw new InitializeException(e);
		} catch (SecurityException e) {
			throw new InitializeException(e);
		} catch (IllegalArgumentException e) {
			throw new InitializeException(e);
		} catch (NoSuchMethodException e) {
			throw new InitializeException(e);
		} catch (InvocationTargetException e) {
			throw new InitializeException(e);
		}
	}

	public List getExplorerProviders() {
		return ToolsLocator.getExtensionPointManager().get(
				DATA_MANAGER_EXPLORER).getNames();
	}


	/*
	 * ====================================================================
	 *
	 * Expresion evaluation related services
	 */
	public Evaluator createExpresion(String expresion)
			throws InitializeException {
		try {
			return (Evaluator) ToolsLocator.getExtensionPointManager().get(
					DATA_MANAGER_EXPRESION_EVALUATOR).create(
					DATA_MANAGER_EXPRESION_EVALUATOR_DEFAULT,
					new Object[] { expresion });
		} catch (SecurityException e) {
			throw new InitializeException(e);
		} catch (IllegalArgumentException e) {
			throw new InitializeException(e);
		} catch (NoSuchMethodException e) {
			throw new InitializeException(e);
		} catch (InstantiationException e) {
			throw new InitializeException(e);
		} catch (IllegalAccessException e) {
			throw new InitializeException(e);
		} catch (InvocationTargetException e) {
			throw new InitializeException(e);
		}
	}

	public void registerDefaultEvaluator(Class evaluatorClass) {
		if (!Evaluator.class.isAssignableFrom(evaluatorClass)) {
			throw new ClassCastException();
		}
		ToolsLocator.getExtensionPointManager().add(
				DATA_MANAGER_EXPRESION_EVALUATOR,
				DATA_MANAGER_EXPRESION_EVALUATOR_DESCRIPTION).append(
				DATA_MANAGER_EXPRESION_EVALUATOR_DEFAULT,
				"Default expresion evaluator for use in DAL", evaluatorClass);
	}

	/*
	 * ====================================================================
	 *
	 * Index related services
	 */


	public List getFeatureIndexProviders() {
		return ToolsLocator.getExtensionPointManager().get(DATA_MANAGER_INDEX)
		.getNames();
	}

	public void setDefaultFeatureIndexProviderName(int dataType, String name) {
		defaultDataIndexProviders.put(new Integer(dataType), name);
	}

	public String getDefaultFeatureIndexProviderName(int dataType) {
		return (String) defaultDataIndexProviders.get(new Integer(dataType));
	}

	public FeatureIndexProviderServices createFeatureIndexProvider(
			String name, FeatureStore store, FeatureType type, String indexName,
			FeatureAttributeDescriptor attr)
			throws InitializeException, ProviderNotRegisteredException {

			if (name == null) {
				name = getDefaultFeatureIndexProviderName(attr.getDataType());
			}

			if (name == null) {
				throw new InitializeException("There not any index provider registered.", null);
			}

			try {
				FeatureIndexProvider provider = (FeatureIndexProvider) ToolsLocator
						.getExtensionPointManager()
						.get(
								DATA_MANAGER_INDEX)
						.create(name);
				if (provider == null) {
					throw new ProviderNotRegisteredException(name);
				}
				FeatureIndexProviderServices services = new DefaultFeatureIndex((FeatureStoreProviderServices) store, type, provider, attr.getName(), indexName);
				services.initialize();
				return services;
			} catch (InstantiationException e) {
				throw new InitializeException(e);
			} catch (IllegalAccessException e) {
				throw new InitializeException(e);
			} catch (SecurityException e) {
				throw new InitializeException(e);
			} catch (IllegalArgumentException e) {
				throw new InitializeException(e);
			}
	}

	public void registerFeatureIndexProvider(String name, String description,
			Class clazz, int dataType) {
		ToolsLocator.getExtensionPointManager().add(DATA_MANAGER_INDEX,
				DATA_MANAGER_INDEX_DESCRIPTION).append(name, null,
				clazz);

		if (getDefaultFeatureIndexProviderName(dataType) == null) {
			setDefaultFeatureIndexProviderName(dataType, name);
		}
	}

	private void initializeIndexes() {
		this.defaultDataIndexProviders = new HashMap();
	}

	public void intializeDataStore(DataStoreImplementation store,
			DataStoreParameters parameters)
			throws InitializeException, ProviderNotRegisteredException {

		store.intializePhase1(this, parameters);
		String name = parameters.getDataStoreName();
		DataStoreProvider provider;
		try {
			provider = (DataStoreProvider) ToolsLocator
					.getExtensionPointManager().get(DATA_MANAGER_STORE).create(
							name,
							new Object[] { parameters,
									(DataStoreProviderServices) store });

		} catch (Exception e) {
			throw new InitializeException(e);
		}
		if (provider == null) {
			throw new ProviderNotRegisteredException(name);
		}
		store.intializePhase2(provider);

	}

}
