/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Gobernment (CIT)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 * 
 */

/*
 * AUTHORS (In addition to CIT):
 * 2008 {DiSiD Technologies}  {New extension for installation and update of text translations}
 */
package org.gvsig.i18n.impl;

import java.io.*;
import java.security.AccessController;
import java.util.*;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.gvsig.i18n.I18nException;
import org.gvsig.i18n.I18nManager;
import org.gvsig.i18n.Messages;

import sun.security.action.GetPropertyAction;

import com.iver.andami.Launcher;
import com.iver.andami.PluginServices;
import com.iver.andami.config.generate.AndamiConfig;
import com.iver.utiles.StringUtilities;
import com.iver.utiles.XMLEntity;

/**
 * Implementation of the I18nManager interface.
 * 
 * @author <a href="mailto:dcervera@disid.com">David Cervera</a>
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public class I18nManagerImpl implements I18nManager {

    private static final String LOCALES_FILE_NAME = "locales.csv";

    private static final String CSV_SEPARATOR = ",";

    private static final String I18N_EXTENSION = "org.gvsig.i18n";

    private static final String VARIANT = "variant";

    private static final String COUNTRY = "country";

    private static final String LANGUAGE = "language";

    private static final String REGISTERED_LOCALES_PERSISTENCE = "RegisteredLocales";

    private static final I18nManager DEFAULT = new I18nManagerImpl();

    private Set registeredLocales;

    /**
     * The list of default reference locales. The last one will be used to get
     * all the keys when translating to a new locale.
     */
    private Locale[] referenceLocales = new Locale[] { ENGLISH, SPANISH };

    private Locale[] defaultLocales = new Locale[] {
    // Default supported locales
	    new Locale("ca"), // Catalan
	    new Locale("cs"), // Czech
	    new Locale("de"), // German
	    ENGLISH, // English
	    SPANISH, // Spanish
	    new Locale("eu"), // Basque
	    new Locale("fr"), // French
	    new Locale("gl"), // Galician
	    new Locale("it"), // Italian
	    new Locale("pl"), // Polish
	    new Locale("pt"), // Portuguese
	    new Locale("ro"), // Romanian
	    new Locale("zh"), // Chinese
        new Locale("ru"), // Russian
        new Locale("el"), // Greek
	    new Locale("ro"), // Romanian
	    new Locale("pl"), // Polish
    };

    /**
     * Returns the unique instance of the I18nManager.
     * 
     * @return the unique instance
     */
    public static I18nManager getInstance() {
	return DEFAULT;
    }

    public static String capitalize(String text) {
	// Convert the first letter to uppercase
	String capitalLetter = new String(new char[] { Character
		.toUpperCase(text.charAt(0)) });
	return capitalLetter.concat(text.substring(1));
    }

    /**
     * Empty constructor.
     */
    I18nManagerImpl() {
    }

    public Locale[] getInstalledLocales() {
	if (registeredLocales == null) {

	    XMLEntity child = getRegisteredLocalesPersistence();

	    // If the list of registered locales is not already persisted,
	    // this should be the first time gvSIG is run with the I18nPlugin
	    // so we will take the list of default locales
	    if (child == null) {
		Locale[] defaultLocales = getDefaultLocales();
		registeredLocales = new HashSet(defaultLocales.length);
		for (int i = 0; i < defaultLocales.length; i++) {
		    registeredLocales.add(defaultLocales[i]);
		}
		storeInstalledLocales();
	    } else {
		XMLEntity localesEntity = getRegisteredLocalesPersistence();
		registeredLocales = new HashSet(localesEntity
			.getChildrenCount());
		for (int i = 0; i < localesEntity.getChildrenCount(); i++) {
		    XMLEntity localeEntity = localesEntity.getChild(i);
		    String language = localeEntity.getStringProperty(LANGUAGE);
		    String country = localeEntity.getStringProperty(COUNTRY);
		    String variant = localeEntity.getStringProperty(VARIANT);
		    Locale locale = new Locale(language, country, variant);
		    registeredLocales.add(locale);
		}
	    }
	}

	return (Locale[]) registeredLocales
		.toArray(new Locale[registeredLocales.size()]);
    }

    public void uninstallLocale(Locale locale) throws I18nException {
	if (getCurrentLocale().equals(locale) || isReferenceLocale(locale)) {
	    throw new UninstallLocaleException(locale);
	}

	if (registeredLocales.remove(locale)) {
	    // Remove from the configured locale list
	    storeInstalledLocales();

	    // Remove the resource bundle file
	    File bundleFile = new File(getResourcesFolder(),
		    getResourceFileName(locale));

	    if (bundleFile.exists()) {
		bundleFile.delete();
	    }
	}
    }

    public String getDisplayName(Locale locale) {
	return getDisplayName(locale, locale);
    }

    public String getDisplayName(Locale locale, Locale displayLocale) {
	StringBuffer name = new StringBuffer(getLanguageDisplayName(locale,
		displayLocale));

	if (!isEmpty(locale.getCountry())) {
	    name.append(" - ");
	    name.append(locale.getDisplayCountry(displayLocale));
	}

	if (!isEmpty(locale.getVariant())) {
	    name.append(" - ");
	    name.append(locale.getDisplayVariant(displayLocale));
	}

	name.append(" (").append(locale.toString()).append(")");

	return name.toString();
    }

    private boolean isEmpty(String text) {
	return text == null || text.trim().length() == 0;
    }

    public String getLanguageDisplayName(Locale locale) {
	return getLanguageDisplayName(locale, locale);
    }

    public String getLanguageDisplayName(Locale locale, Locale displayLocale) {

	String displayName;

	// Correction for the Basque language display name,
	// show it in Basque, not in Spanish
	if ("eu".equals(locale.getLanguage())
		&& "vascuence".equals(locale.getDisplayLanguage())) {
	    displayName = "Euskera";
	}
	// Patch for Valencian/Catalan
	else if ("ca".equals(locale.getLanguage())) {
            // displayName = Messages.getText("__valenciano");
            // if ("__valenciano".equals(displayName)) {
            // displayName = Messages.getText("__catalan");
            // }
            displayName = "Valencià";
	} else {
	    displayName = locale.getDisplayLanguage(displayLocale);
	}

	return capitalize(displayName);
    }

    public Locale getCurrentLocale() {
	return Locale.getDefault();
    }

    public Locale getDefaultSystemLocale() {
	String language, region, country, variant;
	language = (String) AccessController
		.doPrivileged(new GetPropertyAction("user.language", "en"));
	// for compatibility, check for old user.region property
	region = (String) AccessController.doPrivileged(new GetPropertyAction(
		"user.region"));
	if (region != null) {
	    // region can be of form country, country_variant, or _variant
	    int i = region.indexOf('_');
	    if (i >= 0) {
		country = region.substring(0, i);
		variant = region.substring(i + 1);
	    } else {
		country = region;
		variant = "";
	    }
	} else {
	    country = (String) AccessController
		    .doPrivileged(new GetPropertyAction("user.country", ""));
	    variant = (String) AccessController
		    .doPrivileged(new GetPropertyAction("user.variant", ""));
	}
	return new Locale(language, country, variant);
    }

    public void setCurrentLocale(Locale locale) {
	AndamiConfig config = Launcher.getAndamiConfig();
	config.setLocaleLanguage(locale.getLanguage());
	config.setLocaleCountry(locale.getCountry());
	config.setLocaleVariant(locale.getVariant());
    }

    public Locale[] installLocales(File importFile) throws I18nException {
	List importLocales = new ArrayList();

	try {
	    ZipFile zipFile = new ZipFile(importFile);

	    Map locales = getZipFileNonReferenceLocales(zipFile);

	    if (locales == null || locales.size() == 0) {
		return null;
	    }

	    for (Iterator iterator = locales.entrySet().iterator(); iterator
		    .hasNext();) {
		Entry entry = (Entry) iterator.next();

		String fileName = (String) entry.getKey();
		Locale locale = (Locale) entry.getValue();
		importLocales.add(locale);

		// Add the locale to the list of installed ones, if it
		// is new, otherwise, update the texts.
		if (!registeredLocales.contains(locale)) {
		    registeredLocales.add(locale);
		    storeInstalledLocales();
		}

		// Replace the old bundle with the new one
		ZipEntry zipEntry = zipFile.getEntry(fileName);
		InputStream is = zipFile.getInputStream(zipEntry);
		BufferedReader reader = new BufferedReader(
			new InputStreamReader(is));

		File bundleFile = getResourceFile(locale);
		FileWriter fileWriter = new FileWriter(bundleFile);
		BufferedWriter writer = new BufferedWriter(fileWriter);

		String line;
		while ((line = reader.readLine()) != null) {
		    writer.write(line);
		    writer.write('\n');
		}
		writer.flush();
		writer.close();
		fileWriter.close();
		reader.close();
		is.close();
	    }

	} catch (Exception ex) {
	    throw new InstallLocalesException(importFile, ex);
	}

	return (Locale[]) importLocales
		.toArray(new Locale[importLocales.size()]);
    }

    public void exportLocaleForUpdate(Locale locale, Locale referenceLocale,
	    File exportFile) throws I18nException {

	exportLocalesForUpdate(new Locale[] { locale }, referenceLocale,
		exportFile);
    }

    public void exportLocalesForUpdate(Locale[] locales,
	    Locale referenceLocale, File exportFile) throws I18nException {

	exportLocale(locales, new Locale[] { referenceLocale }, exportFile,
		true);
    }
    
    public void exportLocaleForTranslation(Locale locale,
	    Locale referenceLocale, File exportFile) throws I18nException {
	
	exportLocaleForTranslation(locale, new Locale[] { referenceLocale },
		exportFile);
    }

    public void exportLocaleForTranslation(Locale locale,
	    Locale[] referenceLocales, File exportFile) throws I18nException {

	exportLocale(new Locale[] { locale }, referenceLocales, exportFile,
		false);
    }

    public Locale[] getReferenceLocales() {
	return referenceLocales;
    }

    public void setReferenceLocales(Locale[] referenceLocales) {
	this.referenceLocales = referenceLocales;
    }

    public void setDefaultLocales(Locale[] defaultLocales) {
	this.defaultLocales = defaultLocales;
    }

    private void exportLocale(Locale[] locales, Locale[] referenceLocales,
	    File exportFile, boolean update) throws I18nException {

	Locale[] refArray = getReferenceLocalesToExport(locales,
		referenceLocales);

	try {
	    FileOutputStream fos = new FileOutputStream(exportFile);
	    ZipOutputStream zipos = new ZipOutputStream(fos);

	    // Create the index file
	    writeZipFileLocales(zipos, locales, refArray);

	    PrintStream ps = new PrintStream(zipos);
	    Map texts = null;

	    if (update) {
		// First, export the locales to update
		if (locales != null) {
		    for (int i = 0; i < locales.length; i++) {
			texts = getAllTexts(locales[i]);
			putResourceInZip(zipos, ps, texts,
				getResourceFileName(locales[i]));
		    }
		}
	    } else { // translate
		// First, export the locales to translate, taking the keys from
		// the reference locales, but without values
		// We will use the keys of the reference locales
		texts = getAllTexts(referenceLocales[0]);
		if (locales != null) {
		    for (int i = 0; i < locales.length; i++) {
			putResourceInZip(zipos, ps, texts,
				getResourceFileName(locales[i]), false);
		    }
		}
	    }

	    // Next, export the reference locales
	    if (refArray != null) {
		for (int i = 0; i < refArray.length; i++) {
		    texts = getAllTexts(refArray[i]);
		    putResourceInZip(zipos, ps, texts,
			    getResourceFileName(refArray[i]));
		}
	    }

	    ps.flush();
	    ps.close();
	    zipos.close();
	    fos.close();
	} catch (IOException ex) {
	    throw new ExportLocaleException(locales, ex);
	}
    }

    /**
     * Returns the list of reference locales to export, as the union of the
     * default reference locales list and the one selected as reference. The
     * locales to translate or update are extracted from the list.
     */
    private Locale[] getReferenceLocalesToExport(Locale[] locales,
	    Locale[] referenceLocalesSelected) {
	// The reference locales to export are the default ones plus the
	// selected by the user.
	Set exportRefLocales = new HashSet(referenceLocales.length);
	for (int i = 0; i < referenceLocales.length; i++) {
	    exportRefLocales.add(referenceLocales[i]);
	}
	if (referenceLocalesSelected != null) {
	    for (int i = 0; i < referenceLocalesSelected.length; i++) {
		exportRefLocales.add(referenceLocalesSelected[i]);
	    }
	}
	if (locales != null) {
	    for (int i = 0; i < locales.length; i++) {
		exportRefLocales.remove(locales[i]);
	    }
	}
	Locale[] refArray = (Locale[]) exportRefLocales
		.toArray(new Locale[exportRefLocales.size()]);
	return refArray;
    }

    /**
     * Returns all the localized texts and its keys for a locale.
     */
    private Map getAllTexts(Locale locale) {
	return Messages.getAllTexts(locale);
    }

    private Map getZipFileNonReferenceLocales(ZipFile zipFile)
	    throws I18nException {
	ZipEntry zipEntry = zipFile.getEntry(LOCALES_FILE_NAME);

	if (zipEntry == null) {
	    return null;
	}

	Map locales;
	try {
	    InputStream is = zipFile.getInputStream(zipEntry);
	    BufferedReader reader = new BufferedReader(
		    new InputStreamReader(is));

	    locales = new HashMap(2);
	    String line;
	    while ((line = reader.readLine()) != null) {
		// The excepted format is:
		// FILENAME,LANGUAGE,COUNTRY,VARIANT,IS_REFERENCE
		StringTokenizer st = new StringTokenizer(line, CSV_SEPARATOR,
			true);
		// First: locale file name (required)
		String fileName = st.nextToken();
		if (CSV_SEPARATOR.equals(fileName)) {
		    throw new LocaleFileNameRequiredException(line);
		} else {
		    // Read the next separator
		    st.nextToken();
		}
		// Second: the locale language (required)
		String language = st.nextToken();
		if (CSV_SEPARATOR.equals(language)) {
		    throw new LocaleLanguageRequiredException(line);
		} else {
		    // Read the next separator
		    st.nextToken();
		}
		// Third: the country
		String country = st.nextToken();
		if (CSV_SEPARATOR.equals(country)) {
		    country = null;
		} else {
		    // Read the next separator
		    st.nextToken();
		}
		// Fourth: the variant
		String variant = st.nextToken();
		if (CSV_SEPARATOR.equals(variant)) {
		    variant = null;
		} else {
		    // Read the next separator
		    st.nextToken();
		}
		// Fifth: is a reference locale?
		String refStr = st.nextToken();
		if (CSV_SEPARATOR.equals(refStr)) {
		    refStr = null;
		}

		// Only add non reference locales
		if (refStr != null && !"true".equals(refStr.toLowerCase())) {
		    // Variant only accepted if country defined
		    if (country == null) {
			variant = null;
		    }
		    country = country == null ? "" : country;
		    variant = variant == null ? "" : variant;
		    Locale locale = new Locale(language, country, variant);

		    locales.put(fileName, locale);
		}
	    }

	    reader.close();
	    is.close();
	} catch (IOException ex) {
	    throw new ReadCSVLocalesFileException(ex);
	}

	return locales;
    }

    private void writeZipFileLocales(ZipOutputStream zos, Locale[] locales,
	    Locale[] referenceLocales) throws IOException {
	ZipEntry zipEntry = new ZipEntry(LOCALES_FILE_NAME);

	zos.putNextEntry(zipEntry);
	BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(zos));

	if (locales != null) {
	    for (int i = 0; i < locales.length; i++) {
		writeLocaleEntry(locales[i], writer, false);
	    }
	}
	if (referenceLocales != null) {
	    for (int i = 0; i < referenceLocales.length; i++) {
		writeLocaleEntry(referenceLocales[i], writer, true);
	    }
	}

	writer.flush();
	zos.closeEntry();
    }

    /**
     * Writes the locale entry into a writer.
     * 
     * @param locale
     *            the locale to create the entry for
     * @param writer
     *            to write to
     * @param reference
     *            is it is a reference locale or not
     * @throws IOException
     *             if there is an error creating the locale entry
     */
    private void writeLocaleEntry(Locale locale, BufferedWriter writer,
	    boolean reference) throws IOException {
	String language = locale.getLanguage();
	String country = locale.getCountry();
	country = country == null ? "" : country;
	String variant = locale.getVariant();
	variant = variant == null ? "" : variant;

	writer.write(getResourceFileName(locale));
	writer.write(',');
	writer.write(language);
	writer.write(',');
	writer.write(country);
	writer.write(',');
	writer.write(variant);
	writer.write(',');
	writer.write(Boolean.toString(reference));
	writer.write('\n');
    }

    /**
     * Returns if a locale is one of the default reference ones.
     */
    private boolean isReferenceLocale(Locale locale) {
	for (int i = 0; i < referenceLocales.length; i++) {
	    if (referenceLocales[i].equals(locale)) {
		return true;
	    }
	}
	return false;
    }

    /**
     * Puts a new resource file into a Jar file.
     */
    private void putResourceInZip(ZipOutputStream zipos, PrintStream ps,
	    Map texts, String resourceFileName) throws IOException {

	putResourceInZip(zipos, ps, texts, resourceFileName, true);
    }

    /**
     * Puts a new resource file into a Jar file.
     */
    private void putResourceInZip(ZipOutputStream zipos, PrintStream ps,
	    Map texts, String resourceFileName, boolean withValue)
	    throws IOException {
	// Add ZIP entry for the resource bundle file
	zipos.putNextEntry(new ZipEntry(resourceFileName));

	for (Iterator iterator = texts.entrySet().iterator(); iterator
		.hasNext();) {
	    Entry entry = (Entry) iterator.next();
	    String keyEncoded = escape((String) entry.getKey(), true);
	    ps.print(keyEncoded);
	    ps.print("=");
	    if (withValue) {
		String valueEncoded = escape((String) entry.getValue(), false);
		ps.println(valueEncoded);
	    } else {
		ps.println();
	    }
	}

	ps.flush();

	// Close the ZIP entry, the file is complete
	zipos.closeEntry();
    }

    /**
     * Returns the file which contains the translations for a locale.
     */
    private File getResourceFile(Locale locale) {
	return new File(getResourcesFolder(), getResourceFileName(locale));
    }

    /**
     * Returns the name of the file which contains the translations for a
     * locale.
     */
    private String getResourceFileName(Locale locale) {
	StringBuffer fileName = new StringBuffer("text");

	// Spanish without country is the default locale
	if (!(isEmpty(locale.getCountry()) && "es".equals(locale.getLanguage()))) {
	    fileName.append('_').append(locale.getLanguage());
	}

	// Add the locale country
	if (!isEmpty(locale.getCountry())) {
	    fileName.append('_').append(locale.getCountry());
	}

	// Add the locale variant
	if (!isEmpty(locale.getVariant())) {
	    fileName.append('_').append(locale.getVariant());
	}

	fileName.append(".properties");
	return fileName.toString();
    }

    /**
     * Returns the folder where to store the resource bundle files.
     */
    private File getResourcesFolder() {
	return PluginServices.getPluginServices("com.iver.cit.gvsig")
		.getPluginDirectory();
    }

    /**
     * Returns the child XMLEntity with the RegisteredLocales.
     */
    private XMLEntity getRegisteredLocalesPersistence() {
	XMLEntity entity = getI18nPersistence();
	XMLEntity child = null;
	for (int i = entity.getChildrenCount() - 1; i >= 0; i--) {
	    XMLEntity tmpchild = entity.getChild(i);
	    if (tmpchild.getName().equals(REGISTERED_LOCALES_PERSISTENCE)) {
		child = tmpchild;
		break;
	    }
	}
	return child;
    }

    /**
     * Returns the I18n Plugin persistence.
     */
    private XMLEntity getI18nPersistence() {
	XMLEntity entity = PluginServices.getPluginServices(I18N_EXTENSION)
		.getPersistentXML();
	return entity;
    }

    /**
     * Returns the list of default locales bundled with gvSIG.
     */
    private Locale[] getDefaultLocales() {
	return defaultLocales;
    }

    /**
     * Stores the list of installed locales into the plugin persistence.
     */
    private void storeInstalledLocales() {
	XMLEntity localesEntity = getRegisteredLocalesPersistence();

	// Remove the previous list of registered languages
	if (localesEntity != null) {
	    XMLEntity i18nPersistence = getI18nPersistence();
	    for (int i = i18nPersistence.getChildrenCount() - 1; i >= 0; i--) {
		XMLEntity child = i18nPersistence.getChild(i);
		if (child.getName().equals(REGISTERED_LOCALES_PERSISTENCE)) {
		    i18nPersistence.removeChild(i);
		    break;
		}
	    }
	}

	// Create the new persistence for the registered languages
	localesEntity = new XMLEntity();

	localesEntity.setName(REGISTERED_LOCALES_PERSISTENCE);

	for (Iterator iterator = registeredLocales.iterator(); iterator
		.hasNext();) {
	    Locale locale = (Locale) iterator.next();
	    XMLEntity localeEntity = new XMLEntity();
	    localeEntity.setName(locale.getDisplayName());
	    localeEntity.putProperty(LANGUAGE, locale.getLanguage());
	    localeEntity.putProperty(COUNTRY, locale.getCountry());
	    localeEntity.putProperty(VARIANT, locale.getVariant());

	    localesEntity.addChild(localeEntity);
	}

	getI18nPersistence().addChild(localesEntity);
    }

    private String escape(String value, boolean replaceBlanks) {
	// First replace non printable characters
	if (replaceBlanks) {
	    value = StringUtilities.replace(value, " ", "\\ ");
	}
	value = StringUtilities.replace(value, ":", "\\:");
	value = StringUtilities.replace(value, "\n", "\\n");
	value = StringUtilities.replace(value, "\t", "\\t");
	value = StringUtilities.replace(value, "\b", "\\b");
	value = StringUtilities.replace(value, "\f", "\\f");
	value = StringUtilities.replace(value, "\r", "\\r");
	// value = StringUtilities.replace(value, "\\", "\\\\");
	// value = StringUtilities.replace(value, "\'", "\\\'");
	// value = StringUtilities.replace(value, "\"", "\\\"");

	// Next, encode in raw-unicode-escape
	return toRawUnicodeEncoded(value);
    }

    private String toRawUnicodeEncoded(String value) {
	StringBuffer sb = new StringBuffer();
	for (int i = 0; i < value.length(); i++) {
	    char c = value.charAt(i);
	    if (c <= 0x80) {
		sb.append(c);
	    } else {
		sb.append("\\u");
		String hexValue = Integer.toHexString((int) c);
		// Append 0 if the hex value has less than 4 digits
		for (int j = hexValue.length(); j < 4; j++) {
		    sb.append('0');
		}
		sb.append(hexValue);
	    }
	}
	return sb.toString();
    }
}