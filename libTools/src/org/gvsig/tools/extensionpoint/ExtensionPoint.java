package org.gvsig.tools.extensionpoint;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public interface ExtensionPoint {

	/**
	 * Clase que representa un registro dentro de un punto de extension.
	 *
	 * @author jjdelcerro
	 *
	 */
	public interface Extension {

		/**
		 * Obtiene Nombre de la extension registrada.
		 *
		 * @return
		 */
		public String getName();

		/**
		 * Obtienen la descripcion de la extension registrada.
		 *
		 * @return
		 */
		public String getDescription();

		/**
		 * Obtiene la lista de alias que existen para este registro de
		 * extension.
		 *
		 * @return
		 */
		public List getAlias();

		/**
		 * Inica si el registro de la extension contiene una clase o una
		 * instancia que actua como factoria. En caso de que contenga una
		 * instancia esta implementara el interface ExtensionBuilder.
		 *
		 * @return true si el registro uso un ExtensioBuilder en lugar de una
		 *         clase.
		 */
		public boolean isBuilder();

		/**
		 * Obtiene la clase asociada a este registro de extension.
		 *
		 * @return la clase o null si isBuilder retorna true.
		 */
		public Class getExtension();

		/**
		 * Obtiene la instancia del ExtenstionBuilder asociada a este registro.
		 *
		 * @return una instancia de ExtensionBuilder o null si isBuilder retorna
		 *         false.
		 */
		public ExtensionBuilder getBuilder();

		public Object create() throws InstantiationException,
			IllegalAccessException;

		public Object create(Object[] args) throws SecurityException,
			IllegalArgumentException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException;

		public Object create(Map args) throws SecurityException,
			IllegalArgumentException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException;

	}

	/**
	 * Obtiene el nombre del punto de extension. El nombre de un punto de
	 * extension se fija en la creacion de este y no puede ser modificado
	 * posteriormente.
	 *
	 * @return
	 */
	public String getName();

	/**
	 * Obtiene la descripcion del punto de extension.
	 *
	 * @return
	 */
	public String getDescription();

	/**
	 * Permite actualizar la descripcion del punto de extension.
	 *
	 * @param description
	 */
	public void setDescription(String description);

	/**
	 * Añade una extension nueva basada en una clase al registro de extensiones.
	 * La nueva extension se añadira al final de las ya existentes.
	 *
	 * @param name
	 *            , nombre de la extension
	 * @param description
	 *            , descripcion de la extenscion.
	 * @param extension
	 *            , clase que implementa esa extension
	 * @return una instancia de la extension creada.
	 */
	public Extension append(String name, String description, Class extension);

	/**
	 * Añade una extension nueva basada en ExtensioBuilder al registro de
	 * extensiones. La nueva extension se añadira al final de las ya existentes.
	 *
	 * @param name
	 *            , nombre de la extension
	 * @param description
	 *            , descripcion de la extenscion.
	 * @param builder
	 *            , instancia de ExtensionBuilder usada para crear la extension.
	 * @return una instancia de la extension creada.
	 */
	public Extension append(String name, String description,
			ExtensionBuilder builder);

	/**
	 * Añade una extension nueva basada en una clase al registro de extensiones.
	 * La nueva extension se insertara al principio de las ya existentes.
	 *
	 * @param name
	 *            , nombre de la extension
	 * @param description
	 *            , descripcion de la extenscion.
	 * @param extension
	 *            , clase que implementa esa extension
	 * @return una instancia de la extension creada.
	 */
	public Extension insert(String name, String description, Class extension);

	/**
	 * Añade una extension nueva basada en ExtensioBuilder al registro de
	 * extensiones. La nueva extension se insertara al principio de las ya
	 * existentes.
	 *
	 * @param name
	 *            , nombre de la extension
	 * @param description
	 *            , descripcion de la extenscion.
	 * @param builder
	 *            , instancia de ExtensionBuilder usada para crear la extension.
	 * @return una instancia de la extension creada.
	 */
	public Extension insert(String name, String description,
			ExtensionBuilder builder);

	/**
	 * Añade una extension nueva basada en una clase al registro de extensiones.
	 * La nueva extension se insertara antes de la extension registrada con el
	 * nombre indicado en beforeName.
	 *
	 * @param beforeName
	 *            , nombre de la extension delante de la que se insertara la
	 *            nueva.
	 * @param name
	 *            , nombre de la extension
	 * @param description
	 *            , descripcion de la extenscion.
	 * @param extension
	 *            , clase que implementa esa extension
	 * @return una instancia de la extension creada.
	 */
	public Extension insert(String beforeName, String name, String description,
			Class extension);

	/**
	 * Añade una extension nueva basada en ExtensioBuilder al registro de
	 * extensiones. La nueva extension se insertara antes de la extension
	 * registrada con el nombre indicado en beforeName.
	 *
	 * @param beforeName
	 *            , nombre de la extension delante de la que se insertara la
	 *            nueva.
	 * @param name
	 *            , nombre de la extension
	 * @param description
	 *            , descripcion de la extenscion.
	 * @param builder
	 *            , instancia de ExtensionBuilder usada para crear la extension.
	 * @return una instancia de la extension creada.
	 */
	public Extension insert(String beforeName, String name, String description,
			ExtensionBuilder builder);

	/**
	 * Crea un alias para un registro de extension.
	 *
	 * @param name
	 *            , nombre de la extension sobre la que se va a añadir un alias.
	 * @param alias
	 *            , alias a crear.
	 * @return
	 */
	public boolean addAlias(String name, String alias);

	/**
	 * Devuelbe un iterador sobre las extensiones registradas en este punto de
	 * extension.
	 *
	 * @return
	 */
	public Iterator iterator();

	/**
	 * Devuelbe una lista con los nombres de las extensiones registradas en el
	 * punto de extension.
	 *
	 * @return
	 */
	public List getNames();

	/**
	 * Devuelbe el numero de registros que existen en el punto de extension.
	 *
	 * @return
	 */
	public int getCount();

	/**
	 * Permite obtener un registro de extension dado su nombre o alias.
	 *
	 * @param name
	 *            , nombre o alias de la extension
	 * @return extension pedida o null si no existe una con el nombre o alias
	 *         indicado.
	 */
	public Extension get(String name);

	/**
	 * Crea una instancia asociada al contenido del registro de extension de
	 * nombre o alias indicado. Si el registro tiene asociada una clase creara
	 * una nueva instancia de esta y la devolbera. Si tiene asociado un builder
	 * lo usara para construir una instancia y la devolvera.
	 *
	 * @param name
	 *            , de la entrada del registro de la que queremos obtener la
	 *            instancia.
	 * @return instancia creada o null si no existen una con el nombre o alias
	 *         indicado.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public Object create(String name) throws InstantiationException,
			IllegalAccessException;

	/**
	 * Crea una instancia asociada al contenido del registro de extension de
	 * nombre o alias indicado. Si el registro tiene asociada una clase creara
	 * una nueva instancia de esta y la devolbera. Si tiene asociado un builder
	 * lo usara para construir una instancia y la devolvera.
	 *
	 *
	 * @param name
	 *            , de la entrada del registro de la que queremos obtener la
	 *            instancia.
	 * @param args
	 *            , representa un array con los parametros a suministrar en la
	 *            creacion de la nueva instancia.
	 * @return instancia creada o null si no existen una con el nombre o alias
	 *         indicado.
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Object create(String name, Object[] args) throws SecurityException,
			IllegalArgumentException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException;

	/**
	 * Crea una instancia asociada al contenido del registro de extension de
	 * nombre o alias indicado. Si el registro tiene asociada una clase creara
	 * una nueva instancia de esta y la devolbera. Si tiene asociado un builder
	 * lo usara para construir una instancia y la devolvera.
	 *
	 *
	 * @param name
	 *            , de la entrada del registro de la que queremos obtener la
	 *            instancia.
	 * @param args
	 *            , representa un map que se pasara como primer parametro en la
	 *            construccion de la instancia.
	 * @return instancia creada o null si no existen una con el nombre o alias
	 *         indicado.
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public Object create(String name, Map args) throws SecurityException,
			IllegalArgumentException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException;

	public boolean has(String name);


}
