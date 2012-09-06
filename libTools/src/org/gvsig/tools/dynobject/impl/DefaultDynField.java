package org.gvsig.tools.dynobject.impl;

import org.gvsig.tools.dynobject.DynField;
import org.gvsig.tools.dynobject.DynObjectValueItem;

public class DefaultDynField implements DynField {
	private String name;
	private String description;

	private int dataType;

	private Object defaultValue;

	private int typeOfAvailableValues;
	private DynObjectValueItem[] availableValues;
	private Object minValue;
	private Object maxValue;
	private boolean mandatory;
	private boolean persistent;

	public DefaultDynField(String name) {
		this.name = name;
		this.dataType = 0;
		this.defaultValue = null;
		this.persistent = false;
		this.mandatory = false;
		this.typeOfAvailableValues = DynField.SINGLE;
	}

	public String getName() {
		return name;
	}

	public DynField setDescription(String description) {
		this.description = description;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public DynField setType(int dataType) {
		this.dataType = dataType;
		return this;
	}

	public int getType() {
		return dataType;
	}

	public DynField setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	public int getTheTypeOfAvailableValues() {
		return typeOfAvailableValues;
	}

	public DynField setAvailableValues(DynObjectValueItem[] availableValues) {
		this.availableValues = availableValues;
		return this;
	}

	public DynObjectValueItem[] getAvailableValues() {
		return availableValues;
	}

	public DynField setMinValue(Object minValue) {
		this.minValue = minValue;
		return this;
	}

	public Object getMinValue() {
		return minValue;
	}

	public DynField setMaxValue(Object maxValue) {
		this.maxValue = maxValue;
		return this;
	}

	public Object getMaxValue() {
		return maxValue;
	}

	public Object coerce(Object value) {
		return value; // FIXME: Falta por comprobar los tipos
	}

	public boolean isMandatory() {
		return this.mandatory;
	}

	public boolean isPersistent() {
		return this.persistent;
	}

	public DynField setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
		return this;
	}

	public DynField setPersistent(boolean persistent) {
		this.persistent = persistent;
		return this;
	}

	public DynField setTheTypeOfAvailableValues(int type) {
		if (type == DynField.SINGLE || type == DynField.RANGE
				|| type == DynField.CHOICE) {
		this.typeOfAvailableValues = type;
		} else {
			// FIXME
			throw new IllegalArgumentException();
		}
		return this;
	}


    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof DynField) {
            return name.equals(((DynField) obj).getName());
        }
        return false;
    }

    public String toString() {
        return "name: ".concat(getName()) + ", description: "
                + getDescription() + ", type: " + Integer.toString(getType())
                + ", minValue: " + getMinValue() + ", maxValue: "
                + getMaxValue() + ", mandatory?:" + isMandatory()
                + ", persistent?: " + isPersistent() + ", default value: "
                + getDefaultValue();
    }
}