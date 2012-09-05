package org.gvsig.fmap.dal.feature.operation;

import java.util.Map;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.Feature;

public interface LegendLabelingManager {

	public void startLoading(Map params);

	public void add(Feature feature, Map params) throws DataException;

	public Object getDefaultLegend();

	public Object getLabeling();

	public void endLoading();
}
