package org.gvsig.fmap.data.feature.db.jdbc.postgresql;

import java.sql.ResultSet;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureReference;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.exception.IsNotFeatureSettingException;
import org.gvsig.fmap.data.feature.db.jdbc.JDBCFeature;
import org.gvsig.fmap.data.feature.db.jdbc.JDBCStore;
import org.gvsig.fmap.geom.Geometry;
import org.postgis.PGgeometry;



public class PostgresqlFeature extends JDBCFeature{

	/**
	 *
	 */
	private static final long serialVersionUID = -8217916635473746942L;


	PostgresqlFeature(FeatureType featureType, JDBCStore store, ResultSet rs) throws ReadException {
		super(featureType, store, rs);
	}

	protected PostgresqlFeature(PostgresqlFeature feature) throws ReadException{
		super(feature);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.AbstractFeature#cloneFeature()
	 */
	protected Feature cloneFeature() throws DataException {
		return new PostgresqlFeature(this);
	}


	public FeatureReference getID() {
		return new PostgresqlFeatureID(this.store,featureKey);
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.db.jdbc.JDBCFeature#loadValueFromResulset(java.sql.ResultSet, org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor)
	 */
	protected void loadValueFromResulset(ResultSet rs, FeatureAttributeDescriptor attr) throws ReadException {
		Geometry geom = null;
		String name = attr.getName();

		try {
			if (attr.getDataType().equals(FeatureAttributeDescriptor.GEOMETRY)) {

				PGgeometry data =(PGgeometry)rs.getObject(name);
				if (data == null) {
					geom = null;
				} else{
					geom = PostGIS2Geometry.getGeneralPath(data);
				}
				this.setGeometry(name,geom);
			} else {
				this.setAttribute(name, rs.getObject(name));
			}
		} catch (java.sql.SQLException e) {
			throw new ReadException(this.store.getName(),e);
		} catch (IsNotFeatureSettingException e) {
			throw new ReadException(this.store.getName(),e);
		}

	}


}

