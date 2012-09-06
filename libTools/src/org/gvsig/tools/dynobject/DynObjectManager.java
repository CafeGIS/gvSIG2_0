package org.gvsig.tools.dynobject;

import java.util.Iterator;
import java.util.List;

import org.gvsig.tools.dynobject.exception.DynMethodException;


public interface DynObjectManager {

	public int NULLCODE = -1;

	public DynClass createDynClass(String name, String description);

	public boolean add(DynClass dynClass);

	public DynClass add(String name, String description);

	public DynClass add(String name);

	/**
	 * Obtiene el la clase asociado al nombre indicado.
	 *
	 * @param name
	 *            , nombre de la clase que queremos obtener.
	 * @return la clase requerida.
	 */
	public DynClass get(String name);

	/**
	 * Comprueba si esta registrada una clase.
	 *
	 * @return true si la clase "name" esta registrada, false si no.
	 */
	public boolean has(String name);

	/**
	 * Obtiene el numero de clases registradas.
	 *
	 * @return
	 */
	public int getCount();

	/**
	 * Obtiene un iterador sobre las clases registradas.
	 *
	 * @return
	 */
	public Iterator interator();

	/**
	 * Obtiene la lista de nombres de las clases registradas.
	 *
	 * @return
	 */
	public List getNames();


	/**
	 * Crea un nuevo objeto asociandole como clase base la indicada como
	 * parametro.
	 *
	 * @param dynClass
	 * @return el nuevo DynObject
	 */
	public DynObject createDynObject(DynClass dynClass);

	/**
	 * Crea un nuevo objeto asociandole como clase base la indicada que tiene el
	 * nombre indicado.
	 * 
	 * @param dynClassName
	 * @return el nuevo DynObject
	 */
	public DynObject createDynObject(String dynClassName);

	/**
	 * Actualiza todas las DynClasses registradas para reflejar la
	 * herencia de forma adecuada.
	 */
	public void consolide();


	/**
	 * Register the method in the dynClass.
	 *
	 * @param dynClass class over the method is registred
	 * @param dynMethod method to registry
	 * @return unique code of method
	 */
	public int registerDynMethod(DynClass dynClass, DynMethod dynMethod);

	/**
	 * Register the method in the class.
	 *
	 * @param theClass class over the method is registred
	 * @param dynMethod method to registry
	 * @return unique code of method
	 */
	public int registerDynMethod(Class theClass, DynMethod dynMethod);


	/**
	 * Obtain the method for the indicated code of the dynObject.
	 *
	 * @param dynObject
	 * @param code code of the requeted method
	 * @return the required DynMethod
	 *
	 * @throws DynMethodException
	 */
	public DynMethod getDynMethod(DynObject dynObject, int code) throws DynMethodException ;

	public DynMethod getDynMethod(DynClass dynClass, int code) throws DynMethodException;

	public DynMethod getDynMethod(Object obj, int code) throws DynMethodException;

	public DynMethod getDynMethod(Class theClass, int code) throws DynMethodException;

	public DynMethod getDynMethod(int code) throws DynMethodException;

	/**
	 * Invoke the method of the indicated code for the object self, with
	 * parameters in context.
	 *
	 * @param self object over the method is invoked
	 * @param code code for the method to invoke
	 * @param context paramters of method
	 * @return return value for the method
	 * @throws DynMethodException
	 */
	public Object invokeDynMethod(Object self, int code, DynObject context) throws DynMethodException;


}
