package org.gvsig.fmap.dal;

/**
 * This interface represents a notification produced by a DataStore.
 * 
 * Notifications can be of several types. This interface also defines a set 
 * of constants that represent the types of notifications that a DataStore 
 * can produce.
 */
public interface DataStoreNotification {

	/** Complex notification for special situations */
	public static final String COMPLEX_NOTIFICATION = "complex_notification";

	/** Fired before opening the store */
	public static final String BEFORE_OPEN = "before_Open_DataStore";
	
	/** Fired after opening the store */
	public static final String AFTER_OPEN = "after_Open_DataStore";

	/** Fired before closing the store */
	public static final String BEFORE_CLOSE = "before_Close_DataStore";
	
	/** Fired after closing the store */
	public static final String AFTER_CLOSE = "after_Close_DataStore";

	/** Fired before disposing the store */
	public static final String BEFORE_DISPOSE = "before_Dispose_DataStore";
	
	/** Fired after disposing the store */
	public static final String AFTER_DISPOSE = "after_Dispose_DataStore";

	/** Fired after the store selection has changed */
	public static final String SELECTION_CHANGE = "after_SelectionChange_DataStore";

	/** Fired when a resource of the store has changed */
	public static final String RESOURCE_CHANGED = "resourceChange_DataStore";

	/**
	 * Returns the DataStore that produced this notification
	 * @return DataStore source of this
	 */
	public DataStore getSource();
	
	/**
	 * Returns the type of this notification, represented by one of the constants defined in this interface.
	 * @return a String containing this notification's type
	 */
	public String getType();

}
