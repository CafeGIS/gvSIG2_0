package org.gvsig.fmap.dal.feature.impl;

import org.gvsig.fmap.dal.DataSet;
import org.gvsig.fmap.dal.DataStore;
import org.gvsig.fmap.dal.feature.EditableFeatureType;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureStoreNotification;
import org.gvsig.tools.undo.command.Command;

public class DefaultFeatureStoreNotification implements
		FeatureStoreNotification {
	private Feature feature = null;
	private DataSet collectionResult = null;
	private boolean loadCollectionSucefully = false;
	private Exception exceptionLoading = null;
	private Command command = null;
	private EditableFeatureType featureType = null;
	private String type;
	private DataStore source;

	protected void init(DataStore source, String type) {
		this.source = source;
		this.type = type;
	}

	public DefaultFeatureStoreNotification(DataStore source, String type) {
		this.init(source, type);
	}

	public DefaultFeatureStoreNotification(DataStore source, String type, Feature feature) {
		this.init(source, type);
		this.feature = feature;
	}
	public DefaultFeatureStoreNotification(DataStore source, String type, Command command) {
		this.init(source, type);
		this.command = command;
	}

	public DefaultFeatureStoreNotification(DataStore source, String type, Exception exception) {
		this.init(source, type);
		this.loadCollectionSucefully = false;
		this.exceptionLoading = exception;
	}

	public DefaultFeatureStoreNotification(DataStore source, String type, DataSet collection) {
		this.init(source, type);
		this.loadCollectionSucefully = true;
		this.collectionResult = collection;
	}


	public DefaultFeatureStoreNotification(DataStore source, String type,
			EditableFeatureType featureType) {
		this.init(source, type);
		this.featureType = featureType;
	}

	public Feature getFeature() {
		return feature;
	}

	public EditableFeatureType getFeatureType() {
		return featureType;
	}

	public DataSet getCollectionResult() {
		return this.collectionResult;
	}

	public Exception getExceptionLoading() {
		return this.exceptionLoading;
	}

	public boolean loadSucefully() {
		return this.loadCollectionSucefully;
	}

	public Command getCommand() {
		return this.command;
	}

	public DataStore getSource() {
		return source;
	}

	public String getType() {
		return type;
	}

}
