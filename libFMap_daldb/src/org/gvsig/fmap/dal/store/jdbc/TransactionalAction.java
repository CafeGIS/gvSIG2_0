package org.gvsig.fmap.dal.store.jdbc;

/**
 * Contains an action to execute with
 * {@link JDBCHelper#doConnectionAction(ConnectionAction)} inside of a DB
 * transaction.
 * 
 * @see {@link JDBCHelper#doConnectionAction(ConnectionAction)},
 *      {@link ConnectionAction}
 */
public interface TransactionalAction extends ConnectionAction {

	/**
	 * If before run <code>action</code> there's a transaction open and this
	 * methos returns <code>false</code> an exception will be throwed
	 *
	 * @return
	 */
	boolean continueTransactionAllowed();

}