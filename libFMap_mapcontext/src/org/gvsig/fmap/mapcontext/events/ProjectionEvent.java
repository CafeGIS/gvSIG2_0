package org.gvsig.fmap.mapcontext.events;

import org.cresques.cts.IProjection;

/**
 * <p>Event produced when the projection of the view port has changed.</p>
 */
public class ProjectionEvent extends FMapEvent {
	/**
	 * <p>The new projection.</p>
	 */
	private IProjection newProyection;

	/**
	 * <p>Identifies this object as an event related with the projection.</p>
	 */
	private static final int PROJECTION_EVENT = 0;

	/**
	 * <p>Creates a new projection event.</p>
	 * 
	 * @param proj the new projection
	 * 
	 * @return a new projection event
	 */
	public static ProjectionEvent createProjectionEvent(IProjection proj){
		return new ProjectionEvent(proj, PROJECTION_EVENT);
	}

	/**
	 * <p>Creates a new projection event of the specified type.</p>
	 *
	 * @param proj the new projection
	 * @param eventType identifier of this kind of event
	 */
	private ProjectionEvent(IProjection proj, int eventType) {
		setEventType(eventType);
		newProyection = proj;
	}

	/**
	 * <p>Gets the new projection.</p>
	 *
	 * @return the new projection
	 */
	public IProjection getNewProjection() {
		return newProyection;
	}
}
