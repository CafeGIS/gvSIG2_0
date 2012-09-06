package org.cresques.cts;

public interface ICRSFactory {
	
	public boolean doesRigurousTransformations();
	
    /**
     * Devuelve una proyeccion a partir de una cadena.
     * @param name abreviatura de la proyecccion (i.e. EPSG:23030)
     * @return Proyeccion si existe
     */
    public IProjection get(String name);
}
