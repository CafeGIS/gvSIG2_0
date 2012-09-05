package org.gvsig.fmap.mapcontext.rendering.legend;


/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class NullIntervalValue implements IInterval {

	public boolean isInInterval(Object v) {
		return (v == null);
	}

	/**
	 * Crea un nuevo NullInterval.
	 */
	public NullIntervalValue() {
	}
	/* (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.rendering.IInterval#toString()
	 */
	public String toString() {
		return "Default";
	}
}
