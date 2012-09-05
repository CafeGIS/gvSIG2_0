package org.gvsig.fmap.data.feature.db;

import org.gvsig.fmap.dal.feature.AttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.exception.IsNotAttributeSettingException;

public class DBAttributeDescriptor extends AttributeDescriptor {
	private boolean autoIncrement;
	private String label;
	private int sqlType;
	private String className;
	private String sqlTypeName;
	private String catalogName;
	private String schemaName;
	private String tableName;
	private boolean caseSensitive;
	private boolean currency;
	private boolean definitelyWritable;
	private boolean searchable;
	private boolean signed;
	private boolean writable = true;

	protected DBAttributeDescriptor(FeatureType featureType) {
		super(featureType);
    }

	protected DBAttributeDescriptor(FeatureType featureType,boolean asNew) {
		super(featureType, asNew);
    }

	public boolean isAutoIncrement() {
		if (useNewAttributeValue())
			return ((DBAttributeDescriptor)newAttributeDescriptor).isAutoIncrement();
		else
			return autoIncrement;
	}

	public void setAutoIncrement(boolean autoIncrement) throws IsNotAttributeSettingException {
		canSetValue();
		if (useNewAttributeValue()){
			newAttributeDescriptor.loading();
			((DBAttributeDescriptor)newAttributeDescriptor).setAutoIncrement(autoIncrement);
			newAttributeDescriptor.stopLoading();
		}else{
			this.autoIncrement = autoIncrement;
		}
	}

	public String getLabel() {
		if (useNewAttributeValue())
			return ((DBAttributeDescriptor)newAttributeDescriptor).getLabel();
		else
			return label;
	}
	public void setLabel(String label) throws IsNotAttributeSettingException {
		canSetValue();
		if (useNewAttributeValue()){
			newAttributeDescriptor.loading();
			((DBAttributeDescriptor)newAttributeDescriptor).setLabel(label);
			newAttributeDescriptor.stopLoading();
		}else{
			this.label = label;
		}
	}

	public FeatureAttributeDescriptor getCopy() {
		DBAttributeDescriptor newFD = (DBAttributeDescriptor)super.getCopy();
		newFD.readOnly = this.readOnly;
		newFD.autoIncrement = this.autoIncrement;
		newFD.label = this.label;
		newFD.sqlType = this.sqlType;
		newFD.className = this.className;
		newFD.sqlTypeName = this.sqlTypeName;
		newFD.schemaName = this.schemaName;
		newFD.tableName = this.tableName;
		newFD.caseSensitive = this.caseSensitive;
		newFD.currency = this.currency;
		newFD.definitelyWritable = this.definitelyWritable;
		newFD.searchable = this.searchable;
		newFD.signed = this.signed;
		newFD.writable = this.writable;
		newFD.catalogName = this.catalogName;

		return newFD;
	}
	public int getSqlType() {
		if (useNewAttributeValue())
			return ((DBAttributeDescriptor)newAttributeDescriptor).getSqlType();
		else
			return this.sqlType;
	}

	public void setSqlType(int sqlType){
		this.sqlType = sqlType;
	}
	public String getClassName() {
		if (useNewAttributeValue())
			return ((DBAttributeDescriptor)newAttributeDescriptor).getClassName();
		else
			return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}

	public String getSqlTypeName() {
		if (useNewAttributeValue())
			return ((DBAttributeDescriptor)newAttributeDescriptor).getSqlTypeName();
		else
			return sqlTypeName;
	}
	public void setSqlTypeName(String sqlTypeName) {
		this.sqlTypeName = sqlTypeName;
	}
	public String getSchemaName() {
		if (useNewAttributeValue())
			return ((DBAttributeDescriptor)newAttributeDescriptor).getSchemaName();
		else
			return schemaName;
	}
	public void setSchemaName(String schemaName) throws IsNotAttributeSettingException {
		if (!editing && !loading)
			throw new IsNotAttributeSettingException("setSchemaName");
		this.schemaName = schemaName;
	}
	public String getTableName() {
		if (useNewAttributeValue())
			return ((DBAttributeDescriptor)newAttributeDescriptor).getTableName();
		else
			return tableName;
	}
	public void setTableName(String tableName) throws IsNotAttributeSettingException {
		if (!editing && !loading)
			throw new IsNotAttributeSettingException("setTableName");
		this.tableName = tableName;
	}
	public boolean isCaseSensitive() {
		if (useNewAttributeValue())
			return ((DBAttributeDescriptor)newAttributeDescriptor).isCaseSensitive();
		else
			return caseSensitive;
	}
	public void setCaseSensitive(boolean caseSensitive) throws IsNotAttributeSettingException {
		if (!editing && !loading)
			throw new IsNotAttributeSettingException("setCaseSensitive");
		this.caseSensitive = caseSensitive;
	}
	public boolean isCurrency() {
		if (useNewAttributeValue())
			return ((DBAttributeDescriptor)newAttributeDescriptor).isCurrency();
		else
			return currency;
	}
	public void setCurrency(boolean currency) throws IsNotAttributeSettingException {
		if (!editing && !loading)
			throw new IsNotAttributeSettingException("setCurrency");
		this.currency = currency;
	}
	public boolean isDefinitelyWritable() {
		if (useNewAttributeValue())
			return ((DBAttributeDescriptor)newAttributeDescriptor).isDefinitelyWritable();
		else
			return definitelyWritable;
	}
	public void setDefinitelyWritable(boolean definitelyWritable) throws IsNotAttributeSettingException {
		if (!editing && !loading)
			throw new IsNotAttributeSettingException("setDefinetelyWritable");
		this.definitelyWritable = definitelyWritable;
	}
	public boolean isSearchable() {
		if (useNewAttributeValue())
			return ((DBAttributeDescriptor)newAttributeDescriptor).isSearchable();
		else
			return searchable;
	}
	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}
	public boolean isSigned() {
		return signed;
	}
	public void setSigned(boolean signed) {
		this.signed = signed;
	}
	public boolean isWritable() {
		return writable;
	}
	public void setWritable(boolean writable) throws IsNotAttributeSettingException {
		if (!editing && !loading)
			throw new IsNotAttributeSettingException("setWritable");
		this.writable = writable;
	}
	public String getCatalogName() {
		return catalogName;
	}
	public void setCatalogName(String catalogName) throws IsNotAttributeSettingException {
		if (!editing && !loading)
			throw new IsNotAttributeSettingException("setCatalogName");
		this.catalogName = catalogName;
	}
	public boolean isReadOnly() {
		return super.isReadOnly() || autoIncrement || (!this.writable);
	}

}
