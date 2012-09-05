package org.gvsig.fmap.dal.feature.spi;

import org.gvsig.fmap.dal.feature.FeatureReference;

public interface FeatureReferenceProviderServices extends FeatureReference {

	public Object getOID();

	public String[] getKeyNames();

	public Object getKeyValue(String name);

	public boolean isNewFeature();

	public String getFeatureTypeId();

}
