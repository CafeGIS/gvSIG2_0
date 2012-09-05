package org.gvsig.fmap.data.feature.db.jdbc.h2;

import java.sql.ResultSet;
import java.sql.SQLException;

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
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.operation.GeometryOperationNotSupportedException;
import org.gvsig.fmap.geom.operation.fromwkb.FromWKB;
import org.gvsig.fmap.geom.operation.fromwkb.FromWKBGeometryOperationContext;
import org.gvsig.fmap.geom.operation.towkb.ToWKB;
import org.gvsig.fmap.geom.primitive.NullGeometry;

public class H2Feature extends JDBCFeature{

	H2Feature(FeatureType featureType, JDBCStore store, ResultSet rs) throws ReadException {
		super(featureType, store, rs);
	}

	protected H2Feature(H2Feature feature) throws ReadException{
		super(feature);
	}

	/**
	 *
	 */
	private static final long serialVersionUID = -5836612523458472800L;

	public FeatureReference getID() {
		return new H2FeatureID(this.store,featureKey);
	}

	protected void loadValueFromResulset(ResultSet rs, FeatureAttributeDescriptor attr) throws ReadException {
		Geometry geom;
		String name = attr.getName();
		try{
			if (attr.getDataType().equals(FeatureAttributeDescriptor.GEOMETRY)) {
				byte[] data = rs.getBytes(name);

				if (data == null) {
					geom = null;
				} else{
					geom=new NullGeometry();
					FromWKBGeometryOperationContext fwkb=new FromWKBGeometryOperationContext();
					fwkb.setData(data);
					geom=(Geometry)geom.invokeOperation(FromWKB.CODE,fwkb);
				}
	//			feature.setDefaultGeometry(geom);
				this.setGeometry(name,geom);
			} else {
				this.setAttribute(name, rs.getObject(name));
			}
		} catch (SQLException e) {
			throw new ReadException(this.store.getName(),e);
		} catch (IsNotFeatureSettingException e) {
			throw new ReadException(this.store.getName(),e);
		} catch (GeometryOperationNotSupportedException e) {
			throw new ReadException(this.store.getName(),e);
		} catch (GeometryOperationException e) {
			throw new ReadException(this.store.getName(),e);
		}


	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.AbstractFeature#cloneFeature()
	 */
	protected Feature cloneFeature() throws DataException {
		return new H2Feature(this);
	}
}
