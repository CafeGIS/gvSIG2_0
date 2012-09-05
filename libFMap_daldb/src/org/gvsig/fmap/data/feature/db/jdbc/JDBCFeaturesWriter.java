package org.gvsig.fmap.data.feature.db.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.gvsig.fmap.dal.exception.CloseException;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.OpenException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.exception.WriteException;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.exception.InitializeWriterException;
import org.gvsig.fmap.dal.feature.impl.SelectiveWriter;
import org.gvsig.fmap.data.feature.db.DBAttributeDescriptor;
import org.gvsig.fmap.data.feature.db.DBFeatureType;
import org.gvsig.fmap.data.feature.db.jdbc.h2.H2StoreParameters;
import org.gvsig.fmap.data.feature.db.jdbc.postgresql.PostgresqlStore;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.operation.GeometryOperationNotSupportedException;
import org.gvsig.fmap.geom.operation.towkb.ToWKB;

public abstract class JDBCFeaturesWriter implements SelectiveWriter{

	protected Connection conex;
	protected JDBCStore store;
	protected boolean previousAutocommit;


	public void init(FeatureStore store) throws InitializeWriterException{
		this.store = (JDBCStore)store;
		try {
			this.conex = this.store.getWriterConnection();
		} catch (ReadException e) {
			throw new InitializeWriterException(this.store.getName(),e);
		}
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.FeaturesWriter#dispose()
	 */
	public void dispose() throws DataException {
		try {
			this.conex.close();
		} catch (SQLException e) {
			throw new CloseException(this.store.getName(),e);
		}
		this.conex = null;

	}


	public void preProcess() throws WriteException, ReadException {
		try {
			previousAutocommit = conex.getAutoCommit();
			conex.setAutoCommit(false);
		} catch (SQLException e) {
			throw new WriteException(this.store.getName(),e);
		}

	}

	public void postProcess() throws OpenException, WriteException {
		try {
			conex.commit();
			if (previousAutocommit){
				conex.setAutoCommit(true);
			}
		} catch (SQLException e) {
			throw new WriteException(this.store.getName(),e);
		}
	}

	public void cancelProcess() throws WriteException {
		try {
			conex.rollback();
			if (previousAutocommit){
				conex.setAutoCommit(true);
			}
		} catch (SQLException e) {
			throw new WriteException(this.store.getName(),e);
		}
	}

	protected void loadValueInPreparedStatement(PreparedStatement ps, int paramIndex, DBAttributeDescriptor attr, Feature feature) throws java.sql.SQLException, WriteException {
			Object value = feature.getAttribute(attr.ordinal());
			if (value == null){
				ps.setNull(paramIndex, attr.getSqlType());
				return;
			}
	
			if (attr.getDataType() == FeatureAttributeDescriptor.GEOMETRY){
				Geometry geom =(Geometry)feature.getAttribute(attr.ordinal());
				try {
	//				TODO Falta pasar el SRS de la geometría para pasarla a WKB.
					ps.setBytes(
						paramIndex,	(byte[])geom.invokeOperation(ToWKB.CODE,null)
					);
				} catch (GeometryOperationNotSupportedException e) {
					throw new WriteException(this.store.getName(),e);
				} catch (GeometryOperationException e) {
					throw new WriteException(this.store.getName(),e);
				}
				return;
			}
			ps.setObject(paramIndex, feature.getAttribute(attr.ordinal()));
		}

}