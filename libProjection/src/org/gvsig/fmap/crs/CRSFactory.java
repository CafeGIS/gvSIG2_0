package org.gvsig.fmap.crs;

import org.cresques.cts.ICRSFactory;
import org.cresques.cts.IProjection;

/**
 * Fabrica de CRS.
 * Centraliza las peticiones de creación de objetos CRS de todo fmap.
 * @author luisw
 *
 */
public class CRSFactory {
	public static ICRSFactory cp = null;

	public static IProjection getCRS(String code) {
		return cp.get(code);
	}
}
