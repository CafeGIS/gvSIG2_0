package org.gvsig.tools.dynobject;

public class DynObjectValueItem {

	private Object value;
	private String label;

	public DynObjectValueItem(Object value, String label) {
		this.setLabel(label);
		this.setValue(value);
	}

	public DynObjectValueItem(Object value) {
		this.setLabel(null);
		this.setValue(value);
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public String toString() {
		return label;
	}

	public boolean equals(Object obj) {
		if (obj == null){
			return false;
		}
		if (!(obj instanceof DynObjectValueItem)) {
			return false;
		}
		DynObjectValueItem other = (DynObjectValueItem) obj;
		if (value != other.value
				&& (value == null || !value.equals(other.value))) {
			return false;
		}
		if (label != other.label
				&& (label == null || !label.equals(other.label))) {
			return false;
		}
		return true;

	}


}
