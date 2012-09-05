package com.iver.utiles.console;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * DOCUMENT ME!
 *
 * @author Fernando González Cortés
 */
public class ResponseListenerSupport {
	private ArrayList listeners = new ArrayList();

	/**
	 * DOCUMENT ME!
	 *
	 * @param listener DOCUMENT ME!
	 */
	public void addResponseListener(ResponseListener listener) {
		listeners.add(listener);
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param listener DOCUMENT ME!
	 */
	public void removeResponseListener(ResponseListener listener) {
		listeners.remove(listener);
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param arg0 DOCUMENT ME!
	 *
	 * @throws InvalidResponseException DOCUMENT ME!
	 */
	public void callAcceptResponse(java.lang.String arg0) {
		Iterator i = listeners.iterator();

		while (i.hasNext()) {
			((ResponseListener) i.next()).acceptResponse(arg0);
		}
	}
}
