/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
*
* Copyright (C) 2006-2007 IVER T.I. and Generalitat Valenciana.
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
*
* For more information, contact:
*
*  Generalitat Valenciana
*   Conselleria d'Infraestructures i Transport
*   Av. Blasco Ibáñez, 50
*   46010 VALENCIA
*   SPAIN
*
*      +34 963862235
*   gvsig@gva.es
*      www.gvsig.gva.es
*
*    or
*
*   IVER T.I. S.A
*   Salamanca 50
*   46005 Valencia
*   Spain
*
*   +34 963163400
*   dac@iver.es
*/

package org.gvsig.i18n;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>This class offers some methods to provide internationalization services
 * to other projects. All the methods are static.</p>
 *
 * <p>The most useful method is {@link #getText(String) getText(key)} (and family),
 * which returns the translation associated
 * with the provided key. The class must be initialized before getText can be
 * used.</p>
 *
 * <p>The typical usage sequence would be:</p>
 * <ul>
 * <li>Add some locale to the prefered locales list: <code>Messages.addLocale(new Locale("es"))</code></li>
 * <li>Add some resource file containing translations: <code>Messages.addResourceFamily("text", new File("."))</code></li>
 * <li>And finaly getText can be used: <code>Messages.getText("aceptar")</code></li>
 * </ul>
 *
 * <p>The resource files are Java properties files, which contain <code>key=translation</code>
 * pairs, one
 * pair per line. These files must be encoded using iso-8859-1 encoding, and unicode escaped
 * sequences must be used to include characters outside the former encoding.
 * There are several ways to specify the property file to load, see the different
 * addResourceFamily methods for details.</p>
 *
 * @author Cesar Martinez Izquierdo (cesar.martinez@iver.es)
 *
 */
public class Messages {
    private static Logger logger = LoggerFactory.getLogger("Messages");
    private static String _CLASSNAME = "org.gvsig.i18n.Messages";

    /* Each entry will contain a hashmap with translations. Each hasmap
     * contains the translations for one language, indexed by the
     * translation key. The translations for language (i) in the preferred locales
     * list are contained in the position (i) of the localeResources list */
    private static ArrayList localeResources = new ArrayList();
	private static ArrayList preferredLocales = new ArrayList(); // contains the ordered list of prefered languages/locales (class Locale)


	/* Set of resource families and classloaders used to load i18n resources. */
	private static Set resourceFamilies = new HashSet();
	private static Set classLoaders = new HashSet();

	/*
	 * The language considered the origin of translations, which will
	 * (possibly) be stored in a property file without language suffix
	 * (ie: text.properties instead of text_es.properties).
	 */
	private static String baseLanguage = "es";
	private static Locale baseLocale = new Locale(baseLanguage);

	/**
	 * <p>Gets the localized message associated with the provided key.
	 * If the key is not in the dictionary, return the key and register
	 * the failure in the log.</p>
	 *
	 * <p>The <code>callerName</code> parameter is only
	 * used as a label when logging, so any String can be used. However, a
	 * meaningful String should be used, such as the name of the class requiring
	 * the translation services, in order to identify the source of the failure
	 * in the log.</p>
	 *
	 * @param key         An String which identifies the translation that we want to get.
	 * @param callerName  A symbolic name given to the caller of this method, to
	 *                    show it in the log if the key was not found
	 * @return            an String with the message associated with the provided key.
	 *                    If the key is not in the dictionary, return the key. If the key
	 *                    is null, return null.
	 */
	public static String getText(String key, String callerName) {
		if (key==null) {
			return null;
		}
		for (int numLocale=0; numLocale<localeResources.size(); numLocale++) {
			// try to get the translation for any of the languagues in the preferred languages list
			String translation = ((Properties)localeResources.get(numLocale)).getProperty(key);
			if (translation!=null && !translation.equals("")) {
				return translation;
			}
		}
		logger.warn(callerName+ " -- Cannot find translation for "+key);
		return key;
	}

	public static String getText(String key,  String[] arguments, String callerName) {
		String translation = getText(key, callerName);
		if (translation!=null) {
			try {
				translation = MessageFormat.format(translation, arguments);
			}
			catch (IllegalFormatException ex) {
				logger.error(callerName+" -- Error formating key: "+key+" -- "+translation);
			}
		}
		return translation;
	}

	/**
	 * <p>Gets the localized message associated with the provided key.
	 * If the key is not in the dictionary or the translation is empty,
	 * return the key and register the failure in the log.</p>
	 *
	 * @param key     An String which identifies the translation that we want to get.
	 * @return        an String with the message associated with the provided key.
	 *                If the key is not in the dictionary or the translation is empty,
	 *                return the key. If the key is null, return null.
	 */
	public static String getText(String key) {
		return getText(key, _CLASSNAME);
	}

	public static String getText(String key, String[] arguments) {
		return getText(key, arguments, _CLASSNAME);
	}

	/**
	 * <p>Gets the localized message associated with the provided key.
	 * If the key is not in the dictionary or the translation is empty,
	 * it returns null and the failure is only registered in the log if
	 * the param log is true.</p>
	 *
	 * @param key	An String which identifies the translation that we want
	 * 				to get.
	 * @param log	Determines whether log a key failure or not
	 * @return		an String with the message associated with the provided key,
	 * 				or null if the key is not in the dictionary or the
	 * 				translation is empty.
	 */
	public static String getText(String key, boolean log) {
		return getText(key, _CLASSNAME, log);
	}

	public static String getText(String key, String[] arguments, boolean log) {
		String translation = getText(key, _CLASSNAME, log);
		if (translation!=null) {
			try {
				translation = MessageFormat.format(translation, arguments);
			}
			catch (IllegalFormatException ex) {
				if (log) {
					logger.error(_CLASSNAME+" -- Error formating key: "+key+" -- "+translation);
				}
			}
		}
		return translation;
	}

	/**
	 * <p>Gets the localized message associated with the provided key.
	 * If the key is not in the dictionary, it returns null and the failure
	 * is only registered in the log if the param log is true.</p>
	 *
	 * @param key         An String which identifies the translation that we want to get.
	 * @param callerName  A symbolic name given to the caller of this method, to
	 *                    show it in the log if the key was not found
	 * @param log         Determines whether log a key failure or not
	 * @return            an String with the message associated with the provided key,
	 *                    or null if the key is not in the dictionary.
	 */
	public static String getText(String key, String callerName, boolean log) {
		if (key==null) {
			return null;
		}
		for (int numLocale=0; numLocale<localeResources.size(); numLocale++) {
			// try to get the translation for any of the languagues in the preferred languages list
			String translation = ((Properties)localeResources.get(numLocale)).getProperty(key);
			if (translation!=null && !translation.equals("")) {
				return translation;
			}
		}
		if (log) {
			logger.warn(callerName+" -- Cannot find translation for "+key);
		}
		return null;
	}

	public static String getText(String key, String[] arguments, String callerName, boolean log) {
		String translation = getText(key, callerName, log);
		if (translation!=null) {
			try {
				translation = MessageFormat.format(translation, arguments);
			}
			catch (IllegalFormatException ex) {
				if (log) {
					logger.error(callerName+" -- Error formating key: "+key+" -- "+translation);
				}
			}
		}
		return translation;
	}

	/**
	 * <p>Adds an additional family of resource files containing some translations.
	 * A family is a group of files with a common baseName.
	 * The file must be an iso-8859-1 encoded file, which can contain any unicode
	 * character using unicode escaped sequences, and following the syntax:
	 * <code>key1=value1
	 * key2=value2</code>
	 * where 'key1' is the key used to identify the string and must not
	 * contain the '=' symbol, and 'value1' is the associated translation.</p>
	 * <p<For example:</p>
	 * <code>cancel=Cancelar
	 * accept=Aceptar</code>
	 * <p>Only one pair key-value is allowed per line.</p>
	 *
	 * <p>The actual name of the resource file to load is determined using the rules
	 * explained in the class java.util.ResourceBundle. Summarizing, for each language
	 * in the specified preferred locales list it will try to load a file with
	 *  the following structure: <code>family_locale.properties</code></p>
	 *
	 * <p>For example, if the preferred locales list contains {"fr", "es", "en"}, and
	 * the family name is "text", it will try to load the files "text_fr.properties",
	 * "text_es.properties" and finally "text_en.properties".</p>
	 *
	 * <p>Locales might be more specific, such us "es_AR"  (meaning Spanish from Argentina)
	 * or "es_AR_linux" (meaning Linux system preferring Spanish from Argentina). In the
	 * later case, it will try to load "text_es_AR_linux.properties", then
	 * "text_es_AR.properties" if the former fails, and finally "text_es.properties".</p>
	 *
	 * <p>The directory used to locate the resource file is determining by using the
	 * getResource method from the provided ClassLoader.</p>
	 *
	 * @param family    The family name (or base name) which is used to search
	 *                  actual properties files.
	 * @param loader    A ClassLoader which is able to find a property file matching
	 * 					the specified family name and the preferred locales
	 * @see             <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/util/ResourceBundle.html">ResourceBundle</a>
	 */
	public static void addResourceFamily(String family, ClassLoader loader) {
		addResourceFamily(family, loader, "");
	}

	/**
	 * <p>Adds an additional family of resource files containing some translations.
	 * The search path to locate the files is provided by the dirList parameter.</p>
	 *
	 * <p>See {@link addResourceFamily(String, ClassLoader)} for a discussion about the
	 * format of the property files and the way to determine the candidat files
	 * to load. Note that those methods are different in the way to locate the
	 * candidat files. This method searches in the provided paths (<code>dirList</code>
	 * parameter), while the referred method searches using the getResource method
	 * of the provided ClassLoader.</p>
	 *
	 * @param family    The family name (or base name) which is used to search
	 *                  actual properties files.
	 * @param dirList   A list of search paths to locate the property files
	 * @throws MalformedURLException
	 * @see             <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/util/ResourceBundle.html">ResourceBundle</a>
	 */
	public static void addResourceFamily(String family, File[] dirList) throws MalformedURLException{
		// use our own classloader
		URL[] urls = new URL[dirList.length];

			int i;
			for (i=0; i<urls.length; i++) {
				urls[i] = dirList[i].toURL();
			}

		ClassLoader loader = new MessagesClassLoader(urls);
		addResourceFamily(family, loader, "");
	}

	/**
	 * <p>Adds an additional family of resource files containing some translations.
	 * The search path to locate the files is provided by the dir parameter.</p>
	 *
	 * <p>See {@link addResourceFamily(String, ClassLoader)} for a discussion about the
	 * format of the property files and the way to determine the candidat files
	 * to load. Note that those methods are different in the way to locate the
	 * candidat files. This method searches in the provided path (<code>dir</code>
	 * parameter), while the referred method searches using the getResource method
	 * of the provided ClassLoader.</p>
	 *
	 * @param family    The family name (or base name) which is used to search
	 *                  actual properties files.
	 * @param dir       The search path to locate the property files
	 * @throws MalformedURLException
	 * @see             <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/util/ResourceBundle.html">ResourceBundle</a>
	 */
	public static void addResourceFamily(String family, File dir) throws MalformedURLException{
		// use our own classloader
		URL[] urls = new URL[1];
		urls[0] = dir.toURL();
		ClassLoader loader = new MessagesClassLoader(urls);
		addResourceFamily(family, loader, "");
	}


	/**
	 * <p>Adds an additional family of resource files containing some translations.
	 * The search path is determined by the getResource method from the
	 * provided ClassLoader.</p>
	 *
	 * <p>This method is identical to {@link addResourceFamily(String, ClassLoader)},
	 * except that it adds a <pode>callerName</code> parameter to show in the log.</p>
	 *
	 * <p>See {@link addResourceFamily(String, ClassLoader)} for a discussion about the
	 * format of the property files andthe way to determine the candidat files
	 * to load.</p>
	 *
	 * @param family      The family name (or base name) which is used to search
	 *                    actual properties files.
	 * @param loader      A ClassLoader which is able to find a property file matching
	 * 					  the specified family name and the preferred locales
	 * @param callerName  A symbolic name given to the caller of this method, to
	 *                    show it in the log if there is an error
	 * @see               <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/util/ResourceBundle.html">ResourceBundle</a>
	 */
	public static void addResourceFamily(String family, ClassLoader loader, String callerName) {
		String currentKey;
		Enumeration keys;
		Locale lang;
		Properties properties, translations;
		int totalLocales = preferredLocales.size();

		if (totalLocales == 0) {
			// if it's empty, warn about that
			logger.warn("There is not preferred languages list. Maybe the Messages class was not initialized");
		}

		resourceFamilies.add(family);
		classLoaders.add(loader);

		for (int numLocale=0; numLocale<totalLocales; numLocale++) { // for each language
			properties =  new Properties();

			lang = (Locale) preferredLocales.get(numLocale);
			translations = (Properties) localeResources.get(numLocale);

			addResourceFamily(lang, translations, family, loader, callerName);
		}
	}

	private static void addResourceFamily(Locale lang, Properties translations,
			String family, ClassLoader loader, String callerName) {
		Properties properties = new Properties();
		String resource = family.replace('.', '/') + "_" + lang.toString()
				+ ".properties";
		InputStream is = loader.getResourceAsStream(resource);
		if (is != null) {
			try {
				properties.load(is);
			} catch (IOException e) {
			}
		} else if (lang.equals(baseLocale)) {
			// try also "text.properties" for the base language
			is = loader.getResourceAsStream(family.replace('.', '/')
					+ ".properties");


			if (is != null) {
				try {
					properties.load(is);
				} catch (IOException e) {
				}
			}

		}
		Enumeration keys = properties.keys();
		while (keys.hasMoreElements()) {
			String currentKey = (String) keys.nextElement();
			if (!translations.containsKey(currentKey)) {
				translations
						.put(currentKey, properties.getProperty(currentKey));
			}
		}

	}

	/**
	 * <p>Adds an additional family of resource files containing some translations.</p>
	 *
	 * <p>This method is identical to {@link addResourceFamily(String, ClassLoader, String)},
	 * except that it uses the caller's class loader.</p>
	 *
	 * <p>See {@link addResourceFamily(String, ClassLoader)} for a discussion about the
	 * format of the property files and the way to determine the candidat files
	 * to load.</p>
	 *
	 * @param family      The family name (or base name) which is used to search
	 *                    actual properties files.
	 * @param callerName  A symbolic name given to the caller of this method, to
	 *                    show it in the log if there is an error. This is only used
	 *                    to show
	 *                    something meaningful in the log, so you can use any string
	 * @see               <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/util/ResourceBundle.html">ResourceBundle</a>
	 */
	public static void addResourceFamily(String family, String callerName) {
		addResourceFamily(family, Messages.class.getClassLoader(), callerName);
	}


	/**
	 * Returns an ArrayList containing the ordered list of prefered Locales
	 * Each element of the ArrayList is a Locale object.
	 *
	 * @return an ArrayList containing the ordered list of prefered Locales
	 * Each element of the ArrayList is a Locale object.
	 */
	public static ArrayList getPreferredLocales() {
		return preferredLocales;
	}

	/**
	 * <p>Sets the ordered list of preferred locales.
	 * Each element of the ArrayList is a Locale object.</p>
	 *
	 * <p>Note that calling this method does not load any translation, it just
	 * adds the language to the preferred locales list, so this method must
	 * be always called before the translations are loaded using
	 * the addResourceFamily() methods.</p>
	 *
	 * <p>It there was any language in the preferred locale list, the language
	 * and its associated translations are deleted.</p>
	 *
	 *
	 * @param preferredLocales an ArrayList containing Locale objects.
	 * The ArrayList represents an ordered list of preferred locales
	 */
	public static void setPreferredLocales(ArrayList preferredLocalesList) {
		// delete all existing locales
		Iterator oldLocales = preferredLocales.iterator();
		while (oldLocales.hasNext()) {
			removeLocale((Locale) oldLocales.next());
		}

		// add the new locales now
		for (int numLocale=0; numLocale < preferredLocalesList.size(); numLocale++) {
			addLocale((Locale) preferredLocalesList.get(numLocale));
		}
	}

	/**
	 * Adds a Locale at the end of the ordered list of preferred locales.
	 * Note that calling this method does not load any translation, it just
	 * adds the language to the preferred locales list, so this method must
	 * be always called before the translations are loaded using
	 * the addResourceFamily() methods.
	 *
	 * @param lang   A Locale object specifying the locale to add
	 */
	public static void addLocale(Locale lang) {
		if (!preferredLocales.contains(lang)) { // avoid duplicates
				preferredLocales.add(lang); // add the lang to the ordered list of preferred locales
				Properties dict = new Properties();
				localeResources.add(dict); // add a hashmap which will contain the translation for this language
		}
	}

	/**
	 * Removes the specified Locale from the list of preferred locales and the
	 * translations associated with this locale.
	 *
	 * @param lang   A Locale object specifying the locale to remove
	 * @return       True if the locale was in the preferred locales list, false otherwise
	 */
	public static boolean removeLocale(Locale lang) {
		int numLocale = preferredLocales.indexOf(lang);
		if (numLocale!=-1) { // we found the locale in the list
			try {
				preferredLocales.remove(numLocale);
				localeResources.remove(numLocale);
			}
			catch (IndexOutOfBoundsException ex) {
				logger.warn(_CLASSNAME + "." + "removeLocale: " + ex.getLocalizedMessage());
			}
			return true;
		}
		return false;
	}

	/**
	 * Cleans the translation tables (removes all the translations from memory).
	 */
	public static void removeResources() {
		for (int numLocale=0; numLocale<localeResources.size(); numLocale++) {
			((Properties)localeResources.get(numLocale)).clear();
		}
	}

	/**
	 * The number of translation keys which have been loaded till now
	 * (In other words: the number of available translation strings).
	 *
	 * @param lang The language for which we want to know the number of translation keys
	 * return The number of translation keys for the provided language.
	 */
	protected static int size(Locale lang) {
		int numLocale = preferredLocales.indexOf(lang);
		if (numLocale!=-1) {
			return ((Properties)localeResources.get(numLocale)).size();
		};
		return 0;
	}

	protected static Set keySet(Locale lang) {
		int numLocale = preferredLocales.indexOf(lang);
		if (numLocale!=-1) {
			return ((Properties)localeResources.get(numLocale)).keySet();
		} else {
			return null;
		}
	}

	/**
	 * Checks if some locale has been added to the preferred locales
	 * list, which is necessary before loading any translation because
	 * only the translations for the preferred locales are loaded.
	 *
	 * @return
	 */
	public static boolean hasLocales() {
		return preferredLocales.size()>0;
	}

	/**
	 * Gets the base language, the language considered the origin of
	 * translations, which will be (possibly) stored in a property
	 * file without language suffix
	 * (ie: text.properties instead of text_es.properties).
	 */
	public static String getBaseLanguage() {
		return baseLanguage;
	}

	/**
	 * Sets the base language, the language considered the origin of
	 * translations, which will be (possibly)
	 * stored in a property file without language suffix
	 * (ie: text.properties instead of text_es.properties).
	 *
	 * @param lang The base language to be set
	 */
	public static void setBaseLanguage(String lang) {
		baseLanguage = lang;
		baseLocale = new Locale(baseLanguage);
	}

	/*
	 * Searches the subdirectories of the provided directory, finding
	 * all the translation files, and constructing a list of available translations.
	 * It reports different country codes or variants, if available.
	 * For example, if there is an en_US translation and an en_GB translation, both
	 * locales will be present in the Vector.
	 *
	 * @return
	 */

	/**
	 *
	 * @return A Vector containing the available locales. Each element is a Locale object
	 */
	/*public static Vector getAvailableLocales() {
		return _availableLocales;
	}*/

	/**
	 *
	 * @return A Vector containing the available languages. Each element is an String object
	 */
	/*public static Vector getAvailableLanguages() {
		Vector availableLanguages = new Vector();
		Locale lang;
		Enumeration locales = _availableLocales.elements();
		while (locales.hasMoreElements()) {
			lang = (Locale) locales.nextElement();
			availableLanguages.add(lang.getLanguage());
		}
		return availableLanguages;
	}*/

	public static Properties getAllTexts(Locale lang) {
		Properties texts = new Properties();
		getAllTexts(lang, null, texts);
		for (Iterator iterator = classLoaders.iterator(); iterator.hasNext();) {
			getAllTexts(lang, (ClassLoader) iterator.next(), texts);
		}
		return texts;
	}

	private static void getAllTexts(Locale lang, ClassLoader classLoader,
			Properties texts) {
		ClassLoader loader = classLoader == null ? Messages.class
				.getClassLoader() : classLoader;

		for (Iterator iterator = resourceFamilies.iterator(); iterator
				.hasNext();) {
			String family = (String) iterator.next();
			addResourceFamily(lang, texts, family, loader,
					"Messages.getAllTexts");
		}
	}

}