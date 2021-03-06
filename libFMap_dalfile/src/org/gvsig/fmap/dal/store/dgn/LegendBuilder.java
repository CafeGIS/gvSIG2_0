package org.gvsig.fmap.dal.store.dgn;

import org.gvsig.fmap.dal.feature.spi.FeatureProvider;
import org.gvsig.fmap.dal.feature.spi.FeatureStoreProvider;
import org.gvsig.fmap.dal.store.dgn.lib.DGNReader;


public interface LegendBuilder {

	final public static String DYNMETHOD_BUILDER_NAME = "getLegendBuilder";
	final public static String DYNMETHOD_GETLEGEND_NAME = "getLegend";
	final public static String DYNMETHOD_GETLABELING_NAME = "getLabeling";

	public LegendBuilder initialize(FeatureStoreProvider store);

	public void begin();

	public void process(FeatureProvider feature, DGNReader dgnReader);

	public void end();

	public Object getLegend();
	public Object getLabeling();

}
