package org.gvsig.fmap.dal.feature.spi;

import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.primitive.Envelope;

public interface FeatureProvider {

	public void set(int i, Object value);

	public Object get(int i);

	public void set(String name, Object value);

	public Object get(String name);

	public void setOID(Object oid);

	public Object getOID();

	public FeatureType getType();

	public FeatureProvider getCopy();

	public Envelope getDefaultEnvelope();

	public Geometry getDefaultGeometry();

	public void setDefaultEnvelope(Envelope extent);

	public void setDefaultGeometry(Geometry geom);

	public boolean isNull(int i);

	public boolean isNull(String name);
	
	public boolean isNew();

	public void setNew(boolean isNew);

}