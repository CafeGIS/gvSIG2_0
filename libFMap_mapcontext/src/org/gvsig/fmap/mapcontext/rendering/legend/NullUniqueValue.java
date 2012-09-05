package org.gvsig.fmap.mapcontext.rendering.legend;

import java.sql.Types;


/**
 * Clase que extiende a NullValue para especificar los vlaores no representados
 * por ningún otro valor.
 *
 * @author Vicente Caballero Navarro
 */
public class NullUniqueValue extends NullValue {
	public int getSQLType() {
		return Types.OTHER;
	}

	public String toString() {
		return "Default";
	}
}
