package org.gvsig.tools.observer;

public interface ComplexObservable {

    /**
	 * Disable the notification of events to registered Observers.
	 */
	void disableNotifications();

	/**
	 * Enable (default) the notification of events to registered Observers.
	 */
	void enableNotifications();

	/**
	 * Sets the Observable in complex notification mode. All notifications must
	 * be stored, until the mode ends, and then notify all Observers with all
	 * the notifications.
	 */
	void beginComplexNotification();

	/**
	 * Ends the complex notification mode. All notifications stored while in
	 * complex notification mode, will be notified to the registered Observers.
	 */
	void endComplexNotification();
}
