package org.gvsig.fmap.dal.feature.impl;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.EditableFeatureType;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureRules;
import org.gvsig.fmap.dal.feature.FeatureType;

public class DefaultFeatureType extends ArrayList implements FeatureType {

	/**
	 *
	 */
	private static final long serialVersionUID = -7988721447349282215L;

	private DefaultFeatureRules rules;
	private boolean hasEvaluators;
	protected String defaultGeometryAttributeName;
	protected int defaultGeometryAttributeIndex;
	private String id;
	protected boolean hasOID;
	protected boolean allowAtomaticValues;
	protected FeatureAttributeDescriptor[] pk = null;

	private List srsList = null;

	protected DefaultFeatureType(String id) {
		this.id = id;
		this.rules = new DefaultFeatureRules();
		this.hasEvaluators = false;
		this.defaultGeometryAttributeName = null;
		this.defaultGeometryAttributeIndex = -1;
		this.allowAtomaticValues = false;
	}

	protected DefaultFeatureType() {
		this("default");
	}

	protected DefaultFeatureType(DefaultFeatureType other) {
		initialize(other, true);
	}

	protected DefaultFeatureType(DefaultFeatureType other,
			boolean copyAttributes) {
		initialize(other, copyAttributes);
	}

	protected void initialize(DefaultFeatureType other, boolean copyAttributes) {
		this.id = other.getId();
		if (copyAttributes) {
			Iterator iter = other.iterator();
			DefaultFeatureAttributeDescriptor attr;
			while (iter.hasNext()) {
				attr = (DefaultFeatureAttributeDescriptor) iter.next();
				this.intitalizeAddAttibute(attr);
			}
		}
		this.defaultGeometryAttributeName = other.defaultGeometryAttributeName;
		this.hasEvaluators = other.hasEvaluators;
		this.rules = (DefaultFeatureRules) other.rules.getCopy();
		this.defaultGeometryAttributeIndex = other.defaultGeometryAttributeIndex;
		this.hasOID = other.hasOID;
		this.id = other.id; // XXX ???? copiar o no esto????
	}

	protected void intitalizeAddAttibute(DefaultFeatureAttributeDescriptor attr) {
		super.add(attr.getCopy());
	}

	public String getId() {
		return this.id;
	}

	public Object get(String name) {
		FeatureAttributeDescriptor attr;
		Iterator iter = this.iterator();
		while (iter.hasNext()) {
			attr = (FeatureAttributeDescriptor) iter.next();
			if (attr.getName().equalsIgnoreCase(name)) {
				return attr;
			}
		}
		return null;
	}

	public FeatureAttributeDescriptor getAttributeDescriptor(String name) {
		FeatureAttributeDescriptor attr;
		Iterator iter = this.iterator();
		while (iter.hasNext()) {
			attr = (FeatureAttributeDescriptor) iter.next();
			if (attr.getName().equalsIgnoreCase(name)) {
				return attr;
			}
		}
		return null;
	}

	public FeatureAttributeDescriptor getAttributeDescriptor(int index) {
		return (FeatureAttributeDescriptor) super.get(index);
	}

	public FeatureType getCopy() {
		return new DefaultFeatureType(this);
	}

	public int getDefaultGeometryAttributeIndex() {
		return this.defaultGeometryAttributeIndex;
	}

	public String getDefaultGeometryAttributeName() {
		return this.defaultGeometryAttributeName;
	}

	public EditableFeatureType getEditable() {
		return new DefaultEditableFeatureType(this);
	}

	public int getIndex(String name) {
		FeatureAttributeDescriptor attr;
		Iterator iter = this.iterator();
		while (iter.hasNext()) {
			attr = (FeatureAttributeDescriptor) iter.next();
			if (attr.getName().equalsIgnoreCase(name)) {
				return attr.getIndex();
			}
		}
		return -1;
	}

	public FeatureRules getRules() {
		return this.rules;
	}

	public boolean hasEvaluators() {
		return this.hasEvaluators;
	}

	public List getSRSs() {
		if (this.srsList == null) {
			ArrayList tmp = new ArrayList();
			Iterator iter = iterator();
			Iterator tmpIter;
			boolean allreadyHave;
			IProjection tmpSRS;
			FeatureAttributeDescriptor attr;
			while (iter.hasNext()){
				attr = (FeatureAttributeDescriptor) iter.next();
				if (attr.getDataType() == DataTypes.GEOMETRY
						&& attr.getSRS() != null) {
					allreadyHave = false;
					tmpIter = tmp.iterator();
					while (tmpIter.hasNext()) {
						tmpSRS = (IProjection) tmpIter.next();
						if (tmpSRS.getAbrev().equals(attr.getSRS().getAbrev())) {
							allreadyHave = true;
							break;
						}
					}
					if (!allreadyHave) {
						tmp.add(attr.getSRS());
					}
				}
			}
			this.srsList = Collections.unmodifiableList(tmp);
		}
		return this.srsList;
	}

	public IProjection getDefaultSRS() {
		if (this.getDefaultGeometryAttributeIndex() < 0) {
			return null;
		}
		return this.getAttributeDescriptor(
				this.getDefaultGeometryAttributeIndex()).getSRS();
	}

	public void validateFeature(Feature feature, int mode) {
		if (Feature.UPDATE == mode){
			((DefaultFeatureRules)getRules()).validate(feature);
		}
	}

	public FeatureType getSubtype(String[] names) throws DataException {
		return new SubtypeFeatureType(this, names);
	}

	public boolean isSubtypeOf(FeatureType featureType) {
		return false;
	}



	class SubtypeFeatureType extends DefaultFeatureType {
		/**
		 *
		 */
		private static final long serialVersionUID = 6913732960073922540L;
		WeakReference parent;

		SubtypeFeatureType(DefaultFeatureType parent, String[] names)
				throws DataException {
			super(parent, false);
			DefaultFeatureAttributeDescriptor attrcopy;
			DefaultFeatureAttributeDescriptor attr;
			// Copy attributes
			for (int i = 0; i < names.length; i++) {
				attr = (DefaultFeatureAttributeDescriptor) parent
						.getAttributeDescriptor(names[i]);
				if (attr == null) {
					throw new SubtypeFeatureTypeNameException(names[i], parent
							.getId());
				}
				attrcopy = new DefaultFeatureAttributeDescriptor(attr);
				this.add(attrcopy);
				attrcopy.index = i;
			}

			// Add missing pk fiels if any
			if (!parent.hasOID()) {
				Iterator iter = parent.iterator();
				while (iter.hasNext()) {
					attr = (DefaultFeatureAttributeDescriptor) iter.next();
					if (attr.isPrimaryKey()
							&& this.getIndex(attr.getName()) < 0) {
						attrcopy = new DefaultFeatureAttributeDescriptor(attr);
						this.add(attrcopy);
						attrcopy.index = this.size() - 1;
					}
				}
			}

			this.defaultGeometryAttributeIndex = this
					.getIndex(this.defaultGeometryAttributeName);
			if (this.defaultGeometryAttributeIndex < 0) {
				this.defaultGeometryAttributeName = null;
			}
			this.parent = new WeakReference(parent);
		}

		public FeatureType getSubtype(String[] names) throws DataException {
			return new SubtypeFeatureType((DefaultFeatureType) this.parent
					.get(), names);
		}

		public boolean isSubtypeOf(FeatureType featureType) {
			if (featureType == null) {
				return false;
			}
			FeatureType parent = (FeatureType) this.parent.get();
			return featureType.equals(parent);
		}

		public EditableFeatureType getEditable() {
			throw new UnsupportedOperationException();
		}
	}

	public class SubtypeFeatureTypeNameException extends DataException {

		/**
		 *
		 */
		private static final long serialVersionUID = -4414242486723260101L;
		private final static String MESSAGE_FORMAT = "Attribute name '%(name)s' not found in type (%(type)s).";
		private final static String MESSAGE_KEY = "_SubtypeFeatureTypeNameException";

		public SubtypeFeatureTypeNameException(String name, String type) {
			super(MESSAGE_FORMAT, MESSAGE_KEY, serialVersionUID);
			setValue("name", name);
			setValue("type", type);
		}
	}

	public boolean hasOID() {
		return hasOID;
	}
	public String toString(){
		StringBuffer s = new StringBuffer();
		s.append(this.getId());
		s.append(":[");
		String attName;
		for (int i = 0; i < size(); i++) {
			attName =((FeatureAttributeDescriptor)get(i)).getName().toString();
			s.append(attName);
			if (i < size() - 1) {
				s.append(',');
			}
		}
		s.append(']');
		return s.toString();
	}

	public Iterator iterator() {
		return getIterator(super.iterator());
	}

	protected Iterator getIterator(Iterator iter) {
		return new DelegatedIterator(iter);
	}

	protected class DelegatedIterator implements Iterator {

		protected Iterator iterator;

		public DelegatedIterator(Iterator iter) {
			this.iterator = iter;
		}

		public boolean hasNext() {
			return iterator.hasNext();
		}

		public Object next() {
			return iterator.next();
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	public boolean allowAutomaticValues() {
		return this.allowAtomaticValues;
	}

	public FeatureAttributeDescriptor[] getAttributeDescriptors() {
		return (FeatureAttributeDescriptor[]) super
				.toArray(new FeatureAttributeDescriptor[super.size()]);
	}

	public FeatureAttributeDescriptor[] getPrimaryKey() {
		if (pk == null) {
			List pkList = new ArrayList();
			Iterator iter = super.iterator();
			FeatureAttributeDescriptor attr;
			while (iter.hasNext()){
				attr = (FeatureAttributeDescriptor) iter.next();
				if (attr.isPrimaryKey()){
					pkList.add(attr);
				}
			}
			pk = (FeatureAttributeDescriptor[]) pkList
					.toArray(new FeatureAttributeDescriptor[pkList.size()]);
		}
		return pk;
	}

	public FeatureAttributeDescriptor getDefaultGeometryAttribute() {
		if (this.defaultGeometryAttributeIndex < 0) {
			return null;
		}
		return (FeatureAttributeDescriptor) super
				.get(this.defaultGeometryAttributeIndex);
	}


	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof DefaultFeatureType)) {
			return false;
		}
		DefaultFeatureType otherType = (DefaultFeatureType) other;
		if (!this.id.equals(otherType.id)) {
			return false;
		}
		if (this.size() != otherType.size()) {
			return false;
		}
		FeatureAttributeDescriptor attr,attrOther;
		Iterator iter,iterOther;
		iter = this.iterator();
		iterOther = otherType.iterator();
		while (iter.hasNext()) {
			attr = (FeatureAttributeDescriptor) iter.next();
			attrOther = (FeatureAttributeDescriptor) iterOther.next();
			if (!attr.equals(attrOther)) {
				return false;
			}
		}

		if (defaultGeometryAttributeName != otherType.defaultGeometryAttributeName) {
			if (defaultGeometryAttributeName == null) {
				return false;
			}
			return defaultGeometryAttributeName
					.equals(otherType.defaultGeometryAttributeName);

		}
		return true;

	}

}
