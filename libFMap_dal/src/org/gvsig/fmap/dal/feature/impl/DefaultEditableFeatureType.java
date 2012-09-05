package org.gvsig.fmap.dal.feature.impl;

import java.util.Iterator;

import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.exception.DataListException;
import org.gvsig.fmap.dal.feature.EditableFeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.EditableFeatureType;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.exception.FeatureTypeIntegrityException;
import org.gvsig.tools.evaluator.Evaluator;

public class DefaultEditableFeatureType extends DefaultFeatureType implements
		EditableFeatureType {

	/**
	 *
	 */
	private static final long serialVersionUID = -713976880396024389L;

	private boolean hasStrongChanges;
	private DefaultFeatureType source;

	public DefaultEditableFeatureType() {
		super();
		this.hasStrongChanges = false;
		this.source = null;
	}

	public DefaultEditableFeatureType(String id) {
		super(id);
		this.hasStrongChanges = false;
		this.source = null;
	}

	protected DefaultEditableFeatureType(DefaultEditableFeatureType other) {
		super(other);
		this.source = (DefaultFeatureType) other.getSource();
	}

	protected DefaultEditableFeatureType(DefaultFeatureType other) {
		super(other);
		this.source = other;
	}

	protected void intitalizeAddAttibute(DefaultFeatureAttributeDescriptor attr) {
		super.add(new DefaultEditableFeatureAttributeDescriptor(attr));
	}


	public boolean hasStrongChanges() {
		if (hasStrongChanges) {
			return true;
		}
		Iterator iter = this.iterator();
		DefaultEditableFeatureAttributeDescriptor attr;
		while (iter.hasNext()) {
			attr = (DefaultEditableFeatureAttributeDescriptor) iter.next();
			if (attr.hasStrongChanges()) {
				return true;
			}
		}
		return false;
	}

	public FeatureType getCopy() {
		return new DefaultEditableFeatureType(this);
	}

	public EditableFeatureType getEditable() {
		throw new UnsupportedOperationException();
	}

	public boolean addAll(DefaultFeatureType other) {
		Iterator iter = other.iterator();
		DefaultFeatureAttributeDescriptor attr;
		DefaultEditableFeatureAttributeDescriptor editableAttr;
		while (iter.hasNext()) {
			attr = (DefaultFeatureAttributeDescriptor) iter.next();
			if (attr instanceof DefaultEditableFeatureAttributeDescriptor) {
				editableAttr = new DefaultEditableFeatureAttributeDescriptor(
						attr);
			} else {
				editableAttr = new DefaultEditableFeatureAttributeDescriptor(
						attr);
			}
			super.add(editableAttr);
		}
		this.pk = null;
		this.fixAll();
		return true;
	}

	public FeatureType getSource() {
		return source;
	}

	public FeatureType getNotEditableCopy() {
		DefaultFeatureType copy = new DefaultFeatureType(this, false);
		Iterator iter = this.iterator();
		DefaultFeatureAttributeDescriptor attr;
		while (iter.hasNext()) {
			attr = (DefaultFeatureAttributeDescriptor) iter.next();
			copy.add(new DefaultFeatureAttributeDescriptor(attr));
		}
		return copy;
	}

	public EditableFeatureAttributeDescriptor add(String name, int type) {
		DefaultEditableFeatureAttributeDescriptor attr = new DefaultEditableFeatureAttributeDescriptor();
		// FIXME: Comprobar que no hay nombres repetidos.
		attr.setName(name);
		attr.setDataType(type);
		attr.setIndex(this.size());
		super.add(attr);
		hasStrongChanges = true;
		this.pk = null;
		return attr;
	}

	public EditableFeatureAttributeDescriptor add(String name, int type,
			int size) {
		hasStrongChanges = true;
		return this.add(name, type).setSize(size);
	}

	public EditableFeatureAttributeDescriptor add(String name, int type,
			Evaluator evaluator) {
		return this.add(name, type).setEvaluator(evaluator);
	}

	public Object remove(String name) {
		DefaultFeatureAttributeDescriptor attr = (DefaultFeatureAttributeDescriptor) this
				.get(name);
		if (attr == null) {
			return null;
		}
		if (attr.getEvaluator() != null) {
			hasStrongChanges = true;
		}
		super.remove(attr);
		this.pk = null;
		this.fixAll();
		return attr;
	}

	protected void fixAll() {
		int i = 0;
		Iterator iter = super.iterator();
		DefaultFeatureAttributeDescriptor attr;

		while (iter.hasNext()) {
			attr = (DefaultFeatureAttributeDescriptor) iter
					.next();
			attr.setIndex(i++);
			if (attr instanceof DefaultEditableFeatureAttributeDescriptor) {
				((DefaultEditableFeatureAttributeDescriptor) attr).fixAll();
			}
		}
		if (this.defaultGeometryAttributeName == null) {
			return;
		}
		this.defaultGeometryAttributeIndex = this
				.getIndex(this.defaultGeometryAttributeName);
	}

	public void checkIntegrity() throws DataListException {
		Iterator iter = super.iterator();
		FeatureTypeIntegrityException ex = null;

		while (iter.hasNext()) {
			DefaultEditableFeatureAttributeDescriptor attr = (DefaultEditableFeatureAttributeDescriptor) iter
					.next();
			try {
				attr.checkIntegrity();
			} catch (Exception e) {
				if (ex == null) {
					ex = new FeatureTypeIntegrityException(this.getId());
				}
				ex.add(e);
			}
		}
		if (ex != null) {
			throw ex;
		}
	}

	public boolean remove(EditableFeatureAttributeDescriptor attribute) {
		if (attribute.getEvaluator() != null) {
			hasStrongChanges = true;
		}
		if (!super.remove(attribute)) {
			return false;
		}
		this.fixAll();
		return true;
	}

	public void setDefaultGeometryAttributeName(String name) {
		if (name == null || name.length() == 0) {
			this.defaultGeometryAttributeName = null;
			this.defaultGeometryAttributeIndex = -1;
			return;
		}
		DefaultFeatureAttributeDescriptor attr = (DefaultFeatureAttributeDescriptor) this
				.get(name);
		if (attr == null) {
			throw new IllegalArgumentException("Attribute '" + name
					+ "' not found.");
		}
		if (attr.getDataType() != DataTypes.GEOMETRY) {
			throw new IllegalArgumentException("Attribute '" + name
					+ "' is not a geometry.");
		}
		this.defaultGeometryAttributeName = name;
		this.defaultGeometryAttributeIndex = attr.getIndex();
	}

	public void setHasOID(boolean hasOID) {
		this.hasOID = hasOID;
	}

	protected Iterator getIterator(Iterator iter) {
		return new EditableDelegatedIterator(iter, this);
	}

	protected class EditableDelegatedIterator extends DelegatedIterator {

		private DefaultEditableFeatureType fType;

		public EditableDelegatedIterator(Iterator iter,
				DefaultEditableFeatureType fType) {
			super(iter);
			this.fType = fType;
		}

		public void remove() {
			this.iterator.remove();
			this.fType.fixAll();
		}

	}

	protected void setAllowAutomaticValues(boolean value) {
		this.allowAtomaticValues = value;
	}
}
