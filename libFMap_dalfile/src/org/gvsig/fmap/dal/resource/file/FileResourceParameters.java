package org.gvsig.fmap.dal.resource.file;

import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.resource.spi.AbstractResourceParameters;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DelegatedDynObject;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynField;
import org.gvsig.tools.dynobject.DynObjectManager;

public class FileResourceParameters extends AbstractResourceParameters {

    public static final String DYNCLASS_NAME = "FileResourceParameters";

    private static final String DYNFIELDNAME_FILE_NAME = "fileName";

    public FileResourceParameters() {
		this.delegatedDynObject = (DelegatedDynObject) ToolsLocator
				.getDynObjectManager()
				.createDynObject(registerDynClass());
	}

	public FileResourceParameters(Object[] params) {
		this();
		// XXX puede que sobre
        setDynValue(DYNFIELDNAME_FILE_NAME, params[0]);
	}

	public FileResourceParameters(String name) {
		this();
		setDynValue(DYNFIELDNAME_FILE_NAME, name);
	}

	public String getFileName() {
		return (String) this.getDynValue(DYNFIELDNAME_FILE_NAME);
	}

	public FileResourceParameters setFileName(String fileName) {
		this.setDynValue(DYNFIELDNAME_FILE_NAME, fileName);
		return this;
	}

	public String getTypeName() {
		return FileResource.NAME;
	}

    private DynClass registerDynClass() {
        DynObjectManager dynman = ToolsLocator.getDynObjectManager();
        DynClass dynClass = dynman.get(DYNCLASS_NAME);
        DynField field;
        if (dynClass == null) {
            dynClass = dynman.add(DYNCLASS_NAME);

            field = dynClass.addDynField(DYNFIELDNAME_FILE_NAME);
            field.setTheTypeOfAvailableValues(DynField.SINGLE);
            field.setDescription("The name of the file");
            field.setType(DataTypes.STRING);
        }
        return dynClass;
    }
}
