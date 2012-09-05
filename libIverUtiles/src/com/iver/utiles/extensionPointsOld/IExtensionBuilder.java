package com.iver.utiles.extensionPointsOld;

import java.util.Map;

/**
 * Interface utilizado para indicar al registro de extensiones que no se trata
 * de una clase lo que hey registrado, si no una instancia de un objeto a usar
 * para crear la extension.
 * 
 * 
 * @author jjdelcerro
 * @deprecated @see org.gvsig.tools.extensionPoint.IExtensionBuilder
 */
public interface IExtensionBuilder {
	/**
	 * Crea una instancia de la extension y la retorna.
	 * <br>
	 * @return
	 */
	public Object create();
	
	/**
	 * Crea una instancia de la extension y la retorna.
	 * <br>
	 * En <i>args</i> recibira la lista de argumeentos a utilizar
	 * para crear la extension.
	 * <br>
	 * @param args
	 * @return
	 */
	public Object create(Object [] args);
	
	public Object create(Map args);
}
