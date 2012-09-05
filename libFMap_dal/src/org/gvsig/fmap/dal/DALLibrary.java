package org.gvsig.fmap.dal;

import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.impl.DefaultDataManager;
import org.gvsig.fmap.dal.resource.ResourceManager;
import org.gvsig.fmap.dal.resource.impl.DefaultResourceManager;
import org.gvsig.tools.ToolsLibrary;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynField;
import org.gvsig.tools.dynobject.DynObjectManager;
import org.gvsig.tools.locator.BaseLibrary;
import org.gvsig.tools.locator.ReferenceNotRegisteredException;

/**
 * Initializes gvSIG's desktop DAL by registering the default implementation for {@link DataManager}
 * and {@link ResourceManager}.
 *
 */
public class DALLibrary extends BaseLibrary {

    public void initialize() throws ReferenceNotRegisteredException {
        super.initialize();
		ToolsLibrary toolsLib = new ToolsLibrary();

		toolsLib.initialize();
		toolsLib.postInitialize();

		DALLocator.registerDefaultDataManager(DefaultDataManager.class);
		DALLocator.registerResourceManager(DefaultResourceManager.class);

		DynObjectManager dynManager = ToolsLocator.getDynObjectManager();

		DynField field;
		DynClass dataStoreDynClass = dynManager.get(DataStore.DYNCLASS_NAME);

		if (dataStoreDynClass == null) {
			dataStoreDynClass = dynManager.add(DataStore.DYNCLASS_NAME);

			field = dataStoreDynClass.addDynField("DefaultSRS");
			field.setType(DataTypes.SRS);
			field.setDescription("Spatial Referencing System");

			field = dataStoreDynClass.addDynField("Envelope");
			field.setType(DataTypes.OBJECT); // FIXME ???
			field.setDescription("Envelope");
		}

		DynClass featureStoreDynClass = dynManager
				.get(FeatureStore.DYNCLASS_NAME);

		if (featureStoreDynClass == null) {
			featureStoreDynClass = dynManager.add(FeatureStore.DYNCLASS_NAME);

			featureStoreDynClass.extend(dataStoreDynClass);

			//			XXX ???
			//			field = featureStoreDynClass.addDynField("nativeGeometrySupported");
			//			field.setType(DataTypes.STRING);
			//			field.setDescription("Goemtry data type is supported natively");
		}

	}

	public void postInitialize() {
		super.postInitialize();

		// Validate there is any implementation registered.
		DataManager dataManager = DALLocator.getDataManager();
		if (dataManager == null) {
			throw new ReferenceNotRegisteredException(
					DALLocator.DATA_MANAGER_NAME, DALLocator.getInstance());
		}

		ResourceManager resourceManager = DALLocator.getResourceManager();
		if (resourceManager == null) {
			throw new ReferenceNotRegisteredException(
					DALLocator.RESOURCE_MANAGER_NAME, DALLocator.getInstance());
		}
	}

}
