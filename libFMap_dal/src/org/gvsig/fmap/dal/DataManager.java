package org.gvsig.fmap.dal;

import java.util.List;

import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.ProviderNotRegisteredException;
import org.gvsig.fmap.dal.exception.ValidateDataParametersException;
import org.gvsig.fmap.dal.feature.FeatureIndex;
import org.gvsig.fmap.dal.feature.spi.index.FeatureIndexProvider;
import org.gvsig.fmap.dal.resource.ResourceManager;
import org.gvsig.tools.evaluator.Evaluator;

/**
 * There are two top level management roles within DAL: data access and resource management.
 *
 * This class is responsible of the data access management role. It provides ways
 * for registering and instantiating {@link DataServerExplorer}(s), {@link DataStore}(s),
 * {@link Evaluator}(s) and {@link FeatureIndex}(es).
 *
 * @see ResourceManager
 *
 */

public interface DataManager {

	/**
	 * Returns the default DAL's temporary directory
	 *
	 * @return Temporary directory name
	 */
	public String getTemporaryDirectory();

	/*
	 * ====================================================================
	 *
	 * Store related services
	 */

	/**
	 * Creates, initializes and returns an instance of DataStoreParameters given
	 * the name with which their provider is registered.
	 *
	 * @param name
	 *            provider name
	 *
	 * @throws InitializeException
	 *
	 * @throws ProviderNotRegisteredException
	 **/
	public DataStoreParameters createStoreParameters(String name)
			throws InitializeException, ProviderNotRegisteredException;

	/**
	 *
	 * Creates, initializes and returns an instance of DataStore given the
	 * DataStoreParameters.
	 *
	 * @param parameters
	 *            parameters used to instantiate and initialize the DataStore
	 * 
	 * @throws InitializeException
	 *
	 * @throws ProviderNotRegisteredException
	 * @throws ValidateDataParametersException
	 **/
	public DataStore createStore(DataStoreParameters parameters)
			throws InitializeException, ProviderNotRegisteredException,
			ValidateDataParametersException;

	/**
	 * Returns a list of Strings containing the names of all available DataStore
	 * providers.
	 *
	 * @return list of String containing available DataStore provider names
	 */
	public List getStoreProviders();

	/*
	 * ====================================================================
	 *
	 * Explorer related services
	 */
	/**
	 * Returns an instance of {@link DataServerExplorerParameters} corresponding to
	 * the given name.
	 *
	 * @param name
	 *            name of a registered server explorer provider
	 *
	 * @throws InitializeException
	 * 			if parameter initialization causes an error.
	 *
	 * @throws ProviderNotRegisteredException
	 * 			if could not find a provider by the given name.
	 *
	 **/
	public DataServerExplorerParameters createServerExplorerParameters(
			String name)
			throws InitializeException, ProviderNotRegisteredException;

	/**
	 * Returns an instance of {@link DataServerExplorer} given its parameters.
	 *
	 * @param parameters
	 *            parameters used to instantiate and initialize the
	 *            {@link DataServerExplorer}.
	 *
	 * @return an instance of {@link DataServerExplorer}.
	 *
	 * @throws InitializeException
	 * 
	 * @throws ProviderNotRegisteredException
	 * @throws ValidateDataParametersException
	 */
	public DataServerExplorer createServerExplorer(
			DataServerExplorerParameters parameters)
			throws InitializeException, ProviderNotRegisteredException,
			ValidateDataParametersException;

	/**
	 * Returns a list of String containing the names of the available
	 * DataServerExplorer providers.
	 *
	 * @return list of String containing the names of the available
	 *         DataServerExplorer providers.
	 */
	public List getExplorerProviders();

	/*
	 * ====================================================================
	 *
	 * Expression evaluation related services
	 */

	/**
	 * Registers the default expression evaluator. It is used by DAL to evaluate
	 * and resolve query filters and expressions.
	 *
	 * @param evaluator
	 *            Class that will be called to evaluate the expression. It must
	 *            implement {@link Evaluator}.
	 */
	public void registerDefaultEvaluator(Class evaluator);

	/**
	 * Creates an instance of Evaluator that represents the given expression.
	 *
	 * @param expression
	 *            String containing a CQL expression.
	 * @return instance of Evaluator representing the given expression.
	 * @throws InitializeException
	 */
	public Evaluator createExpresion(String expression)
			throws InitializeException;

	/*
	 * ====================================================================
	 *
	 * Index related services
	 */


	/**
	 * Returns a list of String containing the names of the available index providers.
	 *
	 * @return
	 * 		list of strings with the names of the available index providers
	 */
	public List getFeatureIndexProviders();

	/**
	 * Sets the default DataIndexProvider for the given data type.
	 *
	 * @param dataType
	 * 				one of the data types defined in {@link DataTypes}.
	 * @param name
	 * 			Provider's name
	 */
    public void setDefaultFeatureIndexProviderName(int dataType, String name);

	/**
	 * Returns the default DataIndexProvider name, given a data type. Data types
	 * are defined in {@link DataTypes}.
	 *
	 * @param dataType
	 *            one of the constants in {@link DataTypes}.
	 *
	 * @return
	 * 		the name of the default {@link FeatureIndexProvider} if there is
	 * 		anyone available for the given data type.
	 */
    public String getDefaultFeatureIndexProviderName(int dataType);


}