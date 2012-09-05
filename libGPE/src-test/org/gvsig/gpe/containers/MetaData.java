package org.gvsig.gpe.containers;

import java.util.ArrayList;

public class MetaData {
	
	private MetaData parentData = null;
	private ArrayList dataList = new ArrayList();
	private String tagType = null;
	private String tagData = null;
	
	/**
	 * @return the tagType
	 */
	public String getTagType() {
		return this.tagType;
	}
	
	/**
	 * @param name the tagType to set
	 */
	public void setTagType(String tagType) {
		this.tagType = tagType;
	}
	
	/**
	 * @param name the tag data to set
	 */
	public void setTagData(String tagData) {
		this.tagData = tagData;
	}
	
	/**
	 * @param name the tag data to set
	 */
	public String getTagData() {
		return tagData;
	}
	
	/**
	 * @return the data list
	 */
	public ArrayList getDataList() {
		return dataList;
	}
	
	/**
	 * @return the metadata at position i
	 * @param i
	 * Element position
	 */
	public MetaData getElementAt(int i) {
		return (MetaData)dataList.get(i);
	}
	
	/**
	 * @return the parent metadata
	 */
	public MetaData getParentData() {
		return parentData;
	}
	
	/**
	 * @param parent metadata the parentElement to set
	 */
	public void setParentData(Object parentData) {
		if (parentData != null){
			this.parentData = (MetaData)parentData;
			((MetaData)parentData).addChildData(this);
		}
	}
		
	/**
	 * @param adds a child metadata
	 */
	public void addChildData(MetaData subData) {
		getDataList().add(subData);
	}
	
}
