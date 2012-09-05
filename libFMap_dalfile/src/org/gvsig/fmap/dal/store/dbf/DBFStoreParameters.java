package org.gvsig.fmap.dal.store.dbf;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemStoreParameters;
import org.gvsig.fmap.dal.spi.AbstractDataParameters;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DelegatedDynObject;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynField;
import org.gvsig.tools.dynobject.DynObjectManager;
import org.gvsig.tools.dynobject.DynObjectValueItem;


public class DBFStoreParameters extends AbstractDataParameters implements
		DataStoreParameters, FilesystemStoreParameters {

    public static final String DYNCLASS_NAME = "DBFStoreParameters";

    protected static final String FIELD_DBFFILENAME = "dbffilename";

	public static final String FIELD_ENCODING = "encoding";

	protected static DynClass DYNCLASS = null;

    protected static void registerDynClass() {
		DynObjectManager dynman = ToolsLocator.getDynObjectManager();
		DynClass dynClass;
		DynField field;
		if (DYNCLASS == null) {
			dynClass = dynman.add(DYNCLASS_NAME);

			field = dynClass.addDynField(FIELD_DBFFILENAME);
			field.setTheTypeOfAvailableValues(DynField.SINGLE);
			field.setDescription("DBF file name");
			field.setType(DataTypes.STRING);
			field.setMandatory(true);
			DYNCLASS = dynClass;


			field = dynClass.addDynField(FIELD_ENCODING);
			field.setTheTypeOfAvailableValues(DynField.CHOICE);
			field.setDescription("Encoding");
			field.setType(DataTypes.STRING);
			field.setMandatory(false);
			field.setDefaultValue(null);


			Set charsetSet = new LinkedHashSet(160);

			charsetSet.add(new DynObjectValueItem(null, "{default}"));
			charsetSet.add(new DynObjectValueItem("US-ASCII"));
			charsetSet.add(new DynObjectValueItem("ISO-8859-1"));
			charsetSet.add(new DynObjectValueItem("ISO-8859-15"));
			charsetSet.add(new DynObjectValueItem("windows-1250"));
			charsetSet.add(new DynObjectValueItem("windows-1252"));
			charsetSet.add(new DynObjectValueItem("UTF-8"));
			charsetSet.add(new DynObjectValueItem("UTF-16"));
			charsetSet.add(new DynObjectValueItem("UTF-16BE"));
			charsetSet.add(new DynObjectValueItem("UTF-16LE"));
			Map charsets = Charset.availableCharsets();
			Iterator iter = charsets.keySet().iterator();
			while (iter.hasNext()){
				charsetSet.add(new DynObjectValueItem(iter.next()));
			}
			field.setAvailableValues((DynObjectValueItem[]) charsetSet
					.toArray(new DynObjectValueItem[0]));



			DYNCLASS = dynClass;

		}

	}

	public DBFStoreParameters() {
		super();
		initialize();
	}

	protected void initialize() {
		this.delegatedDynObject = (DelegatedDynObject) ToolsLocator
				.getDynObjectManager()
				.createDynObject(
						DBFStoreParameters.DYNCLASS);
	}

	public String getDataStoreName() {
		return DBFStoreProvider.NAME;
	}

	public boolean isValid() {
		return (this.getDBFFileName() != null);
	}

	public File getFile() {
		return new File(this.getDBFFileName());
	}

	public void setFile(File aFile) {
		this.setDBFFile(aFile);
	}

	public void setFile(String aFileName) {
		this.setDBFFileName(aFileName);
	}

	public String getDBFFileName() {
		return (String) this.getDynValue(FIELD_DBFFILENAME);
	}

	public File getDBFFile() {
		return new File((String) this.getDynValue(FIELD_DBFFILENAME));
	}

	public void setDBFFile(File aFile) {
		this.setDynValue(FIELD_DBFFILENAME, aFile.getPath());
	}

	public void setDBFFileName(String aFileName) {
		this.setDynValue(FIELD_DBFFILENAME, aFileName);
	}

	public String getDescription() {
		return DBFStoreProvider.DESCRIPTION;
	}

	public String getEncodingName() {
		return (String) getDynValue(FIELD_ENCODING);
	}
	public Charset getEncoding() {
		String name = getEncodingName();
		if (name == null) {
			return null;
		}
		return Charset.forName(name);
	}

	public void setEncoding(String encoding) {
		this.setEncoding(Charset.forName(encoding));
	}

	public void setEncoding(Charset charset) {
		this.setDynValue(FIELD_ENCODING, charset.name());
	}
}
