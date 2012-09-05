package com.iver.utiles.extensionPointsOld;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Clase de utilidad usada para crear las extensiones.
 * 
 * Esta clase presenta un par de metodos estaticos para permitir crear un objeto
 * a partir de una clase.
 * 
 * @author jjdelcerro
 * @deprecated @see org.gvsig.tools.extensionPoint.ExtensionBuilder
 */
public abstract class ExtensionBuilder implements IExtensionBuilder {

	/**
	 * Crea un objeto de la clase indicada.
	 * 
	 * @param cls Clase de la que crear la instancia
	 * @return
	 * 
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static Object create(Class cls) throws InstantiationException, IllegalAccessException {
		Object obj = null;

		if( cls == null ) {
			return null;
		}
		obj = cls.newInstance();
		return obj;
	}
	
	/**
	 * Crea un objeto de la clase indicada.
	 * 
	 * Crea un objeto de la clase indicada pasandole al constructor
	 * los argumentos indicados en <i>args</i>.
	 * <br>
	 * @param cls Clase de la que crear la instancia
	 * @param args Argumentos que pasar al constructor.
	 * @return
	 * 
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static Object create(Class cls, Object [] args) throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		Object obj = null;
		Constructor create = null;
		Class [] types = new Class[args.length];
		
		if( cls == null ) {
			return null;
		}
		for( int n=0 ; n<args.length ; n++ ) {
			Object arg = args[n]; 
			types[n] = arg.getClass();
		}
		create = cls.getConstructor(types);
		obj = create.newInstance(args);
		return obj;
	}
	/**
	 * Crea un objeto de la clase indicada.
	 * 
	 * Crea un objeto de la clase indicada pasandole al constructor
	 * un como argumento un Map..
	 * <br>
	 * @param cls Clase de la que crear la instancia
	 * @param args Map a pasar como argumento al constructor.
	 * @return
	 * 
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static Object create(Class cls, Map args) throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		Object obj = null;
		Constructor create = null;
		Class [] types = new Class[1];
		Object [] argsx = new Object[1];
		
		if( cls == null ) {
			return null;
		}
		types[0] = Map.class;
		argsx[0] = args;
		create = cls.getConstructor(types);
		obj = create.newInstance(argsx);
		return obj;
	}
}
