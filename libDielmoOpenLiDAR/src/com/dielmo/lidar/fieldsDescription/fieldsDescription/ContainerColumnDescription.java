package com.dielmo.lidar.fieldsDescription.fieldsDescription;

import java.util.ArrayList;

public class ContainerColumnDescription {
	
	private ArrayList<ColumnDescription> columns;

	public ContainerColumnDescription() {
		
		this.columns = new ArrayList<ColumnDescription>();
	}

	public ArrayList<ColumnDescription> getColumns() {
		return columns;
	}

	public void setColumns(ArrayList<ColumnDescription> columns) {
		this.columns = columns;
	}

	public void add(String fieldName, int fieldType, int fieldSize,
			int fieldPrecision, Object fieldDefaultValue){
		
		ColumnDescription c = new ColumnDescription(fieldName, fieldType, fieldSize, fieldPrecision, fieldDefaultValue);
		this.columns.add(c);
	}
	
	public ColumnDescription get(int index){
		
		ColumnDescription c = columns.get(index);
		return c;
	}
}
