package org.gvsig.fmap.dal.serverexplorer.filesystem;

import org.gvsig.fmap.dal.DataServerExplorerParameters;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.spi.AbstractDataParameters;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DelegatedDynObject;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynField;
import org.gvsig.tools.dynobject.DynObjectManager;

public class FilesystemServerExplorerParameters extends AbstractDataParameters
		implements DataServerExplorerParameters {

    public static final String DYNCLASS_NAME = "FilesystemServerExplorerParameters";

    private static final String FIELD_ROOT = "root";

	public FilesystemServerExplorerParameters() {
		this.delegatedDynObject = (DelegatedDynObject) ToolsLocator
				.getDynObjectManager()
				.createDynObject(this.registerDynClass());
	}

	private DynClass registerDynClass() {
	   	DynObjectManager dynman = ToolsLocator.getDynObjectManager();
    	DynClass dynClass = dynman.get(DYNCLASS_NAME);
    	DynField field;
    	if (dynClass == null) {
    		dynClass = dynman.add(DYNCLASS_NAME);

    		field = dynClass.addDynField(FIELD_ROOT);
    		field.setType(DataTypes.STRING);
    		field.setDescription("Root directory path of the explorer");
    		field.setTheTypeOfAvailableValues(DynField.SINGLE);


    		field = dynClass.addDynField("initialpath");
			field.setType(DataTypes.STRING);
			field.setDescription("Initial path of the explorer");
			field.setTheTypeOfAvailableValues(DynField.SINGLE);

    	}
    	return dynClass;
	}

	public void setRoot(String path) {
		this.setDynValue(FIELD_ROOT, path);
	}

	public String getRoot() {
		return (String) this.getDynValue(FIELD_ROOT);
	}

	public void setInitialpath(String path) {
		this.setDynValue("initialpath", path);
	}

	public String getInitialpath() {
		return (String) this.getDynValue("initialpath");
	}

	public String getExplorerName() {
		return FilesystemServerExplorer.NAME;
	}

}
