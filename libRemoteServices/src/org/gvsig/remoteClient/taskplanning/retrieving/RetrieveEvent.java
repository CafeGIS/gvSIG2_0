/*
 * Created on 01-oct-2005
 */
package org.gvsig.remoteClient.taskplanning.retrieving;

/**
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 * Luis W. Sevilla (sevilla_lui@gva.es)
 */
public class RetrieveEvent {
	public static final int NOT_STARTED = 0;
	public static final int CONNECTING = 1;
	public static final int TRANSFERRING = 2;
	public static final int REQUEST_FINISHED = 3;
	public static final int REQUEST_FAILED = 4;
	public static final int REQUEST_CANCELLED = 5;
	public static final int POSTPROCESSING = 6;
	
	/**
	 * redundant; use REQUEST_FAILED
	 * @deprecated ?
	 */
	public static final int ERROR = 11;
	
	private int eventType;
	
	public void setType(int type) {
		eventType = type;
	}

	public int getType() {
		return eventType;
	}
}
