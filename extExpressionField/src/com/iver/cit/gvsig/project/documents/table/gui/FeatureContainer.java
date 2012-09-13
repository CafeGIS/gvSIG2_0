package com.iver.cit.gvsig.project.documents.table.gui;

import org.gvsig.fmap.dal.feature.Feature;

public class FeatureContainer {
private Feature feature = null;

public Feature getFeature() {
	return feature;
}

public void setFeature(Feature feature) {
	this.feature = feature;
}

public boolean containsFeature() {
	return feature!=null;
}
}
