package org.gvsig.tools.extensionpoint;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

public interface ExtensionPointManager {

	/**
	 * Crea un nuevo punto de extension y nos lo debuelve. El nuevo punto de
	 * extension no se añade al registro.
	 *
	 * @param name
	 *            , nombre del punto de extension a crear.
	 * @param description
	 *            , descripcion del punto de extension a crear.
	 * @return el punto de extension creado.
	 */
	public ExtensionPoint create(String name, String description);

	/**
	 * Añade el punto de extension indicado al registro de puntos de extension.
	 * Si el punto de extension ya existiese, lo actualia sustituyendo el
	 * anterior por el nuevo.
	 *
	 * @param extensionPoint
	 *            , punto de extension a añadir al registro.
	 * @return true si todo va bien, false si no.
	 */
	public boolean add(ExtensionPoint extensionPoint);

	/**
	 * Obtiene el punto de extension asociado al nombre indicado.
	 *
	 * @param name
	 *            , nombre del punto de extension que queremos obtener.
	 * @return el punto de extension requerido.
	 */
	public ExtensionPoint get(String name);

	/**
	 * Comprueba si un punto de extension existe.
	 *
	 * @return true si el punto de extension "name" existe.
	 */
	public boolean has(String name);

	/**
	 * Obtiene el numero de puntos de extension existentes.
	 *
	 * @return
	 */
	public int getCount();

	/**
	 * Obtiene un iterador sobre los puntos de extension existentes.
	 *
	 * @return
	 */
	public Iterator interator();

	/**
	 * Obtiene la lista de nombres de los puntos de extension existentes.
	 *
	 * @return
	 */
	public List getNames();

	/**
	 * Metodo de utilidad para facilitar la creacion de puntos de extension.
	 * Comprueba si el punto de extension "name" no existe y en ese caso lo crea
	 * y añade.
	 *
	 * @param nam
	 *            , nombre del punto de extension
	 * @param description
	 *            , descripcion del punto de extension
	 * @return el punto de extension recien creado o el ya existente con ese
	 *         nmbre
	 */
	public ExtensionPoint add(String name, String description);

	public ExtensionPoint add(String name);


	public Object createObject(Class klass, Object[] paramters)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, InstantiationException,
			IllegalAccessException, InvocationTargetException;
}
