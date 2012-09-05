package com.dielmo.lidar.fieldsDescription.fieldsDescription;

public class ColumnDescription {
	
	public static final int INT=0;
	public static final int DOUBLE=1;
	public static final int STRING=2;
	public static final int LONG=3;
	public static final int BYTE=4;
	
	private String fieldName;
	private int fieldType;
	private int fieldSize;
	private int fieldPrecision;
	private Object fieldDefaultValue;
	
	
	public ColumnDescription(String fieldName, int fieldType, int fieldSize,
			int fieldPrecision, Object fieldDefaultValue) {
		
		this.fieldName = fieldName;
		this.fieldType = fieldType;
		this.fieldSize = fieldSize;
		this.fieldPrecision = fieldPrecision;
		
		switch(fieldType){
			case ColumnDescription.INT:
				this.fieldDefaultValue = (Integer)fieldDefaultValue;
				break;
			case ColumnDescription.BYTE:
				this.fieldDefaultValue = (Byte)fieldDefaultValue;
				break;
			case ColumnDescription.STRING:
				this.fieldDefaultValue = (String)fieldDefaultValue;
				break;
			case ColumnDescription.DOUBLE:
				this.fieldDefaultValue = (Double)fieldDefaultValue;
				break;
			case ColumnDescription.LONG:
				this.fieldDefaultValue = (Long)fieldDefaultValue;
				break;
				
			default:
		}
			
	}
	
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public int getFieldType() {
		return fieldType;
	}
	public void setFieldType(int fieldType) {
		this.fieldType = fieldType;
	}
	public int getFieldSize() {
		return fieldSize;
	}
	public void setFieldSize(int fieldSize) {
		this.fieldSize = fieldSize;
	}
	public int getFieldPrecision() {
		return fieldPrecision;
	}
	public void setFieldPrecision(int fieldPrecision) {
		this.fieldPrecision = fieldPrecision;
	}
	public Object getFieldDefaultValue() {
		return fieldDefaultValue;
	}
	public void setFieldDefaultValue(Object fieldDefaultValue) {
		this.fieldDefaultValue = fieldDefaultValue;
	}
}
