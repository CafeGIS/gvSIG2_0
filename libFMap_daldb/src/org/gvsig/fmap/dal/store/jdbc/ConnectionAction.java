package org.gvsig.fmap.dal.store.jdbc;

import java.sql.Connection;

import org.gvsig.fmap.dal.exception.DataException;

/**
 * Contains an action to execute with
 * {@link JDBCHelper#doConnectionAction(ConnectionAction)}
 * 
 * @see {@link JDBCHelper#doConnectionAction(ConnectionAction)},
 *      {@link TransactionalAction}
 */
public interface ConnectionAction {
	Object action(Connection conn) throws DataException;
}