package com.iver.utiles.extensionPointsOld;

import java.lang.reflect.InvocationTargetException;
import java.security.KeyException;
import java.util.*;

/**
 * Esta clase permite registrar extensiones para un punto de extension. <br>
 * <br>
 * La clase se comporta como un Map que mantiene el orden de insercion de los
 * elementos, para que puedan ser recorridos en ese orden. <br>
 * Ademas de registrar las extensiones para un punto de extension, presenta
 * metodos para facilitar la creacion de la extension. <br>
 * A la hora de registrar una extension, mediante el metodo <i>put</i>, podremos
 * suministrarle una clase o una instancia que implemente el interface
 * IExtensionBuilder. Si le suministramos una clase, cuando queramos crear la
 * extension mediante el metodo <i>create</i>, se creara una instancia de la
 * clase y se retornara. Si lo que se suministro fue una instancia que
 * implementa el interface IExtensionBuilder, se invocara al metodo
 * <i>create</i> de esta para crear la extension. <br>
 * 
 * @author jjdelcerro
 * 
 * @deprecated @see org.gvsig.tools.extensionPoint.ExtensionPoint
 */
public class ExtensionPoint extends LinkedHashMap {

    private static final long serialVersionUID = -5908427725588553371L;

	private String name;
	private String description;
	private Hashtable extensionDescriptions = new Hashtable();
	private Hashtable aliases = new Hashtable();

	/**
	 * Construye un punto de extension.
	 * <br>
	 * @param extensionPointName Nombre del punto de extension.
	 */
	public ExtensionPoint(String extensionPointName) {
		this.name = extensionPointName;
	}
	
	/**
	 * Construye un punto de extension.
	 * <br>
	 * @param extensionPointName Nombre del punto de extension
	 * @param description Descripcion del punto de extension
	 */
	public ExtensionPoint(String extensionPointName, String description) {
		this.name = extensionPointName;
		this.description = description;
	}
	
	/**
	 * Retorna el nombre de punto de extension.
	 * <br>
	 * @return Nombre del punto de extension
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Retorna la descripcion asociada al punto de extension.
	 * <br>
	 * @return descripcion del punto de extension
	 */
	public String getDescription() {
		return this.description;
	}
	
	/**
	 * Asocia una descripcion al punto de extension.
	 * <br>
	 * 
	 * @param description
	 */
	public void setDescripcion(String description) {
		this.description = description;
	}
	
	/**
	 * Retorna la descripcion asociada a una extension.
	 * <br>
	 * @param key 
	 * <br>
	 * @return descripcion del punto de extension
	 */
	public String getExtensionDescription(String key) {
		return (String)this.extensionDescriptions.get(key);
	}
	
	/**
	 * Asocia una descripcion a una extension registrada.
	 * <br>
	 * 
	 * @param key Nombre de la extension
	 * @param description
	 */	
	public void setExtensionDescription(String key,String description) {
		if (this.containsKey(key)) {
			this.extensionDescriptions.put(key,description);
		}
	}
	
	/**
	 * Añade una extension con su descripcion asociada
	 * 
	 * @param key clave de la extension
	 * @param decription descripcion de la extension
	 * @param value extension
	 */
	
	public Object put(String key, String description,Object value) {		
		this.extensionDescriptions.put(key,description);
		return super.put(key,value);
	}

	/**
	 * Añade una extension antes de la indicada con beforeKey,
	 * con su descripcion asociada
	 * 
	 * @param key clave de la extension
	 * @param decription descripcion de la extension
	 * @param value extension
	 */
	
	public Object insert(String beforeKey, String key, String description,Object value) {		
		boolean mover = false;
		Map tmp = new LinkedHashMap();

		for (Iterator i = this.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry e = (Map.Entry)i.next();
            if ( e.getKey().equals(beforeKey) ) {
            	mover = true;
            }
            if( mover ) {
            	tmp.put(e.getKey(), e.getValue());
            }
        }
		for (Iterator i = tmp.keySet().iterator(); i.hasNext(); ) {
			String key1 = (String)i.next();
			this.remove(key1);
		}
		if ( description!= null ) {
			this.extensionDescriptions.put(key,description);
		}
		Object returnValue = super.put(key,value);
		this.putAll(tmp);
		return returnValue;
	}
	
	/**
	 * Crea una extension.
	 * <br>
	 * Dado un nombre de extension asociada a este punto de extension, crea
	 * el objeto registrado para manejar la extension.
	 * <br>
	 * Si el objeto registrado para esa extension implementa el interface
	 * <i>IExtensionBuilder</i>, se invoca al metodo create para crear la instancia
	 * de la extension. 
	 * <br>
	 * Si no implementa este interface, debera ser una clase, y se creara una
	 * instancia de esa clase.
	 * <br>
	 * @param name Nombre de la extension a crear.
	 * @return La instancia creada de la extension.
	 * 
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public Object create(String name) throws InstantiationException, IllegalAccessException {
		Object extension = this.get(name);
		if (extension == null) {
			extension = this.get(this.aliases.get(name));
		}
		
		if( extension instanceof IExtensionBuilder ) {
			return ((IExtensionBuilder)extension).create();
		}
		return ExtensionBuilder.create((Class) extension);
	}
	
	/**
	 * Crea una extension.
	 * <br>
	 * Dado un nombre de extension asociada a este punto de extension, crea
	 * el objeto registrado para manejar la extension.
	 * <br>
	 * A la hora de crear la instancia de la extension, le pasara los parametros
	 * indicados en <i>args</i>.
	 * <br>
	 * Debido a que los argumentos se pasan como un array de objetos, no es posible
	 * pasar al constructor de la extension parametros de tipos basicos como <i>int</i>
	 * o <i>long</i>. Se deberan pasar como objetos y existir un constructor de la clase
	 * que los pueda recibir de esta manera.
	 * <br>
	 * @param name Nombre de la extension a crear.
	 * @param args Array de objetos a pasar como parametros en la construccion de la instancia de la extension.
	 * @return La instancia creada de la extension.
	 * 
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Object create(String name, Object [] args) throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		Object extension = this.get(name);
		if (extension == null) {
			extension = this.get(this.aliases.get(name));
		}
		
		if( extension instanceof IExtensionBuilder ) {
			return ((IExtensionBuilder)extension).create(args);
		}
		return ExtensionBuilder.create((Class) extension, args);
	}	

	public Object create(String name, Map args) throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		Object extension = this.get(name);
		if (extension == null) {
			extension = this.get(this.aliases.get(name));
		}
		
		if( extension instanceof IExtensionBuilder ) {
			return ((IExtensionBuilder)extension).create(args);
		}
		return ExtensionBuilder.create((Class) extension, args);
	}
	
	/**
	 * Crea un alias para una extension registrada.
	 * <br>
	 * @param item Nombre de la extension registrada.
	 * @param alias alias a añadir.
	 * 
	 **/	
	public void addAlias(String item, String alias) throws KeyException{
		if (!this.containsKey(item)) {
			throw new KeyException(item);
		}
		this.aliases.put(alias,item);		
	}
}
