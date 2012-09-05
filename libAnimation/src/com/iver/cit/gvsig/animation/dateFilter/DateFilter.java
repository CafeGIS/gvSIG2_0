package com.iver.cit.gvsig.animation.dateFilter;


//public class DateFilter implements ICustomVectorialFilter, IPersistence {
	public class DateFilter {
//
//	public static int FIELDTYPE_DATE = 0;
//	public static int FIELDTYPE_DATESTRING = 1;
//
//	public static int BOTH = 2 | 4;
//	public static int BEFORE_BEGIN = 2;
//	public static int AFTER_END = 4;
//
//	private int fieldType = -1;
//
//	private int filterFieldIndex = -1;
//	private Date minDate = null;
//	private Date maxDate = null;
//	// derived
//	private SimpleDateFormat localeDF = null;
//	private SimpleDateFormat javaDF = null;
//	private Calendar cl = null;
//
//	private int filterMode = BEFORE_BEGIN;
//
//	public void setFieldIndex(int index) {
//		filterFieldIndex = index;
//	}
//
//	public void setFieldType(int type) {
//		fieldType = type;
//	}
//
//	public void setMinDate(Date miniDate) {
//		minDate = miniDate;
//	}
//
//	public void setMaxDate(Date maxiDate) {
//		maxDate = maxiDate;
//	}
//
//	public int getFieldType() {
//		return fieldType;
//	}
//
//	public Date getMinDate() {
//		return minDate;
//	}
//
//	public Date getMaxDate() {
//		return maxDate;
//	}
//
//	public boolean accepts(IFeature feature) {
//
//		if (filterFieldIndex == -1 || minDate == null || maxDate == null)
//			return false;
//
//		Value val = feature.getAttribute(filterFieldIndex);
//		if (val instanceof NullValue)
//			return false;
//
//		Date dateVal = null;
//		if (fieldType == FIELDTYPE_DATE) {
//			try {
//				dateVal = ((DateValue) val).getValue();
//			} catch (RuntimeException e) {
//				return false;
//			}
//		} else if (fieldType == FIELDTYPE_DATESTRING) {
//			if (localeDF == null)
//				localeDF = new SimpleDateFormat("dd/MM/yyyy");
//			if (javaDF == null)
//				javaDF = new SimpleDateFormat("yyyy-MM-dd");
//
//			String valStr = val.toString();
//
//			try {
//				java.util.Date utDate = localeDF.parse(valStr); // reads locale
//				// dd/MM/yyyy
//				// strings
//				String utDateStr = javaDF.format(utDate); // outputs
//				// yyyy-MM-dd
//				dateVal = Date.valueOf(utDateStr); // creates Date for
//				// comparison
//			} catch (ParseException e) {
//				return false;
//			}
//		}
//
//		try {
//			String dateValStr = dateVal.toString();
//			String minDateStr = minDate.toString();
//			String maxDateStr = maxDate.toString();
//
//			if (((this.filterMode & BEFORE_BEGIN) != BEFORE_BEGIN))
//				if (dateVal.compareTo(minDate) < 0)
//					return false;
//
//			if (((this.filterMode & AFTER_END) != AFTER_END))
//				if (dateVal.compareTo(maxDate) > 0)
//					return false;
//
//		} catch (Exception e) {
//			return false;
//		}
//		return true;
//	}
//
//	public String getClassName() {
//		return this.getClass().getName();
//	}
//
//	public XMLEntity getXMLEntity() {
//		XMLEntity xml = new XMLEntity();
//
//		xml.putProperty("fieldType", fieldType);
//		xml.putProperty("fieldIndex", filterFieldIndex);
//		xml.putProperty("minDate", minDate.toString());
//		xml.putProperty("maxDate", maxDate.toString());
//
//		return xml;
//	}
//
//	public void setXMLEntity(XMLEntity xml) {
//		if (xml.contains("fieldType"))
//			fieldType = xml.getIntProperty("fieldType");
//
//		if (xml.contains("fieldIndex"))
//			filterFieldIndex = xml.getIntProperty("fieldIndex");
//
//		if (xml.contains("minDate"))
//			minDate = Date.valueOf(xml.getStringProperty("minDate"));
//
//		if (xml.contains("maxDate"))
//			maxDate = Date.valueOf(xml.getStringProperty("maxDate"));
//	}
//
//	public String toString() {
//		String result = "";
//
//		result += minDate.toString() + "\n" + maxDate.toString();
//
//		return result;
//	}
//
//	public boolean compareTo(ICustomVectorialFilter filter) {
//		DateFilter filterAux = (DateFilter) filter;
//		boolean type = this.getFieldType() == filterAux.getFieldType();
//		boolean min = this.getMinDate().toString().equals(
//				filterAux.getMinDate().toString());
//		boolean max = this.getMaxDate().toString().equals(
//				filterAux.getMaxDate().toString());
//
//		return (type && min && max);
//	}
//
//	public int getFilterMode() {
//		return filterMode;
//	}
//
//	public void setFilterMode(int filterMode) {
//		this.filterMode = filterMode;
//	}
}
