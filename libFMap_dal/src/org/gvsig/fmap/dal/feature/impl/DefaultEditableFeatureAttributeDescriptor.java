package org.gvsig.fmap.dal.feature.impl;

import java.util.HashMap;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.dal.feature.EditableFeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.exception.AttributeFeatureTypeIntegrityException;
import org.gvsig.fmap.dal.feature.exception.AttributeFeatureTypeSizeException;
import org.gvsig.tools.evaluator.Evaluator;

public class DefaultEditableFeatureAttributeDescriptor extends
		DefaultFeatureAttributeDescriptor implements
		EditableFeatureAttributeDescriptor {

	private DefaultFeatureAttributeDescriptor source;
	private boolean hasStrongChanges;

	protected DefaultEditableFeatureAttributeDescriptor(
			DefaultFeatureAttributeDescriptor other) {
		super(other);
		this.source = other;
		hasStrongChanges=false;
	}

	protected DefaultEditableFeatureAttributeDescriptor(
			DefaultEditableFeatureAttributeDescriptor other) {
		super(other);
		this.source = other.getSource();
		hasStrongChanges = false;
	}

	public DefaultEditableFeatureAttributeDescriptor() {
		super();
		this.source = null;
		hasStrongChanges = false;
	}

	public DefaultFeatureAttributeDescriptor getSource() {
		return this.source;
	}

	public void fixAll() {
	}

	public void checkIntegrity() throws AttributeFeatureTypeIntegrityException {
		AttributeFeatureTypeIntegrityException ex = new AttributeFeatureTypeIntegrityException(
				getName());
		if (this.size < 0) {
			ex.add(new AttributeFeatureTypeSizeException(this.size));
		}

		// TODO: añadir resto de comprobaciones de integridad.

		if (ex.size() > 0) {
			throw ex;
		}
	}

	public EditableFeatureAttributeDescriptor setAllowNull(boolean allowNull) {
		this.allowNull = allowNull;
		if( this.evaluator != null ) {
			hasStrongChanges=true;
		}
		return this;
	}

	public EditableFeatureAttributeDescriptor setDataType(int type) {
		this.dataType  = type;
		if( this.evaluator != null ) {
			hasStrongChanges=true;
		}
		return this;
	}

	public EditableFeatureAttributeDescriptor setDefaultValue(
			Object defaultValue) {
		this.defaultValue = defaultValue;
		if( this.evaluator != null ) {
			hasStrongChanges=true;
		}
		return this;
	}

	public EditableFeatureAttributeDescriptor setEvaluator(Evaluator evaluator) {
		this.evaluator = evaluator;
		if( this.evaluator != null ) {
			hasStrongChanges=true;
		}
		return this;
	}

	public EditableFeatureAttributeDescriptor setGeometryType(int geometryType) {
		this.geometryType= geometryType;
		if( this.evaluator != null ) {
			hasStrongChanges=true;
		}
		return this;
	}

	public EditableFeatureAttributeDescriptor setIsPrimaryKey(
			boolean isPrimaryKey) {
		this.primaryKey = isPrimaryKey;
		if( this.evaluator != null ) {
			hasStrongChanges=true;
		}
		return this;
	}

	public EditableFeatureAttributeDescriptor setIsReadOnly(boolean isReadOnly) {
		this.readOnly = isReadOnly;
		if( this.evaluator != null ) {
			hasStrongChanges=true;
		}
		return this;
	}

	public EditableFeatureAttributeDescriptor setMaximumOccurrences(
			int maximumOccurrences) {
		this.maximumOccurrences = maximumOccurrences;
		if( this.evaluator != null ) {
			hasStrongChanges=true;
		}
		return this;
	}

	public EditableFeatureAttributeDescriptor setMinimumOccurrences(
			int minimumOccurrences) {
		this.minimumOccurrences = minimumOccurrences;
		if( this.evaluator != null ) {
			hasStrongChanges=true;
		}
		return this;
	}

	public EditableFeatureAttributeDescriptor setName(String name) {
		this.name = name;
		if( this.evaluator != null ) {
			hasStrongChanges=true;
		}
		return this;
	}

	public EditableFeatureAttributeDescriptor setObjectClass(Class theClass) {
		this.objectClass = theClass;
		if( this.evaluator != null ) {
			hasStrongChanges=true;
		}
		return this;
	}

	public EditableFeatureAttributeDescriptor setPrecision(int precision) {
		this.precision = precision;
		if( this.evaluator != null ) {
			hasStrongChanges=true;
		}
		return this;
	}

	public EditableFeatureAttributeDescriptor setSRS(IProjection SRS) {
		this.SRS = SRS;
		if( this.evaluator != null ) {
			hasStrongChanges=true;
		}
		return this;
	}

	public EditableFeatureAttributeDescriptor setSize(int size) {
		this.size = size;
		if( this.evaluator != null ) {
			hasStrongChanges=true;
		}
		return this;
	}

	public boolean hasStrongChanges() {
		return hasStrongChanges;
	}

	public EditableFeatureAttributeDescriptor setAdditionalInfo(String infoName, Object value) {
		if (this.additionalInfo == null) {
			this.additionalInfo = new HashMap();
		}
		this.additionalInfo.put(infoName, value);
		return this;
	}

	public EditableFeatureAttributeDescriptor setIsAutomatic(boolean isAutomatic) {
		this.isAutomatic = isAutomatic;
		return this;
	}

	public EditableFeatureAttributeDescriptor setGeometrySubType(
			int geometrySubType) {
		this.geometrySubType = geometrySubType;
		return this;
	}


}
