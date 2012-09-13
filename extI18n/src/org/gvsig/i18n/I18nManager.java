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
package org.gvsig.i18n;

import java.io.File;
import java.util.Locale;

/**
 * Management of I18n and Locale related activities.
 * 
 * @author <a href="mailto:dcervera@disid.com">David Cervera</a>
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public interface I18nManager {

    public static final Locale SPANISH = new Locale("es");

    public static final Locale ENGLISH = new Locale("en");

    /**
     * Returns the display name of a locale language.
     * 
     * @param locale
     *            to get the language display name
     * @param displayLocale
     *            the locale to display the name
     * @return the locale language display name
     */
    String getLanguageDisplayName(Locale locale, Locale displayLocale);

    /**
     * Returns the display name of a locale language.
     * 
     * @param locale
     *            to get the language display name
     * @return the locale language display name
     */
    String getLanguageDisplayName(Locale locale);

    /**
     * Returns the display name of a locale in the given locale to be displayed.
     * 
     * @param locale
     *            to get the display name
     * @param displayLocale
     *            the locale to display the name
     * @return the locale display name
     */
    String getDisplayName(Locale locale, Locale displayLocale);

    /**
     * Returns the display name of a locale.
     * 
     * @param locale
     *            to get the display name
     * @return the locale display name
     */
    String getDisplayName(Locale locale);

    /**
     * Returns the current locale.
     * 
     * @return the current locale
     */
    Locale getCurrentLocale();

    /**
     * Sets the current application locale.
     * 
     * @param locale
     *            the current application locale
     */
    void setCurrentLocale(Locale locale);
    
    /**
     * Returns the application host system default locale.
     * 
     * @return the default system locale
     */
    Locale getDefaultSystemLocale();

    /**
     * Returns the list of locales supported by the current gvSIG installation.
     * 
     * @return the list of locales supported
     */
    Locale[] getInstalledLocales();

    /**
     * Installs or a new locale (or a list) and its translation, or updates an
     * already existing one.
     * 
     * @param importFile
     *            the jar or zip file which contains the locales and the
     *            translations as resource bundle files
     * @return the list of installed or updated locales
     * @throws I18nException
     *             if the locales could'n be installed
     */
    Locale[] installLocales(File importFile) throws I18nException;

    /**
     * Uninstalls a locale and its translation.
     * 
     * @param locale
     *            to uninstall
     * @throws I18nException
     *             if the locale can't be uninstalled
     */
    void uninstallLocale(Locale locale) throws I18nException;

    /**
     * Exports the translations of a locale to update or complete its
     * translation, into a jar file. Into the file is also included the
     * translations of another existing locale to be used as reference for
     * updating the locale.
     * 
     * @param locale
     *            the locale to update or complete
     * @param referenceLocale
     *            the locale to be used as reference
     * @param exportFile
     *            the jar file to export to
     * @throws I18nException
     *             if the locale could not be exported for update
     */
    void exportLocaleForUpdate(Locale locale, Locale referenceLocale,
	    File exportFile) throws I18nException;

    /**
     * Exports the translations of a list of locales to update or complete its
     * translation, into a jar file. Into the file is also included the
     * translations of another existing locale to be used as reference for
     * updating the locale.
     * 
     * @param locales
     *            the locales to update or complete
     * @param referenceLocale
     *            the locale to be used as reference
     * @param exportFile
     *            the jar file to export to
     * @throws I18nException
     *             if the locale could not be exported for update
     */
    void exportLocalesForUpdate(Locale[] locales, Locale referenceLocale,
	    File exportFile) throws I18nException;

    /**
     * Exports the keys of text to translate to a new locale to a jar file. Into
     * the file is also included the translations of another existing locale to
     * be used as reference for translating to the new locale.
     * 
     * @param locale
     *            the new locale to translate to
     * @param referenceLocale
     *            the locale to be used as reference
     * @param exportFile
     *            the jar file to export to
     * @throws I18nException
     *             if the locale could not be exported for translation
     */
    void exportLocaleForTranslation(Locale locale, Locale referenceLocale,
	    File exportFile) throws I18nException;

    /**
     * Exports the keys of text to translate to a new locale to a jar file. Into
     * the file is also included the translations of another existing locales to
     * be used as reference for translating to the new locale.
     * 
     * @param locale
     *            the new locale to translate to
     * @param referenceLocales
     *            the locales to be used as reference
     * @param exportFile
     *            the jar file to export to
     * @throws I18nException
     *             if the locale could not be exported for translation
     */
    void exportLocaleForTranslation(Locale locale, Locale[] referenceLocales,
	    File exportFile) throws I18nException;

    /**
     * Returns the list of default locales to use as reference when exporting to
     * translate a new locale, or update or complete an existing one.
     * 
     * @return the list of default locales to export
     */
    Locale[] getReferenceLocales();

    /**
     * Sets the list of default locales to export.
     * 
     * @param referenceLocales
     *            the list of default locales to export
     */
    void setReferenceLocales(Locale[] referenceLocales);

    /**
     * Sets the list of default locales bundled with gvSIG. That list of locales
     * will be used as the list of installed ones when gvSIG is run for the
     * first time with the I18n extension. The following times, that list will
     * be taken form the extension configuration in the plugins persistence.
     * 
     * @param defaultLocales
     */
    void setDefaultLocales(Locale[] defaultLocales);
}