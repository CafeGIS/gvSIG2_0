package org.gvsig.fmap.dal.feature.impl;

import java.util.ArrayList;
import java.util.Iterator;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureRule;
import org.gvsig.fmap.dal.feature.FeatureRules;

public class DefaultFeatureRules extends ArrayList implements FeatureRules {

	/**
	 *
	 */
	private static final long serialVersionUID = -8084546505498274121L;

	public FeatureRule add(FeatureRule rule) {
		if (super.add(rule)) {
			return rule;
		}
		return null;
	}

	public FeatureRule getRule(int index) {
		return (FeatureRule) super.get(index);
	}

	public boolean remove(FeatureRule rule) {
		return super.remove(rule);
	}

	public FeatureRules getCopy() {
		DefaultFeatureRules copy = new DefaultFeatureRules();
		copy.addAll(this);
		return copy;
	}

	public void validate(Feature feature) {
		Iterator featureRules=iterator();
		while (featureRules.hasNext()) {
			FeatureRule rule = (FeatureRule) featureRules.next();
			try {
				rule.validate(feature, ((DefaultFeature)feature).getStore());
			} catch (DataException e) {
				e.printStackTrace();
			}
		}

	}




}
